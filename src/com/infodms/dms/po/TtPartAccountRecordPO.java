/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-07-26 16:46:29
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartAccountRecordPO extends PO{

	private Integer state;
	private String invoiceNo;
	private Long dealerId;
	private Long sourceId;
	private Long accountId;
	private Integer changeType;
	private String orderCode;
	private String remark;
	private Long createBy;
	private Double invoAmount;
	private String sourceCode;
	private Integer status;
	private Double amount;
	private Long orderId;
	private Long recordId;
	private String functionName;
	private Date createDate;
	private String boCode;
	private Long boId;


	public void setBoId(Long boId){
		this.boId=boId;
	}

	public Long getBoId(){
		return this.boId;
	}


	public void setBoCode(String boCode){
		this.boCode=boCode;
	}

	public String getBoCode(){
		return this.boCode;
	}
	public void setState(Integer state){
		this.state=state;
	}

	public Integer getState(){
		return this.state;
	}

	public void setInvoiceNo(String invoiceNo){
		this.invoiceNo=invoiceNo;
	}

	public String getInvoiceNo(){
		return this.invoiceNo;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setSourceId(Long sourceId){
		this.sourceId=sourceId;
	}

	public Long getSourceId(){
		return this.sourceId;
	}

	public void setAccountId(Long accountId){
		this.accountId=accountId;
	}

	public Long getAccountId(){
		return this.accountId;
	}

	public void setChangeType(Integer changeType){
		this.changeType=changeType;
	}

	public Integer getChangeType(){
		return this.changeType;
	}

	public void setOrderCode(String orderCode){
		this.orderCode=orderCode;
	}

	public String getOrderCode(){
		return this.orderCode;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setInvoAmount(Double invoAmount){
		this.invoAmount=invoAmount;
	}

	public Double getInvoAmount(){
		return this.invoAmount;
	}

	public void setSourceCode(String sourceCode){
		this.sourceCode=sourceCode;
	}

	public String getSourceCode(){
		return this.sourceCode;
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

	public void setOrderId(Long orderId){
		this.orderId=orderId;
	}

	public Long getOrderId(){
		return this.orderId;
	}

	public void setRecordId(Long recordId){
		this.recordId=recordId;
	}

	public Long getRecordId(){
		return this.recordId;
	}

	public void setFunctionName(String functionName){
		this.functionName=functionName;
	}

	public String getFunctionName(){
		return this.functionName;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

}