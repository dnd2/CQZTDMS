/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-08-28 16:48:23
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrRulemappingPO extends PO{

	private String type;
	private Long ruleElement;
	private Date updateDate;
	private Long updateBy;
	private Long createBy;
	private Long warrantyGroup;
	private Long oemCompanyId;
	private String priorLevel;
	private Date createDate;
	private String role;

	public void setType(String type){
		this.type=type;
	}

	public String getType(){
		return this.type;
	}

	public void setRuleElement(Long ruleElement){
		this.ruleElement=ruleElement;
	}

	public Long getRuleElement(){
		return this.ruleElement;
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

	public void setWarrantyGroup(Long warrantyGroup){
		this.warrantyGroup=warrantyGroup;
	}

	public Long getWarrantyGroup(){
		return this.warrantyGroup;
	}

	public void setOemCompanyId(Long oemCompanyId){
		this.oemCompanyId=oemCompanyId;
	}

	public Long getOemCompanyId(){
		return this.oemCompanyId;
	}

	public void setPriorLevel(String priorLevel){
		this.priorLevel=priorLevel;
	}

	public String getPriorLevel(){
		return this.priorLevel;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setRole(String role){
		this.role=role;
	}

	public String getRole(){
		return this.role;
	}

}