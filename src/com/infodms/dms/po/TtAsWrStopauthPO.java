/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-01-10 02:54:11
* CreateBy   : zhuwei
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrStopauthPO extends PO{

	private Integer authStatus;
	private Long createBy;
	private Date authDate;
	private Long authBy;
	private Date createDate;
	private Long reportId;
	private String remark;
	private Long id;

	public void setAuthStatus(Integer authStatus){
		this.authStatus=authStatus;
	}

	public Integer getAuthStatus(){
		return this.authStatus;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setAuthDate(Date authDate){
		this.authDate=authDate;
	}

	public Date getAuthDate(){
		return this.authDate;
	}

	public void setAuthBy(Long authBy){
		this.authBy=authBy;
	}

	public Long getAuthBy(){
		return this.authBy;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setReportId(Long reportId){
		this.reportId=reportId;
	}

	public Long getReportId(){
		return this.reportId;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

}