/**
 * 
 */
package com.infodms.dms.actions.sales.ordermanage.orderreport;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.sales.financemanage.AccountBalanceDetailDao;
import com.infodms.dms.dao.sales.ordermanage.delivery.OrderDeliveryDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmBusinessParaPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TtVsDlvryReqDtlPO;
import com.infodms.dms.po.TtVsDlvryReqPO;
import com.infodms.dms.po.TtVsOrderDetailPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.businessUtil.GetOrderNOUtil;
import com.infodms.dms.util.businessUtil.ReqLogUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.ActionContextExtend;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;

import static com.infodms.dms.actions.sales.financemanage.AccountOpera.*;

/**
 * @author Administrator
 * 
 */
public class MonthGeneralOrderCall {

	public Logger logger = Logger.getLogger(MonthGeneralOrderCall.class);
	OrderDeliveryDao dao = OrderDeliveryDao.getInstance();
	private final AccountBalanceDetailDao accoutDao = AccountBalanceDetailDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();

	private final String initUrl = "/jsp/sales/ordermanage/orderreport/monthGeneralOrderCallPre.jsp";
	private final String listUrl = "/jsp/sales/ordermanage/orderreport/monthGeneralOrderCall.jsp";

	/**
	 * 常规订单发运申请页面初始化
	 */
	public void applyQueryInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			List<Map<String, Object>> dateList = dao.getMonthMakeDateList(oemCompanyId);
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getDealerBusiness(logonUser.getPoseId().toString(), Constant.DEALER_LEVEL_01.toString()) ;
			act.setOutData("dateList", dateList);
			act.setOutData("areaList", areaList);
			act.setForword(initUrl);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"月度常规订单启票初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 常规订单发运申请查询
	 */
	public void applyQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			ActionContextExtend atx = (ActionContextExtend) ActionContext.getContext();
			String reqURL = atx.getRequest().getContextPath();
			if("/CVS-SALES".equals(reqURL.toUpperCase())){
				act.setOutData("returnValue", 1);
			}else{
				act.setOutData("returnValue", 2);
			}
			String orderYearMonth = request.getParamValue("orderYearMonth");
			String[] array = orderYearMonth.split("-");
			String orderYear = array[0];
			String orderMonth = array[1];
			String areaId = request.getParamValue("areaId"); // 业务范围
			String dealerId = request.getParamValue("dealerId"); // 经销商ID
			String orderNo = request.getParamValue("orderNo"); // 订单号码
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			List<Map<String, Object>> list = dao.getMonthGeneralOrderCallList(orderYear,
					orderMonth, areaId, dealerId, dealerId, orderNo, oemCompanyId);
			List<Map<String, Object>> accountlist = dao.getDealerAccount(dealerId);
			// 获得是否需要资金检查
			TmBusinessParaPO para = dao.getTmBusinessParaPO(
					Constant.URGENT_ORDER_ACCOUNT_CHECK_PARA, oemCompanyId);
			String isCheck = para.getParaValue();
			// 获得订单启票最大折扣点
			TmBusinessParaPO para2 = dao.getTmBusinessParaPO(
					Constant.ORDER_REPORT_MAX_DISCOUNT_RATE_PARA, oemCompanyId);
			String ratePara = para2.getParaValue();
			act.setOutData("isCheck", isCheck);
			act.setOutData("list", list);
			act.setOutData("accountlist", accountlist);
			act.setOutData("dealerId", dealerId);
			act.setOutData("ratePara", ratePara);
			act.setForword(listUrl);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"月度常规订单启票查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 常规订单发运申请
	 */
	public void applySubmit() {

		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		Long userId = logonUser.getUserId();

		try {
			String[] detailId = request.getParamValues("detailId");
			String[] orderId = request.getParamValues("orderId");
			String[] areaId = request.getParamValues("areaId");
			String[] materialId = request.getParamValues("materialId");
			String[] callAmount = request.getParamValues("callAmount");
			String[] applyAmount = request.getParamValues("applyAmount");
			String[] singlePrice = request.getParamValues("singlePrice");
			String[] totalPrice = request.getParamValues("totalPrice");
			String[] discountRate = request.getParamValues("discountRate");
			String[] discountSPrice = request.getParamValues("discountSPrice");
			String[] discountPrice = request.getParamValues("discountPrice");

			String typeId = request.getParamValue("typeId"); // 账户类型ID
			String transportType = request.getParamValue("transportType"); // 发运方式
			String addressId = request.getParamValue("addressId"); // 发运地址ID
			String isCheck = request.getParamValue("isCheck"); // 资金开关
			String fleetId = request.getParamValue("fleetId"); // 大客户ID
			String address = request.getParamValue("address"); // 大客户地址
			String priceId = CommonUtils.checkNull(request.getParamValue("priceId"));// 价格类型
			String otherPriceReason = CommonUtils.checkNull(request.getParamValue("otherPriceReason"));// 使用其他价格原因
			String receiver = CommonUtils.checkNull(request.getParamValue("receiver"));// 收货方
			String linkMan = CommonUtils.checkNull(request.getParamValue("linkMan"));// 联系人
			String tel = CommonUtils.checkNull(request.getParamValue("tel"));// 联系电话
			String accountId = request.getParamValue("accountId"); // 经销商账户ID
			String discountAccountId = CommonUtils.checkNull(request.getParamValue("discountAccountId"));// 折扣账户
			String isCover = CommonUtils.checkNull(request.getParamValue("isCover")); // 是否代交车
			String reqTotalPrice = request.getParamValue("reqTotalPrice"); // 订单价格合计
			String discountTotalPrice = request.getParamValue("discountTotalPrice"); // 折扣额合计
			String reqTotalAmount = request.getParamValue("reqTotalAmount"); // 发运申请总量
			String dealerId = request.getParamValue("dealerId"); // 经销商id
			String reqRemark = CommonUtils.checkNull(request.getParamValue("orderRemark")).trim() ;
			
			String returnValue = "1" ;

			TtVsOrderDetailPO tsodpContion = new TtVsOrderDetailPO(); // 订单明细表
			TtVsOrderDetailPO tsodpValue = new TtVsOrderDetailPO();

			if (applyAmount != null && applyAmount.length != 0) {
				for (int i = 0; i < applyAmount.length; i++) {
					if (applyAmount[i] != null && !"".equals(applyAmount[i]) && Integer.parseInt(applyAmount[i]) > 0) {
						if(singlePrice[i].equals("") || singlePrice[i].equals("0")) {
							returnValue = "3" ;
							break ;
						}
					}
				}
				
				if(returnValue != "3"){
					String str = getMatCode(detailId, applyAmount) ;
					
					if(!CommonUtils.isNullString(str)){
						act.setOutData("metStr", str);
						returnValue = "2" ;
					} else {
						TtVsDlvryReqPO tvdrpo = new TtVsDlvryReqPO(); // 发运申请表
						Long reqId = Long.parseLong(SequenceManager.getSequence(""));
						tvdrpo.setReqId(reqId);
						tvdrpo.setAreaId(new Long(areaId[0]));
						tvdrpo.setOrderId(new Long(orderId[0]));
						tvdrpo.setFundType(new Long(typeId));
						if (null != isCover && "1".equals(isCover)) {
							tvdrpo.setIsFleet(1);
							tvdrpo.setFleetId(Long.parseLong(fleetId));
							tvdrpo.setFleetAddress(address);
							tvdrpo.setReqStatus(Constant.ORDER_REQ_STATUS_08);
						} else {
							tvdrpo.setIsFleet(0);
							tvdrpo.setDeliveryType(Integer.parseInt(transportType));
							if (Integer.parseInt(transportType) == Constant.TRANSPORT_TYPE_02) {
								tvdrpo.setAddressId(Long.parseLong(addressId));
								tvdrpo.setReceiver(new Long(receiver));
								tvdrpo.setLinkMan(linkMan);
								tvdrpo.setTel(tel);
							} else {
								tvdrpo.setReceiver(new Long(dealerId));
							}
							tvdrpo.setReqStatus(Constant.ORDER_REQ_STATUS_01);
						}
						tvdrpo.setReqDate(new Date());
						tvdrpo.setReqTotalAmount(new Integer(reqTotalAmount));
						tvdrpo.setReqExecStatus(Constant.REQ_EXEC_STATUS_01);
						tvdrpo.setVer(new Integer(0));
						tvdrpo.setCreateBy(userId);
						tvdrpo.setCreateDate(new Date());
						tvdrpo.setPriceId(priceId);
						tvdrpo.setOtherPriceReason(otherPriceReason);
						tvdrpo.setDiscount(new Double(discountTotalPrice));
						tvdrpo.setReqTotalPrice(new Double(reqTotalPrice));
						tvdrpo.setReqRemark(reqRemark) ;
						// 获得业务范围编码
						Map<String, String> codeMap = GetOrderNOUtil.getAreaShortcode(areaId[0]);
						String areaCode = codeMap.get("AREA_SHORTCODE");
						// 获得经销商代码
						TmDealerPO tmDealerPO = new TmDealerPO();
						tmDealerPO.setDealerId(new Long(dealerId));
						List<PO> dealerList = dao.select(tmDealerPO);
						String dealerCode = ((TmDealerPO) dealerList.get(0)).getDealerCode();
						// 获得发运订单编号
						String dlvryReqNO = GetOrderNOUtil.getDlvryReqNO(areaCode, "D", dealerCode);
						tvdrpo.setDlvryReqNo(dlvryReqNO);
		
						dao.insert(tvdrpo);// 插入发运申请
						//向发运申请操作日志表写入日志信息
						ReqLogUtil.creatReqLog(reqId, Constant.REQ_LOG_TYPE_01, logonUser.getUserId());
						
						for (int i = 0; i < applyAmount.length; i++) {
							if (applyAmount[i] != null && !"".equals(applyAmount[i]) && Integer.parseInt(applyAmount[i]) > 0) {
								TtVsDlvryReqDtlPO tvdrdpo = new TtVsDlvryReqDtlPO(); // 发运申请明细表
								tvdrdpo.setDetailId(Long.parseLong(SequenceManager.getSequence("")));// 设置发运申请明细主键
								tvdrdpo.setReqId(tvdrpo.getReqId());
								tvdrdpo.setOrderDetailId(Long.parseLong(detailId[i]));
								tvdrdpo.setMaterialId(Long.parseLong(materialId[i]));
								tvdrdpo.setReqAmount(Integer.parseInt(applyAmount[i]));
								tvdrdpo.setVer(0);
								tvdrdpo.setCreateBy(userId);
								tvdrdpo.setCreateDate(new Date());
								tvdrdpo.setSinglePrice(Double.parseDouble(singlePrice[i]));
								tvdrdpo.setTotalPrice(Double.parseDouble(totalPrice[i]) - Double.parseDouble(discountPrice[i]));
								tvdrdpo.setDiscountRate(Float.parseFloat(discountRate[i]));
								tvdrdpo.setDiscountSPrice(Double.parseDouble(discountSPrice[i]));
								tvdrdpo.setDiscountPrice(Double.parseDouble(discountPrice[i]));
								dao.insert(tvdrdpo); // 插入发运申请明细表
		
								tsodpContion.setDetailId(Long.parseLong(detailId[i]));
								tsodpValue.setCallAmount(Integer.parseInt(callAmount[i]) + Integer.parseInt(applyAmount[i]));
								tsodpValue.setUpdateBy(userId);
								tsodpValue.setUpdateDate(new Date());
								dao.update(tsodpContion, tsodpValue);// 更新订单明细表
							}
						}
		
						// if ("0".equals(isCheck)) {
							// 冻结资金
							if (!accountId.equals("")) {
								dmsFreezePrice_Report(tvdrpo.getReqId().toString(), accountId, new BigDecimal(tvdrpo.getReqTotalPrice()), logonUser.getUserId().toString());
							}
							if (!discountAccountId.equals("")) {
								dmsFreezePrice_Report(tvdrpo.getReqId().toString(),discountAccountId, new BigDecimal(tvdrpo.getDiscount()), logonUser.getUserId().toString());
							}
							/*// 冻结资金
							if (!accountId.equals("")) {
								accoutDao.syncAccountFreeze(tvdrpo.getReqId().toString(), accountId, new BigDecimal(tvdrpo.getReqTotalPrice()), logonUser.getUserId().toString());
							}
							if (!discountAccountId.equals("")) {
								accoutDao.syncAccountFreeze(tvdrpo.getReqId().toString(),discountAccountId, new BigDecimal(tvdrpo.getDiscount()), logonUser.getUserId().toString());
							}*/
						// }
					}
				}
			}
			act.setOutData("returnValue", returnValue);
		} catch(RuntimeException e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.PUTIN_FAILURE_CODE,e.getMessage());
			logger.error(logonUser,e1);
			act.setException(e);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"月度常规订单启票");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/*---------------------------------------------------   by fengalon   ----------------------------------------------
	------------------------------------------------------------------------------------------   2011/01/06   --------*/
	/** 获取传回的所有订单明细中超量的物料编码
	*@param detailId
	*	订单明细id数组
	*@param amount
	*	声请数量数组
	*@return java.lang.String
	*	返回所有的超量物料编码
	*/
	public String getMatCode(String[] detailId, String[] amount) {
		StringBuffer strBuf = new StringBuffer("") ;
		int dtlLen = detailId.length ;

		for(int i=0; i<dtlLen; i++) {
			if (Integer.parseInt(amount[i]) > 0) {
				int stbLen = strBuf.length() ;
	
				if(stbLen > 0) {
					strBuf.append(",").append(chkAmount(detailId[i], amount[i])) ;
				} else {
					strBuf.append(chkAmount(detailId[i], amount[i])) ;
				}
			}
		}

		return strBuf.toString() ;
	}
	
	/** 判断常规订单启票数量是否大于审核数量，若大于则返回该订单明细超量的物料代码
	*@param detailId
	*	订单明细id
	*@param amount
	*	界面上传回的申请启票数量
	*@return java.lang.String
	*	若存在超量则返回物料代码，反正则返回"" 
	*/
	private String chkAmount(String detailId, String amount) {
		// 保存常规订单中超量的物料代码
		String str = "" ;	

		TtVsOrderDetailPO tvod = new TtVsOrderDetailPO() ;
		tvod.setDetailId(Long.parseLong(detailId)) ;

		// 通过订单查询该订单的所有明细
		List<TtVsOrderDetailPO> orderDtlList = dao.select(tvod) ;

		if(!CommonUtils.isNullList(orderDtlList)) {
			Integer chkAmount = orderDtlList.get(0).getCheckAmount() ;
			Integer callAmount = orderDtlList.get(0).getCallAmount() ;

			// 若审核数量小于启票数量则执行以下代码
			if(chkAmount < callAmount + Integer.parseInt(amount)) {			
				str = OrderDeliveryDao.getMaterialCode(Long.parseLong(detailId)) ;
			}
		}

		return str.toString() ;
	}
}
