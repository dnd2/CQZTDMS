/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-06-07 09:43:49
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.math.BigDecimal;
import java.util.Date;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrModelPartGuaranteesPO extends PO{

	private BigDecimal gurnMonth;
	private Date updateDate;
	private Long updateBy;
	private Long createBy;
	private Integer partType;
	private Long gurnId;
	private BigDecimal gurnMile;
	private Date createDate;
	private Long modelId;

	public void setGurnMonth(BigDecimal gurnMonth){
		this.gurnMonth=gurnMonth;
	}

	public BigDecimal getGurnMonth(){
		return this.gurnMonth;
	}

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

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setPartType(Integer partType){
		this.partType=partType;
	}

	public Integer getPartType(){
		return this.partType;
	}

	public void setGurnId(Long gurnId){
		this.gurnId=gurnId;
	}

	public Long getGurnId(){
		return this.gurnId;
	}

	public void setGurnMile(BigDecimal gurnMile){
		this.gurnMile=gurnMile;
	}

	public BigDecimal getGurnMile(){
		return this.gurnMile;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setModelId(Long modelId){
		this.modelId=modelId;
	}

	public Long getModelId(){
		return this.modelId;
	}

}