/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-07-16 09:28:08
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrSpeoutfeeAuditingPO extends PO{

	private String auditingOpinion;
	private Long presonDept;
	private Long auditingPerson;
	private Date auditingDate;
	private Date updateDate;
	private Integer status;
	private Long updateBy;
	private Long createBy;
	private Date createDate;
	private Long feeId;
	private Long id;

	public void setAuditingOpinion(String auditingOpinion){
		this.auditingOpinion=auditingOpinion;
	}

	public String getAuditingOpinion(){
		return this.auditingOpinion;
	}

	public void setPresonDept(Long presonDept){
		this.presonDept=presonDept;
	}

	public Long getPresonDept(){
		return this.presonDept;
	}

	public void setAuditingPerson(Long auditingPerson){
		this.auditingPerson=auditingPerson;
	}

	public Long getAuditingPerson(){
		return this.auditingPerson;
	}

	public void setAuditingDate(Date auditingDate){
		this.auditingDate=auditingDate;
	}

	public Date getAuditingDate(){
		return this.auditingDate;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
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

	public void setFeeId(Long feeId){
		this.feeId=feeId;
	}

	public Long getFeeId(){
		return this.feeId;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

}