/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-07-20 10:31:21
* CreateBy   : KFQ
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsPackgeChangeDetailPO extends PO{

	private Date auditDate;
	private Long id;
	private Double auditAcount;
	private String auditRemark;
	private Integer auditStatus;
	private Long auditPerson;
	private Long applyId;

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

	public void setAuditAcount(Double auditAcount){
		this.auditAcount=auditAcount;
	}

	public Double getAuditAcount(){
		return this.auditAcount;
	}

	public void setAuditRemark(String auditRemark){
		this.auditRemark=auditRemark;
	}

	public String getAuditRemark(){
		return this.auditRemark;
	}

	public void setAuditStatus(Integer auditStatus){
		this.auditStatus=auditStatus;
	}

	public Integer getAuditStatus(){
		return this.auditStatus;
	}

	public void setAuditPerson(Long auditPerson){
		this.auditPerson=auditPerson;
	}

	public Long getAuditPerson(){
		return this.auditPerson;
	}

	public void setApplyId(Long applyId){
		this.applyId=applyId;
	}

	public Long getApplyId(){
		return this.applyId;
	}

}