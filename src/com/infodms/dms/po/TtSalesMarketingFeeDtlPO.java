/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2015-01-16 10:11:09
* CreateBy   : wizard_lee
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtSalesMarketingFeeDtlPO extends PO{

	private Long dtlId;
	private Long updateBy;
	private Date updateDate;
	private Long createBy;
	private Date createDate;
	private Integer activityType;
	private Long feeId;

	public void setDtlId(Long dtlId){
		this.dtlId=dtlId;
	}

	public Long getDtlId(){
		return this.dtlId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
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

	public void setActivityType(Integer activityType){
		this.activityType=activityType;
	}

	public Integer getActivityType(){
		return this.activityType;
	}

	public void setFeeId(Long feeId){
		this.feeId=feeId;
	}

	public Long getFeeId(){
		return this.feeId;
	}

}