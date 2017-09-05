/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-10-28 13:53:57
* CreateBy   : wizard_lee
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsDlrStorageChglogPO extends PO{

	private Long toDealer;
	private Long toAddressId;
	private String toAddress;
	private String fromAddress;
	private Long fromAddressId;
	private Long changeId;
	private Long createBy;
	private Date createDate;
	private Long fromDealer;

	public void setToDealer(Long toDealer){
		this.toDealer=toDealer;
	}

	public Long getToDealer(){
		return this.toDealer;
	}

	public void setToAddressId(Long toAddressId){
		this.toAddressId=toAddressId;
	}

	public Long getToAddressId(){
		return this.toAddressId;
	}

	public void setToAddress(String toAddress){
		this.toAddress=toAddress;
	}

	public String getToAddress(){
		return this.toAddress;
	}

	public void setFromAddress(String fromAddress){
		this.fromAddress=fromAddress;
	}

	public String getFromAddress(){
		return this.fromAddress;
	}

	public void setFromAddressId(Long fromAddressId){
		this.fromAddressId=fromAddressId;
	}

	public Long getFromAddressId(){
		return this.fromAddressId;
	}

	public void setChangeId(Long changeId){
		this.changeId=changeId;
	}

	public Long getChangeId(){
		return this.changeId;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setFromDealer(Long fromDealer){
		this.fromDealer=fromDealer;
	}

	public Long getFromDealer(){
		return this.fromDealer;
	}

}