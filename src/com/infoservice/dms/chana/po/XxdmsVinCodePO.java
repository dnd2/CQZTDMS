/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-11-18 14:22:45
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infoservice.dms.chana.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class XxdmsVinCodePO extends PO{

	private String vinCode;
	private Date hgDate;
	private String engineCode;
	private String itemCode;
	private Integer organizationId;
	private String hegezhengCode; //YH 2011.10.25

	public void setVinCode(String vinCode){
		this.vinCode=vinCode;
	}

	public String getVinCode(){
		return this.vinCode;
	}

	public void setHgDate(Date hgDate){
		this.hgDate=hgDate;
	}

	public Date getHgDate(){
		return this.hgDate;
	}

	public void setEngineCode(String engineCode){
		this.engineCode=engineCode;
	}

	public String getEngineCode(){
		return this.engineCode;
	}

	public void setItemCode(String itemCode){
		this.itemCode=itemCode;
	}

	public String getItemCode(){
		return this.itemCode;
	}

	public void setOrganizationId(Integer organizationId){
		this.organizationId=organizationId;
	}

	public Integer getOrganizationId(){
		return this.organizationId;
	}

	public String getHegezhengCode() {
		return hegezhengCode;
	}

	public void setHegezhengCode(String hegezhengCode) {
		this.hegezhengCode = hegezhengCode;
	}

}