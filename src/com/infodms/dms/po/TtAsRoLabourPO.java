/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-04-12 10:26:22
* CreateBy   : KFQ
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsRoLabourPO extends PO{

	private String modelLabourCode;
	private Date updateDate;
	private Long createBy;
	private String remark;
	private String technician;
	private String activityCode;
	private String manageSortCode;
	private Long updateBy;
	private Double labourAmount;
	private String wrLabourname;
	private Long id;
	private String troubleDesc;
	private String workerTypeCode;
	private Integer needlessRepair;
	private Double discount;
	private Integer preCheck;
	private Integer interReturn;
	private String foreLevel;
	private String isSel;
	private String labourName;
	private Double assignLabourHour;
	private String localLabourName;
	private Integer assignTag;
	private String repairTypeCode;
	private String wrLabourcode;
	private Long isFore;
	private String localLabourCode;
	private String labourCode;
	private Double stdLabourHour;
	private String packageCode;
	private Float labourPrice;
	private Integer payType;
	private String chargePartitionCode;
	private Long roId;
	private Integer consignExterior;
	private Integer isClaim;
	private Date createDate;
	private String troubleCause;
	private Long malFunction;
	private Long qudDamage;
	private String labCamcode;
	
	public Long getMalFunction() {
		return malFunction;
	}

	public void setMalFunction(Long malFunction) {
		this.malFunction = malFunction;
	}

	public Long getQudDamage() {
		return qudDamage;
	}

	public void setQudDamage(Long qudDamage) {
		this.qudDamage = qudDamage;
	}

	public void setModelLabourCode(String modelLabourCode){
		this.modelLabourCode=modelLabourCode;
	}

	public String getModelLabourCode(){
		return this.modelLabourCode;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setTechnician(String technician){
		this.technician=technician;
	}

	public String getTechnician(){
		return this.technician;
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

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setLabourAmount(Double labourAmount){
		this.labourAmount=labourAmount;
	}

	public Double getLabourAmount(){
		return this.labourAmount;
	}

	public void setWrLabourname(String wrLabourname){
		this.wrLabourname=wrLabourname;
	}

	public String getWrLabourname(){
		return this.wrLabourname;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setTroubleDesc(String troubleDesc){
		this.troubleDesc=troubleDesc;
	}

	public String getTroubleDesc(){
		return this.troubleDesc;
	}

	public void setWorkerTypeCode(String workerTypeCode){
		this.workerTypeCode=workerTypeCode;
	}

	public String getWorkerTypeCode(){
		return this.workerTypeCode;
	}

	public void setNeedlessRepair(Integer needlessRepair){
		this.needlessRepair=needlessRepair;
	}

	public Integer getNeedlessRepair(){
		return this.needlessRepair;
	}

	public void setDiscount(Double discount){
		this.discount=discount;
	}

	public Double getDiscount(){
		return this.discount;
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

	public void setForeLevel(String foreLevel){
		this.foreLevel=foreLevel;
	}

	public String getForeLevel(){
		return this.foreLevel;
	}

	public void setIsSel(String isSel){
		this.isSel=isSel;
	}

	public String getIsSel(){
		return this.isSel;
	}

	public void setLabourName(String labourName){
		this.labourName=labourName;
	}

	public String getLabourName(){
		return this.labourName;
	}

	public void setAssignLabourHour(Double assignLabourHour){
		this.assignLabourHour=assignLabourHour;
	}

	public Double getAssignLabourHour(){
		return this.assignLabourHour;
	}

	public void setLocalLabourName(String localLabourName){
		this.localLabourName=localLabourName;
	}

	public String getLocalLabourName(){
		return this.localLabourName;
	}

	public void setAssignTag(Integer assignTag){
		this.assignTag=assignTag;
	}

	public Integer getAssignTag(){
		return this.assignTag;
	}

	public void setRepairTypeCode(String repairTypeCode){
		this.repairTypeCode=repairTypeCode;
	}

	public String getRepairTypeCode(){
		return this.repairTypeCode;
	}

	public void setWrLabourcode(String wrLabourcode){
		this.wrLabourcode=wrLabourcode;
	}

	public String getWrLabourcode(){
		return this.wrLabourcode;
	}

	public void setIsFore(Long isFore){
		this.isFore=isFore;
	}

	public Long getIsFore(){
		return this.isFore;
	}

	public void setLocalLabourCode(String localLabourCode){
		this.localLabourCode=localLabourCode;
	}

	public String getLocalLabourCode(){
		return this.localLabourCode;
	}

	public void setLabourCode(String labourCode){
		this.labourCode=labourCode;
	}

	public String getLabourCode(){
		return this.labourCode;
	}

	public void setStdLabourHour(Double stdLabourHour){
		this.stdLabourHour=stdLabourHour;
	}

	public Double getStdLabourHour(){
		return this.stdLabourHour;
	}

	public void setPackageCode(String packageCode){
		this.packageCode=packageCode;
	}

	public String getPackageCode(){
		return this.packageCode;
	}

	public void setLabourPrice(Float labourPrice){
		this.labourPrice=labourPrice;
	}

	public Float getLabourPrice(){
		return this.labourPrice;
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

	public void setTroubleCause(String troubleCause){
		this.troubleCause=troubleCause;
	}

	public String getTroubleCause(){
		return this.troubleCause;
	}

	public String getLabCamcode() {
		return labCamcode;
	}

	public void setLabCamcode(String labCamcode) {
		this.labCamcode = labCamcode;
	}

}