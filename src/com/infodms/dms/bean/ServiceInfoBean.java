package com.infodms.dms.bean;

/**
 * 
 * <p>Title:ServiceInfoBean.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2010</p>
 *
 * <p>Company: www.infoservice.com.cn</p>
 * <p>Date:2010-5-17</p>
 *
 * @author zouchao
 * @version 1.0
 * @remark
 */
public class ServiceInfoBean {
    // 工单号
	private String orderId;
	// 申请单位ID
	private String dealerId;
	// 邮寄方式
	private String mailType;
	// 邮寄地址
	private String mailAddress;
	// 申请时间
	private String appDate;
	// 申请内容
	private String seContent;
	// 申请状态
	private String status;
	// 联系人
	private String linkMan;
	// 联系电话
	private String tel;
	// 传真
	private String fax;
	// 创建时间
	private String createDate;
	// 创建人员
	private String createBy;
	// 更新时间
	private String updateDate;
	// 更新人员
	private String updateBy;
	// 删除标识(1删除，默认0)
	private String isDel;
	//组织ID
	private String orgId;		  
	//提报开始时间
	private String beginTime;	
	//提报结束时间
	private String endTime;     
	//add by xiayanpeng 加入DEALER_CODE 和 DEALER_NAME
	private Long companyId;
	
	private String dealerCode;
	
	private String dealerName;
	
	public String getDealerCode() {
		return dealerCode;
	}
	public void setDealerCode(String dealerCode) {
		this.dealerCode = dealerCode;
	}
	public String getDealerName() {
		return dealerName;
	}
	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getDealerId() {
		return dealerId;
	}
	public void setDealerId(String dealerId) {
		this.dealerId = dealerId;
	}
	public String getMailType() {
		return mailType;
	}
	public void setMailType(String mailType) {
		this.mailType = mailType;
	}
	public String getMailAddress() {
		return mailAddress;
	}
	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}
	public String getAppDate() {
		return appDate;
	}
	public void setAppDate(String appDate) {
		this.appDate = appDate;
	}
	public String getSeContent() {
		return seContent;
	}
	public void setSeContent(String seContent) {
		this.seContent = seContent;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLinkMan() {
		return linkMan;
	}
	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
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
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	
	
}
