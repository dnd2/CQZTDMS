/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-07-22 11:11:03
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsActivityRelationPO extends PO{

	private Long largessType;
	private String giftName;
	private Double gifeAmount;
	private Long projectId;
	private String remark;
	private Long activityId;
	private String projectCode;
	private Double projectAmount;
	private String projectName;

	public void setLargessType(Long largessType){
		this.largessType=largessType;
	}

	public Long getLargessType(){
		return this.largessType;
	}

	public void setGiftName(String giftName){
		this.giftName=giftName;
	}

	public String getGiftName(){
		return this.giftName;
	}

	public void setGifeAmount(Double gifeAmount){
		this.gifeAmount=gifeAmount;
	}

	public Double getGifeAmount(){
		return this.gifeAmount;
	}

	public void setProjectId(Long projectId){
		this.projectId=projectId;
	}

	public Long getProjectId(){
		return this.projectId;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setActivityId(Long activityId){
		this.activityId=activityId;
	}

	public Long getActivityId(){
		return this.activityId;
	}

	public void setProjectCode(String projectCode){
		this.projectCode=projectCode;
	}

	public String getProjectCode(){
		return this.projectCode;
	}

	public void setProjectAmount(Double projectAmount){
		this.projectAmount=projectAmount;
	}

	public Double getProjectAmount(){
		return this.projectAmount;
	}

	public void setProjectName(String projectName){
		this.projectName=projectName;
	}

	public String getProjectName(){
		return this.projectName;
	}

}