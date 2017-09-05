package com.infodms.dms.bean;

import java.util.Date;

import com.infodms.dms.po.TtAsWrApplicationExtPO;

@SuppressWarnings("serial")
public class TtAsWrApplicationExtBean extends TtAsWrApplicationExtPO{
	private String deliverer;
	private String delivererPhone;
	private String yieldlyName;
	private Date auditDate;
	private String username;
	private String groupName;
	private String kouJian;
	private Integer isCounter;
	
	public Date getAuditDate() {
		return auditDate;
	}
	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}
	public String getDeliverer() {
		return deliverer;
	}
	public void setDeliverer(String deliverer) {
		this.deliverer = deliverer;
	}
	public String getDelivererPhone() {
		return delivererPhone;
	}
	public void setDelivererPhone(String delivererPhone) {
		this.delivererPhone = delivererPhone;
	}
	public String getYieldlyName() {
		return yieldlyName;
	}
	public void setYieldlyName(String yieldlyName) {
		this.yieldlyName = yieldlyName;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getKouJian() {
		return kouJian;
	}
	public void setKouJian(String kouJian) {
		this.kouJian = kouJian;
	}
	public Integer getIsCounter() {
		return isCounter;
	}
	public void setIsCounter(Integer isCounter) {
		this.isCounter = isCounter;
	}
	
}
