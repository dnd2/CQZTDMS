package com.infodms.dms.actions.report.service;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.report.serviceReport.ClaimReportDao;
import com.infodms.dms.dao.report.serviceReport.CustomerCareActivityDelDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

public class CustomerCareActivityDel {

	 private Logger logger = Logger.getLogger(CustomerCareActivityDel.class);
	 private final CustomerCareActivityDelDao cdao = CustomerCareActivityDelDao.getInstance();
	 private final ClaimReportDao dao  = ClaimReportDao.getInstance();
	 private final String DEALER_FINF_DETAIL = "/jsp/report/service/Activity_Customer_Care_Detail.jsp";
	
	 	ActionContext act = ActionContext.getContext();
	 	AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	 	RequestWrapper request = act.getRequest();
		 
	 /**
	 * 初始化页面
	 */
	public void CustomerCareActivityDelInit(){

	    	try {
				List<Map<String, Object>> list = dao.getBigOrgList();
				act.setOutData("list", list);
				act.setOutData("serviceactivityType", "");
	    		act.setForword(DEALER_FINF_DETAIL);
	    		
	    	} catch (Exception e) {
	    		BizException e1 = new BizException(act, e,
	    				ErrorCodeConstant.QUERY_FAILURE_CODE, "客户关怀活动数据明细表");
	    		logger.error(logonUser, e1);
	    		act.setException(e1);
	    	}
	    	
	    }
	
	/**
	 * 查询页面
	 */
	public void CustomerCareActivityQueryDel(){

		try {
			Map<String, Object> map = new HashMap<String, Object>();
			String serviceactivityType = CommonUtils.checkNull(request.getParamValue("serviceactivity_type"));//活动类型
			String ButieBh = CommonUtils.checkNull(request.getParamValue("ButieBh"));//主题编号
			String ButieName = CommonUtils.checkNull(request.getParamValue("ButieName"));//主题名称
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealer_code"));//服务商代码
			String bigorgId = CommonUtils.checkNull(request.getParamValue("big_org"));//大区

			map.put("serviceactivityType", serviceactivityType);
			map.put("ButieBh", ButieBh);
			map.put("ButieName", ButieName);
			map.put("dealerCode", dealerCode);
			map.put("bigorgId", bigorgId);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
					.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps= cdao.RqueryCustomerCareActivityDel(map,Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "客户关怀活动数据明细表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

		}
	

	/**
	 * 导出页面
	 */
	public void CustomerCareActivityDelExport(){
		OutputStream os = null;

	
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			String serviceactivityType = CommonUtils.checkNull(request.getParamValue("serviceactivity_type"));//活动类型
			String ButieBh = CommonUtils.checkNull(request.getParamValue("ButieBh"));//主题编号
			String ButieName = CommonUtils.checkNull(request.getParamValue("ButieName"));//主题名称
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealer_code"));//服务商代码
			String bigorgId = CommonUtils.checkNull(request.getParamValue("big_org"));//大区

			map.put("serviceactivityType", serviceactivityType);
			map.put("ButieBh", ButieBh);
			map.put("ButieName", ButieName);
			map.put("dealerCode", dealerCode);
			map.put("bigorgId", bigorgId);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
					.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps= cdao.RqueryCustomerCareActivityDel(map,99999, curPage);
			ResponseWrapper response = act.getResponse();
			// 导出的文件名
			String fileName = "客户关怀活动数据明细表.xls";
			// 导出的文字编码客户关怀活动数据明细表
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			// 定义一个集合
			List<List<Object>> list = new LinkedList<List<Object>>();
			// 标题
			List<Object> listTemp = new LinkedList<Object>();
			listTemp.add("大区");
			listTemp.add("服务站代码");
			listTemp.add("服务站名称");
			listTemp.add("主题编号");
			listTemp.add("活动主题");
			listTemp.add("活动编号");
			listTemp.add("工单号码");
			listTemp.add("车型");
			listTemp.add("VIN号码");
			listTemp.add("开单日期");
			listTemp.add("公里数");
			listTemp.add("车主");
			listTemp.add("车主电话");
			listTemp.add("送修人");
			listTemp.add("送修人电话");
			listTemp.add("免费检测费用");
			listTemp.add("赠送金额");
			listTemp.add("赠送礼品费用");
			listTemp.add("赠送配件费用");
			listTemp.add("赠送保养金额");
			listTemp.add("材料优惠费用");
			listTemp.add("工时优惠费用");
			list.add(listTemp);
			List<Map<String, Object>> rslist = ps.getRecords();
			if(rslist!=null){
			for (int i = 0; i < rslist.size(); i++) {
				map = rslist.get(i);
				List<Object> listValue = new LinkedList<Object>();
				listValue = new LinkedList<Object>();
				
				
				listValue.add(map.get("ROOT_ORG_NAME") != null ? map.get("ROOT_ORG_NAME") : "");
				listValue.add(map.get("DEALER_CODE") != null ? map.get("DEALER_CODE") : "");
				listValue.add(map.get("DEALER_NAME") != null ? map.get("DEALER_NAME") : "");
				listValue.add(map.get("SUBJECT_NO") != null ? map.get("SUBJECT_NO") : "");
				listValue.add(map.get("SUBJECT_NAME") != null ? map.get("SUBJECT_NAME") : "");
				listValue.add(map.get("ACTIVITY_CODE") != null ? map.get("ACTIVITY_CODE") : "");
			//	listValue.add(map.get("ACTIVITY_NAME") != null ? map.get("ACTIVITY_NAME") : "");
				listValue.add(map.get("RO_NO") != null ? map.get("RO_NO") : "");
				listValue.add(map.get("MODEL") != null ? map.get("MODEL") : "");
				listValue.add(map.get("VIN") != null ? map.get("VIN") : "");
				listValue.add(map.get("RO_CREATE_DATE") != null ? map.get("RO_CREATE_DATE") : "");
				listValue.add(map.get("IN_MILEAGE") != null ? map.get("IN_MILEAGE") : "");
				listValue.add(map.get("OWNER_NAME") != null ? map.get("OWNER_NAME") : "");
				listValue.add(map.get("MAIN_PHONE") != null ? map.get("MAIN_PHONE") : "");
				listValue.add(map.get("DELIVERER") != null ? map.get("DELIVERER") : "");
				listValue.add(map.get("DELIVERER_PHONE") != null ? map.get("DELIVERER_PHONE") : "");
				listValue.add(map.get("FREE_JC_AMOUNT") != null ? map.get("FREE_JC_AMOUNT") : "");
				listValue.add(map.get("ZS_AMOUNT") != null ? map.get("ZS_AMOUNT") : "");
				listValue.add(map.get("LP_AMOUNT") != null ? map.get("LP_AMOUNT") : "");
				listValue.add(map.get("PART_ZS_AMOUNT") != null ? map.get("PART_ZS_AMOUNT") : "");
				listValue.add(map.get("BAOYANG_ZS_AMOUNT") != null ? map.get("BAOYANG_ZS_AMOUNT") : "");
				listValue.add(map.get("PART_AMOUNT") != null ? map.get("PART_AMOUNT") : "");
				listValue.add(map.get("LABOUR_AMOUNT") != null ? map.get("LABOUR_AMOUNT") : "");
				list.add(listValue);
			}
			}
			os = response.getOutputStream();
			CsvWriterUtil.createXlsFileToName(list, os,"客户关怀活动数据明细表");

			os.flush();			
	  } catch (Exception e) {
		BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"客户关怀活动数据明细表导出");
		logger.error(logonUser,e1);
		act.setException(e1);
		}
	
	}
}
