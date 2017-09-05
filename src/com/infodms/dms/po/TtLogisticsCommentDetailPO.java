/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2012-11-20 17:18:56
* CreateBy   : qindb
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtLogisticsCommentDetailPO extends PO{

	private Date createdDate;
	private Long createdBy;
	private Long updatedBy;
	private Long status;
	private Long score;
	private Date updateDate;
	private Long detailId;
	private Integer points;
	private Long itemId;
	private Long lcommentId;

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

	public void setUpdatedBy(Long updatedBy){
		this.updatedBy=updatedBy;
	}

	public Long getUpdatedBy(){
		return this.updatedBy;
	}

	public void setStatus(Long status){
		this.status=status;
	}

	public Long getStatus(){
		return this.status;
	}

	public void setScore(Long score){
		this.score=score;
	}

	public Long getScore(){
		return this.score;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setDetailId(Long detailId){
		this.detailId=detailId;
	}

	public Long getDetailId(){
		return this.detailId;
	}

	public void setPoints(Integer points){
		this.points=points;
	}

	public Integer getPoints(){
		return this.points;
	}

	public void setItemId(Long itemId){
		this.itemId=itemId;
	}

	public Long getItemId(){
		return this.itemId;
	}

	public void setLcommentId(Long lcommentId){
		this.lcommentId=lcommentId;
	}

	public Long getLcommentId(){
		return this.lcommentId;
	}

}