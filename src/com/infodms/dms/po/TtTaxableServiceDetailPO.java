/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-09-24 13:26:52
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtTaxableServiceDetailPO extends PO{

	private Double goodsExclTaxAmount;
	private Long serialNumber;
	private Long taxableServiceId;
	private Double goodsUnitPrice;
	private Double goodsSumAmount;
	private String goodsName;
	private Double taxAmount;
	private String goodsUnit;
	private String goodsModel;
	private Long taxableServiceDetailId;
	private Float goodsNum;
	private String goodsCode;
	private Double taxRate;

	public void setGoodsExclTaxAmount(Double goodsExclTaxAmount){
		this.goodsExclTaxAmount=goodsExclTaxAmount;
	}

	public Double getGoodsExclTaxAmount(){
		return this.goodsExclTaxAmount;
	}

	public void setSerialNumber(Long serialNumber){
		this.serialNumber=serialNumber;
	}

	public Long getSerialNumber(){
		return this.serialNumber;
	}

	public void setTaxableServiceId(Long taxableServiceId){
		this.taxableServiceId=taxableServiceId;
	}

	public Long getTaxableServiceId(){
		return this.taxableServiceId;
	}

	public void setGoodsUnitPrice(Double goodsUnitPrice){
		this.goodsUnitPrice=goodsUnitPrice;
	}

	public Double getGoodsUnitPrice(){
		return this.goodsUnitPrice;
	}

	public void setGoodsSumAmount(Double goodsSumAmount){
		this.goodsSumAmount=goodsSumAmount;
	}

	public Double getGoodsSumAmount(){
		return this.goodsSumAmount;
	}

	public void setGoodsName(String goodsName){
		this.goodsName=goodsName;
	}

	public String getGoodsName(){
		return this.goodsName;
	}

	public void setTaxAmount(Double taxAmount){
		this.taxAmount=taxAmount;
	}

	public Double getTaxAmount(){
		return this.taxAmount;
	}

	public void setGoodsUnit(String goodsUnit){
		this.goodsUnit=goodsUnit;
	}

	public String getGoodsUnit(){
		return this.goodsUnit;
	}

	public void setGoodsModel(String goodsModel){
		this.goodsModel=goodsModel;
	}

	public String getGoodsModel(){
		return this.goodsModel;
	}

	public void setTaxableServiceDetailId(Long taxableServiceDetailId){
		this.taxableServiceDetailId=taxableServiceDetailId;
	}

	public Long getTaxableServiceDetailId(){
		return this.taxableServiceDetailId;
	}

	public void setGoodsNum(Float goodsNum){
		this.goodsNum=goodsNum;
	}

	public Float getGoodsNum(){
		return this.goodsNum;
	}

	public void setGoodsCode(String goodsCode){
		this.goodsCode=goodsCode;
	}

	public String getGoodsCode(){
		return this.goodsCode;
	}

	public void setTaxRate(Double taxRate){
		this.taxRate=taxRate;
	}

	public Double getTaxRate(){
		return this.taxRate;
	}

}