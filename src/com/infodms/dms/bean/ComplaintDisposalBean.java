package com.infodms.dms.bean;

/**
 * 
 * <p>Title:ComplaintDisposalBean.java</p>
 *
 * <p>Description:用于客户投诉处理输入输出的BEAN</p>
 *
 * <p>Copyright: Copyright (c) 2010</p>
 *
 * <p>Company: www.infoservice.com.cn</p>
 * <p>Date:2010-6-1</p>
 *
 * @author zouchao
 * @version 1.0
 * @remark
 */
public class ComplaintDisposalBean {
	// 投诉ID
	private String compId;
	// 客户姓名
	private String linkman;
	// 性别
	private String sex;
	// 生日
	private String birthday;
	// 年龄
	private String age;
	// 客户电话
	private String tel;
	// 所属区域id
	private String ownOrgId;
	// 所属区域code
	private String ownOrgCode;
	// 投诉经销商id
	private String compDealerId;
	// 投诉经销商code
	private String compDealerCode;
	// 省份
	private String province;
	// 市
	private String city;
	// 地区，县级市
	private String district;
	// 家庭住址
	private String address;
	// 邮箱
	private String eMail;
	// 邮编
	private String zipCode;
	// 车型
	private String modelCode;
	// 处理的组织ID
	private String orgId;
	// 处理的服务中心ID
	private String dealerId;
	// 投诉编号
	private String compCode;
	// 投诉来源
	private String compSource;
	// 投诉类型大类
	private String compType;
	// 投诉类型小类
	private String compType2;
	// 投诉等级
	private String compLevel;
	// 投诉状态
	private String status;
	// 投诉内容
	private String compContent;
	// 删除标识
	private String isDel;
	// 接口状态
	private String intStatus;
	// 车辆VIN
	private String vin;
	// 发动机号
	private String engineNo;
	// 车牌号
	private String licenseNo;
	// 购车日期
	private String purchasedDate;
	// 创建时间
	private String createDate;
	// 创建人
	private String createBy;
	// 更新时间
	private String updateDate;
	// 更新人
	private String updateBy;
	// 投诉日期开始时间
	private String beginTime;
	// 投诉日期结束时间 
	private String endTime;
	// 处理结果（处理中状态）
	private String auditResult;
	
	public String getCompId() {
		return compId;
	}
	public void setCompId(String compId) {
		this.compId = compId;
	}
	public String getLinkman() {
		return linkman;
	}
	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getOwnOrgId() {
		return ownOrgId;
	}
	public void setOwnOrgId(String ownOrgId) {
		this.ownOrgId = ownOrgId;
	}
	public String getOwnOrgCode() {
		return ownOrgCode;
	}
	public void setOwnOrgCode(String ownOrgCode) {
		this.ownOrgCode = ownOrgCode;
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
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getModelCode() {
		return modelCode;
	}
	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getDealerId() {
		return dealerId;
	}
	public void setDealerId(String dealerId) {
		this.dealerId = dealerId;
	}
	public String getCompCode() {
		return compCode;
	}
	public void setCompCode(String compCode) {
		this.compCode = compCode;
	}
	public String getCompSource() {
		return compSource;
	}
	public void setCompSource(String compSource) {
		this.compSource = compSource;
	}
	public String getCompType() {
		return compType;
	}
	public void setCompType(String compType) {
		this.compType = compType;
	}
	public String getCompLevel() {
		return compLevel;
	}
	public void setCompLevel(String compLevel) {
		this.compLevel = compLevel;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCompContent() {
		return compContent;
	}
	public void setCompContent(String compContent) {
		this.compContent = compContent;
	}
	public String getIsDel() {
		return isDel;
	}
	public void setIsDel(String isDel) {
		this.isDel = isDel;
	}
	public String getIntStatus() {
		return intStatus;
	}
	public void setIntStatus(String intStatus) {
		this.intStatus = intStatus;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public String getEngineNo() {
		return engineNo;
	}
	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}
	public String getLicenseNo() {
		return licenseNo;
	}
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}
	public String getPurchasedDate() {
		return purchasedDate;
	}
	public void setPurchasedDate(String purchasedDate) {
		this.purchasedDate = purchasedDate;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	public String getCompDealerId() {
		return compDealerId;
	}
	public void setCompDealerId(String compDealerId) {
		this.compDealerId = compDealerId;
	}
	public String getCompDealerCode() {
		return compDealerCode;
	}
	public void setCompDealerCode(String compDealerCode) {
		this.compDealerCode = compDealerCode;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getAuditResult() {
		return auditResult;
	}
	public void setAuditResult(String auditResult) {
		this.auditResult = auditResult;
	}
	public String getCompType2() {
		return compType2;
	}
	public void setCompType2(String compType2) {
		this.compType2 = compType2;
	}
	
	
	
	
}
