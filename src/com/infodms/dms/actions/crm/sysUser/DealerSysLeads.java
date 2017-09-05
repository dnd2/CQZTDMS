package com.infodms.dms.actions.crm.sysUser;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sysmng.usemng.SgmDealerSysUser;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TPcGroupPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.DynaBean;


public class DealerSysLeads {
	
	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private POFactory factory = POFactoryBuilder.getInstance();
	private final String querySgmDealerSysLeadsInitUrl = "/jsp/crm/sysUser/dealerSysLeadsInit.jsp";
	private final String addSgmDealerSysUserInitUrl = "/jsp/crm/sysUser/dealerSysUserAdd.jsp";
	private final String modfiSgmDealerSysUserInitUrl = "/jsp/crm/sysUser/dealerSysUserModify.jsp";
	private final String queryPoseInitUrl = "/jsp/crm/sysUser/sgmDealerPoseSearch.jsp";
	private final String toPoseListURL = "/jsp/crm/sysUser/userList.jsp";
	private DealerSysLeadsDao dao = DealerSysLeadsDao.getInstance();

	/**
	 * 维护经销商用户查询初始化
	 * 
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void doInit() {
		
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
		System.out.println("执行了该方法33333333333333333!");
		String CompanyId=logonUser.getCompanyId().toString();
			//获取线索来源类型
			List<DynaBean> leadsTypeList = dao.getLeadsOriginType();
			List<DynaBean> adviserList = dao.getSelectAdviserInfo(CompanyId,"60151017");
			act.setOutData("leadsTypeList", leadsTypeList);
			act.setOutData("adviserList", adviserList);
			act.setOutData("OriginType", "60151017");
			act.setForword(querySgmDealerSysLeadsInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "SGM维护经销商用户");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	

	
	public void doCarInit() {
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String originType = CommonUtils.checkNull(request.getParamValue("originType"));
		try {
		System.out.println("执行了该方法doEasyCarInit!");
		String CompanyId=logonUser.getCompanyId().toString();
			//获取线索来源类型
			List<DynaBean> leadsTypeList = dao.getLeadsOriginType();
			List<DynaBean> adviserList = dao.getSelectAdviserInfo(CompanyId,originType);
			act.setOutData("leadsTypeList", leadsTypeList);
			act.setOutData("adviserList", adviserList);
			act.setOutData("OriginType", originType);
			act.setForword(querySgmDealerSysLeadsInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "SGM维护经销商用户");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	
	public void doUserOriginSave() {
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String originType = CommonUtils.checkNull(request.getParamValue("originType"));
		String userGroup = CommonUtils.checkNull(request.getParamValue("userId"));
		String dealerId=logonUser.getDealerId();
		Map<String, String> map=new HashMap<String, String>();
		map.put("dealerId", dealerId);
		map.put("originType", originType);
		dao.deleteUserOriginRelation(map);//先删除旧的关联关系
		if(userGroup.length()>1){
		String[] userIdGroup = userGroup.split(",");
		
		for (int i = 0; i < userIdGroup.length; i++) {
			String userId = userIdGroup[i];//用户ID
			map.put("userId", userId);
			dao.InsertUserOriginRelation(map);//建立新的关联关系
			}
		}
		
		String CompanyId=logonUser.getCompanyId().toString();
		//获取线索来源类型
		List<DynaBean> leadsTypeList = dao.getLeadsOriginType();
		List<DynaBean> adviserList = dao.getSelectAdviserInfo(CompanyId,originType);
		act.setOutData("leadsTypeList", leadsTypeList);
		act.setOutData("adviserList", adviserList);
		act.setOutData("OriginType", originType);
		act.setForword(querySgmDealerSysLeadsInitUrl);


		System.out.println("originType:"+originType);
		//System.out.println("userId:"+userId);
	
	}



}
