/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-11-25 11:44:18
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TPcOrderPO extends PO{

	private Integer newProductSale;
	private String dealType;
	private Integer orderStatus;
	private String oldLevel;
	private String createBy;
	private String remark;
	private String deliveryAddress;
	private String salesProgress;
	private Long customerId;
	private Date finishDate;
	private String ownerProvince;
	private String ownerPaperType;
	private String ownerName;
	private String ownerPaperNo;
	private String oldSalesProgress;
	private String ownerPhone;
	private String ownerCity;
	private String ownerArea;
	private Long orderId;
	private String newLevel;
	private Integer testDriving;
	private Date createDate;
	private String ownerAddress;
	private Date orderDate;
	private Integer taskStatus;
	private Long previousTask;
	private Long nextTask;
	private String dataFrom;

	public String getDataFrom() {
		return dataFrom;
	}

	public void setDataFrom(String dataFrom) {
		this.dataFrom = dataFrom;
	}

	public Integer getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(Integer taskStatus) {
		this.taskStatus = taskStatus;
	}

	public Long getPreviousTask() {
		return previousTask;
	}

	public void setPreviousTask(Long previousTask) {
		this.previousTask = previousTask;
	}

	public Long getNextTask() {
		return nextTask;
	}

	public void setNextTask(Long nextTask) {
		this.nextTask = nextTask;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public void setNewProductSale(Integer newProductSale){
		this.newProductSale=newProductSale;
	}

	public Integer getNewProductSale(){
		return this.newProductSale;
	}

	public void setDealType(String dealType){
		this.dealType=dealType;
	}

	public String getDealType(){
		return this.dealType;
	}

	public void setOrderStatus(Integer orderStatus){
		this.orderStatus=orderStatus;
	}

	public Integer getOrderStatus(){
		return this.orderStatus;
	}

	public void setOldLevel(String oldLevel){
		this.oldLevel=oldLevel;
	}

	public String getOldLevel(){
		return this.oldLevel;
	}

	public void setCreateBy(String createBy){
		this.createBy=createBy;
	}

	public String getCreateBy(){
		return this.createBy;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setDeliveryAddress(String deliveryAddress){
		this.deliveryAddress=deliveryAddress;
	}

	public String getDeliveryAddress(){
		return this.deliveryAddress;
	}

	public void setSalesProgress(String salesProgress){
		this.salesProgress=salesProgress;
	}

	public String getSalesProgress(){
		return this.salesProgress;
	}

	public void setCustomerId(Long customerId){
		this.customerId=customerId;
	}

	public Long getCustomerId(){
		return this.customerId;
	}

	public void setFinishDate(Date finishDate){
		this.finishDate=finishDate;
	}

	public Date getFinishDate(){
		return this.finishDate;
	}

	public void setOwnerProvince(String ownerProvince){
		this.ownerProvince=ownerProvince;
	}

	public String getOwnerProvince(){
		return this.ownerProvince;
	}

	public void setOwnerPaperType(String ownerPaperType){
		this.ownerPaperType=ownerPaperType;
	}

	public String getOwnerPaperType(){
		return this.ownerPaperType;
	}

	public void setOwnerName(String ownerName){
		this.ownerName=ownerName;
	}

	public String getOwnerName(){
		return this.ownerName;
	}

	public void setOwnerPaperNo(String ownerPaperNo){
		this.ownerPaperNo=ownerPaperNo;
	}

	public String getOwnerPaperNo(){
		return this.ownerPaperNo;
	}

	public void setOldSalesProgress(String oldSalesProgress){
		this.oldSalesProgress=oldSalesProgress;
	}

	public String getOldSalesProgress(){
		return this.oldSalesProgress;
	}

	public void setOwnerPhone(String ownerPhone){
		this.ownerPhone=ownerPhone;
	}

	public String getOwnerPhone(){
		return this.ownerPhone;
	}

	public void setOwnerCity(String ownerCity){
		this.ownerCity=ownerCity;
	}

	public String getOwnerCity(){
		return this.ownerCity;
	}

	public void setOwnerArea(String ownerArea){
		this.ownerArea=ownerArea;
	}

	public String getOwnerArea(){
		return this.ownerArea;
	}

	public void setOrderId(Long orderId){
		this.orderId=orderId;
	}

	public Long getOrderId(){
		return this.orderId;
	}

	public void setNewLevel(String newLevel){
		this.newLevel=newLevel;
	}

	public String getNewLevel(){
		return this.newLevel;
	}

	public void setTestDriving(Integer testDriving){
		this.testDriving=testDriving;
	}

	public Integer getTestDriving(){
		return this.testDriving;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setOwnerAddress(String ownerAddress){
		this.ownerAddress=ownerAddress;
	}

	public String getOwnerAddress(){
		return this.ownerAddress;
	}

}