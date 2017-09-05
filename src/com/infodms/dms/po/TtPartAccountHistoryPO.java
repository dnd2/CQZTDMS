/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-04-16 11:52:00
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartAccountHistoryPO extends PO{

	private Integer state;
	private String childorgName;
	private Integer acountKind;
	private String childorgCode;
	private String acountType;
	private String invoiceNo;
	private Long accountId;
	private Double discountAmount;
	private Long childorgId;
	private Long createBy;
	private String remark;
	private Double saleQty;
	private Integer status;
	private Double amount;
	private Long histroryId;
	private Long parentorgId;
	private String parentorgCode;
	private Date createDate;
	private String parentorgName;

	public void setState(Integer state){
		this.state=state;
	}

	public Integer getState(){
		return this.state;
	}

	public void setChildorgName(String childorgName){
		this.childorgName=childorgName;
	}

	public String getChildorgName(){
		return this.childorgName;
	}

	public void setAcountKind(Integer acountKind){
		this.acountKind=acountKind;
	}

	public Integer getAcountKind(){
		return this.acountKind;
	}

	public void setChildorgCode(String childorgCode){
		this.childorgCode=childorgCode;
	}

	public String getChildorgCode(){
		return this.childorgCode;
	}

	public void setAcountType(String acountType){
		this.acountType=acountType;
	}

	public String getAcountType(){
		return this.acountType;
	}

	public void setInvoiceNo(String invoiceNo){
		this.invoiceNo=invoiceNo;
	}

	public String getInvoiceNo(){
		return this.invoiceNo;
	}

	public void setAccountId(Long accountId){
		this.accountId=accountId;
	}

	public Long getAccountId(){
		return this.accountId;
	}

	public void setDiscountAmount(Double discountAmount){
		this.discountAmount=discountAmount;
	}

	public Double getDiscountAmount(){
		return this.discountAmount;
	}

	public void setChildorgId(Long childorgId){
		this.childorgId=childorgId;
	}

	public Long getChildorgId(){
		return this.childorgId;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setSaleQty(Double saleQty){
		this.saleQty=saleQty;
	}

	public Double getSaleQty(){
		return this.saleQty;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setAmount(Double amount){
		this.amount=amount;
	}

	public Double getAmount(){
		return this.amount;
	}

	public void setHistroryId(Long histroryId){
		this.histroryId=histroryId;
	}

	public Long getHistroryId(){
		return this.histroryId;
	}

	public void setParentorgId(Long parentorgId){
		this.parentorgId=parentorgId;
	}

	public Long getParentorgId(){
		return this.parentorgId;
	}

	public void setParentorgCode(String parentorgCode){
		this.parentorgCode=parentorgCode;
	}

	public String getParentorgCode(){
		return this.parentorgCode;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setParentorgName(String parentorgName){
		this.parentorgName=parentorgName;
	}

	public String getParentorgName(){
		return this.parentorgName;
	}

}