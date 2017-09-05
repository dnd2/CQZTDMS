/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-07-19 16:47:40
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmOrgCustomPO extends PO{

	private Long updateBy;
	private String orgName;
	private Date updateDate;
	private Long createBy;
	private Integer orgLevel;
	private Long parentOrgId;
	private String orgCode;
	private Date createDate;
	private Integer status;
	private Long orgId;

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setOrgName(String orgName){
		this.orgName=orgName;
	}

	public String getOrgName(){
		return this.orgName;
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

	public void setOrgLevel(Integer orgLevel){
		this.orgLevel=orgLevel;
	}

	public Integer getOrgLevel(){
		return this.orgLevel;
	}

	public void setParentOrgId(Long parentOrgId){
		this.parentOrgId=parentOrgId;
	}

	public Long getParentOrgId(){
		return this.parentOrgId;
	}

	public void setOrgCode(String orgCode){
		this.orgCode=orgCode;
	}

	public String getOrgCode(){
		return this.orgCode;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setOrgId(Long orgId){
		this.orgId=orgId;
	}

	public Long getOrgId(){
		return this.orgId;
	}

}