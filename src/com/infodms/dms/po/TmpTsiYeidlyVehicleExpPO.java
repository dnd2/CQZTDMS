/*
 * Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
 * This software is published under the terms of the Infoservice Software
 * License version 1.0, a copy of which has been included with this
 * distribution in the LICENSE.txt file.
 *
 * CreateDate : 2014-05-27 09:41:02
 * CreateBy   : Administrator
 * Comment    : generate by com.sgm.po.POGen
 */

package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmpTsiYeidlyVehicleExpPO extends PO {

	private Long id;
	private String rowNumber;
	private String vin;
	private String materialCode;
	private String materialName;
	private String colorCode;
	private String colorName;
	private String engineNo;
	private String hegezhengCode;
	private String productDate;
	private String offlineDate;
	private Date createDate;
	private Long createBy;
	private String gearboxNo;
	
	public String getGearboxNo() {
		return gearboxNo;
	}
	public void setGearboxNo(String gearboxNo) {
		this.gearboxNo = gearboxNo;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRowNumber() {
		return rowNumber;
	}
	public void setRowNumber(String rowNumber) {
		this.rowNumber = rowNumber;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public String getMaterialCode() {
		return materialCode;
	}
	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}
	public String getMaterialName() {
		return materialName;
	}
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	public String getColorCode() {
		return colorCode;
	}
	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}
	public String getColorName() {
		return colorName;
	}
	public void setColorName(String colorName) {
		this.colorName = colorName;
	}
	public String getEngineNo() {
		return engineNo;
	}
	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}
	public String getHegezhengCode() {
		return hegezhengCode;
	}
	public void setHegezhengCode(String hegezhengCode) {
		this.hegezhengCode = hegezhengCode;
	}
	public String getProductDate() {
		return productDate;
	}
	public void setProductDate(String productDate) {
		this.productDate = productDate;
	}
	public String getOfflineDate() {
		return offlineDate;
	}
	public void setOfflineDate(String offlineDate) {
		this.offlineDate = offlineDate;
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