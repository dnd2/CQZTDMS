/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-07-15 14:57:27
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsResourcePO extends PO{

	private Long companyId;
	private Integer initInventAmount;
	private Integer resourceYear;
	private Date updateDate;
	private Long resourceId;
	private Integer resourceWeek;
	private Long createBy;
	private Integer checkAmount;
	private Date createDate;
	private String specialBatchNo;
	private Long updateBy;
	private Long materialId;
	private Integer resourceMonth;
	private Integer resourceAmount;
	private Integer availPlanAmount;

	public void setCompanyId(Long companyId){
		this.companyId=companyId;
	}

	public Long getCompanyId(){
		return this.companyId;
	}

	public void setInitInventAmount(Integer initInventAmount){
		this.initInventAmount=initInventAmount;
	}

	public Integer getInitInventAmount(){
		return this.initInventAmount;
	}

	public void setResourceYear(Integer resourceYear){
		this.resourceYear=resourceYear;
	}

	public Integer getResourceYear(){
		return this.resourceYear;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setResourceId(Long resourceId){
		this.resourceId=resourceId;
	}

	public Long getResourceId(){
		return this.resourceId;
	}

	public void setResourceWeek(Integer resourceWeek){
		this.resourceWeek=resourceWeek;
	}

	public Integer getResourceWeek(){
		return this.resourceWeek;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setCheckAmount(Integer checkAmount){
		this.checkAmount=checkAmount;
	}

	public Integer getCheckAmount(){
		return this.checkAmount;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setSpecialBatchNo(String specialBatchNo){
		this.specialBatchNo=specialBatchNo;
	}

	public String getSpecialBatchNo(){
		return this.specialBatchNo;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setMaterialId(Long materialId){
		this.materialId=materialId;
	}

	public Long getMaterialId(){
		return this.materialId;
	}

	public void setResourceMonth(Integer resourceMonth){
		this.resourceMonth=resourceMonth;
	}

	public Integer getResourceMonth(){
		return this.resourceMonth;
	}

	public void setResourceAmount(Integer resourceAmount){
		this.resourceAmount=resourceAmount;
	}

	public Integer getResourceAmount(){
		return this.resourceAmount;
	}

	public void setAvailPlanAmount(Integer availPlanAmount){
		this.availPlanAmount=availPlanAmount;
	}

	public Integer getAvailPlanAmount(){
		return this.availPlanAmount;
	}

}