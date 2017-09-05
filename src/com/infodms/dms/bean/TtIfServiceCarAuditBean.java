package com.infodms.dms.bean;

import com.infodms.dms.po.TtIfServicecarAuditPO;

public class TtIfServiceCarAuditBean extends TtIfServicecarAuditPO {
	private String userName; 
	private String orgName;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	
}
