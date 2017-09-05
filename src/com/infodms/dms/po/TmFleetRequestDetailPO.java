/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-02-04 16:00:49
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmFleetRequestDetailPO extends PO{

	private Long amount;
	private String discribe;
	private Long materId;
	private Long detailId;
	private Long fleetId;

	public void setAmount(Long amount){
		this.amount=amount;
	}

	public Long getAmount(){
		return this.amount;
	}

	public void setDiscribe(String discribe){
		this.discribe=discribe;
	}

	public String getDiscribe(){
		return this.discribe;
	}

	public void setMaterId(Long materId){
		this.materId=materId;
	}

	public Long getMaterId(){
		return this.materId;
	}

	public void setDetailId(Long detailId){
		this.detailId=detailId;
	}

	public Long getDetailId(){
		return this.detailId;
	}

	public void setFleetId(Long fleetId){
		this.fleetId=fleetId;
	}

	public Long getFleetId(){
		return this.fleetId;
	}

}