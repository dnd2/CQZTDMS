package com.infodms.dms.bean;

import java.util.Date;


public class TmExpensesBean {

	private Long expensesId;
	private String brandName;
	private String orgCode;
	private String orgName;
	private String planCode;
	private String planName;
	private Integer businessType;
	private Integer paymentType;
	private Double payAmount;
	private String remark;
	private Integer status;
	private String statusStr;
	private Long userId;
	private Long orgId;
	private String paymentTypeCode;
	private Integer accountingUnits;
	private Integer accountingSubjects;
	
	private String mainBrandName;
	private Long brandId;
	private String approvalCode;
	private Date createDate;
	private Long chargeId;
	private String mainPlanCode;
	
	private String chargeIds;
	
	//页面传递过来的审核日期字段
	private String approveDateStart;
	private String approveDateEnd;
	//审批时间
	private Date signDate;
	//发票号[多组]
	private String invoiceNos;
	//发票号
	private String invoiceNo;
	private Date invoiceDate;
	//发票总数
	private int invoiceCount;
	//方案总数
	private int planCount;

	private String printTitle;
	
	private String accountBank;
	private String accountNo;
	
	//批售申报数量
	private Integer applyAmount;
	//批售申报优惠点数
	private Double preferentialPoint;
	//批售申报优惠金额
	private Integer preferentialAmount;
	private String modelCode;
	private String modelName;
	private String customerCode;
	private String orderNo;
	private Integer guidePrice;
	//销售渠道(正票)
	private Integer channel;
	//订单类型
	private Long svoId;
	
	public Long getSvoId() {
		return svoId;
	}
	public void setSvoId(Long svoId) {
		this.svoId = svoId;
	}
	public Integer getChannel() {
		return channel;
	}
	public void setChannel(Integer channel) {
		this.channel = channel;
	}
	public Integer getGuidePrice() {
		return guidePrice;
	}
	public void setGuidePrice(Integer guidePrice) {
		this.guidePrice = guidePrice;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getModelCode() {
		return modelCode;
	}
	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public String getCustomerCode() {
		return customerCode;
	}
	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}
	public String getChargeIds() {
		return chargeIds;
	}
	public void setChargeIds(String chargeIds) {
		this.chargeIds = chargeIds;
	}
	public Integer getApplyAmount() {
		return applyAmount;
	}
	public void setApplyAmount(Integer applyAmount) {
		this.applyAmount = applyAmount;
	}
	public Double getPreferentialPoint() {
		return preferentialPoint;
	}
	public void setPreferentialPoint(Double preferentialPoint) {
		this.preferentialPoint = preferentialPoint;
	}
	public Integer getPreferentialAmount() {
		return preferentialAmount;
	}
	public void setPreferentialAmount(Integer preferentialAmount) {
		this.preferentialAmount = preferentialAmount;
	}
	public int getPlanCount() {
		return planCount;
	}
	public void setPlanCount(int planCount) {
		this.planCount = planCount;
	}
	public int getInvoiceCount() {
		return invoiceCount;
	}
	public void setInvoiceCount(int invoiceCount) {
		this.invoiceCount = invoiceCount;
	}
	public String getStatusStr() {
		return statusStr;
	}
	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}
	public String getAccountBank() {
		return accountBank;
	}
	public void setAccountBank(String accountBank) {
		this.accountBank = accountBank;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getPrintTitle() {
		return printTitle;
	}
	public void setPrintTitle(String printTitle) {
		this.printTitle = printTitle;
	}
	public Date getSignDate() {
		return signDate;
	}
	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}
	public String getMainPlanCode() {
		return mainPlanCode;
	}
	public void setMainPlanCode(String mainPlanCode) {
		this.mainPlanCode = mainPlanCode;
	}
	public Date getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public Integer getAccountingUnits() {
		return accountingUnits;
	}
	public void setAccountingUnits(Integer accountingUnits) {
		this.accountingUnits = accountingUnits;
	}
	public Integer getAccountingSubjects() {
		return accountingSubjects;
	}
	public void setAccountingSubjects(Integer accountingSubjects) {
		this.accountingSubjects = accountingSubjects;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public Long getChargeId() {
		return chargeId;
	}
	public void setChargeId(Long chargeId) {
		this.chargeId = chargeId;
	}
	public String getInvoiceNos() {
		return invoiceNos;
	}
	public void setInvoiceNos(String invoiceNos) {
		this.invoiceNos = invoiceNos;
	}
	public String getApproveDateStart() {
		return approveDateStart;
	}
	public void setApproveDateStart(String approveDateStart) {
		this.approveDateStart = approveDateStart;
	}
	public String getApproveDateEnd() {
		return approveDateEnd;
	}
	public void setApproveDateEnd(String approveDateEnd) {
		this.approveDateEnd = approveDateEnd;
	}
	public Long getBrandId() {
		return brandId;
	}
	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}
	public String getMainBrandName() {
		return mainBrandName;
	}
	public void setMainBrandName(String mainBrandName) {
		this.mainBrandName = mainBrandName;
	}
	public String getApprovalCode() {
		return approvalCode;
	}
	public void setApprovalCode(String approvalCode) {
		this.approvalCode = approvalCode;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getPlanName() {
		return planName;
	}
	public void setPlanName(String planName) {
		this.planName = planName;
	}
	public String getPaymentTypeCode() {
		return paymentTypeCode;
	}
	public void setPaymentTypeCode(String paymentTypeCode) {
		this.paymentTypeCode = paymentTypeCode;
	}
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getExpensesId() {
		return expensesId;
	}
	public void setExpensesId(Long expensesId) {
		this.expensesId = expensesId;
	}
	public Integer getBusinessType() {
		return businessType;
	}
	public void setBusinessType(Integer businessType) {
		this.businessType = businessType;
	}
	public String getPlanCode() {
		return planCode;
	}
	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
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
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(Integer paymentType) {
		this.paymentType = paymentType;
	}
	public Double getPayAmount() {
		return payAmount;
	}
	public void setPayAmount(Double payAmount) {
		this.payAmount = payAmount;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
