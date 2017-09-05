/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-01-21 15:15:06
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsDlvryErpDtlPO extends PO{

	private String carStatus;
	private String vin;
	private Date updateDate;
	private Long isReceive;
	private Long createBy;
	private Long detailId;
	private String carType;
	private String enginerId;
	private Long reqId;
	private Long sendcarHeaderId;
	private Long updateBy;
	private String color;
	private String invoiceNumber;
	private Date createDate;
	private Long vehicleId;

	public void setCarStatus(String carStatus){
		this.carStatus=carStatus;
	}

	public String getCarStatus(){
		return this.carStatus;
	}

	public void setVin(String vin){
		this.vin=vin;
	}

	public String getVin(){
		return this.vin;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setIsReceive(Long isReceive){
		this.isReceive=isReceive;
	}

	public Long getIsReceive(){
		return this.isReceive;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setDetailId(Long detailId){
		this.detailId=detailId;
	}

	public Long getDetailId(){
		return this.detailId;
	}

	public void setCarType(String carType){
		this.carType=carType;
	}

	public String getCarType(){
		return this.carType;
	}

	public void setEnginerId(String enginerId){
		this.enginerId=enginerId;
	}

	public String getEnginerId(){
		return this.enginerId;
	}

	public void setReqId(Long reqId){
		this.reqId=reqId;
	}

	public Long getReqId(){
		return this.reqId;
	}

	public void setSendcarHeaderId(Long sendcarHeaderId){
		this.sendcarHeaderId=sendcarHeaderId;
	}

	public Long getSendcarHeaderId(){
		return this.sendcarHeaderId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setColor(String color){
		this.color=color;
	}

	public String getColor(){
		return this.color;
	}

	public void setInvoiceNumber(String invoiceNumber){
		this.invoiceNumber=invoiceNumber;
	}

	public String getInvoiceNumber(){
		return this.invoiceNumber;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setVehicleId(Long vehicleId){
		this.vehicleId=vehicleId;
	}

	public Long getVehicleId(){
		return this.vehicleId;
	}

}