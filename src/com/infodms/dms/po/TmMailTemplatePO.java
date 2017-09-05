/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-01-05 11:38:50
* CreateBy   : Andy
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmMailTemplatePO extends PO{

	private String templateName;
	private Date updateDate;
	private String templatePrefix;
	private Long updateBy;
	private Long createBy;
	private String templateSuffixes;
	private Date createDate;
	private Long templateId;
	private Integer templateType;
	private Integer templateStatus;
	private String templateContent;

	public void setTemplateName(String templateName){
		this.templateName=templateName;
	}

	public String getTemplateName(){
		return this.templateName;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setTemplatePrefix(String templatePrefix){
		this.templatePrefix=templatePrefix;
	}

	public String getTemplatePrefix(){
		return this.templatePrefix;
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

	public void setTemplateSuffixes(String templateSuffixes){
		this.templateSuffixes=templateSuffixes;
	}

	public String getTemplateSuffixes(){
		return this.templateSuffixes;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setTemplateId(Long templateId){
		this.templateId=templateId;
	}

	public Long getTemplateId(){
		return this.templateId;
	}

	public void setTemplateType(Integer templateType){
		this.templateType=templateType;
	}

	public Integer getTemplateType(){
		return this.templateType;
	}

	public void setTemplateStatus(Integer templateStatus){
		this.templateStatus=templateStatus;
	}

	public Integer getTemplateStatus(){
		return this.templateStatus;
	}

	public void setTemplateContent(String templateContent){
		this.templateContent=templateContent;
	}

	public String getTemplateContent(){
		return this.templateContent;
	}

}