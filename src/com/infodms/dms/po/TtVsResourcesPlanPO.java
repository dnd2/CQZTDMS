/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-09-02 16:17:53
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.math.BigDecimal;
import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsResourcesPlanPO extends PO{

	private Long planId;
	private Long companyId;
	private Date updateDate;
	private Integer planYear;
	private Integer planVer;
	private Long createBy;
	private Long areaId;
	private String planDesc;
	private Integer status;
	private Long updateBy;
	private BigDecimal ver;
	private Date createDate;
	private Integer planWeek;
	private Integer planMonth;

	public void setPlanId(Long planId){
		this.planId=planId;
	}

	public Long getPlanId(){
		return this.planId;
	}

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

	public void setPlanYear(Integer planYear){
		this.planYear=planYear;
	}

	public Integer getPlanYear(){
		return this.planYear;
	}

	public void setPlanVer(Integer planVer){
		this.planVer=planVer;
	}

	public Integer getPlanVer(){
		return this.planVer;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setAreaId(Long areaId){
		this.areaId=areaId;
	}

	public Long getAreaId(){
		return this.areaId;
	}

	public void setPlanDesc(String planDesc){
		this.planDesc=planDesc;
	}

	public String getPlanDesc(){
		return this.planDesc;
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

	public void setVer(BigDecimal ver){
		this.ver=ver;
	}

	public BigDecimal getVer(){
		return this.ver;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setPlanWeek(Integer planWeek){
		this.planWeek=planWeek;
	}

	public Integer getPlanWeek(){
		return this.planWeek;
	}

	public void setPlanMonth(Integer planMonth){
		this.planMonth=planMonth;
	}

	public Integer getPlanMonth(){
		return this.planMonth;
	}

}