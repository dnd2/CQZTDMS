package com.infodms.dms.bean;

import com.infoservice.po3.bean.PO;

public class SpeFeeApproveLogListBean extends PO{

	private static final long serialVersionUID = -8539879405487964190L;

	private String auditing_date;
	private String user_name;
	private String dept_name;
	private String audit_status;
	private String auditing_opinion;
	public String getAuditing_date() {
		return auditing_date;
	}
	public void setAuditing_date(String auditing_date) {
		this.auditing_date = auditing_date;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getDept_name() {
		return dept_name;
	}
	public void setDept_name(String dept_name) {
		this.dept_name = dept_name;
	}
	public String getAudit_status() {
		return audit_status;
	}
	public void setAudit_status(String audit_status) {
		this.audit_status = audit_status;
	}
	public String getAuditing_opinion() {
		return auditing_opinion;
	}
	public void setAuditing_opinion(String auditing_opinion) {
		this.auditing_opinion = auditing_opinion;
	}
}
