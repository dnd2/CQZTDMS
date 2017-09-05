package com.infoservice.dms.chana.vo;

import java.util.Date;

public class PartSalesVO extends BaseVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Date uploadDate;     //日期
	private String serviceBy;    //服务专员
	private String partNo;       //零件代码
	private String partName;     //零件名称
	private Double salesCount;    //销售数量
	private Double amount;       //金额
	private String brand;        //零件品牌
	public Date getUploadDate() {
		return uploadDate;
	}
	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}
	public String getServiceBy() {
		return serviceBy;
	}
	public void setServiceBy(String serviceBy) {
		this.serviceBy = serviceBy;
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
	public Double getSalesCount() {
		return salesCount;
	}
	public void setSalesCount(Double salesCount) {
		this.salesCount = salesCount;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
}
