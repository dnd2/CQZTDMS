/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-07-09 10:04:10
* CreateBy   : wangsw
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtSalesPolicyPO extends PO{

	private Date startDate;
	private Date stopDate;
	private Long createUser;
	private Date deployTime;
	private Long companyId;
	private Date updateDate;
	private Integer deployStatus;
	private String policyName;
	private Date createDate;
	private Long updateUser;
	private Double dismoney;
	private Long policyId;
	private Long policyType;

	public void setStartDate(Date startDate){
		this.startDate=startDate;
	}

	public Date getStartDate(){
		return this.startDate;
	}

	public void setStopDate(Date stopDate){
		this.stopDate=stopDate;
	}

	public Date getStopDate(){
		return this.stopDate;
	}

	public void setCreateUser(Long createUser){
		this.createUser=createUser;
	}

	public Long getCreateUser(){
		return this.createUser;
	}

	public void setDeployTime(Date deployTime){
		this.deployTime=deployTime;
	}

	public Date getDeployTime(){
		return this.deployTime;
	}

	public void setCompanyId(Long companyId){
		this.companyId=companyId;
	}

	public Long getCompanyId(){
		return this.companyId;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setDeployStatus(Integer deployStatus){
		this.deployStatus=deployStatus;
	}

	public Integer getDeployStatus(){
		return this.deployStatus;
	}

	public void setPolicyName(String policyName){
		this.policyName=policyName;
	}

	public String getPolicyName(){
		return this.policyName;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setUpdateUser(Long updateUser){
		this.updateUser=updateUser;
	}

	public Long getUpdateUser(){
		return this.updateUser;
	}

	public void setDismoney(Double dismoney){
		this.dismoney=dismoney;
	}

	public Double getDismoney(){
		return this.dismoney;
	}

	public void setPolicyId(Long policyId){
		this.policyId=policyId;
	}

	public Long getPolicyId(){
		return this.policyId;
	}

	public Long getPolicyType() {
		return policyType;
	}

	public void setPolicyType(Long policyType) {
		this.policyType = policyType;
	}
	

}