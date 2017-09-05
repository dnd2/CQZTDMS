/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-05-27 17:43:50
* CreateBy   : 
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtSalesOrderDetailPO extends PO{

	private Integer orderAmount;
	private Double singlePrice;
	private Date updateDate;
	private Integer matchAmount;
	private Long createBy;
	private Long detailId;
	private Integer checkAmount;
	private Integer callAmount;
	private Long materialId;
	private Long orderId;
	private Long updateBy;
	private Double totalPrice;
	private Integer ver;
	private Date createDate;

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

	public void setCheckAmount(Integer checkAmount){
		this.checkAmount=checkAmount;
	}

	public Integer getCheckAmount(){
		return this.checkAmount;
	}

	public void setCallAmount(Integer callAmount){
		this.callAmount=callAmount;
	}

	public Integer getCallAmount(){
		return this.callAmount;
	}

	public void setMaterialId(Long materialId){
		this.materialId=materialId;
	}

	public Long getMaterialId(){
		return this.materialId;
	}

	public void setOrderId(Long orderId){
		this.orderId=orderId;
	}

	public Long getOrderId(){
		return this.orderId;
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