/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-02-26 03:33:10
* CreateBy   : tianheng
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmActivityplanCostPO extends PO{

	private Double amount;
	private Long planCostId;
	private Integer costType;
	private Date updateDate;
	private Double auditAmountSum;
	private Double amountSum;
	private Long updateBy;
	private Long createBy;
	private Double price;
	private Long planId;
	private Date createDate;
	private String item;

	public void setAmount(Double amount){
		this.amount=amount;
	}

	public Double getAmount(){
		return this.amount;
	}

	public void setPlanCostId(Long planCostId){
		this.planCostId=planCostId;
	}

	public Long getPlanCostId(){
		return this.planCostId;
	}

	public void setCostType(Integer costType){
		this.costType=costType;
	}

	public Integer getCostType(){
		return this.costType;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setAuditAmountSum(Double auditAmountSum){
		this.auditAmountSum=auditAmountSum;
	}

	public Double getAuditAmountSum(){
		return this.auditAmountSum;
	}

	public void setAmountSum(Double amountSum){
		this.amountSum=amountSum;
	}

	public Double getAmountSum(){
		return this.amountSum;
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

	public void setPrice(Double price){
		this.price=price;
	}

	public Double getPrice(){
		return this.price;
	}

	public void setPlanId(Long planId){
		this.planId=planId;
	}

	public Long getPlanId(){
		return this.planId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setItem(String item){
		this.item=item;
	}

	public String getItem(){
		return this.item;
	}

}