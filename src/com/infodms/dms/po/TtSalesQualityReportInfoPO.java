/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-03-26 12:55:05
* CreateBy   : yuewei
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtSalesQualityReportInfoPO extends PO{

	private Long qualityId;
	private String oilLeaRemark;
	private String customerComplainNeed;
	private String theme;
	private String firstProblemCode;
	private String userAddr;
	private String isKeepFit;
	private Long mileage;
	private String isAduit;
	private String engineNo;
	private String faultDate;
	private String wayStatus;
	private String carStatusByProblem;
	private Integer inTimeScore;
	private String problemCode;
	private String dealerCode;
	private Integer addQualityScore;
	private Integer addNumberScore;
	private String carClass;
	private String contactType;
	private Integer problemTheSameRemark;
	private Integer engineSpeed;
	private String isFit;
	private String carUseType;
	private Integer runSpeed;
	private Integer countScore;
	private String serviceDate;
	private String problemProperties;
	private Integer verifyStatus;
	private Long dealerId;
	private String vin;
	private String reportName;
	private String oilLeak;
	private String buyCarDate;
	private String carType;
	private String carNo;
	private String weatherStatus;
	private String productDate;
	private String partChangeStatus;
	private String problemTheSame;
	private String claimNo;
	private String applyNews;
	private String userPhone;
	private String reportNameAdvice;
	private String checkStepFit;
	private String productQuality;
	private String firstProblemSupplierCode;
	private String listGroup;
	private String problemReason;
	private String dealerName;
	private String userName;
	private String carSpend;
	private String problemCodeRemark;
	private String randomId;
	private String reportDate;
	private String carStatusRemark;
	private String firstProblemName;
	private String dealAdvice;
	private String againAuditAdvice;
	private String technicalAuditAdvice;
	private String isend;
	private String reportDateBtn;
	
	private String auditName;
	private String auditDate;

	
	public String getAuditName() {
		return auditName;
	}

	public void setAuditName(String auditName) {
		this.auditName = auditName;
	}

	public String getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(String auditDate) {
		this.auditDate = auditDate;
	}

	public TtSalesQualityReportInfoPO(Long qualityId) {
		this.qualityId = qualityId;
	}

	public TtSalesQualityReportInfoPO() {
	}

	public void setQualityId(Long qualityId){
		this.qualityId=qualityId;
	}

	public Long getQualityId(){
		return this.qualityId;
	}

	public void setOilLeaRemark(String oilLeaRemark){
		this.oilLeaRemark=oilLeaRemark;
	}

	public String getOilLeaRemark(){
		return this.oilLeaRemark;
	}

	public void setCustomerComplainNeed(String customerComplainNeed){
		this.customerComplainNeed=customerComplainNeed;
	}

	public String getCustomerComplainNeed(){
		return this.customerComplainNeed;
	}

	public void setTheme(String theme){
		this.theme=theme;
	}

	public String getTheme(){
		return this.theme;
	}

	public void setFirstProblemCode(String firstProblemCode){
		this.firstProblemCode=firstProblemCode;
	}

	public String getFirstProblemCode(){
		return this.firstProblemCode;
	}

	public void setUserAddr(String userAddr){
		this.userAddr=userAddr;
	}

	public String getUserAddr(){
		return this.userAddr;
	}

	public void setIsKeepFit(String isKeepFit){
		this.isKeepFit=isKeepFit;
	}

	public String getIsKeepFit(){
		return this.isKeepFit;
	}

	public void setMileage(Long mileage){
		this.mileage=mileage;
	}

	public Long getMileage(){
		return this.mileage;
	}

	public void setIsAduit(String isAduit){
		this.isAduit=isAduit;
	}

	public String getIsAduit(){
		return this.isAduit;
	}

	public void setEngineNo(String engineNo){
		this.engineNo=engineNo;
	}

	public String getEngineNo(){
		return this.engineNo;
	}

	public void setFaultDate(String faultDate){
		this.faultDate=faultDate;
	}

	public String getFaultDate(){
		return this.faultDate;
	}

	public void setWayStatus(String wayStatus){
		this.wayStatus=wayStatus;
	}

	public String getWayStatus(){
		return this.wayStatus;
	}

	public void setCarStatusByProblem(String carStatusByProblem){
		this.carStatusByProblem=carStatusByProblem;
	}

	public String getCarStatusByProblem(){
		return this.carStatusByProblem;
	}

	public void setInTimeScore(Integer inTimeScore){
		this.inTimeScore=inTimeScore;
	}

	public Integer getInTimeScore(){
		return this.inTimeScore;
	}

	public void setProblemCode(String problemCode){
		this.problemCode=problemCode;
	}

	public String getProblemCode(){
		return this.problemCode;
	}

	public void setDealerCode(String dealerCode){
		this.dealerCode=dealerCode;
	}

	public String getDealerCode(){
		return this.dealerCode;
	}

	public void setAddQualityScore(Integer addQualityScore){
		this.addQualityScore=addQualityScore;
	}

	public Integer getAddQualityScore(){
		return this.addQualityScore;
	}

	public void setAddNumberScore(Integer addNumberScore){
		this.addNumberScore=addNumberScore;
	}

	public Integer getAddNumberScore(){
		return this.addNumberScore;
	}

	public void setCarClass(String carClass){
		this.carClass=carClass;
	}

	public String getCarClass(){
		return this.carClass;
	}

	public void setContactType(String contactType){
		this.contactType=contactType;
	}

	public String getContactType(){
		return this.contactType;
	}

	public void setProblemTheSameRemark(Integer problemTheSameRemark){
		this.problemTheSameRemark=problemTheSameRemark;
	}

	public Integer getProblemTheSameRemark(){
		return this.problemTheSameRemark;
	}

	public void setEngineSpeed(Integer engineSpeed){
		this.engineSpeed=engineSpeed;
	}

	public Integer getEngineSpeed(){
		return this.engineSpeed;
	}

	public void setIsFit(String isFit){
		this.isFit=isFit;
	}

	public String getIsFit(){
		return this.isFit;
	}

	public void setCarUseType(String carUseType){
		this.carUseType=carUseType;
	}

	public String getCarUseType(){
		return this.carUseType;
	}

	public void setRunSpeed(Integer runSpeed){
		this.runSpeed=runSpeed;
	}

	public Integer getRunSpeed(){
		return this.runSpeed;
	}

	public void setCountScore(Integer countScore){
		this.countScore=countScore;
	}

	public Integer getCountScore(){
		return this.countScore;
	}

	public void setServiceDate(String serviceDate){
		this.serviceDate=serviceDate;
	}

	public String getServiceDate(){
		return this.serviceDate;
	}

	public void setProblemProperties(String problemProperties){
		this.problemProperties=problemProperties;
	}

	public String getProblemProperties(){
		return this.problemProperties;
	}

	public void setVerifyStatus(Integer verifyStatus){
		this.verifyStatus=verifyStatus;
	}

	public Integer getVerifyStatus(){
		return this.verifyStatus;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setVin(String vin){
		this.vin=vin;
	}

	public String getVin(){
		return this.vin;
	}

	public void setReportName(String reportName){
		this.reportName=reportName;
	}

	public String getReportName(){
		return this.reportName;
	}

	public void setOilLeak(String oilLeak){
		this.oilLeak=oilLeak;
	}

	public String getOilLeak(){
		return this.oilLeak;
	}

	public void setBuyCarDate(String buyCarDate){
		this.buyCarDate=buyCarDate;
	}

	public String getBuyCarDate(){
		return this.buyCarDate;
	}

	public void setCarType(String carType){
		this.carType=carType;
	}

	public String getCarType(){
		return this.carType;
	}

	public void setCarNo(String carNo){
		this.carNo=carNo;
	}

	public String getCarNo(){
		return this.carNo;
	}

	public void setWeatherStatus(String weatherStatus){
		this.weatherStatus=weatherStatus;
	}

	public String getWeatherStatus(){
		return this.weatherStatus;
	}

	public void setProductDate(String productDate){
		this.productDate=productDate;
	}

	public String getProductDate(){
		return this.productDate;
	}

	public void setPartChangeStatus(String partChangeStatus){
		this.partChangeStatus=partChangeStatus;
	}

	public String getPartChangeStatus(){
		return this.partChangeStatus;
	}

	public void setProblemTheSame(String problemTheSame){
		this.problemTheSame=problemTheSame;
	}

	public String getProblemTheSame(){
		return this.problemTheSame;
	}

	public void setClaimNo(String claimNo){
		this.claimNo=claimNo;
	}

	public String getClaimNo(){
		return this.claimNo;
	}

	public void setApplyNews(String applyNews){
		this.applyNews=applyNews;
	}

	public String getApplyNews(){
		return this.applyNews;
	}

	public void setUserPhone(String userPhone){
		this.userPhone=userPhone;
	}

	public String getUserPhone(){
		return this.userPhone;
	}

	public void setReportNameAdvice(String reportNameAdvice){
		this.reportNameAdvice=reportNameAdvice;
	}

	public String getReportNameAdvice(){
		return this.reportNameAdvice;
	}

	public void setCheckStepFit(String checkStepFit){
		this.checkStepFit=checkStepFit;
	}

	public String getCheckStepFit(){
		return this.checkStepFit;
	}

	public void setProductQuality(String productQuality){
		this.productQuality=productQuality;
	}

	public String getProductQuality(){
		return this.productQuality;
	}

	public void setFirstProblemSupplierCode(String firstProblemSupplierCode){
		this.firstProblemSupplierCode=firstProblemSupplierCode;
	}

	public String getFirstProblemSupplierCode(){
		return this.firstProblemSupplierCode;
	}

	public void setListGroup(String listGroup){
		this.listGroup=listGroup;
	}

	public String getListGroup(){
		return this.listGroup;
	}

	public void setProblemReason(String problemReason){
		this.problemReason=problemReason;
	}

	public String getProblemReason(){
		return this.problemReason;
	}

	public void setDealerName(String dealerName){
		this.dealerName=dealerName;
	}

	public String getDealerName(){
		return this.dealerName;
	}

	public void setUserName(String userName){
		this.userName=userName;
	}

	public String getUserName(){
		return this.userName;
	}

	public void setCarSpend(String carSpend){
		this.carSpend=carSpend;
	}

	public String getCarSpend(){
		return this.carSpend;
	}

	public void setProblemCodeRemark(String problemCodeRemark){
		this.problemCodeRemark=problemCodeRemark;
	}

	public String getProblemCodeRemark(){
		return this.problemCodeRemark;
	}

	public void setRandomId(String randomId){
		this.randomId=randomId;
	}

	public String getRandomId(){
		return this.randomId;
	}

	public void setReportDate(String reportDate){
		this.reportDate=reportDate;
	}

	public String getReportDate(){
		return this.reportDate;
	}

	public void setCarStatusRemark(String carStatusRemark){
		this.carStatusRemark=carStatusRemark;
	}

	public String getCarStatusRemark(){
		return this.carStatusRemark;
	}

	public void setFirstProblemName(String firstProblemName){
		this.firstProblemName=firstProblemName;
	}

	public String getFirstProblemName(){
		return this.firstProblemName;
	}

	public String getDealAdvice() {
		return dealAdvice;
	}

	public void setDealAdvice(String dealAdvice) {
		this.dealAdvice = dealAdvice;
	}

	public String getAgainAuditAdvice() {
		return againAuditAdvice;
	}

	public void setAgainAuditAdvice(String againAuditAdvice) {
		this.againAuditAdvice = againAuditAdvice;
	}

	public String getTechnicalAuditAdvice() {
		return technicalAuditAdvice;
	}

	public void setTechnicalAuditAdvice(String technicalAuditAdvice) {
		this.technicalAuditAdvice = technicalAuditAdvice;
	}

	public String getIsend() {
		return isend;
	}

	public void setIsend(String isend) {
		this.isend = isend;
	}

	public String getReportDateBtn() {
		return reportDateBtn;
	}

	public void setReportDateBtn(String reportDateBtn) {
		this.reportDateBtn = reportDateBtn;
	}

}