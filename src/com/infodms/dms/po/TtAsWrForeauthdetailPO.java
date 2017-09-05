/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-04-15 11:37:51
* CreateBy   : KFQ
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrForeauthdetailPO extends PO{

	private Long fid;
	private Long updateBy;
	private String auditLevelNmae;
	private Date updateDate;
	private Date auditDate;
	private Long id;
	private Long createBy;
	private String remark;
	private String approvalLevelCode;
	private Date createDate;
	private String auditResult;
	private String auditPerson;

	public void setFid(Long fid){
		this.fid=fid;
	}

	public Long getFid(){
		return this.fid;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setAuditLevelNmae(String auditLevelNmae){
		this.auditLevelNmae=auditLevelNmae;
	}

	public String getAuditLevelNmae(){
		return this.auditLevelNmae;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setAuditDate(Date auditDate){
		this.auditDate=auditDate;
	}

	public Date getAuditDate(){
		return this.auditDate;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
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

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setAuditResult(String auditResult){
		this.auditResult=auditResult;
	}

	public String getAuditResult(){
		return this.auditResult;
	}

	public void setAuditPerson(String auditPerson){
		this.auditPerson=auditPerson;
	}

	public String getAuditPerson(){
		return this.auditPerson;
	}

}