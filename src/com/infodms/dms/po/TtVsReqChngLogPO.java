/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-10-25 14:16:43
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsReqChngLogPO extends PO{

	private Long logId;
	private Date chngDate;
	private Long reqId;
	private Long userId;
	private Integer chngType;

	public void setLogId(Long logId){
		this.logId=logId;
	}

	public Long getLogId(){
		return this.logId;
	}

	public void setChngDate(Date chngDate){
		this.chngDate=chngDate;
	}

	public Date getChngDate(){
		return this.chngDate;
	}

	public void setReqId(Long reqId){
		this.reqId=reqId;
	}

	public Long getReqId(){
		return this.reqId;
	}

	public void setUserId(Long userId){
		this.userId=userId;
	}

	public Long getUserId(){
		return this.userId;
	}

	public void setChngType(Integer chngType){
		this.chngType=chngType;
	}

	public Integer getChngType(){
		return this.chngType;
	}

}