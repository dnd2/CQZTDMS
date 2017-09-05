package com.infodms.dms.bean;

import java.util.Date;

public class PromotionplanBean {
	
	private String planEndDate;
	private Date updateDate;
	private Long createBy;
	private Long accountingUnit;
	private Long accountingAccount;
	private Long orgId;
	private Date createDate;
	private String productDesc;
	private String orgCode;
	private String orgName;
	private String planStartDate;
	private Long brandId;
	private Long promotionApplyId;
	private Long paymentType;
	private String brandName;
	private String planName;
	private Integer status;
	private Long updateBy;
	private String planCode;
	private String businessType;
	private String discountDesc;
	private Long promotionId;
	private Long confirmCount;
	private String planDateArea;
	private String flag;//等于零没有处于中间状态的 不等于零则有处于中间状态的 此状态将驳回认为是中间状态  所以判断是否为中间状态需要结合flagStatus 的值进行判读
	private String flagStatus;//为0 则不是驳回状态  不为零则是驳回状态
	private Long confirmQuantity;//认定金额
	private Long confirmAmount;//认定数量
	
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public Long getCreateBy() {
		return createBy;
	}
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getProductDesc() {
		return productDesc;
	}
	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
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
	public Long getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
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
	public String getPlanDateArea() {
		return planDateArea;
	}
	public void setPlanDateArea(String planDateArea) {
		this.planDateArea = planDateArea;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public Long getConfirmCount() {
		return confirmCount;
	}
	public void setConfirmCount(Long confirmCount) {
		this.confirmCount = confirmCount;
	}
	public String getFlagStatus() {
		return flagStatus;
	}
	public void setFlagStatus(String flagStatus) {
		this.flagStatus = flagStatus;
	}
	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public Long getConfirmQuantity() {
		return confirmQuantity;
	}
	public void setConfirmQuantity(Long confirmQuantity) {
		this.confirmQuantity = confirmQuantity;
	}
	public Long getConfirmAmount() {
		return confirmAmount;
	}
	public void setConfirmAmount(Long confirmAmount) {
		this.confirmAmount = confirmAmount;
	}
	public Long getPromotionApplyId() {
		return promotionApplyId;
	}
	public void setPromotionApplyId(Long promotionApplyId) {
		this.promotionApplyId = promotionApplyId;
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
	public Long getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(Long paymentType) {
		this.paymentType = paymentType;
	}
	public String getPlanEndDate() {
		return planEndDate;
	}
	public void setPlanEndDate(String planEndDate) {
		this.planEndDate = planEndDate;
	}
	public String getPlanStartDate() {
		return planStartDate;
	}
	public void setPlanStartDate(String planStartDate) {
		this.planStartDate = planStartDate;
	}
	
	
}
