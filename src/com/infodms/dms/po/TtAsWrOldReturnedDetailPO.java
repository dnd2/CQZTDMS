/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-07-18 09:33:32
* CreateBy   : KFQ
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrOldReturnedDetailPO extends PO{

	private Double price;
	private String vin;
	private Date updateDate;
	private String partName;
	private Long isUpload;
	private String producerCode;
	private String remark;
	private Integer nReturnAmount;
	private Long createBy;
	private Long dealerReturnId;
	private String barcodeNo;
	private String boxNo;
	private Integer deductRemark;
	private Long updateBy;
	private Long partId;
	private Long id;
	private String procFactory;
	private Double deductiblePrice;
	private Integer stockAmount;
	private String claimNo;
	private Integer isOut;
	private String producerName;
	private String warehouseRegion;
	private Long isStorage;
	private String partCode;
	private Long returnId;
	private String isCliam;
	private Long isSign;
	private Long deductibleReasonCode;
	private Long claimId;
	private Date createDate;
	private Integer returnAmount;
	private Integer signAmount;
	private Integer isScan;
	private Integer isMainCode;
	private String mainPartCode;
	private Integer isImport;
	private String localWarHouse;
	private String localWarShel;
	private String localWarLayer;
	private String deductPartCode;
	private Integer isInHouse;
	private Date inDate;
	private Integer executiveDirectorSta;
	private Date executiveDirectorDate;
	private String executiveDirectorRam;
	private String otherRemark;
	private Integer qhjFlag;
	private String supplierRemark;//修改供应商备注
	
	
	
	
	public String getSupplierRemark() {
		return supplierRemark;
	}

	public void setSupplierRemark(String supplierRemark) {
		this.supplierRemark = supplierRemark;
	}

	public Integer getQhjFlag() {
		return qhjFlag;
	}

	public void setQhjFlag(Integer qhjFlag) {
		this.qhjFlag = qhjFlag;
	}

	public Integer getKcdbFlag() {
		return kcdbFlag;
	}

	public void setKcdbFlag(Integer kcdbFlag) {
		this.kcdbFlag = kcdbFlag;
	}

	private Integer kcdbFlag;
	public String getOtherRemark() {
		return otherRemark;
	}

	public void setOtherRemark(String otherRemark) {
		this.otherRemark = otherRemark;
	}

	public Integer getExecutiveDirectorSta() {
		return executiveDirectorSta;
	}

	public void setExecutiveDirectorSta(Integer executiveDirectorSta) {
		this.executiveDirectorSta = executiveDirectorSta;
	}

	public Date getExecutiveDirectorDate() {
		return executiveDirectorDate;
	}

	public void setExecutiveDirectorDate(Date executiveDirectorDate) {
		this.executiveDirectorDate = executiveDirectorDate;
	}

	public String getExecutiveDirectorRam() {
		return executiveDirectorRam;
	}

	public void setExecutiveDirectorRam(String executiveDirectorRam) {
		this.executiveDirectorRam = executiveDirectorRam;
	}

	public Date getInDate() {
		return inDate;
	}

	public void setInDate(Date inDate) {
		this.inDate = inDate;
	}

	public Integer getnReturnAmount() {
		return nReturnAmount;
	}

	public void setnReturnAmount(Integer nReturnAmount) {
		this.nReturnAmount = nReturnAmount;
	}

	public Integer getIsInHouse() {
		return isInHouse;
	}

	public void setIsInHouse(Integer isInHouse) {
		this.isInHouse = isInHouse;
	}

	public String getDeductPartCode() {
		return deductPartCode;
	}

	public void setDeductPartCode(String deductPartCode) {
		this.deductPartCode = deductPartCode;
	}

	public String getLocalWarShel() {
		return localWarShel;
	}

	public void setLocalWarShel(String localWarShel) {
		this.localWarShel = localWarShel;
	}

	public String getLocalWarLayer() {
		return localWarLayer;
	}

	public void setLocalWarLayer(String localWarLayer) {
		this.localWarLayer = localWarLayer;
	}

	public String getLocalWarHouse() {
		return localWarHouse;
	}

	public void setLocalWarHouse(String localWarHouse) {
		this.localWarHouse = localWarHouse;
	}

	public Integer getIsMainCode() {
		return isMainCode;
	}

	public void setIsMainCode(Integer isMainCode) {
		this.isMainCode = isMainCode;
	}

	public String getMainPartCode() {
		return mainPartCode;
	}

	public void setMainPartCode(String mainPartCode) {
		this.mainPartCode = mainPartCode;
	}

	public Integer getIsScan() {
		return isScan;
	}

	public void setIsScan(Integer isScan) {
		this.isScan = isScan;
	}

	public void setPrice(Double price){
		this.price=price;
	}

	public Double getPrice(){
		return this.price;
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

	public void setPartName(String partName){
		this.partName=partName;
	}

	public String getPartName(){
		return this.partName;
	}

	public void setIsUpload(Long isUpload){
		this.isUpload=isUpload;
	}

	public Long getIsUpload(){
		return this.isUpload;
	}

	public void setProducerCode(String producerCode){
		this.producerCode=producerCode;
	}

	public String getProducerCode(){
		return this.producerCode;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setNReturnAmount(Integer nReturnAmount){
		this.nReturnAmount=nReturnAmount;
	}

	public Integer getNReturnAmount(){
		return this.nReturnAmount;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setDealerReturnId(Long dealerReturnId){
		this.dealerReturnId=dealerReturnId;
	}

	public Long getDealerReturnId(){
		return this.dealerReturnId;
	}

	public void setBarcodeNo(String barcodeNo){
		this.barcodeNo=barcodeNo;
	}

	public String getBarcodeNo(){
		return this.barcodeNo;
	}

	public void setBoxNo(String boxNo){
		this.boxNo=boxNo;
	}

	public String getBoxNo(){
		return this.boxNo;
	}

	public void setDeductRemark(Integer deductRemark){
		this.deductRemark=deductRemark;
	}

	public Integer getDeductRemark(){
		return this.deductRemark;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setPartId(Long partId){
		this.partId=partId;
	}

	public Long getPartId(){
		return this.partId;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setProcFactory(String procFactory){
		this.procFactory=procFactory;
	}

	public String getProcFactory(){
		return this.procFactory;
	}

	public void setDeductiblePrice(Double deductiblePrice){
		this.deductiblePrice=deductiblePrice;
	}

	public Double getDeductiblePrice(){
		return this.deductiblePrice;
	}

	public void setStockAmount(Integer stockAmount){
		this.stockAmount=stockAmount;
	}

	public Integer getStockAmount(){
		return this.stockAmount;
	}

	public void setClaimNo(String claimNo){
		this.claimNo=claimNo;
	}

	public String getClaimNo(){
		return this.claimNo;
	}

	public void setIsOut(Integer isOut){
		this.isOut=isOut;
	}

	public Integer getIsOut(){
		return this.isOut;
	}

	public void setProducerName(String producerName){
		this.producerName=producerName;
	}

	public String getProducerName(){
		return this.producerName;
	}

	public void setWarehouseRegion(String warehouseRegion){
		this.warehouseRegion=warehouseRegion;
	}

	public String getWarehouseRegion(){
		return this.warehouseRegion;
	}

	public void setIsStorage(Long isStorage){
		this.isStorage=isStorage;
	}

	public Long getIsStorage(){
		return this.isStorage;
	}

	public void setPartCode(String partCode){
		this.partCode=partCode;
	}

	public String getPartCode(){
		return this.partCode;
	}

	public void setReturnId(Long returnId){
		this.returnId=returnId;
	}

	public Long getReturnId(){
		return this.returnId;
	}

	public void setIsCliam(String isCliam){
		this.isCliam=isCliam;
	}

	public String getIsCliam(){
		return this.isCliam;
	}

	public void setIsSign(Long isSign){
		this.isSign=isSign;
	}

	public Long getIsSign(){
		return this.isSign;
	}

	public void setDeductibleReasonCode(Long deductibleReasonCode){
		this.deductibleReasonCode=deductibleReasonCode;
	}

	public Long getDeductibleReasonCode(){
		return this.deductibleReasonCode;
	}

	public void setClaimId(Long claimId){
		this.claimId=claimId;
	}

	public Long getClaimId(){
		return this.claimId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setReturnAmount(Integer returnAmount){
		this.returnAmount=returnAmount;
	}

	public Integer getReturnAmount(){
		return this.returnAmount;
	}

	public void setSignAmount(Integer signAmount){
		this.signAmount=signAmount;
	}

	public Integer getSignAmount(){
		return this.signAmount;
	}

	public Integer getIsImport() {
		return isImport;
	}

	public void setIsImport(Integer isImport) {
		this.isImport = isImport;
	}

}