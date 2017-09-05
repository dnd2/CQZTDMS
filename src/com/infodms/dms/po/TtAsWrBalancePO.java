/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-07-23 13:23:58
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrBalancePO extends PO{

	private Date balanceDate;
	private String procFactory;
	private Integer reductionFlag;
	private Date updateDate;
	private String invoiceNo;
	private Long createBy;
	private Date createDate;
	private String balanceNo;
	private Long dealerId;
	private Long updateBy;
	private Long oemCompanyId;
	private Integer invoiceNoFlag;
	private Long id;
	private Double balance;

	public void setBalanceDate(Date balanceDate){
		this.balanceDate=balanceDate;
	}

	public Date getBalanceDate(){
		return this.balanceDate;
	}

	public void setProcFactory(String procFactory){
		this.procFactory=procFactory;
	}

	public String getProcFactory(){
		return this.procFactory;
	}

	public void setReductionFlag(Integer reductionFlag){
		this.reductionFlag=reductionFlag;
	}

	public Integer getReductionFlag(){
		return this.reductionFlag;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setInvoiceNo(String invoiceNo){
		this.invoiceNo=invoiceNo;
	}

	public String getInvoiceNo(){
		return this.invoiceNo;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setBalanceNo(String balanceNo){
		this.balanceNo=balanceNo;
	}

	public String getBalanceNo(){
		return this.balanceNo;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setOemCompanyId(Long oemCompanyId){
		this.oemCompanyId=oemCompanyId;
	}

	public Long getOemCompanyId(){
		return this.oemCompanyId;
	}

	public void setInvoiceNoFlag(Integer invoiceNoFlag){
		this.invoiceNoFlag=invoiceNoFlag;
	}

	public Integer getInvoiceNoFlag(){
		return this.invoiceNoFlag;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setBalance(Double balance){
		this.balance=balance;
	}

	public Double getBalance(){
		return this.balance;
	}

}