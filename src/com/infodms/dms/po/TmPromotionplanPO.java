/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-01-14 13:40:59
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmPromotionplanPO extends PO{

	private Date planEndDate;
	private Date updateDate;
	private Long createBy;
	private Long orgId;
	private Date createDate;
	private String productDesc;
	private Date planStartDate;
	private Long brandId;
	private String planName;
	private Integer status;
	private Long updateBy;
	private String planCode;
	private String discountDesc;
	private Long promotionId;

	public void setPlanEndDate(Date planEndDate){
		this.planEndDate=planEndDate;
	}

	public Date getPlanEndDate(){
		return this.planEndDate;
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

	public void setOrgId(Long orgId){
		this.orgId=orgId;
	}

	public Long getOrgId(){
		return this.orgId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setProductDesc(String productDesc){
		this.productDesc=productDesc;
	}

	public String getProductDesc(){
		return this.productDesc;
	}

	public void setPlanStartDate(Date planStartDate){
		this.planStartDate=planStartDate;
	}

	public Date getPlanStartDate(){
		return this.planStartDate;
	}

	public void setBrandId(Long brandId){
		this.brandId=brandId;
	}

	public Long getBrandId(){
		return this.brandId;
	}

	public void setPlanName(String planName){
		this.planName=planName;
	}

	public String getPlanName(){
		return this.planName;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setPlanCode(String planCode){
		this.planCode=planCode;
	}

	public String getPlanCode(){
		return this.planCode;
	}

	public void setDiscountDesc(String discountDesc){
		this.discountDesc=discountDesc;
	}

	public String getDiscountDesc(){
		return this.discountDesc;
	}

	public void setPromotionId(Long promotionId){
		this.promotionId=promotionId;
	}

	public Long getPromotionId(){
		return this.promotionId;
	}

}