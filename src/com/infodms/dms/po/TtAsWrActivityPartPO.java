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
public class TtAsWrActivityPartPO extends PO{

	private Integer isMain;
	private Integer applyPartCount;
	private Long partMainId;
	private String partName;
	private Long partId;
	private Long id;
	private Long activityId;
	private String partCode;
	private String activityHoursCode;
	private Integer partUseType;

	public Integer getPartUseType() {
		return partUseType;
	}

	public void setPartUseType(Integer partUseType) {
		this.partUseType = partUseType;
	}

	public String getActivityHoursCode() {
		return activityHoursCode;
	}

	public void setActivityHoursCode(String activityHoursCode) {
		this.activityHoursCode = activityHoursCode;
	}

	public Integer getApplyPartCount() {
		return applyPartCount;
	}

	public void setApplyPartCount(Integer applyPartCount) {
		this.applyPartCount = applyPartCount;
	}

	

	public void setIsMain(Integer isMain){
		this.isMain=isMain;
	}

	public Integer getIsMain(){
		return this.isMain;
	}

	

	public void setPartMainId(Long partMainId){
		this.partMainId=partMainId;
	}

	public Long getPartMainId(){
		return this.partMainId;
	}

	public void setPartName(String partName){
		this.partName=partName;
	}

	public String getPartName(){
		return this.partName;
	}

	public void setPartId(Long partId){
		this.partId=partId;
	}

	public Long getPartId(){
		return this.partId;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setActivityId(Long activityId){
		this.activityId=activityId;
	}

	public Long getActivityId(){
		return this.activityId;
	}

	public void setPartCode(String partCode){
		this.partCode=partCode;
	}

	public String getPartCode(){
		return this.partCode;
	}

	

}