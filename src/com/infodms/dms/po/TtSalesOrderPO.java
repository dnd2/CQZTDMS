/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-05-31 20:42:32
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtSalesOrderPO extends PO{

	private Long companyId;
	private Long billingOrgId;
	private Integer orderYear;
	private String orderRemark;
	private Long createBy;
	private Long orderOrgId;
	private Integer orderWeek;
	private Integer ver;
	private Integer orderStatus;
	private Date createDate;
	private Integer deliveryType;
	private String specialBatchNo;
	private Integer orderOrgType;
	private Long areaId;
	private Integer orderMonth;
	private Long supplyOrgId;
	private Date updateDate;
	private String deliveryAddress;
	private Integer isRefitOrder;
	private Integer orderType;
	private Double orderPrice;
	private Long orderId;
	private Date raiseDate;
	private String orderNo;
	private String refitRemark;
	private Long updateBy;
	private Long fundType;

	public void setCompanyId(Long companyId){
		this.companyId=companyId;
	}

	public Long getCompanyId(){
		return this.companyId;
	}

	public void setBillingOrgId(Long billingOrgId){
		this.billingOrgId=billingOrgId;
	}

	public Long getBillingOrgId(){
		return this.billingOrgId;
	}

	public void setOrderYear(Integer orderYear){
		this.orderYear=orderYear;
	}

	public Integer getOrderYear(){
		return this.orderYear;
	}

	public void setOrderRemark(String orderRemark){
		this.orderRemark=orderRemark;
	}

	public String getOrderRemark(){
		return this.orderRemark;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setOrderOrgId(Long orderOrgId){
		this.orderOrgId=orderOrgId;
	}

	public Long getOrderOrgId(){
		return this.orderOrgId;
	}

	public void setOrderWeek(Integer orderWeek){
		this.orderWeek=orderWeek;
	}

	public Integer getOrderWeek(){
		return this.orderWeek;
	}

	public void setVer(Integer ver){
		this.ver=ver;
	}

	public Integer getVer(){
		return this.ver;
	}

	public void setOrderStatus(Integer orderStatus){
		this.orderStatus=orderStatus;
	}

	public Integer getOrderStatus(){
		return this.orderStatus;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setDeliveryType(Integer deliveryType){
		this.deliveryType=deliveryType;
	}

	public Integer getDeliveryType(){
		return this.deliveryType;
	}

	public void setSpecialBatchNo(String specialBatchNo){
		this.specialBatchNo=specialBatchNo;
	}

	public String getSpecialBatchNo(){
		return this.specialBatchNo;
	}

	public void setOrderOrgType(Integer orderOrgType){
		this.orderOrgType=orderOrgType;
	}

	public Integer getOrderOrgType(){
		return this.orderOrgType;
	}

	public void setAreaId(Long areaId){
		this.areaId=areaId;
	}

	public Long getAreaId(){
		return this.areaId;
	}

	public void setOrderMonth(Integer orderMonth){
		this.orderMonth=orderMonth;
	}

	public Integer getOrderMonth(){
		return this.orderMonth;
	}

	public void setSupplyOrgId(Long supplyOrgId){
		this.supplyOrgId=supplyOrgId;
	}

	public Long getSupplyOrgId(){
		return this.supplyOrgId;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setDeliveryAddress(String deliveryAddress){
		this.deliveryAddress=deliveryAddress;
	}

	public String getDeliveryAddress(){
		return this.deliveryAddress;
	}

	public void setIsRefitOrder(Integer isRefitOrder){
		this.isRefitOrder=isRefitOrder;
	}

	public Integer getIsRefitOrder(){
		return this.isRefitOrder;
	}

	public void setOrderType(Integer orderType){
		this.orderType=orderType;
	}

	public Integer getOrderType(){
		return this.orderType;
	}

	public void setOrderPrice(Double orderPrice){
		this.orderPrice=orderPrice;
	}

	public Double getOrderPrice(){
		return this.orderPrice;
	}

	public void setOrderId(Long orderId){
		this.orderId=orderId;
	}

	public Long getOrderId(){
		return this.orderId;
	}

	public void setRaiseDate(Date raiseDate){
		this.raiseDate=raiseDate;
	}

	public Date getRaiseDate(){
		return this.raiseDate;
	}

	public void setOrderNo(String orderNo){
		this.orderNo=orderNo;
	}

	public String getOrderNo(){
		return this.orderNo;
	}

	public void setRefitRemark(String refitRemark){
		this.refitRemark=refitRemark;
	}

	public String getRefitRemark(){
		return this.refitRemark;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setFundType(Long fundType){
		this.fundType=fundType;
	}

	public Long getFundType(){
		return this.fundType;
	}

}