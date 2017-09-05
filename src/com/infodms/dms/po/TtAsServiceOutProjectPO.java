/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2017-08-17 17:42:38
* CreateBy   : liaoyu
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsServiceOutProjectPO extends PO{

	private String feeCode;
	private String feeName;
	private Integer feePaymentMethod;
	private Long serviceOrderId;
	private Double feePrice;
	private String feeRemark;
	private Long createBy;
	private Date createDate;
	private Long serviceOutProjectId;
	private Long feeRelationMainPart;
	private Long feeId;

	public void setFeeCode(String feeCode){
		this.feeCode=feeCode;
	}

	public String getFeeCode(){
		return this.feeCode;
	}

	public void setFeeName(String feeName){
		this.feeName=feeName;
	}

	public String getFeeName(){
		return this.feeName;
	}

	public void setFeePaymentMethod(Integer feePaymentMethod){
		this.feePaymentMethod=feePaymentMethod;
	}

	public Integer getFeePaymentMethod(){
		return this.feePaymentMethod;
	}

	public void setServiceOrderId(Long serviceOrderId){
		this.serviceOrderId=serviceOrderId;
	}

	public Long getServiceOrderId(){
		return this.serviceOrderId;
	}

	public void setFeePrice(Double feePrice){
		this.feePrice=feePrice;
	}

	public Double getFeePrice(){
		return this.feePrice;
	}

	public void setFeeRemark(String feeRemark){
		this.feeRemark=feeRemark;
	}

	public String getFeeRemark(){
		return this.feeRemark;
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

	public void setServiceOutProjectId(Long serviceOutProjectId){
		this.serviceOutProjectId=serviceOutProjectId;
	}

	public Long getServiceOutProjectId(){
		return this.serviceOutProjectId;
	}

	public void setFeeRelationMainPart(Long feeRelationMainPart){
		this.feeRelationMainPart=feeRelationMainPart;
	}

	public Long getFeeRelationMainPart(){
		return this.feeRelationMainPart;
	}

	public void setFeeId(Long feeId){
		this.feeId=feeId;
	}

	public Long getFeeId(){
		return this.feeId;
	}

}