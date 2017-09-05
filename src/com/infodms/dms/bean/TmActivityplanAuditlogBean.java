package com.infodms.dms.bean;

import java.util.Date;

public class TmActivityplanAuditlogBean {
	private Long sumId;
	private Long createBy;
	private Long planId;
	private Long orgId;
	private String orgName;
	private String createUser;
	private String auditlog;
	private Integer logType;
	private String logTypeName;
	private String createDate;
	private Long auditlogId;
	public Long getCreateBy() {
		return createBy;
	}
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}
	public Long getPlanId() {
		return planId;
	}
	public void setPlanId(Long planId) {
		this.planId = planId;
	}
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getAuditlog() {
		return auditlog;
	}
	public void setAuditlog(String auditlog) {
		this.auditlog = auditlog;
	}
	public Integer getLogType() {
		return logType;
	}
	public void setLogType(Integer logType) {
		this.logType = logType;
	}
	public String getLogTypeName() {
		return logTypeName;
	}
	public void setLogTypeName(String logTypeName) {
		this.logTypeName = logTypeName;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public Long getAuditlogId() {
		return auditlogId;
	}
	public void setAuditlogId(Long auditlogId) {
		this.auditlogId = auditlogId;
	}
	public Long getSumId() {
		return sumId;
	}
	public void setSumId(Long sumId) {
		this.sumId = sumId;
	}
}
