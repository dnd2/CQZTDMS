/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-07-11 16:39:50
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartDiscountDefinePO extends PO{

	private Double orderAmount;
	private Integer state;
	private Long discountId;
	private Date disableDate;
	private Date deleteDate;
	private Date updateDate;
	private Date validFrom;
	private Long createBy;
	private Integer status;
	private Double discountRate;
	private Date validTo;
	private Long updateBy;
	private Integer dealerType;
	private Long deleteBy;
	private Date createDate;
	private Long disableBy;
	private Integer discountType;

	public void setOrderAmount(Double orderAmount){
		this.orderAmount=orderAmount;
	}

	public Double getOrderAmount(){
		return this.orderAmount;
	}

	public void setState(Integer state){
		this.state=state;
	}

	public Integer getState(){
		return this.state;
	}

	public void setDiscountId(Long discountId){
		this.discountId=discountId;
	}

	public Long getDiscountId(){
		return this.discountId;
	}

	public void setDisableDate(Date disableDate){
		this.disableDate=disableDate;
	}

	public Date getDisableDate(){
		return this.disableDate;
	}

	public void setDeleteDate(Date deleteDate){
		this.deleteDate=deleteDate;
	}

	public Date getDeleteDate(){
		return this.deleteDate;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setValidFrom(Date validFrom){
		this.validFrom=validFrom;
	}

	public Date getValidFrom(){
		return this.validFrom;
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

	public void setDiscountRate(Double discountRate){
		this.discountRate=discountRate;
	}

	public Double getDiscountRate(){
		return this.discountRate;
	}

	public void setValidTo(Date validTo){
		this.validTo=validTo;
	}

	public Date getValidTo(){
		return this.validTo;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setDealerType(Integer dealerType){
		this.dealerType=dealerType;
	}

	public Integer getDealerType(){
		return this.dealerType;
	}

	public void setDeleteBy(Long deleteBy){
		this.deleteBy=deleteBy;
	}

	public Long getDeleteBy(){
		return this.deleteBy;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setDisableBy(Long disableBy){
		this.disableBy=disableBy;
	}

	public Long getDisableBy(){
		return this.disableBy;
	}

	public void setDiscountType(Integer discountType){
		this.discountType=discountType;
	}

	public Integer getDiscountType(){
		return this.discountType;
	}

}