/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2015-03-23 10:40:35
* CreateBy   : yuewei
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.yxdms.entity.warranty;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrWarrantyManualPO extends PO{

	private Date sendDate;
	private Long dealerId;
	private Long sendBy;
	private Long auditBy;
	private String sendNo;
	private Long createBy;
	private String dealerContactPerson;
	private String dealerContactPhone;
	private Integer status;
	private Date auditDate;
	private Long id;
	private String auditRemark;
	private Date createDate;
	private Date reportDate;
	private String remark;
	private String reportNo;
	private Integer isSend;

	public void setSendDate(Date sendDate){
		this.sendDate=sendDate;
	}

	public Date getSendDate(){
		return this.sendDate;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setSendBy(Long sendBy){
		this.sendBy=sendBy;
	}

	public Long getSendBy(){
		return this.sendBy;
	}

	public void setAuditBy(Long auditBy){
		this.auditBy=auditBy;
	}

	public Long getAuditBy(){
		return this.auditBy;
	}

	public void setSendNo(String sendNo){
		this.sendNo=sendNo;
	}

	public String getSendNo(){
		return this.sendNo;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setDealerContactPerson(String dealerContactPerson){
		this.dealerContactPerson=dealerContactPerson;
	}

	public String getDealerContactPerson(){
		return this.dealerContactPerson;
	}

	public void setDealerContactPhone(String dealerContactPhone){
		this.dealerContactPhone=dealerContactPhone;
	}

	public String getDealerContactPhone(){
		return this.dealerContactPhone;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
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

	public void setReportDate(Date reportDate){
		this.reportDate=reportDate;
	}

	public Date getReportDate(){
		return this.reportDate;
	}

	public void setReportNo(String reportNo){
		this.reportNo=reportNo;
	}

	public String getReportNo(){
		return this.reportNo;
	}

	public Integer getIsSend() {
		return isSend;
	}

	public void setIsSend(Integer isSend) {
		this.isSend = isSend;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}