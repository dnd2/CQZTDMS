/*
 * Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
 * This software is published under the terms of the Infoservice Software
 * License version 1.0, a copy of which has been included with this
 * distribution in the LICENSE.txt file.
 *
 * CreateDate : 2014-06-03 17:37:51
 * CreateBy   : Administrator
 * Comment    : generate by com.sgm.po.POGen
 */

package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmpTtSalesStoragePO extends PO {

	private String materialCode;
	private String userId;
	private String vin;
	private String specialOrderNo;
	private String offlineDate;
	private String orgStorageDate;
	private String engineNo;
	private String prodtuctDate;
	private String rowNumber;
	private String hegezhengCode;
	private String accPerCode;
	private String erpOrderId;
	private String inOutDesc;//出入库类型
	
	
	public String getInOutDesc() {
		return inOutDesc;
	}

	public void setInOutDesc(String inOutDesc) {
		this.inOutDesc = inOutDesc;
	}

	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}

	public String getMaterialCode() {
		return this.materialCode;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getVin() {
		return this.vin;
	}

	public void setSpecialOrderNo(String specialOrderNo) {
		this.specialOrderNo = specialOrderNo;
	}

	public String getSpecialOrderNo() {
		return this.specialOrderNo;
	}

	public void setOfflineDate(String offlineDate) {
		this.offlineDate = offlineDate;
	}

	public String getOfflineDate() {
		return this.offlineDate;
	}

	public void setOrgStorageDate(String orgStorageDate) {
		this.orgStorageDate = orgStorageDate;
	}

	public String getOrgStorageDate() {
		return this.orgStorageDate;
	}

	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}

	public String getEngineNo() {
		return this.engineNo;
	}

	public void setProdtuctDate(String prodtuctDate) {
		this.prodtuctDate = prodtuctDate;
	}

	public String getProdtuctDate() {
		return this.prodtuctDate;
	}

	public void setRowNumber(String rowNumber) {
		this.rowNumber = rowNumber;
	}

	public String getRowNumber() {
		return this.rowNumber;
	}

	public void setHegezhengCode(String hegezhengCode) {
		this.hegezhengCode = hegezhengCode;
	}

	public String getHegezhengCode() {
		return this.hegezhengCode;
	}

	public String getAccPerCode() {
		return accPerCode;
	}

	public void setAccPerCode(String accPerCode) {
		this.accPerCode = accPerCode;
	}

	public String getErpOrderId() {
		return erpOrderId;
	}

	public void setErpOrderId(String erpOrderId) {
		this.erpOrderId = erpOrderId;
	}

}