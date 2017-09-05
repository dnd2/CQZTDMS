/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2009-11-03 16:36:02
* CreateBy   : xianchao zhang
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmDlrDeptEmpPO extends PO{

	private String deptEmpId;
	private String empStat;
	private Date updateDate;
	private String empName;
	private String updateBy;
	private String createBy;
	private String dlrDeptId;
	private Date createDate;

	public void setDeptEmpId(String deptEmpId){
		this.deptEmpId=deptEmpId;
	}

	public String getDeptEmpId(){
		return this.deptEmpId;
	}

	public void setEmpStat(String empStat){
		this.empStat=empStat;
	}

	public String getEmpStat(){
		return this.empStat;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setEmpName(String empName){
		this.empName=empName;
	}

	public String getEmpName(){
		return this.empName;
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

	public void setDlrDeptId(String dlrDeptId){
		this.dlrDeptId=dlrDeptId;
	}

	public String getDlrDeptId(){
		return this.dlrDeptId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

}