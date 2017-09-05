/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-02-26 03:33:10
* CreateBy   : tianheng
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmActivityplanAuditlogPO extends PO{

	private Long createBy;
	private Long planId;
	private Long orgId;
	private String auditlog;
	private Integer logType;
	private Date createDate;
	private Long auditlogId;

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setPlanId(Long planId){
		this.planId=planId;
	}

	public Long getPlanId(){
		return this.planId;
	}

	public void setOrgId(Long orgId){
		this.orgId=orgId;
	}

	public Long getOrgId(){
		return this.orgId;
	}

	public void setAuditlog(String auditlog){
		this.auditlog=auditlog;
	}

	public String getAuditlog(){
		return this.auditlog;
	}

	public void setLogType(Integer logType){
		this.logType=logType;
	}

	public Integer getLogType(){
		return this.logType;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setAuditlogId(Long auditlogId){
		this.auditlogId=auditlogId;
	}

	public Long getAuditlogId(){
		return this.auditlogId;
	}

}