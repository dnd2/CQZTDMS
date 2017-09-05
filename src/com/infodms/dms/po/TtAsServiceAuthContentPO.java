/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2017-08-18 17:15:40
* CreateBy   : liaoyu
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsServiceAuthContentPO extends PO{

	private Long authContentValue;
	private Long serviceOrderId;
	private Long authContentId;
	private Integer authContentType;
	private Long createBy;
	private Date createDate;

	public void setAuthContentValue(Long authContentValue){
		this.authContentValue=authContentValue;
	}

	public Long getAuthContentValue(){
		return this.authContentValue;
	}

	public void setServiceOrderId(Long serviceOrderId){
		this.serviceOrderId=serviceOrderId;
	}

	public Long getServiceOrderId(){
		return this.serviceOrderId;
	}

	public void setAuthContentId(Long authContentId){
		this.authContentId=authContentId;
	}

	public Long getAuthContentId(){
		return this.authContentId;
	}

	public void setAuthContentType(Integer authContentType){
		this.authContentType=authContentType;
	}

	public Integer getAuthContentType(){
		return this.authContentType;
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

}