package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;

public class TmAsWrBarcodePartStockPO extends PO{
	private Long id;
	private Long returnId;
	private Long partId;
	private String partName;
	private String yieldly;
	private Long isLibrary;
	private Long BarcodeNo;
	private Long  deductibleReasonCode;
	private String remark;
	private Long stasus;
	private Long stockType;
	private String partCode;
	private Date srockDate;
	private Long 	isCliam;
	private Long 	count;
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getReturnId() {
		return returnId;
	}
	public void setReturnId(Long returnId) {
		this.returnId = returnId;
	}
	public Long getPartId() {
		return partId;
	}
	public void setPartId(Long partId) {
		this.partId = partId;
	}
	public String getPartName() {
		return partName;
	}
	public void setPartName(String partName) {
		this.partName = partName;
	}
	public String getYieldly() {
		return yieldly;
	}
	public void setYieldly(String yieldly) {
		this.yieldly = yieldly;
	}
	public Long getIsLibrary() {
		return isLibrary;
	}
	public void setIsLibrary(Long isLibrary) {
		this.isLibrary = isLibrary;
	}
	public Long getBarcodeNo() {
		return BarcodeNo;
	}
	public void setBarcodeNo(Long barcodeNo) {
		BarcodeNo = barcodeNo;
	}
	public Long getDeductibleReasonCode() {
		return deductibleReasonCode;
	}
	public void setDeductibleReasonCode(Long deductibleReasonCode) {
		this.deductibleReasonCode = deductibleReasonCode;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Long getStasus() {
		return stasus;
	}
	public void setStasus(Long stasus) {
		this.stasus = stasus;
	}
	public Long getStockType() {
		return stockType;
	}
	public void setStockType(Long stockType) {
		this.stockType = stockType;
	}
	public String getPartCode() {
		return partCode;
	}
	public void setPartCode(String partCode) {
		this.partCode = partCode;
	}
	public Date getSrockDate() {
		return srockDate;
	}
	public void setSrockDate(Date srockDate) {
		this.srockDate = srockDate;
	}
	public Long getIsCliam() {
		return isCliam;
	}
	public void setIsCliam(Long isCliam) {
		this.isCliam = isCliam;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public Long getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
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
	private Date updateDate;
	private Long updateBy;
	private Long createBy;
	private Date createDate;
}
