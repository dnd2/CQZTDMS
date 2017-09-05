/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-06-29 16:51:06
* CreateBy   : 
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtCampaignAttachPO extends PO{

	private Long updateBy;
	private Long campaignId;
	private String attachmentName;
	private Date updateDate;
	private Integer attachmentType;
	private Long fileId;
	private Long createBy;
	private Long attachmentId;
	private Date createDate;
	private String attachmentPath;

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setCampaignId(Long campaignId){
		this.campaignId=campaignId;
	}

	public Long getCampaignId(){
		return this.campaignId;
	}

	public void setAttachmentName(String attachmentName){
		this.attachmentName=attachmentName;
	}

	public String getAttachmentName(){
		return this.attachmentName;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setAttachmentType(Integer attachmentType){
		this.attachmentType=attachmentType;
	}

	public Integer getAttachmentType(){
		return this.attachmentType;
	}

	public void setFileId(Long fileId){
		this.fileId=fileId;
	}

	public Long getFileId(){
		return this.fileId;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setAttachmentId(Long attachmentId){
		this.attachmentId=attachmentId;
	}

	public Long getAttachmentId(){
		return this.attachmentId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setAttachmentPath(String attachmentPath){
		this.attachmentPath=attachmentPath;
	}

	public String getAttachmentPath(){
		return this.attachmentPath;
	}

}