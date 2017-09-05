/**********************************************************************
* <pre>
* FILE : DeliveryApply.java
* CLASS : DeliveryApply
* AUTHOR : 
* FUNCTION : 订单发运
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.|   DATE   |    NAME    | REASON  |  CHANGE REQ.
*----------------------------------------------------------------------
*         |2010-05-21|            | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
package com.infodms.dms.actions.sales.ordermanage.delivery;


import static com.infodms.dms.actions.sales.financemanage.AccountOpera.dmsFreezePrice_Report;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.ordermanage.orderreport.MonthGeneralOrderCall;
import com.infodms.dms.actions.sales.planmanage.QuotaAssign.ResourceReserveQuery;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.dao.sales.financemanage.AccountBalanceDetailDao;
import com.infodms.dms.dao.sales.ordermanage.delivery.OrderDeliveryDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmBusinessParaPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TtVsAccountPO;
import com.infodms.dms.po.TtVsDlvryReqDtlPO;
import com.infodms.dms.po.TtVsDlvryReqPO;
import com.infodms.dms.po.TtVsOrderDetailPO;
import com.infodms.dms.po.TtVsOrderPO;
import com.infodms.dms.po.TtVsOrderResourceReservePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.OrderCode;
import com.infodms.dms.util.businessUtil.GetOrderNOUtil;
import com.infodms.dms.util.businessUtil.ReqLogUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.ActionContextExtend;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * @Title: 常规订单发运申请Action
 * @Description:InfoFrame3.0.V01
 * @Copyright: Copyright (c) 2010
 * @Company: www.infoservice.com.cn
 * @Date: 2010-5-21
 * @author 
 * @mail  	
 * @version 1.0
 * @remark 
 */
public class DeliveryApply {
	
	public Logger logger = Logger.getLogger(DeliveryApply.class);   
	OrderDeliveryDao dao  = OrderDeliveryDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	
	private final String initUrl = "/jsp/sales/ordermanage/delivery/deliveryApply.jsp";
	private final String ListUrl = "/jsp/sales/ordermanage/delivery/deliveryApplyList.jsp";
	private final String totalInitUrl="/jsp/sales/ordermanage/delivery/deliveryTotalInit.jsp";
	private final String detailInitUrl="/jsp/sales/ordermanage/delivery/deliveryDetail.jsp";
	private final String totalDetailUrl="/jsp/sales/ordermanage/delivery/deliveryTotal.jsp";
	private final String dlrDelvUrl="/jsp/sales/ordermanage/delivery/deliveryInfoDetail.jsp";
	/**
	 * 常规订单发运申请页面初始化
	 */
	public void applyQueryInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			List<Map<String,Object>> dateList = dao.getGeneralDeliveryDateList_New(oemCompanyId);//dao.getGeneralDateList();
			
			List<Map<String, Object>> areaList = new ArrayList<Map<String, Object>>() ;
			
			String para = CommonDAO.getPara(Constant.CHANA_SYS.toString()) ;
			
			if(Constant.COMPANY_CODE_JC.equals(para.toUpperCase())) {
				areaList = MaterialGroupManagerDao.getDealerLevelBusiness(logonUser.getPoseId().toString());
			} else if (Constant.COMPANY_CODE_CVS.equals(para.toUpperCase())) {
				areaList = MaterialGroupManagerDao.getDealerLevelBusinessAll(logonUser.getPoseId().toString());
			} else {
				throw new RuntimeException("判断当前系统的系统参数错误！") ;
			}
			Map<String,Object> m=CommonUtils.getIsConstracExpire(logonUser.getDealerCode());
			//int count= 	Integer.parseInt(m.get("COUNT").toString());
			String contractDate=m.get("CONTRACT_DATE").toString();
			act.setOutData("IsExpire", 2);
			act.setOutData("expireDate", contractDate);
			act.setOutData("dateList", dateList);
			act.setOutData("areaList", areaList);
			act.setForword(initUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"常规订单发运申请初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 常规订单发运申请查询
	 */
	public void applyQuery(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			ActionContextExtend atx = (ActionContextExtend) ActionContext.getContext();
			String reqURL = atx.getRequest().getContextPath();
			if("/CVS-SALES".equals(reqURL.toUpperCase())){
				act.setOutData("returnValue", 1);
			}else{
				act.setOutData("returnValue", 2);
			}
			String orderYearWeek = request.getParamValue("orderYearWeek");
			String[] array = orderYearWeek.split("-");
			String orderYear = array[0];
			String orderWeek = array[1];
			String areaId = request.getParamValue("areaId");		//业务范围
			String dealerId = request.getParamValue("dealerId");	//经销商ID
			String orderNo = CommonUtils.checkNull(request.getParamValue("orderNO")) ;
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			List<Map<String, Object>> list = dao.getDeliveryApplyList(orderYear, orderWeek, areaId, dealerId,oemCompanyId, orderNo);
			List<Map<String,Object>> accountlist = dao.getDealerAccount(dealerId);
			List<Map<String,Object>> addresslist = dao.getDealerAddress(dealerId);
			// 获得是否需要资金检查
			TmBusinessParaPO para = dao.getTmBusinessParaPO(Constant.URGENT_ORDER_ACCOUNT_CHECK_PARA,oemCompanyId);
			String isCheck = para.getParaValue();
			// 获得订单启票最大折扣点
			TmBusinessParaPO para2 = dao.getTmBusinessParaPO(
					Constant.ORDER_REPORT_MAX_DISCOUNT_RATE_PARA, oemCompanyId);
			String ratePara = para2.getParaValue();
			//获取价格类型
			TmDealerPO tdPo=new TmDealerPO();
			tdPo.setDealerId(new Long(dealerId));
			tdPo=(TmDealerPO) dao.select(tdPo).get(0);
			act.setOutData("priceId", tdPo.getPersonCharge());
			act.setOutData("isCheck",isCheck);
			act.setOutData("list", list);
			act.setOutData("accountlist", accountlist);
			act.setOutData("addresslist",addresslist);
			act.setOutData("dealerId", dealerId);
			act.setOutData("ratePara", ratePara);
			act.setOutData("curDealerId", logonUser.getDealerId());
			act.setForword(ListUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"常规订单发运申请");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 常规订单发运申请
	 */
	public void applySubmit(){
		
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		Long userId = logonUser.getUserId();
		
		try{
			String applyAmounts = request.getParamValue("Amounts");			//申请发运数量
			String callAmounts = request.getParamValue("cAmounts");		    //已申请数量
			String orderIds = request.getParamValue("orderIds");		    //订单ID
			String detailIds = request.getParamValue("detailIds");			//订单明细ID
			String materialIds = request.getParamValue("materialIds");		//物料ID
			String singlePrices = request.getParamValue("singlePrices");	//物料单价
			String typeId = request.getParamValue("typeId");				//账户类型ID
			String accountId = request.getParamValue("accountId");			//经销商账户ID
			String availableAmount = request.getParamValue("availableAmount");//经销商账户可用余额
			String freezeAmount = request.getParamValue("freezeAmount");	//经销商账户预扣
			String transportType = request.getParamValue("transportType");	//发运方式
			String addressId = request.getParamValue("addressId");			//发运地址ID
			String areasIds = request.getParamValue("areaIds");				//业务范围
			String isCheck = request.getParamValue("isCheck");				//资金开关
			String fleetId = request.getParamValue("fleetId");			    //集团客户ID
			String address = request.getParamValue("address");			    //集团客户地址
			String priceId = CommonUtils.checkNull(request.getParamValue("priceId"));// 价格类型
			String otherPriceReason = CommonUtils.checkNull(request.getParamValue("otherPriceReason"));// 使用其他价格原因
//			String discount = CommonUtils.checkNull(request.getParamValue("discount"));// 使用折让
			String receiver = CommonUtils.checkNull(request.getParamValue("receiver"));// 收货方
			String linkMan = CommonUtils.checkNull(request.getParamValue("linkMan"));// 联系人
			String tel = CommonUtils.checkNull(request.getParamValue("tel"));// 联系电话
			String discountAccountId = CommonUtils.checkNull(request.getParamValue("discountAccountId"));// 折扣账户
			String reqRemark = CommonUtils.checkNull(request.getParamValue("orderRemark")).trim() ;
			String tmp_license_amount = CommonUtils.checkNull(request.getParamValue("tmp_license_amount")) ; //临牌数量
//			String discount_rates = CommonUtils.checkNull(request.getParamValue("DISCOUNT_rate_"));
//			String discount_s_prices = CommonUtils.checkNull(request.getParamValue("DISCOUNT_s_price_"));
//			String discount_prices = CommonUtils.checkNull(request.getParamValue("DISCOUNT_price_"));
			String totailAccount = CommonUtils.checkNull(request.getParamValue("totailAccount"));//订单总价
			String isCover = CommonUtils.checkNull(request.getParamValue("isCover"));            //是否代交车
			String orderPriceSums = request.getParamValue("orderPriceSums");   //订单价格合计
			
			String[] applyAmount = applyAmounts.split(",");					//取得所有数量放在数组中
			String[] detailId = detailIds.split(",");						
			String[] orderId = orderIds.split(",");
			String[] areasId = areasIds.split(",");
			String[] singlePrice = singlePrices.split(",");
			String[] materialId = materialIds.split(",");
			String[] callAmount = callAmounts.split(",");
			String[] priceListIds=request.getParamValue("priceListIds").split(",");
			
//			String[] discount_rate = discount_rates.split(",");
//			String[] discount_s_price = discount_s_prices.split(",");
//			String[] discount_price = discount_prices.split(",");
			String[] orderPriceSum = orderPriceSums.split(",");
			
			TtVsOrderDetailPO tsodpContion = new TtVsOrderDetailPO();		//订单明细表
			TtVsOrderDetailPO tsodpValue = new TtVsOrderDetailPO();
			TtVsAccountPO tsapContion = new TtVsAccountPO();				//账户表
			TtVsAccountPO tsapValue = new TtVsAccountPO();
			TtVsDlvryReqPO tvdrpo = new TtVsDlvryReqPO();					//发运申请表
			TtVsDlvryReqPO tvdrpovalue = new TtVsDlvryReqPO();			    //发运申请表
			TtVsDlvryReqDtlPO tvdrdpo = new TtVsDlvryReqDtlPO();			//发运申请明细表
			String orderIdflg = "";
			Integer reqTotalAmount = 0;
			Double reqTotalPrice = new Double(0);
			Double reqDiscountPrice = new Double(0);
			
			String returnValue = "1" ;
			
			if(!"".equals(applyAmount) && applyAmount != null){
				for (int i = 0; i < applyAmount.length; i++) {
					if (applyAmount[i] != null && !"".equals(applyAmount[i]) && Integer.parseInt(applyAmount[i]) > 0) {
						if(singlePrice[i].equals("") || singlePrice[i].equals("0")) {
							returnValue = "4" ;
							break ;
						}
					}
				}
				
				if(returnValue != "4"){
					MonthGeneralOrderCall mgoc = new MonthGeneralOrderCall() ;
					String str = mgoc.getMatCode(detailId, applyAmount) ;
					
					if(!CommonUtils.isNullString(str)){
						act.setOutData("metStr", str);
						returnValue = "3" ;
					} else {
						for(int i=0; i<applyAmount.length; i++){
							if(!"".equals(applyAmount[i])&&applyAmount[i]!=null&&Integer.parseInt(applyAmount[i])>0){
								String orderIdvalue = orderId[i].toString();
								
								// 计算发运申请总价和折扣额总价
								Double totalPrice = Double.parseDouble(singlePrice[i])*Integer.parseInt(applyAmount[i]);
//								Double discountPrice = Double.parseDouble(singlePrice[i])*Integer.parseInt(applyAmount[i]) * Float.parseFloat(discount_rate[i])/100;
								Double discountPrice = Double.parseDouble(singlePrice[i])*Integer.parseInt(applyAmount[i]);
							
								if(!orderIdflg.equals(orderIdvalue)){
									// 订单号不相等并且不是循环第一次时冻结资金
									if(!orderIdflg.equals("")){
										// if("0".equals(isCheck)){
											// 冻结资金
											if(!accountId.equals("")){
												dmsFreezePrice_Report(tvdrpovalue.getReqId().toString(), accountId, new BigDecimal(tvdrpovalue.getReqTotalPrice()), logonUser.getUserId().toString());
											}
											if(!discountAccountId.equals("")){
												dmsFreezePrice_Report(tvdrpovalue.getReqId().toString(), discountAccountId, new BigDecimal(tvdrpovalue.getDiscount()), logonUser.getUserId().toString());
											}
											/*// 冻结资金
											if(!accountId.equals("")){
												accoutDao.syncAccountFreeze(tvdrpovalue.getReqId().toString(), accountId, new BigDecimal(tvdrpovalue.getReqTotalPrice()), logonUser.getUserId().toString());
											}
											if(!discountAccountId.equals("")){
												accoutDao.syncAccountFreeze(tvdrpovalue.getReqId().toString(), discountAccountId, new BigDecimal(tvdrpovalue.getDiscount()), logonUser.getUserId().toString());
											}*/
										// }
									}
									
									orderIdflg = orderIdvalue;
									tvdrpovalue = new TtVsDlvryReqPO();	
									Long req_id = Long.parseLong(SequenceManager.getSequence(""));
									tvdrpovalue.setReqId(req_id);//设置申请主键
									tvdrpovalue.setOrderId(Long.parseLong(orderIdvalue));
									tvdrpovalue.setAreaId(Long.parseLong(areasId[i]));
									tvdrpovalue.setFundType(Long.parseLong(typeId));
									tvdrpovalue.setReqTotalAmount(Integer.parseInt(applyAmount[i]));
									tvdrpovalue.setReqDate(new Date(System.currentTimeMillis()));
									tvdrpovalue.setPriceId(priceId);
									tvdrpovalue.setOtherPriceReason(otherPriceReason);
									tvdrpovalue.setReqRemark(reqRemark) ;
									tvdrpovalue.setTmpLicenseAmount(new Integer(tmp_license_amount));

									TtVsOrderPO ttVsOrderPO = new TtVsOrderPO();
									ttVsOrderPO.setOrderId(Long.parseLong(orderId[i]));
									List orderList = dao.select(ttVsOrderPO);
									TtVsOrderPO tempPO = (TtVsOrderPO)orderList.get(0);
									
									if(null != isCover && "1".equals(isCover)){
										tvdrpovalue.setIsFleet(1);
										tvdrpovalue.setFleetId(Long.parseLong(fleetId));
										tvdrpovalue.setFleetAddress(address);
										tvdrpovalue.setReqStatus(Constant.ORDER_REQ_STATUS_08);
									}
									else{
										tvdrpovalue.setIsFleet(0);
										tvdrpovalue.setDeliveryType(Integer.parseInt(transportType));
//										if(Integer.parseInt(transportType)==Constant.TRANSPORT_TYPE_02){
											tvdrpovalue.setAddressId(Long.parseLong(addressId));
											tvdrpovalue.setReceiver(new Long(receiver));
											tvdrpovalue.setLinkMan(linkMan);
											tvdrpovalue.setTel(tel);
//										}
//										else{
//											tvdrpovalue.setReceiver(tempPO.getOrderOrgId());
//										}
										tvdrpovalue.setReqStatus(Constant.ORDER_REQ_STATUS_01);
									}
									tvdrpovalue.setReqExecStatus(Constant.REQ_EXEC_STATUS_01);
									tvdrpovalue.setCreateBy(userId);
									tvdrpovalue.setCreateDate(new Date(System.currentTimeMillis()));
									tvdrpovalue.setReqTotalPrice(totalPrice);
									tvdrpovalue.setDiscount(discountPrice);
									tvdrpovalue.setVer(0);
										
									//String orderNO = tempPO.getOrderNo();
									// 获得业务范围编码
									Map<String,String> codeMap  = GetOrderNOUtil.getAreaShortcode(tempPO.getAreaId().toString());
									String areaCode = codeMap.get("AREA_SHORTCODE");
									// 获得经销商代码
									TmDealerPO tmDealerPO = new TmDealerPO();
									tmDealerPO.setDealerId(tempPO.getOrderOrgId());
									List<PO> dealerList = dao.select(tmDealerPO);
									String dealerCode = ((TmDealerPO)dealerList.get(0)).getDealerCode();
									// 获得发运订单编号
//									String dlvryReqNO = GetOrderNOUtil.getDlvryReqNO(areaCode, "D", dealerCode);
									
									String dlvryReqNO = OrderCode.getDLVRY_CG(tvdrpovalue.getFundType());
									tvdrpovalue.setDlvryReqNo(dlvryReqNO);
									
									dao.insert(tvdrpovalue);					//插入发运申请表
									//向发运申请操作日志表写入日志信息
									ReqLogUtil.creatReqLog(req_id, Constant.REQ_LOG_TYPE_01, logonUser.getUserId());
									reqTotalAmount = 0;
									reqTotalPrice = new Double(0);
									reqDiscountPrice = new Double(0);
								}else{
									reqTotalAmount = tvdrpovalue.getReqTotalAmount()+ Integer.parseInt(applyAmount[i]);
									reqTotalPrice = tvdrpovalue.getReqTotalPrice() + totalPrice;
									reqDiscountPrice = tvdrpovalue.getDiscount() + discountPrice;
									tvdrpo.setReqId(tvdrpovalue.getReqId());
									tvdrpovalue.setReqTotalAmount(reqTotalAmount);
									tvdrpovalue.setReqTotalPrice(reqTotalPrice);
									tvdrpovalue.setDiscount(reqDiscountPrice);
									dao.update(tvdrpo, tvdrpovalue);	//更新发运申请表 发运总量
								}
								
								
								
								
								tvdrdpo.setDetailId(Long.parseLong(SequenceManager.getSequence("")));//设置发运申请明细主键
								tvdrdpo.setOrderDetailId(Long.parseLong(detailId[i]));
								tvdrdpo.setMaterialId(Long.parseLong(materialId[i]));
								tvdrdpo.setPriceId(priceListIds[i]==null||"".equals(priceListIds[i])?null:new Long(priceListIds[i]));
								if(priceListIds[i]==null||"0".equals(priceListIds[i])||"-1".equals(priceListIds[i])){
									throw new BizException("erp价格id错误，请联系管理员！");
								}
								tvdrdpo.setReqId(tvdrpovalue.getReqId());
								tvdrdpo.setReqAmount(Integer.parseInt(applyAmount[i]));
								tvdrdpo.setCreateBy(userId);
								tvdrdpo.setCreateDate(new Date(System.currentTimeMillis()));
								tvdrdpo.setSinglePrice(Double.parseDouble(singlePrice[i]));
								tvdrdpo.setTotalPrice(Double.parseDouble(orderPriceSum[i]));
//								tvdrdpo.setDiscountRate(Float.parseFloat(discount_rate[i]));
//								tvdrdpo.setDiscountSPrice(Double.parseDouble(discount_s_price[i]));
//								tvdrdpo.setDiscountPrice(Double.parseDouble(discount_price[i]));
								tvdrdpo.setDealerReqAmount(Integer.parseInt(applyAmount[i])); //update by zhulei on 2016-08-16 for 插入经销商原始申请数量
								tvdrdpo.setVer(0);
								dao.insert(tvdrdpo);					//插入发运申请明细表
								
								tsodpContion.setDetailId(Long.parseLong(detailId[i]));
								TtVsOrderDetailPO tempdet = new TtVsOrderDetailPO() ;
								tempdet.setDetailId(Long.parseLong(detailId[i]));
								tempdet = (TtVsOrderDetailPO)dao.select(tempdet).get(0) ;
								
								if(tempdet.getCheckAmount().intValue() < tempdet.getCallAmount().intValue() + Integer.parseInt(applyAmount[i])) {
									throw new BizException("本次申请数量过大，请重新输入！") ;
								}
								
								tsodpValue.setCallAmount(tempdet.getCallAmount().intValue()+Integer.parseInt(applyAmount[i]));
								tsodpValue.setUpdateBy(userId);
								//tsodpValue.setDiscountRate(Float.parseFloat(discount_rate[i]));			//折扣率
								//tsodpValue.setDiscountSPrice(Double.parseDouble(discount_s_price[i]));	//折扣后单价
								//tsodpValue.setDiscountPrice(Double.parseDouble(discount_price[i]));		//折扣额
								tsodpValue.setUpdateDate(new Date(System.currentTimeMillis()));
								dao.update(tsodpContion, tsodpValue);//更新订单明细表
								
								/*if("0".equals(isCheck)){
									tsapContion.setAccountId(Long.parseLong(accountId));
									tsapValue.setAvailableAmount(Double.parseDouble(availableAmount)-totalPrice);
									tsapValue.setFreezeAmount(Double.parseDouble(freezeAmount)+totalPrice);
									tsapValue.setUpdateBy(userId);
									tsapValue.setUpdateDate(new Date(System.currentTimeMillis()));
									dao.update(tsapContion, tsapValue);	//更新账户表
								}*/
							}
						}
					
						// 循环跳出后冻结最后一次资金
						if(applyAmount != null && applyAmount.length != 0){
							// if("0".equals(isCheck)){
								// 冻结资金
								if(!accountId.equals("")){
									dmsFreezePrice_Report(tvdrpovalue.getReqId().toString(), accountId, new BigDecimal(tvdrpovalue.getReqTotalPrice()), logonUser.getUserId().toString());
								}
								if(!discountAccountId.equals("")){
									dmsFreezePrice_Report(tvdrpovalue.getReqId().toString(), discountAccountId, new BigDecimal(tvdrpovalue.getDiscount()), logonUser.getUserId().toString());
								}
								/*// 冻结资金
								if(!accountId.equals("")){
									accoutDao.syncAccountFreeze(tvdrpovalue.getReqId().toString(), accountId, new BigDecimal(tvdrpovalue.getReqTotalPrice()), logonUser.getUserId().toString());
								}
								if(!discountAccountId.equals("")){
									accoutDao.syncAccountFreeze(tvdrpovalue.getReqId().toString(), discountAccountId, new BigDecimal(tvdrpovalue.getDiscount()), logonUser.getUserId().toString());
								}*/
							// }
						}
					}
				}
			}
			act.setOutData("returnValue", returnValue);
		} catch(RuntimeException e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.PUTIN_FAILURE_CODE,e.getMessage());
			logger.error(logonUser,e1);
			act.setException(e);
		} catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"常规订单发运申请");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 常规订单发运申请(otd)
	 */
	public void applyDelvSubmit(){
		
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		Long userId = logonUser.getUserId();
		
		try{
			String applyAmounts = request.getParamValue("Amounts");			//申请发运数量
			String callAmounts = request.getParamValue("cAmounts");		    //已申请数量
			String orderIds = request.getParamValue("orderIds");		    //订单ID
			String detailIds = request.getParamValue("detailIds");			//订单明细ID
			String materialIds = request.getParamValue("materialIds");		//物料ID
			String singlePrices = request.getParamValue("singlePrices");	//物料单价
			String typeId = request.getParamValue("typeId");				//账户类型ID
			String accountId = request.getParamValue("accountId");			//经销商账户ID
			String availableAmount = request.getParamValue("availableAmount");//经销商账户可用余额
			String freezeAmount = request.getParamValue("freezeAmount");	//经销商账户预扣
			String transportType = request.getParamValue("transportType");	//发运方式
			String addressId = request.getParamValue("addressId");			//发运地址ID
			String areasIds = request.getParamValue("areaIds");				//业务范围
			String isCheck = request.getParamValue("isCheck");				//资金开关
			String fleetId = request.getParamValue("fleetId");			    //集团客户ID
			String address = request.getParamValue("address");			    //集团客户地址
			String priceId = CommonUtils.checkNull(request.getParamValue("priceId"));// 价格类型
			String otherPriceReason = CommonUtils.checkNull(request.getParamValue("otherPriceReason"));// 使用其他价格原因
//			String discount = CommonUtils.checkNull(request.getParamValue("discount"));// 使用折让
			String receiver = CommonUtils.checkNull(request.getParamValue("receiver"));// 收货方
			String linkMan = CommonUtils.checkNull(request.getParamValue("linkMan"));// 联系人
			String tel = CommonUtils.checkNull(request.getParamValue("tel"));// 联系电话
			String discountAccountId = CommonUtils.checkNull(request.getParamValue("discountAccountId"));// 折扣账户
			String reqRemark = CommonUtils.checkNull(request.getParamValue("orderRemark")).trim() ;
			String tmp_license_amount = CommonUtils.checkNull(request.getParamValue("tmp_license_amount")) ; //临牌数量
//			String discount_rates = CommonUtils.checkNull(request.getParamValue("DISCOUNT_rate_"));
//			String discount_s_prices = CommonUtils.checkNull(request.getParamValue("DISCOUNT_s_price_"));
//			String discount_prices = CommonUtils.checkNull(request.getParamValue("DISCOUNT_price_"));
			String totailAccount = CommonUtils.checkNull(request.getParamValue("totailAccount"));//订单总价
			String isCover = CommonUtils.checkNull(request.getParamValue("isCover"));            //是否代交车
			String orderPriceSums = request.getParamValue("orderPriceSums");   //订单价格合计
			
			String[] applyAmount = applyAmounts.split(",");					//取得所有数量放在数组中
			String[] detailId = detailIds.split(",");						
			String[] orderId = orderIds.split(",");
			String[] areasId = areasIds.split(",");
			String[] singlePrice = singlePrices.split(",");
			String[] materialId = materialIds.split(",");
			String[] callAmount = callAmounts.split(",");
			String priceListId=request.getParamValue("priceListIds");
			String [] priceListIds=priceListId.split(",");
			
			
//			String[] discount_rate = discount_rates.split(",");
//			String[] discount_s_price = discount_s_prices.split(",");
//			String[] discount_price = discount_prices.split(",");
			String[] orderPriceSum = orderPriceSums.split(",");
			
			TtVsOrderDetailPO tsodpContion = new TtVsOrderDetailPO();		//订单明细表
			TtVsOrderDetailPO tsodpValue = new TtVsOrderDetailPO();
			TtVsAccountPO tsapContion = new TtVsAccountPO();				//账户表
			TtVsAccountPO tsapValue = new TtVsAccountPO();
			TtVsDlvryReqPO tvdrpo = new TtVsDlvryReqPO();					//发运申请表
			TtVsDlvryReqPO tvdrpovalue = new TtVsDlvryReqPO();			    //发运申请表
			TtVsDlvryReqDtlPO tvdrdpo = new TtVsDlvryReqDtlPO();			//发运申请明细表
			String orderIdflg = "";
			Integer reqTotalAmount = 0;
			Double reqTotalPrice = new Double(0);
			Double reqDiscountPrice = new Double(0);
			
			String returnValue = "1" ;
			//验证价格
			boolean priceFlag=true;
			priceFlag=dao.judgePrice(priceListIds);
			if(priceFlag){
				returnValue="p";
				act.setOutData("returnValue", returnValue);
				return ;
			}
			
			//校验当前数据是否满足资源
			
			boolean resflag=true;
			resflag=dao.judgeResource(materialId,applyAmount,detailId);
			if(!resflag){
				returnValue="a";
				act.setOutData("returnValue", returnValue);
				return ;
				
			}
			if(!"".equals(applyAmount) && applyAmount != null){
				for (int i = 0; i < applyAmount.length; i++) {
					if (applyAmount[i] != null && !"".equals(applyAmount[i]) && Integer.parseInt(applyAmount[i]) > 0) {
						if(singlePrice[i].equals("") || singlePrice[i].equals("0")) {
							returnValue = "4" ;
							break ;
						}
					}
				}
				
				if(returnValue != "4"){
					MonthGeneralOrderCall mgoc = new MonthGeneralOrderCall() ;
					String str = mgoc.getMatCode(detailId, applyAmount) ;
					
					if(!CommonUtils.isNullString(str)){
						act.setOutData("metStr", str);
						returnValue = "3" ;
					} else {
						for(int i=0; i<applyAmount.length; i++){
							if(!"".equals(applyAmount[i])&&applyAmount[i]!=null&&Integer.parseInt(applyAmount[i])>0){
								String orderIdvalue = orderId[i].toString();
								
								// 计算发运申请总价和折扣额总价
								Double totalPrice = Double.parseDouble(singlePrice[i])*Integer.parseInt(applyAmount[i]);
//								Double discountPrice = Double.parseDouble(singlePrice[i])*Integer.parseInt(applyAmount[i]) * Float.parseFloat(discount_rate[i])/100;
								Double discountPrice = Double.parseDouble(singlePrice[i])*Integer.parseInt(applyAmount[i]);
							
								if(!orderIdflg.equals(orderIdvalue)){
									// 订单号不相等并且不是循环第一次时冻结资金
									if(!orderIdflg.equals("")){
										// if("0".equals(isCheck)){
											// 冻结资金
											if(!accountId.equals("")){
												dmsFreezePrice_Report(tvdrpovalue.getReqId().toString(), accountId, new BigDecimal(tvdrpovalue.getReqTotalPrice()), logonUser.getUserId().toString());
											}
											if(!discountAccountId.equals("")){
												dmsFreezePrice_Report(tvdrpovalue.getReqId().toString(), discountAccountId, new BigDecimal(tvdrpovalue.getDiscount()), logonUser.getUserId().toString());
											}
											/*// 冻结资金
											if(!accountId.equals("")){
												accoutDao.syncAccountFreeze(tvdrpovalue.getReqId().toString(), accountId, new BigDecimal(tvdrpovalue.getReqTotalPrice()), logonUser.getUserId().toString());
											}
											if(!discountAccountId.equals("")){
												accoutDao.syncAccountFreeze(tvdrpovalue.getReqId().toString(), discountAccountId, new BigDecimal(tvdrpovalue.getDiscount()), logonUser.getUserId().toString());
											}*/
										// }
									}
									
									orderIdflg = orderIdvalue;
									tvdrpovalue = new TtVsDlvryReqPO();	
									Long req_id = Long.parseLong(SequenceManager.getSequence(""));
									tvdrpovalue.setReqId(req_id);//设置申请主键
									tvdrpovalue.setOrderId(Long.parseLong(orderIdvalue));
									tvdrpovalue.setAreaId(Long.parseLong(areasId[i]));
									tvdrpovalue.setFundType(Long.parseLong(typeId));
									tvdrpovalue.setReqTotalAmount(Integer.parseInt(applyAmount[i]));
									tvdrpovalue.setReqDate(new Date(System.currentTimeMillis()));
									tvdrpovalue.setPriceId(priceId);
									tvdrpovalue.setOtherPriceReason(otherPriceReason);
									tvdrpovalue.setReqRemark(reqRemark) ;
									tvdrpovalue.setTmpLicenseAmount(new Integer(tmp_license_amount));
									tvdrpovalue.setWarehouseId(new Long("2012111519032210"));
									TtVsOrderPO ttVsOrderPO = new TtVsOrderPO();
									ttVsOrderPO.setOrderId(Long.parseLong(orderId[i]));
									List orderList = dao.select(ttVsOrderPO);
									TtVsOrderPO tempPO = (TtVsOrderPO)orderList.get(0);
									
									if(null != isCover && "1".equals(isCover)){
										tvdrpovalue.setIsFleet(1);
										tvdrpovalue.setFleetId(Long.parseLong(fleetId));
										tvdrpovalue.setFleetAddress(address);
										tvdrpovalue.setReqStatus(Constant.ORDER_REQ_STATUS_08);
									}
									else{
										tvdrpovalue.setIsFleet(0);
										tvdrpovalue.setDeliveryType(Integer.parseInt(transportType));
//										if(Integer.parseInt(transportType)==Constant.TRANSPORT_TYPE_02){
											tvdrpovalue.setAddressId(Long.parseLong(addressId));
											tvdrpovalue.setReceiver(new Long(receiver));
											tvdrpovalue.setLinkMan(linkMan);
											tvdrpovalue.setTel(tel);
//										}
//										else{
//											tvdrpovalue.setReceiver(tempPO.getOrderOrgId());
//										}
										tvdrpovalue.setReqStatus(Constant.ORDER_REQ_STATUS_01);
									}
									tvdrpovalue.setReqExecStatus(Constant.REQ_EXEC_STATUS_01);
									tvdrpovalue.setCreateBy(userId);
									tvdrpovalue.setCreateDate(new Date(System.currentTimeMillis()));
									tvdrpovalue.setReqTotalPrice(totalPrice);
									tvdrpovalue.setDiscount(discountPrice);
									tvdrpovalue.setVer(0);
										
									//String orderNO = tempPO.getOrderNo();
									// 获得业务范围编码
									Map<String,String> codeMap  = GetOrderNOUtil.getAreaShortcode(tempPO.getAreaId().toString());
									String areaCode = codeMap.get("AREA_SHORTCODE");
									// 获得经销商代码
									TmDealerPO tmDealerPO = new TmDealerPO();
									tmDealerPO.setDealerId(tempPO.getOrderOrgId());
									List<PO> dealerList = dao.select(tmDealerPO);
									String dealerCode = ((TmDealerPO)dealerList.get(0)).getDealerCode();
									// 获得发运订单编号
//									String dlvryReqNO = GetOrderNOUtil.getDlvryReqNO(areaCode, "D", dealerCode);
									
									String dlvryReqNO = OrderCode.getDLVRY_CG(tvdrpovalue.getFundType());
									tvdrpovalue.setDlvryReqNo(dlvryReqNO);
									
									dao.insert(tvdrpovalue);					//插入发运申请表
									//向发运申请操作日志表写入日志信息
									ReqLogUtil.creatReqLog(req_id, Constant.REQ_LOG_TYPE_01, logonUser.getUserId());
									reqTotalAmount = 0;
									reqTotalPrice = new Double(0);
									reqDiscountPrice = new Double(0);
								}else{
									reqTotalAmount = tvdrpovalue.getReqTotalAmount()+ Integer.parseInt(applyAmount[i]);
									reqTotalPrice = tvdrpovalue.getReqTotalPrice() + totalPrice;
									reqDiscountPrice = tvdrpovalue.getDiscount() + discountPrice;
									tvdrpo.setReqId(tvdrpovalue.getReqId());
									tvdrpovalue.setReqTotalAmount(reqTotalAmount);
									tvdrpovalue.setReqTotalPrice(reqTotalPrice);
									tvdrpovalue.setDiscount(reqDiscountPrice);
									dao.update(tvdrpo, tvdrpovalue);	//更新发运申请表 发运总量
								}
								
								
								
								Long reqDetailId=Long.parseLong(SequenceManager.getSequence(""));
								tvdrdpo.setDetailId(reqDetailId);//设置发运申请明细主键
								tvdrdpo.setOrderDetailId(Long.parseLong(detailId[i]));
								tvdrdpo.setMaterialId(Long.parseLong(materialId[i]));
								tvdrdpo.setPriceId(priceListIds[i]==null||"".equals(priceListIds[i])?null:new Long(priceListIds[i]));
								if(priceListIds[i]==null||"0".equals(priceListIds[i])||"-1".equals(priceListIds[i])){
									throw new BizException("erp价格id错误，请联系管理员！");
								}
								tvdrdpo.setReqId(tvdrpovalue.getReqId());
								tvdrdpo.setReqAmount(Integer.parseInt(applyAmount[i]));
								tvdrdpo.setCreateBy(userId);
								tvdrdpo.setCreateDate(new Date(System.currentTimeMillis()));
								tvdrdpo.setSinglePrice(Double.parseDouble(singlePrice[i]));
								tvdrdpo.setTotalPrice(Double.parseDouble(orderPriceSum[i]));
								tvdrdpo.setReserveAmount(Integer.parseInt(applyAmount[i]));
								tvdrdpo.setVer(0);
								dao.insert(tvdrdpo);					//插入发运申请明细表
								//向资源保留表中加入数据
								ResourceReserveQuery rrq = new ResourceReserveQuery();
								TtVsOrderResourceReservePO tvorrp = new TtVsOrderResourceReservePO();
								tvorrp.setReserveId(Long.parseLong(SequenceManager.getSequence("")));
								tvorrp.setReqDetailId(reqDetailId);
								tvorrp.setMaterialId(new Long(materialId[i]));
								tvorrp.setBatchNo((i+1)+"");
								tvorrp.setAmount(Integer.parseInt(applyAmount[i]));
								tvorrp.setReserveStatus(Constant.RESOURCE_RESERVE_STATUS_01);
								tvorrp.setOemCompanyId(logonUser.getCompanyId());
								tvorrp.setWarehouseId(new Long("2012111519032210"));
								tvorrp.setReserveType(Constant.RESERVE_TYPE_01);
								tvorrp.setCreateBy(logonUser.getUserId());
								tvorrp.setCreateDate(new java.util.Date());
								dao.insert(tvorrp);
								Long logId=Long.parseLong(SequenceManager.getSequence(""));
								rrq.insDtlLog(logId, detailId[i], i+"", applyAmount[i], "0", materialId[i], logonUser.getUserId());
								
								tsodpContion.setDetailId(Long.parseLong(detailId[i]));
								TtVsOrderDetailPO tempdet = new TtVsOrderDetailPO() ;
								tempdet.setDetailId(Long.parseLong(detailId[i]));
								tempdet = (TtVsOrderDetailPO)dao.select(tempdet).get(0) ;
								
								if(tempdet.getCheckAmount().intValue() < tempdet.getCallAmount().intValue() + Integer.parseInt(applyAmount[i])) {
									throw new BizException("本次申请数量过大，请重新输入！") ;
								}
								
								tsodpValue.setCallAmount(tempdet.getCallAmount().intValue()+Integer.parseInt(applyAmount[i]));
								
								tsodpValue.setUpdateBy(userId);
								tsodpValue.setUpdateDate(new Date(System.currentTimeMillis()));
								dao.update(tsodpContion, tsodpValue);//更新订单明细表
							}
						}
					
						// 循环跳出后冻结最后一次资金
						if(applyAmount != null && applyAmount.length != 0){
							// if("0".equals(isCheck)){
								// 冻结资金
								if(!accountId.equals("")){
									dmsFreezePrice_Report(tvdrpovalue.getReqId().toString(), accountId, new BigDecimal(tvdrpovalue.getReqTotalPrice()), logonUser.getUserId().toString());
								}
								if(!discountAccountId.equals("")){
									dmsFreezePrice_Report(tvdrpovalue.getReqId().toString(), discountAccountId, new BigDecimal(tvdrpovalue.getDiscount()), logonUser.getUserId().toString());
								}
								/*// 冻结资金
								if(!accountId.equals("")){
									accoutDao.syncAccountFreeze(tvdrpovalue.getReqId().toString(), accountId, new BigDecimal(tvdrpovalue.getReqTotalPrice()), logonUser.getUserId().toString());
								}
								if(!discountAccountId.equals("")){
									accoutDao.syncAccountFreeze(tvdrpovalue.getReqId().toString(), discountAccountId, new BigDecimal(tvdrpovalue.getDiscount()), logonUser.getUserId().toString());
								}*/
							// }
						}
					}
				}
			}
			act.setOutData("returnValue", returnValue);
		} catch(RuntimeException e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.PUTIN_FAILURE_CODE,e.getMessage());
			logger.error(logonUser,e1);
			act.setException(e);
		} catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"常规订单发运申请");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 常规订单发运申请（针对订单）
	 */
	public void applyTotalInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			List<Map<String,Object>> dateList = dao.getGeneralDeliveryDateList_New(oemCompanyId);//dao.getGeneralDateList();
			
			List<Map<String, Object>> areaList = new ArrayList<Map<String, Object>>() ;
			
			String para = CommonDAO.getPara(Constant.CHANA_SYS.toString()) ;
			
			if(Constant.COMPANY_CODE_JC.equals(para.toUpperCase())) {
				areaList = MaterialGroupManagerDao.getDealerLevelBusiness(logonUser.getPoseId().toString());
			} else if (Constant.COMPANY_CODE_CVS.equals(para.toUpperCase())) {
				areaList = MaterialGroupManagerDao.getDealerLevelBusinessAll(logonUser.getPoseId().toString());
			} else {
				throw new RuntimeException("判断当前系统的系统参数错误！") ;
			}
			act.setOutData("dateList", dateList);
			act.setOutData("areaList", areaList);
			act.setForword(totalInitUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"常规订单发运申请初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 常规订单发运申请查询(单个订单)（点击其中某个订单）
	 */
	public void applyTotal(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			ActionContextExtend atx = (ActionContextExtend) ActionContext.getContext();
			List<Map<String, Object>> areaList = new ArrayList<Map<String, Object>>() ;
			String reqURL = atx.getRequest().getContextPath();
			if("/CVS-SALES".equals(reqURL.toUpperCase())){
				act.setOutData("returnValue", 1);
			}else{
				act.setOutData("returnValue", 2);
			}
			String para = CommonDAO.getPara(Constant.CHANA_SYS.toString()) ;
			if(Constant.COMPANY_CODE_JC.equals(para.toUpperCase())) {
				areaList = MaterialGroupManagerDao.getDealerLevelBusiness(logonUser.getPoseId().toString());
			} else if (Constant.COMPANY_CODE_CVS.equals(para.toUpperCase())) {
				areaList = MaterialGroupManagerDao.getDealerLevelBusinessAll(logonUser.getPoseId().toString());
			} else {
				throw new RuntimeException("判断当前系统的系统参数错误！") ;
			}
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			List<Map<String,Object>> dateList = dao.getGeneralDeliveryDateList_New(oemCompanyId);//dao.getGeneralDateList();
			String orderYearWeek =dateList.get(0).get("code").toString();
			String[] array = orderYearWeek.split("-");
			String orderYear = array[0];
			String orderWeek = array[1];
			String areaId = request.getParamValue("areaId");		//业务范围
			String dealerId = null;	//经销商ID
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId")) ;
			TtVsOrderPO tvo=new TtVsOrderPO();
			tvo.setOrderId(new Long(orderId));
			tvo=(TtVsOrderPO) dao.select(tvo).get(0);
			dealerId=tvo.getOrderOrgId().toString();
			 oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			 Map<String,String >map=new HashMap<String,String>();
				map.put("orderYear", orderYear);
				map.put("orderWeek", orderWeek);
				map.put("areaId", areaId);
				map.put("oemCompanyId", oemCompanyId.toString());
				map.put("orderId", orderId);
				List<Map<String, Object>> list = dao.getDeliveryDetailList(map);
			List<Map<String,Object>> accountlist = dao.getDealerAccount(dealerId);
			List<Map<String,Object>> addresslist = dao.getDealerAddress(dealerId);
			// 获得是否需要资金检查
			TmBusinessParaPO para1 = dao.getTmBusinessParaPO(Constant.URGENT_ORDER_ACCOUNT_CHECK_PARA,oemCompanyId);
			String isCheck = para1.getParaValue();
			// 获得订单启票最大折扣点
			TmBusinessParaPO para2 = dao.getTmBusinessParaPO(
					Constant.ORDER_REPORT_MAX_DISCOUNT_RATE_PARA, oemCompanyId);
			String ratePara = para2.getParaValue();
			//获取价格类型
			TmDealerPO tdPo=new TmDealerPO();
			tdPo.setDealerId(new Long(dealerId));
			tdPo=(TmDealerPO) dao.select(tdPo).get(0);
			act.setOutData("priceId", tdPo.getPersonCharge());
			act.setOutData("isCheck",isCheck);
			act.setOutData("list", list);
			act.setOutData("accountlist", accountlist);
			act.setOutData("addresslist",addresslist);
			act.setOutData("dealerId", dealerId);
			act.setOutData("ratePara", ratePara);
			act.setOutData("areaList", areaList);
			act.setOutData("curDealerId", logonUser.getDealerId());
			act.setForword(totalDetailUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"常规订单发运申请");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 常规订单发运申请（针对订单）
	 */
	public void applyTotalQuery(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			ActionContextExtend atx = (ActionContextExtend) ActionContext.getContext();
			String reqURL = atx.getRequest().getContextPath();
			if("/CVS-SALES".equals(reqURL.toUpperCase())){
				act.setOutData("returnValue", 1);
			}else{
				act.setOutData("returnValue", 2);
			}
			String orderYearWeek = request.getParamValue("orderYearWeek");
			String[] array = orderYearWeek.split("-");
			String orderYear = array[0];
			String orderWeek = array[1];
			String areaId = request.getParamValue("areaId");		//业务范围
			String dealerId = request.getParamValue("dealerId");	//经销商ID
			String orderNo = CommonUtils.checkNull(request.getParamValue("orderNO")) ;
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			String startDate=CommonUtils.checkNull(request.getParamValue("startDate")) ;
			String endDate=CommonUtils.checkNull(request.getParamValue("endDate"));
			String dealerCodes=CommonUtils.checkNull(request.getParamValue("dealerCodes"));
			
			Map<String,String >map=new HashMap<String,String>();
			map.put("orderYear", orderYear);
			map.put("orderWeek", orderWeek);
			map.put("areaId", areaId);
			map.put("dealerId", dealerId);
			map.put("startDate", startDate);
			map.put("endDate", endDate);
			map.put("oemCompanyId", oemCompanyId.toString());
			map.put("orderNo", orderNo);
			map.put("dealerCodes",dealerCodes);
			PageResult<Map<String, Object>> ps = dao.getApplyTotalQuery(map,10,curPage);
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"常规订单发运主表查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 常规订单发运申请（针对订单明细）
	 */
	public void applyDetail(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
				ActionContextExtend atx = (ActionContextExtend) ActionContext.getContext();
				List<Map<String, Object>> areaList = new ArrayList<Map<String, Object>>() ;
				String reqURL = atx.getRequest().getContextPath();
				if("/CVS-SALES".equals(reqURL.toUpperCase())){
					act.setOutData("returnValue", 1);
				}else{
					act.setOutData("returnValue", 2);
				}
				String para = CommonDAO.getPara(Constant.CHANA_SYS.toString()) ;
				
				if(Constant.COMPANY_CODE_JC.equals(para.toUpperCase())) {
					areaList = MaterialGroupManagerDao.getDealerLevelBusiness(logonUser.getPoseId().toString());
				} else if (Constant.COMPANY_CODE_CVS.equals(para.toUpperCase())) {
					areaList = MaterialGroupManagerDao.getDealerLevelBusinessAll(logonUser.getPoseId().toString());
				} else {
					throw new RuntimeException("判断当前系统的系统参数错误！") ;
				}
				Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
				List<Map<String,Object>> dateList = dao.getGeneralDeliveryDateList_New(oemCompanyId);//dao.getGeneralDateList();
				String orderYearWeek =dateList.get(0).get("code").toString();
				String[] array = orderYearWeek.split("-");
				String orderYear = array[0];
				String orderWeek = array[1];
				String areaId = request.getParamValue("areaId");		//业务范围
				String dealerId = request.getParamValue("dealerId");	//经销商ID
				String orderNo = CommonUtils.checkNull(request.getParamValue("orderNO")) ;
				Map<String,String >map=new HashMap<String,String>();
				map.put("orderYear", orderYear);
				map.put("orderWeek", orderWeek);
				map.put("areaId", areaId);
				map.put("oemCompanyId", oemCompanyId.toString());
				map.put("orderNo", orderNo);
			//	List<Map<String, Object>> list = dao.getDeliveryDetailList(map);
				List<Map<String,Object>> accountlist = dao.getDealerAccount(dealerId);
				List<Map<String,Object>> addresslist = dao.getDealerAddress(dealerId);
				// 获得是否需要资金检查
			//	TmBusinessParaPO para = dao.getTmBusinessParaPO(Constant.URGENT_ORDER_ACCOUNT_CHECK_PARA,oemCompanyId);
//				String isCheck = para.getParaValue();
				//// 获得订单启票最大折扣点
//				TmBusinessParaPO para2 = dao.getTmBusinessParaPO(
//						Constant.ORDER_REPORT_MAX_DISCOUNT_RATE_PARA, oemCompanyId);
//				String ratePara = para2.getParaValue();
				//获取价格类型
			
//				act.setOutData("isCheck",isCheck);
				//act.setOutData("list", list);
				act.setOutData("accountlist", accountlist);
				act.setOutData("addresslist",addresslist);
				act.setOutData("dealerId", dealerId);
//				act.setOutData("ratePara", ratePara);
				act.setOutData("dateList", dateList);
				act.setOutData("areaList", areaList);
				act.setForword(detailInitUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"常规订单发运申请初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 常规订单发运申请（针对订单明细）
	 */
	public void applyDetailQuery(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
				ActionContextExtend atx = (ActionContextExtend) ActionContext.getContext();
				List<Map<String, Object>> areaList = new ArrayList<Map<String, Object>>() ;
				String reqURL = atx.getRequest().getContextPath();
				if("/CVS-SALES".equals(reqURL.toUpperCase())){
					act.setOutData("returnValue", 1);
				}else{
					act.setOutData("returnValue", 2);
				}
				String para = CommonDAO.getPara(Constant.CHANA_SYS.toString()) ;
				if(Constant.COMPANY_CODE_JC.equals(para.toUpperCase())) {
					areaList = MaterialGroupManagerDao.getDealerLevelBusiness(logonUser.getPoseId().toString());
				} else if (Constant.COMPANY_CODE_CVS.equals(para.toUpperCase())) {
					areaList = MaterialGroupManagerDao.getDealerLevelBusinessAll(logonUser.getPoseId().toString());
				} else {
					throw new RuntimeException("判断当前系统的系统参数错误！") ;
				}
				Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
				List<Map<String,Object>> dateList = dao.getGeneralDeliveryDateList_New(oemCompanyId);//dao.getGeneralDateList();
				String orderYearWeek =dateList.get(0).get("code").toString();
				String[] array = orderYearWeek.split("-");
				String orderYear = array[0];
				String orderWeek = array[1];
				String areaId = request.getParamValue("areaId");		//业务范围
				String orderNo = CommonUtils.checkNull(request.getParamValue("orderNO")) ;
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
				String startDate=CommonUtils.checkNull(request.getParamValue("startDate")) ;
				String endDate=CommonUtils.checkNull(request.getParamValue("endDate"));
				String dealerCodes=CommonUtils.checkNull(request.getParamValue("dealerCodes"));
				Map<String,String >map=new HashMap<String,String>();
				map.put("orderYear", orderYear);
				map.put("orderWeek", orderWeek);
				map.put("areaId", areaId);
				map.put("startDate", startDate);
				map.put("endDate", endDate);
				map.put("oemCompanyId", oemCompanyId.toString());
				map.put("orderNo", orderNo);
				map.put("dealerCodes",dealerCodes);
				PageResult<Map<String, Object>> list = dao.getDeliveryDetailQuery(map,100,curPage);
				act.setOutData("ps", list);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"常规订单发运申请初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 发运经销商信息初始化
	 */
	public void queryDealerDelvInit() throws Exception{
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			ActionContextExtend atx = (ActionContextExtend) ActionContext.getContext();
			List<Map<String, Object>> areaList = new ArrayList<Map<String, Object>>() ;
			String reqURL = atx.getRequest().getContextPath();
			if("/CVS-SALES".equals(reqURL.toUpperCase())){
				act.setOutData("returnValue", 1);
			}else{
				act.setOutData("returnValue", 2);
			}
			String para = CommonDAO.getPara(Constant.CHANA_SYS.toString()) ;
			if(Constant.COMPANY_CODE_JC.equals(para.toUpperCase())) {
				areaList = MaterialGroupManagerDao.getDealerLevelBusiness(logonUser.getPoseId().toString());
			} else if (Constant.COMPANY_CODE_CVS.equals(para.toUpperCase())) {
				areaList = MaterialGroupManagerDao.getDealerLevelBusinessAll(logonUser.getPoseId().toString());
			} else {
				throw new RuntimeException("判断当前系统的系统参数错误！") ;
			}
			areaList=dao.getBusinessArea();
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId")) ;
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			TtVsOrderPO tvo=new TtVsOrderPO();
			tvo.setOrderId(new Long(orderId));
			tvo=(TtVsOrderPO) dao.select(tvo).get(0);
			String dealerId=tvo.getOrderOrgId().toString();
			List<Map<String,Object>> accountlist = dao.getDealerAccount(dealerId);
			List<Map<String,Object>> addresslist = dao.getDealerAddress(dealerId);
			// 获得是否需要资金检查
			TmBusinessParaPO para1 = dao.getTmBusinessParaPO(Constant.URGENT_ORDER_ACCOUNT_CHECK_PARA,oemCompanyId);
			String isCheck = para1.getParaValue();
			// 获得订单启票最大折扣点
			TmBusinessParaPO para2 = dao.getTmBusinessParaPO(
					Constant.ORDER_REPORT_MAX_DISCOUNT_RATE_PARA, oemCompanyId);
			String ratePara = para2.getParaValue();
			//获取价格类型
			TmDealerPO tdPo=new TmDealerPO();
			tdPo.setDealerId(new Long(dealerId));
			tdPo=(TmDealerPO) dao.select(tdPo).get(0);
			act.setOutData("priceId", tdPo.getPersonCharge());
			act.setOutData("isCheck",isCheck);
			act.setOutData("accountlist", accountlist);
			act.setOutData("addresslist",addresslist);
			act.setOutData("dealerId", dealerId);
			act.setOutData("ratePara", ratePara);
			act.setOutData("areaList", areaList);
			act.setOutData("curDealerId", logonUser.getDealerId());
			act.setForword(dlrDelvUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商公司查询初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
