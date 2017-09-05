package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;

public class TmDealerCreditPO extends PO{
	private Long creditId;
	private Long dealerId;
	private Long type;
	private Date creditDate;
	private Long createBy;
	private Date createDate;
	private Long updateBy;
	private Date updateDate;
	public Long getCreditId() {
		return creditId;
	}
	public void setCreditId(Long creditId) {
		this.creditId = creditId;
	}
	public Long getDealerId() {
		return dealerId;
	}
	public void setDealerId(Long dealerId) {
		this.dealerId = dealerId;
	}
	public Long getType() {
		return type;
	}
	public void setType(Long type) {
		this.type = type;
	}
	public Date getCreditDate() {
		return creditDate;
	}
	public void setCreditDate(Date creditDate) {
		this.creditDate = creditDate;
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
