/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2017-07-21 23:28:07
* CreateBy   : lei
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsOrderDetailPO extends PO{

	private Integer deliveryAmount;
	private Long isLock;
	private Integer orderAmount;
	private Double singlePrice;
	private Double discountSPrice;
	private String specialBatchNo;
	private Long priceListId;
	private Date updateDate;
	private Integer matchAmount;
	private Long createBy;
	private Long detailId;
	private Float discountRate;
	private Integer checkAmount;
	private Integer callAmount;
	private Long materialId;
	private Long orderId;
	private Double discountPrice;
	private Long updateBy;
	private Double totalPrice;
	private Integer ver;
	private Integer applyedAmount;
	private Date createDate;
	private Long respondAmount;
	private Integer regionCheckAmount;
	private Double rebateAmount;
	private Double afterRebatePrice;
	
	
	public Double getAfterRebatePrice() {
		return afterRebatePrice;
	}

	public void setAfterRebatePrice(Double afterRebatePrice) {
		this.afterRebatePrice = afterRebatePrice;
	}

	public Double getRebateAmount() {
		return rebateAmount;
	}

	public void setRebateAmount(Double rebateAmount) {
		this.rebateAmount = rebateAmount;
	}
	public void setDeliveryAmount(Integer deliveryAmount){
		this.deliveryAmount=deliveryAmount;
	}

	public Integer getDeliveryAmount(){
		return this.deliveryAmount;
	}

	public void setIsLock(Long isLock){
		this.isLock=isLock;
	}

	public Long getIsLock(){
		return this.isLock;
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

	public void setDiscountSPrice(Double discountSPrice){
		this.discountSPrice=discountSPrice;
	}

	public Double getDiscountSPrice(){
		return this.discountSPrice;
	}

	public void setSpecialBatchNo(String specialBatchNo){
		this.specialBatchNo=specialBatchNo;
	}

	public String getSpecialBatchNo(){
		return this.specialBatchNo;
	}

	public void setPriceListId(Long priceListId){
		this.priceListId=priceListId;
	}

	public Long getPriceListId(){
		return this.priceListId;
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

	public void setDiscountRate(Float discountRate){
		this.discountRate=discountRate;
	}

	public Float getDiscountRate(){
		return this.discountRate;
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

	public void setApplyedAmount(Integer applyedAmount){
		this.applyedAmount=applyedAmount;
	}

	public Integer getApplyedAmount(){
		return this.applyedAmount;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setRespondAmount(Long respondAmount){
		this.respondAmount=respondAmount;
	}

	public Long getRespondAmount(){
		return this.respondAmount;
	}

	public void setRegionCheckAmount(Integer regionCheckAmount){
		this.regionCheckAmount=regionCheckAmount;
	}

	public Integer getRegionCheckAmount(){
		return this.regionCheckAmount;
	}

}