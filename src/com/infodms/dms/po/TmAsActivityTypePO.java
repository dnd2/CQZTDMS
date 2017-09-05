/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-06-01 10:40:05
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmAsActivityTypePO extends PO{

	private Long companyId;
	private String activityTypeCode;
	private Date updateDate;
	private String activityTypeName;
	private Long activityTypeId;
	private Long updateBy;
	private Long createBy;
	private String activityTypeRemark;
	private Date createDate;

	public void setCompanyId(Long companyId){
		this.companyId=companyId;
	}

	public Long getCompanyId(){
		return this.companyId;
	}

	public void setActivityTypeCode(String activityTypeCode){
		this.activityTypeCode=activityTypeCode;
	}

	public String getActivityTypeCode(){
		return this.activityTypeCode;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setActivityTypeName(String activityTypeName){
		this.activityTypeName=activityTypeName;
	}

	public String getActivityTypeName(){
		return this.activityTypeName;
	}

	public void setActivityTypeId(Long activityTypeId){
		this.activityTypeId=activityTypeId;
	}

	public Long getActivityTypeId(){
		return this.activityTypeId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setActivityTypeRemark(String activityTypeRemark){
		this.activityTypeRemark=activityTypeRemark;
	}

	public String getActivityTypeRemark(){
		return this.activityTypeRemark;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

}