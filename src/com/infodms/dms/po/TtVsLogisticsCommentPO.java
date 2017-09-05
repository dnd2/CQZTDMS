/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2012-11-15 18:00:12
* CreateBy   : qindb
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsLogisticsCommentPO extends PO{

	private String commentGuest;
	private Date createdDate;
	private Long createdBy;
	private Long commentTotal;
	private Long updatedBy;
	private String commentHost;
	private String commentObject;
	private Long commentAssist;
	private Long commentType;
	private Date updatedDate;
	private String remark;
	private Long status;
	private Long lcommentId;

	public void setCommentGuest(String commentGuest){
		this.commentGuest=commentGuest;
	}

	public String getCommentGuest(){
		return this.commentGuest;
	}

	public void setCreatedDate(Date createdDate){
		this.createdDate=createdDate;
	}

	public Date getCreatedDate(){
		return this.createdDate;
	}

	public void setCreatedBy(Long createdBy){
		this.createdBy=createdBy;
	}

	public Long getCreatedBy(){
		return this.createdBy;
	}

	public void setCommentTotal(Long commentTotal){
		this.commentTotal=commentTotal;
	}

	public Long getCommentTotal(){
		return this.commentTotal;
	}

	public void setUpdatedBy(Long updatedBy){
		this.updatedBy=updatedBy;
	}

	public Long getUpdatedBy(){
		return this.updatedBy;
	}

	public void setCommentHost(String commentHost){
		this.commentHost=commentHost;
	}

	public String getCommentHost(){
		return this.commentHost;
	}

	public void setCommentObject(String commentObject){
		this.commentObject=commentObject;
	}

	public String getCommentObject(){
		return this.commentObject;
	}

	public void setCommentAssist(Long commentAssist){
		this.commentAssist=commentAssist;
	}

	public Long getCommentAssist(){
		return this.commentAssist;
	}

	public void setCommentType(Long commentType){
		this.commentType=commentType;
	}

	public Long getCommentType(){
		return this.commentType;
	}

	public void setUpdatedDate(Date updatedDate){
		this.updatedDate=updatedDate;
	}

	public Date getUpdatedDate(){
		return this.updatedDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setStatus(Long status){
		this.status=status;
	}

	public Long getStatus(){
		return this.status;
	}

	public void setLcommentId(Long lcommentId){
		this.lcommentId=lcommentId;
	}

	public Long getLcommentId(){
		return this.lcommentId;
	}

}