/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-06-09 16:35:00
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrAuthmonitorpartPO extends PO{

	private String partName;
	private Long modelGroup;
	private Date updateDate;
	private Long updateBy;
	private Long createBy;
	private String partCode;
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

	public void setPartName(String partName){
		this.partName=partName;
	}

	public String getPartName(){
		return this.partName;
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