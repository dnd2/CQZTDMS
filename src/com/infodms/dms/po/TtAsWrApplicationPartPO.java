/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-07-29 15:58:07
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrApplicationPartPO extends PO{

	private Double partDiscount;
	private Long partId;
	private Long applicationPart;

	public void setPartDiscount(Double partDiscount){
		this.partDiscount=partDiscount;
	}

	public Double getPartDiscount(){
		return this.partDiscount;
	}

	public void setPartId(Long partId){
		this.partId=partId;
	}

	public Long getPartId(){
		return this.partId;
	}

	public void setApplicationPart(Long applicationPart){
		this.applicationPart=applicationPart;
	}

	public Long getApplicationPart(){
		return this.applicationPart;
	}

}