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
public class TmVehicleCostbudgetPO extends PO{

	private String isInvoice;
	private Long updateBy;
	private Integer vCount;
	private Date updateDate;
	private Long createBy;
	private Date createDate;
	private String vOrderId;
	private String isDel;
	private Double costPrice;

	public void setIsInvoice(String isInvoice){
		this.isInvoice=isInvoice;
	}

	public String getIsInvoice(){
		return this.isInvoice;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setVCount(Integer vCount){
		this.vCount=vCount;
	}

	public Integer getVCount(){
		return this.vCount;
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

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setVOrderId(String vOrderId){
		this.vOrderId=vOrderId;
	}

	public String getVOrderId(){
		return this.vOrderId;
	}

	public void setIsDel(String isDel){
		this.isDel=isDel;
	}

	public String getIsDel(){
		return this.isDel;
	}

	public void setCostPrice(Double costPrice){
		this.costPrice=costPrice;
	}

	public Double getCostPrice(){
		return this.costPrice;
	}

}