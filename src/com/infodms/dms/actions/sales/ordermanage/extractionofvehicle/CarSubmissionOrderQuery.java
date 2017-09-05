/**
 * FileName: CarSubmissionDLR.java
 * Author: yudanwen
 * Date: 2013-4-8 下午08:19:21
 * Email: wyd_soul@163.com
 * Copyright ORARO Corporation 2013
 */

package com.infodms.dms.actions.sales.ordermanage.extractionofvehicle;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.claim.basicData.ToExcel;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.component.dict.CodeDict;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.AjaxSelectDao;
import com.infodms.dms.dao.sales.ordermanage.carSubmission.CarSubmissionQueryDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtVsOrderDetailPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * @Description
 * @author yudanwen
 * @date 2013-4-8 下午08:19:21
 * @version 2.0
 */
public class CarSubmissionOrderQuery {

	private Logger logger = Logger.getLogger(CarSubmissionOrderQuery.class);
	private ActionContext act = ActionContext.getContext();
	private RequestWrapper request = act.getRequest();
	private ResponseWrapper response = act.getResponse();

	private static final CarSubmissionQueryDao dao = CarSubmissionQueryDao.getInstance();
	private static final AjaxSelectDao ajaxDao = AjaxSelectDao.getInstance();

	private static final String ORDER_MATERIAL_PRIV = "/jsp/sales/ordermanage/extractionofvehicle/audit/material_priv.jsp";
	private static final String ORDER_MATERIAL_PRIV_01 = "/jsp/sales/ordermanage/extractionofvehicle/audit/material_priv_01.jsp";
	private static final String ORDER_DETAIL_PAGE = "/jsp/sales/ordermanage/extractionofvehicle/order.jsp";
	private static final String ORDER_DETAIL_PAGE_ALL = "/jsp/sales/ordermanage/extractionofvehicle/order_detail.jsp";
	private static final String RESOURCE_DETAIL = "/jsp/sales/ordermanage/extractionofvehicle/searches/resource_shared.jsp";

	/**
	 * 方法描述 ： 查询提车单统计明细信息<br/>
	 * 
	 * @author wangsongwei
	 */
	public void downLoadCountQueryDetail() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		OutputStream os = null;
		try
		{
			Map<String, Object> infoMap = new HashMap<String, Object>();

			infoMap.put("groupCode", CommonUtils.checkNull(request.getParamValue("groupCode")));
			infoMap.put("materialCode", CommonUtils.checkNull(request.getParamValue("materialCode")));
			infoMap.put("orderType", CommonUtils.checkNull(request.getParamValue("orderType")));
			infoMap.put("orderStatus", CommonUtils.checkNull(request.getParamValue("orderStatus")));
			infoMap.put("orderNo", CommonUtils.checkNull(request.getParamValue("orderNo")));
			infoMap.put("startdate", CommonUtils.checkNull(request.getParamValue("startdate")));
			infoMap.put("endDate", CommonUtils.checkNull(request.getParamValue("endDate")));
			infoMap.put("vin", CommonUtils.checkNull(request.getParamValue("vin")));
			infoMap.put("isdisabled", CommonUtils.checkNull(request.getParamValue("isdisabled")));
			infoMap.put("invoiceNo", CommonUtils.checkNull(request.getParamValue("invoiceNo")));
			infoMap.put("areaId", CommonUtils.checkNull(request.getParamValue("areaId")));
			infoMap.put("dealerCode", CommonUtils.checkNull(request.getParamValue("dealerCode")));
			infoMap.put("cstartdate", CommonUtils.checkNull(request.getParamValue("cstartdate")));
			infoMap.put("cendDate", CommonUtils.checkNull(request.getParamValue("cendDate")));
			infoMap.put("isout", CommonUtils.checkNull(request.getParamValue("isout")));

			List<Map<String, Object>> expList = dao.getOrderCountDetailList(infoMap, logonUser, 1, 99999).getRecords();

			// 用于下载传参的集合
			List<List<Object>> list = new LinkedList<List<Object>>();

			// 标题
			List<Object> listHead = new LinkedList<Object>();
			listHead.add("省份");
			listHead.add("车系");
			listHead.add("经销商代码");
			listHead.add("经销商代码");
			listHead.add("经销商名称");
			listHead.add("销售订单号");
			listHead.add("提报日期");
			listHead.add("提车单类型");
			listHead.add("提车单状态");
			listHead.add("提报数量");
			listHead.add("审核数量");
			listHead.add("开票数量");
			listHead.add("组板数量");
			listHead.add("配车数量");
			listHead.add("出库数量");
			listHead.add("发运数量");
			listHead.add("验收数量");
			listHead.add("运送地址");

			list.add(listHead);

			if (!CommonUtils.isNullList(expList))
			{
				for (int i = 0; i < expList.size(); i++)
				{
					List<Object> listValue = new LinkedList<Object>();
					Map map = expList.get(i);
					String orgName = CommonUtils.checkNull(map.get("ORG_NAME"));
					String seriesName = CommonUtils.checkNull(map.get("SERIES_NAME"));
					String dealerCode = CommonUtils.checkNull(map.get("DEALER_CODE"));
					String dealerName = CommonUtils.checkNull(map.get("DEALER_SHORTNAME"));
					String orderNo = CommonUtils.checkNull(map.get("ORDER_NO"));
					String raiseDate = CommonUtils.checkNull(map.get("RAISE_DATE"));
					String orderType = CommonUtils.checkNull(map.get("ORDER_TYPE"));
					String orderStatus = CommonUtils.checkNull(map.get("ORDER_STATUS"));
					String orderAmount = CommonUtils.checkNull(map.get("ORDER_AMOUNT"));
					String checkAmount = CommonUtils.checkNull(map.get("CHECK_AMOUNT"));
					String invoAmount = CommonUtils.checkNull(map.get("INVO_AMOUNT"));
					String boardNumber = CommonUtils.checkNull(map.get("BOARD_NUMBER"));
					String matchAmount = CommonUtils.checkNull(map.get("MATCH_AMOUNT"));
					String outAmount = CommonUtils.checkNull(map.get("OUT_AMOUNT"));
					String deliveryAmount = CommonUtils.checkNull(map.get("DELIVERY_AMOUNT"));
					String accAmount = CommonUtils.checkNull(map.get("ACC_AMOUNT"));
					String address = CommonUtils.checkNull(map.get("ADDRESS"));
					
					listValue.add(orgName);
					listValue.add(seriesName);
					listValue.add(dealerCode);
					listValue.add(dealerName);
					listValue.add(orderNo);
					listValue.add(raiseDate);

					listValue.add(CodeDict.getDictDescById(Constant.ORDER_TYPE.toString(), orderType));
					listValue.add(CodeDict.getDictDescById(Constant.ORDER_STATUS.toString(), orderStatus));
					listValue.add(orderAmount);
					listValue.add(checkAmount);
					listValue.add(invoAmount);
					listValue.add(boardNumber);
					listValue.add(matchAmount);
					listValue.add(outAmount);
					listValue.add(deliveryAmount);
					listValue.add(accAmount);
					listValue.add(address);
					list.add(listValue);
				}
			}
			// 导出的文件名
			String fileName = "提车单明细导出.xls";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/xls");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			os = response.getOutputStream();
			dao.createXlsFile(list, os, "提车单明细导出");
			os.flush();
		}
		catch (Exception e)
		{//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, e.getMessage() + "<br/>");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 方法描述 ： 下载提车单统计明细信息<br/>
	 * 
	 * @author wangsongwei
	 */
	public void downLoadCountQueryTotal() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		OutputStream os = null;
		try
		{
			Map<String, Object> infoMap = new HashMap<String, Object>();

			infoMap.put("groupCode", CommonUtils.checkNull(request.getParamValue("groupCode")));
			infoMap.put("materialCode", CommonUtils.checkNull(request.getParamValue("materialCode")));
			infoMap.put("orderType", CommonUtils.checkNull(request.getParamValue("orderType")));
			infoMap.put("orderStatus", CommonUtils.checkNull(request.getParamValue("orderStatus")));
			infoMap.put("orderNo", CommonUtils.checkNull(request.getParamValue("orderNo")));
			infoMap.put("startdate", CommonUtils.checkNull(request.getParamValue("startdate")));
			infoMap.put("endDate", CommonUtils.checkNull(request.getParamValue("endDate")));
			infoMap.put("vin", CommonUtils.checkNull(request.getParamValue("vin")));
			infoMap.put("isdisabled", CommonUtils.checkNull(request.getParamValue("isdisabled")));
			infoMap.put("invoiceNo", CommonUtils.checkNull(request.getParamValue("invoiceNo")));
			infoMap.put("areaId", CommonUtils.checkNull(request.getParamValue("areaId")));
			infoMap.put("dealerCode", CommonUtils.checkNull(request.getParamValue("dealerCode")));
			infoMap.put("cstartdate", CommonUtils.checkNull(request.getParamValue("cstartdate")));
			infoMap.put("cendDate", CommonUtils.checkNull(request.getParamValue("cendDate")));
			infoMap.put("isout", CommonUtils.checkNull(request.getParamValue("isout")));
			
			List<Map<String, Object>> expList = dao.getOrderCountList(infoMap, logonUser, 1, 99999).getRecords();

			// 用于下载传参的集合
			List<List<Object>> list = new LinkedList<List<Object>>();

			// 标题
			List<Object> listHead = new LinkedList<Object>();

			listHead.add("物料编号");
			listHead.add("物料名称");
			//			listHead.add("提车单类型");
			listHead.add("提报数量");
			listHead.add("审核数量");
			listHead.add("开票数量");
			listHead.add("组板数量");
			listHead.add("配车数量");
			listHead.add("出库数量");
			listHead.add("发运数量");
			listHead.add("验收数量");
			list.add(listHead);

			if (!CommonUtils.isNullList(expList))
			{
				for (int i = 0; i < expList.size(); i++)
				{
					List<Object> listValue = new LinkedList<Object>();
					Map map = expList.get(i);
					String materialCode = CommonUtils.checkNull(map.get("MATERIAL_CODE"));
					String materialName = CommonUtils.checkNull(map.get("MATERIAL_NAME"));
					String orderType = CommonUtils.checkNull(map.get("ORDER_TYPE"));
					String orderAmount = CommonUtils.checkNull(map.get("ORDER_AMOUNT"));
					String checkAmount = CommonUtils.checkNull(map.get("CHECK_AMOUNT"));
					String invoAmount = CommonUtils.checkNull(map.get("INVO_AMOUNT"));
					String boardNumber = CommonUtils.checkNull(map.get("BOARD_NUMBER"));
					String matchAmount = CommonUtils.checkNull(map.get("MATCH_AMOUNT"));
					String outAmount = CommonUtils.checkNull(map.get("OUT_AMOUNT"));
					String deliveryAmount = CommonUtils.checkNull(map.get("DELIVERY_AMOUNT"));
					String accAmount = CommonUtils.checkNull(map.get("ACC_AMOUNT"));

					listValue.add(materialCode);
					listValue.add(materialName);
					//					listValue.add(CodeDict.getDictDescById(Constant.ORDER_TYPE.toString(), orderType));
					listValue.add(orderAmount);
					listValue.add(checkAmount);
					listValue.add(invoAmount);
					listValue.add(boardNumber);
					listValue.add(matchAmount);
					listValue.add(outAmount);
					listValue.add(deliveryAmount);
					listValue.add(accAmount);

					list.add(listValue);
				}
			}
			// 导出的文件名
			String fileName = "提车单汇总统计.xls";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/xls");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			os = response.getOutputStream();
			dao.createXlsFile(list, os, "提车单统计");
			os.flush();
			os.close();
		}
		catch (Exception e)
		{//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, e.getMessage() + "<br/>");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 方法描述 ：下载提车单发运信息 <br/>
	 * 
	 * @author wangsongwei
	 */
	public void downLoadQueryDespatch() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		OutputStream os = null;

		try
		{
			String materialCode = CommonUtils.checkNull(request.getParamValue("materialCode")); //物料组编码
			String startdate = CommonUtils.checkNull(request.getParamValue("startdate"));
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));
			String orderNo = CommonUtils.checkNull(request.getParamValue("orderNo"));
			String deliveryType = CommonUtils.checkNull(request.getParamValue("deliveryType"));
			String deliveryStatus = CommonUtils.checkNull(request.getParamValue("deliveryStatus"));
			String orderType = CommonUtils.checkNull(request.getParamValue("orderType"));
			String accountType = CommonUtils.checkNull(request.getParamValue("accountType"));

			Map<String, Object> infoMap = new HashMap<String, Object>();

			infoMap.put("materialCode", materialCode);
			infoMap.put("startdate", startdate);
			infoMap.put("orderType", orderType);
			infoMap.put("endDate", endDate);
			infoMap.put("orderNo", orderNo);
			infoMap.put("deliveryType", deliveryType);
			infoMap.put("deliveryStatus", deliveryStatus);
			infoMap.put("accountType", accountType);

			String dealerId = logonUser.getDealerId() == null ? "" : logonUser.getDealerId();
			infoMap.put("dealerId", dealerId);

			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页

			PageResult<Map<String, Object>> ps = dao.getOrderDespatchList(infoMap, curPage, Constant.PAGE_SIZE);
			List<Map<String, Object>> expList = ps.getRecords();

			// 用于下载传参的集合
			List<List<Object>> list = new LinkedList<List<Object>>();

			// 标题
			List<Object> listHead = new LinkedList<Object>();

			listHead.add("组织代码");
			listHead.add("组织名称");
			listHead.add("订单类型");
			listHead.add("资金类型");
			listHead.add("提车单号");
			listHead.add("发运数量");
			listHead.add("配车数量");
			listHead.add("总价");
			listHead.add("发运地址");
			listHead.add("运送状态");
			list.add(listHead);
			
			

			if (!CommonUtils.isNullList(expList))
			{
				for (int i = 0; i < expList.size(); i++)
				{
					List<Object> listValue = new LinkedList<Object>();

					listValue.add(expList.get(i).get("ORG_CODE") != null ? expList.get(i).get("ORG_CODE") : "");
					listValue.add(expList.get(i).get("ORG_NAME") != null ? expList.get(i).get("ORG_NAME") : "");
					listValue.add(expList.get(i).get("ORDER_TYPE") != null ? expList.get(i).get("ORDER_TYPE") : "");
					listValue.add(expList.get(i).get("FUND_TYPE_ID") != null ? expList.get(i).get("FUND_TYPE_ID") : "");
					listValue.add(expList.get(i).get("ORDER_NO") != null ? expList.get(i).get("ORDER_NO") : "");
					listValue.add(expList.get(i).get("DELIVERY_AMOUNT") != null ? expList.get(i).get("DELIVERY_AMOUNT") : "");
					listValue.add(expList.get(i).get("MATCH_AMOUNT") != null ? expList.get(i).get("MATCH_AMOUNT") : "");
					listValue.add(expList.get(i).get("ORDER_YF_PRICE") != null ? expList.get(i).get("ORDER_YF_PRICE") : "");
					listValue.add(expList.get(i).get("ADDRESS") != null ? expList.get(i).get("ADDRESS") : "");
					listValue.add(expList.get(i).get("GET_STATUS") != null ? expList.get(i).get("GET_STATUS") : "");

					list.add(listValue);
				}
			}
			// 导出的文件名
			String fileName = "提车单发运明细.xls";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/xls");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			os = response.getOutputStream();
			dao.createXlsFile(list, os, "提车单发运明细");
			os.flush();

		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "提车单提报DLR");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 方法描述 ： 查询提车单统计明细信息<br/>
	 * 
	 * @author wangsongwei
	 */
	public void loadCountQueryDetail() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

		try
		{
			Map<String, Object> infoMap = new HashMap<String, Object>();

			infoMap.put("groupCode", CommonUtils.checkNull(request.getParamValue("groupCode")));
			infoMap.put("materialCode", CommonUtils.checkNull(request.getParamValue("materialCode")));
			infoMap.put("orderType", CommonUtils.checkNull(request.getParamValue("orderType")));
			infoMap.put("orderStatus", CommonUtils.checkNull(request.getParamValue("orderStatus")));
			infoMap.put("orderNo", CommonUtils.checkNull(request.getParamValue("orderNo")));
			infoMap.put("startdate", CommonUtils.checkNull(request.getParamValue("startdate")));
			infoMap.put("endDate", CommonUtils.checkNull(request.getParamValue("endDate")));
			infoMap.put("vin", CommonUtils.checkNull(request.getParamValue("vin")));
			infoMap.put("orgId", CommonUtils.checkNull(request.getParamValue("orgId")));
			infoMap.put("invoiceNo", CommonUtils.checkNull(request.getParamValue("invoiceNo")));
			infoMap.put("areaId", CommonUtils.checkNull(request.getParamValue("areaId")));
			infoMap.put("dealerCode", CommonUtils.checkNull(request.getParamValue("dealerCode")));
			infoMap.put("cstartdate", CommonUtils.checkNull(request.getParamValue("cstartdate")));
			infoMap.put("cendDate", CommonUtils.checkNull(request.getParamValue("cendDate")));
			infoMap.put("isout", CommonUtils.checkNull(request.getParamValue("isout")));
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getOrderCountDetailList(infoMap, logonUser, curPage, Constant.PAGE_SIZE);

			act.setOutData("ps", ps);
		}
		catch (Exception e)
		{//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, e.getMessage() + "<br/>");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 方法描述 ： 查询提车单统计信息<br/>
	 * 
	 * @author wangsongwei
	 */
	public void loadCountQueryTotal() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

		try
		{
			Map<String, Object> infoMap = new HashMap<String, Object>();

			infoMap.put("groupCode", CommonUtils.checkNull(request.getParamValue("groupCode")));
			infoMap.put("materialCode", CommonUtils.checkNull(request.getParamValue("materialCode")));
			infoMap.put("orderType", CommonUtils.checkNull(request.getParamValue("orderType")));
			infoMap.put("orderStatus", CommonUtils.checkNull(request.getParamValue("orderStatus")));
			infoMap.put("orderNo", CommonUtils.checkNull(request.getParamValue("orderNo")));
			infoMap.put("startdate", CommonUtils.checkNull(request.getParamValue("startdate")));
			infoMap.put("endDate", CommonUtils.checkNull(request.getParamValue("endDate")));
			infoMap.put("vin", CommonUtils.checkNull(request.getParamValue("vin")));
			infoMap.put("orgId", CommonUtils.checkNull(request.getParamValue("orgId")));
			infoMap.put("invoiceNo", CommonUtils.checkNull(request.getParamValue("invoiceNo")));
			infoMap.put("areaId", CommonUtils.checkNull(request.getParamValue("areaId")));
			infoMap.put("dealerCode", CommonUtils.checkNull(request.getParamValue("dealerCode")));
			infoMap.put("cstartdate", CommonUtils.checkNull(request.getParamValue("cstartdate")));
			infoMap.put("cendDate", CommonUtils.checkNull(request.getParamValue("cendDate")));
			infoMap.put("isout", CommonUtils.checkNull(request.getParamValue("isout")));
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;

			PageResult<Map<String, Object>> ps = dao.getOrderCountList(infoMap, logonUser, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		}
		catch (Exception e)
		{//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, e.getMessage() + "<br/>");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 方法描述 ： 统计提出信息<br/>
	 * 
	 * @author wangsongwei
	 */
	public void loadCountQueryOrder() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

		try
		{
			Map<String, Object> infoMap = new HashMap<String, Object>();

			infoMap.put("groupCode", CommonUtils.checkNull(request.getParamValue("groupCode")));
			infoMap.put("materialCode", CommonUtils.checkNull(request.getParamValue("materialCode")));
			infoMap.put("orderType", CommonUtils.checkNull(request.getParamValue("orderType")));
			infoMap.put("orderStatus", CommonUtils.checkNull(request.getParamValue("orderStatus")));
			infoMap.put("orderNo", CommonUtils.checkNull(request.getParamValue("orderNo")));
			infoMap.put("startdate", CommonUtils.checkNull(request.getParamValue("startdate")));
			infoMap.put("endDate", CommonUtils.checkNull(request.getParamValue("endDate")));
			infoMap.put("vin", CommonUtils.checkNull(request.getParamValue("vin")));
			infoMap.put("orgId", CommonUtils.checkNull(request.getParamValue("orgId")));
			infoMap.put("invoiceNo", CommonUtils.checkNull(request.getParamValue("invoiceNo")));
			infoMap.put("areaId", CommonUtils.checkNull(request.getParamValue("areaId")));
			infoMap.put("dealerCode", CommonUtils.checkNull(request.getParamValue("dealerCode")));
			infoMap.put("isout", CommonUtils.checkNull(request.getParamValue("isout")));
			/* -----------------------------------------------------------------------------
			 * add by wangsw 2013-11-11 
			 * 查询条件增加开票日期
			 * -----------------------------------------------------------------------------*/
			infoMap.put("cstartdate", CommonUtils.checkNull(request.getParamValue("cstartdate")));
			infoMap.put("cendDate", CommonUtils.checkNull(request.getParamValue("cendDate")));
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			Map<String, Object> countAll = dao.getOrderCountAllList(infoMap, logonUser);

			act.setOutData("data", countAll);
		}
		catch (Exception e)
		{//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, e.getMessage() + "<br/>");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 方法描述 ： 获取产地账户信息<br/>
	 * 
	 * @author wangsongwei
	 */
	public void loadDealerAreaAccountInfo() {

		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

		try
		{
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			String yieldlyId = CommonUtils.checkNull(request.getParamValue("yieldlyId"));
			String finType = CommonUtils.checkNull(request.getParamValue("finType"));

			Map<String, Object> ps = dao.getDealerAccountInfo(dealerId, yieldlyId, finType);

			act.setOutData("info", ps);

		}
		catch (Exception e)
		{//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "提车单提报DLR");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}

	/**
	 * 方法描述 ： <br/>
	 * 
	 * @author wangsongwei
	 */
	public void loadOrderCheckResult() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

		try
		{
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));

			List<?> resultList = dao.getOrderCheckResult(orderId);
			act.setOutData("info", resultList);

		}
		catch (Exception e)
		{//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "提车单提报DLR");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 方法描述 : 加载订单的详细信息<br/>
	 * 
	 * @description
	 * @author wangsongwei
	 */
	public void loadOrderDetail() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

		try
		{
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId")); // 订单ID号

			// 查询订单基本信息返回到页面
			Map<String, Object> map = dao.getOrderSubmissionInfo(orderId);
			act.setOutData("info", map);

		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "提车单提报DLR");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 方法描述 : 查询物料的详细信息
	 * 
	 * @author wangsongwei
	 */
	public void loadOrderMaterialSaved() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

		try
		{
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId")); // 订单ID号

			// 返回订单的物料信息
			List<Map<String, Object>> materialList = dao.getOrderMaterialDetailList(orderId);
			act.setOutData("info", materialList);

		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "查询提车单物料数据失败!");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}

	/**
	 * 方法描述 : 查询物料的详细信息
	 * 
	 * @author wangsongwei
	 */
	public void loadOrderMaterialSavedWithCheck() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

		try
		{
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId")); // 订单ID号

			// 返回订单的物料信息
			List<Map<String, Object>> materialList = dao.getOrderMaterialDetailWithCheckList(orderId);
			act.setOutData("info", materialList);

		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "查询提车单物料数据失败!");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}

	/**
	 * 生成订单转提车单的基础物料数据提取
	 */
	public void loadOrderProductCreateMaterialInfo() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

		try
		{
			String createId = CommonUtils.checkNull(request.getParamValue("createId")); // 订单ID号

			// 返回订单的物料信息
			List<Map<String, Object>> materialList = dao.getProductOrderMaterailList(createId, null);
			act.setOutData("info", materialList);

		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "提车单提报DLR");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 方法描述 ： 加载订单明细信息 <br/>
	 * 
	 * @author wangsongwei
	 */
	public void loadOrderReportDetail() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

		try
		{
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
			act.setOutData("orderId", orderId);

			act.setForword(ORDER_DETAIL_PAGE_ALL);

		}
		catch (Exception e)
		{//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "提车单提报DLR");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 方法描述 ： 加载订单的返利数据<br/>
	 * 
	 * @author wangsongwei
	 */
	public void loadOrderSalesRebate() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));

			List<Map<String, Object>> ps = dao.getOrderRebateList(orderId);

			act.setOutData("info", ps);

		}
		catch (Exception e)
		{//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "提车单提报DLR");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 方法描述 ： 加载物料省份分配<br/>
	 * 
	 * @author wangsongwei
	 */
	public void loadPrivMaterial() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

		try
		{
			String materialId = CommonUtils.checkNull(request.getParamValue("materialId")); // 订单ID号
			String orderDetailId = CommonUtils.checkNull(request.getParamValue("orderDetailId")); // 订单明细ID号

			TtVsOrderDetailPO tvodp_sql = new TtVsOrderDetailPO();
			tvodp_sql.setDetailId(Long.valueOf(orderDetailId));

			List<?> detailList = dao.select(tvodp_sql);
			if (detailList.size() > 0)
			{
				TtVsOrderDetailPO tvodp = (TtVsOrderDetailPO) detailList.get(0);
				act.setOutData("amount", tvodp.getOrderAmount());
			}

			List<Map<String, Object>> materialList = dao.getOrderMaterialPriv(materialId, orderDetailId, logonUser.getPoseId().toString());
			act.setOutData("materialList", materialList);

			act.setOutData("orderDetailId", orderDetailId);
			act.setOutData("mId", materialId);

			act.setForword(ORDER_MATERIAL_PRIV);

		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "提车单提报DLR");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 方法描述 ： 加载物料省份分配<br/>
	 * 
	 * @author wangsongwei
	 */
	public void loadPrivMaterialManager() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

		try
		{
			String materialId = CommonUtils.checkNull(request.getParamValue("materialId")); // 订单ID号
			String orderDetailId = CommonUtils.checkNull(request.getParamValue("orderDetailId")); // 订单明细ID号

			TtVsOrderDetailPO tvodp_sql = new TtVsOrderDetailPO();
			tvodp_sql.setDetailId(Long.valueOf(orderDetailId));

			List<?> detailList = dao.select(tvodp_sql);
			if (detailList.size() > 0)
			{
				TtVsOrderDetailPO tvodp = (TtVsOrderDetailPO) detailList.get(0);
				act.setOutData("amount", tvodp.getOrderAmount());
			}

			List<Map<String, Object>> materialList = dao.getOrderMaterialPriv(materialId, orderDetailId, logonUser.getPoseId().toString());
			act.setOutData("materialList", materialList);

			act.setOutData("orderDetailId", orderDetailId);
			act.setOutData("mId", materialId);

			act.setForword(ORDER_MATERIAL_PRIV_01);

		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "提车单提报DLR");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 方法描述 ： 加载车系折扣率<br/>
	 * 
	 * @author wangsongwei
	 */
	public void loadSeriesDiscount() {

		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

		try
		{
			String seriesId = CommonUtils.checkNull(request.getParamValue("seriesId"));
			String materialCode = CommonUtils.checkNull(request.getParamValue("materialCode"));
			String yieldId = CommonUtils.checkNull(request.getParamValue("yieldId"));
			String finType = CommonUtils.checkNull(request.getParamValue("finType"));
// 资金类型的折扣率按照实际的折扣率获取
//			if (finType.equals(Constant.ACCOUNT_TYPE_05.toString()))
//			{
//				finType = Constant.ACCOUNT_TYPE_02.toString();
//			}

			Map<String, Object> ps = dao.getSeriesDiscount(seriesId, materialCode, yieldId, finType);

			act.setOutData("info", ps);

		}
		catch (Exception e)
		{//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "提车单提报DLR");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 方法描述 ： 财务开票查询<br/>
	 * 
	 * @author wangsongwei
	 */
	public void orderFinInfoQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

		try
		{
			String orderNo = CommonUtils.checkNull(request.getParamValue("orderNo")); // 订单编号
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId")); // 区域ID
			String orgCode = CommonUtils.checkNull(request.getParamValue("orgCode")); // 区域ID
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")); // 经销商代码
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId")); // 经销商代码
			String startdate = CommonUtils.checkNull(request.getParamValue("startdate")); // 提报日期
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate")); // 提报日期
			String cstartdate = CommonUtils.checkNull(request.getParamValue("cstartdate")); // 开票开始日期
			String cendDate = CommonUtils.checkNull(request.getParamValue("cendDate")); // 开票结束日期
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode")); // 物料组编号
			String orderType = CommonUtils.checkNull(request.getParamValue("orderType")); // 订单类型
			String reqStatus = CommonUtils.checkNull(request.getParamValue("reqStatus")); // 订单状态
			String expstatus = CommonUtils.checkNull(request.getParamValue("expstatus")); // 订单状态
			String reqOrderType = CommonUtils.checkNull(request.getParamValue("reqOrderType"));
			String expOrderType = CommonUtils.checkNull(request.getParamValue("expOrderType"));
			String orderDjType = CommonUtils.checkNull(request.getParamValue("orderDjType"));
			String invoiceNo = CommonUtils.checkNull(request.getParamValue("invoiceNo"));
			String common = CommonUtils.checkNull(request.getParamValue("common")); // 类型
			

			Map<String, Object> infoMap = new HashMap<String, Object>();

			infoMap.put("orderNo", orderNo);
			infoMap.put("orgId", orgId);
			infoMap.put("orgCode", orgCode);
			infoMap.put("dealerCode", dealerCode);
			infoMap.put("dealerId", dealerId);
			infoMap.put("endDate", endDate);
			infoMap.put("startdate", startdate);
			infoMap.put("cendDate", cendDate);
			infoMap.put("cstartdate", cstartdate);
			infoMap.put("groupCode", groupCode);
			infoMap.put("orderType", orderType);
			infoMap.put("reqStatus", reqStatus);
			infoMap.put("expstatus", expstatus);
			infoMap.put("reqOrderType", reqOrderType);
			infoMap.put("expOrderType", expOrderType);
			infoMap.put("poseId", logonUser.getPoseId());
			infoMap.put("orderDjType", orderDjType);
			infoMap.put("logonUser", logonUser);
			infoMap.put("invoiceNo", invoiceNo);

			if (logonUser.getDealerId() != null)
			{
				infoMap.put("curDealerId", logonUser.getDealerId());
			}
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			Integer pageSize = request.getParamValue("pageSize") != null ? Integer.parseInt(request.getParamValue("pageSize")) : Constant.PAGE_SIZE; // 处理当前页
			if("2".equals(common)){//导出 调用
				List<Map<String, Object>> mapList = dao.getAllOrderList(infoMap, curPage, Constant.PAGE_SIZE_MAX).getRecords();
				String [] head={"单据类型","订单号","订单状态","经销商代码","经销商名称","提报日期","产地","资金类型","开票类型","发票号","发票版本号","开票状态","车系","车型编码","配置编码","配置名称","综合折扣率%","标准价","审核价","提报数量","基本折后总价","返利使用总额","订单应付金额"};
				String [] cols={"ORDER_TYPE_NAME","ORDER_NO","ORDER_STATUS_NAME","DEALER_CODE","DEALER_NAME","RAISE_DATE","AREA_NAME","FUND_TYPE_NAME","INVO_TYPE_NAME","INVOICE_NO","INVOICE_VER","INVOICE_NO_NAME","SERIES_NAME","MODEL_NAME","PACKAGE_CODE","PACKAGE_NAME","ZHDIS_RATE","SINGLE_PRICE","CHK_PRICE","CHK_NUM","DIS_AMOUNT","REBATE_PRICE","ORDER_YF_PRICE"};
				ToExcel.toReportExcel(act.getResponse(),request, "提车单财务查询信息.xls",head,cols,mapList);
			}else{
				PageResult<Map<String, Object>> ps = dao.getAllOrderList(infoMap, curPage, pageSize);
				act.setOutData("ps", ps);
			}
			
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "财务开票查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 方法描述 ： 中转出库单查询<br/>
	 * 
	 * @author wangsongwei
	 */
	public void orderPlanInfoQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

		try
		{
			String orderNo = CommonUtils.checkNull(request.getParamValue("orderNo")); // 订单编号
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId")); // 区域ID
			String orgCode = CommonUtils.checkNull(request.getParamValue("orgCode")); // 区域ID
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")); // 经销商代码
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId")); // 经销商代码
			String startdate = CommonUtils.checkNull(request.getParamValue("startdate")); // 提报日期
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate")); // 提报日期
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode")); // 物料组编号
			String orderType = CommonUtils.checkNull(request.getParamValue("orderType")); // 订单类型
			String reqStatus = CommonUtils.checkNull(request.getParamValue("reqStatus")); // 订单状态
			String expstatus = CommonUtils.checkNull(request.getParamValue("expstatus")); // 订单状态
			String reqOrderType = CommonUtils.checkNull(request.getParamValue("reqOrderType"));
			String expOrderType = CommonUtils.checkNull(request.getParamValue("expOrderType"));
			String orderDjType = CommonUtils.checkNull(request.getParamValue("orderDjType"));

			Map<String, Object> infoMap = new HashMap<String, Object>();

			infoMap.put("orderNo", orderNo);
			infoMap.put("orgId", orgId);
			infoMap.put("orgCode", orgCode);
			infoMap.put("dealerCode", dealerCode);
			infoMap.put("dealerId", dealerId);
			infoMap.put("endDate", endDate);
			infoMap.put("startdate", startdate);
			infoMap.put("groupCode", groupCode);
			infoMap.put("orderType", orderType);
			infoMap.put("reqStatus", reqStatus);
			infoMap.put("expstatus", expstatus);
			infoMap.put("reqOrderType", reqOrderType);
			infoMap.put("expOrderType", expOrderType);
			infoMap.put("poseId", logonUser.getPoseId());
			infoMap.put("orderDjType", orderDjType);
			infoMap.put("logonUser", logonUser);

			if (logonUser.getDealerId() != null)
			{
				infoMap.put("curDealerId", logonUser.getDealerId());
			}

			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			Integer pageSize = request.getParamValue("pageSize") != null ? Integer.parseInt(request.getParamValue("pageSize")) : Constant.PAGE_SIZE; // 处理当前页

			PageResult<Map<String, Object>> ps = dao.getAllZhongZhuanOrderList(infoMap, curPage, pageSize);
			act.setOutData("ps", ps);
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "计划中转出库查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 方法描述 ： 中转出库单车辆明细跟踪查询<br/>
	 * 
	 * @author wangsongwei
	 */
	public void orderZhongZhuanQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

		try
		{
			String orderNo = CommonUtils.checkNull(request.getParamValue("orderNo")); // 订单编号
			String orgCode = CommonUtils.checkNull(request.getParamValue("orgCode")); // 区域ID
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")); // 经销商代码
			String startdate = CommonUtils.checkNull(request.getParamValue("startdate")); // 提报日期
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate")); // 提报日期
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode")); // 物料组编号
			String vin = CommonUtils.checkNull(request.getParamValue("vin")); // vin
			String vstatus = CommonUtils.checkNull(request.getParamValue("vstatus")); // 车辆状态
			String paystatus = CommonUtils.checkNull(request.getParamValue("paystatus")); // 还款状态

			Map<String, Object> infoMap = new HashMap<String, Object>();

			infoMap.put("orderNo", orderNo);
			infoMap.put("orgCode", orgCode);
			infoMap.put("paystatus", paystatus);
			infoMap.put("dealerCode", dealerCode);
			infoMap.put("endDate", endDate);
			infoMap.put("startdate", startdate);
			infoMap.put("groupCode", groupCode);
			infoMap.put("vin", vin);
			infoMap.put("vstatus", vstatus);
			infoMap.put("paystatus", paystatus);
			infoMap.put("logonUser", logonUser);

			if (logonUser.getDealerId() != null)
			{
				infoMap.put("curDealerId", logonUser.getDealerId());
			}

			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			Integer pageSize = request.getParamValue("pageSize") != null ? Integer.parseInt(request.getParamValue("pageSize")) : Constant.PAGE_SIZE; // 处理当前页

			PageResult<Map<String, Object>> ps = dao.getZhongZhuanVechicleList(infoMap, curPage, pageSize);
			act.setOutData("ps", ps);
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "计划中转出库查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 方法描述 ： 财务开票查询<br/>
	 * 
	 * @author wangsongwei
	 */
	public void orderResInfoQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

		try
		{
			String orderNo = CommonUtils.checkNull(request.getParamValue("orderNo")); // 订单编号
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId")); // 区域ID
			String orgCode = CommonUtils.checkNull(request.getParamValue("orgCode")); // 区域ID
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")); // 经销商代码
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId")); // 经销商代码
			String startdate = CommonUtils.checkNull(request.getParamValue("startdate")); // 提报日期
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate")); // 提报日期
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode")); // 物料组编号
			String orderType = CommonUtils.checkNull(request.getParamValue("orderType")); // 订单类型
			String reqStatus = CommonUtils.checkNull(request.getParamValue("reqStatus")); // 订单状态
			String expstatus = CommonUtils.checkNull(request.getParamValue("expstatus")); // 订单状态

			Map<String, Object> infoMap = new HashMap<String, Object>();

			infoMap.put("orderNo", orderNo);
			infoMap.put("orgId", orgId);
			infoMap.put("orgCode", orgCode);
			infoMap.put("dealerCode", dealerCode);
			infoMap.put("dealerId", dealerId);
			infoMap.put("endDate", endDate);
			infoMap.put("startdate", startdate);
			infoMap.put("groupCode", groupCode);
			infoMap.put("orderType", orderType);
			infoMap.put("reqStatus", reqStatus);
			infoMap.put("expstatus", expstatus);
			infoMap.put("poseId", logonUser.getPoseId());
			infoMap.put("logonUser", logonUser);

			if (logonUser.getDealerId() != null)
			{
				infoMap.put("curDealerId", logonUser.getDealerId());
			}

			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			Integer pageSize = request.getParamValue("pageSize") != null ? Integer.parseInt(request.getParamValue("pageSize")) : Constant.PAGE_SIZE; // 处理当前页

			PageResult<Map<String, Object>> ps = dao.getAllResOrderList(infoMap, curPage, pageSize);
			act.setOutData("ps", ps);
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "财务开票查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 方法描述 ： 财务承诺占款查询<br/>
	 * 
	 * @author wangsongwei
	 */
	public void orderFinancialPromiseQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

		try
		{
			String orderNo = CommonUtils.checkNull(request.getParamValue("orderNo")); // 订单编号
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId")); // 区域ID
			String orgCode = CommonUtils.checkNull(request.getParamValue("orgCode")); // 区域ID
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")); // 经销商代码
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId")); // 经销商代码
			String startdate = CommonUtils.checkNull(request.getParamValue("startdate")); // 提报日期
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate")); // 提报日期
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode")); // 物料组编号
			String orderType = CommonUtils.checkNull(request.getParamValue("orderType")); // 订单类型
			String reqStatus = CommonUtils.checkNull(request.getParamValue("reqStatus")); // 订单状态
			String expstatus = CommonUtils.checkNull(request.getParamValue("expstatus")); // 订单状态
			String payStatus = CommonUtils.checkNull(request.getParamValue("payStatus")); // 还款状态

			Map<String, Object> infoMap = new HashMap<String, Object>();

			infoMap.put("orderNo", orderNo);
			infoMap.put("orgId", orgId);
			infoMap.put("orgCode", orgCode);
			infoMap.put("dealerCode", dealerCode);
			infoMap.put("dealerId", dealerId);
			infoMap.put("endDate", endDate);
			infoMap.put("startdate", startdate);
			infoMap.put("groupCode", groupCode);
			infoMap.put("orderType", orderType);
			infoMap.put("reqStatus", reqStatus);
			infoMap.put("expstatus", expstatus);
			infoMap.put("payStatus", payStatus);
			infoMap.put("poseId", logonUser.getPoseId());
			infoMap.put("logonUser", logonUser);

			if (logonUser.getDealerId() != null)
			{
				infoMap.put("curDealerId", logonUser.getDealerId());
			}

			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页

			PageResult<Map<String, Object>> ps = dao.getFinancialPromiseQueryList(infoMap, logonUser, curPage, Constant.PAGE_SIZE);

			act.setOutData("ps", ps);
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "财务开票查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 方法描述 ： 财务承诺占款查询<br/>
	 * 
	 * @author wangsongwei
	 */
	public void orderFinancialPromiseExport() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

		try
		{
			String orderNo = CommonUtils.checkNull(request.getParamValue("orderNo")); // 订单编号
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId")); // 区域ID
			String orgCode = CommonUtils.checkNull(request.getParamValue("orgCode")); // 区域ID
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")); // 经销商代码
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId")); // 经销商代码
			String startdate = CommonUtils.checkNull(request.getParamValue("startdate")); // 提报日期
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate")); // 提报日期
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode")); // 物料组编号
			String orderType = CommonUtils.checkNull(request.getParamValue("orderType")); // 订单类型
			String reqStatus = CommonUtils.checkNull(request.getParamValue("reqStatus")); // 订单状态
			String expstatus = CommonUtils.checkNull(request.getParamValue("expstatus")); // 订单状态
			String payStatus = CommonUtils.checkNull(request.getParamValue("payStatus")); // 还款状态

			Map<String, Object> infoMap = new HashMap<String, Object>();

			infoMap.put("orderNo", orderNo);
			infoMap.put("orgId", orgId);
			infoMap.put("orgCode", orgCode);
			infoMap.put("dealerCode", dealerCode);
			infoMap.put("dealerId", dealerId);
			infoMap.put("endDate", endDate);
			infoMap.put("startdate", startdate);
			infoMap.put("groupCode", groupCode);
			infoMap.put("orderType", orderType);
			infoMap.put("reqStatus", reqStatus);
			infoMap.put("expstatus", expstatus);
			infoMap.put("payStatus", payStatus);
			infoMap.put("poseId", logonUser.getPoseId());
			infoMap.put("logonUser", logonUser);

			if (logonUser.getDealerId() != null)
			{
				infoMap.put("curDealerId", logonUser.getDealerId());
			}

			PageResult<Map<String, Object>> ps = dao.getFinancialPromiseQueryList(infoMap, logonUser, 1, 99999);

			List<Map<String, Object>> recordList = ps.getRecords();

			// 用于下载传参的集合
			List<List<Object>> list = new LinkedList<List<Object>>();

			// 标题
			List<Object> listHead = new LinkedList<Object>();

			listHead.add("单据类型");
			listHead.add("订单号");
			listHead.add("经销商");
			listHead.add("车系");
			listHead.add("车型");
			listHead.add("配置");
			listHead.add("使用数量");
			listHead.add("使用金额");
			listHead.add("还款状态");
			listHead.add("综合折扣率%");
			listHead.add("承诺还款日期");
			listHead.add("使用时间");
			listHead.add("承诺还款资金类型");
			listHead.add("承诺总数量");
			listHead.add("已使用数量");
			listHead.add("开票时间");
			listHead.add("开票号");

			list.add(listHead);

			if (!CommonUtils.isNullList(recordList))
			{
				for (Map<String, Object> dataMap : recordList)
				{
					List<Object> listValue = new LinkedList<Object>();

					listValue.add(CodeDict.getDictDescById(Constant.ORDER_INVOICE_TYPE.toString(), CommonUtils.checkNull(dataMap.get("ORDER_DJ_TYPE"))));
					listValue.add(CommonUtils.checkNull(dataMap.get("ORDER_NO")));
					listValue.add(CommonUtils.checkNull(dataMap.get("DEALER_NAME")));
					listValue.add(CommonUtils.checkNull(dataMap.get("SERIES_NAME")));
					listValue.add(CommonUtils.checkNull(dataMap.get("MODEL_NAME")));
					listValue.add(CommonUtils.checkNull(dataMap.get("PACKAGE_NAME")));
					listValue.add(CommonUtils.checkNull(dataMap.get("USE_NUM")));
					listValue.add(CommonUtils.checkNull(dataMap.get("USE_AMOUNT")));
					listValue.add(CodeDict.getDictDescById(Constant.PROMISE_PAY_STATUS.toString(), CommonUtils.checkNull(dataMap.get("STATUS"))));
					listValue.add(CommonUtils.checkNull(dataMap.get("ZHDIS_RATE_CHECK")));
					listValue.add(CommonUtils.checkNull(dataMap.get("PRO_DATE")));
					listValue.add(CommonUtils.checkNull(dataMap.get("USE_DATE")));
					listValue.add(CodeDict.getDictDescById(Constant.ACCOUNT_TYPE.toString(), CommonUtils.checkNull(dataMap.get("PRO_TYPE"))));
					listValue.add(CommonUtils.checkNull(dataMap.get("APPLY_NUM")));
					listValue.add(CommonUtils.checkNull(dataMap.get("USE_NUM_ALL")));
					listValue.add(CommonUtils.checkNull(dataMap.get("INVO_DATE")));
					listValue.add(CommonUtils.checkNull(dataMap.get("INVOICE_NO")));

					list.add(listValue);
				}
			}
			OutputStream os = null;
			// 导出的文件名
			String fileName = "承诺占款.xls";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/xls");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			os = response.getOutputStream();
			dao.createXlsFile(list, os, "承诺占款");
			os.flush();
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "财务开票查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 方法描述 : 订单查询<br/>
	 * 
	 * @author wangsongwei
	 */
	public void orderInfoQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

		try
		{
			String orderNo = CommonUtils.checkNull(request.getParamValue("orderNo")); // 订单编号
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId")); // 区域ID
			String orgCode = CommonUtils.checkNull(request.getParamValue("orgCode")); // 区域ID
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")); // 经销商代码
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId")); // 经销商代码
			String startdate = CommonUtils.checkNull(request.getParamValue("startdate")); // 提报日期
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate")); // 提报日期
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode")); // 物料组编号
			String orderType = CommonUtils.checkNull(request.getParamValue("orderType")); // 订单类型
			String reqStatus = CommonUtils.checkNull(request.getParamValue("reqStatus")); // 订单状态
			String expstatus = CommonUtils.checkNull(request.getParamValue("expstatus")); // 订单状态

			Map<String, Object> infoMap = new HashMap<String, Object>();

			infoMap.put("orderNo", orderNo);
			infoMap.put("orgId", orgId);
			infoMap.put("orgCode", orgCode);
			infoMap.put("dealerCode", dealerCode);
			infoMap.put("dealerId", dealerId);
			infoMap.put("endDate", endDate);
			infoMap.put("startdate", startdate);
			infoMap.put("groupCode", groupCode);
			infoMap.put("orderType", orderType);
			infoMap.put("reqStatus", reqStatus);
			infoMap.put("expstatus", expstatus);
			infoMap.put("poseId", logonUser.getPoseId());

			if (logonUser.getDealerId() != null)
			{
				infoMap.put("curDealerId", logonUser.getDealerId());
			}

			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			Integer pageSize = request.getParamValue("pageSize") != null ? Integer.parseInt(request.getParamValue("pageSize")) : Constant.PAGE_SIZE; // 处理当前页

			PageResult<Map<String, Object>> ps = dao.getOrderList(infoMap, curPage, pageSize);
			act.setOutData("ps", ps);

		}
		catch (Exception e)
		{//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "提车单提报DLR");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 方法描述 ： 查询经销商物料价格 - 车厂端和经销商端共用<br/>
	 * 
	 * @author wangsongwei
	 */
	public void queryDealerPrice() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

		try
		{
			String materialCode = CommonUtils.checkNull(request.getParamValue("materialCode")); // 物料编码
			String materialGroup = CommonUtils.checkNull(request.getParamValue("groupCode")); //物料组编码

			Map<String, Object> infoMap = new HashMap<String, Object>();

			infoMap.put("materialCode", materialCode);
			infoMap.put("materialGroup", materialGroup);

			String dealerId = logonUser.getDealerId() == null ? "" : logonUser.getDealerId();
			infoMap.put("dealerId", dealerId);

			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页

			PageResult<Map<String, Object>> ps = dao.getMaterialPriceList(infoMap, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);

		}
		catch (Exception e)
		{//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "提车单提报DLR");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 方法描述 ： 查询提车单发运信息 <br/>
	 * 
	 * @author wangsongwei
	 */
	public void queryDespatch() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

		try
		{
			String materialGroup = CommonUtils.checkNull(request.getParamValue("groupCode")); //物料组编码
			String startdate = CommonUtils.checkNull(request.getParamValue("startdate"));
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));
			String orderNo = CommonUtils.checkNull(request.getParamValue("orderNo"));
			String dealerOrderNo = CommonUtils.checkNull(request.getParamValue("dealerOrderNo"));
			String deliveryType = CommonUtils.checkNull(request.getParamValue("deliveryType"));
			String deliveryStatus = CommonUtils.checkNull(request.getParamValue("deliveryStatus"));
			String orderType = CommonUtils.checkNull(request.getParamValue("orderType"));
			String orderStatus = CommonUtils.checkNull(request.getParamValue("orderStatus"));
			String materialCode = CommonUtils.checkNull(request.getParamValue("materialCode"));
			String expStatusCode = CommonUtils.checkNull(request.getParamValue("expStatusCode"));
			String dealerName = CommonUtils.checkNull(request.getParamValue("dealerN"));
			String accountType = CommonUtils.checkNull(request.getParamValue("accountType"));
			
			Map<String, Object> infoMap = new HashMap<String, Object>();

			infoMap.put("groupCode", materialGroup);
			infoMap.put("accountType", accountType);
			infoMap.put("startdate", startdate);
			infoMap.put("orderType", orderType);
			infoMap.put("endDate", endDate);
			infoMap.put("orderNo", orderNo);
			infoMap.put("dealerOrderNo", dealerOrderNo);
			infoMap.put("deliveryType", deliveryType);
			infoMap.put("deliveryStatus", deliveryStatus);
			infoMap.put("materialCode", materialCode);
			infoMap.put("orderStatus", orderStatus);
			infoMap.put("expStatusCode", expStatusCode);
			infoMap.put("dealerName", dealerName);
			String dealerId = logonUser.getDealerId() == null ? "" : logonUser.getDealerId();
			infoMap.put("dealerId", dealerId);

			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页

			PageResult<Map<String, Object>> ps = dao.getOrderDespatchList(infoMap, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);

		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "提车单提报DLR");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void dowLoadQueryDespatch(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

		try
		{
			String materialGroup = CommonUtils.checkNull(request.getParamValue("groupCode")); //物料组编码
			String startdate = CommonUtils.checkNull(request.getParamValue("startdate"));
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));
			String orderNo = CommonUtils.checkNull(request.getParamValue("orderNo"));
			String dealerOrderNo = CommonUtils.checkNull(request.getParamValue("dealerOrderNo"));
			String deliveryType = CommonUtils.checkNull(request.getParamValue("deliveryType"));
			String deliveryStatus = CommonUtils.checkNull(request.getParamValue("deliveryStatus"));
			String orderType = CommonUtils.checkNull(request.getParamValue("orderType"));
			String orderStatus = CommonUtils.checkNull(request.getParamValue("orderStatus"));
			String materialCode = CommonUtils.checkNull(request.getParamValue("materialCode"));
			String expStatusCode = CommonUtils.checkNull(request.getParamValue("expStatusCode"));
			String dealerName = CommonUtils.checkNull(request.getParamValue("dealerN"));
			String accountType = CommonUtils.checkNull(request.getParamValue("accountType"));
			
			Map<String, Object> infoMap = new HashMap<String, Object>();

			infoMap.put("groupCode", materialGroup);
			infoMap.put("accountType", accountType);
			infoMap.put("startdate", startdate);
			infoMap.put("orderType", orderType);
			infoMap.put("endDate", endDate);
			infoMap.put("orderNo", orderNo);
			infoMap.put("dealerOrderNo", dealerOrderNo);
			infoMap.put("deliveryType", deliveryType);
			infoMap.put("deliveryStatus", deliveryStatus);
			infoMap.put("materialCode", materialCode);
			infoMap.put("orderStatus", orderStatus);
			infoMap.put("expStatusCode", expStatusCode);
			infoMap.put("dealerName", dealerName);
			String dealerId = logonUser.getDealerId() == null ? "" : logonUser.getDealerId();
			infoMap.put("dealerId", dealerId);
			List<Map<String, Object>> psList = dao.getOrderDespatchListDetail(infoMap);
			String[] head = {"销售清单号","销售商订单号","经销商代码","经销商名称","提报时间","资金类型" ,"车系","车型","配置","颜色","数量","启票单价","启票金额"};
			String[] columns={"ORDER_NO_S","ORDER_NO","DEALER_CODE","DEALER_NAME","SUB_DATE","FUND_TYPE_ID","SERIES_NAME",
					"MODEL_NAME","PACKAGE_NAME","COLOR_NAME","CHECK_AMOUNT","DISCOUNT_S_PRICE","TOTAL_PRICE"};
			try {
				ToExcel.toReportExcel(ActionContext.getContext().getResponse(), request,"销售清单明细.xls", head,columns,psList);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "销售清单数据下载");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 方法描述 ： 页面跳转到订单详细信息的查看页面<br/>
	 * 
	 * @author wangsongwei
	 */
	public void showOrderReport() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

		try
		{
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId")); // 订单ID号
			act.setOutData("orderId", orderId);

			// 查询订单基本信息返回到页面
			Map<String, Object> map = dao.getOrderSubmissionInfo(orderId);
			act.setOutData("orderMap", map);

			// 返回订单的物料信息
			List<Map<String, Object>> materialList = dao.getOrderMaterialDetailList(orderId);
			act.setOutData("materialList", materialList);

			// 审核结果返回
			List<?> resultList = dao.getOrderCheckResult(orderId);
			act.setOutData("resultList", resultList);
			
			List<Map<String, Object>> orderRebList = dao.queryOrderRebByOrderId(orderId);
			act.setOutData("orderRebList", orderRebList);

			act.setForword(ORDER_DETAIL_PAGE);

		}
		catch (Exception e)
		{//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "提车单提报DLR");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 方法描述 ： 页面跳转到订单详细信息的查看页面<br/>
	 * 
	 * @author wangsongwei
	 */
	public void viewOrderReport() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

		try
		{
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId")); // 订单ID号

			act.setOutData("orderId", orderId);
			act.setOutData("curdealerId", logonUser.getDealerId()); // 当前经销商

			if (!"".equals(orderId))
			{
				// 订单信息
				Map<String, Object> orderMap = dao.getOrderSubmissionInfo(orderId);
				act.setOutData("orderObj", orderMap);

				// 返利信息
				List<Map<String, Object>> rebateList = dao.getOrderRebateList(orderId);
				act.setOutData("rebateList", rebateList);

				// 审核结果返回
				List<?> resultList = dao.getOrderCheckResult(orderId);
				act.setOutData("resultList", resultList);

//				// 订做车信息
//				if (orderMap.get("ORDER_TYPE").toString().equals(Constant.ORDER_TYPE_02.toString())
//						|| orderMap.get("ORDER_TYPE").toString().equals(Constant.ORDER_TYPE_03.toString()))
//				{
//					String createId = "";
//
//					String tcop_sql = "SELECT C.CUS_ORDER_ID FROM TT_VS_ORDER A, TT_VS_ORDER_DETAIL B, TM_CUS_ORDER C, TM_CUS_ORDER_DETAIL D"
//							+ "WHERE A.ORDER_ID = B.ORDER_ID AND B.CUS_DETAIL_ID = D.DETAIL_ID AND D.CUS_ORDER_ID = C.CUS_ORDER_ID " + "AND A.ORDER_ID = " + orderId
//							+ "GROUP BY C.CUS_ORDER_ID";
//
//					Map<String, Object> createMap = dao.pageQueryMap(tcop_sql, null, dao.getFunName());
//
//					if (createMap != null)
//					{
//						createId = createMap.get("CUS_ORDER_ID").toString();
//						act.setOutData("createId", createId);
//					}
//					else
//					{
//						throw new RuntimeException("无效的生产订单ID");
//					}
//
//					// 获取生产订单的详细物料明细信息
//					Map<String, Object> map = dao.getProductOrderInfo(createId);
//
//					String cusType = map.get("CUS_TYPE").toString();
//
//					if (cusType.equals(Constant.ORDER_REPORT_TYPE_01.toString()))
//					{
//						act.setOutData("preNomal", cusType);
//					}
//					else if (cusType.equals(Constant.ORDER_REPORT_TYPE_02.toString()))
//					{
//						act.setOutData("preCompany", cusType);
//					}
//
//					act.setOutData("createObj", map);
//
//					List<Map<String, Object>> materialList = dao.getProductOrderMaterailList(createId, orderId);
//					act.setOutData("materialList", materialList);
//				}
//				else
//				{
					// 物料信息
					List<Map<String, Object>> materialList = dao.getOrderMaterialDetailList(orderId);
					act.setOutData("materialList", materialList);
//				}
			}

			/* 加载经销商产地信息列表下拉 */
			List<Map<String, Object>> list = MaterialGroupManagerDao.getDealerBusinessArea(logonUser.getDealerId().toString());
			act.setOutData("yieldList", list);

			/* 加载经销商下级经销商列表下拉 */
			List<Map<String, Object>> delearList = ajaxDao.getDelearList(logonUser.getDealerId().toString());
			act.setOutData("delearList", delearList);

			act.setForword(ORDER_DETAIL_PAGE_ALL);

		}
		catch (Exception e)
		{//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "提车单提报DLR");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 资源站点数据查询
	 */
	public void loadResourceSharedView() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

		try
		{
			List<Map<String, Object>> orgList = ajaxDao.getOrgList(3, Constant.ORG_TYPE_OEM);
			act.setOutData("orglist", orgList);

			List<Map<String, Object>> serisList = ajaxDao.getSerisList(logonUser.getPoseId().toString(), 2);
			act.setOutData("serislist", serisList);

			act.setForword(RESOURCE_DETAIL);
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "资源站点数据查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 资源站点数据查询
	 */
	public void resourceSharedViewQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

		try
		{
			Map<String, Object> dataMap = new HashMap<String, Object>();

			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			//String orgCode = CommonUtils.checkNull(request.getParamValue("orgCode"));
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));

			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页

			dataMap.put("dealerCode", dealerCode);
			//dataMap.put("orgCode", orgCode);
			dataMap.put("groupCode", groupCode);

			PageResult<Map<String, Object>> ps = dao.queryResourceShared(dataMap, logonUser, curPage, 25);

			act.setOutData("ps", ps);
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "资源站点数据查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void resourceSharedViewQueryExport() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			Map<String, Object> dataMap = new HashMap<String, Object>();

			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			//String orgCode = CommonUtils.checkNull(request.getParamValue("orgCode"));
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));
			dataMap.put("dealerCode", dealerCode);
			//dataMap.put("orgCode", orgCode);
			dataMap.put("groupCode", groupCode);

			PageResult<Map<String, Object>> ps = dao.queryResourceShared(dataMap, logonUser, 1, Constant.PAGE_SIZE_MAX);
			String [] head={"经销商","销售清单号","车系","车型","配置","颜色","整车物料码","分配数量","订单状态"};
			String [] cols={"DEALER_SHORTNAME","ORDER_NO","SERIES_NAME","MODEL_NAME","PACKAGE_NAME","COLOR_NAME","MATERIAL_CODE","AMOUNT","ORDER_STATUS_DESC"};
			ToExcel.toReportExcel(act.getResponse(),request, "资源分配明细.xls",head,cols,ps.getRecords());
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "资源站点数据查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
