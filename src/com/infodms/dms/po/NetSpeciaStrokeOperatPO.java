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
public class NetSpeciaStrokeOperatPO extends PO{

	private String  operatId;         
	private String  speciaId;         
	private String  companyName;       
	private String  intentLevel;       
	private String  companyNature;     
	private String  locationCircle;    
	private String  carBrand;         
	private String  companyAddress;    
	private String  dealerNegotiation; 
	private Date    createDate;      
	private Date    updateDate;       
	private Date    followDate;       
	private String  followWay;
	private String  negotiatContent;
	private String  createBy;
	
	
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	
	
	public String getNegotiatContent() {
		return negotiatContent;
	}
	public void setNegotiatContent(String negotiatContent) {
		this.negotiatContent = negotiatContent;
	}
	public String getOperatId() {
		return operatId;
	}
	public void setOperatId(String operatId) {
		this.operatId = operatId;
	}
	public String getSpeciaId() {
		return speciaId;
	}
	public void setSpeciaId(String speciaId) {
		this.speciaId = speciaId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getIntentLevel() {
		return intentLevel;
	}
	public void setIntentLevel(String intentLevel) {
		this.intentLevel = intentLevel;
	}
	public String getCompanyNature() {
		return companyNature;
	}
	public void setCompanyNature(String companyNature) {
		this.companyNature = companyNature;
	}
	public String getLocationCircle() {
		return locationCircle;
	}
	public void setLocationCircle(String locationCircle) {
		this.locationCircle = locationCircle;
	}
	public String getCarBrand() {
		return carBrand;
	}
	public void setCarBrand(String carBrand) {
		this.carBrand = carBrand;
	}
	public String getCompanyAddress() {
		return companyAddress;
	}
	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}
	public String getDealerNegotiation() {
		return dealerNegotiation;
	}
	public void setDealerNegotiation(String dealerNegotiation) {
		this.dealerNegotiation = dealerNegotiation;
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
	public Date getFollowDate() {
		return followDate;
	}
	public void setFollowDate(Date followDate) {
		this.followDate = followDate;
	}
	public String getFollowWay() {
		return followWay;
	}
	public void setFollowWay(String followWay) {
		this.followWay = followWay;
	}         

	
	
	
	
	
	
	
	
	
	
	
	

}