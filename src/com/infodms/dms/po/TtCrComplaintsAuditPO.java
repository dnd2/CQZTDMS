/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-06-01 09:22:40
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtCrComplaintsAuditPO extends PO{

	private Integer auditResult;
	private Date updateDate;
	private Long createBy;
	private Long orgId;
	private String partCode;
	private Date createDate;
	private Date auditDate;
	private Long dealerId;
	private Long compId;
	private String assignContent;
	private String auditContent;
	private Integer auditStatus;
	private String auditAction;
	private Long updateBy;
	private Integer intStatus;
	private String supplier;
	private Long id;
	private Integer ifStatus;

	public void setAuditResult(Integer auditResult){
		this.auditResult=auditResult;
	}

	public Integer getAuditResult(){
		return this.auditResult;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setOrgId(Long orgId){
		this.orgId=orgId;
	}

	public Long getOrgId(){
		return this.orgId;
	}

	public void setPartCode(String partCode){
		this.partCode=partCode;
	}

	public String getPartCode(){
		return this.partCode;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setAuditDate(Date auditDate){
		this.auditDate=auditDate;
	}

	public Date getAuditDate(){
		return this.auditDate;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setCompId(Long compId){
		this.compId=compId;
	}

	public Long getCompId(){
		return this.compId;
	}

	public void setAssignContent(String assignContent){
		this.assignContent=assignContent;
	}

	public String getAssignContent(){
		return this.assignContent;
	}

	public void setAuditContent(String auditContent){
		this.auditContent=auditContent;
	}

	public String getAuditContent(){
		return this.auditContent;
	}

	public void setAuditStatus(Integer auditStatus){
		this.auditStatus=auditStatus;
	}

	public Integer getAuditStatus(){
		return this.auditStatus;
	}

	public void setAuditAction(String auditAction){
		this.auditAction=auditAction;
	}

	public String getAuditAction(){
		return this.auditAction;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setIntStatus(Integer intStatus){
		this.intStatus=intStatus;
	}

	public Integer getIntStatus(){
		return this.intStatus;
	}

	public void setSupplier(String supplier){
		this.supplier=supplier;
	}

	public String getSupplier(){
		return this.supplier;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public Integer getIfStatus() {
		return ifStatus;
	}

	public void setIfStatus(Integer ifStatus) {
		this.ifStatus = ifStatus;
	}

}