/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-11-04 10:55:33
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class NetSpeciaStrokePO extends PO{

	private String  speciaId;
	private String  provinceId;       
	private String  provinceName;     
	private String  cityId;          
	private String  cityName;          
	private Date    createDate;      
	private Date    updateDate;     
	private Date    businessDate;   
	private String  businessTraveller;
	private String  businessGoal;
	private String  businessJobam;
	private String  businessJobpm;
	private String  operatItemId;
	private String  operatItemName;
	private String  operatWayId;
	private String  operatWayName;
	private String  createBy;
	private String  status;
	private String  submitStatus;
	
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSubmitStatus() {
		return submitStatus;
	}
	public void setSubmitStatus(String submitStatus) {
		this.submitStatus = submitStatus;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getSpeciaId() {
		return speciaId;
	}
	public void setSpeciaId(String speciaId) {
		this.speciaId = speciaId;
	}
	public String getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public String getCityId() {
		return cityId;
	}
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public Date getBusinessDate() {
		return businessDate;
	}
	public void setBusinessDate(Date businessDate) {
		this.businessDate = businessDate;
	}
	public String getBusinessTraveller() {
		return businessTraveller;
	}
	public void setBusinessTraveller(String businessTraveller) {
		this.businessTraveller = businessTraveller;
	}
	public String getBusinessGoal() {
		return businessGoal;
	}
	public void setBusinessGoal(String businessGoal) {
		this.businessGoal = businessGoal;
	}
	public String getBusinessJobam() {
		return businessJobam;
	}
	public void setBusinessJobam(String businessJobam) {
		this.businessJobam = businessJobam;
	}
	public String getBusinessJobpm() {
		return businessJobpm;
	}
	public void setBusinessJobpm(String businessJobpm) {
		this.businessJobpm = businessJobpm;
	}
	public String getOperatItemId() {
		return operatItemId;
	}
	public void setOperatItemId(String operatItemId) {
		this.operatItemId = operatItemId;
	}
	public String getOperatItemName() {
		return operatItemName;
	}
	public void setOperatItemName(String operatItemName) {
		this.operatItemName = operatItemName;
	}
	public String getOperatWayId() {
		return operatWayId;
	}
	public void setOperatWayId(String operatWayId) {
		this.operatWayId = operatWayId;
	}
	public String getOperatWayName() {
		return operatWayName;
	}
	public void setOperatWayName(String operatWayName) {
		this.operatWayName = operatWayName;
	}
	
	
	
	
	
	
	

}