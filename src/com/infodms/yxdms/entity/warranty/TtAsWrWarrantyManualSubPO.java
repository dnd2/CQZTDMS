/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2015-03-23 11:00:10
* CreateBy   : yuewei
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.yxdms.entity.warranty;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrWarrantyManualSubPO extends PO{

	private Integer payType;
	private String vin;
	private Date auditDate;
	private Long auditBy;
	private Long parentId;
	private Integer operation;
	private Long id;
	private String reportRemark;
	private Long partId;

	public void setPayType(Integer payType){
		this.payType=payType;
	}

	public Integer getPayType(){
		return this.payType;
	}

	public void setVin(String vin){
		this.vin=vin;
	}

	public String getVin(){
		return this.vin;
	}

	public void setAuditDate(Date auditDate){
		this.auditDate=auditDate;
	}

	public Date getAuditDate(){
		return this.auditDate;
	}

	public void setAuditBy(Long auditBy){
		this.auditBy=auditBy;
	}

	public Long getAuditBy(){
		return this.auditBy;
	}

	public void setOperation(Integer operation){
		this.operation=operation;
	}

	public Integer getOperation(){
		return this.operation;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setReportRemark(String reportRemark){
		this.reportRemark=reportRemark;
	}

	public String getReportRemark(){
		return this.reportRemark;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Long getPartId() {
		return partId;
	}

	public void setPartId(Long partId) {
		this.partId = partId;
	}

}