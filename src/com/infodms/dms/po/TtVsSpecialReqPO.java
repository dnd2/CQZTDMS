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
public class TtVsSpecialReqPO extends PO{

	private Integer reqStatus;
	private Long dealerId;
	private Date updateDate;
	private Long createBy;
	private Long areaId;
	private Date reqDate;
	private String refitDesc;
	private Long reqId;
	private Date reqConfirmDate;
	private Long updateBy;
	private Integer ver;
	private String reqNo;
	private Date createDate;
	
	private Long fleetId;
	private Long accountTypeId;
	private Long priceId;
	private Double preAmount;
	private Long productComboId ;

	public Long getProductComboId() {
		return productComboId;
	}

	public void setProductComboId(Long productComboId) {
		this.productComboId = productComboId;
	}

	public Long getAccountTypeId() {
		return accountTypeId;
	}

	public void setAccountTypeId(Long accountTypeId) {
		this.accountTypeId = accountTypeId;
	}

	public Long getPriceId() {
		return priceId;
	}

	public void setPriceId(Long priceId) {
		this.priceId = priceId;
	}

	public Double getPreAmount() {
		return preAmount;
	}

	public void setPreAmount(Double preAmount) {
		this.preAmount = preAmount;
	}

	public Long getFleetId() {
		return fleetId;
	}

	public void setFleetId(Long fleetId) {
		this.fleetId = fleetId;
	}

	public void setReqStatus(Integer reqStatus){
		this.reqStatus=reqStatus;
	}

	public Integer getReqStatus(){
		return this.reqStatus;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
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

	public void setAreaId(Long areaId){
		this.areaId=areaId;
	}

	public Long getAreaId(){
		return this.areaId;
	}

	public void setReqDate(Date reqDate){
		this.reqDate=reqDate;
	}

	public Date getReqDate(){
		return this.reqDate;
	}

	public void setRefitDesc(String refitDesc){
		this.refitDesc=refitDesc;
	}

	public String getRefitDesc(){
		return this.refitDesc;
	}

	public void setReqId(Long reqId){
		this.reqId=reqId;
	}

	public Long getReqId(){
		return this.reqId;
	}

	public void setReqConfirmDate(Date reqConfirmDate){
		this.reqConfirmDate=reqConfirmDate;
	}

	public Date getReqConfirmDate(){
		return this.reqConfirmDate;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setVer(Integer ver){
		this.ver=ver;
	}

	public Integer getVer(){
		return this.ver;
	}

	public void setReqNo(String reqNo){
		this.reqNo=reqNo;
	}

	public String getReqNo(){
		return this.reqNo;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

}