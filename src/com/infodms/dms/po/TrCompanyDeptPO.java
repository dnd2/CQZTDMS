/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2009-11-03 16:36:03
* CreateBy   : xianchao zhang
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TrCompanyDeptPO extends PO{

	private String companyId;
	private Date updateDate;
	private String deptId;
	private String companyDeptId;
	private String updateBy;
	private String createBy;
	private Date createDate;

	public void setCompanyId(String companyId){
		this.companyId=companyId;
	}

	public String getCompanyId(){
		return this.companyId;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setDeptId(String deptId){
		this.deptId=deptId;
	}

	public String getDeptId(){
		return this.deptId;
	}

	public void setCompanyDeptId(String companyDeptId){
		this.companyDeptId=companyDeptId;
	}

	public String getCompanyDeptId(){
		return this.companyDeptId;
	}

	public void setUpdateBy(String updateBy){
		this.updateBy=updateBy;
	}

	public String getUpdateBy(){
		return this.updateBy;
	}

	public void setCreateBy(String createBy){
		this.createBy=createBy;
	}

	public String getCreateBy(){
		return this.createBy;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

}