package com.infodms.dms.actions.sales.financemanage;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.dao.relation.DealerRelationDAO;
import com.infodms.dms.dao.sales.dealer.DealerInfoDao;
import com.infodms.dms.dao.sales.financemanage.AccountBalanceDetailDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.OrderReportDao;
import com.infodms.dms.po.TtVsAccountFreezePO;
import com.infodms.dms.util.CommonUtils;

import static com.infodms.dms.dao.sales.financemanage.AccountBalanceDetailDao.* ;

public class AccountOpera{
	private Logger logger = Logger.getLogger(AccountOpera.class);

	/** 根据上级经销商账户获得下级对应下级经销商账户
	 * @param dlrId 下级经销商id
	 * @param accountId 上级经销商账户id
	 * @return
	 */
	private String getAccountId(String dlrId, String accountId) {
		return getAccountIdMap(dlrId, accountId) ;
	}
	
	/** 获取冻结金额
	 * @param reqId 发运申请id
	 * @param accountId 对应发运申请的用户账户id
	 * @return
	 */
	private BigDecimal getFreezeAmount(String reqId, String accountId) {
		String fAmount = "0" ;
		
		Map<String, Object> amountMap = getFreezeAmountByReqId(reqId, accountId, Constant.ACCOUNT_FREEZE_STATUS_01.toString()) ;
		
		if(amountMap != null) {
			fAmount = amountMap.get("FREEZE_AMOUNT").toString() ;
		}
		
		return new BigDecimal(fAmount) ;
	}
	
	/** 获取<strong>兵财融资</strong>的账面可用余额
	 * @param accountId 兵财融资经销商账户id
	 * @return
	 */
	private BigDecimal getAvaAmount_CSGC(String accountId) {
		BigDecimal availableAmount = new BigDecimal("0") ;
		
		Map<String, Object> infoMap = getAccountInfo(accountId) ;
		
		if(infoMap != null) {
			String dealerId = infoMap.get("DEALER_ID").toString() ;
			String funTypeId = infoMap.get("ACCOUNT_TYPE_ID").toString() ;
			OrderReportDao ord = OrderReportDao.getInstance();
			Map<String, Object> map = ord.getBinCaiAvailable(dealerId, Long.parseLong(funTypeId)) ;
			availableAmount = new BigDecimal(map.get("AVAILABLE_AMOUNT").toString()) ;
		}
		
		return availableAmount ;
	}
	
	/** 获取账户的账面可用余额
	 * @param accountId 对应账户id
	 * @param isBilling 是否开票单位
	 * @param isSame 是否开票单位和采购单位为同一单位
	 * @return
	 */
	private BigDecimal getAvaAmount(String accountId, boolean isBilling, boolean isSame) {
		String typeClass = getTypeClass(accountId) ;
		
		String availableAmount = getAvailableAmount(accountId) ;

		BigDecimal avAamount = new BigDecimal(availableAmount) ;
		
		if(Constant.ACCOUNT_CLASS_FINANCE.toString().equals(typeClass) && isBilling && isSame) {
			avAamount = avAamount.subtract(new BigDecimal(getAllLowAccount(accountId))) ;
		}
		
		if(Constant.ACCOUNT_CLASS_CSGC.toString().equals(typeClass)) {
			avAamount = getAvaAmount_CSGC(accountId) ;
		}
		
		return avAamount ;
	}
	
	/**	获取采购单位账户可用余额
	 * @param accountId 对应账户id
	 * @return
	 */
	private BigDecimal getAvaAmount_CG(String accountId) {
		return getAvaAmount(accountId, false, false) ;
	}
	
	/** 获取开票单位账户可用余额
	 * @param accountId 对应账户id
	 * @param isSame 是否开票单位和采购单位为同一单位
	 * @return
	 */
	private BigDecimal getAvaAmount_KP(String accountId, boolean isSame) {
		return getAvaAmount(accountId, true, isSame) ;
	}
	
	private void accountCheck(BigDecimal availableAmount, BigDecimal totalPrice, boolean isBilling) throws RuntimeException {
		String excStr = "资金校验错误！" ;
		
		if(isBilling) {
			excStr = "开票单位账户可用余额不足！" ;
		} else {
			excStr = "采购单位账户可用余额不足！" ;
		}

		if(availableAmount.compareTo(new BigDecimal("0")) == -1 || availableAmount.subtract(totalPrice).compareTo(new BigDecimal("0")) == -1) {
			throw new RuntimeException(excStr) ;
		}
	}
	
	/** 采购单位资金校验
	 * @param availableAmount 可用余额
	 * @param totalPrice 冻结总金额
	 */
	private void accountCheck_CG(BigDecimal availableAmount, BigDecimal totalPrice) {
		accountCheck(availableAmount, totalPrice, false) ;
	}
	
	/** 开票单位资金校验
	 * @param availableAmount 可用余额
	 * @param totalPrice 冻结总金额
	 */
	private void accountCheck_KP(BigDecimal availableAmount, BigDecimal totalPrice) {
		accountCheck(availableAmount, totalPrice, true) ;
	}
	
	/** 根据发运申请获取对应的采购单位
	 * @param reqId 发运申请id
	 * @return
	 */
	private String getDlr_CG(String reqId) {
		String dlr = "-1" ;
		
		Map<String, Object> orderMap = getOrderDlr(reqId) ;
		
		if(orderMap != null) {
			dlr = orderMap.get("DLR_CG").toString() ;
		} 
		
		if("-1".equals(dlr)) {
			throw new RuntimeException("该发运申请不存在采购单位！") ;
		}
		
		return dlr ;
	}
	/** 根据发运申请判断采购单位与开票单位是否同一单位，按照<strong>erp_code</strong>判断；若相同返回true，若不同则返回false
	 * @param reqId 发运申请id
	 * @return
	 * @throws RuntimeException
	 */
	private boolean isSame(String reqId) throws RuntimeException {
		Map<String, Object> orderMap = getOrderDlr(reqId) ;

		String billing = "-1" ;
		String ord = "-2" ;

		if(orderMap != null) {
			billing = orderMap.get("ERP_CODE").toString() ;
			ord = orderMap.get("ER_CODE").toString() ;
		} 
		
		if("-1".equals(billing)) {
			throw new RuntimeException("开票单位erp_code未维护！") ;
		}
		
		if("-2".equals(ord)) {
			throw new RuntimeException("采购单位erp_code未维护！") ;
		}

		return billing.equals(ord) ;
	}
	
	/** 获得对应账户的资金类型类
	 * @param accountId 账户id
	 * @return
	 * @throws RuntimeException
	 */
	private String getTypeClass(String accountId) throws RuntimeException {
		String typeClass = getAccountTypeClass(accountId) ;

		if("-1".equals(typeClass)) {
			throw new RuntimeException("账户类型不存在或账户类型类不存在！") ;
		}

		return typeClass ;
	}
	
	public String getFundClass(String accountId) {
		return this.getTypeClass(accountId) ;
	}
	
	/** 冻结资金或释放资金对资金的操作
	 * @param reqId 发运申请id
	 * @param accountId_kp 开票单位账户id
	 * @param totalPrice 冻结总金额
	 * @param userId 操作用户id
	 * @param isAllPay 是否释放
	 * @param isReport 是否提报操作
	 * @throws RuntimeException
	 */
	private void accountOpera(String reqId, String accountId_kp, BigDecimal totalPrice, String userId, boolean isAllPay, boolean isReport) throws RuntimeException {
		// 获取对应开票账户的资金类型类
		String typeClass = getTypeClass(accountId_kp) ;

		// 判断采购单位与开票单位是否为同一单位
		boolean isSame = isSame(reqId) ;
		
		// 资金类型类为三方信贷&兵财融资并且开票单位与采购单位不一致，则需要对采购单位进行资金操作
		if((Constant.ACCOUNT_CLASS_FINANCE.toString().equals(typeClass) || Constant.ACCOUNT_CLASS_CSGC.toString().equals(typeClass)) && !isSame) {
			BigDecimal orderTotalPrice = totalPrice ;
			// 获取采购单位id
			String dlr_cg = getDlr_CG(reqId) ;
			
			String dlrType = this.getDlrType(dlr_cg) ;
			
			/*// 结算中心采购单位若结算中心直属下级,则取其对应直属下级,限制仅为三方信贷款
			if(Constant.DEALER_TYPE_JSZX == Integer.parseInt(dlrType) && Constant.ACCOUNT_CLASS_FINANCE.toString().equals(typeClass)) {
				DealerRelationDAO dr = DealerRelationDAO.getInstance() ;
				
				dlr_cg = dr.getLevelDlr(dlr_cg, "2").get("DEALER_ID").toString() ;
				
				if(!isAllPay){
					orderTotalPrice = new BigDecimal(this.getTotalPrice(dlr_cg, reqId)) ;
				}
			}*/
			
			// 获取采购单位账户id
			String accountId_cg = getAccountId(dlr_cg, accountId_kp) ;
			
			// 若采购单位账户不存在则抛出异常
			if("-1".equals(accountId_cg)) {
				throw new RuntimeException("采购单位账户不存在！") ;
			}
			
			String erp_cg = getOrderDlr(reqId).get("ER_CODE").toString() ;
			if(!("-" + Constant.STATUS_DISABLE).equals(erp_cg)){
				// 若为释放操作，则不进行资金校验；若为冻结操作，则需要进行资金校验
				if(!isAllPay){
					// 资金校验开关需要提报时验证&是提报则执行以下校验操作
					if(isReport) {
						// 获得采购单位账面余额
						BigDecimal ava_cg = getAvaAmount_CG(accountId_cg) ;
						
						// 资金校验
						accountCheck_CG(ava_cg, orderTotalPrice) ;
					} else { // 非提报则执行以下校验操作
						// 获得采购单位余额
						BigDecimal ava_cg = getAvaAmount_CG(accountId_cg).add(getFreezeAmount(reqId, accountId_cg)) ;
						
						accountCheck_CG(ava_cg, orderTotalPrice) ;
					}
				}
			}
			
			// 资金操作
			syncAccountOpera(reqId, accountId_cg, orderTotalPrice, userId, isAllPay) ;
			
			// 若资金类型类为兵财融资，则跳出方法
			if(Constant.ACCOUNT_CLASS_CSGC.toString().equals(typeClass)) {
				return ;
			}
		} 
		
		// 以下是对开票单位账户进行操作
		if(null == accountId_kp || "".equals(accountId_kp)) {
			throw new RuntimeException("开票单位账户不存在！") ;
		}
		
		String erp_kp = getOrderDlr(reqId).get("ERP_CODE").toString() ;
		if(!("-" + Constant.STATUS_DISABLE).equals(erp_kp)){
			if(!isAllPay){
				if(isReport) {
					BigDecimal ava_kp = getAvaAmount_KP(accountId_kp, isSame) ;
			
					accountCheck_KP(ava_kp, totalPrice) ;
				} else {
					BigDecimal ava_kp = getAvaAmount_KP(accountId_kp, isSame).add(getFreezeAmount(reqId, accountId_kp)) ;
					
					accountCheck_KP(ava_kp, totalPrice) ;
				}
			}
		}

		syncAccountOpera(reqId, accountId_kp, totalPrice, userId, isAllPay) ;  
	}
	
	/** 过度方法
	 * @param reqId
	 * @param accountId_kp
	 * @param totalPrice
	 * @param userId
	 * @param isAllPay
	 * @param isReport
	 * @throws RuntimeException
	 */
	private static void dmsAccountOpera(String reqId, String accountId_kp, BigDecimal totalPrice, String userId, boolean isAllPay, boolean isReport) throws RuntimeException {
		AccountOpera ao = new AccountOpera() ;
		
		ao.accountOpera(reqId, accountId_kp, totalPrice, userId, isAllPay, isReport) ;
	}
	
	/***************************************************           action调用操作资金方法                      ***************************************************
	 **********************************************************************************************************************************************/
	
	/** 释放对应发运申请的<strong>所有</strong>冻结资金
	 * @param reqId 发运申请id
	 * @param userId 操作人员用户id
	 */
	public static void dmsReleasePrice(String reqId, String userId) {
		AccountBalanceDetailDao abd = AccountBalanceDetailDao.getInstance();
		
		List<TtVsAccountFreezePO> freezeRecords = abd.getFreezedRecordsByReqId(reqId);
		
		if(freezeRecords != null){
			for(Iterator<TtVsAccountFreezePO> itor = freezeRecords.iterator();itor.hasNext();){
				TtVsAccountFreezePO tmpPo = (TtVsAccountFreezePO) itor.next();
				dmsAccountOpera(reqId,tmpPo.getAccountId().toString(),new BigDecimal(0),userId,true, false);
			}
		} 
	}
	
	/** <strong>非提报</strong>时资金冻结
	 * @param reqId 发运申请id
	 * @param accountId_kp 开票单位需要冻结资金账户
	 * @param totalPrice 冻结总费用
	 * @param userId 操作人员用户id
	 */
	public static void dmsFreezePrice(String reqId, String accountId_kp, BigDecimal totalPrice, String userId) {
		dmsAccountOpera(reqId, accountId_kp, totalPrice, userId, false, false) ;   
	}
	
	/**  <strong>提报</strong>时资金冻结
	 * @param reqId 发运申请id
	 * @param accountId_kp 开票单位需要冻结资金账户
	 * @param totalPrice 冻结总费用
	 * @param userId 操作人员用户id
	 */
	public static void dmsFreezePrice_Report(String reqId, String accountId_kp, BigDecimal totalPrice, String userId) {
		// 获取资金校验开关：0表示提报时需要校验
		String para = CommonDAO.getPara(Constant.URGENT_ORDER_ACCOUNT_CHECK_PARA.toString()) ;
		
		if("0".equals(para)) {
			dmsAccountOpera(reqId, accountId_kp, totalPrice, userId, false, true) ;
		}
	}
	
	public static boolean chkAdmiral(String dealerId) {
		String dlrClass = "" ;
		
		DealerInfoDao did = DealerInfoDao.getInstance();
		
		Map<String, Object> map = did.getDlrClass(dealerId) ;
		
		if(CommonUtils.isNullMap(map)) {
			throw new RuntimeException("对应经销商不存在！") ;
		} else {
			dlrClass = map.get("DEALER_CLASS").toString() ;
		}
		
		if(Constant.DEALER_CLASS_TYPE_12.toString().equals(dlrClass)) {
			return true ;
		} else {
			return false ;
		}
	}
	
	public String getDlrType(String dlrId) {
		DealerRelationDAO dr = DealerRelationDAO.getInstance() ;
		
		return dr.getFirstDlr(dlrId).get("DEALER_TYPE").toString() ;
	}
	
	public String getDlrPrice(String dlrId) {
		AccountBalanceDetailDao abd = AccountBalanceDetailDao.getInstance();
		
		Map<String, Object> map = abd.getDlrPrice(dlrId) ;
		
		if(null == map) {
			throw new RuntimeException("采购单位经销商价格列表未维护!") ;
		}
		
		return abd.getDlrPrice(dlrId).get("PRICE_ID").toString() ;
	}
	
	public String getTotalPrice(String dlrId, String reqId) {
		double totalPrice = 0;
		
		String priceId = this.getDlrPrice(dlrId) ;
		
		AccountBalanceDetailDao abd = AccountBalanceDetailDao.getInstance();
		
		List<Map<String, Object>> list = abd.getTotalPrice(priceId, reqId) ;
		
		if(list != null) {
			int length = list.size() ;
			
			for(int i=0; i<length; i++) {
				double price = Double.parseDouble(list.get(i).get("PRICE").toString()) ;
				
				if(price < 0) {
					throw new RuntimeException("采购单位经销商价格列表中对应配置价格未维护!") ;
				} else {
					totalPrice += price ;
				}
			}
		}
		
		return totalPrice + "" ;
	}
}
