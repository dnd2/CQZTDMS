/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-11-20 20:03:34
* CreateBy   : yuewei
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.yxdms.entity.maintain;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsKeepFitLabourPO extends PO{

	private Double price;
	private String labourName;
	private Long id;
	private Double labourNum;
	private Long keepFitId;
	private Double amount;
	private String labourCode;
	private String payType;

	public void setPrice(Double price){
		this.price=price;
	}

	public Double getPrice(){
		return this.price;
	}

	public void setLabourName(String labourName){
		this.labourName=labourName;
	}

	public String getLabourName(){
		return this.labourName;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setLabourNum(Double labourNum){
		this.labourNum=labourNum;
	}

	public Double getLabourNum(){
		return this.labourNum;
	}

	public void setKeepFitId(Long keepFitId){
		this.keepFitId=keepFitId;
	}

	public Long getKeepFitId(){
		return this.keepFitId;
	}

	public void setAmount(Double amount){
		this.amount=amount;
	}

	public Double getAmount(){
		return this.amount;
	}

	public void setLabourCode(String labourCode){
		this.labourCode=labourCode;
	}

	public String getLabourCode(){
		return this.labourCode;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

}