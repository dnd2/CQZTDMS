/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-09-30 15:49:01
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmpFundMappingPO extends PO{

	private String dmsFundTypeId;
	private String erpFundType;
	private String dmsFundType;

	public void setDmsFundTypeId(String dmsFundTypeId){
		this.dmsFundTypeId=dmsFundTypeId;
	}

	public String getDmsFundTypeId(){
		return this.dmsFundTypeId;
	}

	public void setErpFundType(String erpFundType){
		this.erpFundType=erpFundType;
	}

	public String getErpFundType(){
		return this.erpFundType;
	}

	public void setDmsFundType(String dmsFundType){
		this.dmsFundType=dmsFundType;
	}

	public String getDmsFundType(){
		return this.dmsFundType;
	}

}