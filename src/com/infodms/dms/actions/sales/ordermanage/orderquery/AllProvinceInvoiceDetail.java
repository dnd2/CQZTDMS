package com.infodms.dms.actions.sales.ordermanage.orderquery;
/**
 * @author YH
 * @date:Dec 3, 2010 3:32:08 PM
 * @version : 1.0
 * 
 */
/**
 * 全省启票明细
 */


import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.StandardVipApplyManagerBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.sales.ordermanage.orderquery.DlvryAmountQueryDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

import flex.messaging.io.ArrayList;


public class AllProvinceInvoiceDetail {
   
	public Logger logger = Logger.getLogger(AllProvinceInvoiceDetail.class);
	
	private static final DlvryAmountQueryDao dao = DlvryAmountQueryDao.getInstance();

	private final String initUrl = "/jsp/sales/ordermanage/orderquery/AllProvinceInvoiceDetailInit.jsp";
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	
	public void AllProvinceInvoiceDetailInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(initUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"全省启票明细查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public void queryAllProvinceInvoiceDetail(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.queryAllProvinceInvoiceDetail(dealerCode,beginTime, endTime, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "启票明细显示");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	public List AllProvinceInvoiceDetailExcel(){
		AclUserBean logonUser = null;
		OutputStream os = null;
		Map map=new HashMap();
		try {
			ResponseWrapper response = act.getResponse();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			// 导出的文件名
			String fileName = "启票明细表下载.csv";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			// 定义一个集合
			List<List<Object>> list = new LinkedList<List<Object>>();
			// 标题
			List<Object> listTemp = new LinkedList<Object>();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
					String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			PageResult<Map<String, Object>> ps = dao.queryAllProvinceInvoiceDetail(dealerCode,beginTime, endTime,9999, curPage);
			
			listTemp.add("登记日期");
			listTemp.add("采购订单号");
			listTemp.add("总部销售订单");
			listTemp.add("启票价格");
			listTemp.add("车辆编码");
			listTemp.add("数量");
			listTemp.add("生产组织");
			listTemp.add("发运地点");
			listTemp.add("所属单位");
			listTemp.add("采购单位");
			listTemp.add("收车单位");
			list.add(listTemp);
			List<Map<String, Object>> rslist = ps.getRecords();
			if(rslist!=null){
			for (int i = 0; i < rslist.size(); i++) {
				map = rslist.get(i);
				List<Object> listValue = new LinkedList<Object>();
				listValue = new LinkedList<Object>();
				listValue.add(map.get("BILLING_DATE") != null ? map.get("BILLING_DATE") : "");
				listValue.add(map.get("ORDER_NO") != null ? map.get("ORDER_NO") : "");
				listValue.add(map.get("ERP_ORDER") != null ? map.get("ERP_ORDER") : "");
				listValue.add(map.get("BILLING_AMOUNT") != null ? map.get("BILLING_AMOUNT") : "");
				listValue.add(map.get("MATERIAL_CODE") != null ? map.get("MATERIAL_CODE") : "");
				listValue.add(map.get("DELIVERY_AMOUNT") != null ? map.get("DELIVERY_AMOUNT") : "");
				listValue.add(map.get("CODE_DESC") != null ? map.get("CODE_DESC") : "");
				listValue.add(map.get("ADDRESS") != null ? map.get("ADDRESS") : "");
				listValue.add(map.get("DEALER_NAME1") != null ? map.get("DEALER_NAME1") : "");
				listValue.add(map.get("DEALER_NAME2") != null ? map.get("DEALER_NAME2") : "");
				listValue.add(map.get("DEALER_NAME3") != null ? map.get("DEALER_NAME3") : "");
				list.add(listValue);
			 }
			}
			os = response.getOutputStream();
			CsvWriterUtil.writeCsv(list, os);
			os.flush();
			return null;
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "启票明细:下载");
			logger.error(logonUser,e1);
			act.setException(e1);
		} finally {
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	
	}
	
	
	
//	public void AllProvinceInvoiceDetailExcel(){
//		ActionContext act = ActionContext.getContext();
//		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
//		RequestWrapper request = act.getRequest();
//		try {
//			String beginTime = request.getParamValue("beginTime");
//			String endTime = request.getParamValue("endTime");
//			//当开始时间和结束时间相同时
//			if(null!=beginTime&&!"".equals(beginTime)&&null!=endTime&&!"".equals(endTime)){
//					beginTime = beginTime+" 00:00:00";
//					endTime = endTime+" 23:59:59";
//			}
//			String[] head=new String[13];
//			head[0]="登记日期";
//			head[1]="采购订单号";
//			head[2]="总部销售订单"; 
//			head[3]="启票价格"; 
//			head[4]="启票价格";
//			head[5]="车辆编码";
//			head[6]="数量";
//			head[7]="生产组织";
//			head[8]="发运地点";
//			head[9]="所属单位";
//			head[10]="采购单位";
//			head[11]="收车单位";
//			StandardVipApplyManagerBean bean = new StandardVipApplyManagerBean();
//			bean.setBeginTime(beginTime);
//			bean.setEndTime(endTime);
//			List<Map<String, Object>> list= this.InvoiceDetailDownLoad();
//		    List list1=new ArrayList();
//		    if(list!=null&&list.size()!=0){
//				for(int i=0;i<list.size();i++){
//			    	Map map =(Map)list.get(i);
//			    	if(map!=null&&map.size()!=0){
//						String[]detail=new String[13];
//						detail[0]  = String.valueOf(map.get("BILLING_DATE"));
//						detail[1]  = String.valueOf(map.get("ORDER_NO"));
//						detail[2]  = String.valueOf(map.get("ERP_ORDER"));
//						detail[3]  = String.valueOf(map.get("BILLING_AMOUNT"));
//						detail[5]  = String.valueOf(map.get("MATERIAL_CODE"));
//						detail[6]  = String.valueOf(map.get("DELIVERY_AMOUNT"));
//						detail[7]  = String.valueOf(map.get("CODE_DESC"));
//						detail[8]  = String.valueOf(map.get("ADDRESS"));
//						detail[9]  = String.valueOf(map.get("DEALER_NAME1"));
//						detail[10] = String.valueOf(map.get("DEALER_NAME2"));
//						detail[11] = String.valueOf(map.get("DEALER_NAME3"));
//						list1.add(detail);
//			    	}
//			      }
//				com.infodms.dms.actions.claim.basicData.ToExcel.toExcel(ActionContext.getContext().getResponse(), request, head, list1);
//		    }
//		    act.setForword(initUrl);	
//			}catch (Exception e) {
//				BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"轿车维修车辆报表(不包含服务商)");
//				logger.error(logonUser,e1);
//				act.setException(e1);
//			}
//	}
	
	public List InvoiceDetailDownLoad(){
		AclUserBean logonUser = null;
		OutputStream os = null;
		try {
			ResponseWrapper response = act.getResponse();
			Map map = new HashMap();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));
			// 导出的文件名
			String fileName = "启票明细表.csv";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			// 定义一个集合
			List<List<Object>> list = new LinkedList<List<Object>>();
			// 标题
			List<Object> listTemp = new LinkedList<Object>();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")): 1; // 处理当前页
					String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			PageResult<Map<String, Object>> ps = dao.queryAllProvinceInvoiceDetail(dealerCode,startDate, endDate,  999 , curPage);
			listTemp.add("登记日期");
			listTemp.add("采购订单号");
			listTemp.add("总部销售订单");
			listTemp.add("物料单价");
			listTemp.add("启票价格");
			listTemp.add("车辆编码");
			listTemp.add("数量");
			listTemp.add("生产组织");
			listTemp.add("发运地点");
			listTemp.add("所属单位");
			listTemp.add("采购单位");
			listTemp.add("收车单位");
			list.add(listTemp);
			List<Map<String, Object>> rslist = ps.getRecords();
			if(rslist!=null){
			for (int i = 0; i < rslist.size(); i++) {
				map = rslist.get(i);
				List<Object> listValue = new LinkedList<Object>();
				listValue = new LinkedList<Object>();
				listValue.add(map.get("BILLING_DATE") != null ? map.get("BILLING_DATE") : "");
				listValue.add(map.get("ORDER_NO") != null ? map.get("ORDER_NO") : "");
				listValue.add(map.get("ERP_ORDER") != null ? map.get("ERP_ORDER") : "");
				listValue.add(map.get("SINGLE_PRICE") != null ? map.get("SINGLE_PRICE") : "");
				listValue.add(map.get("BILLING_AMOUNT") != null ? map.get("BILLING_AMOUNT") : "");
				listValue.add(map.get("MATERIAL_CODE") != null ? map.get("MATERIAL_CODE") : "");
				listValue.add(map.get("DELIVERY_AMOUNT") != null ? map.get("DELIVERY_AMOUNT") : "");
				listValue.add(map.get("CODE_DESC") != null ? map.get("CODE_DESC") : "");
				listValue.add(map.get("ADDRESS") != null ? map.get("ADDRESS") : "");
				listValue.add(map.get("UNIT") != null ? map.get("UNIT") : "");
				listValue.add(map.get("ORDER_ORG_ID") != null ? map.get("ORDER_ORG_ID") : "");
				listValue.add(map.get("RECEIVER") != null ? map.get("RECEIVER") : "");
				list.add(listValue);
			 }
			}
			os = response.getOutputStream();
			CsvWriterUtil.writeCsv(list, os);
			os.flush();
			return null;
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "启票明细:下载");
			logger.error(logonUser,e1);
			act.setException(e1);
		} finally {
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	
	}
}
