/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-07-11 19:28:23
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsAplctionDeductionPO extends PO{

	private Double countSum;
	private Long aplId;
	private Long id;
	private Long codeType;

	public void setCountSum(Double countSum){
		this.countSum=countSum;
	}

	public Double getCountSum(){
		return this.countSum;
	}

	public void setAplId(Long aplId){
		this.aplId=aplId;
	}

	public Long getAplId(){
		return this.aplId;
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

}