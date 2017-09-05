/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-12-18 11:41:18
* CreateBy   : wswcx
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmVehicleCostbudgetDetailPO extends PO{

	private Double matPrice;
	private Long updateBy;
	private Date updateDate;
	private Long createBy;
	private Long detailId;
	private Date createDate;
	private Float discount;
	private String vOrderId;
	private Long vehicleId;

	public void setMatPrice(Double matPrice){
		this.matPrice=matPrice;
	}

	public Double getMatPrice(){
		return this.matPrice;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
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

	public void setDetailId(Long detailId){
		this.detailId=detailId;
	}

	public Long getDetailId(){
		return this.detailId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setDiscount(Float discount){
		this.discount=discount;
	}

	public Float getDiscount(){
		return this.discount;
	}

	public void setVOrderId(String vOrderId){
		this.vOrderId=vOrderId;
	}

	public String getVOrderId(){
		return this.vOrderId;
	}

	public void setVehicleId(Long vehicleId){
		this.vehicleId=vehicleId;
	}

	public Long getVehicleId(){
		return this.vehicleId;
	}

}