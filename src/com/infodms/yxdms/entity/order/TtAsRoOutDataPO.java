/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2015-01-09 14:29:08
* CreateBy   : yuewei
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.yxdms.entity.order;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsRoOutDataPO extends PO{

	private String roNo;
	private String outAmount;
	private String payType;
	private String mileage;
	private String address;
	private Long id;

	public void setRoNo(String roNo){
		this.roNo=roNo;
	}

	public String getRoNo(){
		return this.roNo;
	}

	public void setOutAmount(String outAmount){
		this.outAmount=outAmount;
	}

	public String getOutAmount(){
		return this.outAmount;
	}

	public void setPayType(String payType){
		this.payType=payType;
	}

	public String getPayType(){
		return this.payType;
	}

	public void setMileage(String mileage){
		this.mileage=mileage;
	}

	public String getMileage(){
		return this.mileage;
	}

	public void setAddress(String address){
		this.address=address;
	}

	public String getAddress(){
		return this.address;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

}