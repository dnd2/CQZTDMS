/**********************************************************************
* <pre>
* FILE : SpecialNeedQuery.java
* CLASS : SpecialNeedQuery
* AUTHOR : 
* FUNCTION : 订单提报
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.|   DATE   |    NAME    | REASON  |  CHANGE REQ.
*----------------------------------------------------------------------
*         |2010-06-17|            | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
package com.infodms.dms.actions.sales.ordermanage.orderreport;

import static com.infodms.dms.actions.sales.financemanage.AccountOpera.dmsFreezePrice_Report;

import java.math.BigDecimal;
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
import com.infodms.dms.common.relation.DealerRelation;
import com.infodms.dms.dao.sales.financemanage.AccountBalanceDetailDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.OrderReportDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.SpecialNeedDao;
import com.infodms.dms.dao.sales.storageManage.VehicleDispatchDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDateSetPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmFleetPO;
import com.infodms.dms.po.TmVhclMaterialGroupRPO;
import com.infodms.dms.po.TtVsAccountFreezePO;
import com.infodms.dms.po.TtVsAccountPO;
import com.infodms.dms.po.TtVsDlvryReqDtlPO;
import com.infodms.dms.po.TtVsDlvryReqPO;
import com.infodms.dms.po.TtVsOrderDetailPO;
import com.infodms.dms.po.TtVsOrderPO;
import com.infodms.dms.po.TtVsOrderResourceReservePO;
import com.infodms.dms.po.TtVsSpecialReqDtlPO;
import com.infodms.dms.po.TtVsSpecialReqPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.businessUtil.GetOrderNOUtil;
import com.infodms.dms.util.businessUtil.ReqLogUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * @Title: 订做车需求查询Action
 * @Description:InfoFrame3.0.V01
 * @Copyright: Copyright (c) 2010
 * @Company: www.infoservice.com.cn
 * @Date: 2010-6-17
 * @author 
 * @mail   
 * @version 1.0
 * @remark 
 */
public class SpecialNeedQuery {
	
	public Logger logger = Logger.getLogger(SpecialNeedQuery.class);   
	SpecialNeedDao dao  = SpecialNeedDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	
	private final String initUrl = "/jsp/sales/ordermanage/orderreport/specialNeedQuery.jsp";
	private final String detailUrl = "/jsp/sales/ordermanage/orderreport/specialNeedDetail.jsp";
	private final String oemInitUrl = "/jsp/sales/ordermanage/orderreport/oemSpecialNeedQuery.jsp";
	private final String customizedOrderOfBackInit = "/jsp/sales/ordermanage/orderreport/customizedOrderOfBackInit.jsp" ;
	private final String customizedOrderOfBackAdjustmentInit = "/jsp/sales/ordermanage/orderreport/customizedOrderOfBackAdjustmentInit.jsp" ;
	/**
	 * 订做车需求查询初始化
	 */
	public void specialNeedQueryInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getDealerBusiness(logonUser.getPoseId().toString());
			act.setOutData("areaList", areaList);
			act.setForword(initUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"订做车需求查询初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 订做车需求查询
	 */
	public void specialNeedQuery(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String startDate = request.getParamValue("startDate");		//起始时间
			String endDate = request.getParamValue("endDate");			//结束时间
			String areaId = request.getParamValue("areaId");			//业务范围ID
			String area = request.getParamValue("area");				//业务范围IDS
			String needStatus = request.getParamValue("needStatus");	//需求状态
			String dealerId = logonUser.getDealerId();
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getDealerSpecialNeedList(areaId, dealerId, area, needStatus, startDate, endDate, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"订做车需求查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 订做车需求查询初始化
	 */
	public void oemSpecialNeedQueryInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("areaList", areaList);
			act.setForword(oemInitUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"订做车需求查询初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 订做车需求查询
	 */
	public void oemSpecialNeedQuery(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String startDate = request.getParamValue("startDate");		//起始时间
			String endDate = request.getParamValue("endDate");			//结束时间
			String areaId = request.getParamValue("areaId");			//业务范围ID
			String area = request.getParamValue("area");				//业务范围IDS
			String needStatus = request.getParamValue("needStatus");	//需求状态
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId")) ;
			String groupId = CommonUtils.checkNull(request.getParamValue("groupId")) ;
			
			String orgId = "" ;
			String dutyType = logonUser.getDutyType() ;
			
			if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)) {
				orgId = logonUser.getOrgId().toString() ;
			}
			
			Map<String, Object> map = new HashMap<String, Object>() ;
			map.put("dealerId", dealerId) ;
			map.put("groupId", groupId) ;
			
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			
			PageResult<Map<String, Object>> ps = dao.getSpecialNeedList(map, orgId, areaId, area, needStatus, startDate, endDate, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"订做车需求查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 订做车需求明细查询
	 */
	public void specialNeedDetailQuery(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String reqId = request.getParamValue("reqId");				//需求ID
			TtVsSpecialReqPO tvsrpContion = new TtVsSpecialReqPO();
			TtVsSpecialReqPO tvsrpValue = new TtVsSpecialReqPO();
			tvsrpContion.setReqId(Long.parseLong(reqId));
			List<PO> datalist = dao.select(tvsrpContion);
			Boolean isSecond = dao.viewDealerReq(reqId) ;
			
			if(isSecond) {
				act.setOutData("isSecond", "true") ;
			} else {
				act.setOutData("isSecond", "false") ;
			}
			
			TmDealerPO dealerPO = new TmDealerPO();
			if(datalist!=null&&datalist.size()>0){
				tvsrpValue =(TtVsSpecialReqPO)datalist.get(0);
				dealerPO.setDealerId(tvsrpValue.getDealerId());
				List<PO> dealerList = dao.select(dealerPO);
				dealerPO = (TmDealerPO)dealerList.get(0);
			}
			String remark = tvsrpValue.getRefitDesc();
			String fleetName = "";
			if(tvsrpValue.getFleetId() != null){
				TmFleetPO fleet = new TmFleetPO();
				fleet.setFleetId(tvsrpValue.getFleetId());
				List<PO> fleetList = dao.select(fleet);
				if(fleetList.size() != 0){
					fleet = (TmFleetPO) fleetList.get(0);
					fleetName = fleet.getFleetName();
				}
			}
			List<Map<String, Object>> list = dao.getSpecialNeedDetailListByQuery(reqId);
			List<Map<String, Object>> checkList = dao.getSpecialNeedCheck(reqId);
			
			Map<String, String> map = new HashMap<String, String>() ;
			String orderId = request.getParamValue("orderId") ;
			map.put("orderId", orderId) ;
			List<Map<String, Object>> orderList = dao.getOrderInfo(map) ;
			
			act.setOutData("orderList", orderList) ;
			act.setOutData("list", list);
			act.setOutData("checkList", checkList);
			act.setOutData("reqId", reqId);
			act.setOutData("fleetName", fleetName);
			act.setOutData("remark", remark);
			act.setOutData("dealerPO", dealerPO);
			act.setForword(detailUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DETAIL_FAILURE_CODE,"订做车需求明细查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void customizedOrderOfBackInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getDealerBusiness(logonUser.getPoseId().toString());
			act.setOutData("areaList", areaList);
			act.setForword(customizedOrderOfBackInit);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"订做车订单调整初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void customizedOrderOfBackQuery(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId")) ;
			String orderNo = CommonUtils.checkNull(request.getParamValue("orderNo")) ;
			
			Map<String, String> map = new HashMap<String, String>() ;
			map.put("areaId", areaId) ;
			map.put("orderNo", orderNo) ;
			map.put("orderType", Constant.ORDER_TYPE_03.toString()) ;
			map.put("orderStatus", Constant.ORDER_STATUS_04.toString()) ;
			
			DealerRelation dr = new DealerRelation () ;
			String dealerId = dr.getDealerIdByPose(logonUser.getCompanyId(), logonUser.getPoseId()) ;
			
			map.put("dealerId", dealerId) ;
			
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.orderQuery(map, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"订做车订单调整查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void customizedOrderOfBackAdjustmentInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId")) ;
			
			TtVsOrderPO tvo = new TtVsOrderPO() ;
			tvo.setOrderId(new Long(orderId)) ;
			tvo = (TtVsOrderPO)dao.select(tvo).get(0) ;
			
			if(tvo.getIsFleet().intValue() == 1) {
				TmFleetPO tf = new TmFleetPO() ;
				tf.setFleetId(new Long(tvo.getFleetId())) ;
				tf = (TmFleetPO)dao.select(tf).get(0) ;
				
				act.setOutData("tf", tf) ;
			}
			
			String reqId = tvo.getSpecialReqId().toString() ;
			
			TtVsSpecialReqPO tvsrpContion = new TtVsSpecialReqPO();
			TtVsSpecialReqPO tvsrpValue = new TtVsSpecialReqPO();
			tvsrpContion.setReqId(Long.parseLong(reqId));
			List<PO> datalist = dao.select(tvsrpContion);
			TtVsSpecialReqPO reqPO = (TtVsSpecialReqPO)datalist.get(0);
			
			if(datalist!=null&&datalist.size()>0){
				tvsrpValue =(TtVsSpecialReqPO)datalist.get(0);
			}
			String remark = tvsrpValue.getRefitDesc();
			String areaId = tvsrpValue.getAreaId().toString();
			String priceName = "";
			if(tvsrpValue.getPriceId()!=null&&!("").equals(tvsrpValue.getPriceId())){
				priceName = dao.getPriceName(tvsrpValue.getPriceId());
			}
			//String dealerId = tvsrpValue.getDealerId().toString();
			String dealerId = request.getParamValue("dealerId");		//经销商ID
			List<Map<String, Object>> list = dao.getSpecialNeedDetailListNew(reqId, orderId);
			List<Map<String, Object>> checkList = dao.getSpecialNeedCheck(reqId);
			
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getDealerBusiness(logonUser.getPoseId().toString());
			
			String parentDlrId = null ;
			
			if(dao.viewDealerLever(dealerId)) {
				parentDlrId = VehicleDispatchDAO.getParentDealerId(dealerId).get("PARENT_DEALER_D").toString() ;
			} 
			
			if(CommonUtils.isNullString(parentDlrId)) {
				parentDlrId = dealerId ;
			}
			
			List<Map<String, Object>> accList = dao.getDealerAccount(parentDlrId);
			String fleetName = "";
			if(tvsrpValue.getFleetId() != null){
				TmFleetPO fleet = new TmFleetPO();
				fleet.setFleetId(tvsrpValue.getFleetId());
				List<PO> fleetList = dao.select(fleet);
				if(fleetList.size() != 0){
					fleet = (TmFleetPO) fleetList.get(0);
					fleetName = fleet.getFleetName();
				}
			}
			
			TmDealerPO td = new TmDealerPO() ;
			td.setDealerId(Long.parseLong(parentDlrId)) ;
			
			td = (TmDealerPO)dao.select(td).get(0) ;
			
			Boolean boo = dao.viewDealerLever(dealerId);
			
			List<Map<String, Object>> matList = dao.getCustomizedOrderMate(orderId) ;
			
			Boolean isSecond = dao.viewDealerLever(reqPO.getDealerId().toString()) ;
			
			act.setOutData("isSecond", isSecond) ;
			
			act.setOutData("tvo", tvo) ;
			act.setOutData("matList", matList) ;
			act.setOutData("parentErpCode", td.getErpCode()) ;
			act.setOutData("parentDlrId", parentDlrId) ;
			act.setOutData("areaList", areaList);
			act.setOutData("accList",accList);
			act.setOutData("accTypeId", tvsrpValue.getAccountTypeId());
			act.setOutData("list", list);
			act.setOutData("checkList", checkList);
			act.setOutData("reqId", reqId);
			act.setOutData("dealerId", dealerId);
			act.setOutData("areaId", areaId);
			act.setOutData("fleetName", fleetName);
			act.setOutData("remark", remark);
			act.setOutData("priceName", priceName);
			act.setOutData("reqPO", reqPO);
			act.setOutData("boo", boo);
			act.setForword(customizedOrderOfBackAdjustmentInit);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"订做车订单调整明细初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void specialNeedToAdjustment(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String reqId = request.getParamValue("reqId");						//需求ID
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId")) ;
			String dealerId = request.getParamValue("dealerId");				//经销商ID
			String fundType = request.getParamValue("fundType");				//资金类型
			String priceId = request.getParamValue("priceTypeId");				    //价格类型
			String otherPriceReason = CommonUtils.checkNull(request.getParamValue("otherPriceReason"));	// 使用其他价格原因
			String[] dtlId = request.getParamValues("dtlId");					//订做车需求明细id
			String[] groupId = request.getParamValues("groupId");				//配置id
			String[] materialId = request.getParamValues("materialId");			//物料id
			String[] amount = request.getParamValues("amount");					//数量
			//String[] applyAmount = request.getParamValues("applyAmount");		//需求数量
			String[] singlePrice = request.getParamValues("salePrice");   	// 单价
//			String[] discountRate = request.getParamValues("discountRate");		// 折扣率
//			String[] discountSPrice = request.getParamValues("discountSPrice");	// 折扣后单价
//			String[] discountPrice = request.getParamValues("discountPrice");	// 折扣额
			String[] specialBatchNo = request.getParamValues("specialBatchNo");	// 特殊批次号
			String isCover = CommonUtils.checkNull(request.getParamValue("isCover"));					// 是否代交车
			String deliveryType = CommonUtils.checkNull(request.getParamValue("deliveryType"));
			String deliveryAddress = CommonUtils.checkNull(request.getParamValue("deliveryAddress"));
			String receiver = CommonUtils.checkNull(request.getParamValue("receiver"));					// 收货方
			String linkMan = CommonUtils.checkNull(request.getParamValue("linkMan"));					// 联系人
			String tel = CommonUtils.checkNull(request.getParamValue("tel"));							// 联系电话
			//String refitRemark = request.getParamValue("refitRemark");			//改装说明
			String orderRemark = request.getParamValue("orderRemark");			//备注说明
			String fleetId = CommonUtils.checkNull(request.getParamValue("fleetId"));
			String fleetAddress = CommonUtils.checkNull(request.getParamValue("fleetAddress"));
			String accountId = CommonUtils.checkNull(request.getParamValue("accountId"));// 资金账户
			String orderTotalPrice = CommonUtils.checkNull(request.getParamValue("orderTotalPrice"));//订单总价
			String discountAccountId = CommonUtils.checkNull(request.getParamValue("discountAccountId"));// 折扣账户
			String totalDiscountPrice = CommonUtils.checkNull(request.getParamValue("totalDiscountPrice"));//折扣总额
			String reqTotalAmount = CommonUtils.checkNull(request.getParamValue("reqTotalAmount"));//发运申请总数量
			
			String flag = "1"; //是否已全部生成订单标志
			
			Boolean isSecond = dao.viewDealerLever(dealerId) ;
			
			String parentDlrId = dealerId ;
			
			if(isSecond) {
				parentDlrId = VehicleDispatchDAO.getParentDealerId(dealerId).get("PARENT_DEALER_D").toString() ;
			} 
			
			TtVsDlvryReqPO tvdr = new TtVsDlvryReqPO() ;
			tvdr.setOrderId(new Long(orderId)) ;
			List<?> list = dao.select(tvdr) ;
			
			if(!CommonUtils.isNullList(list)) {
				tvdr = (TtVsDlvryReqPO)list.get(0) ;
				
				TtVsDlvryReqDtlPO tvdrd = new TtVsDlvryReqDtlPO() ;
				tvdrd.setReqId(tvdr.getReqId()) ;
				List<?> reqDtlList = dao.select(tvdrd) ;
				
				int len = reqDtlList.size() ;
				
				TtVsOrderResourceReservePO tvorr = null ;
				for(int i=0; i<len; i++) {
					tvorr = new TtVsOrderResourceReservePO() ;
					
					if(!CommonUtils.isNullString(((TtVsDlvryReqDtlPO)reqDtlList.get(i)).getDetailId().toString())) {
						tvorr.setReqDetailId(((TtVsDlvryReqDtlPO)reqDtlList.get(i)).getDetailId()) ;
						
						dao.delete(tvorr) ;
					}
				}
				
				dao.delete(tvdrd) ;
				
				if(isSecond) {
					TtVsDlvryReqPO delTvdr = new TtVsDlvryReqPO() ;
					delTvdr.setReqId(tvdr.getReqId()) ;
					dao.delete(delTvdr) ;
				}
			}
			
			TtVsOrderDetailPO tvod = new TtVsOrderDetailPO() ;
			tvod.setOrderId(new Long(orderId)) ;
			dao.delete(tvod) ;
			
			TtVsOrderPO oldOrder = new TtVsOrderPO() ;
			oldOrder.setOrderId(new Long(orderId)) ;
			
			TtVsOrderPO tvop = new TtVsOrderPO();
			tvop.setOrderRemark(orderRemark);
			//tvop.setRefitRemark(refitRemark);
			
			//产品套餐
			TtVsSpecialReqPO tvsr = new TtVsSpecialReqPO() ;
			tvsr.setReqId(Long.parseLong(reqId)) ;
			tvsr = (TtVsSpecialReqPO)dao.select(tvsr).get(0) ;
			tvop.setProductComboId(tvsr.getProductComboId()) ;
			
			if(!isSecond) {
				tvop.setOrderPrice(Double.parseDouble(orderTotalPrice));
				tvop.setOrderStatus(Constant.ORDER_STATUS_03);
			} else {
				tvop.setOrderStatus(Constant.ORDER_STATUS_02);
			}
			
			tvop.setFundTypeId(Long.parseLong(fundType));
			
			if(!isSecond) {
				tvop.setPriceId(priceId);
				tvop.setOtherPriceReason(otherPriceReason);
			}
			
			if("1".equals(isCover)){	
				tvop.setFleetId(new Long(fleetId));
				tvop.setFleetAddress(fleetAddress);
				
				tvop.setIsFleet(1);
			} else{
				tvop.setDeliveryType(new Integer(deliveryType));
				if(tvop.getDeliveryType().intValue() == Constant.TRANSPORT_TYPE_02.intValue()){
					tvop.setReceiver(new Long(receiver));
					tvop.setDeliveryAddress(new Long(deliveryAddress));
					tvop.setLinkMan(linkMan);
					tvop.setTel(tel);
				} else{
					tvop.setReceiver(new Long(dealerId));
				}
				
				tvop.setIsFleet(0);
			}
			
			//tvop.setVer(0);
			//tvop.setRaiseDate(new Date(System.currentTimeMillis()));
			tvop.setUpdateDate(new Date(System.currentTimeMillis()));
			tvop.setUpdateBy(logonUser.getUserId());
			dao.update(oldOrder, tvop) ;
			
			tvop = (TtVsOrderPO)dao.select(oldOrder).get(0) ;
			
			OrderReportDao reportDao = OrderReportDao.getInstance();
			
			if(!isSecond) {
				//Long dlvryReqId = tvdr.getReqId() ;
				reportDao.updateTtVsDlvryReq(tvop, new Integer(reqTotalAmount));
				
				//向发运申请操作日志表写入日志信息
				ReqLogUtil.creatReqLog(tvdr.getReqId(), Constant.REQ_LOG_TYPE_01, logonUser.getUserId());
			} 
			
			for(int i=0 ; i<materialId.length ; i++){
					//插入订单明细表
					TtVsOrderDetailPO tvodp = new TtVsOrderDetailPO();
					tvodp.setDetailId(Long.parseLong(SequenceManager.getSequence("")));
					tvodp.setOrderId(tvop.getOrderId());
					tvodp.setMaterialId(Long.parseLong(materialId[i]));
					tvodp.setOrderAmount(new Integer(amount[i]));
					
					if(!isSecond) {	
						tvodp.setSinglePrice(new Double(singlePrice[i]));
						/*tvodp.setTotalPrice(new Double(totalPrice[i]) - new Double(discountPrice[i]));*/
						tvodp.setTotalPrice(new Double(singlePrice[i]) * new Double(amount[i]));
					}
					
					tvodp.setSpecialBatchNo(specialBatchNo[i]);
					
					if(!isSecond) {	
						tvodp.setDiscountRate(0F);
						tvodp.setDiscountSPrice(new Double(singlePrice[i]));
						tvodp.setDiscountPrice(0D);
					}
					
					tvodp.setVer(0);
					tvodp.setCreateDate(new Date(System.currentTimeMillis()));
					tvodp.setCreateBy(logonUser.getUserId());
					dao.insert(tvodp);	
					
				if(!isSecond) {	
					// 插入发运申请明细表
					reportDao.insertTtVsDlvryReqDtl(tvodp, tvdr.getReqId());
				}
			}
			
			Map<String, String> map = new HashMap<String, String>() ;
			map.put("reqId", reqId) ;
			//modify by WHX,2012.09.12
			//======================================================START
			map.put("isStatus", Constant.ORDER_STATUS_06.toString()) ;
			//======================================================END
			
			List<Map<String, Object>> resultlist = dao.chkOrderAmount(map) ;
			
			int len = resultlist.size() ;
			
			for(int i=0; i<len; i++) {
				Integer reqAmount = ((BigDecimal)resultlist.get(i).get("AMOUNT")).intValue() ;
				Integer orderAmount = ((BigDecimal)resultlist.get(i).get("ORDER_AMOUNT")).intValue() ;
				
				if(orderAmount < reqAmount) {
					flag = "0" ;
					
					break ;
				}
			}
			
			for(int i=0; i<dtlId.length; i++) {
				// 订做车需求明细更新
				int apply_amount = 0;	//配额数量
				int order_amount = 0;	//已提报订单数量
				for(int j=0; j<materialId.length; j++) {
					TtVsSpecialReqDtlPO tvsrdpValue = new TtVsSpecialReqDtlPO();
					tvsrdpValue.setDtlId(Long.valueOf(dtlId[i]));
					List<PO> tvsrdpList = dao.select(tvsrdpValue);
					
					tvsrdpValue = (TtVsSpecialReqDtlPO) (tvsrdpList.size() > 0 ? tvsrdpList.get(0) : null);
					apply_amount = tvsrdpValue.getAmount();  //车厂预定数量
					Map<String, Object> groupMap = dao.getOrderAmountAndGroupId(materialId[j], reqId);
					BigDecimal result =  (groupMap != null ? (BigDecimal) groupMap.get("ORDER_AMOUNT") : null);
					BigDecimal gId = (groupMap != null ? (BigDecimal) groupMap.get("MATERIAL_ID") : null);
					
					if(gId != null && gId.toString().equals(groupId[i])) {
						order_amount = (result == null ? 0 : result.intValue());
						TtVsSpecialReqDtlPO tvsrdp1 = new TtVsSpecialReqDtlPO();
						tvsrdp1.setDtlId(new Long(dtlId[i]));
						TtVsSpecialReqDtlPO tvsrdp2 = new TtVsSpecialReqDtlPO();
						tvsrdp2.setOrderAmount(new Integer(order_amount));
						dao.update(tvsrdp1, tvsrdp2);
						
						/*if(order_amount < apply_amount){
							flag = "0";
						}*/
					}
				}
			}
			
			TtVsSpecialReqPO reqPO = dao.getTtVsSpecialReqPO(new Long(reqId));
			
			if(flag.equals("1")){
				//更新订做车需求表
				TtVsSpecialReqPO tvsrpContion = new TtVsSpecialReqPO();
				TtVsSpecialReqPO tvsrpValue = new TtVsSpecialReqPO();
				tvsrpContion.setReqId(reqPO.getReqId());
				tvsrpValue.setReqStatus(Constant.SPECIAL_NEED_STATUS_06);
				tvsrpValue.setUpdateBy(logonUser.getUserId());
				tvsrpValue.setUpdateDate(new Date(System.currentTimeMillis()));
				dao.update(tvsrpContion, tvsrpValue);  
				
				//modify by WHX,2012.09.12
				//将预扣释放修改到车辆发送完成
				//======================================================START
//				if(!isSecond) {
//					TtVsAccountFreezePO tvaf = new TtVsAccountFreezePO() ;
//					tvaf.setBuzId(reqPO.getReqId()) ;
//					tvaf.setStatus(Constant.ACCOUNT_FREEZE_STATUS_01) ;
//					List<?> lis = dao.select(tvaf) ;
//					
//					if(!CommonUtils.isNullList(lis)) {
//						tvaf = (TtVsAccountFreezePO)lis.get(0) ;
//						
//						if(tvaf.getFreezeAmount().intValue() != 0) {
//							//释放预扣资金
//							Double pre_amount = reqPO.getPreAmount();//预付款金额
//							Long account_type_id = reqPO.getAccountTypeId();//账户类型ID
//							Long account_id = new Long(0);// 账户id
//							TtVsAccountPO tPO = new TtVsAccountPO();
//							tPO.setDealerId(Long.parseLong(dealerId));
//							tPO.setAccountTypeId(account_type_id);
//							List<PO> tList = dao.select(tPO);
//							if (null != tList && tList.size()>0) {
//								tPO = (TtVsAccountPO)tList.get(0);
//								account_id = tPO.getAccountId(); 
//							}
//							TtVsAccountPO tvapCondition = new TtVsAccountPO();
//							tvapCondition.setAccountId(account_id);
//							TtVsAccountPO tvapValue = new TtVsAccountPO();
//							if(tPO != null && tPO.getFreezeAmount() != null && tPO.getAvailableAmount() != null) {
//								tvapValue.setFreezeAmount(tPO.getFreezeAmount().doubleValue() - pre_amount.doubleValue());
//								tvapValue.setAvailableAmount(tPO.getAvailableAmount().doubleValue() + pre_amount.doubleValue());
//							} else {
//								tvapValue.setFreezeAmount(0D);
//								tvapValue.setAvailableAmount(0D);
//							}
//							dao.update(tvapCondition, tvapValue);
//							if(0 != pre_amount){
//								AccountBalanceDetailDao accoutDao = AccountBalanceDetailDao.getInstance();
//								
//								accoutDao.accountRelease(account_id+"", reqPO.getReqId()+"", pre_amount+"", logonUser.getUserId()+"", Constant.Freeze_TYPE_01);
//							}
//						}
//					}
//				}
				//======================================================END
			}
			
			//if("0".equals(isCheck)){
			if(!isSecond) {
				// 冻结资金
				if(!accountId.equals("")){
					dmsFreezePrice_Report(tvdr.getReqId().toString(), accountId, new BigDecimal(orderTotalPrice), logonUser.getUserId().toString());
				}
				if(!discountAccountId.equals("")){
					dmsFreezePrice_Report(tvdr.getReqId().toString(), discountAccountId, new BigDecimal(totalDiscountPrice), logonUser.getUserId().toString());
				}
			}
			
			act.setOutData("returnValue", 1);
		} catch(RuntimeException e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.PUTIN_FAILURE_CODE,e.getMessage());
			logger.error(logonUser,e1);
			act.setException(e);
		} catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.PUTIN_FAILURE_CODE,"订做车订单调整提报");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void chkCustomizedOrderByOrderAmount() {
		String[] amount = request.getParamValues("amount") ;
		String[] materialId = request.getParamValues("materialId") ;
		
		Map<String, Integer> amountMap = new HashMap<String, Integer>() ;
		
		if(materialId != null) {
			int len = materialId.length ;
			
			for(int i=0; i<len; i++) {
				TmVhclMaterialGroupRPO tvmgr = new TmVhclMaterialGroupRPO() ;
				tvmgr.setMaterialId(Long.parseLong(materialId[i])) ;
				tvmgr = (TmVhclMaterialGroupRPO)dao.select(tvmgr).get(0) ;
				
				if(amountMap.containsKey(tvmgr.getGroupId().toString())) {
					amountMap.put(tvmgr.getGroupId().toString(), amountMap.get(tvmgr.getGroupId().toString()) + new Integer(amount[i])) ;
				} else {
					amountMap.put(tvmgr.getGroupId().toString(), new Integer(amount[i])) ;
				}
			}
		}
		
		String reqId = CommonUtils.checkNull(request.getParamValue("reqId")) ;
		String orderId = CommonUtils.checkNull(request.getParamValue("orderId")) ;
		
		Map<String, String> map = new HashMap<String, String>() ;
		map.put("reqId", reqId) ;
		map.put("orderId", orderId) ;
		map.put("isStatus", "true") ;
		
		List<Map<String, Object>> list = dao.chkOrderAmount(map) ;
		
		if(!CommonUtils.isNullList(list)) {
			int len = list.size() ;
			
			for(int i=0; i<len; i++) {
				Map<String, Object> resultMap = list.get(i) ;
				
				if(amountMap.containsKey(resultMap.get("MATERIAL_ID").toString())) {
					if(amountMap.get(resultMap.get("MATERIAL_ID").toString()).intValue() + Integer.parseInt(resultMap.get("ORDER_AMOUNT").toString()) > Integer.parseInt(resultMap.get("AMOUNT").toString())) {
						act.setOutData("amountFlag", -1) ;
						
						return ;
					}
				}
			}
		}
		
		act.setOutData("amountFlag", 1) ;
	}
}
