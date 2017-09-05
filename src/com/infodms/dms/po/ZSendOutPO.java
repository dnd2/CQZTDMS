/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-02-10 10:11:36
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class ZSendOutPO extends PO{

	private String invoicercode;
	private String vin;
	private String dealercode;
	private String deliverydate;
	private String status;
	private String deliveryaddress;
	private String materialcode;
	private String dealertype;
	private String transporttype;
	private Long id;
	private String time1;
	private String time2;
	private String deliverytype;
	private String time3;
	private String dmssaleno;

	public void setInvoicercode(String invoicercode){
		this.invoicercode=invoicercode;
	}

	public String getInvoicercode(){
		return this.invoicercode;
	}

	public void setVin(String vin){
		this.vin=vin;
	}

	public String getVin(){
		return this.vin;
	}

	public void setDealercode(String dealercode){
		this.dealercode=dealercode;
	}

	public String getDealercode(){
		return this.dealercode;
	}

	public void setDeliverydate(String deliverydate){
		this.deliverydate=deliverydate;
	}

	public String getDeliverydate(){
		return this.deliverydate;
	}

	public void setStatus(String status){
		this.status=status;
	}

	public String getStatus(){
		return this.status;
	}

	public void setDeliveryaddress(String deliveryaddress){
		this.deliveryaddress=deliveryaddress;
	}

	public String getDeliveryaddress(){
		return this.deliveryaddress;
	}

	public void setMaterialcode(String materialcode){
		this.materialcode=materialcode;
	}

	public String getMaterialcode(){
		return this.materialcode;
	}

	public void setDealertype(String dealertype){
		this.dealertype=dealertype;
	}

	public String getDealertype(){
		return this.dealertype;
	}

	public void setTransporttype(String transporttype){
		this.transporttype=transporttype;
	}

	public String getTransporttype(){
		return this.transporttype;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setTime1(String time1){
		this.time1=time1;
	}

	public String getTime1(){
		return this.time1;
	}

	public void setTime2(String time2){
		this.time2=time2;
	}

	public String getTime2(){
		return this.time2;
	}

	public void setDeliverytype(String deliverytype){
		this.deliverytype=deliverytype;
	}

	public String getDeliverytype(){
		return this.deliverytype;
	}

	public void setTime3(String time3){
		this.time3=time3;
	}

	public String getTime3(){
		return this.time3;
	}

	public void setDmssaleno(String dmssaleno){
		this.dmssaleno=dmssaleno;
	}

	public String getDmssaleno(){
		return this.dmssaleno;
	}

}