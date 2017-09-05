package com.infodms.dms.actions.sales.balancecentermanage.stockordermanage;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Date;
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
import com.infodms.dms.dao.sales.balancecentermanage.stockordermanage.StockOrderConfirmDAO;
import com.infodms.dms.dao.sales.financemanage.AccountBalanceDetailDao;
import com.infodms.dms.dao.sales.ordermanage.orderdetail.OrderDetailInfoQueryDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.OrderReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmBusinessParaPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmFleetPO;
import com.infodms.dms.po.TtVsAccountPO;
import com.infodms.dms.po.TtVsDlvryReqDtlPO;
import com.infodms.dms.po.TtVsDlvryReqPO;
import com.infodms.dms.po.TtVsOrderCheckPO;
import com.infodms.dms.po.TtVsOrderDetailPO;
import com.infodms.dms.po.TtVsOrderPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

import static com.infodms.dms.actions.sales.financemanage.AccountOpera.*;

/**   
 * @Title  : StockOrderConfirm.java
 * @Package: com.infodms.dms.actions.sales.balancecentermanage.stockordermanage
 * @Description: 采购订单提报
 * @date   : 2010-6-22 
 * @version: V1.0   
 */

public class StockOrderConfirm extends BaseDao {
	public Logger logger = Logger.getLogger(StockOrderConfirm.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	//private static final StockOrderConfirm dao = new StockOrderConfirm();
	private final OrderReportDao dao = OrderReportDao.getInstance();
	OrderDetailInfoQueryDao dao_ = OrderDetailInfoQueryDao.getInstance();
	private final AccountBalanceDetailDao accoutDao = AccountBalanceDetailDao.getInstance();

	/*public static final StockOrderConfirm getInstance() {
		return dao;
	}*/

	private final OrderReportDao reportDao = OrderReportDao.getInstance();

	private final String stockOrderConfirmInitUrl = "/jsp/sales/balancecentermanage/stockordermanage/stockOrderConfirmInit.jsp";
	private final String stockOrderPrepCheckPreInit = "/jsp/sales/balancecentermanage/stockordermanage/stockOrderPrepCheckPre.jsp";
	
	/**
	 * @Title : stockOrderConfirmInit
	 * @Description: 采购订单确认面初始化
	 * @param :
	 * @return : void
	 * @throws
	 * @LastUpdate :2010-6-22
	 */
	public void stockOrderConfirmInit() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			Long poseId = logonUser.getPoseId();
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUser);
			List<Map<String, Object>> dateList = reportDao.getUrgentDateList(companyId);
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getDealerBusiness(poseId.toString());
			act.setOutData("dateList", dateList);
			act.setOutData("areaList", areaList);
			act.setForword(stockOrderConfirmInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "采购订单提报面初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * @Title : stockOrderConfirmPre
	 * @Description: 查询可进行确认的采购订单
	 * @param :
	 * @return : void
	 * @throws
	 * @LastUpdate :2010-6-22
	 */
	public void stockOrderConfirmPre() {
		AclUserBean logonUser = null;
		String[] array = null;
		String year = "" ;
		String week = "" ;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String orderWeek = CommonUtils.checkNull(request.getParamValue("orderWeek"));
			if (null != orderWeek && !"".equals(orderWeek)) {
				array = orderWeek.split("-");
				year = array[0];
				week = array[1];
			}
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			array = areaId.split("\\|");
			areaId = array[0];
			String dealerId = array[1];
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = StockOrderConfirmDAO.getStockOrderConfirmPreList(year, week, areaId, dealerId,Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "查询可进行确认的采购订单");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/** 
	* @Title	  : stockOrderConfirm 
	* @Description: 采购订单审核
	* @param      : 
	* @return     : 
	* @throws 
	* @LastUpdate :2010-6-22
	*/
	public void stockOrderConfirm(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
			String checkType = CommonUtils.checkNull(request.getParamValue("checkType"));
			String orderNO = CommonUtils.checkNull(request.getParamValue("orderNO"));
			String checkDesc = CommonUtils.checkNull(request.getParamValue("checkDesc"));
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			String returnValue = "1";
			TtVsOrderPO tempPO = new TtVsOrderPO();
			tempPO.setOrderId(Long.parseLong(orderId));
			TtVsOrderPO valuePO = new TtVsOrderPO();
			if (null != checkType && !"".equals(checkType)) {
				if ("1".equals(checkType)) {
					valuePO.setOrderStatus(Constant.ORDER_STATUS_03);//已提报
					TtVsOrderPO orderPO = new TtVsOrderPO();
					orderPO.setOrderId(Long.parseLong(orderId));
					List<PO> orderList = dao.select(orderPO);
					if (null != orderList && orderList.size()>0) {
						Long req_id = Long.parseLong(SequenceManager.getSequence(""));
						
						TtVsOrderPO ttVsOrderPO = (TtVsOrderPO)orderList.get(0);
						
						// 获得是否需要资金检查
						TmBusinessParaPO para = dao.getTmBusinessParaPO(Constant.URGENT_ORDER_ACCOUNT_CHECK_PARA, oemCompanyId);
						String isCheck = para.getParaValue();
						
						String accountId = "";
						String discountAccountId ="";
						if (isCheck.equals("0")){
							// 获得非折扣账户
							Long fundtypeId = ttVsOrderPO.getFundTypeId();//资金类型
							Long dealerId = ttVsOrderPO.getBillingOrgId();
							TtVsAccountPO accountPO = new TtVsAccountPO();
							accountPO.setDealerId(dealerId);
							accountPO.setAccountTypeId(fundtypeId);
							List<PO> aList = dao.select(accountPO);
							
							if (null != aList && aList.size()>0) {
								accountPO = (TtVsAccountPO)aList.get(0);
								accountId = accountPO.getAccountId()+"";
								// 判断可用余额是否大于订单总价
								if (accountPO.getAvailableAmount().doubleValue() < ttVsOrderPO.getOrderPrice().doubleValue()) {
									returnValue = "2";
								}
							}
							// 获得折扣账户
							List<Map<String,Object>> discountList = accoutDao.getDiscountAccountInfoByDealerId(dealerId+"");
							if (null != discountList && discountList.size()>0) {
								discountAccountId = String.valueOf(discountList.get(0).get("ACCOUNT_ID"));
							}
						}
						
						if(returnValue.equals("1")){
							Map<String,String> infoMap = new HashMap<String, String>();
							infoMap.put("area_id", ttVsOrderPO.getAreaId()+"");
							infoMap.put("order_id", orderId);
							infoMap.put("fund_type", ttVsOrderPO.getFundTypeId()+"");
							infoMap.put("delivery_type", ttVsOrderPO.getDeliveryType()+"");
							infoMap.put("address_id", ttVsOrderPO.getDeliveryAddress()+"");
							infoMap.put("isFleet", ttVsOrderPO.getIsFleet()+"");
							infoMap.put("fleet_id", ttVsOrderPO.getFleetId()+"");
							infoMap.put("fleet_address", ttVsOrderPO.getFleetAddress()+"");
							TtVsOrderDetailPO detailPO = new TtVsOrderDetailPO();
							detailPO.setOrderId(Long.parseLong(orderId));
							List detailList = dao.select(detailPO);
							int req_total_amount = 0;
							if (null != detailList && detailList.size()>0) {
								for (int i = 0; i < detailList.size(); i++) {
									TtVsOrderDetailPO orderDetail = (TtVsOrderDetailPO)detailList.get(i);
									req_total_amount += orderDetail.getOrderAmount();
									urgentDlvryReqDetail(req_id+"", orderDetail.getDetailId()+"",orderDetail.getMaterialId()+"", "",orderDetail.getOrderAmount()+"",orderDetail.getSinglePrice()+"",orderDetail.getTotalPrice()+"",
											orderDetail.getDiscountRate()+"",orderDetail.getDiscountSPrice()+"",orderDetail.getDiscountPrice()+"");
								}
							}
							infoMap.put("req_total_amount", req_total_amount+"");
							infoMap.put("price_id", ttVsOrderPO.getPriceId()+"");
							infoMap.put("other_price_reason", ttVsOrderPO.getOtherPriceReason());
							infoMap.put("receiver", ttVsOrderPO.getReceiver()+"");
							infoMap.put("link_man", ttVsOrderPO.getLinkMan());
							infoMap.put("tel", ttVsOrderPO.getTel());
							infoMap.put("total", ttVsOrderPO.getOrderPrice()+"");
							infoMap.put("discount", ttVsOrderPO.getDiscount()+"");
							//String dlvryReqNO = GetOrderNOUtil.getDlvryReqNO(orderNO);
							String dlvryReqNO = orderNO;
							
							urgentDlvryReq(infoMap, req_id,ttVsOrderPO.getIsFleet()+"",dlvryReqNO);
							
							if (isCheck.equals("0")){
								// 冻结资金
								if(!accountId.equals("")){
									dmsFreezePrice(req_id.toString(), accountId, new BigDecimal(ttVsOrderPO.getOrderPrice()), logonUser.getUserId().toString());
								}
								if(!discountAccountId.equals("")){
									dmsFreezePrice(req_id.toString(), discountAccountId, new BigDecimal(ttVsOrderPO.getDiscount()), logonUser.getUserId().toString());
								}
								/*// 冻结资金
								if(!accountId.equals("")){
									accoutDao.syncAccountFreeze(req_id.toString(), accountId, new BigDecimal(ttVsOrderPO.getOrderPrice()), logonUser.getUserId().toString());
								}
								if(!discountAccountId.equals("")){
									accoutDao.syncAccountFreeze(req_id.toString(), discountAccountId, new BigDecimal(ttVsOrderPO.getDiscount()), logonUser.getUserId().toString());
								}*/
							}
						}
					}
					
				}else{
					valuePO.setOrderStatus(Constant.ORDER_STATUS_04);//驳回
					
					/*List orderList = dao.select(tempPO);
					if (null != orderList && orderList.size()>0) {
						TtVsOrderPO r_orderPO = (TtVsOrderPO)orderList.get(0);
						TtVsAccountPO accountPO = new TtVsAccountPO();
						accountPO.setDealerId(r_orderPO.getOrderOrgId());
						accountPO.setAccountTypeId(r_orderPO.getFundTypeId());
						
						List accountList = dao.select(accountPO);
						if (null != accountList && accountList.size()>0) {
							TtVsAccountPO r_TtVsAccountPO = (TtVsAccountPO)accountList.get(0);
							
							TtVsAccountPO condition = new TtVsAccountPO();
							condition.setAccountId(r_TtVsAccountPO.getAccountId());
							
							TtVsAccountPO value = new TtVsAccountPO();
							value.setBalanceAmount(new Double(r_TtVsAccountPO.getBalanceAmount().doubleValue()+ r_orderPO.getOrderPrice().doubleValue()));
							value.setAvailableAmount(new Double(r_TtVsAccountPO.getAvailableAmount().doubleValue()+ r_orderPO.getOrderPrice().doubleValue()));
							dao.update(condition, value);
						}
					}*/
					
				}
			}
			if(returnValue.equals("1")){
				dao.update(tempPO, valuePO);
				//订单审核表
				TtVsOrderCheckPO tvoc = new TtVsOrderCheckPO() ;
				tvoc.setCheckId(Long.parseLong(SequenceManager.getSequence(""))) ;
				tvoc.setOrderId(new Long(orderId));
				tvoc.setCheckOrgId(logonUser.getOrgId());
				tvoc.setCheckPositionId(logonUser.getPoseId());
				tvoc.setCheckUserId(logonUser.getUserId());
				tvoc.setCheckDate(new Date());
				tvoc.setCheckStatus(checkType.equals("1") ? Constant.CHECK_STATUS_01
						: Constant.CHECK_STATUS_02);
				tvoc.setCheckDesc(checkDesc);
				tvoc.setCreateBy(logonUser.getUserId());
				tvoc.setCreateDate(tvoc.getCheckDate());
				dao.insert(tvoc);
			}
			
			act.setOutData("returnValue", returnValue);
			// act.setForword(stockOrderConfirmInitUrl) ;
		}catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "采购订单审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void urgentDlvryReq(Map<String,String> infoMap ,Long req_id,String isCover,String dlvryReqNO){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			
			TtVsDlvryReqPO dlvryReqPO = new TtVsDlvryReqPO();
			dlvryReqPO.setReqId(req_id);
			dlvryReqPO.setDlvryReqNo(dlvryReqNO);									    //发运申请单号
			dlvryReqPO.setAreaId(Long.parseLong(infoMap.get("area_id")));				//业务范围ID	
			dlvryReqPO.setOrderId(Long.parseLong(infoMap.get("order_id")));				//订单ID
			dlvryReqPO.setFundType(Long.parseLong(infoMap.get("fund_type")));			//资金类型
			if (null != infoMap.get("delivery_type") && !"".equals(infoMap.get("delivery_type"))) {
				dlvryReqPO.setDeliveryType(Integer.parseInt(infoMap.get("delivery_type"))); //发运方式
				dlvryReqPO.setAddressId(Long.parseLong(infoMap.get("address_id")));			//发运地址ID
			}
			
			if (null != infoMap.get("isFleet") && "1".equals(infoMap.get("isFleet"))) {
				dlvryReqPO.setFleetId(Long.parseLong(infoMap.get("fleet_id")));			//集团客户ID
				dlvryReqPO.setFleetAddress(infoMap.get("fleet_address"));				//集团客户地址
			}
			
			dlvryReqPO.setReqDate(new Date());											//申请时间
			dlvryReqPO.setReqTotalAmount(Integer.parseInt(infoMap.get("req_total_amount")));//申请数量合计
			dlvryReqPO.setReqExecStatus(Constant.REQ_EXEC_STATUS_01);					//申请执行状态
			dlvryReqPO.setVer(0);														//版本控制
			dlvryReqPO.setPriceId(infoMap.get("price_id"));				//价格类型
			if (null != infoMap.get("other_price_reason") && !"".equals(infoMap.get("other_price_reason"))) {
				dlvryReqPO.setOtherPriceReason(infoMap.get("other_price_reason").trim());//使用其他价格原因
			}
			dlvryReqPO.setReceiver(Long.parseLong(infoMap.get("receiver")));			//收货方
			if (null != infoMap.get("link_man") && !"".equals(infoMap.get("link_man"))) {
				dlvryReqPO.setLinkMan(infoMap.get("link_man")); 						//联系人
			}
			if (null != infoMap.get("tel") && !"".equals(infoMap.get("tel"))) {
				dlvryReqPO.setTel(infoMap.get("tel"));									//联系电话
			}
			dlvryReqPO.setReqTotalPrice(Double.parseDouble(infoMap.get("total")));		//订单总价
			if (null != isCover && "1".equals(isCover)) {
				dlvryReqPO.setReqStatus(Constant.ORDER_REQ_STATUS_08);					//发运申请状态
				dlvryReqPO.setIsFleet(1);
			}else{
				dlvryReqPO.setReqStatus(Constant.ORDER_REQ_STATUS_01);						
			}
			dlvryReqPO.setReqExecStatus(Constant.REQ_EXEC_STATUS_01);
			dlvryReqPO.setCreateBy(logonUser.getUserId());								
			dlvryReqPO.setCreateDate(new Date());
			
			dao.insert(dlvryReqPO);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, " 补充订单提报是写入发运申请");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void urgentDlvryReqDetail(String reqId,String orderDetailId,String materialId,String patchNo,
			String reqAmount,String singlePrice,String totalPrice,String discountRate,String discountSPrice,String discountPrice){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Long detail_id = Long.parseLong(SequenceManager.getSequence(""));				
			TtVsDlvryReqDtlPO dlvryReqDtlPO = new TtVsDlvryReqDtlPO();						
			dlvryReqDtlPO.setDetailId(detail_id);											//明细ID
			dlvryReqDtlPO.setReqId(Long.parseLong(reqId));									//申请ID
			dlvryReqDtlPO.setOrderDetailId(Long.parseLong(orderDetailId));					//订单明细ID
			dlvryReqDtlPO.setMaterialId(Long.parseLong(materialId));						//物料ID
			if (null != patchNo && !"".equals(patchNo)) {
				dlvryReqDtlPO.setPatchNo(patchNo);											//批次号
			}
			
			dlvryReqDtlPO.setReqAmount(Integer.parseInt(reqAmount));						//申请数量
			dlvryReqDtlPO.setVer(0);
			dlvryReqDtlPO.setSinglePrice(Double.parseDouble(singlePrice));					//物料单价
			dlvryReqDtlPO.setTotalPrice(Double.parseDouble(totalPrice)); 					//订单总价
			if (null != discountRate && !"".equals(discountRate)) {
				dlvryReqDtlPO.setDiscountRate(Float.parseFloat(discountRate.trim()));       //折扣率
			}
			if (null != discountSPrice && !"".equals(discountSPrice)) {
				dlvryReqDtlPO.setDiscountSPrice(Double.parseDouble(discountSPrice.trim()));	//折扣后单价
			}
			if (null != discountPrice && !"".equals(discountPrice)) {
				dlvryReqDtlPO.setDiscountPrice(Double.parseDouble(discountPrice.trim()));	//折扣额
			}
			
			dlvryReqDtlPO.setCreateBy(logonUser.getUserId());
			dlvryReqDtlPO.setCreateDate(new Date());
			
			dao.insert(dlvryReqDtlPO);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, " 补充订单提报是写入发运申请明细表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
		
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/** 
	* @Title	  : stockOrderPrepCheckPreInit
	* @Description: 采购订单预审核跳转
	* @param      : 
	* @return     : 
	* @throws 
	* @LastUpdate :2010-9-17
	*/
	public void stockOrderPrepCheckPreInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
			String orderNO = CommonUtils.checkNull(request.getParamValue("orderNO"));
			int priceId=0;
			int ids=0;
			Map<String, Object> map = dao_.orderInfo(orderId, orderNO);
			act.setOutData("map", map);
			TtVsOrderPO ttVsOrderPO = new TtVsOrderPO();
			ttVsOrderPO.setOrderId(Long.parseLong(orderId));
			List list_ = dao.select(ttVsOrderPO);
			TtVsOrderPO r_ttVsOrderPO = (TtVsOrderPO)list_.get(0);
			Long receiver = r_ttVsOrderPO.getReceiver();//收货方id
			TmDealerPO r_dealerPO = new TmDealerPO();
			r_dealerPO.setDealerId(receiver);
			List receList = dao.select(r_dealerPO);
			TmDealerPO r_dealer = (TmDealerPO)receList.get(0);
			String r_dealerName = r_dealer.getDealerShortname();//收货方名称
			act.setOutData("receiver", receiver);
			act.setOutData("r_dealerName", r_dealerName);
			String is_fleet  = CommonUtils.checkNull(request.getParamValue("is_fleet"));
			String fleet_id  = CommonUtils.checkNull(request.getParamValue("fleet_id"));
			if ("1".equals(is_fleet)) {
				TmFleetPO fleetPO = new TmFleetPO();
				fleetPO.setFleetId(Long.parseLong(fleet_id));
				List fleetList = dao.select(fleetPO);
				if (null != fleetList && fleetList.size()>0) {
					TmFleetPO tmFleetPO = (TmFleetPO)fleetList.get(0);
					String fleetName = tmFleetPO.getFleetName();
					String fleetAddress = tmFleetPO.getAddress();
					act.setOutData("is_fleet", is_fleet);
					act.setOutData("fleetName", fleetName);
					act.setOutData("fleetAddress", fleetAddress);
				}
			}
			TtVsOrderPO po = dao.getTtSalesOrder(orderId);

			Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao
					.getDealerBusiness(poseId.toString());
			//List<Map<String, Object>> fundTypeList = dao.getFundTypeList();
			List<Map<String, Object>> detailList = dao
					.getSalesOrderPreCheckDetailList(orderId, logonUser
							.getOemCompanyId(), logonUser.getDealerId());
			int totalCount = 0;
			for (int i = 0; i < detailList.size(); i++) {
				Map<String, Object> map_ = detailList.get(i);
				BigDecimal orderAmount = (BigDecimal) map_.get("ORDER_AMOUNT");
				totalCount += orderAmount.intValue();
			}
			List<Map<String, Object>> checkList = dao.getOrderCheckList(orderId);

			act.setOutData("orderNO", orderNO);
			act.setOutData("order", po);
			act.setOutData("areaList", areaList);// 业务范围列表
			//act.setOutData("fundTypeList", fundTypeList);// 资金类型列表
			act.setOutData("detailList", detailList);
			act.setOutData("checkList", checkList);
			act.setOutData("totalCount", totalCount);
			act.setOutData("orderId", orderId);
			act.setForword(stockOrderPrepCheckPreInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "采购订单预审");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/** 
	* @Title	  : stockOrderPrepCheckPre
	* @Description: 采购订单预审核
	* @param      : 
	* @return     : 
	* @throws 
	* @LastUpdate :2010-9-17
	*/
	public void stockOrderPrepCheckPre() {
		
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
					ErrorCodeConstant.QUERY_FAILURE_CODE, "采购订单预审");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
