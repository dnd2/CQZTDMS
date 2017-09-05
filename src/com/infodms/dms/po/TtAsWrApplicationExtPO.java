package com.infodms.dms.po;

import java.util.Date;

@SuppressWarnings("serial")
public class TtAsWrApplicationExtPO extends TtAsWrApplicationPO{
	
	private String dealerCode; //经销商代码
	private String dealerName; //经销商名称
	private Long modelId;      //车型组ID
	private String claimName; //索赔类型名称
	private String troubleName; //故障名称
	private String damageAreaName; //损坏区域名称
	private String damageDegreeName; //损坏程度名称
	private String damageTypeName; //损坏类型名称
	private Double labourAmounts; //工时费用合计
	private Double partAmounts; //配件费用合计
	private Double netitemAmounts; //其他费用合计
	private Double repairTotals; //索赔金额合计
	private Double taxSums; //税额合计
	private Double grossCredits; //总金额合计
	private String startClaim; //开始工单号
	private String activityName;
	private String endClaim; //结束工单号
	private String dealerShortname; //经销商简称
	private Integer counts; //申请单数
	private String ids; //
	private Date purchasedDate; //销售日期
	private String yieldlyName;//产地名称
	private String campaignName; //活动名称
	private String fromAdress;
	private String endAdress;
	private String outLicenseno;
	private String outPerson;
	//private String roNo;
	private String outSite;
	private Double outMileages;
	private Date endTime;
	private Date startTime;
	private String packageName;//配置名称
	private String seriesName;
	private String packageCode;//配置代码
	private String model;
	private String claimDirectorTelphone; //服务主管电话
	private String color; //车颜色
	private String carUseType;
	private Double mileage;
	private String delivererPhone;
	private String ctmAddress;
	private String ctmName;
	private String ctmPhone;
	private Long ctmId;
	private Long vehicleId;
	public Long getCtmId() {
		return ctmId;
	}
	public void setCtmId(Long ctmId) {
		this.ctmId = ctmId;
	}
	public Long getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(Long vehicleId) {
		this.vehicleId = vehicleId;
	}
	public String getCarUseType() {
		return carUseType;
	}
	public void setCarUseType(String carUseType) {
		this.carUseType = carUseType;
	}
	public String getClaimDirectorTelphone() {
		return claimDirectorTelphone;
	}
	public void setClaimDirectorTelphone(String claimDirectorTelphone) {
		this.claimDirectorTelphone = claimDirectorTelphone;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getPackageCode() {
		return packageCode;
	}
	public void setPackageCode(String packageCode) {
		this.packageCode = packageCode;
	}
	private Double inMileage; //行驶里程
	private Integer repairTimes; //维修次数
	private Date repairDate; //维修日期
	
	private String customerName; //用户姓名
	private String customerPhone;//用户电话
	private String customerAddress;//用户地址
	
	private Double roomCharge; //住宿费
	private Double eatupFee; //餐补费
	private Double transportation; //交通费
	private Double faxFee; //电话传真费
	private Double subsidiesFee; //补助工时费
	
	private Integer freeTimes;
	
	private String authContent; //授权内容
	private String authPerson; //授权人
	
	private String foreAuthPerson; //预授权审核人
	private String foreAuthContent; //预授权审核内容
	
	/**********Iverson add By 2010-11-22*************************/
	private String serviceAdvisor; //接待员
	
	private Integer isDqv;//是否DQV标识
	private Date productDate;//生产日期
	private String phone;//经销商电话
	private String appDate;
	private String agreeDate;
	private String outDate;
	private String buyDate;
	
	private String partCode;
	private String partName;
	private Integer quantity;
	private Double price;
	private Double amount;
	private String supplyName;
	private String dealType;
	private String malName;
	private String labourCode;
	private Float labourHours;
	private Double labourAmount;
	private String deliverPhone;
	private String deliverer;
	private String roDate;
	private String miaoShu;
	private String reason;
	private String opinion;
	private String auditName;
	private String areaName;
	private String materialCode;
	private String groupCode;
	private String banlanceDate;
	private String address;
	private String remarks;
	private String productDates;
	private String freeTimess;
	private String downCode;
	private String downName;
	private String labourName;
	private Double labourPrice;
	private Double partPrice;
	private String num;
	public String getDownCode() {
		return downCode;
	}
	public void setDownCode(String downCode) {
		this.downCode = downCode;
	}
	public String getDownName() {
		return downName;
	}
	public void setDownName(String downName) {
		this.downName = downName;
	}
	public String getLabourName() {
		return labourName;
	}
	public void setLabourName(String labourName) {
		this.labourName = labourName;
	}
	public String getProductDates() {
		return productDates;
	}
	public void setProductDates(String productDates) {
		this.productDates = productDates;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAppDate() {
		return appDate;
	}
	public void setAppDate(String appDate) {
		this.appDate = appDate;
	}
	public String getAgreeDate() {
		return agreeDate;
	}
	public void setAgreeDate(String agreeDate) {
		this.agreeDate = agreeDate;
	}
	public String getOutDate() {
		return outDate;
	}
	public void setOutDate(String outDate) {
		this.outDate = outDate;
	}
	public String getBuyDate() {
		return buyDate;
	}
	public void setBuyDate(String buyDate) {
		this.buyDate = buyDate;
	}
	public Integer getIsDqv() {
		return isDqv;
	}
	public void setIsDqv(Integer isDqv) {
		this.isDqv = isDqv;
	}
	public Date getProductDate() {
		return productDate;
	}
	public void setProductDate(Date productDate) {
		this.productDate = productDate;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getServiceAdvisor() {
		return serviceAdvisor;
	}
	public void setServiceAdvisor(String serviceAdvisor) {
		this.serviceAdvisor = serviceAdvisor;
	}
	/***********Iverson add By 2010-11-22*********************/
	
	
	public String getDealerCode() {
		return dealerCode;
	}
	public void setDealerCode(String dealerCode) {
		this.dealerCode = dealerCode;
	}
	public String getDealerName() {
		return dealerName;
	}
	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}
	/*public String getLicenseNo() {
		return licenseNo;
	}
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}*/
	/*public String getEngineNo() {
		return engineNo;
	}
	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
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
	}*/
	/*public String getTransferNo() {
		return transferNo;
	}
	public void setTransferNo(String transferNo) {
		this.transferNo = transferNo;
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
	}*/
	public String getClaimName() {
		return claimName;
	}
	public void setClaimName(String claimName) {
		this.claimName = claimName;
	}
	public String getTroubleName() {
		return troubleName;
	}
	public void setTroubleName(String troubleName) {
		this.troubleName = troubleName;
	}
	public String getDamageAreaName() {
		return damageAreaName;
	}
	public void setDamageAreaName(String damageAreaName) {
		this.damageAreaName = damageAreaName;
	}
	public String getDamageDegreeName() {
		return damageDegreeName;
	}
	public void setDamageDegreeName(String damageDegreeName) {
		this.damageDegreeName = damageDegreeName;
	}
	public String getDamageTypeName() {
		return damageTypeName;
	}
	public void setDamageTypeName(String damageTypeName) {
		this.damageTypeName = damageTypeName;
	}
	public Long getModelId() {
		return modelId;
	}
	public void setModelId(Long modelId) {
		this.modelId = modelId;
	}
	public Double getLabourAmounts() {
		return labourAmounts;
	}
	public void setLabourAmounts(Double labourAmounts) {
		this.labourAmounts = labourAmounts;
	}
	public Double getPartAmounts() {
		return partAmounts;
	}
	public void setPartAmounts(Double partAmounts) {
		this.partAmounts = partAmounts;
	}
	public Double getNetitemAmounts() {
		return netitemAmounts;
	}
	public void setNetitemAmounts(Double netitemAmounts) {
		this.netitemAmounts = netitemAmounts;
	}
	public Double getRepairTotals() {
		return repairTotals;
	}
	public void setRepairTotals(Double repairTotals) {
		this.repairTotals = repairTotals;
	}
	public Double getTaxSums() {
		return taxSums;
	}
	public void setTaxSums(Double taxSums) {
		this.taxSums = taxSums;
	}
	public Double getGrossCredits() {
		return grossCredits;
	}
	public void setGrossCredits(Double grossCredits) {
		this.grossCredits = grossCredits;
	}
	public String getStartClaim() {
		return startClaim;
	}
	public void setStartClaim(String startClaim) {
		this.startClaim = startClaim;
	}
	public String getEndClaim() {
		return endClaim;
	}
	public void setEndClaim(String endClaim) {
		this.endClaim = endClaim;
	}
	public String getDealerShortname() {
		return dealerShortname;
	}
	public void setDealerShortname(String dealerShortname) {
		this.dealerShortname = dealerShortname;
	}
	public Integer getCounts() {
		return counts;
	}
	public void setCounts(Integer counts) {
		this.counts = counts;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public Date getPurchasedDate() {
		return purchasedDate;
	}
	public void setPurchasedDate(Date purchasedDate) {
		this.purchasedDate = purchasedDate;
	}
	public String getYieldlyName() {
		return yieldlyName;
	}
	public void setYieldlyName(String yieldlyName) {
		this.yieldlyName = yieldlyName;
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
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public Double getInMileage() {
		return inMileage;
	}
	public void setInMileage(Double inMileage) {
		this.inMileage = inMileage;
	}
	public Integer getRepairTimes() {
		return repairTimes;
	}
	public void setRepairTimes(Integer repairTimes) {
		this.repairTimes = repairTimes;
	}
	public Date getRepairDate() {
		return repairDate;
	}
	public void setRepairDate(Date repairDate) {
		this.repairDate = repairDate;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerPhone() {
		return customerPhone;
	}
	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
	}
	public String getCustomerAddress() {
		return customerAddress;
	}
	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}
	public Integer getFreeTimes() {
		return freeTimes;
	}
	public void setFreeTimes(Integer freeTimes) {
		this.freeTimes = freeTimes;
	}
	public Double getRoomCharge() {
		return roomCharge;
	}
	public void setRoomCharge(Double roomCharge) {
		this.roomCharge = roomCharge;
	}
	public Double getEatupFee() {
		return eatupFee;
	}
	public void setEatupFee(Double eatupFee) {
		this.eatupFee = eatupFee;
	}
	public Double getTransportation() {
		return transportation;
	}
	public void setTransportation(Double transportation) {
		this.transportation = transportation;
	}
	public Double getFaxFee() {
		return faxFee;
	}
	public void setFaxFee(Double faxFee) {
		this.faxFee = faxFee;
	}
	public Double getSubsidiesFee() {
		return subsidiesFee;
	}
	public void setSubsidiesFee(Double subsidiesFee) {
		this.subsidiesFee = subsidiesFee;
	}
	public String getAuthContent() {
		return authContent;
	}
	public void setAuthContent(String authContent) {
		this.authContent = authContent;
	}
	public String getAuthPerson() {
		return authPerson;
	}
	public void setAuthPerson(String authPerson) {
		this.authPerson = authPerson;
	}
	public String getForeAuthPerson() {
		return foreAuthPerson;
	}
	public void setForeAuthPerson(String foreAuthPerson) {
		this.foreAuthPerson = foreAuthPerson;
	}
	public String getForeAuthContent() {
		return foreAuthContent;
	}
	public void setForeAuthContent(String foreAuthContent) {
		this.foreAuthContent = foreAuthContent;
	}
	public String getPartCode() {
		return partCode;
	}
	public void setPartCode(String partCode) {
		this.partCode = partCode;
	}
	public String getPartName() {
		return partName;
	}
	public void setPartName(String partName) {
		this.partName = partName;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getSupplyName() {
		return supplyName;
	}
	public void setSupplyName(String supplyName) {
		this.supplyName = supplyName;
	}
	public String getDealType() {
		return dealType;
	}
	public void setDealType(String dealType) {
		this.dealType = dealType;
	}
	public String getMalName() {
		return malName;
	}
	public void setMalName(String malName) {
		this.malName = malName;
	}
	public String getLabourCode() {
		return labourCode;
	}
	public void setLabourCode(String labourCode) {
		this.labourCode = labourCode;
	}
	public Float getLabourHours() {
		return labourHours;
	}
	public void setLabourHours(Float labourHours) {
		this.labourHours = labourHours;
	}
	public Double getLabourAmount() {
		return labourAmount;
	}
	public void setLabourAmount(Double labourAmount) {
		this.labourAmount = labourAmount;
	}
	public String getDeliverPhone() {
		return deliverPhone;
	}
	public void setDeliverPhone(String deliverPhone) {
		this.deliverPhone = deliverPhone;
	}
	public String getDeliverer() {
		return deliverer;
	}
	public void setDeliverer(String deliverer) {
		this.deliverer = deliverer;
	}
	public String getRoDate() {
		return roDate;
	}
	public void setRoDate(String roDate) {
		this.roDate = roDate;
	}
	public String getMiaoShu() {
		return miaoShu;
	}
	public void setMiaoShu(String miaoShu) {
		this.miaoShu = miaoShu;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getOpinion() {
		return opinion;
	}
	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}
	public String getAuditName() {
		return auditName;
	}
	public void setAuditName(String auditName) {
		this.auditName = auditName;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getMaterialCode() {
		return materialCode;
	}
	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}
	public String getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	public String getBanlanceDate() {
		return banlanceDate;
	}
	public void setBanlanceDate(String banlanceDate) {
		this.banlanceDate = banlanceDate;
	}
	public String getFreeTimess() {
		return freeTimess;
	}
	public void setFreeTimess(String freeTimess) {
		this.freeTimess = freeTimess;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public Double getLabourPrice() {
		return labourPrice;
	}
	public void setLabourPrice(Double labourPrice) {
		this.labourPrice = labourPrice;
	}
	public Double getPartPrice() {
		return partPrice;
	}
	public void setPartPrice(Double partPrice) {
		this.partPrice = partPrice;
	}
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	public String getSeriesName() {
		return seriesName;
	}
	public void setSeriesName(String seriesName) {
		this.seriesName = seriesName;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public Double getMileage() {
		return mileage;
	}
	public void setMileage(Double mileage) {
		this.mileage = mileage;
	}
	public String getDelivererPhone() {
		return delivererPhone;
	}
	public void setDelivererPhone(String delivererPhone) {
		this.delivererPhone = delivererPhone;
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
	
}
