package com.infodms.dms.actions.sales.balancecentermanage.dealerordermanage;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSet;
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
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.orgmng.GetOrgIdsOrDealerIdsDAO;
import com.infodms.dms.dao.sales.balancecentermanage.dealerordermanage.DealerOrderQueryDAO;
import com.infodms.dms.dao.sales.ordermanage.orderquery.OrderQueryDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.OrderReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDateSetPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * @Title : DealerOrderQuery.java
 * @Package: com.infodms.dms.actions.sales.balancecentermanage.dealerordermanage
 * @Description: 结算中心管理：经销商订单管理--经销商订单查询
 * @date : 2010-6-25
 * @version: V1.0
 */
public class DealerOrderQuery extends BaseDao {

	public Logger logger = Logger.getLogger(DealerOrderQuery.class);
	private ActionContext act = ActionContext.getContext();
	private static final DealerOrderQuery dao = new DealerOrderQuery();
	ResponseWrapper response = act.getResponse();

	public static final DealerOrderQuery getInstance() {
		return dao;
	}
	private final OrderReportDao reportDao = OrderReportDao.getInstance();
	private final OrderQueryDao orderQueryDao = OrderQueryDao.getInstance();
	RequestWrapper request = act.getRequest();
	private final String dealerOrderQueryInitURL = "/jsp/sales/balancecentermanage/dealerordermanage/dealerOrderQueryInit.jsp";

	/**
	 * @Title : dealerOrderQueryInit
	 * @Description: 结算中心管理：经销商订单查询页面初始化
	 * @throws
	 * @LastUpdate :2010-6-25
	 */
	public void dealerOrderQueryInit() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

			Long poseId = logonUser.getPoseId();
			Long orgId = logonUser.getOrgId();
			List<String> years = orderQueryDao.getYearList();
			List<String> weeks = orderQueryDao.getWeekList();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(poseId.toString());
			Calendar c = Calendar.getInstance();
			// 查看日期配置表中当天的记录
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			TmDateSetPO dateSet = reportDao.getTmDateSetPO(new Date(),
					oemCompanyId);

			act.setOutData("orgId", orgId);
			act.setOutData("years", years);
			act.setOutData("weeks", weeks);
			act.setOutData("curYear", dateSet != null ? dateSet.getSetYear()
					: "");
			act.setOutData("curWeek", dateSet != null ? dateSet.getSetWeek()
					: "");
			act.setOutData("areaList", areaList);

			act.setForword(dealerOrderQueryInitURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商订单审核页面初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * @Title : dealerOrderTotalQuery
	 * @Description: 汇总查询
	 * @throws
	 * @LastUpdate :2010-6-25
	 */
	public void dealerOrderTotalQuery() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

			String orderYear1 = CommonUtils.checkNull(request.getParamValue("orderYear1"));
			String orderWeek1 = CommonUtils.checkNull(request.getParamValue("orderWeek1"));
			String orderYear2 = CommonUtils.checkNull(request.getParamValue("orderYear2"));
			String orderWeek2 = CommonUtils.checkNull(request.getParamValue("orderWeek2"));
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));
			String orderType = CommonUtils.checkNull(request.getParamValue("orderType"));
			String orderStatus = CommonUtils.checkNull(request.getParamValue("orderStatus"));
			String orderNo = CommonUtils.checkNull(request.getParamValue("orderNo"));
			String newOrderNo = CommonUtils.checkNull(request.getParamValue("newOrderNo"));
			String dealerId = logonUser.getDealerId();

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("orderYear1", orderYear1);
			map.put("orderWeek1", orderWeek1);
			map.put("orderYear2", orderYear2);
			map.put("orderWeek2", orderWeek2);
			map.put("areaId", areaId);
			map.put("groupCode", groupCode);
			map.put("orderType", orderType);
			map.put("orderStatus", orderStatus);
			map.put("orderNo", orderNo);
			map.put("newOrderNo", newOrderNo);
			map.put("dealerId", dealerId);

			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = DealerOrderQueryDAO.dealerOrderTotalQueryList(map, curPage, Constant.PAGE_SIZE_MAX);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "明细查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * @Title : dealerOrderDetailQuery
	 * @Description: 明细查询
	 * @throws
	 * @LastUpdate :2010-6-25
	 */
	public void dealerOrderDetailQuery() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

			String orderYear1 = CommonUtils.checkNull(request.getParamValue("orderYear1"));
			String orderWeek1 = CommonUtils.checkNull(request.getParamValue("orderWeek1"));
			String orderYear2 = CommonUtils.checkNull(request.getParamValue("orderYear2"));
			String orderWeek2 = CommonUtils.checkNull(request.getParamValue("orderWeek2"));
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));
			String orderType = CommonUtils.checkNull(request.getParamValue("orderType"));
			String orderStatus = CommonUtils.checkNull(request.getParamValue("orderStatus"));
			String orderNo = CommonUtils.checkNull(request.getParamValue("orderNo"));
			String newOrderNo = CommonUtils.checkNull(request.getParamValue("newOrderNo"));
			String dealerId = logonUser.getDealerId();

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("orderYear1", orderYear1);
			map.put("orderWeek1", orderWeek1);
			map.put("orderYear2", orderYear2);
			map.put("orderWeek2", orderWeek2);
			map.put("areaId", areaId);
			map.put("groupCode", groupCode);
			map.put("orderType", orderType);
			map.put("orderStatus", orderStatus);
			map.put("orderNo", orderNo);
			map.put("newOrderNo", newOrderNo);
			map.put("dealerId", dealerId);

			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = DealerOrderQueryDAO.dealerOrderDetailQueryList(map, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "明细查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * @Title : dealerSalesOrderTotalExport
	 * @Description: 汇总下载
	 * @param : 设定文件
	 * @return : void返回类型
	 * @throws
	 * @LastUpdate :2010-6-25
	 */
	public void dealerSalesOrderTotalExport() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		OutputStream os = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

			String orderYear1 = CommonUtils.checkNull(request.getParamValue("orderYear1"));
			String orderWeek1 = CommonUtils.checkNull(request.getParamValue("orderWeek1"));
			String orderYear2 = CommonUtils.checkNull(request.getParamValue("orderYear2"));
			String orderWeek2 = CommonUtils.checkNull(request.getParamValue("orderWeek2"));
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));
			String orderType = CommonUtils.checkNull(request.getParamValue("orderType"));
			String orderStatus = CommonUtils.checkNull(request.getParamValue("orderStatus"));
			String orderNo = CommonUtils.checkNull(request.getParamValue("orderNo"));
			String newOrderNo = CommonUtils.checkNull(request.getParamValue("newOrderNo"));
			String dealerId = logonUser.getDealerId();

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("orderYear1", orderYear1);
			map.put("orderWeek1", orderWeek1);
			map.put("orderYear2", orderYear2);
			map.put("orderWeek2", orderWeek2);
			map.put("areaId", areaId);
			map.put("groupCode", groupCode);
			map.put("orderType", orderType);
			map.put("orderStatus", orderStatus);
			map.put("orderNo", orderNo);
			map.put("newOrderNo", newOrderNo);
			map.put("dealerId", dealerId);

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
			listTemp.add("已发车量");
			list.add(listTemp);

			List<Map<String, Object>> results = DealerOrderQueryDAO.getDealerSalesOrderTotalExportList(map);
			for (int i = 0; i < results.size(); i++) {
				Map<String, Object> record = results.get(i);
				listTemp = new LinkedList<Object>();
				listTemp.add(CommonUtils.checkNull(record.get("MATERIAL_CODE")));
				listTemp.add(CommonUtils.checkNull(record.get("MATERIAL_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("CODE_DESC")));
				listTemp.add(CommonUtils.checkNull(record.get("ORDER_AMOUNT")));
				listTemp.add(CommonUtils.checkNull(record.get("CHECK_AMOUNT")));
				listTemp.add(CommonUtils.checkNull(record.get("DELIVERY_AMOUNT")));
				list.add(listTemp);
			}
			os = response.getOutputStream();
			CsvWriterUtil.writeCsv(list, os);
			os.flush();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "汇总下载");
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
	 * @Title : dealerSalesOrderDetailExport
	 * @Description: 明细下载
	 * @throws
	 * @LastUpdate :2010-6-25
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
			String orderType = CommonUtils.checkNull(request.getParamValue("orderType"));
			String orderStatus = CommonUtils.checkNull(request.getParamValue("orderStatus"));
			String orderNo = CommonUtils.checkNull(request.getParamValue("orderNo"));
			String newOrderNo = CommonUtils.checkNull(request.getParamValue("newOrderNo"));
			String dealerId = logonUser.getDealerId();

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("orderYear1", orderYear1);
			map.put("orderWeek1", orderWeek1);
			map.put("orderYear2", orderYear2);
			map.put("orderWeek2", orderWeek2);
			map.put("areaId", areaId);
			map.put("groupCode", groupCode);
			map.put("orderType", orderType);
			map.put("orderStatus", orderStatus);
			map.put("orderNo", orderNo);
			map.put("newOrderNo", newOrderNo);
			map.put("dealerId", dealerId);
			// 导出的文件名
			String fileName = "销售订单明细.csv";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

			List<List<Object>> list = new LinkedList<List<Object>>();

			List<Object> listTemp = new LinkedList<Object>();
			listTemp.add("订货方");
			listTemp.add("开票方");
			listTemp.add("订单号");
			listTemp.add("提报状态");
			listTemp.add("上级经销商订单号");
			listTemp.add("上级订单状态");
			listTemp.add("提报日期");
			listTemp.add("提报类型");
			listTemp.add("提报数量");
			listTemp.add("审核数量");
			listTemp.add("已发车量");
			list.add(listTemp);

			List<Map<String, Object>> results = DealerOrderQueryDAO.getDealerSalesOrderDetailExportList(map);
			for (int i = 0; i < results.size(); i++) {
				Map<String, Object> record = results.get(i);
				listTemp = new LinkedList<Object>();
				listTemp.add(CommonUtils.checkNull(record.get("ORDER_ORG_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("BILLING_ORG_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("ORDER_NO")));
				listTemp.add(CommonUtils.checkNull(record.get("CODE_DESC1")));
				listTemp.add(CommonUtils.checkNull(record.get("OLD_ORDER_NO")));
				listTemp.add(CommonUtils.checkNull(record.get("CODE_DESC2")));
				listTemp.add(CommonUtils.checkNull(record.get("RAISE_DATE")));
				listTemp.add(CommonUtils.checkNull(record.get("CODE_DESC")));
				listTemp.add(CommonUtils.checkNull(record.get("ORDER_AMOUNT")));
				listTemp.add(CommonUtils.checkNull(record.get("CHECK_AMOUNT")));
				listTemp.add(CommonUtils.checkNull(record.get("DELIVERY_AMOUNT")));
				list.add(listTemp);
			}
			os = response.getOutputStream();
			CsvWriterUtil.writeCsv(list, os);
			os.flush();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "明细下载");
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

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

}
