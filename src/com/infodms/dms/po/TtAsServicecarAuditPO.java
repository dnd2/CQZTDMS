/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-12-09 16:39:26
* CreateBy   : nova
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsServicecarAuditPO extends PO{

	private Long id;
	private Long createBy;
	private String remark;
	private Date createDate;
	private Long auditMan;
	private Integer auditStatus;
	private Long applyId;

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

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setAuditMan(Long auditMan){
		this.auditMan=auditMan;
	}

	public Long getAuditMan(){
		return this.auditMan;
	}

	public void setAuditStatus(Integer auditStatus){
		this.auditStatus=auditStatus;
	}

	public Integer getAuditStatus(){
		return this.auditStatus;
	}

	public void setApplyId(Long applyId){
		this.applyId=applyId;
	}

	public Long getApplyId(){
		return this.applyId;
	}

}