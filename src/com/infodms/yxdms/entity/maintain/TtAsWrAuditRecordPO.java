/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2017-08-11 14:21:21
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.yxdms.entity.maintain;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrAuditRecordPO extends PO{

	private Long appClaimId;
	private String auditRecord;
	private Integer operaStstus;
	private Long auditBy;
	private Date auditDate;
	private Long id;

	public void setAppClaimId(Long appClaimId){
		this.appClaimId=appClaimId;
	}

	public Long getAppClaimId(){
		return this.appClaimId;
	}

	public void setAuditRecord(String auditRecord){
		this.auditRecord=auditRecord;
	}

	public String getAuditRecord(){
		return this.auditRecord;
	}

	public void setOperaStstus(Integer operaStstus){
		this.operaStstus=operaStstus;
	}

	public Integer getOperaStstus(){
		return this.operaStstus;
	}

	public void setAuditBy(Long auditBy){
		this.auditBy=auditBy;
	}

	public Long getAuditBy(){
		return this.auditBy;
	}

	public void setAuditDate(Date auditDate){
		this.auditDate=auditDate;
	}

	public Date getAuditDate(){
		return this.auditDate;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

}