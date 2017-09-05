/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2017-07-24 11:06:34
* CreateBy   : chenzheng
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrActivityHoursPO extends PO{

	private String hoursName;
	private String hoursCode;
	private Long id;
	private Float applyHoursCount;
	private Long activityId;
	private Long hoursId;

	public void setHoursName(String hoursName){
		this.hoursName=hoursName;
	}

	public String getHoursName(){
		return this.hoursName;
	}

	public void setHoursCode(String hoursCode){
		this.hoursCode=hoursCode;
	}

	public String getHoursCode(){
		return this.hoursCode;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setApplyHoursCount(Float applyHoursCount){
		this.applyHoursCount=applyHoursCount;
	}

	public Float getApplyHoursCount(){
		return this.applyHoursCount;
	}

	public void setActivityId(Long activityId){
		this.activityId=activityId;
	}

	public Long getActivityId(){
		return this.activityId;
	}

	public void setHoursId(Long hoursId){
		this.hoursId=hoursId;
	}

	public Long getHoursId(){
		return this.hoursId;
	}

}