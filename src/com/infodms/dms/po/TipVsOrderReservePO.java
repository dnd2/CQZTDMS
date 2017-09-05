/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-09-30 20:36:37
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TipVsOrderReservePO extends PO{

	private Integer amount;
	private String wareHouse;
	private String materialCode;
	private String batchNo;
	private String wareHouseCode;
	private Long headId;
	private String isErr;
	private String errMessage;
	private Long lineId;

	public void setAmount(Integer amount){
		this.amount=amount;
	}

	public Integer getAmount(){
		return this.amount;
	}

	public void setWareHouse(String wareHouse){
		this.wareHouse=wareHouse;
	}

	public String getWareHouse(){
		return this.wareHouse;
	}

	public void setMaterialCode(String materialCode){
		this.materialCode=materialCode;
	}

	public String getMaterialCode(){
		return this.materialCode;
	}

	public void setBatchNo(String batchNo){
		this.batchNo=batchNo;
	}

	public String getBatchNo(){
		return this.batchNo;
	}

	public void setWareHouseCode(String wareHouseCode){
		this.wareHouseCode=wareHouseCode;
	}

	public String getWareHouseCode(){
		return this.wareHouseCode;
	}

	public void setHeadId(Long headId){
		this.headId=headId;
	}

	public Long getHeadId(){
		return this.headId;
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

	public void setLineId(Long lineId){
		this.lineId=lineId;
	}

	public Long getLineId(){
		return this.lineId;
	}

}