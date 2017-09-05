/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-03-08 19:41:26
* CreateBy   : longwenjun
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmAttachmentPO extends PO{

	private Long attachmentId;
	private Long createBy;
	private String name;
	private String attachmentType;
	private Date createDate;
	private Long businessId;
	private String fileId;
	private String addressUrl;

	public void setAttachmentId(Long attachmentId){
		this.attachmentId=attachmentId;
	}

	public Long getAttachmentId(){
		return this.attachmentId;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setName(String name){
		this.name=name;
	}

	public String getName(){
		return this.name;
	}

	public void setAttachmentType(String attachmentType){
		this.attachmentType=attachmentType;
	}

	public String getAttachmentType(){
		return this.attachmentType;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setBusinessId(Long businessId){
		this.businessId=businessId;
	}

	public Long getBusinessId(){
		return this.businessId;
	}

	public void setFileId(String fileId){
		this.fileId=fileId;
	}

	public String getFileId(){
		return this.fileId;
	}

	public void setAddressUrl(String addressUrl){
		this.addressUrl=addressUrl;
	}

	public String getAddressUrl(){
		return this.addressUrl;
	}

}