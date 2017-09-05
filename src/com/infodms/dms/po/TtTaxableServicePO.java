/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-09-24 13:26:53
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtTaxableServicePO extends PO{

	private Long dlrCompanyId;
	private Long balanceId;
	private Long purchaserId;
	private String purchaserName;
	private String invoiceNo;
	private Long taxableServiceId;
	private Date updateDate;
	private Long createBy;
	private String balanceNo;
	private String salesName;
	private String no;
	private Date claimStartDate;
	private Date claimEndDate;
	private Long oemCompanyId;
	private Long updateBy;
	private Date statisticsDate;
	private Long salesId;
	private Double taxRate;
	private Date createDate;
	private Long dlrId;

	public void setDlrCompanyId(Long dlrCompanyId){
		this.dlrCompanyId=dlrCompanyId;
	}

	public Long getDlrCompanyId(){
		return this.dlrCompanyId;
	}

	public void setBalanceId(Long balanceId){
		this.balanceId=balanceId;
	}

	public Long getBalanceId(){
		return this.balanceId;
	}

	public void setPurchaserId(Long purchaserId){
		this.purchaserId=purchaserId;
	}

	public Long getPurchaserId(){
		return this.purchaserId;
	}

	public void setPurchaserName(String purchaserName){
		this.purchaserName=purchaserName;
	}

	public String getPurchaserName(){
		return this.purchaserName;
	}

	public void setInvoiceNo(String invoiceNo){
		this.invoiceNo=invoiceNo;
	}

	public String getInvoiceNo(){
		return this.invoiceNo;
	}

	public void setTaxableServiceId(Long taxableServiceId){
		this.taxableServiceId=taxableServiceId;
	}

	public Long getTaxableServiceId(){
		return this.taxableServiceId;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setBalanceNo(String balanceNo){
		this.balanceNo=balanceNo;
	}

	public String getBalanceNo(){
		return this.balanceNo;
	}

	public void setSalesName(String salesName){
		this.salesName=salesName;
	}

	public String getSalesName(){
		return this.salesName;
	}

	public void setNo(String no){
		this.no=no;
	}

	public String getNo(){
		return this.no;
	}

	public void setClaimStartDate(Date claimStartDate){
		this.claimStartDate=claimStartDate;
	}

	public Date getClaimStartDate(){
		return this.claimStartDate;
	}

	public void setClaimEndDate(Date claimEndDate){
		this.claimEndDate=claimEndDate;
	}

	public Date getClaimEndDate(){
		return this.claimEndDate;
	}

	public void setOemCompanyId(Long oemCompanyId){
		this.oemCompanyId=oemCompanyId;
	}

	public Long getOemCompanyId(){
		return this.oemCompanyId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setStatisticsDate(Date statisticsDate){
		this.statisticsDate=statisticsDate;
	}

	public Date getStatisticsDate(){
		return this.statisticsDate;
	}

	public void setSalesId(Long salesId){
		this.salesId=salesId;
	}

	public Long getSalesId(){
		return this.salesId;
	}

	public void setTaxRate(Double taxRate){
		this.taxRate=taxRate;
	}

	public Double getTaxRate(){
		return this.taxRate;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setDlrId(Long dlrId){
		this.dlrId=dlrId;
	}

	public Long getDlrId(){
		return this.dlrId;
	}

}