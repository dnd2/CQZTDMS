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
import com.infodms.dms.dao.report.serviceReport.ClaimOldSignDelDao;
import com.infodms.dms.dao.report.serviceReport.ClaimReportDao;
import com.infodms.dms.dao.report.serviceReport.OldPieceAuditDelDao;
import com.infodms.dms.dao.tccode.TcCodeDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

public class ClaimOldSignDel {
	

	private Logger logger = Logger.getLogger(ClaimOldSignDel.class);
	private final ClaimOldSignDelDao cdao = ClaimOldSignDelDao.getInstance();
	private final ClaimReportDao dao  = ClaimReportDao.getInstance();


	private final String Claim_Old_Sign_Del = "/jsp/report/service/Claim_Old_Sign_Del.jsp";

	ActionContext act = ActionContext.getContext();
 	AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
 	RequestWrapper request = act.getRequest();
	 
 /**
 * 初始化页面
 */
public void ClaimOldSignDelinit(){

    	try {
           List<Map<String, Object>> list = dao.getBigOrgList();
    		act.setOutData("list", list);
    		act.setForword(Claim_Old_Sign_Del);
    		
    	} catch (Exception e) {
    		BizException e1 = new BizException(act, e,
    				ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件签收情况明细表");
    		logger.error(logonUser, e1);
    		act.setException(e1);
    	}
    	
    }

/**
 * 查询页面
 */
public void ClaimOldSignDelQueryData(){

	try {
		Map<String, String> map = new HashMap<String, String>();
		String deliveryBatch = CommonUtils.checkNull(request.getParamValue("delivery_batch"));//回运批次
		String Signpeople = CommonUtils.checkNull(request.getParamValue("Signpeople"));//签收人
		String bigorg = CommonUtils.checkNull(request.getParamValue("big_org"));//大区
		String dealercode = CommonUtils.checkNull(request.getParamValue("dealer_code"));//经销商代码
		String smallorg = CommonUtils.checkNull(request.getParamValue("small_org"));//小区
		String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName"));//经销商全称
		String bDate = CommonUtils.checkNull(request.getParamValue("bDate"));//发运开始时间
		String eDate = CommonUtils.checkNull(request.getParamValue("eDate"));//发运结束时间
		String bgDate = CommonUtils.checkNull(request.getParamValue("bgDate"));//签收开始时间
		String egDate = CommonUtils.checkNull(request.getParamValue("egDate"));//签收结束时间


		map.put("deliveryBatch", deliveryBatch);
		map.put("Signpeople", Signpeople);
		map.put("bigorg", bigorg);
		map.put("dealercode", dealercode);
		map.put("smallorg", smallorg);
		map.put("dealerName", dealerName);
		map.put("bDate", bDate);
		map.put("eDate", eDate);
		map.put("bgDate", bgDate);
		map.put("egDate", egDate);

		Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
				.getParamValue("curPage")) : 1;
		PageResult<Map<String, Object>> ps=cdao.QueryClaimOldSignDel(map, Constant.PAGE_SIZE, curPage);
		act.setOutData("ps", ps);
	} catch (Exception e) {
		BizException e1 = new BizException(act, e,
				ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件签收情况明细表");
		logger.error(logonUser, e1);
		act.setException(e1);
	}

	}
/**
 * 导出页面
 */
public void ClaimOldSignDelExport(){
	OutputStream os = null;

	try {
		Map<String, String> map = new HashMap<String, String>();
		String deliveryBatch = CommonUtils.checkNull(request.getParamValue("delivery_batch"));//回运批次
		String Signpeople = CommonUtils.checkNull(request.getParamValue("Signpeople"));//签收人
		String bigorg = CommonUtils.checkNull(request.getParamValue("big_org"));//大区
		String dealercode = CommonUtils.checkNull(request.getParamValue("dealer_code"));//经销商代码
		String smallorg = CommonUtils.checkNull(request.getParamValue("small_org"));//小区
		String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName"));//经销商全称
		String bDate = CommonUtils.checkNull(request.getParamValue("bDate"));//发运开始时间
		String eDate = CommonUtils.checkNull(request.getParamValue("eDate"));//发运结束时间
		String bgDate = CommonUtils.checkNull(request.getParamValue("bgDate"));//签收开始时间
		String egDate = CommonUtils.checkNull(request.getParamValue("egDate"));//签收结束时间


		map.put("deliveryBatch", deliveryBatch);
		map.put("Signpeople", Signpeople);
		map.put("bigorg", bigorg);
		map.put("dealercode", dealercode);
		map.put("smallorg", smallorg);
		map.put("dealerName", dealerName);
		map.put("bDate", bDate);
		map.put("eDate", eDate);
		map.put("bgDate", bgDate);
		map.put("egDate", egDate);

		Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
				.getParamValue("curPage")) : 1;
		PageResult<Map<String, Object>> ps=cdao.QueryClaimOldSignDel(map, 999999, curPage);
		ResponseWrapper response = act.getResponse();
		// 导出的文件名
		String fileName = "索赔旧件签收情况明细表.xls";
		// 导出的文字编码客户关怀活动数据明细表
		fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
		response.setContentType("Application/text/csv");
		response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
		// 定义一个集合
		List<List<Object>> list = new LinkedList<List<Object>>();
		// 标题
		List<Object> listTemp = new LinkedList<Object>();
		listTemp.add("经销商代码");
		listTemp.add("经销商全称");
		listTemp.add("回运批次");
		listTemp.add("小区");
		listTemp.add("货运方式");
		listTemp.add("回运类型");
		listTemp.add("发运单号");
		listTemp.add("发运时间");
		listTemp.add("装箱总数");
		listTemp.add("实到箱数");
		listTemp.add("包装情况");
		listTemp.add("故障卡情况");
		listTemp.add("清单情况");
		listTemp.add("签收人");
		listTemp.add("签收时间");
		listTemp.add("三包员电话");
		listTemp.add("签收备注");
		
		list.add(listTemp);
		List<Map<String, Object>> rslist = ps.getRecords();
		Map<String, Object> map1 = new HashMap<String, Object>();

		if(rslist!=null){

		for (int i = 0; i < rslist.size(); i++) {
			map1 = rslist.get(i);
			List<Object> listValue = new LinkedList<Object>();
			listValue = new LinkedList<Object>();
			listValue.add(map1.get("DEALER_CODE") != null ? map1.get("DEALER_CODE") : "");
			listValue.add(map1.get("DEALER_NAME") != null ? map1.get("DEALER_NAME") : "");
			listValue.add(map1.get("PC") != null ? map1.get("PC") : "");
			listValue.add(map1.get("ORG_NAME") != null ? map1.get("ORG_NAME") : "");
			listValue.add(map1.get("TRANSPORT_TYPE") != null ? map1.get("TRANSPORT_TYPE") : "");
			listValue.add(map1.get("RETURN_TYPE") != null ? map1.get("RETURN_TYPE") : "");
			listValue.add(map1.get("TRANSPORT_NO") != null ? map1.get("TRANSPORT_NO").toString() : "");
			listValue.add(map1.get("SEND_TIME") != null ? map1.get("SEND_TIME") : "");
			listValue.add(map1.get("PARKAGE_AMOUNT") != null ? map1.get("PARKAGE_AMOUNT") : "");
			listValue.add(map1.get("REAL_BOX_NO") != null ? map1.get("REAL_BOX_NO") : "");
			listValue.add(map1.get("PART_PAKGE") != null ? map1.get("PART_PAKGE") : "");
			listValue.add(map1.get("PART_MARK") != null ?TcCodeDao.getInstance().getCodeDescByCodeId(map1.get("PART_MARK").toString()):"");
			listValue.add(map1.get("PART_DETAIL") != null ? map1.get("PART_DETAIL") : "");
			listValue.add(map1.get("NAME") != null ? map1.get("NAME") : "");
			listValue.add(map1.get("SIGN_DATE") != null ? map1.get("SIGN_DATE") : "");
			listValue.add(map1.get("TEL") != null ? map1.get("TEL") : "");
			listValue.add(map1.get("SIGN_REMARK") != null ? map1.get("SIGN_REMARK") : "");
			list.add(listValue);

		}
		}
		os = response.getOutputStream();
		CsvWriterUtil.createXlsFileToName(list, os,"索赔旧件签收情况明细表");
		os.flush();	

	} catch (Exception e) {
		BizException e1 = new BizException(act, e,
				ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件签收情况明细表导出");
		logger.error(logonUser, e1);
		act.setException(e1);
	}

  }


}
