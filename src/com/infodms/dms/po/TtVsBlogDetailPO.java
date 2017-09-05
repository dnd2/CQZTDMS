/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-07-19 16:57:59
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsBlogDetailPO extends PO{

	private Date updateDate;
	private Long updateBy;
	private Long createBy;
	private String blogTwo;
	private Date createDate;
	private Long blogInteg;
	private Long blogId;
	private Long detailId;
	private Long salesId;
	private String blogThree;
	private String blogOne;

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
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

	public void setBlogTwo(String blogTwo){
		this.blogTwo=blogTwo;
	}

	public String getBlogTwo(){
		return this.blogTwo;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setBlogInteg(Long blogInteg){
		this.blogInteg=blogInteg;
	}

	public Long getBlogInteg(){
		return this.blogInteg;
	}

	public void setBlogId(Long blogId){
		this.blogId=blogId;
	}

	public Long getBlogId(){
		return this.blogId;
	}

	public void setDetailId(Long detailId){
		this.detailId=detailId;
	}

	public Long getDetailId(){
		return this.detailId;
	}

	public void setSalesId(Long salesId){
		this.salesId=salesId;
	}

	public Long getSalesId(){
		return this.salesId;
	}

	public void setBlogThree(String blogThree){
		this.blogThree=blogThree;
	}

	public String getBlogThree(){
		return this.blogThree;
	}

	public void setBlogOne(String blogOne){
		this.blogOne=blogOne;
	}

	public String getBlogOne(){
		return this.blogOne;
	}

}