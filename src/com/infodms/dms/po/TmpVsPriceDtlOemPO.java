/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-12-12 16:14:28
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmpVsPriceDtlOemPO extends PO{

	private String groupCode;
	private String salesPrice;
	private String numberNo;
	private Long userId;
	private String noTaxPrice;
	private String priceCode;

	public void setGroupCode(String groupCode){
		this.groupCode=groupCode;
	}

	public String getGroupCode(){
		return this.groupCode;
	}

	public void setSalesPrice(String salesPrice){
		this.salesPrice=salesPrice;
	}

	public String getSalesPrice(){
		return this.salesPrice;
	}

	public void setNumberNo(String numberNo){
		this.numberNo=numberNo;
	}

	public String getNumberNo(){
		return this.numberNo;
	}

	public void setUserId(Long userId){
		this.userId=userId;
	}

	public Long getUserId(){
		return this.userId;
	}

	public void setNoTaxPrice(String noTaxPrice){
		this.noTaxPrice=noTaxPrice;
	}

	public String getNoTaxPrice(){
		return this.noTaxPrice;
	}

	public void setPriceCode(String priceCode){
		this.priceCode=priceCode;
	}

	public String getPriceCode(){
		return this.priceCode;
	}

}