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
public class TtSalesXpPricePO extends PO{

	private Long Id;
	private Long xpDetailId;
	private Double amount;
	private Long dealerId;
	private Integer status;
	private Date startDate;
	private Date endDate;
	private Date updateDate;
	private Long updateBy;
	private Long createBy;
	private Date createDate;
	private String remark;
	private String ksid;
	private String xpCode;
	private Integer auditStatus;
	private Long auditPer;
	private Date auditDate;
	private String auditRemark;

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public Long getXpDetailId() {
		return xpDetailId;
	}

	public void setXpDetailId(Long xpDetailId) {
		this.xpDetailId = xpDetailId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Long getDealerId() {
		return dealerId;
	}

	public void setDealerId(Long dealerId) {
		this.dealerId = dealerId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getKsid() {
		return ksid;
	}

	public void setKsid(String ksid) {
		this.ksid = ksid;
	}

	public String getXpCode() {
		return xpCode;
	}

	public void setXpCode(String xpCode) {
		this.xpCode = xpCode;
	}

	public Integer getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}

	public Long getAuditPer() {
		return auditPer;
	}

	public void setAuditPer(Long auditPer) {
		this.auditPer = auditPer;
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
	
	public TtSalesXpPricePO copy() {
		TtSalesXpPricePO newObj = new TtSalesXpPricePO();
		newObj.setAmount(this.getAmount());
		newObj.setAuditDate(this.getAuditDate());
		newObj.setAuditPer(this.getAuditPer());
		newObj.setAuditRemark(this.getAuditRemark());
		newObj.setAuditStatus(this.getAuditStatus());
		newObj.setCreateBy(this.getCreateBy());
		newObj.setCreateDate(this.getCreateDate());
		newObj.setDealerId(this.getDealerId());
		newObj.setEndDate(this.getEndDate());
		newObj.setId(this.getId());
		newObj.setKsid(this.getKsid());
		newObj.setRemark(this.getRemark());
		newObj.setStartDate(this.getStartDate());
		newObj.setStatus(this.getStatus());
		newObj.setUpdateBy(this.getUpdateBy());
		newObj.setUpdateDate(this.getUpdateDate());
		newObj.setXpCode(this.getXpCode());
		newObj.setXpDetailId(this.getXpDetailId());
		return newObj;
	}
}