package com.infodms.dms.actions.report.reportOne;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.repairOrder.RoMaintainMain;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.ActMater;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.common.relation.DealerRelation;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.dao.report.jcafterservicereport.SpecialCostReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmOrgPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;

public class SalesReport {
	private Logger logger = Logger.getLogger(RoMaintainMain.class);
	ActionContext act = ActionContext.getContext();
	private RequestWrapper request = act.getRequest();
	private ResponseWrapper response = act. getResponse();
	AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	SpecialCostReportDao dao = new SpecialCostReportDao();
	private final String BillReportUrl   = "/jsp/report/billTicket.jsp";
	private final String ActReportUrl   = "/jsp/report/actSales.jsp";
	private final String BillDetailReportUrl   = "/jsp/report/billDetailTicket.jsp";
	private final String ActDetailReportUrl   = "/jsp/report/actDetailSales.jsp";
	private final String BillDealerDetailReportUrl   = "/jsp/report/dealerBillDetaliTicket.jsp";
	private final String ActDealerDetailReportUrl   = "/jsp/report/dealerActDetaliSales.jsp";
	private final String ActDealerReportUrl   = "/jsp/report/actDealerSales.jsp";
	private final String BillDealerReportUrl   = "/jsp/report/billDealerTicket.jsp";
	private final String FleetSalesUrl="/jsp/report/fleetSalesCvs.jsp";
	private final String FleetSalesLogUrl="/jsp/report/fleetSalesLog.jsp";
	private final String ActualSalesLogReportUrl="/jsp/report/fleetSalesLogReport.jsp";
	private final String FleetPreparCvsUrl="/jsp/report/fleetPreparCvs.jsp";
	private final String FleetFollowUrl="/jsp/report/fleetFollow.jsp";
	private final String FleetContractUrl="/jsp/report/fleetContract.jsp";
	private final String FleeActTjUrl="/jsp/report/fleetActTj.jsp";
	private final String ActualSalesReportUrl = "/jsp/report/fleetSalesCvsReport.jsp";
	
	//常规订单超时统计汇总表
	private final String OrderTimeoutStatisticsSummaryUrl = "/jsp/report/orderTimeoutStatisticsSummary.jsp";
	private final String OrderTimeoutStatisticsSummaryReportUrl = "/jsp/report/orderTimeoutStatisticsSummaryReport.jsp";
	//常规订单超时统计明细
	private final String OrderTimeoutStatisticsDetailUrl = "/jsp/report/orderTimeoutStatisticsDetail.jsp";
	//常规订单资源延时满足明细表
	private final String OrderDelayDetailUrl = "/jsp/report/orderDelayDetail.jsp";
	//常规订单资源延时满足明细表
	private final String OrderDelaySummaryUrl = "/jsp/report/orderDelaySummary.jsp";
	
	
	public void getBillTicketReport(){
		try {
			Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(poseId.toString());
			areaList.size();
			String orgId=logonUser.getOrgId().toString();
			TmOrgPO po=new TmOrgPO();
			po.setOrgId(Long.parseLong(orgId));
			po.setDutyType(Constant.DUTY_TYPE_LARGEREGION);
			if(dao.select(po).size()==1){
				act.setOutData("orgId", orgId);
			}
			act.setOutData("areaList", areaList);
			Date date_ = new Date(System.currentTimeMillis());
			Date date1 = new Date(date_.getYear(),date_.getMonth(),01);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String date = dateFormat.format(date_);
			String date2 = dateFormat.format(date1);
			act.setOutData("date", date);
			act.setOutData("date2", date2);
			act.setForword(BillReportUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act, e,
			ErrorCodeConstant.QUERY_FAILURE_CODE, "报表有误！");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	public void getActSalesReport(){
		try {
			Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(poseId.toString());
			areaList.size();
			String orgId=logonUser.getOrgId().toString();
			TmOrgPO po=new TmOrgPO();
			po.setOrgId(Long.parseLong(orgId));
			po.setDutyType(Constant.DUTY_TYPE_LARGEREGION);
			if(dao.select(po).size()==1){
				act.setOutData("orgId", orgId);
			}
			act.setOutData("areaList", areaList);
			Date date_ = new Date(System.currentTimeMillis());
			Date date1 = new Date(date_.getYear(),date_.getMonth(),01);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String date = dateFormat.format(date_);
			String date2 = dateFormat.format(date1);
			act.setOutData("date", date);
			act.setOutData("date2", date2);
			act.setForword(ActReportUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act, e,
			ErrorCodeConstant.QUERY_FAILURE_CODE, "报表有误！");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void getBillMater(){
		try {
			RequestWrapper request = act.getRequest();
			String areaId=request.getParamValue("area_id");
			if(areaId!="" && areaId!=null){
			List<ActMater> maters=dao.selectAllMaters(areaId);
			StringBuffer mat=new StringBuffer("");
			for(int i=0;i<maters.size();i++){
				if(maters.get(i).getMaterId()!=null){
 				String [] a=maters.get(i).getMaterId().split(",");
				for(int j=0;j<a.length;j++){
					mat.append(a[j]);
					if(i==(maters.size()-1)&&j==(a.length-1)){
					}else{
						mat.append(",");
					}
				}
				}
			}
			act.setOutData("mat", mat.toString());
			}else{
				act.setOutData("mat", 1);
			}
		}catch (Exception e) {
			BizException e1 = new BizException(act, e,
			ErrorCodeConstant.QUERY_FAILURE_CODE, "报表有误！");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void getDealerBillTicketReport(){
		try {
			StringBuffer str=new StringBuffer("");
			Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(poseId.toString());
			areaList.size();
			String dealerId=logonUser.getDealerId();
			//List<Map<String, Object>> dealerIds=dao.selectAllDealers(dealerId);
			
			DealerRelation dr = new DealerRelation() ;
			List<Map<String, Object>> dealerIds = dr.getDealerPoseRelation(logonUser.getCompanyId(), poseId) ;
			for(int i=0;i<dealerIds.size();i++){
				if(str.length() == 0) {
					str.append(dealerIds.get(i).get("DEALER_ID"));
				} else {
					str.append(",").append(dealerIds.get(i).get("DEALER_ID"));
				}
			}
			if(dao.selectDealers(dealerId).size()>0){
				//str.append(dealerId);
//				Map map=dao.selectDealers(dealerId).get(0);
//				String a=map.get("DEALER_ID").toString();
//				List map1=dao.selectMyDealers(a);
//				//String b=map1.get("ROOT_DEALER_ID").toString();
//				if(map1!=null && map1.size()>0){
//					act.setOutData("dealerId", dealerId);
//				}else{
					act.setOutData("mydealerId", str);
				//}
			}
			
			Date date_ = new Date(System.currentTimeMillis());
			Date date1 = new Date(date_.getYear(),date_.getMonth(),01);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String date = dateFormat.format(date_);
			String date2 = dateFormat.format(date1);
			act.setOutData("date", date);
			act.setOutData("date2", date2);
			
			act.setOutData("areaList", areaList);
			act.setForword(BillDealerReportUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act, e,
			ErrorCodeConstant.QUERY_FAILURE_CODE, "报表有误！");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	public void getDealerActSalesReport(){
		try {
			
			Date date_ = new Date(System.currentTimeMillis());
			Date date1 = new Date(date_.getYear(),date_.getMonth(),01);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String date = dateFormat.format(date_);
			String date2 = dateFormat.format(date1);
			act.setOutData("date", date);
			act.setOutData("date2", date2);
			
			StringBuffer str=new StringBuffer("");
			Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(poseId.toString());
			areaList.size();
			String dealerId=logonUser.getDealerId();
			
			List<Map<String, Object>> dealerIds=dao.selectAllDealers(dealerId);
			for(int i=0;i<dealerIds.size();i++){
				str.append(dealerIds.get(i).get("DEALER_ID"));
				str.append(",");
			}
			if(dao.selectDealers(dealerId).size()>0){
				str.append(dealerId);
//				Map map=dao.selectDealers(dealerId).get(0);
//				String a=map.get("DEALER_ID").toString();
//				List map1=dao.selectMyDealers(a);
//				//String b=map1.get("ROOT_DEALER_ID").toString();
//				if(map1!=null && map1.size()>0){
//					act.setOutData("dealerId", dealerId);
//				}else{
					act.setOutData("mydealerId", str);
				//}
			}
			act.setOutData("areaList", areaList);
			act.setForword(ActDealerReportUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act, e,
			ErrorCodeConstant.QUERY_FAILURE_CODE, "报表有误！");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	public void getFleetSales(){
		try {
			List<Map<String, Object>> produceAddList=MaterialGroupManagerDao.getProduceAdd();
			act.setOutData("produceAddList", produceAddList);
			act.setForword(FleetSalesUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act, e,
			ErrorCodeConstant.QUERY_FAILURE_CODE, "报表有误！");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void getFleetFollow(){
		try {
			Date date_ = new Date(System.currentTimeMillis());
			Date date1 = new Date(date_.getYear(),date_.getMonth(),01);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String date = dateFormat.format(date_);
			String date2 = dateFormat.format(date1);
			act.setOutData("date", date);
			act.setOutData("date2", date2);
			act.setForword(FleetFollowUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act, e,
			ErrorCodeConstant.QUERY_FAILURE_CODE, "报表有误！");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	public void getFleetContract(){
		try {
			Date date_ = new Date(System.currentTimeMillis());
			Date date1 = new Date(date_.getYear(),date_.getMonth(),01);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String date = dateFormat.format(date_);
			String date2 = dateFormat.format(date1);
			act.setOutData("date", date);
			act.setOutData("date2", date2);
			act.setForword(FleetContractUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act, e,
			ErrorCodeConstant.QUERY_FAILURE_CODE, "报表有误！");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	
	
	public void getFleetSalesLog(){
		try {
			List<Map<String, Object>> produceAddList=MaterialGroupManagerDao.getProduceAdd();
			act.setOutData("produceAddList", produceAddList);
			act.setForword(FleetSalesLogUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act, e,
			ErrorCodeConstant.QUERY_FAILURE_CODE, "报表有误！");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	public void getFleetPrepar(){
		try {
			Date date_ = new Date(System.currentTimeMillis());
			Date date1 = new Date(date_.getYear(),date_.getMonth(),01);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String date = dateFormat.format(date_);
			String date2 = dateFormat.format(date1);
			act.setOutData("date", date);
			act.setOutData("date2", date2);
			act.setForword(FleetPreparCvsUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act, e,
			ErrorCodeConstant.QUERY_FAILURE_CODE, "报表有误！");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	public void getActDetaliSales(){
		try {
			StringBuffer str=new StringBuffer("");
			String dealerId=logonUser.getDealerId();
			List<Map<String, Object>> dealerIds=dao.selectAllDealers(dealerId);
			for(int i=0;i<dealerIds.size();i++){
				str.append(dealerIds.get(i).get("DEALER_ID"));
				str.append(",");
			}
			if(dao.selectDealers(dealerId).size()>0){
				str.append(dealerId);
					act.setOutData("mydealerId", str);
			}
			
			
			String orgId=logonUser.getOrgId().toString();
			TmOrgPO po=new TmOrgPO();
			po.setOrgId(Long.parseLong(orgId));
			po.setDutyType(Constant.DUTY_TYPE_LARGEREGION);
			if(dao.select(po).size()==1){
				act.setOutData("orgId", orgId);
			}
			Date date_ = new Date(System.currentTimeMillis());
			Date date1 = new Date(date_.getYear(),date_.getMonth(),01);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String date = dateFormat.format(date_);
			String date2 = dateFormat.format(date1);
			act.setOutData("date", date);
			act.setOutData("date2", date2);
			Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(poseId.toString());
			act.setOutData("areaList",areaList);
			act.setForword(ActDetailReportUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act, e,
			ErrorCodeConstant.QUERY_FAILURE_CODE, "报表有误！");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	public void getBillDetaliTicket(){
		try {
			
			String orgId=logonUser.getOrgId().toString();
			TmOrgPO po=new TmOrgPO();
			po.setOrgId(Long.parseLong(orgId));
			po.setDutyType(Constant.DUTY_TYPE_LARGEREGION);
			if(dao.select(po).size()==1){
				act.setOutData("orgId", orgId);
			}
			Date date_ = new Date(System.currentTimeMillis());
			Date date1 = new Date(date_.getYear(),date_.getMonth(),01);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String date = dateFormat.format(date_);
			String date2 = dateFormat.format(date1);
			act.setOutData("date", date);
			act.setOutData("date2", date2);
			Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(poseId.toString());
			act.setOutData("areaList",areaList);
			act.setForword(BillDetailReportUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act, e,
			ErrorCodeConstant.QUERY_FAILURE_CODE, "报表有误！");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void getFleetActTj(){
		try {
			Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(poseId.toString());
			act.setOutData("areaList",areaList);
			Date date_ = new Date(System.currentTimeMillis());
			Date date1 = new Date(date_.getYear(),date_.getMonth(),01);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String date = dateFormat.format(date_);
			String date2 = dateFormat.format(date1);
			act.setOutData("date", date);
			act.setOutData("date2", date2);
			act.setForword(FleeActTjUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act, e,
			ErrorCodeConstant.QUERY_FAILURE_CODE, "报表有误！");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void getDealerBillDetaliTicket(){
		
		Date date_ = new Date(System.currentTimeMillis());
		Date date1 = new Date(date_.getYear(),date_.getMonth(),01);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = dateFormat.format(date_);
		String date2 = dateFormat.format(date1);
		act.setOutData("date", date);
		act.setOutData("date2", date2);
		
		StringBuffer str=new StringBuffer("");
		String dealerId=logonUser.getDealerId();
		/*List<Map<String, Object>> dealerIds=dao.selectAllDealers(dealerId); //
		for(int i=0;i<dealerIds.size();i++){
			str.append(dealerIds.get(i).get("DEALER_ID"));
			str.append(",");
		}*/
		
		DealerRelation dr = new DealerRelation() ;
		List<Map<String, Object>> dealerIds = dr.getDealerPoseRelation(logonUser.getCompanyId(), logonUser.getPoseId()) ;
		for(int i=0;i<dealerIds.size();i++){
			if(str.length() == 0) {
				str.append(dealerIds.get(i).get("DEALER_ID"));
			} else {
				str.append(",").append(dealerIds.get(i).get("DEALER_ID"));
			}
		}
		
		if(dao.selectDealers(dealerId).size()>0){
			// str.append(dealerId);
				act.setOutData("mydealerId", str);
		}
		Long poseId = logonUser.getPoseId();
		List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(poseId.toString());
		act.setOutData("areaList",areaList);
		act.setForword(BillDealerDetailReportUrl);
	}
	
	public void getDealerActDetaliSales(){
		
		Date date_ = new Date(System.currentTimeMillis());
		Date date1 = new Date(date_.getYear(),date_.getMonth(),01);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = dateFormat.format(date_);
		String date2 = dateFormat.format(date1);
		act.setOutData("date", date);
		act.setOutData("date2", date2);
		
		StringBuffer str=new StringBuffer("");
		String dealerId=logonUser.getDealerId();
		List<Map<String, Object>> dealerIds=dao.selectAllDealers(dealerId);
		for(int i=0;i<dealerIds.size();i++){
			str.append(dealerIds.get(i).get("DEALER_ID"));
			str.append(",");
		}
		if(dao.selectDealers(dealerId).size()>0){
			str.append(dealerId);
				act.setOutData("mydealerId", str);
		}
		Long poseId = logonUser.getPoseId();
		List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(poseId.toString());
		act.setOutData("areaList",areaList);
		act.setForword(ActDealerDetailReportUrl);
	}
	
	/**
	 * 集团客户实销审核--查询
	 * */
	public void getFleetSalesReport(){
		try {
			String produce = CommonUtils.checkNull(request.getParamValue("produce"));
			String startTime = CommonUtils.checkNull(request.getParamValue("startTime"));
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			String orgCode = CommonUtils.checkNull(request.getParamValue("orgCode"));
			Map<String, String> map = new HashMap<String, String>();
			map.put("produce", produce);
			map.put("startTime", startTime);
			map.put("endTime", endTime);
			map.put("dealerCode", dealerCode);
			map.put("orgCode", orgCode);
			List<Map<String, Object>> reportList = dao.getActualSalesReport(map);
			act.setOutData("startTime", startTime);
			act.setOutData("endTime", endTime);
			act.setOutData("list_report", reportList);
			act.setForword(ActualSalesReportUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act, e,
			ErrorCodeConstant.QUERY_FAILURE_CODE, "报表有误！");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 集团客户实销--查询
	 * */
	public void getFleetSalesLogReport(){
		try {
			String fleetName = CommonUtils.checkNull(request.getParamValue("fleetName"));
			String startTime = CommonUtils.checkNull(request.getParamValue("startTime"));
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			String orgCode = CommonUtils.checkNull(request.getParamValue("orgCode"));
			Map<String, String> map = new HashMap<String, String>();
			map.put("fleetName", fleetName);
			map.put("startTime", startTime);
			map.put("endTime", endTime);
			map.put("dealerCode", dealerCode);
			map.put("orgCode", orgCode);
			List<Map<String, Object>> reportList = dao.getActualSalesLogReport(map);
			act.setOutData("startTime", startTime);
			act.setOutData("endTime", endTime);
			act.setOutData("list_report", reportList);
			act.setForword(ActualSalesLogReportUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act, e,
			ErrorCodeConstant.QUERY_FAILURE_CODE, "报表有误！");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 集团客户实销审核--下载
	 * */
	public void fleetSalesReportdownload(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String produce = CommonUtils.checkNull(request.getParamValue("produce"));
			String startTime = CommonUtils.checkNull(request.getParamValue("startTime"));
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			String orgCode = CommonUtils.checkNull(request.getParamValue("orgCode"));
			Map<String, String> map = new HashMap<String, String>();
			map.put("produce", produce);
			map.put("startTime", startTime);
			map.put("endTime", endTime);
			map.put("dealerCode", dealerCode);
			map.put("orgCode", orgCode);
			
			
			// 导出的文件名
			String fileName = "集团客户实销审核表.xls";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="+ fileName);
			
			OutputStream os = response.getOutputStream();
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			WritableSheet sheet = workbook.createSheet("集团客户实销审核表", 0);
			WritableCellFormat wcf = new WritableCellFormat();
			wcf.setAlignment(Alignment.CENTRE);
			
			Calendar  calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH)+1;
			int day =calendar.get(Calendar.DAY_OF_MONTH);
			
			act.setOutData("year", year);
			act.setOutData("month", month);
			act.setOutData("day", day);
			
			List<Map<String, Object>> reportList = dao.getActualSalesReport(map);

			int len = reportList.size();
			
			//省份合计
			long regionTotal = 0;
			//大区合计
			long orgTotal = 0;
			//总计
			long total = 0;
			int row= 0;
			
			
			sheet.mergeCells(0, row, 7, row);
			sheet.addCell(new  Label(0, row, "集团客户实销审核",wcf));
			
			
			++row;
			sheet.mergeCells(0, row, 7, row);
			sheet.addCell(new  Label(0, row, "起止日期："+startTime+"--"+endTime));
			
			
			
			++row;
			sheet.addCell(new  Label(0, row, "区域事业部"));
			sheet.addCell(new  Label(1, row, "省"));
			sheet.addCell(new  Label(2, row, "审核日期"));
			sheet.addCell(new  Label(3, row, "提报单位"));
			sheet.addCell(new  Label(4, row, "买方"));
			sheet.addCell(new  Label(5, row, "物料"));
			sheet.addCell(new  Label(6, row, "生产基地"));
			sheet.addCell(new  Label(7, row, "数量"));
			
			
			if(reportList != null && len != 0){
				for(int i=0;i<len;i++){
					int colTotal = Integer.parseInt(reportList.get(i).get("ACTAMOUNT").toString());
					if(i==0){
						regionTotal = colTotal;
						orgTotal = colTotal;
						total = colTotal;
						++row;
						sheet.addCell(new  Label(0, row, reportList.get(i).get("ROOT_ORG_NAME")==null?"":reportList.get(i).get("ROOT_ORG_NAME").toString()));
						sheet.addCell(new  Label(1, row, reportList.get(i).get("REGION_NAME")==null?"":reportList.get(i).get("REGION_NAME").toString()));
						sheet.addCell(new  Label(2, row, reportList.get(i).get("AUDITTIME")==null?"":reportList.get(i).get("AUDITTIME").toString()));
						sheet.addCell(new  Label(3, row, reportList.get(i).get("DEALER_NAME")==null?"":reportList.get(i).get("DEALER_NAME").toString()));
						sheet.addCell(new  Label(4, row, reportList.get(i).get("BUY_FROM")==null?"":reportList.get(i).get("BUY_FROM").toString()));
						sheet.addCell(new  Label(5, row, reportList.get(i).get("MATERIAL_NAME")==null?"":reportList.get(i).get("MATERIAL_NAME").toString()));
						sheet.addCell(new  Label(6, row, reportList.get(i).get("CODE_DESC")==null?"":reportList.get(i).get("CODE_DESC").toString()));
						sheet.addCell(new  Label(7, row, reportList.get(i).get("ACTAMOUNT")==null?"":reportList.get(i).get("ACTAMOUNT").toString()));
					}else{
						String before_region = reportList.get(i-1).get("REGION_NAME").toString();
						String cur_region = reportList.get(i).get("REGION_NAME").toString();
						
						String before_org = reportList.get(i-1).get("ROOT_ORG_NAME").toString();
						String cur_org = reportList.get(i).get("ROOT_ORG_NAME").toString();
						
						if(cur_region.equals(before_region) && cur_org.equals(before_org)){
							regionTotal += colTotal;
							orgTotal += colTotal;
							total += colTotal;
							
							++row;
							sheet.addCell(new  Label(0, row, reportList.get(i).get("ROOT_ORG_NAME")==null?"":reportList.get(i).get("ROOT_ORG_NAME").toString()));
							sheet.addCell(new  Label(1, row, reportList.get(i).get("REGION_NAME")==null?"":reportList.get(i).get("REGION_NAME").toString()));
							sheet.addCell(new  Label(2, row, reportList.get(i).get("AUDITTIME")==null?"":reportList.get(i).get("AUDITTIME").toString()));
							sheet.addCell(new  Label(3, row, reportList.get(i).get("DEALER_NAME")==null?"":reportList.get(i).get("DEALER_NAME").toString()));
							sheet.addCell(new  Label(4, row, reportList.get(i).get("BUY_FROM")==null?"":reportList.get(i).get("BUY_FROM").toString()));
							sheet.addCell(new  Label(5, row, reportList.get(i).get("MATERIAL_NAME")==null?"":reportList.get(i).get("MATERIAL_NAME").toString()));
							sheet.addCell(new  Label(6, row, reportList.get(i).get("CODE_DESC")==null?"":reportList.get(i).get("CODE_DESC").toString()));
							sheet.addCell(new  Label(7, row, reportList.get(i).get("ACTAMOUNT")==null?"":reportList.get(i).get("ACTAMOUNT").toString()));
						} else {
							++row;
							sheet.mergeCells(0, row, 6, row);
							sheet.addCell(new Label(0, row, "省份合计",wcf));
							sheet.addCell(new Label(7, row, String.valueOf(regionTotal),wcf));
							
							regionTotal = colTotal;
							total += colTotal;
							
							
							//开始1
							before_org = reportList.get(i-1).get("ROOT_ORG_NAME").toString();
							cur_org = reportList.get(i).get("ROOT_ORG_NAME").toString();
							
							if(cur_org.equals(before_org)){
								orgTotal += colTotal;
							}else{
								++row;
								sheet.mergeCells(0, row, 6, row);
								sheet.addCell(new Label(0, row, "大区合计",wcf));
								sheet.addCell(new Label(7, row, String.valueOf(orgTotal), wcf));
								orgTotal = colTotal;
							}
								++row;
								sheet.addCell(new  Label(0, row, reportList.get(i).get("ROOT_ORG_NAME")==null?"":reportList.get(i).get("ROOT_ORG_NAME").toString()));
								sheet.addCell(new  Label(1, row, reportList.get(i).get("REGION_NAME")==null?"":reportList.get(i).get("REGION_NAME").toString()));
								sheet.addCell(new  Label(2, row, reportList.get(i).get("AUDITTIME")==null?"":reportList.get(i).get("AUDITTIME").toString()));
								sheet.addCell(new  Label(3, row, reportList.get(i).get("DEALER_NAME")==null?"":reportList.get(i).get("DEALER_NAME").toString()));
								sheet.addCell(new  Label(4, row, reportList.get(i).get("BUY_FROM")==null?"":reportList.get(i).get("BUY_FROM").toString()));
								sheet.addCell(new  Label(5, row, reportList.get(i).get("MATERIAL_NAME")==null?"":reportList.get(i).get("MATERIAL_NAME").toString()));
								sheet.addCell(new  Label(6, row, reportList.get(i).get("CODE_DESC")==null?"":reportList.get(i).get("CODE_DESC").toString()));
								sheet.addCell(new  Label(7, row, reportList.get(i).get("ACTAMOUNT")==null?"":reportList.get(i).get("ACTAMOUNT").toString()));
						}
						if(i == len-1) {
							++row;
							sheet.mergeCells(0, row, 6, row);
							sheet.addCell(new Label(0, row, "省份合计",wcf));
							sheet.addCell(new Label(7, row, String.valueOf(regionTotal),wcf));
							
							++row;
							sheet.mergeCells(0, row, 6, row);
							sheet.addCell(new Label(0, row, "大区合计",wcf));
							sheet.addCell(new Label(7, row, String.valueOf(orgTotal), wcf));
						}
					}	
				}		
				++row;
				sheet.mergeCells(0, row, 6, row);
				sheet.addCell(new Label(0, row, "合计",wcf));
				sheet.addCell(new Label(7, row, String.valueOf(total), wcf));		
			}			
			
			workbook.write();
			workbook.close();
			os.flush();
			os.close();
			
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户实销审核下载");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 集团客户实销--下载
	 * */
	public void fleetSalesLogReportdownload(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String fleetName = CommonUtils.checkNull(request.getParamValue("fleetName"));
			String startTime = CommonUtils.checkNull(request.getParamValue("startTime"));
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			String orgCode = CommonUtils.checkNull(request.getParamValue("orgCode"));
			Map<String, String> map = new HashMap<String, String>();
			map.put("fleetName", fleetName);
			map.put("startTime", startTime);
			map.put("endTime", endTime);
			map.put("dealerCode", dealerCode);
			map.put("orgCode", orgCode);
			
			
			
			// 导出的文件名
			String fileName = "集团客户实销审核表.xls";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="+ fileName);
			
			OutputStream os = response.getOutputStream();
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			WritableSheet sheet = workbook.createSheet("集团客户实销审核表", 0);
			WritableCellFormat wcf = new WritableCellFormat();
			wcf.setAlignment(Alignment.CENTRE);
			
			Calendar  calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH)+1;
			int day =calendar.get(Calendar.DAY_OF_MONTH);
			
			act.setOutData("year", year);
			act.setOutData("month", month);
			act.setOutData("day", day);
			
			List<Map<String, Object>> reportList = dao.getActualSalesLogReport(map);

			int len = reportList.size();
			
			//省份合计
			long regionTotal = 0;
			//大区合计
			long orgTotal = 0;
			//总计
			long total = 0;
			int row= 0;
			
			
			sheet.mergeCells(0, row, 6, row);
			sheet.addCell(new  Label(0, row, "集团客户实销审核",wcf));
			
			
			++row;
			sheet.mergeCells(0, row, 6, row);
			sheet.addCell(new  Label(0, row, "起止日期："+startTime+"--"+endTime));
			
			
			
			++row;
			sheet.addCell(new  Label(0, row, "区域事业部"));
			sheet.addCell(new  Label(1, row, "省份"));
			sheet.addCell(new  Label(2, row, "经销商名称"));
			sheet.addCell(new  Label(3, row, "集团客户名称"));
			sheet.addCell(new  Label(4, row, "物料名称"));
			sheet.addCell(new  Label(5, row, "生产基地"));
			sheet.addCell(new  Label(6, row, "数量"));
			
			
			if(reportList != null && len != 0){
				for(int i=0;i<len;i++){
					int colTotal = Integer.parseInt(reportList.get(i).get("ACTAMOUNT").toString());
					if(i==0){
						regionTotal = colTotal;
						orgTotal = colTotal;
						total = colTotal;
						++row;
						sheet.addCell(new  Label(0, row, reportList.get(i).get("ROOT_ORG_NAME")==null?"":reportList.get(i).get("ROOT_ORG_NAME").toString()));
						sheet.addCell(new  Label(1, row, reportList.get(i).get("REGION_NAME")==null?"":reportList.get(i).get("REGION_NAME").toString()));
						sheet.addCell(new  Label(2, row, reportList.get(i).get("DEALER_NAME")==null?"":reportList.get(i).get("DEALER_NAME").toString()));
						sheet.addCell(new  Label(3, row, reportList.get(i).get("MATERIAL_NAME")==null?"":reportList.get(i).get("MATERIAL_NAME").toString()));
						sheet.addCell(new  Label(4, row, reportList.get(i).get("FLEET_NAME")==null?"":reportList.get(i).get("FLEET_NAME").toString()));
						sheet.addCell(new  Label(5, row, reportList.get(i).get("CODE_DESC")==null?"":reportList.get(i).get("CODE_DESC").toString()));
						sheet.addCell(new  Label(6, row, reportList.get(i).get("ACTAMOUNT")==null?"":reportList.get(i).get("ACTAMOUNT").toString()));
					}else{
						String before_region = reportList.get(i-1).get("REGION_NAME").toString();
						String cur_region = reportList.get(i).get("REGION_NAME").toString();
						
						String before_org = reportList.get(i-1).get("ROOT_ORG_NAME").toString();
						String cur_org = reportList.get(i).get("ROOT_ORG_NAME").toString();
						
						if(cur_region.equals(before_region) && cur_org.equals(before_org)){
							regionTotal += colTotal;
							orgTotal += colTotal;
							total += colTotal;
							
							++row;
							sheet.addCell(new  Label(0, row, reportList.get(i).get("ROOT_ORG_NAME")==null?"":reportList.get(i).get("ROOT_ORG_NAME").toString()));
							sheet.addCell(new  Label(1, row, reportList.get(i).get("REGION_NAME")==null?"":reportList.get(i).get("REGION_NAME").toString()));
							sheet.addCell(new  Label(2, row, reportList.get(i).get("DEALER_NAME")==null?"":reportList.get(i).get("DEALER_NAME").toString()));
							sheet.addCell(new  Label(3, row, reportList.get(i).get("MATERIAL_NAME")==null?"":reportList.get(i).get("MATERIAL_NAME").toString()));
							sheet.addCell(new  Label(4, row, reportList.get(i).get("FLEET_NAME")==null?"":reportList.get(i).get("FLEET_NAME").toString()));
							sheet.addCell(new  Label(5, row, reportList.get(i).get("CODE_DESC")==null?"":reportList.get(i).get("CODE_DESC").toString()));
							sheet.addCell(new  Label(6, row, reportList.get(i).get("ACTAMOUNT")==null?"":reportList.get(i).get("ACTAMOUNT").toString()));
						} else {
							++row;
							sheet.mergeCells(0, row, 5, row);
							sheet.addCell(new Label(0, row, "省份合计",wcf));
							sheet.addCell(new Label(6, row, String.valueOf(regionTotal),wcf));
							
							regionTotal = colTotal;
							total += colTotal;
							
							
							//开始1
							before_org = reportList.get(i-1).get("ROOT_ORG_NAME").toString();
							cur_org = reportList.get(i).get("ROOT_ORG_NAME").toString();
							
							if(cur_org.equals(before_org)){
								orgTotal += colTotal;
							}else{
								++row;
								sheet.mergeCells(0, row, 5, row);
								sheet.addCell(new Label(0, row, "大区合计",wcf));
								sheet.addCell(new Label(6, row, String.valueOf(orgTotal), wcf));
								orgTotal = colTotal;
							}
								++row;
								sheet.addCell(new  Label(0, row, reportList.get(i).get("ROOT_ORG_NAME")==null?"":reportList.get(i).get("ROOT_ORG_NAME").toString()));
								sheet.addCell(new  Label(1, row, reportList.get(i).get("REGION_NAME")==null?"":reportList.get(i).get("REGION_NAME").toString()));
								sheet.addCell(new  Label(2, row, reportList.get(i).get("DEALER_NAME")==null?"":reportList.get(i).get("DEALER_NAME").toString()));
								sheet.addCell(new  Label(3, row, reportList.get(i).get("MATERIAL_NAME")==null?"":reportList.get(i).get("MATERIAL_NAME").toString()));
								sheet.addCell(new  Label(4, row, reportList.get(i).get("FLEET_NAME")==null?"":reportList.get(i).get("FLEET_NAME").toString()));
								sheet.addCell(new  Label(5, row, reportList.get(i).get("CODE_DESC")==null?"":reportList.get(i).get("CODE_DESC").toString()));
								sheet.addCell(new  Label(6, row, reportList.get(i).get("ACTAMOUNT")==null?"":reportList.get(i).get("ACTAMOUNT").toString()));
						}
						if(i == len-1) {
							++row;
							sheet.mergeCells(0, row, 5, row);
							sheet.addCell(new Label(0, row, "省份合计",wcf));
							sheet.addCell(new Label(6, row, String.valueOf(regionTotal),wcf));
							
							++row;
							sheet.mergeCells(0, row, 5, row);
							sheet.addCell(new Label(0, row, "大区合计",wcf));
							sheet.addCell(new Label(6, row, String.valueOf(orgTotal), wcf));
						}
					}	
				}		
				++row;
				sheet.mergeCells(0, row, 5, row);
				sheet.addCell(new Label(0, row, "合计",wcf));
				sheet.addCell(new Label(6, row, String.valueOf(total), wcf));		
			}			
			
			workbook.write();
			workbook.close();
			os.flush();
			os.close();
			
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户实销下载");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	
	/**
	 * 常规订单超时统计汇总表初始化
	 * */
	public void getTimeoutStatisticsSummaryInit() {
		try {
			/*Calendar startTime = Calendar.getInstance();
			Calendar endTime = Calendar.getInstance();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");*/
			
			
			//查询目标年份
			Calendar calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH);
			act.setOutData("year", year);
			act.setOutData("month", month+1);
			
			//添加大区范围限制
			String dutyType = "";
			if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(logonUser.getDutyType())) {
				dutyType = logonUser.getDutyType();
				act.setOutData("dutyType", dutyType);
			} else if(Constant.DUTY_TYPE_DEALER.toString().equals(logonUser.getDutyType())) {
				dutyType = logonUser.getDutyType();
				act.setOutData("dutyType", dutyType);
			} else if(Constant.DUTY_TYPE_COMPANY.toString().equals(logonUser.getDutyType())) {
				dutyType = logonUser.getDutyType();
				act.setOutData("dutyType", dutyType);
			}
				
			/*act.setOutData("startTime", dateFormat.format(startTime.getTime()));
			act.setOutData("endTime", dateFormat.format(endTime.getTime()));*/
			act.setForword(OrderTimeoutStatisticsSummaryUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
			ErrorCodeConstant.QUERY_FAILURE_CODE, "常规订单超时统计汇总表初始化！");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 常规订单超时统计汇总表查询
	 * */
	public void getTimeoutStatisticsSummaryReport() {
		try {
			String startTime = CommonUtils.checkNull(request.getParamValue("startTime"));
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
			String orgCode = CommonUtils.checkNull(request.getParamValue("orgCode"));
			String price = CommonDAO.getPara(Constant.ORDER_TIMEOUT_STATISTICS_PRICE.toString()) ;
			
			//添加大区范围限制
			if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(logonUser.getDutyType())) {
				orgId = logonUser.getOrgId().toString();
			} else if(Constant.DUTY_TYPE_DEALER.toString().equals(logonUser.getDutyType())) {
				dealerId = logonUser.getDealerId();
			}
			//添加业务范围限制
			String areaId = "";
			Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(poseId.toString());
			for(int i=0; i<areaList.size(); i++) {
				areaId += "'" + areaList.get(i).get("AREA_ID") + "'";
				if(i != areaList.size()-1){
					areaId +=  ",";
				} 
			}
			
			Map<String, String> map = new HashMap<String, String>();
			map.put("startTime", startTime);
			map.put("endTime", endTime);
			map.put("dealerId", dealerId);
			map.put("dealerCode", dealerCode);
			map.put("orgId", orgId);
			map.put("orgCode", orgCode);
			map.put("price", price);
			map.put("areaId", areaId);
			List<Map<String, Object>> reportList = dao.getTimeoutStatisticsSummaryReport(map);
			act.setOutData("startTime", startTime);
			act.setOutData("endTime", endTime);
			act.setOutData("list_report", reportList);
			act.setForword(OrderTimeoutStatisticsSummaryReportUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act, e,
			ErrorCodeConstant.QUERY_FAILURE_CODE, "常规订单超时统计汇总表查询！");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 常规订单超时统计汇总表下载
	 * */
	public void getTimeoutStatisticsSummaryDownload() {
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String startYear = CommonUtils.checkNull(request.getParamValue("startYear"));
			String startMonth = CommonUtils.checkNull(request.getParamValue("startMonth"));
			String endYear = CommonUtils.checkNull(request.getParamValue("endYear"));
			String endMonth = CommonUtils.checkNull(request.getParamValue("endMonth"));
			/*String startTime = CommonUtils.checkNull(request.getParamValue("startTime"));
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));*/
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
			String orgCode = CommonUtils.checkNull(request.getParamValue("orgCode"));
			String price = CommonDAO.getPara(Constant.ORDER_TIMEOUT_STATISTICS_PRICE.toString()) ;
			
			//添加大区范围限制
			if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(logonUser.getDutyType())) {
				orgId = logonUser.getOrgId().toString();
			} else if(Constant.DUTY_TYPE_DEALER.toString().equals(logonUser.getDutyType())) {
				dealerId = logonUser.getDealerId();
			}
			//添加业务范围限制
			String areaId = null;
			Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(poseId.toString());
			for(int i=0; i<areaList.size(); i++) {
				areaId += areaList.get(i).get("AREA_ID");
				if(i != areaList.size()-1){
					areaId +=  ",";
				} 
			}
			
			
			// 调用发运存储过程计算明细
			//p_cq_report_dtl(开始年,开始月, 结束年, 结束月);
			List<Object> ins = new LinkedList<Object>();
			ins.add(startYear);
			ins.add(startMonth);
			ins.add(endYear);
			ins.add(endMonth);
			dao.callProcedure("p_cq_report_dtl", ins, null);
			
			Map<String, String> map = new HashMap<String, String>();
			map.put("startYear", startYear);
			map.put("startMonth", startMonth);
			map.put("endYear", endYear);
			map.put("endMonth", endMonth);
			/*map.put("startTime", startTime);
			map.put("endTime", endTime);*/
			map.put("dealerId", dealerId);
			map.put("dealerCode", dealerCode);
			map.put("orgId", orgId);
			map.put("orgCode", orgCode);
			map.put("price", price);
			map.put("areaId", areaId);
			
			
			// 导出的文件名
			String fileName = "常规订单超时统计汇总表.xls";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="+ fileName);
			
			OutputStream os = response.getOutputStream();
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			WritableSheet sheet = workbook.createSheet("常规订单超时统计汇总表", 0);
			WritableCellFormat wcf = new WritableCellFormat();
			wcf.setAlignment(Alignment.CENTRE);
			
			Calendar  calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH)+1;
			int day =calendar.get(Calendar.DAY_OF_MONTH);
			
			act.setOutData("year", year);
			act.setOutData("month", month);
			act.setOutData("day", day);
			
			List<Map<String, Object>> reportList = dao.getTimeoutStatisticsSummaryReport(map);

			int len = reportList.size();
			//列数
			int colCount = 8;
			
			//经销商合计
			BigDecimal dealerTotal = BigDecimal.ZERO;
			//省份合计
			BigDecimal regionTotal = BigDecimal.ZERO;
			//大区合计
			BigDecimal orgTotal = BigDecimal.ZERO;
			//全国总计
			BigDecimal total = BigDecimal.ZERO;
			
			int row = 0;
			
			
			sheet.mergeCells(0, row, colCount, row);
			sheet.addCell(new  Label(0, row, "常规订单超时统计汇总表",wcf));
			
			
			++row;
			sheet.mergeCells(0, row, colCount, row);
			sheet.addCell(new  Label(0, row, "统计日期："+startYear+"年"+ startMonth +  "月  ~ " +  endYear + "年" +endMonth + "月"));
			
			
			
			
			++row;
			sheet.addCell(new  Label(0, row, "大区"));
			sheet.addCell(new  Label(1, row, "省份"));
			sheet.addCell(new  Label(2, row, "单位名称"));
			sheet.addCell(new  Label(3, row, "订单周度"));
			sheet.addCell(new  Label(4, row, "数量"));
			sheet.addCell(new  Label(5, row, "当前超期天数"));
			sheet.addCell(new  Label(6, row, "当前扣款金额"));
			sheet.addCell(new  Label(7, row, "总超期天数"));
			sheet.addCell(new  Label(8, row, "总扣款金额"));
			
			
			if(reportList != null && len != 0){
				for(int i=0;i<len;i++){
					BigDecimal totalPrice = ((BigDecimal)reportList.get(i).get("AMOUNT"));
					if(i==0){//首条记录
						dealerTotal = totalPrice;
						regionTotal = totalPrice;
						orgTotal = totalPrice;
						total = totalPrice;
						++row;
						sheet.addCell(new  Label(0, row, reportList.get(i).get("ORG_NAME")==null?"":reportList.get(i).get("ORG_NAME").toString()));
						sheet.addCell(new  Label(1, row, reportList.get(i).get("REGION_NAME")==null?"":reportList.get(i).get("REGION_NAME").toString()));
						sheet.addCell(new  Label(2, row, reportList.get(i).get("DEALER_NAME")==null?"":reportList.get(i).get("DEALER_NAME").toString()));
						sheet.addCell(new  Label(3, row, reportList.get(i).get("ORDER_WEEKLY")==null?"":reportList.get(i).get("ORDER_WEEKLY").toString()));
						sheet.addCell(new  Label(4, row, reportList.get(i).get("AMOUNT")==null?"":reportList.get(i).get("AMOUNT").toString()));
						sheet.addCell(new  Label(5, row, reportList.get(i).get("OVERTIME_DAYS")==null?"":reportList.get(i).get("OVERTIME_DAYS").toString()));
						sheet.addCell(new  Label(6, row, reportList.get(i).get("PAYMENT_PRICE")==null?"":reportList.get(i).get("PAYMENT_PRICE").toString()));
						sheet.addCell(new  Label(7, row, reportList.get(i).get("TOTAL_OVERTIME_DAYS")==null?"":reportList.get(i).get("TOTAL_OVERTIME_DAYS").toString()));
						sheet.addCell(new  Label(8, row, reportList.get(i).get("TOTAL_PRICE")==null?"":reportList.get(i).get("TOTAL_PRICE").toString()));
					}else{
						
						String before_region = reportList.get(i-1).get("REGION_NAME").toString();
						String cur_region = reportList.get(i).get("REGION_NAME").toString();
						
						String before_org = reportList.get(i-1).get("ORG_NAME").toString();
						String cur_org = reportList.get(i).get("ORG_NAME").toString();
						
						if(cur_region.equals(before_region) && cur_org.equals(before_org)){//大区和省份相同
							
							String before_dealer = reportList.get(i-1).get("DEALER_NAME").toString();
							String cur_dealer = reportList.get(i).get("DEALER_NAME").toString();
							if(!before_dealer.equals(cur_dealer)) {
								++row;
								sheet.mergeCells(0, row, 2, row);
								sheet.addCell(new Label(0, row, before_dealer + "合计",wcf));
								sheet.addCell(new Label(4, row, String.valueOf(dealerTotal),wcf));
								dealerTotal = totalPrice;
							} else {
								dealerTotal = dealerTotal.add(totalPrice);
							}
							regionTotal = regionTotal.add(totalPrice);
							orgTotal = orgTotal.add(totalPrice);
							total = total.add(totalPrice);
							++row;
							sheet.addCell(new  Label(0, row, reportList.get(i).get("ORG_NAME")==null?"":reportList.get(i).get("ORG_NAME").toString()));
							sheet.addCell(new  Label(1, row, reportList.get(i).get("REGION_NAME")==null?"":reportList.get(i).get("REGION_NAME").toString()));
							sheet.addCell(new  Label(2, row, reportList.get(i).get("DEALER_NAME")==null?"":reportList.get(i).get("DEALER_NAME").toString()));
							sheet.addCell(new  Label(3, row, reportList.get(i).get("ORDER_WEEKLY")==null?"":reportList.get(i).get("ORDER_WEEKLY").toString()));
							sheet.addCell(new  Label(4, row, reportList.get(i).get("AMOUNT")==null?"":reportList.get(i).get("AMOUNT").toString()));
							sheet.addCell(new  Label(5, row, reportList.get(i).get("OVERTIME_DAYS")==null?"":reportList.get(i).get("OVERTIME_DAYS").toString()));
							sheet.addCell(new  Label(6, row, reportList.get(i).get("PAYMENT_PRICE")==null?"":reportList.get(i).get("PAYMENT_PRICE").toString()));
							sheet.addCell(new  Label(7, row, reportList.get(i).get("TOTAL_OVERTIME_DAYS")==null?"":reportList.get(i).get("TOTAL_OVERTIME_DAYS").toString()));
							sheet.addCell(new  Label(8, row, reportList.get(i).get("TOTAL_PRICE")==null?"":reportList.get(i).get("TOTAL_PRICE").toString()));
						} else {//大区或省份不同
							
							String before_dealer = reportList.get(i-1).get("DEALER_NAME").toString();
							String cur_dealer = reportList.get(i).get("DEALER_NAME").toString();
							if(!before_dealer.equals(cur_dealer)) {//经销商不同
								++row;
								sheet.mergeCells(0, row, 2, row);
								sheet.addCell(new Label(0, row, before_dealer + "合计",wcf));
								sheet.addCell(new Label(4, row, String.valueOf(dealerTotal),wcf));
								dealerTotal = totalPrice;
							} 
									
							
							//大区
							before_org = reportList.get(i-1).get("ORG_NAME").toString();
							cur_org = reportList.get(i).get("ORG_NAME").toString();
							
							if(cur_org.equals(before_org)){//大区相同，省份不同
								++row;
								sheet.mergeCells(0, row, 1, row);
								sheet.addCell(new Label(0, row, before_region + "合计",wcf));
								sheet.addCell(new Label(4, row, String.valueOf(regionTotal), wcf));
								regionTotal = totalPrice;
								orgTotal = orgTotal.add(totalPrice);
							}else{//大区不同
								++row;
								sheet.mergeCells(0, row, 1, row);
								sheet.addCell(new Label(0, row, before_region + "合计",wcf));
								sheet.addCell(new Label(4, row, String.valueOf(regionTotal), wcf));
								++row;
								sheet.addCell(new Label(0, row, before_org + "合计",wcf));
								sheet.addCell(new Label(4, row, String.valueOf(orgTotal), wcf));
								regionTotal = totalPrice;
								orgTotal = totalPrice;
							}
							total = total.add(totalPrice);
							++row;
							sheet.addCell(new  Label(0, row, reportList.get(i).get("ORG_NAME")==null?"":reportList.get(i).get("ORG_NAME").toString()));
							sheet.addCell(new  Label(1, row, reportList.get(i).get("REGION_NAME")==null?"":reportList.get(i).get("REGION_NAME").toString()));
							sheet.addCell(new  Label(2, row, reportList.get(i).get("DEALER_NAME")==null?"":reportList.get(i).get("DEALER_NAME").toString()));
							sheet.addCell(new  Label(3, row, reportList.get(i).get("ORDER_WEEKLY")==null?"":reportList.get(i).get("ORDER_WEEKLY").toString()));
							sheet.addCell(new  Label(4, row, reportList.get(i).get("AMOUNT")==null?"":reportList.get(i).get("AMOUNT").toString()));
							sheet.addCell(new  Label(5, row, reportList.get(i).get("OVERTIME_DAYS")==null?"":reportList.get(i).get("OVERTIME_DAYS").toString()));
							sheet.addCell(new  Label(6, row, reportList.get(i).get("PAYMENT_PRICE")==null?"":reportList.get(i).get("PAYMENT_PRICE").toString()));
							sheet.addCell(new  Label(7, row, reportList.get(i).get("TOTAL_OVERTIME_DAYS")==null?"":reportList.get(i).get("TOTAL_OVERTIME_DAYS").toString()));
							sheet.addCell(new  Label(8, row, reportList.get(i).get("TOTAL_PRICE")==null?"":reportList.get(i).get("TOTAL_PRICE").toString()));
						}
						if(i == len-1) {//最后一条数据
							
							String cur_dealer = reportList.get(i).get("DEALER_NAME").toString();
							
							++row;
							sheet.mergeCells(0, row, 2, row);
							sheet.addCell(new Label(0, row, cur_dealer + "合计",wcf));
							sheet.addCell(new Label(4, row, String.valueOf(dealerTotal),wcf));
							
							++row;
							sheet.mergeCells(0, row, 1, row);
							sheet.addCell(new Label(0, row, cur_region + "合计",wcf));
							sheet.addCell(new Label(4, row, String.valueOf(regionTotal),wcf));
							
							++row;
							sheet.addCell(new Label(0, row, cur_org + "合计",wcf));
							sheet.addCell(new Label(4, row, String.valueOf(orgTotal), wcf));
						}
					}	
				}		
				++row;
				sheet.addCell(new Label(0, row, "合计",wcf));
				sheet.addCell(new Label(4, row, String.valueOf(total), wcf));		
			}			
			
			workbook.write();
			workbook.close();
			os.flush();
			os.close();
			
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"常规订单超时统计汇总表下载");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 常规订单超时统计明细表初始化
	 * */
	public void getTimeoutStatisticsDetailInit() {
		try {
			/*Calendar startTime = Calendar.getInstance();
			Calendar endTime = Calendar.getInstance();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");*/
			
			//查询目标年份
			Calendar calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH);
			act.setOutData("year", year);
			act.setOutData("month", month+1);
			
			//添加大区范围限制
			String dutyType = "";
			if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(logonUser.getDutyType())) {
				dutyType = logonUser.getDutyType();
				act.setOutData("dutyType", dutyType);
			} else if(Constant.DUTY_TYPE_DEALER.toString().equals(logonUser.getDutyType())) {
				dutyType = logonUser.getDutyType();
				act.setOutData("dutyType", dutyType);
			} else if(Constant.DUTY_TYPE_COMPANY.toString().equals(logonUser.getDutyType())) {
				dutyType = logonUser.getDutyType();
				act.setOutData("dutyType", dutyType);
			}
			/*act.setOutData("startTime", dateFormat.format(startTime.getTime()));
			act.setOutData("endTime", dateFormat.format(endTime.getTime()));*/
			act.setForword(OrderTimeoutStatisticsDetailUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
			ErrorCodeConstant.QUERY_FAILURE_CODE, "常规订单超时统计明细表初始化！");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 *常规订单超时统计明细表查询
	 * */
	public void getTimeoutStatisticsDetailReport() {
		try {
			String startTime = CommonUtils.checkNull(request.getParamValue("startTime"));
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
			String orgCode = CommonUtils.checkNull(request.getParamValue("orgCode"));
			String price = CommonDAO.getPara(Constant.ORDER_TIMEOUT_STATISTICS_PRICE.toString()) ;
			
			//添加大区范围限制
			if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(logonUser.getDutyType())) {
				orgId = logonUser.getOrgId().toString();
			} else if(Constant.DUTY_TYPE_DEALER.toString().equals(logonUser.getDutyType())) {
				dealerId = logonUser.getDealerId();
			}
			//添加业务范围限制
			String areaId = null;
			Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(poseId.toString());
			for(int i=0; i<areaList.size(); i++) {
				areaId += areaList.get(i).get("AREA_ID");
				if(i != areaList.size()-1){
					areaId +=  ",";
				} 
			}
			
			Map<String, String> map = new HashMap<String, String>();
			map.put("startTime", startTime);
			map.put("endTime", endTime);
			map.put("dealerId", dealerId);
			map.put("dealerCode", dealerCode);
			map.put("orgId", orgId);
			map.put("orgCode", orgCode);
			map.put("price", price);
			map.put("areaId", areaId);
			List<Map<String, Object>> reportList = dao.getTimeoutStatisticsSummaryReport(map);
			act.setOutData("startTime", startTime);
			act.setOutData("endTime", endTime);
			act.setOutData("list_report", reportList);
			act.setForword(OrderTimeoutStatisticsSummaryReportUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act, e,
			ErrorCodeConstant.QUERY_FAILURE_CODE, "常规订单超时统计明细表查询！");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 常规订单超时统计明细表下载
	 * */
	public void getTimeoutStatisticsDetailDownload() {
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String startYear = CommonUtils.checkNull(request.getParamValue("startYear"));
			String startMonth = CommonUtils.checkNull(request.getParamValue("startMonth"));
			String endYear = CommonUtils.checkNull(request.getParamValue("endYear"));
			String endMonth = CommonUtils.checkNull(request.getParamValue("endMonth"));
			/*String startTime = CommonUtils.checkNull(request.getParamValue("startTime"));
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));*/
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
			String orgCode = CommonUtils.checkNull(request.getParamValue("orgCode"));
			String price = CommonDAO.getPara(Constant.ORDER_TIMEOUT_STATISTICS_PRICE.toString()) ;
			
			//添加大区范围限制
			if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(logonUser.getDutyType())) {
				orgId = logonUser.getOrgId().toString();
			} else if(Constant.DUTY_TYPE_DEALER.toString().equals(logonUser.getDutyType())) {
				dealerId = logonUser.getDealerId();
			}
			//添加业务范围限制
			String areaId = null;
			Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(poseId.toString());
			for(int i=0; i<areaList.size(); i++) {
				areaId += areaList.get(i).get("AREA_ID");
				if(i != areaList.size()-1){
					areaId +=  ",";
				} 
			}
			
			// 调用发运存储过程计算明细
			//p_cq_report_dtl(开始年,开始月, 结束年, 结束月);
			List<Object> ins = new LinkedList<Object>();
			ins.add(startYear);
			ins.add(startMonth);
			ins.add(endYear);
			ins.add(endMonth);
			dao.callProcedure("p_cq_report_dtl", ins, null);
			
			Map<String, String> map = new HashMap<String, String>();
			map.put("startYear", startYear);
			map.put("startMonth", startMonth);
			map.put("endYear", endYear);
			map.put("endMonth", endMonth);
			/*map.put("startTime", startTime);
			map.put("endTime", endTime);*/
			map.put("dealerId", dealerId);
			map.put("dealerCode", dealerCode);
			map.put("orgId", orgId);
			map.put("orgCode", orgCode);
			map.put("price", price);
			map.put("areaId", areaId);
			
			
			// 导出的文件名
			String fileName = "常规订单超时统计明细表.xls";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="+ fileName);
			
			OutputStream os = response.getOutputStream();
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			WritableSheet sheet = workbook.createSheet("常规订单超时统计明细表", 0);
			WritableCellFormat wcf = new WritableCellFormat();
			wcf.setAlignment(Alignment.CENTRE);
			
			Calendar  calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH)+1;
			int day =calendar.get(Calendar.DAY_OF_MONTH);
			
			act.setOutData("year", year);
			act.setOutData("month", month);
			act.setOutData("day", day);
			
			List<Map<String, Object>> reportList = dao.getTimeoutStatisticsDetailReport(map);

			int len = reportList.size();
			//列数
			int colCount = 10;
			
			//经销商合计
			BigDecimal dealerTotal = BigDecimal.ZERO;
			//省份合计
			BigDecimal regionTotal = BigDecimal.ZERO;
			//大区合计
			BigDecimal orgTotal = BigDecimal.ZERO;
			//全国总计
			BigDecimal total = BigDecimal.ZERO;
			
			int row = 0;
			
			
			sheet.mergeCells(0, row, colCount, row);
			sheet.addCell(new  Label(0, row, "常规订单超时统计明细表",wcf));
			
			
			++row;
			sheet.mergeCells(0, row, colCount, row);
			sheet.addCell(new  Label(0, row, "统计日期："+startYear+"年"+ startMonth +  "月  ~ " +  endYear + "年" +endMonth + "月"));
			
			
			
			
			++row;
			sheet.addCell(new  Label(0, row, "大区"));
			sheet.addCell(new  Label(1, row, "省份"));
			sheet.addCell(new  Label(2, row, "单位名称"));
			sheet.addCell(new  Label(3, row, "订单周度"));
			sheet.addCell(new  Label(4, row, "常规订单号"));
			sheet.addCell(new  Label(5, row, "车型编码"));
			sheet.addCell(new  Label(6, row, "数量"));
			sheet.addCell(new  Label(7, row, "当前超期天数"));
			sheet.addCell(new  Label(8, row, "当前扣款金额"));
			sheet.addCell(new  Label(9, row, "总超期天数"));
			sheet.addCell(new  Label(10, row, "总扣款金额"));
			
			
			if(reportList != null && len != 0){
				for(int i=0;i<len;i++){
					BigDecimal totalPrice = ((BigDecimal)reportList.get(i).get("AMOUNT"));
					if(i==0){//首条记录
						dealerTotal = totalPrice;
						regionTotal = totalPrice;
						orgTotal = totalPrice;
						total = totalPrice;
						++row;
						sheet.addCell(new  Label(0, row, reportList.get(i).get("ORG_NAME")==null?"":reportList.get(i).get("ORG_NAME").toString()));
						sheet.addCell(new  Label(1, row, reportList.get(i).get("REGION_NAME")==null?"":reportList.get(i).get("REGION_NAME").toString()));
						sheet.addCell(new  Label(2, row, reportList.get(i).get("DEALER_NAME")==null?"":reportList.get(i).get("DEALER_NAME").toString()));
						sheet.addCell(new  Label(3, row, reportList.get(i).get("ORDER_WEEKLY")==null?"":reportList.get(i).get("ORDER_WEEKLY").toString()));
						sheet.addCell(new  Label(4, row, reportList.get(i).get("ORDER_NO")==null?"":reportList.get(i).get("ORDER_NO").toString()));
						sheet.addCell(new  Label(5, row, reportList.get(i).get("MATERIAL_CODE")==null?"":reportList.get(i).get("MATERIAL_CODE").toString()));
						sheet.addCell(new  Label(6, row, reportList.get(i).get("AMOUNT")==null?"":reportList.get(i).get("AMOUNT").toString()));
						sheet.addCell(new  Label(7, row, reportList.get(i).get("OVERTIME_DAYS")==null?"":reportList.get(i).get("OVERTIME_DAYS").toString()));
						sheet.addCell(new  Label(8, row, reportList.get(i).get("PAYMENT_PRICE")==null?"":reportList.get(i).get("PAYMENT_PRICE").toString()));
						sheet.addCell(new  Label(9, row, reportList.get(i).get("TOTAL_OVERTIME_DAYS")==null?"":reportList.get(i).get("TOTAL_OVERTIME_DAYS").toString()));
						sheet.addCell(new  Label(10, row, reportList.get(i).get("TOTAL_PRICE")==null?"":reportList.get(i).get("TOTAL_PRICE").toString()));
					}else{
						
						String before_region = reportList.get(i-1).get("REGION_NAME").toString();
						String cur_region = reportList.get(i).get("REGION_NAME").toString();
						
						String before_org = reportList.get(i-1).get("ORG_NAME").toString();
						String cur_org = reportList.get(i).get("ORG_NAME").toString();
						
						if(cur_region.equals(before_region) && cur_org.equals(before_org)){//大区和省份相同
							
							String before_dealer = reportList.get(i-1).get("DEALER_NAME").toString();
							String cur_dealer = reportList.get(i).get("DEALER_NAME").toString();
							if(!before_dealer.equals(cur_dealer)) {
								++row;
								sheet.mergeCells(0, row, 2, row);
								sheet.addCell(new Label(0, row, before_dealer + "合计",wcf));
								sheet.addCell(new Label(6, row, String.valueOf(dealerTotal),wcf));
								dealerTotal = totalPrice;
							} else {
								dealerTotal = dealerTotal.add(totalPrice);
							}
							regionTotal = regionTotal.add(totalPrice);
							orgTotal = orgTotal.add(totalPrice);
							total = total.add(totalPrice);
							++row;
							sheet.addCell(new  Label(0, row, reportList.get(i).get("ORG_NAME")==null?"":reportList.get(i).get("ORG_NAME").toString()));
							sheet.addCell(new  Label(1, row, reportList.get(i).get("REGION_NAME")==null?"":reportList.get(i).get("REGION_NAME").toString()));
							sheet.addCell(new  Label(2, row, reportList.get(i).get("DEALER_NAME")==null?"":reportList.get(i).get("DEALER_NAME").toString()));
							sheet.addCell(new  Label(3, row, reportList.get(i).get("ORDER_WEEKLY")==null?"":reportList.get(i).get("ORDER_WEEKLY").toString()));
							sheet.addCell(new  Label(4, row, reportList.get(i).get("ORDER_NO")==null?"":reportList.get(i).get("ORDER_NO").toString()));
							sheet.addCell(new  Label(5, row, reportList.get(i).get("MATERIAL_CODE")==null?"":reportList.get(i).get("MATERIAL_CODE").toString()));
							sheet.addCell(new  Label(6, row, reportList.get(i).get("AMOUNT")==null?"":reportList.get(i).get("AMOUNT").toString()));
							sheet.addCell(new  Label(7, row, reportList.get(i).get("OVERTIME_DAYS")==null?"":reportList.get(i).get("OVERTIME_DAYS").toString()));
							sheet.addCell(new  Label(8, row, reportList.get(i).get("PAYMENT_PRICE")==null?"":reportList.get(i).get("PAYMENT_PRICE").toString()));
							sheet.addCell(new  Label(9, row, reportList.get(i).get("TOTAL_OVERTIME_DAYS")==null?"":reportList.get(i).get("TOTAL_OVERTIME_DAYS").toString()));
							sheet.addCell(new  Label(10, row, reportList.get(i).get("TOTAL_PRICE")==null?"":reportList.get(i).get("TOTAL_PRICE").toString()));
						} else {//大区或省份不同
							
							String before_dealer = reportList.get(i-1).get("DEALER_NAME").toString();
							String cur_dealer = reportList.get(i).get("DEALER_NAME").toString();
							if(!before_dealer.equals(cur_dealer)) {//经销商不同
								++row;
								sheet.mergeCells(0, row, 2, row);
								sheet.addCell(new Label(0, row, before_dealer + "合计",wcf));
								sheet.addCell(new Label(6, row, String.valueOf(dealerTotal),wcf));
								dealerTotal = totalPrice;
							} 
									
							
							//大区
							before_org = reportList.get(i-1).get("ORG_NAME").toString();
							cur_org = reportList.get(i).get("ORG_NAME").toString();
							
							if(cur_org.equals(before_org)){//大区相同，省份不同
								++row;
								sheet.mergeCells(0, row, 1, row);
								sheet.addCell(new Label(0, row, before_region + "合计",wcf));
								sheet.addCell(new Label(6, row, String.valueOf(regionTotal), wcf));
								regionTotal = totalPrice;
								orgTotal= orgTotal.add(totalPrice);
							}else{//大区不同
								++row;
								sheet.mergeCells(0, row, 1, row);
								sheet.addCell(new Label(0, row, before_region + "合计",wcf));
								sheet.addCell(new Label(6, row, String.valueOf(regionTotal), wcf));
								++row;
								sheet.addCell(new Label(0, row, before_org + "合计",wcf));
								sheet.addCell(new Label(6, row, String.valueOf(orgTotal), wcf));
								regionTotal = totalPrice;
								orgTotal = totalPrice;
							}
							total = total.add(totalPrice);
							++row;
							sheet.addCell(new  Label(0, row, reportList.get(i).get("ORG_NAME")==null?"":reportList.get(i).get("ORG_NAME").toString()));
							sheet.addCell(new  Label(1, row, reportList.get(i).get("REGION_NAME")==null?"":reportList.get(i).get("REGION_NAME").toString()));
							sheet.addCell(new  Label(2, row, reportList.get(i).get("DEALER_NAME")==null?"":reportList.get(i).get("DEALER_NAME").toString()));
							sheet.addCell(new  Label(3, row, reportList.get(i).get("ORDER_WEEKLY")==null?"":reportList.get(i).get("ORDER_WEEKLY").toString()));
							sheet.addCell(new  Label(4, row, reportList.get(i).get("ORDER_NO")==null?"":reportList.get(i).get("ORDER_NO").toString()));
							sheet.addCell(new  Label(5, row, reportList.get(i).get("MATERIAL_CODE")==null?"":reportList.get(i).get("MATERIAL_CODE").toString()));
							sheet.addCell(new  Label(6, row, reportList.get(i).get("AMOUNT")==null?"":reportList.get(i).get("AMOUNT").toString()));
							sheet.addCell(new  Label(7, row, reportList.get(i).get("OVERTIME_DAYS")==null?"":reportList.get(i).get("OVERTIME_DAYS").toString()));
							sheet.addCell(new  Label(8, row, reportList.get(i).get("PAYMENT_PRICE")==null?"":reportList.get(i).get("PAYMENT_PRICE").toString()));
							sheet.addCell(new  Label(9, row, reportList.get(i).get("TOTAL_OVERTIME_DAYS")==null?"":reportList.get(i).get("TOTAL_OVERTIME_DAYS").toString()));
							sheet.addCell(new  Label(10, row, reportList.get(i).get("TOTAL_PRICE")==null?"":reportList.get(i).get("TOTAL_PRICE").toString()));
						}
						if(i == len-1) {//最后一条数据
							
							String cur_dealer = reportList.get(i).get("DEALER_NAME").toString();
							
							++row;
							sheet.mergeCells(0, row, 2, row);
							sheet.addCell(new Label(0, row, cur_dealer + "合计",wcf));
							sheet.addCell(new Label(6, row, String.valueOf(dealerTotal),wcf));
							
							++row;
							sheet.mergeCells(0, row, 1, row);
							sheet.addCell(new Label(0, row, cur_region + "合计",wcf));
							sheet.addCell(new Label(6, row, String.valueOf(regionTotal),wcf));
							
							++row;
							sheet.addCell(new Label(0, row, cur_org + "合计",wcf));
							sheet.addCell(new Label(6, row, String.valueOf(orgTotal), wcf));
						}
					}	
				}		
				++row;
				sheet.addCell(new Label(0, row, "合计",wcf));
				sheet.addCell(new Label(6, row, String.valueOf(total), wcf));		
			}			
			
			workbook.write();
			workbook.close();
			os.flush();
			os.close();
			
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"常规订单超时统计明细表下载");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 常规订单资源延时满足明细表初始化
	 * */
	public void getDelayDetailInit() {
		try {
			//查询目标年份
			Calendar calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH);
			act.setOutData("year", year);
			act.setOutData("month", month+1);
			act.setForword(OrderDelayDetailUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
			ErrorCodeConstant.QUERY_FAILURE_CODE, "常规订单资源延时满足明细表初始化！");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 常规订单资源延时满足明细表下载
	 * */
	public void getDelayDetailDownload() {
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String startYear = CommonUtils.checkNull(request.getParamValue("startYear"));
			String startMonth = CommonUtils.checkNull(request.getParamValue("startMonth"));
			String endYear = CommonUtils.checkNull(request.getParamValue("endYear"));
			String endMonth = CommonUtils.checkNull(request.getParamValue("endMonth"));
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
			String orgCode = CommonUtils.checkNull(request.getParamValue("orgCode"));
			String orderNo = CommonUtils.checkNull(request.getParamValue("orderNo"));
			String deliveryNo = CommonUtils.checkNull(request.getParamValue("deliveryNo"));
			
			
			Map<String, String> map = new HashMap<String, String>();
			map.put("startYear", startYear);
			map.put("startMonth", startMonth);
			map.put("endYear", endYear);
			map.put("endMonth", endMonth);
			map.put("dealerId", dealerId);
			map.put("dealerCode", dealerCode);
			map.put("orgId", orgId);
			map.put("orgCode", orgCode);
			map.put("orderNo", orderNo);
			map.put("deliveryNo", deliveryNo);
			
			
			// 常规订单资源延时满足汇总表调用存储过程p_yq_report_dtl
			dao.callProcedure("p_yq_report_dtl", null, null);
			
			// 导出的文件名
			String fileName = "常规订单资源延时满足明细表.xls";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="+ fileName);
			
			OutputStream os = response.getOutputStream();
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			WritableSheet sheet = workbook.createSheet("常规订单资源延时满足明细表", 0);
			WritableCellFormat wcf = new WritableCellFormat();
			wcf.setAlignment(Alignment.CENTRE);
			
			Calendar  calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH)+1;
			int day =calendar.get(Calendar.DAY_OF_MONTH);
			
			act.setOutData("year", year);
			act.setOutData("month", month);
			act.setOutData("day", day);
			
			List<Map<String, Object>> reportList = dao.getDealyDetailReport(map);

			int len = reportList.size();
			//列数
			int colCount = 8;
			
			//经销商合计
			BigDecimal dealerAmount = BigDecimal.ZERO;
			//省份合计
			BigDecimal regionAmount = BigDecimal.ZERO;
			//大区合计
			BigDecimal orgAmount = BigDecimal.ZERO;
			//全国总计
			BigDecimal totalAmount = BigDecimal.ZERO;
			
			int row = 0;
			
			
			sheet.mergeCells(0, row, colCount, row);
			sheet.addCell(new  Label(0, row, "常规订单资源延时满足明细表",wcf));
			
			
			/*++row;
			sheet.mergeCells(0, row, colCount, row);
			sheet.addCell(new  Label(0, row, "统计日期："+startYear+"年"+ startMonth +  "月  ~ " +  endYear + "年" +endMonth + "月"));
			
			*/
			
			
			++row;
			sheet.addCell(new  Label(0, row, "大区"));
			sheet.addCell(new  Label(1, row, "省份"));
			sheet.addCell(new  Label(2, row, "单位名称"));
			sheet.addCell(new  Label(3, row, "订单周度"));
			sheet.addCell(new  Label(4, row, "常规订单号"));
			sheet.addCell(new  Label(5, row, "发运申请单号"));
			sheet.addCell(new  Label(6, row, "车型编码"));
			sheet.addCell(new  Label(7, row, "数量"));
			sheet.addCell(new  Label(8, row, "总超期天数"));
			
			
			if(reportList != null && len != 0){
				for(int i=0;i<len;i++){
					BigDecimal amount = ((BigDecimal)reportList.get(i).get("AMOUNT"));
					if(i==0){//首条记录
						dealerAmount = amount;
						regionAmount = amount;
						orgAmount = amount;
						totalAmount = amount;
						++row;
						sheet.addCell(new  Label(0, row, reportList.get(i).get("ORG_NAME")==null?"":reportList.get(i).get("ORG_NAME").toString()));
						sheet.addCell(new  Label(1, row, reportList.get(i).get("REGION_NAME")==null?"":reportList.get(i).get("REGION_NAME").toString()));
						sheet.addCell(new  Label(2, row, reportList.get(i).get("DEALER_NAME")==null?"":reportList.get(i).get("DEALER_NAME").toString()));
						sheet.addCell(new  Label(3, row, reportList.get(i).get("ORDER_WEEKLY")==null?"":reportList.get(i).get("ORDER_WEEKLY").toString()));
						sheet.addCell(new  Label(4, row, reportList.get(i).get("ORDER_NO")==null?"":reportList.get(i).get("ORDER_NO").toString()));
						sheet.addCell(new  Label(5, row, reportList.get(i).get("DELIVERY_NO")==null?"":reportList.get(i).get("DELIVERY_NO").toString()));
						sheet.addCell(new  Label(6, row, reportList.get(i).get("MATERIAL_CODE")==null?"":reportList.get(i).get("MATERIAL_CODE").toString()));
						sheet.addCell(new  Label(7, row, reportList.get(i).get("AMOUNT")==null?"":reportList.get(i).get("AMOUNT").toString()));
						sheet.addCell(new  Label(8, row, reportList.get(i).get("TOTAL_OVERTIME_DAYS")==null?"":reportList.get(i).get("TOTAL_OVERTIME_DAYS").toString()));
					}else{
						
						String before_region = reportList.get(i-1).get("REGION_NAME").toString();
						String cur_region = reportList.get(i).get("REGION_NAME").toString();
						
						String before_org = reportList.get(i-1).get("ORG_NAME").toString();
						String cur_org = reportList.get(i).get("ORG_NAME").toString();
						
						if(cur_region.equals(before_region) && cur_org.equals(before_org)){//大区和省份相同
							
							String before_dealer = reportList.get(i-1).get("DEALER_NAME").toString();
							String cur_dealer = reportList.get(i).get("DEALER_NAME").toString();
							if(!before_dealer.equals(cur_dealer)) {
								++row;
								sheet.mergeCells(0, row, 2, row);
								sheet.addCell(new Label(0, row, before_dealer + "合计",wcf));
								sheet.addCell(new Label(7, row, String.valueOf(dealerAmount),wcf));
								dealerAmount = amount;
							} else {
								dealerAmount = dealerAmount.add(amount);
							}
							regionAmount = regionAmount.add(amount);
							orgAmount = orgAmount.add(amount);
							totalAmount = totalAmount.add(amount);
							++row;
							sheet.addCell(new  Label(0, row, reportList.get(i).get("ORG_NAME")==null?"":reportList.get(i).get("ORG_NAME").toString()));
							sheet.addCell(new  Label(1, row, reportList.get(i).get("REGION_NAME")==null?"":reportList.get(i).get("REGION_NAME").toString()));
							sheet.addCell(new  Label(2, row, reportList.get(i).get("DEALER_NAME")==null?"":reportList.get(i).get("DEALER_NAME").toString()));
							sheet.addCell(new  Label(3, row, reportList.get(i).get("ORDER_WEEKLY")==null?"":reportList.get(i).get("ORDER_WEEKLY").toString()));
							sheet.addCell(new  Label(4, row, reportList.get(i).get("ORDER_NO")==null?"":reportList.get(i).get("ORDER_NO").toString()));
							sheet.addCell(new  Label(5, row, reportList.get(i).get("DELIVERY_NO")==null?"":reportList.get(i).get("DELIVERY_NO").toString()));
							sheet.addCell(new  Label(6, row, reportList.get(i).get("MATERIAL_CODE")==null?"":reportList.get(i).get("MATERIAL_CODE").toString()));
							sheet.addCell(new  Label(7, row, reportList.get(i).get("AMOUNT")==null?"":reportList.get(i).get("AMOUNT").toString()));
							sheet.addCell(new  Label(8, row, reportList.get(i).get("TOTAL_OVERTIME_DAYS")==null?"":reportList.get(i).get("TOTAL_OVERTIME_DAYS").toString()));
						} else {//大区或省份不同
							
							String before_dealer = reportList.get(i-1).get("DEALER_NAME").toString();
							String cur_dealer = reportList.get(i).get("DEALER_NAME").toString();
							if(!before_dealer.equals(cur_dealer)) {//经销商不同
								++row;
								sheet.mergeCells(0, row, 2, row);
								sheet.addCell(new Label(0, row, before_dealer + "合计",wcf));
								sheet.addCell(new Label(7, row, String.valueOf(dealerAmount),wcf));
								dealerAmount = amount;
							} 
									
							
							//大区
							before_org = reportList.get(i-1).get("ORG_NAME").toString();
							cur_org = reportList.get(i).get("ORG_NAME").toString();
							
							if(cur_org.equals(before_org)){//大区相同，省份不同
								++row;
								sheet.mergeCells(0, row, 1, row);
								sheet.addCell(new Label(0, row, before_region + "合计",wcf));
								sheet.addCell(new Label(7, row, String.valueOf(regionAmount), wcf));
								orgAmount = amount;
								totalAmount = totalAmount.add(amount);
							}else{//大区不同
								++row;
								sheet.mergeCells(0, row, 1, row);
								sheet.addCell(new Label(0, row, before_region + "合计",wcf));
								sheet.addCell(new Label(7, row, String.valueOf(regionAmount), wcf));
								++row;
								sheet.addCell(new Label(0, row, before_org + "合计",wcf));
								sheet.addCell(new Label(7, row, String.valueOf(orgAmount), wcf));
								regionAmount = amount;
								orgAmount = amount;
							}
							totalAmount = totalAmount.add(amount);
							++row;
							sheet.addCell(new  Label(0, row, reportList.get(i).get("ORG_NAME")==null?"":reportList.get(i).get("ORG_NAME").toString()));
							sheet.addCell(new  Label(1, row, reportList.get(i).get("REGION_NAME")==null?"":reportList.get(i).get("REGION_NAME").toString()));
							sheet.addCell(new  Label(2, row, reportList.get(i).get("DEALER_NAME")==null?"":reportList.get(i).get("DEALER_NAME").toString()));
							sheet.addCell(new  Label(3, row, reportList.get(i).get("ORDER_WEEKLY")==null?"":reportList.get(i).get("ORDER_WEEKLY").toString()));
							sheet.addCell(new  Label(4, row, reportList.get(i).get("ORDER_NO")==null?"":reportList.get(i).get("ORDER_NO").toString()));
							sheet.addCell(new  Label(5, row, reportList.get(i).get("DELIVERY_NO")==null?"":reportList.get(i).get("DELIVERY_NO").toString()));
							sheet.addCell(new  Label(6, row, reportList.get(i).get("MATERIAL_CODE")==null?"":reportList.get(i).get("MATERIAL_CODE").toString()));
							sheet.addCell(new  Label(7, row, reportList.get(i).get("AMOUNT")==null?"":reportList.get(i).get("AMOUNT").toString()));
							sheet.addCell(new  Label(8, row, reportList.get(i).get("TOTAL_OVERTIME_DAYS")==null?"":reportList.get(i).get("TOTAL_OVERTIME_DAYS").toString()));
						}
						if(i == len-1) {//最后一条数据
							
							String cur_dealer = reportList.get(i).get("DEALER_NAME").toString();
							
							++row;
							sheet.mergeCells(0, row, 2, row);
							sheet.addCell(new Label(0, row, cur_dealer + "合计",wcf));
							sheet.addCell(new Label(7, row, String.valueOf(dealerAmount),wcf));
							
							++row;
							sheet.mergeCells(0, row, 1, row);
							sheet.addCell(new Label(0, row, cur_region + "合计",wcf));
							sheet.addCell(new Label(7, row, String.valueOf(regionAmount),wcf));
							
							++row;
							sheet.addCell(new Label(0, row, cur_org + "合计",wcf));
							sheet.addCell(new Label(7, row, String.valueOf(orgAmount), wcf));
						}
					}	
				}		
				++row;
				sheet.addCell(new Label(0, row, "合计",wcf));
				sheet.addCell(new Label(7, row, String.valueOf(totalAmount), wcf));		
			}			
			
			workbook.write();
			workbook.close();
			os.flush();
			os.close();
			
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"常规订单超时统计明细表下载");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 常规订单资源延时满足汇总表初始化
	 * */
	public void getDelaySummaryInit() {
		try {
			//查询目标年份
			Calendar calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH);
			act.setOutData("year", year);
			act.setOutData("month", month+1);
			act.setForword(OrderDelaySummaryUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
			ErrorCodeConstant.QUERY_FAILURE_CODE, "常规订单资源延时满足汇总表初始化！");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 常规订单资源延时满足汇总表下载
	 * */
	public void getDelaySummaryDownload() {
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String startYear = CommonUtils.checkNull(request.getParamValue("startYear"));
			String startMonth = CommonUtils.checkNull(request.getParamValue("startMonth"));
			String endYear = CommonUtils.checkNull(request.getParamValue("endYear"));
			String endMonth = CommonUtils.checkNull(request.getParamValue("endMonth"));
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
			String orgCode = CommonUtils.checkNull(request.getParamValue("orgCode"));
			
			
			Map<String, String> map = new HashMap<String, String>();
			map.put("startYear", startYear);
			map.put("startMonth", startMonth);
			map.put("endYear", endYear);
			map.put("endMonth", endMonth);
			map.put("dealerId", dealerId);
			map.put("dealerCode", dealerCode);
			map.put("orgId", orgId);
			map.put("orgCode", orgCode);
			
			
			// 常规订单资源延时满足汇总表调用存储过程p_yq_report_dtl
			dao.callProcedure("p_yq_report_dtl", null, null);
			
			// 导出的文件名
			String fileName = "常规订单资源延时满足汇总表.xls";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="+ fileName);
			
			OutputStream os = response.getOutputStream();
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			WritableSheet sheet = workbook.createSheet("常规订单资源延时满足汇总表", 0);
			WritableCellFormat wcf = new WritableCellFormat();
			wcf.setAlignment(Alignment.CENTRE);
			
			Calendar  calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH)+1;
			int day =calendar.get(Calendar.DAY_OF_MONTH);
			
			act.setOutData("year", year);
			act.setOutData("month", month);
			act.setOutData("day", day);
			
			List<Map<String, Object>> reportList = dao.getDealySummaryReport(map);

			int len = reportList.size();
			//列数
			int colCount = 6;
			
			//经销商合计
			BigDecimal dealerAmount = BigDecimal.ZERO;
			//省份合计
			BigDecimal regionAmount = BigDecimal.ZERO;
			//大区合计
			BigDecimal orgAmount = BigDecimal.ZERO;
			//全国总计
			BigDecimal totalAmount = BigDecimal.ZERO;
			
			int row = 0;
			
			
			sheet.mergeCells(0, row, colCount, row);
			sheet.addCell(new  Label(0, row, "常规订单资源延时满足汇总表",wcf));
			
			/*
			++row;
			sheet.mergeCells(0, row, colCount, row);
			sheet.addCell(new  Label(0, row, "统计日期："+startYear+"年"+ startMonth +  "月  ~ " +  endYear + "年" +endMonth + "月"));
			*/
			
			
			
			++row;
			sheet.addCell(new  Label(0, row, "大区"));
			sheet.addCell(new  Label(1, row, "省份"));
			sheet.addCell(new  Label(2, row, "单位名称"));
			sheet.addCell(new  Label(3, row, "订单周度"));
			sheet.addCell(new  Label(4, row, "物料编码"));
			sheet.addCell(new  Label(5, row, "数量"));
			sheet.addCell(new  Label(6, row, "总超期天数"));
			
			
			if(reportList != null && len != 0){
				for(int i=0;i<len;i++){
					BigDecimal amount = ((BigDecimal)reportList.get(i).get("AMOUNT"));
					if(i==0){//首条记录
						dealerAmount = amount;
						regionAmount = amount;
						orgAmount = amount;
						totalAmount = amount;
						++row;
						sheet.addCell(new  Label(0, row, reportList.get(i).get("ORG_NAME")==null?"":reportList.get(i).get("ORG_NAME").toString()));
						sheet.addCell(new  Label(1, row, reportList.get(i).get("REGION_NAME")==null?"":reportList.get(i).get("REGION_NAME").toString()));
						sheet.addCell(new  Label(2, row, reportList.get(i).get("DEALER_NAME")==null?"":reportList.get(i).get("DEALER_NAME").toString()));
						sheet.addCell(new  Label(3, row, reportList.get(i).get("ORDER_WEEKLY")==null?"":reportList.get(i).get("ORDER_WEEKLY").toString()));
						sheet.addCell(new  Label(4, row, reportList.get(i).get("MATERIAL_CODE")==null?"":reportList.get(i).get("MATERIAL_CODE").toString()));
						sheet.addCell(new  Label(5, row, reportList.get(i).get("AMOUNT")==null?"":reportList.get(i).get("AMOUNT").toString()));
						sheet.addCell(new  Label(6, row, reportList.get(i).get("TOTAL_OVERTIME_DAYS")==null?"":reportList.get(i).get("TOTAL_OVERTIME_DAYS").toString()));
					}else{
						
						String before_region = reportList.get(i-1).get("REGION_NAME").toString();
						String cur_region = reportList.get(i).get("REGION_NAME").toString();
						
						String before_org = reportList.get(i-1).get("ORG_NAME").toString();
						String cur_org = reportList.get(i).get("ORG_NAME").toString();
						
						if(cur_region.equals(before_region) && cur_org.equals(before_org)){//大区和省份相同
							
							String before_dealer = reportList.get(i-1).get("DEALER_NAME").toString();
							String cur_dealer = reportList.get(i).get("DEALER_NAME").toString();
							if(!before_dealer.equals(cur_dealer)) {
								++row;
								sheet.mergeCells(0, row, 2, row);
								sheet.addCell(new Label(0, row, before_dealer + "合计",wcf));
								sheet.addCell(new Label(5, row, String.valueOf(dealerAmount),wcf));
								dealerAmount = amount;
							} else {
								dealerAmount = dealerAmount.add(amount);
							}
							regionAmount = regionAmount.add(amount);
							orgAmount = orgAmount.add(amount);
							totalAmount = totalAmount.add(amount);
							++row;
							sheet.addCell(new  Label(0, row, reportList.get(i).get("ORG_NAME")==null?"":reportList.get(i).get("ORG_NAME").toString()));
							sheet.addCell(new  Label(1, row, reportList.get(i).get("REGION_NAME")==null?"":reportList.get(i).get("REGION_NAME").toString()));
							sheet.addCell(new  Label(2, row, reportList.get(i).get("DEALER_NAME")==null?"":reportList.get(i).get("DEALER_NAME").toString()));
							sheet.addCell(new  Label(3, row, reportList.get(i).get("ORDER_WEEKLY")==null?"":reportList.get(i).get("ORDER_WEEKLY").toString()));
							sheet.addCell(new  Label(4, row, reportList.get(i).get("MATERIAL_CODE")==null?"":reportList.get(i).get("MATERIAL_CODE").toString()));
							sheet.addCell(new  Label(5, row, reportList.get(i).get("AMOUNT")==null?"":reportList.get(i).get("AMOUNT").toString()));
							sheet.addCell(new  Label(6, row, reportList.get(i).get("TOTAL_OVERTIME_DAYS")==null?"":reportList.get(i).get("TOTAL_OVERTIME_DAYS").toString()));
						} else {//大区或省份不同
							
							String before_dealer = reportList.get(i-1).get("DEALER_NAME").toString();
							String cur_dealer = reportList.get(i).get("DEALER_NAME").toString();
							if(!before_dealer.equals(cur_dealer)) {//经销商不同
								++row;
								sheet.mergeCells(0, row, 2, row);
								sheet.addCell(new Label(0, row, before_dealer + "合计",wcf));
								sheet.addCell(new Label(5, row, String.valueOf(dealerAmount),wcf));
								dealerAmount = amount;
							} 
									
							
							//大区
							before_org = reportList.get(i-1).get("ORG_NAME").toString();
							cur_org = reportList.get(i).get("ORG_NAME").toString();
							
							if(cur_org.equals(before_org)){//大区相同，省份不同
								++row;
								sheet.mergeCells(0, row, 1, row);
								sheet.addCell(new Label(0, row, before_region + "合计",wcf));
								sheet.addCell(new Label(5, row, String.valueOf(regionAmount), wcf));
								orgAmount = amount;
								totalAmount = totalAmount.add(amount);
							}else{//大区不同
								++row;
								sheet.mergeCells(0, row, 1, row);
								sheet.addCell(new Label(0, row, before_region + "合计",wcf));
								sheet.addCell(new Label(5, row, String.valueOf(regionAmount), wcf));
								++row;
								sheet.addCell(new Label(0, row, before_org + "合计",wcf));
								sheet.addCell(new Label(5, row, String.valueOf(orgAmount), wcf));
								regionAmount = amount;
								orgAmount = amount;
							}
							totalAmount = totalAmount.add(amount);
							++row;
							sheet.addCell(new  Label(0, row, reportList.get(i).get("ORG_NAME")==null?"":reportList.get(i).get("ORG_NAME").toString()));
							sheet.addCell(new  Label(1, row, reportList.get(i).get("REGION_NAME")==null?"":reportList.get(i).get("REGION_NAME").toString()));
							sheet.addCell(new  Label(2, row, reportList.get(i).get("DEALER_NAME")==null?"":reportList.get(i).get("DEALER_NAME").toString()));
							sheet.addCell(new  Label(3, row, reportList.get(i).get("ORDER_WEEKLY")==null?"":reportList.get(i).get("ORDER_WEEKLY").toString()));
							sheet.addCell(new  Label(4, row, reportList.get(i).get("MATERIAL_CODE")==null?"":reportList.get(i).get("MATERIAL_CODE").toString()));
							sheet.addCell(new  Label(5, row, reportList.get(i).get("AMOUNT")==null?"":reportList.get(i).get("AMOUNT").toString()));
							sheet.addCell(new  Label(6, row, reportList.get(i).get("TOTAL_OVERTIME_DAYS")==null?"":reportList.get(i).get("TOTAL_OVERTIME_DAYS").toString()));
						}
						if(i == len-1) {//最后一条数据
							
							String cur_dealer = reportList.get(i).get("DEALER_NAME").toString();
							
							++row;
							sheet.mergeCells(0, row, 2, row);
							sheet.addCell(new Label(0, row, cur_dealer + "合计",wcf));
							sheet.addCell(new Label(5, row, String.valueOf(dealerAmount),wcf));
							
							++row;
							sheet.mergeCells(0, row, 1, row);
							sheet.addCell(new Label(0, row, cur_region + "合计",wcf));
							sheet.addCell(new Label(5, row, String.valueOf(regionAmount),wcf));
							
							++row;
							sheet.addCell(new Label(0, row, cur_org + "合计",wcf));
							sheet.addCell(new Label(5, row, String.valueOf(orgAmount), wcf));
						}
					}	
				}		
				++row;
				sheet.addCell(new Label(0, row, "合计",wcf));
				sheet.addCell(new Label(5, row, String.valueOf(totalAmount), wcf));		
			}			
			
			workbook.write();
			workbook.close();
			os.flush();
			os.close();
			
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"常规订单资源延时满足汇总表下载");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
