/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-04-09 10:50:21
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.math.BigDecimal;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class CuxErpReturnVinPO extends PO{

	private String vin;
	private String itemCode;
	private BigDecimal seqId;
	private String orderNumber;

	public void setVin(String vin){
		this.vin=vin;
	}

	public String getVin(){
		return this.vin;
	}

	public void setItemCode(String itemCode){
		this.itemCode=itemCode;
	}

	public String getItemCode(){
		return this.itemCode;
	}

	public void setSeqId(BigDecimal seqId){
		this.seqId=seqId;
	}

	public BigDecimal getSeqId(){
		return this.seqId;
	}

	public void setOrderNumber(String orderNumber){
		this.orderNumber=orderNumber;
	}

	public String getOrderNumber(){
		return this.orderNumber;
	}

}