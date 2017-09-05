/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-09-15 18:25:31
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsActualSalesReturnPO extends PO{

	private Long dealerId;
	private String chkRemark;
	private Date updateDate;
	private Long createBy;
	private String returnReason;
	private Integer status;
	private Date reqDate;
	private Long returnId;
	private Long orderId;
	private Long oemCompanyId;
	private Date chkDate;
	private Long updateBy;
	private Date createDate;
	private Long returnType;
	private Date actualSalesDate;
	
	private Long auditStatus;
	
	public Long getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(Long auditStatus) {
		this.auditStatus = auditStatus;
	}

	public Long getReturnType() {
		return returnType;
	}

	public void setReturnType(Long returnType) {
		this.returnType = returnType;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setChkRemark(String chkRemark){
		this.chkRemark=chkRemark;
	}

	public String getChkRemark(){
		return this.chkRemark;
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

	public void setReturnReason(String returnReason){
		this.returnReason=returnReason;
	}

	public String getReturnReason(){
		return this.returnReason;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setReqDate(Date reqDate){
		this.reqDate=reqDate;
	}

	public Date getReqDate(){
		return this.reqDate;
	}

	public void setReturnId(Long returnId){
		this.returnId=returnId;
	}

	public Long getReturnId(){
		return this.returnId;
	}

	public void setOrderId(Long orderId){
		this.orderId=orderId;
	}

	public Long getOrderId(){
		return this.orderId;
	}

	public void setOemCompanyId(Long oemCompanyId){
		this.oemCompanyId=oemCompanyId;
	}

	public Long getOemCompanyId(){
		return this.oemCompanyId;
	}

	public void setChkDate(Date chkDate){
		this.chkDate=chkDate;
	}

	public Date getChkDate(){
		return this.chkDate;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public Date getActualSalesDate() {
		return actualSalesDate;
	}

	public void setActualSalesDate(Date actualSalesDate) {
		this.actualSalesDate = actualSalesDate;
	}

}