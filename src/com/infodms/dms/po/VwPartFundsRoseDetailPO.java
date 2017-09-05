/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2015-04-29 18:25:38
* CreateBy   : Yu
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class VwPartFundsRoseDetailPO extends PO{

	private Integer isType;
	private Integer finType;
	private Long dealerId;
	private String createDate;
	private Integer amount;

	public void setIsType(Integer isType){
		this.isType=isType;
	}

	public Integer getIsType(){
		return this.isType;
	}

	public void setFinType(Integer finType){
		this.finType=finType;
	}

	public Integer getFinType(){
		return this.finType;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setCreateDate(String createDate){
		this.createDate=createDate;
	}

	public String getCreateDate(){
		return this.createDate;
	}

	public void setAmount(Integer amount){
		this.amount=amount;
	}

	public Integer getAmount(){
		return this.amount;
	}

}