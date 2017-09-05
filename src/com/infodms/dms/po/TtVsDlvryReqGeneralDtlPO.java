/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2012-05-07 11:36:53
* CreateBy   : fengalon
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsDlvryReqGeneralDtlPO extends PO{

	private Integer deliveryAmount;
	private Double singlePrice;
	private String patchNo;
	private Double discountSPrice;
	private Integer reserveAmount;
	private Long orderDetailId;
	private Date updateDate;
	private Long createBy;
	private Long detailId;
	private Float discountRate;
	private Long materialId;
	private Double discountPrice;
	private Long reqId;
	private Long updateBy;
	private Double totalPrice;
	private Integer ver;
	private Date createDate;
	private Integer reqAmount;
	private Long oldDetailId;

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

	public void setPatchNo(String patchNo){
		this.patchNo=patchNo;
	}

	public String getPatchNo(){
		return this.patchNo;
	}

	public void setDiscountSPrice(Double discountSPrice){
		this.discountSPrice=discountSPrice;
	}

	public Double getDiscountSPrice(){
		return this.discountSPrice;
	}

	public void setReserveAmount(Integer reserveAmount){
		this.reserveAmount=reserveAmount;
	}

	public Integer getReserveAmount(){
		return this.reserveAmount;
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

	public void setMaterialId(Long materialId){
		this.materialId=materialId;
	}

	public Long getMaterialId(){
		return this.materialId;
	}

	public void setDiscountPrice(Double discountPrice){
		this.discountPrice=discountPrice;
	}

	public Double getDiscountPrice(){
		return this.discountPrice;
	}

	public void setReqId(Long reqId){
		this.reqId=reqId;
	}

	public Long getReqId(){
		return this.reqId;
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

	public void setReqAmount(Integer reqAmount){
		this.reqAmount=reqAmount;
	}

	public Integer getReqAmount(){
		return this.reqAmount;
	}

	public Long getOldDetailId() {
		return oldDetailId;
	}

	public void setOldDetailId(Long oldDetailId) {
		this.oldDetailId = oldDetailId;
	}

}