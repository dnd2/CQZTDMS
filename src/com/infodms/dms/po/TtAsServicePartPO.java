/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2017-08-22 19:43:23
* CreateBy   : liaoyu
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsServicePartPO extends PO{

	private Long relationMainPart;
	private Integer isMainPart;
	private Long serviceOrderId;
	private Integer partWarType;
	private Double partPrice;
	private Integer isThreeGuarantee;
	private String failureModeCode;
	private Long createBy;
	private Long servicePartId;
	private Double salePrice1;
	private String partCode;
	private Integer partPaymentMethod;
	private Double partFareRate;
	private Integer partNum;
	private Integer partUseType;
	private String partCname;
	private Long partId;
	private Long relationLabour;
	private Date createDate;

	public void setRelationMainPart(Long relationMainPart){
		this.relationMainPart=relationMainPart;
	}

	public Long getRelationMainPart(){
		return this.relationMainPart;
	}

	public void setIsMainPart(Integer isMainPart){
		this.isMainPart=isMainPart;
	}

	public Integer getIsMainPart(){
		return this.isMainPart;
	}

	public void setServiceOrderId(Long serviceOrderId){
		this.serviceOrderId=serviceOrderId;
	}

	public Long getServiceOrderId(){
		return this.serviceOrderId;
	}

	public void setPartWarType(Integer partWarType){
		this.partWarType=partWarType;
	}

	public Integer getPartWarType(){
		return this.partWarType;
	}

	public void setPartPrice(Double partPrice){
		this.partPrice=partPrice;
	}

	public Double getPartPrice(){
		return this.partPrice;
	}

	public void setIsThreeGuarantee(Integer isThreeGuarantee){
		this.isThreeGuarantee=isThreeGuarantee;
	}

	public Integer getIsThreeGuarantee(){
		return this.isThreeGuarantee;
	}

	public void setFailureModeCode(String failureModeCode){
		this.failureModeCode=failureModeCode;
	}

	public String getFailureModeCode(){
		return this.failureModeCode;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setServicePartId(Long servicePartId){
		this.servicePartId=servicePartId;
	}

	public Long getServicePartId(){
		return this.servicePartId;
	}

	public void setSalePrice1(Double salePrice1){
		this.salePrice1=salePrice1;
	}

	public Double getSalePrice1(){
		return this.salePrice1;
	}

	public void setPartCode(String partCode){
		this.partCode=partCode;
	}

	public String getPartCode(){
		return this.partCode;
	}

	public void setPartPaymentMethod(Integer partPaymentMethod){
		this.partPaymentMethod=partPaymentMethod;
	}

	public Integer getPartPaymentMethod(){
		return this.partPaymentMethod;
	}

	public void setPartFareRate(Double partFareRate){
		this.partFareRate=partFareRate;
	}

	public Double getPartFareRate(){
		return this.partFareRate;
	}

	public void setPartNum(Integer partNum){
		this.partNum=partNum;
	}

	public Integer getPartNum(){
		return this.partNum;
	}

	public void setPartUseType(Integer partUseType){
		this.partUseType=partUseType;
	}

	public Integer getPartUseType(){
		return this.partUseType;
	}

	public void setPartCname(String partCname){
		this.partCname=partCname;
	}

	public String getPartCname(){
		return this.partCname;
	}

	public void setPartId(Long partId){
		this.partId=partId;
	}

	public Long getPartId(){
		return this.partId;
	}

	public void setRelationLabour(Long relationLabour){
		this.relationLabour=relationLabour;
	}

	public Long getRelationLabour(){
		return this.relationLabour;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

}