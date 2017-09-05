package com.infodms.dms.bean;

import java.util.Date;

import com.infoservice.po3.bean.PO;

public class LabourPartBean extends PO {
	private Date roCreateDate; // 维修日期
	private String dealerCode; // 经销商
	private Double totalMileage; // 行驶里程
	private Date purchasedDate; // 购车日期
	private String ownerName; // 客户名称
	private String wrLabourcode; // 作业名称代码
	private String wrLabourname; // 作业名称
	private String partNo; // 零件件号
	private String partName; // 零件名称
	private String vin; // VIN码

	public Date getRoCreateDate() {
		return roCreateDate;
	}

	public void setRoCreateDate(Date roCreateDate) {
		this.roCreateDate = roCreateDate;
	}

	public String getDealerCode() {
		return dealerCode;
	}

	public void setDealerCode(String dealerCode) {
		this.dealerCode = dealerCode;
	}

	public Double getTotalMileage() {
		return totalMileage;
	}

	public void setTotalMileage(Double totalMileage) {
		this.totalMileage = totalMileage;
	}

	public Date getPurchasedDate() {
		return purchasedDate;
	}

	public void setPurchasedDate(Date purchasedDate) {
		this.purchasedDate = purchasedDate;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getWrLabourcode() {
		return wrLabourcode;
	}

	public void setWrLabourcode(String wrLabourcode) {
		this.wrLabourcode = wrLabourcode;
	}

	public String getWrLabourname() {
		return wrLabourname;
	}

	public void setWrLabourname(String wrLabourname) {
		this.wrLabourname = wrLabourname;
	}

	public String getPartNo() {
		return partNo;
	}

	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}

	public String getPartName() {
		return partName;
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}
}