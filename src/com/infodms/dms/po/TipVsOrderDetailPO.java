/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-09-30 20:36:00
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TipVsOrderDetailPO extends PO{

	private String billNo;
	private Float discountRate;
	private Integer amount;
	private String materialCode;
	private Double discountSPrice;
	private String materialName;
	private Double discountPrice;
	private Double singlePrice;
	private Long headId;
	private String isErr;
	private String errMessage;
	private Long lineId;

	public void setBillNo(String billNo){
		this.billNo=billNo;
	}

	public String getBillNo(){
		return this.billNo;
	}

	public void setDiscountRate(Float discountRate){
		this.discountRate=discountRate;
	}

	public Float getDiscountRate(){
		return this.discountRate;
	}

	public void setAmount(Integer amount){
		this.amount=amount;
	}

	public Integer getAmount(){
		return this.amount;
	}

	public void setMaterialCode(String materialCode){
		this.materialCode=materialCode;
	}

	public String getMaterialCode(){
		return this.materialCode;
	}

	public void setDiscountSPrice(Double discountSPrice){
		this.discountSPrice=discountSPrice;
	}

	public Double getDiscountSPrice(){
		return this.discountSPrice;
	}

	public void setMaterialName(String materialName){
		this.materialName=materialName;
	}

	public String getMaterialName(){
		return this.materialName;
	}

	public void setDiscountPrice(Double discountPrice){
		this.discountPrice=discountPrice;
	}

	public Double getDiscountPrice(){
		return this.discountPrice;
	}

	public void setSinglePrice(Double singlePrice){
		this.singlePrice=singlePrice;
	}

	public Double getSinglePrice(){
		return this.singlePrice;
	}

	public void setHeadId(Long headId){
		this.headId=headId;
	}

	public Long getHeadId(){
		return this.headId;
	}

	public void setIsErr(String isErr){
		this.isErr=isErr;
	}

	public String getIsErr(){
		return this.isErr;
	}

	public void setErrMessage(String errMessage){
		this.errMessage=errMessage;
	}

	public String getErrMessage(){
		return this.errMessage;
	}

	public void setLineId(Long lineId){
		this.lineId=lineId;
	}

	public Long getLineId(){
		return this.lineId;
	}

}