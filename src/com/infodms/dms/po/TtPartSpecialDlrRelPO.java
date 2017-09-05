/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2015-04-24 11:04:36
* CreateBy   : Yu
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartSpecialDlrRelPO extends PO{

	private Long relId;
	private Integer stutus;
	private Integer state;
	private Long dealerId;
	private Long describeId;

	public void setRelId(Long relId){
		this.relId=relId;
	}

	public Long getRelId(){
		return this.relId;
	}

	public void setStutus(Integer stutus){
		this.stutus=stutus;
	}

	public Integer getStutus(){
		return this.stutus;
	}

	public void setState(Integer state){
		this.state=state;
	}

	public Integer getState(){
		return this.state;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setDescribeId(Long describeId){
		this.describeId=describeId;
	}

	public Long getDescribeId(){
		return this.describeId;
	}

}