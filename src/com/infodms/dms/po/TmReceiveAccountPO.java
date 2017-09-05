/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-02-27 17:22:13
* CreateBy   : longwenjun
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmReceiveAccountPO extends PO{

	private String receiveName;
	private Long createUesr;
	private String accountNo;
	private Date updateDate;
	private Long status;
	private Long receiveAccountId;
	private Long updateBy;
	private Date createDate;
	private String receiveBank;

	public void setReceiveName(String receiveName){
		this.receiveName=receiveName;
	}

	public String getReceiveName(){
		return this.receiveName;
	}

	public void setCreateUesr(Long createUesr){
		this.createUesr=createUesr;
	}

	public Long getCreateUesr(){
		return this.createUesr;
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

	public void setStatus(Long status){
		this.status=status;
	}

	public Long getStatus(){
		return this.status;
	}

	public void setReceiveAccountId(Long receiveAccountId){
		this.receiveAccountId=receiveAccountId;
	}

	public Long getReceiveAccountId(){
		return this.receiveAccountId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setReceiveBank(String receiveBank){
		this.receiveBank=receiveBank;
	}

	public String getReceiveBank(){
		return this.receiveBank;
	}

}