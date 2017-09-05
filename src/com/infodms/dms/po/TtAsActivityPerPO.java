/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-10-16 19:44:20
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsActivityPerPO extends PO{

	private Long dealerId;
	private Long perCount;
	private String partName;
	private Long id;
	private Long codeType;
	private Long summaryId;

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setPerCount(Long perCount){
		this.perCount=perCount;
	}

	public Long getPerCount(){
		return this.perCount;
	}

	public void setPartName(String partName){
		this.partName=partName;
	}

	public String getPartName(){
		return this.partName;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setCodeType(Long codeType){
		this.codeType=codeType;
	}

	public Long getCodeType(){
		return this.codeType;
	}

	public void setSummaryId(Long summaryId){
		this.summaryId=summaryId;
	}

	public Long getSummaryId(){
		return this.summaryId;
	}

}