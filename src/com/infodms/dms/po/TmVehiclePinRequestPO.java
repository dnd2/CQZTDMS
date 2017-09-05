/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-12-15 17:18:12
* CreateBy   : nova
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmVehiclePinRequestPO extends PO{

	private Long pinId;
	private Long dealerId;
	private Long updateBy;
	private String pinCode;
	private String vin;
	private String pin;
	
	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	private Date updateDate;
	private String backRemark;
	private Date makeDate;
	private Long createBy;
	private String remark;
	private Date createDate;
	private Integer status;


	public Long getPinId() {
		return pinId;
	}

	public void setPinId(Long pinId) {
		this.pinId = pinId;
	}

	public Long getDealerId() {
		return dealerId;
	}

	public void setDealerId(Long dealerId) {
		this.dealerId = dealerId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setPinCode(String pinCode){
		this.pinCode=pinCode;
	}

	public String getPinCode(){
		return this.pinCode;
	}

	public void setVin(String vin){
		this.vin=vin;
	}

	public String getVin(){
		return this.vin;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setBackRemark(String backRemark){
		this.backRemark=backRemark;
	}

	public String getBackRemark(){
		return this.backRemark;
	}

	public void setMakeDate(Date makeDate){
		this.makeDate=makeDate;
	}

	public Date getMakeDate(){
		return this.makeDate;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

}