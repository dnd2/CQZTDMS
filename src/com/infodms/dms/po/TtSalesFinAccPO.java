/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-04-10 17:14:30
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtSalesFinAccPO extends PO{

	private Long finType;
	private Double freezeAmount;
	private Long dealerId;
	private Long updateBy;
	private Date updateDate;
	private Long ver;
	private Long createBy;
	private Long accId;
	private Date createDate;
	private Long yieldly;
	private Double amount;

	public void setFinType(Long finType){
		this.finType=finType;
	}

	public Long getFinType(){
		return this.finType;
	}

	public void setFreezeAmount(Double freezeAmount){
		this.freezeAmount=freezeAmount;
	}

	public Double getFreezeAmount(){
		return this.freezeAmount;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
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

	public void setVer(Long ver){
		this.ver=ver;
	}

	public Long getVer(){
		return this.ver;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setAccId(Long accId){
		this.accId=accId;
	}

	public Long getAccId(){
		return this.accId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setYieldly(Long yieldly){
		this.yieldly=yieldly;
	}

	public Long getYieldly(){
		return this.yieldly;
	}

	public void setAmount(Double amount){
		this.amount=amount;
	}

	public Double getAmount(){
		return this.amount;
	}

}