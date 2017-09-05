/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-08-13 10:31:23
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtProxyAreaPO extends PO{

	private Long dealerId;
	private String proxyAreaName;
	private Long id;
	private Long createBy;
	private Date createDate;
	private String proxyArea;

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setProxyAreaName(String proxyAreaName){
		this.proxyAreaName=proxyAreaName;
	}

	public String getProxyAreaName(){
		return this.proxyAreaName;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
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

	public void setProxyArea(String proxyArea){
		this.proxyArea=proxyArea;
	}

	public String getProxyArea(){
		return this.proxyArea;
	}

}