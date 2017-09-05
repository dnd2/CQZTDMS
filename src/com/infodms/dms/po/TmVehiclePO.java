/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-10-27 13:50:26
* CreateBy   : wizard_lee
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmVehiclePO extends PO{

	private Long sdNumber;
	private Date comInvoDate;
	private Date wrEndDate;
	private Long warehouseId;
	private Integer orgType;
	private String vn;
	private String batchNo;
	private String vehicleArea;
	private String remark;
	private Integer nodeCode;
	private Date hgzPrintDate;
	private Double vhclPrice;
	private Date purchasedDate;
	private String hgzNo;
	private Long modelId;
	private Long oemCompanyId;
	private Long updateBy;
	private Double mileage;
	private String licenseNo;
	private Date offlineDate;
	private Integer isDomestic;
	private String engineNo;
	private Integer freeTimes;
	private Long packageId;
	private Long perId;
	private Long outDetailId;
	private String color;
	private Date factoryDate;
	private Integer isStatus;
	private Integer outStatus;
	private Long recDealerId;
	private Long planDetailId;
	private String erpMaterialCode;
	private Integer hgzStatus;
	private Date storageDate;
	private String recDealerName;
	private Integer lifeCycle;
	private Integer ver;
	private String rearaxleNo;
	private Date createDate;
	private String recDealerShortname;
	private String importPeople;
	private Double historyMile;
	private Long addressId;
	private Double meterMile;
	private String transferNo;
	private Long dealerId;
	private String specialBatchNo;
	private String vin;
	private Date updateDate;
	private Date orgStorageDate;
	private String erpFph;
	private Long createBy;
	private Long claimTacticsId;
	private Integer isPdi;
	private Long areaId;
	private Long hgzPrintPer;
	private Integer lockStatus;
	private Long materialId;
	private Date nodeDate;
	private String sitCode;
	private Date productDate;
	private Long seriesId;
	private String gearboxNo;
	private String pin;
	private Long provId;
	private Long yieldly;
	private Long vehicleId;
	private Integer repairAsQcar;
	private String processType;
	private String manufacturedate;
	private String dealerShortname;
	private String warehouseAddress;
	private Long sitId;
	private String orderNo;
	private Date arrivDate;
	private String zwCount;
	private Date importDate;
	private String createType;
	private Double startMileage;
	private Date licenseDate;
	private Long orgId;
	private String dealerName;
	private Long location;
	private Integer isVcost;
	private String modelYear;
	private Integer vehicleType;
	private String hegezhengCode;
	
	private Integer isDamage;
	private Integer isPassStatus;
	private Long lastAdjustId;
	private Date lastAdjustDate;
	private String lastAdjustRemark;
	private String specialOrderNo;
	private String erpOrderId;
	
	public String getErpOrderId() {
		return erpOrderId;
	}

	public void setErpOrderId(String erpOrderId) {
		this.erpOrderId = erpOrderId;
	}

	public String getSpecialOrderNo() {
		return specialOrderNo;
	}

	public void setSpecialOrderNo(String specialOrderNo) {
		this.specialOrderNo = specialOrderNo;
	}

	public Long getLastAdjustId() {
		return lastAdjustId;
	}

	public void setLastAdjustId(Long lastAdjustId) {
		this.lastAdjustId = lastAdjustId;
	}

	public Date getLastAdjustDate() {
		return lastAdjustDate;
	}

	public void setLastAdjustDate(Date lastAdjustDate) {
		this.lastAdjustDate = lastAdjustDate;
	}

	public String getLastAdjustRemark() {
		return lastAdjustRemark;
	}

	public void setLastAdjustRemark(String lastAdjustRemark) {
		this.lastAdjustRemark = lastAdjustRemark;
	}

	public Integer getIsPassStatus() {
		return isPassStatus;
	}

	public void setIsPassStatus(Integer isPassStatus) {
		this.isPassStatus = isPassStatus;
	}

	public Integer getIsDamage() {
		return isDamage;
	}

	public void setIsDamage(Integer isDamage) {
		this.isDamage = isDamage;
	}

	public void setSdNumber(Long sdNumber){
		this.sdNumber=sdNumber;
	}

	public Long getSdNumber(){
		return this.sdNumber;
	}

	public void setComInvoDate(Date comInvoDate){
		this.comInvoDate=comInvoDate;
	}

	public Date getComInvoDate(){
		return this.comInvoDate;
	}

	public void setWrEndDate(Date wrEndDate){
		this.wrEndDate=wrEndDate;
	}

	public Date getWrEndDate(){
		return this.wrEndDate;
	}

	public void setWarehouseId(Long warehouseId){
		this.warehouseId=warehouseId;
	}

	public Long getWarehouseId(){
		return this.warehouseId;
	}

	public void setOrgType(Integer orgType){
		this.orgType=orgType;
	}

	public Integer getOrgType(){
		return this.orgType;
	}

	public void setVn(String vn){
		this.vn=vn;
	}

	public String getVn(){
		return this.vn;
	}

	public void setBatchNo(String batchNo){
		this.batchNo=batchNo;
	}

	public String getBatchNo(){
		return this.batchNo;
	}

	public void setVehicleArea(String vehicleArea){
		this.vehicleArea=vehicleArea;
	}

	public String getVehicleArea(){
		return this.vehicleArea;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setNodeCode(Integer nodeCode){
		this.nodeCode=nodeCode;
	}

	public Integer getNodeCode(){
		return this.nodeCode;
	}

	public void setHgzPrintDate(Date hgzPrintDate){
		this.hgzPrintDate=hgzPrintDate;
	}

	public Date getHgzPrintDate(){
		return this.hgzPrintDate;
	}

	public void setVhclPrice(Double vhclPrice){
		this.vhclPrice=vhclPrice;
	}

	public Double getVhclPrice(){
		return this.vhclPrice;
	}

	public void setPurchasedDate(Date purchasedDate){
		this.purchasedDate=purchasedDate;
	}

	public Date getPurchasedDate(){
		return this.purchasedDate;
	}

	public void setHgzNo(String hgzNo){
		this.hgzNo=hgzNo;
	}

	public String getHgzNo(){
		return this.hgzNo;
	}

	public void setModelId(Long modelId){
		this.modelId=modelId;
	}

	public Long getModelId(){
		return this.modelId;
	}

	public void setOemCompanyId(Long oemCompanyId){
		this.oemCompanyId=oemCompanyId;
	}

	public Long getOemCompanyId(){
		return this.oemCompanyId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setMileage(Double mileage){
		this.mileage=mileage;
	}

	public Double getMileage(){
		return this.mileage;
	}

	public void setLicenseNo(String licenseNo){
		this.licenseNo=licenseNo;
	}

	public String getLicenseNo(){
		return this.licenseNo;
	}

	public void setOfflineDate(Date offlineDate){
		this.offlineDate=offlineDate;
	}

	public Date getOfflineDate(){
		return this.offlineDate;
	}

	public void setIsDomestic(Integer isDomestic){
		this.isDomestic=isDomestic;
	}

	public Integer getIsDomestic(){
		return this.isDomestic;
	}

	public void setEngineNo(String engineNo){
		this.engineNo=engineNo;
	}

	public String getEngineNo(){
		return this.engineNo;
	}

	public void setFreeTimes(Integer freeTimes){
		this.freeTimes=freeTimes;
	}

	public Integer getFreeTimes(){
		return this.freeTimes;
	}

	public void setPackageId(Long packageId){
		this.packageId=packageId;
	}

	public Long getPackageId(){
		return this.packageId;
	}

	public void setPerId(Long perId){
		this.perId=perId;
	}

	public Long getPerId(){
		return this.perId;
	}

	public void setOutDetailId(Long outDetailId){
		this.outDetailId=outDetailId;
	}

	public Long getOutDetailId(){
		return this.outDetailId;
	}

	public void setColor(String color){
		this.color=color;
	}

	public String getColor(){
		return this.color;
	}

	public void setFactoryDate(Date factoryDate){
		this.factoryDate=factoryDate;
	}

	public Date getFactoryDate(){
		return this.factoryDate;
	}

	public void setIsStatus(Integer isStatus){
		this.isStatus=isStatus;
	}

	public Integer getIsStatus(){
		return this.isStatus;
	}

	public void setOutStatus(Integer outStatus){
		this.outStatus=outStatus;
	}

	public Integer getOutStatus(){
		return this.outStatus;
	}

	public void setRecDealerId(Long recDealerId){
		this.recDealerId=recDealerId;
	}

	public Long getRecDealerId(){
		return this.recDealerId;
	}

	public void setPlanDetailId(Long planDetailId){
		this.planDetailId=planDetailId;
	}

	public Long getPlanDetailId(){
		return this.planDetailId;
	}

	public void setErpMaterialCode(String erpMaterialCode){
		this.erpMaterialCode=erpMaterialCode;
	}

	public String getErpMaterialCode(){
		return this.erpMaterialCode;
	}

	public void setHgzStatus(Integer hgzStatus){
		this.hgzStatus=hgzStatus;
	}

	public Integer getHgzStatus(){
		return this.hgzStatus;
	}

	public void setStorageDate(Date storageDate){
		this.storageDate=storageDate;
	}

	public Date getStorageDate(){
		return this.storageDate;
	}

	public void setRecDealerName(String recDealerName){
		this.recDealerName=recDealerName;
	}

	public String getRecDealerName(){
		return this.recDealerName;
	}

	public void setLifeCycle(Integer lifeCycle){
		this.lifeCycle=lifeCycle;
	}

	public Integer getLifeCycle(){
		return this.lifeCycle;
	}

	public void setVer(Integer ver){
		this.ver=ver;
	}

	public Integer getVer(){
		return this.ver;
	}

	public void setRearaxleNo(String rearaxleNo){
		this.rearaxleNo=rearaxleNo;
	}

	public String getRearaxleNo(){
		return this.rearaxleNo;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setRecDealerShortname(String recDealerShortname){
		this.recDealerShortname=recDealerShortname;
	}

	public String getRecDealerShortname(){
		return this.recDealerShortname;
	}

	public void setImportPeople(String importPeople){
		this.importPeople=importPeople;
	}

	public String getImportPeople(){
		return this.importPeople;
	}

	public void setHistoryMile(Double historyMile){
		this.historyMile=historyMile;
	}

	public Double getHistoryMile(){
		return this.historyMile;
	}

	public void setAddressId(Long addressId){
		this.addressId=addressId;
	}

	public Long getAddressId(){
		return this.addressId;
	}

	public void setMeterMile(Double meterMile){
		this.meterMile=meterMile;
	}

	public Double getMeterMile(){
		return this.meterMile;
	}

	public void setTransferNo(String transferNo){
		this.transferNo=transferNo;
	}

	public String getTransferNo(){
		return this.transferNo;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setSpecialBatchNo(String specialBatchNo){
		this.specialBatchNo=specialBatchNo;
	}

	public String getSpecialBatchNo(){
		return this.specialBatchNo;
	}

	public void setVin(String vin){
		this.vin=vin;
	}

	public String getVin(){
		return this.vin;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setOrgStorageDate(Date orgStorageDate){
		this.orgStorageDate=orgStorageDate;
	}

	public Date getOrgStorageDate(){
		return this.orgStorageDate;
	}

	public void setErpFph(String erpFph){
		this.erpFph=erpFph;
	}

	public String getErpFph(){
		return this.erpFph;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setClaimTacticsId(Long claimTacticsId){
		this.claimTacticsId=claimTacticsId;
	}

	public Long getClaimTacticsId(){
		return this.claimTacticsId;
	}

	public void setIsPdi(Integer isPdi){
		this.isPdi=isPdi;
	}

	public Integer getIsPdi(){
		return this.isPdi;
	}

	public void setAreaId(Long areaId){
		this.areaId=areaId;
	}

	public Long getAreaId(){
		return this.areaId;
	}

	public void setHgzPrintPer(Long hgzPrintPer){
		this.hgzPrintPer=hgzPrintPer;
	}

	public Long getHgzPrintPer(){
		return this.hgzPrintPer;
	}

	public void setLockStatus(Integer lockStatus){
		this.lockStatus=lockStatus;
	}

	public Integer getLockStatus(){
		return this.lockStatus;
	}

	public void setMaterialId(Long materialId){
		this.materialId=materialId;
	}

	public Long getMaterialId(){
		return this.materialId;
	}

	public void setNodeDate(Date nodeDate){
		this.nodeDate=nodeDate;
	}

	public Date getNodeDate(){
		return this.nodeDate;
	}

	public void setSitCode(String sitCode){
		this.sitCode=sitCode;
	}

	public String getSitCode(){
		return this.sitCode;
	}

	public void setProductDate(Date productDate){
		this.productDate=productDate;
	}

	public Date getProductDate(){
		return this.productDate;
	}

	public void setSeriesId(Long seriesId){
		this.seriesId=seriesId;
	}

	public Long getSeriesId(){
		return this.seriesId;
	}

	public void setGearboxNo(String gearboxNo){
		this.gearboxNo=gearboxNo;
	}

	public String getGearboxNo(){
		return this.gearboxNo;
	}

	public void setPin(String pin){
		this.pin=pin;
	}

	public String getPin(){
		return this.pin;
	}

	public void setProvId(Long provId){
		this.provId=provId;
	}

	public Long getProvId(){
		return this.provId;
	}

	public void setYieldly(Long yieldly){
		this.yieldly=yieldly;
	}

	public Long getYieldly(){
		return this.yieldly;
	}

	public void setVehicleId(Long vehicleId){
		this.vehicleId=vehicleId;
	}

	public Long getVehicleId(){
		return this.vehicleId;
	}

	public void setRepairAsQcar(Integer repairAsQcar){
		this.repairAsQcar=repairAsQcar;
	}

	public Integer getRepairAsQcar(){
		return this.repairAsQcar;
	}

	public void setProcessType(String processType){
		this.processType=processType;
	}

	public String getProcessType(){
		return this.processType;
	}

	public void setManufacturedate(String manufacturedate){
		this.manufacturedate=manufacturedate;
	}

	public String getManufacturedate(){
		return this.manufacturedate;
	}

	public void setDealerShortname(String dealerShortname){
		this.dealerShortname=dealerShortname;
	}

	public String getDealerShortname(){
		return this.dealerShortname;
	}

	public void setWarehouseAddress(String warehouseAddress){
		this.warehouseAddress=warehouseAddress;
	}

	public String getWarehouseAddress(){
		return this.warehouseAddress;
	}

	public void setSitId(Long sitId){
		this.sitId=sitId;
	}

	public Long getSitId(){
		return this.sitId;
	}

	public void setOrderNo(String orderNo){
		this.orderNo=orderNo;
	}

	public String getOrderNo(){
		return this.orderNo;
	}

	public void setArrivDate(Date arrivDate){
		this.arrivDate=arrivDate;
	}

	public Date getArrivDate(){
		return this.arrivDate;
	}

	public void setZwCount(String zwCount){
		this.zwCount=zwCount;
	}

	public String getZwCount(){
		return this.zwCount;
	}

	public void setImportDate(Date importDate){
		this.importDate=importDate;
	}

	public Date getImportDate(){
		return this.importDate;
	}

	public void setCreateType(String createType){
		this.createType=createType;
	}

	public String getCreateType(){
		return this.createType;
	}

	public void setStartMileage(Double startMileage){
		this.startMileage=startMileage;
	}

	public Double getStartMileage(){
		return this.startMileage;
	}

	public void setLicenseDate(Date licenseDate){
		this.licenseDate=licenseDate;
	}

	public Date getLicenseDate(){
		return this.licenseDate;
	}

	public void setOrgId(Long orgId){
		this.orgId=orgId;
	}

	public Long getOrgId(){
		return this.orgId;
	}

	public void setDealerName(String dealerName){
		this.dealerName=dealerName;
	}

	public String getDealerName(){
		return this.dealerName;
	}

	public void setLocation(Long location){
		this.location=location;
	}

	public Long getLocation(){
		return this.location;
	}

	public void setIsVcost(Integer isVcost){
		this.isVcost=isVcost;
	}

	public Integer getIsVcost(){
		return this.isVcost;
	}

	public void setModelYear(String modelYear){
		this.modelYear=modelYear;
	}

	public String getModelYear(){
		return this.modelYear;
	}

	public void setVehicleType(Integer vehicleType){
		this.vehicleType=vehicleType;
	}

	public Integer getVehicleType(){
		return this.vehicleType;
	}

	public void setHegezhengCode(String hegezhengCode){
		this.hegezhengCode=hegezhengCode;
	}

	public String getHegezhengCode(){
		return this.hegezhengCode;
	}

}