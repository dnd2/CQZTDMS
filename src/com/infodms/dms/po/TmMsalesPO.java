/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2009-12-23 17:05:08
* CreateBy   : xwj
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmMsalesPO extends PO{

	private Long invoiceId;
	private String invoiceNo;
	private Long orderId;
	private Date saledate;
	private Integer qty;
	private String itemcode;
	private String orderNo;
	private Double money;

	public void setInvoiceId(Long invoiceId){
		this.invoiceId=invoiceId;
	}

	public Long getInvoiceId(){
		return this.invoiceId;
	}

	public void setInvoiceNo(String invoiceNo){
		this.invoiceNo=invoiceNo;
	}

	public String getInvoiceNo(){
		return this.invoiceNo;
	}

	public void setOrderId(Long orderId){
		this.orderId=orderId;
	}

	public Long getOrderId(){
		return this.orderId;
	}

	public void setSaledate(Date saledate){
		this.saledate=saledate;
	}

	public Date getSaledate(){
		return this.saledate;
	}

	public void setQty(Integer qty){
		this.qty=qty;
	}

	public Integer getQty(){
		return this.qty;
	}

	public void setItemcode(String itemcode){
		this.itemcode=itemcode;
	}

	public String getItemcode(){
		return this.itemcode;
	}

	public void setOrderNo(String orderNo){
		this.orderNo=orderNo;
	}

	public String getOrderNo(){
		return this.orderNo;
	}

	public void setMoney(Double money){
		this.money=money;
	}

	public Double getMoney(){
		return this.money;
	}

}