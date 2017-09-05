/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-04-10 17:18:17
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtSalesFinInoutPO extends PO{

	private Long auditPer;
	private Long inAccId;
	private Date updateDate;
	private String orderNo;
	private String remark;
	private Long createBy;
	private Double amount;
	private Long status;
	private Long outAccId;
	private String evidenceNo;
	private Long updateBy;
	private Date auditDate;
	private Long id;
	private String auditRemark;
	private Date createDate;

	public void setAuditPer(Long auditPer){
		this.auditPer=auditPer;
	}

	public Long getAuditPer(){
		return this.auditPer;
	}

	public void setInAccId(Long inAccId){
		this.inAccId=inAccId;
	}

	public Long getInAccId(){
		return this.inAccId;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setOrderNo(String orderNo){
		this.orderNo=orderNo;
	}

	public String getOrderNo(){
		return this.orderNo;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setAmount(Double amount){
		this.amount=amount;
	}

	public Double getAmount(){
		return this.amount;
	}

	public void setStatus(Long status){
		this.status=status;
	}

	public Long getStatus(){
		return this.status;
	}

	public void setOutAccId(Long outAccId){
		this.outAccId=outAccId;
	}

	public Long getOutAccId(){
		return this.outAccId;
	}

	public void setEvidenceNo(String evidenceNo){
		this.evidenceNo=evidenceNo;
	}

	public String getEvidenceNo(){
		return this.evidenceNo;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
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

	public void setAuditRemark(String auditRemark){
		this.auditRemark=auditRemark;
	}

	public String getAuditRemark(){
		return this.auditRemark;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

}