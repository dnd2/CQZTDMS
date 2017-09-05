/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-04-08 17:59:13
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtCrmReturnVisitRecordPO extends PO{

	private String rdContent;
	private Date updateDate;
	private Long rdUserId;
	private String rdUser;
	private Date rdDate;
	private Long createBy;
	private Long rdId;
	private Integer var;
	private Long updateBy;
	private Long rvId;
	private Integer rdMode;
	private Integer rdIsAccept;
	private Date createDate;

	public void setRdContent(String rdContent){
		this.rdContent=rdContent;
	}

	public String getRdContent(){
		return this.rdContent;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setRdUserId(Long rdUserId){
		this.rdUserId=rdUserId;
	}

	public Long getRdUserId(){
		return this.rdUserId;
	}

	public void setRdUser(String rdUser){
		this.rdUser=rdUser;
	}

	public String getRdUser(){
		return this.rdUser;
	}

	public void setRdDate(Date rdDate){
		this.rdDate=rdDate;
	}

	public Date getRdDate(){
		return this.rdDate;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setRdId(Long rdId){
		this.rdId=rdId;
	}

	public Long getRdId(){
		return this.rdId;
	}

	public void setVar(Integer var){
		this.var=var;
	}

	public Integer getVar(){
		return this.var;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setRvId(Long rvId){
		this.rvId=rvId;
	}

	public Long getRvId(){
		return this.rvId;
	}

	public void setRdMode(Integer rdMode){
		this.rdMode=rdMode;
	}

	public Integer getRdMode(){
		return this.rdMode;
	}

	public void setRdIsAccept(Integer rdIsAccept){
		this.rdIsAccept=rdIsAccept;
	}

	public Integer getRdIsAccept(){
		return this.rdIsAccept;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

}