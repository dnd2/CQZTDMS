/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-05-27 17:47:24
* CreateBy   : 
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtSaAccountPO extends PO{

	private Double availableAmount;
	private Double freezeAmount;
	private Long dealerId;
	private Long updateBy;
	private Long accountId;
	private Date updateDate;
	private String accountName;
	private Long createBy;
	private Long accountTypeId;
	private Date createDate;
	private String accountCode;
	private Double balanceAmount;

	public void setAvailableAmount(Double availableAmount){
		this.availableAmount=availableAmount;
	}

	public Double getAvailableAmount(){
		return this.availableAmount;
	}

	public void setFreezeAmount(Double freezeAmount){
		this.freezeAmount=freezeAmount;
	}

	public Double getFreezeAmount(){
		return this.freezeAmount;
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

	public void setAccountName(String accountName){
		this.accountName=accountName;
	}

	public String getAccountName(){
		return this.accountName;
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

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setAccountCode(String accountCode){
		this.accountCode=accountCode;
	}

	public String getAccountCode(){
		return this.accountCode;
	}

	public void setBalanceAmount(Double balanceAmount){
		this.balanceAmount=balanceAmount;
	}

	public Double getBalanceAmount(){
		return this.balanceAmount;
	}

}