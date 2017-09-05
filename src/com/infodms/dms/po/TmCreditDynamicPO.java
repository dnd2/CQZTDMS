/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-01-21 11:34:13
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmCreditDynamicPO extends PO{

	private String year;
	private Integer carStyle;
	private String month;
	private String wmflag;
	private Long modeType;
	private Double backMoney;
	private Double confirmMoney;
	private Double useMoney;
	private Long dynamicId;
	private Double transMoney;
	private Double totalReturn;

	public void setYear(String year){
		this.year=year;
	}

	public String getYear(){
		return this.year;
	}

	public void setCarStyle(Integer carStyle){
		this.carStyle=carStyle;
	}

	public Integer getCarStyle(){
		return this.carStyle;
	}

	public void setMonth(String month){
		this.month=month;
	}

	public String getMonth(){
		return this.month;
	}

	public void setWmflag(String wmflag){
		this.wmflag=wmflag;
	}

	public String getWmflag(){
		return this.wmflag;
	}

	public void setModeType(Long modeType){
		this.modeType=modeType;
	}

	public Long getModeType(){
		return this.modeType;
	}

	public void setBackMoney(Double backMoney){
		this.backMoney=backMoney;
	}

	public Double getBackMoney(){
		return this.backMoney;
	}

	public void setConfirmMoney(Double confirmMoney){
		this.confirmMoney=confirmMoney;
	}

	public Double getConfirmMoney(){
		return this.confirmMoney;
	}

	public void setUseMoney(Double useMoney){
		this.useMoney=useMoney;
	}

	public Double getUseMoney(){
		return this.useMoney;
	}

	public void setDynamicId(Long dynamicId){
		this.dynamicId=dynamicId;
	}

	public Long getDynamicId(){
		return this.dynamicId;
	}

	public void setTransMoney(Double transMoney){
		this.transMoney=transMoney;
	}

	public Double getTransMoney(){
		return this.transMoney;
	}

	public void setTotalReturn(Double totalReturn){
		this.totalReturn=totalReturn;
	}

	public Double getTotalReturn(){
		return this.totalReturn;
	}

}