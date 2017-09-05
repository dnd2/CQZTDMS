/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-02-05 16:11:40
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmIntentCustomerPO extends PO{

	private String customerName;
	private Date updateDate;
	private Long createBy;
	private String parentShortname;
	private Integer customerLevel;
	private Date createDate;
	private String parentDealerCode;
	private Integer coustomerFrom;
	private String intentBrand;
	private String dealerShortname;
	private String customerCode;
	private Date createCardDate;
	private String linkmanMobile;
	private String linkmanName;
	private Integer status;
	private String dealerCode;
	private Long updateBy;
	private Date intendBuyTime;
	private Long intentCustomerId;

	public void setCustomerName(String customerName){
		this.customerName=customerName;
	}

	public String getCustomerName(){
		return this.customerName;
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

	public void setParentShortname(String parentShortname){
		this.parentShortname=parentShortname;
	}

	public String getParentShortname(){
		return this.parentShortname;
	}

	public void setCustomerLevel(Integer customerLevel){
		this.customerLevel=customerLevel;
	}

	public Integer getCustomerLevel(){
		return this.customerLevel;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setParentDealerCode(String parentDealerCode){
		this.parentDealerCode=parentDealerCode;
	}

	public String getParentDealerCode(){
		return this.parentDealerCode;
	}

	public void setCoustomerFrom(Integer coustomerFrom){
		this.coustomerFrom=coustomerFrom;
	}

	public Integer getCoustomerFrom(){
		return this.coustomerFrom;
	}

	public void setIntentBrand(String intentBrand){
		this.intentBrand=intentBrand;
	}

	public String getIntentBrand(){
		return this.intentBrand;
	}

	public void setDealerShortname(String dealerShortname){
		this.dealerShortname=dealerShortname;
	}

	public String getDealerShortname(){
		return this.dealerShortname;
	}

	public void setCustomerCode(String customerCode){
		this.customerCode=customerCode;
	}

	public String getCustomerCode(){
		return this.customerCode;
	}

	public void setCreateCardDate(Date createCardDate){
		this.createCardDate=createCardDate;
	}

	public Date getCreateCardDate(){
		return this.createCardDate;
	}

	public void setLinkmanMobile(String linkmanMobile){
		this.linkmanMobile=linkmanMobile;
	}

	public String getLinkmanMobile(){
		return this.linkmanMobile;
	}

	public void setLinkmanName(String linkmanName){
		this.linkmanName=linkmanName;
	}

	public String getLinkmanName(){
		return this.linkmanName;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setDealerCode(String dealerCode){
		this.dealerCode=dealerCode;
	}

	public String getDealerCode(){
		return this.dealerCode;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setIntendBuyTime(Date intendBuyTime){
		this.intendBuyTime=intendBuyTime;
	}

	public Date getIntendBuyTime(){
		return this.intendBuyTime;
	}

	public void setIntentCustomerId(Long intentCustomerId){
		this.intentCustomerId=intentCustomerId;
	}

	public Long getIntentCustomerId(){
		return this.intentCustomerId;
	}

}