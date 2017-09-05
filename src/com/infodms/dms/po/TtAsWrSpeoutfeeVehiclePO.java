/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-07-15 13:21:04
* CreateBy   : ZLD
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrSpeoutfeeVehiclePO extends PO{

	private String customerName;
	private Date saleDate;
	private String vin;
	private String customerPhone;
	private Date updateDate;
	private String remark;
	private Long createBy;
	private Long feeId;
	private String model;
	private Long updateBy;
	private Double mileage;
	private Date productDate;
	private Long id;
	private String engineNo;
	private Date createDate;

	public void setCustomerName(String customerName){
		this.customerName=customerName;
	}

	public String getCustomerName(){
		return this.customerName;
	}

	public void setSaleDate(Date saleDate){
		this.saleDate=saleDate;
	}

	public Date getSaleDate(){
		return this.saleDate;
	}

	public void setVin(String vin){
		this.vin=vin;
	}

	public String getVin(){
		return this.vin;
	}

	public void setCustomerPhone(String customerPhone){
		this.customerPhone=customerPhone;
	}

	public String getCustomerPhone(){
		return this.customerPhone;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setFeeId(Long feeId){
		this.feeId=feeId;
	}

	public Long getFeeId(){
		return this.feeId;
	}

	public void setModel(String model){
		this.model=model;
	}

	public String getModel(){
		return this.model;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setMileage(Double mileage){
		this.mileage=mileage;
	}

	public Double getMileage(){
		return this.mileage;
	}

	public void setProductDate(Date productDate){
		this.productDate=productDate;
	}

	public Date getProductDate(){
		return this.productDate;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setEngineNo(String engineNo){
		this.engineNo=engineNo;
	}

	public String getEngineNo(){
		return this.engineNo;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

}