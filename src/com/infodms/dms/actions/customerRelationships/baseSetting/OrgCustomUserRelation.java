package com.infodms.dms.actions.customerRelationships.baseSetting;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.customerRelationships.OrgCustomSearchDao;
import com.infodms.dms.dao.customerRelationships.OrgCustomUserRelationDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmOrgCusUserRelationPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.XHBUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * 
 * @ClassName     : OrgCustomUserRelation 
 * @Description   : 部门人员维护
 * @author        : wangming
 * CreateDate     : 2013-7-20
 */
public class OrgCustomUserRelation {
	private static Logger logger = Logger.getLogger(OrgCustomUserRelation.class);
	//部门人员维护初始化页面
	private final String orgCustomUserRelationURL = "/jsp/customerRelationships/baseSetting/orgCustomUserRelation.jsp";
	//部门人员维护修改页面
	private final String orgCustomUserRelationUpdate = "/jsp/customerRelationships/baseSetting/orgCustomUserRelationUpdate.jsp";

	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	RequestWrapper request = act.getRequest();
	
	
	/**
	 * 部门人员维护初始化
	 */
	public void orgCustomUserRelationInit(){		
		try{
			act.setForword(orgCustomUserRelationURL);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"部门人员维护初始化");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	public void queryOrgCustomUserRelation(){
		act.getResponse().setContentType("application/json");
		try{
			
			String name = CommonUtils.checkNull(request.getParamValue("name"));   //用户名				
			String orgid = CommonUtils.checkNull(request.getParamValue("DEPT_ID")); //组织ID 				
 						
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
				
			OrgCustomUserRelationDao dao = OrgCustomUserRelationDao.getInstance();
			PageResult<Map<String,Object>> ps = dao.queryOrgCustomUserRelation(name,orgid,Constant.PAGE_SIZE,curPage);
			
			act.setOutData("ps", ps);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"部门人员维护查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 部门人员维护修改
	 * @Description: 部门人员维护修改 
	 * LastDate    : 2013-7-21
	 */
	public void orgCustomUserRelationUpdate(){

		String id = CommonUtils.checkNull(request.getParamValue("userid"));
				
		OrgCustomSearchDao orgCustomSearchDao = OrgCustomSearchDao.getInstance();
		String orgCode = request.getParamValue("orgCode");
		String orgName = request.getParamValue("orgName");
		String level = "1,2,3";		
		//查询所有部门集合
		List<Map<String,Object>> allList = orgCustomSearchDao.getOrgCustomSeleteOrgInfo(level,orgCode,orgName);
		if(id!=null&&!"".equals(id)){
			OrgCustomUserRelationDao orgCustomUserRelationDao = OrgCustomUserRelationDao.getInstance();
			List<Map<String,Object>>  list = orgCustomUserRelationDao.getOrgCustomByOrgId(Long.parseLong(id));
			//为被已选中的部门标识
			for(Map<String,Object> map :list){
				for(Map<String, Object> mapAll : allList){
					if(((BigDecimal)map.get("ORG_ID")).longValue()==((BigDecimal)mapAll.get("ORG_ID")).longValue()){
						mapAll.put("isCheck", true);
					}
				}
			}			
		}
		act.setOutData("userid", id);
		act.setOutData("user", orgCustomSearchDao.getUser(Long.parseLong(id)));
		if(!XHBUtil.IsNull(orgCode))act.setOutData("orgCode", orgCode);
		if(!XHBUtil.IsNull(orgName))act.setOutData("orgName", orgName);

		act.setOutData("allList", allList);
		act.setForword(orgCustomUserRelationUpdate);
		
	}
	/**
	 * 
	 * @Title      : 部门人员维护修改提交
	 * @Description: 部门人员维护修改提交
	 * LastDate    : 2013-7-21
	 */
	public void orgCustomUserRelationUpdateSubmit(){
		
		String userid = CommonUtils.checkNull(request.getParamValue("userid"));  				//用户
		String orgids = CommonUtils.checkNull(request.getParamValue("orgids"));  		//职位
		
		try{
			OrgCustomUserRelationDao orgCustomUserRelationDao = OrgCustomUserRelationDao.getInstance();
			//先删除用户关系
			orgCustomUserRelationDao.deleteOrgCustomUserRelation(userid);
			
			for(String orgidStr :orgids.split(",")){
				TmOrgCusUserRelationPO tmOrgCusUserRelationPO = new TmOrgCusUserRelationPO();
				tmOrgCusUserRelationPO.setOcurId(new Long(SequenceManager.getSequence("")));
				tmOrgCusUserRelationPO.setOrgId(Long.parseLong(orgidStr));
				tmOrgCusUserRelationPO.setUserId(Long.parseLong(userid));
				tmOrgCusUserRelationPO.setCreateBy(logonUser.getUserId());
				tmOrgCusUserRelationPO.setCreateDate(new Date());
				
				orgCustomUserRelationDao.insert(tmOrgCusUserRelationPO);
			}
			act.setOutData("success", "true");
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"部门人员维护修改提交");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}

}