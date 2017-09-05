/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-07-05 19:15:45
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsActivityProjectPO extends PO{

	private Integer dealWay;
	private Long paid;
	private Long updateBy;
	private Date updateDate;
	private Long id;
	private Long createBy;
	private Integer proCode;
	private Long activityId;
	private Date createDate;
	private Double amount;
	private Integer maintainTime;

	public void setDealWay(Integer dealWay){
		this.dealWay=dealWay;
	}

	public Integer getDealWay(){
		return this.dealWay;
	}

	public void setPaid(Long paid){
		this.paid=paid;
	}

	public Long getPaid(){
		return this.paid;
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

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setProCode(Integer proCode){
		this.proCode=proCode;
	}

	public Integer getProCode(){
		return this.proCode;
	}

	public void setActivityId(Long activityId){
		this.activityId=activityId;
	}

	public Long getActivityId(){
		return this.activityId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setAmount(Double amount){
		this.amount=amount;
	}

	public Double getAmount(){
		return this.amount;
	}

	public void setMaintainTime(Integer maintainTime){
		this.maintainTime=maintainTime;
	}

	public Integer getMaintainTime(){
		return this.maintainTime;
	}

}