/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2009-12-08 18:36:25
* CreateBy   : Zhang tianheng
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtSalesXpPriceAuditRecordPO extends PO{

	private Long recordId;
	private Long priceId;
	private Integer auditStatus;
	private Long auditBy;
	private Date auditDate;
	private String auditRemark;
	private Double priceBefore;
	private Double priceNow;
	private Integer statusBefore;
	private Integer statusNow;
	private Date startDateBefore;
	private Date startDateNow;
	private Date endDateBefore;
	private Date endDateNow;
	private Long createBy;
	private Date createDate;
	
	public Long getRecordId() {
		return recordId;
	}
	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}
	public Long getPriceId() {
		return priceId;
	}
	public void setPriceId(Long priceId) {
		this.priceId = priceId;
	}
	public Integer getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}
	public Long getAuditBy() {
		return auditBy;
	}
	public void setAuditBy(Long auditBy) {
		this.auditBy = auditBy;
	}
	public Date getAuditDate() {
		return auditDate;
	}
	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}
	public String getAuditRemark() {
		return auditRemark;
	}
	public void setAuditRemark(String auditRemark) {
		this.auditRemark = auditRemark;
	}
	public Double getPriceBefore() {
		return priceBefore;
	}
	public void setPriceBefore(Double priceBefore) {
		this.priceBefore = priceBefore;
	}
	public Double getPriceNow() {
		return priceNow;
	}
	public void setPriceNow(Double priceNow) {
		this.priceNow = priceNow;
	}
	public Integer getStatusBefore() {
		return statusBefore;
	}
	public void setStatusBefore(Integer statusBefore) {
		this.statusBefore = statusBefore;
	}
	public Integer getStatusNow() {
		return statusNow;
	}
	public void setStatusNow(Integer statusNow) {
		this.statusNow = statusNow;
	}
	public Date getStartDateBefore() {
		return startDateBefore;
	}
	public void setStartDateBefore(Date startDateBefore) {
		this.startDateBefore = startDateBefore;
	}
	public Date getStartDateNow() {
		return startDateNow;
	}
	public void setStartDateNow(Date startDateNow) {
		this.startDateNow = startDateNow;
	}
	public Date getEndDateBefore() {
		return endDateBefore;
	}
	public void setEndDateBefore(Date endDateBefore) {
		this.endDateBefore = endDateBefore;
	}
	public Date getEndDateNow() {
		return endDateNow;
	}
	public void setEndDateNow(Date endDateNow) {
		this.endDateNow = endDateNow;
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
}