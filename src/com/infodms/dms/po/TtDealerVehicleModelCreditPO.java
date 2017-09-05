/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-07-12 18:00:58
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtDealerVehicleModelCreditPO extends PO{

	private Long id;
	private Long dealerId;
	private String vehicleModelId;
	private Double creditAmount;
	private Date startDate;
	private Date endDate;
	private Integer status;
	private Double avaliableAmount;
	private String remark;
	private Integer auditStatus;
	private Date updateDate;
	private Long updateBy;
	private Long createBy;
	private Date createDate;
	private Integer finType;
	private String vehicleModelName;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getDealerId() {
		return dealerId;
	}
	public void setDealerId(Long dealerId) {
		this.dealerId = dealerId;
	}
	public String getVehicleModelId() {
		return vehicleModelId;
	}
	public void setVehicleModelId(String vehicleModelId) {
		this.vehicleModelId = vehicleModelId;
	}
	public Double getCreditAmount() {
		return creditAmount;
	}
	public void setCreditAmount(Double creditAmount) {
		this.creditAmount = creditAmount;
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
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Double getAvaliableAmount() {
		return avaliableAmount;
	}
	public void setAvaliableAmount(Double avaliableAmount) {
		this.avaliableAmount = avaliableAmount;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
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
	public Integer getFinType() {
		return finType;
	}
	public void setFinType(Integer finType) {
		this.finType = finType;
	}
	public String getVehicleModelName() {
		return vehicleModelName;
	}
	public void setVehicleModelName(String vehicleModelName) {
		this.vehicleModelName = vehicleModelName;
	}
	
}