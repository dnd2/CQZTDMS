/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-10-15 14:05:40
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsActivityTypePO extends PO{

	private Long countSum;
	private Long dealerId;
	private Double monney;
	private Long id;
	private Long codeType;
	private Integer actType;
	private Long summaryId;
	private Integer status;

	public void setCountSum(Long countSum){
		this.countSum=countSum;
	}

	public Long getCountSum(){
		return this.countSum;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setMonney(Double monney){
		this.monney=monney;
	}

	public Double getMonney(){
		return this.monney;
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

	public void setActType(Integer actType){
		this.actType=actType;
	}

	public Integer getActType(){
		return this.actType;
	}

	public void setSummaryId(Long summaryId){
		this.summaryId=summaryId;
	}

	public Long getSummaryId(){
		return this.summaryId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}