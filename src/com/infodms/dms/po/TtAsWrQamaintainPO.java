/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-09-30 18:00:40
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrQamaintainPO extends PO{

	private Integer maxDays;
	private Integer month;
	private Date updateDate;
	private Long updateBy;
	private Integer freeTimes;
	private Long createBy;
	private Double endMileage;
	private Long oemCompanyId;
	private Double startMileage;
	private Integer minDays;
	private Date createDate;
	private Long id;

	public void setMaxDays(Integer maxDays){
		this.maxDays=maxDays;
	}

	public Integer getMaxDays(){
		return this.maxDays;
	}

	public void setMonth(Integer month){
		this.month=month;
	}

	public Integer getMonth(){
		return this.month;
	}

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

	public void setFreeTimes(Integer freeTimes){
		this.freeTimes=freeTimes;
	}

	public Integer getFreeTimes(){
		return this.freeTimes;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setEndMileage(Double endMileage){
		this.endMileage=endMileage;
	}

	public Double getEndMileage(){
		return this.endMileage;
	}

	public void setOemCompanyId(Long oemCompanyId){
		this.oemCompanyId=oemCompanyId;
	}

	public Long getOemCompanyId(){
		return this.oemCompanyId;
	}

	public void setStartMileage(Double startMileage){
		this.startMileage=startMileage;
	}

	public Double getStartMileage(){
		return this.startMileage;
	}

	public void setMinDays(Integer minDays){
		this.minDays=minDays;
	}

	public Integer getMinDays(){
		return this.minDays;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

}