/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-02-17 21:18:08
* CreateBy   : Yu
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartDefineHistoryPO extends PO{

	private Double gpPrice;
	private Integer isExport;
	private Long repartMby;
	private Integer partType;
	private String remark;
	private Integer isLack;
	private Long repartId;
	private Integer oemSales;
	private String modelId;
	private Long updateBy;
	private Integer isReplaced;
	private String partId;
	private String partEname;
	private Long minPackage1;
	private Long minPackage2;
	private Integer position;
	private String repartCode;
	private Long history;
	private Integer isEngine;
	private Integer produceState;
	private String seriesName;
	private Integer oemPlan;
	private Date deleteDate;
	private Integer isJscontrol;
	private String note;
	private Double retailPrice;
	private String partCname;
	private Double planPrice;
	private Integer isGift;
	private Integer isDirect;
	private Date createDate;
	private Integer guaranteesTime;
	private Integer isAssembly;
	private String groupCode;
	private Long parentId;
	private Double salesPrice;
	private Long whmanId;
	private String sortCode;
	private String partTpcode;
	private Integer state;
	private Integer partMaterial;
	private Integer packState;
	private String parentCode;
	private Date updateDate;
	private String enUnit;
	private String height;
	private Long createBy;
	private Integer dlrSales;
	private Integer status;
	private Integer mobility;
	private Long repartBy;
	private String weight;
	private String seriesId;
	private Date repartMdate;
	private String depth;
	private Long deleteBy;
	private Long disableBy;
	private Integer isPermanent;
	private Integer isMaintain;
	private Integer partOrigin;
	private String unit;
	private Integer guaranteesMile;
	private Date disableDate;
	private Long planerId;
	private Integer isPlan;
	private Integer buyState;
	private String partCode;
	private String modelName;
	private String volume;
	private Integer produceFac;
	private String width;
	private Long buyerId;
	private Date repartDate;
	private String partOldcode;

	private String modelCode;
    private Integer ownedBase;
    private Integer length;
    private Integer vehicleVolume;
    private Integer isSaleDisable;
    private Date saleDisableDate;
    private Integer isStopLoad;
    private Date stopLoadDate;
    private Integer partFit;
    private Date firstWarnhouseDate;
    private Integer isEntrusrPack;
    private Integer cccFlag;
    private Integer minSale;
    private Integer minPurchase;
    private Long maxSaleVolume;
    private String packSpecification;
    private String otherPartId;
    private Integer isSecurity;
    private Integer isProtocolPack;
    private Integer isMagBatch;
    private Integer partCategory;
    private Integer isPartDisable;
    private Date partDisableDate;
    private String dlrPartId;
    
	public void setGpPrice(Double gpPrice){
		this.gpPrice=gpPrice;
	}

	public Double getGpPrice(){
		return this.gpPrice;
	}

	public String getDlrPartId() {
        return dlrPartId;
    }

    public void setDlrPartId(String dlrPartId) {
        this.dlrPartId = dlrPartId;
    }

    public void setIsExport(Integer isExport){
		this.isExport=isExport;
	}

	public Integer getIsExport(){
		return this.isExport;
	}

	public void setRepartMby(Long repartMby){
		this.repartMby=repartMby;
	}

	public Long getRepartMby(){
		return this.repartMby;
	}

	public void setPartType(Integer partType){
		this.partType=partType;
	}

	public Integer getPartType(){
		return this.partType;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setIsLack(Integer isLack){
		this.isLack=isLack;
	}

	public Integer getIsLack(){
		return this.isLack;
	}

	public void setRepartId(Long repartId){
		this.repartId=repartId;
	}

	public Long getRepartId(){
		return this.repartId;
	}

	public void setOemSales(Integer oemSales){
		this.oemSales=oemSales;
	}

	public Integer getOemSales(){
		return this.oemSales;
	}

	public void setModelId(String modelId){
		this.modelId=modelId;
	}

	public String getModelId(){
		return this.modelId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setIsReplaced(Integer isReplaced){
		this.isReplaced=isReplaced;
	}

	public Integer getIsReplaced(){
		return this.isReplaced;
	}

	public void setPartId(String partId){
		this.partId=partId;
	}

	public String getPartId(){
		return this.partId;
	}

	public void setPartEname(String partEname){
		this.partEname=partEname;
	}

	public String getPartEname(){
		return this.partEname;
	}

	public void setMinPackage1(Long minPackage1){
		this.minPackage1=minPackage1;
	}

	public Long getMinPackage1(){
		return this.minPackage1;
	}

	public void setMinPackage2(Long minPackage2){
		this.minPackage2=minPackage2;
	}

	public Long getMinPackage2(){
		return this.minPackage2;
	}

	public void setPosition(Integer position){
		this.position=position;
	}

	public Integer getPosition(){
		return this.position;
	}

	public void setRepartCode(String repartCode){
		this.repartCode=repartCode;
	}

	public String getRepartCode(){
		return this.repartCode;
	}

	public void setHistory(Long history){
		this.history=history;
	}

	public Long getHistory(){
		return this.history;
	}

	public void setIsEngine(Integer isEngine){
		this.isEngine=isEngine;
	}

	public Integer getIsEngine(){
		return this.isEngine;
	}

	public void setProduceState(Integer produceState){
		this.produceState=produceState;
	}

	public Integer getProduceState(){
		return this.produceState;
	}

	public void setSeriesName(String seriesName){
		this.seriesName=seriesName;
	}

	public String getSeriesName(){
		return this.seriesName;
	}

	public void setOemPlan(Integer oemPlan){
		this.oemPlan=oemPlan;
	}

	public Integer getOemPlan(){
		return this.oemPlan;
	}

	public void setDeleteDate(Date deleteDate){
		this.deleteDate=deleteDate;
	}

	public Date getDeleteDate(){
		return this.deleteDate;
	}

	public void setIsJscontrol(Integer isJscontrol){
		this.isJscontrol=isJscontrol;
	}

	public Integer getIsJscontrol(){
		return this.isJscontrol;
	}

	public void setNote(String note){
		this.note=note;
	}

	public String getNote(){
		return this.note;
	}

	public void setRetailPrice(Double retailPrice){
		this.retailPrice=retailPrice;
	}

	public Double getRetailPrice(){
		return this.retailPrice;
	}

	public void setPartCname(String partCname){
		this.partCname=partCname;
	}

	public String getPartCname(){
		return this.partCname;
	}

	public void setPlanPrice(Double planPrice){
		this.planPrice=planPrice;
	}

	public Double getPlanPrice(){
		return this.planPrice;
	}

	public void setIsGift(Integer isGift){
		this.isGift=isGift;
	}

	public Integer getIsGift(){
		return this.isGift;
	}

	public void setIsDirect(Integer isDirect){
		this.isDirect=isDirect;
	}

	public Integer getIsDirect(){
		return this.isDirect;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setGuaranteesTime(Integer guaranteesTime){
		this.guaranteesTime=guaranteesTime;
	}

	public Integer getGuaranteesTime(){
		return this.guaranteesTime;
	}

	public void setIsAssembly(Integer isAssembly){
		this.isAssembly=isAssembly;
	}

	public Integer getIsAssembly(){
		return this.isAssembly;
	}

	public void setGroupCode(String groupCode){
		this.groupCode=groupCode;
	}

	public String getGroupCode(){
		return this.groupCode;
	}

	public void setParentId(Long parentId){
		this.parentId=parentId;
	}

	public Long getParentId(){
		return this.parentId;
	}

	public void setSalesPrice(Double salesPrice){
		this.salesPrice=salesPrice;
	}

	public Double getSalesPrice(){
		return this.salesPrice;
	}

	public void setWhmanId(Long whmanId){
		this.whmanId=whmanId;
	}

	public Long getWhmanId(){
		return this.whmanId;
	}

	public void setSortCode(String sortCode){
		this.sortCode=sortCode;
	}

	public String getSortCode(){
		return this.sortCode;
	}

	public void setPartTpcode(String partTpcode){
		this.partTpcode=partTpcode;
	}

	public String getPartTpcode(){
		return this.partTpcode;
	}

	public void setState(Integer state){
		this.state=state;
	}

	public Integer getState(){
		return this.state;
	}

	public void setPartMaterial(Integer partMaterial){
		this.partMaterial=partMaterial;
	}

	public Integer getPartMaterial(){
		return this.partMaterial;
	}

	public void setPackState(Integer packState){
		this.packState=packState;
	}

	public Integer getPackState(){
		return this.packState;
	}

	public void setParentCode(String parentCode){
		this.parentCode=parentCode;
	}

	public String getParentCode(){
		return this.parentCode;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setEnUnit(String enUnit){
		this.enUnit=enUnit;
	}

	public String getEnUnit(){
		return this.enUnit;
	}

	public void setHeight(String height){
		this.height=height;
	}

	public String getHeight(){
		return this.height;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setDlrSales(Integer dlrSales){
		this.dlrSales=dlrSales;
	}

	public Integer getDlrSales(){
		return this.dlrSales;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setMobility(Integer mobility){
		this.mobility=mobility;
	}

	public Integer getMobility(){
		return this.mobility;
	}

	public void setRepartBy(Long repartBy){
		this.repartBy=repartBy;
	}

	public Long getRepartBy(){
		return this.repartBy;
	}

	public void setWeight(String weight){
		this.weight=weight;
	}

	public String getWeight(){
		return this.weight;
	}

	public void setSeriesId(String seriesId){
		this.seriesId=seriesId;
	}

	public String getSeriesId(){
		return this.seriesId;
	}

	public void setRepartMdate(Date repartMdate){
		this.repartMdate=repartMdate;
	}

	public Date getRepartMdate(){
		return this.repartMdate;
	}

	public void setDepth(String depth){
		this.depth=depth;
	}

	public String getDepth(){
		return this.depth;
	}

	public void setDeleteBy(Long deleteBy){
		this.deleteBy=deleteBy;
	}

	public Long getDeleteBy(){
		return this.deleteBy;
	}

	public void setDisableBy(Long disableBy){
		this.disableBy=disableBy;
	}

	public Long getDisableBy(){
		return this.disableBy;
	}

	public void setIsPermanent(Integer isPermanent){
		this.isPermanent=isPermanent;
	}

	public Integer getIsPermanent(){
		return this.isPermanent;
	}

	public void setIsMaintain(Integer isMaintain){
		this.isMaintain=isMaintain;
	}

	public Integer getIsMaintain(){
		return this.isMaintain;
	}

	public void setPartOrigin(Integer partOrigin){
		this.partOrigin=partOrigin;
	}

	public Integer getPartOrigin(){
		return this.partOrigin;
	}

	public void setUnit(String unit){
		this.unit=unit;
	}

	public String getUnit(){
		return this.unit;
	}

	public void setGuaranteesMile(Integer guaranteesMile){
		this.guaranteesMile=guaranteesMile;
	}

	public Integer getGuaranteesMile(){
		return this.guaranteesMile;
	}

	public void setDisableDate(Date disableDate){
		this.disableDate=disableDate;
	}

	public Date getDisableDate(){
		return this.disableDate;
	}

	public void setPlanerId(Long planerId){
		this.planerId=planerId;
	}

	public Long getPlanerId(){
		return this.planerId;
	}

	public void setIsPlan(Integer isPlan){
		this.isPlan=isPlan;
	}

	public Integer getIsPlan(){
		return this.isPlan;
	}

	public void setBuyState(Integer buyState){
		this.buyState=buyState;
	}

	public Integer getBuyState(){
		return this.buyState;
	}

	public void setPartCode(String partCode){
		this.partCode=partCode;
	}

	public String getPartCode(){
		return this.partCode;
	}

	public void setModelName(String modelName){
		this.modelName=modelName;
	}

	public String getModelName(){
		return this.modelName;
	}

	public void setVolume(String volume){
		this.volume=volume;
	}

	public String getVolume(){
		return this.volume;
	}

	public void setProduceFac(Integer produceFac){
		this.produceFac=produceFac;
	}

	public Integer getProduceFac(){
		return this.produceFac;
	}

	public void setWidth(String width){
		this.width=width;
	}

	public String getWidth(){
		return this.width;
	}

	public void setBuyerId(Long buyerId){
		this.buyerId=buyerId;
	}

	public Long getBuyerId(){
		return this.buyerId;
	}

	public void setRepartDate(Date repartDate){
		this.repartDate=repartDate;
	}

	public Date getRepartDate(){
		return this.repartDate;
	}

	public void setPartOldcode(String partOldcode){
		this.partOldcode=partOldcode;
	}

	public String getPartOldcode(){
		return this.partOldcode;
	}

    public Integer getOwnedBase() {
        return ownedBase;
    }

    public void setOwnedBase(Integer ownedBase) {
        this.ownedBase = ownedBase;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getVehicleVolume() {
        return vehicleVolume;
    }

    public void setVehicleVolume(Integer vehicleVolume) {
        this.vehicleVolume = vehicleVolume;
    }

    public Integer getIsSaleDisable() {
        return isSaleDisable;
    }

    public void setIsSaleDisable(Integer isSaleDisable) {
        this.isSaleDisable = isSaleDisable;
    }

    public Date getSaleDisableDate() {
        return saleDisableDate;
    }

    public void setSaleDisableDate(Date saleDisableDate) {
        this.saleDisableDate = saleDisableDate;
    }

    public Integer getIsStopLoad() {
        return isStopLoad;
    }

    public void setIsStopLoad(Integer isStopLoad) {
        this.isStopLoad = isStopLoad;
    }

    public Date getStopLoadDate() {
        return stopLoadDate;
    }

    public void setStopLoadDate(Date stopLoadDate) {
        this.stopLoadDate = stopLoadDate;
    }

    public Integer getPartFit() {
        return partFit;
    }

    public void setPartFit(Integer partFit) {
        this.partFit = partFit;
    }

    public Date getFirstWarnhouseDate() {
        return firstWarnhouseDate;
    }

    public void setFirstWarnhouseDate(Date firstWarnhouseDate) {
        this.firstWarnhouseDate = firstWarnhouseDate;
    }

    public Integer getIsEntrusrPack() {
        return isEntrusrPack;
    }

    public void setIsEntrusrPack(Integer isEntrusrPack) {
        this.isEntrusrPack = isEntrusrPack;
    }

    public Integer getCccFlag() {
        return cccFlag;
    }

    public void setCccFlag(Integer cccFlag) {
        this.cccFlag = cccFlag;
    }

    public Integer getMinSale() {
        return minSale;
    }

    public void setMinSale(Integer minSale) {
        this.minSale = minSale;
    }

    public Integer getMinPurchase() {
        return minPurchase;
    }

    public void setMinPurchase(Integer minPurchase) {
        this.minPurchase = minPurchase;
    }

    public Long getMaxSaleVolume() {
        return maxSaleVolume;
    }

    public void setMaxSaleVolume(Long maxSaleVolume) {
        this.maxSaleVolume = maxSaleVolume;
    }

    public String getPackSpecification() {
        return packSpecification;
    }

    public void setPackSpecification(String packSpecification) {
        this.packSpecification = packSpecification;
    }

    public String getOtherPartId() {
        return otherPartId;
    }

    public void setOtherPartId(String otherPartId) {
        this.otherPartId = otherPartId;
    }

    public Integer getIsSecurity() {
        return isSecurity;
    }

    public void setIsSecurity(Integer isSecurity) {
        this.isSecurity = isSecurity;
    }

    public Integer getIsProtocolPack() {
        return isProtocolPack;
    }

    public void setIsProtocolPack(Integer isProtocolPack) {
        this.isProtocolPack = isProtocolPack;
    }

    public Integer getIsMagBatch() {
        return isMagBatch;
    }

    public void setIsMagBatch(Integer isMagBatch) {
        this.isMagBatch = isMagBatch;
    }

    public Integer getPartCategory() {
        return partCategory;
    }

    public void setPartCategory(Integer partCategory) {
        this.partCategory = partCategory;
    }

    public Integer getIsPartDisable() {
        return isPartDisable;
    }

    public void setIsPartDisable(Integer isPartDisable) {
        this.isPartDisable = isPartDisable;
    }

    public Date getPartDisableDate() {
        return partDisableDate;
    }

    public void setPartDisableDate(Date partDisableDate) {
        this.partDisableDate = partDisableDate;
    }

    public String getModelCode() {
        return modelCode;
    }

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }
}