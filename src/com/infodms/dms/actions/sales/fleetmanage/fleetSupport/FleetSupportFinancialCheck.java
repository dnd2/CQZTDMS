package com.infodms.dms.actions.sales.fleetmanage.fleetSupport;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.CompanyBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.sales.fleetmanage.fleetsupport.FleetSupportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtFleetIntentAuditPO;
import com.infodms.dms.po.TtFleetIntentPO;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * @Title: 集团客户支持财务复核Action
 * @Description:InfoFrame3.0.V01
 * @Copyright: Copyright (c) 2010
 * @Company: www.infoservice.com.cn
 * @Date: 2010-6-25
 * @author 
 * @mail  
 * @version 1.0
 * @remark 
 */
public class FleetSupportFinancialCheck {
	public Logger logger = Logger.getLogger(FleetSupportFinancialCheck.class);   
	FleetSupportDao dao  = FleetSupportDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final String initUrl = "/jsp/sales/fleetmanage/fleetSupport/fleetSupportApplyFinancialCheck.jsp";
	private final String detailUrl = "/jsp/sales/fleetmanage/fleetSupport/fleetSupportApplyFinancialCheckDetail.jsp";
	private final String dlrCompayUrl = "/jsp/sales/fleetmanage/fleetSupport/queryDealerCompany.jsp";
	/**
	 * 集团客户支持财务复核页面初始化
	 */
	public void fleetSupportFinancialCheckInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(initUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户支持财务复核页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 经销商公司查询初始化
	 */
	public void queryCompany() throws Exception{
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(dlrCompayUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商公司查询初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 经销商公司查询
	 */
	public void queryCom(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String companyCode = request.getParamValue("companyCode");
			String companyName = request.getParamValue("companyName");
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<CompanyBean> ps = dao.selectCompany(companyCode, companyName, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商公司查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 集团客户支持财务复核查询
	 */
	public void fleetSupportFinancialCheckQuery(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String fleetName = request.getParamValue("fleetName");	//客户名称
			String fleetType = request.getParamValue("fleetType");	//客户类型
			String startDate = request.getParamValue("startDate");	//起始时间
			String endDate = request.getParamValue("endDate");		//结束时间
			String companyId = request.getParamValue("companyId");	//经销商公司ID
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.fleetSupportApplyFinancialCheckQuery(fleetName, fleetType, startDate, endDate, companyId, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户支持财务复核查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 集团客户支持财务复核明细查询
	 */
	public void fleetSupportFinancialCheckDetail(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String fleetId = request.getParamValue("fleetId");	//客户Id
			String intentId = request.getParamValue("intentId");	//意向Id
			Map<String, Object> map = dao.getFleetInfobyId(fleetId);
			Map<String, Object> map2 = dao.getFleetIntentInfobyId(fleetId);
			List<Map<String, Object>> list1 = dao.getFleetIntentDetailInfobyId(fleetId, intentId,"");
			List<Map<String, Object>> list2 = dao.getFleetIntentCheckInfobyId(fleetId, intentId);
			act.setOutData("intentList", list1);
			act.setOutData("checkList", list2);
			act.setOutData("intentMap", map2);
			act.setOutData("intentId", intentId);
			act.setOutData("fleetMap", map);
			act.setForword(detailUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户支持财务复核明细查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 集团客户支持财务复核提交
	 */
	public void fleetSupportFinancialCheckConfirm(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String fleetId = request.getParamValue("fleetId");		//客户Id
			String intentId = request.getParamValue("intentId");	//意向Id
			String flag = request.getParamValue("flag");			//页面参数
			String remark = request.getParamValue("remark");		//审核意见
			TtFleetIntentPO tfipContion = new TtFleetIntentPO();
			TtFleetIntentPO tfipValue = new TtFleetIntentPO();
			tfipContion.setIntentId(Long.parseLong(intentId));
			if("1".equals(flag)){
				tfipValue.setStatus(Constant.FLEET_SUPPORT_STATUS_05);
			}else{
				tfipValue.setStatus(Constant.FLEET_SUPPORT_STATUS_04);
			}
			tfipValue.setUpdateDate(new Date(System.currentTimeMillis()));
			tfipValue.setUpdateBy(logonUser.getUserId());
			dao.update(tfipContion, tfipValue);
			TtFleetIntentAuditPO tficp = new TtFleetIntentAuditPO();
			tficp.setAuditId(Long.parseLong(SequenceManager.getSequence("")));
			tficp.setAuditRemark(remark);
			if("1".equals(flag)){
				tficp.setAuditResult(Constant.FLEET_SUPPORT_CHECK_STATUS_02);
			}else{
				tficp.setAuditResult(Constant.FLEET_SUPPORT_CHECK_STATUS_01);
			}
			tficp.setAuditDate(new Date(System.currentTimeMillis()));
			tficp.setIntentId(Long.parseLong(intentId));
			tficp.setOrgId(logonUser.getOrgId());
			tficp.setFleetId(Long.parseLong(fleetId));
			tficp.setAuditUserId(logonUser.getUserId());
			tficp.setCreateDate(new Date(System.currentTimeMillis()));
			tficp.setCreateBy(logonUser.getUserId());
			dao.insert(tficp);
			act.setOutData("returnValue", 1);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.PUTIN_FAILURE_CODE,"集团客户支持财务复核提交");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}	
