/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2017-07-27 10:24:52
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.yxdms.entity.special;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsSpecialAmountRangePO extends PO{

	private String name;
	private Long id;
	private Integer queryStatus;
	private Integer auditStatus;
	private Long positionId;
	private Integer amountOffline;
	private Integer amountCeil;

	public void setName(String name){
		this.name=name;
	}

	public String getName(){
		return this.name;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setQueryStatus(Integer queryStatus){
		this.queryStatus=queryStatus;
	}

	public Integer getQueryStatus(){
		return this.queryStatus;
	}

	public void setAuditStatus(Integer auditStatus){
		this.auditStatus=auditStatus;
	}

	public Integer getAuditStatus(){
		return this.auditStatus;
	}

	public void setPositionId(Long positionId){
		this.positionId=positionId;
	}

	public Long getPositionId(){
		return this.positionId;
	}

	public void setAmountOffline(Integer amountOffline){
		this.amountOffline=amountOffline;
	}

	public Integer getAmountOffline(){
		return this.amountOffline;
	}

	public void setAmountCeil(Integer amountCeil){
		this.amountCeil=amountCeil;
	}

	public Integer getAmountCeil(){
		return this.amountCeil;
	}

}