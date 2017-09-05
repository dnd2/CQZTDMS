/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-04-04 13:00:36
* CreateBy   : Yu
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtCrmKnowledgePO extends PO{

	private Long kgFileId;
	private Integer var;
	private Long updateBy;
	private Date updateDate;
	private Long createBy;
	private String kgMemo;
	private String kgTopic;
	private Long kgType;
	private Date createDate;
	private Long kgId;
	private Integer kgStatus;
	private Date kgSignTime;

	public void setKgFileId(Long kgFileId){
		this.kgFileId=kgFileId;
	}

	public Long getKgFileId(){
		return this.kgFileId;
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

	public void setKgMemo(String kgMemo){
		this.kgMemo=kgMemo;
	}

	public String getKgMemo(){
		return this.kgMemo;
	}

	public void setKgTopic(String kgTopic){
		this.kgTopic=kgTopic;
	}

	public String getKgTopic(){
		return this.kgTopic;
	}

	public void setKgType(Long kgType){
		this.kgType=kgType;
	}

	public Long getKgType(){
		return this.kgType;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setKgId(Long kgId){
		this.kgId=kgId;
	}

	public Long getKgId(){
		return this.kgId;
	}

	public void setKgStatus(Integer kgStatus){
		this.kgStatus=kgStatus;
	}

	public Integer getKgStatus(){
		return this.kgStatus;
	}

	public void setKgSignTime(Date kgSignTime){
		this.kgSignTime=kgSignTime;
	}

	public Date getKgSignTime(){
		return this.kgSignTime;
	}

}