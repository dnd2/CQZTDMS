/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2009-12-21 20:29:50
* CreateBy   : xwj
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmCredenceUsePO extends PO{

	private Date updateDate;
	private Integer status;
	private Long redNum;
	private Long orderNum;
	private Long updateBy;
	private Long createBy;
	private Double orderPrice;
	private Long orderId;
	private Long credenceId;
	private Double useMoney;
	private Date createDate;
	private Long credenceUseId;
	private Long backNum;

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setRedNum(Long redNum){
		this.redNum=redNum;
	}

	public Long getRedNum(){
		return this.redNum;
	}

	public void setOrderNum(Long orderNum){
		this.orderNum=orderNum;
	}

	public Long getOrderNum(){
		return this.orderNum;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setOrderPrice(Double orderPrice){
		this.orderPrice=orderPrice;
	}

	public Double getOrderPrice(){
		return this.orderPrice;
	}

	public void setOrderId(Long orderId){
		this.orderId=orderId;
	}

	public Long getOrderId(){
		return this.orderId;
	}

	public void setCredenceId(Long credenceId){
		this.credenceId=credenceId;
	}

	public Long getCredenceId(){
		return this.credenceId;
	}

	public void setUseMoney(Double useMoney){
		this.useMoney=useMoney;
	}

	public Double getUseMoney(){
		return this.useMoney;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setCredenceUseId(Long credenceUseId){
		this.credenceUseId=credenceUseId;
	}

	public Long getCredenceUseId(){
		return this.credenceUseId;
	}

	public Long getBackNum() {
		return backNum;
	}

	public void setBackNum(Long backNum) {
		this.backNum = backNum;
	}

}