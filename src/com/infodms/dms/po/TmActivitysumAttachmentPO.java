/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-02-26 03:33:10
* CreateBy   : tianheng
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmActivitysumAttachmentPO extends PO{

	private Date updateDate;
	private Long updateBy;
	private Long createBy;
	private Long auditlogId;
	private Date createDate;
	private String attachmentName;
	private Long sumId;
	private Long activityAttachmentId;
	private String fileId;

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

	public void setAuditlogId(Long auditlogId){
		this.auditlogId=auditlogId;
	}

	public Long getAuditlogId(){
		return this.auditlogId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setAttachmentName(String attachmentName){
		this.attachmentName=attachmentName;
	}

	public String getAttachmentName(){
		return this.attachmentName;
	}

	public void setSumId(Long sumId){
		this.sumId=sumId;
	}

	public Long getSumId(){
		return this.sumId;
	}

	public void setActivityAttachmentId(Long activityAttachmentId){
		this.activityAttachmentId=activityAttachmentId;
	}

	public Long getActivityAttachmentId(){
		return this.activityAttachmentId;
	}

	public void setFileId(String fileId){
		this.fileId=fileId;
	}

	public String getFileId(){
		return this.fileId;
	}

}