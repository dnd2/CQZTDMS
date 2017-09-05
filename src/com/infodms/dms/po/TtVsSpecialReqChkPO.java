/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-01-14 09:54:59
* CreateBy   : wswcx
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsSpecialReqChkPO extends PO{

	private Date chkDate;
	private Long reqId;
	private String chkDesc;
	private Long updateBy;
	private Integer checkType;
	private Long chkId;
	private Date updateDate;
	private Long chkUserId;
	private Long createBy;
	private Date createDate;
	private Long chkOrgId;
	private Integer chkStatus;

	public void setChkDate(Date chkDate){
		this.chkDate=chkDate;
	}

	public Date getChkDate(){
		return this.chkDate;
	}

	public void setReqId(Long reqId){
		this.reqId=reqId;
	}

	public Long getReqId(){
		return this.reqId;
	}

	public void setChkDesc(String chkDesc){
		this.chkDesc=chkDesc;
	}

	public String getChkDesc(){
		return this.chkDesc;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setCheckType(Integer checkType){
		this.checkType=checkType;
	}

	public Integer getCheckType(){
		return this.checkType;
	}

	public void setChkId(Long chkId){
		this.chkId=chkId;
	}

	public Long getChkId(){
		return this.chkId;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setChkUserId(Long chkUserId){
		this.chkUserId=chkUserId;
	}

	public Long getChkUserId(){
		return this.chkUserId;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setChkOrgId(Long chkOrgId){
		this.chkOrgId=chkOrgId;
	}

	public Long getChkOrgId(){
		return this.chkOrgId;
	}

	public void setChkStatus(Integer chkStatus){
		this.chkStatus=chkStatus;
	}

	public Integer getChkStatus(){
		return this.chkStatus;
	}

}