/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2015-03-31 11:17:08
* CreateBy   : yuewei
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.yxdms.entity.special;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrSpecialRecordPO extends PO{

	private Integer specialType;
	private String auditRecord;
	private Integer operaStstus;
	private Long specialId;
	private Long auditBy;
	private Date auditDate;
	private Long id;

	public void setSpecialType(Integer specialType){
		this.specialType=specialType;
	}

	public Integer getSpecialType(){
		return this.specialType;
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

	public void setSpecialId(Long specialId){
		this.specialId=specialId;
	}

	public Long getSpecialId(){
		return this.specialId;
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