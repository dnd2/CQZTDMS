/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-03-15 09:49:33
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.math.BigDecimal;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmMaterialPricePO extends PO{

	private BigDecimal partyNumber;
	private String customerClassCode;
	private BigDecimal operand;
	private BigDecimal listHeaderId;
	private String categories;
	private String partyName;

	public void setPartyNumber(BigDecimal partyNumber){
		this.partyNumber=partyNumber;
	}

	public BigDecimal getPartyNumber(){
		return this.partyNumber;
	}

	public void setCustomerClassCode(String customerClassCode){
		this.customerClassCode=customerClassCode;
	}

	public String getCustomerClassCode(){
		return this.customerClassCode;
	}

	public void setOperand(BigDecimal operand){
		this.operand=operand;
	}

	public BigDecimal getOperand(){
		return this.operand;
	}

	public void setListHeaderId(BigDecimal listHeaderId){
		this.listHeaderId=listHeaderId;
	}

	public BigDecimal getListHeaderId(){
		return this.listHeaderId;
	}

	public void setCategories(String categories){
		this.categories=categories;
	}

	public String getCategories(){
		return this.categories;
	}

	public void setPartyName(String partyName){
		this.partyName=partyName;
	}

	public String getPartyName(){
		return this.partyName;
	}

}