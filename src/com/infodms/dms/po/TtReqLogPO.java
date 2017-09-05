/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-05-19 10:37:45
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.math.BigDecimal;
import java.util.Date;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtReqLogPO extends PO{

	private BigDecimal logId;
	private Date updateDate;
	private BigDecimal updateBy;
	private BigDecimal createBy;
	private BigDecimal reqId;
	private Date createDate;
	private BigDecimal reqStatus;

	public void setLogId(BigDecimal logId){
		this.logId=logId;
	}

	public BigDecimal getLogId(){
		return this.logId;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setUpdateBy(BigDecimal updateBy){
		this.updateBy=updateBy;
	}

	public BigDecimal getUpdateBy(){
		return this.updateBy;
	}

	public void setCreateBy(BigDecimal createBy){
		this.createBy=createBy;
	}

	public BigDecimal getCreateBy(){
		return this.createBy;
	}

	public void setReqId(BigDecimal reqId){
		this.reqId=reqId;
	}

	public BigDecimal getReqId(){
		return this.reqId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setReqStatus(BigDecimal reqStatus){
		this.reqStatus=reqStatus;
	}

	public BigDecimal getReqStatus(){
		return this.reqStatus;
	}

}