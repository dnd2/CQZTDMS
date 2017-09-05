/**********************************************************************
 * <pre>
 * FILE : TmPromotionplanbBean.java
 * CLASS : TmPromotionplanbBean
 *
 * AUTHOR : zzg
 *
 * FUNCTION : TODO
 *
 *
 *======================================================================
 * CHANGE HISTORY LOG
 *----------------------------------------------------------------------
 * MOD. NO.|   DATE   |   NAME  | REASON  | CHANGE REQ.
 *----------------------------------------------------------------------
 * 		    |2010-1-22|zzg| Created |
 * DESCRIPTION:
 * </pre>
 ***********************************************************************/

package com.infodms.dms.bean;
public class TmPromotionplanbBean {
	private String planEndDate;
	private Long orgId;
	private Long applyQuantity;
	private Long sumConfirmAmount;
	private Long countNum;
	private Long promotionApplyId;
	private String productDesc;
	private String planStartDate;
	private Long brandId;
	private String planName;
	private Integer status;
	private String planCode;
	private String discountDesc;
	private String brandName;
	private String orgName;
	private String approvalCode;
	private String orgCode;
	private Long promotionId;
	private Long accountingUnit;
	private Long accountingAccount;
	private String businessType;
	private Integer paymentType;
	private Integer confirmQuantity;
	private Integer confirmAmount;
	public String getPlanEndDate() {
		return planEndDate;
	}
	public void setPlanEndDate(String planEndDate) {
		this.planEndDate = planEndDate;
	}
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public String getProductDesc() {
		return productDesc;
	}
	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}
	public String getPlanStartDate() {
		return planStartDate;
	}
	public void setPlanStartDate(String planStartDate) {
		this.planStartDate = planStartDate;
	}
	public Long getBrandId() {
		return brandId;
	}
	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}
	public String getPlanName() {
		return planName;
	}
	public void setPlanName(String planName) {
		this.planName = planName;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getPlanCode() {
		return planCode;
	}
	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}
	public String getDiscountDesc() {
		return discountDesc;
	}
	public void setDiscountDesc(String discountDesc) {
		this.discountDesc = discountDesc;
	}
	public Long getPromotionId() {
		return promotionId;
	}
	public void setPromotionId(Long promotionId) {
		this.promotionId = promotionId;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public Long getApplyQuantity() {
		return applyQuantity;
	}
	public void setApplyQuantity(Long applyQuantity) {
		this.applyQuantity = applyQuantity;
	}
	public Long getPromotionApplyId() {
		return promotionApplyId;
	}
	public void setPromotionApplyId(Long promotionApplyId) {
		this.promotionApplyId = promotionApplyId;
	}
	public Integer getConfirmQuantity() {
		return confirmQuantity;
	}
	public void setConfirmQuantity(Integer confirmQuantity) {
		this.confirmQuantity = confirmQuantity;
	}
	public Integer getConfirmAmount() {
		return confirmAmount;
	}
	public void setConfirmAmount(Integer confirmAmount) {
		this.confirmAmount = confirmAmount;
	}
	public String getApprovalCode() {
		return approvalCode;
	}
	public void setApprovalCode(String approvalCode) {
		this.approvalCode = approvalCode;
	}
	public Long getSumConfirmAmount() {
		return sumConfirmAmount;
	}
	public void setSumConfirmAmount(Long sumConfirmAmount) {
		this.sumConfirmAmount = sumConfirmAmount;
	}
	public Long getCountNum() {
		return countNum;
	}
	public void setCountNum(Long countNum) {
		this.countNum = countNum;
	}
	public Integer getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(Integer paymentType) {
		this.paymentType = paymentType;
	}
	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	public Long getAccountingUnit() {
		return accountingUnit;
	}
	public void setAccountingUnit(Long accountingUnit) {
		this.accountingUnit = accountingUnit;
	}
	public Long getAccountingAccount() {
		return accountingAccount;
	}
	public void setAccountingAccount(Long accountingAccount) {
		this.accountingAccount = accountingAccount;
	}
}