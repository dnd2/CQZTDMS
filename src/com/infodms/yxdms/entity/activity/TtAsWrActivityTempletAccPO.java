/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-12-16 17:06:32
* CreateBy   : yuewei
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.yxdms.entity.activity;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrActivityTempletAccPO extends PO{

	private String accCode;
	private Double accPrice;
	private Long id;
	private String accName;
	private Long templetId;
	private String mainPartCode;

	public void setAccCode(String accCode){
		this.accCode=accCode;
	}

	public String getAccCode(){
		return this.accCode;
	}

	public void setAccPrice(Double accPrice){
		this.accPrice=accPrice;
	}

	public Double getAccPrice(){
		return this.accPrice;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setAccName(String accName){
		this.accName=accName;
	}

	public String getAccName(){
		return this.accName;
	}

	public void setTempletId(Long templetId){
		this.templetId=templetId;
	}

	public Long getTempletId(){
		return this.templetId;
	}

	public String getMainPartCode() {
		return mainPartCode;
	}

	public void setMainPartCode(String mainPartCode) {
		this.mainPartCode = mainPartCode;
	}

}