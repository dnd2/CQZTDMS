package com.infodms.dms.actions.sales.customerInfoManage;

import static com.infodms.dms.actions.sales.customerInfoManage.SalesReport.setStr;

import java.io.OutputStream;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.common.FileUploadManager;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.common.relation.DealerRelation;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.dao.sales.customerInfoManage.SalesVehicleReturnDAO;
import com.infodms.dms.dao.sales.marketmanage.planmanage.ActivitiesPlanManageDao;
import com.infodms.dms.dao.sales.storageManage.CheckVehicleDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TPcDelvyPO;
import com.infodms.dms.po.TPcRevisitPO;
import com.infodms.dms.po.TcUserPO;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TtAsRepairOrderPO;
import com.infodms.dms.po.TtAsWrApplicationPO;
import com.infodms.dms.po.TtDealerActualSalesPO;
import com.infodms.dms.po.TtReturnChkPO;
import com.infodms.dms.po.TtVsActualSalesReturnPO;
import com.infodms.dms.po.TtVsIntegrationChangePO;
import com.infodms.dms.po.TtVsPersonPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.ActionContextExtend;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class SalesVehicleReturn extends BaseDao{

	public Logger logger = Logger.getLogger(SalesVehicleReturn.class);
	private ActionContext act = ActionContext.getContext();
	private AclUserBean logonUser;
	private ResponseWrapper response = act.getResponse();
	private static final SalesVehicleReturnDAO dao = SalesVehicleReturnDAO.getInstance();
	RequestWrapper request = act.getRequest();
	private final String  salesVehicleReturnReqInitURL = "/jsp/sales/customerInfoManage/salesVehicleReturnReqInit.jsp";
	private final String  salesVehicleReturnCheckInitURL = "/jsp/sales/customerInfoManage/salesVehicleReturnCheckInit.jsp";
	private final String  salesVehicleReturnQueryInitURL = "/jsp/sales/customerInfoManage/salesVehicleReturnQueryInit.jsp";
	private final String  salesVehicleReturnCheckQueryInitURL = "/jsp/sales/customerInfoManage/salesVehicleReturnCheckQueryInit.jsp";
	private final String  returnVehicleInfoURL = "/jsp/sales/customerInfoManage/returnVehicleInfo.jsp";
	private final String  toCheckReturnVehicleURL = "/jsp/sales/customerInfoManage/toCheckReturnVehicle.jsp";
	private final String  CHK_RECORD_URL = "/jsp/sales/customerInfoManage/salesVehicleReturnCheckRecords.jsp";
	
	
	private final String  salesDepartmentReturnReqInitURL = "/jsp/sales/customerInfoManage/salesDepartmentReturnInit.jsp";
	private final String  toSalesDepartmentCheckReturnVehicleURL = "/jsp/sales/customerInfoManage/toSalesDepartmentCheckReturnVehicle.jsp";
	/** 
	* @Title	  : salesInfoChangeInit 
	* @Description: 实销退车申请页面初始化 
	* @throws 
	* @LastUpdate :2010-6-28
	*/
	public void salesVehicleReturnReqInit(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> areaList1 = MaterialGroupManagerDao.getDealerBusiness(poseId.toString());
			
			act.setOutData("areaList", areaList1);
			act.setForword(salesVehicleReturnReqInitURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "实销退车申请页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 售后部实施退车
	 * */
	public void salesDepartmentReturnInit(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> areaList1 = MaterialGroupManagerDao.getDealerBusiness(poseId.toString());
			
			act.setOutData("areaList", areaList1);
			act.setForword(salesDepartmentReturnReqInitURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "实销退车申请页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
		
	}
	
	/**
	 * 售后部实销退车查询
	 * */
	
	public void salesDepartmentQueryToReturnVehicleList(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String vin =  CommonUtils.checkNull(request.getParamValue("vin"));
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			String entity = CommonUtils.checkNull(request.getParamValue("entity"));
			Long orgId = logonUser.getOrgId() ;
			String dutyType = logonUser.getDutyType() ;
			String status = null ;
			
			if (null != vin && !"".equals(vin)) {
				vin = vin.trim();
			}
			
			if (null != dealerCode && !"".equals(dealerCode)) {
				dealerCode = dealerCode.trim();
			}
			
			if(CommonUtils.isNullString(entity)) {
				entity = getEntityStr() ;
			}
			
			if(Constant.DUTY_TYPE_COMPANY.toString().equals(dutyType)) {
				orgId = null ;
				status = Constant.RETURN_CHECK_STATUS_01.toString() ;
			} else if (Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)) {
				status = Constant.RETURN_CHECK_STATUS_SYB.toString() ;
			}
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = SalesVehicleReturnDAO.salePartmentGetToReturnVehicleList_Entity(dutyType, orgId , logonUser.getUserId(),status, entity, vin, dealerCode, Constant.PAGE_SIZE, curPage);
			
			// PageResult<Map<String, Object>> ps = SalesVehicleReturnDAO.getToReturnVehicleList(entity,vin,dealerCode, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "实销退车审核:查询带审核退车信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/** 
	* @Title	  : salesVehicleReturnCheckInit 
	* @Description: 审核退车信息（售后部）
	* @throws 
	* @LastUpdate :2010-6-28
	*/
	public void toCheckSalesDepartmentReturnVehicle(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			
			String dutyType = logonUser.getDutyType() ;
			
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("areaList", areaList);
			String vehicleId =  CommonUtils.checkNull(request.getParamValue("vehicle_id"));
			String order_id =  CommonUtils.checkNull(request.getParamValue("order_id"));
			String return_id =  CommonUtils.checkNull(request.getParamValue("return_id"));
			
//			Map<String, String> salesVehicleInfo = SalesVehicleReturnDAO.getSalesVehicleInfo(vehicleId);
//			Map<String, String> salesCusInfo = SalesVehicleReturnDAO.getSalesCusInfo(vehicleId);
			Map<String, String> salesVehicleInfo = SalesVehicleReturnDAO.getSalesVehicleInfoByOrderId(order_id);
			Map<String, String> salesCusInfo = SalesVehicleReturnDAO.getSalesCusInfoByOrderId(order_id);
			Map<String, String> returnReason = SalesVehicleReturnDAO.getReturnReason(return_id);
			String isFleet = String.valueOf(salesCusInfo.get("IS_FLEET"));
			String ctmType = String.valueOf(salesCusInfo.get("CTM_TYPE"));
			//根据return_id查询上传附件
			List<Map<String, Object>> attachList = ActivitiesPlanManageDao.getInstance().getAttachInfos(return_id);
			if(null!=attachList&&attachList.size()!=0){
				act.setOutData("attachList", attachList);
			}
			
			String entity = request.getParamValue("entity");
			
			Double scCost = SalesVehicleReturnDAO.getScCost(vehicleId) ;	// 售后费用

            List<Map<String,Object>> checkRecords=dao.getReturnChks(return_id);
			act.setOutData("checkRecords", checkRecords) ;

			act.setOutData("dutyType", dutyType) ;
			act.setOutData("scCost", scCost);
			act.setOutData("entity", entity);
			act.setOutData("isFleet", isFleet);
			act.setOutData("ctmType", ctmType);
			act.setOutData("salesVehicleInfo", salesVehicleInfo);
			act.setOutData("salesCusInfo", salesCusInfo);
			act.setOutData("returnReason", returnReason);
			act.setOutData("returnId", return_id);
			act.setForword(toSalesDepartmentCheckReturnVehicleURL);
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "实销退车审核:查询带审核退车信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/** 
	* @Title	  : salesVehicleReturnCheckQueryInit 
	* @Description: 实销退车审核查询(售后部)
	* @throws 
	* @LastUpdate :2010-6-28
	*/
	public void salesDepartmentReturnFindInit(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			ActionContextExtend atx = (ActionContextExtend) ActionContext.getContext();
			String reqURL = atx.getRequest().getContextPath();
			if("/CVS-SALES".equals(reqURL.toUpperCase())){
				act.setOutData("returnValue", 1);
			}else{
				act.setOutData("returnValue", 2);
			}
			
			List<Map<String, Object>> entityList = SalesVehicleReturnDAO.getEntity(logonUser.getPoseId()) ;

			act.setOutData("entityList", entityList) ;
			
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("areaList", areaList);
			act.setForword(salesVehicleReturnCheckQueryInitURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "实销退车申请页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 获取实体列表
	 */
	/*public void getEntity() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			Long orgId = logonUser.getOrgId() ;
			
			List<Map<String, Object>> entityList = SalesVehicleReturnDAO.getEntity(orgId) ;
			
			act.setOutData("entityList", entityList) ;
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "获取实体列表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}*/
	
	/** 
	* @Title	  : salesInfoChangeInit 
	* @Description: 查询实销车辆 
	* @throws 
	* @LastUpdate :2010-6-28
	*/
	public void querySalesedVehicleList(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String vin =  CommonUtils.checkNull(request.getParamValue("vin"));
			vin = setStr(vin, "L") ;	// 由于当前（2011/3/29）所有的vin均存在“L”，故以此作为查询条件加快查询速度
			if (null != vin && !"".equals(vin)) {
				vin = vin.trim();
			}
			String areaId = request.getParamValue("areaId");
	        DealerRelation dr = new DealerRelation() ;
			String dealerIds = dr.getDealerIdByPose(logonUser.getCompanyId(), logonUser.getPoseId());					//当前用户职位对应的经销商ID
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			String orgId=logonUser.getOrgId().toString();	
			String dutyType=logonUser.getDutyType(logonUser.getDealerId());
			PageResult<Map<String, Object>> ps = SalesVehicleReturnDAO.getSalesedVehicleList(areaId,dealerIds,orgId,dutyType, vin, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "查询实销车辆 ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/** 
	* @Title	  : returnVehicleInfo 
	* @Description: 查询实销信息
	* @throws 
	* @LastUpdate :2010-6-28
	*/
	public void returnVehicleInfo(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String vehicleId =  CommonUtils.checkNull(request.getParamValue("vehicle_id"));
			String  order_id=  CommonUtils.checkNull(request.getParamValue("order_id"));
//			Map<String, String> salesVehicleInfo = SalesVehicleReturnDAO.getSalesVehicleInfo(vehicleId);
			Map<String, String> salesVehicleInfo = SalesVehicleReturnDAO.getSalesVehicleInfoByOrderId(order_id);
//			Map<String, String> salesCusInfo = SalesVehicleReturnDAO.getSalesCusInfo(vehicleId);
			Map<String, String> salesCusInfo = SalesVehicleReturnDAO.getSalesCusInfoByOrderId(order_id);
			String isFleet = String.valueOf(salesCusInfo.get("IS_FLEET"));
			String ctmType = String.valueOf(salesCusInfo.get("CTM_TYPE"));
			TmVehiclePO tmv = new TmVehiclePO() ;
			tmv.setVehicleId(Long.parseLong(vehicleId)) ;
			
			Double scCost = SalesVehicleReturnDAO.getScCost(vehicleId) ;	// 售后费用
			
			act.setOutData("scCost", scCost);
			act.setOutData("isFleet", isFleet);
			act.setOutData("ctmType", ctmType);
			act.setOutData("salesVehicleInfo", salesVehicleInfo);
			act.setOutData("salesCusInfo", salesCusInfo);
			act.setForword(returnVehicleInfoURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "查询实销车辆 信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/** 
	* @Title	  : reportSubmit 
	* @Description: 退车提交
	* @throws 
	* @LastUpdate :2010-6-28
	*/
	public void reportSubmit(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String orderId =  CommonUtils.checkNull(request.getParamValue("orderId"));			//实销id
			String vehicleId = CommonUtils.checkNull(request.getParamValue("vehicleId"));		//车辆id
			String returnType = CommonUtils.checkNull(request.getParamValue("returnType"));		//退车类型
			String returnReason = CommonUtils.checkNull(request.getParamValue("returnReason"));	//退车原因
			String scCost = CommonUtils.checkNull(request.getParamValue("scCost"));	//售后费用
			Double mScCost = Double.parseDouble(scCost) ;
			
			String []fjids = request.getParamValues("fjid");					    			//附件ID
			
			TmVehiclePO tmVehiclePO = new TmVehiclePO();
			tmVehiclePO.setVehicleId(Long.parseLong(vehicleId));
			List vList = dao.select(tmVehiclePO);
			Long dealerId = ((TmVehiclePO)vList.get(0)).getDealerId();
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
		
			TtVsActualSalesReturnPO returnPO = new TtVsActualSalesReturnPO();
			String returnId = SequenceManager.getSequence("") ;
			returnPO.setReturnId(Long.parseLong(returnId));
			returnPO.setOrderId(Long.parseLong(orderId));	
			returnPO.setOemCompanyId(oemCompanyId);
			returnPO.setDealerId(dealerId);
			if (null != returnReason && !"".equals(returnReason)) {
				returnPO.setReturnReason(returnReason.trim());
			}
			if (null != returnType && !"".equals(returnType)) {
				returnPO.setReturnType(Long.parseLong(returnType)) ;
			}
			
			String para = CommonDAO.getPara(Constant.SALE_RETURN_PARA_SH_PRICE.toString()) ;
			if(Integer.parseInt(para) > 0) {
				// 有售后费用状态变更为待总部审核，无售后费用的状态变更为待事业部审核
				if(mScCost > 0) {
					returnPO.setStatus(Constant.RETURN_CHECK_STATUS_01);
				} else {
					returnPO.setStatus(Constant.RETURN_CHECK_STATUS_SYB);
				}
			} else {
				returnPO.setStatus(Constant.RETURN_CHECK_STATUS_01);
			}
			returnPO.setAuditStatus(Long.parseLong(Constant.TO_SMALL_AUDIT.toString()));
			returnPO.setReqDate(new Date());
			returnPO.setCreateDate(new Date());
			returnPO.setCreateBy(logonUser.getUserId());
			dao.insert(returnPO);
			TmVehiclePO valuePO = new TmVehiclePO();
			valuePO.setLockStatus(Constant.LOCK_STATUS_05);
			dao.update(tmVehiclePO, valuePO);
			
			//附件添加
			FileUploadManager.fileUploadByBusiness(returnId, fjids, logonUser);
			
			salesVehicleReturnReqInit();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "实销退车提交");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/** 
	* @Title	  : salesVehicleReturnQueryInit 
	* @Description: 退车查询页面初始化
	* @throws 
	* @LastUpdate :2010-6-28
	*/
	public void salesVehicleReturnQueryInit(){
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
			Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> areaList1 = MaterialGroupManagerDao.getDealerBusiness(poseId.toString());
			act.setOutData("areaList", areaList1);
			act.setForword(salesVehicleReturnQueryInitURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "实销退车申请页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/** 
	* @Title	  : queryReturnReqList 
	* @Description: 退车查询结果展示 
	* @throws 
	* @LastUpdate :2010-6-28
	*/
	public void queryReturnReqList_DLR(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String vin =  CommonUtils.checkNull(request.getParamValue("vin"));
			String checkstartDate = request.getParamValue("checkstartDate");		//审核开始日期
			String checkendDate = request.getParamValue("checkendDate");  		//审核结束日期
			String salstartDate = request.getParamValue("salstartDate");//实销开始日期
			String salendDate = request.getParamValue("salendDate");//实销结束日期
			String reqStartDate = CommonUtils.checkNull(request.getParamValue("reqStartDate")); //申请开始日期
			String reqEndDate = CommonUtils.checkNull(request.getParamValue("reqEndDate")); //申请开始日期
			if (null != vin && !"".equals(vin)) {
				vin = vin.trim();
			}
			DealerRelation dr = new DealerRelation() ;
			String dealerIds = dr.getDealerIdByPose(logonUser.getCompanyId(), logonUser.getPoseId());					//当前用户职位对应的经销商ID
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1; // 处理当前页
			PageResult<Map<String, Object>> ps = SalesVehicleReturnDAO.getReturnReqList_DLR(dealerIds,areaId, vin, checkstartDate, checkendDate, salstartDate, salendDate, reqStartDate, reqEndDate, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "实销退车申请页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	public void salesVehicleReturnCheckInitNum(String areId){
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
			if(areId.equals("") || areId==null){
				areId="1";
			}
			act.setOutData("areId", areId);
			act.setForword(salesVehicleReturnCheckInitURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "实销退车申请页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/** 
	* @Title	  : salesVehicleReturnCheckInit 
	* @Description: 退车审核页面初始化
	* @throws 
	* @LastUpdate :2010-6-28
	*/
	public void salesVehicleReturnCheckInit(){
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
			Long poseId = logonUser.getPoseId() ;
			
			List<Map<String, Object>> entityList = SalesVehicleReturnDAO.getEntity(poseId) ;
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
//			getEntity() ;
			act.setOutData("entityList", entityList) ;
			act.setOutData("areaList", areaList);
			act.setOutData("dutyType", logonUser.getDutyType(logonUser.getDealerId()));
			act.setForword(salesVehicleReturnCheckInitURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "实销退车申请页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/** 
	* @Title	  : salesVehicleReturnCheckInit 
	* @Description: 查询待审核退车信息
	* @throws 
	* @LastUpdate :2010-6-28
	*/
	public void queryToReturnVehicleList(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String vin =  CommonUtils.checkNull(request.getParamValue("vin"));
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			String entity = CommonUtils.checkNull(request.getParamValue("entity"));
			Long orgId = logonUser.getOrgId() ;
			String dutyType = logonUser.getDutyType() ;
			String status = null ;
			
			if (null != vin && !"".equals(vin)) {
				vin = vin.trim();
			}
			
			if (null != dealerCode && !"".equals(dealerCode)) {
				dealerCode = dealerCode.trim();
			}
			
			if(CommonUtils.isNullString(entity)) {
				entity = getEntityStr() ;
			}
			
			if(Constant.DUTY_TYPE_COMPANY.toString().equals(dutyType)) {
				orgId = null ;
				status = Constant.RETURN_CHECK_STATUS_01.toString() ;
			} else if (Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)) {
				status = Constant.RETURN_CHECK_STATUS_SYB.toString() ;
			}
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = SalesVehicleReturnDAO.getToReturnVehicleList_Entity(dutyType, orgId , logonUser.getUserId(),status, entity, vin, dealerCode, Constant.PAGE_SIZE, curPage);
			
			// PageResult<Map<String, Object>> ps = SalesVehicleReturnDAO.getToReturnVehicleList(entity,vin,dealerCode, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "实销退车审核:查询带审核退车信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/** 
	* @Title	  : salesVehicleReturnCheckInit 
	* @Description: 审核退车信息
	* @throws 
	* @LastUpdate :2010-6-28
	*/
	public void toCheckReturnVehicle(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			
			String dutyType = logonUser.getDutyType() ;
			
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("areaList", areaList);
			String vehicleId =  CommonUtils.checkNull(request.getParamValue("vehicle_id"));
			String order_id =  CommonUtils.checkNull(request.getParamValue("order_id"));
			String return_id =  CommonUtils.checkNull(request.getParamValue("return_id"));
			
//			Map<String, String> salesVehicleInfo = SalesVehicleReturnDAO.getSalesVehicleInfo(vehicleId);
//			Map<String, String> salesCusInfo = SalesVehicleReturnDAO.getSalesCusInfo(vehicleId);
			Map<String, String> salesVehicleInfo = SalesVehicleReturnDAO.getSalesVehicleInfoByOrderId(order_id);
			Map<String, String> salesCusInfo = SalesVehicleReturnDAO.getSalesCusInfoByOrderId(order_id);
			Map<String, String> returnReason = SalesVehicleReturnDAO.getReturnReason(return_id);
			String isFleet = String.valueOf(salesCusInfo.get("IS_FLEET"));
			String ctmType = String.valueOf(salesCusInfo.get("CTM_TYPE"));
			//根据return_id查询上传附件
			List<Map<String, Object>> attachList = ActivitiesPlanManageDao.getInstance().getAttachInfos(return_id);
			if(null!=attachList&&attachList.size()!=0){
				act.setOutData("attachList", attachList);
			}
			
			String entity = request.getParamValue("entity");
			
			Double scCost = SalesVehicleReturnDAO.getScCost(vehicleId) ;	// 售后费用

            List<Map<String,Object>> checkRecords=dao.getReturnChks(return_id);
			act.setOutData("checkRecords", checkRecords) ;

			act.setOutData("dutyType", dutyType) ;
			act.setOutData("scCost", scCost);
			act.setOutData("entity", entity);
			act.setOutData("isFleet", isFleet);
			act.setOutData("ctmType", ctmType);
			act.setOutData("salesVehicleInfo", salesVehicleInfo);
			act.setOutData("salesCusInfo", salesCusInfo);
			act.setOutData("returnReason", returnReason);
			act.setOutData("returnId", return_id);
			act.setForword(toCheckReturnVehicleURL);
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "实销退车审核:查询带审核退车信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void isChkUser () {
		AclUserBean logonUser = null;
		try {
			String vehicleId =  CommonUtils.checkNull(request.getParamValue("vehicleId"));
			String returnId =  CommonUtils.checkNull(request.getParamValue("returnId"));
			
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			Long userId = logonUser.getUserId() ;
			
			if (SalesVehicleReturnDAO.isScCost(vehicleId) && !SalesVehicleReturnDAO.isChkUser(userId)) {
				TcUserPO tcu = new TcUserPO() ;
				tcu.setUserId(userId) ;
				List<TcUserPO> tcuList = dao.select(tcu) ;
				
				String acnt = tcuList.get(0).getAcnt() ;
				
				act.setOutData("acnt", acnt) ;
			}
			
			act.setOutData("vehicleId", vehicleId) ;
			act.setOutData("returnId", returnId) ;
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "实销退车审核:检查审核权限");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/** 
	* @Title	  : salesVehicleReturnCheckInit 
	* @Description: 审核退车:提交
	* @throws 
	* @LastUpdate :2010-6-28
	*/
	public void toCheckReturnMultiSubmit(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String returnId =  CommonUtils.checkNull(request.getParamValue("returnId"));   //实销id
			String vehicleId =  "";
			String vin = "" ;
			String orderId="";
			String isPass = "1";      //审核结果，1：通过，0：驳回
			String chk_remark = "";//审核描述
			String[] returnIds=returnId.split(",");
			for(String str:returnIds){
				//根据returnId获取orderId
				TtVsActualSalesReturnPO tvsr =new TtVsActualSalesReturnPO();
				tvsr.setReturnId(new Long(str));
				tvsr=(TtVsActualSalesReturnPO) dao.select(tvsr).get(0);
				orderId=tvsr.getOrderId().toString();
				TtDealerActualSalesPO tdasp=new TtDealerActualSalesPO();
				tdasp.setOrderId(new Long(orderId));
				tdasp=(TtDealerActualSalesPO) dao.select(tdasp).get(0);
				vehicleId=tdasp.getVehicleId().toString();
				TmVehiclePO tv=new TmVehiclePO();
				tv.setVehicleId(tdasp.getVehicleId());
				tv=(TmVehiclePO) dao.select(tv).get(0);
				vin=tv.getVin();
				checkData(logonUser, vehicleId, vin, str, orderId, isPass,
						chk_remark);
			}
			act.setOutData("subFlag", "success");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "审核退车:提交");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/** 
	* @Title	  : salesVehicleReturnCheckInit 
	* @Description: 审核退车:提交
	* @throws 
	* @LastUpdate :2010-6-28
	*/
	public void returnVehicleSubmit(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String vehicleId =  CommonUtils.checkNull(request.getParamValue("vehicleId"));
			String vin = CommonUtils.checkNull(request.getParamValue("vin")) ;
			String return_id =  CommonUtils.checkNull(request.getParamValue("returnId"));//退车申请id
			String orderId =  CommonUtils.checkNull(request.getParamValue("orderId"));   //实销id
			String isPass = CommonUtils.checkNull(request.getParamValue("isPass"));      //审核结果，1：通过，0：驳回
			String chk_remark = CommonUtils.checkNull(request.getParamValue("chk_remark"));//审核描述
			
			int num = checkSalesRepareRecorde(vin);
			if(num>0&&"1".equals(isPass)&&"10431001".equals(logonUser.getDutyType())){//有维修记录，转给维修部审核			
					//车厂
				changToSalsePartment(logonUser,return_id,chk_remark);				
			}
			else{
				checkData(logonUser, vehicleId, vin, return_id, orderId, isPass,chk_remark);
			}

			salesVehicleReturnCheckInit();
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "审核退车:提交");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 得到维记录条数
	 * */
	private int checkSalesRepareRecorde(String vin){
		List<Map<String, Object>> recoredList = dao.getRepareRecorde(vin);
		if(recoredList.size()>0){
			return Integer.parseInt(recoredList.get(0).get("COUNT(1)").toString()); 
		}
		return 0;
	}
	
	
	/**
	 * 转到售后部
	 * */
	private void changToSalsePartment(AclUserBean logonUser,String return_id,String chk_remark){
		//修改退车申请审核状态
		TtVsActualSalesReturnPO tempReturnPO = new TtVsActualSalesReturnPO();
		tempReturnPO.setReturnId(Long.parseLong(return_id));
		TtVsActualSalesReturnPO valueReturnPO = new TtVsActualSalesReturnPO();
		
		valueReturnPO.setStatus(Constant.RETURN_CHECK_STATUS_06);//审核通过
		valueReturnPO.setAuditStatus(Constant.TO_SSEVICE_AUDIT);//(Constant.TO_SSEVICE_AUDIT);
		
		valueReturnPO.setUpdateBy(logonUser.getUserId());
		valueReturnPO.setUpdateDate(new Date());
		valueReturnPO.setChkDate(new Date());
		dao.update(tempReturnPO, valueReturnPO);

		TtReturnChkPO chkPO=new TtReturnChkPO();
		chkPO.setChkId(new Long(SequenceManager.getSequence("")));
		chkPO.setReturnId(new Long(return_id));
		chkPO.setChkOrgId(logonUser.getOrgId());
		chkPO.setChkDesc(chk_remark);
		chkPO.setChkBy(logonUser.getUserId());
		chkPO.setChkDate(new Date());		
		 chkPO.setStatus(Constant.RETURN_CHECK_STATUS_06);
		dao.insert(chkPO);
		
	}
	
	/** 
	* @Title	  : salesVehicleReturnCheckInit 
	* @Description: 审核退车:提交（售后部）
	* @throws 
	* @LastUpdate :2010-6-28
	*/
	public void salesDespartmentReturnVehicleSubmit(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String vehicleId =  CommonUtils.checkNull(request.getParamValue("vehicleId"));
			String vin = CommonUtils.checkNull(request.getParamValue("vin")) ;
			String return_id =  CommonUtils.checkNull(request.getParamValue("returnId"));//退车申请id
			String orderId =  CommonUtils.checkNull(request.getParamValue("orderId"));   //实销id
			String isPass = CommonUtils.checkNull(request.getParamValue("isPass"));      //审核结果，1：通过，0：驳回
			String chk_remark = CommonUtils.checkNull(request.getParamValue("chk_remark"));//审核描述						
			salesDepartmentCheckData(logonUser, vehicleId, vin, return_id, orderId, isPass,chk_remark);
			salesVehicleReturnCheckInit();			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "审核退车:提交");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 售后部提交数据检查
	 * */
	private void salesDepartmentCheckData(AclUserBean logonUser, String vehicleId, String vin,
			String return_id, String orderId, String isPass, String chk_remark) {
		//修改退车申请审核状态
		TtVsActualSalesReturnPO tempReturnPO = new TtVsActualSalesReturnPO();
		tempReturnPO.setReturnId(Long.parseLong(return_id));
		
		TtVsActualSalesReturnPO valueReturnPO = new TtVsActualSalesReturnPO();
		
		if ("1".equals(isPass)) {
			valueReturnPO.setStatus(Constant.RETURN_CHECK_STATUS_02);//审核通过
		
			valueReturnPO.setAuditStatus(Constant.TO_SSEVICE_THROUGH);				
			//修改车辆状态
			SalesVehicleReturnDAO.updateVechile(Long.parseLong(vehicleId)) ;
				
			// 更新工单退车状态
			TtAsRepairOrderPO oldTaro = new TtAsRepairOrderPO() ;
			oldTaro.setVin(vin) ;
			TtAsRepairOrderPO newTaro = new TtAsRepairOrderPO() ;
			newTaro.setIsReturn(Constant.IF_TYPE_YES) ;
			dao.update(oldTaro, newTaro) ;
				
			// 更新索赔单退车状态
			TtAsWrApplicationPO oldTawa = new TtAsWrApplicationPO() ;
			oldTawa.setVin(vin) ;
			TtAsWrApplicationPO newTawa = new TtAsWrApplicationPO() ;
			newTawa.setIsReturn(Constant.IF_TYPE_YES) ;
			dao.update(oldTawa, newTawa) ;
				
			//修改实销信息：已退车
			TtDealerActualSalesPO tempSalesPO = new TtDealerActualSalesPO();
			tempSalesPO.setOrderId(Long.parseLong(orderId));
			TtDealerActualSalesPO valueSalesPO = new TtDealerActualSalesPO();
			valueSalesPO.setIsReturn(Constant.IF_TYPE_YES);
			valueSalesPO.setReturnDate(new Date());
			valueSalesPO.setUpdateDate(new Date());
			valueSalesPO.setUpdateBy(logonUser.getUserId());
			dao.update(tempSalesPO, valueSalesPO);
				
							
			//start yinsh
			//给机构人员信息表中写入记录
			//根据vechileId获取销售人员
			TtDealerActualSalesPO tdasPO=new TtDealerActualSalesPO();
			tdasPO.setOrderId(new Long(orderId));
			tdasPO.setIsReturn(Constant.IF_TYPE_YES);
			tdasPO=(TtDealerActualSalesPO) dao.select(tdasPO).get(0);
				
			//修改交车明细记录状态为退车
			String delvDetailId=tdasPO.getDelvDetailId();
			if(delvDetailId !=null && !"".equals(delvDetailId)){
				TPcDelvyPO tpdp=new TPcDelvyPO();
				tpdp.setDelvDetailId(Long.parseLong(delvDetailId));
				TPcDelvyPO tpdpnew=new TPcDelvyPO();
				tpdpnew.setDeliveryStatus(Constant.delivery_status_03);
				dao.update(tpdp, tpdpnew);
			}
			String sales_man_id=tdasPO.getSalesConId().toString();//销售人员
			TtVsPersonPO tvpp=new TtVsPersonPO();
			tvpp.setPersonId(new Long(sales_man_id));
			//tvpp.setPositionStatus(Constant.POSITION_STATUS_ON);
			TtVsPersonPO tvpp0=new TtVsPersonPO();
			if(dao.select(tvpp)!=null&&dao.select(tvpp).size()!=0){
				tvpp0=(TtVsPersonPO)dao.select(tvpp).get(0);
				//得到原来的业绩积分
				Long performanceInteg=tvpp0.getPerformanceInteg()==null?Long.parseLong("0"):tvpp0.getPerformanceInteg();
					
				//根据vehicle_id 获取车系code
				CheckVehicleDAO daos=CheckVehicleDAO.getInstance();
				Map<String,String> m =daos.getGroupByVechileId(vehicleId.toString());
					
				Long addIteg=tdasPO.getInteg();
				if(addIteg==null){
					return;
				}
				//得到销售顾问最终积分
				Long finalInteg=performanceInteg-addIteg;
				TtVsPersonPO tvpp1=new TtVsPersonPO();
				tvpp1.setPerformanceInteg(finalInteg);
				dao.update(tvpp, tvpp1);
				//给积分变动历史表中写入记录
				TtVsIntegrationChangePO tvic=new TtVsIntegrationChangePO();
				Long integ_change_id = Long.parseLong(SequenceManager.getSequence(""));
				tvic.setIntegChangeId(integ_change_id);
				tvic.setDealerId(tdasPO.getDealerId());
				tvic.setCreateDate(new Date());
				tvic.setIdNo(tvpp0.getIdNo());
				tvic.setName(tvpp0.getName());
				tvic.setIntegBefore(performanceInteg);
				tvic.setIntegAfter(finalInteg);
//				tvic.setIntegType(Constant.PERFORMANCE_INTEG);
				tvic.setChangeType(Constant.INTEG_CHANGE_RETURN);
				tvic.setRelationId(new Long(orderId));
				tvic.setThisChangeInteg(-addIteg);
				tvic.setPerformanceInteg(-addIteg);
				dao.insert(tvic);
					//end
			}
						
		}else{
			//修改车辆状态
			TmVehiclePO tempVehiclePO = new TmVehiclePO();
			tempVehiclePO.setVehicleId(Long.parseLong(vehicleId));
			TmVehiclePO valueVehiclePO = new TmVehiclePO();
			valueVehiclePO.setLifeCycle(Constant.VEHICLE_LIFE_04);
			valueVehiclePO.setLockStatus(Constant.LOCK_STATUS_01) ; // 车辆锁定状态修改，退车锁定-->正常状态
			dao.update(tempVehiclePO,valueVehiclePO);			
			valueReturnPO.setStatus(Constant.RETURN_CHECK_STATUS_03);//驳回			
			valueReturnPO.setAuditStatus(Constant.REGECT_STATUS);
		}

		valueReturnPO.setUpdateBy(logonUser.getUserId());
		valueReturnPO.setUpdateDate(new Date());
		valueReturnPO.setChkDate(new Date());
		dao.update(tempReturnPO, valueReturnPO);

		TtReturnChkPO chkPO=new TtReturnChkPO();
		chkPO.setChkId(new Long(SequenceManager.getSequence("")));
		chkPO.setReturnId(new Long(return_id));
		chkPO.setChkOrgId(logonUser.getOrgId());
		chkPO.setChkDesc(chk_remark);
		chkPO.setChkBy(logonUser.getUserId());
		chkPO.setChkDate(new Date());
		if("1".endsWith(isPass)){
		    chkPO.setStatus(Constant.RETURN_CHECK_STATUS_02);
		}else {
		    chkPO.setStatus(Constant.RETURN_CHECK_STATUS_03);
		}
		dao.insert(chkPO);
	}
	
	
	
	private void checkData(AclUserBean logonUser, String vehicleId, String vin,
			String return_id, String orderId, String isPass, String chk_remark) {
		//修改退车申请审核状态
		TtVsActualSalesReturnPO tempReturnPO = new TtVsActualSalesReturnPO();
		tempReturnPO.setReturnId(Long.parseLong(return_id));
		
		TtVsActualSalesReturnPO valueReturnPO = new TtVsActualSalesReturnPO();
		
		if ("1".equals(isPass)) {
			valueReturnPO.setStatus(Constant.RETURN_CHECK_STATUS_02);//审核通过
			//车厂
			if("10431001".equals(logonUser.getDutyType())){
				valueReturnPO.setAuditStatus(Constant.OTD_AUDIT_THROUGH);
				
				//修改车辆状态
				SalesVehicleReturnDAO.updateVechile(Long.parseLong(vehicleId)) ;
				
				// 更新工单退车状态
				TtAsRepairOrderPO oldTaro = new TtAsRepairOrderPO() ;
				oldTaro.setVin(vin) ;
				TtAsRepairOrderPO newTaro = new TtAsRepairOrderPO() ;
				newTaro.setIsReturn(Constant.IF_TYPE_YES) ;
				dao.update(oldTaro, newTaro) ;
				
				// 更新索赔单退车状态
				TtAsWrApplicationPO oldTawa = new TtAsWrApplicationPO() ;
				oldTawa.setVin(vin) ;
				TtAsWrApplicationPO newTawa = new TtAsWrApplicationPO() ;
				newTawa.setIsReturn(Constant.IF_TYPE_YES) ;
				dao.update(oldTawa, newTawa) ;
				
				//修改实销信息：已退车
				TtDealerActualSalesPO tempSalesPO = new TtDealerActualSalesPO();
				tempSalesPO.setOrderId(Long.parseLong(orderId));
				TtDealerActualSalesPO valueSalesPO = new TtDealerActualSalesPO();
				valueSalesPO.setIsReturn(Constant.IF_TYPE_YES);
				valueSalesPO.setReturnDate(new Date());
				valueSalesPO.setUpdateDate(new Date());
				valueSalesPO.setUpdateBy(logonUser.getUserId());
				dao.update(tempSalesPO, valueSalesPO);
				
				
				
				//start yinsh
				//给机构人员信息表中写入记录
				//根据vechileId获取销售人员
				TtDealerActualSalesPO tdasPO=new TtDealerActualSalesPO();
				tdasPO.setOrderId(new Long(orderId));
				tdasPO.setIsReturn(Constant.IF_TYPE_YES);
				tdasPO=(TtDealerActualSalesPO) dao.select(tdasPO).get(0);
				
				//修改交车明细记录状态为退车
				String delvDetailId=tdasPO.getDelvDetailId();
				if(delvDetailId !=null && !"".equals(delvDetailId)){
					TPcDelvyPO tpdp=new TPcDelvyPO();
					tpdp.setDelvDetailId(Long.parseLong(delvDetailId));
					TPcDelvyPO tpdpnew=new TPcDelvyPO();
					tpdpnew.setDeliveryStatus(Constant.delivery_status_03);
				    dao.update(tpdp, tpdpnew);
				}
				String sales_man_id=tdasPO.getSalesConId().toString();//销售人员
				TtVsPersonPO tvpp=new TtVsPersonPO();
				tvpp.setPersonId(new Long(sales_man_id));
				//tvpp.setPositionStatus(Constant.POSITION_STATUS_ON);
				TtVsPersonPO tvpp0=new TtVsPersonPO();
				if(dao.select(tvpp)!=null&&dao.select(tvpp).size()!=0){
					tvpp0=(TtVsPersonPO)dao.select(tvpp).get(0);
					//得到原来的业绩积分
					Long performanceInteg=tvpp0.getPerformanceInteg()==null?Long.parseLong("0"):tvpp0.getPerformanceInteg();
					
					//根据vehicle_id 获取车系code
					CheckVehicleDAO daos=CheckVehicleDAO.getInstance();
					Map<String,String> m =daos.getGroupByVechileId(vehicleId.toString());
					
					Long addIteg=tdasPO.getInteg();
					if(addIteg==null){
						return;
					}
					//得到销售顾问最终积分
					Long finalInteg=performanceInteg-addIteg;
					TtVsPersonPO tvpp1=new TtVsPersonPO();
					tvpp1.setPerformanceInteg(finalInteg);
					dao.update(tvpp, tvpp1);
					//给积分变动历史表中写入记录
					TtVsIntegrationChangePO tvic=new TtVsIntegrationChangePO();
					Long integ_change_id = Long.parseLong(SequenceManager.getSequence(""));
					tvic.setIntegChangeId(integ_change_id);
					tvic.setDealerId(tdasPO.getDealerId());
					tvic.setCreateDate(new Date());
					tvic.setIdNo(tvpp0.getIdNo());
					tvic.setName(tvpp0.getName());
					tvic.setIntegBefore(performanceInteg);
					tvic.setIntegAfter(finalInteg);
//					tvic.setIntegType(Constant.PERFORMANCE_INTEG);
					tvic.setChangeType(Constant.INTEG_CHANGE_RETURN);
					tvic.setRelationId(new Long(orderId));
					tvic.setThisChangeInteg(-addIteg);
					tvic.setPerformanceInteg(-addIteg);
					dao.insert(tvic);
					//end
				}
				/*
				//yinshunhui 修改潜客部分信息  结束提醒 结束回访任务   start
				TtDealerActualSalesPO salesPO = new TtDealerActualSalesPO();
				salesPO.setOrderId(new Long(orderId));
				salesPO=(TtDealerActualSalesPO) dao.select(salesPO).get(0);
				TPcRevisitPO tpr0=new TPcRevisitPO();
				tpr0.setVinId(new Long(vehicleId));
				tpr0.setTaskStatus(Constant.TASK_STATUS_01);
				if(dao.select(tpr0).size()>0){
					tpr0=(TPcRevisitPO) dao.select(tpr0).get(0);
					TPcRevisitPO tpr=new TPcRevisitPO();
					tpr.setVinId(new Long(vehicleId));
					tpr.setTaskStatus(Constant.TASK_STATUS_01);
					TPcRevisitPO tpr1=new TPcRevisitPO();
					tpr1.setTaskStatus(Constant.TASK_STATUS_03);
					dao.update(tpr, tpr1);//结束回访任务
					CommonUtils.setRemindDone(tpr0.getRevisitId().toString(),Constant.REMIND_TYPE_12.toString());//结束提醒
				}
				*/
				//end
				//小区
			}else if("10431003".equals(logonUser.getDutyType()) || "10431004".equals(logonUser.getDutyType())){
				valueReturnPO.setAuditStatus(Constant.TO_OTD_AUDIT);
			}
			
			/*//修改车辆状态
			TmVehiclePO tempVehiclePO = new TmVehiclePO();
			tempVehiclePO.setVehicleId(Long.parseLong(vehicleId));
			TmVehiclePO valueVehiclePO = new TmVehiclePO();
			valueVehiclePO.setFreeTimes(0) ; 						// 保养次数
			valueVehiclePO.setClaimTacticsId(new Long("")) ;				// 三包id
			valueVehiclePO.setHistoryMile(new Double(0)) ;			// 行驶公里数
			valueVehiclePO.setMeterMile(new Double(0)) ;			// 表盘公里数
			valueVehiclePO.setMileage(new Double(0)) ;				// 里程数
			valueVehiclePO.setStartMileage(new Double(0)) ;			// 开始里程数
			valueVehiclePO.setPurchasedDate(null) ;					// 更新购车日期
			valueVehiclePO.setLifeCycle(Constant.VEHICLE_LIFE_03);
			valueVehiclePO.setLockStatus(Constant.LOCK_STATUS_01) ; // 车辆锁定状态修改，退车锁定-->正常状态
			dao.update(tempVehiclePO,valueVehiclePO);*/
			
		}else{
			//修改车辆状态
			TmVehiclePO tempVehiclePO = new TmVehiclePO();
			tempVehiclePO.setVehicleId(Long.parseLong(vehicleId));
			TmVehiclePO valueVehiclePO = new TmVehiclePO();
			valueVehiclePO.setLifeCycle(Constant.VEHICLE_LIFE_04);
			valueVehiclePO.setLockStatus(Constant.LOCK_STATUS_01) ; // 车辆锁定状态修改，退车锁定-->正常状态
			dao.update(tempVehiclePO,valueVehiclePO);
			
			valueReturnPO.setStatus(Constant.RETURN_CHECK_STATUS_03);//驳回
			
			valueReturnPO.setAuditStatus(Constant.REGECT_STATUS);
		}
		//改为在TT_RETURN_CHK表中存储审核记录
		/*if (!"".equals(chk_remark)) {
			valueReturnPO.setChkRemark(chk_remark.trim());
		}
		valueReturnPO.setChkDate(new Date());*/
		valueReturnPO.setUpdateBy(logonUser.getUserId());
		valueReturnPO.setUpdateDate(new Date());
		valueReturnPO.setChkDate(new Date());
		dao.update(tempReturnPO, valueReturnPO);

		TtReturnChkPO chkPO=new TtReturnChkPO();
		chkPO.setChkId(new Long(SequenceManager.getSequence("")));
		chkPO.setReturnId(new Long(return_id));
		chkPO.setChkOrgId(logonUser.getOrgId());
		chkPO.setChkDesc(chk_remark);
		chkPO.setChkBy(logonUser.getUserId());
		chkPO.setChkDate(new Date());
		if("1".endsWith(isPass)){
		    chkPO.setStatus(Constant.RETURN_CHECK_STATUS_02);
		}else {
		    chkPO.setStatus(Constant.RETURN_CHECK_STATUS_03);
		}
		dao.insert(chkPO);
	}
	
	/** 
	* @Title	  : salesVehicleReturnCheckQueryInit 
	* @Description: 实销退车审核查询
	* @throws 
	* @LastUpdate :2010-6-28
	*/
	public void salesVehicleReturnCheckQueryInit(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			ActionContextExtend atx = (ActionContextExtend) ActionContext.getContext();
			String reqURL = atx.getRequest().getContextPath();
			if("/CVS-SALES".equals(reqURL.toUpperCase())){
				act.setOutData("returnValue", 1);
			}else{
				act.setOutData("returnValue", 2);
			}
			
			List<Map<String, Object>> entityList = SalesVehicleReturnDAO.getEntity(logonUser.getPoseId()) ;

			act.setOutData("entityList", entityList) ;
			
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("areaList", areaList);
			act.setForword(salesVehicleReturnCheckQueryInitURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "实销退车申请页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/** 
	* @Title	  : salesVehicleReturnCheckQueryInit 
	* @Description: 实销退车审核查询信息展示
	* @throws 
	* @LastUpdate :2010-6-28
	*/
	public void queryReturnCheckList(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String vin =  CommonUtils.checkNull(request.getParamValue("vin"));
			String dealerCode =  CommonUtils.checkNull(request.getParamValue("dealerCode"));
			String status = CommonUtils.checkNull(request.getParamValue("status"));					//状态
			String checkstartDate=request.getParamValue("checkstartDate");		//审核开始日期
			String checkendDate=request.getParamValue("checkendDate");  		//审核结束日期
			String salstartDate=request.getParamValue("salstartDate");//实销开始日期
			String salendDate=request.getParamValue("salendDate");//实销结束日期
			//2012-03-12 新增申请日期
			String reqStartDate = CommonUtils.checkNull(request.getParamValue("reqStartDate")); //申请开始日期
			String reqEndDate = CommonUtils.checkNull(request.getParamValue("reqEndDate")); //申请开始日期
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId")) ;	// 大区id
			String entity = CommonUtils.checkNull(request.getParamValue("entity"));
			
			String dutyType = logonUser.getDutyType() ;
			
			if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)) {
				orgId = logonUser.getOrgId().toString() ;
			}
			
			if(CommonUtils.isNullString(entity)) {
				entity = getEntityStr() ;
			}
			
			if (null != dealerCode && !"".equals(dealerCode)) {
				dealerCode = dealerCode.trim();
			}
			if (null != vin && !"".equals(vin)) {
				vin = vin.trim();
			}
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
					/*String areaId="";
					if(request.getParamValue("areaId")!=null&&request.getParamValue("areaId")!=""){
						areaId=request.getParamValue("areaId");
					}else{
						areaId=GetCommonArea.getMyArea();
					}*/
			PageResult<Map<String, Object>> ps = SalesVehicleReturnDAO.getReturnCheckList(logonUser.getUserId(), dutyType, orgId, checkstartDate,checkendDate,salstartDate,salendDate, reqStartDate, reqEndDate, entity,vin, status,dealerCode,Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "实销退车审核查询信息展示");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public String getEntityStr() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER) ;	
		
		List<Map<String, Object>> salesRetList = SalesVehicleReturnDAO.getEntity(logonUser.getPoseId()) ;
		
		StringBuffer entityStr = new StringBuffer("") ;
		
		if(!CommonUtils.isNullList(salesRetList)) {
			int len = salesRetList.size() ;
			
			for(int i=0; i<len; i++) {
				if(CommonUtils.isNullString(entityStr.toString())) {
					entityStr.append(salesRetList.get(i).get("PRODUCE_BASE").toString()) ;
				} else {
					entityStr.append(",").append(salesRetList.get(i).get("PRODUCE_BASE").toString()) ;
				}
			}
		}
		
		return entityStr.toString() ;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 实销退车审核查询--下载
	 * */
	public void download(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
		try {
		String vin =  CommonUtils.checkNull(request.getParamValue("vin"));
		String dealerCode =  CommonUtils.checkNull(request.getParamValue("dealerCode"));
		String status = CommonUtils.checkNull(request.getParamValue("status"));					//状态
		String checkstartDate=request.getParamValue("checkstartDate");		//审核开始日期
		String checkendDate=request.getParamValue("checkendDate");  		//审核结束日期
		String salstartDate=request.getParamValue("salstartDate");//实销开始日期
		String salendDate=request.getParamValue("salendDate");//实销结束日期
		//2012-03-12 新增申请日期
		String reqStartDate = CommonUtils.checkNull(request.getParamValue("reqStartDate")); //申请开始日期
		String reqEndDate = CommonUtils.checkNull(request.getParamValue("reqEndDate")); //申请开始日期
		String orgId = CommonUtils.checkNull(request.getParamValue("orgId")) ;	// 大区id
		String entity = CommonUtils.checkNull(request.getParamValue("entity"));
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("vin", vin);
		map.put("dealerCode", dealerCode);
		map.put("status", status);
		map.put("checkstartDate", checkstartDate);
		map.put("checkendDate", checkendDate);
		map.put("salstartDate", salstartDate);
		map.put("salendDate", salendDate);
		map.put("reqStartDate", reqStartDate);
		map.put("reqEndDate", reqEndDate);
		map.put("orgId", orgId);
		map.put("entity", entity);
		map.put("logonUser", logonUser);

		// 导出的文件名
		String fileName = "实销退车审核报表.xls";
		// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
		
		response.setContentType("Application/text/csv");
		response.addHeader("Content-Disposition", "attachment;filename="
				+ fileName);
		
		
		OutputStream os = response.getOutputStream();
		WritableWorkbook workbook = Workbook.createWorkbook(os);
		WritableSheet sheet = workbook.createSheet("实销退车审核报表", 0);
		WritableCellFormat wcf = new WritableCellFormat();
		wcf.setAlignment(Alignment.CENTRE);
		
		List<Map<String, Object>> list = dao.getActualSaleReturnList(map);
		int len = list.size();
			
			/*Calendar  calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH)+1;
			int day =calendar.get(Calendar.DAY_OF_MONTH);*/
			
			
			int row =0;
			
			
			/*act.setOutData("year", year);
			act.setOutData("month", month);
			act.setOutData("day", day);
			 */
			sheet.mergeCells(0, row, 9, row);
			sheet.addCell(new jxl.write.Label(0, row, "实销退车审核报表",wcf));
			
			/*	++row;
			sheet.mergeCells(0, row, 9, row);
			sheet.addCell(new jxl.write.Label(0, row, "日期："+String.valueOf(year)+"年"+String.valueOf(month)+"月"+String.valueOf(day)+"日"));
			*/
			
			
			++row;
			sheet.addCell(new Label(0, row, "车系代码"));
			sheet.addCell(new Label(1, row, "车系名称"));
			sheet.addCell(new Label(2, row, "车型代码"));
			sheet.addCell(new Label(3, row, "车型名称"));
			sheet.addCell(new Label(4, row, "VIN"));
			sheet.addCell(new Label(5, row, "经销商"));
			sheet.addCell(new Label(6, row, "实销日期"));
			sheet.addCell(new Label(7, row, "审核日期"));
			sheet.addCell(new Label(8, row, "申请日期"));
			sheet.addCell(new Label(9, row, "退车原因"));
			
			for(int i=0; i<list.size(); i++) {
				++row;
				sheet.addCell(new Label(0, row, list.get(i).get("SERIES_CODE")==null ? "":list.get(i).get("SERIES_CODE").toString()));
				sheet.addCell(new Label(1, row, list.get(i).get("SERIES_NAME")==null ? "":list.get(i).get("SERIES_NAME").toString()));
				sheet.addCell(new Label(2, row, list.get(i).get("MODEL_CODE")==null ? "":list.get(i).get("MODEL_CODE").toString()));
				sheet.addCell(new Label(3, row, list.get(i).get("MODEL_NAME")==null ? "":list.get(i).get("MODEL_NAME").toString()));
				sheet.addCell(new Label(4, row, list.get(i).get("VIN")==null ? "":list.get(i).get("VIN").toString()));
				sheet.addCell(new Label(5, row, list.get(i).get("DEALER_SHORTNAME")==null ? "":list.get(i).get("DEALER_SHORTNAME").toString()));
				sheet.addCell(new Label(6, row, list.get(i).get("SALES_DATE")==null ? "":list.get(i).get("SALES_DATE").toString()));
				sheet.addCell(new Label(7, row, list.get(i).get("CHK_DATE")==null ? "":list.get(i).get("CHK_DATE").toString()));
				sheet.addCell(new Label(8, row, list.get(i).get("REQ_DATE")==null ? "":list.get(i).get("REQ_DATE").toString()));
				sheet.addCell(new Label(9, row, list.get(i).get("RETURN_REASON")==null ? "":list.get(i).get("RETURN_REASON").toString()));
				
			}
							
			workbook.write();
			workbook.close();
			
			os.flush();
			os.close();
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"实销退车审核报表下载");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 实销退车查询--下载
	 * */
	public void downloadBySVRQ(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String vin =  CommonUtils.checkNull(request.getParamValue("vin"));
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String checkstartDate=request.getParamValue("checkstartDate");		//审核开始日期
			String checkendDate=request.getParamValue("checkendDate");  		//审核结束日期
			String salstartDate=request.getParamValue("salstartDate");//实销开始日期
			String salendDate=request.getParamValue("salendDate");//实销结束日期
			String reqStartDate = CommonUtils.checkNull(request.getParamValue("reqStartDate")); //申请开始日期
			String reqEndDate = CommonUtils.checkNull(request.getParamValue("reqEndDate")); //申请开始日期
			if (null != vin && !"".equals(vin)) {
				vin = vin.trim();
			}
			DealerRelation dr = new DealerRelation() ;
			String dealerIds = dr.getDealerIdByPose(logonUser.getCompanyId(), logonUser.getPoseId());	//当前用户职位对应的经销商ID
			

			// 导出的文件名
			String fileName = "实销退车查询报表.xls";
			// 导出的文字编码
				fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			
			
			OutputStream os = response.getOutputStream();
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			WritableSheet sheet = workbook.createSheet("实销退车查询报表", 0);
			WritableCellFormat wcf = new WritableCellFormat();
			wcf.setAlignment(Alignment.CENTRE);
			
			List<Map<String, Object>> list = SalesVehicleReturnDAO.getReturnReqList_DLR(dealerIds,areaId, vin, checkstartDate, checkendDate, salstartDate, salendDate, reqStartDate, reqEndDate);
			int len = list.size();
				
		/*	Calendar  calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH)+1;
			int day =calendar.get(Calendar.DAY_OF_MONTH);*/
			
			
			int row =0;
			
			
			/*act.setOutData("year", year);
			act.setOutData("month", month);
			act.setOutData("day", day);*/
	
			sheet.mergeCells(0, row, 11, row);
			sheet.addCell(new jxl.write.Label(0, row, "实销退车查询报表",wcf));
			
			/*++row;
			sheet.mergeCells(0, row, 8, row);
			sheet.addCell(new jxl.write.Label(0, row, "日期："+String.valueOf(year)+"年"+String.valueOf(month)+"月"+String.valueOf(day)+"日"));
			
			*/
			
			++row;
			sheet.addCell(new Label(0, row, "车系代码"));
			sheet.addCell(new Label(1, row, "车系名称"));
			sheet.addCell(new Label(2, row, "车型代码"));
			sheet.addCell(new Label(3, row, "车型名称"));
			sheet.addCell(new Label(4, row, "VIN"));
			sheet.addCell(new Label(5, row, "实销日期"));
			sheet.addCell(new Label(6, row, "审核日期"));
			sheet.addCell(new Label(7, row, "申请日期"));
			sheet.addCell(new Label(8, row, "退车原因"));
			sheet.addCell(new Label(9, row, "审核人"));
			sheet.addCell(new Label(10, row, "审核状态"));
			sheet.addCell(new Label(11, row, "审核备注"));
			
			String status = null;
			for(int i=0; i<list.size(); i++) {
				++row;
				sheet.addCell(new Label(0, row, list.get(i).get("SERIES_CODE")==null ? "":list.get(i).get("SERIES_CODE").toString()));
				sheet.addCell(new Label(1, row, list.get(i).get("SERIES_NAME")==null ? "":list.get(i).get("SERIES_NAME").toString()));
				sheet.addCell(new Label(2, row, list.get(i).get("MODEL_CODE")==null ? "":list.get(i).get("MODEL_CODE").toString()));
				sheet.addCell(new Label(3, row, list.get(i).get("MODEL_NAME")==null ? "":list.get(i).get("MODEL_NAME").toString()));
				sheet.addCell(new Label(4, row, list.get(i).get("VIN")==null ? "":list.get(i).get("VIN").toString()));
				sheet.addCell(new Label(5, row, list.get(i).get("SALES_DATE")==null ? "":list.get(i).get("SALES_DATE").toString()));
				sheet.addCell(new Label(6, row, list.get(i).get("CHK_DATE")==null ? "":list.get(i).get("CHK_DATE").toString()));
				sheet.addCell(new Label(7, row, list.get(i).get("REQ_DATE")==null ? "":list.get(i).get("REQ_DATE").toString()));
				sheet.addCell(new Label(8, row, list.get(i).get("RETURN_REASON")==null ? "":list.get(i).get("RETURN_REASON").toString()));
				sheet.addCell(new Label(9, row, list.get(i).get("NAME")==null ? "":list.get(i).get("NAME").toString()));
				status = list.get(i).get("STATUS") == null ? "" : list.get(i).get("STATUS").toString();
				if(status == null) {
					status = "";
				} else if(status.equals(Constant.RETURN_CHECK_STATUS_01.toString())) {
					status = "待总部批复";
				} else if(status.equals(Constant.RETURN_CHECK_STATUS_02.toString())) {
					status = "审核通过";
				} else if(status.equals(Constant.RETURN_CHECK_STATUS_03.toString())) {
					status = "驳回";
				} else if(status.equals(Constant.RETURN_CHECK_STATUS_SYB.toString())) {
					status = "待事业部批复";
				}
				sheet.addCell(new Label(10, row, status));
				sheet.addCell(new Label(11, row, list.get(i).get("CHK_REMARK")==null ? "":list.get(i).get("CHK_REMARK").toString()));
			}
							
			workbook.write();
			workbook.close();
			
			os.flush();
			os.close();
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商退车查询报表下载");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

	public void openCheckDesc(){
		AclUserBean logonUser = null;
		try {
            String returnId=request.getParamValue("returnId");
            List<Map<String,Object>> checkRecords=dao.getReturnChks(returnId);
			act.setOutData("checkRecords", checkRecords) ;
			act.setForword(CHK_RECORD_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "实销退车审批记录初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
