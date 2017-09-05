/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-05-27 17:40:57
* CreateBy   : 
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtDeliveryDetailPO extends PO{

	private Integer deliveryAmount;
	private Double singlePrice;
	private Long deliveryId;
	private Long orderDetailId;
	private Date updateDate;
	private Integer matchAmount;
	private Long createBy;
	private Long detailId;
	private Long materialId;
	private Long updateBy;
	private Double totalPrice;
	private Integer ver;
	private Date createDate;

	public void setDeliveryAmount(Integer deliveryAmount){
		this.deliveryAmount=deliveryAmount;
	}

	public Integer getDeliveryAmount(){
		return this.deliveryAmount;
	}

	public void setSinglePrice(Double singlePrice){
		this.singlePrice=singlePrice;
	}

	public Double getSinglePrice(){
		return this.singlePrice;
	}

	public void setDeliveryId(Long deliveryId){
		this.deliveryId=deliveryId;
	}

	public Long getDeliveryId(){
		return this.deliveryId;
	}

	public void setOrderDetailId(Long orderDetailId){
		this.orderDetailId=orderDetailId;
	}

	public Long getOrderDetailId(){
		return this.orderDetailId;
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

	public void setVer(Integer ver){
		this.ver=ver;
	}

	public Integer getVer(){
		return this.ver;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

}