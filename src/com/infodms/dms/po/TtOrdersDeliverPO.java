/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-04-07 13:20:17
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.math.BigDecimal;
import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtOrdersDeliverPO extends PO{
	
	private String dealerName;
	private String status;
	private String materialCode;
	private BigDecimal shipNumber;
	private String orderType;
	private String dealerCode;
	private Date createDate;
	private Date updateDate;
	private Long orderNo;
	private Long id;

	public void setDealerName(String dealerName){
		this.dealerName=dealerName;
	}

	public String getDealerName(){
		return this.dealerName;
	}


	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setMaterialCode(String materialCode){
		this.materialCode=materialCode;
	}

	public String getMaterialCode(){
		return this.materialCode;
	}

	public void setShipNumber(BigDecimal shipNumber){
		this.shipNumber=shipNumber;
	}

	public BigDecimal getShipNumber(){
		return this.shipNumber;
	}

	public void setOrderType(String orderType){
		this.orderType=orderType;
	}

	public String getOrderType(){
		return this.orderType;
	}

	public void setDealerCode(String dealerCode){
		this.dealerCode=dealerCode;
	}

	public String getDealerCode(){
		return this.dealerCode;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public void setOrderNo(Long orderNo){
		this.orderNo=orderNo;
	}

	public Long getOrderNo(){
		return this.orderNo;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

}