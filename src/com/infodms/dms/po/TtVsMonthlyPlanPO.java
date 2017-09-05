/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-08-19 14:15:15
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsMonthlyPlanPO extends PO{

	private Long companyId;
	private Date updateDate;
	private Long createBy;
	private Long orgId;
	private Integer ver;
	private String planDesc;
	private Date createDate;
	private Integer planMonth;
	private Integer planVer;
	private Long dealerId;
	private Integer planType;
	private Integer status;
	private Long updateBy;
	private Integer planYear;
	private Long planId;
	private Integer isFlag;
	private Long areaId;
	private Integer orgType;

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

	public void setVer(Integer ver){
		this.ver=ver;
	}

	public Integer getVer(){
		return this.ver;
	}

	public void setPlanDesc(String planDesc){
		this.planDesc=planDesc;
	}

	public String getPlanDesc(){
		return this.planDesc;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setPlanMonth(Integer planMonth){
		this.planMonth=planMonth;
	}

	public Integer getPlanMonth(){
		return this.planMonth;
	}

	public void setPlanVer(Integer planVer){
		this.planVer=planVer;
	}

	public Integer getPlanVer(){
		return this.planVer;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setPlanType(Integer planType){
		this.planType=planType;
	}

	public Integer getPlanType(){
		return this.planType;
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

	public void setPlanYear(Integer planYear){
		this.planYear=planYear;
	}

	public Integer getPlanYear(){
		return this.planYear;
	}

	public void setPlanId(Long planId){
		this.planId=planId;
	}

	public Long getPlanId(){
		return this.planId;
	}

	public void setIsFlag(Integer isFlag){
		this.isFlag=isFlag;
	}

	public Integer getIsFlag(){
		return this.isFlag;
	}

	public void setAreaId(Long areaId){
		this.areaId=areaId;
	}

	public Long getAreaId(){
		return this.areaId;
	}

	public void setOrgType(Integer orgType){
		this.orgType=orgType;
	}

	public Integer getOrgType(){
		return this.orgType;
	}

}