/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-09-14 20:07:14
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsAccountPO extends PO{

	private Long companyId;
	private Long accountId;
	private Date updateDate;
	private Long createBy;
	private Long accountTypeId;
	private String accountName;
	private Double availableAmount;
	private Date createDate;
	private Long dealerId;
	private Long updateBy;
	private Double balanceAmount;
	private String accountCode;
	private Double freezeAmount;

	public void setCompanyId(Long companyId){
		this.companyId=companyId;
	}

	public Long getCompanyId(){
		return this.companyId;
	}

	public void setAccountId(Long accountId){
		this.accountId=accountId;
	}

	public Long getAccountId(){
		return this.accountId;
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

	public void setAccountTypeId(Long accountTypeId){
		this.accountTypeId=accountTypeId;
	}

	public Long getAccountTypeId(){
		return this.accountTypeId;
	}

	public void setAccountName(String accountName){
		this.accountName=accountName;
	}

	public String getAccountName(){
		return this.accountName;
	}

	public void setAvailableAmount(Double availableAmount){
		this.availableAmount=availableAmount;
	}

	public Double getAvailableAmount(){
		return this.availableAmount;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
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

	public void setBalanceAmount(Double balanceAmount){
		this.balanceAmount=balanceAmount;
	}

	public Double getBalanceAmount(){
		return this.balanceAmount;
	}

	public void setAccountCode(String accountCode){
		this.accountCode=accountCode;
	}

	public String getAccountCode(){
		return this.accountCode;
	}

	public void setFreezeAmount(Double freezeAmount){
		this.freezeAmount=freezeAmount;
	}

	public Double getFreezeAmount(){
		return this.freezeAmount;
	}

}