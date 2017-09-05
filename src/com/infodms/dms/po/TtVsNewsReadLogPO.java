/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-01-28 17:11:27
* CreateBy   : cqca43189
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsNewsReadLogPO extends PO{

	private Integer operateType;
	private Date readerDate;
	private Long readLogId;
	private Long readerId;
	private Long newsId;

	public void setOperateType(Integer operateType){
		this.operateType=operateType;
	}

	public Integer getOperateType(){
		return this.operateType;
	}

	public void setReaderDate(Date readerDate){
		this.readerDate=readerDate;
	}

	public Date getReaderDate(){
		return this.readerDate;
	}

	public void setReadLogId(Long gId){
		this.readLogId=gId;
	}

	public Long getReadLogId(){
		return this.readLogId;
	}

	public void setReaderId(Long readerId){
		this.readerId=readerId;
	}

	public Long getReaderId(){
		return this.readerId;
	}

	public void setNewsId(Long newsId){
		this.newsId=newsId;
	}

	public Long getNewsId(){
		return this.newsId;
	}

}