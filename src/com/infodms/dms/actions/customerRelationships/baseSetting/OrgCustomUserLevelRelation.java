package com.infodms.dms.actions.customerRelationships.baseSetting;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.infodms.dms.actions.util.CommonUtilActions;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.customerRelationships.OrgCustomSearchDao;
import com.infodms.dms.dao.customerRelationships.OrgCustomUserLevelRelationDao;
import com.infodms.dms.dao.customerRelationships.OrgCustomUserRelationDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmOrgCusUserLevelRelationPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * 
 * @ClassName     : OrgCustomUserLevelRelation 
 * @Description   : 延期授权人员维护
 * @author        : wangming
 * CreateDate     : 2013-7-22
 */
public class OrgCustomUserLevelRelation {
	private static Logger logger = Logger.getLogger(OrgCustomUserLevelRelation.class);
	//延期授权人员维护初始化页面
	private final String orgCustomUserLevelRelationURL = "/jsp/customerRelationships/baseSetting/orgCustomUserLevelRelation.jsp";
	//延期授权人员维护修改页面
	private final String orgCustomUserLevelRelationUpdate = "/jsp/customerRelationships/baseSetting/orgCustomUserLevelRelationUpdate.jsp";

	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	RequestWrapper request = act.getRequest();
	
	
	/**
	 * 延期授权人员维护初始化
	 */
	public void orgCustomUserLevelRelationInit(){		
		try{
			act.setForword(orgCustomUserLevelRelationURL);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"客户关系机构人员职位关系维护初始化");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	public void queryOrgCustomUserLevelRelation(){
		act.getResponse().setContentType("application/json");
		try{
			
			String name = CommonUtils.checkNull(request.getParamValue("name"));   //用户名				
			String orgid = CommonUtils.checkNull(request.getParamValue("DEPT_ID")); //组织ID 				
 						
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
				
			OrgCustomUserLevelRelationDao dao = OrgCustomUserLevelRelationDao.getInstance();
			PageResult<Map<String,Object>> ps = dao.queryOrgCustomUserLevelRelation(name,orgid,Constant.PAGE_SIZE,curPage);
			
			act.setOutData("ps", ps);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"延期授权人员维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 延期授权人员修改
	 * @Description: 延期授权人员修改 
	 * LastDate    : 2013-7-21
	 */
	public void orgCustomUserLevelRelationUpdate(){

		String id = CommonUtils.checkNull(request.getParamValue("userid"));
				
		OrgCustomSearchDao orgCustomSearchDao = OrgCustomSearchDao.getInstance();
		
		String level = "1,2,3";		
		//查询所有部门集合
		List<Map<String,Object>> allList = orgCustomSearchDao.getOrgCustomSelete(level);
		if(id!=null&&!"".equals(id)){
			OrgCustomUserLevelRelationDao orgCustomUserLevelRelationDao = OrgCustomUserLevelRelationDao.getInstance();
			//所属部门
			OrgCustomUserRelationDao orgCustomUserRelationDao = OrgCustomUserRelationDao.getInstance();
			List<Map<String,Object>>  orglist = orgCustomUserRelationDao.getOrgCustomByOrgId(Long.parseLong(id));
			
			//授权级别
			List<Map<String,Object>>  list = orgCustomUserLevelRelationDao.getOrgCustomUserLevelRelationByUserId(Long.parseLong(id));
			
			//为被已选中的部门标识
			for(Map<String,Object> map :orglist){
				for(Map<String, Object> mapAll : allList){
					if(((BigDecimal)map.get("ORG_ID")).longValue()==((BigDecimal)mapAll.get("ORG_ID")).longValue()){
						mapAll.put("isCheck", true);
					}
				}
			}
			
			//授权级别
			for(Map<String,Object> map :list){
				for(Map<String, Object> mapAll : allList){
					if(((BigDecimal)map.get("ORG_ID")).longValue()==((BigDecimal)mapAll.get("ORG_ID")).longValue()){
						putLevel(map, mapAll);
					}
				}
			}			
		}
		
		act.setOutData("userid", id);
		act.setOutData("user", orgCustomSearchDao.getUser(Long.parseLong(id)));

		act.setOutData("allList", allList);
		act.setForword(orgCustomUserLevelRelationUpdate);
		
	}
	
	/**
	 * 设置集合下的职位级别
	 * @param map 已被选中的集合
	 * @param mapAll 所有集合
	 */
	private void putLevel(Map<String,Object> map,Map<String, Object> mapAll){
		CommonUtilActions commonUtilActions = new CommonUtilActions();
		List<Map<String, Object>> list = commonUtilActions.getTcCode(Constant.Level_Manager.toString());
		for(Map<String, Object> levelMap : list){
			String codeId= levelMap.get("CODE_ID").toString();
			if(codeId.equals(map.get("ORGLEVEL").toString()) && codeId.equals(Constant.Level_Manager_01.toString())){
				mapAll.put("isLevelOne", true);
			}else if(codeId.equals(map.get("ORGLEVEL").toString()) && codeId.equals(Constant.Level_Manager_02.toString())){
				mapAll.put("isLevelTwo", true);
			}else if(codeId.equals(map.get("ORGLEVEL").toString()) && codeId.equals(Constant.Level_Manager_03.toString())){
				mapAll.put("isLevelThree", true);
			}
		}
	}
	/**
	 * 
	 * @Title      : 延期授权人员修改提交
	 * @Description: 延期授权人员修改提交
	 * LastDate    : 2013-7-21
	 */
	public void orgCustomUserLevelRelationUpdateSubmit(){
		
		String userid = CommonUtils.checkNull(request.getParamValue("userid"));  				//用户
		String orgids = CommonUtils.checkNull(request.getParamValue("orgids"));  				//职位
		String orglevels = CommonUtils.checkNull(request.getParamValue("orglevels"));  			//延期授权
		
		try{
			OrgCustomUserLevelRelationDao orgCustomUserLevelRelationDao = OrgCustomUserLevelRelationDao.getInstance();
			//先删除用户关系
			orgCustomUserLevelRelationDao.deleteOrgCustomUserLevelRelation(userid);
			//添加组织关系
			for(String orgidStr :orgids.split(",")){
				for(String level :orglevels.split(",")){
					String orgid = (String)level.split("-")[0];
					String orgLevel = (String)level.split("-")[1];
					if(orgidStr.equals(orgid)){
						TmOrgCusUserLevelRelationPO tmOrgCusUserLevelRelationPO = new TmOrgCusUserLevelRelationPO();
						tmOrgCusUserLevelRelationPO.setOculrId(new Long(SequenceManager.getSequence("")));
						tmOrgCusUserLevelRelationPO.setOculrLevel(Integer.parseInt(orgLevel));
						tmOrgCusUserLevelRelationPO.setOrgId(Long.parseLong(orgid));
						tmOrgCusUserLevelRelationPO.setUserId(Long.parseLong(userid));
						
						tmOrgCusUserLevelRelationPO.setCreateBy(logonUser.getUserId());
						tmOrgCusUserLevelRelationPO.setCreateDate(new Date());
						
						orgCustomUserLevelRelationDao.insert(tmOrgCusUserLevelRelationPO);
					}
				}				
				
			}
			act.setOutData("success", "true");
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"延期授权人员修改提交");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 延期授权人员部门注销
	 * @Description: 延期授权人员部门注销
	 * LastDate    : 2013-7-22
	 */
	public void orgCustomUserLevelRelationDelete(){

		String id = CommonUtils.checkNull(request.getParamValue("userid"));
				
		OrgCustomUserLevelRelationDao orgCustomUserLevelRelationDao = OrgCustomUserLevelRelationDao.getInstance();
		//先删除用户关系
		orgCustomUserLevelRelationDao.deleteOrgCustomUserLevelRelation(id);

		act.setOutData("success", "true");
	}

}