/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-03-11 11:46:03
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPurchaseInvoicesProductPO extends PO{

	private Long invoiceId;
	private Integer amount;
	private Double taxSum;
	private Double taxTotalSum;
	private Double taxPrice;
	private Double taxRate;
	private Long invoiceProductId;
	private String productCode;
	private String productName;
	private Date createDate;
	private Long createUser;
	


	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Long getCreateUser() {
		return createUser;
	}

	public void setCreateUser(Long createUser) {
		this.createUser = createUser;
	}

	public void setInvoiceId(Long invoiceId){
		this.invoiceId=invoiceId;
	}

	public Long getInvoiceId(){
		return this.invoiceId;
	}

	public void setAmount(Integer amount){
		this.amount=amount;
	}

	public Integer getAmount(){
		return this.amount;
	}

	public void setTaxSum(Double taxSum){
		this.taxSum=taxSum;
	}

	public Double getTaxSum(){
		return this.taxSum;
	}

	public void setTaxTotalSum(Double taxTotalSum){
		this.taxTotalSum=taxTotalSum;
	}

	public Double getTaxTotalSum(){
		return this.taxTotalSum;
	}

	public void setTaxPrice(Double taxPrice){
		this.taxPrice=taxPrice;
	}

	public Double getTaxPrice(){
		return this.taxPrice;
	}

	public void setTaxRate(Double taxRate){
		this.taxRate=taxRate;
	}

	public Double getTaxRate(){
		return this.taxRate;
	}

	public void setInvoiceProductId(Long invoiceProductId){
		this.invoiceProductId=invoiceProductId;
	}

	public Long getInvoiceProductId(){
		return this.invoiceProductId;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}



}