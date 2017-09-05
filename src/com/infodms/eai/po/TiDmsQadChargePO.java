/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-01-05 18:40:34
* CreateBy   : xwj
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.eai.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TiDmsQadChargePO extends PO{

	private String referenceNo;
	private String accountingUnit;
	private Date updateDate;
	private String agentCode;
	private String subject;
	private String invoiceNo;
	private Long createBy;
	private Date invoiceDate;
	private String fkVoucherNo;
	private Date createDate;
	private String campaignName;
	private String gkVoucherNo;
	private String agentName;
	private Integer actType;
	private String campaignNo;
	private Long chargeId;
	private Long updateBy;
	private Integer dmsStatus;
	private Long seqId;
	private Date dmsDate;
	private Double money;
	private Date confirmDate;

	public Date getConfirmDate() {
		return confirmDate;
	}

	public void setConfirmDate(Date confirmDate) {
		this.confirmDate = confirmDate;
	}

	public void setReferenceNo(String referenceNo){
		this.referenceNo=referenceNo;
	}

	public String getReferenceNo(){
		return this.referenceNo;
	}

	public void setAccountingUnit(String accountingUnit){
		this.accountingUnit=accountingUnit;
	}

	public String getAccountingUnit(){
		return this.accountingUnit;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setAgentCode(String agentCode){
		this.agentCode=agentCode;
	}

	public String getAgentCode(){
		return this.agentCode;
	}

	public void setSubject(String subject){
		this.subject=subject;
	}

	public String getSubject(){
		return this.subject;
	}

	public void setInvoiceNo(String invoiceNo){
		this.invoiceNo=invoiceNo;
	}

	public String getInvoiceNo(){
		return this.invoiceNo;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setInvoiceDate(Date invoiceDate){
		this.invoiceDate=invoiceDate;
	}

	public Date getInvoiceDate(){
		return this.invoiceDate;
	}

	public void setFkVoucherNo(String fkVoucherNo){
		this.fkVoucherNo=fkVoucherNo;
	}

	public String getFkVoucherNo(){
		return this.fkVoucherNo;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setCampaignName(String campaignName){
		this.campaignName=campaignName;
	}

	public String getCampaignName(){
		return this.campaignName;
	}

	public void setGkVoucherNo(String gkVoucherNo){
		this.gkVoucherNo=gkVoucherNo;
	}

	public String getGkVoucherNo(){
		return this.gkVoucherNo;
	}

	public void setAgentName(String agentName){
		this.agentName=agentName;
	}

	public String getAgentName(){
		return this.agentName;
	}

	public void setActType(Integer actType){
		this.actType=actType;
	}

	public Integer getActType(){
		return this.actType;
	}

	public void setCampaignNo(String campaignNo){
		this.campaignNo=campaignNo;
	}

	public String getCampaignNo(){
		return this.campaignNo;
	}

	public void setChargeId(Long chargeId){
		this.chargeId=chargeId;
	}

	public Long getChargeId(){
		return this.chargeId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setDmsStatus(Integer dmsStatus){
		this.dmsStatus=dmsStatus;
	}

	public Integer getDmsStatus(){
		return this.dmsStatus;
	}

	public void setSeqId(Long seqId){
		this.seqId=seqId;
	}

	public Long getSeqId(){
		return this.seqId;
	}

	public void setDmsDate(Date dmsDate){
		this.dmsDate=dmsDate;
	}

	public Date getDmsDate(){
		return this.dmsDate;
	}

	public void setMoney(Double money){
		this.money=money;
	}

	public Double getMoney(){
		return this.money;
	}

}