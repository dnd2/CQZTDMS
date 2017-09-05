/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-07-12 09:44:02
* CreateBy   : Arthur_Liu
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TcRolePO extends PO{

	private String roleDesc;
	private Integer roleType;
	private Long oemCompanyId;
	private Long updateBy;
	private Integer roleStatus;
	private Date updateDate;
	private Long createBy;
	private Date createDate;
	private String roleName;
	private Long roleId;

	public void setRoleDesc(String roleDesc){
		this.roleDesc=roleDesc;
	}

	public String getRoleDesc(){
		return this.roleDesc;
	}

	public void setRoleType(Integer roleType){
		this.roleType=roleType;
	}

	public Integer getRoleType(){
		return this.roleType;
	}

	public void setOemCompanyId(Long oemCompanyId){
		this.oemCompanyId=oemCompanyId;
	}

	public Long getOemCompanyId(){
		return this.oemCompanyId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setRoleStatus(Integer roleStatus){
		this.roleStatus=roleStatus;
	}

	public Integer getRoleStatus(){
		return this.roleStatus;
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

	public void setRoleName(String roleName){
		this.roleName=roleName;
	}

	public String getRoleName(){
		return this.roleName;
	}

	public void setRoleId(Long roleId){
		this.roleId=roleId;
	}

	public Long getRoleId(){
		return this.roleId;
	}

}