/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-02-24 10:13:55
* CreateBy   : xwj
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmActivityplanResultPO extends PO{

	private Long companyId;
	private Date factActionDate;
	private Date updateDate;
	private Long activityResultId;
	private Long customerId;
	private Long updateBy;
	private Long intentId;
	private Long createBy;
	private Long planId;
	private String saleAdviserId;
	private Date createDate;
	private Long dealerId;

	public void setCompanyId(Long companyId){
		this.companyId=companyId;
	}

	public Long getCompanyId(){
		return this.companyId;
	}

	public void setFactActionDate(Date factActionDate){
		this.factActionDate=factActionDate;
	}

	public Date getFactActionDate(){
		return this.factActionDate;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setActivityResultId(Long activityResultId){
		this.activityResultId=activityResultId;
	}

	public Long getActivityResultId(){
		return this.activityResultId;
	}

	public void setCustomerId(Long customerId){
		this.customerId=customerId;
	}

	public Long getCustomerId(){
		return this.customerId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setIntentId(Long intentId){
		this.intentId=intentId;
	}

	public Long getIntentId(){
		return this.intentId;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setPlanId(Long planId){
		this.planId=planId;
	}

	public Long getPlanId(){
		return this.planId;
	}

	public void setSaleAdviserId(String saleAdviserId){
		this.saleAdviserId=saleAdviserId;
	}

	public String getSaleAdviserId(){
		return this.saleAdviserId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

}