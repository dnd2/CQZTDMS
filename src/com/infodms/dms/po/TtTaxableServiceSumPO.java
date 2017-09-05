/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-09-28 18:32:53
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtTaxableServiceSumPO extends PO{

	private Long dlrCompanyId;
	private Long taxableServiceSumId;
	private Long purchaserId;
	private String purchaserName;
	private String invoiceNo;
	private Date updateDate;
	private Long createBy;
	private String salesName;
	private String sumParameterNo;
	private Long sumParameterId;
	private Long oemCompanyId;
	private Long updateBy;
	private Date statisticsDate;
	private Long salesId;
	private Double taxRate;
	private Date createDate;
	private Long dlrId;
	private Integer authStatus; //状态 YH 2010.12.16
	private Double amount;   //金额 YH 2010.12.16
	
	private Integer count_num;
	private Integer status;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getCount_num() {
		return count_num;
	}

	public void setCount_num(Integer countNum) {
		count_num = countNum;
	}

	public void setDlrCompanyId(Long dlrCompanyId){
		this.dlrCompanyId=dlrCompanyId;
	}

	public Long getDlrCompanyId(){
		return this.dlrCompanyId;
	}

	public void setTaxableServiceSumId(Long taxableServiceSumId){
		this.taxableServiceSumId=taxableServiceSumId;
	}

	public Long getTaxableServiceSumId(){
		return this.taxableServiceSumId;
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

	public void setSalesName(String salesName){
		this.salesName=salesName;
	}

	public String getSalesName(){
		return this.salesName;
	}

	public void setSumParameterNo(String sumParameterNo){
		this.sumParameterNo=sumParameterNo;
	}

	public String getSumParameterNo(){
		return this.sumParameterNo;
	}

	public void setSumParameterId(Long sumParameterId){
		this.sumParameterId=sumParameterId;
	}

	public Long getSumParameterId(){
		return this.sumParameterId;
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

	public Integer getAuthStatus() {
		return authStatus;
	}

	public void setAuthStatus(Integer authStatus) {
		this.authStatus = authStatus;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

}