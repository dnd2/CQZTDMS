package com.infodms.dms.actions.sales.displacement;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.common.FileUploadManager;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.sales.dealer.DealerInfoDao;
import com.infodms.dms.dao.sales.displacement.DisplacementCarDao;
import com.infodms.dms.dao.sales.marketmanage.planmanage.ActivitiesPlanManageDao;
import com.infodms.dms.dao.sales.storageManage.VehicleDispatchDAO;
import com.infodms.dms.dao.sales.storageManage.VehicleLocationChangeDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

public class DisplacementCarChk {
	public Logger logger = Logger.getLogger(DisplacementCarChk.class);
	private ActionContext act = ActionContext.getContext();
	// private ResponseWrapper response = act.getResponse();
	private RequestWrapper request = act.getRequest() ;
	private DisplacementCarDao dao = new DisplacementCarDao() ;
	
	private final String OPERA_INIT = "/jsp/sales/displacement/DisplancementCarAreaCheck.jsp" ;
	private final String DETAIL_INIT = "/jsp/sales/displacement/DisplancementCarDetailChk.jsp" ;
	
	private final String SURE_INIT = "/jsp/sales/displacement/DisplancementCarAreaSure.jsp" ;
	
	private final String CLI_INIT = "/jsp/sales/displacement/DisplancementCarAreaCli.jsp" ;
	private final String DETAIL_CLI_INIT = "/jsp/sales/displacement/DisplancementCarDetailCli.jsp" ;
	
	private final String OEM_TOTAL_INIT = "/jsp/sales/displacement/DisplancementCarTotalInit.jsp";
	private final String OEM_QUERY_INIT = "/jsp/sales/displacement/DisplancementCarQueryInit.jsp" ;
	private final String DEALER_QUERY_INIT = "/jsp/sales/displacement/DisplancementCarDlrQueryInit.jsp" ;
	private final String THE_DETAIL_INIT = "/jsp/sales/displacement/detail_init.jsp" ;
	
	private final String DEALER_INIT = "/jsp/sales/displacement/DisplancementCarDealerCheck.jsp";
	private final String DEALER_DETAIL_INIT = "/jsp/sales/displacement/DisplancementCarDealerDetailChk.jsp";
	private final String DEALER_DETAIL_INIT_PRINT = "/jsp/sales/displacement/printDisplacementDetail.jsp";
	
	
//	public void displacementCarAreaChkInit() {
//		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
//		try {
//			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
//			
//			ActivitiesCamCost acc = new ActivitiesCamCost() ;
//			String areas = acc.getAreaStr(areaList) ;
//			
//			act.setOutData("areas", areas) ;
//			act.setOutData("opera", "审核") ;
//			act.setOutData("status", Constant.DisplancementCarrequ_cek_1) ;
//			act.setOutData("chkStatus", Constant.DisplancementCarrequ_cek_2) ;
//			
//			act.setForword(OPERA_INIT) ;
//		} catch(Exception e) {
//			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "二手车审核初始化");
//			logger.error(logonUser, e1);
//			act.setException(e1);
//		}
//	}
	
//	public void displacementCarOemChkInit() {
//		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
//		try {
//			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
//			
//			ActivitiesCamCost acc = new ActivitiesCamCost() ;
//			String areas = acc.getAreaStr(areaList) ;
//			
//			act.setOutData("areas", areas) ;
//			act.setOutData("opera", "审核") ;
//			act.setOutData("status", Constant.DisplancementCarrequ_cek_2) ;
//			act.setOutData("chkStatus", Constant.DisplancementCarrequ_cek_3) ;
//			
//			act.setForword(OPERA_INIT) ;
//		} catch(Exception e) {
//			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "二手车审核初始化");
//			logger.error(logonUser, e1);
//			act.setException(e1);
//		}
//	}
	
//	public void displacementCarOemSureInit() {
//		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
//		try {
//			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
//			
//			ActivitiesCamCost acc = new ActivitiesCamCost() ;
//			String areas = acc.getAreaStr(areaList) ;
//			
//			act.setOutData("areas", areas) ;
//			act.setOutData("opera", "确认") ;
//			act.setOutData("status", Constant.DisplancementCarrequ_cek_3) ;
//			act.setOutData("chkStatus", Constant.DisplancementCarrequ_cek_4) ;
//			
//			act.setForword(SURE_INIT) ;
//		} catch(Exception e) {
//			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "二手车确认初始化");
//			logger.error(logonUser, e1);
//			act.setException(e1);
//		}
//	}
	
//	public void displacementCarOemCliInit() {
//		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
//		try {
//			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
//			
//			ActivitiesCamCost acc = new ActivitiesCamCost() ;
//			String areas = acc.getAreaStr(areaList) ;
//			
//			act.setOutData("areas", areas) ;
//			act.setOutData("opera", "签收") ;
//			act.setOutData("status", Constant.DisplancementCarrequ_cek_5) ;
//			act.setOutData("chkStatus", Constant.DisplancementCarrequ_cek_SYBQS) ;
//			
//			act.setForword(CLI_INIT) ;
//		} catch(Exception e) {
//			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "二手车确认初始化");
//			logger.error(logonUser, e1);
//			act.setException(e1);
//		}
//	}
	
//	public void displacementCarDealerSureInit() {
//		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
//		try {
//			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
//			
//			ActivitiesCamCost acc = new ActivitiesCamCost() ;
//			String areas = acc.getAreaStr(areaList) ;
//			
//			act.setOutData("areas", areas) ;
//			act.setOutData("opera", "确认") ;
//			act.setOutData("status", Constant.DisplancementCarrequ_cek_4) ;
//			act.setOutData("chkStatus", Constant.DisplancementCarrequ_cek_4) ;
//			
//			act.setForword(DEALER_INIT) ;
//		} catch(Exception e) {
//			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "二手车确认初始化");
//			logger.error(logonUser, e1);
//			act.setException(e1);
//		}
//	}
	
//	public void oemQueryInit() {
//		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
//		try {
//			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
//			
//			ActivitiesCamCost acc = new ActivitiesCamCost() ;
//			String areas = acc.getAreaStr(areaList) ;
//			
//			act.setOutData("areas", areas) ;
//			
//			act.setForword(OEM_QUERY_INIT) ;
//		} catch(Exception e) {
//			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "二手车查询初始化");
//			logger.error(logonUser, e1);
//			act.setException(e1);
//		}
//	}
	
//	public void oemTotalInit() {
//		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
//		try {
//			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
//			
//			ActivitiesCamCost acc = new ActivitiesCamCost() ;
//			String areas = acc.getAreaStr(areaList) ;
//			
//			act.setOutData("areas", areas) ;		
//			act.setForword(OEM_TOTAL_INIT) ;
//		} catch(Exception e) {
//			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "二手车查询初始化");
//			logger.error(logonUser, e1);
//			act.setException(e1);
//		}
//	}
//	
//	public void dlrQueryInit() {
//		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
//		try {
//			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
//			
//			ActivitiesCamCost acc = new ActivitiesCamCost() ;
//			String areas = acc.getAreaStr(areaList) ;
//			
//			act.setOutData("areas", areas) ;
//			
//			act.setForword(DEALER_QUERY_INIT) ;
//		} catch(Exception e) {
//			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "二手车查询初始化");
//			logger.error(logonUser, e1);
//			act.setException(e1);
//		}
//	}
	
	public void displacementCarChkQuery() {
		AclUserBean logonUser = null;
		try{
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String dutyType = logonUser.getDutyType() ;
			String orgId = null ;
			String disNo = CommonUtils.checkNull(request.getParamValue("disNo")) ;
			String status = CommonUtils.checkNull(request.getParamValue("status")) ;
			String type = CommonUtils.checkNull(request.getParamValue("type")) ;
			String region = CommonUtils.checkNull(request.getParamValue("region")) ;
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId")) ;
			String base = CommonUtils.checkNull(request.getParamValue("base")) ;
			String newVin = CommonUtils.checkNull(request.getParamValue("newVin")) ;
			String oldVin = CommonUtils.checkNull(request.getParamValue("oldVin")) ;
			String clientName = CommonUtils.checkNull(request.getParamValue("clientName")) ;
			String areas = CommonUtils.checkNull(request.getParamValue("areas")) ;
			String startDate =  CommonUtils.checkNull(request.getParamValue("startDate")) ;
			String endDate =  CommonUtils.checkNull(request.getParamValue("endDate")) ;
			
			if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)) {
				orgId = logonUser.getOrgId().toString() ;
			}
			
			Map<String, String> map = new HashMap<String, String>() ;
			
			map.put("orgId", orgId) ;
			map.put("disNo", disNo) ;
			map.put("status", status) ;
			map.put("type", type) ;
			map.put("region", region) ;
			map.put("dealerId", dealerId) ;
			map.put("base", base) ;
			map.put("newVin", newVin) ;
			map.put("oldVin", oldVin) ;
			map.put("clientName", clientName) ;
			map.put("areas", areas) ;
			map.put("startDate", startDate);
			map.put("endDate", endDate);
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			
			PageResult<Map<String, Object>> ps = dao.headInfoQuery(map, Constant.PAGE_SIZE, curPage) ;
			
			act.setOutData("ps", ps);
		} catch(Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "二手车置换查询异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void displacementCarDealerChkQuery() {
		AclUserBean logonUser = null;
		try{
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String dutyType = logonUser.getDutyType() ;
			String orgId = null ;
			String disNo = CommonUtils.checkNull(request.getParamValue("disNo")) ;
			String status = CommonUtils.checkNull(request.getParamValue("status")) ;
			String type = CommonUtils.checkNull(request.getParamValue("type")) ;
			String region = CommonUtils.checkNull(request.getParamValue("region")) ;
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId")) ;
			String base = CommonUtils.checkNull(request.getParamValue("base")) ;
			String newVin = CommonUtils.checkNull(request.getParamValue("newVin")) ;
			String oldVin = CommonUtils.checkNull(request.getParamValue("oldVin")) ;
			String areas = CommonUtils.checkNull(request.getParamValue("areas")) ;
			
			if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)) {
				orgId = logonUser.getOrgId().toString() ;
			}
			dealerId = this.sureDealerByPose();
			
			Map<String, String> map = new HashMap<String, String>() ;
			
			map.put("orgId", orgId) ;
			map.put("disNo", disNo) ;
			map.put("status", status) ;
			map.put("type", type) ;
			map.put("region", region) ;
			map.put("dealerId", dealerId) ;
			map.put("base", base) ;
			map.put("newVin", newVin) ;
			map.put("oldVin", oldVin) ;
			map.put("areas", areas) ;
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			
			PageResult<Map<String, Object>> ps = dao.headInfoDealerQuery(map, Constant.PAGE_SIZE, curPage) ;
			
			act.setOutData("ps", ps);
		} catch(Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "二手车置换查询异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void displacementCarDealerQuery() {
		AclUserBean logonUser = null;
		try{
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String disNo = CommonUtils.checkNull(request.getParamValue("disNo")) ;
			String status = CommonUtils.checkNull(request.getParamValue("status")) ;
			String type = CommonUtils.checkNull(request.getParamValue("type")) ;
			String region = CommonUtils.checkNull(request.getParamValue("region")) ;
			String base = CommonUtils.checkNull(request.getParamValue("base")) ;
			String newVin = CommonUtils.checkNull(request.getParamValue("newVin")) ;
			String oldVin = CommonUtils.checkNull(request.getParamValue("oldVin")) ;
			String areas = CommonUtils.checkNull(request.getParamValue("areas")) ;
			
			String dealerId = this.sureDealerByPose();
			
			Map<String, String> map = new HashMap<String, String>() ;
			
			map.put("disNo", disNo) ;
			map.put("status", status) ;
			map.put("type", type) ;
			map.put("region", region) ;
			map.put("dealerId", dealerId) ;
			map.put("base", base) ;
			map.put("newVin", newVin) ;
			map.put("oldVin", oldVin) ;
			map.put("areas", areas) ;
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			
			PageResult<Map<String, Object>> ps = dao.headInfoDealerQuery(map, Constant.PAGE_SIZE, curPage) ;
			
			act.setOutData("ps", ps);
		} catch(Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "二手车置换查询异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	public void infoDetailQuery() {
		AclUserBean logonUser = null;
		try{
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			
			String vehicleId = CommonUtils.checkNull(request.getParamValue("vehicle_id")) ;
			String disId = CommonUtils.checkNull(request.getParamValue("dis_id")) ;
			String chkStatus = CommonUtils.checkNull(request.getParamValue("chkStatus")) ;
			
			Map<String, Object> vehMap = dao.getDisplanceMentInfo(vehicleId) ;
			Map<String, Object> disMap = dao.getDisInfo(disId) ;
			List<Map<String,Object>> logList = dao.getCheckLog(disId) ;
			
			List<Map<String, Object>> attachList = ActivitiesPlanManageDao.getInstance().getAttachInfos(disId);
			if(null!=attachList&&attachList.size()!=0){
				act.setOutData("attachList", attachList);
			}
			
			act.setOutData("logList", logList) ;
			act.setOutData("vehMap", vehMap) ;
			act.setOutData("disMap", disMap) ;
			act.setOutData("chkStatus", chkStatus) ;
			
			act.setForword(DETAIL_INIT) ;
		} catch(Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "二手车置换明细页面查询异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void infoDetailNewQuery() {
		AclUserBean logonUser = null;
		try{
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			
			String vehicleId = CommonUtils.checkNull(request.getParamValue("vehicle_id")) ;
			String disId = CommonUtils.checkNull(request.getParamValue("dis_id")) ;
			String chkStatus = CommonUtils.checkNull(request.getParamValue("chkStatus")) ;
			
			Map<String, Object> vehMap = dao.getDisplanceMentInfo(vehicleId) ;
			Map<String, Object> disMap = dao.getDisInfo(disId) ;
			List<Map<String,Object>> logList = dao.getCheckLog(disId) ;
			
			List<Map<String, Object>> chList = dao.getCheckIdByStatus(disId, Constant.DisplancementCarrequ_cek_5.toString()) ;
			
			int myLen = chList.size() ;
			String checkId = "00000000" ;  //如不存在则取此值，不能查处任何附件信息。
			if(myLen > 0) {
				checkId = chList.get(0).get("CHECK_ID").toString() ;
			}
			
			List<Map<String, Object>> attachList = ActivitiesPlanManageDao.getInstance().getAttachInfos(disId);
			List<Map<String, Object>> attachListNew = ActivitiesPlanManageDao.getInstance().getAttachInfos(checkId);
			if(null!=attachList&&attachList.size()!=0){
				act.setOutData("attachList", attachList);
			}
			if(null!=attachListNew&&attachListNew.size()!=0){
				act.setOutData("attachListNew", attachListNew);
			}
			
			act.setOutData("logList", logList) ;
			act.setOutData("vehMap", vehMap) ;
			act.setOutData("disMap", disMap) ;
			act.setOutData("chkStatus", chkStatus) ;
			
			act.setForword(DETAIL_CLI_INIT) ;
		} catch(Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "二手车置换明细页面查询异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void infoTheDetailQuery() {
		AclUserBean logonUser = null;
		try{
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			
			String vehicleId = CommonUtils.checkNull(request.getParamValue("vehicle_id")) ;
			String disId = CommonUtils.checkNull(request.getParamValue("dis_id")) ;
			
			Map<String, Object> vehMap = dao.getDisplanceMentInfo(vehicleId) ;
			Map<String, Object> disMap = dao.getDisInfo(disId) ;
			List<Map<String,Object>> logList = dao.getCheckLog(disId) ;
			
			List<Map<String, Object>> chList = dao.getCheckIdByStatus(disId, Constant.DisplancementCarrequ_cek_5.toString()) ;
			
			int myLen = chList.size() ;
			String checkId = "00000000" ;  //如不存在则取此值，不能查处任何附件信息。
			if(myLen > 0) {
				checkId = chList.get(0).get("CHECK_ID").toString() ;
			}
			
			List<Map<String, Object>> attachList = ActivitiesPlanManageDao.getInstance().getAttachInfos(disId);
			List<Map<String, Object>> attachListNew = ActivitiesPlanManageDao.getInstance().getAttachInfos(checkId);
			if(null!=attachList&&attachList.size()!=0){
				act.setOutData("attachList", attachList);
			}
			if(null!=attachListNew&&attachListNew.size()!=0){
				act.setOutData("attachListNew", attachListNew);
			}
			
			act.setOutData("logList", logList) ;
			act.setOutData("vehMap", vehMap) ;
			act.setOutData("disMap", disMap) ;
			
			act.setForword(THE_DETAIL_INIT) ;
		} catch(Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "二手车置换明细页面查询异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void infoDealerDetailQuery() {
		AclUserBean logonUser = null;
		try{
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			
			String vehicleId = CommonUtils.checkNull(request.getParamValue("vehicle_id")) ;
			String disId = CommonUtils.checkNull(request.getParamValue("dis_id")) ;
			String chkStatus = CommonUtils.checkNull(request.getParamValue("chkStatus")) ;
			
			Map<String, Object> vehMap = dao.getDisplanceMentInfo(vehicleId) ;
			Map<String, Object> disMap = dao.getDisInfo(disId) ;
			List<Map<String,Object>> logList = dao.getCheckLog(disId) ;
			
			List<Map<String, Object>> attachList = ActivitiesPlanManageDao.getInstance().getAttachInfos(disId);
			if(null!=attachList&&attachList.size()!=0){
				act.setOutData("attachList", attachList);
			}
			
			act.setOutData("logList", logList) ;
			act.setOutData("vehMap", vehMap) ;
			act.setOutData("disMap", disMap) ;
			act.setOutData("chkStatus", chkStatus) ;
			
			act.setForword(DEALER_DETAIL_INIT);
		} catch(Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "二手车置换明细页面查询异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void infoDealerDetailPrint() {
		AclUserBean logonUser = null;
		try{
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			
			String vehicleId = CommonUtils.checkNull(request.getParamValue("vehicle_id")) ;
			String disId = CommonUtils.checkNull(request.getParamValue("dis_id")) ;
			String chkStatus = CommonUtils.checkNull(request.getParamValue("chkStatus")) ;
			
			Map<String, Object> vehMap = dao.getDisplanceMentInfo(vehicleId) ;
			Map<String, Object> disMap = dao.getDisInfo(disId) ;
			List<Map<String,Object>> logList = dao.getCheckLog(disId) ;
			
			List<Map<String, Object>> attachList = ActivitiesPlanManageDao.getInstance().getAttachInfos(disId);
			if(null!=attachList&&attachList.size()!=0){
				act.setOutData("attachList", attachList);
			}
			
			act.setOutData("logList", logList) ;
			act.setOutData("vehMap", vehMap) ;
			act.setOutData("disMap", disMap) ;
			act.setOutData("chkStatus", chkStatus) ;
			
			act.setForword(DEALER_DETAIL_INIT_PRINT);
		} catch(Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "二手车置换明细页面查询异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void toCheck() {
		AclUserBean logonUser = null;
		try{
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			
			String disId = CommonUtils.checkNull(request.getParamValue("disId")) ;
			String chkStatus = CommonUtils.checkNull(request.getParamValue("chkStatus")) ;
			String opinion = CommonUtils.checkNull(request.getParamValue("opinion")) ;
			
			dao.statusUpdate(disId, chkStatus, logonUser.getUserId().toString()) ;			
			Map<String, String> map = new HashMap<String, String>();		
			map.put("opinion", opinion);
			map.put("disId", disId);
			map.put("status", chkStatus);
			map.put("userId", logonUser.getUserId().toString());		
			dao.checkLogInsert(map);	
				
			if(Long.parseLong(chkStatus) == Constant.DisplancementCarrequ_cek_RETRUN){
					dao.retSalesStatus(disId) ;
			}
			
			if(Long.parseLong(chkStatus) == Constant.DisplancementCarrequ_cek_SYBQS) {
				Map<String ,Object> disInfoList = dao.getDisInfo(disId) ;
				
				Long vehicleId = Long.parseLong(disInfoList.get("VEHICLE_ID").toString()) ;
				
				TmVehiclePO oldTvPO = new TmVehiclePO() ;
				oldTvPO.setVehicleId(vehicleId) ;
				TmVehiclePO newTvPO = new TmVehiclePO() ;
				newTvPO.setLockStatus(Constant.LOCK_STATUS_01) ; 
				
				dao.update(oldTvPO, newTvPO) ;
			}
			
			act.setForword(OPERA_INIT) ;
		} catch(Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "二手车置换审核异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void toChecks() {
		AclUserBean logonUser = null;
		try{
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			
			String[] disIds = request.getParamValues("disIds") ;
			String chkStatus = CommonUtils.checkNull(request.getParamValue("chkStatus")) ;
			String opinion = CommonUtils.checkNull(request.getParamValue("remark")) ;
			
			int len = disIds.length ;
			
			for(int i=0; i<len; i++) {
				dao.statusUpdate(disIds[i], chkStatus, logonUser.getUserId().toString()) ;			
				Map<String, String> map = new HashMap<String, String>();		
				map.put("opinion", opinion);
				map.put("disId", disIds[i]);
				map.put("status", chkStatus);
				map.put("userId", logonUser.getUserId().toString());		
				dao.checkLogInsert(map);		
				if(Long.parseLong(chkStatus) == Constant.DisplancementCarrequ_cek_RETRUN){
					dao.retSalesStatus(disIds[i]) ;
				}
				
				if(Long.parseLong(chkStatus) == Constant.DisplancementCarrequ_cek_SYBQS) {
					Map<String ,Object> disInfoList = dao.getDisInfo(disIds[i]) ;
					
					Long vehicleId = Long.parseLong(disInfoList.get("VEHICLE_ID").toString()) ;
					
					TmVehiclePO oldTvPO = new TmVehiclePO() ;
					oldTvPO.setVehicleId(vehicleId) ;
					TmVehiclePO newTvPO = new TmVehiclePO() ;
					newTvPO.setLockStatus(Constant.LOCK_STATUS_01) ; 
					
					dao.update(oldTvPO, newTvPO) ;
				}
			}
			// act.setForword(OPERA_INIT) ;
		} catch(Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "二手车置换审核异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void toDealerCheck() {
		AclUserBean logonUser = null;
		try{
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			
			String disId = CommonUtils.checkNull(request.getParamValue("disId")) ;
			String chkStatus = CommonUtils.checkNull(request.getParamValue("chkStatus")) ;
			String opinion = CommonUtils.checkNull(request.getParamValue("opinion")) ;
			String []fjids = request.getParamValues("fjid");
				
			dao.statusUpdate(disId, chkStatus, logonUser.getUserId().toString()) ;	
			
			Map<String, String> map = new HashMap<String, String>();		
			map.put("opinion", opinion);
			map.put("disId", disId);
			map.put("status", chkStatus);
			map.put("userId", logonUser.getUserId().toString());
			
			String chekId = dao.checkLogInsert(map);		
			dao.statusUpdate(disId, chkStatus, logonUser.getUserId().toString());
			FileUploadManager.fileUploadByBusiness(chekId, fjids, logonUser); //附件添加
			
			if(Long.parseLong(chkStatus) == Constant.DisplancementCarrequ_cek_RETRUN){
				dao.retSalesStatus(disId) ;
			}
			act.setForword(DEALER_INIT);	
		} catch(Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "二手车置换审核异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	@SuppressWarnings("unused")
	private String sureDealerByPose(){
		AclUserBean logonUser = null;
		String dealerIds__ = "";
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String warehouseType = act.getRequest().getParamValue("warehouseType") ;
			if (warehouseType == null || warehouseType.equals("")) {
				warehouseType = "" ;
			}
			Long poseId = logonUser.getPoseId();
			Long comId = logonUser.getCompanyId() ;
			List<Map<String, Object>> areaList = MaterialGroupManagerDao
					.getDealerId(comId.toString(),poseId.toString());
			
			for(int i=0; i<areaList.size();i++) {
				dealerIds__ += areaList.get(i).get("DEALER_ID").toString()+"," ;
			}
			if (!"".equals(dealerIds__)) {
				dealerIds__ = dealerIds__.substring(0,(dealerIds__.length()-1)) ;
			} else {
				dealerIds__ = "" ;
			}
			 
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "当前经销商用户仓库查询失败");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
		return dealerIds__;
	}
	
	public void AnalyseExclel_cash(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1; // 处理当前页

		String zige = CommonUtils.checkNull(request.getParamValue("zige")); //置换资格
		String type = CommonUtils.checkNull(request.getParamValue("type")); //置换类型
		String status = CommonUtils.checkNull(request.getParamValue("status")); //置换状态
		String base = CommonUtils.checkNull(request.getParamValue("base")); //生产基地
		String region = CommonUtils.checkNull(request.getParamValue("region")); //省份
		String areas = CommonUtils.checkNull(request.getParamValue("areas")) ;
		String startDate =CommonUtils.checkNull(request.getParamValue("startDate")) ;
		String endDate =CommonUtils.checkNull(request.getParamValue("endDate")) ;
		
		String signInStartDate = CommonUtils.checkNull(request.getParamValue("signInStartDate")) ; //签约时间
		String signInEndDate = CommonUtils.checkNull(request.getParamValue("signInEndDate")) ; //签约时间
		
	    StringBuffer con = new StringBuffer();	
	    //资格
		if (zige != null && !"".equals(zige)) {
			con.append("  and cdd.displacement_prc = ").append(zige).append("\n"); 
		}			    
		//类型
		if (type != null && !"".equals(type)) {
			con.append("   and tvu.displacement_type = ").append(type).append("\n");
		}	
		//状态
		if (status != null && !"".equals(status)) {
			con.append("   and tvu.operate_status = ").append(status).append("\n");
		}	
		//生产基地
		if (base != null && !"".equals(base)) {
			con.append("   and tba.produce_base = ").append(base).append("\n");
		}	
	    //省份
		if (region != null && !"".equals(region)) {
			con.append("   and tmd.province_id = ").append(region).append("\n");
		}
		//渠道
		if (areas != null && !"".equals(areas)) {
			con.append("   and tba.area_id in (").append(areas).append(")\n");
		}
		
		if (startDate != null && !"".equals(startDate)) {
			con.append("   and trunc(tvu.create_date) >= to_date('").append(startDate).append("', 'yyyy-mm-dd')\n");
		}
		
		if (endDate != null && !"".equals(endDate)) {
			con.append("   and trunc(tvu.create_date) <= to_date('").append(endDate).append("', 'yyyy-mm-dd')\n");
		}
		
		con.append("and exists\n");
		con.append("         (select 1\n");  
		con.append("                  from tt_vs_car_displacement_cek tvcdc\n");  
		con.append("                 where tvcdc.displacement_id = tvu.displacement_id\n");  
		con.append("                   and tvcdc.status = 80201006\n");  
		
		if(signInStartDate != null && !"".equals(signInStartDate)) {
			con.append("                   and tvcdc.create_date >=\n");  
			con.append("               to_date('").append(signInStartDate).append(" 000000").append("', 'yyyy-mm-dd hh24:mi:ss')\n");  
		}
		if(signInEndDate != null && !"".equals(signInEndDate)) {
			con.append("                   and tvcdc.create_date <=\n");  
			con.append("               to_date('").append(signInEndDate).append(" 235959").append("', 'yyyy-mm-dd hh24:mi:ss')");
		}
		con.append(")\n");
		
		List<Map<String,Object>> list = dao.CashTotalQuery(con.toString());
		List total_list = new ArrayList();
		int total_AMOUNT = 0;
		int total_PRICE = 0;
		int total_PRICE_AMOUNT = 0;
		for(int i = 0; i<list.size();i++) {
			Map map = (Map)list.get(i);
			total_AMOUNT = total_AMOUNT + Integer.parseInt(map.get("AMOUNT").toString());
			total_PRICE = total_PRICE + Integer.parseInt(map.get("PRICE").toString());
			total_PRICE_AMOUNT = total_PRICE_AMOUNT + Integer.parseInt(map.get("PRICE_AMOUNT").toString());
		}
		total_list.add(String.valueOf(total_AMOUNT));
		total_list.add(String.valueOf(total_PRICE));
		total_list.add(String.valueOf(total_PRICE_AMOUNT));
		
		 List list1 = new ArrayList();
		    if(list != null&&list.size()!=0){
				for(int i=0;i<list.size();i++){
			    	Map map =(Map)list.get(i);
			    	if(map!=null&&map.size()!=0){
						String[]detail=new String[6];
							detail[0] = String.valueOf(map.get("REGION_NAME"));
							detail[1] = String.valueOf(map.get("ROOT_DEALER_NAME"));
							detail[2] = String.valueOf(map.get("DEALER_NAME"));
							detail[3] = String.valueOf(map.get("AMOUNT"));
							detail[4] = String.valueOf(map.get("PRICE"));
							detail[5] = String.valueOf(map.get("PRICE_AMOUNT"));	
							list1.add(detail);					
			    	}
				}
		    }
		String head[] = new String[6];
		head[0]="省份";
		head[1]="上级单位";
		head[2]="经销商名称";
		head[3]="数量";
		head[4]="单车支持政策(元/辆)";
		head[5]="支持金额(元)";
		
		try {
			toCashExcel(ActionContext.getContext().getResponse(), request, head, list1, total_list ,"二手车置换(OEM)兑现汇总.xls");
		} catch (Exception e) {
			e.printStackTrace();
		}	      
	}

	public void AnalyseExclel_detail(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1; // 处理当前页

		String zige = CommonUtils.checkNull(request.getParamValue("zige")); //置换资格
		String type = CommonUtils.checkNull(request.getParamValue("type")); //置换类型
		String status = CommonUtils.checkNull(request.getParamValue("status")); //置换状态
		String base = CommonUtils.checkNull(request.getParamValue("base")); //生产基地
		String region = CommonUtils.checkNull(request.getParamValue("region")); //省份
		String areas = CommonUtils.checkNull(request.getParamValue("areas")) ;
		String startDate =CommonUtils.checkNull(request.getParamValue("startDate")) ;
		String endDate =CommonUtils.checkNull(request.getParamValue("endDate")) ;
		
		String signInStartDate = CommonUtils.checkNull(request.getParamValue("signInStartDate")); //签约起始时间
		String signInEndDate = CommonUtils.checkNull(request.getParamValue("signInEndDate")); //签约起始时间
	    StringBuffer con = new StringBuffer();	
	    //资格
		if (zige != null && !"".equals(zige)) {
			con.append("  and cdd.displacement_prc = ").append(zige).append("\n"); 
		}			    
		//类型
		if (type != null && !"".equals(type)) {
			con.append("   and tvu.displacement_type = ").append(type).append("\n");
		}	
		//状态
		if(status != null && !"".equals(status)) {
			con.append("	and tvu.operate_status = ").append(status).append("\n");
		}
		//生产基地
		if (base != null && !"".equals(base)) {
			con.append("   and tba.produce_base = ").append(base).append("\n");
		}	
	    //省份
		if (region != null && !"".equals(region)) {
			con.append("   and tmd.province_id = ").append(region).append("\n");
		}
		//渠道
		if (areas != null && !"".equals(areas)) {
			con.append("   and tba.area_id in (").append(areas).append(")\n");
		}
		
		if (startDate != null && !"".equals(startDate)) {
			con.append("   and trunc(tvu.create_date) >= to_date('").append(startDate).append("', 'yyyy-mm-dd')\n");
		}
		
		if (endDate != null && !"".equals(endDate)) {
			con.append("   and trunc(tvu.create_date) <= to_date('").append(endDate).append("', 'yyyy-mm-dd')\n");
		}
		
		con.append("and exists\n");
		con.append("         (select 1\n");  
		con.append("                  from tt_vs_car_displacement_cek tvcdc\n");  
		con.append("                 where tvcdc.displacement_id = tvu.displacement_id\n");  
		con.append("                   and tvcdc.status = 80201006\n");  
		
		if(signInStartDate != null && !"".equals(signInStartDate)) {
			con.append("                   and tvcdc.create_date >=\n");  
			con.append("               to_date('").append(signInStartDate).append(" 000000").append("', 'yyyy-mm-dd hh24:mi:ss')\n");  
		}
		if(signInEndDate != null && !"".equals(signInEndDate)) {
			con.append("                   and tvcdc.create_date <=\n");  
			con.append("               to_date('").append(signInEndDate).append(" 235959").append("', 'yyyy-mm-dd hh24:mi:ss')");
		}
		con.append(")\n");
		
		
		
		List<Map<String,Object>> list = dao.DetailTotalQuery(con.toString());
		
		List list1 = new ArrayList();
	    if(list != null&&list.size()!=0){
			for(int i=0;i<list.size();i++){
		    	Map map =(Map)list.get(i);
		    	if(map!=null&&map.size()!=0){
					String[]detail=new String[15];
						detail[0] = String.valueOf(map.get("ROWNUM"));
						detail[1] = String.valueOf(map.get("ROOT_ORG_CODE"));
						detail[2] = String.valueOf(map.get("REGION_NAME"));
						detail[3] = String.valueOf(map.get("ROOT_DEALER_NAME"));
						detail[4] = String.valueOf(map.get("CTM_NAME"));
						detail[5] = String.valueOf(map.get("CARD_NUM"));
						detail[6] = String.valueOf(map.get("LICENSE_NO"));
						detail[7] = String.valueOf(map.get("OLD_VIN"));
						detail[8] = String.valueOf(map.get("SCRAP_DATE"));
						detail[9] = String.valueOf(map.get("SCRAP_CERTIFY_NO"));
						detail[10] = String.valueOf(map.get("PURCHASED_DATE"));
						detail[11] = String.valueOf(map.get("PRODUCE_BASE"));
						detail[12] = String.valueOf(map.get("GROUP_NAME"));
						detail[13] = String.valueOf(map.get("NEW_VIN"));
						detail[14] = String.valueOf(map.get("PRICE"));		
						list1.add(detail);					
		    	}
			}
	    }
		
		try {
			toDetailExcel(ActionContext.getContext().getResponse(), request, list1 ,"二手车置换(OEM)明细汇总.xls");
		} catch (Exception e) {
			e.printStackTrace();
		}	      
	}

	
	public Object toCashExcel(ResponseWrapper response,
			RequestWrapper request, String[] head, List<String[]> list,List<String> total_list,String name)
			throws Exception {

		jxl.write.WritableWorkbook wwb = null;
		OutputStream out = null;
		try {
			response.setContentType("application/octet-stream");
		    response.addHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(name, "utf-8"));
			out = response.getOutputStream();
			wwb = Workbook.createWorkbook(out);
			jxl.write.WritableSheet ws = wwb.createSheet("兑现表", 0);
			
			WritableFont wf = new WritableFont(WritableFont.ARIAL, 11,
			WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE,
			jxl.format.Colour.BLACK); // 定义格式 字体 下划线 斜体 粗体 颜色
			
			WritableCellFormat wcf = new WritableCellFormat(wf); // 单元格定义
			
			if (head != null && head.length > 0) {
				for (int i = 0; i < head.length; i++) {
					ws.addCell(new Label(i, 0, head[i],wcf));
				}
			}
			int counts=0;
			int flag=0;
			for (int z = 1; z < list.size() + 1; z++) {
				int pageSizess=z/30000;
				counts++;
				if(pageSizess>flag){
					counts=1;
				}
				flag=pageSizess;
				pageSizess=pageSizess<1?0:pageSizess;
				pageSizess=(new BigDecimal(pageSizess).setScale(0, BigDecimal.ROUND_HALF_UP)).intValue();
				String[] str = list.get(z - 1);
				for (int i = 0; i < str.length; i++) {
					ws.addCell(new Label(i, z, str[i]));
				}
			}
			ws.addCell(new Label(0, list.size()+1, "合计"));
			ws.mergeCells(0,list.size()+1,2,list.size()+1);
			
			ws.addCell(new Label(3, list.size()+1, total_list.get(0)));
			ws.addCell(new Label(4, list.size()+1, total_list.get(1)));
			ws.addCell(new Label(5, list.size()+1, total_list.get(2)));
			
			wwb.write();
			out.flush();	
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (null != wwb) {
				wwb.close();
			}
			if (null != out) {
				out.close();
			}
		}
		return null;
	}
	
	public Object toDetailExcel(ResponseWrapper response,
			RequestWrapper request, List list,String name)
			throws Exception {

		jxl.write.WritableWorkbook wwb = null;
		OutputStream out = null;
		try {
			response.setContentType("application/octet-stream");
		    response.addHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(name, "utf-8"));
			out = response.getOutputStream();
			wwb = Workbook.createWorkbook(out);
			jxl.write.WritableSheet ws = wwb.createSheet("明细表", 0);
			WritableFont wf = new WritableFont(WritableFont.ARIAL, 11,
			WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE,
			jxl.format.Colour.BLACK); // 定义格式 字体 下划线 斜体 粗体 颜色
					
		    WritableCellFormat wcf = new WritableCellFormat(wf); // 单元格定义
		    wcf.setAlignment(jxl.format.Alignment.CENTRE);
		    
		    ws.addCell(new Label(0, 0, "序号",wcf));
		    ws.mergeCells(0,0,0,2);
		    
		    ws.addCell(new Label(1, 0, "事业部",wcf));
		    ws.mergeCells(1,0,1,2);
			 
		    ws.addCell(new Label(2, 0, "省份",wcf));
		    ws.mergeCells(2,0,2,2);
		    
		    ws.addCell(new Label(3, 0, "经销商名称(全称)",wcf));
		    ws.mergeCells(3,0,3,2);
		    
		    ws.addCell(new Label(4, 0, "车主姓名/单位名称",wcf));
		    ws.mergeCells(4,0,4,2);
		    
		    ws.addCell(new Label(5, 0, "身份证号码/组织机构代码号",wcf));
		    ws.mergeCells(5,0,5,2);
		    
		    ws.addCell(new Label(6, 0, "旧车信息",wcf));
		    ws.mergeCells(6,0,9,0);
		    
		    ws.addCell(new Label(6, 1, "车辆品牌及型号",wcf));
		    ws.mergeCells(6,1,6,2);
		    
		    ws.addCell(new Label(7, 1, "车辆底盘号",wcf));
		    ws.mergeCells(7,1,7,2);
		    
		    ws.addCell(new Label(8, 1, "报废车辆",wcf));
		    ws.mergeCells(8,1,9,1);
		    
		    ws.addCell(new Label(8, 2, "车辆报废时间（即：《机动车注销证明》办理完毕的时间）",wcf));
		    
		    ws.addCell(new Label(9, 2, "《报废汽车回收证明》编号",wcf));
		    
		    ws.addCell(new Label(10, 0, "新车信息",wcf));
		    ws.mergeCells(10,0,13,0);
		    
		    ws.addCell(new Label(10, 1, "购车时间",wcf));
		    ws.mergeCells(10,1,10,2);
		    ws.addCell(new Label(11, 1, "生产基地",wcf));
		    ws.mergeCells(11,1,11,2);
		    
		    ws.addCell(new Label(12, 1, "车辆型号",wcf));
		    ws.mergeCells(12,1,12,2);
		    
		    ws.addCell(new Label(13, 1, "底盘号",wcf));
		    ws.mergeCells(13,1,13,2);
		    
		    ws.addCell(new Label(14, 0, "支持政策（元）",wcf));
		    ws.mergeCells(14,0,14,2);
		    
		    int line = 3 ;
		    for(int i =0;i<list.size();i++){
		    	 String [] str = (String [])list.get(i);
		    	 for(int j = 0;j<str.length;j++){
		    		 ws.addCell(new Label(j, line, str[j])); 
		    	 }
		    	 ++line;
		    }    
		    
			wwb.write();
			out.flush();	
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (null != wwb) {
				wwb.close();
			}
			if (null != out) {
				out.close();
			}
		}
		return null;
	}

}
