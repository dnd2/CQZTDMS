package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;

public class TtAsWrNegativeInventoryPO extends PO{
	
	private Long id;
	private Long partNum;
	private Long dealerId;
	private Date createDate;
	private Long createBy;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getPartNum() {
		return partNum;
	}
	public void setPartNum(Long partNum) {
		this.partNum = partNum;
	}
	public Long getDealerId() {
		return dealerId;
	}
	public void setDealerId(Long dealerId) {
		this.dealerId = dealerId;
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
	
	
}
