/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-07-08 12:12:12
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtCampaignSummeryChkPO extends PO{

	private Integer checkStatus;
	private Date updateDate;
	private Long checkId;
	private Long createBy;
	private Long checkPositionId;
	private Integer attachmentType;
	private String checkDesc;
	private String attachmentName;
	private Date createDate;
	private Long fileId;
	private Long updateBy;
	private Date checkDate;
	private Long executeId;
	private Long checkUserId;
	private String attachmentPath;
	private Long checkOrgId;

	public void setCheckStatus(Integer checkStatus){
		this.checkStatus=checkStatus;
	}

	public Integer getCheckStatus(){
		return this.checkStatus;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setCheckId(Long checkId){
		this.checkId=checkId;
	}

	public Long getCheckId(){
		return this.checkId;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setCheckPositionId(Long checkPositionId){
		this.checkPositionId=checkPositionId;
	}

	public Long getCheckPositionId(){
		return this.checkPositionId;
	}

	public void setAttachmentType(Integer attachmentType){
		this.attachmentType=attachmentType;
	}

	public Integer getAttachmentType(){
		return this.attachmentType;
	}

	public void setCheckDesc(String checkDesc){
		this.checkDesc=checkDesc;
	}

	public String getCheckDesc(){
		return this.checkDesc;
	}

	public void setAttachmentName(String attachmentName){
		this.attachmentName=attachmentName;
	}

	public String getAttachmentName(){
		return this.attachmentName;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setFileId(Long fileId){
		this.fileId=fileId;
	}

	public Long getFileId(){
		return this.fileId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setCheckDate(Date checkDate){
		this.checkDate=checkDate;
	}

	public Date getCheckDate(){
		return this.checkDate;
	}

	public void setExecuteId(Long executeId){
		this.executeId=executeId;
	}

	public Long getExecuteId(){
		return this.executeId;
	}

	public void setCheckUserId(Long checkUserId){
		this.checkUserId=checkUserId;
	}

	public Long getCheckUserId(){
		return this.checkUserId;
	}

	public void setAttachmentPath(String attachmentPath){
		this.attachmentPath=attachmentPath;
	}

	public String getAttachmentPath(){
		return this.attachmentPath;
	}

	public void setCheckOrgId(Long checkOrgId){
		this.checkOrgId=checkOrgId;
	}

	public Long getCheckOrgId(){
		return this.checkOrgId;
	}

}