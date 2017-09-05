/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2017-09-02 22:46:11
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrBalanceBillPO extends PO{

	private Float taxMoney;
	private Float money;
	private Long id;
	private Long createBy;
	private String remark;
	private Float total;
	private String pcNo;
	private String balanceNo;
	private Date createDate;
	private String billNo;

	public void setTaxMoney(Float taxMoney){
		this.taxMoney=taxMoney;
	}

	public Float getTaxMoney(){
		return this.taxMoney;
	}

	public void setMoney(Float money){
		this.money=money;
	}

	public Float getMoney(){
		return this.money;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setTotal(Float total){
		this.total=total;
	}

	public Float getTotal(){
		return this.total;
	}

	public void setPcNo(String pcNo){
		this.pcNo=pcNo;
	}

	public String getPcNo(){
		return this.pcNo;
	}

	public void setBalanceNo(String balanceNo){
		this.balanceNo=balanceNo;
	}

	public String getBalanceNo(){
		return this.balanceNo;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setBillNo(String billNo){
		this.billNo=billNo;
	}

	public String getBillNo(){
		return this.billNo;
	}

}