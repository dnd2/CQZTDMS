package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;

public class TtDeliveryOrderPO extends PO{
	private Long stockId;
	private String stockNo;
	private Date stockDate;
	private Long supplierId;
	private String supplierName;
	private String supplierCode;
	private Long stockType;
	
	private Long stockState;
	private Long stockNumber;
	
	private Date endTime;
	private Date starTime;
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Date getStarTime() {
		return starTime;
	}
	public void setStarTime(Date starTime) {
		this.starTime = starTime;
	}
	public Long getStockId() {
		return stockId;
	}
	public void setStockId(Long stockId) {
		this.stockId = stockId;
	}
	public String getStockNo() {
		return stockNo;
	}
	public void setStockNo(String stockNo) {
		this.stockNo = stockNo;
	}
	public Date getStockDate() {
		return stockDate;
	}
	public void setStockDate(Date stockDate) {
		this.stockDate = stockDate;
	}
	public Long getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public String getSupplierCode() {
		return supplierCode;
	}
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	public Long getStockType() {
		return stockType;
	}
	public void setStockType(Long stockType) {
		this.stockType = stockType;
	}
	public Long getStockState() {
		return stockState;
	}
	public void setStockState(Long stockState) {
		this.stockState = stockState;
	}
	public Long getStockNumber() {
		return stockNumber;
	}
	public void setStockNumber(Long stockNumber) {
		this.stockNumber = stockNumber;
	}
	public Long getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public Long getCreateBy() {
		return createBy;
	}
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	private Long updateBy;
	private Date updateDate;
	private Long createBy;
	private Date createDate;
}
