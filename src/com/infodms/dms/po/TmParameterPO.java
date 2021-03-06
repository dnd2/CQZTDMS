/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-12-23 16:26:57
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmParameterPO extends PO{

	private Integer parameterStatus;
	private String parameterCode;
	private Integer parameterType;
	private String parameterName;
	private Long id;
	private Long createBy;
	private Date createDate;
	private Double amount;
	private Integer timeout;

	public void setParameterStatus(Integer parameterStatus){
		this.parameterStatus=parameterStatus;
	}

	public Integer getParameterStatus(){
		return this.parameterStatus;
	}

	public void setParameterCode(String parameterCode){
		this.parameterCode=parameterCode;
	}

	public String getParameterCode(){
		return this.parameterCode;
	}

	public void setParameterType(Integer parameterType){
		this.parameterType=parameterType;
	}

	public Integer getParameterType(){
		return this.parameterType;
	}

	public void setParameterName(String parameterName){
		this.parameterName=parameterName;
	}

	public String getParameterName(){
		return this.parameterName;
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

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setAmount(Double amount){
		this.amount=amount;
	}

	public Double getAmount(){
		return this.amount;
	}

	public void setTimeout(Integer timeout){
		this.timeout=timeout;
	}

	public Integer getTimeout(){
		return this.timeout;
	}

}