/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-06-28 18:28:13
* CreateBy   : 
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtDealerActualSalesLogPO extends PO{

	private Long logId;
	private Date logDate;
	private Long orderId;
	private Long userId;
	private Long updateBy;
	private Date updateDate;
	private Long createBy;
	private String remark;
	private Date createDate;
	private Integer logStatus;
	private Long orgId;
	private String userName;
	private Long logType;
	private Long auditId;

	public Long getAuditId() {
		return auditId;
	}

	public void setAuditId(Long auditId) {
		this.auditId = auditId;
	}

	public void setLogId(Long logId){
		this.logId=logId;
	}

	public Long getLogId(){
		return this.logId;
	}

	public void setUserName(String userName){
		this.userName=userName;
	}

	public String getUserName(){
		return this.userName;
	}
	
	public void setLogType(Long logType){
		this.logType=logType;
	}

	public Long getLogType(){
		return this.logType;
	}
	
	public void setLogDate(Date logDate){
		this.logDate=logDate;
	}

	public Date getLogDate(){
		return this.logDate;
	}

	public void setOrderId(Long orderId){
		this.orderId=orderId;
	}

	public Long getOrderId(){
		return this.orderId;
	}

	public void setUserId(Long userId){
		this.userId=userId;
	}

	public Long getUserId(){
		return this.userId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
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

	public void setLogStatus(Integer logStatus){
		this.logStatus=logStatus;
	}

	public Integer getLogStatus(){
		return this.logStatus;
	}

	public void setOrgId(Long orgId){
		this.orgId=orgId;
	}

	public Long getOrgId(){
		return this.orgId;
	}

}