/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-09-28 18:32:53
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtTaxableServiceSumDetailPO extends PO{

	private Long taxableServiceSumDetailId;
	private Double packagePartsAmount;
	private Integer sumAmountType;
	private Long taxableServiceSumId;
	private Double exclTaxAmount;
	private Double taxRate;
	private String packageId;
	private Double sumAmount;
	private String sumAmountName;
	private Double taxAmount;
	private Float packageLabourAmount;
	private String packageFosterAmount;

	public void setTaxableServiceSumDetailId(Long taxableServiceSumDetailId){
		this.taxableServiceSumDetailId=taxableServiceSumDetailId;
	}

	public Long getTaxableServiceSumDetailId(){
		return this.taxableServiceSumDetailId;
	}

	public void setPackagePartsAmount(Double packagePartsAmount){
		this.packagePartsAmount=packagePartsAmount;
	}

	public Double getPackagePartsAmount(){
		return this.packagePartsAmount;
	}

	public void setSumAmountType(Integer sumAmountType){
		this.sumAmountType=sumAmountType;
	}

	public Integer getSumAmountType(){
		return this.sumAmountType;
	}

	public void setTaxableServiceSumId(Long taxableServiceSumId){
		this.taxableServiceSumId=taxableServiceSumId;
	}

	public Long getTaxableServiceSumId(){
		return this.taxableServiceSumId;
	}

	public void setExclTaxAmount(Double exclTaxAmount){
		this.exclTaxAmount=exclTaxAmount;
	}

	public Double getExclTaxAmount(){
		return this.exclTaxAmount;
	}

	public void setTaxRate(Double taxRate){
		this.taxRate=taxRate;
	}

	public Double getTaxRate(){
		return this.taxRate;
	}

	public void setPackageId(String packageId){
		this.packageId=packageId;
	}

	public String getPackageId(){
		return this.packageId;
	}

	public void setSumAmount(Double sumAmount){
		this.sumAmount=sumAmount;
	}

	public Double getSumAmount(){
		return this.sumAmount;
	}

	public void setSumAmountName(String sumAmountName){
		this.sumAmountName=sumAmountName;
	}

	public String getSumAmountName(){
		return this.sumAmountName;
	}

	public void setTaxAmount(Double taxAmount){
		this.taxAmount=taxAmount;
	}

	public Double getTaxAmount(){
		return this.taxAmount;
	}

	public void setPackageLabourAmount(Float packageLabourAmount){
		this.packageLabourAmount=packageLabourAmount;
	}

	public Float getPackageLabourAmount(){
		return this.packageLabourAmount;
	}

	public void setPackageFosterAmount(String packageFosterAmount){
		this.packageFosterAmount=packageFosterAmount;
	}

	public String getPackageFosterAmount(){
		return this.packageFosterAmount;
	}

}