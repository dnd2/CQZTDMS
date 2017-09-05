/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-09-16 16:00:49
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtCrComplaintsCallPO extends PO{

	private String shut;
	private Long compId;
	private String callPerson;
	private String callFail;
	private Date callDate;
	private String satisfied;
	private String support;
	private String cause;
	private String supportPerson;
	private String remark;
	private String callCycle;
	private Long id;

	public void setShut(String shut){
		this.shut=shut;
	}

	public String getShut(){
		return this.shut;
	}

	public void setCompId(Long compId){
		this.compId=compId;
	}

	public Long getCompId(){
		return this.compId;
	}

	public void setCallPerson(String callPerson){
		this.callPerson=callPerson;
	}

	public String getCallPerson(){
		return this.callPerson;
	}

	public void setCallFail(String callFail){
		this.callFail=callFail;
	}

	public String getCallFail(){
		return this.callFail;
	}

	public void setCallDate(Date callDate){
		this.callDate=callDate;
	}

	public Date getCallDate(){
		return this.callDate;
	}

	public void setSatisfied(String satisfied){
		this.satisfied=satisfied;
	}

	public String getSatisfied(){
		return this.satisfied;
	}

	public void setSupport(String support){
		this.support=support;
	}

	public String getSupport(){
		return this.support;
	}

	public void setCause(String cause){
		this.cause=cause;
	}

	public String getCause(){
		return this.cause;
	}

	public void setSupportPerson(String supportPerson){
		this.supportPerson=supportPerson;
	}

	public String getSupportPerson(){
		return this.supportPerson;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setCallCycle(String callCycle){
		this.callCycle=callCycle;
	}

	public String getCallCycle(){
		return this.callCycle;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

}