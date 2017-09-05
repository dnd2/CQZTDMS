package com.infodms.dms.po;

import java.util.Date;

public class TtIfServicecarExtPO extends TtIfServicecarPO{
	private String dealerCode; //经销商代码
	private String dealerName; //单位名称
	private String modelName; //车型
	private String auditContent; //审批意见
	private Integer auditStatus; //审批状态
	private Long auditBy; //人员
	private String auditByName;//人员姓名
	private Date auditDate; //审批时间
	private Long id;
	private String deptName;//人员部门

	public String getAuditContent() {
		return auditContent;
	}

	public void setAuditContent(String auditContent) {
		this.auditContent = auditContent;
	}

	public Integer getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}

	public Long getAuditBy() {
		return auditBy;
	}

	public void setAuditBy(Long auditBy) {
		this.auditBy = auditBy;
	}

	public Date getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getAuditByName() {
		return auditByName;
	}

	public void setAuditByName(String auditByName) {
		this.auditByName = auditByName;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getDealerName() {
		return dealerName;
	}

	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}

	public String getDealerCode() {
		return dealerCode;
	}

	public void setDealerCode(String dealerCode) {
		this.dealerCode = dealerCode;
	}
	
	

}
