package com.infodms.dms.bean;

import java.util.Date;

import com.infoservice.po3.bean.PO;

/**
 * @author PGM 索赔预授权---索赔预授权审核作业--[审核]查询
 */
public class PreclaimAuditBean extends PO {
	private static final long serialVersionUID = -2302347681373581030L;
    private String id;
    private String dealerCode;
    private String dealerShortname;
    private String roNo;
    private String startTime;
    private String inFactoryDate;
    private String inMileage;
    private String destClerk;
    private String vin;
    private String licenseNo;
    private String engineNo;
    private String brandGroupName;
    private String serisesGroupName;
    private String modelGroupName;
    private String deliverer;
    private String approvalDate;
    private String delivererPhone;
    private String itemId;
    private String itemType;
    private String itemCode;
    private String itemDesc;
    private String checkRemark;
    private String rum;
    private String authCode;
    private String status;
    private String keepBegDate;
    private String brandName; 
    private String seriesName;
    private String modelName;
    private String yieldly; //产地
    private String dealerRemark;//故障描述
    private String approvalPerson;//申请人
    private String approvalPhone;//申请人电话
    private String approvalType;
	private String outPerson;
	private String outDate;
	private String outFee;
    
    
	public String getOutPerson() {
		return outPerson;
	}
	public void setOutPerson(String outPerson) {
		this.outPerson = outPerson;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDealerCode() {
		return dealerCode;
	}
	public void setDealerCode(String dealerCode) {
		this.dealerCode = dealerCode;
	}
	public String getDealerShortname() {
		return dealerShortname;
	}
	public void setDealerShortname(String dealerShortname) {
		this.dealerShortname = dealerShortname;
	}
	public String getRoNo() {
		return roNo;
	}
	public void setRoNo(String roNo) {
		this.roNo = roNo;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getInFactoryDate() {
		return inFactoryDate;
	}
	public void setInFactoryDate(String inFactoryDate) {
		this.inFactoryDate = inFactoryDate;
	}
	public String getInMileage() {
		return inMileage;
	}
	public void setInMileage(String inMileage) {
		this.inMileage = inMileage;
	}
	public String getDestClerk() {
		return destClerk;
	}
	public void setDestClerk(String destClerk) {
		this.destClerk = destClerk;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public String getLicenseNo() {
		return licenseNo;
	}
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}
	public String getEngineNo() {
		return engineNo;
	}
	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}
	public String getBrandGroupName() {
		return brandGroupName;
	}
	public void setBrandGroupName(String brandGroupName) {
		this.brandGroupName = brandGroupName;
	}
	public String getSerisesGroupName() {
		return serisesGroupName;
	}
	public void setSerisesGroupName(String serisesGroupName) {
		this.serisesGroupName = serisesGroupName;
	}
	public String getModelGroupName() {
		return modelGroupName;
	}
	public void setModelGroupName(String modelGroupName) {
		this.modelGroupName = modelGroupName;
	}
	public String getDeliverer() {
		return deliverer;
	}
	public void setDeliverer(String deliverer) {
		this.deliverer = deliverer;
	}
	public String getApprovalDate() {
		return approvalDate;
	}
	public void setApprovalDate(String approvalDate) {
		this.approvalDate = approvalDate;
	}
	public String getDelivererPhone() {
		return delivererPhone;
	}
	public void setDelivererPhone(String delivererPhone) {
		this.delivererPhone = delivererPhone;
	}
	
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemDesc() {
		return itemDesc;
	}
	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}
	public String getCheckRemark() {
		return checkRemark;
	}
	public void setCheckRemark(String checkRemark) {
		this.checkRemark = checkRemark;
	}
	public String getRum() {
		return rum;
	}
	public void setRum(String rum) {
		this.rum = rum;
	}
	public String getAuthCode() {
		return authCode;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getKeepBegDate() {
		return keepBegDate;
	}
	public void setKeepBegDate(String keepBegDate) {
		this.keepBegDate = keepBegDate;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getSeriesName() {
		return seriesName;
	}
	public void setSeriesName(String seriesName) {
		this.seriesName = seriesName;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	/** 
	 * @return yieldly 
	 */
	public String getYieldly() {
		return yieldly;
	}
	/** 
	 * @param yieldly 要设置的 yieldly 
	 */
	public void setYieldly(String yieldly) {
		this.yieldly = yieldly;
	}
	/** 
	 * @return dealerRemark 
	 */
	public String getDealerRemark() {
		return dealerRemark;
	}
	/** 
	 * @param dealerRemark 要设置的 dealerRemark 
	 */
	public void setDealerRemark(String dealerRemark) {
		this.dealerRemark = dealerRemark;
	}
	/** 
	 * @return approvalPerson 
	 */
	public String getApprovalPerson() {
		return approvalPerson;
	}
	/** 
	 * @param approvalPerson 要设置的 approvalPerson 
	 */
	public void setApprovalPerson(String approvalPerson) {
		this.approvalPerson = approvalPerson;
	}
	/** 
	 * @return approvalPhone 
	 */
	public String getApprovalPhone() {
		return approvalPhone;
	}
	/** 
	 * @param approvalPhone 要设置的 approvalPhone 
	 */
	public void setApprovalPhone(String approvalPhone) {
		this.approvalPhone = approvalPhone;
	}
	public String getOutDate() {
		return outDate;
	}
	public void setOutDate(String outDate) {
		this.outDate = outDate;
	}
	public String getOutFee() {
		return outFee;
	}
	public void setOutFee(String outFee) {
		this.outFee = outFee;
	}
	public String getApprovalType() {
		return approvalType;
	}
	public void setApprovalType(String approvalType) {
		this.approvalType = approvalType;
	}
	
	
	
}