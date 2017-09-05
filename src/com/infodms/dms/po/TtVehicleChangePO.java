/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-10-19 17:32:03
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVehicleChangePO extends PO{
	private Integer applyType;
	private String applyRemark;
	private String vin;
	private Date updateDate;
	private Long createBy;
	private Long claimTacticsId;
	private Date applyDate;
	private Date checkDate;
	private Integer status;
	private Long applyPerson;
	private String applyData;
	private Integer isDel;
	private Long ctmId;
	private Date purchasedDate;
	private Long modelId;
	private Long updateBy;
	private String checkRemark;
	private String errorRoCode;
	private Double mileage;
	private Long seriesId;
	private Long id;
	private Integer freeTimes;
	private String engineNo;
	private String yieldly;
	private Long errorDealerId;
	private Double cMileage;
	private Long checkPerson;
	private String vehicleNo;
	private Double roMileage;
	private Integer ver;
	private Date createDate;
	private String cFreeTimes;
	private Integer roFreeTimes;
	private String roDealerCode;
	private Integer errorType;
	private Date cPurchaseDate;
	private String cCtmName;
	private String cCtmPhone;
	private String cCtmAddress;
	private Double claimMelieageNew;
	private Long claimMonthNew;
	private Long ruleListId;
	
	//zhumingwei 2012-07-16
	private String oldCtmName;
	public String getOldCtmName() {
		return oldCtmName;
	}

	public void setOldCtmName(String oldCtmName) {
		this.oldCtmName = oldCtmName;
	}

	public String getOldCtmPhone() {
		return oldCtmPhone;
	}

	public void setOldCtmPhone(String oldCtmPhone) {
		this.oldCtmPhone = oldCtmPhone;
	}

	public String getOldCtmAddress() {
		return oldCtmAddress;
	}

	public void setOldCtmAddress(String oldCtmAddress) {
		this.oldCtmAddress = oldCtmAddress;
	}

	private String oldCtmPhone;
	private String oldCtmAddress;
	
	
	public Long getRuleListId() {
		return ruleListId;
	}

	public void setRuleListId(Long ruleListId) {
		this.ruleListId = ruleListId;
	}

	public Double getClaimMelieageNew() {
		return claimMelieageNew;
	}

	public void setClaimMelieageNew(Double claimMelieageNew) {
		this.claimMelieageNew = claimMelieageNew;
	}

	public Long getClaimMonthNew() {
		return claimMonthNew;
	}

	public void setClaimMonthNew(Long claimMonthNew) {
		this.claimMonthNew = claimMonthNew;
	}

	public Date getcPurchaseDate() {
		return cPurchaseDate;
	}

	public void setcPurchaseDate(Date cPurchaseDate) {
		this.cPurchaseDate = cPurchaseDate;
	}

	public String getcCtmName() {
		return cCtmName;
	}

	public void setcCtmName(String cCtmName) {
		this.cCtmName = cCtmName;
	}

	public String getcCtmPhone() {
		return cCtmPhone;
	}

	public void setcCtmPhone(String cCtmPhone) {
		this.cCtmPhone = cCtmPhone;
	}

	public String getcCtmAddress() {
		return cCtmAddress;
	}

	public void setcCtmAddress(String cCtmAddress) {
		this.cCtmAddress = cCtmAddress;
	}

	public void setApplyType(Integer applyType){
		this.applyType=applyType;
	}
	
	public Integer getApplyType(){
		return this.applyType;
	}
	
	public void setApplyRemark(String applyRemark){
		this.applyRemark=applyRemark;
	}
	
	public String getApplyRemark(){
		return this.applyRemark;
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
	
	public void setApplyDate(Date applyDate){
		this.applyDate=applyDate;
	}
	
	public Date getApplyDate(){
		return this.applyDate;
	}
	
	public void setCheckDate(Date checkDate){
		this.checkDate=checkDate;
	}
	
	public Date getCheckDate(){
		return this.checkDate;
	}
	
	public void setStatus(Integer status){
		this.status=status;
	}
	
	public Integer getStatus(){
		return this.status;
	}
	
	public void setApplyPerson(Long applyPerson){
		this.applyPerson=applyPerson;
	}
	
	public Long getApplyPerson(){
		return this.applyPerson;
	}
	
	public void setApplyData(String applyData){
		this.applyData=applyData;
	}
	
	public String getApplyData(){
		return this.applyData;
	}
	
	public void setIsDel(Integer isDel){
		this.isDel=isDel;
	}
	
	public Integer getIsDel(){
		return this.isDel;
	}
	
	public void setCtmId(Long ctmId){
		this.ctmId=ctmId;
	}
	
	public Long getCtmId(){
		return this.ctmId;
	}
	
	public void setPurchasedDate(Date purchasedDate){
		this.purchasedDate=purchasedDate;
	}
	
	public Date getPurchasedDate(){
		return this.purchasedDate;
	}
	
	public void setModelId(Long modelId){
		this.modelId=modelId;
	}
	
	public Long getModelId(){
		return this.modelId;
	}
	
	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}
	
	public Long getUpdateBy(){
		return this.updateBy;
	}
	
	public void setCheckRemark(String checkRemark){
		this.checkRemark=checkRemark;
	}
	
	public String getCheckRemark(){
		return this.checkRemark;
	}
	
	public void setErrorRoCode(String errorRoCode){
		this.errorRoCode=errorRoCode;
	}
	
	public String getErrorRoCode(){
		return this.errorRoCode;
	}
	
	public void setMileage(Double mileage){
		this.mileage=mileage;
	}
	
	public Double getMileage(){
		return this.mileage;
	}
	
	public void setSeriesId(Long seriesId){
		this.seriesId=seriesId;
	}
	
	public Long getSeriesId(){
		return this.seriesId;
	}
	
	public void setId(Long id){
		this.id=id;
	}
	
	public Long getId(){
		return this.id;
	}
	
	public void setFreeTimes(Integer freeTimes){
		this.freeTimes=freeTimes;
	}
	
	public Integer getFreeTimes(){
		return this.freeTimes;
	}
	
	public void setEngineNo(String engineNo){
		this.engineNo=engineNo;
	}
	
	public String getEngineNo(){
		return this.engineNo;
	}
	
	public void setYieldly(String yieldly){
		this.yieldly=yieldly;
	}
	
	public String getYieldly(){
		return this.yieldly;
	}
	
	public void setErrorDealerId(Long errorDealerId){
		this.errorDealerId=errorDealerId;
	}
	
	public Long getErrorDealerId(){
		return this.errorDealerId;
	}
	
	public void setCMileage(Double cMileage){
		this.cMileage=cMileage;
	}
	
	public Double getCMileage(){
		return this.cMileage;
	}
	
	public void setCheckPerson(Long checkPerson){
		this.checkPerson=checkPerson;
	}
	
	public Long getCheckPerson(){
		return this.checkPerson;
	}
	
	public void setVehicleNo(String vehicleNo){
		this.vehicleNo=vehicleNo;
	}
	
	public String getVehicleNo(){
		return this.vehicleNo;
	}
	
	public void setRoMileage(Double roMileage){
		this.roMileage=roMileage;
	}
	
	public Double getRoMileage(){
		return this.roMileage;
	}
	
	public void setVer(Integer ver){
		this.ver=ver;
	}
	
	public Integer getVer(){
		return this.ver;
	}
	
	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}
	
	public Date getCreateDate(){
		return this.createDate;
	}
	
	public String getCFreeTimes() {
		return cFreeTimes;
	}

	public void setCFreeTimes(String freeTimes) {
		cFreeTimes = freeTimes;
	}

	public void setRoFreeTimes(Integer roFreeTimes){
		this.roFreeTimes=roFreeTimes;
	}
	
	public Integer getRoFreeTimes(){
		return this.roFreeTimes;
	}
	
	public void setRoDealerCode(String roDealerCode){
		this.roDealerCode=roDealerCode;
	}
	
	public String getRoDealerCode(){
		return this.roDealerCode;
	}
	
	public void setErrorType(Integer errorType){
		this.errorType=errorType;
	}
	
	public Integer getErrorType(){
		return this.errorType;
	}
}