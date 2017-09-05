/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-12-02 17:34:13
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TPcOrderDetailAuditPO extends PO{

	private String intentColor;
	private Float price;
	private Integer deliveryNumber;
	private String vin;
	private Long orderDetailId;
	private Date updateDate;
	private String createBy;
	private Long customerId;
	private Date deliveryDate;
	private Float deposit;
	private Float amount;
	private Integer status;
	private Date balanceDate;
	private Long orderDetailAuditId;
	private Long material;
	private String updateBy;
	private String intentModel;
	private Date createDate;
	private String color;
	private Integer num;
	private Long orderAuditId;
	private Long orderId;
	private Float earnest;
	private Long oldOrderDetailId;

	public Long getOldOrderDetailId() {
		return oldOrderDetailId;
	}

	public void setOldOrderDetailId(Long oldOrderDetailId) {
		this.oldOrderDetailId = oldOrderDetailId;
	}

	public Float getEarnest() {
		return earnest;
	}

	public void setEarnest(Float earnest) {
		this.earnest = earnest;
	}

	public Long getOrderAuditId() {
		return orderAuditId;
	}

	public void setOrderAuditId(Long orderAuditId) {
		this.orderAuditId = orderAuditId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public void setIntentColor(String intentColor){
		this.intentColor=intentColor;
	}

	public String getIntentColor(){
		return this.intentColor;
	}

	public void setPrice(Float price){
		this.price=price;
	}

	public Float getPrice(){
		return this.price;
	}

	public void setDeliveryNumber(Integer deliveryNumber){
		this.deliveryNumber=deliveryNumber;
	}

	public Integer getDeliveryNumber(){
		return this.deliveryNumber;
	}

	public void setVin(String vin){
		this.vin=vin;
	}

	public String getVin(){
		return this.vin;
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

	public void setCreateBy(String createBy){
		this.createBy=createBy;
	}

	public String getCreateBy(){
		return this.createBy;
	}

	public void setCustomerId(Long customerId){
		this.customerId=customerId;
	}

	public Long getCustomerId(){
		return this.customerId;
	}

	public void setDeliveryDate(Date deliveryDate){
		this.deliveryDate=deliveryDate;
	}

	public Date getDeliveryDate(){
		return this.deliveryDate;
	}

	public void setDeposit(Float deposit){
		this.deposit=deposit;
	}

	public Float getDeposit(){
		return this.deposit;
	}

	public void setAmount(Float amount){
		this.amount=amount;
	}

	public Float getAmount(){
		return this.amount;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setBalanceDate(Date balanceDate){
		this.balanceDate=balanceDate;
	}

	public Date getBalanceDate(){
		return this.balanceDate;
	}

	public void setOrderDetailAuditId(Long orderDetailAuditId){
		this.orderDetailAuditId=orderDetailAuditId;
	}

	public Long getOrderDetailAuditId(){
		return this.orderDetailAuditId;
	}

	public void setMaterial(Long material){
		this.material=material;
	}

	public Long getMaterial(){
		return this.material;
	}

	public void setUpdateBy(String updateBy){
		this.updateBy=updateBy;
	}

	public String getUpdateBy(){
		return this.updateBy;
	}

	public void setIntentModel(String intentModel){
		this.intentModel=intentModel;
	}

	public String getIntentModel(){
		return this.intentModel;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setColor(String color){
		this.color=color;
	}

	public String getColor(){
		return this.color;
	}

	public void setNum(Integer num){
		this.num=num;
	}

	public Integer getNum(){
		return this.num;
	}

}