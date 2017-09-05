/*
 * Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
 * This software is published under the terms of the Infoservice Software
 * License version 1.0, a copy of which has been included with this
 * distribution in the LICENSE.txt file.
 *
 * CreateDate : 2008-07-03 18:26:09
 * CreateBy   : kevin
 * Comment    : generate by com.infoservice.po.POGen
 */

package com.infoservice.dms.chana.vo;

import java.util.Date;


@SuppressWarnings("serial")
public class CustomerComplaintDetailVO extends BaseVO {

	private Date dealDate; //下端：处理时间  TIMESTAMP  上端：AUDIT_DATE DATE 
	private String dealer; //下端：处理人  VARCHAR(30)  上端：CREATE_BY NUMBER(16) 
	private String dealResult; //下端：处理结果  VARCHAR(900)  上端：AUDIT_CONTENT VARCHAR2(600) 
	
	public Date getDealDate() {
		return dealDate;
	}
	public void setDealDate(Date dealDate) {
		this.dealDate = dealDate;
	}
	public String getDealer() {
		return dealer;
	}
	public void setDealer(String dealer) {
		this.dealer = dealer;
	}
	public String getDealResult() {
		return dealResult;
	}
	public void setDealResult(String dealResult) {
		this.dealResult = dealResult;
	}

	

}