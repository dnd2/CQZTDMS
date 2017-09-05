/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-04-12 10:27:32
* CreateBy   : KFQ
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsRoRepairPartPO extends PO{

	private String modelLabourCode;
	private Double partCostAmount;
	private String sender;
	private Date updateDate;
	private String partName;
	private Integer batchNo;
	private Double partCostPrice;
	private Date sendTime;
	private Long createBy;
	private String repairtypecode;
	private String activityCode;
	private String manageSortCode;
	private Float priceRate;
	private Long updateBy;
	private Long id;
	private Integer needlessRepair;
	private Float partQuantity;
	private String storageCode;
	private Double discount;
	private Double partSalesPrice;
	private Integer preCheck;
	private Integer interReturn;
	private Double partSalesAmount;
	private String foreLevel;
	private Integer isFinished;
	private String isSel;
	private String receiver;
	private String partBatchNo;
	private String partNo;
	private String storagePositionCode;
	private Long isFore;
	private String labourCode;
	private Integer printBatchNo;
	private Integer isGua;
	private String packageCode;
	private Integer isDiscount;
	private Integer priceType;
	private Integer payType;
	private String chargePartitionCode;
	private Long roId;
	private Integer consignExterior;
	private Integer isClaim;
	private Date createDate;
	private Date printRpTime;
	private Double oemLimitPrice;
	private String unitCode;
	private String labour;
	private Integer responsNature;
	private Long realPartId;
	private Integer hasPart;
	private Integer partUseType;
	private String mainPartCode;
	private String troubleDescribe;
	private String troubleReason;
	private String dealMethod;
	private String zfRono;
	private Integer isUse;
	private String partCamcode;
	
	public String getZfRono() {
		return zfRono;
	}

	public void setZfRono(String zfRono) {
		this.zfRono = zfRono;
	}

	public String getTroubleDescribe() {
		return troubleDescribe;
	}

	public void setTroubleDescribe(String troubleDescribe) {
		this.troubleDescribe = troubleDescribe;
	}

	public String getTroubleReason() {
		return troubleReason;
	}

	public void setTroubleReason(String troubleReason) {
		this.troubleReason = troubleReason;
	}

	public String getDealMethod() {
		return dealMethod;
	}

	public void setDealMethod(String dealMethod) {
		this.dealMethod = dealMethod;
	}

	public String getMainPartCode() {
		return mainPartCode;
	}

	public void setMainPartCode(String mainPartCode) {
		this.mainPartCode = mainPartCode;
	}

	public Integer getPartUseType() {
		return partUseType;
	}

	public void setPartUseType(Integer partUseType) {
		this.partUseType = partUseType;
	}

	public Integer getHasPart() {
		return hasPart;
	}

	public void setHasPart(Integer hasPart) {
		this.hasPart = hasPart;
	}

	public Long getRealPartId() {
		return realPartId;
	}

	public void setRealPartId(Long realPartId) {
		this.realPartId = realPartId;
	}

	public void setModelLabourCode(String modelLabourCode){
		this.modelLabourCode=modelLabourCode;
	}

	public String getModelLabourCode(){
		return this.modelLabourCode;
	}

	public void setPartCostAmount(Double partCostAmount){
		this.partCostAmount=partCostAmount;
	}

	public Double getPartCostAmount(){
		return this.partCostAmount;
	}

	public void setSender(String sender){
		this.sender=sender;
	}

	public String getSender(){
		return this.sender;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setPartName(String partName){
		this.partName=partName;
	}

	public String getPartName(){
		return this.partName;
	}

	public void setBatchNo(Integer batchNo){
		this.batchNo=batchNo;
	}

	public Integer getBatchNo(){
		return this.batchNo;
	}

	public void setPartCostPrice(Double partCostPrice){
		this.partCostPrice=partCostPrice;
	}

	public Double getPartCostPrice(){
		return this.partCostPrice;
	}

	public void setSendTime(Date sendTime){
		this.sendTime=sendTime;
	}

	public Date getSendTime(){
		return this.sendTime;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setRepairtypecode(String repairtypecode){
		this.repairtypecode=repairtypecode;
	}

	public String getRepairtypecode(){
		return this.repairtypecode;
	}

	public void setActivityCode(String activityCode){
		this.activityCode=activityCode;
	}

	public String getActivityCode(){
		return this.activityCode;
	}

	public void setManageSortCode(String manageSortCode){
		this.manageSortCode=manageSortCode;
	}

	public String getManageSortCode(){
		return this.manageSortCode;
	}

	public void setPriceRate(Float priceRate){
		this.priceRate=priceRate;
	}

	public Float getPriceRate(){
		return this.priceRate;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setNeedlessRepair(Integer needlessRepair){
		this.needlessRepair=needlessRepair;
	}

	public Integer getNeedlessRepair(){
		return this.needlessRepair;
	}

	public void setPartQuantity(Float partQuantity){
		this.partQuantity=partQuantity;
	}

	public Float getPartQuantity(){
		return this.partQuantity;
	}

	public void setStorageCode(String storageCode){
		this.storageCode=storageCode;
	}

	public String getStorageCode(){
		return this.storageCode;
	}

	public void setDiscount(Double discount){
		this.discount=discount;
	}

	public Double getDiscount(){
		return this.discount;
	}

	public void setPartSalesPrice(Double partSalesPrice){
		this.partSalesPrice=partSalesPrice;
	}

	public Double getPartSalesPrice(){
		return this.partSalesPrice;
	}

	public void setPreCheck(Integer preCheck){
		this.preCheck=preCheck;
	}

	public Integer getPreCheck(){
		return this.preCheck;
	}

	public void setInterReturn(Integer interReturn){
		this.interReturn=interReturn;
	}

	public Integer getInterReturn(){
		return this.interReturn;
	}

	public void setPartSalesAmount(Double partSalesAmount){
		this.partSalesAmount=partSalesAmount;
	}

	public Double getPartSalesAmount(){
		return this.partSalesAmount;
	}

	public void setForeLevel(String foreLevel){
		this.foreLevel=foreLevel;
	}

	public String getForeLevel(){
		return this.foreLevel;
	}

	public void setIsFinished(Integer isFinished){
		this.isFinished=isFinished;
	}

	public Integer getIsFinished(){
		return this.isFinished;
	}

	public void setIsSel(String isSel){
		this.isSel=isSel;
	}

	public String getIsSel(){
		return this.isSel;
	}

	public void setReceiver(String receiver){
		this.receiver=receiver;
	}

	public String getReceiver(){
		return this.receiver;
	}

	public void setPartBatchNo(String partBatchNo){
		this.partBatchNo=partBatchNo;
	}

	public String getPartBatchNo(){
		return this.partBatchNo;
	}

	public void setPartNo(String partNo){
		this.partNo=partNo;
	}

	public String getPartNo(){
		return this.partNo;
	}

	public void setStoragePositionCode(String storagePositionCode){
		this.storagePositionCode=storagePositionCode;
	}

	public String getStoragePositionCode(){
		return this.storagePositionCode;
	}

	public void setIsFore(Long isFore){
		this.isFore=isFore;
	}

	public Long getIsFore(){
		return this.isFore;
	}

	public void setLabourCode(String labourCode){
		this.labourCode=labourCode;
	}

	public String getLabourCode(){
		return this.labourCode;
	}

	public void setPrintBatchNo(Integer printBatchNo){
		this.printBatchNo=printBatchNo;
	}

	public Integer getPrintBatchNo(){
		return this.printBatchNo;
	}

	public void setIsGua(Integer isGua){
		this.isGua=isGua;
	}

	public Integer getIsGua(){
		return this.isGua;
	}

	public void setPackageCode(String packageCode){
		this.packageCode=packageCode;
	}

	public String getPackageCode(){
		return this.packageCode;
	}

	public void setIsDiscount(Integer isDiscount){
		this.isDiscount=isDiscount;
	}

	public Integer getIsDiscount(){
		return this.isDiscount;
	}

	public void setPriceType(Integer priceType){
		this.priceType=priceType;
	}

	public Integer getPriceType(){
		return this.priceType;
	}

	public void setPayType(Integer payType){
		this.payType=payType;
	}

	public Integer getPayType(){
		return this.payType;
	}

	public void setChargePartitionCode(String chargePartitionCode){
		this.chargePartitionCode=chargePartitionCode;
	}

	public String getChargePartitionCode(){
		return this.chargePartitionCode;
	}

	public void setRoId(Long roId){
		this.roId=roId;
	}

	public Long getRoId(){
		return this.roId;
	}

	public void setConsignExterior(Integer consignExterior){
		this.consignExterior=consignExterior;
	}

	public Integer getConsignExterior(){
		return this.consignExterior;
	}

	public void setIsClaim(Integer isClaim){
		this.isClaim=isClaim;
	}

	public Integer getIsClaim(){
		return this.isClaim;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setPrintRpTime(Date printRpTime){
		this.printRpTime=printRpTime;
	}

	public Date getPrintRpTime(){
		return this.printRpTime;
	}

	public void setOemLimitPrice(Double oemLimitPrice){
		this.oemLimitPrice=oemLimitPrice;
	}

	public Double getOemLimitPrice(){
		return this.oemLimitPrice;
	}

	public void setUnitCode(String unitCode){
		this.unitCode=unitCode;
	}

	public String getUnitCode(){
		return this.unitCode;
	}

	public String getLabour() {
		return labour;
	}

	public void setLabour(String labour) {
		this.labour = labour;
	}

	public Integer getResponsNature() {
		return responsNature;
	}

	public void setResponsNature(Integer responsNature) {
		this.responsNature = responsNature;
	}

	public Integer getIsUse() {
		return isUse;
	}

	public void setIsUse(Integer isUse) {
		this.isUse = isUse;
	}

	public String getPartCamcode() {
		return partCamcode;
	}

	public void setPartCamcode(String partCamcode) {
		this.partCamcode = partCamcode;
	}

}