package com.infodms.dms.actions.sales.fleetmanage.fleetSupport;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.CompanyBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.sales.fleetmanage.fleetsupport.FleetSupportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtDealerActualSalesLogPO;
import com.infodms.dms.po.TtDealerActualSalesPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * @Title: 集团客户实销信息审核Action
 * @Description:InfoFrame3.0.V01
 * @Copyright: Copyright (c) 2010
 * @Company: www.infoservice.com.cn
 * @Date: 2010-6-28
 * @author 
 * @mail  
 * @version 1.0
 * @remark 
 */
public class FleetSalesInfoCheck {
	
	public Logger logger = Logger.getLogger(FleetSalesInfoCheck.class);   
	FleetSupportDao dao  = FleetSupportDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final String initUrl = "/jsp/sales/fleetmanage/fleetSupport/fleetSalesInfoCheck.jsp";
	private final String detailUrl = "/jsp/sales/fleetmanage/fleetSupport/fleetSalesInfoCheckDetail.jsp";
	private final String dlrCompayUrl = "/jsp/sales/fleetmanage/fleetSupport/queryDealerCompany.jsp";
	
	/**
	 * 集团客户实销信息审核页面初始化
	 */
	public void fleetSalesInfoCheckInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			Date date_ = new Date(System.currentTimeMillis());
			Date date1 = new Date(date_.getYear(),date_.getMonth(),01);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String date = dateFormat.format(date_);
			String date2 = dateFormat.format(date1);
			act.setOutData("date", date);
			act.setOutData("date2", date2);
			act.setForword(initUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户实销信息审核页面初始化");
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
	 * 集团客户实销信息审核查询
	 */
	public void fleetSalesInfoCheckQuery(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String fleetName = request.getParamValue("fleetName");	//客户名称
			String fleetType = request.getParamValue("fleetType");	//客户类型
			String startDate = request.getParamValue("startDate");	//起始时间
			String endDate = request.getParamValue("endDate");		//结束时间
			String logsDate = CommonUtils.checkNull(request.getParamValue("logsDate"));//审核开始时间
			String logeDate = CommonUtils.checkNull(request.getParamValue("logeDate"));//审核结束时间
			String companyId = request.getParamValue("companyId");	//经销商公司ID
			String groupCode = request.getParamValue("groupCode");	//物料代码
			String contractNo = request.getParamValue("contractNo");//合同编号
			String vin = request.getParamValue("vin");				//底盘号
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.fleetSalesInfoCheckQuery(fleetName, fleetType, startDate, endDate, companyId, groupCode, contractNo, vin, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户实销信息审核查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 集团客户实销信息审核明细查询
	 */
	public void fleetSalesInfoCheckDetailQuery(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String fleetId = request.getParamValue("fleetId");		//根据fleetTYpe -1 TM_COMPANY_PACT, 其他TM_FLEET
			String orderId = request.getParamValue("orderId");		//实销ID
			String fleetType = request.getParamValue("fleetType");
			Map<String, Object> map = null;
			Map<String, Object> map3 = null;
			if(fleetType.equals("-1")){
				map = dao.getCompanyPactInfoById(fleetId);
			}else{
			    map = dao.getFleetInfobyId(fleetId);
			    map3 = dao.getFleetConById(fleetId);
			}
			Map<String, Object> map2 = dao.getVehicleInfobyId(orderId);
			act.setOutData("begin",Constant.CUSTOMER_TYPE_01);
			act.setOutData("end", Constant.CUSTOMER_TYPE_02);
			act.setOutData("vhclInfoMap", map2);
			act.setOutData("orderId", orderId);
			act.setOutData("fleetMap", map);
			act.setOutData("map3", map3);
			act.setOutData("fleetType", fleetType);
			act.setForword(detailUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户实销信息审核明细查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 集团客户实销信息审核提交
	 */
	public void fleetSalesInfoCheckConfirm(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String orderId = request.getParamValue("orderId");				//客户Id
			String remark = request.getParamValue("remark");			    //页面参数
			String flag = request.getParamValue("mysubmit");			    //页面参数
			TtDealerActualSalesPO tdaspContion = new TtDealerActualSalesPO();
			TtDealerActualSalesPO tdaspValue = new TtDealerActualSalesPO();
			Date logDate = new Date(System.currentTimeMillis());
			tdaspContion.setOrderId(Long.parseLong(orderId));
			if("0".equals(flag)){
				tdaspValue.setFleetStatus(Constant.Fleet_SALES_CHECK_STATUS_02);
			}else{
				tdaspValue.setFleetStatus(Constant.Fleet_SALES_CHECK_STATUS_03);
			}
			tdaspValue.setUpdateDate(logDate);
			tdaspValue.setUpdateBy(logonUser.getUserId());
			dao.update(tdaspContion, tdaspValue);
			
			TtDealerActualSalesLogPO tdaslp = new TtDealerActualSalesLogPO();
			tdaslp.setLogId(Long.parseLong(SequenceManager.getSequence("")));
			tdaslp.setOrderId(Long.parseLong(orderId));
			tdaslp.setOrgId(logonUser.getOrgId());
			tdaslp.setUserId(logonUser.getUserId());
			if("0".equals(flag)){
				tdaslp.setLogStatus(Constant.SALES_CHECK_STATUS_01);
			}else{
				tdaslp.setLogStatus(Constant.SALES_CHECK_STATUS_02);
			}
			tdaslp.setLogDate(logDate);
			tdaslp.setRemark(remark);
			tdaslp.setCreateBy(logonUser.getUserId());
			tdaslp.setCreateDate(logDate);
			dao.insert(tdaslp);
			act.setOutData("returnValue", 1);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.PUTIN_FAILURE_CODE,"集团客户实销信息审核提交");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
