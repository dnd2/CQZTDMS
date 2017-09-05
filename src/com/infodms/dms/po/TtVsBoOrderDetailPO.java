/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-01-23 13:18:32
* CreateBy   : wswcx
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsBoOrderDetailPO extends PO{

	private Integer deliveryAmount;
	private Integer orderAmount;
	private Double singlePrice;
	private Integer outAmount;
	private Long eMaterialId;
	private Date updateDate;
	private Integer matchAmount;
	private Long createBy;
	private Long detailId;
	private Double dealerPrice;
	private Double modelPrice;
	private Integer checkAmount;
	private Long materialId;
	private Long updateBy;
	private Double totalPrice;
	private Integer accAmount;
	private Date createDate;
	private Integer boardNumber;
	private Long boOrderId;
	private Double disSinglePrice;

	public void setDeliveryAmount(Integer deliveryAmount){
		this.deliveryAmount=deliveryAmount;
	}

	public Integer getDeliveryAmount(){
		return this.deliveryAmount;
	}

	public void setOrderAmount(Integer orderAmount){
		this.orderAmount=orderAmount;
	}

	public Integer getOrderAmount(){
		return this.orderAmount;
	}

	public void setSinglePrice(Double singlePrice){
		this.singlePrice=singlePrice;
	}

	public Double getSinglePrice(){
		return this.singlePrice;
	}

	public void setOutAmount(Integer outAmount){
		this.outAmount=outAmount;
	}

	public Integer getOutAmount(){
		return this.outAmount;
	}

	public void setEMaterialId(Long eMaterialId){
		this.eMaterialId=eMaterialId;
	}

	public Long getEMaterialId(){
		return this.eMaterialId;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setMatchAmount(Integer matchAmount){
		this.matchAmount=matchAmount;
	}

	public Integer getMatchAmount(){
		return this.matchAmount;
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

	public void setDealerPrice(Double dealerPrice){
		this.dealerPrice=dealerPrice;
	}

	public Double getDealerPrice(){
		return this.dealerPrice;
	}

	public void setModelPrice(Double modelPrice){
		this.modelPrice=modelPrice;
	}

	public Double getModelPrice(){
		return this.modelPrice;
	}

	public void setCheckAmount(Integer checkAmount){
		this.checkAmount=checkAmount;
	}

	public Integer getCheckAmount(){
		return this.checkAmount;
	}

	public void setMaterialId(Long materialId){
		this.materialId=materialId;
	}

	public Long getMaterialId(){
		return this.materialId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setTotalPrice(Double totalPrice){
		this.totalPrice=totalPrice;
	}

	public Double getTotalPrice(){
		return this.totalPrice;
	}

	public void setAccAmount(Integer accAmount){
		this.accAmount=accAmount;
	}

	public Integer getAccAmount(){
		return this.accAmount;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setBoardNumber(Integer boardNumber){
		this.boardNumber=boardNumber;
	}

	public Integer getBoardNumber(){
		return this.boardNumber;
	}

	public void setBoOrderId(Long boOrderId){
		this.boOrderId=boOrderId;
	}

	public Long getBoOrderId(){
		return this.boOrderId;
	}

	public void setDisSinglePrice(Double disSinglePrice){
		this.disSinglePrice=disSinglePrice;
	}

	public Double getDisSinglePrice(){
		return this.disSinglePrice;
	}

}