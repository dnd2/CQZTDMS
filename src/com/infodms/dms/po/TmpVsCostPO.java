/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-08-25 09:49:04
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmpVsCostPO extends PO{

	private String costType;
	private String costAmount;
	private String orgCode;
	private String dealerCode;
	private String rowNumber;
	private String userId;
	private String costSource;

	public void setCostType(String costType){
		this.costType=costType;
	}

	public String getCostType(){
		return this.costType;
	}

	public void setCostAmount(String costAmount){
		this.costAmount=costAmount;
	}

	public String getCostAmount(){
		return this.costAmount;
	}

	public void setOrgCode(String orgCode){
		this.orgCode=orgCode;
	}

	public String getOrgCode(){
		return this.orgCode;
	}

	public void setDealerCode(String dealerCode){
		this.dealerCode=dealerCode;
	}

	public String getDealerCode(){
		return this.dealerCode;
	}

	public void setRowNumber(String rowNumber){
		this.rowNumber=rowNumber;
	}

	public String getRowNumber(){
		return this.rowNumber;
	}

	public void setUserId(String userId){
		this.userId=userId;
	}

	public String getUserId(){
		return this.userId;
	}

	public void setCostSource(String costSource){
		this.costSource=costSource;
	}

	public String getCostSource(){
		return this.costSource;
	}

}