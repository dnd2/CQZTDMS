/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-02-26 06:35:52
* CreateBy   : tianheng
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmOrgTypePO extends PO{

	private Date updateDate;
	private Long updateBy;
	private String orgTypeDesc;
	private Long createBy;
	private Integer orgTypeId;
	private Integer orgTypeClassification;
	private Date createDate;
	private String orgType;

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setOrgTypeDesc(String orgTypeDesc){
		this.orgTypeDesc=orgTypeDesc;
	}

	public String getOrgTypeDesc(){
		return this.orgTypeDesc;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setOrgTypeId(Integer orgTypeId){
		this.orgTypeId=orgTypeId;
	}

	public Integer getOrgTypeId(){
		return this.orgTypeId;
	}

	public void setOrgTypeClassification(Integer orgTypeClassification){
		this.orgTypeClassification=orgTypeClassification;
	}

	public Integer getOrgTypeClassification(){
		return this.orgTypeClassification;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setOrgType(String orgType){
		this.orgType=orgType;
	}

	public String getOrgType(){
		return this.orgType;
	}

}