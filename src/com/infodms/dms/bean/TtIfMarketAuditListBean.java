package com.infodms.dms.bean;

import com.infoservice.po3.bean.PO;

public class TtIfMarketAuditListBean extends PO{
	private static final long serialVersionUID = 5301003320149439131L;
	private String audit_date;//审核日期
	private String user_name;//审批人员
	private String org_name	;//部门
	private String status;//审批状态
	private String audit_content;//审核意见
	public String getAudit_date() {
		return audit_date;
	}
	public void setAudit_date(String audit_date) {
		this.audit_date = audit_date;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getOrg_name() {
		return org_name;
	}
	public void setOrg_name(String org_name) {
		this.org_name = org_name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAudit_content() {
		return audit_content;
	}
	public void setAudit_content(String audit_content) {
		this.audit_content = audit_content;
	}
}
