/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-09-30 20:35:16
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TipVsDlvryDtlPO extends PO{

	private String materialCode;
	private String status;
	private String engineNo;
	private String series;
	private String vin;
	private Long headId;
	private String isErr;
	private String errMessage;
	private String color;
	private Long lineId;

	public void setMaterialCode(String materialCode){
		this.materialCode=materialCode;
	}

	public String getMaterialCode(){
		return this.materialCode;
	}

	public void setStatus(String status){
		this.status=status;
	}

	public String getStatus(){
		return this.status;
	}

	public void setEngineNo(String engineNo){
		this.engineNo=engineNo;
	}

	public String getEngineNo(){
		return this.engineNo;
	}

	public void setSeries(String series){
		this.series=series;
	}

	public String getSeries(){
		return this.series;
	}

	public void setVin(String vin){
		this.vin=vin;
	}

	public String getVin(){
		return this.vin;
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

	public void setColor(String color){
		this.color=color;
	}

	public String getColor(){
		return this.color;
	}

	public void setLineId(Long lineId){
		this.lineId=lineId;
	}

	public Long getLineId(){
		return this.lineId;
	}

}