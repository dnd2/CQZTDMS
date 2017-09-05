/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-07-12 15:27:35
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsPricePO extends PO{

	private Long companyId;
	private Date updateDate;
	private Long priceId;
	private String priceDesc;
	private Long updateBy;
	private Long createBy;
	private Date createDate;
	private Date startDate;
	private Date endDate;
	private String priceCode;

	public void setCompanyId(Long companyId){
		this.companyId=companyId;
	}

	public Long getCompanyId(){
		return this.companyId;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setPriceId(Long priceId){
		this.priceId=priceId;
	}

	public Long getPriceId(){
		return this.priceId;
	}

	public void setPriceDesc(String priceDesc){
		this.priceDesc=priceDesc;
	}

	public String getPriceDesc(){
		return this.priceDesc;
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

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setStartDate(Date startDate){
		this.startDate=startDate;
	}

	public Date getStartDate(){
		return this.startDate;
	}

	public void setEndDate(Date endDate){
		this.endDate=endDate;
	}

	public Date getEndDate(){
		return this.endDate;
	}

	public void setPriceCode(String priceCode){
		this.priceCode=priceCode;
	}

	public String getPriceCode(){
		return this.priceCode;
	}

}