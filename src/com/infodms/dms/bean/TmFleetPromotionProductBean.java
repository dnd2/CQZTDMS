/**********************************************************************
 * <pre>
 * FILE : TmFleetPromotionProductBean.java
 * CLASS : TmFleetPromotionProductBean
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
 * 		    |2010-1-5|zzg| Created |
 * DESCRIPTION:
 * </pre>
 ***********************************************************************/
package com.infodms.dms.bean;

import java.util.Date;

public class TmFleetPromotionProductBean {
	
	
	private Long promotionProductId;
	private Long promotionId;
	private Long promotionApplyId;
	private Long vehicleId;//车辆id
	private Long orderId;
	private Long productId;//车型id
	private String productCode;
	private String brandName;
	private String modelName;
	private Integer confirmQuantity;
	private Integer confirmAmount;
	private Integer applyAmount;
	private Integer contractPrice;
	private Integer guidePrice;
	private Integer reportAmount;
	private Integer normalPrice;//订单价格
	private Double preferentialPoint;
	private Integer preferentialAmount;//折让金额出现小数 则四舍五入
	private Integer orderAmount;
	private String orderNo;
	private String applyTypeName;
	private String modelCode;
	private Integer channel;//沟通渠道 Z:正票，Y1固定额度，Y3:临时额度，Y2:促销额度
	private String vin;
	private String invoiceNo;//发票号
	private String paymentType;//发票类型
	private String statusinvdate;//发票日期
	private String 	customeCode;//客户代码
	private String 	flagUsed;//是否已经被返利审批和变动促销申报过
	private Integer flag;//是否为批售支持订单中
	private Integer logType;//审批状态
	private String orgName;//审批部门
	private String auditlog;//审批意见
	private String userName;//用户名
	private String approveDate;//审批日期
	private Long isConfirm;//是否确认
	private Long preferentialApplyDetailId;//变动促销明细id
	
	public Long getPromotionProductId() {
		return promotionProductId;
	}
	public void setPromotionProductId(Long promotionProductId) {
		this.promotionProductId = promotionProductId;
	}
	public Long getPromotionId() {
		return promotionId;
	}
	public void setPromotionId(Long promotionId) {
		this.promotionId = promotionId;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public Integer getApplyAmount() {
		return applyAmount;
	}
	public void setApplyAmount(Integer applyAmount) {
		this.applyAmount = applyAmount;
	}
	public Integer getContractPrice() {
		return contractPrice;
	}
	public void setContractPrice(Integer contractPrice) {
		this.contractPrice = contractPrice;
	}
	public Integer getGuidePrice() {
		return guidePrice;
	}
	public void setGuidePrice(Integer guidePrice) {
		this.guidePrice = guidePrice;
	}
	public Integer getNormalPrice() {
		return normalPrice;
	}
	public void setNormalPrice(Integer normalPrice) {
		this.normalPrice = normalPrice;
	}
	public Integer getPreferentialAmount() {
		return preferentialAmount;
	}
	public void setPreferentialAmount(Integer preferentialAmount) {
		this.preferentialAmount = preferentialAmount;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public Integer getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(Integer orderAmount) {
		this.orderAmount = orderAmount;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public Double getPreferentialPoint() {
		return preferentialPoint;
	}
	public void setPreferentialPoint(Double preferentialPoint) {
		this.preferentialPoint = preferentialPoint;
	}
	public Integer getReportAmount() {
		return reportAmount;
	}
	public void setReportAmount(Integer reportAmount) {
		this.reportAmount = reportAmount;
	}
	public String getApplyTypeName() {
		return applyTypeName;
	}
	public void setApplyTypeName(String applyTypeName) {
		this.applyTypeName = applyTypeName;
	}
	public Integer getChannel() {
		return channel;
	}
	public void setChannel(Integer channel) {
		this.channel = channel;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getStatusinvdate() {
		return statusinvdate;
	}
	public void setStatusinvdate(String statusinvdate) {
		this.statusinvdate = statusinvdate;
	}
	public Long getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(Long vehicleId) {
		this.vehicleId = vehicleId;
	}
	public Integer getFlag() {
		return flag;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	public String getCustomeCode() {
		return customeCode;
	}
	public void setCustomeCode(String customeCode) {
		this.customeCode = customeCode;
	}
	public String getFlagUsed() {
		return flagUsed;
	}
	public void setFlagUsed(String flagUsed) {
		this.flagUsed = flagUsed;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public Integer getLogType() {
		return logType;
	}
	public void setLogType(Integer logType) {
		this.logType = logType;
	}
	public String getAuditlog() {
		return auditlog;
	}
	public void setAuditlog(String auditlog) {
		this.auditlog = auditlog;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getApproveDate() {
		return approveDate;
	}
	public void setApproveDate(String approveDate) {
		this.approveDate = approveDate;
	}
	public String getModelCode() {
		return modelCode;
	}
	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}
	public Long getPromotionApplyId() {
		return promotionApplyId;
	}
	public void setPromotionApplyId(Long promotionApplyId) {
		this.promotionApplyId = promotionApplyId;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public Long getIsConfirm() {
		return isConfirm;
	}
	public void setIsConfirm(Long isConfirm) {
		this.isConfirm = isConfirm;
	}
	public Long getPreferentialApplyDetailId() {
		return preferentialApplyDetailId;
	}
	public void setPreferentialApplyDetailId(Long preferentialApplyDetailId) {
		this.preferentialApplyDetailId = preferentialApplyDetailId;
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
}
