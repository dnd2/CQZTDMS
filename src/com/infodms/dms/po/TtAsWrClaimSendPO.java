/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-07-15 15:01:35
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrClaimSendPO extends PO{

	private Integer balanceYieldly;
	private Long balanceId;
	private Long dealerId;
	private Date startdate;
	private Long secondDealerId;
	private Double amountSum;
	private Double serviceCharge;
	private Double materialCharge;
	private Long yieldly;
	private String secondDealerCode;
	private Date enddate;

	public void setBalanceYieldly(Integer balanceYieldly){
		this.balanceYieldly=balanceYieldly;
	}

	public Integer getBalanceYieldly(){
		return this.balanceYieldly;
	}

	public void setBalanceId(Long balanceId){
		this.balanceId=balanceId;
	}

	public Long getBalanceId(){
		return this.balanceId;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setStartdate(Date startdate){
		this.startdate=startdate;
	}

	public Date getStartdate(){
		return this.startdate;
	}

	public void setSecondDealerId(Long secondDealerId){
		this.secondDealerId=secondDealerId;
	}

	public Long getSecondDealerId(){
		return this.secondDealerId;
	}

	public void setAmountSum(Double amountSum){
		this.amountSum=amountSum;
	}

	public Double getAmountSum(){
		return this.amountSum;
	}

	public void setServiceCharge(Double serviceCharge){
		this.serviceCharge=serviceCharge;
	}

	public Double getServiceCharge(){
		return this.serviceCharge;
	}

	public void setMaterialCharge(Double materialCharge){
		this.materialCharge=materialCharge;
	}

	public Double getMaterialCharge(){
		return this.materialCharge;
	}

	public void setYieldly(Long yieldly){
		this.yieldly=yieldly;
	}

	public Long getYieldly(){
		return this.yieldly;
	}

	public void setSecondDealerCode(String secondDealerCode){
		this.secondDealerCode=secondDealerCode;
	}

	public String getSecondDealerCode(){
		return this.secondDealerCode;
	}

	public void setEnddate(Date enddate){
		this.enddate=enddate;
	}

	public Date getEnddate(){
		return this.enddate;
	}

}