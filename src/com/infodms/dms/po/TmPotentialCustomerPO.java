/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-09-26 17:17:07
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.math.BigDecimal;
import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmPotentialCustomerPO extends PO{
	private Long id;
	private Integer isReported;
	private String customerName;
	private String hobby;
	private Integer educationLevel;
	private Integer organTypeCode;
	private Integer isPersonDriveCar;
	private String remark;
	private Integer reportStatus;
	private Integer mediaType;
	private Long soldBy;
	private Integer intentLevel;
	private Long updateBy;
	private Date submitTime;
	private String customerNo;
	private Integer choiceReason;
	private String organType;
	private Integer initLevel;
	private Integer familyIncome;
	private Date downTimestamp;
	private String fax;
	private Integer buyPurpose;
	private String buyReason;
	private Date consultantTime;
	private String zipCode;
	private String campaignCode;
	private Integer dKey;
	private String recommendEmpName;
	private String countryCode;
	private String positionName;
	private String contactorMobile;
	private Integer ctCode;
	private String industrySecond;
	private BigDecimal ver;
	private Long failConsultant;
	private Integer isDirect;
	private String industryFirst;
	private Date createDate;
	private String city;
	private String dcrcService;
	private String eMail;
	private Integer bestContactType;
	private Double gatheredSum;
	private Date updateDate;
	private Integer isUpload;
	private Double conPayedSum;
	private Long createBy;
	private Date foundDate;
	private String contactorPhone;
	private String reportAbortReason;
	private Double usableAmount;
	private Integer customerType;
	private String entityCode;
	private Integer bestContactTime;
	private Long delayConsultant;
	private Integer cusSource;
	private Integer vocationType;
	private Integer isWholesaler;
	private Integer hasDriverLicense;
	private Double unWriteoffSum;
	private String sodCustomerId;
	private Date birthday;
	private String district;
	private Long intentId;
	private Integer ageStage;
	private String address;
	private String reportAuditingRemark;
	private Integer customerStatus;
	private Integer isFirstBuy;
	private Integer isCrpvip;
	private Double orderPayedSum;
	private String modifyReason;
	private String largeCustomerNo;
	private Integer gender;
	private String province;
	private Long ownedBy;
	private Integer ownerMarriage;
	private String reportRemark;
	private String certificateNo;
	private Date reportDatetime;
	private Long isTryDrive; //是否试驾
	private Long isFirstCome; //是否首次来店
	private Integer customerNum; //来店人数
	private Long groupId;  //拟购车系
	private String colorName; //选择颜色
	private Date comeTime;
	private Date leaveTime;
	private Integer stayMinute;
	private Long dealerId;
	public Long getDealerId() {
		return dealerId;
	}

	public void setDealerId(Long dealerId) {
		this.dealerId = dealerId;
	}

	public Date getComeTime() {
		return comeTime;
	}

	public void setComeTime(Date comeTime) {
		this.comeTime = comeTime;
	}

	public Date getLeaveTime() {
		return leaveTime;
	}

	public void setLeaveTime(Date leaveTime) {
		this.leaveTime = leaveTime;
	}

	public Integer getStayMinute() {
		return stayMinute;
	}

	public void setStayMinute(Integer stayMinute) {
		this.stayMinute = stayMinute;
	}

	public Long getIsTryDrive() {
		return isTryDrive;
	}

	public void setIsTryDrive(Long isTryDrive) {
		this.isTryDrive = isTryDrive;
	}

	public Long getIsFirstCome() {
		return isFirstCome;
	}

	public void setIsFirstCome(Long isFirstCome) {
		this.isFirstCome = isFirstCome;
	}

	public Integer getCustomerNum() {
		return customerNum;
	}

	public void setCustomerNum(Integer customerNum) {
		this.customerNum = customerNum;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setIsReported(Integer isReported){
		this.isReported=isReported;
	}

	public Integer getIsReported(){
		return this.isReported;
	}

	public void setCustomerName(String customerName){
		this.customerName=customerName;
	}

	public String getCustomerName(){
		return this.customerName;
	}

	public void setHobby(String hobby){
		this.hobby=hobby;
	}

	public String getHobby(){
		return this.hobby;
	}

	public void setEducationLevel(Integer educationLevel){
		this.educationLevel=educationLevel;
	}

	public Integer getEducationLevel(){
		return this.educationLevel;
	}

	public void setOrganTypeCode(Integer organTypeCode){
		this.organTypeCode=organTypeCode;
	}

	public Integer getOrganTypeCode(){
		return this.organTypeCode;
	}

	public void setIsPersonDriveCar(Integer isPersonDriveCar){
		this.isPersonDriveCar=isPersonDriveCar;
	}

	public Integer getIsPersonDriveCar(){
		return this.isPersonDriveCar;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setReportStatus(Integer reportStatus){
		this.reportStatus=reportStatus;
	}

	public Integer getReportStatus(){
		return this.reportStatus;
	}

	public void setMediaType(Integer mediaType){
		this.mediaType=mediaType;
	}

	public Integer getMediaType(){
		return this.mediaType;
	}

	public void setSoldBy(Long soldBy){
		this.soldBy=soldBy;
	}

	public Long getSoldBy(){
		return this.soldBy;
	}

	public void setIntentLevel(Integer intentLevel){
		this.intentLevel=intentLevel;
	}

	public Integer getIntentLevel(){
		return this.intentLevel;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setSubmitTime(Date submitTime){
		this.submitTime=submitTime;
	}

	public Date getSubmitTime(){
		return this.submitTime;
	}

	public void setCustomerNo(String customerNo){
		this.customerNo=customerNo;
	}

	public String getCustomerNo(){
		return this.customerNo;
	}

	public void setChoiceReason(Integer choiceReason){
		this.choiceReason=choiceReason;
	}

	public Integer getChoiceReason(){
		return this.choiceReason;
	}

	public void setOrganType(String organType){
		this.organType=organType;
	}

	public String getOrganType(){
		return this.organType;
	}

	public void setInitLevel(Integer initLevel){
		this.initLevel=initLevel;
	}

	public Integer getInitLevel(){
		return this.initLevel;
	}

	public void setFamilyIncome(Integer familyIncome){
		this.familyIncome=familyIncome;
	}

	public Integer getFamilyIncome(){
		return this.familyIncome;
	}

	public void setDownTimestamp(Date downTimestamp){
		this.downTimestamp=downTimestamp;
	}

	public Date getDownTimestamp(){
		return this.downTimestamp;
	}

	public void setFax(String fax){
		this.fax=fax;
	}

	public String getFax(){
		return this.fax;
	}

	public void setBuyPurpose(Integer buyPurpose){
		this.buyPurpose=buyPurpose;
	}

	public Integer getBuyPurpose(){
		return this.buyPurpose;
	}

	public void setBuyReason(String buyReason){
		this.buyReason=buyReason;
	}

	public String getBuyReason(){
		return this.buyReason;
	}

	public void setConsultantTime(Date consultantTime){
		this.consultantTime=consultantTime;
	}

	public Date getConsultantTime(){
		return this.consultantTime;
	}

	public void setZipCode(String zipCode){
		this.zipCode=zipCode;
	}

	public String getZipCode(){
		return this.zipCode;
	}

	public void setCampaignCode(String campaignCode){
		this.campaignCode=campaignCode;
	}

	public String getCampaignCode(){
		return this.campaignCode;
	}

	public void setDKey(Integer dKey){
		this.dKey=dKey;
	}

	public Integer getDKey(){
		return this.dKey;
	}

	public void setRecommendEmpName(String recommendEmpName){
		this.recommendEmpName=recommendEmpName;
	}

	public String getRecommendEmpName(){
		return this.recommendEmpName;
	}

	public void setCountryCode(String countryCode){
		this.countryCode=countryCode;
	}

	public String getCountryCode(){
		return this.countryCode;
	}

	public void setPositionName(String positionName){
		this.positionName=positionName;
	}

	public String getPositionName(){
		return this.positionName;
	}

	public void setContactorMobile(String contactorMobile){
		this.contactorMobile=contactorMobile;
	}

	public String getContactorMobile(){
		return this.contactorMobile;
	}

	public void setCtCode(Integer ctCode){
		this.ctCode=ctCode;
	}

	public Integer getCtCode(){
		return this.ctCode;
	}

	public void setIndustrySecond(String industrySecond){
		this.industrySecond=industrySecond;
	}

	public String getIndustrySecond(){
		return this.industrySecond;
	}

	public void setVer(BigDecimal ver){
		this.ver=ver;
	}

	public BigDecimal getVer(){
		return this.ver;
	}

	public void setFailConsultant(Long failConsultant){
		this.failConsultant=failConsultant;
	}

	public Long getFailConsultant(){
		return this.failConsultant;
	}

	public void setIsDirect(Integer isDirect){
		this.isDirect=isDirect;
	}

	public Integer getIsDirect(){
		return this.isDirect;
	}

	public void setIndustryFirst(String industryFirst){
		this.industryFirst=industryFirst;
	}

	public String getIndustryFirst(){
		return this.industryFirst;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setCity(String city){
		this.city=city;
	}

	public String getCity(){
		return this.city;
	}

	public void setDcrcService(String dcrcService){
		this.dcrcService=dcrcService;
	}

	public String getDcrcService(){
		return this.dcrcService;
	}

	public void setEMail(String eMail){
		this.eMail=eMail;
	}

	public String getEMail(){
		return this.eMail;
	}

	public void setBestContactType(Integer bestContactType){
		this.bestContactType=bestContactType;
	}

	public Integer getBestContactType(){
		return this.bestContactType;
	}

	public void setGatheredSum(Double gatheredSum){
		this.gatheredSum=gatheredSum;
	}

	public Double getGatheredSum(){
		return this.gatheredSum;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setIsUpload(Integer isUpload){
		this.isUpload=isUpload;
	}

	public Integer getIsUpload(){
		return this.isUpload;
	}

	public void setConPayedSum(Double conPayedSum){
		this.conPayedSum=conPayedSum;
	}

	public Double getConPayedSum(){
		return this.conPayedSum;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setFoundDate(Date foundDate){
		this.foundDate=foundDate;
	}

	public Date getFoundDate(){
		return this.foundDate;
	}

	public void setContactorPhone(String contactorPhone){
		this.contactorPhone=contactorPhone;
	}

	public String getContactorPhone(){
		return this.contactorPhone;
	}

	public void setReportAbortReason(String reportAbortReason){
		this.reportAbortReason=reportAbortReason;
	}

	public String getReportAbortReason(){
		return this.reportAbortReason;
	}

	public void setUsableAmount(Double usableAmount){
		this.usableAmount=usableAmount;
	}

	public Double getUsableAmount(){
		return this.usableAmount;
	}

	public void setCustomerType(Integer customerType){
		this.customerType=customerType;
	}

	public Integer getCustomerType(){
		return this.customerType;
	}

	public void setEntityCode(String entityCode){
		this.entityCode=entityCode;
	}

	public String getEntityCode(){
		return this.entityCode;
	}

	public void setBestContactTime(Integer bestContactTime){
		this.bestContactTime=bestContactTime;
	}

	public Integer getBestContactTime(){
		return this.bestContactTime;
	}

	public void setDelayConsultant(Long delayConsultant){
		this.delayConsultant=delayConsultant;
	}

	public Long getDelayConsultant(){
		return this.delayConsultant;
	}

	public void setCusSource(Integer cusSource){
		this.cusSource=cusSource;
	}

	public Integer getCusSource(){
		return this.cusSource;
	}

	public void setVocationType(Integer vocationType){
		this.vocationType=vocationType;
	}

	public Integer getVocationType(){
		return this.vocationType;
	}

	public void setIsWholesaler(Integer isWholesaler){
		this.isWholesaler=isWholesaler;
	}

	public Integer getIsWholesaler(){
		return this.isWholesaler;
	}

	public void setHasDriverLicense(Integer hasDriverLicense){
		this.hasDriverLicense=hasDriverLicense;
	}

	public Integer getHasDriverLicense(){
		return this.hasDriverLicense;
	}

	public void setUnWriteoffSum(Double unWriteoffSum){
		this.unWriteoffSum=unWriteoffSum;
	}

	public Double getUnWriteoffSum(){
		return this.unWriteoffSum;
	}

	public void setSodCustomerId(String sodCustomerId){
		this.sodCustomerId=sodCustomerId;
	}

	public String getSodCustomerId(){
		return this.sodCustomerId;
	}

	public void setBirthday(Date birthday){
		this.birthday=birthday;
	}

	public Date getBirthday(){
		return this.birthday;
	}

	public void setDistrict(String district){
		this.district=district;
	}

	public String getDistrict(){
		return this.district;
	}

	public void setIntentId(Long intentId){
		this.intentId=intentId;
	}

	public Long getIntentId(){
		return this.intentId;
	}

	public void setAgeStage(Integer ageStage){
		this.ageStage=ageStage;
	}

	public Integer getAgeStage(){
		return this.ageStage;
	}

	public void setAddress(String address){
		this.address=address;
	}

	public String getAddress(){
		return this.address;
	}

	public void setReportAuditingRemark(String reportAuditingRemark){
		this.reportAuditingRemark=reportAuditingRemark;
	}

	public String getReportAuditingRemark(){
		return this.reportAuditingRemark;
	}

	public void setCustomerStatus(Integer customerStatus){
		this.customerStatus=customerStatus;
	}

	public Integer getCustomerStatus(){
		return this.customerStatus;
	}

	public void setIsFirstBuy(Integer isFirstBuy){
		this.isFirstBuy=isFirstBuy;
	}

	public Integer getIsFirstBuy(){
		return this.isFirstBuy;
	}

	public void setIsCrpvip(Integer isCrpvip){
		this.isCrpvip=isCrpvip;
	}

	public Integer getIsCrpvip(){
		return this.isCrpvip;
	}

	public void setOrderPayedSum(Double orderPayedSum){
		this.orderPayedSum=orderPayedSum;
	}

	public Double getOrderPayedSum(){
		return this.orderPayedSum;
	}

	public void setModifyReason(String modifyReason){
		this.modifyReason=modifyReason;
	}

	public String getModifyReason(){
		return this.modifyReason;
	}

	public void setLargeCustomerNo(String largeCustomerNo){
		this.largeCustomerNo=largeCustomerNo;
	}

	public String getLargeCustomerNo(){
		return this.largeCustomerNo;
	}

	public void setGender(Integer gender){
		this.gender=gender;
	}

	public Integer getGender(){
		return this.gender;
	}

	public void setProvince(String province){
		this.province=province;
	}

	public String getProvince(){
		return this.province;
	}

	public void setOwnedBy(Long ownedBy){
		this.ownedBy=ownedBy;
	}

	public Long getOwnedBy(){
		return this.ownedBy;
	}

	public void setOwnerMarriage(Integer ownerMarriage){
		this.ownerMarriage=ownerMarriage;
	}

	public Integer getOwnerMarriage(){
		return this.ownerMarriage;
	}

	public void setReportRemark(String reportRemark){
		this.reportRemark=reportRemark;
	}

	public String getReportRemark(){
		return this.reportRemark;
	}

	public void setCertificateNo(String certificateNo){
		this.certificateNo=certificateNo;
	}

	public String getCertificateNo(){
		return this.certificateNo;
	}

	public void setReportDatetime(Date reportDatetime){
		this.reportDatetime=reportDatetime;
	}

	public Date getReportDatetime(){
		return this.reportDatetime;
	}

}