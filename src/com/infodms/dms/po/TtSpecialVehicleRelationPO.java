/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-07-22 10:32:25
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtSpecialVehicleRelationPO extends PO{

	private Integer state;
	private Long id;
	private Integer lowerRole;
	private String roleName;
	private Integer type;
	private Integer roleId;
	private Integer higherRole;

	public void setState(Integer state){
		this.state=state;
	}

	public Integer getState(){
		return this.state;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setLowerRole(Integer lowerRole){
		this.lowerRole=lowerRole;
	}

	public Integer getLowerRole(){
		return this.lowerRole;
	}

	public void setRoleName(String roleName){
		this.roleName=roleName;
	}

	public String getRoleName(){
		return this.roleName;
	}

	public void setType(Integer type){
		this.type=type;
	}

	public Integer getType(){
		return this.type;
	}

	public void setRoleId(Integer roleId){
		this.roleId=roleId;
	}

	public Integer getRoleId(){
		return this.roleId;
	}

	public void setHigherRole(Integer higherRole){
		this.higherRole=higherRole;
	}

	public Integer getHigherRole(){
		return this.higherRole;
	}

}