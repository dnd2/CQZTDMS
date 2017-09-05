/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2011-09-08 12:20:43
* CreateBy   : fengalon
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtUnitGroupDtlPO extends PO{

	private Long theId;
	private Long groupId;
	private Long groupDtlId;
	private Long updateBy;
	private String theName;
	private Date updateDate;
	private Long createBy;
	private Date createDate;
	private String theCode;

	public void setTheId(Long theId){
		this.theId=theId;
	}

	public Long getTheId(){
		return this.theId;
	}

	public void setGroupId(Long groupId){
		this.groupId=groupId;
	}

	public Long getGroupId(){
		return this.groupId;
	}

	public void setGroupDtlId(Long groupDtlId){
		this.groupDtlId=groupDtlId;
	}

	public Long getGroupDtlId(){
		return this.groupDtlId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setTheName(String theName){
		this.theName=theName;
	}

	public String getTheName(){
		return this.theName;
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

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setTheCode(String theCode){
		this.theCode=theCode;
	}

	public String getTheCode(){
		return this.theCode;
	}

}