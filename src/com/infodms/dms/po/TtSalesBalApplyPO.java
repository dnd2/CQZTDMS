/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-05-21 14:02:56
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtSalesBalApplyPO extends PO{
	private Long applyId;
	private String applyNo;
	private Long logiId;
	private Integer balCount;
	private Double balAmount;
	private Double deductMoney;
	private Double otherMoney;
	private Double supplyMoney;
	private Long createBy;
	private Date createDate;
	private Long updateBy;
	private Date updateDate;
	private Integer status;
	private Long auditBy;
	private Date auditTime;
	private String auditRemark;
	private String invoiceNo;
	
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public Long getAuditBy() {
		return auditBy;
	}
	public void setAuditBy(Long auditBy) {
		this.auditBy = auditBy;
	}
	public Date getAuditTime() {
		return auditTime;
	}
	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}
	public String getAuditRemark() {
		return auditRemark;
	}
	public void setAuditRemark(String auditRemark) {
		this.auditRemark = auditRemark;
	}
	public String getApplyNo() {
		return applyNo;
	}
	public void setApplyNo(String applyNo) {
		this.applyNo = applyNo;
	}
	public Long getApplyId() {
		return applyId;
	}
	public void setApplyId(Long applyId) {
		this.applyId = applyId;
	}
	public Long getLogiId() {
		return logiId;
	}
	public void setLogiId(Long logiId) {
		this.logiId = logiId;
	}
	public Integer getBalCount() {
		return balCount;
	}
	public void setBalCount(Integer balCount) {
		this.balCount = balCount;
	}
	public Double getBalAmount() {
		return balAmount;
	}
	public void setBalAmount(Double balAmount) {
		this.balAmount = balAmount;
	}
	public Double getDeductMoney() {
		return deductMoney;
	}
	public void setDeductMoney(Double deductMoney) {
		this.deductMoney = deductMoney;
	}
	public Double getOtherMoney() {
		return otherMoney;
	}
	public void setOtherMoney(Double otherMoney) {
		this.otherMoney = otherMoney;
	}
	public Double getSupplyMoney() {
		return supplyMoney;
	}
	public void setSupplyMoney(Double supplyMoney) {
		this.supplyMoney = supplyMoney;
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
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
}