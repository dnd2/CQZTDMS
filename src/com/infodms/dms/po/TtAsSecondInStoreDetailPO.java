/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-09-05 11:14:41
* CreateBy   : yuewei
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsSecondInStoreDetailPO extends PO{

	private String dealerId;
	private String dealerCode;
	private String claimId;
	private String isMainCode;
	private Long id;
	private String remark;
	private String balanceNo;
	private Double amount;
	private String partCode;
	private Date createDate;
	private Long createBy;
	private String isBc;
	
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setDealerId(String dealerId){
		this.dealerId=dealerId;
	}

	public String getDealerId(){
		return this.dealerId;
	}

	public void setDealerCode(String dealerCode){
		this.dealerCode=dealerCode;
	}

	public String getDealerCode(){
		return this.dealerCode;
	}

	public void setClaimId(String claimId){
		this.claimId=claimId;
	}

	public String getClaimId(){
		return this.claimId;
	}

	public void setIsMainCode(String isMainCode){
		this.isMainCode=isMainCode;
	}

	public String getIsMainCode(){
		return this.isMainCode;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setBalanceNo(String balanceNo){
		this.balanceNo=balanceNo;
	}

	public String getBalanceNo(){
		return this.balanceNo;
	}

	public void setAmount(Double amount){
		this.amount=amount;
	}

	public Double getAmount(){
		return this.amount;
	}

	public void setPartCode(String partCode){
		this.partCode=partCode;
	}

	public String getPartCode(){
		return this.partCode;
	}

	public Long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	public String getIsBc() {
		return isBc;
	}

	public void setIsBc(String isBc) {
		this.isBc = isBc;
	}

}