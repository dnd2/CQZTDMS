/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-12-09 11:35:20
* CreateBy   : nova
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrKnowledgebasePO extends PO{

	private String orgName;
	private String contents;
	private Date updateDate;
	private Long createBy;
	private String voicePerson;
	private Integer status;
	private Long orgId;
	private Long updateBy;
	private Long id;
	private Date publishedDate;
	private Date createDate;
	private String code;
	private String title;
	private Long wrgroupId;
	private Long modelPart;

	public Long getWrgroupId() {
		return wrgroupId;
	}

	public void setWrgroupId(Long wrgroupId) {
		this.wrgroupId = wrgroupId;
	}


	public Long getModelPart() {
		return modelPart;
	}

	public void setModelPart(Long modelPart) {
		this.modelPart = modelPart;
	}

	public void setOrgName(String orgName){
		this.orgName=orgName;
	}

	public String getOrgName(){
		return this.orgName;
	}

	public void setContents(String contents){
		this.contents=contents;
	}

	public String getContents(){
		return this.contents;
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

	public void setVoicePerson(String voicePerson){
		this.voicePerson=voicePerson;
	}

	public String getVoicePerson(){
		return this.voicePerson;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setOrgId(Long orgId){
		this.orgId=orgId;
	}

	public Long getOrgId(){
		return this.orgId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setPublishedDate(Date publishedDate){
		this.publishedDate=publishedDate;
	}

	public Date getPublishedDate(){
		return this.publishedDate;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setCode(String code){
		this.code=code;
	}

	public String getCode(){
		return this.code;
	}

	public void setTitle(String title){
		this.title=title;
	}

	public String getTitle(){
		return this.title;
	}

}