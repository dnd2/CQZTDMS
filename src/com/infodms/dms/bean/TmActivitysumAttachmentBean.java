package com.infodms.dms.bean;

import java.util.Date;

public class TmActivitysumAttachmentBean {
	private Date updateDate;
	private Long updateBy;
	private Long createBy;
	private Long sumId;
	private String attachmentRemark;
	private Date createDate;
	private String attachmentName;
	private Long activityAttachmentId;
	private String fileId;
	private Long auditlogId;
	private String attachmentUrl;
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public Long getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}
	public Long getCreateBy() {
		return createBy;
	}
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}
	public Long getSumId() {
		return sumId;
	}
	public void setSumId(Long sumId) {
		this.sumId = sumId;
	}
	public String getAttachmentRemark() {
		return attachmentRemark;
	}
	public void setAttachmentRemark(String attachmentRemark) {
		this.attachmentRemark = attachmentRemark;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getAttachmentName() {
		return attachmentName;
	}
	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}
	public Long getActivityAttachmentId() {
		return activityAttachmentId;
	}
	public void setActivityAttachmentId(Long activityAttachmentId) {
		this.activityAttachmentId = activityAttachmentId;
	}
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	public Long getAuditlogId() {
		return auditlogId;
	}
	public void setAuditlogId(Long auditlogId) {
		this.auditlogId = auditlogId;
	}
	public String getAttachmentUrl() {
		return attachmentUrl;
	}
	public void setAttachmentUrl(String attachmentUrl) {
		this.attachmentUrl = attachmentUrl;
	}
}
