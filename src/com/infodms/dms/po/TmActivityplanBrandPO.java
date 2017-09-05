/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-02-26 03:33:10
* CreateBy   : tianheng
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmActivityplanBrandPO extends PO{

	private Long brandId;
	private Long planId;
	private Integer brandType;
	private Long activityBrandId;

	public void setBrandId(Long brandId){
		this.brandId=brandId;
	}

	public Long getBrandId(){
		return this.brandId;
	}

	public void setPlanId(Long planId){
		this.planId=planId;
	}

	public Long getPlanId(){
		return this.planId;
	}

	public void setBrandType(Integer brandType){
		this.brandType=brandType;
	}

	public Integer getBrandType(){
		return this.brandType;
	}

	public void setActivityBrandId(Long activityBrandId){
		this.activityBrandId=activityBrandId;
	}

	public Long getActivityBrandId(){
		return this.activityBrandId;
	}

}