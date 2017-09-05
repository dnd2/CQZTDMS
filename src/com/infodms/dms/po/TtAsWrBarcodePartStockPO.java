package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;

public class TtAsWrBarcodePartStockPO extends PO{
	private Long id;
	private Long returnId;
	private Long partId;
	private String partName;
	private String partCode;
	private Long isCliam;
	
	public Long getIsCliam() {
		return isCliam;
	}
	public void setIsCliam(Long isCliam) {
		this.isCliam = isCliam;
	}
	public String getPartCode() {
		return partCode;
	}
	public void setPartCode(String partCode) {
		this.partCode = partCode;
	}
	private Long  yieldly;
	private Long isLibrary ;
	private Long deductibleReasonCode ;
	private String remark ;
	private Long status ;
	private Long barcodeNo ;
	private Long stockType;
	
	public Long getStockType() {
		return stockType;
	}
	public void setStockType(Long stockType) {
		this.stockType = stockType;
	}
	public Long getBarcodeNo() {
		return barcodeNo;
	}
	public void setBarcodeNo(Long barcodeNo) {
		this.barcodeNo = barcodeNo;
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
	public Long getYieldly() {
		return yieldly;
	}
	public void setYieldly(Long yieldly) {
		this.yieldly = yieldly;
	}
	public Long getIsLibrary() {
		return isLibrary;
	}
	public void setIsLibrary(Long isLibrary) {
		this.isLibrary = isLibrary;
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
	
	public Long getStatus() {
		return status;
	}
	public void setStatus(Long status) {
		this.status = status;
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
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	private Long updateBy;
	private Long createBy;
	private Date createDate;
	private Date updateDate;
}
