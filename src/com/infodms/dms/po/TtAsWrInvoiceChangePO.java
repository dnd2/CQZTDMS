/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2011-10-10 15:08:57
* CreateBy   : ray
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrInvoiceChangePO extends PO{

	private Long delaerid;
	private Date updateDate;
	private String changeNo;
	private Date auditingDate;
	private String remark;
	private String createBy;
	private String dealercode;
	private String balanceNo;
	private Integer status;
	private String invoiceMark;
	private String dealername;
	private String auditingPerson;
	private String updateBy;
	private Long id;
	private Integer yieldly;
	private Date createDate;
	private String newInvoiceMark;
	private Long newInvoiceId;
	private Date submitDate;
	private String submitPerson;
	private String invoiceDate;
	private String auditingRemark;
	private String newInvoiceCode;
	public String getNewInvoiceCode() {
		return newInvoiceCode;
	}

	public void setNewInvoiceCode(String newInvoiceCode) {
		this.newInvoiceCode = newInvoiceCode;
	}

	public String getAuditingRemark() {
		return auditingRemark;
	}

	public void setAuditingRemark(String auditingRemark) {
		this.auditingRemark = auditingRemark;
	}

	public String getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public Date getSubmitDate() {
		return submitDate;
	}

	public void setSubmitDate(Date submitDate) {
		this.submitDate = submitDate;
	}

	public String getSubmitPerson() {
		return submitPerson;
	}

	public void setSubmitPerson(String submitPerson) {
		this.submitPerson = submitPerson;
	}

	public void setDelaerid(Long delaerid){
		this.delaerid=delaerid;
	}

	public Long getDelaerid(){
		return this.delaerid;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setChangeNo(String changeNo){
		this.changeNo=changeNo;
	}

	public String getChangeNo(){
		return this.changeNo;
	}

	public void setAuditingDate(Date auditingDate){
		this.auditingDate=auditingDate;
	}

	public Date getAuditingDate(){
		return this.auditingDate;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setCreateBy(String createBy){
		this.createBy=createBy;
	}

	public String getCreateBy(){
		return this.createBy;
	}

	public void setDealercode(String dealercode){
		this.dealercode=dealercode;
	}

	public String getDealercode(){
		return this.dealercode;
	}

	public void setBalanceNo(String balanceNo){
		this.balanceNo=balanceNo;
	}

	public String getBalanceNo(){
		return this.balanceNo;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setInvoiceMark(String invoiceMark){
		this.invoiceMark=invoiceMark;
	}

	public String getInvoiceMark(){
		return this.invoiceMark;
	}

	public void setDealername(String dealername){
		this.dealername=dealername;
	}

	public String getDealername(){
		return this.dealername;
	}

	public void setAuditingPerson(String auditingPerson){
		this.auditingPerson=auditingPerson;
	}

	public String getAuditingPerson(){
		return this.auditingPerson;
	}

	public void setUpdateBy(String updateBy){
		this.updateBy=updateBy;
	}

	public String getUpdateBy(){
		return this.updateBy;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setYieldly(Integer yieldly){
		this.yieldly=yieldly;
	}

	public Integer getYieldly(){
		return this.yieldly;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setNewInvoiceMark(String newInvoiceMark){
		this.newInvoiceMark=newInvoiceMark;
	}

	public String getNewInvoiceMark(){
		return this.newInvoiceMark;
	}

	public void setNewInvoiceId(Long newInvoiceId){
		this.newInvoiceId=newInvoiceId;
	}

	public Long getNewInvoiceId(){
		return this.newInvoiceId;
	}

}