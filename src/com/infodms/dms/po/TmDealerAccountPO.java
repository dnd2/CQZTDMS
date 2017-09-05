/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2009-12-30 16:34:32
* CreateBy   : Andy
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmDealerAccountPO extends PO{

	private String accountBank;
	private Long accountId;
	private String accountNo;
	private Date updateDate;
	private String invHead;
	private Long updateBy;
	private Long createBy;
	private Date createDate;
	private String invHeadContent;
	private Long dealerId;

	public void setAccountBank(String accountBank){
		this.accountBank=accountBank;
	}

	public String getAccountBank(){
		return this.accountBank;
	}

	public void setAccountId(Long accountId){
		this.accountId=accountId;
	}

	public Long getAccountId(){
		return this.accountId;
	}

	public void setAccountNo(String accountNo){
		this.accountNo=accountNo;
	}

	public String getAccountNo(){
		return this.accountNo;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setInvHead(String invHead){
		this.invHead=invHead;
	}

	public String getInvHead(){
		return this.invHead;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
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

	public void setInvHeadContent(String invHeadContent){
		this.invHeadContent=invHeadContent;
	}

	public String getInvHeadContent(){
		return this.invHeadContent;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

}