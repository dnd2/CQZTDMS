package com.infodms.dms.actions.sales.fleetmanage.fleetInfoManage;

import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.sales.fleetmanage.fleetinfomanage.FleetFactDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmCompanyPactPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * @Title: SupplierInfoSearch.java
 *
 * @Description:CHANADMS
 *
 * @Copyright: Copyright (c) 2010
 *
 * @Company: www.infoservice.com.cn
 * @Date: 2010-10-13
 *
 * @author lishuai 
 * @mail   lishuai103@yahoo.cn	
 * @version 1.0
 * @remark 
 */
public class FleetFact {
	public Logger logger = Logger.getLogger(FleetFact.class);   
	FleetFactDao dao = new FleetFactDao();
	private final String initUrl = "/jsp/sales/fleetmanage/fleetinfomanage/queryfleetfactList.jsp";
	private final String addUrl = "/jsp/sales/fleetmanage/fleetinfomanage/addfleetfact.jsp";
	private final String infoUrl = "/jsp/sales/fleetmanage/fleetinfomanage/updatefleetfact.jsp";

	/*
	 * 批售项目查询
	 */
	public void queryFleetFact(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(initUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"批售项目");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void queryFleetFactList(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String factNo = CommonUtils.checkNull(request.getParamValue("factNo"));
			String factName = CommonUtils.checkNull(request.getParamValue("factName"));
			String companyId = String.valueOf(logonUser.getCompanyId());
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = dao.queryFleetFactList(factNo,factName,companyId, curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"批售项目");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void addFleetFact(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(addUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"批售项目");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void addFact(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String pactNo = request.getParamValue("pactNo");
			String pactName = request.getParamValue("pactName");
			String status = request.getParamValue("status");
			String pactType = request.getParamValue("pactType");
			String isAllowApply = request.getParamValue("isAllowApply");
			String remark = CommonUtils.checkNull(request.getParamValue("remark"));
			Long companyId = logonUser.getCompanyId();
			
			TmCompanyPactPO po = new TmCompanyPactPO();
			po.setPactId(Long.parseLong(SequenceManager.getSequence("")));
			po.setPactName(pactName);
			po.setPactNo(pactNo);
			po.setStatus(Integer.parseInt(status));
			po.setRemart(remark);
			po.setOemCompanyId(companyId);
			po.setParentFleetType(pactType) ;
			po.setIsAllowApply(Long.parseLong(isAllowApply)) ;
			po.setCreateBy(logonUser.getUserId());
			po.setCreateDate(new Date());
			dao.insert(po);
			
			act.setOutData("returnValue", 1);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"批售项目");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void editFleetFact(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String id = request.getParamValue("id");
			Map<String, Object> map = dao.getPactInfo(id);
			act.setOutData("map", map);
			act.setForword(infoUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"特殊外出费用详细");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void updateFact(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String id = request.getParamValue("id");
			String pactNo = request.getParamValue("pactNo");
			String pactName = request.getParamValue("pactName");
			String status = request.getParamValue("status");
			String pactType = request.getParamValue("pactType");
			String isAllowApply = request.getParamValue("isAllowApply");
			String remark = CommonUtils.checkNull(request.getParamValue("remark"));
			
			TmCompanyPactPO po = new TmCompanyPactPO();
			TmCompanyPactPO tp = new TmCompanyPactPO();
			tp.setPactId(Long.parseLong(id));
			po.setPactName(pactName);
			po.setPactNo(pactNo);
			po.setStatus(Integer.parseInt(status));
			po.setIsAllowApply(Long.parseLong(isAllowApply)) ;
			po.setParentFleetType(pactType) ;
			po.setRemart(remark);
			po.setUpdateBy(logonUser.getUserId());
			po.setUpdateDate(new Date());
			dao.update(tp, po);
			
			act.setOutData("returnValue", 1);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"批售项目");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
