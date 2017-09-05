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
import com.infodms.dms.dao.report.serviceReport.BaseReportViemDao;
import com.infodms.dms.dao.report.serviceReport.ClaimReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;



/**
 * @author wenyudan
 *报表公共类
 */
public class FreightBillingDetails{
	 Logger logger = Logger.getLogger(FreightBillingDetails.class);
	 final BaseReportViemDao cdao = BaseReportViemDao.getInstance();
	private static final AjaxSelectDao ajaxDao = AjaxSelectDao.getInstance();
	private final ClaimReportDao dao  = ClaimReportDao.getInstance();



	//运费结算明细
	 final String Freight_SETTLEMENT_SUMMARY = "/jsp/report/service/Freight_Settlement_Summary_del.jsp"; 
	 //售后服务情况
	 final String AFTER_SALES_SERVICE = "/jsp/report/service/After_Sales_Service.jsp"; 
	 //外部质量损失
	 final String EXTERNAL_QUALITY = "/jsp/report/service/External_Quality.jsp"; 
	 
		ActionContext act = ActionContext.getContext();
 	 	AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
 	 	RequestWrapper request = act.getRequest();
	/**
 	 * 运费结算明细
 	 */
 	public void FreightBillingDetailsInit(){


    	try {

    		act.setForword(Freight_SETTLEMENT_SUMMARY);
    		
    	} catch (Exception e) {
    		BizException e1 = new BizException(act, e,
    				ErrorCodeConstant.QUERY_FAILURE_CODE, "运费结算明细");
    		logger.error(logonUser, e1);
    		act.setException(e1);
    	}
 		
 	}

 	/**
 	 * 运费结算汇总
 	 */
 	public void FreightBillingDetailstQueryData(){

 		try {
 			Map<String, String> map = new HashMap<String, String>();

 			String yieldlyType = CommonUtils.checkNull(request.getParamValue("YIELDLY_TYPE"));//结算基地
 			String supplyCode = CommonUtils.checkNull(request.getParamValue("supply_code"));//代码
 			String supplyName = CommonUtils.checkNull(request.getParamValue("supply_name"));//全称
 			String bDate = CommonUtils.checkNull(request.getParamValue("bDate"));//开始日期
 			String eDate = CommonUtils.checkNull(request.getParamValue("eDate"));//结束日期
 			String bgDate = CommonUtils.checkNull(request.getParamValue("bgDate"));//开始日期
 			String egDate = CommonUtils.checkNull(request.getParamValue("egDate"));//结束日期
 			map.put("yieldlyType", yieldlyType);
 			map.put("supplyCode", supplyCode);
 			map.put("supplyName", supplyName);
 			map.put("bDate", bDate);
 			map.put("eDate", eDate);
 			map.put("bgDate", bgDate);
 			map.put("egDate", egDate);
 			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
 					.getParamValue("curPage")) : 1;
 			PageResult<Map<String, Object>> ps=cdao.FreightSettlementSummaryQuery(map, Constant.PAGE_SIZE, curPage);
 			act.setOutData("ps", ps);
 		} catch (Exception e) {
 			BizException e1 = new BizException(act, e,
 					ErrorCodeConstant.QUERY_FAILURE_CODE, "运费结算明细");
 			logger.error(logonUser, e1);
 			act.setException(e1);
 		}

 		}
 	

 	/**
 	 * 运费结算明细导出页面
 	 */
 	public void FreightBillingDetailstExport(){
 		OutputStream os = null;

 		try {
 			Map<String, String> map = new HashMap<String, String>();

 			String yieldlyType = CommonUtils.checkNull(request.getParamValue("YIELDLY_TYPE"));//结算基地
 			String supplyCode = CommonUtils.checkNull(request.getParamValue("supply_code"));//代码
 			String supplyName = CommonUtils.checkNull(request.getParamValue("supply_name"));//全称
 			String bDate = CommonUtils.checkNull(request.getParamValue("bDate"));//开始日期
 			String eDate = CommonUtils.checkNull(request.getParamValue("eDate"));//结束日期
 			String bgDate = CommonUtils.checkNull(request.getParamValue("bgDate"));//开始日期
 			String egDate = CommonUtils.checkNull(request.getParamValue("egDate"));//结束日期
 			map.put("yieldlyType", yieldlyType);
 			map.put("supplyCode", supplyCode);
 			map.put("supplyName", supplyName);
 			map.put("bDate", bDate);
 			map.put("eDate", eDate);
 			map.put("bgDate", bgDate);
 			map.put("egDate", egDate);
 			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
 					.getParamValue("curPage")) : 1;
 			PageResult<Map<String, Object>> ps=cdao.FreightSettlementSummaryQuery(map, 999999, curPage);

 			ResponseWrapper response = act.getResponse();
 			// 导出的文件名
 			String fileName = "运费结算明细.xls";
 			// 导出的文字编码客户关怀活动数据明细表
 			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
 			response.setContentType("Application/text/csv");
 			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
 			// 定义一个集合
 			List<List<Object>> list = new LinkedList<List<Object>>();
 			// 标题
 			List<Object> listTemp = new LinkedList<Object>();
 			listTemp.add("一级站代码");
 			listTemp.add("服务站代码");
 			listTemp.add("服务站名称");
 			listTemp.add("结算年月");
 			listTemp.add("运费");
 			listTemp.add("结算基地");
 			listTemp.add("系统确认时间");


 			list.add(listTemp);
 			List<Map<String, Object>> rslist = ps.getRecords();
 			Map<String, Object> map1 = new HashMap<String, Object>();

 			if(rslist!=null){

 			for (int i = 0; i < rslist.size(); i++) {
 				map1 = rslist.get(i);
 				List<Object> listValue = new LinkedList<Object>();

 				listValue.add(map1.get("ROOT_DEALER_CODE") != null ? map1.get("ROOT_DEALER_CODE") : "");
 				listValue.add(map1.get("DEALER_CODE") != null ? map1.get("DEALER_CODE") : "");
 				listValue.add(map1.get("DEALER_NAME") != null ? map1.get("DEALER_NAME") : "");
 				listValue.add(map1.get("BALANCE_MONTH") != null ? map1.get("BALANCE_MONTH") : "");
 				listValue.add(map1.get("TRAN_SUM") != null ? map1.get("TRAN_SUM") : "");
 				listValue.add(map1.get("BALANCE_YIELDLY") != null ? map1.get("BALANCE_YIELDLY") : "");
 				listValue.add(map1.get("FI_MONTH") != null ? map1.get("FI_MONTH") : "");

 				list.add(listValue);
 			}
 			}
 			os = response.getOutputStream();
 			CsvWriterUtil.createXlsFileToName(list, os,"运费结算明细");
 			os.flush();	

 		} catch (Exception e) {
 			BizException e1 = new BizException(act, e,
 					ErrorCodeConstant.QUERY_FAILURE_CODE, "运费结算明细导出");
 			logger.error(logonUser, e1);
 			act.setException(e1);
 		}

 	  }
 	
 	/**
 	 * 售后服务情况
 	 */
 	public void AfterSalesServiceInit(){


    	try {

    		act.setForword(AFTER_SALES_SERVICE);
    		
    	} catch (Exception e) {
    		BizException e1 = new BizException(act, e,
    				ErrorCodeConstant.QUERY_FAILURE_CODE, "售后服务情况");
    		logger.error(logonUser, e1);
    		act.setException(e1);
    	}
 		
 	}

 	/**
 	 * 售后服务情况
 	 */
 	public void AfterSalesServiceQueryData(){

 		try {
 			Map<String, String> map = new HashMap<String, String>();

 			String bDate = CommonUtils.checkNull(request.getParamValue("bDate"));//开始日期
 			String eDate = CommonUtils.checkNull(request.getParamValue("eDate"));//结束日期


 			map.put("bDate", bDate);
 			map.put("eDate", eDate);

 			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
 					.getParamValue("curPage")) : 1;
 			PageResult<Map<String, Object>> ps=cdao.AfterSalesServiceQuery(map, Constant.PAGE_SIZE, curPage);
 			act.setOutData("ps", ps);
 		} catch (Exception e) {
 			BizException e1 = new BizException(act, e,
 					ErrorCodeConstant.QUERY_FAILURE_CODE, "售后服务情况");
 			logger.error(logonUser, e1);
 			act.setException(e1);
 		}

 		}
	/**
 	 * 售后服务情况导出页面
 	 */
 	public void AfterSalesServiceExport(){
 		OutputStream os = null;

 		try {
 			Map<String, String> map = new HashMap<String, String>();


 			String bDate = CommonUtils.checkNull(request.getParamValue("bDate"));//开始日期
 			String eDate = CommonUtils.checkNull(request.getParamValue("eDate"));//结束日期


 			map.put("bDate", bDate);
 			map.put("eDate", eDate);

 			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
 					.getParamValue("curPage")) : 1;
 			PageResult<Map<String, Object>> ps=cdao.AfterSalesServiceQuery(map, 999999, curPage);

 			ResponseWrapper response = act.getResponse();
 			// 导出的文件名
 			String fileName = "售后服务情况表.xls";
 			// 导出的文字编码客户关怀活动数据明细表
 			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
 			response.setContentType("Application/text/csv");
 			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
 			// 定义一个集合
 			List<List<Object>> list = new LinkedList<List<Object>>();
 			// 标题
 			List<Object> listTemp = new LinkedList<Object>();
 			listTemp.add("类型");
 			listTemp.add("当年单月");
 			listTemp.add("前年单月");
 			listTemp.add("前年单月比");
 			listTemp.add("当年月累计");
 			listTemp.add("前年月累计");
 			listTemp.add("前年累计比");
 			



 			list.add(listTemp);
 			List<Map<String, Object>> rslist = ps.getRecords();
 			Map<String, Object> map1 = new HashMap<String, Object>();

 			if(rslist!=null){

 			for (int i = 0; i < rslist.size(); i++) {
 				map1 = rslist.get(i);
 				List<Object> listValue = new LinkedList<Object>();

 				listValue.add(map1.get("TYPE") != null ? map1.get("TYPE") : "");
 				listValue.add(map1.get("MNTOTAL") != null ? map1.get("MNTOTAL") : "");
 				listValue.add(map1.get("MYTOTAL") != null ? map1.get("MYTOTAL") : "");
 				listValue.add(map1.get("MPER") != null ? map1.get("MPER") : "");
 				listValue.add(map1.get("YNTOTAL") != null ? map1.get("YNTOTAL") : "");
 				listValue.add(map1.get("YYTOTAL") != null ? map1.get("YYTOTAL") : "");
 				listValue.add(map1.get("YPER") != null ? map1.get("YPER") : "");

 				list.add(listValue);
 			}
 			}
 			os = response.getOutputStream();
 			CsvWriterUtil.createXlsFileToName(list, os,"售后服务情况");
 			os.flush();	

 		} catch (Exception e) {
 			BizException e1 = new BizException(act, e,
 					ErrorCodeConstant.QUERY_FAILURE_CODE, "售后服务情况导出");
 			logger.error(logonUser, e1);
 			act.setException(e1);
 		}

 	  }
 	
 	/**
 	 *外部质量损失
 	 */
 	public void ExternalQualityInit(){


    	try {
			List<Map<String, Object>> list = dao.getBigOrgList();
			List<Map<String, Object>> serisList = ajaxDao.getSerisList(logonUser.getPoseId().toString(), 2);
			act.setOutData("serislist", serisList);
			act.setOutData("list", list);
    		act.setForword(EXTERNAL_QUALITY);
    		
    	} catch (Exception e) {
    		BizException e1 = new BizException(act, e,
    				ErrorCodeConstant.QUERY_FAILURE_CODE, "外部质量损失");
    		logger.error(logonUser, e1);
    		act.setException(e1);
    	}
 		
 	}

 	/**
 	 * 外部质量损失
 	 */
 	public void ExternalQualityQueryData(){

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
 			PageResult<Map<String, Object>> ps=cdao.ExternalQualityQuery(map, Constant.PAGE_SIZE, curPage);
 			act.setOutData("ps", ps);
 		} catch (Exception e) {
 			BizException e1 = new BizException(act, e,
 					ErrorCodeConstant.QUERY_FAILURE_CODE, "外部质量损失");
 			logger.error(logonUser, e1);
 			act.setException(e1);
 		}

 		}
	/**
 	 * 外部质量损失导出页面
 	 */
 	public void ExternalQualityExport(){
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
 			PageResult<Map<String, Object>> ps=cdao.ExternalQualityQuery(map, 999999, curPage);

 			ResponseWrapper response = act.getResponse();
 			// 导出的文件名
 			String fileName = "外部质量损失汇总表.xls";
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
 			listTemp.add("强保费工时费");
 			listTemp.add("强保费材料费");
 			listTemp.add("强保费小计");
 			listTemp.add("售前费工时费");
 			listTemp.add("售前费材料费");
 			listTemp.add("售前费小计");
 			listTemp.add("保修费工时费");
 			listTemp.add("保修费材料费");
 			listTemp.add("保修费派出费");
 			listTemp.add("保修费小计");
 			listTemp.add("索赔费工时费");
 			listTemp.add("索赔费材料费");
 			listTemp.add("索赔费小计");
 			listTemp.add("退货损失费工时费");
 			listTemp.add("退货损失费材料费");
 			listTemp.add("退货损失费小计");
 			listTemp.add("批量费工时费");
 			listTemp.add("批量费材料费");
 			listTemp.add("批量费管理费");
 			listTemp.add("批量费小计");
 			



 			list.add(listTemp);
 			List<Map<String, Object>> rslist = ps.getRecords();
 			Map<String, Object> map1 = new HashMap<String, Object>();

 			if(rslist!=null){

 			for (int i = 0; i < rslist.size(); i++) {
 				map1 = rslist.get(i);
 				List<Object> listValue = new LinkedList<Object>();

 				listValue.add(map1.get("SERIES") != null ? map1.get("SERIES") : "");
 				listValue.add(map1.get("MODEL_CODE") != null ? map1.get("MODEL_CODE") : "");
 				listValue.add(map1.get("FL_AMOUNT") != null ? map1.get("FL_AMOUNT") : "");
 				listValue.add(map1.get("FP_AMOUNT") != null ? map1.get("FP_AMOUNT") : "");
 				listValue.add(map1.get("F_AMOUNT") != null ? map1.get("F_AMOUNT") : "");
 				listValue.add(map1.get("QL_AMOUNT") != null ? map1.get("QL_AMOUNT") : "");
 				listValue.add(map1.get("QP_AMOUNT") != null ? map1.get("QP_AMOUNT") : "");
 				listValue.add(map1.get("Q_AMOUNT") != null ? map1.get("Q_AMOUNT") : "");
 				listValue.add(map1.get("BL_AMOUNT") != null ? map1.get("BL_AMOUNT") : "");
 				listValue.add(map1.get("BP_AMOUNT") != null ? map1.get("BP_AMOUNT") : "");
 				listValue.add(map1.get("BW_AMOUNT") != null ? map1.get("BW_AMOUNT") : "");
 				listValue.add(map1.get("B_AMOUNT") != null ? map1.get("B_AMOUNT") : "");
 				listValue.add(map1.get("TL_AMOUNT") != null ? map1.get("TL_AMOUNT") : "");
 				listValue.add(map1.get("TP_AMOUNT") != null ? map1.get("TP_AMOUNT") : "");
 				//listValue.add(map1.get("D_MILEAGE") != null ? map1.get("D_MILEAGE") : "");
 				listValue.add(map1.get("T_AMOUNT") != null ? map1.get("T_AMOUNT") : "");
 				listValue.add(map1.get("HL_AMOUNT") != null ? map1.get("HL_AMOUNT") : "");
 				listValue.add(map1.get("HP_AMOUNT") != null ? map1.get("HP_AMOUNT") : "");
 				listValue.add(map1.get("H_AMOUNT") != null ? map1.get("H_AMOUNT") : "");
 				listValue.add(map1.get("PL_AMOUNT") != null ? map1.get("PL_AMOUNT") : "");
 				listValue.add(map1.get("PP_AMOUNT") != null ? map1.get("PP_AMOUNT") : "");
 				listValue.add(map1.get("PG_AMOUNT") != null ? map1.get("PG_AMOUNT") : "");
 				listValue.add(map1.get("P_AMOUNT") != null ? map1.get("P_AMOUNT") : "");

 				list.add(listValue);
 			}
 			}
 			os = response.getOutputStream();
 			CsvWriterUtil.createXlsFileToName(list, os,"外部质量损失汇总");
 			os.flush();	

 		} catch (Exception e) {
 			BizException e1 = new BizException(act, e,
 					ErrorCodeConstant.QUERY_FAILURE_CODE, "外部质量损失导出");
 			logger.error(logonUser, e1);
 			act.setException(e1);
 		}

 	  }
}
