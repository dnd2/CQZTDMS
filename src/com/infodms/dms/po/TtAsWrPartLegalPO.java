/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-04-15 15:07:52
* CreateBy   : ray
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrPartLegalPO extends PO{

	private String partLegalName;
	private String partLegalCode;
	private Long updateBy;
	private Date updateDate;
	private Long createBy;
	private Date createDate;
	private Integer status;
	private Long partsAssemblyId;
	private Long partLegalId;
	private Integer isDe;

	public void setPartLegalName(String partLegalName){
		this.partLegalName=partLegalName;
	}

	public String getPartLegalName(){
		return this.partLegalName;
	}

	public void setPartLegalCode(String partLegalCode){
		this.partLegalCode=partLegalCode;
	}

	public String getPartLegalCode(){
		return this.partLegalCode;
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

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setPartsAssemblyId(Long partsAssemblyId){
		this.partsAssemblyId=partsAssemblyId;
	}

	public Long getPartsAssemblyId(){
		return this.partsAssemblyId;
	}

	public void setPartLegalId(Long partLegalId){
		this.partLegalId=partLegalId;
	}

	public Long getPartLegalId(){
		return this.partLegalId;
	}

	public void setIsDe(Integer isDe){
		this.isDe=isDe;
	}

	public Integer getIsDe(){
		return this.isDe;
	}

}