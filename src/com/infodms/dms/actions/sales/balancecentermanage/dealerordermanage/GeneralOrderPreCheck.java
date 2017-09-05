package com.infodms.dms.actions.sales.balancecentermanage.dealerordermanage;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.ordermanage.orderreport.MonthGeneralOrderCall;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.relation.DealerRelationDAO;
import com.infodms.dms.dao.sales.balancecentermanage.dealerordermanage.DealerGeneralOrderCheckDAO;
import com.infodms.dms.dao.sales.balancecentermanage.dealerordermanage.DealerOrderCheckDAO;
import com.infodms.dms.dao.sales.financemanage.AccountBalanceDetailDao;
import com.infodms.dms.dao.sales.ordermanage.delivery.OrderDeliveryDao;
import com.infodms.dms.dao.sales.ordermanage.orderquery.OrderQueryDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.OrderReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmBusinessParaPO;
import com.infodms.dms.po.TtVsAccountPO;
import com.infodms.dms.po.TtVsDlvryReqDtlPO;
import com.infodms.dms.po.TtVsDlvryReqPO;
import com.infodms.dms.po.TtVsOrderDetailPO;
import com.infodms.dms.po.TtVsReqCheckPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.businessUtil.ReqLogUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.ActionContextExtend;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

import static com.infodms.dms.actions.sales.financemanage.AccountOpera.*;

public class GeneralOrderPreCheck extends BaseDao{
	public Logger logger = Logger.getLogger(GeneralOrderPreCheck.class);
	private ActionContext act = ActionContext.getContext();
	ResponseWrapper response = act.getResponse();
	private static final GeneralOrderPreCheck dao = new GeneralOrderPreCheck();
	private final OrderQueryDao orderQueryDao = OrderQueryDao.getInstance();

	public static final GeneralOrderPreCheck getInstance() {
		return dao;
	}
	RequestWrapper request = act.getRequest();
	
	/*------------------------------------------------------------- url -------------------------------------------------------------*/
	
	private static final String GENERAL_PRE_CHK_INIT = "/jsp/sales/balancecentermanage/dealerordermanage/generalOrderPreCheckInit.jsp" ;
	private static final String GENERAL_ORDER_INFO = "/jsp/sales/balancecentermanage/dealerordermanage/generalOrderChkDtl.jsp" ;
	
	/*---------------------------------------------------------- action方法 ----------------------------------------------------------*/
	
	public void generalPreChkInit() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			
			Long poseId = logonUser.getPoseId();
			
			// 年列表
			List<String> years = orderQueryDao.getYearList() ;
			// 月列表
			List<String> months = orderQueryDao.getMonthList() ;
			// 周列表
			List<String> weeks = orderQueryDao.getWeekList() ;	
			
			// 获取只为对应业务范围
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(poseId.toString());
			
			int curYear = OrderDeliveryDao.getNowDate()[0] ;
			int curMonth = OrderDeliveryDao.getNowDate()[1] ;
			int curWeek = OrderDeliveryDao.getNowDate()[2] ;
			
			act.setOutData("years", years);
			act.setOutData("months", months);
			act.setOutData("weeks", weeks);
			act.setOutData("curYear", curYear);
			act.setOutData("curMonth", curMonth);
			act.setOutData("curWeek", curWeek);
			act.setOutData("areaList", areaList);
			
			act.setForword(GENERAL_PRE_CHK_INIT);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商订单审核页面初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void dealerOrderCheckInit_Query() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String orderYear1 = CommonUtils.checkNull(request.getParamValue("orderYear1"));
			String orderWeek1 = CommonUtils.checkNull(request.getParamValue("orderWeek1"));
			String orderMonth1 = CommonUtils.checkNull(request.getParamValue("orderMonth1"));
			String orderYear2 = CommonUtils.checkNull(request.getParamValue("orderYear2"));
			String orderWeek2 = CommonUtils.checkNull(request.getParamValue("orderWeek2"));
			String orderMonth2 = CommonUtils.checkNull(request.getParamValue("orderMonth2"));
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));
			String orderNo = CommonUtils.checkNull(request.getParamValue("orderNo"));
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			String dealerId = logonUser.getDealerId();

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("orderYear1", orderYear1);//订单周度
			map.put("orderWeek1", orderWeek1);
			map.put("orderMonth1", orderMonth1);
			map.put("orderYear2", orderYear2);
			map.put("orderWeek2", orderWeek2);
			map.put("orderMonth2", orderMonth2);
			map.put("areaId", areaId);		  //业务范围
			map.put("groupCode", groupCode);  //物料组
			map.put("orderNo", orderNo);	  //订单号
			map.put("dealerCode", dealerCode);    
			map.put("dealerId", dealerId);    

			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = DealerGeneralOrderCheckDAO.getDealerOrderCheckInit_Query(map, Constant.PAGE_SIZE, curPage);
			
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商订单审核：查询可审核的订单");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void ReqDetailInto(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String reqId = CommonUtils.checkNull(request.getParamValue("reqId"));
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			
			ActionContextExtend atx = (ActionContextExtend) ActionContext.getContext();
			String reqURL = atx.getRequest().getContextPath();
			if("/CVS-SALES".equals(reqURL.toUpperCase())){
				act.setOutData("returnValue", 1);
			}else{
				act.setOutData("returnValue", 2);
			}
			
			//1.订单信息
			Map<String, Object> orderMap = DealerGeneralOrderCheckDAO.orderInfoQuery(reqId) ;
			//2.发运申请信息
			Map<String, Object> reqMap = DealerGeneralOrderCheckDAO.reqInfoQuery(reqId) ;
			//3.发运申请明细信息
			List<Map<String, Object>> reqDtlList = DealerGeneralOrderCheckDAO.reqDtlInfoQuery(reqId) ;
			
			String areaId = reqMap.get("AREA_ID").toString() ;
			String is_fleet = reqMap.get("IS_FLEET").toString() ;
			String k_dealer = reqMap.get("K_ID").toString() ;
			String fundType = reqMap.get("FUND_TYPE").toString() ;
			
			if (null != is_fleet && "1".equals(is_fleet)) {
				Map<String,String> fleetInfo = DealerGeneralOrderCheckDAO.getFleetInfo(reqId) ;
				String fleet_id = String.valueOf(fleetInfo.get("FLEET_ID"));
				String fleet_name = String.valueOf(fleetInfo.get("FLEET_NAME"));
				String address = String.valueOf(fleetInfo.get("ADDRESS"));
				
				act.setOutData("fleet_id", fleet_id);
				act.setOutData("fleet_name", fleet_name);
				act.setOutData("address", address);
			}
			
			//4.订单审核日志信息
			String orderId = orderMap.get("ORDER_ID").toString() ;
			List<Map<String,Object>> checkHisList = DealerOrderCheckDAO.getCheckHisList(orderId);
			
			//5. 发运申请变更日志信息
			List<Map<String,Object>> reqChngList = DealerGeneralOrderCheckDAO.reqChngQuery(reqId) ;
			
			//6. 发运申请审核日志信息
			List<Map<String,Object>> reqChkList = DealerGeneralOrderCheckDAO.reqChkQuery(reqId) ;
			
			
			Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getDealerBusiness(poseId.toString());// 业务范围列表
			List<Map<String, Object>> fundTypeList = AccountBalanceDetailDao.getInstance().getNoDiscountAccountInfoByDealerId(k_dealer);
	
			// 获得是否需要资金检查
			TmBusinessParaPO para = OrderReportDao.getInstance().getTmBusinessParaPO(Constant.URGENT_ORDER_ACCOUNT_CHECK_PARA, oemCompanyId);
			String isCheck = para.getParaValue();
				
			// 获得订单启票最大折扣点
			TmBusinessParaPO para2 = OrderReportDao.getInstance().getTmBusinessParaPO(Constant.ORDER_REPORT_MAX_DISCOUNT_RATE_PARA, oemCompanyId);
			String ratePara = para2.getParaValue();
			
			act.setOutData("areaList", areaList);// 业务范围列表
			act.setOutData("fundTypeList", fundTypeList);// 资金类型列表
			act.setOutData("fundType", fundType) ;
			act.setOutData("isCheck", isCheck);
			act.setOutData("is_fleet", is_fleet) ;
			act.setOutData("reqChngList", reqChngList) ;
			act.setOutData("reqChkList", reqChkList) ;  
			act.setOutData("deliveryType", Constant.TRANSPORT_TYPE_02) ;
				
			act.setOutData("orderInfo", orderMap);
			act.setOutData("reqMap", reqMap);
			act.setOutData("orderDetailList", reqDtlList);
			act.setOutData("checkHisList", checkHisList);
			act.setOutData("areaId", areaId);
			act.setOutData("ratePara", ratePara);
				
			act.setForword(GENERAL_ORDER_INFO) ;
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商订单审核：查询要审核订单详细信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void checkSubmitAction(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER); /////
		try {
			String[] orderDtlId = request.getParamValues("detail_id");										//订单明细id
			String[] reqDtlId = request.getParamValues("reqDtlId") ;											// 发运明细id
			String[] buyNO = request.getParamValues("buyNO");
			String[] singlePrice = request.getParamValues("singlePrice") ;									// 单价
			String[] discountRate = request.getParamValues("discount_rate") ;								// 折扣率
			String[] discountSPrice = request.getParamValues("discount_s_price") ;							// 折扣后单价
			String[] discountPrice = request.getParamValues("discount_price") ;								// 折扣额
			String[] acountPrices_ = request.getParamValues("acountPrices_") ;								// 发运明细合计
			
			String reqId = CommonUtils.checkNull(request.getParamValue("reqId")) ;							// 发运申请id
			String k_dealerId = CommonUtils.checkNull(request.getParamValue("k_dealerId"));					//开票方
			String caigouMoneyAll = CommonUtils.checkNull(request.getParamValue("totalPrice_"));    		//采购总价
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String totalDiscountPrice = CommonUtils.checkNull(request.getParamValue("totalDiscountPrice"));	//折扣总额
			String fundType = CommonUtils.checkNull(request.getParamValue("fundTypeId")); 					// 选择资金类型
			String isBingcai = CommonUtils.checkNull(request.getParamValue("isBingcai"));	//资金类型：1=兵财，0=非兵财
			String caigouAllNumber = CommonUtils.checkNull(request.getParamValue("caigouAllNumber"));	//采购总数量
			String priceId = CommonUtils.checkNull(request.getParamValue("priceId")) ;
			String otherPriceReason = CommonUtils.checkNull(request.getParamValue("otherPriceReason")) ;
			String checkDesc = CommonUtils.checkNull(request.getParamValue("check_desc")) ;
			String reqRemark = CommonUtils.checkNull(request.getParamValue("orderRemark")).trim() ;
			
			String returnValue = "1" ;
			
			if(buyNO != null){
				for (int i = 0; i < buyNO.length; i++) {
					if (buyNO[i] != null && !"".equals(buyNO[i]) && Integer.parseInt(buyNO[i]) > 0) {
						if(singlePrice[i].equals("") || singlePrice[i].equals("0")) {
							returnValue = "4" ;
							break ;
						}
					}
				}
			}
			
			if(returnValue != "4") {
				MonthGeneralOrderCall mgoc = new MonthGeneralOrderCall() ;
				String str = mgoc.getMatCode(orderDtlId, buyNO) ;
				
				if(!CommonUtils.isNullString(str)){
					act.setOutData("metStr", str);
					returnValue = "3" ;
				} else {
					Map<String, String> map = new HashMap<String, String>() ;
					
					map.put("userId", logonUser.getUserId().toString()) ;
					map.put("fundType", fundType) ;
					map.put("totalAmount", caigouAllNumber) ;
					map.put("priceId", priceId) ;
					map.put("otherPrice", otherPriceReason) ;
					map.put("caigouMoneyAll", caigouMoneyAll) ;
					map.put("discount", totalDiscountPrice) ;
					map.put("checkDesc", checkDesc) ;
					map.put("poseId", logonUser.getPoseId().toString()) ;
					map.put("orgId", logonUser.getParentOrgId()) ;
					map.put("reqRemark", reqRemark) ;
					
						
					TmBusinessParaPO para = OrderReportDao.getInstance().getTmBusinessParaPO(Constant.URGENT_ORDER_ACCOUNT_CHECK_PARA, GetOemcompanyId.getOemCompanyId(logonUser));
					String isCheck = para.getParaValue();
		
					/*if (!isMoneyEnough(logonUser, k_dealerId, caigouMoneyAll, fundType, isBingcai, isCheck)) {
						returnValue = "2" ;
					} else {*/
			
						int len = buyNO.length;
			
						for (int i = 0; i < len; i++) {
							map.put("singlePrice", singlePrice[i]) ;
							map.put("discountRate", discountRate[i]) ;
							map.put("discountSPrice", discountSPrice[i]) ;
							map.put("discountPrice", discountPrice[i]) ;
							map.put("acountPrices_", acountPrices_[i]) ;
							
							/*if(Integer.parseInt(buyNO[i]) == 0) {
								deleteReqDtl(Long.parseLong(reqDtlId[i])) ;
								
								continue ;
							}*/
							
							updateReqDtlInfo(Long.parseLong(reqDtlId[i]), Integer.parseInt(buyNO[i]), map);
							updateOrderDtlInfo(Long.parseLong(orderDtlId[i]), Integer.parseInt(buyNO[i]), map);
						}
			
						TtVsDlvryReqPO tvdrp = new TtVsDlvryReqPO() ;
						tvdrp.setReqId(Long.parseLong(reqId)) ;
						tvdrp = (TtVsDlvryReqPO)dao.select(tvdrp).get(0) ;
						
						if("1".equals(tvdrp.getIsFleet().toString())) {
							updateReqInfo(Long.parseLong(reqId), Constant.ORDER_REQ_STATUS_08, map);
						} else {
							updateReqInfo(Long.parseLong(reqId), Constant.ORDER_REQ_STATUS_01, map);
						}
						
						insertReqChkInfo(Long.parseLong(reqId), Constant.CHECK_STATUS_01, map) ;
			
						ReqLogUtil.creatReqLog(Long.parseLong(reqId), Constant.REQ_LOG_TYPE_01, logonUser.getUserId()) ;
					// }
						syncBillDealerAccountFreeze(logonUser, k_dealerId, caigouMoneyAll, areaId, totalDiscountPrice, fundType, isBingcai, isCheck, Long.parseLong(reqId));
						
						DealerRelationDAO dr = DealerRelationDAO.getInstance() ;
						String dlrType = dr.getFirstDlr(k_dealerId).get("DEALER_TYPE").toString() ;
						
						if(Constant.DEALER_TYPE_JSZX == Integer.parseInt(dlrType)) {
								AccountBalanceDetailDao.getInstance().syncReqToDFS(Long.parseLong(reqId), false);
						}
				}
			}
			
			act.setOutData("returnValue", returnValue) ;
		} catch(RuntimeException e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.PUTIN_FAILURE_CODE,e.getMessage());
			logger.error(logonUser,e1);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商普通订单预审核：提交");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void retBack() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		
		try {
			String reqId = CommonUtils.checkNull(request.getParamValue("reqId")) ;							// 发运申请id
			String checkDesc = CommonUtils.checkNull(request.getParamValue("check_desc")) ;
			String k_dealerId = CommonUtils.checkNull(request.getParamValue("k_dealerId"));					//开票方
			
			Map<String, String> map = new HashMap<String, String>() ;
			map.put("userId", logonUser.getUserId().toString()) ;
			map.put("checkDesc", checkDesc) ;
			map.put("poseId", logonUser.getPoseId().toString()) ;
			map.put("orgId", logonUser.getParentOrgId()) ;
			
			updateReqStatus(Long.parseLong(reqId), Constant.ORDER_REQ_STATUS_07, logonUser.getUserId()) ;
			
			insertReqChkInfo(Long.parseLong(reqId), Constant.CHECK_STATUS_02, map) ;
			
			ReqLogUtil.creatReqLog(Long.parseLong(reqId), Constant.REQ_LOG_TYPE_09, logonUser.getUserId()) ;
			
			DealerRelationDAO dr = DealerRelationDAO.getInstance() ;
			String dlrType = dr.getFirstDlr(k_dealerId).get("DEALER_TYPE").toString() ;
			
			if(Constant.DEALER_TYPE_JSZX == Integer.parseInt(dlrType)) {
				AccountBalanceDetailDao.getInstance().syncReqToDFS(Long.parseLong(reqId), true);
			}
			act.setOutData("returnValue", 1) ;
		} catch(Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商普通订单预审核：提交");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/*---------------------------------------------------------- private方法 ----------------------------------------------------------*/
	
	private int updateReqStatus(Long reqId, Integer chkStatus, Long userId) {
		TtVsDlvryReqPO oldReq = new TtVsDlvryReqPO() ;
		oldReq.setReqId(reqId) ;
		
		TtVsDlvryReqPO newReq = new TtVsDlvryReqPO() ;
		newReq.setReqStatus(chkStatus);
		newReq.setUpdateBy(userId) ;
		newReq.setUpdateDate(new Date(System.currentTimeMillis())) ;
		
		int no = dao.update(oldReq, newReq) ;
		
		return no ;
	}
	
	private int updateReqInfo(Long reqId, Integer chkStatus, Map<String, String> map) {
		String fundType = map.get("fundType") ;
		String totalAmount = map.get("totalAmount") ;
		String userId = map.get("userId") ;
		String priceId = map.get("priceId") ;
		String otherPrice = map.get("otherPrice") ;
		String discount = map.get("discount") ;
		String caigouMoneyAll = map.get("caigouMoneyAll") ;
		String reqRemark = map.get("reqRemark") ;
		
		TtVsDlvryReqPO oldReq = new TtVsDlvryReqPO() ;
		oldReq.setReqId(reqId) ;
		
		TtVsDlvryReqPO newReq = new TtVsDlvryReqPO() ;
		newReq.setReqStatus(chkStatus) ;
		newReq.setFundType(Long.parseLong(fundType)) ;
		newReq.setReqRemark(reqRemark) ;
		newReq.setReqTotalPrice(Double.parseDouble(caigouMoneyAll)) ;
		newReq.setReqTotalAmount(Integer.parseInt(totalAmount)) ;
		newReq.setPriceId(priceId) ;
		newReq.setOtherPriceReason(otherPrice) ;
		newReq.setDiscount(Double.parseDouble(discount)) ;
		newReq.setUpdateBy(Long.parseLong(userId)) ;
		newReq.setUpdateDate(new Date(System.currentTimeMillis())) ;
		
		int no = dao.update(oldReq, newReq) ;
		
		return no ;
	}
	
	private int updateReqDtlInfo(Long detailId, Integer applyAmount, Map<String, String> map) {
		String userId = map.get("userId") ;
		String singlePrice = map.get("singlePrice") ;
		String discountRate = map.get("discountRate") ;
		String discountSPrice = map.get("discountSPrice") ;
		String discountPrice = map.get("discountPrice") ;
		String acountPrices_ = map.get("acountPrices_") ;
		
		double singlePrice_ = Double.parseDouble(singlePrice) ;
		
		if(singlePrice_ > Constant.MATERIAL_PRICE_MAX) {
			singlePrice_ = 0 ;
		}
		
		
		TtVsDlvryReqDtlPO oldDtl = new TtVsDlvryReqDtlPO() ;
		oldDtl.setDetailId(detailId) ;
		
		TtVsDlvryReqDtlPO newDtl = new TtVsDlvryReqDtlPO() ;
		newDtl.setSinglePrice(singlePrice_) ;
		newDtl.setDiscountRate(Float.parseFloat(discountRate)) ;
		newDtl.setDiscountSPrice(Double.parseDouble(discountSPrice)) ;
		newDtl.setDiscountPrice(Double.parseDouble(discountPrice)) ;
		newDtl.setTotalPrice(Double.parseDouble(acountPrices_)) ;
		newDtl.setReqAmount(applyAmount) ;
		newDtl.setUpdateBy(Long.parseLong(userId)) ;
		newDtl.setUpdateDate(new Date(System.currentTimeMillis())) ;
		
		int no = dao.update(oldDtl, newDtl) ;
		
		return no ;
	}
	
	private int deleteReqDtl(Long detailId) {
		TtVsDlvryReqDtlPO oldDtl = new TtVsDlvryReqDtlPO() ;
		oldDtl.setDetailId(detailId) ;
		
		int no = dao.delete(oldDtl) ;
		
		return no ;
	}
	
	private int updateOrderDtlInfo(Long detailId, Integer applyAmount, Map<String, String> map) {
		String userId = map.get("userId") ;
		
		Integer callAmount = 0 ;
		
		TtVsOrderDetailPO oldDtl = new TtVsOrderDetailPO() ;
		oldDtl.setDetailId(detailId) ;
		List<TtVsOrderDetailPO> dtlList = dao.select(oldDtl) ;
		
		if(!CommonUtils.isNullList(dtlList)) {
			callAmount = dtlList.get(0).getCallAmount() + applyAmount ;
		}
		
		TtVsOrderDetailPO newDtl = new TtVsOrderDetailPO() ;
		newDtl.setCallAmount(callAmount) ;
		newDtl.setUpdateBy(Long.parseLong(userId)) ;
		newDtl.setUpdateDate(new Date(System.currentTimeMillis())) ;
		
		int no = dao.update(oldDtl, newDtl) ;
		
		return no ;
	}
	
	private void insertReqChkInfo(Long reqId, Integer chngStatus, Map<String, String> map) {
		String checkDesc = map.get("checkDesc") ;
		String userId = map.get("userId") ;
		String poseId = map.get("poseId") ;
		String orgId = map.get("orgId") ;
		
		Long chkId = Long.parseLong(SequenceManager.getSequence(""));
		
		TtVsReqCheckPO newReqChk = new TtVsReqCheckPO() ;
		newReqChk.setCheckId(chkId) ;
		newReqChk.setReqId(reqId) ;
		newReqChk.setCheckOrgId(Long.parseLong(orgId)) ;
		newReqChk.setCheckPositionId(Long.parseLong(poseId)) ;
		newReqChk.setCheckUserId(Long.parseLong(userId)) ;
		newReqChk.setCheckDate(new Date(System.currentTimeMillis())) ;
		newReqChk.setCheckStatus(chngStatus) ;
		newReqChk.setCheckDesc(checkDesc) ;
		newReqChk.setCreateBy(Long.parseLong(userId)) ;
		newReqChk.setCreateDate(new Date(System.currentTimeMillis())) ;
		
		dao.insert(newReqChk) ;
	}
	
	private boolean isMoneyEnough(AclUserBean logonUser, String k_dealerId, String caigouMoneyAll, String fundType, String isBingcai,String isCheck) {
		if (isCheck.equals("0") &&  !"1".equals(isBingcai)) {//若在提报时校验资金，并且付款方式不是兵财
			TtVsAccountPO accountPO = new TtVsAccountPO();
			accountPO.setDealerId(Long.parseLong(k_dealerId));
			accountPO.setAccountTypeId(Long.parseLong(fundType));
			accountPO = OrderReportDao.getInstance().geTtVsAccountPO(accountPO);
			
			if (accountPO != null) {
				if (accountPO.getAvailableAmount().doubleValue() < Double.parseDouble(caigouMoneyAll)) {
					return false;
				}
			} else {
				return false;
			}
		}
		return true;
	}
	
	private void syncBillDealerAccountFreeze(AclUserBean logonUser, String k_dealerId, String caigouMoneyAll, String areaId, String totalDiscountPrice, String fundType, String isBingcai, String isCheck, Long req_id) {
		String parentDealerId = "";
		Map<String,String> parInfo = DealerOrderCheckDAO.checkParentDealer(k_dealerId + "", areaId);
		if (null != parInfo && parInfo.size()>0) {
			parentDealerId = String.valueOf(DealerOrderCheckDAO.checkParentDealer(k_dealerId + "", areaId).get("PARENT_DEALER_D"));
		}else{
			parentDealerId = "-1";
		}

		if ("-1".equals(parentDealerId)) {
			TtVsAccountPO accountPO = new TtVsAccountPO();
			//String k_dealerId = CommonUtils.checkNull(request.getParamValue("k_dealerId"));
			accountPO.setDealerId(Long.parseLong(k_dealerId));
			accountPO.setAccountTypeId(Long.parseLong(fundType));
			accountPO = OrderReportDao.getInstance().geTtVsAccountPO(accountPO);
			
			dmsFreezePrice_Report(req_id+"", accountPO.getAccountId()+"", new BigDecimal(caigouMoneyAll), logonUser.getUserId()+"");
			// 扣折扣账户
			String discountAccountId = CommonUtils.checkNull(request.getParamValue("discountAccountId"));
			if (!discountAccountId.equals("")) {
				dmsFreezePrice_Report(req_id+"", discountAccountId, new BigDecimal(totalDiscountPrice), logonUser.getUserId().toString());
			}
			
			/*AccountBalanceDetailDao abdo = AccountBalanceDetailDao.getInstance();
			abdo.syncAccountFreeze(req_id+"", accountPO.getAccountId()+"", new BigDecimal(caigouMoneyAll), logonUser.getUserId()+"");
			
			// 扣折扣账户
			String discountAccountId = CommonUtils.checkNull(request.getParamValue("discountAccountId"));
			if (!discountAccountId.equals("")) {
				abdo.syncAccountFreeze(req_id+"", discountAccountId, new BigDecimal(totalDiscountPrice), logonUser.getUserId().toString());
			}*/
		}
	}
	
	public boolean chkToDfs(String reqId) {
		AccountBalanceDetailDao abd = AccountBalanceDetailDao.getInstance() ;
		
		int headCount = abd.getFirstToDfsHeadReq(reqId) ;
		
		if(0 == headCount) {
			return true ;
		}
		
		int lineCount = abd.getFirstToDfsLineReq(reqId) ;
		int dfsCount = lineCount / headCount ;
		
		List<Map<String, Object>> dfsReqDtl = abd.getFirstToDfsDtl(reqId) ;
		List<Map<String, Object>> dmsReqDtl = abd.getDmsDtl(reqId) ;
		
		int dmsCount = dmsReqDtl.size() ;
		
		if(headCount != dmsCount) {
			return true ;
		} else {
			for(int i=0; i<dfsCount; i++) {
				for(int j=0; j<dmsCount; j++) {
					if(dmsReqDtl.get(j).get("MATERIAL_CODE").toString().equals(dfsReqDtl.get(i).get("MATERIAL_CODE").toString())) {
						if(!dmsReqDtl.get(j).get("TOTAL_AMOUNT").toString().equals(dfsReqDtl.get(i).get("AMOUNT").toString())) {
							return true ;
						}
					}
				}
			}
		}
		
		return false ;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
}
