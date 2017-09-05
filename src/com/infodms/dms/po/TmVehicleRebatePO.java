/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-10-27 13:50:26
* CreateBy   : wizard_lee
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmVehicleRebatePO extends PO{
	private Long rebateId;
	private Long vehicleId;
	private Date rebateDate;
	private Long dealerId;
	private String vin;
	private Double rebateAmount;
	private Long isHaveRebate;
	private Date createDate;
	private Long createBy;
	private Long updateBy;
	private Date updateDate;
	public Long getRebateId() {
		return rebateId;
	}
	public void setRebateId(Long rebateId) {
		this.rebateId = rebateId;
	}
	public Long getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(Long vehicleId) {
		this.vehicleId = vehicleId;
	}
	public Date getRebateDate() {
		return rebateDate;
	}
	public void setRebateDate(Date rebateDate) {
		this.rebateDate = rebateDate;
	}
	public Long getDealerId() {
		return dealerId;
	}
	public void setDealerId(Long dealerId) {
		this.dealerId = dealerId;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public Double getRebateAmount() {
		return rebateAmount;
	}
	public void setRebateAmount(Double rebateAmount) {
		this.rebateAmount = rebateAmount;
	}
	public Long getIsHaveRebate() {
		return isHaveRebate;
	}
	public void setIsHaveRebate(Long isHaveRebate) {
		this.isHaveRebate = isHaveRebate;
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