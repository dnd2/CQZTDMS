/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-12-16 17:06:01
* CreateBy   : yuewei
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.yxdms.entity.activity;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrActivityTempletLabPO extends PO{

	private Double labourAmount;
	private Double labourPrice;
	private String labourName;
	private Long id;
	private Long templetId;
	private String mainPartCode;
	private Double labourQuotiety;
	private String labourCode;

	public void setLabourAmount(Double labourAmount){
		this.labourAmount=labourAmount;
	}

	public Double getLabourAmount(){
		return this.labourAmount;
	}

	public void setLabourPrice(Double labourPrice){
		this.labourPrice=labourPrice;
	}

	public Double getLabourPrice(){
		return this.labourPrice;
	}

	public void setLabourName(String labourName){
		this.labourName=labourName;
	}

	public String getLabourName(){
		return this.labourName;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setTempletId(Long templetId){
		this.templetId=templetId;
	}

	public Long getTempletId(){
		return this.templetId;
	}
	
	public void setLabourQuotiety(Double labourQuotiety){
		this.labourQuotiety=labourQuotiety;
	}

	public Double getLabourQuotiety(){
		return this.labourQuotiety;
	}

	public void setLabourCode(String labourCode){
		this.labourCode=labourCode;
	}

	public String getLabourCode(){
		return this.labourCode;
	}

	public String getMainPartCode() {
		return mainPartCode;
	}

	public void setMainPartCode(String mainPartCode) {
		this.mainPartCode = mainPartCode;
	}

}