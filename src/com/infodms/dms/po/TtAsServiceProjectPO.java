/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2017-08-09 19:57:15
* CreateBy   : liaoyu
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsServiceProjectPO extends PO{

	private Float labourHour;
	private Double labourPrice;
	private Long serviceOrderId;
	private Long labourId;
	private Long createBy;
	private Date createDate;
	private Integer labourPaymentMethod;
	private String cnDes;
	private Long serviceProjectId;
	private String labourCode;

	public void setLabourHour(Float labourHour){
		this.labourHour=labourHour;
	}

	public Float getLabourHour(){
		return this.labourHour;
	}

	public void setLabourPrice(Double labourPrice){
		this.labourPrice=labourPrice;
	}

	public Double getLabourPrice(){
		return this.labourPrice;
	}

	public void setServiceOrderId(Long serviceOrderId){
		this.serviceOrderId=serviceOrderId;
	}

	public Long getServiceOrderId(){
		return this.serviceOrderId;
	}

	public void setLabourId(Long labourId){
		this.labourId=labourId;
	}

	public Long getLabourId(){
		return this.labourId;
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

	public void setLabourPaymentMethod(Integer labourPaymentMethod){
		this.labourPaymentMethod=labourPaymentMethod;
	}

	public Integer getLabourPaymentMethod(){
		return this.labourPaymentMethod;
	}

	public void setCnDes(String cnDes){
		this.cnDes=cnDes;
	}

	public String getCnDes(){
		return this.cnDes;
	}

	public void setServiceProjectId(Long serviceProjectId){
		this.serviceProjectId=serviceProjectId;
	}

	public Long getServiceProjectId(){
		return this.serviceProjectId;
	}

	public void setLabourCode(String labourCode){
		this.labourCode=labourCode;
	}

	public String getLabourCode(){
		return this.labourCode;
	}

}