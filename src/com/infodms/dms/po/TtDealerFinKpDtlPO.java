/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-04-10 17:14:30
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtDealerFinKpDtlPO extends PO{

	private Long dtlId;
	private Long dealerId;
	private Long finReturnType;
	private Double amount;
	private Date finMonth;
	private Double kpAmount;
	private Double kpSplusAmount;
	private Long kpStatus;
	private Long createBy;
	private Date createDate;
	private Long updateBy;
	private Date updateDate;
	private Long ver;
	private Long auditStatus;
	public Long getDtlId() {
		return dtlId;
	}
	public void setDtlId(Long dtlId) {
		this.dtlId = dtlId;
	}
	public Long getDealerId() {
		return dealerId;
	}
	public void setDealerId(Long dealerId) {
		this.dealerId = dealerId;
	}
	public Long getFinReturnType() {
		return finReturnType;
	}
	public void setFinReturnType(Long finReturnType) {
		this.finReturnType = finReturnType;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Date getFinMonth() {
		return finMonth;
	}
	public void setFinMonth(Date finMonth) {
		this.finMonth = finMonth;
	}
	public Double getKpAmount() {
		return kpAmount;
	}
	public void setKpAmount(Double kpAmount) {
		this.kpAmount = kpAmount;
	}
	public Double getKpSplusAmount() {
		return kpSplusAmount;
	}
	public void setKpSplusAmount(Double kpSplusAmount) {
		this.kpSplusAmount = kpSplusAmount;
	}
	public Long getKpStatus() {
		return kpStatus;
	}
	public void setKpStatus(Long kpStatus) {
		this.kpStatus = kpStatus;
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
	public Long getVer() {
		return ver;
	}
	public void setVer(Long ver) {
		this.ver = ver;
	}
	public Long getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(Long auditStatus) {
		this.auditStatus = auditStatus;
	}
	
	
	
}