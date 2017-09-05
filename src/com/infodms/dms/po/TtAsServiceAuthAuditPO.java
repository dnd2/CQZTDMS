/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2017-08-18 17:15:13
* CreateBy   : liaoyu
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsServiceAuthAuditPO extends PO{

	private String approvalLevel;
	private Long authAuditId;
	private Long serviceOrderId;
	private Integer authAuditStatus;
	private Long createBy;
	private String authAuditRemark;
	private Long authAuditBy;
	private Date createDate;
	private Date authAuditDate;

	public void setApprovalLevel(String approvalLevel){
		this.approvalLevel=approvalLevel;
	}

	public String getApprovalLevel(){
		return this.approvalLevel;
	}

	public void setAuthAuditId(Long authAuditId){
		this.authAuditId=authAuditId;
	}

	public Long getAuthAuditId(){
		return this.authAuditId;
	}

	public void setServiceOrderId(Long serviceOrderId){
		this.serviceOrderId=serviceOrderId;
	}

	public Long getServiceOrderId(){
		return this.serviceOrderId;
	}

	public void setAuthAuditStatus(Integer authAuditStatus){
		this.authAuditStatus=authAuditStatus;
	}

	public Integer getAuthAuditStatus(){
		return this.authAuditStatus;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setAuthAuditRemark(String authAuditRemark){
		this.authAuditRemark=authAuditRemark;
	}

	public String getAuthAuditRemark(){
		return this.authAuditRemark;
	}

	public void setAuthAuditBy(Long authAuditBy){
		this.authAuditBy=authAuditBy;
	}

	public Long getAuthAuditBy(){
		return this.authAuditBy;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setAuthAuditDate(Date authAuditDate){
		this.authAuditDate=authAuditDate;
	}

	public Date getAuthAuditDate(){
		return this.authAuditDate;
	}

}