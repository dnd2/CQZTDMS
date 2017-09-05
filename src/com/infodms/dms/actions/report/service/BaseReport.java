package com.infodms.dms.actions.report.service;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.common.AjaxSelectDao;
import com.infodms.dms.dao.report.serviceReport.BaseReportDao;
import com.infodms.dms.dao.report.serviceReport.ClaimOldSignDelDao;
import com.infodms.dms.dao.report.serviceReport.ClaimReportDao;
import com.infodms.dms.dao.tccode.TcCodeDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

public class BaseReport {
	private Logger logger = Logger.getLogger(BaseReport.class);
	private final BaseReportDao cdao = BaseReportDao.getInstance();
	private final ClaimReportDao dao  = ClaimReportDao.getInstance();
	private static final AjaxSelectDao ajaxDao = AjaxSelectDao.getInstance();
	private static final TcCodeDao tcDao = TcCodeDao.getInstance();

    //平均故障里程   
	private final String Average_Fault_Distance = "/jsp/report/service/Average_Fault_Distance.jsp";
	//维修频次
	private final String Maintenance_Frequency = "/jsp/report/service/Maintenance_Frequency.jsp"; 
	//出门证及索赔通知单
	private final String Shipper_Claim = "/jsp/report/service/Shipper_Claim.jsp"; 
	//二次索赔开票
	private final String TWO_CLAIM_INVOICE = "/jsp/report/service/TWO_CLAIM_INVOICE.jsp"; 
	//二次索赔开票汇总
	private final String TWO_CLAIM_INVOICE_TOTAL = "/jsp/report/service/TWO_CLAIM_INVOICE_TOTAL.jsp"; 
	//结算数量金额明细
	private final String SETTLEMENT_AMOUNT_SUBSIDIARY = "/jsp/report/service/Settlement_Amount_Subsidiary.jsp"; 
	//结算汇总
	private final String SETTLEMENT_SUMMARY = "/jsp/report/service/Settlement_Summary.jsp"; 
	//结算汇总明细
	private final String SETTLEMENT_SUMMARY_Del = "/jsp/report/service/Settlement_Summary_Del.jsp"; 
	private final String SETTLEMENTWORKLOAD = "/jsp/report/service/Settlement_Workload.jsp"; 
	private final String SETTLEMENTCOUNT = "/jsp/report/service/Settlement_Count.jsp"; 
	

	ActionContext act = ActionContext.getContext();
 	AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
 	RequestWrapper request = act.getRequest();
 	
 	/**
 	 * 初始化平均故障里程
 	 */
 	public void AverageFaultDistanceInit(){

    	try {
			List<Map<String, Object>> list = dao.getBigOrgList();
			List<Map<String, Object>> serisList = ajaxDao.getSerisList(logonUser.getPoseId().toString(), 2);
			act.setOutData("serislist", serisList);
			act.setOutData("list", list);
    		act.setForword(Average_Fault_Distance);
    		
    	} catch (Exception e) {
    		BizException e1 = new BizException(act, e,
    				ErrorCodeConstant.QUERY_FAILURE_CODE, "初始化平均故障里程");
    		logger.error(logonUser, e1);
    		act.setException(e1);
    	}
 		
 	}
 	
 	
 	public void SettlementCount(){

    	try {
    		List<Map<String, Object>> list = dao.getBigOrgList();
			List<Map<String, Object>> serisList = ajaxDao.getSerisList(logonUser.getPoseId().toString(), 2);
			act.setOutData("serislist", serisList);
			act.setOutData("list", list);
    		act.setForword(SETTLEMENTCOUNT);
    		
    	} catch (Exception e) {
    		BizException e1 = new BizException(act, e,
    				ErrorCodeConstant.QUERY_FAILURE_CODE, "初始化平均故障里程");
    		logger.error(logonUser, e1);
    		act.setException(e1);
    	}
 		
 	}
 	
 	
 	
	public void SettlementWorkload(){

    	try {
    		List<Map<String, Object>> list = dao.getSmallOrgList("");
			act.setOutData("list", list);
    		act.setForword(SETTLEMENTWORKLOAD);
    	} catch (Exception e) {
    		BizException e1 = new BizException(act, e,
    				ErrorCodeConstant.QUERY_FAILURE_CODE, "初始化平均故障里程");
    		logger.error(logonUser, e1);
    		act.setException(e1);
    	}
 		
 	}
 	
	public void SettlementWorkloadQueryData(){

 		try {
 			Map<String, String> map = new HashMap<String, String>();

 			String YIELDLY_TYPE = CommonUtils.checkNull(request.getParamValue("YIELDLY_TYPE"));
 			String bDate = CommonUtils.checkNull(request.getParamValue("bDate"));
 			String eDate = CommonUtils.checkNull(request.getParamValue("eDate"));
 			String supply_code = CommonUtils.checkNull(request.getParamValue("supply_code"));
 			String supply_name = CommonUtils.checkNull(request.getParamValue("supply_name"));
 			String small_org = CommonUtils.checkNull(request.getParamValue("small_org"));
 			map.put("YIELDLY_TYPE", YIELDLY_TYPE);
 			map.put("bDate", bDate);
 			map.put("eDate", eDate);
 			map.put("supply_code", supply_code);
 			map.put("supply_name", supply_name);
 			map.put("small_org", small_org);
 			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
 					.getParamValue("curPage")) : 1;
 			PageResult<Map<String, Object>> ps=cdao.SettlementWorkloadQueryData(map, Constant.PAGE_SIZE, curPage);
 			act.setOutData("ps", ps);
 		} catch (Exception e) {
 			BizException e1 = new BizException(act, e,
 					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算室审核工作量查询报错");
 			logger.error(logonUser, e1);
 			act.setException(e1);
 		}

 		}
 	
	
	public void SettlementCountQueryData(){

 		try {
 			Map<String, String> map = new HashMap<String, String>();

 			String YIELDLY_TYPE = CommonUtils.checkNull(request.getParamValue("YIELDLY_TYPE"));
 			String bDate = CommonUtils.checkNull(request.getParamValue("bDate"));
 			String eDate = CommonUtils.checkNull(request.getParamValue("eDate"));
 			String supply_code = CommonUtils.checkNull(request.getParamValue("supply_code"));
 			String supply_name = CommonUtils.checkNull(request.getParamValue("supply_name"));
 			String small_org = CommonUtils.checkNull(request.getParamValue("small_org"));
 			String big_org = CommonUtils.checkNull(request.getParamValue("big_org"));
 			map.put("YIELDLY_TYPE", YIELDLY_TYPE);
 			map.put("bDate", bDate);
 			map.put("eDate", eDate);
 			map.put("supply_code", supply_code);
 			map.put("supply_name", supply_name);
 			map.put("small_org", small_org);
 			map.put("big_org", big_org);
 			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
 					.getParamValue("curPage")) : 1;
 			PageResult<Map<String, Object>> ps=cdao.SettlementCountQueryData(map, Constant.PAGE_SIZE, curPage);
 			act.setOutData("ps", ps);
 		} catch (Exception e) {
 			BizException e1 = new BizException(act, e,
 					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算室审核工作量查询报错");
 			logger.error(logonUser, e1);
 			act.setException(e1);
 		}

 		}
 	
	
	
	
 	
 	/**
 	 * 平均故障里程查询页面
 	 */
 	public void AverageFaultDistanceQueryData(){

 		try {
 			Map<String, String> map = new HashMap<String, String>();

 			String serisid = CommonUtils.checkNull(request.getParamValue("serisid"));//车型ID
 			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));//车型code
 			String bDate = CommonUtils.checkNull(request.getParamValue("bDate"));//开始日期
 			String eDate = CommonUtils.checkNull(request.getParamValue("eDate"));//结束日期
 			map.put("serisid", serisid);
 			map.put("groupCode", groupCode);
 			map.put("bDate", bDate);
 			map.put("eDate", eDate);
 			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
 					.getParamValue("curPage")) : 1;
 			PageResult<Map<String, Object>> ps=cdao.QueryAverageFaultDistance(map, Constant.PAGE_SIZE, curPage);
 			act.setOutData("ps", ps);
 		} catch (Exception e) {
 			BizException e1 = new BizException(act, e,
 					ErrorCodeConstant.QUERY_FAILURE_CODE, "平均故障里程查询页面");
 			logger.error(logonUser, e1);
 			act.setException(e1);
 		}

 		}
 	
 	
 	
 	
 	
 	
 	public void SettlementCountExport(){
 		OutputStream os = null;
 		try {
 			Map<String, String> map = new HashMap<String, String>();

 			String YIELDLY_TYPE = CommonUtils.checkNull(request.getParamValue("YIELDLY_TYPE"));
 			String bDate = CommonUtils.checkNull(request.getParamValue("bDate"));
 			String eDate = CommonUtils.checkNull(request.getParamValue("eDate"));
 			String supply_code = CommonUtils.checkNull(request.getParamValue("supply_code"));
 			String supply_name = CommonUtils.checkNull(request.getParamValue("supply_name"));
 			String small_org = CommonUtils.checkNull(request.getParamValue("small_org"));
 			String big_org = CommonUtils.checkNull(request.getParamValue("big_org"));
 			map.put("YIELDLY_TYPE", YIELDLY_TYPE);
 			map.put("bDate", bDate);
 			map.put("eDate", eDate);
 			map.put("supply_code", supply_code);
 			map.put("supply_name", supply_name);
 			map.put("small_org", small_org);
 			map.put("big_org", big_org);
 			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
 					.getParamValue("curPage")) : 1;
 			PageResult<Map<String, Object>> ps=cdao.SettlementCountQueryData(map, 999999, curPage);
 			
 			ResponseWrapper response = act.getResponse();
 			// 导出的文件名
 			String fileName = "各服务商结算汇总.xls";
 			// 导出的文字编码
 			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
 			response.setContentType("Application/text/csv");
 			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
 			// 定义一个集合
 			List<List<Object>> list = new LinkedList<List<Object>>();
 			// 标题
 			List<Object> listTemp = new LinkedList<Object>();
 			listTemp.add("服务站代码");
 			listTemp.add("上报年月");
 			listTemp.add("结算编号");
 			listTemp.add("服务站名称");
 			listTemp.add("正常维修数量");
 			listTemp.add("售前单数量");
 			listTemp.add("特殊单数量");
 			listTemp.add("外派单数量");
 			listTemp.add("工时费");
 			listTemp.add("材料费");
 			listTemp.add("派出服务费");
 			listTemp.add("强保数量");
 			listTemp.add("强保工时费");
 			listTemp.add("强保材料费");
 			listTemp.add("强保金额合计");
 			listTemp.add("活动数量");
 			listTemp.add("活动工时费");
 			listTemp.add("活动材料费");
 			listTemp.add("活动赠送费");
 			listTemp.add("活动金额合计");
 			listTemp.add("特殊费用数量");
 			listTemp.add("特殊费用工时费");
 			listTemp.add("特殊费用材料费");
 			listTemp.add("特殊费用金额合计");
 			listTemp.add("凭证补办数量");
 			listTemp.add("凭证补办金额合计");
 			listTemp.add("正负激励数量");
 			listTemp.add("正负激励工时费");
 			listTemp.add("正负激励材料费");
 			listTemp.add("运费");
 			listTemp.add("抵扣费用");
 			listTemp.add("总计单据总数");
 			listTemp.add("总计劳务费");
 			listTemp.add("总计材料费");
 			listTemp.add("总计金额总计");
 			list.add(listTemp);
 			List<Map<String, Object>> rslist = ps.getRecords();
 			Map<String, Object> map1 = new HashMap<String, Object>();

 			if(rslist!=null){

 			for (int i = 0; i < rslist.size(); i++) {
 				map1 = rslist.get(i);
 				List<Object> listValue = new LinkedList<Object>();

 				listValue.add(map1.get("DEALER_CODE") != null ? map1.get("DEALER_CODE") : "");
 				listValue.add(map1.get("REPORT_DATE") != null ? map1.get("REPORT_DATE") : "");
 				listValue.add(map1.get("BALANCE_ODER") != null ? map1.get("BALANCE_ODER") : "");
 				listValue.add(map1.get("ROOT_DEALER_NAME") != null ? map1.get("ROOT_DEALER_NAME") : "");
 				listValue.add(map1.get("CLA_TYPE_01") != null ? map1.get("CLA_TYPE_01") : "");
 				listValue.add(map1.get("CLA_TYPE_07") != null ? map1.get("CLA_TYPE_07") : "");
 				listValue.add(map1.get("CLA_TYPE_10") != null ? map1.get("CLA_TYPE_10") : "");
 				listValue.add(map1.get("CLA_TYPE_09") != null ? map1.get("CLA_TYPE_09") : "");

 				listValue.add(map1.get("BALANCE_LABOUR_AMOUNT") != null ? map1.get("BALANCE_LABOUR_AMOUNT") : "");
 				listValue.add(map1.get("BALANCE_PART_AMOUNT") != null ? map1.get("BALANCE_PART_AMOUNT") : "");
 				listValue.add(map1.get("BALANCE_NETITEM_AMOUNT") != null ? map1.get("BALANCE_NETITEM_AMOUNT") : "");
 				listValue.add(map1.get("CLA_TYPE_02") != null ? map1.get("CLA_TYPE_02") : "");

 				listValue.add(map1.get("LABOUR_PRICE") != null ? map1.get("LABOUR_PRICE") : "");
 				listValue.add(map1.get("PART_PRICE") != null ? map1.get("PART_PRICE") : "");
 				listValue.add(map1.get("COUNT_PRICE") != null ? map1.get("COUNT_PRICE") : "");
 				listValue.add(map1.get("CLA_TYPE_06") != null ? map1.get("CLA_TYPE_06") : "");

 				listValue.add(map1.get("BALANCE_LABOUR_AMOUNT_06") != null ? map1.get("BALANCE_LABOUR_AMOUNT_06") : "");
 				listValue.add(map1.get("BALANCE_PART_AMOUNT_06") != null ? map1.get("BALANCE_PART_AMOUNT_06") : "");
 				listValue.add(map1.get("BALANCE_NETITEM_AMOUNT_06") != null ? map1.get("BALANCE_NETITEM_AMOUNT_06") : "");
 				listValue.add(map1.get("BALANCE_AMOUNT_06") != null ? map1.get("BALANCE_AMOUNT_06") : "");
 				listValue.add(map1.get("SPEE_COUNT") != null ? map1.get("SPEE_COUNT") : "");
 				listValue.add(map1.get("SPEE_LABOUR") != null ? map1.get("SPEE_LABOUR") : "");

 				listValue.add(map1.get("SPEE_PART") != null ? map1.get("SPEE_PART") : "");
 				listValue.add(map1.get("SPEE_BALANCE") != null ? map1.get("SPEE_BALANCE") : "");
 				listValue.add(map1.get("PING_COUNT") != null ? map1.get("PING_COUNT") : "");
 				listValue.add(map1.get("PING_BALANCE") != null ? map1.get("PING_BALANCE") : "");

 				listValue.add(map1.get("FINE_COUNT") != null ? map1.get("FINE_COUNT") : "");
 				listValue.add(map1.get("FINE_LABOUR") != null ? map1.get("FINE_LABOUR") : "");
 				listValue.add(map1.get("FINE_PART") != null ? map1.get("FINE_PART") : "");
 				listValue.add(map1.get("YUNFEI") != null ? map1.get("YUNFEI") : "");
 				listValue.add(map1.get("DISCOUNT") != null ? map1.get("DISCOUNT") : "");
 				listValue.add(map1.get("COUNT_SUM") != null ? map1.get("COUNT_SUM") : "");
 				
 				listValue.add(map1.get("BALANCE_LABOUR_AMOUNT_COUNT") != null ? map1.get("BALANCE_LABOUR_AMOUNT_COUNT") : "");
 				listValue.add(map1.get("BALANCE_PART_AMOUNT_COUNT") != null ? map1.get("BALANCE_PART_AMOUNT_COUNT") : "");

 				listValue.add(map1.get("BALANCE_AMOUNT_COUNT") != null ? map1.get("BALANCE_AMOUNT_COUNT") : "");
 				
 				

 				list.add(listValue);
 			}
 			}
 			os = response.getOutputStream();
 			CsvWriterUtil.createXlsFileToName(list, os,"平均故障里程统计");
 			os.flush();	
 			
 			
 			
 		} catch (Exception e) {
 			BizException e1 = new BizException(act, e,
 					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算室审核工作量导出报错");
 			logger.error(logonUser, e1);
 			act.setException(e1);
 		}

 		}
 	
 	
 	
 	public void SettlementWorkloadExport(){
 		OutputStream os = null;
 		try {
 			Map<String, String> map = new HashMap<String, String>();

 			String YIELDLY_TYPE = CommonUtils.checkNull(request.getParamValue("YIELDLY_TYPE"));
 			String bDate = CommonUtils.checkNull(request.getParamValue("bDate"));
 			String eDate = CommonUtils.checkNull(request.getParamValue("eDate"));
 			String supply_code = CommonUtils.checkNull(request.getParamValue("supply_code"));
 			String supply_name = CommonUtils.checkNull(request.getParamValue("supply_name"));
 			String small_org = CommonUtils.checkNull(request.getParamValue("small_org"));
 			map.put("YIELDLY_TYPE", YIELDLY_TYPE);
 			map.put("bDate", bDate);
 			map.put("eDate", eDate);
 			map.put("supply_code", supply_code);
 			map.put("supply_name", supply_name);
 			map.put("small_org", small_org);
 			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
 					.getParamValue("curPage")) : 1;
 			PageResult<Map<String, Object>> ps=cdao.SettlementWorkloadQueryData(map, 999999, curPage);
 			
 			ResponseWrapper response = act.getResponse();
 			// 导出的文件名
 			String fileName = "结算室审核工作量.xls";
 			// 导出的文字编码
 			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
 			response.setContentType("Application/text/csv");
 			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
 			// 定义一个集合
 			List<List<Object>> list = new LinkedList<List<Object>>();
 			// 标题
 			List<Object> listTemp = new LinkedList<Object>();
 			listTemp.add("服务站代码");
 			listTemp.add("服务站名称");
 			listTemp.add("小区");
 			listTemp.add("索赔单数");
 			listTemp.add("特殊单数");
 			listTemp.add("单据合计");
 			listTemp.add("劳务费");
 			listTemp.add("材料费");
 			listTemp.add("救急费");
 			listTemp.add("费用合计");
 			listTemp.add("结算劳务费");
 			listTemp.add("结算材料费");
 			listTemp.add("结算救急费");
 			listTemp.add("结算合计");
 			listTemp.add("审去劳务费");
 			listTemp.add("审去材料费");
 			listTemp.add("审去救急费");
 			listTemp.add("审去合计");
 			list.add(listTemp);
 			List<Map<String, Object>> rslist = ps.getRecords();
 			Map<String, Object> map1 = new HashMap<String, Object>();

 			if(rslist!=null){

 			for (int i = 0; i < rslist.size(); i++) {
 				map1 = rslist.get(i);
 				List<Object> listValue = new LinkedList<Object>();

 				listValue.add(map1.get("DEALER_CODE") != null ? map1.get("DEALER_CODE") : "");
 				listValue.add(map1.get("DEALER_NAME") != null ? map1.get("DEALER_NAME") : "");
 				listValue.add(map1.get("REGION_NAME") != null ? map1.get("REGION_NAME") : "");
 				listValue.add(map1.get("APPSUM") != null ? map1.get("APPSUM") : "");
 				listValue.add(map1.get("SFEESUM") != null ? map1.get("SFEESUM") : "");
 				listValue.add(map1.get("COUNTSUM") != null ? map1.get("COUNTSUM") : "");
 				listValue.add(map1.get("LABOUR_AMOUNT") != null ? map1.get("LABOUR_AMOUNT") : "");
 				listValue.add(map1.get("PART_AMOUNT") != null ? map1.get("PART_AMOUNT") : "");

 				listValue.add(map1.get("NETITEM_AMOUNT") != null ? map1.get("NETITEM_AMOUNT") : "");
 				listValue.add(map1.get("REPAIR_TOTAL") != null ? map1.get("REPAIR_TOTAL") : "");
 				listValue.add(map1.get("BALANCE_LABOUR_AMOUNT") != null ? map1.get("BALANCE_LABOUR_AMOUNT") : "");
 				listValue.add(map1.get("BALANCE_PART_AMOUNT") != null ? map1.get("BALANCE_PART_AMOUNT") : "");

 				listValue.add(map1.get("BALANCE_NETITEM_AMOUNT") != null ? map1.get("BALANCE_NETITEM_AMOUNT") : "");
 				listValue.add(map1.get("BALANCE_AMOUNT") != null ? map1.get("BALANCE_AMOUNT") : "");
 				listValue.add(map1.get("DIS_LABOUR_AMOUNT") != null ? map1.get("DIS_LABOUR_AMOUNT") : "");
 				listValue.add(map1.get("DIS_PART_AMOUNT") != null ? map1.get("DIS_PART_AMOUNT") : "");

 				listValue.add(map1.get("DIS_NETITEM_AMOUNT") != null ? map1.get("DIS_NETITEM_AMOUNT") : "");
 				listValue.add(map1.get("DIS_AMOUNT") != null ? map1.get("DIS_AMOUNT") : "");

 				list.add(listValue);
 			}
 			}
 			os = response.getOutputStream();
 			CsvWriterUtil.createXlsFileToName(list, os,"平均故障里程统计");
 			os.flush();	
 			
 			
 			
 		} catch (Exception e) {
 			BizException e1 = new BizException(act, e,
 					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算室审核工作量导出报错");
 			logger.error(logonUser, e1);
 			act.setException(e1);
 		}

 		}
 	
 	
 	
 	/**
 	 * 平均故障里程导出页面
 	 */
 	public void AverageFaultDistanceExport(){
 		OutputStream os = null;

 		try {
 			Map<String, String> map = new HashMap<String, String>();
 			String serisid = CommonUtils.checkNull(request.getParamValue("serisid"));//车型ID
 			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));//车型code
 			String bDate = CommonUtils.checkNull(request.getParamValue("bDate"));//开始日期
 			String eDate = CommonUtils.checkNull(request.getParamValue("eDate"));//结束日期
 			map.put("serisid", serisid);
 			map.put("groupCode", groupCode);
 			map.put("bDate", bDate);
 			map.put("eDate", eDate);
 			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
 					.getParamValue("curPage")) : 1;
 			PageResult<Map<String, Object>> ps=cdao.QueryAverageFaultDistance(map, 999999, curPage);
 			act.setOutData("ps", ps);
 			ResponseWrapper response = act.getResponse();
 			// 导出的文件名
 			String fileName = "平均故障里程统计.xls";
 			// 导出的文字编码客户关怀活动数据明细表
 			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
 			response.setContentType("Application/text/csv");
 			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
 			// 定义一个集合
 			List<List<Object>> list = new LinkedList<List<Object>>();
 			// 标题
 			List<Object> listTemp = new LinkedList<Object>();
 			listTemp.add("车系");
 			listTemp.add("车型");
 			listTemp.add("平均首次故障公里数");
 			listTemp.add("平均故障间隔公里数");


 			list.add(listTemp);
 			List<Map<String, Object>> rslist = ps.getRecords();
 			Map<String, Object> map1 = new HashMap<String, Object>();

 			if(rslist!=null){

 			for (int i = 0; i < rslist.size(); i++) {
 				map1 = rslist.get(i);
 				List<Object> listValue = new LinkedList<Object>();

 				listValue.add(map1.get("SERIES") != null ? map1.get("SERIES") : "");
 				listValue.add(map1.get("MODEL") != null ? map1.get("MODEL") : "");
 				listValue.add(map1.get("F_MILEAGE") != null ? map1.get("F_MILEAGE") : "");
 				listValue.add(map1.get("D_MILEAGE") != null ? map1.get("D_MILEAGE") : "");

 				list.add(listValue);
 			}
 			}
 			os = response.getOutputStream();
 			CsvWriterUtil.createXlsFileToName(list, os,"平均故障里程统计");
 			os.flush();	

 		} catch (Exception e) {
 			BizException e1 = new BizException(act, e,
 					ErrorCodeConstant.QUERY_FAILURE_CODE, "导出");
 			logger.error(logonUser, e1);
 			act.setException(e1);
 		}

 	  }
 	/**
 	 * 初始化维修频次
 	 */
 	public void MaintenanceFrequencyInit(){

    	try {
			List<Map<String, Object>> serisList = ajaxDao.getSerisList(logonUser.getPoseId().toString(), 2);
			act.setOutData("serislist", serisList);
    		act.setForword(Maintenance_Frequency);
    		
    	} catch (Exception e) {
    		BizException e1 = new BizException(act, e,
    				ErrorCodeConstant.QUERY_FAILURE_CODE, "初始化维修频次统计");
    		logger.error(logonUser, e1);
    		act.setException(e1);
    	}
 		
 	}
 	/**
 	 * 维修频次查询页面
 	 */
 	public void MaintenanceFrequencyQueryData(){

 		try {
 			Map<String, String> map = new HashMap<String, String>();

 			String serisid = CommonUtils.checkNull(request.getParamValue("serisid"));//车型ID
 			//艾春2013.11.25修改查询条件, 获得配件代码
 			String partcode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));
// 			String partcode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));//partcode
 			//艾春2013.11.25修改查询条件, 获得配件代码
 			String clatype[] = request.getParamValues("CLA_TYPE");//索赔类型
 			String bDate = CommonUtils.checkNull(request.getParamValue("bDate"));//开始日期
 			String eDate = CommonUtils.checkNull(request.getParamValue("eDate"));//结束日期
 			String bgDate = CommonUtils.checkNull(request.getParamValue("bgDate"));//销售日期
 			map.put("serisid", serisid);
 			map.put("partcode", partcode);
 			String claType="";
 			if(clatype!=null&&clatype.length>0){
 			for(int i=0;i<clatype.length;i++){
 			  if(i==clatype.length-1){
 				 claType+=clatype[i];	  
 			  }else{
 				 claType+= clatype[i]+",";	  
 			  }	
 			}
 			}
 			map.put("clatype", claType);
 			map.put("bDate", bDate);
 			map.put("eDate", eDate);
 			map.put("bgDate", bgDate);
 			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
 					.getParamValue("curPage")) : 1;
 			PageResult<Map<String, Object>> ps=cdao.QueryMaintenanceFrequency(map, Constant.PAGE_SIZE, curPage);
 			act.setOutData("ps", ps);
 		} catch (Exception e) {
 			BizException e1 = new BizException(act, e,
 					ErrorCodeConstant.QUERY_FAILURE_CODE, "维修频次统计查询页面");
 			logger.error(logonUser, e1);
 			act.setException(e1);
 		}

 		}
 	
 	/**
 	 * 维修频次导出页面
 	 */
 	public void MaintenanceFrequencyExport(){
 		OutputStream os = null;

 		try {
 			Map<String, String> map = new HashMap<String, String>();
 			String serisid = CommonUtils.checkNull(request.getParamValue("serisid"));//车型ID
 			String partcode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));//partcode
 			String clatype[] = request.getParamValues("CLA_TYPE");//索赔类型
 			String bDate = CommonUtils.checkNull(request.getParamValue("bDate"));//开始日期
 			String eDate = CommonUtils.checkNull(request.getParamValue("eDate"));//结束日期
 			String bgDate = CommonUtils.checkNull(request.getParamValue("bgDate"));//销售日期
 			map.put("serisid", serisid);
 			map.put("partcode", partcode);
 			String claType="";
 			if(clatype!=null&&clatype.length>0){
 			for(int i=0;i<clatype.length;i++){
 			  if(i==clatype.length-1){
 				 claType+=clatype[i];	  
 			  }else{
 				 claType+= clatype[i]+",";	  
 			  }	
 			}
 			}
 			map.put("clatype", claType);
 			map.put("bDate", bDate);
 			map.put("eDate", eDate);
 			map.put("bgDate", bgDate);
 			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
 					.getParamValue("curPage")) : 1;
 			PageResult<Map<String, Object>> ps=cdao.QueryMaintenanceFrequency(map, 999999, curPage);
 			ResponseWrapper response = act.getResponse();
 			// 导出的文件名
 			String fileName = "维修频次统计.xls";
 			// 导出的文字编码客户关怀活动数据明细表
 			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
 			response.setContentType("Application/text/csv");
 			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
 			// 定义一个集合
 			List<List<Object>> list = new LinkedList<List<Object>>();
 			// 标题
 			List<Object> listTemp = new LinkedList<Object>();
 			listTemp.add("车系");
 			listTemp.add("销售总数");
 			listTemp.add("维修台次");
 			listTemp.add("总费用");
 			listTemp.add("单次频次");
 			listTemp.add("单车费用");


 			list.add(listTemp);
 			List<Map<String, Object>> rslist = ps.getRecords();
 			Map<String, Object> map1 = new HashMap<String, Object>();

 			if(rslist!=null){

 			for (int i = 0; i < rslist.size(); i++) {
 				map1 = rslist.get(i);
 				List<Object> listValue = new LinkedList<Object>();

 				listValue.add(map1.get("GROUP_NAME") != null ? map1.get("GROUP_NAME") : "");
 				listValue.add(map1.get("S_TOTAL") != null ? map1.get("S_TOTAL") : "");
 				listValue.add(map1.get("R_TOTAL") != null ? map1.get("R_TOTAL") : "");
 				listValue.add(map1.get("R_AMOUNT") != null ? map1.get("R_AMOUNT") : "");
 				listValue.add(map1.get("RD_TIMES") != null ? map1.get("RD_TIMES") : "");
 				listValue.add(map1.get("RD_AMOUNT") != null ? map1.get("RD_AMOUNT") : "");

 				list.add(listValue);
 			}
 			}
 			os = response.getOutputStream();
 			CsvWriterUtil.createXlsFileToName(list, os,"维修频次统计");
 			os.flush();	

 		} catch (Exception e) {
 			BizException e1 = new BizException(act, e,
 					ErrorCodeConstant.QUERY_FAILURE_CODE, "维修频次统计导出");
 			logger.error(logonUser, e1);
 			act.setException(e1);
 		}

 	  }
 	
 	/**
 	 * 初始化出门证及索赔通知单
 	 */
 	public void ShipperClaimInit(){

    	try {

    		act.setForword(Shipper_Claim);
    		
    	} catch (Exception e) {
    		BizException e1 = new BizException(act, e,
    				ErrorCodeConstant.QUERY_FAILURE_CODE, "出门证及索赔通知单");
    		logger.error(logonUser, e1);
    		act.setException(e1);
    	}
 		
 	}

 	
 	/**
 	 * 出门证及索赔通知单查询页面
 	 */
 	public void ShipperClaimQueryData(){

 		try {
 			Map<String, String> map = new HashMap<String, String>();

 			String yieldlyType = CommonUtils.checkNull(request.getParamValue("YIELDLY_TYPE"));//结算基地
 			String modelName = CommonUtils.checkNull(request.getParamValue("model_name"));//型号
 			String outCompany = CommonUtils.checkNull(request.getParamValue("out_company"));//出门单位
 			String noticeNo = CommonUtils.checkNull(request.getParamValue("notice_no"));//索赔通知单
 			String bDate = CommonUtils.checkNull(request.getParamValue("bDate"));//开始日期
 			String eDate = CommonUtils.checkNull(request.getParamValue("eDate"));//结束日期
 			map.put("yieldlyType", yieldlyType);
 			map.put("modelName", modelName);
 			map.put("outCompany", outCompany);
 			map.put("noticeNo", noticeNo);
 			map.put("bDate", bDate);
 			map.put("eDate", eDate);
 			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
 					.getParamValue("curPage")) : 1;
 			PageResult<Map<String, Object>> ps=cdao.QueryShipperClaimQuery(map, Constant.PAGE_SIZE, curPage);
 			act.setOutData("ps", ps);
 		} catch (Exception e) {
 			BizException e1 = new BizException(act, e,
 					ErrorCodeConstant.QUERY_FAILURE_CODE, "出门证及索赔通知单查询页面");
 			logger.error(logonUser, e1);
 			act.setException(e1);
 		}

 		}
 	
 	/**
 	 * 出门证及索赔通知单
 	 */
 	public void ShipperClaimExport(){
 		OutputStream os = null;

 		try {
 			Map<String, String> map = new HashMap<String, String>();

 			String yieldlyType = CommonUtils.checkNull(request.getParamValue("YIELDLY_TYPE"));//结算基地
 			String modelName = CommonUtils.checkNull(request.getParamValue("model_name"));//型号
 			String outCompany = CommonUtils.checkNull(request.getParamValue("out_company"));//出门单位
 			String noticeNo = CommonUtils.checkNull(request.getParamValue("notice_no"));//索赔通知单
 			String bDate = CommonUtils.checkNull(request.getParamValue("bDate"));//开始日期
 			String eDate = CommonUtils.checkNull(request.getParamValue("eDate"));//结束日期
 			map.put("yieldlyType", yieldlyType);
 			map.put("modelName", modelName);
 			map.put("outCompany", outCompany);
 			map.put("noticeNo", noticeNo);
 			map.put("bDate", bDate);
 			map.put("eDate", eDate);
 			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
 					.getParamValue("curPage")) : 1;
 			PageResult<Map<String, Object>> ps=cdao.QueryShipperClaimQuery(map, 999999, curPage);
 			ResponseWrapper response = act.getResponse();
 			// 导出的文件名
 			String fileName = "出门证及索赔通知单.xls";
 			// 导出的文字编码客户关怀活动数据明细表
 			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
 			response.setContentType("Application/text/csv");
 			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
 			// 定义一个集合
 			List<List<Object>> list = new LinkedList<List<Object>>();
 			// 标题
 			List<Object> listTemp = new LinkedList<Object>();

 			listTemp.add("出门证号");
 			listTemp.add("出门证生产时间(天)");
 			listTemp.add("出门单位");
 			listTemp.add("型号");
 			listTemp.add("配件代码");
 			listTemp.add("配件名称");
 			listTemp.add("出门证数量");
 			listTemp.add("索赔通知单号");
 			listTemp.add("开票时间");
 			listTemp.add("索赔数量");
 			listTemp.add("索赔单价");
 			listTemp.add("工时定额");
 			listTemp.add("金额总计");
 			listTemp.add("开票经办");
 			listTemp.add("备注");


 			list.add(listTemp);
 			List<Map<String, Object>> rslist = ps.getRecords();
 			Map<String, Object> map1 = new HashMap<String, Object>();

 			if(rslist!=null){

 			for (int i = 0; i < rslist.size(); i++) {
 				map1 = rslist.get(i);
 				List<Object> listValue = new LinkedList<Object>();

 				listValue.add(map1.get("OUT_NO") != null ? map1.get("OUT_NO") : "");
 				listValue.add(map1.get("DOOR_DATE") != null ? map1.get("DOOR_DATE") : "");
 				listValue.add(map1.get("OUT_COMPANY") != null ? map1.get("OUT_COMPANY") : "");
 				listValue.add(map1.get("MODEL_NAME") != null ? map1.get("MODEL_NAME") : "");
 				listValue.add(map1.get("PART_CODE") != null ? map1.get("PART_CODE") : "");
 				listValue.add(map1.get("PART_NAME") != null ? map1.get("PART_NAME") : "");
 				listValue.add(map1.get("OUT_NUM") != null ? map1.get("OUT_NUM") : "");
 				listValue.add(map1.get("NOTICE_NO") != null ? map1.get("NOTICE_NO") : "");
 				listValue.add(map1.get("NOTICE_DATE") != null ? map1.get("NOTICE_DATE") : "");
 				listValue.add(map1.get("OUT_NUM1") != null ? map1.get("OUT_NUM1") : "");
 				listValue.add(map1.get("PART_PRICE") != null ? map1.get("PART_PRICE") : "");
 				listValue.add(map1.get("LABOUR_PRICE") != null ? map1.get("LABOUR_PRICE") : "");
 				listValue.add(map1.get("TOTAL") != null ? map1.get("TOTAL") : "");
 				listValue.add(map1.get("NAME") != null ? map1.get("NAME") : "");
 				listValue.add(map1.get("OUT_REMARK") != null ? map1.get("OUT_REMARK") : "");
 				list.add(listValue);
 			}
 			}
 			os = response.getOutputStream();
 			CsvWriterUtil.createXlsFileToName(list, os,"出门证及索赔通知单");
 			os.flush();	

 		} catch (Exception e) {
 			BizException e1 = new BizException(act, e,
 					ErrorCodeConstant.QUERY_FAILURE_CODE, "出门证及索赔通知单导出");
 			logger.error(logonUser, e1);
 			act.setException(e1);
 		}

 	  }
 	
 	/**
 	 * 初始化二次索赔开票查询
 	 */
 	public void TwoClaimInvoiceInit(){

    	try {

    		act.setForword(TWO_CLAIM_INVOICE);
    		
    	} catch (Exception e) {
    		BizException e1 = new BizException(act, e,
    				ErrorCodeConstant.QUERY_FAILURE_CODE, "二次索赔开票查询");
    		logger.error(logonUser, e1);
    		act.setException(e1);
    	}
 		
 	}
 	/**
 	 * 二次索赔开票查询页面
 	 */
 	public void TwoClaimInvoiceQueryData(){

 		try {
 			Map<String, String> map = new HashMap<String, String>();

 			String yieldlyType = CommonUtils.checkNull(request.getParamValue("YIELDLY_TYPE"));//结算基地
 			String modelName = CommonUtils.checkNull(request.getParamValue("model_name"));//车型
 			String supplyCode = CommonUtils.checkNull(request.getParamValue("supply_code"));//供应商代码
 			String supplyName = CommonUtils.checkNull(request.getParamValue("supply_name"));//供应商名称
 			String bDate = CommonUtils.checkNull(request.getParamValue("bDate"));//开始日期
 			String eDate = CommonUtils.checkNull(request.getParamValue("eDate"));//结束日期
 			map.put("yieldlyType", yieldlyType);
 			map.put("modelName", modelName);
 			map.put("supplyCode", supplyCode);
 			map.put("supplyName", supplyName);
 			map.put("bDate", bDate);
 			map.put("eDate", eDate);
 			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
 					.getParamValue("curPage")) : 1;
 			PageResult<Map<String, Object>> ps=cdao.TwoClaimInvoiceQuery(map, Constant.PAGE_SIZE, curPage);
 			act.setOutData("ps", ps);
 		} catch (Exception e) {
 			BizException e1 = new BizException(act, e,
 					ErrorCodeConstant.QUERY_FAILURE_CODE, "二次索赔开票查询页面");
 			logger.error(logonUser, e1);
 			act.setException(e1);
 		}

 		}
 	
 	/**
 	 * 二次索赔开票查询导出
 	 */
 	public void TwoClaimInvoiceQueryExport(){
 		OutputStream os = null;

 		try {
 			Map<String, String> map = new HashMap<String, String>();


 			String yieldlyType = CommonUtils.checkNull(request.getParamValue("YIELDLY_TYPE"));//结算基地
 			String modelName = CommonUtils.checkNull(request.getParamValue("model_name"));//车型
 			String supplyCode = CommonUtils.checkNull(request.getParamValue("supply_code"));//供应商代码
 			String supplyName = CommonUtils.checkNull(request.getParamValue("supply_name"));//供应商名称
 			String bDate = CommonUtils.checkNull(request.getParamValue("bDate"));//开始日期
 			String eDate = CommonUtils.checkNull(request.getParamValue("eDate"));//结束日期
 			map.put("yieldlyType", yieldlyType);
 			map.put("modelName", modelName);
 			map.put("supplyCode", supplyCode);
 			map.put("supplyName", supplyName);
 			map.put("bDate", bDate);
 			map.put("eDate", eDate);
 			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
 					.getParamValue("curPage")) : 1;
 			PageResult<Map<String, Object>> ps=cdao.TwoClaimInvoiceQuery(map, 999999, curPage);
 			ResponseWrapper response = act.getResponse();
 			// 导出的文件名
 			String fileName = "二次索赔开票查询(月报).xls";
 			// 导出的文字编码客户关怀活动数据明细表
 			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
 			response.setContentType("Application/text/csv");
 			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
 			// 定义一个集合
 			List<List<Object>> list = new LinkedList<List<Object>>();
 			// 标题
 			List<Object> listTemp = new LinkedList<Object>();

 			listTemp.add("开票时间(天)");
 			listTemp.add("索赔通知单号");
 			listTemp.add("供应商代码");
 			listTemp.add("供应商名称");
 			listTemp.add("车型");
 			listTemp.add("配件代码");
 			listTemp.add("配件名称");
 			listTemp.add("配件数量");
 			listTemp.add("索赔单价");
 			listTemp.add("配件材料费");
 			listTemp.add("工时");
 			listTemp.add("三包工时费");
 			listTemp.add("包装托运费");
 			listTemp.add("小计");
 			listTemp.add("税额");
 			listTemp.add("金额总计");

 			list.add(listTemp);
 			List<Map<String, Object>> rslist = ps.getRecords();
 			Map<String, Object> map1 = new HashMap<String, Object>();

 			if(rslist!=null){

 			for (int i = 0; i < rslist.size(); i++) {
 				map1 = rslist.get(i);
 				List<Object> listValue = new LinkedList<Object>();

 				listValue.add(map1.get("NOTICE_DATE") != null ? map1.get("NOTICE_DATE") : "");
 				listValue.add(map1.get("NOTICE_NO") != null ? map1.get("NOTICE_NO") : "");
 				listValue.add(map1.get("NOTICE_CODE") != null ? map1.get("NOTICE_CODE") : "");
 				listValue.add(map1.get("MAKER_NAME") != null ? map1.get("MAKER_NAME") : "");
 				listValue.add(map1.get("MODEL_NAME") != null ? map1.get("MODEL_NAME") : "");
 				listValue.add(map1.get("PART_CODE") != null ? map1.get("PART_CODE") : "");
 				listValue.add(map1.get("PART_NAME") != null ? map1.get("PART_NAME") : "");
 				listValue.add(map1.get("OUT_NUM") != null ? map1.get("OUT_NUM") : "");
 				listValue.add(map1.get("CLAIM_PRICE") != null ? map1.get("CLAIM_PRICE") : "");
 				listValue.add(map1.get("PART_PRICE") != null ? map1.get("PART_PRICE") : "");
 				listValue.add(map1.get("CLAIM_LABOUR") != null ? map1.get("CLAIM_LABOUR") : "");
 				listValue.add(map1.get("LABOUR_PRICE") != null ? map1.get("LABOUR_PRICE") : "");
 				listValue.add(map1.get("OTHER_PRICE") != null ? map1.get("OTHER_PRICE") : "");
 				listValue.add(map1.get("SMALL_TOTAL") != null ? map1.get("SMALL_TOTAL") : "");
 				listValue.add(map1.get("TAX_TOTAL") != null ? map1.get("TAX_TOTAL") : "");
 				listValue.add(map1.get("TOTAL") != null ? map1.get("TOTAL") : "");
 				list.add(listValue);
 			}
 			}
 			os = response.getOutputStream();
 			CsvWriterUtil.createXlsFileToName(list, os,"二次索赔开票统计");
 			os.flush();	

 		} catch (Exception e) {
 			BizException e1 = new BizException(act, e,
 					ErrorCodeConstant.QUERY_FAILURE_CODE, "二次索赔开票查询导出");
 			logger.error(logonUser, e1);
 			act.setException(e1);
 		}

 	  }
 	
 	/**
 	 * 初始化二次索赔开票汇总查询
 	 */
 	public void TwoClaimInvoiceTotalInit(){

    	try {

    		act.setForword(TWO_CLAIM_INVOICE_TOTAL);
    		
    	} catch (Exception e) {
    		BizException e1 = new BizException(act, e,
    				ErrorCodeConstant.QUERY_FAILURE_CODE, "二次索赔开票汇总查询");
    		logger.error(logonUser, e1);
    		act.setException(e1);
    	}
 		
 	}
 	
 	/**
 	 * 二次索赔开票汇总查询页面
 	 */
 	public void TwoClaimInvoiceTotalQueryData(){

 		try {
 			Map<String, String> map = new HashMap<String, String>();

 			String yieldlyType = CommonUtils.checkNull(request.getParamValue("YIELDLY_TYPE"));//结算基地
 			String modelName = CommonUtils.checkNull(request.getParamValue("model_name"));//车型
 			String supplyCode = CommonUtils.checkNull(request.getParamValue("supply_code"));//供应商代码
 			String supplyName = CommonUtils.checkNull(request.getParamValue("supply_name"));//供应商名称
 			String bDate = CommonUtils.checkNull(request.getParamValue("bDate"));//开始日期
 			String eDate = CommonUtils.checkNull(request.getParamValue("eDate"));//结束日期
 			map.put("yieldlyType", yieldlyType);
 			map.put("modelName", modelName);
 			map.put("supplyCode", supplyCode);
 			map.put("supplyName", supplyName);
 			map.put("bDate", bDate);
 			map.put("eDate", eDate);
 			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
 					.getParamValue("curPage")) : 1;
 			PageResult<Map<String, Object>> ps=cdao.TwoClaimInvoiceTotalQuery(map, Constant.PAGE_SIZE, curPage);
 			act.setOutData("ps", ps);
 		} catch (Exception e) {
 			BizException e1 = new BizException(act, e,
 					ErrorCodeConstant.QUERY_FAILURE_CODE, "二次索赔开票汇总查询页面");
 			logger.error(logonUser, e1);
 			act.setException(e1);
 		}

 		}
 	/**
 	 * 二次索赔开票汇总查询导出
 	 */
 	public void TwoClaimInvoiceTotalQueryExport(){
 		OutputStream os = null;

 		try {
 			Map<String, String> map = new HashMap<String, String>();


 			String yieldlyType = CommonUtils.checkNull(request.getParamValue("YIELDLY_TYPE"));//结算基地
 			String modelName = CommonUtils.checkNull(request.getParamValue("model_name"));//车型
 			String supplyCode = CommonUtils.checkNull(request.getParamValue("supply_code"));//供应商代码
 			String supplyName = CommonUtils.checkNull(request.getParamValue("supply_name"));//供应商名称
 			String bDate = CommonUtils.checkNull(request.getParamValue("bDate"));//开始日期
 			String eDate = CommonUtils.checkNull(request.getParamValue("eDate"));//结束日期
 			map.put("yieldlyType", yieldlyType);
 			map.put("modelName", modelName);
 			map.put("supplyCode", supplyCode);
 			map.put("supplyName", supplyName);
 			map.put("bDate", bDate);
 			map.put("eDate", eDate);
 			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
 					.getParamValue("curPage")) : 1;
 			PageResult<Map<String, Object>> ps=cdao.TwoClaimInvoiceTotalQuery(map, 999999, curPage);

 			ResponseWrapper response = act.getResponse();
 			// 导出的文件名
 			String fileName = "二次索赔开票汇总表.xls";
 			// 导出的文字编码客户关怀活动数据明细表
 			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
 			response.setContentType("Application/text/csv");
 			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
 			// 定义一个集合
 			List<List<Object>> list = new LinkedList<List<Object>>();
 			// 标题
 			List<Object> listTemp = new LinkedList<Object>();


 			listTemp.add("供应商代码");
 			listTemp.add("供应商名称");
 			listTemp.add("车型");
 			listTemp.add("索赔数量");
 			listTemp.add("配件材料费");
 			listTemp.add("三包工时费");
 			listTemp.add("包装托运费");
 			listTemp.add("金额总计");

 			list.add(listTemp);
 			List<Map<String, Object>> rslist = ps.getRecords();
 			Map<String, Object> map1 = new HashMap<String, Object>();

 			if(rslist!=null){

 			for (int i = 0; i < rslist.size(); i++) {
 				map1 = rslist.get(i);
 				List<Object> listValue = new LinkedList<Object>();

 				listValue.add(map1.get("NOTICE_CODE") != null ? map1.get("NOTICE_CODE") : "");
 				listValue.add(map1.get("MAKER_NAME") != null ? map1.get("MAKER_NAME") : "");
 				listValue.add(map1.get("MODEL_NAME") != null ? map1.get("MODEL_NAME") : "");
 				listValue.add(map1.get("OUT_NUM") != null ? map1.get("OUT_NUM") : "");
 				listValue.add(map1.get("PART_PRICE") != null ? map1.get("PART_PRICE") : "");
 				listValue.add(map1.get("LABOUR_PRICE") != null ? map1.get("LABOUR_PRICE") : "");
 				listValue.add(map1.get("OTHER_PRICE") != null ? map1.get("OTHER_PRICE") : "");
 				listValue.add(map1.get("AMOUNT") != null ? map1.get("AMOUNT") : "");

 				list.add(listValue);
 			}
 			}
 			os = response.getOutputStream();
 			CsvWriterUtil.createXlsFileToName(list, os,"二次索赔开票汇总表");
 			os.flush();	

 		} catch (Exception e) {
 			BizException e1 = new BizException(act, e,
 					ErrorCodeConstant.QUERY_FAILURE_CODE, "二次索赔开票汇总导出");
 			logger.error(logonUser, e1);
 			act.setException(e1);
 		}

 	  }	
 	
 	/**
 	 * 初始化结算数量金额明细
 	 */
 	public void SettlementAmountSubsidiaryInit(){

    	try {

    		act.setForword(SETTLEMENT_AMOUNT_SUBSIDIARY);
    		
    	} catch (Exception e) {
    		BizException e1 = new BizException(act, e,
    				ErrorCodeConstant.QUERY_FAILURE_CODE, "结算数量金额明细查询");
    		logger.error(logonUser, e1);
    		act.setException(e1);
    	}
 		
 	}
 	
 	/**
 	 * 结算数量金额明细查询页面
 	 */
 	public void SettlementAmountSubsidiaryQueryData(){

 		try {
 			Map<String, String> map = new HashMap<String, String>();

 			String bDate = CommonUtils.checkNull(request.getParamValue("bDate"));//开始日期
 			String eDate = CommonUtils.checkNull(request.getParamValue("eDate"));//结束日期
 			map.put("bDate", bDate);
 			map.put("eDate", eDate);
 			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
 					.getParamValue("curPage")) : 1;
 			PageResult<Map<String, Object>> ps=cdao.SettlementAmountSubsidiaryQuery(map, Constant.PAGE_SIZE, curPage);
 			act.setOutData("ps", ps);
 		} catch (Exception e) {
 			BizException e1 = new BizException(act, e,
 					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算数量金额明细查询页面");
 			logger.error(logonUser, e1);
 			act.setException(e1);
 		}

 		}
 	/**
 	 * 结算数量金额明细查询导出
 	 */
 	public void SettlementAmountSubsidiaryExport(){
 		OutputStream os = null;

 		try {
 			Map<String, String> map = new HashMap<String, String>();


 			String bDate = CommonUtils.checkNull(request.getParamValue("bDate"));//开始日期
 			String eDate = CommonUtils.checkNull(request.getParamValue("eDate"));//结束日期
 			map.put("bDate", bDate);
 			map.put("eDate", eDate);
 			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
 					.getParamValue("curPage")) : 1;
 			PageResult<Map<String, Object>> ps=cdao.SettlementAmountSubsidiaryQuery(map,999999, curPage);

 			ResponseWrapper response = act.getResponse();
 			// 导出的文件名
 			String fileName = "结算数量金额明细表.xls";
 			// 导出的文字编码客户关怀活动数据明细表
 			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
 			response.setContentType("Application/text/csv");
 			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
 			// 定义一个集合
 			List<List<Object>> list = new LinkedList<List<Object>>();
 			// 标题
 			List<Object> listTemp = new LinkedList<Object>();



 			listTemp.add("车系");
 			listTemp.add("配件代码");
 			listTemp.add("配件名称");
 			listTemp.add("故障件名称");
 			listTemp.add("供应商");
 			listTemp.add("更换件数量");
 			listTemp.add("结算金额");

 			list.add(listTemp);
 			List<Map<String, Object>> rslist = ps.getRecords();
 			Map<String, Object> map1 = new HashMap<String, Object>();

 			if(rslist!=null){

 			for (int i = 0; i < rslist.size(); i++) {
 				map1 = rslist.get(i);
 				List<Object> listValue = new LinkedList<Object>();
 				listValue.add(map1.get("SERIES_NAME") != null ? map1.get("SERIES_NAME") : "");
 				listValue.add(map1.get("PART_CODE") != null ? map1.get("PART_CODE") : "");
 				listValue.add(map1.get("PART_NAME") != null ? map1.get("PART_NAME") : "");
 				listValue.add(map1.get("MAL_NAME") != null ? map1.get("MAL_NAME") : "");
 				listValue.add(map1.get("MAKER_NAME") != null ? map1.get("MAKER_NAME") : "");
 				listValue.add(map1.get("BALANCE_QUANTITY") != null ? map1.get("BALANCE_QUANTITY") : "");
 				listValue.add(map1.get("BALANCE_AMOUNT") != null ? map1.get("BALANCE_AMOUNT") : "");

 				list.add(listValue);
 			}
 			}
 			os = response.getOutputStream();
 			CsvWriterUtil.createXlsFileToName(list, os,"结算数量金额明细表");
 			os.flush();	

 		} catch (Exception e) {
 			BizException e1 = new BizException(act, e,
 					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算数量金额明细查询导出");
 			logger.error(logonUser, e1);
 			act.setException(e1);
 		}
}
 	/**
 	 * 结算汇总明细
 	 */
 	public void SettlementSummaryDelInit(){

    	try {

			List<Map<String, Object>> serisList = ajaxDao.getSerisList(logonUser.getPoseId().toString(), 2);
			act.setOutData("serislist", serisList);

    		act.setForword(SETTLEMENT_SUMMARY_Del);
    		
    	} catch (Exception e) {
    		BizException e1 = new BizException(act, e,
    				ErrorCodeConstant.QUERY_FAILURE_CODE, "结算汇总明细");
    		logger.error(logonUser, e1);
    		act.setException(e1);
    	}
 		
 	}
 	/**
 	 * 结算汇总明细
 	 */
 	public void SettlementSummaryDelQueryData(){

 		try {
 			Map<String, String> map = new HashMap<String, String>();

 			String yieldlyType = CommonUtils.checkNull(request.getParamValue("YIELDLY_TYPE"));//结算基地
 			String settlementNo = CommonUtils.checkNull(request.getParamValue("settlement_no"));//结算单号
 			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));//车型
 			String serisid = CommonUtils.checkNull(request.getParamValue("serisid"));//车系
 			String vin = CommonUtils.checkNull(request.getParamValue("vin"));//vin
 			String supplyCode = CommonUtils.checkNull(request.getParamValue("supply_code"));//供应商代码
 			String supplyName = CommonUtils.checkNull(request.getParamValue("supply_name"));//供应商名称
 			String partCode = CommonUtils.checkNull(request.getParamValue("part_code"));//配件代码
 			String partName = CommonUtils.checkNull(request.getParamValue("part_name"));//配件名称
 			String homeworkCode = CommonUtils.checkNull(request.getParamValue("homework_code"));//配件代码
 			String homeworkName = CommonUtils.checkNull(request.getParamValue("homework_name"));//配件名称
 			String bDate = CommonUtils.checkNull(request.getParamValue("bDate"));//开始日期
 			String eDate = CommonUtils.checkNull(request.getParamValue("eDate"));//结束日期
 			map.put("yieldlyType", yieldlyType);
 			map.put("settlementNo", settlementNo);
 			map.put("vin", vin);
 			map.put("partCode", partCode);
 			map.put("partName", partName);
 			map.put("homeworkCode", homeworkCode);
 			map.put("homeworkName", homeworkName);
 			map.put("groupCode", groupCode);
 			map.put("serisid", serisid);
 			map.put("supplyCode", supplyCode);
 			map.put("supplyName", supplyName);
 			map.put("bDate", bDate);
 			map.put("eDate", eDate);
 			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
 					.getParamValue("curPage")) : 1;
 			PageResult<Map<String, Object>> ps=cdao.SettlementSummaryDelQuery(map, Constant.PAGE_SIZE, curPage);
 			act.setOutData("ps", ps);
 		} catch (Exception e) {
 			BizException e1 = new BizException(act, e,
 					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算汇总明细");
 			logger.error(logonUser, e1);
 			act.setException(e1);
 		}

 		}
 	
 	/**
 	 * 结算汇总明细下载
 	 */
 	public void SettlementSummaryDelExport(){
 		OutputStream os = null;

 		try {
 			Map<String, String> map = new HashMap<String, String>();


 			String yieldlyType = CommonUtils.checkNull(request.getParamValue("YIELDLY_TYPE"));//结算基地
 			String settlementNo = CommonUtils.checkNull(request.getParamValue("settlement_no"));//结算单号
 			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));//车型
 			String serisid = CommonUtils.checkNull(request.getParamValue("serisid"));//车系
 			String vin = CommonUtils.checkNull(request.getParamValue("vin"));//vin

 			String supplyCode = CommonUtils.checkNull(request.getParamValue("supply_code"));//供应商代码
 			String supplyName = CommonUtils.checkNull(request.getParamValue("supply_name"));//供应商名称
 			String partCode = CommonUtils.checkNull(request.getParamValue("part_code"));//配件代码
 			String partName = CommonUtils.checkNull(request.getParamValue("part_name"));//配件名称
 			String homeworkCode = CommonUtils.checkNull(request.getParamValue("homework_code"));//作业代码
 			String homeworkName = CommonUtils.checkNull(request.getParamValue("homework_name"));//作业名称
 			String bDate = CommonUtils.checkNull(request.getParamValue("bDate"));//开始日期
 			String eDate = CommonUtils.checkNull(request.getParamValue("eDate"));//结束日期
 			map.put("yieldlyType", yieldlyType);
 			map.put("settlementNo", settlementNo);
 			map.put("partCode", partCode);
 			map.put("vin", vin);
 			map.put("partName", partName);
 			map.put("homeworkCode", homeworkCode);
 			map.put("homeworkName", homeworkName);
 			map.put("groupCode", groupCode);
 			map.put("serisid", serisid);
 			map.put("supplyCode", supplyCode);
 			map.put("supplyName", supplyName);
 			map.put("bDate", bDate);
 			map.put("eDate", eDate);
 			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
 					.getParamValue("curPage")) : 1;
 			PageResult<Map<String, Object>> ps=cdao.SettlementSummaryDelQuery(map, 999999, curPage);

 			ResponseWrapper response = act.getResponse();
 			// 导出的文件名
 			String fileName = "结算明细表.xls";
 			// 导出的文字编码客户关怀活动数据明细表
 			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
 			response.setContentType("Application/text/csv");
 			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
 			// 定义一个集合
 			List<List<Object>> list = new LinkedList<List<Object>>();
 			// 标题
 			List<Object> listTemp = new LinkedList<Object>();



 			listTemp.add("维修类型");
 			listTemp.add("单据号码");
 			listTemp.add("一级站代码");
 			listTemp.add("服务站代码");
 			listTemp.add("活动名称");
 			listTemp.add("车系");
 			listTemp.add("车型");
 			listTemp.add("发动机号码");
 			listTemp.add("VIN号码");
 			listTemp.add("销售日期");
 			listTemp.add("维修日期");
 			//listTemp.add("处理类型");
 			listTemp.add("换上件代码");
 			listTemp.add("换上件名称");
 			listTemp.add("数量");
 			listTemp.add("作业代码");
 			listTemp.add("作业名称");
 			listTemp.add("上报工时费");
 			listTemp.add("上报材料费");
 			listTemp.add("上报派出费用");
 			listTemp.add("上报赠送费用");
 			listTemp.add("工时费	");
 			listTemp.add("材料费");
 			listTemp.add("外派费用");
 			listTemp.add("赠送费用");
 			listTemp.add("其他费用");
 		//	listTemp.add("运费");
 			listTemp.add("结算基地");
 			listTemp.add("系统确认时间");
 			listTemp.add("备注");
 	
 			list.add(listTemp);
 			List<Map<String, Object>> rslist = ps.getRecords();
 			Map<String, Object> map1 = new HashMap<String, Object>();

 			if(rslist!=null){

 			for (int i = 0; i < rslist.size(); i++) {
 				map1 = rslist.get(i);
 				List<Object> listValue = new LinkedList<Object>();
 				listValue.add(map1.get("TYPE") != null ? map1.get("TYPE") : "");
 				listValue.add(map1.get("ORDER_NO") != null ? map1.get("ORDER_NO") : "");
 				listValue.add(map1.get("ROOT_DEALER_CODE") != null ? map1.get("ROOT_DEALER_CODE") : "");
 				listValue.add(map1.get("DEALER_CODE") != null ? map1.get("DEALER_CODE") : "");
 				listValue.add(map1.get("ACTIVITY_NAME") != null ? map1.get("ACTIVITY_NAME") : "");
 				listValue.add(map1.get("SERIES") != null ? map1.get("SERIES") : "");
 				listValue.add(map1.get("MODEL") != null ? map1.get("MODEL") : "");
 				listValue.add(map1.get("ENGINE_NO") != null ? map1.get("ENGINE_NO") : "");
 				listValue.add(map1.get("VIN") != null ? map1.get("VIN") : "");
 				listValue.add(map1.get("BUY_DATE") != null ? map1.get("BUY_DATE") : "");
 				listValue.add(map1.get("FINE_DATE") != null ? map1.get("FINE_DATE") : "");
 				listValue.add(map1.get("PART_CODE") != null ? map1.get("PART_CODE") : "");
 				listValue.add(map1.get("PART_NAME") != null ? map1.get("PART_NAME") : "");
 				listValue.add(map1.get("NUM") != null ? map1.get("NUM") : "");
 				listValue.add(map1.get("LABOUR_CODE") != null ? map1.get("LABOUR_CODE") : "");
 				listValue.add(map1.get("LABOUR_NAME") != null ? map1.get("LABOUR_NAME") : "");
 				
 				listValue.add(map1.get("LABOUR_SUM") != null ? map1.get("LABOUR_SUM") : "");
 				listValue.add(map1.get("PART_SUM") != null ? map1.get("PART_SUM") : "");
 				listValue.add(map1.get("SEND_SUM") != null ? map1.get("SEND_SUM") : "");
 				listValue.add(map1.get("FREE_SUM") != null ? map1.get("FREE_SUM") : "");
 				
 				listValue.add(map1.get("B_LABOUR_SUM") != null ? map1.get("B_LABOUR_SUM") : "");
 				listValue.add(map1.get("B_PART_SUM") != null ? map1.get("B_PART_SUM") : "");
 				listValue.add(map1.get("B_SEND_SUM") != null ? map1.get("B_SEND_SUM") : "");
 				listValue.add(map1.get("B_FREE_SUM") != null ? map1.get("B_FREE_SUM") : "");
 				
 				
 				
 				listValue.add(map1.get("OTHER_SUM") != null ? map1.get("OTHER_SUM") : "");
 				listValue.add(map1.get("BALANCE_YIELD") != null ?map1.get("BALANCE_YIELD"): "");
 				listValue.add(map1.get("FI_DATE") != null ? map1.get("FI_DATE") : "");
 				listValue.add(map1.get("REMARK") != null ? map1.get("REMARK") : "");
 				
 				list.add(listValue);
 			}
 			}
 			os = response.getOutputStream();
 			CsvWriterUtil.createXlsFileToName(list, os,"结算明细表");
 			os.flush();	

 		} catch (Exception e) {
 			BizException e1 = new BizException(act, e,
 					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算数量金额明细查询导出");
 			logger.error(logonUser, e1);
 			act.setException(e1);
 		}
}
 	/**
 	 * 结算汇总
 	 */
 	public void SettlementSummaryInit(){

    	try {

			List<Map<String, Object>> serisList = ajaxDao.getSerisList(logonUser.getPoseId().toString(), 2);
			act.setOutData("serislist", serisList);

    		act.setForword(SETTLEMENT_SUMMARY);
    		
    	} catch (Exception e) {
    		BizException e1 = new BizException(act, e,
    				ErrorCodeConstant.QUERY_FAILURE_CODE, "结算汇总");
    		logger.error(logonUser, e1);
    		act.setException(e1);
    	}
 		
 	}
 	/**
 	 * 结算汇总
 	 */
 	public void SettlementSummaryQueryData(){

 		try {
 			Map<String, String> map = new HashMap<String, String>();

 			String yieldlyType = CommonUtils.checkNull(request.getParamValue("YIELDLY_TYPE"));//结算基地
 			String costType = CommonUtils.checkNull(request.getParamValue("cost_type"));//费用类型
 			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));//车型
 			String serisid = CommonUtils.checkNull(request.getParamValue("serisid"));//车系
 			String bDate = CommonUtils.checkNull(request.getParamValue("bDate"));//开始日期
 			String eDate = CommonUtils.checkNull(request.getParamValue("eDate"));//结束日期
 			map.put("yieldlyType", yieldlyType);
 			map.put("costType", costType);;
 			map.put("groupCode", groupCode);
 			map.put("serisid", serisid);
 			map.put("bDate", bDate);
 			map.put("eDate", eDate);
 			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
 					.getParamValue("curPage")) : 1;
 			PageResult<Map<String, Object>> ps=cdao.SettlementSummaryQuery(map, Constant.PAGE_SIZE, curPage);
 			act.setOutData("ps", ps);
 		} catch (Exception e) {
 			BizException e1 = new BizException(act, e,
 					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算汇总");
 			logger.error(logonUser, e1);
 			act.setException(e1);
 		}

 		}
 	
 	/**
 	 * 结算汇总下载
 	 */
 	public void SettlementSummaryExport(){
 		OutputStream os = null;

 		try {
 			Map<String, String> map = new HashMap<String, String>();


 			String yieldlyType = CommonUtils.checkNull(request.getParamValue("YIELDLY_TYPE"));//结算基地
 			String costType = CommonUtils.checkNull(request.getParamValue("cost_type"));//费用类型
 			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));//车型
 			String serisid = CommonUtils.checkNull(request.getParamValue("serisid"));//车系
 			String bDate = CommonUtils.checkNull(request.getParamValue("bDate"));//开始日期
 			String eDate = CommonUtils.checkNull(request.getParamValue("eDate"));//结束日期
 			map.put("yieldlyType", yieldlyType);
 			map.put("costType", costType);;
 			map.put("groupCode", groupCode);
 			map.put("serisid", serisid);
 			map.put("bDate", bDate);
 			map.put("eDate", eDate);
 			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
 					.getParamValue("curPage")) : 1;
 			PageResult<Map<String, Object>> ps=cdao.SettlementSummaryQuery(map, 999999, curPage);
 			ResponseWrapper response = act.getResponse();
 			// 导出的文件名
 			String fileName = "结算汇总表.xls";
 			// 导出的文字编码客户关怀活动数据明细表
 			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
 			response.setContentType("Application/text/csv");
 			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
 			// 定义一个集合
 			List<List<Object>> list = new LinkedList<List<Object>>();
 			// 标题
 			List<Object> listTemp = new LinkedList<Object>();



 			listTemp.add("维修类型");
 			listTemp.add("车系");
 			listTemp.add("车型");
 			listTemp.add("上报工时费");
 			listTemp.add("上报材料费");
 			listTemp.add("上报派出费用");
 			listTemp.add("上报赠送费用");
 			listTemp.add("赠送费用");
 			listTemp.add("材料费");
 			listTemp.add("工时费");
 			listTemp.add("外派费用");
 			listTemp.add("其他费用");
 			listTemp.add("抵扣费用");
 			// listTemp.add("运费");
 			listTemp.add("系统确认时间");
 			

 			list.add(listTemp);
			List<Map<String, Object>> rslist = ps.getRecords();
 			Map<String, Object> map1 = new HashMap<String, Object>();

 			if(rslist!=null){

 			for (int i = 0; i < rslist.size(); i++) {
 				map1 = rslist.get(i);
 				List<Object> listValue = new LinkedList<Object>();
 				listValue.add(map1.get("TYPE") != null ? map1.get("TYPE") : "");
 				listValue.add(map1.get("SERIES") != null ? map1.get("SERIES") : "");
 				listValue.add(map1.get("MODEL") != null ? map1.get("MODEL") : "");
 				listValue.add(map1.get("LABOUR_SUM") != null ? map1.get("LABOUR_SUM") : "");
 				listValue.add(map1.get("PART_SUM") != null ? map1.get("PART_SUM") : "");
 				listValue.add(map1.get("SEND_SUM") != null ? map1.get("SEND_SUM") : "");
 				listValue.add(map1.get("FREE_SUM") != null ? map1.get("FREE_SUM") : "");
 				listValue.add(map1.get("B_FREE_SUM") != null ? map1.get("B_FREE_SUM") : "");
 				listValue.add(map1.get("B_PART_SUM") != null ? map1.get("B_PART_SUM") : "");
 				listValue.add(map1.get("B_LABOUR_SUM") != null ? map1.get("B_LABOUR_SUM") : "");
 				listValue.add(map1.get("B_SEND_SUM") != null ? map1.get("B_SEND_SUM") : "");
 				listValue.add(map1.get("OTHER_SUM") != null ? map1.get("OTHER_SUM") : "");
 				listValue.add(map1.get("DK_SUM") != null ? map1.get("DK_SUM") : "");
 				// listValue.add(map1.get("TRAN_SUM") != null ? map1.get("TRAN_SUM") : "");
 				listValue.add(map1.get("FI_MONTH") != null ? map1.get("FI_MONTH") : "");

 				
 				list.add(listValue);
 			}
 			}
 			os = response.getOutputStream();
 			CsvWriterUtil.createXlsFileToName(list, os,"结算明细表");
 			os.flush();	

 		} catch (Exception e) {
 			BizException e1 = new BizException(act, e,
 					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算数量金额明细查询导出");
 			logger.error(logonUser, e1);
 			act.setException(e1);
 		}
}
}