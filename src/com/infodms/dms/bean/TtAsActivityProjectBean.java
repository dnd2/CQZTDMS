package com.infodms.dms.bean;

import com.infodms.dms.po.TtAsActivityProjectPO;

@SuppressWarnings("serial")
public class TtAsActivityProjectBean extends TtAsActivityProjectPO{
	
	private String proName;
	private String paidName;
	private String activityCode;
	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	public String getPaidName() {
		return paidName;
	}

	public void setPaidName(String paidName) {
		this.paidName = paidName;
	}

	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}
}
