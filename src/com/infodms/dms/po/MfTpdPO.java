/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-03-04 12:36:20
* CreateBy   : -Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class MfTpdPO extends PO{

	private Integer forcastMonth;
	private Long relId;
	private Long materialId;
	private Integer version;
	private Long updateBy;
	private Integer forcastYear;
	private Date updateDate;
	private Long createBy;
	private Date createDate;
	private Long amount;
	private Long totalNum;

	public void setForcastMonth(Integer forcastMonth){
		this.forcastMonth=forcastMonth;
	}

	public Integer getForcastMonth(){
		return this.forcastMonth;
	}

	public void setRelId(Long relId){
		this.relId=relId;
	}

	public Long getRelId(){
		return this.relId;
	}

	public void setMaterialId(Long materialId){
		this.materialId=materialId;
	}

	public Long getMaterialId(){
		return this.materialId;
	}

	public void setVersion(Integer version){
		this.version=version;
	}

	public Integer getVersion(){
		return this.version;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setForcastYear(Integer forcastYear){
		this.forcastYear=forcastYear;
	}

	public Integer getForcastYear(){
		return this.forcastYear;
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

	public void setAmount(Long amount){
		this.amount=amount;
	}

	public Long getAmount(){
		return this.amount;
	}

	public void setTotalNum(Long totalNum){
		this.totalNum=totalNum;
	}

	public Long getTotalNum(){
		return this.totalNum;
	}

}