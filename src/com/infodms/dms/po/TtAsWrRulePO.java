/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-07-13 17:00:28
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrRulePO extends PO{

	private Long companyId;
	private Date updateDate;
	private String ruleName;
	private Long updateBy;
	private String ruleCode;
	private Long createBy;
	private Integer ruleStatus;
	private Date createDate;
	private Integer ruleType;
	private Long id;

	public void setCompanyId(Long companyId){
		this.companyId=companyId;
	}

	public Long getCompanyId(){
		return this.companyId;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setRuleName(String ruleName){
		this.ruleName=ruleName;
	}

	public String getRuleName(){
		return this.ruleName;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setRuleCode(String ruleCode){
		this.ruleCode=ruleCode;
	}

	public String getRuleCode(){
		return this.ruleCode;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public Integer getRuleStatus() {
		return ruleStatus;
	}

	public void setRuleStatus(Integer ruleStatus) {
		this.ruleStatus = ruleStatus;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setRuleType(Integer ruleType){
		this.ruleType=ruleType;
	}

	public Integer getRuleType(){
		return this.ruleType;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}
}