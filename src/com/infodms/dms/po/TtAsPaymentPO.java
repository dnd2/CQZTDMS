/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-09-29 16:41:25
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsPaymentPO extends PO{

	private Date creatDate;
	private String labourReceipt;
	private Long paymentType;
	private String balanceOder;
	private String partReceipt;
	private Long taxRate;
	private String remark;
	private String serialNumber;
	private Integer status;
	private Double  amountOfMoney;
	private Double  taxRateMoney;
	private Double  fineAmountOfMoney;
	private Double  fineTaxRateMoney;
	private Double  amountSum;
	private Long id;
	private String invoice;
	private String createBy;
	private String project;
	

	

	private Long collectTickets;
	private Long checkTickets;
	private Long transferTickets;
	private Date collectTicketsDate;
	private Date checkTicketsDate;
	private Date transferTicketsDate;
	private String notes;
	
	
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getInvoice() {
		return invoice;
	}

	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	public Double getAmountSum() {
		return amountSum;
	}

	public void setAmountSum(Double amountSum) {
		this.amountSum = amountSum;
	}

	public void setCreatDate(Date creatDate){
		this.creatDate=creatDate;
	}

	public Date getCreatDate(){
		return this.creatDate;
	}

	public void setLabourReceipt(String labourReceipt){
		this.labourReceipt=labourReceipt;
	}

	public String getLabourReceipt(){
		return this.labourReceipt;
	}

	public void setPaymentType(Long paymentType){
		this.paymentType=paymentType;
	}

	public Long getPaymentType(){
		return this.paymentType;
	}

	public void setBalanceOder(String balanceOder){
		this.balanceOder=balanceOder;
	}

	public String getBalanceOder(){
		return this.balanceOder;
	}

	public void setPartReceipt(String partReceipt){
		this.partReceipt=partReceipt;
	}

	public String getPartReceipt(){
		return this.partReceipt;
	}

	public void setTaxRate(Long taxRate){
		this.taxRate=taxRate;
	}

	public Long getTaxRate(){
		return this.taxRate;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Double getAmountOfMoney() {
		return amountOfMoney;
	}

	public void setAmountOfMoney(Double amountOfMoney) {
		this.amountOfMoney = amountOfMoney;
	}

	public Double getTaxRateMoney() {
		return taxRateMoney;
	}

	public void setTaxRateMoney(Double taxRateMoney) {
		this.taxRateMoney = taxRateMoney;
	}

	public Double getFineAmountOfMoney() {
		return fineAmountOfMoney;
	}

	public void setFineAmountOfMoney(Double fineAmountOfMoney) {
		this.fineAmountOfMoney = fineAmountOfMoney;
	}

	public Double getFineTaxRateMoney() {
		return fineTaxRateMoney;
	}

	public void setFineTaxRateMoney(Double fineTaxRateMoney) {
		this.fineTaxRateMoney = fineTaxRateMoney;
	}

	public Long getCollectTickets() {
		return collectTickets;
	}

	public void setCollectTickets(Long collectTickets) {
		this.collectTickets = collectTickets;
	}

	public Long getCheckTickets() {
		return checkTickets;
	}

	public void setCheckTickets(Long checkTickets) {
		this.checkTickets = checkTickets;
	}

	public Long getTransferTickets() {
		return transferTickets;
	}

	public void setTransferTickets(Long transferTickets) {
		this.transferTickets = transferTickets;
	}

	public Date getCollectTicketsDate() {
		return collectTicketsDate;
	}

	public void setCollectTicketsDate(Date collectTicketsDate) {
		this.collectTicketsDate = collectTicketsDate;
	}

	public Date getCheckTicketsDate() {
		return checkTicketsDate;
	}

	public void setCheckTicketsDate(Date checkTicketsDate) {
		this.checkTicketsDate = checkTicketsDate;
	}

	public Date getTransferTicketsDate() {
		return transferTicketsDate;
	}

	public void setTransferTicketsDate(Date transferTicketsDate) {
		this.transferTicketsDate = transferTicketsDate;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

}