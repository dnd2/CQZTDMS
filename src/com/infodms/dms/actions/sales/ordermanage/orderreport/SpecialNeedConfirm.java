/**********************************************************************
* <pre>
* FILE : SpecialNeedConfirm.java
* CLASS : SpecialNeedConfirm
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
import com.infodms.dms.dao.productmanage.ProductManageDao;
import com.infodms.dms.dao.sales.financemanage.AccountBalanceDetailDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.OrderReportDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.SpecialNeedDao;
import com.infodms.dms.dao.sales.storageManage.VehicleDispatchDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmBusinessParaPO;
import com.infodms.dms.po.TmDateSetPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmFleetPO;
import com.infodms.dms.po.TmVhclMaterialGroupRPO;
import com.infodms.dms.po.TmVhclMaterialPO;
import com.infodms.dms.po.TtVsAccountFreezePO;
import com.infodms.dms.po.TtVsAccountPO;
import com.infodms.dms.po.TtVsOrderDetailPO;
import com.infodms.dms.po.TtVsOrderPO;
import com.infodms.dms.po.TtVsSpecialReqDtlPO;
import com.infodms.dms.po.TtVsSpecialReqPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.businessUtil.GetOrderNOUtil;
import com.infodms.dms.util.businessUtil.ReqLogUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.ActionContextExtend;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

import static com.infodms.dms.actions.sales.financemanage.AccountOpera.*;

/**
 * @Title: 订做车需求确认Action
 * @Description:InfoFrame3.0.V01
 * @Copyright: Copyright (c) 2010
 * @Company: www.infoservice.com.cn
 * @Date: 2010-6-17
 * @author 
 * @mail   
 * @version 1.0
 * @remark 
 */
public class SpecialNeedConfirm  {

	public Logger logger = Logger.getLogger(SpecialNeedConfirm.class);   
	SpecialNeedDao dao  = SpecialNeedDao.getInstance();
	private final OrderReportDao reportDao = OrderReportDao.getInstance();
	private final AccountBalanceDetailDao accoutDao = AccountBalanceDetailDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	
	private final String initUrl = "/jsp/sales/ordermanage/orderreport/specialNeedConfirmQuery.jsp";
	private final String detailUrl = "/jsp/sales/ordermanage/orderreport/specialNeedDetailConfirm.jsp";
	private final String orderUrl = "/jsp/sales/ordermanage/orderreport/specialNeedToOrder.jsp";
	private final String dlrFleetUrl = "/jsp/sales/ordermanage/orderreport/fleetQuery.jsp";
	private final String initDo = "/sales/ordermanage/orderreport/SpecialNeedConfirm/specialNeedConfirmInit.do";
	/**
	 * 订做车需求确认初始化
	 */
	public void specialNeedConfirmInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getDealerBusiness(logonUser.getPoseId().toString());
			act.setOutData("areaList", areaList);
			act.setForword(initUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"订做车需求确认初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 订做车需求确认查询
	 */
	public void specialNeedConfirmQuery(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String startDate = request.getParamValue("startDate");		//起始时间
			String endDate = request.getParamValue("endDate");			//结束时间
			String areaId = request.getParamValue("areaId");			//业务范围ID
			String area = request.getParamValue("area");				//业务范围IDS
			
			String dealerId = logonUser.getDealerId() ;
			
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getSpecialNeedConfirmQuery(dealerId, areaId, area, startDate, endDate, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"订做车需求确认查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 订做车需求确认明细查询
	 */
	public void specialNeedDetailConfirmInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String reqId = request.getParamValue("reqId");				//需求ID
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
			List<Map<String, Object>> list = dao.getSpecialNeedDetailList(reqId);
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
			act.setForword(detailUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DETAIL_FAILURE_CODE,"订做车需求确认明细查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 订做车需求确认---放弃订购
	 */
	public void specialNeedDetailConfirm(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String reqId = request.getParamValue("reqId");				//需求ID
			TtVsSpecialReqPO tvsrpContion = new TtVsSpecialReqPO();
			TtVsSpecialReqPO tvsrpValue = new TtVsSpecialReqPO();
			tvsrpContion.setReqId(Long.parseLong(reqId));
			tvsrpValue.setReqStatus(Constant.SPECIAL_NEED_STATUS_07);
			tvsrpValue.setUpdateBy(logonUser.getUserId());
			tvsrpValue.setUpdateDate(new Date(System.currentTimeMillis()));
			dao.update(tvsrpContion, tvsrpValue);  				//更新订做车需求表
			act.setOutData("returnValue", 1);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DETAIL_FAILURE_CODE,"订做车需求放弃订购");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 订做车需求确认---生成订单页面初始化
	 */
	public void specialNeedToReportInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			ActionContextExtend atx = (ActionContextExtend) ActionContext.getContext();
			String reqURL = atx.getRequest().getContextPath();
			if("/CVS-SALES".equals(reqURL.toUpperCase())){
				act.setOutData("returnValue", 1);
			}else{
				act.setOutData("returnValue", 2);
			}
			String reqId = request.getParamValue("reqId");				//需求ID
			//String areaId = request.getParamValue("areaId");			//业务范围ID
			//String dealerId = request.getParamValue("dealerId");		//经销商ID
			//String remark = request.getParamValue("remark");			//改装说明
			// 获得是否需要资金检查
			TmBusinessParaPO para = dao.getTmBusinessParaPO(Constant.URGENT_ORDER_ACCOUNT_CHECK_PARA);
			String isCheck = para.getParaValue();
			TtVsSpecialReqPO reqPo = dao.getTtVsSpecialReqPO(new Long(reqId));
			
			//modify by WHX
			//-------------------------------------------------------start
			List<Map<String, Object>> list = dao.getSpecialNeedDetailListNew(reqId, "");  
			//-------------------------------------------------------end
			
			//List<Map<String, Object>> list = dao.getSpecialNeedtoReportDetail(reqId, logonUser.getOemCompanyId());
			//List<Map<String, Object>> accList = dao.getDealerAccount(dealerId);
			//List<Map<String, Object>> addList = dao.getDealerAddress(dealerId);
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getDealerBusiness(logonUser.getPoseId().toString());
			// 获得订单启票最大折扣点
			TmBusinessParaPO para2 = dao.getTmBusinessParaPO(
					Constant.ORDER_REPORT_MAX_DISCOUNT_RATE_PARA, new Long(logonUser.getOemCompanyId()));
			String ratePara = para2.getParaValue();
			
			Boolean isSecond = dao.viewDealerLever(reqPo.getDealerId().toString()) ;
			
			act.setOutData("isSecond", isSecond) ;
			act.setOutData("areaList", areaList);
			act.setOutData("list",list);
			//act.setOutData("accList",accList);
			//act.setOutData("addList",addList);
			//act.setOutData("areaId", areaId);
			//act.setOutData("dealerId", dealerId);
			act.setOutData("isCheck", isCheck);
			//act.setOutData("remark", remark);
			//act.setOutData("reqId", reqId);
			act.setOutData("reqPo", reqPo);
			act.setOutData("ratePara", ratePara);
			act.setForword(orderUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DETAIL_FAILURE_CODE,"订做车需求生成订单页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 订做车需求确认---生成订单
	 */
	public void specialNeedToReport(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String reqId = request.getParamValue("reqId");						//需求ID
			String areaId = request.getParamValue("area");					    //业务范围ID
			String dealerId = request.getParamValue("dealerId");				//经销商ID
			String fundType = request.getParamValue("fundType");				//资金类型
			String priceId = request.getParamValue("priceTypeId");				    //价格类型
			String otherPriceReason = CommonUtils.checkNull(request.getParamValue("otherPriceReason"));	// 使用其他价格原因
			String[] dtlId = request.getParamValues("dtlId");					//订做车需求明细id
			String[] groupId = request.getParamValues("groupId");				//配置id
			String[] materialId = request.getParamValues("materialId");			//物料id
			String[] amount = request.getParamValues("amount");					//数量
			String[] applyAmount = request.getParamValues("applyAmount");		//需求数量
			String[] orderAmount = request.getParamValues("orderAmount");		//已提报订单数量
			String[] singlePrice = request.getParamValues("salePrice");   	// 单价
			String[] discountRate = request.getParamValues("discountRate");		// 折扣率
			String[] totalPrice = request.getParamValues("totalPrice");			// 金额
			String[] discountSPrice = request.getParamValues("discountSPrice");	// 折扣后单价
			String[] discountPrice = request.getParamValues("discountPrice");	// 折扣额
			String[] specialBatchNo = request.getParamValues("specialBatchNo");	// 特殊批次号
			String isCover = CommonUtils.checkNull(request.getParamValue("isCover"));					// 是否代交车
			String deliveryType = CommonUtils.checkNull(request.getParamValue("deliveryType"));
			String deliveryAddress = CommonUtils.checkNull(request.getParamValue("deliveryAddress"));
			String receiver = CommonUtils.checkNull(request.getParamValue("receiver"));					// 收货方
			String linkMan = CommonUtils.checkNull(request.getParamValue("linkMan"));					// 联系人
			String tel = CommonUtils.checkNull(request.getParamValue("tel"));							// 联系电话
			String refitRemark = request.getParamValue("refitRemark");			//改装说明
			String orderRemark = request.getParamValue("orderRemark");			//备注说明
			String fleetId = CommonUtils.checkNull(request.getParamValue("fleetId"));
			String fleetAddress = CommonUtils.checkNull(request.getParamValue("fleetAddress"));
			String isCheck = request.getParamValue("isCheck");					//资金开关参数
			String accountId = CommonUtils.checkNull(request.getParamValue("accountId"));// 资金账户
			String orderTotalPrice = CommonUtils.checkNull(request.getParamValue("orderTotalPrice"));//订单总价
			String discountAccountId = CommonUtils.checkNull(request.getParamValue("discountAccountId"));// 折扣账户
			String totalDiscountPrice = CommonUtils.checkNull(request.getParamValue("totalDiscountPrice"));//折扣总额
			String reqTotalAmount = CommonUtils.checkNull(request.getParamValue("reqTotalAmount"));//发运申请总数量
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			String flag = "1"; //是否已全部生成订单标志
			
			Boolean isSecond = dao.viewDealerLever(dealerId) ;
			
			String parentDlrId = dealerId ;
			
			if(isSecond) {
				parentDlrId = VehicleDispatchDAO.getParentDealerId(dealerId).get("PARENT_DEALER_D").toString() ;
			} 
			
			// 得到当前年度月份周度
			TmDateSetPO dateSet = reportDao.getTmDateSetPO(new Date(), oemCompanyId);
			String year = dateSet.getSetYear();
			String month = dateSet.getSetMonth();
			String week = dateSet.getSetWeek();

			// 获得订单编号
			TmDealerPO dealerPO = new TmDealerPO();
			dealerPO.setDealerId(Long.parseLong(dealerId));
			List<PO> dealerList = dao.select(dealerPO);
			String dealerCode = ((TmDealerPO)dealerList.get(0)).getDealerCode();
			Map<String,String> codeMap  = GetOrderNOUtil.getAreaShortcode(areaId);
			String areaCode = codeMap.get("AREA_SHORTCODE");
			String orderNO = GetOrderNOUtil.getOrderNO(areaCode,Constant.ORDER_TYPE_03+"", "D", dealerCode);
			
			//插入订单表
			TtVsOrderPO tvop = new TtVsOrderPO();
			tvop.setOrderId(Long.parseLong(SequenceManager.getSequence("")));
			tvop.setAreaId(Long.parseLong(areaId));
			tvop.setCompanyId(oemCompanyId);
			tvop.setOrderOrgId(Long.parseLong(dealerId));
			tvop.setOrderOrgType(logonUser.getOrgType());
			tvop.setOrderYear(Integer.parseInt(year));
			tvop.setOrderMonth(Integer.parseInt(month));
			tvop.setOrderWeek(Integer.parseInt(week));
			tvop.setBillingOrgType(Long.parseLong(logonUser.getOrgType().toString()));
			tvop.setBillingOrgId(Long.parseLong(parentDlrId));
			tvop.setOrderNo(orderNO);
			tvop.setOrderType(Constant.ORDER_TYPE_03);
			tvop.setIsRefitOrder(1);
			tvop.setOrderRemark(orderRemark);
			tvop.setRefitRemark(refitRemark);
			
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
			}
			else{
				tvop.setDeliveryType(new Integer(deliveryType));
				if(tvop.getDeliveryType().intValue() == Constant.TRANSPORT_TYPE_02.intValue()){
					tvop.setReceiver(new Long(receiver));
					tvop.setDeliveryAddress(new Long(deliveryAddress));
					tvop.setLinkMan(linkMan);
					tvop.setTel(tel);
				}
				else{
					tvop.setReceiver(new Long(dealerId));
				}
				tvop.setIsFleet(0);
			}
			tvop.setVer(0);
			tvop.setRaiseDate(new Date(System.currentTimeMillis()));
			tvop.setCreateDate(new Date(System.currentTimeMillis()));
			tvop.setCreateBy(logonUser.getUserId());
			tvop.setSpecialReqId(new Long(reqId));
			dao.insert(tvop);
			
			Long dlvryReqId = Long.parseLong(SequenceManager.getSequence(""));
			if(!isSecond) {
				// 插入发运申请明细
				reportDao.insertTtVsDlvryReq(tvop, dlvryReqId, new Integer(reqTotalAmount));
				//向发运申请操作日志表写入日志信息
				ReqLogUtil.creatReqLog(dlvryReqId, Constant.REQ_LOG_TYPE_01, logonUser.getUserId());
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
						tvodp.setDiscountRate(discountRate == null || discountRate[i] == null || "".equals(discountRate[i]) ? 0 : new Float(discountRate[i]));
						tvodp.setDiscountSPrice(new Double(singlePrice[i]) - new Double(discountSPrice[i]));
						tvodp.setDiscountPrice(new Double(discountPrice[i]));
					}
					tvodp.setVer(0);
					tvodp.setCreateDate(new Date(System.currentTimeMillis()));
					tvodp.setCreateBy(logonUser.getUserId());
					dao.insert(tvodp);		
				if(!isSecond) {	
					// 插入发运申请明细表
					reportDao.insertTtVsDlvryReqDtl(tvodp, dlvryReqId);
				}
				
			}
			
			//判断是否提报完所有可提报数量
			Map<String, String> map = new HashMap<String, String>();
			map.put("reqId", reqId);
			//modify by WHX,2012.09.12
			//======================================================START
			map.put("isStatus", Constant.ORDER_STATUS_06.toString()) ;
			//======================================================END
			
			List<Map<String, Object>> materialOrderAmountList =  dao.chkOrderAmount(map) ;
			
			int len = materialOrderAmountList.size() ;
			
			for(int i=0; i<len; i++) {
				int orderDtlAmount = ((BigDecimal)materialOrderAmountList.get(i).get("ORDER_AMOUNT")).intValue();
				int reqDtlAmount = ((BigDecimal)materialOrderAmountList.get(i).get("AMOUNT")).intValue();
				
				if(orderDtlAmount < reqDtlAmount) {
					flag = "0";
					
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
					/*Map<String, Object> tvmgrMap = dao.getGroupIdByMaterialId(mId.toString());
					BigDecimal gId = (tvmgrMap != null ? (BigDecimal) tvmgrMap.get("GROUP_ID") : null);*/
					if(gId != null && gId.toString().equals(groupId[i])) {
						order_amount = (result == null ? 0 : result.intValue());
						/*order_amount +=  Integer.parseInt(amount[j]);//已提报订单数量*/
						TtVsSpecialReqDtlPO tvsrdp1 = new TtVsSpecialReqDtlPO();
						tvsrdp1.setDtlId(new Long(dtlId[i]));
						TtVsSpecialReqDtlPO tvsrdp2 = new TtVsSpecialReqDtlPO();
						tvsrdp2.setOrderAmount(new Integer(order_amount));
						dao.update(tvsrdp1, tvsrdp2);
						if(order_amount < apply_amount){
							flag = "0";
						}
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
					dmsFreezePrice_Report(dlvryReqId.toString(), accountId, new BigDecimal(orderTotalPrice), logonUser.getUserId().toString());
				}
				if(!discountAccountId.equals("")){
					dmsFreezePrice_Report(dlvryReqId.toString(), discountAccountId, new BigDecimal(totalDiscountPrice), logonUser.getUserId().toString());
				}
				/*// 冻结资金
				if(!accountId.equals("")){
					accoutDao.syncAccountFreeze(dlvryReqId.toString(), accountId, new BigDecimal(orderTotalPrice), logonUser.getUserId().toString());
				}
				if(!discountAccountId.equals("")){
					accoutDao.syncAccountFreeze(dlvryReqId.toString(), discountAccountId, new BigDecimal(totalDiscountPrice), logonUser.getUserId().toString());
				}*/
			}
			//}
			act.setOutData("returnValue", 1);
		} catch(RuntimeException e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.PUTIN_FAILURE_CODE,e.getMessage());
			logger.error(logonUser,e1);
			act.setException(e);
		} catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DETAIL_FAILURE_CODE,"订做车需求生成订单");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 大用户查询初始化
	 */
	public void queryFleetInit() throws Exception{
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(dlrFleetUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商公司查询初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 大用户查询
	 */
	public void queryFleet(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String fleetName = request.getParamValue("fleetName");
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			Long companyId = logonUser.getCompanyId();
			PageResult<Map<String,Object>> ps = dao.selectFleet(companyId, fleetName, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商公司查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 定做车需求确认:提交确认
	 * */
	public void requirementConfirm(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String reqId = request.getParamValue("reqId");
			String dealerId = request.getParamValue("dealerId");
			Long typeId = null;
			
			Double pre_amount = new Double(0);					//预付款金额
			TtVsSpecialReqPO tempPO = new TtVsSpecialReqPO();
			tempPO.setReqId(Long.parseLong(reqId));
			List<PO> list = dao.select(tempPO);
			if (null != list && list.size()>0) {
				TtVsSpecialReqPO ttVsSpecialReqPO = (TtVsSpecialReqPO)list.get(0);
				pre_amount = ttVsSpecialReqPO.getPreAmount();
				typeId = ttVsSpecialReqPO.getAccountTypeId();
			}
			
			TtVsSpecialReqPO valuePO = new TtVsSpecialReqPO();
			valuePO.setReqStatus(Constant.SPECIAL_NEED_STATUS_08);
			valuePO.setReqConfirmDate(new java.util.Date());
			//valuePO.setAccountTypeId(Long.parseLong(typeId));
			dao.update(tempPO, valuePO);
			
			//需求确认时扣款
			
			Double available_amount = new Double(0);			//可用额度
			Double freeze_amount = new Double(0);				//冻结额度
			TtVsAccountPO tempAccountPO = new TtVsAccountPO();
			tempAccountPO.setDealerId(Long.parseLong(dealerId));
			tempAccountPO.setAccountTypeId(typeId);
			List<PO> accountList = dao.select(tempAccountPO);
			Long accountId = new Long(0);
			if (null != accountList && accountList.size()>0) {
				TtVsAccountPO accountPO = (TtVsAccountPO)accountList.get(0);
				available_amount = accountPO.getAvailableAmount();
				freeze_amount = accountPO.getFreezeAmount();
				accountId = accountPO.getAccountId();
			}
			
			TtVsAccountPO valueAccountPO = new TtVsAccountPO();
			valueAccountPO.setAvailableAmount(available_amount-pre_amount);
			valueAccountPO.setFreezeAmount(freeze_amount+pre_amount);
			
			dao.update(tempAccountPO, valueAccountPO);
			if(0 != pre_amount){
				accoutDao.accountFreeze(accountId+"", reqId, pre_amount+"", logonUser.getUserId()+"", Constant.Freeze_TYPE_01);
			}
			act.setForword(initDo);
		} catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"定做车需求确认:提交确认");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void spcialReqStatusUpdate(Map<String, String> map) {
		TtVsSpecialReqPO tvsr = dao.specialReqQuery(Long.parseLong(map.get("reqId"))) ;
		
		if(tvsr.getReqStatus().intValue() == Constant.SPECIAL_NEED_STATUS_06.intValue()) {
			dao.specialReqUpdate(map) ;
		}
	}
	
}
