/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-11-27 18:55:48
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TPcOrderDetailPO extends PO{

	private String intentColor;
	private Float price;
	private Integer deliveryNumber;
	private Long vehicleId;
	private Long orderDetailId;
	private Date updateDate;
	private String remark;
	private String createBy;
	private Date deliveryDate;
	private Long nextTask;
	private Float deposit;
	private Float amount;
	private Date balanceDate;
	private Long orderId;
	private Integer taskStatus;
	private Long material;
	private String updateBy;
	private Long previousTask;
	private String intentModel;
	private String color;
	private Date createDate;
	private Integer num;
	private Date actDelvDate;
	private Long customerId;
	private Float earnest;
	private Date orderdDate;
	private Long oldOrderDetailId;

	public Long getOldOrderDetailId() {
		return oldOrderDetailId;
	}

	public void setOldOrderDetailId(Long oldOrderDetailId) {
		this.oldOrderDetailId = oldOrderDetailId;
	}
	

	public Date getOrderdDate() {
		return orderdDate;
	}

	public void setOrderdDate(Date orderdDate) {
		this.orderdDate = orderdDate;
	}

	public Float getEarnest() {
		return earnest;
	}

	public void setEarnest(Float earnest) {
		this.earnest = earnest;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Long getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(Long vehicleId) {
		this.vehicleId = vehicleId;
	}

	public Date getActDelvDate() {
		return actDelvDate;
	}

	public void setActDelvDate(Date actDelvDate) {
		this.actDelvDate = actDelvDate;
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

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setCreateBy(String createBy){
		this.createBy=createBy;
	}

	public String getCreateBy(){
		return this.createBy;
	}

	public void setDeliveryDate(Date deliveryDate){
		this.deliveryDate=deliveryDate;
	}

	public Date getDeliveryDate(){
		return this.deliveryDate;
	}

	public void setNextTask(Long nextTask){
		this.nextTask=nextTask;
	}

	public Long getNextTask(){
		return this.nextTask;
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

	public void setBalanceDate(Date balanceDate){
		this.balanceDate=balanceDate;
	}

	public Date getBalanceDate(){
		return this.balanceDate;
	}

	public void setOrderId(Long orderId){
		this.orderId=orderId;
	}

	public Long getOrderId(){
		return this.orderId;
	}

	public void setTaskStatus(Integer taskStatus){
		this.taskStatus=taskStatus;
	}

	public Integer getTaskStatus(){
		return this.taskStatus;
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

	public void setPreviousTask(Long previousTask){
		this.previousTask=previousTask;
	}

	public Long getPreviousTask(){
		return this.previousTask;
	}

	public void setIntentModel(String intentModel){
		this.intentModel=intentModel;
	}

	public String getIntentModel(){
		return this.intentModel;
	}

	public void setColor(String color){
		this.color=color;
	}

	public String getColor(){
		return this.color;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setNum(Integer num){
		this.num=num;
	}

	public Integer getNum(){
		return this.num;
	}
	
	

}