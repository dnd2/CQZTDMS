/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-03-07 15:15:24
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtSalesAllocaDePO extends PO{

	private Float singlePrice;
	private Integer pjCount;
	private Date updateDate;
	private Long sendBy;
	private Long createBy;
	private Integer isSend;
	private Long outBy;
	private Long detailId;
	private Integer status;
	private Integer isPj;
	private Date outDate;
	private Long updateBy;
	private Date reBillPrintDate;
	private Date accDate;
	private Long sendDistance;
	private Long vehicleId;
	private Double balAmount;
	private Long accBy;
	private Date sendDate;
	private Integer isOut;
	private Long assDetailId;
	private Date allocaDate;
	private Integer isGxp;
	private Long allocaPer;
	private Integer gxpCount;
	private Long reBillPrintPer;
	private Integer isAcc;
	private Long billId;
	private Date createDate;
	
	private Integer invoiceStatus;
	private Long invoiceUser;
	private Date invoiceDate;
	private String invoiceNo;
	private String invoiceRemark;
	private Long boDeId;
	private Double fuelCoefficient;//燃油附加调节系数
	private Double deductMoney;
	private Double otherMoney;
	private String balRemark;
	
	public Double getFuelCoefficient() {
		return fuelCoefficient;
	}

	public void setFuelCoefficient(Double fuelCoefficient) {
		this.fuelCoefficient = fuelCoefficient;
	}

	public Double getDeductMoney() {
		return deductMoney;
	}

	public void setDeductMoney(Double deductMoney) {
		this.deductMoney = deductMoney;
	}

	public Double getOtherMoney() {
		return otherMoney;
	}

	public void setOtherMoney(Double otherMoney) {
		this.otherMoney = otherMoney;
	}

	public String getBalRemark() {
		return balRemark;
	}

	public void setBalRemark(String balRemark) {
		this.balRemark = balRemark;
	}

	public Long getBoDeId() {
		return boDeId;
	}

	public void setBoDeId(Long boDeId) {
		this.boDeId = boDeId;
	}

	public Integer getInvoiceStatus() {
		return invoiceStatus;
	}

	public void setInvoiceStatus(Integer invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}

	public Long getInvoiceUser() {
		return invoiceUser;
	}

	public void setInvoiceUser(Long invoiceUser) {
		this.invoiceUser = invoiceUser;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getInvoiceRemark() {
		return invoiceRemark;
	}

	public void setInvoiceRemark(String invoiceRemark) {
		this.invoiceRemark = invoiceRemark;
	}

	

	public void setSinglePrice(Float singlePrice){
		this.singlePrice=singlePrice;
	}

	public Float getSinglePrice(){
		return this.singlePrice;
	}

	public void setPjCount(Integer pjCount){
		this.pjCount=pjCount;
	}

	public Integer getPjCount(){
		return this.pjCount;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setSendBy(Long sendBy){
		this.sendBy=sendBy;
	}

	public Long getSendBy(){
		return this.sendBy;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setIsSend(Integer isSend){
		this.isSend=isSend;
	}

	public Integer getIsSend(){
		return this.isSend;
	}

	public void setOutBy(Long outBy){
		this.outBy=outBy;
	}

	public Long getOutBy(){
		return this.outBy;
	}

	public void setDetailId(Long detailId){
		this.detailId=detailId;
	}

	public Long getDetailId(){
		return this.detailId;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setIsPj(Integer isPj){
		this.isPj=isPj;
	}

	public Integer getIsPj(){
		return this.isPj;
	}

	public void setOutDate(Date outDate){
		this.outDate=outDate;
	}

	public Date getOutDate(){
		return this.outDate;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setReBillPrintDate(Date reBillPrintDate){
		this.reBillPrintDate=reBillPrintDate;
	}

	public Date getReBillPrintDate(){
		return this.reBillPrintDate;
	}

	public void setAccDate(Date accDate){
		this.accDate=accDate;
	}

	public Date getAccDate(){
		return this.accDate;
	}

	public void setSendDistance(Long sendDistance){
		this.sendDistance=sendDistance;
	}

	public Long getSendDistance(){
		return this.sendDistance;
	}

	public void setVehicleId(Long vehicleId){
		this.vehicleId=vehicleId;
	}

	public Long getVehicleId(){
		return this.vehicleId;
	}

	public void setBalAmount(Double balAmount){
		this.balAmount=balAmount;
	}

	public Double getBalAmount(){
		return this.balAmount;
	}

	public void setAccBy(Long accBy){
		this.accBy=accBy;
	}

	public Long getAccBy(){
		return this.accBy;
	}

	public void setSendDate(Date sendDate){
		this.sendDate=sendDate;
	}

	public Date getSendDate(){
		return this.sendDate;
	}

	public void setIsOut(Integer isOut){
		this.isOut=isOut;
	}

	public Integer getIsOut(){
		return this.isOut;
	}

	public void setAssDetailId(Long assDetailId){
		this.assDetailId=assDetailId;
	}

	public Long getAssDetailId(){
		return this.assDetailId;
	}

	public void setAllocaDate(Date allocaDate){
		this.allocaDate=allocaDate;
	}

	public Date getAllocaDate(){
		return this.allocaDate;
	}

	public void setIsGxp(Integer isGxp){
		this.isGxp=isGxp;
	}

	public Integer getIsGxp(){
		return this.isGxp;
	}

	public void setAllocaPer(Long allocaPer){
		this.allocaPer=allocaPer;
	}

	public Long getAllocaPer(){
		return this.allocaPer;
	}

	public void setGxpCount(Integer gxpCount){
		this.gxpCount=gxpCount;
	}

	public Integer getGxpCount(){
		return this.gxpCount;
	}

	public void setReBillPrintPer(Long reBillPrintPer){
		this.reBillPrintPer=reBillPrintPer;
	}

	public Long getReBillPrintPer(){
		return this.reBillPrintPer;
	}

	public void setIsAcc(Integer isAcc){
		this.isAcc=isAcc;
	}

	public Integer getIsAcc(){
		return this.isAcc;
	}

	public void setBillId(Long billId){
		this.billId=billId;
	}

	public Long getBillId(){
		return this.billId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

}