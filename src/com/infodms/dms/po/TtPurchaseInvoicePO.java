/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-07-16 13:51:18
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPurchaseInvoicePO extends PO{

	private Long invoiceId;
	private String purchaseNo;
	private Long billDeptId;
	private Double totalAmount;
	private Date ticketDate;
	private Long accountTypeId;
	private Long dlvryReqId;
	private String retailOrderNo;
	private String purchaseInvoiceNo;
	private Long receiveDeptId;
	private String dlvryCode;
	private String invoiceCode;
	private String requisitionNo;
	private String remark;
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

	public void setPurchaseNo(String purchaseNo){
		this.purchaseNo=purchaseNo;
	}

	public String getPurchaseNo(){
		return this.purchaseNo;
	}

	public void setBillDeptId(Long billDeptId){
		this.billDeptId=billDeptId;
	}

	public Long getBillDeptId(){
		return this.billDeptId;
	}

	public void setTotalAmount(Double totalAmount){
		this.totalAmount=totalAmount;
	}

	public Double getTotalAmount(){
		return this.totalAmount;
	}

	public void setTicketDate(Date ticketDate){
		this.ticketDate=ticketDate;
	}

	public Date getTicketDate(){
		return this.ticketDate;
	}

	public void setAccountTypeId(Long accountTypeId){
		this.accountTypeId=accountTypeId;
	}

	public Long getAccountTypeId(){
		return this.accountTypeId;
	}

	public void setDlvryReqId(Long dlvryReqId){
		this.dlvryReqId=dlvryReqId;
	}

	public Long getDlvryReqId(){
		return this.dlvryReqId;
	}

	public void setRetailOrderNo(String retailOrderNo){
		this.retailOrderNo=retailOrderNo;
	}

	public String getRetailOrderNo(){
		return this.retailOrderNo;
	}

	public void setPurchaseInvoiceNo(String purchaseInvoiceNo){
		this.purchaseInvoiceNo=purchaseInvoiceNo;
	}

	public String getPurchaseInvoiceNo(){
		return this.purchaseInvoiceNo;
	}

	public void setReceiveDeptId(Long receiveDeptId){
		this.receiveDeptId=receiveDeptId;
	}

	public Long getReceiveDeptId(){
		return this.receiveDeptId;
	}

	public void setDlvryCode(String dlvryCode){
		this.dlvryCode=dlvryCode;
	}

	public String getDlvryCode(){
		return this.dlvryCode;
	}

	public void setInvoiceCode(String invoiceCode){
		this.invoiceCode=invoiceCode;
	}

	public String getInvoiceCode(){
		return this.invoiceCode;
	}

	public void setRequisitionNo(String requisitionNo){
		this.requisitionNo=requisitionNo;
	}

	public String getRequisitionNo(){
		return this.requisitionNo;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

}