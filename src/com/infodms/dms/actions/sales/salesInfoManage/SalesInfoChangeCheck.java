package com.infodms.dms.actions.sales.salesInfoManage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.common.FileUpLoadDAO;
import com.infodms.dms.dao.orgmng.GetOrgIdsOrDealerIdsDAO;
import com.infodms.dms.dao.sales.customerInfoManage.SalesReportDAO;
import com.infodms.dms.dao.sales.salesInfoManage.SalesInfoChangeCheckDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TmCompanyPactPO;
import com.infodms.dms.po.TmFleetPO;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TtCustomerEditLogPO;
import com.infodms.dms.po.TtCustomerPO;
import com.infodms.dms.po.TtDealerActualSalesAuditPO;
import com.infodms.dms.po.TtDealerActualSalesPO;
import com.infodms.dms.po.TtFleetContractPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.ActionContextExtend;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * @Title : SalesInfoChangeCheck.java
 * @Package: com.infodms.dms.actions.sales.salesInfoManage
 * @Description: 实销信息更改审核
 * @date : 2010-6-30
 * @version: V1.0
 */
public class SalesInfoChangeCheck extends BaseDao {
	public Logger logger = Logger.getLogger(SalesInfoChangeCheck.class);
	private ActionContext act = ActionContext.getContext();
	private static final SalesInfoChangeCheck dao = new SalesInfoChangeCheck();

	public static final SalesInfoChangeCheck getInstance() {
		return dao;
	}

	RequestWrapper request = act.getRequest();
	private final String salesInfoChangeCheckInit = "/jsp/sales/salesInfoManage/salesInfoChangeCheckInit.jsp";
	private final String toCheckSalesInfoChange = "/jsp/sales/salesInfoManage/toCheckSalesInfoChange.jsp";
	private final String salseInfoChangeDetail = "/jsp/sales/salesInfoManage/salseInfoChangeDetail.jsp";
	private final String salesInfoChangeCheckpInit = "/jsp/sales/salesInfoManage/salesInfoChangeCheckpInit.jsp";
	private final String toCheckSalesInfopChange = "/jsp/sales/salesInfoManage/toCheckSalesInfopChange.jsp";
	private final String salesInfoChangeCheckFleetInit = "/jsp/sales/salesInfoManage/salesInfoChangeCheckFleetInit.jsp";

	/**
	 * @Title : salesInfoChangeCheckInit
	 * @Description: 实销信息更改审核页面初始化
	 * @throws
	 * @LastUpdate :2010-6-30
	 */
	public void salesInfoChangeCheckInit() {
		AclUserBean logonUser = null;
		try {
			ActionContextExtend atx = (ActionContextExtend) ActionContext.getContext();
			String reqURL = atx.getRequest().getContextPath();
			if("/CVS-SALES".equals(reqURL.toUpperCase())){
				act.setOutData("returnValue", 1);
			}else{
				act.setOutData("returnValue", 2);
			}
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("areaList", areaList);
			act.setForword(salesInfoChangeCheckInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息更改审核页面初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * @Title : salesInfoChangeCheckInit
	 * @Description: 实销信息更改审核页面初始化(省系)
	 * @throws
	 * @LastUpdate :2010-6-30
	 */
	public void salesInfoChangeCheckpInit() {
		AclUserBean logonUser = null;
		try {
			ActionContextExtend atx = (ActionContextExtend) ActionContext.getContext();
			String reqURL = atx.getRequest().getContextPath();
			if("/CVS-SALES".equals(reqURL.toUpperCase())){
				act.setOutData("returnValue", 1);
			}else{
				act.setOutData("returnValue", 2);
			}
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("areaList", areaList);
			act.setForword(salesInfoChangeCheckpInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息更改审核页面初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void salesInfoChangeCheckFleetInit() {
		AclUserBean logonUser = null;
		try {
			ActionContextExtend atx = (ActionContextExtend) ActionContext.getContext();
			String reqURL = atx.getRequest().getContextPath();
			if("/CVS-SALES".equals(reqURL.toUpperCase())){
				act.setOutData("returnValue", 1);
			}else{
				act.setOutData("returnValue", 2);
			}
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("areaList", areaList);
			act.setForword(salesInfoChangeCheckFleetInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "集团客户更改审核页面初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * @Title : changeListQuery
	 * @Description: 查询可审核信息
	 * @throws
	 * @LastUpdate :2010-6-30
	 */
	public void changeListQuery() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);
			String customer_type = CommonUtils.checkNull(request.getParamValue("customer_type")); 	// 客户类型
			String customer_name = CommonUtils.checkNull(request.getParamValue("customer_name")); 	// 客户名称
			String vin = CommonUtils.checkNull(request.getParamValue("vin")); 						// VIN
			String customer_phone = CommonUtils.checkNull(request.getParamValue("customer_phone")); // 客户电话
			String fleet_name = CommonUtils.checkNull(request.getParamValue("fleet_name")); 		// 集团客户名称
			String materialCode = CommonUtils.checkNull(request.getParamValue("materialCode")); 	// 选择物料组
			String is_fleet = CommonUtils.checkNull(request.getParamValue("is_fleet")); 			// 是否集团客户
			String contract_no = CommonUtils.checkNull(request.getParamValue("contract_no")); 		// 集团客户合同
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate")); 			// 上报日期:开始
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate")); 				// 上报日期 ：截止
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")); 		// 经销商代码
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));					// 业务范围
			String orgId = logonUser.getOrgId().toString();
			String parentOrgId = logonUser.getParentOrgId();
			String dutyType = logonUser.getDutyType();
			String dealerSql = GetOrgIdsOrDealerIdsDAO.getDealerIdsForOrder(
					orgId, parentOrgId, dutyType, "TMD");
			Map<String, String> map = new HashMap<String, String>();
			map.put("customer_type", customer_type);
			map.put("customer_name", customer_name);
			map.put("vin", vin);
			map.put("customer_phone", customer_phone);
			map.put("fleet_name", fleet_name);
			map.put("materialCode", materialCode);
			map.put("is_fleet", is_fleet);
			map.put("contract_no", contract_no);
			map.put("startDate", startDate);
			map.put("endDate", endDate);
			map.put("dealerCode", dealerCode);
			map.put("areaId", areaId);
			map.put("dealerSql", dealerSql);
			map.put("dutyType", logonUser.getDutyType(logonUser.getDealerId()));
			map.put("orgId", orgId);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = SalesInfoChangeCheckDAO.salesInfoChangeQueryList(map, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "查询可审核信息 ");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void changeListQuery1() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);
			String customer_type = CommonUtils.checkNull(request.getParamValue("customer_type")); 	// 客户类型
			String customer_name = CommonUtils.checkNull(request.getParamValue("customer_name")); 	// 客户名称
			String vin = CommonUtils.checkNull(request.getParamValue("vin")); 						// VIN
			String customer_phone = CommonUtils.checkNull(request.getParamValue("customer_phone")); // 客户电话
			String fleet_name = CommonUtils.checkNull(request.getParamValue("fleet_name")); 		// 集团客户名称
			String materialCode = CommonUtils.checkNull(request.getParamValue("materialCode")); 	// 选择物料组
			String is_fleet = CommonUtils.checkNull(request.getParamValue("is_fleet")); 			// 是否集团客户
			String contract_no = CommonUtils.checkNull(request.getParamValue("contract_no")); 		// 集团客户合同
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate")); 			// 上报日期:开始
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate")); 				// 上报日期 ：截止
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")); 		// 经销商代码
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));					// 业务范围
			String orgId = logonUser.getOrgId().toString();
			String parentOrgId = logonUser.getParentOrgId();
			String dutyType = logonUser.getDutyType();
			String dealerSql = GetOrgIdsOrDealerIdsDAO.getDealerIdsForOrder(
					orgId, parentOrgId, dutyType, "TMD");
			Map<String, String> map = new HashMap<String, String>();
			map.put("customer_type", customer_type);
			map.put("customer_name", customer_name);
			map.put("vin", vin);
			map.put("customer_phone", customer_phone);
			map.put("fleet_name", fleet_name);
			map.put("materialCode", materialCode);
			map.put("is_fleet", is_fleet);
			map.put("contract_no", contract_no);
			map.put("startDate", startDate);
			map.put("endDate", endDate);
			map.put("dealerCode", dealerCode);
			map.put("areaId", areaId);
			map.put("dealerSql", dealerSql);
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = SalesInfoChangeCheckDAO.salesInfoChangeQueryList1(map, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "查询可审核信息 ");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * @Title : toCheck
	 * @Description: 实销信息更改审核PRE
	 * @throws
	 * @LastUpdate :2010-6-30
	 */
	public void toCheck() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);
			String vehicleId = CommonUtils.checkNull(request.getParamValue("vehicleId"));
			String isFleet = CommonUtils.checkNull(request.getParamValue("isFleet")); 					// 是否是集团客户
			String ctm_edit_id = CommonUtils.checkNull(request.getParamValue("ctm_edit_id")); 			// "客户信息更改"id
			String log_id = CommonUtils.checkNull(request.getParamValue("log_id")); 					// "实销信息更改"id
			String order_id = CommonUtils.checkNull(request.getParamValue("order_id"));
//			Map<String, Object> dlrInfo = SalesReportDAO.getSaleDLRInfo(vehicleId) ;

			//通过实效id获取下面上传附件的文件地址
			FsFileuploadPO detail = new FsFileuploadPO();
			detail.setYwzj(Long.valueOf(log_id));
			List<FsFileuploadPO> lists = dao.select(detail);
			
			Map<String, Object> dlrInfo = SalesReportDAO.getSaleDLRInfoByOderId(order_id) ;
			act.setOutData("dlrInfo", dlrInfo);
			Map<String, Object> vehicleInfo = SalesReportDAO.getVehicleInfo(vehicleId); 				// 1、车辆资料
			act.setOutData("vehicleInfo", vehicleInfo);
			TtDealerActualSalesAuditPO auditPO = new TtDealerActualSalesAuditPO();
			auditPO.setLogId(Long.parseLong(log_id));
			List aList = dao.select(auditPO);
			if (null != aList && aList.size() > 0) {
				TtDealerActualSalesAuditPO salesAuditInfo = (TtDealerActualSalesAuditPO) aList.get(0); // 2、销售信息
				act.setOutData("salesAuditInfo", salesAuditInfo);
				/**
				  * 原来的销售信息
				  */
				TtDealerActualSalesPO salesInfo=new TtDealerActualSalesPO();
				Long orderId=salesAuditInfo.getOrderId();
				salesInfo.setOrderId(orderId);
				salesInfo=(TtDealerActualSalesPO)dao.select(salesInfo).get(0);
				act.setOutData("salesInfo", salesInfo);
				/*
				 * 如果是集团客户
				 */
				if (null != isFleet && isFleet.equals(Constant.IF_TYPE_YES + "")) {
					Long fleetId = salesAuditInfo.getFleetId(); 						// 集团客户id
					Long contractId = salesAuditInfo.getContractId(); 					// 集团客户合同id
					TmFleetPO fleetPO = new TmFleetPO();
					fleetPO.setFleetId(fleetId);
					List fList = dao.select(fleetPO);
					if (null != fList && fList.size() > 0) {
						String fleetName = ((TmFleetPO) fList.get(0)).getFleetName();   // 集团客户名称
						act.setOutData("fleetName", fleetName);
						String fleetCode = ((TmFleetPO) fList.get(0)).getFleetCode();   // 集团客户名称
						act.setOutData("fleetCode", fleetCode);
					} else {
						TmCompanyPactPO tcpp = new TmCompanyPactPO() ;
						tcpp.setPactId(fleetId) ;
						
						List cList = dao.select(tcpp);
						
						if (null != cList && cList.size() > 0) {
							String fleetName = ((TmCompanyPactPO) cList.get(0)).getPactName();   // 集团客户名称
							act.setOutData("fleetName", fleetName);
						}
					}
					TtFleetContractPO contractPO = new TtFleetContractPO();
					contractPO.setContractId(contractId);
					List conList = dao.select(contractPO);
					if (null != conList && conList.size() > 0) {
						String contractNo = ((TtFleetContractPO) conList.get(0)).getContractNo(); // 集团客户合同
						act.setOutData("contractNo", contractNo);
					}
				} 
				TtCustomerEditLogPO editLogPO = new TtCustomerEditLogPO();
				editLogPO.setCtmEditId(Long.parseLong(ctm_edit_id));
				List cList = dao.select(editLogPO);
				if (null != cList && cList.size() > 0) {
					TtCustomerEditLogPO cusInfo = (TtCustomerEditLogPO) cList.get(0);
					act.setOutData("cusInfo", cusInfo); // 3、客户信息
					/**
					 * 原来的客户信息
					 */
					TtCustomerPO tc=new TtCustomerPO();
					Long ctm_id=cusInfo.getCtmId();
					tc.setCtmId(ctm_id);
					tc=(TtCustomerPO) dao.select(tc).get(0);
					act.setOutData("oldcusInfo", tc); // 03、原来客户信息
					List linkManList = SalesReportDAO.getLink_List(cusInfo.getCtmId()); // 4、其他联系人信息
					act.setOutData("linkManList", linkManList);
				}
			}
			act.setOutData("lists",lists);
			act.setForword(toCheckSalesInfoChange);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息更改审核PRE");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * @Title : toCheck
	 * @Description: 实销信息更改审核PRE(省系)
	 * @throws
	 * @LastUpdate :2010-6-30
	 */
	public void topCheck() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);

			String vehicleId = CommonUtils.checkNull(request.getParamValue("vehicleId"));
			String isFleet = CommonUtils.checkNull(request.getParamValue("isFleet")); 					// 是否是集团客户
			String ctm_edit_id = CommonUtils.checkNull(request.getParamValue("ctm_edit_id")); 			// "客户信息更改"id
			String log_id = CommonUtils.checkNull(request.getParamValue("log_id")); 					// "实销信息更改"id
			String order_id = CommonUtils.checkNull(request.getParamValue("order_id"));
//			Map<String, Object> dlrInfo = SalesReportDAO.getSaleDLRInfo(vehicleId) ;
			Map<String, Object> dlrInfo = SalesReportDAO.getSaleDLRInfoByOderId(order_id) ;
			act.setOutData("dlrInfo", dlrInfo);
			
			//通过实效id获取下面上传附件的文件地址
			FsFileuploadPO detail = new FsFileuploadPO();
			detail.setYwzj(Long.valueOf(log_id));
			List<FsFileuploadPO> lists = dao.select(detail);
			
			Map<String, Object> vehicleInfo = SalesReportDAO.getVehicleInfo(vehicleId); 				// 1、车辆资料
			act.setOutData("vehicleInfo", vehicleInfo);
			TtDealerActualSalesAuditPO auditPO = new TtDealerActualSalesAuditPO();
			auditPO.setLogId(Long.parseLong(log_id));
			List aList = dao.select(auditPO);
			if (null != aList && aList.size() > 0) {
				TtDealerActualSalesAuditPO salesAuditInfo = (TtDealerActualSalesAuditPO) aList.get(0); // 2、销售信息
				act.setOutData("salesAuditInfo", salesAuditInfo);
				/**
				  * 原来的销售信息
				  */
				TtDealerActualSalesPO salesInfo=new TtDealerActualSalesPO();
				Long orderId=salesAuditInfo.getOrderId();
				salesInfo.setOrderId(orderId);
				salesInfo=(TtDealerActualSalesPO)dao.select(salesInfo).get(0);
				act.setOutData("salesInfo", salesInfo);
				/*
				 * 如果是集团客户
				 */
				if (null != isFleet && isFleet.equals(Constant.IF_TYPE_YES + "")) {
					Long fleetId = salesAuditInfo.getFleetId(); 						// 集团客户id
					Long contractId = salesAuditInfo.getContractId(); 					// 集团客户合同id
					TmFleetPO fleetPO = new TmFleetPO();
					fleetPO.setFleetId(fleetId);
					List fList = dao.select(fleetPO);
					if (null != fList && fList.size() > 0) {
						String fleetName = ((TmFleetPO) fList.get(0)).getFleetName();   // 集团客户名称
						act.setOutData("fleetName", fleetName);
						String fleetCode = ((TmFleetPO) fList.get(0)).getFleetCode();   // 集团客户代码
						act.setOutData("fleetCode", fleetCode);
					} else {
						TmCompanyPactPO tcpp = new TmCompanyPactPO() ;
						tcpp.setPactId(fleetId) ;
						
						List cList = dao.select(tcpp);
						
						if (null != cList && cList.size() > 0) {
							String fleetName = ((TmCompanyPactPO) cList.get(0)).getPactName();   // 集团客户名称
							act.setOutData("fleetName", fleetName);
						}
					}
					TtFleetContractPO contractPO = new TtFleetContractPO();
					contractPO.setContractId(contractId);
					List conList = dao.select(contractPO);
					if (null != conList && conList.size() > 0) {
						String contractNo = ((TtFleetContractPO) conList.get(0)).getContractNo(); // 集团客户合同
						act.setOutData("contractNo", contractNo);
					}
				} 
				TtCustomerEditLogPO editLogPO = new TtCustomerEditLogPO();
				editLogPO.setCtmEditId(Long.parseLong(ctm_edit_id));
				List cList = dao.select(editLogPO);
				if (null != cList && cList.size() > 0) {
					TtCustomerEditLogPO cusInfo = (TtCustomerEditLogPO) cList.get(0);
					act.setOutData("cusInfo", cusInfo); // 3、客户信息
					/**
					 * 原来的客户信息
					 */
					TtCustomerPO tc=new TtCustomerPO();
					Long ctm_id=cusInfo.getCtmId();
					tc.setCtmId(ctm_id);
					tc=(TtCustomerPO) dao.select(tc).get(0);
					act.setOutData("oldcusInfo", tc); // 03、原来客户信息
					List linkManList = SalesReportDAO.getLink_List(cusInfo.getCtmId()); // 4、其他联系人信息
					act.setOutData("linkManList", linkManList);
				}
			}

			act.setOutData("lists",lists);
			act.setForword(toCheckSalesInfopChange);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息更改审核PRE");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * @Title : salesInfoChangeCheckInit
	 * @Description: 批量审核
	 * @throws
	 * @LastUpdate :2010-6-30
	 */
	public void multiCheck() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String checkStatus = Constant.SALES_INFO_CHANGE_STATUS_02.toString();
			String logIds = CommonUtils.checkNull(request.getParamValue("logId"));
			String ctmEditId = "";
			String isFleet = "";
			String checkRamark ="";
			String []logidArr=logIds.split(",");
			try {
				for(String str:logidArr){
					TtDealerActualSalesAuditPO tempAuditPO = new TtDealerActualSalesAuditPO();
					tempAuditPO.setLogId(new Long(str));
					List auditList = dao.select(tempAuditPO);
					if(auditList!=null&&auditList.size()>0){
						tempAuditPO=(TtDealerActualSalesAuditPO) auditList.get(0);
						isFleet=tempAuditPO.getIsFleet().toString();
						ctmEditId=tempAuditPO.getCtmEditId().toString();
						checkData( checkStatus,str,ctmEditId, isFleet, checkRamark);
					}
				}
				act.setOutData("subFlag","success");
			} catch (Exception e) {
				act.setOutData("subFlag","failure" );
			}
			
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息更改批量审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * @Title : checkSubmitAction
	 * @Description: 审核提交
	 * @throws
	 * @LastUpdate :2010-6-30
	 */
	public void checkSubmitAction() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);

			String checkStatus = CommonUtils.checkNull(request.getParamValue("checkStatus"));
			String logId = CommonUtils.checkNull(request.getParamValue("logId"));
			String ctmEditId = CommonUtils.checkNull(request.getParamValue("ctmEditId"));
			String isFleet = CommonUtils.checkNull(request.getParamValue("isFleet"));
			String checkRamark = CommonUtils.checkNull(request.getParamValue("checkRamark"));
			checkData( checkStatus,logId,ctmEditId, isFleet, checkRamark);
			if(logonUser.getDutyType().toString().equals(Constant.DUTY_TYPE_COMPANY.toString())){
				salesInfoChangeCheckInit();
				//act.setForword(forword);
			}else{
				//act.setForword(forword)
				salesInfoChangeCheckpInit();
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息更改审核:审核提交");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	public void checkData(String checkStatus,String logId,String ctmEditId,String isFleet,String checkRamark){
		if (null != checkStatus && !"".equals(checkStatus)) {

			TtDealerActualSalesAuditPO tempAuditPO = new TtDealerActualSalesAuditPO();
			tempAuditPO.setLogId(Long.parseLong(logId));
			List auditList = dao.select(tempAuditPO);
			if (null != auditList && auditList.size() > 0) {
				// 修改后的实销信息
				TtDealerActualSalesAuditPO valueAuditPO = (TtDealerActualSalesAuditPO) auditList.get(0);
				// 如果车厂审核通过
				if (checkStatus.equals(Constant.SALES_INFO_CHANGE_STATUS_03 + "")) {
					Long orderId = valueAuditPO.getOrderId();
					TtDealerActualSalesPO tempSalesPO = new TtDealerActualSalesPO();
					tempSalesPO.setOrderId(orderId);
					List salesList = dao.select(tempSalesPO);
					if (null != salesList && salesList.size() > 0) {
						// 修改前的实销信息
						TtDealerActualSalesPO valueSalesPO = (TtDealerActualSalesPO) salesList.get(0);
						
						TtDealerActualSalesPO chnSalesPO = new TtDealerActualSalesPO();
						
						chnSalesPO.setInvoiceDate(valueAuditPO.getInvoiceDate());
						chnSalesPO.setInvoiceNo(valueAuditPO.getInvoiceNo() == null ? " " : valueAuditPO.getInvoiceNo());
						chnSalesPO.setContractNo(valueAuditPO.getContractNo() == null ? " " : valueAuditPO.getContractNo());
						chnSalesPO.setPrice(valueAuditPO.getPrice());
						chnSalesPO.setPayment(valueAuditPO.getPayment());
						chnSalesPO.setCarCharactor(Integer.parseInt(valueAuditPO.getCarCharactor().toString()));
						chnSalesPO.setMiles(valueAuditPO.getMiles());
						chnSalesPO.setConsignationDate(valueAuditPO.getConsignationDate());
						chnSalesPO.setSalesAddress(valueAuditPO.getSalesAddress());
						chnSalesPO.setSalesReson(valueAuditPO.getSalesReson());
						chnSalesPO.setVehicleNo(valueAuditPO.getVehicleNo() == null ? " " : valueAuditPO.getVehicleNo());
						chnSalesPO.setInsuranceCompany(valueAuditPO.getInsuranceCompany() == null ? " " : valueAuditPO.getInsuranceCompany());
						
						//修改购置方式
						if(valueAuditPO.getPayment().toString().equals(Constant.PAYMENT_03.toString())){
						chnSalesPO.setMortgageType(Long.valueOf( valueAuditPO.getMortgageType()));
						chnSalesPO.setShoufuRatio(Double.valueOf(valueAuditPO.getShoufuRatio()));
						chnSalesPO.setLoansType(Long.valueOf(valueAuditPO.getLoansType()));
						chnSalesPO.setLoansYear(Long.valueOf(valueAuditPO.getLoansYear()));
						chnSalesPO.setMoney(valueAuditPO.getMoney());
						chnSalesPO.setLv(valueAuditPO.getLv());
						chnSalesPO.setBank(valueAuditPO.getBank());
						}else if(valueAuditPO.getPayment().toString().equals(Constant.PAYMENT_04.toString())){
							chnSalesPO.setThischange(valueAuditPO.getThischange());
							chnSalesPO.setLoanschange(valueAuditPO.getLoanschange());
							chnSalesPO.setMortgageType(Long.valueOf(0));
							chnSalesPO.setShoufuRatio(Double.valueOf(0));
							chnSalesPO.setLoansType(Long.valueOf(0));
							chnSalesPO.setLoansYear(Long.valueOf(0));
							chnSalesPO.setMoney(Double.valueOf(0));
							chnSalesPO.setLv(Double.valueOf(0));
							chnSalesPO.setBank(null);
						}else if(valueAuditPO.getPayment().toString().equals(Constant.PAYMENT_05.toString())){
							chnSalesPO.setMortgageType(Long.valueOf( valueAuditPO.getMortgageType()));
							chnSalesPO.setShoufuRatio(Double.valueOf(valueAuditPO.getShoufuRatio()));
							chnSalesPO.setLoansType(Long.valueOf(valueAuditPO.getLoansType()));
							chnSalesPO.setLoansYear(Long.valueOf(valueAuditPO.getLoansYear()));
							chnSalesPO.setMoney(valueAuditPO.getMoney());
							chnSalesPO.setLv(valueAuditPO.getLv());
							chnSalesPO.setBank(valueAuditPO.getBank());
							chnSalesPO.setThischange(valueAuditPO.getThischange());
							chnSalesPO.setLoanschange(valueAuditPO.getLoanschange());
						}
						
						if(valueAuditPO.getIsFleet().intValue() == Constant.IF_TYPE_YES.intValue()) {
							chnSalesPO.setIsFleet(Constant.IF_TYPE_YES) ;
							chnSalesPO.setFleetId(valueAuditPO.getFleetId()) ;
							chnSalesPO.setContractId(valueAuditPO.getContractId()) ;
							chnSalesPO.setContractNo(valueAuditPO.getContractNo()) ;
							chnSalesPO.setFleetStatus(Constant.Fleet_SALES_CHECK_STATUS_01) ;
						}
						
						if(valueAuditPO.getIsFleet().intValue() == Constant.IF_TYPE_NO.intValue()) {
							chnSalesPO.setIsFleet(Constant.IF_TYPE_NO) ;
						}
						
					
						Date insuranceDate = valueAuditPO.getInsuranceDate();
						if (null != insuranceDate && !"".equals(insuranceDate)) {
							chnSalesPO.setInsuranceDate(insuranceDate);
						}
						chnSalesPO.setMemo(valueAuditPO.getMemo() == null ? " " : valueAuditPO.getMemo());
						chnSalesPO.setUpdateDate(new Date());
						// 如果是集团客户
						if (null != isFleet && isFleet.equals(Constant.IF_TYPE_YES + "")) {
							chnSalesPO.setIsFleet(Constant.IF_TYPE_YES);
							chnSalesPO.setFleetId(valueAuditPO.getFleetId());
							chnSalesPO.setContractId(valueAuditPO.getContractId());
						}
						// 执行实销信息更改操作
						TtDealerActualSalesPO sPO = new TtDealerActualSalesPO();
						sPO.setOrderId(valueSalesPO.getOrderId());
						dao.update(sPO, chnSalesPO);
						// 修改实销更改申请表的审核状态
						TtDealerActualSalesAuditPO salPO = new TtDealerActualSalesAuditPO();
						salPO.setStatus(Constant.SALES_INFO_CHANGE_STATUS_03);
						salPO.setUpdateDate(new Date());
						if (null != checkRamark && !"".equals(checkRamark)) {
							salPO.setCheckRamark(checkRamark.trim());
						}
						
						TtDealerActualSalesAuditPO tActualSalesAuditPO = new TtDealerActualSalesAuditPO();
						tActualSalesAuditPO.setLogId(Long.parseLong(logId));
						dao.update(tActualSalesAuditPO, salPO);

						// 如果不是集团客户，修改客户信息
//						 if (null != isFleet && isFleet.equals(Constant.IF_TYPE_NO + "")) {
							TtCustomerEditLogPO tempCustomerEditLogPO = new TtCustomerEditLogPO();
							tempCustomerEditLogPO.setCtmEditId(Long.parseLong(ctmEditId));
							TtCustomerEditLogPO ttCustomerEditLogPO1=new TtCustomerEditLogPO();
							ttCustomerEditLogPO1.setIsReturn("0");
							ttCustomerEditLogPO1.setUpdateDate(new Date());
							dao.update(tempCustomerEditLogPO, ttCustomerEditLogPO1);
							List cusList = dao.select(tempCustomerEditLogPO);
							if (null != cusList && cusList.size() > 0) {
								// 修改后的客户信息
								TtCustomerEditLogPO valCustomerEditLogPO = (TtCustomerEditLogPO) cusList.get(0);
								Long ctmId = valCustomerEditLogPO.getCtmId();
								TtCustomerPO tempCustomerPO = new TtCustomerPO();
								tempCustomerPO.setCtmId(ctmId);
								
								List cuList = dao.select(tempCustomerPO);
								if (null != cuList && cuList.size() > 0) {
									// 修改前的客户信息
									TtCustomerPO valueCustomerPO = (TtCustomerPO) cuList.get(0);

									TtCustomerPO chnCustomerPO = new TtCustomerPO();
									chnCustomerPO.setCtmName(valCustomerEditLogPO.getCtmName());
									chnCustomerPO.setCtmType(valCustomerEditLogPO.getCtmType());
									chnCustomerPO.setCardType(valCustomerEditLogPO.getCardType());
									chnCustomerPO.setCardNum(valCustomerEditLogPO.getCardNum() == null ? " " : valCustomerEditLogPO.getCardNum());
									chnCustomerPO.setSex(valCustomerEditLogPO.getSex());
									chnCustomerPO.setBirthday(valCustomerEditLogPO.getBirthday());
									chnCustomerPO.setIndustry(valCustomerEditLogPO.getIndustry());
									chnCustomerPO.setProfession(valCustomerEditLogPO.getProfession());
									chnCustomerPO.setJob(valCustomerEditLogPO.getJob() == null ? " " : valCustomerEditLogPO.getJob());
									chnCustomerPO.setCtmForm(valCustomerEditLogPO.getCtmForm());
									chnCustomerPO.setMainPhone(valCustomerEditLogPO.getMainPhone());
									chnCustomerPO.setOtherPhone(valCustomerEditLogPO.getOtherPhone());
									chnCustomerPO.setIncome(valCustomerEditLogPO.getIncome());
									chnCustomerPO.setEducation(valCustomerEditLogPO.getEducation());
									chnCustomerPO.setIsMarried(valCustomerEditLogPO.getIsMarried());
									chnCustomerPO.setEmail(valCustomerEditLogPO.getEmail() == null ? " " : valCustomerEditLogPO.getEmail());
									if (null != valCustomerEditLogPO.getProvince() && !"".equals(valCustomerEditLogPO.getProvince())) {
										chnCustomerPO.setProvince(valCustomerEditLogPO.getProvince());
									}
									if (null != valCustomerEditLogPO.getCity() && !"".equals(valCustomerEditLogPO.getCity())) {
										chnCustomerPO.setCity(valCustomerEditLogPO.getCity());
									}
									if (null != valCustomerEditLogPO.getTown() && !"".equals(valCustomerEditLogPO.getTown())) {
										chnCustomerPO.setTown(valCustomerEditLogPO.getTown());
									}
									chnCustomerPO.setAddress(valCustomerEditLogPO.getAddress());
									chnCustomerPO.setPostCode(valCustomerEditLogPO.getPostCode() == null ? " " : valCustomerEditLogPO.getPostCode());
									chnCustomerPO.setCompanyName(valCustomerEditLogPO.getCompanyName() == null ? " " : valCustomerEditLogPO.getCompanyName());
									chnCustomerPO.setCompanySName(valCustomerEditLogPO.getCompanySName() == null ? " " : valCustomerEditLogPO.getCompanySName());
									chnCustomerPO.setKind(valCustomerEditLogPO.getKind());
									chnCustomerPO.setLevelId(valCustomerEditLogPO.getLevelId());
									chnCustomerPO.setCompanyPhone(valCustomerEditLogPO.getCompanyPhone() == null ? " " : valCustomerEditLogPO.getCompanyPhone());
									if (null != valCustomerEditLogPO.getVehicleNum() && "0".equals(valCustomerEditLogPO.getVehicleNum() + "")) {
										chnCustomerPO.setVehicleNum(valCustomerEditLogPO.getVehicleNum());
									}
									chnCustomerPO.setCompanyCode(valCustomerEditLogPO.getCompanyCode());
									chnCustomerPO.setCtmName(valCustomerEditLogPO.getCtmName());
									chnCustomerPO.setMainPhone(valCustomerEditLogPO.getMainPhone());
									chnCustomerPO.setUpdateDate(new Date());
									//TODO 新增是否二手车置换 2012-07-03 韩晓宇
									if(valueAuditPO != null && valueAuditPO.getIsSecond() != null) {
										chnCustomerPO.setIsSecond(Long.parseLong(valueAuditPO.getIsSecond().toString()));
									}
									//TODO END
									TtCustomerPO cCustomerPO = new TtCustomerPO();
									cCustomerPO.setCtmId(ctmId);
									dao.update(cCustomerPO, chnCustomerPO);
								}
							}
						}
					}
//				}
				// 如果省系审核通过
				if (checkStatus.equals(Constant.SALES_INFO_CHANGE_STATUS_02 + "")) {
					
//					TtCustomerEditLogPO tempCustomerEditLogPO = new TtCustomerEditLogPO();
//					tempCustomerEditLogPO.setCtmEditId(Long.parseLong(ctmEditId));
//					TtCustomerEditLogPO ttCustomerEditLogPO1=new TtCustomerEditLogPO();
//					ttCustomerEditLogPO1.setIsReturn("0");
//					dao.update(tempCustomerEditLogPO, ttCustomerEditLogPO1);
					// 修改实销更改申请表的审核状态
					TtDealerActualSalesAuditPO salPO = new TtDealerActualSalesAuditPO();
					salPO.setStatus(Constant.SALES_INFO_CHANGE_STATUS_02);
					if (null != checkRamark && !"".equals(checkRamark)) {
						salPO.setCheckRamark(checkRamark.trim());
					}
					TtDealerActualSalesAuditPO tAuditPO = new TtDealerActualSalesAuditPO();
					tAuditPO.setLogId(Long.parseLong(logId));
					dao.update(tAuditPO, salPO);
				}
				// 如果驳回
				if (checkStatus.equals(Constant.SALES_INFO_CHANGE_STATUS_04 + "")) {
					TtCustomerEditLogPO tempCustomerEditLogPO = new TtCustomerEditLogPO();
					tempCustomerEditLogPO.setCtmEditId(Long.parseLong(ctmEditId));
					TtCustomerEditLogPO ttCustomerEditLogPO1=new TtCustomerEditLogPO();
					ttCustomerEditLogPO1.setIsReturn("0");
					ttCustomerEditLogPO1.setUpdateDate(new Date());
					dao.update(tempCustomerEditLogPO, ttCustomerEditLogPO1);
					// 修改实销更改申请表的审核状态
					TtDealerActualSalesAuditPO salPO = new TtDealerActualSalesAuditPO();
					salPO.setStatus(Constant.SALES_INFO_CHANGE_STATUS_04);
					salPO.setUpdateDate(new Date());
					if (null != checkRamark && !"".equals(checkRamark)) {
						salPO.setCheckRamark(checkRamark.trim());
					}
					TtDealerActualSalesAuditPO tAuditPO = new TtDealerActualSalesAuditPO();
					tAuditPO.setLogId(Long.parseLong(logId));
					dao.update(tAuditPO, salPO);
				}
			}
			// 如果车厂审核通过
			if (checkStatus.equals(Constant.SALES_INFO_CHANGE_STATUS_03 + "")) {
				AclUserBean logonUser = null;
				logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
				//通过审核更改申请ID查询是否有修改上传附件信息
				String strFjid="";
				Long OrderId=null;
				FsFileuploadPO xPO = new FsFileuploadPO();
				xPO.setYwzj(Long.parseLong(logId));
				List xlist=dao.select(xPO);
				if (null != xlist && xlist.size() > 0) {//如果有修改上传附件
		        for(int i=0;i<xlist.size();i++) {
				Long xFjid=((FsFileuploadPO) xlist.get(i)).getFjid();//获取修改后的上传附件的文件ID
				String fileUrl=((FsFileuploadPO) xlist.get(i)).getFileurl();
				String fileName=((FsFileuploadPO) xlist.get(i)).getFilename();
				String fileId=((FsFileuploadPO) xlist.get(i)).getFileid();
				
				long sfjid=Long.valueOf(SequenceManager.getSequence(null));
				strFjid +=sfjid+",";
				FsFileuploadPO Insertpo = new FsFileuploadPO();
				Insertpo.setFileid(fileId);
				Insertpo.setFileurl(fileUrl);
				Insertpo.setFilename(fileName);
				Insertpo.setFjid(sfjid);
				
				FileUpLoadDAO dao = new FileUpLoadDAO();
				try {
					dao.reviewedFile(Insertpo, logonUser);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				}//循环结束
		     if (null != xlist && xlist.size() > 0) {//如果有修改上传附件
			    OrderId=((TtDealerActualSalesAuditPO) auditList.get(0)).getOrderId();
			    Long vehicle_id=((TtDealerActualSalesAuditPO) auditList.get(0)).getVehicleId();
				//通过实销信息的orderId查询出来修改上传附件的原件的文件ID
				FsFileuploadPO fsPO = new FsFileuploadPO();
				fsPO.setYwzj(OrderId);
				List fslist=dao.select(fsPO);
				for(int j=0;j<fslist.size();j++) {
				Long Fjid=((FsFileuploadPO) fslist.get(j)).getFjid();//获取修改文件之前的原文件ID
				//修改原文件的关系业务关联为审核车辆的vehicle_id
				FsFileuploadPO po = new FsFileuploadPO();
				po.setFjid(Fjid);
				FsFileuploadPO po2 = new FsFileuploadPO();
				po2.setYwzj(vehicle_id);
				dao.update(po, po2);
				}
		  }
		  //更新审核通过的附件文件的业务关系为实销信息对应的业务关系
		  	try {
		  		FileUpLoadDAO dao1 = new FileUpLoadDAO();
		  		if(strFjid.length()>0){
		  			strFjid = strFjid.substring(0,strFjid.length()-1);
				}
				dao1.addenableFile(OrderId.toString(),strFjid,logonUser); 
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				}
			}
			// 如果是车厂审核通过 或者 驳回时 需要修改车辆的状态为正常
			if (checkStatus.equals(Constant.SALES_INFO_CHANGE_STATUS_03 + "")||checkStatus.equals(Constant.SALES_INFO_CHANGE_STATUS_04 + "")) {
			//修改车辆的状态为正常
				Long VehicleId=((TtDealerActualSalesAuditPO) auditList.get(0)).getVehicleId();
				//根据vehicle_id 修改车辆的状态为正常
				TmVehiclePO tp=new TmVehiclePO();
				tp.setVehicleId(VehicleId);
				TmVehiclePO tpnew=new TmVehiclePO();
				tpnew.setLockStatus(Constant.LOCK_STATUS_01);
				tpnew.setUpdateDate(new Date());
				dao.update(tp,tpnew);
			}
			
		}
		
//		if (null != isFleet && isFleet.equals(Constant.IF_TYPE_YES + "")) {
//			salesInfoChangeCheckFleetInit();
//		}else{
//			salesInfoChangeCheckInit();
//		}
	}
	/** 
	* @Title	  : checkDetailInfo 
	* @Description: 查看审核明细信息
	* @throws 
	* @LastUpdate :2010-7-1
	*/
	public void checkDetailInfo(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);

			String vehicleId = CommonUtils.checkNull(request.getParamValue("vehicleId"));
			String isFleet = CommonUtils.checkNull(request.getParamValue("isFleet")); 					// 是否是集团客户
			String ctm_edit_id = CommonUtils.checkNull(request.getParamValue("ctm_edit_id")); 			// "客户信息更改"id
			String log_id = CommonUtils.checkNull(request.getParamValue("log_id")); 					// "实销信息更改"id
			

			//通过实效id获取下面上传附件的文件地址
			FsFileuploadPO detail = new FsFileuploadPO();
			detail.setYwzj(Long.valueOf(log_id));
			List<FsFileuploadPO> lists = dao.select(detail); 
			
			Map<String, Object> vehicleInfo = SalesReportDAO.getVehicleInfo(vehicleId); 				// 1、车辆资料
			act.setOutData("vehicleInfo", vehicleInfo);
			TtDealerActualSalesAuditPO auditPO = new TtDealerActualSalesAuditPO();
			auditPO.setLogId(Long.parseLong(log_id));
			List aList = dao.select(auditPO);
			if (null != aList && aList.size() > 0) {
				TtDealerActualSalesAuditPO salesAuditInfo = (TtDealerActualSalesAuditPO) aList.get(0); // 2、销售信息
				act.setOutData("salesAuditInfo", salesAuditInfo);
				/*
				 * 如果是集团客户
				 */
				if (null != isFleet && isFleet.equals(Constant.IF_TYPE_YES + "")) {
					Long fleetId = salesAuditInfo.getFleetId(); 						// 集团客户id
					Long contractId = salesAuditInfo.getContractId(); 					// 集团客户合同id
					TmFleetPO fleetPO = new TmFleetPO();
					fleetPO.setFleetId(fleetId);
					List fList = dao.select(fleetPO);
					if (null != fList && fList.size() > 0) {
						String fleetName = ((TmFleetPO) fList.get(0)).getFleetName();   // 集团客户名称
						act.setOutData("fleetName", fleetName);
					}
					TtFleetContractPO contractPO = new TtFleetContractPO();
					contractPO.setContractId(contractId);
					List conList = dao.select(contractPO);
					if (null != conList && conList.size() > 0) {
						String contractNo = ((TtFleetContractPO) conList.get(0)).getContractNo(); // 集团客户合同
						act.setOutData("contractNo", contractNo);
					}
				}
				TtCustomerEditLogPO editLogPO = new TtCustomerEditLogPO();
				editLogPO.setCtmEditId(Long.parseLong(ctm_edit_id));
				List cList = dao.select(editLogPO);
				if (null != cList && cList.size() > 0) {
					TtCustomerEditLogPO cusInfo = (TtCustomerEditLogPO) cList.get(0);
					act.setOutData("cusInfo", cusInfo); // 3、客户信息

					List linkManList = SalesReportDAO.getLink_List(cusInfo.getCtmId()); // 4、其他联系人信息
					act.setOutData("linkManList", linkManList);
				}
			}
			act.setOutData("lists",lists);
			act.setForword(salseInfoChangeDetail);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "查看审核明细信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
		
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
}
