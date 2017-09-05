/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-04-25 14:07:33
* CreateBy   : wangsongwei
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtSalesOrderAccPO extends PO{

	private Long usePer;
	private Long useStatus;
	private Long orderId;
	private Long updateBy;
	private Date useDate;
	private Date updateDate;
	private Long createBy;
	private Double useAmount;
	private Long accId;
	private Long detailId;
	private Date createDate;
	private Long orderType;
	public Long getOrderType() {
		return orderType;
	}

	public void setOrderType(Long orderType) {
		this.orderType = orderType;
	}

	public void setUsePer(Long usePer){
		this.usePer=usePer;
	}

	public Long getUsePer(){
		return this.usePer;
	}

	public void setUseStatus(Long useStatus){
		this.useStatus=useStatus;
	}

	public Long getUseStatus(){
		return this.useStatus;
	}

	public void setOrderId(Long orderId){
		this.orderId=orderId;
	}

	public Long getOrderId(){
		return this.orderId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setUseDate(Date useDate){
		this.useDate=useDate;
	}

	public Date getUseDate(){
		return this.useDate;
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

	public void setUseAmount(Double useAmount){
		this.useAmount=useAmount;
	}

	public Double getUseAmount(){
		return this.useAmount;
	}

	public void setAccId(Long accId){
		this.accId=accId;
	}

	public Long getAccId(){
		return this.accId;
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

}