package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;

public class TtAsWrBarcodePartStockLPO extends PO{
	private Long updateBy;
	private Date updateDate;
	private Date createDate;
	private Long createBy;
	private Long id;
	private String partName;
	private String partCode;
	private Long stockType;
	private Long stockNum;
	private Date stockDate;
	private String  remark ;
	private Long  status;
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
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Long getCreateBy() {
		return createBy;
	}
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPartName() {
		return partName;
	}
	public void setPartName(String partName) {
		this.partName = partName;
	}
	public String getPartCode() {
		return partCode;
	}
	public void setPartCode(String partCode) {
		this.partCode = partCode;
	}
	public Long getStockType() {
		return stockType;
	}
	public void setStockType(Long stockType) {
		this.stockType = stockType;
	}
	public Long getStockNum() {
		return stockNum;
	}
	public void setStockNum(Long stockNum) {
		this.stockNum = stockNum;
	}
	public Date getStockDate() {
		return stockDate;
	}
	public void setStockDate(Date stockDate) {
		this.stockDate = stockDate;
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
	
	
}
