package com.infodms.dms.po;

import java.util.Date;

@SuppressWarnings("serial")
public class TtAsRepairOrderExtPO extends TtAsRepairOrderPO{
	private String dealerName;
	private String dealerCodes;
	private String yieldlyName;
	private Long modelId;      //车型组ID
	private Long brandId;
	private Long seriesId;
	private Long  packageId;
	private String roCreateTime;
	private String balanceTime;
	private String groupName;
	public Long getPackageId() {
		return packageId;
	}

	public void setPackageId(Long packageId) {
		this.packageId = packageId;
	}

	private String brandCode;
	private String seriesCode;
	private String modelCode;
	private String brandName;
	private String seriesName;
	private String modelName;
	private String modelNames;
	private Date purchasedDate;
	private String rearaxleNo;
	private String gearboxNo;
	private String transferNo;
	private String licenseNo;
	private String yieldly;
	private String engineNoV;
	private String fromAdress;
	private String endAdress;
	private String outLicenseno;
	private String outPerson;
	private String roNo;
	private String outSite;
	private Double outMileages;
	private Date endTime;
	private Date startTime;
	private String campaignName;
	private Integer camFix;
	private Double campaignFee;
	private Long claimTacticsId; //三包策略ID
	private Integer inClaim;
	private Date productDate;
	private String endDate;
	private String startDate;
	private String ownerPhone;
	private String ownerTel;
	private String subjectNo;
	private String gdid;
	private Double free;
	private String ctmAddress;
	private String ctmName;
	private String ctmPhone;
	private String claimDirectorTelphone; //服务主管电话
	private Double mileage;
	public String getClaimDirectorTelphone() {
		return claimDirectorTelphone;
	}

	public void setClaimDirectorTelphone(String claimDirectorTelphone) {
		this.claimDirectorTelphone = claimDirectorTelphone;
	}

	public String getCtmAddress() {
		return ctmAddress;
	}

	public void setCtmAddress(String ctmAddress) {
		this.ctmAddress = ctmAddress;
	}

	public String getCtmName() {
		return ctmName;
	}

	public void setCtmName(String ctmName) {
		this.ctmName = ctmName;
	}

	public String getCtmPhone() {
		return ctmPhone;
	}

	public void setCtmPhone(String ctmPhone) {
		this.ctmPhone = ctmPhone;
	}

	public String getGdid() {
		return gdid;
	}

	public void setGdid(String gdid) {
		this.gdid = gdid;
	}

	/*****zhumingwei 2011-6-9******************/
	private String packageName;
	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	/*****zhumingwei 2011-6-9******************/
	public Date getProductDate() {
		return productDate;
	}

	public void setProductDate(Date productDate) {
		this.productDate = productDate;
	}

	/**************************Iverson add with 2010-11-12***********************/
	private String orderValuebleType;
	
	public String getOrderValuebleType() {
		return orderValuebleType;
	}

	public void setOrderValuebleType(String orderValuebleType) {
		this.orderValuebleType = orderValuebleType;
	}

	/**************************Iverson add with 2010-11-12 end***********************/

	public String getEngineNoV() {
		return engineNoV;
	}

	public void setEngionNoV(String engineNoV) {
		this.engineNoV = engineNoV;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getSeriesCode() {
		return seriesCode;
	}

	public void setSeriesCode(String seriesCode) {
		this.seriesCode = seriesCode;
	}

	public String getModelCode() {
		return modelCode;
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
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

	public Date getPurchasedDate() {
		return purchasedDate;
	}

	public void setPurchasedDate(Date purchasedDate) {
		this.purchasedDate = purchasedDate;
	}

	public String getRearaxleNo() {
		return rearaxleNo;
	}

	public void setRearaxleNo(String rearaxleNo) {
		this.rearaxleNo = rearaxleNo;
	}

	public String getGearboxNo() {
		return gearboxNo;
	}

	public void setGearboxNo(String gearboxNo) {
		this.gearboxNo = gearboxNo;
	}

	public String getTransferNo() {
		return transferNo;
	}

	public void setTransferNo(String transferNo) {
		this.transferNo = transferNo;
	}

	public String getLicenseNo() {
		return licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	public String getYieldly() {
		return yieldly;
	}

	public void setYieldly(String yieldly) {
		this.yieldly = yieldly;
	}

	public String getDealerName() {
		return dealerName;
	}

	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}

	public void setEngineNoV(String engineNoV) {
		this.engineNoV = engineNoV;
	}

	public Long getModelId() {
		return modelId;
	}

	public void setModelId(Long modelId) {
		this.modelId = modelId;
	}

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public Long getSeriesId() {
		return seriesId;
	}

	public void setSeriesId(Long seriesId) {
		this.seriesId = seriesId;
	}

	public String getFromAdress() {
		return fromAdress;
	}

	public void setFromAdress(String fromAdress) {
		this.fromAdress = fromAdress;
	}

	public String getEndAdress() {
		return endAdress;
	}

	public void setEndAdress(String endAdress) {
		this.endAdress = endAdress;
	}

	public String getOutLicenseno() {
		return outLicenseno;
	}

	public void setOutLicenseno(String outLicenseno) {
		this.outLicenseno = outLicenseno;
	}

	public String getOutPerson() {
		return outPerson;
	}

	public void setOutPerson(String outPerson) {
		this.outPerson = outPerson;
	}

	public String getRoNo() {
		return roNo;
	}

	public void setRoNo(String roNo) {
		this.roNo = roNo;
	}

	public String getOutSite() {
		return outSite;
	}

	public void setOutSite(String outSite) {
		this.outSite = outSite;
	}

	public Double getOutMileages() {
		return outMileages;
	}

	public void setOutMileages(Double outMileages) {
		this.outMileages = outMileages;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public String getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}

	public Integer getCamFix() {
		return camFix;
	}

	public void setCamFix(Integer camFix) {
		this.camFix = camFix;
	}

	public Double getCampaignFee() {
		return campaignFee;
	}

	public void setCampaignFee(Double campaignFee) {
		this.campaignFee = campaignFee;
	}

	public Long getClaimTacticsId() {
		return claimTacticsId;
	}

	public void setClaimTacticsId(Long claimTacticsId) {
		this.claimTacticsId = claimTacticsId;
	}

	public Integer getInClaim() {
		return inClaim;
	}

	public void setInClaim(Integer inClaim) {
		this.inClaim = inClaim;
	}

	public String getYieldlyName() {
		return yieldlyName;
	}

	public void setYieldlyName(String yieldlyName) {
		this.yieldlyName = yieldlyName;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getModelNames() {
		return modelNames;
	}

	public void setModelNames(String modelNames) {
		this.modelNames = modelNames;
	}

	public String getOwnerPhone() {
		return ownerPhone;
	}

	public void setOwnerPhone(String ownerPhone) {
		this.ownerPhone = ownerPhone;
	}

	public String getOwnerTel() {
		return ownerTel;
	}

	public void setOwnerTel(String ownerTel) {
		this.ownerTel = ownerTel;
	}

	public String getDealerCodes() {
		return dealerCodes;
	}

	public void setDealerCodes(String dealerCodes) {
		this.dealerCodes = dealerCodes;
	}

	public String getSubjectNo() {
		return subjectNo;
	}

	public void setSubjectNo(String subjectNo) {
		this.subjectNo = subjectNo;
	}

	public String getRoCreateTime() {
		return roCreateTime;
	}

	public void setRoCreateTime(String roCreateTime) {
		this.roCreateTime = roCreateTime;
	}

	public String getBalanceTime() {
		return balanceTime;
	}

	public void setBalanceTime(String balanceTime) {
		this.balanceTime = balanceTime;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Double getFree() {
		return free;
	}

	public void setFree(Double free) {
		this.free = free;
	}

	public Double getMileage() {
		return mileage;
	}

	public void setMileage(Double mileage) {
		this.mileage = mileage;
	}
	
	
}
