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
import com.infodms.dms.dao.report.serviceReport.ClaimReportDao;
import com.infodms.dms.dao.report.serviceReport.OldPieceAuditDelDao;
import com.infodms.dms.dao.report.serviceReport.TechnologyUpgradeDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

public class OldPieceAuditDel {

	
	private Logger logger = Logger.getLogger(OldPieceAuditDel.class);
	private final OldPieceAuditDelDao cdao = OldPieceAuditDelDao.getInstance();
	private final ClaimReportDao dao  = ClaimReportDao.getInstance();
	private static final AjaxSelectDao ajaxDao = AjaxSelectDao.getInstance();


	private final String OLD_PIECE_AUDIT_DEL = "/jsp/report/service/Old_Piece_Audit_Del.jsp";

	ActionContext act = ActionContext.getContext();
 	AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
 	RequestWrapper request = act.getRequest();
	 
 /**
 * 初始化页面
 */
public void OldPieceAuditDelinit(){

    	try {
			List<Map<String, Object>> list = dao.getSmallOrgList("");
			act.setOutData("list", list);

    		act.setForword(OLD_PIECE_AUDIT_DEL);
    		
    	} catch (Exception e) {
    		BizException e1 = new BizException(act, e,
    				ErrorCodeConstant.QUERY_FAILURE_CODE, "客户关怀活动数据统计表");
    		logger.error(logonUser, e1);
    		act.setException(e1);
    	}
    	
    }

/**
 * 查询页面
 */
public void OldPieceAuditDelQueryData(){

	try {
		Map<String, String> map = new HashMap<String, String>();
		String settlementNo = CommonUtils.checkNull(request.getParamValue("settlementNo"));//结算单号
		String vin = CommonUtils.checkNull(request.getParamValue("vin"));//Vin
		String OldpartDeductType = CommonUtils.checkNull(request.getParamValue("OLDPART_DEDUCT_TYPE"));//扣减愿意
		String dealercode = CommonUtils.checkNull(request.getParamValue("dealer_code"));//经销商代码
		String smallorg = CommonUtils.checkNull(request.getParamValue("small_org"));//小区
		String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName"));//经销商全称
		String bDate = CommonUtils.checkNull(request.getParamValue("bDate"));//结算开始时间
		String eDate = CommonUtils.checkNull(request.getParamValue("eDate"));//结算结束时间


		map.put("settlementNo", settlementNo);
		map.put("vin", vin);
		map.put("OldpartDeductType", OldpartDeductType);
		map.put("dealercode", dealercode);
		map.put("smallorg", smallorg);
		map.put("dealerName", dealerName);
		map.put("bDate", bDate);
		map.put("eDate", eDate);

		Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
				.getParamValue("curPage")) : 1;
		PageResult<Map<String, Object>> ps=cdao.QueryOldPieceAuditDel(map, Constant.PAGE_SIZE, curPage);
		act.setOutData("ps", ps);
	} catch (Exception e) {
		BizException e1 = new BizException(act, e,
				ErrorCodeConstant.QUERY_FAILURE_CODE, "旧件审核扣件明细表查询");
		logger.error(logonUser, e1);
		act.setException(e1);
	}

	}


/**
 * 导出页面
 */
public void OldPieceAuditDelExport(){
	OutputStream os = null;

	try {
		Map<String, String> map = new HashMap<String, String>();
		String settlementNo = CommonUtils.checkNull(request.getParamValue("settlementNo"));//结算单号
		String vin = CommonUtils.checkNull(request.getParamValue("vin"));//Vin
		String OldpartDeductType = CommonUtils.checkNull(request.getParamValue("OLDPART_DEDUCT_TYPE"));//扣减愿意
		String dealercode = CommonUtils.checkNull(request.getParamValue("dealer_code"));//经销商代码
		String smallorg = CommonUtils.checkNull(request.getParamValue("small_org"));//小区
		String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName"));//经销商全称
		String bDate = CommonUtils.checkNull(request.getParamValue("bDate"));//结算开始时间
		String eDate = CommonUtils.checkNull(request.getParamValue("eDate"));//结算结束时间


		map.put("settlementNo", settlementNo);
		map.put("vin", vin);
		map.put("OldpartDeductType", OldpartDeductType);
		map.put("dealercode", dealercode);
		map.put("smallorg", smallorg);
		map.put("dealerName", dealerName);
		map.put("bDate", bDate);
		map.put("eDate", eDate);
		Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
				.getParamValue("curPage")) : 1;
		PageResult<Map<String, Object>> ps=cdao.QueryOldPieceAuditDel(map, 999999, curPage);
		ResponseWrapper response = act.getResponse();
		// 导出的文件名
		String fileName = "旧件审核扣件明细表.xls";
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
		listTemp.add("小区");
		listTemp.add("车型");
		listTemp.add("VIN");
		listTemp.add("结算单号");
		listTemp.add("故障件代码（换下件）");
		listTemp.add("故障件名称");
		listTemp.add("申报金额");
		listTemp.add("回运数");
		listTemp.add("签收数");
		listTemp.add("扣件原因");
		listTemp.add("条码");
		listTemp.add("旧件审核日期");

		
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
			listValue.add(map1.get("ORG_NAME") != null ? map1.get("ORG_NAME") : "");
			listValue.add(map1.get("MODEL_NAME") != null ? map1.get("MODEL_NAME") : "");
			listValue.add(map1.get("VIN") != null ? map1.get("VIN") : "");
			listValue.add(map1.get("CLAIM_NO") != null ? map1.get("CLAIM_NO") : "");
			listValue.add(map1.get("PART_CODE") != null ? map1.get("PART_CODE") : "");
			listValue.add(map1.get("PART_NAME") != null ? map1.get("PART_NAME") : "");
			listValue.add(map1.get("PRICE") != null ? map1.get("PRICE") : "");
			listValue.add(map1.get("RETURN_NUM") != null ? map1.get("RETURN_NUM") : "");
			listValue.add(map1.get("SIGN_NUM") != null ? map1.get("SIGN_NUM") : "");
			listValue.add(map1.get("DEDUCT_REMARK") != null ? map1.get("DEDUCT_REMARK") : "");
			listValue.add(map1.get("BARCODE_NO") != null ? map1.get("BARCODE_NO") : "");
			listValue.add(map1.get("REPORT_DATE") != null ? map1.get("REPORT_DATE") : "");
			list.add(listValue);

		}
		}
		os = response.getOutputStream();
		CsvWriterUtil.createXlsFileToName(list, os,"旧件审核扣件明细表");
		os.flush();	

	} catch (Exception e) {
		BizException e1 = new BizException(act, e,
				ErrorCodeConstant.QUERY_FAILURE_CODE, "旧件审核扣件明细表导出");
		logger.error(logonUser, e1);
		act.setException(e1);
	}

  }


}
