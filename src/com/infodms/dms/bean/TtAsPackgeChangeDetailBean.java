package com.infodms.dms.bean;

import com.infodms.dms.po.TtAsPackgeChangeDetailPO;

@SuppressWarnings("serial")
public class TtAsPackgeChangeDetailBean extends TtAsPackgeChangeDetailPO {
	
	private String auditName;
	private String auditTime;
	private String vin;
	private String modelCode;
	private String productDates;
	private String dealerName;
	private String zipCode;
	private String address;
	private String phone;
	private String saleDate;
	public String getAuditName() {
		return auditName;
	}
	public void setAuditName(String auditName) {
		this.auditName = auditName;
	}
	public String getAuditTime() {
		return auditTime;
	}
	public void setAuditTime(String auditTime) {
		this.auditTime = auditTime;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public String getModelCode() {
		return modelCode;
	}
	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}
	public String getProductDates() {
		return productDates;
	}
	public void setProductDates(String productDates) {
		this.productDates = productDates;
	}
	public String getDealerName() {
		return dealerName;
	}
	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getSaleDate() {
		return saleDate;
	}
	public void setSaleDate(String saleDate) {
		this.saleDate = saleDate;
	}
}
