package com.infodms.dms.bean;

import java.util.Date;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class MarketBean extends PO {
	private String orgName; 
	private String dealerName; 
	private Date   orderDate;
	private String modelCode;
	private String vin;
	private String ctmName;
	private String compType;
	private Double money;
	private String auditByQy;
	private String auditByDq;
	private String auditByKf;
	
	public String getAuditByKf() {
		return auditByKf;
	}
	public void setAuditByKf(String auditByKf) {
		this.auditByKf = auditByKf;
	}
	
	public String getAuditByDq() {
		return auditByDq;
	}
	public void setAuditByDq(String auditByDq) {
		this.auditByDq = auditByDq;
	}
	
	public String getAuditByQy() {
		return auditByQy;
	}
	public void setAuditByQy(String auditByQy) {
		this.auditByQy = auditByQy;
	}
	
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	
	public String getCompType() {
		return compType;
	}
	public void setCompType(String compType) {
		this.compType = compType;
	}
	
	public String getCtmName() {
		return ctmName;
	}
	public void setCtmName(String ctmName) {
		this.ctmName = ctmName;
	}
	
	public String getModelCode() {
		return modelCode;
	}
	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}
	
	public Date getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	
	public String getDealerName() {
		return dealerName;
	}
	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}
	
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
}