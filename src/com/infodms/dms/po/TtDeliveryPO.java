/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-05-27 18:58:31
* CreateBy   : 
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtDeliveryPO extends PO{

	private Long addressId;
	private String deliveryNo;
	private Long deliveryId;
	private String specialBatchNo;
	private Integer isRefitOrder;
	private String orderRemark;
	private Date updateDate;
	private Long createBy;
	private Double deliveryPrice;
	private Date deliveryDate;
	private Integer orderOrgType;
	private Long orderOrgId;
	private Long billingOrgId;
	private String refitRemark;
	private Long orderId;
	private Integer deliveryType;
	private Long updateBy;
	private Integer deliveryStatus;
	private Integer ver;
	private Date createDate;
	private Long supplyOrgId;
	private Long fundType;

	public void setAddressId(Long addressId){
		this.addressId=addressId;
	}

	public Long getAddressId(){
		return this.addressId;
	}

	public void setDeliveryNo(String deliveryNo){
		this.deliveryNo=deliveryNo;
	}

	public String getDeliveryNo(){
		return this.deliveryNo;
	}

	public void setDeliveryId(Long deliveryId){
		this.deliveryId=deliveryId;
	}

	public Long getDeliveryId(){
		return this.deliveryId;
	}

	public void setSpecialBatchNo(String specialBatchNo){
		this.specialBatchNo=specialBatchNo;
	}

	public String getSpecialBatchNo(){
		return this.specialBatchNo;
	}

	public void setIsRefitOrder(Integer isRefitOrder){
		this.isRefitOrder=isRefitOrder;
	}

	public Integer getIsRefitOrder(){
		return this.isRefitOrder;
	}

	public void setOrderRemark(String orderRemark){
		this.orderRemark=orderRemark;
	}

	public String getOrderRemark(){
		return this.orderRemark;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setDeliveryPrice(Double deliveryPrice){
		this.deliveryPrice=deliveryPrice;
	}

	public Double getDeliveryPrice(){
		return this.deliveryPrice;
	}

	public void setDeliveryDate(Date deliveryDate){
		this.deliveryDate=deliveryDate;
	}

	public Date getDeliveryDate(){
		return this.deliveryDate;
	}

	public void setOrderOrgType(Integer orderOrgType){
		this.orderOrgType=orderOrgType;
	}

	public Integer getOrderOrgType(){
		return this.orderOrgType;
	}

	public void setOrderOrgId(Long orderOrgId){
		this.orderOrgId=orderOrgId;
	}

	public Long getOrderOrgId(){
		return this.orderOrgId;
	}

	public void setBillingOrgId(Long billingOrgId){
		this.billingOrgId=billingOrgId;
	}

	public Long getBillingOrgId(){
		return this.billingOrgId;
	}

	public void setRefitRemark(String refitRemark){
		this.refitRemark=refitRemark;
	}

	public String getRefitRemark(){
		return this.refitRemark;
	}

	public void setOrderId(Long orderId){
		this.orderId=orderId;
	}

	public Long getOrderId(){
		return this.orderId;
	}

	public void setDeliveryType(Integer deliveryType){
		this.deliveryType=deliveryType;
	}

	public Integer getDeliveryType(){
		return this.deliveryType;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setDeliveryStatus(Integer deliveryStatus){
		this.deliveryStatus=deliveryStatus;
	}

	public Integer getDeliveryStatus(){
		return this.deliveryStatus;
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

	public void setSupplyOrgId(Long supplyOrgId){
		this.supplyOrgId=supplyOrgId;
	}

	public Long getSupplyOrgId(){
		return this.supplyOrgId;
	}

	public void setFundType(Long fundType){
		this.fundType=fundType;
	}

	public Long getFundType(){
		return this.fundType;
	}

}