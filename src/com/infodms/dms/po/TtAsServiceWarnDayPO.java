/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2017-08-18 17:14:33
* CreateBy   : liaoyu
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsServiceWarnDayPO extends PO{

	private String vin;
	private Long serviceOrderId;
	private Long warnRuleId;
	private Long createBy;
	private Date createDate;
	private Long warnDayId;
	private Integer repairDays;

	public void setVin(String vin){
		this.vin=vin;
	}

	public String getVin(){
		return this.vin;
	}

	public void setServiceOrderId(Long serviceOrderId){
		this.serviceOrderId=serviceOrderId;
	}

	public Long getServiceOrderId(){
		return this.serviceOrderId;
	}

	public void setWarnRuleId(Long warnRuleId){
		this.warnRuleId=warnRuleId;
	}

	public Long getWarnRuleId(){
		return this.warnRuleId;
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

	public void setWarnDayId(Long warnDayId){
		this.warnDayId=warnDayId;
	}

	public Long getWarnDayId(){
		return this.warnDayId;
	}

	public void setRepairDays(Integer repairDays){
		this.repairDays=repairDays;
	}

	public Integer getRepairDays(){
		return this.repairDays;
	}

}