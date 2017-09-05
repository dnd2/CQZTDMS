/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-11-20 16:36:25
* CreateBy   : nova
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmChananelMileagePO extends PO{

	private Double bridgeFee;
	private Long mileage;
	private Long id;
	private Long outDays;
	private Double personFee;
	private Double trainFee;
	private Double cateringFee;
	private Double expensesFee;
	private Integer chananelType;

	public void setBridgeFee(Double bridgeFee){
		this.bridgeFee=bridgeFee;
	}

	public Double getBridgeFee(){
		return this.bridgeFee;
	}

	public void setMileage(Long mileage){
		this.mileage=mileage;
	}

	public Long getMileage(){
		return this.mileage;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setOutDays(Long outDays){
		this.outDays=outDays;
	}

	public Long getOutDays(){
		return this.outDays;
	}

	public void setPersonFee(Double personFee){
		this.personFee=personFee;
	}

	public Double getPersonFee(){
		return this.personFee;
	}

	public void setTrainFee(Double trainFee){
		this.trainFee=trainFee;
	}

	public Double getTrainFee(){
		return this.trainFee;
	}

	public void setCateringFee(Double cateringFee){
		this.cateringFee=cateringFee;
	}

	public Double getCateringFee(){
		return this.cateringFee;
	}

	public void setExpensesFee(Double expensesFee){
		this.expensesFee=expensesFee;
	}

	public Double getExpensesFee(){
		return this.expensesFee;
	}

	public void setChananelType(Integer chananelType){
		this.chananelType=chananelType;
	}

	public Integer getChananelType(){
		return this.chananelType;
	}

}