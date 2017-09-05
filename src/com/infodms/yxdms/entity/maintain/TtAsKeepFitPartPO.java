/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-11-20 20:02:30
* CreateBy   : yuewei
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.yxdms.entity.maintain;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsKeepFitPartPO extends PO{

	private Double price;
	private String partName;
	private Long id;
	private Long keepFitId;
	private Double amount;
	private String partCode;
	private Integer partNum;
	private String payType;
	private String partUseType;
	private String realPartId; 
	
	public void setPrice(Double price){
		this.price=price;
	}

	public Double getPrice(){
		return this.price;
	}

	public void setPartName(String partName){
		this.partName=partName;
	}

	public String getPartName(){
		return this.partName;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
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

	public void setPartCode(String partCode){
		this.partCode=partCode;
	}

	public String getPartCode(){
		return this.partCode;
	}

	public void setPartNum(Integer partNum){
		this.partNum=partNum;
	}

	public Integer getPartNum(){
		return this.partNum;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getPartUseType() {
		return partUseType;
	}

	public void setPartUseType(String partUseType) {
		this.partUseType = partUseType;
	}

	public String getRealPartId() {
		return realPartId;
	}

	public void setRealPartId(String realPartId) {
		this.realPartId = realPartId;
	}

}