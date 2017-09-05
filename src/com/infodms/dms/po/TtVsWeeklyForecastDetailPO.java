/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2012-11-19 18:02:56
* CreateBy   : HXY
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsWeeklyForecastDetailPO extends PO{

	private Long groupId;
	private Integer forecastAmount;
	private Long updateBy;
	private Date updateDate;
	private Integer forecastWeek;
	private Long createBy;
	private Long detailId;
	private Date createDate;
	private Long forecastId;

	public void setGroupId(Long groupId){
		this.groupId=groupId;
	}

	public Long getGroupId(){
		return this.groupId;
	}

	public void setForecastAmount(Integer forecastAmount){
		this.forecastAmount=forecastAmount;
	}

	public Integer getForecastAmount(){
		return this.forecastAmount;
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

	public void setForecastWeek(Integer forecastWeek){
		this.forecastWeek=forecastWeek;
	}

	public Integer getForecastWeek(){
		return this.forecastWeek;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setDetailId(Long detailId){
		this.detailId=detailId;
	}

	public Long getDetailId(){
		return this.detailId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setForecastId(Long forecastId){
		this.forecastId=forecastId;
	}

	public Long getForecastId(){
		return this.forecastId;
	}

}