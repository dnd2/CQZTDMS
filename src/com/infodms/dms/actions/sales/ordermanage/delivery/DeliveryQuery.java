/**********************************************************************
* <pre>
* FILE : DeliveryQuery.java
* CLASS : DeliveryQuery
* AUTHOR : 
* FUNCTION : 订单发运
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.|   DATE   |    NAME    | REASON  |  CHANGE REQ.
*----------------------------------------------------------------------
*         |2010-05-26|            | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
package com.infodms.dms.actions.sales.ordermanage.delivery;

import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.sales.ordermanage.delivery.OrderDeliveryDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.OrderReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDateSetPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.DateTimeUtil;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * @Title: 
 * @Description:InfoFrame3.0.V01
 * @Copyright: Copyright (c) 2010
 * @Company: www.infoservice.com.cn
 * @Date: 2010-5-26
 * @author  
 * @mail   	
 * @version 1.0
 * @remark 订单发运查询
 */
public class DeliveryQuery {
	
	public Logger logger = Logger.getLogger(DeliveryApply.class);   
	OrderDeliveryDao dao  = OrderDeliveryDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	ResponseWrapper response = act.getResponse();
	private final OrderReportDao reportDao = OrderReportDao.getInstance();
	private final String initUrl = "/jsp/sales/ordermanage/delivery/deliveryQuery.jsp";
	
	/**
	 * 订单发运查询页面初始化
	 */
	public void orderDeliveryQueryInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			// 查看日期配置表中当天的记录
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			List<Map<String, Object>> areaBusList=MaterialGroupManagerDao.getDealerBusiness(logonUser.getPoseId().toString());
			TmDateSetPO dateSet = reportDao.getTmDateSetPO(new java.util.Date(), oemCompanyId);
			String dateStr = DateTimeUtil.parseDateToDate(new Date());
			List yearList = dao.getDateYearList();
			List weekList = dao.getDateWeekList();
			act.setOutData("yearList", yearList);
			act.setOutData("weekList", weekList);
			act.setOutData("areaBusList", areaBusList);
			act.setOutData("year", dateSet != null ? dateSet.getSetYear() : "");
			act.setOutData("week", dateSet != null ? dateSet.getSetWeek() : "");
			act.setOutData("dateStr", dateStr);
			act.setForword(initUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"订单发运查询初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 订单发运查询
	 */
	public void orderDeliveryQuery(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		String dealerId = logonUser.getDealerId().toString();
		try {
			String startYear =request.getParamValue("startYear");			//订单起始年
			String endYear =request.getParamValue("endYear");				//订单结束年
			String startWeek = request.getParamValue("startWeek");			//订单起始周
			String endWeek = request.getParamValue("endWeek");				//订单结束周
			String startDate = request.getParamValue("startDate");			//发运起始时间
			String endDate = request.getParamValue("endDate");				//发运结束时间
			String orderType = request.getParamValue("orderType");			//订单类型
			String orderNo = request.getParamValue("orderNo");				//订单号码
			String transportType = request.getParamValue("transportType");	//运送方式
			String deliveryStatus = request.getParamValue("deliveryStatus");//发运状态
			String groupCode = request.getParamValue("groupCode");			//物料组CODE
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			StringBuffer areaIds = new StringBuffer();
			List<Map<String, Object>> areaBusList=MaterialGroupManagerDao.getDealerBusiness(logonUser.getPoseId().toString());
			for (int i = 0; i < areaBusList.size(); i++) {
				if (!"".equals(String.valueOf(areaBusList.get(i).get("AREA_ID")))) {
					areaIds.append(String.valueOf(areaBusList.get(i).get("AREA_ID")));
				}
				if (i<areaBusList.size()-1) {
					areaIds.append(",");
				}
			}
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getOrderDeliveryQueryList(areaId,startYear, startWeek, endYear, endWeek, startDate, endDate, orderType, dealerId, orderNo, transportType, deliveryStatus, groupCode, areaIds.toString(),oemCompanyId,curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"订单发运查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 订单发运下载
	 */
	public void orderDeliveryLoad(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		String dealerId = logonUser.getDealerId().toString();
		OutputStream os = null;
		try {
			String startYear =request.getParamValue("startYear");			//订单起始年
			String endYear =request.getParamValue("endYear");				//订单结束年
			String startWeek = request.getParamValue("startWeek");			//订单起始周
			String endWeek = request.getParamValue("endWeek");				//订单结束周
			String startDate = request.getParamValue("startDate");			//发运起始时间
			String endDate = request.getParamValue("endDate");				//发运结束时间
			String orderType = request.getParamValue("orderType");			//订单类型
			String orderNo = request.getParamValue("orderNo");				//订单号码
			String transportType = request.getParamValue("transportType");	//运送方式
			String deliveryStatus = request.getParamValue("deliveryStatus");//发运状态
			String groupCode = request.getParamValue("groupCode");			//物料组CODE
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			StringBuffer areaIds = new StringBuffer();
			List<Map<String, Object>> areaBusList=MaterialGroupManagerDao.getDealerBusiness(logonUser.getPoseId().toString());
			for (int i = 0; i < areaBusList.size(); i++) {
				if (!"".equals(String.valueOf(areaBusList.get(i).get("AREA_ID")))) {
					areaIds.append(String.valueOf(areaBusList.get(i).get("AREA_ID")));
				}
				if (i<areaBusList.size()-1) {
					areaIds.append(",");
				}
			}
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			Map<String, Object> map = new HashMap<String, Object>();
			// 导出的文件名
			String fileName = "发运订单.csv";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);

			List<List<Object>> list = new LinkedList<List<Object>>();

			List<Object> listTemp = new LinkedList<Object>();
			listTemp.add("组织代码");
			listTemp.add("组织名称");
			listTemp.add("订单类型");
			listTemp.add("订单号码");
			listTemp.add("订单周度");
			listTemp.add("发运单号");
			listTemp.add("定坐车批次号");
			listTemp.add("计划数量");
			listTemp.add("发运数量");
			listTemp.add("总价");
			listTemp.add("运送方式");
			listTemp.add("运送地址");
			listTemp.add("状态");
			listTemp.add("发运时间");
			list.add(listTemp);

			List<Map<String, Object>> results = dao.getOrderDeliveryQueryListLoad(areaId,startYear, startWeek, endYear, endWeek, startDate, endDate, orderType, dealerId, orderNo, transportType, deliveryStatus, groupCode, areaIds.toString(),oemCompanyId,curPage, Constant.PAGE_SIZE);
			for (int i = 0; i < results.size(); i++) {
				Map<String, Object> record = results.get(i);
				listTemp = new LinkedList<Object>();
				listTemp.add(CommonUtils.checkNull(record.get("CODE")));
				listTemp.add(CommonUtils.checkNull(record.get("TTNAME")));
				listTemp.add(CommonUtils.checkNull(record.get("ORDER_TYPE")));
				listTemp.add(CommonUtils.checkNull(record.get("ORDER_NO")));
				listTemp.add(CommonUtils.checkNull(record.get("ORDER_WEEK")));
				listTemp.add(CommonUtils.checkNull(record.get("DELIVERY_NO")));
				listTemp.add(CommonUtils.checkNull(record.get("SPECIAL_BATCH_NO")));
				listTemp.add(CommonUtils.checkNull(record.get("DELIVERY_AMOUNT")));
				listTemp.add(CommonUtils.checkNull(record.get("MATCH_AMOUNT")));
				listTemp.add(CommonUtils.checkNull(record.get("TOTAL_PRICE")));
				listTemp.add(CommonUtils.checkNull(record.get("DELIVERY_TYPE")));
				listTemp.add(CommonUtils.checkNull(record.get("ADDRESS")));
				listTemp.add(CommonUtils.checkNull(record.get("DELIVERY_STATUS")));
				listTemp.add(CommonUtils.checkNull(record.get("DELIVERY_DATE")));
				list.add(listTemp);
			}
			os = response.getOutputStream();
			CsvWriterUtil.writeCsv(list, os);
			os.flush();
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"订单发运查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
}
