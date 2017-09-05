package com.infodms.dms.actions.customerRelationships.clientManage;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.common.CommonUtilDao;
import com.infodms.dms.dao.customerRelationships.RegionManageDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmOrgRegionRelationPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 
 * @ClassName     : RegionManage
 * @Description   : 大区管理
 * @author        : wangming
 * CreateDate     : 2013-4-10
 */
public class RegionManage {
	private static Logger logger = Logger.getLogger(RegionManage.class);
	// 大区管理初始化页面
	private final String regionManageUrl = "/jsp/customerRelationships/clientManage/regionManage.jsp";
	//大区管理新增页面
	private final String regionManageAddOrUpdateUrl = "/jsp/customerRelationships/clientManage/regionManageAddOrUpdate.jsp";

	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	RequestWrapper request = act.getRequest();
	
	
	/**
	 * 大区管理初始化
	 */
	public void regionManageInit(){		
		try{
			act.setForword(regionManageUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"大区管理初始化");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	public void queryRegionManage(){
		act.getResponse().setContentType("application/json");
		try{
			
			String orgName = CommonUtils.checkNull(request.getParamValue("orgName"));  				
			
			RegionManageDao dao = RegionManageDao.getInstance();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
				
			PageResult<Map<String,Object>> regionManageData = dao.queryRegionManage(orgName,Constant.PAGE_SIZE,curPage);
			
			act.setOutData("ps", regionManageData);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"大区管理查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 大区管理增加或修改
	 * @Description: 大区管理增加或修改 
	 * LastDate    : 2013-4-10
	 */
	public void addOrUpdateRegionManage(){
		ActionContext act = ActionContext.getContext();
		
		String id = request.getParamValue("id");
		RegionManageDao dao = RegionManageDao.getInstance();
		//查询所有省份集合
		List<Map<String, Object>> tmRegionMapList = new CommonUtilDao().getProvice();
		if(id!=null&&!"".equals(id)){
			List<TmOrgRegionRelationPO>  list = dao.queryTmOrgRegionRelation(Long.parseLong(id));
			//为被已选中的省份标识
			for(TmOrgRegionRelationPO tm :list){
				for(Map<String, Object> map : tmRegionMapList){
					if(tm.getRegionId()==((BigDecimal)map.get("REGION_ID")).longValue()){
						map.put("isCheck", true);
					}
				}
			}
			act.setOutData("isUpdate", true);
		}
		act.setOutData("orgId", id);
		CommonUtilDao commonUtilDao = new CommonUtilDao();
		//查询所有大区集合
		act.setOutData("orgList", commonUtilDao.queryTmOrgPO());
		act.setOutData("provinceList", tmRegionMapList);
		act.setForword(regionManageAddOrUpdateUrl);
	}
	/**
	 * 
	 * @Title      : 新增修改大区管理提交
	 * @Description: TODO 新增修改大区管理提交
	 * LastDate    : 2013-4-1
	 */
	public void addOrUpdateRegionManageSubmit(){
		
		String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));  				//大区
		String provinceIds = CommonUtils.checkNull(request.getParamValue("regionIds"));  		//省份
		
		try{
			RegionManageDao dao = RegionManageDao.getInstance();
			//先删除大区关系
			dao.deleteTmOrgRegionRelationPO(orgId);
			//添加大区关系
			for(String provinceIdStr :provinceIds.split(",")){
				TmOrgRegionRelationPO tmOrgRegionRelationPO = new TmOrgRegionRelationPO();
				tmOrgRegionRelationPO.setRelId(new Long(SequenceManager.getSequence("")));
				tmOrgRegionRelationPO.setCreateBy(logonUser.getUserId());
				tmOrgRegionRelationPO.setCreateDate(new Date());
				tmOrgRegionRelationPO.setOrgId(Long.parseLong(orgId));
				tmOrgRegionRelationPO.setRegionId(Long.parseLong(provinceIdStr));
				tmOrgRegionRelationPO.setStatus(Constant.STATUS_ENABLE);
				dao.insert(tmOrgRegionRelationPO);
			}
			act.setOutData("success", "true");
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"大区管理新增修改");
			logger.error(logger,e1);
			act.setException(e1);
		}

	}
	
	public void delRegionManageSubmit(){
		try{
			String ids = request.getParamValue("ids");
			ids = ids.replaceAll(",", "','");
			RegionManageDao dao = RegionManageDao.getInstance();
			dao.deleteTmOrgRegionRelationPO(ids);
			act.setOutData("success", "true");
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DELETE_FAILURE_CODE,"大区管理");
			logger.error(logger,e1);
			act.setException(e1);
		}	
	}

}