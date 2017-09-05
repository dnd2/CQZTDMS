/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-04-15 14:23:21
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtSalesFinInoutAuditPO extends PO{

	private Long auditPer;
	private Long updateBy;
	private Date updateDate;
	private Date auditDate;
	private Long id;
	private Long createBy;
	private String remark;
	private Long auditId;
	private Date createDate;
	private Long auditStatus;

	public void setAuditPer(Long auditPer){
		this.auditPer=auditPer;
	}

	public Long getAuditPer(){
		return this.auditPer;
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

	public void setAuditId(Long auditId){
		this.auditId=auditId;
	}

	public Long getAuditId(){
		return this.auditId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setAuditStatus(Long auditStatus){
		this.auditStatus=auditStatus;
	}

	public Long getAuditStatus(){
		return this.auditStatus;
	}

}