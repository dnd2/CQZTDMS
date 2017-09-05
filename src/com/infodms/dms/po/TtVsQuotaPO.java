/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-07-22 15:08:31
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsQuotaPO extends PO{

	private Long companyId;
	private Date updateDate;
	private Long quotaId;
	private Long createBy;
	private Long orgId;
	private Date createDate;
	private Long dealerId;
	private Integer quotaYear;
	private Integer quotaType;
	private Integer status;
	private Long updateBy;
	private Integer quotaMonth;
	private Integer quotaWeek;
	private Long areaId;

	public void setCompanyId(Long companyId){
		this.companyId=companyId;
	}

	public Long getCompanyId(){
		return this.companyId;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setQuotaId(Long quotaId){
		this.quotaId=quotaId;
	}

	public Long getQuotaId(){
		return this.quotaId;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setOrgId(Long orgId){
		this.orgId=orgId;
	}

	public Long getOrgId(){
		return this.orgId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setQuotaYear(Integer quotaYear){
		this.quotaYear=quotaYear;
	}

	public Integer getQuotaYear(){
		return this.quotaYear;
	}

	public void setQuotaType(Integer quotaType){
		this.quotaType=quotaType;
	}

	public Integer getQuotaType(){
		return this.quotaType;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setQuotaMonth(Integer quotaMonth){
		this.quotaMonth=quotaMonth;
	}

	public Integer getQuotaMonth(){
		return this.quotaMonth;
	}

	public void setQuotaWeek(Integer quotaWeek){
		this.quotaWeek=quotaWeek;
	}

	public Integer getQuotaWeek(){
		return this.quotaWeek;
	}

	public void setAreaId(Long areaId){
		this.areaId=areaId;
	}

	public Long getAreaId(){
		return this.areaId;
	}

}