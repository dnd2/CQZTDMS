/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-09-30 22:31:24
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtTaxableSumAuthitemPO extends PO{

	private Date authTime;
	private Long balanceId;
	private String authPersonName;
	private String authRemark;
	private Integer authStatus;
	private Date updateDate;
	private String authPersonLevel;
	private Long createBy;
	private Long authOrgId;
	private Long updateBy;
	private Long authPersonId;
	private String authPersonRole;
	private Date createDate;

	public void setAuthTime(Date authTime){
		this.authTime=authTime;
	}

	public Date getAuthTime(){
		return this.authTime;
	}

	public void setBalanceId(Long balanceId){
		this.balanceId=balanceId;
	}

	public Long getBalanceId(){
		return this.balanceId;
	}

	public void setAuthPersonName(String authPersonName){
		this.authPersonName=authPersonName;
	}

	public String getAuthPersonName(){
		return this.authPersonName;
	}

	public void setAuthRemark(String authRemark){
		this.authRemark=authRemark;
	}

	public String getAuthRemark(){
		return this.authRemark;
	}

	public void setAuthStatus(Integer authStatus){
		this.authStatus=authStatus;
	}

	public Integer getAuthStatus(){
		return this.authStatus;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setAuthPersonLevel(String authPersonLevel){
		this.authPersonLevel=authPersonLevel;
	}

	public String getAuthPersonLevel(){
		return this.authPersonLevel;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setAuthOrgId(Long authOrgId){
		this.authOrgId=authOrgId;
	}

	public Long getAuthOrgId(){
		return this.authOrgId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setAuthPersonId(Long authPersonId){
		this.authPersonId=authPersonId;
	}

	public Long getAuthPersonId(){
		return this.authPersonId;
	}

	public void setAuthPersonRole(String authPersonRole){
		this.authPersonRole=authPersonRole;
	}

	public String getAuthPersonRole(){
		return this.authPersonRole;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

}