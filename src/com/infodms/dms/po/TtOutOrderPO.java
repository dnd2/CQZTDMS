/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-09-06 14:46:15
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtOutOrderPO extends PO{

	private Integer orderStatus;
	private Long dealerId;
	private Long fundTypeId;
	private Double rebatePrice;
	private Date updateDate;
	private Long createBy;
	private Integer invoType;
	private Long areaId;
	private Date invoDate;
	private String resRemark;
	private Long updateBy;
	private Double discount;
	private Date raiseDate;
	private String towRemark;
	private String invoiceNo;
	private String orderRemark;
	private Double orderPrice;
	private String orderNo;
	private String isRetail;
	private Double orderYfPrice;
	private Long orderId;
	private Integer ver;
	private Date createDate;
	private String invoiceVer;
	private String invoRemark;
	private String finRemark;
	private Integer auditDpt;
	private String isPromise;
	
	public String getIsPromise()
	{
		return isPromise;
	}

	public void setIsPromise(String isPromise)
	{
		this.isPromise = isPromise;
	}

	public Integer getAuditDpt() {
		return auditDpt;
	}

	public void setAuditDpt(Integer auditDpt) {
		this.auditDpt = auditDpt;
	}

	public void setOrderStatus(Integer orderStatus){
		this.orderStatus=orderStatus;
	}

	public Integer getOrderStatus(){
		return this.orderStatus;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setFundTypeId(Long fundTypeId){
		this.fundTypeId=fundTypeId;
	}

	public Long getFundTypeId(){
		return this.fundTypeId;
	}

	public void setRebatePrice(Double rebatePrice){
		this.rebatePrice=rebatePrice;
	}

	public Double getRebatePrice(){
		return this.rebatePrice;
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

	public void setInvoType(Integer invoType){
		this.invoType=invoType;
	}

	public Integer getInvoType(){
		return this.invoType;
	}

	public void setAreaId(Long areaId){
		this.areaId=areaId;
	}

	public Long getAreaId(){
		return this.areaId;
	}

	public void setInvoDate(Date invoDate){
		this.invoDate=invoDate;
	}

	public Date getInvoDate(){
		return this.invoDate;
	}

	public void setResRemark(String resRemark){
		this.resRemark=resRemark;
	}

	public String getResRemark(){
		return this.resRemark;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setDiscount(Double discount){
		this.discount=discount;
	}

	public Double getDiscount(){
		return this.discount;
	}

	public void setRaiseDate(Date raiseDate){
		this.raiseDate=raiseDate;
	}

	public Date getRaiseDate(){
		return this.raiseDate;
	}

	public void setTowRemark(String towRemark){
		this.towRemark=towRemark;
	}

	public String getTowRemark(){
		return this.towRemark;
	}

	public void setInvoiceNo(String invoiceNo){
		this.invoiceNo=invoiceNo;
	}

	public String getInvoiceNo(){
		return this.invoiceNo;
	}

	public void setOrderRemark(String orderRemark){
		this.orderRemark=orderRemark;
	}

	public String getOrderRemark(){
		return this.orderRemark;
	}

	public void setOrderPrice(Double orderPrice){
		this.orderPrice=orderPrice;
	}

	public Double getOrderPrice(){
		return this.orderPrice;
	}

	public void setOrderNo(String orderNo){
		this.orderNo=orderNo;
	}

	public String getOrderNo(){
		return this.orderNo;
	}

	public void setIsRetail(String isRetail){
		this.isRetail=isRetail;
	}

	public String getIsRetail(){
		return this.isRetail;
	}

	public void setOrderYfPrice(Double orderYfPrice){
		this.orderYfPrice=orderYfPrice;
	}

	public Double getOrderYfPrice(){
		return this.orderYfPrice;
	}

	public void setOrderId(Long orderId){
		this.orderId=orderId;
	}

	public Long getOrderId(){
		return this.orderId;
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

	public void setInvoiceVer(String invoiceVer){
		this.invoiceVer=invoiceVer;
	}

	public String getInvoiceVer(){
		return this.invoiceVer;
	}

	public void setInvoRemark(String invoRemark){
		this.invoRemark=invoRemark;
	}

	public String getInvoRemark(){
		return this.invoRemark;
	}

	public void setFinRemark(String finRemark){
		this.finRemark=finRemark;
	}

	public String getFinRemark(){
		return this.finRemark;
	}

}