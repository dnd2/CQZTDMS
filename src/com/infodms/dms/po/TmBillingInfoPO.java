/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2017-08-31 18:29:30
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmBillingInfoPO extends PO{

	private String bank;
	private Long dealerId;
	private Long updateBy;
	private Integer dealerType;
	private Date updateDate;
	private Long billingInfoId;
	private Long createBy;
	private String billingAddress;
	private Date createDate;
	private Integer status;
	private String account;
	private String taxNo;
	private String billingUnit;
	private Integer billingType;

	public void setBank(String bank){
		this.bank=bank;
	}

	public String getBank(){
		return this.bank;
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

	public void setDealerType(Integer dealerType){
		this.dealerType=dealerType;
	}

	public Integer getDealerType(){
		return this.dealerType;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setBillingInfoId(Long billingInfoId){
		this.billingInfoId=billingInfoId;
	}

	public Long getBillingInfoId(){
		return this.billingInfoId;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setBillingAddress(String billingAddress){
		this.billingAddress=billingAddress;
	}

	public String getBillingAddress(){
		return this.billingAddress;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setAccount(String account){
		this.account=account;
	}

	public String getAccount(){
		return this.account;
	}

	public void setTaxNo(String taxNo){
		this.taxNo=taxNo;
	}

	public String getTaxNo(){
		return this.taxNo;
	}

	public void setBillingUnit(String billingUnit){
		this.billingUnit=billingUnit;
	}

	public String getBillingUnit(){
		return this.billingUnit;
	}

	public void setBillingType(Integer billingType){
		this.billingType=billingType;
	}

	public Integer getBillingType(){
		return this.billingType;
	}

	@Override
	public String toString() {
		return "TmBillingInfoPO [bank=" + bank + ", dealerId=" + dealerId + ", updateBy=" + updateBy + ", dealerType="
				+ dealerType + ", updateDate=" + updateDate + ", billingInfoId=" + billingInfoId + ", createBy="
				+ createBy + ", billingAddress=" + billingAddress + ", createDate=" + createDate + ", status=" + status
				+ ", account=" + account + ", taxNo=" + taxNo + ", billingUnit=" + billingUnit + ", billingType="
				+ billingType + "]";
	}
	

}