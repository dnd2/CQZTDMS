/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-06-23 16:27:00
* CreateBy   : 
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtFleetIntentDetailPO extends PO{

	private Double originalPrice;
	private Long fleetId;
	private Long intentId;
	private Date updateDate;
	private String remark;
	private Long createBy;
	private Long detailId;
	private Integer amount;
	private Double discountPrice;
	private Long updateBy;
	private Long intentModel;
	private Date createDate;
	private String discount;

	public void setOriginalPrice(Double originalPrice){
		this.originalPrice=originalPrice;
	}

	public Double getOriginalPrice(){
		return this.originalPrice;
	}

	public void setFleetId(Long fleetId){
		this.fleetId=fleetId;
	}

	public Long getFleetId(){
		return this.fleetId;
	}

	public void setIntentId(Long intentId){
		this.intentId=intentId;
	}

	public Long getIntentId(){
		return this.intentId;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setDetailId(Long detailId){
		this.detailId=detailId;
	}

	public Long getDetailId(){
		return this.detailId;
	}

	public void setAmount(Integer amount){
		this.amount=amount;
	}

	public Integer getAmount(){
		return this.amount;
	}

	public void setDiscountPrice(Double discountPrice){
		this.discountPrice=discountPrice;
	}

	public Double getDiscountPrice(){
		return this.discountPrice;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setIntentModel(Long intentModel){
		this.intentModel=intentModel;
	}

	public Long getIntentModel(){
		return this.intentModel;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setDiscount(String discount){
		this.discount=discount;
	}

	public String getDiscount(){
		return this.discount;
	}

}