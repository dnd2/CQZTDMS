/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-05-14 14:18:19
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TrRoleActionPO extends PO{

	private Long roleActionId;
	private Long updateBy;
	private Date updateDate;
	private Long createBy;
	private Date createDate;
	private Long actionId;
	private Long roleId;

	public void setRoleActionId(Long roleActionId){
		this.roleActionId=roleActionId;
	}

	public Long getRoleActionId(){
		return this.roleActionId;
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

	public void setActionId(Long actionId){
		this.actionId=actionId;
	}

	public Long getActionId(){
		return this.actionId;
	}

	public void setRoleId(Long roleId){
		this.roleId=roleId;
	}

	public Long getRoleId(){
		return this.roleId;
	}

}