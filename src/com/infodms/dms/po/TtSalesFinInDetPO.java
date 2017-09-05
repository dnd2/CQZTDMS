/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-04-15 21:48:34
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtSalesFinInDetPO extends PO{

	private Long auditPer;
	private Long finType;
	private Long dealerId;
	private Date updateDate;
	private String orderNo;
	private Long createBy;
	private String remark;
	private Long detailId;
	private Double amount;
	private Long status;
	private String evidenceNo;
	private Long updateBy;
	private Date auditDate;
	private Long accId;
	private String auditRemark;
	private Date createDate;
	private Long yieldly;
	private String summary;
	private String ncpk;
	private String bankCode;
	private String vins;
	
	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public void setAuditPer(Long auditPer){
		this.auditPer=auditPer;
	}

	public Long getAuditPer(){
		return this.auditPer;
	}

	public void setFinType(Long finType){
		this.finType=finType;
	}

	public Long getFinType(){
		return this.finType;
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

	public void setOrderNo(String orderNo){
		this.orderNo=orderNo;
	}

	public String getOrderNo(){
		return this.orderNo;
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

	public void setDetailId(Long detailId){
		this.detailId=detailId;
	}

	public Long getDetailId(){
		return this.detailId;
	}

	public void setAmount(Double amount){
		this.amount=amount;
	}

	public Double getAmount(){
		return this.amount;
	}

	public void setStatus(Long status){
		this.status=status;
	}

	public Long getStatus(){
		return this.status;
	}

	public void setEvidenceNo(String evidenceNo){
		this.evidenceNo=evidenceNo;
	}

	public String getEvidenceNo(){
		return this.evidenceNo;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setAuditDate(Date auditDate){
		this.auditDate=auditDate;
	}

	public Date getAuditDate(){
		return this.auditDate;
	}

	public void setAccId(Long accId){
		this.accId=accId;
	}

	public Long getAccId(){
		return this.accId;
	}

	public void setAuditRemark(String auditRemark){
		this.auditRemark=auditRemark;
	}

	public String getAuditRemark(){
		return this.auditRemark;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setYieldly(Long yieldly){
		this.yieldly=yieldly;
	}

	public Long getYieldly(){
		return this.yieldly;
	}

	public String getNcpk()
	{
		return ncpk;
	}

	public void setNcpk(String ncpk)
	{
		this.ncpk = ncpk;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getVins() {
		return vins;
	}

	public void setVins(String vins) {
		this.vins = vins;
	}
	
}