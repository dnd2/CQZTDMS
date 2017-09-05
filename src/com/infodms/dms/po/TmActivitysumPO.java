/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-02-26 03:33:10
* CreateBy   : tianheng
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmActivitysumPO extends PO{

	private Integer accountingUnit;
	private String gradeComment;
	private Date updateDate;
	private Long createBy;
	private Long orgId;
	private String customerDesc;
	private String financeComment;
	private Date createDate;
	private Long sumId;
	private Double auditAmount;
	private String approvalTitle;
	private String approvalRemark;
	private Integer status;
	private Long chargeId;
	private Integer grade;
	private Long updateBy;
	private Long planId;
	private String approvalCode;
	private String brandComment;
	private Integer payTime;
	private Integer accountingAccount;

	public void setAccountingUnit(Integer accountingUnit){
		this.accountingUnit=accountingUnit;
	}

	public Integer getAccountingUnit(){
		return this.accountingUnit;
	}

	public void setGradeComment(String gradeComment){
		this.gradeComment=gradeComment;
	}

	public String getGradeComment(){
		return this.gradeComment;
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

	public void setCustomerDesc(String customerDesc){
		this.customerDesc=customerDesc;
	}

	public String getCustomerDesc(){
		return this.customerDesc;
	}

	public void setFinanceComment(String financeComment){
		this.financeComment=financeComment;
	}

	public String getFinanceComment(){
		return this.financeComment;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setSumId(Long sumId){
		this.sumId=sumId;
	}

	public Long getSumId(){
		return this.sumId;
	}

	public void setAuditAmount(Double auditAmount){
		this.auditAmount=auditAmount;
	}

	public Double getAuditAmount(){
		return this.auditAmount;
	}

	public void setApprovalTitle(String approvalTitle){
		this.approvalTitle=approvalTitle;
	}

	public String getApprovalTitle(){
		return this.approvalTitle;
	}

	public void setApprovalRemark(String approvalRemark){
		this.approvalRemark=approvalRemark;
	}

	public String getApprovalRemark(){
		return this.approvalRemark;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setChargeId(Long chargeId){
		this.chargeId=chargeId;
	}

	public Long getChargeId(){
		return this.chargeId;
	}

	public void setGrade(Integer grade){
		this.grade=grade;
	}

	public Integer getGrade(){
		return this.grade;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setPlanId(Long planId){
		this.planId=planId;
	}

	public Long getPlanId(){
		return this.planId;
	}

	public void setApprovalCode(String approvalCode){
		this.approvalCode=approvalCode;
	}

	public String getApprovalCode(){
		return this.approvalCode;
	}

	public void setBrandComment(String brandComment){
		this.brandComment=brandComment;
	}

	public String getBrandComment(){
		return this.brandComment;
	}

	public void setPayTime(Integer payTime){
		this.payTime=payTime;
	}

	public Integer getPayTime(){
		return this.payTime;
	}

	public void setAccountingAccount(Integer accountingAccount){
		this.accountingAccount=accountingAccount;
	}

	public Integer getAccountingAccount(){
		return this.accountingAccount;
	}

}