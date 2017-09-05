/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-05-06 16:02:56
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAccessoryDtlPO extends PO{

	private Double price;
	private String roNo;
	private String mainPartCode;
	private Long id;
	private String workhourCode;
	private String workhourName;

	public void setPrice(Double price){
		this.price=price;
	}

	public Double getPrice(){
		return this.price;
	}

	public void setRoNo(String roNo){
		this.roNo=roNo;
	}

	public String getRoNo(){
		return this.roNo;
	}

	public void setMainPartCode(String mainPartCode){
		this.mainPartCode=mainPartCode;
	}

	public String getMainPartCode(){
		return this.mainPartCode;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setWorkhourCode(String workhourCode){
		this.workhourCode=workhourCode;
	}

	public String getWorkhourCode(){
		return this.workhourCode;
	}

	public void setWorkhourName(String workhourName){
		this.workhourName=workhourName;
	}

	public String getWorkhourName(){
		return this.workhourName;
	}

}