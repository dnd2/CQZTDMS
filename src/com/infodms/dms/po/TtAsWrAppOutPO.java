/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2017-08-28 18:29:44
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrAppOutPO extends PO{

	private Double feeClaimPrice;
	private String feeCode;
	private String feeName;
	private Long outId;
	private Integer feePaymentMethod;
	private Long appclaimId;
	private Double feePrice;
	private String feeRemark;
	private Long createBy;
	private Double feeSettlementPrice;
	private Date createDate;
	private Long feeRelationMainPart;
	private Long feeId;

	public void setFeeClaimPrice(Double feeClaimPrice){
		this.feeClaimPrice=feeClaimPrice;
	}

	public Double getFeeClaimPrice(){
		return this.feeClaimPrice;
	}

	public void setFeeCode(String feeCode){
		this.feeCode=feeCode;
	}

	public String getFeeCode(){
		return this.feeCode;
	}

	public void setFeeName(String feeName){
		this.feeName=feeName;
	}

	public String getFeeName(){
		return this.feeName;
	}

	public void setOutId(Long outId){
		this.outId=outId;
	}

	public Long getOutId(){
		return this.outId;
	}

	public void setFeePaymentMethod(Integer feePaymentMethod){
		this.feePaymentMethod=feePaymentMethod;
	}

	public Integer getFeePaymentMethod(){
		return this.feePaymentMethod;
	}

	public void setAppclaimId(Long appclaimId){
		this.appclaimId=appclaimId;
	}

	public Long getAppclaimId(){
		return this.appclaimId;
	}

	public void setFeePrice(Double feePrice){
		this.feePrice=feePrice;
	}

	public Double getFeePrice(){
		return this.feePrice;
	}

	public void setFeeRemark(String feeRemark){
		this.feeRemark=feeRemark;
	}

	public String getFeeRemark(){
		return this.feeRemark;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setFeeSettlementPrice(Double feeSettlementPrice){
		this.feeSettlementPrice=feeSettlementPrice;
	}

	public Double getFeeSettlementPrice(){
		return this.feeSettlementPrice;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setFeeRelationMainPart(Long feeRelationMainPart){
		this.feeRelationMainPart=feeRelationMainPart;
	}

	public Long getFeeRelationMainPart(){
		return this.feeRelationMainPart;
	}

	public void setFeeId(Long feeId){
		this.feeId=feeId;
	}

	public Long getFeeId(){
		return this.feeId;
	}

}