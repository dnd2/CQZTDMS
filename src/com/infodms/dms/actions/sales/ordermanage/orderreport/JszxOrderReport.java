/**
 * 
 */
package com.infodms.dms.actions.sales.ordermanage.orderreport;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.OrderReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmBusinessParaPO;
import com.infodms.dms.po.TmDateSetPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmFleetPO;
import com.infodms.dms.po.TtVsOrderCheckPO;
import com.infodms.dms.po.TtVsOrderDetailPO;
import com.infodms.dms.po.TtVsOrderPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.businessUtil.GetOrderNOUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * @author Administrator
 * 
 */
public class JszxOrderReport {
	private Logger logger = Logger.getLogger(JszxOrderReport.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final OrderReportDao dao = OrderReportDao.getInstance();

	private final String JSZX_ORDER_REPORT_QUERY_URL = "/jsp/sales/ordermanage/orderreport/jszxOrderReportQuery.jsp";// 结算中心订单提报查询页面
	private final String JSZX_ORDER_REPORT_ADD_URL = "/jsp/sales/ordermanage/orderreport/jszxOrderReportAdd.jsp";// 结算中心订单提报新增页面
	private final String JSZX_ORDER_REPORT_MOD_URL = "/jsp/sales/ordermanage/orderreport/jszxOrderReportMod.jsp";// 结算中心订单提报修改页面

	public void jszxOrderReoprtQueryPre() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Long poseId = logonUser.getPoseId();
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			List<Map<String, Object>> dateList = dao.getUrgentDateList(oemCompanyId);
			List<Map<String, Object>> areaList = MaterialGroupManagerDao
					.getJszxDealerBusiness(poseId.toString());
			TmDateSetPO dateSet = dao.getTmDateSetPO(new Date(), oemCompanyId);
			String year = dateSet != null ? dateSet.getSetYear() : "";
			String week = dateSet != null ? dateSet.getSetWeek() : "";
			String myWeek = year + "-" + week;
			act.setOutData("myWeek", myWeek);
			act.setOutData("dateList", dateList);
			act.setOutData("areaList", areaList);
			act.setForword(JSZX_ORDER_REPORT_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"补充订单提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void jszxOrderReoprtQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String dealerIds = logonUser.getDealerId();
			Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao
					.getJszxDealerBusiness(poseId.toString());
			// 业务范围字符串
			String areaIds = "";
			for (int i = 0; i < areaList.size(); i++) {
				BigDecimal temp = (BigDecimal) areaList.get(i).get("AREA_ID");
				areaIds += temp;
				if (i != areaList.size() - 1) {
					areaIds += ",";
				}
			}

			String orderWeek = CommonUtils.checkNull(request.getParamValue("orderWeek"));
			String companyId = logonUser.getOemCompanyId();
			String[] array = orderWeek.split("-");
			String year = array[0];
			String week = array[1];
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			if (!areaId.equals("")) {
				array = areaId.split("\\|");
				areaId = array[0];
			}

			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
					.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getUrgentOrderReportList(year, week, areaId,
					areaIds, dealerIds, companyId, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"补充订单提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void jszxOrderReoprtAddPre() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			// String orderWeek =
			// CommonUtils.checkNull(request.getParamValue("orderWeek"));
			// String[] array = orderWeek.split("-");
			// String year = array[0];
			// String week = array[1];

			Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao
					.getJszxDealerBusiness(poseId.toString());// 业务范围列表

			// 获得是否需要资金检查
			TmBusinessParaPO para = dao.getTmBusinessParaPO(
					Constant.URGENT_ORDER_ACCOUNT_CHECK_PARA, oemCompanyId);
			String isCheck = para.getParaValue();
			List<Map<String, Object>> dateList = dao.getUrgentDateList(oemCompanyId);
			TmDateSetPO dateSet = dao.getTmDateSetPO(new Date(), oemCompanyId);
			String year = dateSet != null ? dateSet.getSetYear() : "";
			String week = dateSet != null ? dateSet.getSetWeek() : "";
			String myWeek = year + "-" + week;
			act.setOutData("myWeek", myWeek);
			act.setOutData("dateList", dateList);
			act.setOutData("year", year);
			act.setOutData("week", week);
			act.setOutData("areaList", areaList);// 业务范围列表
			act.setOutData("isCheck", isCheck);
			act.setForword(JSZX_ORDER_REPORT_ADD_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"补充订单提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void jszxOrderReoprtAdd() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Integer orgType = logonUser.getOrgType();
			String areaId = CommonUtils.checkNull(request.getParamValue("area"));
			String[] array = areaId.split("\\|");
			areaId = array[0];
			String dealerId = array[1];
			TmDealerPO dealerPO = dao.getTmDealer(dealerId);
			String myWeek = request.getParamValue("orderWeek");
			String ourWeek[] = myWeek.split("-");
			String year = ourWeek[0];
			String week = ourWeek[1];
			String fundType = CommonUtils.checkNull(request.getParamValue("fundType"));
			String deliveryType = CommonUtils.checkNull(request.getParamValue("deliveryType"));
			String deliveryAddress = CommonUtils.checkNull(request.getParamValue("deliveryAddress"));
			String orderRemark = CommonUtils.checkNull(request.getParamValue("orderRemark"));
			String fleetId = CommonUtils.checkNull(request.getParamValue("fleetId"));
			String fleetAddress = CommonUtils.checkNull(request.getParamValue("fleetAddress"));
			String receiver = CommonUtils.checkNull(request.getParamValue("receiver")); // 收货方
			String linkMan = CommonUtils.checkNull(request.getParamValue("linkMan")); // 联系人
			String tel = CommonUtils.checkNull(request.getParamValue("tel")); // 联系电话
			String submitType = CommonUtils.checkNull(request.getParamValue("submitType")); // 提交类型
			String isCover = CommonUtils.checkNull(request.getParamValue("isCover")); // 是否代交车
			String payRemark = CommonUtils.checkNull(request.getParamValue("payRemark")); // 付款信息备注
			String priceId = CommonUtils.checkNull(request.getParamValue("priceId")); // 价格类型
			String otherPriceReason = CommonUtils.checkNull(request.getParamValue("otherPriceReason")); // 使用其他价格原因
			String totalPrice = CommonUtils.checkNull(request.getParamValue("total"));
			String productId = CommonUtils.checkNull(request.getParamValue("_productName_")) ;
			String isJSZX = CommonUtils.checkNull(request.getParamValue("isJSZX")) ;

			String returnValue = "1";
			// 校验通过时
			if (returnValue.equals("1")) {
				// 得到当前月份
				TmDateSetPO datePO = new TmDateSetPO();
				datePO.setSetYear(year);
				datePO.setSetWeek(week);
				datePO = dao.geTmDateSetPO(datePO);
				Integer month = datePO != null ? new Integer(datePO.getSetMonth()) : null;

				TtVsOrderPO po = new TtVsOrderPO();

				po.setOrderId(Long.parseLong(SequenceManager.getSequence("")));
				po.setCompanyId(GetOemcompanyId.getOemCompanyId(logonUser));
				po.setAreaId(new Long(areaId));
				po.setOrderType(Constant.ORDER_TYPE_02);
				po.setIsRefitOrder(0);
				po.setOrderYear(new Integer(year));
				po.setOrderMonth(month);
				po.setOrderWeek(new Integer(week));
				po.setOrderOrgType(orgType);
				po.setOrderOrgId(new Long(dealerId));
				po.setBillingOrgType(new Long(orgType));
				po.setBillingOrgId(dealerPO.getParentDealerD());
				po.setFundTypeId(new Long(fundType));
				po.setOrderPrice(new Double(totalPrice));
				po.setPriceId(priceId);
				po.setOtherPriceReason(otherPriceReason);
				
				if(!CommonUtils.isNullString(productId)) {
					po.setProductComboId(Long.parseLong(productId)) ;
				}
				
				Map<String, String> codeMap = GetOrderNOUtil.getAreaShortcode(areaId);
				String areaCode = codeMap.get("AREA_SHORTCODE");
				String orderNO = GetOrderNOUtil.getOrderNO(areaCode, Constant.ORDER_TYPE_02 + "", "D", dealerPO.getDealerCode());
				po.setOrderNo(orderNO);

				if ("1".equals(isCover)) {
					po.setFleetId(new Long(fleetId));
					po.setFleetAddress(fleetAddress);
					po.setIsFleet(1);
				} else {
					po.setIsFleet(0);

					po.setDeliveryType(new Integer(deliveryType));
					if (po.getDeliveryType().intValue() == Constant.TRANSPORT_TYPE_01.intValue()) {
						po.setReceiver(new Long(dealerId));
					} else {
						po.setReceiver(new Long(receiver));
						po.setDeliveryAddress(new Long(deliveryAddress));
						po.setLinkMan(linkMan);
						po.setTel(tel);
					}
				}
				po.setOrderRemark(orderRemark);
				po.setVer(new Integer(0));
				po.setCreateDate(new Date());
				po.setCreateBy(logonUser.getUserId());
				po.setPayRemark(payRemark.trim());

				// 保存时
				if (submitType.equals("1")) {
					po.setOrderStatus(Constant.ORDER_STATUS_01);
					dao.insertSalesOrder(po);
				}
				// 提报时
				else {
					po.setRaiseDate(new Date());
					po.setOrderStatus(Constant.ORDER_STATUS_10);// 预审核
					dao.insertSalesOrder(po);
				}

				Enumeration<String> enumeration = request.getParamNames();
				while (enumeration.hasMoreElements()) {
					String temp = enumeration.nextElement();
					// 客户订单车型明细保存
					if (temp.length() > 6 && temp.substring(0, 6).equals("amount")) {
						String subStr = temp.substring(6, temp.length());
						String amount = request.getParamValue(temp);
						String materialId = request.getParamValue("materialId" + subStr);
						String price = request.getParamValue("singlePrice" + subStr);

						TtVsOrderDetailPO detailPO = new TtVsOrderDetailPO();
						String orderDetailId = SequenceManager.getSequence("");
						detailPO.setDetailId(new Long(orderDetailId));
						detailPO.setOrderId(po.getOrderId());
						detailPO.setMaterialId(new Long(materialId));
						detailPO.setOrderAmount(new Integer(amount));
						detailPO.setSinglePrice(new Double(price));
						detailPO.setTotalPrice(detailPO.getOrderAmount().intValue()
								* detailPO.getSinglePrice().doubleValue());
						detailPO.setVer(new Integer(0));
						detailPO.setCreateDate(new Date());
						detailPO.setCreateBy(logonUser.getUserId());

						dao.insertSalesOrderDetail(detailPO);
					}
				}

				if (!submitType.equals("1")) {
					// 调用发运存储过程
					List<Object> ins = new LinkedList<Object>();
					ins.add(po.getOrderId().toString());
					ins.add(isJSZX);

					List<Integer> outs = new LinkedList<Integer>();
					dao.callProcedure("P_INSERTDATA_TO_DFS_JS", ins, outs);
				}
			}
			act.setOutData("returnValue", returnValue);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"补充订单提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void jszxOrderReoprtModPre() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
			String orderNO = CommonUtils.checkNull(request.getParamValue("orderNO"));
			TtVsOrderPO po = dao.getTtSalesOrder(orderId);
			String year = String.valueOf(po.getOrderYear());
			String week = String.valueOf(po.getOrderWeek());
			Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao
					.getJszxDealerBusiness(poseId.toString());

			// 查看日期配置表中当天的记录
			// TmDateSetPO dateSet = dao.getTmDateSetPO(new Date(),
			// oemCompanyId);
			// String year = dateSet != null ? dateSet.getSetYear() : "";
			// String week = dateSet != null ? dateSet.getSetWeek() : "";
			List<Map<String, Object>> detailList = dao.getSalesOrderDetailList(orderId, year, week,
					oemCompanyId.toString());
			int totalCount = 0;
			for (int i = 0; i < detailList.size(); i++) {
				Map<String, Object> map = detailList.get(i);
				BigDecimal orderAmount = (BigDecimal) map.get("ORDER_AMOUNT");
				totalCount += orderAmount.intValue();
			}
			List<Map<String, Object>> checkList = dao.getOrderCheckList(orderId);

			// 获得是否需要资金检查
			TmBusinessParaPO para = dao.getTmBusinessParaPO(
					Constant.URGENT_ORDER_ACCOUNT_CHECK_PARA, oemCompanyId);
			String isCheck = para.getParaValue();

			String fleetId = "";
			String fleetName = "";
			String fleetAddress = "";
			String isFleet = "";
			if (po.getIsFleet().intValue() == 1) {
				TmFleetPO fleet = new TmFleetPO();
				fleet.setFleetId(po.getFleetId());
				List<PO> fleetList = dao.select(fleet);
				fleet = fleetList.size() != 0 ? (TmFleetPO) fleetList.get(0) : null;

				isFleet = "1";
				fleetId = fleet.getFleetId().toString();
				fleetName = fleet.getFleetName();
				fleetAddress = po.getFleetAddress();
			}
			TmDealerPO dealer = dao.getTmDealer(po.getOrderOrgId().toString());
			String myWeek = year + "-" + week;
			act.setOutData("myWeek", myWeek);
			List<Map<String, Object>> dateList = dao.getUrgentDateList(oemCompanyId);
			act.setOutData("dateList", dateList);
			act.setOutData("orderNO", orderNO);
			act.setOutData("order", po);
			act.setOutData("areaList", areaList);// 业务范围列表
			act.setOutData("detailList", detailList);
			act.setOutData("checkList", checkList);
			act.setOutData("totalCount", totalCount);
			act.setOutData("isCheck", isCheck);
			act.setOutData("isFleet", isFleet);
			act.setOutData("fleetId", fleetId);
			act.setOutData("fleetName", fleetName);
			act.setOutData("fleetAddress", fleetAddress);
			act.setForword(JSZX_ORDER_REPORT_MOD_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"补充订单提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void jszxOrderReoprtMod() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Integer orgType = logonUser.getOrgType();
			String orderNO = CommonUtils.checkNull(request.getParamValue("orderNO"));
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
			String fundType = CommonUtils.checkNull(request.getParamValue("fundType"));
			String deliveryType = CommonUtils.checkNull(request.getParamValue("deliveryType"));
			String deliveryAddress = CommonUtils.checkNull(request.getParamValue("deliveryAddress"));
			String orderRemark = CommonUtils.checkNull(request.getParamValue("orderRemark"));
			String fleetId = CommonUtils.checkNull(request.getParamValue("fleetId"));
			String fleetAddress = CommonUtils.checkNull(request.getParamValue("fleetAddress"));
			String receiver = CommonUtils.checkNull(request.getParamValue("receiver")); // 收货方
			String linkMan = CommonUtils.checkNull(request.getParamValue("linkMan")); // 联系人
			String tel = CommonUtils.checkNull(request.getParamValue("tel")); // 联系电话
			String submitType = CommonUtils.checkNull(request.getParamValue("submitType")); // 提交类型
			String areaId = CommonUtils.checkNull(request.getParamValue("area"));
			String isCover = CommonUtils.checkNull(request.getParamValue("isCover"));
			String payRemark = CommonUtils.checkNull(request.getParamValue("payRemark")); // 付款信息备注
			String priceId = CommonUtils.checkNull(request.getParamValue("priceId")); // 价格类型
			String otherPriceReason = CommonUtils.checkNull(request.getParamValue("otherPriceReason")); // 使用其他价格原因
			String totalPrice = CommonUtils.checkNull(request.getParamValue("total"));
			String productId = CommonUtils.checkNull(request.getParamValue("_productName_")) ;
			String isJSZX = CommonUtils.checkNull(request.getParamValue("isJSZX")) ;
			String[] array = areaId.split("\\|");
			areaId = array[0];
			String dealerId = array[1];

			String returnValue = "1";

			Long order_id = Long.parseLong(orderId);

			// 校验通过时
			if (returnValue.equals("1")) {
				TtVsOrderPO condition = new TtVsOrderPO();
				condition.setOrderId(order_id);

				TtVsOrderPO value = new TtVsOrderPO();
				value.setOrderId(order_id);
				value.setOrderNo(orderNO);
				value.setAreaId(new Long(areaId));
				value.setOrderOrgType(orgType);
				value.setOrderOrgId(new Long(dealerId));
				value.setBillingOrgType(new Long(orgType));
				String parentId = dao.getParentDealerId(dealerId, Constant.DEALER_LEVEL_01);
				value.setBillingOrgId(parentId != null ? new Long(parentId) : null);
				value.setFundTypeId(new Long(fundType));
				String orderWeek = request.getParamValue("orderWeek");
				String myWeek[] = orderWeek.split("-");
				value.setOrderWeek(Integer.parseInt(myWeek[1]));
				value.setOrderYear(Integer.parseInt(myWeek[0]));
				value.setOrderPrice(new Double(totalPrice));
				value.setPriceId(priceId);
				value.setOtherPriceReason(otherPriceReason);
				
				if(!CommonUtils.isNullString(productId)) {
					value.setProductComboId(Long.parseLong(productId)) ;
				}
				
				if ("1".equals(isCover)) {
					value.setIsFleet(1);
					value.setFleetId(new Long(fleetId));
					value.setFleetAddress(fleetAddress);
				} else {
					value.setIsFleet(0);
					value.setDeliveryType(new Integer(deliveryType));

					if (value.getDeliveryType().intValue() == Constant.TRANSPORT_TYPE_01.intValue()) {
						value.setReceiver(new Long(dealerId));
					}
					if (value.getDeliveryType().intValue() == Constant.TRANSPORT_TYPE_02.intValue()) {
						value.setReceiver(new Long(receiver));
						value.setDeliveryAddress(new Long(deliveryAddress));
						value.setLinkMan(linkMan);
						value.setTel(tel);
					}
				}
				if (null != orderRemark && !"".equals(orderRemark)) {
					value.setOrderRemark(orderRemark.trim());
				} else {
					value.setOrderRemark(" ");
				}
				if (null != payRemark && !"".equals(payRemark)) {
					value.setPayRemark(payRemark.trim());
				} else {
					value.setPayRemark(" ");
				}
				value.setUpdateDate(new Date());
				value.setUpdateBy(logonUser.getUserId());
				// 保存时
				if (submitType.equals("1")) {
					value.setOrderStatus(Constant.ORDER_STATUS_01);
				}
				// 提报时
				else {
					value.setRaiseDate(new Date());
					value.setOrderStatus(Constant.ORDER_STATUS_10);// 预审核
				}

				dao.updateSalesOrder(condition, value);

				TtVsOrderDetailPO detailPO = new TtVsOrderDetailPO();
				detailPO.setOrderId(new Long(orderId));
				dao.deleteSalesOrderDetail(detailPO);
				Enumeration<String> enumeration = request.getParamNames();
				while (enumeration.hasMoreElements()) {
					String temp = enumeration.nextElement();
					// 客户订单车型明细保存
					if (temp.length() > 6 && temp.substring(0, 6).equals("amount")) {
						String subStr = temp.substring(6, temp.length());
						String amount = request.getParamValue(temp);
						String materialId = request.getParamValue("materialId" + subStr);
						String price = request.getParamValue("singlePrice" + subStr);

						detailPO = new TtVsOrderDetailPO();
						String orderDetailId = SequenceManager.getSequence("");
						detailPO.setDetailId(new Long(orderDetailId));
						detailPO.setOrderId(new Long(orderId));
						detailPO.setMaterialId(new Long(materialId));
						detailPO.setOrderAmount(new Integer(amount));
						detailPO.setSinglePrice(new Double(price));
						detailPO.setTotalPrice(detailPO.getOrderAmount().intValue()
								* detailPO.getSinglePrice().doubleValue());

						dao.insertSalesOrderDetail(detailPO);
					}
				}

				if (!submitType.equals("1")) {
					// 调用发运存储过程
					List<Object> ins = new LinkedList<Object>();
					ins.add(orderId);
					ins.add(isJSZX);

					List<Integer> outs = new LinkedList<Integer>();
					dao.callProcedure("P_INSERTDATA_TO_DFS_JS", ins, outs);
				}
			}
			act.setOutData("returnValue", returnValue);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"补充订单提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void jszxOrderReoprtDel() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));

			TtVsOrderDetailPO detail = new TtVsOrderDetailPO();
			detail.setOrderId(new Long(orderId));
			dao.delete(detail);

			TtVsOrderCheckPO check = new TtVsOrderCheckPO();
			check.setOrderId(new Long(orderId));
			dao.delete(check);

			TtVsOrderPO po = new TtVsOrderPO();
			po.setOrderId(new Long(orderId));
			dao.delete(po);
			act.setOutData("returnValue", 1);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"补充订单提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void jszxOrderReoprtSubmit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
			TtVsOrderPO condition = new TtVsOrderPO();
			TtVsOrderPO value = new TtVsOrderPO();
			condition.setOrderId(new Long(orderId));
			value.setRaiseDate(new Date());
			value.setOrderStatus(Constant.ORDER_STATUS_02);// 预审核
			value.setUpdateBy(logonUser.getUserId());
			value.setUpdateDate(new Date());
			dao.updateSalesOrder(condition, value);
			act.setOutData("returnValue", 1);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"补充订单提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
