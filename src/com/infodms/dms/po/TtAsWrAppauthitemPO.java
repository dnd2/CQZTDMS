/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-06-01 09:06:13
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrAppauthitemPO extends PO{

	private Date updateDate;
	private String approvalPerson;
	private Date approvalDate;
	private Long updateBy;
	private Long createBy;
	private Date createDate;
	private String approvalResult;
	private String authorizedCode;
	private String remark;
	private String approvalLevelCode;
	private Long id;

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setApprovalPerson(String approvalPerson){
		this.approvalPerson=approvalPerson;
	}

	public String getApprovalPerson(){
		return this.approvalPerson;
	}

	public void setApprovalDate(Date approvalDate){
		this.approvalDate=approvalDate;
	}

	public Date getApprovalDate(){
		return this.approvalDate;
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

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setApprovalResult(String approvalResult){
		this.approvalResult=approvalResult;
	}

	public String getApprovalResult(){
		return this.approvalResult;
	}

	public void setAuthorizedCode(String authorizedCode){
		this.authorizedCode=authorizedCode;
	}

	public String getAuthorizedCode(){
		return this.authorizedCode;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setApprovalLevelCode(String approvalLevelCode){
		this.approvalLevelCode=approvalLevelCode;
	}

	public String getApprovalLevelCode(){
		return this.approvalLevelCode;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

}