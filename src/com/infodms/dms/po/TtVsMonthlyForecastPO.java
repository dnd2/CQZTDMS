/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-04-25 10:47:46
* CreateBy   : ASUS
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsMonthlyForecastPO extends PO{

	private Long dealerId;
	private Long updateBy;
	private Integer forecastYear;
	private Integer orgType;
	private Date updateDate;
	private Integer forecastMonth;
	private Long createBy;
	private Date createDate;
	private Long areaId;
	private Integer status;
	private Long forecastId;
	private Long orgId;
	private Long companyId;
	private Integer subStatus; //预测小状态 YH 2011.6.29
	private Integer forecastType; //zxf add 2017.7.8

	public Integer getSubStatus() {
		return subStatus;
	}

	public void setSubStatus(Integer subStatus) {
		this.subStatus = subStatus;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setForecastYear(Integer forecastYear){
		this.forecastYear=forecastYear;
	}

	public Integer getForecastYear(){
		return this.forecastYear;
	}

	public void setOrgType(Integer orgType){
		this.orgType=orgType;
	}

	public Integer getOrgType(){
		return this.orgType;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setForecastMonth(Integer forecastMonth){
		this.forecastMonth=forecastMonth;
	}

	public Integer getForecastMonth(){
		return this.forecastMonth;
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

	public void setAreaId(Long areaId){
		this.areaId=areaId;
	}

	public Long getAreaId(){
		return this.areaId;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setForecastId(Long forecastId){
		this.forecastId=forecastId;
	}

	public Long getForecastId(){
		return this.forecastId;
	}

	public void setOrgId(Long orgId){
		this.orgId=orgId;
	}

	public Long getOrgId(){
		return this.orgId;
	}
	
	public Integer getForecastType(){
		return forecastType;
	}
	
	public void setForecastType(Integer forecastType){
		this.forecastType = forecastType;
	}
}