/**********************************************************************
 * <pre>
 * FILE : NastyOrderCheck.java
 * CLASS : NastyOrderCheck
 * AUTHOR : 
 * FUNCTION : 订单审核
 *======================================================================
 * CHANGE HISTORY LOG
 *----------------------------------------------------------------------
 * MOD. NO.|   DATE   |    NAME    | REASON  |  CHANGE REQ.
 *----------------------------------------------------------------------
 *         |2010-06-10|            | Created |
 * DESCRIPTION:
 * </pre>
 ***********************************************************************/
package com.infodms.dms.actions.sales.ordermanage.orderaudit;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.VerFlagDao;
import com.infodms.dms.dao.sales.ordermanage.audit.OrderAuditDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.OrderReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmBusinessParaPO;
import com.infodms.dms.po.TtVsAccountPO;
import com.infodms.dms.po.TtVsDlvryReqDtlPO;
import com.infodms.dms.po.TtVsDlvryReqPO;
import com.infodms.dms.po.TtVsOrderCheckPO;
import com.infodms.dms.po.TtVsOrderDetailPO;
import com.infodms.dms.po.TtVsOrderPO;
import com.infodms.dms.po.TtVsResourceChngPO;
import com.infodms.dms.po.TtVsResourcePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * @Title: 补充订单审核Action
 * @Description:InfoFrame3.0.V01
 * @Copyright: Copyright (c) 2010
 * @Company: www.infoservice.com.cn
 * @Date: 2010-6-10
 * @author
 * @mail
 * @version 1.0
 * @remark
 */
public class NastyOrderCheck {

	public Logger logger = Logger.getLogger(NastyOrderCheck.class);
	OrderAuditDao dao = OrderAuditDao.getInstance();
	OrderReportDao reportDao = OrderReportDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();

	private final String initUrl = "/jsp/sales/ordermanage/orderaudit/nastyOrderCheck.jsp";
	private final String detailUrl = "/jsp/sales/ordermanage/orderaudit/nastyOrderDetailCheck.jsp";

	/**
	 * 补充订单审核页面初始化
	 */
	public void nastyOrderCheckInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			List<Map<String, Object>> dateList = dao.getNastyDateList(oemCompanyId);
			List<Map<String, Object>> areaBusList = MaterialGroupManagerDao
					.getPoseIdBusiness(logonUser.getPoseId().toString());
			// 获得是否需要资金检查
			TmBusinessParaPO para = dao.getTmBusinessParaPO(
					Constant.URGENT_ORDER_ACCOUNT_CHECK_PARA, oemCompanyId);
			String isCheck = para.getParaValue();
			act.setOutData("dateList", dateList);
			act.setOutData("areaBusList", areaBusList);
			act.setOutData("isCheck", isCheck);
			act.setForword(initUrl);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"补充订单审核查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 补充订单审核查询
	 */
	public void nastyOrderCheckQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String orderYearWeek = request.getParamValue("orderYearWeek"); // 订单年周
			String[] array = orderYearWeek.split("-");
			String orderYear = array[0];
			String orderWeek = array[1];
			String areaId = request.getParamValue("areaId"); // 业务范围ID
			String area = request.getParamValue("area"); // 业务范围IDS
			String dealerCode = request.getParamValue("dealerCode"); // 经销商CODE
			String groupCode = request.getParamValue("groupCode"); // 物料组CODE
			String orderType = request.getParamValue("orderType"); // 订单类型
			String orderNo = request.getParamValue("orderNo"); // 订单号码
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
					.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getNastyOrderCheckList(orderYear, orderWeek,
					areaId, area, groupCode, dealerCode, orderType, orderNo, companyId, curPage,
					Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
			act.setOutData("orderType", orderType);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"补充订单审核查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 *补充订单审核明细页面查询
	 */
	public void nastyOrderDetailCheckQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String orderId = request.getParamValue("orderId"); // 订单ID
			String orderType = request.getParamValue("orderType"); // 订单类型
			Map<String, Object> map = dao.getOrderInfo(orderId);
			List<Map<String, Object>> list1 = dao.getNastyOrderDetailList(orderId, orderType, logonUser.getCompanyId().toString());
			List<Map<String, Object>> list2 = dao.getOrderCheckList(orderId);
			List<Map<String, Object>> fundTypeList = reportDao.getFundTypeList();// 资金类型列表
			act.setOutData("map", map);
			act.setOutData("orderId", orderId);
			act.setOutData("list1", list1);
			act.setOutData("list2", list2);
			act.setOutData("fundTypeList", fundTypeList);
			act.setForword(detailUrl);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"补充订单审核明细查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 补充订单审核调整保存
	 */
	public void NastyOrderCheckSave() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String detailIds = request.getParamValue("detailIds"); // 订单明细ID
			String checkAmounts = request.getParamValue("strAmounts"); // 审核数量
			String oldAmounts = request.getParamValue("oldAmounts"); // 已审核数量
			String orderAmounts = request.getParamValue("odAmounts"); // 提报数量
			String materialIds = request.getParamValue("materialIds"); // 物料ID
			String batchNos = request.getParamValue("batchNos"); // 订做车批次号
			String orderId = request.getParamValue("orderId"); // 订单ID
			String orderVer = request.getParamValue("orderVer");
			String orderYear = request.getParamValue("orderYear"); // 订单年度
			String orderWeek = request.getParamValue("orderWeek"); // 订单周度
			// String areaId = request.getParamValue("areaId"); //业务范围ID
			String orderType = request.getParamValue("orderType"); // 订单类型
			String fundTypeId = request.getParamValue("fundType"); // 资源类型
			String priceId = request.getParamValue("priceId"); // 价格类型
			String otherPriceReason = request.getParamValue("otherPriceReason"); // 使用其他价格原因
			String orderPrice = request.getParamValue("total"); // 总价
			String checkPrice = request.getParamValue("checkPrice"); // 审核总价
			String dealerId = request.getParamValue("dealerId"); // 开票方id
			String checkRemark = request.getParamValue("checkRemark"); // 审核描述
			String flag = request.getParamValue("flag"); // 通过或驳回标志
			String vers = request.getParamValue("vers");
			String returnValue = "1";

			String[] detailId = detailIds.split(",");
			String[] ver = vers.split(",");
			String[] checkAmount = checkAmounts.split(",");
			String[] oldAmount = oldAmounts.split(",");
			String[] orderAmount = orderAmounts.split(",");
			String[] materialId = materialIds.split(",");
			boolean verFlag = true;
			verFlag = VerFlagDao.getVerFlag("TT_VS_ORDER", "ORDER_ID", orderId, orderVer);
			if (verFlag) {
				for (int m = 0; m < detailId.length; m++) {
					boolean verFlagTmp = VerFlagDao.getVerFlag("TT_VS_ORDER_DETAIL", "DETAIL_ID",
							detailId[m], ver[m]);
					if (verFlagTmp == false) {
						verFlag = false;
					}
				}
				if (verFlag) {
					// 获得资金检查开关
					TmBusinessParaPO para = dao.getTmBusinessParaPO(
							Constant.URGENT_ORDER_ACCOUNT_CHECK_PARA, logonUser.getCompanyId());
					String isCheck = para.getParaValue();// 0为提报时，1为审核时

					TtVsOrderPO orderPo = reportDao.getTtSalesOrder(orderId);
					// 审核通过时
					if (flag.equals("1")) {

						// 获得原资金账户
						TtVsAccountPO accountPO1 = new TtVsAccountPO();
						accountPO1.setDealerId(new Long(dealerId));
						accountPO1.setAccountTypeId(orderPo.getFundTypeId());
						accountPO1 = reportDao.geTtVsAccountPO(accountPO1);

						// 获得新资金账户
						TtVsAccountPO accountPO2 = new TtVsAccountPO();
						accountPO2.setDealerId(new Long(dealerId));
						accountPO2.setAccountTypeId(new Long(fundTypeId));
						accountPO2 = reportDao.geTtVsAccountPO(accountPO2);

						// 资金校验
						if (isCheck.equals("0")
								&& (new Long(fundTypeId) == orderPo.getFundTypeId().longValue())) {
							if (accountPO2 == null
									|| accountPO2.getAvailableAmount().doubleValue()
											+ orderPo.getOrderPrice().doubleValue() < Double
											.parseDouble(checkPrice)) {
								returnValue = "3";
							}
						} else {
							if (accountPO2 == null
									|| accountPO2.getAvailableAmount().doubleValue() < Double
											.parseDouble(checkPrice)) {
								returnValue = "3";
							}
						}

						// 校验通过，冻结资金
						if (returnValue.equals("1")) {
							if (isCheck.equals("0")) {
								// 原账户资金释放
								TtVsAccountPO condition = new TtVsAccountPO();
								condition.setAccountId(accountPO1.getAccountId());

								TtVsAccountPO value = new TtVsAccountPO();
								value.setFreezeAmount(new Double(accountPO1.getFreezeAmount()
										.doubleValue()
										- orderPo.getOrderPrice()));// 冻结额度
								value.setAvailableAmount(new Double(accountPO1.getAvailableAmount()
										.doubleValue()
										+ orderPo.getOrderPrice()));// 可用额度

								dao.update(condition, value);
							}

							// 新账户资金冻结
							TtVsAccountPO condition = new TtVsAccountPO();
							condition.setAccountId(accountPO2.getAccountId());

							TtVsAccountPO value = new TtVsAccountPO();
							value.setFreezeAmount(new Double(accountPO2.getFreezeAmount()
									.doubleValue()
									+ Double.parseDouble(checkPrice)));// 冻结额度
							value.setAvailableAmount(new Double(accountPO2.getAvailableAmount()
									.doubleValue()
									- Double.parseDouble(checkPrice)));// 可用额度

							dao.update(condition, value);

							TtVsOrderPO tvop1 = new TtVsOrderPO();
							TtVsOrderPO tvop2 = new TtVsOrderPO();
							tvop1.setOrderId(Long.parseLong(orderId));
							tvop2.setVer(Integer.parseInt(orderVer) + 1);
							tvop2.setFundTypeId(new Long(fundTypeId));
							tvop2.setPriceId(priceId);
							tvop2.setOtherPriceReason(otherPriceReason);
							tvop2.setOrderPrice(new Double(orderPrice));
							tvop2.setOrderStatus(Constant.ORDER_STATUS_05);
							tvop2.setUpdateBy(logonUser.getUserId());
							tvop2.setUpdateDate(new Date(System.currentTimeMillis()));
							dao.update(tvop1, tvop2); // 更新订单表VER

							orderPo = reportDao.getTtSalesOrder(orderId);

							int totalCheck = 0;
							if (Integer.parseInt(orderType) == Constant.ORDER_TYPE_02) {
								for (int i = 0; i < detailId.length; i++) {
									TtVsOrderDetailPO tvodpContion = new TtVsOrderDetailPO();
									TtVsOrderDetailPO tvodpValue = new TtVsOrderDetailPO();
									if (!"".equals(checkAmount[i]) && checkAmount[i] != null) {
										totalCheck += Integer.parseInt(checkAmount[i]);
										String singlePrice = CommonUtils.checkNull(request
												.getParamValue("singlePrice" + detailId[i]));

										tvodpContion.setDetailId(Long.parseLong(detailId[i]));
										tvodpValue.setSinglePrice(new Double(singlePrice));
										tvodpValue.setCheckAmount(new Integer(checkAmount[i]));
										tvodpValue.setTotalPrice(Double.parseDouble(singlePrice)
												* Integer.parseInt(orderAmount[i]));
										tvodpValue.setVer(Integer.parseInt(ver[i]) + 1);
										tvodpValue.setUpdateBy(logonUser.getUserId());
										tvodpValue.setUpdateDate(new Date(System
												.currentTimeMillis()));
										dao.update(tvodpContion, tvodpValue); // 更新订单明细表

										Map<String, Object> map = dao.getResourcePO(materialId[i]
												.toString(), orderType, orderYear, orderWeek);
										if (map != null && map.containsKey("RESOURCE_ID")) {
											TtVsResourcePO tvrpContion = new TtVsResourcePO();
											TtVsResourcePO tvrpValue = new TtVsResourcePO();
											tvrpContion.setResourceId(Long.parseLong(map.get(
													"RESOURCE_ID").toString()));
											tvrpValue.setCheckAmount(Integer.parseInt(map.get(
													"CHECK_AMOUNT").toString())
													+ Integer.parseInt(checkAmount[i]));
											tvrpValue.setResourceAmount(Integer.parseInt(map.get(
													"RESOURCE_AMOUNT").toString())
													- Integer.parseInt(checkAmount[i]));
											tvrpValue.setUpdateBy(logonUser.getUserId());
											tvrpValue.setUpdateDate(new Date(System
													.currentTimeMillis()));
											dao.update(tvrpContion, tvrpValue); // 更新资源表

											TtVsResourceChngPO tvrcp = new TtVsResourceChngPO();
											tvrcp.setChngId(Long.parseLong(SequenceManager
													.getSequence("")));
											tvrcp.setResourceId(tvrpContion.getResourceId());
											tvrcp.setChngType(Constant.RESOURCE_CHNG_TYPE_02);
											tvrcp.setOrderId(Long.parseLong(orderId));
											tvrcp.setOrderDetailId(Long.parseLong(detailId[i]));
											tvrcp.setChngOrgId(logonUser.getOrgId());
											tvrcp.setChngUserId(logonUser.getUserId());
											tvrcp.setChngDate(new Date(System.currentTimeMillis()));
											tvrcp.setCreateBy(logonUser.getUserId());
											tvrcp
													.setCreateDate(new Date(System
															.currentTimeMillis()));
											dao.insert(tvrcp); // 插入资源变更表
										}
									}
								}
							}
							if (Integer.parseInt(orderType) == Constant.ORDER_TYPE_03) {
								String[] batchNo = batchNos.split(",");
								for (int i = 0; i < detailId.length; i++) {
									TtVsOrderDetailPO tvodpContion = new TtVsOrderDetailPO();
									TtVsOrderDetailPO tvodpValue = new TtVsOrderDetailPO();
									if (!"".equals(checkAmount[i]) && checkAmount[i] != null) {
										totalCheck += Integer.parseInt(checkAmount[i]);
										String singlePrice = CommonUtils.checkNull(request
												.getParamValue("singlePrice" + detailId[i]));
										tvodpContion.setDetailId(Long.parseLong(detailId[i]));
										tvodpValue.setSinglePrice(new Double(singlePrice));
										tvodpValue.setCheckAmount(new Integer(checkAmount[i]));
										tvodpValue.setCallAmount(new Integer(checkAmount[i]));
										tvodpValue.setTotalPrice(Double.parseDouble(singlePrice)
												* Integer.parseInt(orderAmount[i]));
										tvodpValue.setVer(Integer.parseInt(ver[i]) + 1);
										tvodpValue.setUpdateBy(logonUser.getUserId());
										tvodpValue.setUpdateDate(new Date(System
												.currentTimeMillis()));
										dao.update(tvodpContion, tvodpValue); // 更新订单明细表
										Map<String, Object> map = dao.getResourceBatchPO(
												materialId[i].toString(), orderType, batchNo[i]
														.toString(), orderYear, orderWeek);
										TtVsResourcePO tvrpContion = new TtVsResourcePO();
										TtVsResourcePO tvrpValue = new TtVsResourcePO();
										tvrpContion.setResourceId(Long.parseLong(map.get(
												"RESOURCE_ID").toString()));
										tvrpValue.setCheckAmount(Integer.parseInt(map.get(
												"CHECK_AMOUNT").toString())
												+ Integer.parseInt(checkAmount[i]));
										tvrpValue.setResourceAmount(Integer.parseInt(map.get(
												"RESOURCE_AMOUNT").toString())
												- Integer.parseInt(checkAmount[i]));
										tvrpValue.setUpdateBy(logonUser.getUserId());
										tvrpValue
												.setUpdateDate(new Date(System.currentTimeMillis()));
										dao.update(tvrpContion, tvrpValue); // 更新资源表

										TtVsResourceChngPO tvrcp = new TtVsResourceChngPO();
										tvrcp.setChngId(Long.parseLong(SequenceManager
												.getSequence("")));
										tvrcp.setResourceId(tvrpContion.getResourceId());
										tvrcp.setChngType(Constant.RESOURCE_CHNG_TYPE_02);
										tvrcp.setOrderId(Long.parseLong(orderId));
										tvrcp.setOrderDetailId(Long.parseLong(detailId[i]));
										tvrcp.setChngOrgId(logonUser.getOrgId());
										tvrcp.setChngUserId(logonUser.getUserId());
										tvrcp.setChngDate(new Date(System.currentTimeMillis()));
										tvrcp.setCreateBy(logonUser.getUserId());
										tvrcp.setCreateDate(new Date(System.currentTimeMillis()));
										dao.insert(tvrcp); // 插入资源变更表
									}
								}
							}

							TtVsOrderCheckPO tvocp = new TtVsOrderCheckPO();
							tvocp.setCheckId(Long.parseLong(SequenceManager.getSequence("")));
							tvocp.setOrderId(orderPo.getOrderId());
							tvocp.setCheckOrgId(logonUser.getOrgId());
							tvocp.setCheckUserId(logonUser.getUserId());
							if (!"".equals(checkRemark) && checkRemark != null) {
								tvocp.setCheckDesc(checkRemark);
							}
							tvocp.setCheckStatus(Constant.CHECK_STATUS_03);
							tvocp.setCheckDate(new Date(System.currentTimeMillis()));
							tvocp.setCreateBy(logonUser.getUserId());
							tvocp.setCreateDate(new Date(System.currentTimeMillis()));
							dao.insert(tvocp); // 更新订单审核表

							TtVsDlvryReqPO tvdrpo = new TtVsDlvryReqPO(); // 发运申请表
							tvdrpo.setReqId(Long.parseLong(SequenceManager.getSequence("")));// 设置申请主键
							tvdrpo.setOrderId(orderPo.getOrderId());
							tvdrpo.setAreaId(orderPo.getAreaId());
							tvdrpo.setDeliveryType(orderPo.getDeliveryType());
							tvdrpo.setFundType(orderPo.getFundTypeId());
							tvdrpo.setDiscount(orderPo.getDiscount());
							tvdrpo.setPriceId(orderPo.getPriceId());
							tvdrpo.setOtherPriceReason(orderPo.getOtherPriceReason());
							tvdrpo.setReqDate(new Date(System.currentTimeMillis()));
							if (Constant.TRANSPORT_TYPE_02.equals(orderPo.getDeliveryType())) {
								tvdrpo.setReceiver(orderPo.getReceiver());
								tvdrpo.setAddressId(orderPo.getDeliveryAddress());
								tvdrpo.setLinkMan(orderPo.getLinkMan());
								tvdrpo.setTel(orderPo.getTel());
							}
							if (Constant.TRANSPORT_TYPE_03.equals(orderPo.getDeliveryType())) {
								tvdrpo.setFleetId(orderPo.getFleetId());
								tvdrpo.setFleetAddress(orderPo.getFleetAddress());
							}
							tvdrpo.setReqExecStatus(Constant.REQ_EXEC_STATUS_01);
							tvdrpo.setReqTotalAmount(new Integer(totalCheck));
							tvdrpo.setVer(0);
							tvdrpo.setCreateBy(logonUser.getUserId());
							tvdrpo.setCreateDate(new Date(System.currentTimeMillis()));
							dao.insert(tvdrpo); // 插入发运申请表

							TtVsOrderDetailPO detailPo = new TtVsOrderDetailPO();
							detailPo.setOrderId(orderPo.getOrderId());
							List dlist = dao.select(detailPo);
							for (int i = 0; i < dlist.size(); i++) {
								detailPo = (TtVsOrderDetailPO) dlist.get(i);
								if (detailPo.getCheckAmount().intValue() > 0) {
									TtVsDlvryReqDtlPO tvdrdpo = new TtVsDlvryReqDtlPO(); // 发运申请明细表
									tvdrdpo.setDetailId(Long.parseLong(SequenceManager
											.getSequence("")));// 设置发运申请明细主键
									tvdrdpo.setOrderDetailId(detailPo.getDetailId());
									tvdrdpo.setMaterialId(detailPo.getMaterialId());
									tvdrdpo.setReqId(tvdrpo.getReqId());
									tvdrdpo.setReqAmount(detailPo.getCheckAmount());
									tvdrdpo.setCreateBy(logonUser.getUserId());
									tvdrdpo.setVer(0);
									tvdrdpo.setCreateDate(new Date(System.currentTimeMillis()));
									if (Constant.ORDER_TYPE_03.equals(orderPo.getOrderType())) {
										tvdrdpo.setPatchNo(detailPo.getSpecialBatchNo());
									}
									dao.insert(tvdrdpo); // 插入发运申请明细表
								}
							}
						}

					}
					// 审核驳回时
					else {
						TtVsOrderPO tvopContion = new TtVsOrderPO();
						TtVsOrderPO tvopValue = new TtVsOrderPO();
						tvopContion.setOrderId(new Long(orderId));
						tvopValue.setOrderStatus(Constant.ORDER_STATUS_04);
						tvopValue.setVer(Integer.parseInt(orderVer) + 1);
						tvopValue.setUpdateBy(logonUser.getUserId());
						tvopValue.setUpdateDate(new Date(System.currentTimeMillis()));
						dao.update(tvopContion, tvopValue); // 更新订单表

						TtVsOrderCheckPO tvocp = new TtVsOrderCheckPO();
						tvocp.setCheckId(Long.parseLong(SequenceManager.getSequence("")));
						tvocp.setOrderId(new Long(orderId));
						tvocp.setCheckOrgId(logonUser.getOrgId());
						tvocp.setCheckUserId(logonUser.getUserId());
						if (!"".equals(checkRemark) && checkRemark != null) {
							tvocp.setCheckDesc(checkRemark);
						}
						tvocp.setCheckStatus(Constant.CHECK_STATUS_04);
						tvocp.setCheckDate(new Date(System.currentTimeMillis()));
						tvocp.setCreateBy(logonUser.getUserId());
						tvocp.setCreateDate(new Date(System.currentTimeMillis()));
						dao.insert(tvocp); // 更新订单审核表

						TtVsOrderDetailPO detailPo = new TtVsOrderDetailPO();
						detailPo.setOrderId(new Long(orderId));
						List dlist = dao.select(detailPo);
						for (int i = 0; i < dlist.size(); i++) {
							detailPo = (TtVsOrderDetailPO) dlist.get(i);
							TtVsOrderDetailPO dcondition = new TtVsOrderDetailPO();
							TtVsOrderDetailPO dvalue = new TtVsOrderDetailPO();

							dcondition.setDetailId(detailPo.getDetailId());
							dvalue.setCallAmount(0);
							dvalue.setCheckAmount(0);
							dvalue.setSinglePrice(new Double(0));
							dvalue.setVer(detailPo.getVer() + 1);
							dvalue.setUpdateDate(new Date(System.currentTimeMillis()));
							dvalue.setUpdateBy(logonUser.getUserId());
							dao.update(dcondition, dvalue); // 更新订单明细表

						}

						if ("0".equals(isCheck)) {
							TtVsAccountPO tsapContion = new TtVsAccountPO();// 账户表
							TtVsAccountPO tsapValue = new TtVsAccountPO();
							TtVsAccountPO tsap = new TtVsAccountPO();
							tsapContion.setAccountTypeId(orderPo.getFundTypeId());
							tsapContion.setDealerId(orderPo.getBillingOrgId());
							tsap = (TtVsAccountPO) dao.select(tsapContion).get(0);
							tsapValue.setFreezeAmount(tsap.getFreezeAmount()
									- orderPo.getOrderPrice());
							tsapValue.setAvailableAmount(tsap.getAvailableAmount()
									+ orderPo.getOrderPrice());
							tsapValue.setUpdateDate(new Date(System.currentTimeMillis()));
							tsapValue.setUpdateBy(logonUser.getUserId());
							dao.update(tsapContion, tsapValue);
						}
					}
				} else {
					returnValue = "2";
				}
			} else {
				returnValue = "2";
			}
			act.setOutData("returnValue", returnValue);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SAVE_FAILURE_CODE,
					"补充订单审核调整保存");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 补充订单审核提交
	 */
	public void NastyOrderCheckSubmit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String orderIds = request.getParamValue("orderIds"); // 订单ID
			String vers = request.getParamValue("vers");
			String flag = request.getParamValue("flag"); // 页面参数
			String remark = request.getParamValue("remark"); // 审核描述
			String isCheck = request.getParamValue("isCheck");
			String[] orderId = orderIds.split(",");
			String[] ver = vers.split(",");
			boolean verFlag = true;
			for (int m = 0; m < orderId.length; m++) {
				boolean verFlagTmp = VerFlagDao.getVerFlag("TT_VS_ORDER", "ORDER_ID", orderId[m],
						ver[m]);
				if (verFlagTmp == false) {
					verFlag = false;
				}
			}
			if (verFlag) {
				for (int i = 0; i < orderId.length; i++) {
					TtVsOrderPO tvopContion = new TtVsOrderPO();
					TtVsOrderPO tvopValue = new TtVsOrderPO();
					tvopContion.setOrderId(Long.parseLong(orderId[i]));
					if (0 == Integer.parseInt(flag)) {
						tvopValue.setOrderStatus(Constant.ORDER_STATUS_05);
					}
					if (1 == Integer.parseInt(flag)) {
						tvopValue.setOrderStatus(Constant.ORDER_STATUS_04);
					}
					tvopValue.setVer(Integer.parseInt(ver[i]) + 1);
					tvopValue.setUpdateBy(logonUser.getUserId());
					tvopValue.setUpdateDate(new Date(System.currentTimeMillis()));
					dao.update(tvopContion, tvopValue); // 更新订单表

					TtVsOrderCheckPO tvocp = new TtVsOrderCheckPO();
					tvocp.setCheckId(Long.parseLong(SequenceManager.getSequence("")));
					tvocp.setOrderId(Long.parseLong(orderId[i]));
					tvocp.setCheckOrgId(logonUser.getOrgId());
					tvocp.setCheckUserId(logonUser.getUserId());
					if (!"".equals(remark) && remark != null) {
						tvocp.setCheckDesc(remark);
					}
					if (0 == Integer.parseInt(flag)) {
						tvocp.setCheckStatus(Constant.CHECK_STATUS_03);
					}
					if (1 == Integer.parseInt(flag)) {
						tvocp.setCheckStatus(Constant.CHECK_STATUS_04);
					}
					tvocp.setCheckDate(new Date(System.currentTimeMillis()));
					tvocp.setCreateBy(logonUser.getUserId());
					tvocp.setCreateDate(new Date(System.currentTimeMillis()));
					dao.insert(tvocp); // 更新订单审核表
					TtVsOrderPO tvop = new TtVsOrderPO();
					tvop.setOrderId(Long.parseLong(orderId[i]));
					List list = dao.select(tvop);
					if (list.size() > 0) {
						tvop = (TtVsOrderPO) list.get(0);
					}
					// 审核通过时
					if (0 == Integer.parseInt(flag)) {
						TtVsDlvryReqPO tvdrpo = new TtVsDlvryReqPO(); // 发运申请表
						tvdrpo.setReqId(Long.parseLong(SequenceManager.getSequence("")));// 设置申请主键
						tvdrpo.setOrderId(tvop.getOrderId());
						tvdrpo.setAreaId(tvop.getAreaId());
						tvdrpo.setDeliveryType(tvop.getDeliveryType());
						tvdrpo.setFundType(tvop.getFundTypeId());
						tvdrpo.setDiscount(tvop.getDiscount());
						tvdrpo.setPriceId(tvop.getPriceId());
						tvdrpo.setOtherPriceReason(tvop.getOtherPriceReason());
						tvdrpo.setReqDate(new Date(System.currentTimeMillis()));
						if (Constant.TRANSPORT_TYPE_02.equals(tvop.getDeliveryType())) {
							tvdrpo.setReceiver(tvop.getReceiver());
							tvdrpo.setAddressId(tvop.getDeliveryAddress());
							tvdrpo.setLinkMan(tvop.getLinkMan());
							tvdrpo.setTel(tvop.getTel());
						}
						if (Constant.TRANSPORT_TYPE_03.equals(tvop.getDeliveryType())) {
							tvdrpo.setFleetId(tvop.getFleetId());
							tvdrpo.setFleetAddress(tvop.getFleetAddress());
						}
						tvdrpo.setReqExecStatus(Constant.REQ_EXEC_STATUS_01);
						tvdrpo.setVer(0);
						tvdrpo.setCreateBy(logonUser.getUserId());
						tvdrpo.setCreateDate(new Date(System.currentTimeMillis()));
						dao.insert(tvdrpo); // 插入发运申请表

						TtVsOrderDetailPO tvodp = new TtVsOrderDetailPO();
						TtVsOrderDetailPO tvodp1 = new TtVsOrderDetailPO();
						TtVsOrderDetailPO tvodp2 = new TtVsOrderDetailPO();
						tvodp.setOrderId(Long.parseLong(orderId[i]));
						List dtllist = dao.select(tvodp);
						Integer reqTotalAmount = 0;
						for (int j = 0; j < dtllist.size(); j++) {
							tvodp = (TtVsOrderDetailPO) dtllist.get(j);
							tvodp1.setDetailId(tvodp.getDetailId());
							tvodp2.setCallAmount(tvodp.getCheckAmount());
							tvodp2.setVer(tvodp.getVer() + 1);
							tvodp2.setUpdateDate(new Date(System.currentTimeMillis()));
							tvodp2.setUpdateBy(logonUser.getUserId());
							dao.update(tvodp1, tvodp2); // 更新订单明细表
							reqTotalAmount = reqTotalAmount + tvodp.getCheckAmount();
							if ("0".equals(isCheck)) {
								if (tvodp.getCheckAmount() < tvodp.getOrderAmount()) {
									TtVsAccountPO tsap = new TtVsAccountPO();
									TtVsAccountPO tsapContion = new TtVsAccountPO();// 账户表
									TtVsAccountPO tsapValue = new TtVsAccountPO();
									tsap.setDealerId(tvop.getOrderOrgId());
									tsap.setAccountTypeId(tvop.getFundTypeId());
									List acclist = dao.select(tsap);
									if (acclist.size() > 0) {
										tsap = (TtVsAccountPO) acclist.get(0);
									}
									tsapContion.setAccountId(tsap.getAccountId());
									tsapValue.setAvailableAmount(tsap.getAvailableAmount()
											+ (tvodp.getOrderAmount() - tvodp.getCheckAmount())
											* tvodp.getSinglePrice());
									tsapValue.setFreezeAmount(tsap.getFreezeAmount()
											- (tvodp.getOrderAmount() - tvodp.getCheckAmount())
											* tvodp.getSinglePrice());
									tsapValue.setUpdateBy(logonUser.getUserId());
									tsapValue.setUpdateDate(new Date(System.currentTimeMillis()));
									dao.update(tsapContion, tsapValue); // 更新账户表
								}
							}
							if (tvodp.getCheckAmount() > 0) {
								TtVsDlvryReqDtlPO tvdrdpo = new TtVsDlvryReqDtlPO(); // 发运申请明细表
								tvdrdpo
										.setDetailId(Long
												.parseLong(SequenceManager.getSequence("")));// 设置发运申请明细主键
								tvdrdpo.setOrderDetailId(tvodp.getDetailId());
								tvdrdpo.setMaterialId(tvodp.getMaterialId());
								tvdrdpo.setReqId(tvdrpo.getReqId());
								tvdrdpo.setReqAmount(tvodp.getCheckAmount());
								tvdrdpo.setCreateBy(logonUser.getUserId());
								tvdrdpo.setVer(0);
								tvdrdpo.setCreateDate(new Date(System.currentTimeMillis()));
								if (Constant.ORDER_TYPE_03.equals(tvop.getOrderType())) {
									tvdrdpo.setPatchNo(tvodp.getSpecialBatchNo());
								}
								dao.insert(tvdrdpo); // 插入发运申请明细表
							}
						}
						TtVsDlvryReqPO tvdrpo1 = new TtVsDlvryReqPO();
						TtVsDlvryReqPO tvdrpo2 = new TtVsDlvryReqPO();
						tvdrpo1.setReqId(tvdrpo.getReqId());
						tvdrpo2.setReqTotalAmount(reqTotalAmount);
						dao.update(tvdrpo1, tvdrpo2); // 更新发运总量
					}
					// 审核驳回时
					if (1 == Integer.parseInt(flag)) {
						TtVsOrderPO tvop1 = new TtVsOrderPO();
						TtVsOrderDetailPO tvodp = new TtVsOrderDetailPO();
						TtVsOrderDetailPO tvodp1 = new TtVsOrderDetailPO();
						TtVsOrderDetailPO tvodp2 = new TtVsOrderDetailPO();
						tvop1.setOrderId(Long.parseLong(orderId[i]));
						tvodp.setOrderId(Long.parseLong(orderId[i]));
						tvop1 = (TtVsOrderPO) dao.select(tvop1).get(0);
						List dtllist = dao.select(tvodp);
						Double account = new Double(0);
						for (int j = 0; j < dtllist.size(); j++) {
							tvodp = (TtVsOrderDetailPO) dtllist.get(j);
							account = account + (tvodp.getOrderAmount() * tvodp.getSinglePrice());
							tvodp1.setDetailId(tvodp.getDetailId());
							tvodp2.setCallAmount(0);
							tvodp2.setCheckAmount(0);
							tvodp2.setSinglePrice(new Double(0));
							tvodp2.setVer(tvodp.getVer() + 1);
							tvodp2.setUpdateDate(new Date(System.currentTimeMillis()));
							tvodp2.setUpdateBy(logonUser.getUserId());
							dao.update(tvodp1, tvodp2); // 更新订单明细表
						}
						if ("0".equals(isCheck)) {
							TtVsAccountPO tsapContion = new TtVsAccountPO();// 账户表
							TtVsAccountPO tsapValue = new TtVsAccountPO();
							TtVsAccountPO tsap = new TtVsAccountPO();
							tsapContion.setAccountTypeId(tvop1.getFundTypeId());
							tsapContion.setDealerId(tvop1.getBillingOrgId());
							tsap = (TtVsAccountPO) dao.select(tsapContion).get(0);
							tsapValue.setBalanceAmount(tsap.getFreezeAmount() - account);
							tsapValue.setAvailableAmount(tsap.getAvailableAmount() + account);
							tsapValue.setUpdateDate(new Date(System.currentTimeMillis()));
							tsapValue.setUpdateBy(logonUser.getUserId());
							dao.update(tsapContion, tsapValue);
						}
					}
				}
				act.setOutData("returnValue", 1);
			} else {
				act.setOutData("returnValue", 2);
			}
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SAVE_FAILURE_CODE,
					"补充订单审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
