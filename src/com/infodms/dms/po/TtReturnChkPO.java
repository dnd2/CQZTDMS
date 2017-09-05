/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-04-23 17:56:44
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtReturnChkPO extends PO{

	private Long returnId;
	private Date chkDate;
	private String chkDesc;
	private Long chkId;
	private Integer status;
	private Long chkOrgId;
	private Long chkBy;

	public void setReturnId(Long returnId){
		this.returnId=returnId;
	}

	public Long getReturnId(){
		return this.returnId;
	}

	public void setChkDate(Date chkDate){
		this.chkDate=chkDate;
	}

	public Date getChkDate(){
		return this.chkDate;
	}

	public void setChkDesc(String chkDesc){
		this.chkDesc=chkDesc;
	}

	public String getChkDesc(){
		return this.chkDesc;
	}

	public void setChkId(Long chkId){
		this.chkId=chkId;
	}

	public Long getChkId(){
		return this.chkId;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setChkOrgId(Long chkOrgId){
		this.chkOrgId=chkOrgId;
	}

	public Long getChkOrgId(){
		return this.chkOrgId;
	}

	public void setChkBy(Long chkBy){
		this.chkBy=chkBy;
	}

	public Long getChkBy(){
		return this.chkBy;
	}

}