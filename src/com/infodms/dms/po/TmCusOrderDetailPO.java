/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-06-24 19:16:36
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmCusOrderDetailPO extends PO{

	private Integer orderNum;
	private Long updateBy;
	private Date updateDate;
	private Double totalAmount;
	private Long createBy;
	private Double standPrice;
	private Long cusOrderId;
	private Long detailId;
	private Date createDate;
	private Integer num;
	private Long maiId;

	public void setOrderNum(Integer orderNum){
		this.orderNum=orderNum;
	}

	public Integer getOrderNum(){
		return this.orderNum;
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

	public void setTotalAmount(Double totalAmount){
		this.totalAmount=totalAmount;
	}

	public Double getTotalAmount(){
		return this.totalAmount;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setStandPrice(Double standPrice){
		this.standPrice=standPrice;
	}

	public Double getStandPrice(){
		return this.standPrice;
	}

	public void setCusOrderId(Long cusOrderId){
		this.cusOrderId=cusOrderId;
	}

	public Long getCusOrderId(){
		return this.cusOrderId;
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

	public void setNum(Integer num){
		this.num=num;
	}

	public Integer getNum(){
		return this.num;
	}

	public void setMaiId(Long maiId){
		this.maiId=maiId;
	}

	public Long getMaiId(){
		return this.maiId;
	}

}