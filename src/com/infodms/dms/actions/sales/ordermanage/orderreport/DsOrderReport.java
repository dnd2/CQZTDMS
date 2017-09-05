/**
 * @Title: UrgentOrderReport.java
 * 
 * @Description:
 * 
 * @Copyright: Copyright (c) 2010
 * 
 * @Company: www.infoservice.com.cn
 * @Date: 2010-5-27
 * 
 * @author yuyong
 * @mail yuyong@infoservice.com.cn
 * @version 1.0
 * @remark
 */
package com.infodms.dms.actions.sales.ordermanage.orderreport;

import static com.infodms.dms.actions.sales.financemanage.AccountOpera.dmsFreezePrice_Report;

import java.math.BigDecimal;
import java.util.Calendar;
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
import com.infodms.dms.common.relation.ProductComboRelation;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.dao.sales.financemanage.AccountBalanceDetailDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.DsOrderReportDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.OrderReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmBusinessParaPO;
import com.infodms.dms.po.TmDateSetPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmDealerPriceRelationPO;
import com.infodms.dms.po.TmFleetPO;
import com.infodms.dms.po.TmMaterialPricePO;
import com.infodms.dms.po.TmVhclMaterialGroupPO;
import com.infodms.dms.po.TmVhclMaterialGroupRPO;
import com.infodms.dms.po.TmVhclMaterialPO;
import com.infodms.dms.po.TtVsAccountPO;
import com.infodms.dms.po.TtVsAccountTypePO;
import com.infodms.dms.po.TtVsDlvryReqDtlPO;
import com.infodms.dms.po.TtVsDlvryReqPO;
import com.infodms.dms.po.TtVsOrderDetailPO;
import com.infodms.dms.po.TtVsOrderPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.businessUtil.GetOrderNOUtil;
import com.infodms.dms.util.businessUtil.ReqLogUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.ActionContextExtend;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * @author yinshunhui
 * 
 */
public class DsOrderReport {
	private Logger logger = Logger.getLogger(DsOrderReport.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private static final DsOrderReportDao dao = DsOrderReportDao.getInstance();
	private final AccountBalanceDetailDao accoutDao = AccountBalanceDetailDao.getInstance();

	private final String DS_ORDER_REPORT_QUERY_URL = "/jsp/sales/ordermanage/orderreport/dsOrderReportQuery.jsp";// 补充订单提报查询页面
	private final String DS_ORDER_REPORT_ADD_URL = "/jsp/sales/ordermanage/orderreport/dsOrderReportAdd.jsp";// 补充订单提报新增页面
	private final String DS_ORDER_REPORT_MOD_URL = "/jsp/sales/ordermanage/orderreport/dsOrderReportMod.jsp";// 补充订单提报修改页面

	public void doInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			Long poseId = logonUser.getPoseId();
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			List<Map<String, Object>> dateList = dao
					.getUrgentDateList(oemCompanyId);
			List<Map<String, Object>> areaList = MaterialGroupManagerDao
					.getDealerBusinessNoJszx(poseId.toString());
			TmDateSetPO dateSet = dao.getTmDateSetPO(new Date(), oemCompanyId);
			String year = dateSet != null ? dateSet.getSetYear() : "";
			String week = dateSet != null ? dateSet.getSetWeek() : "";
			String myWeek=year+"-"+week;
			
			int isFlag ;
			
			String para = CommonDAO.getPara(Constant.CHANA_SYS.toString()) ;
			
			if(Constant.COMPANY_CODE_JC.equals(para.toUpperCase())) {
				isFlag = 1 ;
			} else if (Constant.COMPANY_CODE_CVS.equals(para.toUpperCase())) {
				isFlag = 0 ;
			} else {
				throw new RuntimeException("判断当前系统的系统参数错误！") ;
			}
			Map<String,Object> m=CommonUtils.getIsConstracExpire(logonUser.getDealerCode());
			int count= 	Integer.parseInt(m.get("COUNT").toString());
			String contractDate=m.get("CONTRACT_DATE").toString();
			act.setOutData("IsExpire", count);//count
			act.setOutData("expireDate", contractDate);
			act.setOutData("isFlag", isFlag) ;
			
			act.setOutData("myWeek", myWeek);
			act.setOutData("dateList", dateList);
			act.setOutData("areaList", areaList);
			 
			act.setForword(DS_ORDER_REPORT_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "补充订单提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void urgentOrderReoprtQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String dealerIds = logonUser.getDealerId();
			Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao
					.getDealerBusinessNoJszx(poseId.toString());
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
			PageResult<Map<String, Object>> ps = dao.getUrgentOrderReportList(
					year, week, areaId, areaIds, dealerIds, companyId, curPage,
					Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "补充订单提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void dsOrderReoprtAddPre() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			 ActionContextExtend atx = (ActionContextExtend) ActionContext.getContext();
				String reqURL = atx.getRequest().getContextPath();
				if("/CVS-SALES".equals(reqURL.toUpperCase())){
					act.setOutData("returnValue", 1);
				}else{
					act.setOutData("returnValue", 2);
				}
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
//			String orderWeek = CommonUtils.checkNull(request
//					.getParamValue("orderWeek"));
//			String[] array = orderWeek.split("-");
//			String year = array[0];
//			String week = array[1];

			Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getDealerBusinessNoJszx(poseId.toString());// 业务范围列表
			//List<Map<String, Object>> fundTypeList = dao.getFundTypeList();// 资金类型列表
			List<Map<String, Object>> dateList = dao
			.getUrgentDateList(oemCompanyId);
			// 获得是否需要资金检查
			TmBusinessParaPO para = dao.getTmBusinessParaPO(
					Constant.URGENT_ORDER_ACCOUNT_CHECK_PARA, oemCompanyId);
			String isCheck = para.getParaValue();
			TmDateSetPO dateSet = dao.getTmDateSetPO(new Date(), oemCompanyId);
			String year = dateSet != null ? dateSet.getSetYear() : "";
			String week = dateSet != null ? dateSet.getSetWeek() : "";
			String myWeek=year+"-"+week;

			// 获得订单启票最大折扣点
			TmBusinessParaPO para2 = dao.getTmBusinessParaPO(
					Constant.ORDER_REPORT_MAX_DISCOUNT_RATE_PARA, oemCompanyId);
			String ratePara = para2.getParaValue();
			act.setOutData("curDealerId", logonUser.getDealerId());
			act.setOutData("myWeek", myWeek);
			act.setOutData("year", year);
			act.setOutData("week", week);
			act.setOutData("dateList", dateList);
			act.setOutData("areaList", areaList);// 业务范围列表
			//act.setOutData("fundTypeList", fundTypeList);// 资金类型列表
			act.setOutData("isCheck", isCheck);
			act.setOutData("ratePara", ratePara);
			act.setForword(DS_ORDER_REPORT_ADD_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "补充订单提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 补充订单提报是写入发运申请主表
	 * */
	public void urgentDlvryReq(Map<String,String> infoMap ,Long req_id,String isCover,String dlvryReqNO){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			
			TtVsDlvryReqPO dlvryReqPO = new TtVsDlvryReqPO();
			dlvryReqPO.setReqId(req_id);
			dlvryReqPO.setDlvryReqNo(dlvryReqNO);										//发运申请单号
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
			if (null != infoMap.get("discount") && !"".equals(infoMap.get("discount"))) {
				dlvryReqPO.setDiscount(Double.parseDouble(infoMap.get("discount")));		//折扣总额
			}
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
	/**
	 * 补充订单提报是写入发运申请明细表
	 * */
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
	
	
	
	public void dsOrderReoprtAdd() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Integer orgType = logonUser.getOrgType();
			String areaId = CommonUtils
					.checkNull(request.getParamValue("area"));
			String[] array = areaId.split("\\|");
			areaId = array[0];
			String dealerId = array[1];
			TmDealerPO dealerPO = dao.getTmDealer(dealerId);
			String myWeek[]=request.getParamValue("orderWeek").split("-");
			//String year = CommonUtils.checkNull(request.getParamValue("year"));
			//String week = CommonUtils.checkNull(request.getParamValue("week"));
			String year=myWeek[0];
			String week=myWeek[1];
			String fundType = CommonUtils.checkNull(request.getParamValue("fundType"));
			String totalPrice = CommonUtils.checkNull(request.getParamValue("total"));
			String deliveryType = CommonUtils.checkNull(request.getParamValue("deliveryType"));
			String deliveryAddress = CommonUtils.checkNull(request.getParamValue("deliveryAddress"));
			String orderRemark = CommonUtils.checkNull(request.getParamValue("orderRemark"));
			String fleetId = CommonUtils.checkNull(request.getParamValue("fleetId"));
			String fleetAddress = CommonUtils.checkNull(request.getParamValue("fleetAddress"));
			String priceId = CommonUtils.checkNull(request.getParamValue("priceId"));					// 价格类型
			String otherPriceReason = CommonUtils.checkNull(request.getParamValue("otherPriceReason"));	// 使用其他价格原因
			String discount = CommonUtils.checkNull(request.getParamValue("totalDiscountPrice_"));		// 使用折让
			String receiver = CommonUtils.checkNull(request.getParamValue("receiver"));					// 收货方
			String linkMan = CommonUtils.checkNull(request.getParamValue("linkMan"));					// 联系人
			String tel = CommonUtils.checkNull(request.getParamValue("tel"));							// 联系电话
			String submitType = CommonUtils.checkNull(request.getParamValue("submitType"));				// 提交类型
			String isCover = CommonUtils.checkNull(request.getParamValue("isCover"));					// 是否代交车
			String totalAmount_ = CommonUtils.checkNull(request.getParamValue("totalAmount_"));
			//String total = CommonUtils.checkNull(request.getParamValue("total"));						//订单总价
			String accountId = CommonUtils.checkNull(request.getParamValue("accountId"));// 资金账户
			String discountAccountId = CommonUtils.checkNull(request.getParamValue("discountAccountId"));// 折扣账户
			String totalDiscountPrice_ = CommonUtils.checkNull(request.getParamValue("totalDiscountPrice_"));//折扣总额
			String payRemark = CommonUtils.checkNull(request.getParamValue("payRemark")); 				 //付款信息备注
			String isBingcai = CommonUtils.checkNull(request.getParamValue("isBingcai")); //资金类型:1=兵财存货融资; 0=非兵财存货融资
			String productId = CommonUtils.checkNull(request.getParamValue("_productName_")) ;
			String orderType = CommonUtils.checkNull(request.getParamValue("orderType")) ; //订单类型
			String tmp_license_amount = CommonUtils.checkNull(request.getParamValue("tmp_license_amount")) ; //临牌数量

			// 获得是否需要资金检查
			TmBusinessParaPO para = dao.getTmBusinessParaPO(Constant.URGENT_ORDER_ACCOUNT_CHECK_PARA, new Long(logonUser.getOemCompanyId()));
			String isCheck = para.getParaValue();
			
			String returnValue = "1";
			// 选提报时进行资金校验
			/*if (submitType.equals("2")) {
				if (dealerPO.getDealerLevel().intValue() == Constant.DEALER_LEVEL_01
						.intValue()) {
					//资金类型:1=兵财存货融资,不进行资金校验
					if (isCheck.equals("0") && !"1".equals(isBingcai)) {
						TtVsAccountPO accountPO = new TtVsAccountPO();
						accountPO.setDealerId(new Long(dealerId));
						accountPO.setAccountTypeId(new Long(fundType.split("\\|")[0]));
						accountPO = dao.geTtVsAccountPO(accountPO);
						if (accountPO != null) {
							if (accountPO.getAvailableAmount().doubleValue() < Double.parseDouble(totalPrice)) {
								returnValue = "2";
							} else {
							}
						} else {
							returnValue = "2";
						}
					}
				}
			}*/


			Long order_id = Long.parseLong(SequenceManager.getSequence(""));
			Long req_id = Long.parseLong(SequenceManager.getSequence(""));
			
			
			// 校验通过时
			if (returnValue.equals("1")) {
				// 得到当前月份
				TmDateSetPO datePO = new TmDateSetPO();
				datePO.setSetYear(year);
				datePO.setSetWeek(week);
				datePO = dao.geTmDateSetPO(datePO);
				Integer month = datePO != null ? new Integer(datePO.getSetMonth()) : null;

				TtVsOrderPO po = new TtVsOrderPO();
				
				po.setOrderId(order_id);
				po.setCompanyId(GetOemcompanyId.getOemCompanyId(logonUser));
				po.setAreaId(new Long(areaId));
				po.setOrderType(new Integer(orderType));
				po.setTmpLicenseAmount(new Integer(tmp_license_amount));
				
				po.setIsRefitOrder(0);
				po.setOrderYear(new Integer(year));
				po.setOrderMonth(month);
				po.setOrderWeek(new Integer(week));
				po.setOrderOrgType(orgType);
				po.setOrderOrgId(new Long(dealerId));
				po.setBillingOrgType(new Long(orgType));
				po.setFundTypeId(new Long(fundType.split("\\|")[0]));
				
				if(!CommonUtils.isNullString(productId)) {
					po.setProductComboId(Long.parseLong(productId)) ;
				}
				/*String parentId = dao.getParentDealerId(dealerId,
						Constant.DEALER_LEVEL_01);
				po
						.setBillingOrgId(parentId != null ? new Long(parentId)
								: null);*/
				TmDealerPO tmDealerPO = new TmDealerPO();
				String orderNO = "";
				orderNO = SequenceManager.getSequence("");
				if (dealerPO.getDealerLevel().intValue() == Constant.DEALER_LEVEL_01.intValue()) {
					po.setBillingOrgId(dealerPO.getDealerId());
					tmDealerPO.setDealerId(Long.parseLong(dealerId));
					List<PO> dealerList = dao.select(tmDealerPO);
					String dealerCode = ((TmDealerPO)dealerList.get(0)).getDealerCode();
					Map<String,String> codeMap  = GetOrderNOUtil.getAreaShortcode(areaId);
					String areaCode = codeMap.get("AREA_SHORTCODE");
//					orderNO = GetOrderNOUtil.getOrderNO(areaCode,Constant.ORDER_TYPE_02+"", "D", dealerCode);
//					orderNO = GetOrderNOUtil.getOrderNO(areaCode,Constant.ORDER_TYPE_02+"", "D", dealerCode);
					//if(receiver==null||"".equals(receiver)||"-1".equals(receiver)){
						//orderNO="6"+orderNO;
				//	}
					po.setOrderNo(orderNO);
					po.setPriceId(priceId);
					po.setOtherPriceReason(otherPriceReason);
				}
				else{					
					po.setBillingOrgId(dealerPO.getParentDealerD());
					tmDealerPO.setDealerId(Long.parseLong(dealerId));
					List<PO> dealerList = dao.select(tmDealerPO);
					String dealerCode = ((TmDealerPO)dealerList.get(0)).getDealerCode();
					Map<String,String> codeMap  = GetOrderNOUtil.getAreaShortcode(areaId);
					String areaCode = codeMap.get("AREA_SHORTCODE");
//					orderNO = GetOrderNOUtil.getOrderNO(areaCode,Constant.ORDER_TYPE_02+"", "D", dealerCode);
					po.setOrderNo(orderNO);
				}
				po.setOrderPrice(new Double(totalPrice));
				
				if("1".equals(isCover)){	
					po.setFleetId(new Long(fleetId));
					po.setFleetAddress(fleetAddress);
					po.setIsFleet(1);
				} 
				else{
					po.setIsFleet(0);

					po.setDeliveryType(new Integer(deliveryType));
					if (po.getDeliveryType().intValue() == Constant.TRANSPORT_TYPE_01.intValue()) {
						po.setReceiver(new Long(dealerId));
					}
//					if (po.getDeliveryType().intValue() == Constant.TRANSPORT_TYPE_02.intValue()) {
						receiver = OrderReportDao.contralReceiver(areaId, receiver, orderNO) ;		// 对收货方做控制
						
						po.setReceiver(new Long(receiver));
						po.setDeliveryAddress(deliveryAddress==null||"".equals(deliveryAddress)?null:new Long(deliveryAddress));
						po.setLinkMan(linkMan);
						po.setTel(tel);
//					} 
				}
				po.setOrderRemark(orderRemark);
				po.setVer(new Integer(0));
				po.setCreateDate(new Date());
				po.setCreateBy(logonUser.getUserId());
				po.setDiscount(new Double(discount));

				if (null != payRemark && !"".equals(payRemark)) {
					po.setPayRemark(payRemark.trim());
				}
				
				/*Map<String,String> infoMap = new HashMap<String, String>();
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
				infoMap.put("discount", discount);*/
				
				
				// 保存时
				if (submitType.equals("1")) {
					po.setOrderStatus(Constant.ORDER_STATUS_01);
					dao.insertSalesOrder(po);
				}
				// 提报时
				else {
					po.setRaiseDate(new Date());
					if (dealerPO.getDealerLevel().intValue() == Constant.DEALER_LEVEL_01.intValue()) {
						po.setOrderStatus(Constant.ORDER_STATUS_03);// 已提报
						
						dao.insertSalesOrder(po);
						//String dlvryReqNO = GetOrderNOUtil.getDlvryReqNO(orderNO);
						//String dlvryReqNO = orderNO;
						
						//插入发运申请单
						dao.insertTtVsDlvryReq(po, req_id, new Integer(totalAmount_));
						
						//urgentDlvryReq(infoMap, req_id,isCover,dlvryReqNO);
						//资金类型:1=兵财存货融资,不进行资金同步
						//if (isCheck.equals("0") && !"1".equals(isBingcai)){
							// 冻结资金
							if(!accountId.equals("")){
								dmsFreezePrice_Report(req_id.toString(),accountId, new BigDecimal(totalPrice), logonUser.getUserId().toString()) ;
							}
							if(!discountAccountId.equals("")){
								dmsFreezePrice_Report(req_id.toString(), discountAccountId, new BigDecimal(totalDiscountPrice_), logonUser.getUserId().toString());
							}
							/*if(!accountId.equals("")){
								accoutDao.syncAccountFreeze(req_id.toString(), accountId, new BigDecimal(totalPrice), logonUser.getUserId().toString());
							}
							if(!discountAccountId.equals("")){
								accoutDao.syncAccountFreeze(req_id.toString(), discountAccountId, new BigDecimal(totalDiscountPrice_), logonUser.getUserId().toString());
							}*/
						//}
					} else {
						po.setOrderStatus(Constant.ORDER_STATUS_02);// 预审核
						dao.insertSalesOrder(po);
					}
					//向发运申请操作日志表写入日志信息
					ReqLogUtil.creatReqLog(req_id, Constant.REQ_LOG_TYPE_01, logonUser.getUserId());
				}

				Enumeration<String> enumeration = request.getParamNames();
				while (enumeration.hasMoreElements()) {
					String temp = enumeration.nextElement();
					// 客户订单车型明细保存
					if (temp.length() > 6 && temp.substring(0, 6).equals("amount")) {
						String subStr = temp.substring(6, temp.length());
						String amount = request.getParamValue(temp);
						String materialId = request.getParamValue("materialId"+ subStr);
						String price = request.getParamValue("singlePrice"+ subStr);
						String discount_rate = request.getParamValue("discount_rate"+subStr);			//折扣率
						String discount_s_price_ = request.getParamValue("discount_s_price_"+subStr);	//折扣后单价
						String discount_price_ = request.getParamValue("discount_price_"+subStr);	
						String price_list_id = request.getParamValue("priceList"+subStr);//price_list_id
						if(null==price_list_id||"0".equals(price_list_id)){
							throw new BizException("界面价格id为0,请联系管理员！");
						}
						TtVsOrderDetailPO detailPO = new TtVsOrderDetailPO();
						String orderDetailId = SequenceManager.getSequence("");
						detailPO.setDetailId(new Long(orderDetailId));
						detailPO.setOrderId(po.getOrderId());
						detailPO.setMaterialId(new Long(materialId));
						detailPO.setOrderAmount(new Integer(amount));
						detailPO.setCallAmount(new Integer(amount));
						detailPO.setSinglePrice(new Double(price));
						detailPO.setPriceListId(new Long(price_list_id));
						double totalPrice_ = 0;
						if (dealerPO.getDealerLevel().intValue() == Constant.DEALER_LEVEL_01.intValue()) {
							totalPrice_ = detailPO.getOrderAmount().intValue()* Double.parseDouble(price);
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
						dao.insertSalesOrderDetail(detailPO);
						if (submitType.equals("2")) {
							if (dealerPO.getDealerLevel().intValue() == Constant.DEALER_LEVEL_01.intValue()) {
								dao.insertTtVsDlvryReqDtl(detailPO, req_id);
								/*urgentDlvryReqDetail(req_id+"", orderDetailId, materialId, "", amount, price, totalPrice_+"", discount_rate,
									discount_s_price_, discount_price_);*/
							}
						}
					}
				}
			}
			act.setOutData("returnValue", returnValue);

		} catch(RuntimeException e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.PUTIN_FAILURE_CODE,e.getMessage());
			logger.error(logonUser,e1);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "补充订单提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void dsOrderReoprtModPre() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			 ActionContextExtend atx = (ActionContextExtend) ActionContext.getContext();
				String reqURL = atx.getRequest().getContextPath();
				if("/CVS-SALES".equals(reqURL.toUpperCase())){
					act.setOutData("returnValue", 1);
				}else{
					act.setOutData("returnValue", 2);
				}
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
			String orderNO = CommonUtils.checkNull(request.getParamValue("orderNO"));
			TtVsOrderPO po = dao.getTtSalesOrder(orderId);
			List<Map<String, Object>> dateList = dao.getUrgentDateList(oemCompanyId);
				act.setOutData("dateList", dateList);

			Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao
					.getDealerBusinessNoJszx(poseId.toString());
			//List<Map<String, Object>> fundTypeList = dao.getFundTypeList();

			// 查看日期配置表中当天的记录
			//TmDateSetPO dateSet = dao.getTmDateSetPO(new Date(), oemCompanyId);
			//String year = dateSet != null ? dateSet.getSetYear() : "";
			//String week = dateSet != null ? dateSet.getSetWeek() : "";
			String year=String.valueOf(po.getOrderYear());
			String week=String.valueOf(po.getOrderWeek());
			List<Map<String, Object>> detailList = dao.getSalesOrderDetailList(
					orderId, year, week, oemCompanyId.toString());
			int totalCount = 0;
			for (int i = 0; i < detailList.size(); i++) {
				Map<String, Object> map = detailList.get(i);
				BigDecimal orderAmount = (BigDecimal) map.get("ORDER_AMOUNT");
				totalCount += orderAmount.intValue();
			}
			List<Map<String, Object>> checkList = dao
					.getOrderCheckList(orderId);

			// 获得是否需要资金检查
			TmBusinessParaPO para = dao.getTmBusinessParaPO(
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

			TmDealerPO dealer = dao.getTmDealer(po.getOrderOrgId().toString());
			String myWeek=year+"-"+week;
			//获取价格类型的值
			String dealerId=logonUser.getDealerId();
			TmDealerPO tdPO=new TmDealerPO();
			tdPO.setDealerId(new Long(dealerId));
			tdPO=(TmDealerPO) dao.select(tdPO).get(0);
			// 获得订单启票最大折扣点
			TmBusinessParaPO para2 = dao.getTmBusinessParaPO(
					Constant.ORDER_REPORT_MAX_DISCOUNT_RATE_PARA, oemCompanyId);
			String ratePara = para2.getParaValue();
			act.setOutData("myWeek", myWeek);
			//act.setOutData("week", week);
			act.setOutData("orderNO", orderNO);
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
			act.setOutData("ratePara", ratePara);
			act.setOutData("priceId", tdPO.getPersonCharge());
			act.setOutData("sessionId", act.getSession().getId());
			act.setOutData("billId", orderId);
			act.setForword(DS_ORDER_REPORT_MOD_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "补充订单提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void urgentOrderReoprtMod() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		
		
		try {
			Integer orgType = logonUser.getOrgType();
			//String year = CommonUtils.checkNull(request.getParamValue("year"));
			//String week = CommonUtils.checkNull(request.getParamValue("week"));
			String orderNO = CommonUtils.checkNull(request.getParamValue("orderNO"));
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
			String fundType = CommonUtils.checkNull(request.getParamValue("fundType"));
			String totalPrice = CommonUtils.checkNull(request.getParamValue("total"));
			String deliveryType = CommonUtils.checkNull(request.getParamValue("deliveryType"));
			String deliveryAddress = CommonUtils.checkNull(request.getParamValue("deliveryAddress"));
			String orderRemark = CommonUtils.checkNull(request.getParamValue("orderRemark"));
			String fleetId = CommonUtils.checkNull(request.getParamValue("fleetId"));
			String fleetAddress = CommonUtils.checkNull(request.getParamValue("fleetAddress"));
			String priceId = CommonUtils.checkNull(request.getParamValue("priceId"));					// 价格类型
			String otherPriceReason = CommonUtils.checkNull(request.getParamValue("otherPriceReason"));	// 使用其他价格原因
			String discount = CommonUtils.checkNull(request.getParamValue("discount_totalPrice_"));		// 使用折让
			String receiver = CommonUtils.checkNull(request.getParamValue("receiver"));					// 收货方
			String linkMan = CommonUtils.checkNull(request.getParamValue("linkMan"));					// 联系人
			String tel = CommonUtils.checkNull(request.getParamValue("tel"));							// 联系电话
			String submitType = CommonUtils.checkNull(request.getParamValue("submitType"));				// 提交类型
			String totalAmount_ = CommonUtils.checkNull(request.getParamValue("totalAmount_"));
			String areaId = CommonUtils.checkNull(request.getParamValue("area"));
			String isCover = CommonUtils.checkNull(request.getParamValue("isCover"));
			//String total = CommonUtils.checkNull(request.getParamValue("total")); 						//订单总价
			String accountId = CommonUtils.checkNull(request.getParamValue("accountId"));				// 资金账户
			String discountAccountId = CommonUtils.checkNull(request.getParamValue("discountAccountId"));// 折扣账户
			String totalDiscountPrice_ = CommonUtils.checkNull(request.getParamValue("discount_totalPrice_"));//折扣总额
			String payRemark = CommonUtils.checkNull(request.getParamValue("payRemark")); 				 //付款信息备注
			String isBingcai = CommonUtils.checkNull(request.getParamValue("isBingcai"));				 //资金类型 1=兵财；0=非兵财
			String productId = CommonUtils.checkNull(request.getParamValue("_productName_")) ;

			String orderType = CommonUtils.checkNull(request.getParamValue("orderType")) ;
			String tmp_license_amount = CommonUtils.checkNull(request.getParamValue("tmp_license_amount")) ; //临牌数量
			//判断订单的状态
			TtVsOrderPO tvo = new TtVsOrderPO();
			tvo.setOrderId(new Long(orderId));
			tvo=(TtVsOrderPO) dao.select(tvo).get(0);
			if(!(tvo.getOrderStatus().intValue()==Constant.ORDER_STATUS_01||tvo.getOrderStatus().intValue()==Constant.ORDER_STATUS_04)){
				act.setOutData("returnValue", 2);
				return;
			}

			String[] array = areaId.split("\\|");
			areaId = array[0];
			String dealerId = array[1];
			TmDealerPO dealerPO = dao.getTmDealer(dealerId);

			String returnValue = "1";
			/*// 获得是否需要资金检查
			TmBusinessParaPO para = dao.getTmBusinessParaPO(
					Constant.URGENT_ORDER_ACCOUNT_CHECK_PARA, new Long(
							logonUser.getOemCompanyId()));
			String isCheck = para.getParaValue();
			// 选提报时进行资金校验
			if (submitType.equals("2")) {
				if (dealerPO.getDealerLevel().intValue() == Constant.DEALER_LEVEL_01.intValue()) {
					if (isCheck.equals("0") && !"1".equals(isBingcai)) {
						TtVsAccountPO accountPO = new TtVsAccountPO();
						accountPO.setDealerId(new Long(dealerId));
						accountPO.setAccountTypeId(new Long(fundType.split("\\|")[0]));
						accountPO = dao.geTtVsAccountPO(accountPO);
						if (accountPO != null) {
							if (accountPO.getAvailableAmount().doubleValue() < Double
									.parseDouble(totalPrice)) {
								returnValue = "2";
							} else {
							}
						} else {
							returnValue = "2";
						}
					}
				}
			}*/

			Long order_id = Long.parseLong(orderId);
			Long req_id = Long.parseLong(SequenceManager.getSequence(""));
			String orderWeek=request.getParamValue("orderWeek");
			String myWeek[]=orderWeek.split("-");
			
			// 校验通过时
			if (returnValue.equals("1")) {
				TtVsOrderPO condition = new TtVsOrderPO();
				condition.setOrderId(order_id);

				TtVsOrderPO value = new TtVsOrderPO();
				value.setOrderId(order_id);
				value.setOrderNo(orderNO);
				value.setAreaId(new Long(areaId));
				value.setOrderType(new Integer(orderType));
				value.setTmpLicenseAmount(new Integer(tmp_license_amount));

				value.setOrderOrgType(orgType);
				value.setOrderOrgId(new Long(dealerId));
				value.setBillingOrgType(new Long(orgType));
				value.setOrderWeek(Integer.parseInt(myWeek[1]));
				value.setOrderYear(Integer.parseInt(myWeek[0]));
				value.setFundTypeId(new Long(fundType.split("\\|")[0]));
				
				if(!CommonUtils.isNullString(productId)) {
					value.setProductComboId(Long.parseLong(productId)) ;
				}
				//String parentId = dao.getParentDealerId(dealerId,
						//Constant.DEALER_LEVEL_01);
				//value.setBillingOrgId(parentId != null ? new Long(parentId): null);
				if (dealerPO.getDealerLevel().intValue() == Constant.DEALER_LEVEL_01.intValue()) {
					value.setBillingOrgId(dealerPO.getDealerId());
					value.setPriceId(priceId);
					if (null != otherPriceReason && !"".equals(otherPriceReason)) {
						value.setOtherPriceReason(otherPriceReason.trim());
					}
				}
				else{
					value.setBillingOrgId(dealerPO.getParentDealerD());
				}
				value.setOrderPrice(new Double(totalPrice));
				
				if("1".equals(isCover)){	
					value.setIsFleet(1);
					value.setFleetId(new Long(fleetId));
					value.setFleetAddress(fleetAddress);
				} else {
					value.setIsFleet(0);
					value.setDeliveryType(new Integer(deliveryType));
					
					if (value.getDeliveryType().intValue() == Constant.TRANSPORT_TYPE_01.intValue()) {
						value.setReceiver(new Long(dealerId));
					}
//					if (value.getDeliveryType().intValue() == Constant.TRANSPORT_TYPE_02.intValue()) {
						receiver = OrderReportDao.contralReceiver(areaId, receiver, orderNO) ;		// 对收货方做控制
						
						value.setReceiver(new Long(receiver));  //--
						value.setDeliveryAddress(deliveryAddress==null||"".equals(deliveryAddress)?null:new Long(deliveryAddress));
						value.setLinkMan(linkMan);
						value.setTel(tel);
//					}
				}
				if (null != orderRemark && !"".equals(orderRemark)) {
					value.setOrderRemark(orderRemark.trim());
				}else{
					value.setOrderRemark(" ");
				}
				if (null != payRemark && !"".equals(payRemark)) {
					value.setPayRemark(payRemark.trim());
				}else{
					value.setPayRemark(" ");
				}
				value.setUpdateDate(new Date());
				value.setUpdateBy(logonUser.getUserId());
				value.setDiscount(new Double(discount));

				
				/*Map<String,String> infoMap = new HashMap<String, String>();
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
				infoMap.put("discount", discount);*/
				// 保存时
				if (submitType.equals("1")) {
					value.setOrderStatus(Constant.ORDER_STATUS_01);
				}
				// 提报时
				else {
					value.setRaiseDate(new Date());
					if (dealerPO.getDealerLevel().intValue() == Constant.DEALER_LEVEL_01
							.intValue()) {
						value.setOrderStatus(Constant.ORDER_STATUS_03);// 已提报
						//String dlvryReqNO = GetOrderNOUtil.getDlvryReqNO(orderNO);
						
						dao.insertTtVsDlvryReq(value, req_id, new Integer(totalAmount_));// 新增发运申请
						//String dlvryReqNO = orderNO;
						//urgentDlvryReq(infoMap, req_id,isCover,dlvryReqNO);
						// if (isCheck.equals("0") && !"1".equals(isBingcai)){
							// 冻结资金
							if(!accountId.equals("")){
								dmsFreezePrice_Report(req_id.toString(), accountId, new BigDecimal(totalPrice), logonUser.getUserId().toString());
							}
							if(!discountAccountId.equals("")){
								dmsFreezePrice_Report(req_id.toString(), discountAccountId, new BigDecimal(totalDiscountPrice_), logonUser.getUserId().toString());
							}
							/*// 冻结资金
							if(!accountId.equals("")){
								accoutDao.syncAccountFreeze(req_id.toString(), accountId, new BigDecimal(totalPrice), logonUser.getUserId().toString());
							}
							if(!discountAccountId.equals("")){
								accoutDao.syncAccountFreeze(req_id.toString(), discountAccountId, new BigDecimal(totalDiscountPrice_), logonUser.getUserId().toString());
							}*/
						// }
					} else {
						value.setOrderStatus(Constant.ORDER_STATUS_02);// 预审核
					}
				}

				dao.updateSalesOrder(condition, value);

				TtVsOrderDetailPO detailPO = new TtVsOrderDetailPO();
				detailPO.setOrderId(new Long(orderId));
				dao.deleteSalesOrderDetail(detailPO);
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
						String priceListId = request.getParamValue("priceListId"+ subStr);//erp价格id
						String discount_rate = request.getParamValue("discount_rate"+ subStr);		//折扣率
	//					String discount_s_price = request.getParamValue("discount_s_price"+ subStr);//折扣后单价
	//					String discount_price = request.getParamValue("discount_price"+ subStr);	//折扣额
						if(null==priceListId||"0".equals(priceListId)){
							throw new BizException("ERP价格ID错误，请联系管理员！！");
						}
							
						detailPO = new TtVsOrderDetailPO();
						String orderDetailId = SequenceManager.getSequence("");
						detailPO.setDetailId(new Long(orderDetailId));
						detailPO.setOrderId(new Long(orderId));
						detailPO.setMaterialId(new Long(materialId));
						detailPO.setOrderAmount(new Integer(amount));
						detailPO.setCallAmount(new Integer(amount));
						detailPO.setSinglePrice(new Double(price));
						detailPO.setPriceListId(new Long(priceListId));
						double totalPrice_ = 0;
//						if (dealerPO.getDealerLevel().intValue() == Constant.DEALER_LEVEL_01.intValue()) {
//							totalPrice_ = detailPO.getOrderAmount().intValue()* Double.parseDouble(discount_s_price);
//						}else{
							totalPrice_ = detailPO.getOrderAmount().intValue()* detailPO.getSinglePrice().doubleValue();
//						}
						detailPO.setTotalPrice(new Double(totalPrice_));
						detailPO.setVer(new Integer(0));
						detailPO.setCreateDate(new Date());
						detailPO.setCreateBy(logonUser.getUserId());
						if (null != discount_rate && !"".equals(discount_rate)) {
							detailPO.setDiscountRate(Float.parseFloat(discount_rate));
						}
//						if (null != discount_s_price && !"".equals(discount_s_price)) {
//							detailPO.setDiscountSPrice(Double.parseDouble(discount_s_price));
//						}
//						if (null != discount_price && !"".equals(discount_price)) {
//							detailPO.setDiscountPrice(Double.parseDouble(discount_price));
//						}
						
						dao.insertSalesOrderDetail(detailPO);
						if (submitType.equals("2")) {
							if (dealerPO.getDealerLevel().intValue() == Constant.DEALER_LEVEL_01.intValue()) {
								dao.insertTtVsDlvryReqDtl(detailPO, req_id);//  发运申请明细新增
							/*urgentDlvryReqDetail(req_id+"", orderDetailId, materialId, "", amount, price, totalPrice_+"", discount_rate,
								discount_s_price, discount_price);*/
							}
						}
					}
				}
			}
			act.setOutData("returnValue", returnValue);
		} catch(RuntimeException e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.PUTIN_FAILURE_CODE,e.getMessage());
			logger.error(logonUser,e1);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "补充订单提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	

	public void dsOrderReoprtDel() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String orderId = CommonUtils.checkNull(request
					.getParamValue("orderId"));
			//如果订单是已提报或者删除状态这提示状态不对无法提报
			TtVsOrderPO tvo = new TtVsOrderPO();
			tvo.setOrderId(new Long(orderId));
			tvo=(TtVsOrderPO) dao.select(tvo).get(0);
			if(tvo.getOrderStatus().intValue()==Constant.ORDER_STATUS_03||
					tvo.getOrderStatus().intValue()==Constant.ORDER_STATUS_06){
				act.setOutData("returnValue",2);
				return;
			}
			TtVsOrderPO orderCondition = new TtVsOrderPO();
			orderCondition.setOrderId(new Long(orderId));
			TtVsOrderPO orderValue = new TtVsOrderPO();
			orderValue.setOrderStatus(Constant.ORDER_STATUS_06);
			orderValue.setUpdateBy(logonUser.getUserId());
			orderValue.setUpdateDate(new Date());
			dao.update(orderCondition, orderValue);
			
			TtVsDlvryReqPO reqCondition = new TtVsDlvryReqPO();
			reqCondition.setOrderId(new Long(orderId));
			TtVsDlvryReqPO reqValue = new TtVsDlvryReqPO();
			reqValue.setReqStatus(Constant.ORDER_REQ_STATUS_07);
			reqValue.setUpdateBy(logonUser.getUserId());
			reqValue.setUpdateDate(new Date());
			dao.update(reqCondition, reqValue);
			
			act.setOutData("returnValue", 1);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "补充订单提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 页面直接提报方法：（铃木）
	 *
	 */
	public void dsOrderReoprtSubmit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
			//String orderNO = CommonUtils.checkNull(request.getParamValue("orderNO"));
			String amount = CommonUtils.checkNull(request.getParamValue("amount"));
			TtVsOrderPO orderPO = dao.getTtSalesOrder(orderId);
			Long dealerId = orderPO.getOrderOrgId();
			TmDealerPO dealerPO = dao.getTmDealer(dealerId.toString());
			boolean isPass = true;


			/*// 获得是否需要资金检查
			TmBusinessParaPO para = dao.getTmBusinessParaPO(Constant.URGENT_ORDER_ACCOUNT_CHECK_PARA, oemCompanyId);
			String isCheck = para.getParaValue();
			if (dealerPO.getDealerLevel().intValue() == Constant.DEALER_LEVEL_01.intValue()) {
				if (isCheck.equals("0")) {
					TtVsAccountPO accountPO = new TtVsAccountPO();
					accountPO.setDealerId(orderPO.getOrderOrgId());
					accountPO.setAccountTypeId(orderPO.getFundTypeId());
					accountPO = dao.geTtVsAccountPO(accountPO);
					if (accountPO != null) {
						if (accountPO.getAvailableAmount().doubleValue() < orderPO.getOrderPrice().doubleValue()) {
							isPass = false;
							act.setOutData("returnValue", 2);
						} else {
						}
					} else {
						isPass = false;
						act.setOutData("returnValue", 2);
					}
				}
			}*/

			if (isPass) {
				TtVsOrderPO condition = new TtVsOrderPO();
				TtVsOrderPO value = new TtVsOrderPO();
				condition.setOrderId(new Long(orderId));
				//如果订单是已提报或者删除状态这提示状态不对无法提报
				TtVsOrderPO tvo = new TtVsOrderPO();
				tvo.setOrderId(new Long(orderId));
				tvo=(TtVsOrderPO) dao.select(tvo).get(0);
				if(!(tvo.getOrderStatus().intValue()==Constant.ORDER_STATUS_01||tvo.getOrderStatus().intValue()==Constant.ORDER_STATUS_04)){
					act.setOutData("returnValue", 2);
					return;
				}
				if (dealerPO.getDealerLevel().intValue() == Constant.DEALER_LEVEL_01.intValue()) {
					value.setRaiseDate(new Date());
					value.setOrderStatus(Constant.ORDER_STATUS_03);// 已提报
					List<PO> orderList = dao.select(condition);
					if (null != orderList && orderList.size()>0) {
						TtVsOrderPO ttVsOrderPO = (TtVsOrderPO)orderList.get(0);
						/*Map<String,String> infoMap = new HashMap<String, String>();
						infoMap.put("area_id", ttVsOrderPO.getAreaId()+"");
						infoMap.put("order_id", ttVsOrderPO.getOrderId()+"");
						infoMap.put("fund_type", ttVsOrderPO.getFundTypeId()+"");
						infoMap.put("delivery_type", ttVsOrderPO.getDeliveryType()+"");
						infoMap.put("address_id", ttVsOrderPO.getDeliveryAddress()+"");
						infoMap.put("isFleet", ttVsOrderPO.getIsFleet()+"");
						infoMap.put("fleet_id", ttVsOrderPO.getFleetId()+"");
						infoMap.put("fleet_address", ttVsOrderPO.getFleetAddress());
						infoMap.put("req_total_amount", amount);
						infoMap.put("price_id", ttVsOrderPO.getPriceId()+"");
						infoMap.put("other_price_reason", ttVsOrderPO.getOtherPriceReason());
						infoMap.put("receiver", ttVsOrderPO.getReceiver()+"");
						infoMap.put("link_man", ttVsOrderPO.getLinkMan());
						infoMap.put("tel", ttVsOrderPO.getTel());
						infoMap.put("total", ttVsOrderPO.getOrderPrice()+"");*/
						Long req_id = Long.parseLong(SequenceManager.getSequence(""));
						//String dlvryReqNO = GetOrderNOUtil.getDlvryReqNO(orderNO);
						//String dlvryReqNO = orderNO;
						//urgentDlvryReq(infoMap, req_id,ttVsOrderPO.getIsFleet()+"",dlvryReqNO);
						dao.insertTtVsDlvryReq(ttVsOrderPO, req_id, new Integer(amount));
						// if (isCheck.equals("0")){
							// 获得资金账户
							TtVsAccountPO accountPO = new TtVsAccountPO();
							accountPO.setDealerId(orderPO.getBillingOrgId());
							accountPO.setAccountTypeId(orderPO.getFundTypeId());
							accountPO = dao.geTtVsAccountPO(accountPO);
							
							// 冻结资金
							if(accountPO != null){
								dmsFreezePrice_Report(req_id.toString(), accountPO.getAccountId().toString(), new BigDecimal(orderPO.getOrderPrice()), logonUser.getUserId().toString());
							}
							// 获得折扣账户
							List<Map<String,Object>> disAccountList = accoutDao.getDiscountAccountInfoByDealerId(orderPO.getBillingOrgId().toString());
							if(disAccountList.size() != 0 && orderPO.getDiscount() != null){
								Map<String,Object> accountMap = disAccountList.get(0);
								BigDecimal discountAccountId = (BigDecimal)accountMap.get("ACCOUNT_ID");
								dmsFreezePrice_Report(req_id.toString(), discountAccountId.toString(), new BigDecimal(orderPO.getDiscount()), logonUser.getUserId().toString());
							}
							
							/*// 冻结资金
							if(accountPO != null){
								accoutDao.syncAccountFreeze(req_id.toString(), accountPO.getAccountId().toString(), new BigDecimal(orderPO.getOrderPrice()), logonUser.getUserId().toString());
							}
							
							// 获得折扣账户
							List<Map<String,Object>> disAccountList = accoutDao.getDiscountAccountInfoByDealerId(orderPO.getBillingOrgId().toString());
							if(disAccountList.size() != 0 && orderPO.getDiscount() != null){
								Map<String,Object> accountMap = disAccountList.get(0);
								BigDecimal discountAccountId = (BigDecimal)accountMap.get("ACCOUNT_ID");
								accoutDao.syncAccountFreeze(req_id.toString(), discountAccountId.toString(), new BigDecimal(orderPO.getDiscount()), logonUser.getUserId().toString());
							}*/
						// }
						
						TtVsOrderDetailPO ttVsOrderDetailPO = new TtVsOrderDetailPO();
						ttVsOrderDetailPO.setOrderId(ttVsOrderPO.getOrderId());
						List<PO> detailList = dao.select(ttVsOrderDetailPO);
						if (null != detailList && detailList.size()>0) {
								for(int i=0; i<detailList.size(); i++){
								TtVsOrderDetailPO detailPO = (TtVsOrderDetailPO)detailList.get(i);
								//String orderDetailId = SequenceManager.getSequence("");
								dao.insertTtVsDlvryReqDtl(detailPO, req_id);
								/*urgentDlvryReqDetail(req_id+"", detailPO.getDetailId().toString(),detailPO.getMaterialId()+"","",detailPO.getOrderAmount()+"", 
										detailPO.getSinglePrice()+"",detailPO.getTotalPrice()+"",detailPO.getDiscountRate()+"",
										detailPO.getDiscountSPrice()+"",detailPO.getDiscountPrice()+"");*/
								}
							}
					}
					
				} else {
					value.setRaiseDate(new Date());
					value.setOrderStatus(Constant.ORDER_STATUS_02);// 预审核
				}
				value.setUpdateBy(logonUser.getUserId());
				value.setUpdateDate(new Date());
				dao.updateSalesOrder(condition, value);
				act.setOutData("returnValue", 1);
			}
		} catch(RuntimeException e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.PUTIN_FAILURE_CODE,e.getMessage());
			logger.error(logonUser,e1);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "补充订单提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 获取账户余额
	 */
	public void getAvailableAmount() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String fundTypeId = CommonUtils.checkNull(request.getParamValue("fundTypeId"));
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			if(dealerId==null||"".equals(dealerId)){
				dealerId=logonUser.getDealerId();
			}
			String reqId = CommonUtils.checkNull(request.getParamValue("reqId"));
			/*String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
			
			if(chkBinCai(fundTypeId, null) && !"".equals(orderId)) {	// 兵财查询经销商
				dealerId = OrderReportDao.getOrderDlr(orderId) ;
			}*/
			
			boolean flag = false;
			if(!reqId.equals("")){
				flag = true;
			}

			Map<String, Object> map = new HashMap<String, Object>();

//			if (flag) {
//				map = dao.getAvailableAmount(fundTypeId, dealerId, reqId);
//			} else {
//				map = dao.getAvailableAmount(fundTypeId, dealerId);
//			}
			map=CommonUtils.getAvailableAmount(fundTypeId, dealerId);
			BigDecimal outstandingAccount = dao.getOutstandingAccount(dealerId,fundTypeId);//冻结资金
			
			//act.setOutData("returnValue", map != null ? ((BigDecimal) map.get("AVAILABLE_AMOUNT")).subtract(dao.getOutstandingAccount(dealerId,fundTypeId)).toString() : "0");
//			if (flag) {
//				act.setOutData("returnValue1", map != null ? ((BigDecimal) map.get("AVAILABLE_AMOUNT_1")).toString() : "0");
//			}
			act.setOutData("returnValue", map != null ? ((BigDecimal) map.get("AVAILABLE_AMOUNT")).subtract(outstandingAccount):"0");//可用余额
			act.setOutData("accountId", map != null ? ((BigDecimal)map.get("ACCOUNT_ID")).toString() : "");
			
			act.setOutData("returnValue1", map != null ? ((BigDecimal)map.get("AVAILABLE_AMOUNT")).toString() : "0");//视图的账户余额
			act.setOutData("returnValue2", outstandingAccount);//冻结资金	
			act.setOutData("returnValue3", map != null ? ((BigDecimal)map.get("CREDIT_AMOUNT")).toString() : "0");//信用额度
			act.setOutData("returnValue4", map != null ? ((BigDecimal)map.get("FIN_AMOUNT")).toString() : "0");//账户余额-信用额度
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "补充订单提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void getDiscountAmount() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String accountId = CommonUtils.checkNull(request.getParamValue("accountId"));
			accountId = accountId.equals("") ? "0" : accountId;
			String reqId = CommonUtils.checkNull(request.getParamValue("reqId"));

			Map<String, Object> map = dao.getAvailableAmount2(accountId, reqId);

			act.setOutData("returnValue", map != null ? ((BigDecimal) map.get("AVAILABLE_AMOUNT")).toString() : "0");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "补充订单提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void getAddressList() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId")) ;

			List<Map<String, Object>> addressList = dao.getAddressList(dealerId, areaId);
			act.setOutData("addressList", addressList);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "补充订单提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void getAddressInfo() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String addressId = CommonUtils.checkNull(request
					.getParamValue("addressId"));
			Map<String, Object> map = dao.getAddressInfo(addressId);
			/*
			 * act.setOutData("info", addressId.equals("") ? "" : "联系人/电话： " +
			 * (map.get("LINK_MAN") == null ? "" : map.get("LINK_MAN")) + " " +
			 * (map.get("TEL") == null ? "" : map.get("TEL")));
			 */
			act.setOutData("info", map);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "补充订单提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 获取资金类型
	 */
	public void showFund() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			if(dealerId==null||"".equals(dealerId)){
				dealerId=logonUser.getDealerId();
			}
			TmDealerPO po = dao.getTmDealer(dealerId);
			act.setOutData("returnValue",po.getDealerLevel().intValue() == Constant.DEALER_LEVEL_01.intValue() ? "1" : "2");
			List<Map<String,Object>> discountList = accoutDao.getDiscountAccountInfoByDealerId(dealerId);
			act.setOutData("discountList", discountList);
			List<Map<String,Object>> fadealer =dao.getTmFaDealer(dealerId);
			System.out.println(fadealer.get(0).get("ROOT_DEALER_ID"));
			if(!fadealer.get(0).get("ROOT_DEALER_ID").equals(dealerId)){
				dealerId=fadealer.get(0).get("ROOT_DEALER_ID").toString();
			}
			String dealer_code=po.getDealerCode();
			List<Map<String,Object>> fundTypeList = accoutDao.getNoDiscountAccountInfoByDealerId(dealer_code);
			act.setOutData("dealerId",  CommonUtils.checkNull(request.getParamValue("dealerId")));
			act.setOutData("fundTypeList", fundTypeList);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "补充订单提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void showFundTypeList() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String dealerId = CommonUtils.checkNull(request
					.getParamValue("dealerId"));
			List<Map<String,Object>> fundTypeList = accoutDao.getNoDiscountAccountInfoByDealerCode(dealerId);
			act.setOutData("fundTypeList", fundTypeList);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "补充订单提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void showJszxFundTypeList() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String dealerId = CommonUtils.checkNull(request
					.getParamValue("dealerId"));
			TmDealerPO dealerPO = dao.getTmDealer(dealerId);
			List<Map<String,Object>> fundTypeList = accoutDao.getNoDiscountAccountInfoByDealerId(dealerPO.getParentDealerD().toString());
			act.setOutData("fundTypeList", fundTypeList);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "补充订单提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void showDiscountInfo() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String dealerId = CommonUtils.checkNull(request
					.getParamValue("dealerId"));
			List<Map<String,Object>> discountList = accoutDao.getDiscountAccountInfoByDealerId(dealerId);
			act.setOutData("discountList", discountList);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "补充订单提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	//增加产品
	public void addMaterial() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			// 查看日期配置表中当天的记录
			TmDateSetPO dateSet = dao.getTmDateSetPO(new Date(), companyId);
			String materialCode = CommonUtils.checkNull(request
					.getParamValue("materialCode"));
			//获取价格
			String dealerCode=logonUser.getDealerCode();
//			dealerCode="1008";
//			groupCode="SC7103GC7C110";
			Map<String,Object> maps=new HashMap<String,Object>();
			if(CommonUtils.getMaterialPirce(materialCode, dealerCode)!=null){
				TmMaterialPricePO tmpp=new TmMaterialPricePO();
				tmpp=CommonUtils.getMaterialPirce(materialCode, dealerCode);
				maps.put("OPERAND", tmpp.getOperand());
				maps.put("LIST_HEADER_ID", tmpp.getListHeaderId());
				if(tmpp!=null&&"0".equals(tmpp.getListHeaderId().toString())){
					throw new BizException("ERP价格id为0,请联系管理员！");
				}
			}else{
				maps.put("OPERAND", Constant.MATERIAL_PRICE_MAX+1);
			}
			
			String priceId = CommonUtils.checkNull(
					request.getParamValue("priceId")).equals("") ? "0"
					: CommonUtils.checkNull(request.getParamValue("priceId"));
			priceId="0";
			String entityCode = CommonUtils.checkNull(request.getParamValue("entityCode"));
			String year = dateSet != null ? dateSet.getSetYear() : "";
			String week = dateSet != null ? dateSet.getSetWeek() : "";
//			String dealerCode1 = "";
//			String orderDealerId = CommonUtils.checkNull(request.getParamValue("orderDealerId"));
//			if(!"".equals(orderDealerId)){
//				TmDealerPO dpo = dao.getDlrByID(new Long(orderDealerId));// 经销商PO
//				if(dpo != null){
//					dealerCode1 = dpo.getDealerCode();
//				}
//			}
			Map<String, Object> map = dao.getMaterialInfo(materialCode, year,
					week, priceId, companyId.toString(), entityCode,dealerCode);
			String para = CommonDAO.getPara(Constant.CHANA_SYS.toString()) ;
			int isFlag;
			String isReturn="";
			Calendar cal = Calendar.getInstance(); 
			int month = cal.get(Calendar.MONTH )+1; 
			if(Constant.COMPANY_CODE_JC.equals(para.toUpperCase())) {
				isFlag = 1 ;
			} else if (Constant.COMPANY_CODE_CVS.equals(para.toUpperCase())) {
				isFlag = 0 ;
				isReturn = dao.viewOrderCount(year,week, request.getParamValue("dealerId"), materialCode);
			
			} else {
				throw new RuntimeException("判断当前系统的系统参数错误！") ;
			}
			act.setOutData("info", map);
			
			act.setOutData("infos", maps);
			act.setOutData("isFlag", isFlag);
			act.setOutData("isReturn",isReturn);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "补充订单提报");
			logger.error(logonUser, e1);
			
			act.setException(e1);
			
		}
	}

	/**
	 * 获得价格类型列表
	 */
	public void getPriceList() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			String dealerId = CommonUtils.checkNull(request
					.getParamValue("dealerId"));
			if(dealerId==null || dealerId.equals("")){
				dealerId=logonUser.getDealerId();
			}
			String companyId = GetOemcompanyId.getOemCompanyId(logonUser)
					.toString();

			map.put("dealerId", dealerId);
			map.put("companyId", companyId);
			TmDealerPO tm=new TmDealerPO();
			tm.setDealerId(logonUser.getDealerId()==null?null:new Long(dealerId));
			tm=(TmDealerPO) dao.select(tm).get(0);
//			List<Map<String, Object>> priceList = dao.getPriceList(map);
//			Map mymap=dao.getMyMap(Long.parseLong(companyId), dealerId);
//			priceList.add(mymap);
//			act.setOutData("priceList", priceList);
			act.setOutData("priceType", tm.getPersonCharge());
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "补充订单提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 获得价格类型列表
	 */
	public void getPriceListByReqId() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			String dealerId = CommonUtils.checkNull(request
					.getParamValue("dealerId"));
			String reqId = CommonUtils.checkNull(request
					.getParamValue("reqId"));
			String companyId = GetOemcompanyId.getOemCompanyId(logonUser)
					.toString();

			map.put("dealerId", dealerId);
			map.put("companyId", companyId);
			map.put("reqId", reqId);

			List<Map<String, Object>> priceList = dao.getPriceList(map);
			Map mymap=dao.getMyMap(Long.parseLong(companyId), dealerId);
			priceList.add(mymap);
			act.setOutData("priceList", priceList);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "补充订单提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	

	/**
	 * 获得收货方地址
	 */
	public void getReceiverList() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));

			map.put("dealerId", dealerId);
			map.put("areaId", areaId);

			TmDealerPO tdPO = this.getDealerClass(map) ;
			map.put("dealerClass", tdPO.getDealerClass()) ;
			
			List<Map<String, Object>> receiverList = dao.getReceiverList(map);
			act.setOutData("receiverList", receiverList);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "补充订单提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 获得收货方地址
	 */
	public void getReceiverListInNull() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));

			map.put("dealerId", dealerId);
			map.put("areaId", areaId);

			TmDealerPO tdPO = this.getDealerClass(map) ;
			map.put("dealerClass", tdPO.getDealerClass()) ;
			
			List<Map<String, Object>> receiverList = dao.getReceiverListInNull(map);
			act.setOutData("receiverList", receiverList);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "补充订单提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public TmDealerPO getDealerClass(Map<String, Object> map) {
		TmDealerPO tdPO = new TmDealerPO() ;
		tdPO.setDealerId(Long.parseLong(map.get("dealerId").toString())) ;
		
		tdPO = (TmDealerPO)dao.select(tdPO).get(0) ;
		
		return tdPO ;
	}

	/*
	 * 判断是否显示使用其他价格原因
	 */
	public void isShowOtherPriceReason() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String dealerId = CommonUtils.checkNull(request
					.getParamValue("dealerId"));
			String priceId = CommonUtils.checkNull(request
					.getParamValue("priceId"));

			TmDealerPriceRelationPO po = new TmDealerPriceRelationPO();
			//po.setDealerId(new Long(dealerId));
			//po.setPriceId(new Long(priceId));
			//List<PO> list = dao.select(po);
			List<TmDealerPriceRelationPO> list =dao.getDealerPriceRelation(dealerId,priceId);
			po = (TmDealerPriceRelationPO) list.get(0);
			String returnValue = "0";
			if (po.getIsDefault().intValue() != Constant.IF_TYPE_YES.intValue()) {
				returnValue = "1";
			}
			act.setOutData("returnValue", returnValue);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "常规订单提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 获得物料单价列表
	 */
	public void getSinglePriceList() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			String priceId = CommonUtils.checkNull(request
					.getParamValue("priceId"));
			String ids = CommonUtils.checkNull(request.getParamValue("ids"));

			map.put("priceId", priceId);
			map.put("ids", ids);

			List<Map<String, Object>> priceList = dao.getSinglePriceList(map);
			act.setOutData("priceList", priceList);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "补充订单提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 获得物料单价列表
	 */
	public void getDetailSinglePriceList() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			String priceId = CommonUtils.checkNull(request
					.getParamValue("priceId"));
			String ids = CommonUtils.checkNull(request.getParamValue("ids"));

			map.put("priceId", priceId);
			map.put("ids", ids);

			List<Map<String, Object>> priceList = dao
					.getDetailSinglePriceList(map);
			act.setOutData("priceList", priceList);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "补充订单提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 获得物料单价列表
	 */
	public void getStock() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			String warehouseId = CommonUtils.checkNull(request
					.getParamValue("warehouseId"));
			String ids = CommonUtils.checkNull(request.getParamValue("ids"));

			map.put("warehouseId", warehouseId);
			map.put("ids", ids);
			map.put("companyId", GetOemcompanyId.getOemCompanyId(logonUser).toString());

			List<Map<String, Object>> stockList = dao.getStockList(map);
			act.setOutData("stockList", stockList);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "补充订单提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 获得结算中心下级价格
	 */
	public void isHasPriceAtJszx() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String materialCode = CommonUtils.checkNull(request.getParamValue("materialCode"));
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));

			String price = dao.getPriceIdByNewOrderIdYz(orderId, materialCode);
			
			String returnValue = "0";
			if(price == null || Integer.parseInt(price) > Constant.MATERIAL_PRICE_MAX.intValue()){
				returnValue = "1";
			}

			act.setOutData("returnValue", returnValue);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "补充订单提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}	
	
	public static boolean chkBinCai(String fundTypeId, String accountId) {
		List typeList = null ;
		
		if (!CommonUtils.isNullString(fundTypeId)) {
			TtVsAccountTypePO tvat = new TtVsAccountTypePO();
			tvat.setTypeId(Long.parseLong(fundTypeId));
			tvat.setTypeCode("CSGC");

			typeList = dao.select(tvat);
		}
		
		if (!CommonUtils.isNullString(accountId)) {
			TtVsAccountPO tva  = new TtVsAccountPO() ;
			tva.setAccountId(Long.parseLong(accountId)) ;
			tva.setAccountCode("CSGC") ;
			
			typeList = dao.select(tva);
		}
		
		if (CommonUtils.isNullList(typeList)) {
			return false;
		}
		
		return true ;
	}
	
	public void getProductStrByCompanyId() {
		String contralId = CommonUtils.checkNull(request.getParamValue("contralId")) ;
		String isDisabled = CommonUtils.checkNull(request.getParamValue("isDisabled")) ;
		String isAll = CommonUtils.checkNull(request.getParamValue("isAll")) ;
		String proValue = CommonUtils.checkNull(request.getParamValue("proValue")) ;
		String para = CommonDAO.getPara(Constant.PRODUCT_COMBO_PARA.toString()) ;
		
		if(Integer.parseInt(para) >= 0) {
			ProductComboRelation rcr = new ProductComboRelation() ;
			rcr.getProductStrByCompanyId();
			
			if("true".equals(isDisabled)) {
				act.setOutData("isDisabled", isDisabled) ;
			} else {
				act.setOutData("isDisabled", "false") ;
			}
			
			if("true".equals(isAll)) {
				act.setOutData("isAll", isAll) ;
			} else {
				act.setOutData("isAll", "false") ;
			}
			
			act.setOutData("proValue", proValue) ;
			act.setOutData("contralId", contralId) ;
		} else {
		}
		
		act.setOutData("para", para) ;
	}
}
