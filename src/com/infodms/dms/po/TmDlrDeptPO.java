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
public class TmDlrDeptPO extends PO{

	private String deptName;
	private String deptStat;
	private Date updateDate;
	private String deptCode;
	private String updateBy;
	private String createBy;
	private String parDlrDeptId;
	private String dlrDeptId;
	private Date createDate;
	private String dlrId;

	public void setDeptName(String deptName){
		this.deptName=deptName;
	}

	public String getDeptName(){
		return this.deptName;
	}

	public void setDeptStat(String deptStat){
		this.deptStat=deptStat;
	}

	public String getDeptStat(){
		return this.deptStat;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setDeptCode(String deptCode){
		this.deptCode=deptCode;
	}

	public String getDeptCode(){
		return this.deptCode;
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

	public void setParDlrDeptId(String parDlrDeptId){
		this.parDlrDeptId=parDlrDeptId;
	}

	public String getParDlrDeptId(){
		return this.parDlrDeptId;
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

	public void setDlrId(String dlrId){
		this.dlrId=dlrId;
	}

	public String getDlrId(){
		return this.dlrId;
	}

}