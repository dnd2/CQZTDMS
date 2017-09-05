/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-12-16 18:03:31
* CreateBy   : yuewei
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.yxdms.entity.activity;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrActivityTempletComPO extends PO{

	private Double comPrice;
	private String mainPartCode;
	private Long id;
	private String remark;
	private Long templetId;

	public void setComPrice(Double comPrice){
		this.comPrice=comPrice;
	}

	public Double getComPrice(){
		return this.comPrice;
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

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setTempletId(Long templetId){
		this.templetId=templetId;
	}

	public Long getTempletId(){
		return this.templetId;
	}

}