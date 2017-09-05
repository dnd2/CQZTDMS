/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2009-12-24 16:51:27
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class SvoCustomerPO extends PO{

	private String carStyle;
	private String customerid;
	private String area;
	private String customer;

	public void setCarStyle(String carStyle){
		this.carStyle=carStyle;
	}

	public String getCarStyle(){
		return this.carStyle;
	}

	public void setCustomerid(String customerid){
		this.customerid=customerid;
	}

	public String getCustomerid(){
		return this.customerid;
	}

	public void setArea(String area){
		this.area=area;
	}

	public String getArea(){
		return this.area;
	}

	public void setCustomer(String customer){
		this.customer=customer;
	}

	public String getCustomer(){
		return this.customer;
	}

}