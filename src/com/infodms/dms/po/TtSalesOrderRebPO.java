/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-07-11 15:38:42
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtSalesOrderRebPO extends PO{

	private Long orderType;
	private Long usePer;
	private Date updateDate;
	private Double disAmount;
	private Long createBy;
	private Long detailId;
	private Double useAmount;
	private Integer status;
	private Long orderId;
	private Long updateBy;
	private Date useDate;
	private Long rebId;
	private Date createDate;
	private Double discount;

	public void setOrderType(Long orderType){
		this.orderType=orderType;
	}

	public Long getOrderType(){
		return this.orderType;
	}

	public void setUsePer(Long usePer){
		this.usePer=usePer;
	}

	public Long getUsePer(){
		return this.usePer;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setDisAmount(Double disAmount){
		this.disAmount=disAmount;
	}

	public Double getDisAmount(){
		return this.disAmount;
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

	public void setUseAmount(Double useAmount){
		this.useAmount=useAmount;
	}

	public Double getUseAmount(){
		return this.useAmount;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
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

	public void setRebId(Long rebId){
		this.rebId=rebId;
	}

	public Long getRebId(){
		return this.rebId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setDiscount(Double discount){
		this.discount=discount;
	}

	public Double getDiscount(){
		return this.discount;
	}

}