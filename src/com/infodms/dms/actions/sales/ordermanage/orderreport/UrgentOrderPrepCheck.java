/**
 * @Title: UrgentOrderPrepCheck.java
 * 
 * @Description:
 * 
 * @Copyright: Copyright (c) 2010
 * 
 * @Company: www.infoservice.com.cn
 * @Date: 2010-6-3
 * 
 * @author yuyong
 * @mail yuyong@infoservice.com.cn
 * @version 1.0
 * @remark
 */
package com.infodms.dms.actions.sales.ordermanage.orderreport;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.balancecentermanage.dealerordermanage.DealerOrderCheck;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.sales.financemanage.AccountBalanceDetailDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.OrderReportDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.SpecialNeedDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmBusinessParaPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmFleetPO;
import com.infodms.dms.po.TtVsAccountFreezePO;
import com.infodms.dms.po.TtVsAccountPO;
import com.infodms.dms.po.TtVsOrderCheckPO;
import com.infodms.dms.po.TtVsOrderDetailPO;
import com.infodms.dms.po.TtVsOrderPO;
import com.infodms.dms.po.TtVsSpecialReqDtlPO;
import com.infodms.dms.po.TtVsSpecialReqPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.businessUtil.ReqLogUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.component.loger.LogerManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.ActionContextExtend;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

import static com.infodms.dms.actions.sales.financemanage.AccountOpera.*;
/**
 * @author yuyong
 * 
 */
public class UrgentOrderPrepCheck {
	private Logger logger = Logger.getLogger(UrgentOrderPrepCheck.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final OrderReportDao dao = OrderReportDao.getInstance();
	private final AccountBalanceDetailDao accoutDao = AccountBalanceDetailDao.getInstance();
	private final DealerOrderCheck dealerOrderCheck =DealerOrderCheck.getInstance();

	private final String URGENT_ORDER_PREP_CHECK_QUERY_URL = "/jsp/sales/ordermanage/orderreport/urgentOrderPrepCheckQuery.jsp";// 补充订单预审查询页面
	private final String URGENT_ORDER_PREP_CHECK_PRE_URL = "/jsp/sales/ordermanage/orderreport/urgentOrderPrepCheckPre.jsp";// 补充订单预审页面

	public void urgentOrderPrepCheckQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			if ("1".equals(request.getParamValue("command"))) {
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
				// 业务范围id字符串
				Long poseId = logonUser.getPoseId();
				String areaIds = MaterialGroupManagerDao.getDealerBusinessIdStr(poseId.toString());
				PageResult<Map<String, Object>> ps = dao.getUrgentOrderPrepCheckList(logonUser.getDealerId(), areaIds, curPage, Constant.PAGE_SIZE);
				act.setOutData("ps", ps);
			}
			act.setForword(URGENT_ORDER_PREP_CHECK_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "补充订单预审");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void urgentOrderPrepCheckPre() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		ActionContextExtend atx = (ActionContextExtend) ActionContext.getContext();
		String reqURL = atx.getRequest().getContextPath();
		if("/CVS-SALES".equals(reqURL.toUpperCase())){
			act.setOutData("returnValue", 1);
		}else{
			act.setOutData("returnValue", 2);
		}
		try {
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
			String orderNO = CommonUtils.checkNull(request.getParamValue("orderNO"));
			String orderType = CommonUtils.checkNull(request.getParamValue("orderType"));
			
			TtVsOrderPO ttVsOrderPO = new TtVsOrderPO();
			ttVsOrderPO.setOrderId(Long.parseLong(orderId));
			List<PO> list = dao.select(ttVsOrderPO);
			TtVsOrderPO r_ttVsOrderPO = (TtVsOrderPO)list.get(0);
			
			List<Map<String, Object>> detailList = new ArrayList<Map<String,Object>>() ;
			
			if(Constant.ORDER_TYPE_03.toString().equals(orderType)) {
				TtVsSpecialReqPO tvsr = new TtVsSpecialReqPO() ;
				tvsr.setReqId(r_ttVsOrderPO.getSpecialReqId()) ;
				TtVsSpecialReqPO headPO = (TtVsSpecialReqPO)dao.select(tvsr).get(0) ;
				act.setOutData("headPO", headPO) ;
				
				SpecialNeedDao snDao = new SpecialNeedDao() ;
				detailList = snDao.getSalesOrderPreCheckDetailList(orderId, logonUser.getOemCompanyId(), logonUser.getDealerId()) ;
			} else {
				detailList = dao.getSalesOrderPreCheckDetailList(orderId, logonUser.getOemCompanyId(), logonUser.getDealerId());
			}
			
			int deliverytype = r_ttVsOrderPO.getDeliveryType();
			Long receiver = r_ttVsOrderPO.getReceiver();//收货方id
			if (0!=receiver) {
				TmDealerPO r_dealerPO = new TmDealerPO();
				r_dealerPO.setDealerId(receiver);
				List<PO> receList = dao.select(r_dealerPO);
				TmDealerPO r_dealer = (TmDealerPO)receList.get(0);
				String r_dealerName = r_dealer.getDealerShortname();//收货方名称
				act.setOutData("receiver", receiver);
				act.setOutData("r_dealerName", r_dealerName);
			}
			
			String is_fleet  = CommonUtils.checkNull(request.getParamValue("isFleet"));
			String fleet_id  = CommonUtils.checkNull(request.getParamValue("fleetId"));
			if ("1".equals(is_fleet)) {
				TmFleetPO fleetPO = new TmFleetPO();
				fleetPO.setFleetId(Long.parseLong(fleet_id));
				List<PO> fleetList = dao.select(fleetPO);
				if (null != fleetList && fleetList.size()>0) {
					TmFleetPO tmFleetPO = (TmFleetPO)fleetList.get(0);
					String fleetName = tmFleetPO.getFleetName();
					String fleetAddress = r_ttVsOrderPO.getFleetAddress();
					act.setOutData("is_fleet", is_fleet);
					act.setOutData("fleetName", fleetName);
					act.setOutData("fleetAddress", fleetAddress);
				}
			}
			TtVsOrderPO po = dao.getTtSalesOrder(orderId);

			Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getDealerBusiness(poseId.toString());
			//List<Map<String, Object>> fundTypeList = dao.getFundTypeList();
			
			int totalCount = 0;
			for (int i = 0; i < detailList.size(); i++) {
				Map<String, Object> map = detailList.get(i);
				BigDecimal orderAmount = (BigDecimal) map.get("ORDER_AMOUNT");
				totalCount += orderAmount.intValue();
			}
			List<Map<String, Object>> checkList = dao.getOrderCheckList(orderId);

			// 获得订单启票最大折扣点
			TmBusinessParaPO para2 = dao.getTmBusinessParaPO(
					Constant.ORDER_REPORT_MAX_DISCOUNT_RATE_PARA, new Long(logonUser.getOemCompanyId()));
			String ratePara = para2.getParaValue();
			
			act.setOutData("orderType", orderType) ;
			act.setOutData("orderNO", orderNO);
			act.setOutData("order", po);
			act.setOutData("areaList", areaList);// 业务范围列表
			//act.setOutData("fundTypeList", fundTypeList);// 资金类型列表
			act.setOutData("detailList", detailList);
			act.setOutData("checkList", checkList);
			act.setOutData("totalCount", totalCount);
			act.setOutData("orderId", orderId);
			act.setOutData("deliverytype", deliverytype);
			act.setOutData("ratePara", ratePara);
			act.setForword(URGENT_ORDER_PREP_CHECK_PRE_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "补充订单预审");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void urgentOrderReoprtCheck() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String orderNO = CommonUtils.checkNull(request.getParamValue("orderNO"));
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
			String isPass = CommonUtils.checkNull(request.getParamValue("isPass"));
			String areaId = CommonUtils.checkNull(request.getParamValue("area"));
			String fundType = CommonUtils.checkNull(request.getParamValue("fundType"));//资金类型
			String priceId = CommonUtils.checkNull(request.getParamValue("priceId"));  //价格类型
			String[] buyNos = request.getParamValues("buyNo");                         //审核数量
			String[] singlePrices = request.getParamValues("singlePrice");			   //单价
			String[] discount_rates = request.getParamValues("discount_rate");         //折扣率
			String[]discount_s_prices = request.getParamValues("discount_s_price");    //折扣后单价
			String[] discount_prices = request.getParamValues("discount_price");       //折扣额
			String[] acountPrices_s = request.getParamValues("acountPrices_");		   //订单明细总价
			String caigouMoneyAll = CommonUtils.checkNull(request.getParamValue("caigouMoneyAll"));//订单总价
			String discount = CommonUtils.checkNull(request.getParamValue("discountAllMoney"));//折扣额合计
			String accountId = CommonUtils.checkNull(request.getParamValue("accountId"));// 资金账户
			String discountAccountId = CommonUtils.checkNull(request.getParamValue("discountAccountId"));// 折扣账户
			String orderType = CommonUtils.checkNull(request.getParamValue("orderType")) ;
			String[] materialIds = request.getParamValues("materialId");
			String[] detail_ids = request.getParamValues("detail_id");
			String isBingcai = CommonUtils.checkNull(request.getParamValue("isBingcai"));//资金类型：1=兵财，0=非兵财
			
			String returnValue = "1";
			int buyAllNos =0;
			for (int i = 0; i < buyNos.length; i++) {
				buyAllNos += Integer.parseInt(buyNos[i]);
			}
			String[] array = areaId.split("\\|");
			areaId = array[0];
			String dealerId = array[1];
			TmDealerPO dealerPO = dao.getTmDealer(dealerId);

			// 订单状态更新
			TtVsOrderPO condition = new TtVsOrderPO();
			condition.setOrderId(new Long(orderId));

			TtVsOrderPO value = new TtVsOrderPO();
			
			if (isPass.equals("1")) {
				if(Constant.ORDER_TYPE_03.toString().equals(orderType) && dealerPO.getDealerLevel().intValue() == Constant.DEALER_LEVEL_01.intValue()) {
					boolean flag = true; 
					SpecialNeedDao snDao = SpecialNeedDao.getInstance();
					
					TtVsOrderPO oldOrder = new TtVsOrderPO();
					oldOrder.setOrderId(new Long(orderId));
					TtVsOrderPO newOrder = (TtVsOrderPO)dao.select(oldOrder).get(0) ;
					
					List<Map<String, Object>> orderAmountList = snDao.getSpecialOrder(newOrder.getSpecialReqId().toString()) ;
					
					List<Map<String, Object>> sDtlList = snDao.getDtl(newOrder.getSpecialReqId().toString()) ;
					
					int orderAmtLen = orderAmountList.size() ;
					int dtlLen = sDtlList.size() ;
					
					if(orderAmtLen != 1) {
						flag = false ;
					} else {
						for(int n=0; n<dtlLen; n++) {
							if(Integer.parseInt(sDtlList.get(n).get("AMOUNT").toString()) > Integer.parseInt(sDtlList.get(n).get("ORDER_AMOUNT").toString())) {
								flag = false ;
								
								break ;
							}
						}
					}
					
					//modify by WHX,2012.09.12
					//将预扣释放修改到车辆发送完成
					//======================================================START
//					if(flag) {
//						TtVsAccountFreezePO tvaf = new TtVsAccountFreezePO() ;
//						tvaf.setBuzId(newOrder.getSpecialReqId()) ;
//						tvaf.setStatus(Constant.ACCOUNT_FREEZE_STATUS_01) ;
//						List<?> list = dao.select(tvaf) ;
//						
//						if(!CommonUtils.isNullList(list)) {
//							tvaf = (TtVsAccountFreezePO)list.get(0) ;
//							
//							if(tvaf.getFreezeAmount().intValue() != 0) {
//								TtVsSpecialReqPO reqPO = snDao.getTtVsSpecialReqPO(newOrder.getSpecialReqId());
//								
//								Double pre_amount = reqPO.getPreAmount();//预付款金额
//								Long account_type_id = reqPO.getAccountTypeId();//账户类型ID
//								Long account_id = new Long(0);// 账户id
//								TtVsAccountPO tPO = new TtVsAccountPO();
//								tPO.setDealerId(Long.parseLong(dealerId));
//								tPO.setAccountTypeId(account_type_id);
//								List<PO> tList = dao.select(tPO);
//								if (null != tList && tList.size()>0) {
//									tPO = (TtVsAccountPO)tList.get(0);
//									account_id = tPO.getAccountId(); 
//									
//									TtVsAccountPO tvapCondition = new TtVsAccountPO();
//									tvapCondition.setAccountId(account_id);
//									TtVsAccountPO tvapValue = new TtVsAccountPO();
//									tvapValue.setFreezeAmount(tPO.getFreezeAmount().doubleValue() - pre_amount.doubleValue());
//									tvapValue.setAvailableAmount(tPO.getAvailableAmount().doubleValue() + pre_amount.doubleValue());
//									dao.update(tvapCondition, tvapValue);
//									if(0 != pre_amount){
//										accoutDao.accountRelease(account_id+"", reqPO.getReqId()+"", pre_amount+"", logonUser.getUserId()+"", Constant.Freeze_TYPE_01);
//									}
//								}
//							}
//						}
//					}
					//======================================================START
				}
				// 判定是否是一级经销商
				if (dealerPO.getDealerLevel().intValue() == Constant.DEALER_LEVEL_01.intValue()) {
					
					/*// 获得是否需要资金检查
					TmBusinessParaPO para = dao.getTmBusinessParaPO(Constant.URGENT_ORDER_ACCOUNT_CHECK_PARA, oemCompanyId);
					String isCheck = para.getParaValue();
					if (isCheck.equals("0") && (dealerPO.getDealerType().intValue() != Constant.DEALER_TYPE_JSZX) && !"1".equals(isBingcai)) {
						TtVsAccountPO accountPO = new TtVsAccountPO();
						accountPO.setDealerId(new Long(dealerId));
						accountPO.setAccountTypeId(new Long(fundType.split("\\|")[0]));
						accountPO = dao.geTtVsAccountPO(accountPO);
						// 判断是否有账户
						if (accountPO != null) {
							// 判断可用余额是否大于订单总价
							if (accountPO.getAvailableAmount().doubleValue() < Double.parseDouble(caigouMoneyAll)) {
								returnValue = "2";
							} else {
							}
						} else {
							returnValue = "2";
						}
					}*/
					
					if(returnValue.equals("1")){
						value.setFundTypeId(new Long(fundType.split("\\|")[0]));
						value.setPriceId(priceId);
						value.setRaiseDate(new Date());
						value.setBillingOrgId(Long.parseLong(dealerId));//修改开票方
						if(dealerPO.getDealerType().intValue() == Constant.DEALER_TYPE_JSZX){
							value.setOrderStatus(Constant.ORDER_STATUS_07);
						}else{
							value.setOrderStatus(Constant.ORDER_STATUS_03); // 已提报
						}
						value.setDiscount(new Double(discount));
						value.setOrderPrice(new Double(caigouMoneyAll));
						
						List<PO> orderList = dao.select(condition);
						TtVsOrderPO ttVsOrderPO = (TtVsOrderPO)orderList.get(0);
						Map<String,String> infoMap = new HashMap<String, String>();
						infoMap.put("area_id", ttVsOrderPO.getAreaId()+"");
						infoMap.put("order_id", ttVsOrderPO.getOrderId()+"");
						infoMap.put("fund_type", fundType.split("\\|")[0]);
						infoMap.put("delivery_type", ttVsOrderPO.getDeliveryType()+"");
						infoMap.put("address_id", ttVsOrderPO.getDeliveryAddress()+"");
						infoMap.put("isFleet", ttVsOrderPO.getIsFleet()+"");
						infoMap.put("fleet_id", ttVsOrderPO.getFleetId()+"");
						infoMap.put("fleet_address", ttVsOrderPO.getFleetAddress());
						infoMap.put("req_total_amount", buyAllNos+"");
						infoMap.put("price_id", priceId);
						infoMap.put("other_price_reason", ttVsOrderPO.getOtherPriceReason());
						infoMap.put("receiver", ttVsOrderPO.getReceiver()+"");
						infoMap.put("link_man", ttVsOrderPO.getLinkMan());
						infoMap.put("tel", ttVsOrderPO.getTel());
						infoMap.put("total", caigouMoneyAll);
						infoMap.put("discount", discount);
						infoMap.put("reqRemark", ttVsOrderPO.getOrderRemark()) ;
						if(dealerPO.getDealerType().intValue() != Constant.DEALER_TYPE_JSZX){
							Long req_id = Long.parseLong(SequenceManager.getSequence(""));
							String dlvryReqNO = orderNO;
							dealerOrderCheck.urgentDlvryReq(infoMap, req_id,ttVsOrderPO.getIsFleet()+"",dlvryReqNO,logonUser.getUserId());
							//向发运申请操作日志表写入日志信息
							ReqLogUtil.creatReqLog(req_id, Constant.REQ_LOG_TYPE_01, logonUser.getUserId());
							for (int i = 0; i < buyNos.length; i++) {
								if(null != buyNos[i] &&!"0".equals(buyNos[i])){
									dealerOrderCheck.urgentDlvryReqDetail(req_id+"", detail_ids[i],materialIds[i],"",buyNos[i], 
											singlePrices[i],acountPrices_s[i],discount_rates[i],
											discount_s_prices[i],discount_prices[i],logonUser.getUserId());
									
								}
							}
							
							//扣款（判断是否是结算中心，如果是结算中心：不扣款）
							//if (isCheck.equals("0") && !"1".equals(isBingcai)){
								// 冻结资金
								if(!accountId.equals("")){
									dmsFreezePrice_Report(req_id.toString(), accountId, new BigDecimal(caigouMoneyAll), logonUser.getUserId().toString());
								}
								if(!discountAccountId.equals("")){
									dmsFreezePrice_Report(req_id.toString(), discountAccountId, new BigDecimal(discount), logonUser.getUserId().toString());
								}
								// 冻结资金
								/*if(!accountId.equals("")){
									accoutDao.syncAccountFreeze(req_id.toString(), accountId, new BigDecimal(caigouMoneyAll), logonUser.getUserId().toString());
								}
								if(!discountAccountId.equals("")){
									accoutDao.syncAccountFreeze(req_id.toString(), discountAccountId, new BigDecimal(discount), logonUser.getUserId().toString());
								}*/
							//}
							
						}
						
						
						/*if(dealerPO.getDealerType().intValue() != Constant.DEALER_TYPE_JSZX){
							for (int i = 0; i < buyNos.length; i++) {
								if(null != buyNos[i] &&!"0".equals(buyNos[i])){
									dealerOrderCheck.urgentDlvryReqDetail(req_id+"", detail_ids[i],materialIds[i],"",buyNos[i], 
											singlePrices[i],acountPrices_s[i],discount_rates[i],
											discount_s_prices[i],discount_prices[i]);
									
								}
							}
						}*/
						for (int i = 0; i < buyNos.length; i++) {
							if(null != buyNos[i] &&!"0".equals(buyNos[i])){
								TtVsOrderDetailPO tempDetailPO = new TtVsOrderDetailPO();
								tempDetailPO.setDetailId(Long.parseLong(detail_ids[i]));
								
								TtVsOrderDetailPO valueDetailPO = new TtVsOrderDetailPO();
								valueDetailPO.setDiscountRate(Float.parseFloat(discount_rates[i]));
								valueDetailPO.setDiscountSPrice(Double.parseDouble(discount_s_prices[i]));
								valueDetailPO.setDiscountPrice(Double.parseDouble(discount_prices[i]));
								valueDetailPO.setTotalPrice(Double.parseDouble(acountPrices_s[i]));
								valueDetailPO.setOrderAmount(Integer.parseInt(buyNos[i]));
								valueDetailPO.setCallAmount(Integer.parseInt(buyNos[i]));
								valueDetailPO.setSinglePrice(Double.parseDouble(singlePrices[i]));
								if(dealerPO.getDealerType().intValue() != Constant.DEALER_TYPE_JSZX){
									valueDetailPO.setApplyedAmount(Integer.parseInt(buyNos[i]));
								}
								dao.update(tempDetailPO, valueDetailPO);
							}
						}
						
					}
					
				}else{
					TmDealerPO tmDealerPO = new TmDealerPO();
					tmDealerPO.setDealerId(Long.parseLong(dealerId));
					List<PO> dealerList = dao.select(tmDealerPO);
					if (null != dealerList && dealerList.size()>0) {
						TmDealerPO value_dealer = (TmDealerPO)dealerList.get(0);
						Long parentDealerId = value_dealer.getParentDealerD();
						value.setBillingOrgId(parentDealerId);	//开票方
					}
					
				}
				
			} else {
				//if(Constant.ORDER_TYPE_03.toString().equals(orderType)) {
				//	value.setOrderStatus(Constant.ORDER_STATUS_06);// 驳回
				//} else {
					value.setOrderStatus(Constant.ORDER_STATUS_04);// 驳回
				//}
			}
			
			value.setUpdateBy(logonUser.getUserId());
			value.setUpdateDate(new Date());

			if (returnValue.equals("1")) {
				// 订单更新
				dao.update(condition, value);

				// 审核记录保存
				TtVsOrderCheckPO orderCheck = new TtVsOrderCheckPO();
				orderCheck.setCheckId(Long.parseLong(SequenceManager.getSequence("")));
				orderCheck.setOrderId(new Long(orderId));
				orderCheck.setCheckOrgId(logonUser.getOrgId());
				orderCheck.setCheckPositionId(logonUser.getPoseId());
				orderCheck.setCheckUserId(logonUser.getUserId());
				orderCheck.setCheckDate(new Date());
				orderCheck.setCheckStatus(isPass.equals("1") ? Constant.CHECK_STATUS_01 : Constant.CHECK_STATUS_02);
				orderCheck.setCheckDesc(CommonUtils.checkNull(request.getParamValue("checkDesc")));
				orderCheck.setCreateBy(logonUser.getUserId());
				orderCheck.setCreateDate(orderCheck.getCheckDate());
				dao.insert(orderCheck);
			}
			
			//add by WHX,2012.09.12
			//======================================================START
			if(!isPass.equals("1")) {
				SpecialNeedDao snDao = SpecialNeedDao.getInstance() ;
				TtVsOrderPO tvo = snDao.orderQuery(Long.parseLong(orderId)) ;
				
				int order_Type = tvo.getOrderType() ;
				
				if(order_Type == Constant.ORDER_TYPE_03.intValue()) {
					Map<String, String> oMap = new HashMap<String, String>() ;
					oMap.put("reqId", tvo.getSpecialReqId().toString()) ;
					oMap.put("status", Constant.SPECIAL_NEED_STATUS_08.toString()) ;
					oMap.put("userId", logonUser.getUserId().toString()) ;
					
					SpecialNeedConfirm snc = new SpecialNeedConfirm() ;
					snc.spcialReqStatusUpdate(oMap) ;
				}
			}
			//======================================================END
			
			act.setOutData("returnValue", returnValue);
		} catch(RuntimeException e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.PUTIN_FAILURE_CODE, e.getMessage());
			logger.error(logonUser,e1);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "补充订单预审");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void getAddressInfo() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String addressId = CommonUtils.checkNull(request.getParamValue("addressId"));
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
			Map<String, Object> map = dao.getAddressInfo_(addressId,orderId);
//			act.setOutData("info", addressId.equals("") ? "" : "运送地点："
//					+ map.get("ADDRESS")
//					+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 联系人/电话： "
//					+ map.get("LINK_MAN") + " " + map.get("TEL"));
			if (null != addressId && !"".equals(addressId)) {
				act.setOutData("ADDRESS", map.get("ADDRESS"));
				act.setOutData("RECEIVE_ORG", map.get("RECEIVE_ORG"));
				act.setOutData("LINK_MAN", map.get("LINK_MAN"));
				act.setOutData("TEL", map.get("TEL"));
			}else{
				act.setOutData("noAddressId", 1);
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "补充订单预审");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
