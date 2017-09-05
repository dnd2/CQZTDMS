/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-01-22 10:44:36
* CreateBy   : HXY
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsOrderPaymentPriceLogPO extends PO{

	private Long logId;
	private Long updateBy;
	private Integer paymentYear;
	private Date updateDate;
	private String remark;
	private Integer paymentMonth;
	private Double paymentPrice;

	public void setLogId(Long logId){
		this.logId=logId;
	}

	public Long getLogId(){
		return this.logId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setPaymentYear(Integer paymentYear){
		this.paymentYear=paymentYear;
	}

	public Integer getPaymentYear(){
		return this.paymentYear;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setPaymentMonth(Integer paymentMonth){
		this.paymentMonth=paymentMonth;
	}

	public Integer getPaymentMonth(){
		return this.paymentMonth;
	}

	public void setPaymentPrice(Double paymentPrice){
		this.paymentPrice=paymentPrice;
	}

	public Double getPaymentPrice(){
		return this.paymentPrice;
	}

}