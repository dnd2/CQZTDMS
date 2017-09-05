/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-09-30 19:46:02
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TipVsDlvryPO extends PO{

	private String erpOrderId;
	private String sendCode;
	private Long sendId;
	private String sendOrg;
	private String status;
	private String vehicleNo;
	private String sendDate;
	private String sender;
	private String isErr;
	private String errMessage;
	private String senderTel;

	public void setErpOrderId(String erpOrderId){
		this.erpOrderId=erpOrderId;
	}

	public String getErpOrderId(){
		return this.erpOrderId;
	}

	public void setSendCode(String sendCode){
		this.sendCode=sendCode;
	}

	public String getSendCode(){
		return this.sendCode;
	}

	public void setSendId(Long sendId){
		this.sendId=sendId;
	}

	public Long getSendId(){
		return this.sendId;
	}

	public void setSendOrg(String sendOrg){
		this.sendOrg=sendOrg;
	}

	public String getSendOrg(){
		return this.sendOrg;
	}

	public void setStatus(String status){
		this.status=status;
	}

	public String getStatus(){
		return this.status;
	}

	public void setVehicleNo(String vehicleNo){
		this.vehicleNo=vehicleNo;
	}

	public String getVehicleNo(){
		return this.vehicleNo;
	}

	public void setSendDate(String sendDate){
		this.sendDate=sendDate;
	}

	public String getSendDate(){
		return this.sendDate;
	}

	public void setSender(String sender){
		this.sender=sender;
	}

	public String getSender(){
		return this.sender;
	}

	public void setIsErr(String isErr){
		this.isErr=isErr;
	}

	public String getIsErr(){
		return this.isErr;
	}

	public void setErrMessage(String errMessage){
		this.errMessage=errMessage;
	}

	public String getErrMessage(){
		return this.errMessage;
	}

	public void setSenderTel(String senderTel){
		this.senderTel=senderTel;
	}

	public String getSenderTel(){
		return this.senderTel;
	}

}