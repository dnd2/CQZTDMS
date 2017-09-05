package com.infodms.dms.actions.sales.balancecentermanage.stockordermanage;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.OrderReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmBusinessParaPO;
import com.infodms.dms.po.TmDateSetPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmFleetPO;
import com.infodms.dms.po.TtVsAccountPO;
import com.infodms.dms.po.TtVsDlvryReqDtlPO;
import com.infodms.dms.po.TtVsDlvryReqPO;
import com.infodms.dms.po.TtVsOrderDetailPO;
import com.infodms.dms.po.TtVsOrderPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.businessUtil.GetOrderNOUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.ActionContextExtend;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/*
 * 结算中心：采购订单提报 
 * */
public class StockOrderReport extends BaseDao {
	public Logger logger = Logger.getLogger(StockOrderReport.class);
	private ActionContext act = ActionContext.getContext();
	private static final StockOrderReport dao = new StockOrderReport();

	public static final StockOrderReport getInstance() {
		return dao;
	}

	private final OrderReportDao reportDao = OrderReportDao.getInstance();
	private final String stockOrderReportInitUrl = "/jsp/sales/balancecentermanage/stockordermanage/stockOrderReportInit.jsp";
	private final String stockOrderAddInitUrl = "/jsp/sales/balancecentermanage/stockordermanage/stockOrderAddInit.jsp";
	private final String stockOrderModPreURL = "/jsp/sales/balancecentermanage/stockordermanage/stockOrderModInit.jsp";

	/**
	 * FUNCTION : 采购订单提报面初始化
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-22
	 */
	public void stockOrderReportInit() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			Long poseId = logonUser.getPoseId();
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			List<Map<String, Object>> dateList = reportDao
					.getUrgentDateList(companyId);
			List<Map<String, Object>> areaList = MaterialGroupManagerDao
					.getDealerBusiness(poseId.toString());
			act.setOutData("dateList", dateList);
			act.setOutData("areaList", areaList);
			act.setForword(stockOrderReportInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "采购订单提报面初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * FUNCTION : 采购订单:新增PRE
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-22
	 */
	public void stockOrderAddPre() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			RequestWrapper request = act.getRequest();
			String orderWeek = CommonUtils.checkNull(request
					.getParamValue("orderWeek"));
			String[] array = orderWeek.split("-");
			String year = array[0];
			String week = array[1];

			Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao
					.getDealerBusiness(poseId.toString());
			//List<Map<String, Object>> fundTypeList = reportDao
					//.getFundTypeList();
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			// 获得是否需要资金检查
			TmBusinessParaPO para = reportDao.getTmBusinessParaPO(
					Constant.URGENT_ORDER_ACCOUNT_CHECK_PARA, companyId);
			String isCheck = para.getParaValue();

			act.setOutData("year", year);
			act.setOutData("week", week);
			act.setOutData("areaList", areaList);// 业务范围列表
			//act.setOutData("fundTypeList", fundTypeList);// 资金类型列表
			act.setOutData("isCheck", isCheck);
			act.setForword(stockOrderAddInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "采购订单提报面初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * FUNCTION : 采购订单:新增
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-22
	 */
	public void stockOrderReportAdd() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			RequestWrapper request = act.getRequest();
			Integer orgType = logonUser.getOrgType();
			String areaId = CommonUtils
					.checkNull(request.getParamValue("area"));
			String[] array = areaId.split("\\|");
			areaId = array[0];
			String dealerId = array[1];
			TmDealerPO dealerPO = reportDao.getTmDealer(dealerId);

			String year = CommonUtils.checkNull(request.getParamValue("year"));
			String week = CommonUtils.checkNull(request.getParamValue("week"));
			String fundType = CommonUtils.checkNull(request.getParamValue("fundType"));
			String totalPrice = CommonUtils.checkNull(request.getParamValue("total"));
			String deliveryType = CommonUtils.checkNull(request.getParamValue("deliveryType"));
			String deliveryAddress = CommonUtils.checkNull(request.getParamValue("deliveryAddress"));
			String orderRemark = CommonUtils.checkNull(request.getParamValue("orderRemark"));
			String fleetId = CommonUtils.checkNull(request.getParamValue("fleetId"));
			String fleetAddress = CommonUtils.checkNull(request.getParamValue("fleetAddress"));
			String priceId = CommonUtils.checkNull(request.getParamValue("priceId"));// 价格类型
			String otherPriceReason = CommonUtils.checkNull(request.getParamValue("otherPriceReason"));// 使用其他价格原因
			String discount = CommonUtils.checkNull(request.getParamValue("discount"));// 使用折让
			String receiver = CommonUtils.checkNull(request.getParamValue("receiver"));// 收货方
			String linkMan = CommonUtils.checkNull(request.getParamValue("linkMan"));// 联系人
			String tel = CommonUtils.checkNull(request.getParamValue("tel"));// 联系电话
			String submitType = CommonUtils.checkNull(request.getParamValue("submitType"));// 提交类型
			String isCover = CommonUtils.checkNull(request.getParamValue("isCover"));					// 是否代交车
			String totalAmount_ = CommonUtils.checkNull(request.getParamValue("totalAmount_"));
			String total = CommonUtils.checkNull(request.getParamValue("total"));						//订单总价
			
			String returnValue = "1";
			// 选提报时进行资金校验
			if (submitType.equals("2")) {
				if (dealerPO.getDealerLevel().intValue() == Constant.DEALER_LEVEL_01
						.intValue()) {
					// 获得是否需要资金检查
					TmBusinessParaPO para = reportDao.getTmBusinessParaPO(
							Constant.URGENT_ORDER_ACCOUNT_CHECK_PARA, new Long(
									logonUser.getOemCompanyId()));
					String isCheck = para.getParaValue();
					if (isCheck.equals("0")) {
						TtVsAccountPO accountPO = new TtVsAccountPO();
						accountPO.setDealerId(new Long(dealerId));
						accountPO.setAccountTypeId(new Long(fundType));
						accountPO = reportDao.geTtVsAccountPO(accountPO);
						if (accountPO != null) {
							if (accountPO.getAvailableAmount().doubleValue() < Double
									.parseDouble(totalPrice)) {
								returnValue = "2";
							} else {
								TtVsAccountPO condition = new TtVsAccountPO();
								condition
										.setAccountId(accountPO.getAccountId());

								TtVsAccountPO value = new TtVsAccountPO();
								value.setFreezeAmount(new Double(accountPO
										.getFreezeAmount().doubleValue()
										+ Double.parseDouble(totalPrice)));// 冻结额度
								value.setAvailableAmount(new Double(accountPO
										.getAvailableAmount().doubleValue()
										- Double.parseDouble(totalPrice)));// 可用额度

								dao.update(condition, value);
							}
						} else {
							returnValue = "2";
						}
					}
				}
			}

			Long order_id = Long.parseLong(SequenceManager.getSequence(""));
			Long req_id = Long.parseLong(SequenceManager.getSequence(""));
			Map<String,String> infoMap = new HashMap<String, String>();
			infoMap.put("area_id", areaId);
			infoMap.put("order_id", order_id+"");
			infoMap.put("fund_type", fundType);
			infoMap.put("delivery_type", deliveryType);
			infoMap.put("address_id", deliveryAddress);
			infoMap.put("isFleet", isCover);
			infoMap.put("fleet_id", fleetId);
			infoMap.put("fleet_address", fleetAddress);
			infoMap.put("req_total_amount", totalAmount_);
			infoMap.put("price_id", priceId);
			infoMap.put("other_price_reason", otherPriceReason);
			infoMap.put("receiver", receiver);
			infoMap.put("link_man", linkMan);
			infoMap.put("tel", tel);
			infoMap.put("total", total);
			// 校验通过时
			if (returnValue.equals("1")) {
				// 得到当前月份
				TmDateSetPO datePO = new TmDateSetPO();
				datePO.setSetYear(year);
				datePO.setSetWeek(week);
				datePO = reportDao.geTmDateSetPO(datePO);
				Integer month = datePO != null ? new Integer(datePO
						.getSetMonth()) : null;

				TtVsOrderPO po = new TtVsOrderPO();
				
				po.setOrderId(new Long(SequenceManager.getSequence("")));
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
				/*String parentId = reportDao.getParentDealerId(dealerId,
						Constant.DEALER_LEVEL_01);
				po
						.setBillingOrgId(parentId != null ? new Long(parentId)
								: null);*/
				TmDealerPO tmDealerPO = new TmDealerPO();
				if (dealerPO.getDealerLevel().intValue() == Constant.DEALER_LEVEL_01
						.intValue()) {
					Long billingOrgId = dealerPO.getDealerId();
					tmDealerPO.setDealerId(Long.parseLong(dealerId));
					List dealerList = dao.select(tmDealerPO);
					String dealerCode = ((TmDealerPO)dealerList.get(0)).getDealerCode();
					Map<String,String> codeMap  = GetOrderNOUtil.getAreaShortcode(areaId);
					String areaCode = codeMap.get("AREA_SHORTCODE");
					String orderNO = GetOrderNOUtil.getOrderNO(areaCode,Constant.ORDER_TYPE_02+"", "D", dealerCode);
					po.setOrderNo(orderNO);
					po.setBillingOrgId(billingOrgId);
					po.setFundTypeId(new Long(fundType));
					po.setPriceId(priceId);
					po.setOtherPriceReason(otherPriceReason);
				}
				else{
					Long billingOrgId = dealerPO.getParentDealerD();
					
					tmDealerPO.setDealerId(Long.parseLong(dealerId));
					List dealerList = dao.select(tmDealerPO);
					String dealerCode = ((TmDealerPO)dealerList.get(0)).getDealerCode();
					Map<String,String> codeMap  = GetOrderNOUtil.getAreaShortcode(areaId);
					String areaCode = codeMap.get("AREA_SHORTCODE");
					String orderNO = GetOrderNOUtil.getOrderNO(areaCode,Constant.ORDER_TYPE_02+"", "D", dealerCode);
					po.setOrderNo(orderNO);
					po.setBillingOrgId(billingOrgId);
				}
				po.setOrderPrice(new Double(totalPrice));
				if (null != deliveryType && !"".equals(deliveryType)) {
					po.setDeliveryType(new Integer(deliveryType));
				}
				if (po.getDeliveryType().intValue() == Constant.TRANSPORT_TYPE_02.intValue()) {
					po.setReceiver(new Long(receiver));
					po.setDeliveryAddress(new Long(deliveryAddress));
					po.setLinkMan(linkMan);
					po.setTel(tel);
//				} else if (po.getDeliveryType().intValue() == Constant.TRANSPORT_TYPE_03.intValue()) {
				}
				if("1".equals(isCover)){	
					po.setFleetId(new Long(fleetId));
					po.setFleetAddress(fleetAddress);
					po.setIsFleet(1);
				} else {
					po.setReceiver(po.getBillingOrgId());
				}
				po.setOrderRemark(orderRemark);
				po.setVer(new Integer(0));
				po.setCreateDate(new Date());
				po.setCreateBy(logonUser.getUserId());
//				po.setDiscount(new Float(discount));

				// 保存时
				if (submitType.equals("1")) {
					po.setOrderStatus(Constant.ORDER_STATUS_01);
					reportDao.insertSalesOrder(po);
				}
				// 提报时
				else {
					po.setRaiseDate(new Date());
					if (dealerPO.getDealerLevel().intValue() == Constant.DEALER_LEVEL_01
							.intValue()) {
						po.setOrderStatus(Constant.ORDER_STATUS_07);// 财务待确认
						reportDao.insertSalesOrder(po);
					} else {
						po.setOrderStatus(Constant.ORDER_STATUS_02);// 预审核
						reportDao.insertSalesOrder(po);
					}
				}
				

				Enumeration<String> enumeration = request.getParamNames();
				while (enumeration.hasMoreElements()) {
					String temp = enumeration.nextElement();
					// 客户订单车型明细保存
					if (temp.length() > 6
							&& temp.substring(0, 6).equals("amount")) {
						String subStr = temp.substring(6, temp.length());
						String amount = request.getParamValue(temp);
						String materialId = request.getParamValue("materialId"+ subStr);
						String price = request.getParamValue("singlePrice"+ subStr);
						String discount_rate = request.getParamValue("discount_rate"+subStr);			//折扣率
						String discount_s_price_ = request.getParamValue("discount_s_price_"+subStr);	//折扣后单价
						String discount_price_ = request.getParamValue("discount_price_"+subStr);		//折扣金额

						TtVsOrderDetailPO detailPO = new TtVsOrderDetailPO();
						String orderDetailId = SequenceManager.getSequence("");
						detailPO.setDetailId(new Long(orderDetailId));
						detailPO.setOrderId(po.getOrderId());
						detailPO.setMaterialId(new Long(materialId));
						detailPO.setOrderAmount(new Integer(amount));
						detailPO.setCallAmount(new Integer(amount));
						detailPO.setSinglePrice(new Double(price));
						double totalPrice_ = 0;
						if (dealerPO.getDealerLevel().intValue() == Constant.DEALER_LEVEL_01.intValue()) {
							totalPrice_ = detailPO.getOrderAmount().intValue()* Double.parseDouble(discount_s_price_);
						}else{
							totalPrice_ = detailPO.getOrderAmount().intValue()* detailPO.getSinglePrice().doubleValue();
						}
						detailPO.setTotalPrice(new Double(totalPrice_));
						detailPO.setVer(new Integer(0));
						detailPO.setCreateDate(new Date());
						detailPO.setCreateBy(logonUser.getUserId());
						if (null != discount_rate && !"".equals(discount_rate)) {
							detailPO.setDiscountRate(Float.parseFloat(discount_rate));
							detailPO.setDiscountSPrice(Double.parseDouble(discount_s_price_));
							detailPO.setDiscountPrice(Double.parseDouble(discount_price_));
						}
						reportDao.insertSalesOrderDetail(detailPO);
					}
				}
			}
			act.setOutData("returnValue", returnValue);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "采购订单提报面初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * FUNCTION : 采购订单:修改PRE
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-22
	 */
	public void stockOrderModPre() {
		AclUserBean logonUser = null;
		try {
			  ActionContextExtend atx = (ActionContextExtend) ActionContext.getContext();
				String reqURL = atx.getRequest().getContextPath();
				if("/CVS-SALES".equals(reqURL.toUpperCase())){
					act.setOutData("returnValue", 1);
				}else{
					act.setOutData("returnValue", 2);
				}
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			RequestWrapper request = act.getRequest();
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			String orderId = CommonUtils.checkNull(request
					.getParamValue("orderId"));
			TtVsOrderPO po = reportDao.getTtSalesOrder(orderId);

			Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao
					.getDealerBusiness(poseId.toString());
			//List<Map<String, Object>> fundTypeList = reportDao
					//.getFundTypeList();

			// 查看日期配置表中当天的记录
			TmDateSetPO dateSet = reportDao.getTmDateSetPO(new Date(),
					oemCompanyId);
			String year = dateSet != null ? dateSet.getSetYear() : "";
			String week = dateSet != null ? dateSet.getSetWeek() : "";
			List<Map<String, Object>> detailList = reportDao
					.getSalesOrderDetailList(orderId, year, week, oemCompanyId.toString());
			int totalCount = 0;
			for (int i = 0; i < detailList.size(); i++) {
				Map<String, Object> map = detailList.get(i);
				BigDecimal orderAmount = (BigDecimal) map.get("ORDER_AMOUNT");
				totalCount += orderAmount.intValue();
			}
			List<Map<String, Object>> checkList = reportDao
					.getOrderCheckList(orderId);

			// 获得是否需要资金检查
			TmBusinessParaPO para = reportDao.getTmBusinessParaPO(
					Constant.URGENT_ORDER_ACCOUNT_CHECK_PARA, oemCompanyId);
			String isCheck = para.getParaValue();

			String fleetId = "";
			String fleetName = "";
			String fleetAddress = "";
			String isFleet = "";
			// 运送方式为代交车时，查询集团客户
//			if (po.getDeliveryType().intValue() == Constant.TRANSPORT_TYPE_03) {
			if (po.getIsFleet().intValue() == 1) {
				TmFleetPO fleet = new TmFleetPO();
				fleet.setFleetId(po.getFleetId());
				List<PO> fleetList = dao.select(fleet);
				fleet = fleetList.size() != 0 ? (TmFleetPO) fleetList.get(0)
						: null;
				isFleet = "1";
				fleetId = fleet.getFleetId().toString();
				fleetName = fleet.getFleetName();
				fleetAddress = po.getFleetAddress();
			}

			TmDealerPO dealer = reportDao.getTmDealer(po.getOrderOrgId().toString());

			act.setOutData("order", po);
			act.setOutData("areaList", areaList);// 业务范围列表
			//act.setOutData("fundTypeList", fundTypeList);// 资金类型列表
			act.setOutData("detailList", detailList);
			act.setOutData("checkList", checkList);
			act.setOutData("totalCount", totalCount);
			act.setOutData("isCheck", isCheck);
			act.setOutData("isFleet", isFleet);
			act.setOutData("fleetId", fleetId);
			act.setOutData("fleetName", fleetName);
			act.setOutData("fleetAddress", fleetAddress);
			act.setOutData(
							"level",
							dealer.getDealerLevel().intValue() == Constant.DEALER_LEVEL_01
									.intValue() ? "1" : "2");
			act.setForword(stockOrderModPreURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "采购订单提报面初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * FUNCTION : 采购订单:修改
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-22
	 */
	public void stockOrderReportMod() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			RequestWrapper request = act.getRequest();
			Integer orgType = logonUser.getOrgType();
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
			String fundType = CommonUtils.checkNull(request.getParamValue("fundType"));
			String totalPrice = CommonUtils.checkNull(request.getParamValue("total"));
			String deliveryType = CommonUtils.checkNull(request.getParamValue("deliveryType"));
			String deliveryAddress = CommonUtils.checkNull(request.getParamValue("deliveryAddress"));
			String orderRemark = CommonUtils.checkNull(request.getParamValue("orderRemark"));
			String fleetId = CommonUtils.checkNull(request.getParamValue("fleetId"));
			String fleetAddress = CommonUtils.checkNull(request.getParamValue("fleetAddress"));
			String priceId = CommonUtils.checkNull(request.getParamValue("priceId"));// 价格类型
			String otherPriceReason = CommonUtils.checkNull(request.getParamValue("otherPriceReason"));// 使用其他价格原因
			String discount = CommonUtils.checkNull(request.getParamValue("discount"));// 使用折让
			String receiver = CommonUtils.checkNull(request.getParamValue("receiver"));// 收货方
			String linkMan = CommonUtils.checkNull(request.getParamValue("linkMan"));// 联系人
			String tel = CommonUtils.checkNull(request.getParamValue("tel"));// 联系电话
			String submitType = CommonUtils.checkNull(request.getParamValue("submitType"));// 提交类型
			String totalAmount_ = CommonUtils.checkNull(request.getParamValue("totalAmount_"));
			String areaId = CommonUtils.checkNull(request.getParamValue("area"));
			String isCover = CommonUtils.checkNull(request.getParamValue("isCover"));
			String total = CommonUtils.checkNull(request.getParamValue("total")); //订单总价

			String[] array = areaId.split("\\|");
			areaId = array[0];
			String dealerId = array[1];
			TmDealerPO dealerPO = reportDao.getTmDealer(dealerId);

			String returnValue = "1";
			// 选提报时进行资金校验
			if (submitType.equals("2")) {
				if (dealerPO.getDealerLevel().intValue() == Constant.DEALER_LEVEL_01
						.intValue()) {
					// 获得是否需要资金检查
					TmBusinessParaPO para = reportDao.getTmBusinessParaPO(
							Constant.URGENT_ORDER_ACCOUNT_CHECK_PARA, new Long(
									logonUser.getOemCompanyId()));
					String isCheck = para.getParaValue();
					if (isCheck.equals("0")) {
						TtVsAccountPO accountPO = new TtVsAccountPO();
						accountPO.setDealerId(new Long(dealerId));
						accountPO.setAccountTypeId(new Long(fundType));
						accountPO = reportDao.geTtVsAccountPO(accountPO);
						if (accountPO != null) {
							if (accountPO.getAvailableAmount().doubleValue() < Double
									.parseDouble(totalPrice)) {
								returnValue = "2";
							} else {
								TtVsAccountPO condition = new TtVsAccountPO();
								condition
										.setAccountId(accountPO.getAccountId());

								TtVsAccountPO value = new TtVsAccountPO();
								value.setFreezeAmount(new Double(accountPO
										.getFreezeAmount().doubleValue()
										+ Double.parseDouble(totalPrice)));// 冻结额度
								value.setAvailableAmount(new Double(accountPO
										.getAvailableAmount().doubleValue()
										- Double.parseDouble(totalPrice)));// 可用额度

								dao.update(condition, value);
							}
						} else {
							returnValue = "2";
						}
					}
				}
			}
			Long order_id = Long.parseLong(orderId);
			Long req_id = Long.parseLong(SequenceManager.getSequence(""));
			Map<String,String> infoMap = new HashMap<String, String>();
			infoMap.put("area_id", areaId);
			infoMap.put("order_id", order_id+"");
			infoMap.put("fund_type", fundType);
			infoMap.put("delivery_type", deliveryType);
			infoMap.put("address_id", deliveryAddress);
			infoMap.put("isFleet", isCover);
			infoMap.put("fleet_id", fleetId);
			infoMap.put("fleet_address", fleetAddress);
			infoMap.put("req_total_amount", totalAmount_);
			infoMap.put("price_id", priceId);
			infoMap.put("other_price_reason", otherPriceReason);
			infoMap.put("receiver", receiver);
			infoMap.put("link_man", linkMan);
			infoMap.put("tel", tel);
			infoMap.put("total", total);
			// 校验通过时
			if (returnValue.equals("1")) {
				TtVsOrderPO condition = new TtVsOrderPO();
				condition.setOrderId(new Long(orderId));

				TtVsOrderPO value = new TtVsOrderPO();
				value.setAreaId(new Long(areaId));
				value.setOrderOrgType(orgType);
				value.setOrderOrgId(new Long(dealerId));
				value.setBillingOrgType(new Long(orgType));
				String parentId = reportDao.getParentDealerId(dealerId,
						Constant.DEALER_LEVEL_01);
				value.setBillingOrgId(parentId != null ? new Long(parentId)
						: null);
				if (dealerPO.getDealerLevel().intValue() == Constant.DEALER_LEVEL_01
						.intValue()) {
					value.setFundTypeId(new Long(fundType));
					value.setPriceId(priceId);
					if (null != otherPriceReason && !"".equals(otherPriceReason)) {
						value.setOtherPriceReason(otherPriceReason.trim());
					}
				}
				value.setOrderPrice(new Double(totalPrice));
				if (null != deliveryType && !"".equals(deliveryType)) {
					value.setDeliveryType(new Integer(deliveryType));
				}
				if (value.getDeliveryType().intValue() == Constant.TRANSPORT_TYPE_02
						.intValue()) {
					value.setReceiver(new Long(receiver));
					value.setDeliveryAddress(new Long(deliveryAddress));
					value.setLinkMan(linkMan);
					value.setTel(tel);
//				} else if (value.getDeliveryType().intValue() == Constant.TRANSPORT_TYPE_03.intValue()) {
				}
				if("1".equals(isCover)){	
					value.setIsFleet(1);
					value.setFleetId(new Long(fleetId));
					value.setFleetAddress(fleetAddress);
				} else {
					value.setReceiver(value.getBillingOrgId());
					value.setIsFleet(0);
				}
				if (null != orderRemark && !"".equals(orderRemark)) {
					value.setOrderRemark(orderRemark.trim());
				}
				value.setUpdateDate(new Date());
				value.setUpdateBy(logonUser.getUserId());
//				value.setDiscount(new Float(discount));

				// 保存时
				if (submitType.equals("1")) {
					value.setOrderStatus(Constant.ORDER_STATUS_01);
				}
				// 提报时
				else {
					value.setRaiseDate(new Date());
					if (dealerPO.getDealerLevel().intValue() == Constant.DEALER_LEVEL_01
							.intValue()) {
						value.setOrderStatus(Constant.ORDER_STATUS_07);// 财务待确认
					} else {
						value.setOrderStatus(Constant.ORDER_STATUS_02);// 预审核
					}
				}

				reportDao.updateSalesOrder(condition, value);

				TtVsOrderDetailPO detailPO = new TtVsOrderDetailPO();
				detailPO.setOrderId(new Long(orderId));
				reportDao.deleteSalesOrderDetail(detailPO);
				Enumeration<String> enumeration = request.getParamNames();
				while (enumeration.hasMoreElements()) {
					String temp = enumeration.nextElement();
					// 客户订单车型明细保存
					if (temp.length() > 6
							&& temp.substring(0, 6).equals("amount")) {
						String subStr = temp.substring(6, temp.length());
						String amount = request.getParamValue(temp);
						String materialId = request.getParamValue("materialId"+ subStr);
						String price = request.getParamValue("singlePrice"+ subStr);
						String discount_rate = request.getParamValue("discount_rate"+ subStr);		//折扣率
						String discount_s_price = request.getParamValue("discount_s_price"+ subStr);//折扣后单价
						String discount_price = request.getParamValue("discount_price"+ subStr);	//折扣额
						detailPO = new TtVsOrderDetailPO();
						String orderDetailId = SequenceManager.getSequence("");
						detailPO.setDetailId(new Long(orderDetailId));
						detailPO.setOrderId(new Long(orderId));
						detailPO.setMaterialId(new Long(materialId));
						detailPO.setOrderAmount(new Integer(amount));
						detailPO.setSinglePrice(new Double(price));
						double totalPrice_ = 0;
						if (dealerPO.getDealerLevel().intValue() == Constant.DEALER_LEVEL_01.intValue()) {
							totalPrice_ = detailPO.getOrderAmount().intValue()* Double.parseDouble(discount_s_price);
						}else{
							totalPrice_ = detailPO.getOrderAmount().intValue()* detailPO.getSinglePrice().doubleValue();
						}
						detailPO.setTotalPrice(new Double(totalPrice_));
						detailPO.setVer(new Integer(0));
						detailPO.setCreateDate(new Date());
						detailPO.setCreateBy(logonUser.getUserId());
						detailPO.setDiscountRate(Float.parseFloat(discount_rate));
						detailPO.setDiscountSPrice(Double.parseDouble(discount_s_price));
						detailPO.setDiscountPrice(Double.parseDouble(discount_price));
						reportDao.insertSalesOrderDetail(detailPO);
					}
				}
			}
			act.setOutData("returnValue", returnValue);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "采购订单提报面初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * FUNCTION : 采购订单:查询可提报的采购订单
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-22
	 */
	public void stockOrderQuery() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			RequestWrapper request = act.getRequest();

			Long poseId = logonUser.getPoseId();
			String dealerIds = logonUser.getDealerId();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao
					.getDealerBusiness(poseId.toString());

			// 业务范围字符串
			String areaIds = "";
			for (int i = 0; i < areaList.size(); i++) {
				BigDecimal temp = (BigDecimal) areaList.get(i).get("AREA_ID");
				areaIds += temp;
				if (i != areaList.size() - 1) {
					areaIds += ",";
				}
			}

			String orderWeek = CommonUtils.checkNull(request
					.getParamValue("orderWeek"));
			String companyId = logonUser.getOemCompanyId();
			String[] array = orderWeek.split("-");
			String year = array[0];
			String week = array[1];
			String areaId = CommonUtils.checkNull(request
					.getParamValue("areaId"));
			if (!areaId.equals("")) {
				array = areaId.split("\\|");
				areaId = array[0];
			}

			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1;
			PageResult<Map<String, Object>> ps = reportDao
					.getUrgentOrderReportList(year, week, areaId, areaIds,
							dealerIds, companyId, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "采购订单:查询可提报的采购订单");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * FUNCTION : 采购订单:提报
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-22
	 */
	public void stockOrderReportSubmit() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			RequestWrapper request = act.getRequest();
			String orderId = CommonUtils.checkNull(request
					.getParamValue("orderId"));

			TtVsOrderPO orderPO = reportDao.getTtSalesOrder(orderId);
			Long dealerId = orderPO.getOrderOrgId();
			TmDealerPO dealerPO = reportDao.getTmDealer(dealerId.toString());
			boolean isPass = true;

			if (dealerPO.getDealerLevel().intValue() == Constant.DEALER_LEVEL_01
					.intValue()) {
				// 获得是否需要资金检查
				Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
				TmBusinessParaPO para = reportDao.getTmBusinessParaPO(
						Constant.URGENT_ORDER_ACCOUNT_CHECK_PARA, companyId);
				String isCheck = para.getParaValue();
				if (isCheck.equals("0")) {
					TtVsAccountPO accountPO = new TtVsAccountPO();
					accountPO.setDealerId(orderPO.getOrderOrgId());
					accountPO.setAccountTypeId(orderPO.getFundTypeId());
					accountPO = reportDao.geTtVsAccountPO(accountPO);
					if (accountPO != null) {
						if (accountPO.getAvailableAmount().doubleValue() < orderPO
								.getOrderPrice().doubleValue()) {
							isPass = false;
							act.setOutData("returnValue", 2);
						} else {
							TtVsAccountPO condition = new TtVsAccountPO();
							condition.setAccountId(accountPO.getAccountId());

							TtVsAccountPO value = new TtVsAccountPO();
							value.setBalanceAmount(new Double(accountPO
									.getBalanceAmount().doubleValue()
									- orderPO.getOrderPrice().doubleValue()));
							value.setAvailableAmount(new Double(accountPO
									.getAvailableAmount().doubleValue()
									- orderPO.getOrderPrice().doubleValue()));
							reportDao.update(condition, value);
						}
					} else {
						isPass = false;
						act.setOutData("returnValue", 2);
					}
				}
			}

			if (isPass) {
				TtVsOrderPO condition = new TtVsOrderPO();
				TtVsOrderPO value = new TtVsOrderPO();
				condition.setOrderId(new Long(orderId));
				value.setRaiseDate(new Date());
				value.setOrderStatus(Constant.ORDER_STATUS_07);// 财务待确认
				value.setUpdateBy(logonUser.getUserId());
				value.setUpdateDate(value.getRaiseDate());
				reportDao.updateSalesOrder(condition, value);
				act.setOutData("returnValue", 1);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "采购订单提:提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
}
