package com.infoservice.dms.chana.vo;

import java.util.Date;
import java.util.LinkedList;

@SuppressWarnings("serial")
public class PotentialCustomerVO extends BaseVO {

	private String customerNo;// DMS客户编号 CHAR(12)
	private String largeCustomerNo;// ��ͻ����ￄ大客户代码 CHAR(20)
	private String sodCustomerId;//SOD客户ID VARCHAR(30)
	private String customerName;// 客户名称 VARCHAR(120)
	private Integer customerStatus;// 客户状态 NUMERIC(8)
	private Integer customerType;//客户类型 NUMERIC(8)
	private Integer gender;// 性别 NUMERIC(8)
	private Date birthday;// 出生日期 TIMESTAMP
	private String zipCode;//邮编 CHAR(6)
	private String countryCode;// 国家代码 NUMERIC(8)
	private String province;//省份 NUMERIC(8)
	private String city;//城市 NUMERIC(8)
	private String district;// 区县 NUMERIC(8)
	private String address;//地址 VARCHAR(120)
	private String eMail;// E_MAIL VARCHAR(60)
	private Integer bestContactType;// 意愿联系方式 NUMERIC(8)
	private Integer ctCode;//证件类型代码 NUMERIC(8)
	private String certificateNo;//证件号码 VARCHAR(40)
	private String hobby;//爱好 VARCHAR(300)
	private String contactorPhone;// 联系人电话 VARCHAR(30)
	private String contactorMobile;// 联系人手机 VARCHAR(30)
	private String fax;//传真 VARCHAR(30)
	private Integer educationLevel;//教育水平 NUMERIC(8)
	private Integer ownerMarriage;// 婚姻状况 NUMERIC(8)
	private Integer buyPurpose;// 购车目的 NUMERIC(8)
	private Integer vocationType;//职业类别 NUMERIC(8)
	private String positionName;//职位  VARCHAR(30)
	private Integer isCrpvip;//是否CRPVIP NUMERIC(8)
	private Integer isFirstBuy;//是否首次购车 NUMERIC(8)
	private Integer hasDriverLicense;//是否有驾照 NUMERIC(8)
	private String recommendEmpName;// 推荐人 VARCHAR(30)
	private Integer initLevel;//初始级别 NUMERIC(8)
	private Integer isWholesaler;//是否批售 NUMERIC(8)
	private Integer intentLevel;// ���򼶱� NUMERIC意向级别 NUMERIC(8)
	private Long failConsultant;//战败再分配 NUMERIC(14)
	private Long delayConsultant;//延时再分配 NUMERIC(14)
	private String industryFirst;//所在行业大类 NUMERIC(8)
	private String industrySecond;//所在行业二类 NUMERIC(8)
	private String soldBy;// 销售代表 VARCHAR(30)
	private Integer cusSource;//客户来源 NUMERIC(8)
	private Integer mediaType;//媒体类型 NUMERIC(8)
	private Integer isReported;//是否报备 NUMERIC(8)
	private String reportRemark;// ����˵�� VARCHAR报备说明 VARCHAR(300)
	private Date reportDatetime;// 报备日期 TIMESTAMP  
	private Integer reportStatus;//报备状态 NUMERIC(8)
	private String reportAuditingRemark;//报备审核说明 VARCHAR(300)
	private String reportAbortReason;//报备中断原因 VARCHAR(300)
	private Integer isPersonDriveCar;//是否亲自驾车 NUMERIC(8)
	private Integer ageStage;// 年龄段 NUMERIC(8)
	private Integer organType;//机构类型 NUMERIC(8)
	private Integer isDirect;//是否直销 NUMERIC(8)
	private String modifyReason;//修改原因 VARCHAR(255)
	private Integer familyIncome;//家庭月收入 NUMERIC(8)
	private Integer choiceReason;//选车理由 NUMERIC(8)(旧字段)
	private String buyReason;// 购车因素��������
	private Date foundDate;// 建档日期 TIMESTAMP  
	private Date updateDate;// 修改日期 TIMESTAMP  ޸����� TIMESTAMP
	private String campaignCode;// �г��市场活动编号  
	private Integer visitType;//访问类型，下端突然多出来的，2013-08-21
	private String intentModel;//意向模式，下端突然多出来的，2013-08-21

	public String getIntentModel() {
		return intentModel;
	}

	public void setIntentModel(String intentModel) {
		this.intentModel = intentModel;
	}

	public String geteMail() {
		return eMail;
	}

	public void seteMail(String eMail) {
		this.eMail = eMail;
	}

	public Integer getVisitType() {
		return visitType;
	}

	public void setVisitType(Integer visitType) {
		this.visitType = visitType;
	}

	private LinkedList<PoCusLinkmanInfoVO> linkManList;//潜在客户联系人信息表 ��

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public String getLargeCustomerNo() {
		return largeCustomerNo;
	}

	public void setLargeCustomerNo(String largeCustomerNo) {
		this.largeCustomerNo = largeCustomerNo;
	}

	public String getSodCustomerId() {
		return sodCustomerId;
	}

	public void setSodCustomerId(String sodCustomerId) {
		this.sodCustomerId = sodCustomerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Integer getCustomerStatus() {
		return customerStatus;
	}

	public void setCustomerStatus(Integer customerStatus) {
		this.customerStatus = customerStatus;
	}

	public Integer getCustomerType() {
		return customerType;
	}

	public void setCustomerType(Integer customerType) {
		this.customerType = customerType;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEMail() {
		return eMail;
	}

	public void setEMail(String mail) {
		eMail = mail;
	}

	public Integer getBestContactType() {
		return bestContactType;
	}

	public void setBestContactType(Integer bestContactType) {
		this.bestContactType = bestContactType;
	}

	public Integer getCtCode() {
		return ctCode;
	}

	public void setCtCode(Integer ctCode) {
		this.ctCode = ctCode;
	}

	public String getCertificateNo() {
		return certificateNo;
	}

	public void setCertificateNo(String certificateNo) {
		this.certificateNo = certificateNo;
	}

	public String getHobby() {
		return hobby;
	}

	public void setHobby(String hobby) {
		this.hobby = hobby;
	}

	public String getContactorPhone() {
		return contactorPhone;
	}

	public void setContactorPhone(String contactorPhone) {
		this.contactorPhone = contactorPhone;
	}

	public String getContactorMobile() {
		return contactorMobile;
	}

	public void setContactorMobile(String contactorMobile) {
		this.contactorMobile = contactorMobile;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public Integer getEducationLevel() {
		return educationLevel;
	}

	public void setEducationLevel(Integer educationLevel) {
		this.educationLevel = educationLevel;
	}

	public Integer getOwnerMarriage() {
		return ownerMarriage;
	}

	public void setOwnerMarriage(Integer ownerMarriage) {
		this.ownerMarriage = ownerMarriage;
	}

	public Integer getBuyPurpose() {
		return buyPurpose;
	}

	public void setBuyPurpose(Integer buyPurpose) {
		this.buyPurpose = buyPurpose;
	}

	public Integer getVocationType() {
		return vocationType;
	}

	public void setVocationType(Integer vocationType) {
		this.vocationType = vocationType;
	}

	public String getPositionName() {
		return positionName;
	}

	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}

	public Integer getIsCrpvip() {
		return isCrpvip;
	}

	public void setIsCrpvip(Integer isCrpvip) {
		this.isCrpvip = isCrpvip;
	}

	public Integer getIsFirstBuy() {
		return isFirstBuy;
	}

	public void setIsFirstBuy(Integer isFirstBuy) {
		this.isFirstBuy = isFirstBuy;
	}

	public Integer getHasDriverLicense() {
		return hasDriverLicense;
	}

	public void setHasDriverLicense(Integer hasDriverLicense) {
		this.hasDriverLicense = hasDriverLicense;
	}

	public String getRecommendEmpName() {
		return recommendEmpName;
	}

	public void setRecommendEmpName(String recommendEmpName) {
		this.recommendEmpName = recommendEmpName;
	}

	public Integer getInitLevel() {
		return initLevel;
	}

	public void setInitLevel(Integer initLevel) {
		this.initLevel = initLevel;
	}

	public Integer getIsWholesaler() {
		return isWholesaler;
	}

	public void setIsWholesaler(Integer isWholesaler) {
		this.isWholesaler = isWholesaler;
	}

	public Integer getIntentLevel() {
		return intentLevel;
	}

	public void setIntentLevel(Integer intentLevel) {
		this.intentLevel = intentLevel;
	}

	public Long getFailConsultant() {
		return failConsultant;
	}

	public void setFailConsultant(Long failConsultant) {
		this.failConsultant = failConsultant;
	}

	public Long getDelayConsultant() {
		return delayConsultant;
	}

	public void setDelayConsultant(Long delayConsultant) {
		this.delayConsultant = delayConsultant;
	}

	public String getIndustryFirst() {
		return industryFirst;
	}

	public void setIndustryFirst(String industryFirst) {
		this.industryFirst = industryFirst;
	}

	public String getIndustrySecond() {
		return industrySecond;
	}

	public void setIndustrySecond(String industrySecond) {
		this.industrySecond = industrySecond;
	}

	public String getSoldBy() {
		return soldBy;
	}

	public void setSoldBy(String soldBy) {
		this.soldBy = soldBy;
	}

	public Integer getCusSource() {
		return cusSource;
	}

	public void setCusSource(Integer cusSource) {
		this.cusSource = cusSource;
	}

	public Integer getMediaType() {
		return mediaType;
	}

	public void setMediaType(Integer mediaType) {
		this.mediaType = mediaType;
	}

	public Integer getIsReported() {
		return isReported;
	}

	public void setIsReported(Integer isReported) {
		this.isReported = isReported;
	}

	public String getReportRemark() {
		return reportRemark;
	}

	public void setReportRemark(String reportRemark) {
		this.reportRemark = reportRemark;
	}

	public Date getReportDatetime() {
		return reportDatetime;
	}

	public void setReportDatetime(Date reportDatetime) {
		this.reportDatetime = reportDatetime;
	}

	public Integer getReportStatus() {
		return reportStatus;
	}

	public void setReportStatus(Integer reportStatus) {
		this.reportStatus = reportStatus;
	}

	public String getReportAuditingRemark() {
		return reportAuditingRemark;
	}

	public void setReportAuditingRemark(String reportAuditingRemark) {
		this.reportAuditingRemark = reportAuditingRemark;
	}

	public String getReportAbortReason() {
		return reportAbortReason;
	}

	public void setReportAbortReason(String reportAbortReason) {
		this.reportAbortReason = reportAbortReason;
	}

	public Integer getIsPersonDriveCar() {
		return isPersonDriveCar;
	}

	public void setIsPersonDriveCar(Integer isPersonDriveCar) {
		this.isPersonDriveCar = isPersonDriveCar;
	}

	public Integer getAgeStage() {
		return ageStage;
	}

	public void setAgeStage(Integer ageStage) {
		this.ageStage = ageStage;
	}

	public Integer getOrganType() {
		return organType;
	}

	public void setOrganType(Integer organType) {
		this.organType = organType;
	}

	public Integer getIsDirect() {
		return isDirect;
	}

	public void setIsDirect(Integer isDirect) {
		this.isDirect = isDirect;
	}

	public String getModifyReason() {
		return modifyReason;
	}

	public void setModifyReason(String modifyReason) {
		this.modifyReason = modifyReason;
	}

	public Integer getFamilyIncome() {
		return familyIncome;
	}

	public void setFamilyIncome(Integer familyIncome) {
		this.familyIncome = familyIncome;
	}

	public Integer getChoiceReason() {
		return choiceReason;
	}

	public void setChoiceReason(Integer choiceReason) {
		this.choiceReason = choiceReason;
	}

	public String getBuyReason() {
		return buyReason;
	}

	public void setBuyReason(String buyReason) {
		this.buyReason = buyReason;
	}

	public Date getFoundDate() {
		return foundDate;
	}

	public void setFoundDate(Date foundDate) {
		this.foundDate = foundDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getCampaignCode() {
		return campaignCode;
	}

	public void setCampaignCode(String campaignCode) {
		this.campaignCode = campaignCode;
	}

	public LinkedList<PoCusLinkmanInfoVO> getLinkManList() {
		return linkManList;
	}

	public void setLinkManList(LinkedList<PoCusLinkmanInfoVO> linkManList) {
		this.linkManList = linkManList;
	}
}
