package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;

public class TtSalesVhclLimmitPO extends PO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long limitId;
	private Long packageId;
	private Integer salesStatus;
	private Long dealerId;
	private Long createBy;
	private Date createDate;
	private Long updateBy;
	private Date updateDate;
	
	public Long getLimitId() {
		return limitId;
	}
	public void setLimitId(Long limitId) {
		this.limitId = limitId;
	}
	public Long getPackageId() {
		return packageId;
	}
	public void setPackageId(Long packageId) {
		this.packageId = packageId;
	}
	public Integer getSalesStatus() {
		return salesStatus;
	}
	public void setSalesStatus(Integer salesStatus) {
		this.salesStatus = salesStatus;
	}
	public Long getDealerId() {
		return dealerId;
	}
	public void setDealerId(Long dealerId) {
		this.dealerId = dealerId;
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
}
