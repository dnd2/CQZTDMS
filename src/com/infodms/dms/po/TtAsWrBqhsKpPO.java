/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2015-03-23 10:28:57
* CreateBy   : yuewei
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrBqhsKpPO extends PO{

	private String invoiceBatchNo;
	private String invoiceNo;
	private String approvalRemarks;
	private Date endDate;
	private Long approvalBy;
	private Long createBy;
	private Integer status;
	private Date startDate;
	private String gunit;
	private Date approvalDate;
	private String broNo;
	private Long id;
	private Date createDate;
	private String xunit;

	public void setInvoiceBatchNo(String invoiceBatchNo){
		this.invoiceBatchNo=invoiceBatchNo;
	}

	public String getInvoiceBatchNo(){
		return this.invoiceBatchNo;
	}

	public void setInvoiceNo(String invoiceNo){
		this.invoiceNo=invoiceNo;
	}

	public String getInvoiceNo(){
		return this.invoiceNo;
	}

	public void setApprovalRemarks(String approvalRemarks){
		this.approvalRemarks=approvalRemarks;
	}

	public String getApprovalRemarks(){
		return this.approvalRemarks;
	}

	public void setEndDate(Date endDate){
		this.endDate=endDate;
	}

	public Date getEndDate(){
		return this.endDate;
	}

	public void setApprovalBy(Long approvalBy){
		this.approvalBy=approvalBy;
	}

	public Long getApprovalBy(){
		return this.approvalBy;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setStartDate(Date startDate){
		this.startDate=startDate;
	}

	public Date getStartDate(){
		return this.startDate;
	}

	public void setGunit(String gunit){
		this.gunit=gunit;
	}

	public String getGunit(){
		return this.gunit;
	}

	public void setApprovalDate(Date approvalDate){
		this.approvalDate=approvalDate;
	}

	public Date getApprovalDate(){
		return this.approvalDate;
	}

	public void setBroNo(String broNo){
		this.broNo=broNo;
	}

	public String getBroNo(){
		return this.broNo;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setXunit(String xunit){
		this.xunit=xunit;
	}

	public String getXunit(){
		return this.xunit;
	}

}