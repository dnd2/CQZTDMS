/**
 * @Title: OemSalesOrderQuery.java
 * 
 * @Description:
 * 
 * @Copyright: Copyright (c) 2010
 * 
 * @Company: www.infoservice.com.cn
 * @Date: 2010-6-8
 * 
 * @author yuyong
 * @mail yuyong@infoservice.com.cn
 * @version 1.0
 * @remark
 */
package com.infodms.dms.actions.sales.ordermanage.orderquery;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.dao.orgmng.GetOrgIdsOrDealerIdsDAO;
import com.infodms.dms.dao.sales.ordermanage.delivery.OrderDeliveryDao;
import com.infodms.dms.dao.sales.ordermanage.orderquery.OrderQueryDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.OrderReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDateSetPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * @author yuyong
 * 
 */
public class OemSalesOrderQuery {
	private Logger logger = Logger.getLogger(OemSalesOrderQuery.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	ResponseWrapper response = act.getResponse();
	private final OrderQueryDao dao = OrderQueryDao.getInstance();
	private final OrderReportDao reportDao = OrderReportDao.getInstance();

	private final String OEM_SALES_ORDER_QUERY_URL = "/jsp/sales/ordermanage/orderquery/oemSalesOrderQueryPre.jsp";// 销售订单查询页面
	private final String delUrl = "/jsp/sales/ordermanage/orderquery/deliveryOrderQuery.jsp";// 发运订单查询

	public void oemSalesOrderQueryPre() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Long poseId = logonUser.getPoseId();
			String dutyType = logonUser.getDutyType();
			String orgId = logonUser.getOrgId().toString();
			String parentOrgId = logonUser.getParentOrgId();
			List<String> years = dao.getYearList();
			List<String> weeks = dao.getWeekList();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(poseId.toString());

			// 查看日期配置表中当天的记录
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			TmDateSetPO dateSet = reportDao.getTmDateSetPO(new Date(), oemCompanyId);

			Integer nowSys = CommonUtils.getNowSys(oemCompanyId);

			String para = CommonDAO.getPara(Constant.CHANA_SYS.toString());
			if (Constant.COMPANY_CODE_JC.equals(para.toUpperCase())) {
				act.setOutData("paraSys", Constant.COMPANY_CODE_JC);
			} else if (Constant.COMPANY_CODE_CVS.equals(para.toUpperCase())) {
				act.setOutData("paraSys", Constant.COMPANY_CODE_CVS);
			} else {
				throw new RuntimeException("判断当前系统的系统参数错误！");
			}

			act.setOutData("nowSys", nowSys);
			act.setOutData("orgId", dutyType.equals(Constant.DUTY_TYPE_DEPT.toString()) ? parentOrgId : orgId);
			act.setOutData("years", years);
			act.setOutData("weeks", weeks);
			act.setOutData("curYear", dateSet != null ? dateSet.getSetYear() : "");
			act.setOutData("curWeek", dateSet != null ? dateSet.getSetWeek() : "");
			act.setOutData("areaList", areaList);
			act.setOutData("dutyType", logonUser.getDutyType());
			act.setForword(OEM_SALES_ORDER_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "销售订单查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void oemSalesOrderTotalQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String dealerCodes = CommonUtils.checkNull(request.getParamValue("dealerCodes"));
			String orderYear1 = CommonUtils.checkNull(request.getParamValue("orderYear1"));
			String orderWeek1 = CommonUtils.checkNull(request.getParamValue("orderWeek1"));
			String orderYear2 = CommonUtils.checkNull(request.getParamValue("orderYear2"));
			String orderWeek2 = CommonUtils.checkNull(request.getParamValue("orderWeek2"));
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));
			String materialCode = CommonUtils.checkNull(request.getParamValue("materialCode"));
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			String orderType = CommonUtils.checkNull(request.getParamValue("orderType"));
			String orderStatus = CommonUtils.checkNull(request.getParamValue("orderStatus"));
			String orderNo = CommonUtils.checkNull(request.getParamValue("orderNo"));
			String dealerCode2 = CommonUtils.checkNull(request.getParamValue("dealerkaipiaoCode"));
			String orgId = logonUser.getOrgId().toString();
			String parentOrgId = logonUser.getParentOrgId();
			String dutyType = logonUser.getDutyType();
			String dealerSql = GetOrgIdsOrDealerIdsDAO.getDealerIdsForOrder(orgId, parentOrgId, dutyType, "TD1");
			String companyId = logonUser.getCompanyId().toString();
			String dataflag = CommonUtils.checkNull(request.getParamValue("dataflag"));// 周度/起止时间
																						// 而选一
			String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));
			// 业务范围id字符串
			Long poseId = logonUser.getPoseId();
			String areaIds = MaterialGroupManagerDao.getPoseIdBusinessIdStr(poseId.toString());

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("dealerCode2", dealerCode2);
			map.put("orderYear1", orderYear1);
			map.put("orderWeek1", orderWeek1);
			map.put("orderYear2", orderYear2);
			map.put("orderWeek2", orderWeek2);
			map.put("areaId", areaId);
			map.put("groupCode", groupCode);
			map.put("materialCode", materialCode);
			map.put("dealerCode", dealerCode);
			map.put("orderType", orderType);
			map.put("orderStatus", orderStatus);
			map.put("orderNo", orderNo);
			map.put("dealerSql", dealerSql);
			map.put("companyId", companyId);
			map.put("areaIds", areaIds);
			map.put("dataflag", dataflag);
			map.put("beginTime", beginTime);
			map.put("endTime", endTime);
			map.put("dealerCodes", dealerCodes);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getOemSalesOrderTotalQueryList(map, curPage, Constant.PAGE_SIZE_MAX);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "销售订单查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void oemSalesOrderDetailQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String dealerCodes = CommonUtils.checkNull(request.getParamValue("dealerCodes"));
			String orderYear1 = CommonUtils.checkNull(request.getParamValue("orderYear1"));
			String orderWeek1 = CommonUtils.checkNull(request.getParamValue("orderWeek1"));
			String orderYear2 = CommonUtils.checkNull(request.getParamValue("orderYear2"));
			String orderWeek2 = CommonUtils.checkNull(request.getParamValue("orderWeek2"));
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));
			String materialCode = CommonUtils.checkNull(request.getParamValue("materialCode"));
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			String orderType = CommonUtils.checkNull(request.getParamValue("orderType"));
			String orderStatus = CommonUtils.checkNull(request.getParamValue("orderStatus"));
			String orderNo = CommonUtils.checkNull(request.getParamValue("orderNo"));
			String groupCode2 = CommonUtils.checkNull(request.getParamValue("dealerkaipiaoCode"));

			String orgId = logonUser.getOrgId().toString();

			String pageSize = CommonUtils.checkNull(request.getParamValue("pageSize"));

			String orgIdN = request.getParamValue("orgId");// 区域代码
			/*
			 * String dutyType = logonUser.getDutyType(); String orgId = "" ;
			 * 
			 * if(Constant.DUTY_TYPE_LARGEREGION.toString().equalsIgnoreCase(dutyType)) {
			 * orgId = logonUser.getOrgId().toString(); } else {
			 * if(!CommonUtils.isNullString(orgIdN)) { orgId = orgIdN ; } }
			 */

			String parentOrgId = logonUser.getParentOrgId();
			String dutyType = logonUser.getDutyType();
			String dealerSql = GetOrgIdsOrDealerIdsDAO.getDealerIdsForOrder(orgId, parentOrgId, dutyType, "TD1");
			String companyId = logonUser.getCompanyId().toString();

			String dataflag = CommonUtils.checkNull(request.getParamValue("dataflag"));// 周度/起止时间
																						// 而选一
			String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));

			// 业务范围id字符串
			Long poseId = logonUser.getPoseId();
			String areaIds = MaterialGroupManagerDao.getPoseIdBusinessIdStr(poseId.toString());

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("orderYear1", orderYear1);
			map.put("orderWeek1", orderWeek1);
			map.put("orderYear2", orderYear2);
			map.put("orderWeek2", orderWeek2);
			map.put("areaId", areaId);
			map.put("groupCode", groupCode);
			map.put("materialCode", materialCode);
			map.put("dealerCode", dealerCode);
			map.put("orderType", orderType);
			map.put("orderStatus", orderStatus);
			map.put("orderNo", orderNo);

			map.put("orgIdN", orgIdN);

			map.put("dealerSql", dealerSql);
			map.put("companyId", companyId);
			map.put("areaIds", areaIds);
			map.put("dataflag", dataflag);
			map.put("beginTime", beginTime);
			map.put("endTime", endTime);
			map.put("groupCode2", groupCode2);
			map.put("dealerCodes", dealerCodes);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getOemSalesOrderDetailQueryList(map, curPage, Integer.parseInt(pageSize));
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "销售订单查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 */
	public void oemSalesOrderTotalExport() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		OutputStream os = null;
		try {
			String dealerCodes = CommonUtils.checkNull(request.getParamValue("dealerCodes"));
			String groupCode2 = CommonUtils.checkNull(request.getParamValue("dealerkaipiaoCode"));
			String orderYear1 = CommonUtils.checkNull(request.getParamValue("orderYear1"));
			String orderWeek1 = CommonUtils.checkNull(request.getParamValue("orderWeek1"));
			String orderYear2 = CommonUtils.checkNull(request.getParamValue("orderYear2"));
			String orderWeek2 = CommonUtils.checkNull(request.getParamValue("orderWeek2"));
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));
			String materialCode = CommonUtils.checkNull(request.getParamValue("materialCode"));
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			String orderType = CommonUtils.checkNull(request.getParamValue("orderType"));
			String orderStatus = CommonUtils.checkNull(request.getParamValue("orderStatus"));
			String orderNo = CommonUtils.checkNull(request.getParamValue("orderNo"));
			String orgId = logonUser.getOrgId().toString();
			String parentOrgId = logonUser.getParentOrgId();
			String dutyType = logonUser.getDutyType();
			String dealerSql = GetOrgIdsOrDealerIdsDAO.getDealerIdsForOrder(orgId, parentOrgId, dutyType, "TD1");
			String companyId = logonUser.getCompanyId().toString();

			String dataflag = CommonUtils.checkNull(request.getParamValue("dataflag"));// 周度/起止时间
																						// 而选一
			String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));
			// 业务范围id字符串
			Long poseId = logonUser.getPoseId();
			String areaIds = MaterialGroupManagerDao.getPoseIdBusinessIdStr(poseId.toString());

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("dealerCode2", groupCode2);
			map.put("orderYear1", orderYear1);
			map.put("orderWeek1", orderWeek1);
			map.put("orderYear2", orderYear2);
			map.put("orderWeek2", orderWeek2);
			map.put("areaId", areaId);
			map.put("groupCode", groupCode);
			map.put("materialCode", materialCode);
			map.put("dealerCode", dealerCode);
			map.put("orderType", orderType);
			map.put("orderStatus", orderStatus);
			map.put("orderNo", orderNo);
			map.put("dealerSql", dealerSql);
			map.put("companyId", companyId);
			map.put("areaIds", areaIds);
			map.put("dataflag", dataflag);
			map.put("beginTime", beginTime);
			map.put("endTime", endTime);
			map.put("dealerCodes", dealerCodes);
			// 导出的文件名
			String fileName = "销售订单汇总.csv";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

			List<List<Object>> list = new LinkedList<List<Object>>();

			List<Object> listTemp = new LinkedList<Object>();
			listTemp.add("物料编号");
			listTemp.add("物料名称");
			listTemp.add("订单类型");
			listTemp.add("提报数量");
			listTemp.add("审核数量");
			/*
			 * //TODO START 新增OTD自动匹配数量 2012-06-18 韩晓宇
			 * listTemp.add("OTD自动匹配数量"); //TODO END
			 */
			listTemp.add("开票数量");
			listTemp.add("发运数量");
			list.add(listTemp);

			List<Map<String, Object>> results = dao.getOemSalesOrderTotalExportList(map);
			for (int i = 0; i < results.size(); i++) {
				Map<String, Object> record = results.get(i);
				listTemp = new LinkedList<Object>();
				listTemp.add(CommonUtils.checkNull(record.get("MATERIAL_CODE")));
				listTemp.add(CommonUtils.checkNull(record.get("MATERIAL_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("ORDER_TYPE")));
				listTemp.add(CommonUtils.checkNull(record.get("ORDER_AMOUNT")));
				listTemp.add(CommonUtils.checkNull(record.get("CHECK_AMOUNT")));
				/*
				 * //TODO START 新增OTD自动匹配数量 2012-06-18 韩晓宇
				 * listTemp.add(CommonUtils.checkNull(record.get("OTD_AMOUNT")));
				 * //TODO END
				 */
				listTemp.add(CommonUtils.checkNull(record.get("BILLING_AMOUNT")));
				listTemp.add(CommonUtils.checkNull(record.get("DELIVERY_AMOUNT")));
				list.add(listTemp);
			}
			os = response.getOutputStream();
			CsvWriterUtil.writeCsv(list, os);
			os.flush();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "销售订单查询");
			logger.error(logonUser, e1);
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
	}

	/**
	 * 
	 */
	public void oemSalesOrderDetailExport() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		OutputStream os = null;
		try {
			String dealerCodes = CommonUtils.checkNull(request.getParamValue("dealerCodes"));
			String groupCode2 = CommonUtils.checkNull(request.getParamValue("dealerkaipiaoCode"));
			String orderYear1 = CommonUtils.checkNull(request.getParamValue("orderYear1"));
			String orderWeek1 = CommonUtils.checkNull(request.getParamValue("orderWeek1"));
			String orderYear2 = CommonUtils.checkNull(request.getParamValue("orderYear2"));
			String orderWeek2 = CommonUtils.checkNull(request.getParamValue("orderWeek2"));
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));
			String materialCode = CommonUtils.checkNull(request.getParamValue("materialCode"));
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			String orderType = CommonUtils.checkNull(request.getParamValue("orderType"));
			String orderStatus = CommonUtils.checkNull(request.getParamValue("orderStatus"));
			String orderNo = CommonUtils.checkNull(request.getParamValue("orderNo"));

			String orgIdN = request.getParamValue("orgId");// 区域代码

			String orgId = logonUser.getOrgId().toString();
			String parentOrgId = logonUser.getParentOrgId();
			String dutyType = logonUser.getDutyType();
			String dealerSql = GetOrgIdsOrDealerIdsDAO.getDealerIdsForOrder(orgId, parentOrgId, dutyType, "TD1");
			String companyId = logonUser.getCompanyId().toString();
			String dataflag = CommonUtils.checkNull(request.getParamValue("dataflag"));// 周度/起止时间
																						// 而选一
			String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));
			// 业务范围id字符串
			Long poseId = logonUser.getPoseId();
			String areaIds = MaterialGroupManagerDao.getPoseIdBusinessIdStr(poseId.toString());

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("groupCode2", groupCode2);
			map.put("orderYear1", orderYear1);
			map.put("orderWeek1", orderWeek1);
			map.put("orderYear2", orderYear2);
			map.put("orderWeek2", orderWeek2);
			map.put("areaId", areaId);
			map.put("groupCode", groupCode);
			map.put("materialCode", materialCode);
			map.put("dealerCode", dealerCode);
			map.put("orderType", orderType);
			map.put("orderStatus", orderStatus);
			map.put("orderNo", orderNo);

			map.put("orgIdN", orgIdN);

			map.put("dealerSql", dealerSql);
			map.put("companyId", companyId);
			map.put("areaIds", areaIds);
			map.put("dataflag", dataflag);
			map.put("beginTime", beginTime);
			map.put("endTime", endTime);
			map.put("dealerCodes", dealerCodes);
			// 导出的文件名
			String fileName = "销售订单明细.csv";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

			List<List<Object>> list = new LinkedList<List<Object>>();

			List<Object> listTemp = new LinkedList<Object>();
			listTemp.add("订货方编码");
			listTemp.add("订货方");
			listTemp.add("开票方");
			listTemp.add("订单号");
			listTemp.add("物料编号");
			listTemp.add("提报日期");
			listTemp.add("提报类型");
			listTemp.add("提报状态");
			listTemp.add("提报数量");
			listTemp.add("审核数量");
			// TODO START 新增OTD自动匹配数量 2012-06-18 韩晓宇
			listTemp.add("OTD自动匹配数量");
			// TODO END
			listTemp.add("开票数量");
			listTemp.add("发运数量");
			listTemp.add("发运方式");
			listTemp.add("运送地址");
			list.add(listTemp);

			List<Map<String, Object>> results = dao.getOemSalesOrderDetailExportList(map);
			for (int i = 0; i < results.size(); i++) {
				Map<String, Object> record = results.get(i);
				listTemp = new LinkedList<Object>();
				listTemp.add(CommonUtils.checkNull(record.get("DEALER_CODE")));
				listTemp.add(CommonUtils.checkNull(record.get("ORDER_ORG_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("BILLING_ORG_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("ORDER_NO")));
				listTemp.add(CommonUtils.checkNull(record.get("MATERIAL_CODE")));
				listTemp.add(CommonUtils.checkNull(record.get("RAISE_DATE")));
				listTemp.add(CommonUtils.checkNull(record.get("ORDER_TYPE")));
				listTemp.add(CommonUtils.checkNull(record.get("ORDER_STATUS")));
				listTemp.add(CommonUtils.checkNull(record.get("ORDER_AMOUNT")));
				listTemp.add(CommonUtils.checkNull(record.get("CHECK_AMOUNT")));
				// TODO START 新增OTD自动匹配数量 2012-06-18 韩晓宇
				listTemp.add(CommonUtils.checkNull(record.get("OTD_AMOUNT")));
				// TODO END
				listTemp.add(CommonUtils.checkNull(record.get("BILLING_AMOUNT")));
				listTemp.add(CommonUtils.checkNull(record.get("DELIVERY_AMOUNT")));
				listTemp.add(CommonUtils.checkNull(record.get("DELIVERY_TYPE")));
				listTemp.add(CommonUtils.checkNull(record.get("DELIVERY_ADDRESS")));
				list.add(listTemp);
			}
			os = response.getOutputStream();
			CsvWriterUtil.writeCsv(list, os);
			os.flush();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "销售订单查询");
			logger.error(logonUser, e1);
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
	}

	public void oemSalesOrderDetailExport_CVS() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		OutputStream os = null;
		try {
			String dealerCodes = CommonUtils.checkNull(request.getParamValue("dealerCodes"));
			String groupCode2 = CommonUtils.checkNull(request.getParamValue("dealerkaipiaoCode"));
			String orderYear1 = CommonUtils.checkNull(request.getParamValue("orderYear1"));
			String orderWeek1 = CommonUtils.checkNull(request.getParamValue("orderWeek1"));
			String orderYear2 = CommonUtils.checkNull(request.getParamValue("orderYear2"));
			String orderWeek2 = CommonUtils.checkNull(request.getParamValue("orderWeek2"));
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));
			String materialCode = CommonUtils.checkNull(request.getParamValue("materialCode"));
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			String orderType = CommonUtils.checkNull(request.getParamValue("orderType"));
			String orderStatus = CommonUtils.checkNull(request.getParamValue("orderStatus"));
			String orderNo = CommonUtils.checkNull(request.getParamValue("orderNo"));

			String orgIdN = request.getParamValue("orgId");// 区域代码

			String orgId = logonUser.getOrgId().toString();
			String parentOrgId = logonUser.getParentOrgId();
			String dutyType = logonUser.getDutyType();
			String dealerSql = GetOrgIdsOrDealerIdsDAO.getDealerIdsForOrder(orgId, parentOrgId, dutyType, "TD1");
			String companyId = logonUser.getCompanyId().toString();
			String dataflag = CommonUtils.checkNull(request.getParamValue("dataflag"));// 周度/起止时间
																						// 而选一
			String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));
			// 业务范围id字符串
			Long poseId = logonUser.getPoseId();
			String areaIds = MaterialGroupManagerDao.getPoseIdBusinessIdStr(poseId.toString());

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("groupCode2", groupCode2);
			map.put("orderYear1", orderYear1);
			map.put("orderWeek1", orderWeek1);
			map.put("orderYear2", orderYear2);
			map.put("orderWeek2", orderWeek2);
			map.put("areaId", areaId);
			map.put("groupCode", groupCode);
			map.put("materialCode", materialCode);
			map.put("dealerCode", dealerCode);
			map.put("orderType", orderType);
			map.put("orderStatus", orderStatus);
			map.put("orderNo", orderNo);

			map.put("orgIdN", orgIdN);

			map.put("dealerSql", dealerSql);
			map.put("companyId", companyId);
			map.put("areaIds", areaIds);
			map.put("dataflag", dataflag);
			map.put("beginTime", beginTime);
			map.put("endTime", endTime);
			map.put("dealerCodes", dealerCodes);
			// 导出的文件名
			String fileName = "销售订单明细.csv";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

			List<List<Object>> list = new LinkedList<List<Object>>();

			List<Object> listTemp = new LinkedList<Object>();
			listTemp.add("订货方编码");
			listTemp.add("订货方");
			listTemp.add("开票方");
			listTemp.add("订单号");
			listTemp.add("物料编号");
			listTemp.add("提报日期");
			listTemp.add("提报类型");
			listTemp.add("提报状态");
			listTemp.add("提报数量");
			listTemp.add("审核数量");
			// TODO START 新增OTD自动匹配数量 2012-06-18 韩晓宇
			// listTemp.add("OTD自动匹配数量");
			// TODO END
			listTemp.add("开票数量");
			listTemp.add("发运数量");
			listTemp.add("发运方式");
			listTemp.add("运送地址");
			list.add(listTemp);

			List<Map<String, Object>> results = dao.getOemSalesOrderDetailExportList(map);
			for (int i = 0; i < results.size(); i++) {
				Map<String, Object> record = results.get(i);
				listTemp = new LinkedList<Object>();
				listTemp.add(CommonUtils.checkNull(record.get("DEALER_CODE")));
				listTemp.add(CommonUtils.checkNull(record.get("ORDER_ORG_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("BILLING_ORG_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("ORDER_NO")));
				listTemp.add(CommonUtils.checkNull(record.get("MATERIAL_CODE")));
				listTemp.add(CommonUtils.checkNull(record.get("RAISE_DATE")));
				listTemp.add(CommonUtils.checkNull(record.get("ORDER_TYPE")));
				listTemp.add(CommonUtils.checkNull(record.get("ORDER_STATUS")));
				listTemp.add(CommonUtils.checkNull(record.get("ORDER_AMOUNT")));
				listTemp.add(CommonUtils.checkNull(record.get("CHECK_AMOUNT")));
				// TODO START 新增OTD自动匹配数量 2012-06-18 韩晓宇
				// listTemp.add(CommonUtils.checkNull(record.get("OTD_AMOUNT")));
				// TODO END
				listTemp.add(CommonUtils.checkNull(record.get("BILLING_AMOUNT")));
				listTemp.add(CommonUtils.checkNull(record.get("MATCH_AMOUNT")));
				listTemp.add(CommonUtils.checkNull(record.get("DELIVERY_TYPE")));
				listTemp.add(CommonUtils.checkNull(record.get("DELIVERY_ADDRESS")));
				list.add(listTemp);
			}
			os = response.getOutputStream();
			CsvWriterUtil.writeCsv(list, os);
			os.flush();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "销售订单查询");
			logger.error(logonUser, e1);
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
	}

	/*
	 * 发运订单查询
	 */
	public void deliveryOrderQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Long poseId = logonUser.getPoseId();
			String dutyType = logonUser.getDutyType();
			Long orgId = logonUser.getOrgId();

			List<Map<String, Object>> orgList = OrderDeliveryDao.getOemOrg(dutyType, orgId);

			act.setOutData("orgList", orgList);
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(poseId.toString());
			act.setOutData("areaList", areaList);
			act.setOutData("nowDuty", Constant.DUTY_TYPE_DEALER);
			act.setOutData("dutyType", dutyType);
			act.setForword(delUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "发运订单查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/*
	 * 发运订单查询
	 */
	public void deliveryOrderQueryByJson() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String dealerCodes = request.getParamValue("dealerCodes"); // 经销商CODE
			String beginTime = request.getParamValue("beginTime");
			String endTime = request.getParamValue("endTime");
			String startDate = request.getParamValue("startDate");
			String endDate = request.getParamValue("endDate");
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));

			String regionId = CommonUtils.checkNull(request.getParamValue("downtown")); // 省份
			String pageOrgId = CommonUtils.checkNull(request.getParamValue("orgId")); // 事业部
			String myOrderType = request.getParamValue("myOrderType"); // 订单类型
			String myOrderNo = request.getParamValue("myOrderNo");// 订单编号
			Long orgId = logonUser.getOrgId();

			String pageSize = CommonUtils.checkNull(request.getParamValue("pageSize"));

			String dutyType = logonUser.getDutyType();
			String dealerCode = null;
			String dealerId = null;
			if (!dutyType.equals(String.valueOf(Constant.DUTY_TYPE_DEALER))) {// 判断是否经销商
				dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
				if (!dealerCode.equals("")) {// 截串加单引号
					String[] supp = dealerCode.split(",");
					dealerCode = "";
					for (int i = 0; i < supp.length; i++) {
						supp[i] = "'" + supp[i] + "'";
						if (!dealerCode.equals("")) {
							dealerCode += "," + supp[i];
						} else {
							dealerCode = supp[i];
						}
					}
				}
			} else {
				dealerId = logonUser.getDealerId();
				dealerId = dao.getDlr(dealerId, Constant.DEALER_LEVEL_01.toString());
			}

			String orderNo = request.getParamValue("orderNo");
			String transType = request.getParamValue("transType");
			String reqStatus = request.getParamValue("reqStatus");
			String isDaijiao = CommonUtils.checkNull(request.getParamValue("isDaijiao"));
			String oldOrderNo = CommonUtils.checkNull(request.getParamValue("oldOrderNo"));
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			if (Utility.testString(beginTime)) {
				beginTime = beginTime + " 00:00:00";
			}
			if (Utility.testString(endTime)) {
				endTime = endTime + " 23:59:59";
			}
			if (Utility.testString(startDate)) {
				startDate = startDate + " 00:00:00";
			}
			if (Utility.testString(endDate)) {
				endDate = endDate + " 23:59:59";
			}
			StringBuffer areaIds = new StringBuffer();
			List<Map<String, Object>> areaBusList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			for (int i = 0; i < areaBusList.size(); i++) {
				if (!"".equals(String.valueOf(areaBusList.get(i).get("AREA_ID")))) {
					areaIds.append(String.valueOf(areaBusList.get(i).get("AREA_ID")));
				}
				if (i < areaBusList.size() - 1) {
					areaIds.append(",");
				}
			}

			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.deliveryOrderQuery(startDate,endDate,dealerCodes,myOrderType,myOrderNo,regionId, pageOrgId, orgId, isDaijiao,areaIds.toString(), dutyType, dealerId, companyId, beginTime, endTime, areaId, dealerCode, orderNo, transType, reqStatus, oldOrderNo,curPage,Integer.parseInt(pageSize));
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "发运单查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void deliveryOrderExportByJson() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		OutputStream os = null;
		try {

			String beginTime = request.getParamValue("beginTime");
			String endTime = request.getParamValue("endTime");
			String startDate = request.getParamValue("startDate");
			String endDate = request.getParamValue("endDate");
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String regionId = CommonUtils.checkNull(request.getParamValue("downtown")); // 省份
			String pageOrgId = CommonUtils.checkNull(request.getParamValue("orgId")); // 事业部

			Long orgId = logonUser.getOrgId();
			String dutyType = logonUser.getDutyType();
			String dealerCode = null;
			String dealerId = null;
			if (!dutyType.equals(String.valueOf(Constant.DUTY_TYPE_DEALER))) {// 判断是否经销商
				dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
				if (!dealerCode.equals("")) {// 截串加单引号
					String[] supp = dealerCode.split(",");
					dealerCode = "";
					for (int i = 0; i < supp.length; i++) {
						supp[i] = "'" + supp[i] + "'";
						if (!dealerCode.equals("")) {
							dealerCode += "," + supp[i];
						} else {
							dealerCode = supp[i];
						}
					}
				}
			} else {
				dealerId = logonUser.getDealerId();
				dealerId = dao.getDlr(dealerId, Constant.DEALER_LEVEL_01.toString());
			}

			String orderNo = request.getParamValue("orderNo");
			String transType = request.getParamValue("transType");
			String reqStatus = request.getParamValue("reqStatus");
			String isDaijiao = CommonUtils.checkNull(request.getParamValue("isDaijiao"));
			String oldOrderNo = CommonUtils.checkNull(request.getParamValue("oldOrderNo"));
			String myOrderNo = request.getParamValue("myOrderNo");
			String myOrderType = request.getParamValue("myOrderType");
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			if (Utility.testString(beginTime)) {
				beginTime = beginTime + " 00:00:00";
			}
			if (Utility.testString(endTime)) {
				endTime = endTime + " 23:59:59";
			}
			if (Utility.testString(startDate)) {
				startDate = startDate + " 00:00:00";
			}
			if (Utility.testString(endDate)) {
				endDate = endDate + " 23:59:59";
			}
			StringBuffer areaIds = new StringBuffer();
			List<Map<String, Object>> areaBusList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			for (int i = 0; i < areaBusList.size(); i++) {
				if (!"".equals(String.valueOf(areaBusList.get(i).get("AREA_ID")))) {
					areaIds.append(String.valueOf(areaBusList.get(i).get("AREA_ID")));
				}
				if (i < areaBusList.size() - 1) {
					areaIds.append(",");
				}
			}

			// 导出的文件名
			String fileName = "发运订单查询.csv";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

			List<List<Object>> list = new LinkedList<List<Object>>();

			List<Object> listTemp = new LinkedList<Object>();
			listTemp.add("采购单位");
			listTemp.add("开票单位代码");
			listTemp.add("开票单位");
			listTemp.add("订单类型");
			listTemp.add("提报时间");
			listTemp.add("资源最终审核日期");
			listTemp.add("启票订单号码");
			listTemp.add("订单号码");
			if (!dutyType.equals(String.valueOf(Constant.DUTY_TYPE_DEALER))) {
				listTemp.add("下级经销商订单号");
			}
			listTemp.add("申请数量");
			listTemp.add("保留资源数量");
			listTemp.add("状态");
			listTemp.add("运送方式");
			listTemp.add("收货方");
			list.add(listTemp);

			List<Map<String, Object>> results = dao.deliveryOrderExport(startDate,endDate,myOrderNo, myOrderType, regionId, pageOrgId, orgId, isDaijiao, areaIds.toString(), dutyType, dealerId, companyId, beginTime, endTime, areaId, dealerCode, orderNo, transType, reqStatus, oldOrderNo);
			for (int i = 0; i < results.size(); i++) {
				Map<String, Object> record = results.get(i);
				listTemp = new LinkedList<Object>();
				listTemp.add(CommonUtils.checkNull(record.get("DEALER_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("DEALER_CODE")));
				listTemp.add(CommonUtils.checkNull(record.get("DEALER_NAME1")));
				listTemp.add(CommonUtils.checkNull(record.get("ORDER_TYPE")));
				listTemp.add(CommonUtils.checkNull(record.get("REQ_DATE")));
				listTemp.add(CommonUtils.checkNull(record.get("UPDATE_DATE")));
				listTemp.add(CommonUtils.checkNull(record.get("DLVRY_REQ_NO")));
				listTemp.add(CommonUtils.checkNull(record.get("ORDER_NO")));
				if (!dutyType.equals(String.valueOf(Constant.DUTY_TYPE_DEALER))) {
					listTemp.add(CommonUtils.checkNull(record.get("OLD_ORDER_NO")));
				}
				listTemp.add(CommonUtils.checkNull(record.get("REQ_TOTAL_AMOUNT")));
				listTemp.add(CommonUtils.checkNull(record.get("RESERVE_AMOUNT")));
				listTemp.add(CommonUtils.checkNull(record.get("REQ_STATUS")));
				listTemp.add(CommonUtils.checkNull(record.get("DELIVERY_TYPE")));
				listTemp.add(CommonUtils.checkNull(record.get("DEALER_NAME2")));
				list.add(listTemp);
			}
			os = response.getOutputStream();
			CsvWriterUtil.writeCsv(list, os);
			os.flush();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "发运订单查询下载");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

}
