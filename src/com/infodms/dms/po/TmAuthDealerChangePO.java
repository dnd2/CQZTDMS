/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2011-03-21 16:53:51
* CreateBy   : Iverson
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmAuthDealerChangePO extends PO{

	private Long authBy;
	private Date authTime;
	private Long updateBy;
	private Date updateDate;
	private Long authStatus;
	private String authRemark;
	public Long getAuthBy() {
		return authBy;
	}

	public String getAuthRemark() {
		return authRemark;
	}

	public void setAuthRemark(String authRemark) {
		this.authRemark = authRemark;
	}

	public void setAuthBy(Long authBy) {
		this.authBy = authBy;
	}

	private Long id;
	private Long createBy;
	private Long authDealer;
	private Date createDate;

	

	public void setAuthTime(Date authTime){
		this.authTime=authTime;
	}

	public Date getAuthTime(){
		return this.authTime;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setAuthStatus(Long authStatus){
		this.authStatus=authStatus;
	}

	public Long getAuthStatus(){
		return this.authStatus;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setAuthDealer(Long authDealer){
		this.authDealer=authDealer;
	}

	public Long getAuthDealer(){
		return this.authDealer;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

}