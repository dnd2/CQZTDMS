/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2017-07-21 17:18:02
* CreateBy   : zhumingwei
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartVenderPO extends PO{

	private String sortNo;
	private Integer state;
	private Date deleteDate;
	private String chgNameRemark;
	private String remark;
	private Long deleteUser;
	private Long status;
	private Long roleId;
	private Date modifyDate;
	private Long orgId;
	private Long createUser;
	private Long partId;
	private Long svId;
	private Long venderId;
	private Date createDate;
	private Integer isDefult;
	private Long modifyUser;

	public void setSortNo(String sortNo){
		this.sortNo=sortNo;
	}

	public String getSortNo(){
		return this.sortNo;
	}

	public void setState(Integer state){
		this.state=state;
	}

	public Integer getState(){
		return this.state;
	}

	public void setDeleteDate(Date deleteDate){
		this.deleteDate=deleteDate;
	}

	public Date getDeleteDate(){
		return this.deleteDate;
	}

	public void setChgNameRemark(String chgNameRemark){
		this.chgNameRemark=chgNameRemark;
	}

	public String getChgNameRemark(){
		return this.chgNameRemark;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setDeleteUser(Long deleteUser){
		this.deleteUser=deleteUser;
	}

	public Long getDeleteUser(){
		return this.deleteUser;
	}

	public void setStatus(Long status){
		this.status=status;
	}

	public Long getStatus(){
		return this.status;
	}

	public void setRoleId(Long roleId){
		this.roleId=roleId;
	}

	public Long getRoleId(){
		return this.roleId;
	}

	public void setModifyDate(Date modifyDate){
		this.modifyDate=modifyDate;
	}

	public Date getModifyDate(){
		return this.modifyDate;
	}

	public void setOrgId(Long orgId){
		this.orgId=orgId;
	}

	public Long getOrgId(){
		return this.orgId;
	}

	public void setCreateUser(Long createUser){
		this.createUser=createUser;
	}

	public Long getCreateUser(){
		return this.createUser;
	}

	public void setPartId(Long partId){
		this.partId=partId;
	}

	public Long getPartId(){
		return this.partId;
	}

	public void setSvId(Long svId){
		this.svId=svId;
	}

	public Long getSvId(){
		return this.svId;
	}

	public void setVenderId(Long venderId){
		this.venderId=venderId;
	}

	public Long getVenderId(){
		return this.venderId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setIsDefult(Integer isDefult){
		this.isDefult=isDefult;
	}

	public Integer getIsDefult(){
		return this.isDefult;
	}

	public void setModifyUser(Long modifyUser){
		this.modifyUser=modifyUser;
	}

	public Long getModifyUser(){
		return this.modifyUser;
	}

}