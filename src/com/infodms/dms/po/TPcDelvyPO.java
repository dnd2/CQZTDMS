/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-12-09 17:10:47
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TPcDelvyPO extends PO{

	private Date updateDate;
	private Date deliveryDate;
	private String createBy;
	private Long material;
	private Date createDate;
	private String color;
	private Float amount;
	private Long delvDetailId;
	private Long vehicleId;
	private Long customerId;
	private Long previousTask;
	private String updateBy;
	private Float price;
	private Long orderDetailId;
	private Long nextTask;
	private Integer taskStatus;
	private Date actDelvDate;
	private String remark;
	private Long status;
	private Integer deliveryStatus;
	private Date failDate;
	

	public Date getFailDate() {
		return failDate;
	}

	public void setFailDate(Date failDate) {
		this.failDate = failDate;
	}

	public Integer getDeliveryStatus() {
		return deliveryStatus;
	}

	public void setDeliveryStatus(Integer deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setDeliveryDate(Date deliveryDate){
		this.deliveryDate=deliveryDate;
	}

	public Date getDeliveryDate(){
		return this.deliveryDate;
	}

	public void setCreateBy(String createBy){
		this.createBy=createBy;
	}

	public String getCreateBy(){
		return this.createBy;
	}

	public void setMaterial(Long material){
		this.material=material;
	}

	public Long getMaterial(){
		return this.material;
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

	public void setAmount(Float amount){
		this.amount=amount;
	}

	public Float getAmount(){
		return this.amount;
	}

	public void setDelvDetailId(Long delvDetailId){
		this.delvDetailId=delvDetailId;
	}

	public Long getDelvDetailId(){
		return this.delvDetailId;
	}

	public void setVehicleId(Long vehicleId){
		this.vehicleId=vehicleId;
	}

	public Long getVehicleId(){
		return this.vehicleId;
	}

	public void setCustomerId(Long customerId){
		this.customerId=customerId;
	}

	public Long getCustomerId(){
		return this.customerId;
	}

	public void setPreviousTask(Long previousTask){
		this.previousTask=previousTask;
	}

	public Long getPreviousTask(){
		return this.previousTask;
	}

	public void setUpdateBy(String updateBy){
		this.updateBy=updateBy;
	}

	public String getUpdateBy(){
		return this.updateBy;
	}

	public void setPrice(Float price){
		this.price=price;
	}

	public Float getPrice(){
		return this.price;
	}

	public void setOrderDetailId(Long orderDetailId){
		this.orderDetailId=orderDetailId;
	}

	public Long getOrderDetailId(){
		return this.orderDetailId;
	}

	public void setNextTask(Long nextTask){
		this.nextTask=nextTask;
	}

	public Long getNextTask(){
		return this.nextTask;
	}

	public void setTaskStatus(Integer taskStatus){
		this.taskStatus=taskStatus;
	}

	public Integer getTaskStatus(){
		return this.taskStatus;
	}

	public void setActDelvDate(Date actDelvDate){
		this.actDelvDate=actDelvDate;
	}

	public Date getActDelvDate(){
		return this.actDelvDate;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

}