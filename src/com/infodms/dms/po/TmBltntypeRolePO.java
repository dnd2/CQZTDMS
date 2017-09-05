/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2009-12-07 17:01:47
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmBltntypeRolePO extends PO{

	private Long typeId;
	private Long createBy;
	private Long employeeId;
	private Date createDate;

	public void setTypeId(Long typeId){
		this.typeId=typeId;
	}

	public Long getTypeId(){
		return this.typeId;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setEmployeeId(Long employeeId){
		this.employeeId=employeeId;
	}

	public Long getEmployeeId(){
		return this.employeeId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

}