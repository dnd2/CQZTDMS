/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2011-01-05 02:16:30
* CreateBy   : Iverson
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrReturnAuthitemPO extends PO{

	private String authRemark;
	private String authBoxNo;
	private Date updateDate;
	private Long createBy;
	private Date authTime;
	private Date createDate;
	private String authPersonLevel;
	private Integer authStatus;
	private Long returnId;
	private String authPersonName;
	private String authPersonRole;
	private Long authPersonId;
	private Long updateBy;
	private Long authOrgId;
	public String getIsReported() {
		return isReported;
	}

	public void setIsReported(String isReported) {
		this.isReported = isReported;
	}

	private String isReported;

	public void setAuthRemark(String authRemark){
		this.authRemark=authRemark;
	}

	public String getAuthRemark(){
		return this.authRemark;
	}

	public void setAuthBoxNo(String authBoxNo){
		this.authBoxNo=authBoxNo;
	}

	public String getAuthBoxNo(){
		return this.authBoxNo;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setAuthTime(Date authTime){
		this.authTime=authTime;
	}

	public Date getAuthTime(){
		return this.authTime;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setAuthPersonLevel(String authPersonLevel){
		this.authPersonLevel=authPersonLevel;
	}

	public String getAuthPersonLevel(){
		return this.authPersonLevel;
	}

	public void setAuthStatus(Integer authStatus){
		this.authStatus=authStatus;
	}

	public Integer getAuthStatus(){
		return this.authStatus;
	}

	public void setReturnId(Long returnId){
		this.returnId=returnId;
	}

	public Long getReturnId(){
		return this.returnId;
	}

	public void setAuthPersonName(String authPersonName){
		this.authPersonName=authPersonName;
	}

	public String getAuthPersonName(){
		return this.authPersonName;
	}

	public void setAuthPersonRole(String authPersonRole){
		this.authPersonRole=authPersonRole;
	}

	public String getAuthPersonRole(){
		return this.authPersonRole;
	}

	public void setAuthPersonId(Long authPersonId){
		this.authPersonId=authPersonId;
	}

	public Long getAuthPersonId(){
		return this.authPersonId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setAuthOrgId(Long authOrgId){
		this.authOrgId=authOrgId;
	}

	public Long getAuthOrgId(){
		return this.authOrgId;
	}

}