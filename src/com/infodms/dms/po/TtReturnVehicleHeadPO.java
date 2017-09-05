/*
 * Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
 * This software is published under the terms of the Infoservice Software
 * License version 1.0, a copy of which has been included with this
 * distribution in the LICENSE.txt file.
 *
 * CreateDate : 2013-07-16 17:30:51
 * CreateBy   : Administrator
 * Comment    : generate by com.sgm.po.POGen
 */

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtReturnVehicleHeadPO extends PO {

	private Long companyId;
	private Long yieldly;
	private Date updateDate;
	private String reason;
	private String invoiceNo;
	private Long createBy;
	private Date invoiceDate;
	private Long accountTypeId;
	private Date createDate;
	private Long dealerId;
	private Long invoicePer;
	private String accountTypeName;
	private Integer status;
	private Long updateBy;
	private Long applyAmount;
	private Long headId;
	private String returnVehicleNo;
	private String isInstock;
	private Long addressId;
	private Integer isWareHousing;

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public String getIsInstock() {
		return isInstock;
	}

	public void setIsInstock(String isInstock) {
		this.isInstock = isInstock;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public Long getCompanyId() {
		return this.companyId;
	}

	public void setYieldly(Long yieldly) {
		this.yieldly = yieldly;
	}

	public Long getYieldly() {
		return this.yieldly;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Date getUpdateDate() {
		return this.updateDate;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getReason() {
		return this.reason;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getInvoiceNo() {
		return this.invoiceNo;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	public Long getCreateBy() {
		return this.createBy;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public Date getInvoiceDate() {
		return this.invoiceDate;
	}

	public void setAccountTypeId(Long accountTypeId) {
		this.accountTypeId = accountTypeId;
	}

	public Long getAccountTypeId() {
		return this.accountTypeId;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setDealerId(Long dealerId) {
		this.dealerId = dealerId;
	}

	public Long getDealerId() {
		return this.dealerId;
	}

	public void setInvoicePer(Long invoicePer) {
		this.invoicePer = invoicePer;
	}

	public Long getInvoicePer() {
		return this.invoicePer;
	}

	public void setAccountTypeName(String accountTypeName) {
		this.accountTypeName = accountTypeName;
	}

	public String getAccountTypeName() {
		return this.accountTypeName;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}

	public Long getUpdateBy() {
		return this.updateBy;
	}

	public void setApplyAmount(Long applyAmount) {
		this.applyAmount = applyAmount;
	}

	public Long getApplyAmount() {
		return this.applyAmount;
	}

	public void setHeadId(Long headId) {
		this.headId = headId;
	}

	public Long getHeadId() {
		return this.headId;
	}

	public void setReturnVehicleNo(String returnVehicleNo) {
		this.returnVehicleNo = returnVehicleNo;
	}

	public String getReturnVehicleNo() {
		return this.returnVehicleNo;
	}

	public Integer getIsWareHousing() {
		return isWareHousing;
	}

	public void setIsWareHousing(Integer isWareHousing) {
		this.isWareHousing = isWareHousing;
	}

}