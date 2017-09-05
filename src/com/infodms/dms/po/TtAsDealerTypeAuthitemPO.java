/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2011-09-15 16:14:22
* CreateBy   : ray
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsDealerTypeAuthitemPO extends PO{

	private Long dealerId;
	private Long id;
	private String remark;
	private Long createBy;
	private Date createDate;
	private Integer updateType;
	private Integer yilie;

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
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

	public void setUpdateType(Integer updateType){
		this.updateType=updateType;
	}

	public Integer getUpdateType(){
		return this.updateType;
	}

	public void setYilie(Integer yilie){
		this.yilie=yilie;
	}

	public Integer getYilie(){
		return this.yilie;
	}

}