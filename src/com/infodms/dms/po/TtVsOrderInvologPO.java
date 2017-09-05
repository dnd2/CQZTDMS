/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-07-11 14:41:28
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsOrderInvologPO extends PO{

	private String orderType;
	private Date updateDate;
	private String orderNo;
	private Long createTy;
	private Date lastInvoDate;
	private Date invoDate;
	private String invoLastVer;
	private Long updateBy;
	private String invoNo;
	private String invoVer;
	private Long invoLogId;
	private String invoLastNo;
	private Date createDate;

	public void setOrderType(String orderType){
		this.orderType=orderType;
	}

	public String getOrderType(){
		return this.orderType;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setOrderNo(String orderNo){
		this.orderNo=orderNo;
	}

	public String getOrderNo(){
		return this.orderNo;
	}

	public void setCreateTy(Long createTy){
		this.createTy=createTy;
	}

	public Long getCreateTy(){
		return this.createTy;
	}

	public void setLastInvoDate(Date lastInvoDate){
		this.lastInvoDate=lastInvoDate;
	}

	public Date getLastInvoDate(){
		return this.lastInvoDate;
	}

	public void setInvoDate(Date invoDate){
		this.invoDate=invoDate;
	}

	public Date getInvoDate(){
		return this.invoDate;
	}

	public void setInvoLastVer(String invoLastVer){
		this.invoLastVer=invoLastVer;
	}

	public String getInvoLastVer(){
		return this.invoLastVer;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setInvoNo(String invoNo){
		this.invoNo=invoNo;
	}

	public String getInvoNo(){
		return this.invoNo;
	}

	public void setInvoVer(String invoVer){
		this.invoVer=invoVer;
	}

	public String getInvoVer(){
		return this.invoVer;
	}

	public void setInvoLogId(Long invoLogId){
		this.invoLogId=invoLogId;
	}

	public Long getInvoLogId(){
		return this.invoLogId;
	}

	public void setInvoLastNo(String invoLastNo){
		this.invoLastNo=invoLastNo;
	}

	public String getInvoLastNo(){
		return this.invoLastNo;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

}