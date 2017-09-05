/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-12-23 10:25:21
* CreateBy   : wizard_lee
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartMakerProblemDtlPO extends PO{

	private Long dtlId;
	private String supplyName;
	private String partName;
	private Long partId;
	private Long supplyId;
	private Long createBy;
	private Long problemId;
	private Date createDate;
	private String partCode;
	private String supplyCode;

	public void setDtlId(Long dtlId){
		this.dtlId=dtlId;
	}

	public Long getDtlId(){
		return this.dtlId;
	}

	public void setSupplyName(String supplyName){
		this.supplyName=supplyName;
	}

	public String getSupplyName(){
		return this.supplyName;
	}

	public void setPartName(String partName){
		this.partName=partName;
	}

	public String getPartName(){
		return this.partName;
	}

	public void setPartId(Long partId){
		this.partId=partId;
	}

	public Long getPartId(){
		return this.partId;
	}

	public void setSupplyId(Long supplyId){
		this.supplyId=supplyId;
	}

	public Long getSupplyId(){
		return this.supplyId;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setProblemId(Long problemId){
		this.problemId=problemId;
	}

	public Long getProblemId(){
		return this.problemId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setPartCode(String partCode){
		this.partCode=partCode;
	}

	public String getPartCode(){
		return this.partCode;
	}

	public void setSupplyCode(String supplyCode){
		this.supplyCode=supplyCode;
	}

	public String getSupplyCode(){
		return this.supplyCode;
	}

}