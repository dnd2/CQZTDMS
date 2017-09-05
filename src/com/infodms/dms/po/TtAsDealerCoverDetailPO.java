/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-09-09 11:16:32
* CreateBy   : KFQ
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsDealerCoverDetailPO extends PO{

	private Long printBy;
	private Date starDate;
	private Date printDate;
	private Long dealerId;
	private Double labourPrice;
	private Double partPrice;
	private Date endDate;
	private Long id;
	private Integer printTimes;
	private Date lastPrintDate;
	private Integer balanceYieldly;

	public Integer getBalanceYieldly() {
		return balanceYieldly;
	}

	public void setBalanceYieldly(Integer balanceYieldly) {
		this.balanceYieldly = balanceYieldly;
	}

	public void setPrintBy(Long printBy){
		this.printBy=printBy;
	}

	public Long getPrintBy(){
		return this.printBy;
	}

	public void setStarDate(Date starDate){
		this.starDate=starDate;
	}

	public Date getStarDate(){
		return this.starDate;
	}

	public void setPrintDate(Date printDate){
		this.printDate=printDate;
	}

	public Date getPrintDate(){
		return this.printDate;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setLabourPrice(Double labourPrice){
		this.labourPrice=labourPrice;
	}

	public Double getLabourPrice(){
		return this.labourPrice;
	}

	public void setPartPrice(Double partPrice){
		this.partPrice=partPrice;
	}

	public Double getPartPrice(){
		return this.partPrice;
	}

	public void setEndDate(Date endDate){
		this.endDate=endDate;
	}

	public Date getEndDate(){
		return this.endDate;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setPrintTimes(Integer printTimes){
		this.printTimes=printTimes;
	}

	public Integer getPrintTimes(){
		return this.printTimes;
	}

	public void setLastPrintDate(Date lastPrintDate){
		this.lastPrintDate=lastPrintDate;
	}

	public Date getLastPrintDate(){
		return this.lastPrintDate;
	}

}