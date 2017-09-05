/**
 * @Title: OrderCancel.java
 * 
 * @Description:
 * 
 * @Copyright: Copyright (c) 2010
 * 
 * @Company: www.infoservice.com.cn
 * @Date: 2010-6-11
 * 
 * @author yuyong
 * @mail yuyong@infoservice.com.cn
 * @version 1.0
 * @remark
 */
package com.infodms.dms.actions.sales.ordermanage.orderadjust;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.VerFlagDao;
import com.infodms.dms.dao.orgmng.GetOrgIdsOrDealerIdsDAO;
import com.infodms.dms.dao.sales.ordermanage.orderadjust.OrderAdjustDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.OrderReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDateSetPO;
import com.infodms.dms.po.TtVsAccountPO;
import com.infodms.dms.po.TtVsOrderChngPO;
import com.infodms.dms.po.TtVsOrderDetailPO;
import com.infodms.dms.po.TtVsOrderPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.DateTimeUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * @author yuyong
 * 
 */
public class OrderCancel {
	private Logger logger = Logger.getLogger(OrderCancel.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	ResponseWrapper response = act.getResponse();
	private final OrderAdjustDao dao = OrderAdjustDao.getInstance();
	private final OrderReportDao reportDao = OrderReportDao.getInstance();

	private final String ORDER_CANCEL_QUERY_URL = "/jsp/sales/ordermanage/orderadjust/orderCancelQuery.jsp";// 订单取消查询页面
	private final String ORDER_PART_CANCEL_QUERY_URL = "/jsp/sales/ordermanage/orderadjust/orderPartCancelQuery.jsp";// 订单部分取消查询页面

	/**
	 * 订单取消初始化
	 */
	public void orderCancelQueryPre() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			Long poseId = logonUser.getPoseId();
			String dutyType = logonUser.getDutyType();
			String orgId = logonUser.getOrgId().toString();
			String parentOrgId = logonUser.getParentOrgId();
			/*List<String> years = queryDao.getYearList();
			List<String> weeks = queryDao.getWeekList();*/
			List<Map<String, Object>> areaList = MaterialGroupManagerDao
					.getPoseIdBusiness(poseId.toString());
			// 获得当前日期在tm_date_set中的配置
			/*Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			TmDateSetPO dateSet = reportDao.getTmDateSetPO(new Date(),
					companyId);*/
			String dateStr = DateTimeUtil.parseDateToDate(new Date());

			act.setOutData("orgId", dutyType.equals(Constant.DUTY_TYPE_DEPT
					.toString()) ? parentOrgId : orgId);
			/*act.setOutData("years", years);
			act.setOutData("weeks", weeks);
			act.setOutData("curYear", dateSet != null ? dateSet.getSetYear()
					: "");
			act.setOutData("curWeek", dateSet != null ? dateSet.getSetWeek()
					: "");*/
			act.setOutData("dateStr", dateStr);
			act.setOutData("areaList", areaList);
			act.setForword(ORDER_CANCEL_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "订单取消");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 订单取消查询
	 */
	public void orderCancelQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			/*String orderYear1 = CommonUtils.checkNull(request
					.getParamValue("orderYear1"));
			String orderWeek1 = CommonUtils.checkNull(request
					.getParamValue("orderWeek1"));
			String orderYear2 = CommonUtils.checkNull(request
					.getParamValue("orderYear2"));
			String orderWeek2 = CommonUtils.checkNull(request
					.getParamValue("orderWeek2"));*/
			String areaId = CommonUtils.checkNull(request
					.getParamValue("areaId"));
			String dealerCode = CommonUtils.checkNull(request
					.getParamValue("dealerCode"));
			String orderType = CommonUtils.checkNull(request
					.getParamValue("orderType"));
			String orderNo = CommonUtils.checkNull(request
					.getParamValue("orderNo"));
			String startDate = CommonUtils.checkNull(request
					.getParamValue("startDate"));
			String endDate = CommonUtils.checkNull(request
					.getParamValue("endDate"));
			String orgId = logonUser.getOrgId().toString();
			String parentOrgId = logonUser.getParentOrgId();
			String dutyType = logonUser.getDutyType();
			String dealerSql = GetOrgIdsOrDealerIdsDAO.getDealerIdsForOrder(
					orgId, parentOrgId, dutyType, "TD1");
			String companyId = logonUser.getCompanyId().toString();
			// 业务范围id字符串
			Long poseId = logonUser.getPoseId();
			String areaIds = MaterialGroupManagerDao
					.getPoseIdBusinessIdStr(poseId.toString());

			Map<String, Object> map = new HashMap<String, Object>();
			/*map.put("orderYear1", orderYear1);
			map.put("orderWeek1", orderWeek1);
			map.put("orderYear2", orderYear2);
			map.put("orderWeek2", orderWeek2);*/
			map.put("areaId", areaId);
			map.put("dealerCode", dealerCode);
			map.put("orderType", orderType);
			map.put("orderNo", orderNo);
			map.put("dealerSql", dealerSql);
			map.put("companyId", companyId);
			map.put("areaIds", areaIds);
			map.put("startDate", startDate);
			map.put("endDate", endDate);

			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1;
			PageResult<Map<String, Object>> ps = dao.getOrderCancelQueryList(
					map, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "订单取消");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 订单部分取消查询
	 */
	public void orderPartCancelQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String command = CommonUtils.checkNull(request
					.getParamValue("command"));
			String orderId = CommonUtils.checkNull(request
					.getParamValue("orderId"));
			String ver = CommonUtils.checkNull(request.getParamValue("ver"));

			if (command.equals("1")) {
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1;
				PageResult<Map<String, Object>> ps = dao
						.getOrderPartCancelQueryList(orderId, curPage,
								Constant.PAGE_SIZE_MAX);
				act.setOutData("ps", ps);
			}
			act.setOutData("orderId", orderId);
			act.setOutData("ver", ver);
			act.setForword(ORDER_PART_CANCEL_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "订单取消");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 订单部分取消
	 */
	public void orderPartCancel() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			int index = Integer.parseInt(CommonUtils.checkNull(request
					.getParamValue("index")));
			String orderId = CommonUtils.checkNull(request
					.getParamValue("orderId"));
			String oldVer = CommonUtils.checkNull(request
					.getParamValue("oldVer"));
			String dealerId = CommonUtils.checkNull(request
					.getParamValue("dealerId"));
			String chngReason = CommonUtils.checkNull(request
					.getParamValue("chngReason"));

			// 获得订单po
			TtVsOrderPO order = reportDao.getTtSalesOrder(orderId);

			// 当前日期po
			/*Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			TmDateSetPO dateSet = reportDao.getTmDateSetPO(new Date(),
					companyId);*/
			boolean verFlag = true;
			verFlag = VerFlagDao.getVerFlag("TT_VS_ORDER", "ORDER_ID", orderId,
					oldVer);
			if (verFlag) {
				for (int m = 0; m < index; m++) {
					String detailId = CommonUtils.checkNull(request
							.getParamValue("detailId" + (m + 1)));
					String ver = CommonUtils.checkNull(request
							.getParamValue("ver" + (m + 1)));
					boolean verFlagTmp = VerFlagDao.getVerFlag(
							"TT_VS_ORDER_DETAIL", "DETAIL_ID", detailId, ver);
					if (verFlagTmp == false) {
						verFlag = false;
					}
				}
				if (verFlag) {
					for (int i = 0; i < index; i++) {
						String detailId = CommonUtils.checkNull(request
								.getParamValue("detailId" + (i + 1)));
						String chngAmt = CommonUtils.checkNull(request
								.getParamValue("chngAmt" + (i + 1)));
						String ver = CommonUtils.checkNull(request
								.getParamValue("ver" + (i + 1)));

						// 获得订单明细po
						TtVsOrderDetailPO detail = reportDao
								.getTtSalesOrderDetail(detailId);
						if (detail != null && Integer.parseInt(chngAmt) != 0) {
							// 释放可利用资源
							//resourceRelease(order, detail, dateSet, chngAmt,logonUser);

							// 更新订单明细表
							orderDetailUpdate(order, detail, ver, chngAmt,
									logonUser.getUserId());

							// 释放资金
							//accountRelease(order, detail, chngAmt);

							// 新增订单修改记录
							TtVsOrderChngPO chngPO = new TtVsOrderChngPO();
							chngPO.setChngId(new Long(SequenceManager
									.getSequence("")));
							chngPO.setChngType(Constant.ORDER_CHNG_TYPE_01);
							chngPO.setOrderId(detail.getOrderId());
							chngPO.setDetailId(detail.getDetailId());
							chngPO.setOldDealerId(new Long(dealerId));
							chngPO.setChngDate(new Date());
							chngPO.setChngUserId(logonUser.getUserId());
							chngPO.setChngPositionId(logonUser.getPoseId());
							chngPO.setChngOrgId(logonUser.getOrgId());
							chngPO.setChngReason(chngReason);
							chngPO.setChngAmt(new Integer(chngAmt));
							chngPO.setCreateBy(logonUser.getUserId());
							chngPO.setCreateDate(new Date());
							dao.insert(chngPO);
						}
					}
					act.setOutData("returnValue", 1);
				} else {
					act.setOutData("returnValue", 2);
				}
			} else {
				act.setOutData("returnValue", 2);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "订单取消");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 */
	public void orderCancel() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			// 当前日期po
			/*Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			TmDateSetPO dateSet = reportDao.getTmDateSetPO(new Date(),
					companyId);*/

			String chngReason = CommonUtils.checkNull(request
					.getParamValue("chngReason"));
			String orderIds = request.getParamValue("orderIds");
			String vers = request.getParamValue("vers");
			String[] ids = orderIds.split(",");
			String[] ver = vers.split(",");
			boolean flag = true;
			boolean verFlag = true;
			for (int m = 0; m < ids.length; m++) {
				boolean verFlagTmp = VerFlagDao.getVerFlag("TT_VS_ORDER",
						"ORDER_ID", ids[m], ver[m]);
				if (verFlagTmp == false) {
					verFlag = false;
				}
			}
			if (verFlag) {
				if (ids != null && ids.length != 0) {
					for (int i = 0; i < ids.length; i++) {
						Long id = new Long(ids[i]);
						Integer oldver = new Integer(ver[i]);
						TtVsOrderPO orderPO = reportDao.getTtSalesOrder(id
								.toString());

						TtVsOrderDetailPO detailPO = new TtVsOrderDetailPO();
						detailPO.setOrderId(id);
						List<PO> details = dao.select(detailPO);
						for (int j = 0; j < details.size(); j++) {
							detailPO = (TtVsOrderDetailPO) details.get(j);

							/*int tempAmt = detailPO.getDeliveryAmount()
									.intValue()
									+ detailPO.getMatchAmount().intValue();*/
							if (detailPO.getCallAmount().intValue() > 0) {
								flag = false;
							}

							int chngAmt = detailPO.getCheckAmount().intValue()
									- detailPO.getCallAmount().intValue();

							// 释放可利用资源
							/*resourceRelease(orderPO, detailPO, dateSet, Integer
									.toString(chngAmt),logonUser);*/

							// 更新订单明细表
							orderDetailUpdate(orderPO, detailPO, detailPO
									.getVer().toString(), Integer
									.toString(chngAmt), logonUser.getUserId());

							// 释放资金
							/*accountRelease(orderPO, detailPO, Integer
									.toString(chngAmt));*/

							// 新增订单修改记录
							TtVsOrderChngPO chngPO = new TtVsOrderChngPO();
							chngPO.setChngId(new Long(SequenceManager
									.getSequence("")));
							chngPO.setChngType(Constant.ORDER_CHNG_TYPE_01);
							chngPO.setOrderId(detailPO.getOrderId());
							chngPO.setDetailId(detailPO.getDetailId());
							chngPO.setOldDealerId(orderPO.getOrderOrgId());
							chngPO.setChngDate(new Date());
							chngPO.setChngUserId(logonUser.getUserId());
							chngPO.setChngPositionId(logonUser.getPoseId());
							chngPO.setChngOrgId(logonUser.getOrgId());
							chngPO.setChngReason(chngReason);
							chngPO.setChngAmt(chngAmt);
							chngPO.setCreateBy(logonUser.getUserId());
							chngPO.setCreateDate(new Date());
							dao.insert(chngPO);
						}

						// 更细订单表，设置订单状态为已取消
						TtVsOrderPO orderCondition = new TtVsOrderPO();
						orderCondition.setOrderId(id);

						TtVsOrderPO orderValue = new TtVsOrderPO();
						if (flag) {
							orderValue.setOrderStatus(Constant.ORDER_STATUS_06);// 已取消
						}
						orderValue.setUpdateBy(logonUser.getUserId());
						orderValue.setUpdateDate(new Date());
						orderValue.setVer(oldver + 1);

						dao.update(orderCondition, orderValue);
					}
				}
				act.setOutData("returnValue", 1);
			} else {
				act.setOutData("returnValue", 2);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "订单取消");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	// 资源释放
	public void resourceRelease(TtVsOrderPO orderPo,
			TtVsOrderDetailPO detailPo, TmDateSetPO datePo, String chngAmt, AclUserBean logonUser) {
		dao.resourceRelease(orderPo, detailPo, datePo, chngAmt, logonUser);
	}

	// 订单明细更新
	public void orderDetailUpdate(TtVsOrderPO orderPo,
			TtVsOrderDetailPO detailPo, String ver, String chngAmt, Long userId) {
		int changeAmount = Integer.parseInt(chngAmt);// 取消数量

		TtVsOrderDetailPO condition = new TtVsOrderDetailPO();
		condition.setDetailId(detailPo.getDetailId());

		TtVsOrderDetailPO value = new TtVsOrderDetailPO();
		value.setCheckAmount(new Integer(detailPo.getCheckAmount().intValue()
				- changeAmount));// 审核数量

		/*int tempAmont = changeAmount
				- (detailPo.getCheckAmount().intValue() - detailPo
						.getCallAmount().intValue());
		if (tempAmont > 0) {
			value.setCallAmount(new Integer(detailPo.getCallAmount().intValue()
					- tempAmont));// 申请发运数量
		}*/
		value.setVer(Integer.parseInt(ver) + 1);
		value.setUpdateBy(userId);
		value.setUpdateDate(new Date());

		dao.update(condition, value);
	}

	// 账户资金释放
	public void accountRelease(TtVsOrderPO orderPo, TtVsOrderDetailPO detailPo,
			String chngAmt) {
		int changeAmount = Integer.parseInt(chngAmt);// 取消数量

		TtVsAccountPO po = new TtVsAccountPO();
		po.setDealerId(orderPo.getOrderOrgId());
		po.setAccountTypeId(orderPo.getFundTypeId());
		List<PO> list = dao.select(po);
		po = list.size() != 0 ? (TtVsAccountPO) list.get(0) : null;

		if (po != null) {
			TtVsAccountPO condition = new TtVsAccountPO();
			condition.setAccountId(po.getAccountId());

			TtVsAccountPO value = new TtVsAccountPO();
			int tempAmont = changeAmount;// 实际需要释放资金的数量
			if (orderPo.getOrderType().intValue() == Constant.ORDER_TYPE_01
					.intValue()) {
				tempAmont = changeAmount
						- (detailPo.getCheckAmount().intValue() - detailPo
								.getCallAmount().intValue());
			}

			if (tempAmont > 0) {
				value.setFreezeAmount(new Double(po.getFreezeAmount()
						.doubleValue()
						- detailPo.getSinglePrice().doubleValue() * tempAmont));// 冻结额度
				value.setAvailableAmount(new Double(po.getAvailableAmount()
						.doubleValue()
						+ detailPo.getSinglePrice().doubleValue() * tempAmont));// 可用额度

			}

			dao.update(condition, value);
		}
	}
}
