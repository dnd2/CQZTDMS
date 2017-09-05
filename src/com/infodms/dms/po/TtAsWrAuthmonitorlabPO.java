/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-06-18 16:32:35
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrAuthmonitorlabPO extends PO{

	private Long modelGroup;
	private Date updateDate;
	private String labourOperationName;
	private String labourOperationNo;
	private Long updateBy;
	private Long createBy;
	private Date createDate;
	private String approvalLevel;
	private Integer isDel;
	private Long id;
	private Long oemCompanyId;

	public Long getOemCompanyId() {
		return oemCompanyId;
	}

	public void setOemCompanyId(Long oemCompanyId) {
		this.oemCompanyId = oemCompanyId;
	}

	public void setModelGroup(Long modelGroup){
		this.modelGroup=modelGroup;
	}

	public Long getModelGroup(){
		return this.modelGroup;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setLabourOperationName(String labourOperationName){
		this.labourOperationName=labourOperationName;
	}

	public String getLabourOperationName(){
		return this.labourOperationName;
	}

	public void setLabourOperationNo(String labourOperationNo){
		this.labourOperationNo=labourOperationNo;
	}

	public String getLabourOperationNo(){
		return this.labourOperationNo;
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

	public void setApprovalLevel(String approvalLevel){
		this.approvalLevel=approvalLevel;
	}

	public String getApprovalLevel(){
		return this.approvalLevel;
	}

	public void setIsDel(Integer isDel){
		this.isDel=isDel;
	}

	public Integer getIsDel(){
		return this.isDel;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

}