/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-02-22 17:14:44
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.math.BigDecimal;
import java.util.Date;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtFleetSupportPO extends PO{

	private Date supportDate;
	private Date updateDate;
	private BigDecimal updateBy;
	private Long createBy;
	private Long supportId;
	private Date createDate;
	private Long supportStatus;
	private Long fleetId;
	private String supportAuditRemark;
	private Long dealerId;
	private String supportRemark;

	public void setSupportDate(Date supportDate){
		this.supportDate=supportDate;
	}

	public Date getSupportDate(){
		return this.supportDate;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setUpdateBy(BigDecimal updateBy){
		this.updateBy=updateBy;
	}

	public BigDecimal getUpdateBy(){
		return this.updateBy;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setSupportId(Long supportId){
		this.supportId=supportId;
	}

	public Long getSupportId(){
		return this.supportId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setSupportStatus(Long supportStatus){
		this.supportStatus=supportStatus;
	}

	public Long getSupportStatus(){
		return this.supportStatus;
	}

	public void setFleetId(Long fleetId){
		this.fleetId=fleetId;
	}

	public Long getFleetId(){
		return this.fleetId;
	}

	public void setSupportAuditRemark(String supportAuditRemark){
		this.supportAuditRemark=supportAuditRemark;
	}

	public String getSupportAuditRemark(){
		return this.supportAuditRemark;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setSupportRemark(String supportRemark){
		this.supportRemark=supportRemark;
	}

	public String getSupportRemark(){
		return this.supportRemark;
	}

}