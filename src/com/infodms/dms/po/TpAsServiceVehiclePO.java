/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-08-26 13:52:46
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TpAsServiceVehiclePO extends PO{

	private String activityCode;
	private String vin;
	private String operateSstCode;
	private Integer ifStatus;
	private Long id;
	private Date campaignDate;

	public void setActivityCode(String activityCode){
		this.activityCode=activityCode;
	}

	public String getActivityCode(){
		return this.activityCode;
	}

	public void setVin(String vin){
		this.vin=vin;
	}

	public String getVin(){
		return this.vin;
	}

	public void setOperateSstCode(String operateSstCode){
		this.operateSstCode=operateSstCode;
	}

	public String getOperateSstCode(){
		return this.operateSstCode;
	}

	public void setIfStatus(Integer ifStatus){
		this.ifStatus=ifStatus;
	}

	public Integer getIfStatus(){
		return this.ifStatus;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public Date getCampaignDate() {
		return campaignDate;
	}

	public void setCampaignDate(Date campaignDate) {
		this.campaignDate = campaignDate;
	}

}