/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-09-01 23:24:27
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtLaborListPO extends PO{

	private String reportCode;
	private Long dealerId;
	private Date updateDate;
	private Long createBy;
	private Double amount;
	private Integer status;
	private Long updateBy;
	private String invoiceCode;
	private String receiveMan;
	private String makeMan;
	private Integer yieldly;
	private Date reportDate;
	private Date createDate;
	private Date receiveDate;
	private Long reportId;
	
	private Double reportAmount;
	private Double authAmount;

	public Double getReportAmount() {
		return reportAmount;
	}

	public void setReportAmount(Double reportAmount) {
		this.reportAmount = reportAmount;
	}

	public Double getAuthAmount() {
		return authAmount;
	}

	public void setAuthAmount(Double authAmount) {
		this.authAmount = authAmount;
	}

	public void setReportCode(String reportCode){
		this.reportCode=reportCode;
	}

	public String getReportCode(){
		return this.reportCode;
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

	public void setAmount(Double amount){
		this.amount=amount;
	}

	public Double getAmount(){
		return this.amount;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setInvoiceCode(String invoiceCode){
		this.invoiceCode=invoiceCode;
	}

	public String getInvoiceCode(){
		return this.invoiceCode;
	}

	public void setReceiveMan(String receiveMan){
		this.receiveMan=receiveMan;
	}

	public String getReceiveMan(){
		return this.receiveMan;
	}

	public void setMakeMan(String makeMan){
		this.makeMan=makeMan;
	}

	public String getMakeMan(){
		return this.makeMan;
	}

	public void setYieldly(Integer yieldly){
		this.yieldly=yieldly;
	}

	public Integer getYieldly(){
		return this.yieldly;
	}

	public void setReportDate(Date reportDate){
		this.reportDate=reportDate;
	}

	public Date getReportDate(){
		return this.reportDate;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setReceiveDate(Date receiveDate){
		this.receiveDate=receiveDate;
	}

	public Date getReceiveDate(){
		return this.receiveDate;
	}

	public void setReportId(Long reportId){
		this.reportId=reportId;
	}

	public Long getReportId(){
		return this.reportId;
	}

}