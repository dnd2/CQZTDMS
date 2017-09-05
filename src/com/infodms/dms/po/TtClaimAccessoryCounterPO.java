/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2015-11-11 14:35:27
* CreateBy   : chenyu
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtClaimAccessoryCounterPO extends PO{

	private String claimNo;
	private Double price;
	private String mainPartCode;
	private Long id;
	private Double appPrice;
	private String workhourCode;
	private String workhourName;

	public void setClaimNo(String claimNo){
		this.claimNo=claimNo;
	}

	public String getClaimNo(){
		return this.claimNo;
	}

	public void setPrice(Double price){
		this.price=price;
	}

	public Double getPrice(){
		return this.price;
	}

	public void setMainPartCode(String mainPartCode){
		this.mainPartCode=mainPartCode;
	}

	public String getMainPartCode(){
		return this.mainPartCode;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setAppPrice(Double appPrice){
		this.appPrice=appPrice;
	}

	public Double getAppPrice(){
		return this.appPrice;
	}

	public void setWorkhourCode(String workhourCode){
		this.workhourCode=workhourCode;
	}

	public String getWorkhourCode(){
		return this.workhourCode;
	}

	public void setWorkhourName(String workhourName){
		this.workhourName=workhourName;
	}

	public String getWorkhourName(){
		return this.workhourName;
	}

}