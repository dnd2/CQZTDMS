/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-02-27 17:36:18
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtFleetSupportInfoPO extends PO{

	private Long intentSeries;
	private Double requestSupport;
	private Double giveAndAccept;
	private Long createBy;
	private Double marketDevelop;
	private Date createDate;
	private Double realPrice;
	private Double auditMoney;
	private Long amount;
	private Double profit;
	private Long supportInfoId;
	private Double price;
	private Double depotProPrice;
	private Double realProfit;
	private Long fleetId;

	public void setIntentSeries(Long intentSeries){
		this.intentSeries=intentSeries;
	}

	public Long getIntentSeries(){
		return this.intentSeries;
	}

	public void setRequestSupport(Double requestSupport){
		this.requestSupport=requestSupport;
	}

	public Double getRequestSupport(){
		return this.requestSupport;
	}

	public void setGiveAndAccept(Double giveAndAccept){
		this.giveAndAccept=giveAndAccept;
	}

	public Double getGiveAndAccept(){
		return this.giveAndAccept;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setMarketDevelop(Double marketDevelop){
		this.marketDevelop=marketDevelop;
	}

	public Double getMarketDevelop(){
		return this.marketDevelop;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setRealPrice(Double realPrice){
		this.realPrice=realPrice;
	}

	public Double getRealPrice(){
		return this.realPrice;
	}

	public void setAuditMoney(Double auditMoney){
		this.auditMoney=auditMoney;
	}

	public Double getAuditMoney(){
		return this.auditMoney;
	}

	public void setAmount(Long amount){
		this.amount=amount;
	}

	public Long getAmount(){
		return this.amount;
	}

	public void setProfit(Double profit){
		this.profit=profit;
	}

	public Double getProfit(){
		return this.profit;
	}

	public void setSupportInfoId(Long supportInfoId){
		this.supportInfoId=supportInfoId;
	}

	public Long getSupportInfoId(){
		return this.supportInfoId;
	}

	public void setPrice(Double price){
		this.price=price;
	}

	public Double getPrice(){
		return this.price;
	}

	public void setDepotProPrice(Double depotProPrice){
		this.depotProPrice=depotProPrice;
	}

	public Double getDepotProPrice(){
		return this.depotProPrice;
	}

	public void setRealProfit(Double realProfit){
		this.realProfit=realProfit;
	}

	public Double getRealProfit(){
		return this.realProfit;
	}

	public void setFleetId(Long fleetId){
		this.fleetId=fleetId;
	}

	public Long getFleetId(){
		return this.fleetId;
	}

}