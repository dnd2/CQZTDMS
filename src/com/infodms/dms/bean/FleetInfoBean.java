/**
 * 
 */
package com.infodms.dms.bean;

/**
 * 
 * <p>Title:FleetInfoBean.java</p>
 *
 * <p>Description:对应TM_FLEET表格用于输入输出的bean </p>
 *
 * <p>Copyright: Copyright (c) 2010</p>
 *
 * <p>Company: www.infoservice.com.cn</p>
 * <p>Date:2010-6-9</p>
 *
 * @author zouchao
 * @version 1.0
 * @remark
 */
public class FleetInfoBean {
	
    // 集团客户ID
	private String fleetId;
	// 车厂公司
	private String oemCompanyId;
	// 经销商公司ID
	private String dlrCompanyId;
	// 经销商公司CODE
	private String dlrCompanyCode;
	// 客户名称
	private String fleetName;
	// 客户类型
	private String fleetType;
	// 主营业务
	private String mainBusiness;
	// 资金规模
	private String fundSize;
	// 人员规模
	private String staffSize;
	// 购车用途
	private String purpose;
	// 区域
	private String region;
	// 邮编
	private String zipCode;
	// 详细地址
	private String address;
	// 主要联系人
	private String mainLinkman;
	// 主要联系人职务
	private String mainJob;
	// 主要联系人电话
	private String mainPhone;
	// 主要联系人电子邮件
	private String mainEmail;
	// 其他联系人
	private String otherLinkman;
	// 其他联系人职务
	private String otherJob;
	// 其他联系人电话
	private String otherPhone;
	// 其他联系人电子邮件
	private String otherEmail;
	// 需求说明
	private String reqRemark;
	// 报备日期
	private String submitDate;
	// 提报人
	private String submitUser;
	// 状态
	private String status;
	// 审核日期
	private String auditDate;
	// 审核人
	private String auditUserId;
	// 审核备注
	private String auditRemark;
	// 创建日期
	private String createDate;
	// 创建人
	private String createBy;
	// 更新日期
	private String updateDate;
	// 更新人
	private String updateBy;
	// 删除标识
	private String isDel;
	
	private String beginTime;
	
	private String endTime;
	//组织ID
	private String orgId;
	//组织级别
	private String dutyType;
	
	private AclUserBean logonUser;
	
	public AclUserBean getLogonUser() {
		return logonUser;
	}
	public void setLogonUser(AclUserBean logonUser) {
		this.logonUser = logonUser;
	}
	public String getDutyType() {
		return dutyType;
	}
	public void setDutyType(String dutyType) {
		this.dutyType = dutyType;
	}
	public String getFleetId() {
		return fleetId;
	}
	public void setFleetId(String fleetId) {
		this.fleetId = fleetId;
	}
	public String getOemCompanyId() {
		return oemCompanyId;
	}
	public void setOemCompanyId(String oemCompanyId) {
		this.oemCompanyId = oemCompanyId;
	}
	public String getDlrCompanyId() {
		return dlrCompanyId;
	}
	public void setDlrCompanyId(String dlrCompanyId) {
		this.dlrCompanyId = dlrCompanyId;
	}
	public String getFleetName() {
		return fleetName;
	}
	public void setFleetName(String fleetName) {
		this.fleetName = fleetName;
	}
	public String getFleetType() {
		return fleetType;
	}
	public void setFleetType(String fleetType) {
		this.fleetType = fleetType;
	}
	public String getMainBusiness() {
		return mainBusiness;
	}
	public void setMainBusiness(String mainBusiness) {
		this.mainBusiness = mainBusiness;
	}
	public String getFundSize() {
		return fundSize;
	}
	public void setFundSize(String fundSize) {
		this.fundSize = fundSize;
	}
	public String getStaffSize() {
		return staffSize;
	}
	public void setStaffSize(String staffSize) {
		this.staffSize = staffSize;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
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
	public String getMainLinkman() {
		return mainLinkman;
	}
	public void setMainLinkman(String mainLinkman) {
		this.mainLinkman = mainLinkman;
	}
	public String getMainJob() {
		return mainJob;
	}
	public void setMainJob(String mainJob) {
		this.mainJob = mainJob;
	}
	public String getMainPhone() {
		return mainPhone;
	}
	public void setMainPhone(String mainPhone) {
		this.mainPhone = mainPhone;
	}
	public String getMainEmail() {
		return mainEmail;
	}
	public void setMainEmail(String mainEmail) {
		this.mainEmail = mainEmail;
	}
	public String getOtherLinkman() {
		return otherLinkman;
	}
	public void setOtherLinkman(String otherLinkman) {
		this.otherLinkman = otherLinkman;
	}
	public String getOtherJob() {
		return otherJob;
	}
	public void setOtherJob(String otherJob) {
		this.otherJob = otherJob;
	}
	public String getOtherPhone() {
		return otherPhone;
	}
	public void setOtherPhone(String otherPhone) {
		this.otherPhone = otherPhone;
	}
	public String getOtherEmail() {
		return otherEmail;
	}
	public void setOtherEmail(String otherEmail) {
		this.otherEmail = otherEmail;
	}
	public String getReqRemark() {
		return reqRemark;
	}
	public void setReqRemark(String reqRemark) {
		this.reqRemark = reqRemark;
	}
	public String getSubmitDate() {
		return submitDate;
	}
	public void setSubmitDate(String submitDate) {
		this.submitDate = submitDate;
	}
	public String getSubmitUser() {
		return submitUser;
	}
	public void setSubmitUser(String submitUser) {
		this.submitUser = submitUser;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAuditDate() {
		return auditDate;
	}
	public void setAuditDate(String auditDate) {
		this.auditDate = auditDate;
	}
	public String getAuditUserId() {
		return auditUserId;
	}
	public void setAuditUserId(String auditUserId) {
		this.auditUserId = auditUserId;
	}
	public String getAuditRemark() {
		return auditRemark;
	}
	public void setAuditRemark(String auditRemark) {
		this.auditRemark = auditRemark;
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
	public String getIsDel() {
		return isDel;
	}
	public void setIsDel(String isDel) {
		this.isDel = isDel;
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
	public String getDlrCompanyCode() {
		return dlrCompanyCode;
	}
	public void setDlrCompanyCode(String dlrCompanyCode) {
		this.dlrCompanyCode = dlrCompanyCode;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	
	
}
