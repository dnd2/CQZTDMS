package com.infoservice.dms.chana.vo;

import java.util.Date;

public class ActivityVehicleVO extends BaseVO {
	private String dutyEntityCode; //下端：责任维修站代码  CHAR(8) 须统一上下端定义 上端：DEALER_CODE VARCHAR2(20) 
	private String dutyEntityName; //下端：责任维修站名称  VARCHAR(150)  上端：DEALER_NAME VARCHAR2(150) 
	private String realEntityCode; //下端：实际维修站代码  CHAR(8) 须统一上下端定义 上端：OPERATE_DEALER_CODE VARCHAR2(20) 
	private String realEntityName; //下端：实际维修站名称  VARCHAR(150)  上端：DEALER_NAME VARCHAR2(150) 
	private String customerName; //下端：客户名称  varchar(120)  上端：CUSTOMER_NAME VARCHAR2(100) 
	private String vin; //下端：VIN  varchar(17)  上端：VIN CHAR(17) 
	private String license; //下端：车牌号  varchar(30)  上端：LINCENSE_TAG VARCHAR2(21) 
	private String contactorName; //下端：联系人  varchar(30)  上端：LINKMAN VARCHAR2(30) 
	private String contactorPhone; //下端：联系人电话  varchar(30)  上端：LINKMAN_OFFICE_PHONE VARCHAR2(30) 
	private String contactorMobile; //下端：联系人手机  varchar(30)  上端：LINKMAN_MOBILE VARCHAR2(12) 
	private Integer province; //下端：省份  NUMERIC(8)  上端：PROVINCE VARCHAR2(30) 
	private Integer city; //下端：城市  NUMERIC(8)  上端：AREA VARCHAR2(30) 
	private Integer district; //下端：区县  NUMERIC(8)  上端：TOWN VARCHAR2(40) 
	private String zipCode; //下端：邮编  CHAR(6)  上端：POSTAL_CODE CHAR(6) 
	private String address; //下端：地址  varchar(120)  上端：CUSTOMER_ADDRESS VARCHAR2(150) 
	private String isAchieve; //下端：是否完成  NUMERIC(8)  上端：?  
	private Date CampaignDate; //下端：参加活动时间  TIMESTAMP  上端：CAMPAIGN_DATE  
	private Integer singleSum; //下端：单台参加活动次数  NUMERIC(8)  上端：  

	public String getDutyEntityCode() {
		return dutyEntityCode;
	}
	public void setDutyEntityCode(String dutyEntityCode) {
		this.dutyEntityCode = dutyEntityCode;
	}
	public String getDutyEntityName() {
		return dutyEntityName;
	}
	public void setDutyEntityName(String dutyEntityName) {
		this.dutyEntityName = dutyEntityName;
	}
	public String getRealEntityCode() {
		return realEntityCode;
	}
	public void setRealEntityCode(String realEntityCode) {
		this.realEntityCode = realEntityCode;
	}
	public String getRealEntityName() {
		return realEntityName;
	}
	public void setRealEntityName(String realEntityName) {
		this.realEntityName = realEntityName;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public String getLicense() {
		return license;
	}
	public void setLicense(String license) {
		this.license = license;
	}
	public String getContactorName() {
		return contactorName;
	}
	public void setContactorName(String contactorName) {
		this.contactorName = contactorName;
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
	public Integer getProvince() {
		return province;
	}
	public void setProvince(Integer province) {
		this.province = province;
	}
	public Integer getCity() {
		return city;
	}
	public void setCity(Integer city) {
		this.city = city;
	}
	public Integer getDistrict() {
		return district;
	}
	public void setDistrict(Integer district) {
		this.district = district;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getIsAchieve() {
		return isAchieve;
	}
	public void setIsAchieve(String isAchieve) {
		this.isAchieve = isAchieve;
	}
	public Date getCampaignDate() {
		return CampaignDate;
	}
	public void setCampaignDate(Date campaignDate) {
		CampaignDate = campaignDate;
	}
	public Integer getSingleSum() {
		return singleSum;
	}
	public void setSingleSum(Integer singleSum) {
		this.singleSum = singleSum;
	}

}
