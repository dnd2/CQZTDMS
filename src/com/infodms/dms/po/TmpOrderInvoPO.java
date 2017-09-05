/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-11-22 10:02:05
* CreateBy   : wswcx
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmpOrderInvoPO extends PO{

	private String invoiceDep;
	private String orderType;
	private String invoiceNo;
	private Date invoiceDate;
	private Integer rowNum;
	private String gJsje;
	private String orderNo;
	private String gZkse;
	private String gZkje;
	private String invoiceNoVer;
	private String pCode;
	private String invoiceNum;
	private Long userId;
	private String sName;
	private String gJsse;
	private String gJssl;
	private String invoiceRemark;
	private Long id;

	public void setInvoiceDep(String invoiceDep){
		this.invoiceDep=invoiceDep;
	}

	public String getInvoiceDep(){
		return this.invoiceDep;
	}

	public void setOrderType(String orderType){
		this.orderType=orderType;
	}

	public String getOrderType(){
		return this.orderType;
	}

	public void setInvoiceNo(String invoiceNo){
		this.invoiceNo=invoiceNo;
	}

	public String getInvoiceNo(){
		return this.invoiceNo;
	}

	public void setInvoiceDate(Date invoiceDate){
		this.invoiceDate=invoiceDate;
	}

	public Date getInvoiceDate(){
		return this.invoiceDate;
	}

	public void setRowNum(Integer rowNum){
		this.rowNum=rowNum;
	}

	public Integer getRowNum(){
		return this.rowNum;
	}

	public void setGJsje(String gJsje){
		this.gJsje=gJsje;
	}

	public String getGJsje(){
		return this.gJsje;
	}

	public void setOrderNo(String orderNo){
		this.orderNo=orderNo;
	}

	public String getOrderNo(){
		return this.orderNo;
	}

	public void setGZkse(String gZkse){
		this.gZkse=gZkse;
	}

	public String getGZkse(){
		return this.gZkse;
	}

	public void setGZkje(String gZkje){
		this.gZkje=gZkje;
	}

	public String getGZkje(){
		return this.gZkje;
	}

	public void setInvoiceNoVer(String invoiceNoVer){
		this.invoiceNoVer=invoiceNoVer;
	}

	public String getInvoiceNoVer(){
		return this.invoiceNoVer;
	}

	public void setPCode(String pCode){
		this.pCode=pCode;
	}

	public String getPCode(){
		return this.pCode;
	}

	public void setInvoiceNum(String invoiceNum){
		this.invoiceNum=invoiceNum;
	}

	public String getInvoiceNum(){
		return this.invoiceNum;
	}

	public void setUserId(Long userId){
		this.userId=userId;
	}

	public Long getUserId(){
		return this.userId;
	}

	public void setSName(String sName){
		this.sName=sName;
	}

	public String getSName(){
		return this.sName;
	}

	public void setGJsse(String gJsse){
		this.gJsse=gJsse;
	}

	public String getGJsse(){
		return this.gJsse;
	}

	public void setGJssl(String gJssl){
		this.gJssl=gJssl;
	}

	public String getGJssl(){
		return this.gJssl;
	}

	public void setInvoiceRemark(String invoiceRemark){
		this.invoiceRemark=invoiceRemark;
	}

	public String getInvoiceRemark(){
		return this.invoiceRemark;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

}