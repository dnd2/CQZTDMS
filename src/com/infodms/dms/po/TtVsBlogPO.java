/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-07-23 09:41:54
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsBlogPO extends PO{

	private Long year;
	private Date updateDate;
	private Long status;
	private Long updateBy;
	private String blogNo;
	private Long createBy;
	private Long integMonth;
	private Date createDate;
	private Long blogId;
	private Long dealerId;

	public void setYear(Long year){
		this.year=year;
	}

	public Long getYear(){
		return this.year;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setStatus(Long status){
		this.status=status;
	}

	public Long getStatus(){
		return this.status;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setBlogNo(String blogNo){
		this.blogNo=blogNo;
	}

	public String getBlogNo(){
		return this.blogNo;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setIntegMonth(Long integMonth){
		this.integMonth=integMonth;
	}

	public Long getIntegMonth(){
		return this.integMonth;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setBlogId(Long blogId){
		this.blogId=blogId;
	}

	public Long getBlogId(){
		return this.blogId;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

}