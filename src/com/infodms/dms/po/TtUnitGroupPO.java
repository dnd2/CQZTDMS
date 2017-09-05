/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2011-09-08 11:15:56
* CreateBy   : fengalon
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtUnitGroupPO extends PO{

	private Long groupId;
	private Long updateBy;
	private Date updateDate;
	private String groupName;
	private Long createBy;
	private Integer groupStatus;
	private Date createDate;
	private Integer groupArea;
	private Integer groupType;

	public void setGroupId(Long groupId){
		this.groupId=groupId;
	}

	public Long getGroupId(){
		return this.groupId;
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

	public void setGroupName(String groupName){
		this.groupName=groupName;
	}

	public String getGroupName(){
		return this.groupName;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setGroupStatus(Integer groupStatus){
		this.groupStatus=groupStatus;
	}

	public Integer getGroupStatus(){
		return this.groupStatus;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setGroupArea(Integer groupArea){
		this.groupArea=groupArea;
	}

	public Integer getGroupArea(){
		return this.groupArea;
	}

	public void setGroupType(Integer groupType){
		this.groupType=groupType;
	}

	public Integer getGroupType(){
		return this.groupType;
	}

}