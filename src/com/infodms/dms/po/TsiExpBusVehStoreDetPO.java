/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-11-03 11:01:00
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TsiExpBusVehStoreDetPO extends PO{

	private String serialno;
	private Long revId;
	private Long detId;
	private String matdocItm;
	private Integer isRead;
	private Date createDate;
	private Long vehicleId;
	private String vin;
	private String inOutFlag;
	private String inOutDesc;
	private Date inOutDate;
	private Integer isSpecialCar;
	
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Long getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(Long vehicleId) {
		this.vehicleId = vehicleId;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getInOutFlag() {
		return inOutFlag;
	}

	public void setInOutFlag(String inOutFlag) {
		this.inOutFlag = inOutFlag;
	}

	public String getInOutDesc() {
		return inOutDesc;
	}

	public void setInOutDesc(String inOutDesc) {
		this.inOutDesc = inOutDesc;
	}

	public Date getInOutDate() {
		return inOutDate;
	}

	public void setInOutDate(Date inOutDate) {
		this.inOutDate = inOutDate;
	}

	public Integer getIsSpecialCar() {
		return isSpecialCar;
	}

	public void setIsSpecialCar(Integer isSpecialCar) {
		this.isSpecialCar = isSpecialCar;
	}

	public void setSerialno(String serialno){
		this.serialno=serialno;
	}

	public String getSerialno(){
		return this.serialno;
	}

	public void setRevId(Long revId){
		this.revId=revId;
	}

	public Long getRevId(){
		return this.revId;
	}

	public void setDetId(Long detId){
		this.detId=detId;
	}

	public Long getDetId(){
		return this.detId;
	}

	public void setMatdocItm(String matdocItm){
		this.matdocItm=matdocItm;
	}

	public String getMatdocItm(){
		return this.matdocItm;
	}

	public void setIsRead(Integer isRead){
		this.isRead=isRead;
	}

	public Integer getIsRead(){
		return this.isRead;
	}

}