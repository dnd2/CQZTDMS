/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-09-24 18:17:31
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtStoPO extends PO{

	private Long companyId;
	private String erpOrderNo;
	private Date updateDate;
	private String orderNo;
	private Long createBy;
	private Long stoId;
	private Integer printFlag;
	private Long toWarehouseId;
	private Integer status;
	private String erpMsg;
	private Long fromWarehouseId;
	private Long updateBy;
	private Long priceId;
	private Date stoDate;
	private Date createDate;

	public void setCompanyId(Long companyId){
		this.companyId=companyId;
	}

	public Long getCompanyId(){
		return this.companyId;
	}

	public void setErpOrderNo(String erpOrderNo){
		this.erpOrderNo=erpOrderNo;
	}

	public String getErpOrderNo(){
		return this.erpOrderNo;
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

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setStoId(Long stoId){
		this.stoId=stoId;
	}

	public Long getStoId(){
		return this.stoId;
	}

	public void setPrintFlag(Integer printFlag){
		this.printFlag=printFlag;
	}

	public Integer getPrintFlag(){
		return this.printFlag;
	}

	public void setToWarehouseId(Long toWarehouseId){
		this.toWarehouseId=toWarehouseId;
	}

	public Long getToWarehouseId(){
		return this.toWarehouseId;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setErpMsg(String erpMsg){
		this.erpMsg=erpMsg;
	}

	public String getErpMsg(){
		return this.erpMsg;
	}

	public void setFromWarehouseId(Long fromWarehouseId){
		this.fromWarehouseId=fromWarehouseId;
	}

	public Long getFromWarehouseId(){
		return this.fromWarehouseId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setPriceId(Long priceId){
		this.priceId=priceId;
	}

	public Long getPriceId(){
		return this.priceId;
	}

	public void setStoDate(Date stoDate){
		this.stoDate=stoDate;
	}

	public Date getStoDate(){
		return this.stoDate;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

}