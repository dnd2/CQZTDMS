package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;

public class TtDeliveryOrderDetailPO extends PO{
	private Long updateBy;
	private Date updateDate;
	private Long createBy;
	private Date createDate;
	
	private Long id;
	private Long stockId;
	private Long barcodeNo;
	private String partName;
	private Long partId;
	private String partCode;
	private Long state;
	private String remark ;
	
	private Long placeNo;
	public Long getPlaceNo() {
		return placeNo;
	}
	public void setPlaceNo(Long placeNo) {
		this.placeNo = placeNo;
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
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getStockId() {
		return stockId;
	}
	public void setStockId(Long stockId) {
		this.stockId = stockId;
	}
	public Long getBarcodeNo() {
		return barcodeNo;
	}
	public void setBarcodeNo(Long barcodeNo) {
		this.barcodeNo = barcodeNo;
	}
	public String getPartName() {
		return partName;
	}
	public void setPartName(String partName) {
		this.partName = partName;
	}
	public Long getPartId() {
		return partId;
	}
	public void setPartId(Long partId) {
		this.partId = partId;
	}

	public String getPartCode() {
		return partCode;
	}
	public void setPartCode(String partCode) {
		this.partCode = partCode;
	}
	public Long getState() {
		return state;
	}
	public void setState(Long state) {
		this.state = state;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
