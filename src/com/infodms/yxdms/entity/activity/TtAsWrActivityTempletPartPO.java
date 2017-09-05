/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-12-16 17:06:21
* CreateBy   : yuewei
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.yxdms.entity.activity;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrActivityTempletPartPO extends PO{

	private String oldPartName;
	private Double partCostAmount;
	private Integer responsibilityType;
	private String partName;
	private String producerCode;
	private Double partCostPrice;
	private Long templetId;
	private String partCode;
	private String oldPartCode;
	private Integer partUseType;
	private Long partId;
	private Long id;
	private Integer partQuantity;
	private Integer isReturn;

	public void setOldPartName(String oldPartName){
		this.oldPartName=oldPartName;
	}

	public String getOldPartName(){
		return this.oldPartName;
	}

	public void setPartCostAmount(Double partCostAmount){
		this.partCostAmount=partCostAmount;
	}

	public Double getPartCostAmount(){
		return this.partCostAmount;
	}

	public void setResponsibilityType(Integer responsibilityType){
		this.responsibilityType=responsibilityType;
	}

	public Integer getResponsibilityType(){
		return this.responsibilityType;
	}

	public void setPartName(String partName){
		this.partName=partName;
	}

	public String getPartName(){
		return this.partName;
	}

	public void setProducerCode(String producerCode){
		this.producerCode=producerCode;
	}

	public String getProducerCode(){
		return this.producerCode;
	}

	public void setPartCostPrice(Double partCostPrice){
		this.partCostPrice=partCostPrice;
	}

	public Double getPartCostPrice(){
		return this.partCostPrice;
	}

	public void setTempletId(Long templetId){
		this.templetId=templetId;
	}

	public Long getTempletId(){
		return this.templetId;
	}

	public void setPartCode(String partCode){
		this.partCode=partCode;
	}

	public String getPartCode(){
		return this.partCode;
	}

	public void setOldPartCode(String oldPartCode){
		this.oldPartCode=oldPartCode;
	}

	public String getOldPartCode(){
		return this.oldPartCode;
	}

	public void setPartUseType(Integer partUseType){
		this.partUseType=partUseType;
	}

	public Integer getPartUseType(){
		return this.partUseType;
	}

	public void setPartId(Long partId){
		this.partId=partId;
	}

	public Long getPartId(){
		return this.partId;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setPartQuantity(Integer partQuantity){
		this.partQuantity=partQuantity;
	}

	public Integer getPartQuantity(){
		return this.partQuantity;
	}

	public void setIsReturn(Integer isReturn){
		this.isReturn=isReturn;
	}

	public Integer getIsReturn(){
		return this.isReturn;
	}

}