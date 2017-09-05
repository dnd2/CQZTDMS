/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-04-18 09:28:35
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmpVsRebatePO extends PO{

	private String evidenceNo;
	private String rebateNo;
	private String userId;
	private String rebateType;
	private String rowNum;
	private String areaName;
	private String dealerCode;
	private String totalAmount;
	private String ver;
	private String remark;
	private String useType;
	private String rebAmount;
	

	public String getRebAmount() {
		return rebAmount;
	}

	public void setRebAmount(String rebAmount) {
		this.rebAmount = rebAmount;
	}

	public void setEvidenceNo(String evidenceNo){
		this.evidenceNo=evidenceNo;
	}

	public String getEvidenceNo(){
		return this.evidenceNo;
	}

	public void setRebateNo(String rebateNo){
		this.rebateNo=rebateNo;
	}

	public String getRebateNo(){
		return this.rebateNo;
	}

	public void setUserId(String userId){
		this.userId=userId;
	}

	public String getUserId(){
		return this.userId;
	}

	public void setRebateType(String rebateType){
		this.rebateType=rebateType;
	}

	public String getRebateType(){
		return this.rebateType;
	}

	public void setRowNum(String rowNum){
		this.rowNum=rowNum;
	}

	public String getRowNum(){
		return this.rowNum;
	}

	public void setAreaName(String areaName){
		this.areaName=areaName;
	}

	public String getAreaName(){
		return this.areaName;
	}

	public void setDealerCode(String dealerCode){
		this.dealerCode=dealerCode;
	}

	public String getDealerCode(){
		return this.dealerCode;
	}

	public void setTotalAmount(String totalAmount){
		this.totalAmount=totalAmount;
	}

	public String getTotalAmount(){
		return this.totalAmount;
	}

	public void setVer(String ver){
		this.ver=ver;
	}

	public String getVer(){
		return this.ver;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setUseType(String useType){
		this.useType=useType;
	}

	public String getUseType(){
		return this.useType;
	}

}