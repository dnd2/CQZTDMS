/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-06-18 09:48:42
* CreateBy   : yuewei
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtDmsFlowPO extends PO{

	private Long id;
	private Long majorKey;
	private String auditName;
	private String auditDate;
	private String nextAuditName;
	private String nextAuditDate;
	private String remark;
	private Long status;
	private String auditAdvice;

	public String getAuditAdvice() {
		return auditAdvice;
	}

	public void setAuditAdvice(String auditAdvice) {
		this.auditAdvice = auditAdvice;
	}

	public void setMajorKey(Long majorKey){
		this.majorKey=majorKey;
	}

	public Long getMajorKey(){
		return this.majorKey;
	}

	public void setAuditName(String auditName){
		this.auditName=auditName;
	}

	public String getAuditName(){
		return this.auditName;
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

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setStatus(Long status){
		this.status=status;
	}

	public Long getStatus(){
		return this.status;
	}

	public String getNextAuditName() {
		return nextAuditName;
	}

	public void setNextAuditName(String nextAuditName) {
		this.nextAuditName = nextAuditName;
	}

	public String getNextAuditDate() {
		return nextAuditDate;
	}

	public void setNextAuditDate(String nextAuditDate) {
		this.nextAuditDate = nextAuditDate;
	}

}