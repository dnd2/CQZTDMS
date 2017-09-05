/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-03-24 10:06:27
* CreateBy   : Yu
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartBookDtlPO extends PO{

	private Long bookedQty;
	private Integer state;
	private Long pickorderId;
	private Integer configId;
	private Date updateDate;
	private Long pkgQty;
	private Long createBy;
	private Integer status;
	private Long whId;
	private Long outQty;
	private Long xcboQty;
	private Long dtlId;
	private Long orderId;
	private Long salesQty;
	private Long updateBy;
	private Long partId;
	private Long locId;
	private Long itemQty;
	private Date createDate;

	public void setBookedQty(Long bookedQty){
		this.bookedQty=bookedQty;
	}

	public Long getBookedQty(){
		return this.bookedQty;
	}

	public void setState(Integer state){
		this.state=state;
	}

	public Integer getState(){
		return this.state;
	}

	public void setPickorderId(Long pickorderId){
		this.pickorderId=pickorderId;
	}

	public Long getPickorderId(){
		return this.pickorderId;
	}

	public void setConfigId(Integer configId){
		this.configId=configId;
	}

	public Integer getConfigId(){
		return this.configId;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setPkgQty(Long pkgQty){
		this.pkgQty=pkgQty;
	}

	public Long getPkgQty(){
		return this.pkgQty;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setWhId(Long whId){
		this.whId=whId;
	}

	public Long getWhId(){
		return this.whId;
	}

	public void setOutQty(Long outQty){
		this.outQty=outQty;
	}

	public Long getOutQty(){
		return this.outQty;
	}

	public void setXcboQty(Long xcboQty){
		this.xcboQty=xcboQty;
	}

	public Long getXcboQty(){
		return this.xcboQty;
	}

	public void setDtlId(Long dtlId){
		this.dtlId=dtlId;
	}

	public Long getDtlId(){
		return this.dtlId;
	}

	public void setOrderId(Long orderId){
		this.orderId=orderId;
	}

	public Long getOrderId(){
		return this.orderId;
	}

	public void setSalesQty(Long salesQty){
		this.salesQty=salesQty;
	}

	public Long getSalesQty(){
		return this.salesQty;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setPartId(Long partId){
		this.partId=partId;
	}

	public Long getPartId(){
		return this.partId;
	}

	public void setLocId(Long locId){
		this.locId=locId;
	}

	public Long getLocId(){
		return this.locId;
	}

	public void setItemQty(Long itemQty){
		this.itemQty=itemQty;
	}

	public Long getItemQty(){
		return this.itemQty;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

}