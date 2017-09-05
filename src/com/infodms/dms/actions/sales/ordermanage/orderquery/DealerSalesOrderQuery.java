/**
 * @Title: DealerSalesOrderQuery.java
 * 
 * @Description:
 * 
 * @Copyright: Copyright (c) 2010
 * 
 * @Company: www.infoservice.com.cn
 * @Date: 2010-6-9
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
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.relation.DealerRelationDAO;
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
public class DealerSalesOrderQuery {
	private Logger logger = Logger.getLogger(DealerSalesOrderQuery.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	ResponseWrapper response = act.getResponse();
	private final OrderQueryDao dao = OrderQueryDao.getInstance();
	private final OrderReportDao reportDao = OrderReportDao.getInstance();

	private final String DEALER_SALES_ORDER_QUERY_URL = "/jsp/sales/ordermanage/orderquery/dealerSalesOrderQueryPre.jsp";// 销售订单查询页面

	public void dealerSalesOrderQueryPre() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Long poseId = logonUser.getPoseId();
			Long orgId = logonUser.getOrgId();
			List<String> years = dao.getYearList();
			List<String> weeks = dao.getWeekList();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getDealerBusiness(poseId.toString());
			// 查看日期配置表中当天的记录
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			TmDateSetPO dateSet = reportDao.getTmDateSetPO(new Date(), oemCompanyId);

			act.setOutData("orgId", orgId);
			act.setOutData("years", years);
			act.setOutData("weeks", weeks);
			act.setOutData("curYear", dateSet != null ? dateSet.getSetYear() : "");
			act.setOutData("curWeek", dateSet != null ? dateSet.getSetWeek() : "");
			act.setOutData("areaList", areaList);
			act.setForword(DEALER_SALES_ORDER_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "销售订单查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void dealerSalesOrderTotalQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String orderYear1 = CommonUtils.checkNull(request.getParamValue("orderYear1"));
			String orderWeek1 = CommonUtils.checkNull(request.getParamValue("orderWeek1"));
			String orderYear2 = CommonUtils.checkNull(request.getParamValue("orderYear2"));
			String orderWeek2 = CommonUtils.checkNull(request.getParamValue("orderWeek2"));
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));
			String materialCode = CommonUtils.checkNull(request.getParamValue("materialCode"));
			String orderType = CommonUtils.checkNull(request.getParamValue("orderType"));
			String orderStatus = CommonUtils.checkNull(request.getParamValue("orderStatus"));
			String orderNo = CommonUtils.checkNull(request.getParamValue("orderNo"));
			// String dealerId = logonUser.getDealerId();
			String companyId = logonUser.getOemCompanyId();
			String dutyType = logonUser.getDutyType();
			String orgId = logonUser.getOrgId().toString();
			String parentOrgId = logonUser.getParentOrgId();
			List<Map<String, Object>> dealerList = DealerRelationDAO.getInstance().getAreaByDlrAndPose(logonUser.getDealerId(), logonUser.getPoseId().toString());
			StringBuffer dealerSql = new StringBuffer("");

			if (!CommonUtils.isNullList(dealerList)) {
				for (int i = 0; i < dealerList.size(); i++) {
					if (dealerSql.length() == 0) {
						dealerSql.append(dealerList.get(i).get("DEALER_ID").toString());
					} else {
						dealerSql.append(",").append(dealerList.get(i).get("DEALER_ID").toString());
					}
				}
			}

			dealerList = DealerRelationDAO.getInstance().getAllDlrByFrtDlr(dealerSql.toString());

			dealerSql = new StringBuffer("");

			if (!CommonUtils.isNullList(dealerList)) {
				for (int i = 0; i < dealerList.size(); i++) {
					if (dealerSql.length() == 0) {
						dealerSql.append(dealerList.get(i).get("DEALER_ID").toString());
					} else {
						dealerSql.append(",").append(dealerList.get(i).get("DEALER_ID").toString());
					}
				}
			}

			// 业务范围id字符串
			Long poseId = logonUser.getPoseId();
			String areaIds = MaterialGroupManagerDao.getDealerBusinessIdStr(poseId.toString());
			String dataflag = CommonUtils.checkNull(request.getParamValue("dataflag"));
			String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("orderYear1", orderYear1);
			map.put("orderWeek1", orderWeek1);
			map.put("orderYear2", orderYear2);
			map.put("orderWeek2", orderWeek2);
			map.put("areaId", areaId);
			map.put("groupCode", groupCode);
			map.put("materialCode", materialCode);
			map.put("orderType", orderType);
			map.put("orderStatus", orderStatus);
			map.put("orderNo", orderNo);
			// map.put("dealerId", dealerId);
			map.put("companyId", companyId);
			map.put("dealerSql", dealerSql.toString());
			map.put("areaIds", areaIds);
			map.put("dataflag", dataflag);
			map.put("beginTime", beginTime);
			map.put("endTime", endTime);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getDealerSalesOrderTotalQueryList(map, curPage, Constant.PAGE_SIZE_MAX);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "销售订单查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void dealerSalesOrderDetailQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String orderYear1 = CommonUtils.checkNull(request.getParamValue("orderYear1"));
			String orderWeek1 = CommonUtils.checkNull(request.getParamValue("orderWeek1"));
			String orderYear2 = CommonUtils.checkNull(request.getParamValue("orderYear2"));
			String orderWeek2 = CommonUtils.checkNull(request.getParamValue("orderWeek2"));
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));
			String materialCode = CommonUtils.checkNull(request.getParamValue("materialCode"));
			String orderType = CommonUtils.checkNull(request.getParamValue("orderType"));
			String orderStatus = CommonUtils.checkNull(request.getParamValue("orderStatus"));
			String orderNo = CommonUtils.checkNull(request.getParamValue("orderNo"));
			// String dealerId = logonUser.getDealerId();
			String companyId = logonUser.getOemCompanyId();
			String dutyType = logonUser.getDutyType();
			String orgId = logonUser.getOrgId().toString();
			String parentOrgId = logonUser.getParentOrgId();
			List<Map<String, Object>> dealerList = DealerRelationDAO.getInstance().getAreaByDlrAndPose(logonUser.getDealerId(), logonUser.getPoseId().toString());
			StringBuffer dealerSql = new StringBuffer("");

			if (!CommonUtils.isNullList(dealerList)) {
				for (int i = 0; i < dealerList.size(); i++) {
					if (dealerSql.length() == 0) {
						dealerSql.append(dealerList.get(i).get("DEALER_ID").toString());
					} else {
						dealerSql.append(",").append(dealerList.get(i).get("DEALER_ID").toString());
					}
				}
			}

			dealerList = DealerRelationDAO.getInstance().getAllDlrByFrtDlr(dealerSql.toString());

			dealerSql = new StringBuffer("");

			if (!CommonUtils.isNullList(dealerList)) {
				for (int i = 0; i < dealerList.size(); i++) {
					if (dealerSql.length() == 0) {
						dealerSql.append(dealerList.get(i).get("DEALER_ID").toString());
					} else {
						dealerSql.append(",").append(dealerList.get(i).get("DEALER_ID").toString());
					}
				}
			}

			// 业务范围id字符串
			Long poseId = logonUser.getPoseId();
			String areaIds = MaterialGroupManagerDao.getDealerBusinessIdStr(poseId.toString());
			String dataflag = CommonUtils.checkNull(request.getParamValue("dataflag"));
			String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("orderYear1", orderYear1);
			map.put("orderWeek1", orderWeek1);
			map.put("orderYear2", orderYear2);
			map.put("orderWeek2", orderWeek2);
			map.put("areaId", areaId);
			map.put("groupCode", groupCode);
			map.put("materialCode", materialCode);
			map.put("orderType", orderType);
			map.put("orderStatus", orderStatus);
			map.put("orderNo", orderNo);
			map.put("dealerSql", dealerSql.toString());
			map.put("companyId", companyId);
			map.put("areaIds", areaIds);
			map.put("dataflag", dataflag);
			map.put("beginTime", beginTime);
			map.put("endTime", endTime);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getDealerSalesOrderDetailQueryList(map, curPage, Constant.PAGE_SIZE);
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
	public void dealerSalesOrderTotalExport() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		OutputStream os = null;
		try {
			String orderYear1 = CommonUtils.checkNull(request.getParamValue("orderYear1"));
			String orderWeek1 = CommonUtils.checkNull(request.getParamValue("orderWeek1"));
			String orderYear2 = CommonUtils.checkNull(request.getParamValue("orderYear2"));
			String orderWeek2 = CommonUtils.checkNull(request.getParamValue("orderWeek2"));
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));
			String materialCode = CommonUtils.checkNull(request.getParamValue("materialCode"));
			String orderType = CommonUtils.checkNull(request.getParamValue("orderType"));
			String orderStatus = CommonUtils.checkNull(request.getParamValue("orderStatus"));
			String orderNo = CommonUtils.checkNull(request.getParamValue("orderNo"));
			String companyId = logonUser.getOemCompanyId();
			String dutyType = logonUser.getDutyType();
			String orgId = logonUser.getOrgId().toString();
			String parentOrgId = logonUser.getParentOrgId();
			/*
			 * String dealerSql = GetOrgIdsOrDealerIdsDAO.getDealerIds(orgId,
			 * parentOrgId, dutyType);
			 */

			List<Map<String, Object>> dealerList = DealerRelationDAO.getInstance().getAreaByDlrAndPose(logonUser.getDealerId(), logonUser.getPoseId().toString());
			StringBuffer dealerSql = new StringBuffer("");

			if (!CommonUtils.isNullList(dealerList)) {
				for (int i = 0; i < dealerList.size(); i++) {
					if (dealerSql.length() == 0) {
						dealerSql.append(dealerList.get(i).get("DEALER_ID").toString());
					} else {
						dealerSql.append(",").append(dealerList.get(i).get("DEALER_ID").toString());
					}
				}
			}

			dealerList = DealerRelationDAO.getInstance().getAllDlrByFrtDlr(dealerSql.toString());

			dealerSql = new StringBuffer("");

			if (!CommonUtils.isNullList(dealerList)) {
				for (int i = 0; i < dealerList.size(); i++) {
					if (dealerSql.length() == 0) {
						dealerSql.append(dealerList.get(i).get("DEALER_ID").toString());
					} else {
						dealerSql.append(",").append(dealerList.get(i).get("DEALER_ID").toString());
					}
				}
			}

			// 业务范围id字符串
			Long poseId = logonUser.getPoseId();
			String areaIds = MaterialGroupManagerDao.getDealerBusinessIdStr(poseId.toString());

			String dataflag = CommonUtils.checkNull(request.getParamValue("dataflag"));
			String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("orderYear1", orderYear1);
			map.put("orderWeek1", orderWeek1);
			map.put("orderYear2", orderYear2);
			map.put("orderWeek2", orderWeek2);
			map.put("areaId", areaId);
			map.put("groupCode", groupCode);
			map.put("materialCode", materialCode);
			map.put("orderType", orderType);
			map.put("orderStatus", orderStatus);
			map.put("orderNo", orderNo);
			map.put("dealerSql", dealerSql.toString());
			map.put("companyId", companyId);
			map.put("areaIds", areaIds);
			map.put("dataflag", dataflag);
			map.put("beginTime", beginTime);
			map.put("endTime", endTime);
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
			listTemp.add("开票数量");
			listTemp.add("发运数量");
			list.add(listTemp);

			List<Map<String, Object>> results = dao.getDealerSalesOrderTotalExportList(map);
			for (int i = 0; i < results.size(); i++) {
				Map<String, Object> record = results.get(i);
				listTemp = new LinkedList<Object>();
				listTemp.add(CommonUtils.checkNull(record.get("MATERIAL_CODE")));
				listTemp.add(CommonUtils.checkNull(record.get("MATERIAL_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("ORDER_TYPE")));
				listTemp.add(CommonUtils.checkNull(record.get("ORDER_AMOUNT")));
				listTemp.add(CommonUtils.checkNull(record.get("CHECK_AMOUNT")));
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
	public void dealerSalesOrderDetailExport() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		OutputStream os = null;
		try {
			String orderYear1 = CommonUtils.checkNull(request.getParamValue("orderYear1"));
			String orderWeek1 = CommonUtils.checkNull(request.getParamValue("orderWeek1"));
			String orderYear2 = CommonUtils.checkNull(request.getParamValue("orderYear2"));
			String orderWeek2 = CommonUtils.checkNull(request.getParamValue("orderWeek2"));
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));
			String materialCode = CommonUtils.checkNull(request.getParamValue("materialCode"));
			String orderType = CommonUtils.checkNull(request.getParamValue("orderType"));
			String orderStatus = CommonUtils.checkNull(request.getParamValue("orderStatus"));
			String orderNo = CommonUtils.checkNull(request.getParamValue("orderNo"));
			String companyId = logonUser.getOemCompanyId();
			String dutyType = logonUser.getDutyType();
			String orgId = logonUser.getOrgId().toString();
			String parentOrgId = logonUser.getParentOrgId();
			/*
			 * String dealerSql = GetOrgIdsOrDealerIdsDAO.getDealerIds(orgId,
			 * parentOrgId, dutyType);
			 */
			List<Map<String, Object>> dealerList = DealerRelationDAO.getInstance().getAreaByDlrAndPose(logonUser.getDealerId(), logonUser.getPoseId().toString());
			StringBuffer dealerSql = new StringBuffer("");

			if (!CommonUtils.isNullList(dealerList)) {
				for (int i = 0; i < dealerList.size(); i++) {
					if (dealerSql.length() == 0) {
						dealerSql.append(dealerList.get(i).get("DEALER_ID").toString());
					} else {
						dealerSql.append(",").append(dealerList.get(i).get("DEALER_ID").toString());
					}
				}
			}

			dealerList = DealerRelationDAO.getInstance().getAllDlrByFrtDlr(dealerSql.toString());

			dealerSql = new StringBuffer("");

			if (!CommonUtils.isNullList(dealerList)) {
				for (int i = 0; i < dealerList.size(); i++) {
					if (dealerSql.length() == 0) {
						dealerSql.append(dealerList.get(i).get("DEALER_ID").toString());
					} else {
						dealerSql.append(",").append(dealerList.get(i).get("DEALER_ID").toString());
					}
				}
			}
			// 业务范围id字符串
			Long poseId = logonUser.getPoseId();
			String areaIds = MaterialGroupManagerDao.getDealerBusinessIdStr(poseId.toString());
			String dataflag = CommonUtils.checkNull(request.getParamValue("dataflag"));
			String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("orderYear1", orderYear1);
			map.put("orderWeek1", orderWeek1);
			map.put("orderYear2", orderYear2);
			map.put("orderWeek2", orderWeek2);
			map.put("areaId", areaId);
			map.put("groupCode", groupCode);
			map.put("materialCode", materialCode);
			map.put("orderType", orderType);
			map.put("orderStatus", orderStatus);
			map.put("orderNo", orderNo);
			map.put("dealerSql", dealerSql.toString());
			map.put("companyId", companyId);
			map.put("areaIds", areaIds);
			map.put("dataflag", dataflag);
			map.put("beginTime", beginTime);
			map.put("endTime", endTime);
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
			listTemp.add("提报日期");
			listTemp.add("提报类型");
			listTemp.add("提报状态");
			listTemp.add("提报数量");
			listTemp.add("审核数量");
			listTemp.add("开票数量");
			listTemp.add("发运数量");
			listTemp.add("发运方式");
			listTemp.add("运送地址");
			list.add(listTemp);

			List<Map<String, Object>> results = dao.getDealerSalesOrderDetailExportList(map);
			for (int i = 0; i < results.size(); i++) {
				Map<String, Object> record = results.get(i);
				listTemp = new LinkedList<Object>();
				listTemp.add(CommonUtils.checkNull(record.get("DEALER_CODE")));
				listTemp.add(CommonUtils.checkNull(record.get("ORDER_ORG_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("BILLING_ORG_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("ORDER_NO")));
				listTemp.add(CommonUtils.checkNull(record.get("RAISE_DATE")));
				listTemp.add(CommonUtils.checkNull(record.get("ORDER_TYPE")));
				listTemp.add(CommonUtils.checkNull(record.get("ORDER_STATUS")));
				listTemp.add(CommonUtils.checkNull(record.get("ORDER_AMOUNT")));
				listTemp.add(CommonUtils.checkNull(record.get("CHECK_AMOUNT")));
				listTemp.add(CommonUtils.checkNull(record.get("BILLING_AMOUNT")));
				listTemp.add(CommonUtils.checkNull(record.get("DELIVERY_AMOUNT")));
				listTemp.add(CommonUtils.checkNull(record.get("DETYPE")));
				listTemp.add(CommonUtils.checkNull(record.get("DEADDR")));
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
}
