/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-10-17 13:34:22
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsSubjietEvaluatePO extends PO{

	private Long dealerId;
	private String balanceOder;
	private Long evaluateRes;
	private Long taxRateFee;
	private Long subjectId;
	private Integer payStatus;
	private Double balanceAmount;

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setBalanceOder(String balanceOder){
		this.balanceOder=balanceOder;
	}

	public String getBalanceOder(){
		return this.balanceOder;
	}

	public void setEvaluateRes(Long evaluateRes){
		this.evaluateRes=evaluateRes;
	}

	public Long getEvaluateRes(){
		return this.evaluateRes;
	}

	public void setTaxRateFee(Long taxRateFee){
		this.taxRateFee=taxRateFee;
	}

	public Long getTaxRateFee(){
		return this.taxRateFee;
	}

	public void setSubjectId(Long subjectId){
		this.subjectId=subjectId;
	}

	public Long getSubjectId(){
		return this.subjectId;
	}

	public void setPayStatus(Integer payStatus){
		this.payStatus=payStatus;
	}

	public Integer getPayStatus(){
		return this.payStatus;
	}

	public void setBalanceAmount(Double balanceAmount){
		this.balanceAmount=balanceAmount;
	}

	public Double getBalanceAmount(){
		return this.balanceAmount;
	}

}