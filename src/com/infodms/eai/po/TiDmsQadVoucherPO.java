/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-01-05 18:40:33
* CreateBy   : xwj
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.eai.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TiDmsQadVoucherPO extends PO{

	private String referenceNo;
	private Date updateDate;
	private String agentCode;
	private Double rebateMoney;
	private Long voucherId;
	private String invoiceNo;
	private Long createBy;
	private Date invoiceDate;
	private String chVoucherNo;
	private Date createDate;
	private String orderNo;
	private Integer num;
	private Double rebatePoint;
	private Integer actType;
	private String carTypeName;
	private String carTypeCode;
	private Long updateBy;
	private String typeDesc;
	private Double price;
	private Integer dmsStatus;
	private Long seqId;
	private String approvFileNo;
	private Date dmsDate;
	private String accountingUnit;

	public String getAccountingUnit() {
		return accountingUnit;
	}

	public void setAccountingUnit(String accountingUnit) {
		this.accountingUnit = accountingUnit;
	}

	public void setReferenceNo(String referenceNo){
		this.referenceNo=referenceNo;
	}

	public String getReferenceNo(){
		return this.referenceNo;
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

	public void setRebateMoney(Double rebateMoney){
		this.rebateMoney=rebateMoney;
	}

	public Double getRebateMoney(){
		return this.rebateMoney;
	}

	public void setVoucherId(Long voucherId){
		this.voucherId=voucherId;
	}

	public Long getVoucherId(){
		return this.voucherId;
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

	public void setChVoucherNo(String chVoucherNo){
		this.chVoucherNo=chVoucherNo;
	}

	public String getChVoucherNo(){
		return this.chVoucherNo;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setOrderNo(String orderNo){
		this.orderNo=orderNo;
	}

	public String getOrderNo(){
		return this.orderNo;
	}

	public void setNum(Integer num){
		this.num=num;
	}

	public Integer getNum(){
		return this.num;
	}

	public void setRebatePoint(Double rebatePoint){
		this.rebatePoint=rebatePoint;
	}

	public Double getRebatePoint(){
		return this.rebatePoint;
	}

	public void setActType(Integer actType){
		this.actType=actType;
	}

	public Integer getActType(){
		return this.actType;
	}

	public void setCarTypeName(String carTypeName){
		this.carTypeName=carTypeName;
	}

	public String getCarTypeName(){
		return this.carTypeName;
	}

	public void setCarTypeCode(String carTypeCode){
		this.carTypeCode=carTypeCode;
	}

	public String getCarTypeCode(){
		return this.carTypeCode;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setTypeDesc(String typeDesc){
		this.typeDesc=typeDesc;
	}

	public String getTypeDesc(){
		return this.typeDesc;
	}

	public void setPrice(Double price){
		this.price=price;
	}

	public Double getPrice(){
		return this.price;
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

	public void setApprovFileNo(String approvFileNo){
		this.approvFileNo=approvFileNo;
	}

	public String getApprovFileNo(){
		return this.approvFileNo;
	}

	public void setDmsDate(Date dmsDate){
		this.dmsDate=dmsDate;
	}

	public Date getDmsDate(){
		return this.dmsDate;
	}

}