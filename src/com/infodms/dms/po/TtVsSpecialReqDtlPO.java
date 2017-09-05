/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-01-14 09:54:59
* CreateBy   : wswcx
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsSpecialReqDtlPO extends PO{

	private Long dtlId;
	private Long groupId;
	private Long reqId;
	private Long updateBy;
	private Date updateDate;
	private Long createBy;
	private Date createDate;
	private Integer amount;
	private Date expectedDate;
	private Double salesPrice;
	private String newMaterialCode;

	private Double changePrice;
	private String specialBatchNo;
	private Integer expectedPeriod;
	private Integer orderAmount;
	private Long materialId;
		
	public Long getMaterialId() {
		return materialId;
	}

	public void setMaterialId(Long materialId) {
		this.materialId = materialId;
	}

	public Integer getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(Integer orderAmount) {
		this.orderAmount = orderAmount;
	}

	public Integer getExpectedPeriod() {
		return expectedPeriod;
	}

	public void setExpectedPeriod(Integer expectedPeriod) {
		this.expectedPeriod = expectedPeriod;
	}

	public String getSpecialBatchNo() {
		return specialBatchNo;
	}

	public void setSpecialBatchNo(String specialBatchNo) {
		this.specialBatchNo = specialBatchNo;
	}

	public Double getChangePrice() {
		return changePrice;
	}

	public void setChangePrice(Double changePrice) {
		this.changePrice = changePrice;
	}

	public void setDtlId(Long dtlId){
		this.dtlId=dtlId;
	}

	public Long getDtlId(){
		return this.dtlId;
	}

	public void setGroupId(Long groupId){
		this.groupId=groupId;
	}

	public Long getGroupId(){
		return this.groupId;
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

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setAmount(Integer amount){
		this.amount=amount;
	}

	public Integer getAmount(){
		return this.amount;
	}

	public void setExpectedDate(Date expectedDate){
		this.expectedDate=expectedDate;
	}

	public Date getExpectedDate(){
		return this.expectedDate;
	}

	public void setSalesPrice(Double salesPrice){
		this.salesPrice=salesPrice;
	}

	public Double getSalesPrice(){
		return this.salesPrice;
	}

	public void setNewMaterialCode(String newMaterialCode){
		this.newMaterialCode=newMaterialCode;
	}

	public String getNewMaterialCode(){
		return this.newMaterialCode;
	}

}