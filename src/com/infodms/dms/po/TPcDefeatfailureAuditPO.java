/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-12-01 09:47:25
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TPcDefeatfailureAuditPO extends PO{

	private String auditRemark;
	private String beforeAudit;
	private Date updateDate;
	private Long status;
	private Long updateBy;
	private Long managerAuditId;
	private Long createBy;
	private Long defeatfailureId;
	private Date createDate;
	private String auditType;
	private String afterAudit;

	public void setAuditRemark(String auditRemark){
		this.auditRemark=auditRemark;
	}

	public String getAuditRemark(){
		return this.auditRemark;
	}

	public void setBeforeAudit(String beforeAudit){
		this.beforeAudit=beforeAudit;
	}

	public String getBeforeAudit(){
		return this.beforeAudit;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setStatus(Long status){
		this.status=status;
	}

	public Long getStatus(){
		return this.status;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setManagerAuditId(Long managerAuditId){
		this.managerAuditId=managerAuditId;
	}

	public Long getManagerAuditId(){
		return this.managerAuditId;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setDefeatfailureId(Long defeatfailureId){
		this.defeatfailureId=defeatfailureId;
	}

	public Long getDefeatfailureId(){
		return this.defeatfailureId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setAuditType(String auditType){
		this.auditType=auditType;
	}

	public String getAuditType(){
		return this.auditType;
	}

	public void setAfterAudit(String afterAudit){
		this.afterAudit=afterAudit;
	}

	public String getAfterAudit(){
		return this.afterAudit;
	}

}