/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-08-15 12:43:33
* CreateBy   : yuewei
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtSalesQualityAuditRecordPO extends PO{

	private String auditDept;
	private String auditDesc;
	private Long qualityId;
	private String auditDate;
	private Long id;
	private String operator;
	private String auditPerson;
	
	
	public TtSalesQualityAuditRecordPO(Long qualityId) {
		this.qualityId = qualityId;
	}

	public TtSalesQualityAuditRecordPO(Long id,Long qualityId, String auditDate,String auditDept,String auditPerson,String operator,String auditDesc) {
		super();
		this.auditDept = auditDept;
		this.auditDesc = auditDesc;
		this.qualityId = qualityId;
		this.auditDate = auditDate;
		this.id = id;
		this.operator = operator;
		this.auditPerson = auditPerson;
	}

	public void setAuditDept(String auditDept){
		this.auditDept=auditDept;
	}

	public String getAuditDept(){
		return this.auditDept;
	}

	public void setAuditDesc(String auditDesc){
		this.auditDesc=auditDesc;
	}

	public String getAuditDesc(){
		return this.auditDesc;
	}

	public void setQualityId(Long qualityId){
		this.qualityId=qualityId;
	}

	public Long getQualityId(){
		return this.qualityId;
	}

	public void setAuditDate(String auditDate){
		this.auditDate=auditDate;
	}

	public String getAuditDate(){
		return this.auditDate;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setOperator(String operator){
		this.operator=operator;
	}

	public String getOperator(){
		return this.operator;
	}

	public void setAuditPerson(String auditPerson){
		this.auditPerson=auditPerson;
	}

	public String getAuditPerson(){
		return this.auditPerson;
	}

}