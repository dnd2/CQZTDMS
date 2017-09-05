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

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmpTtCityDisPO extends PO {

	private String price;
	private String fromPlace;
	private String desCounty;
	private String vehSys;
	private String desProvice;
	private String rowNumber;
	private String transWay;
	private String fuelCoefficeient;
	private String distence;
	private String userId;
	private String desCity;
	private String reachDay;
	private String fuelBeginDate;
	private String fuelEndDate;
	private String handPrice;
	private String remark;
	
	public String getHandPrice() {
		return handPrice;
	}

	public void setHandPrice(String handPrice) {
		this.handPrice = handPrice;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getPrice() {
		return this.price;
	}

	public void setFromPlace(String fromPlace) {
		this.fromPlace = fromPlace;
	}

	public String getFromPlace() {
		return this.fromPlace;
	}

	public void setDesCounty(String desCounty) {
		this.desCounty = desCounty;
	}

	public String getDesCounty() {
		return this.desCounty;
	}

	public void setVehSys(String vehSys) {
		this.vehSys = vehSys;
	}

	public String getVehSys() {
		return this.vehSys;
	}

	public void setDesProvice(String desProvice) {
		this.desProvice = desProvice;
	}

	public String getDesProvice() {
		return this.desProvice;
	}

	public void setRowNumber(String rowNumber) {
		this.rowNumber = rowNumber;
	}

	public String getRowNumber() {
		return this.rowNumber;
	}

	public void setTransWay(String transWay) {
		this.transWay = transWay;
	}

	public String getTransWay() {
		return this.transWay;
	}

	public void setFuelCoefficeient(String fuelCoefficeient) {
		this.fuelCoefficeient = fuelCoefficeient;
	}

	public String getFuelCoefficeient() {
		return this.fuelCoefficeient;
	}

	public void setDistence(String distence) {
		this.distence = distence;
	}

	public String getDistence() {
		return this.distence;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDesCity() {
		return desCity;
	}

	public void setDesCity(String desCity) {
		this.desCity = desCity;
	}

	public String getReachDay() {
		return reachDay;
	}

	public void setReachDay(String reachDay) {
		this.reachDay = reachDay;
	}

	public String getFuelBeginDate() {
		return fuelBeginDate;
	}

	public void setFuelBeginDate(String fuelBeginDate) {
		this.fuelBeginDate = fuelBeginDate;
	}

	public String getFuelEndDate() {
		return fuelEndDate;
	}

	public void setFuelEndDate(String fuelEndDate) {
		this.fuelEndDate = fuelEndDate;
	}
}