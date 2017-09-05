/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2009-12-17 11:18:27
* CreateBy   : xwj
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.eai.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TiSAreaProvincePO extends PO{

	private String provinceCode;
	private String areaName;
	private String areaCode;
	private String provinceName;

	public void setProvinceCode(String provinceCode){
		this.provinceCode=provinceCode;
	}

	public String getProvinceCode(){
		return this.provinceCode;
	}

	public void setAreaName(String areaName){
		this.areaName=areaName;
	}

	public String getAreaName(){
		return this.areaName;
	}

	public void setAreaCode(String areaCode){
		this.areaCode=areaCode;
	}

	public String getAreaCode(){
		return this.areaCode;
	}

	public void setProvinceName(String provinceName){
		this.provinceName=provinceName;
	}

	public String getProvinceName(){
		return this.provinceName;
	}

}