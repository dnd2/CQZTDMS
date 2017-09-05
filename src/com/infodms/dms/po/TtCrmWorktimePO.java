/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-04-03 17:46:30
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtCrmWorktimePO extends PO{

	private Integer wtType;
	private Integer var;
	private Long updateBy;
	private Integer wtStaOnMinute;
	private Date updateDate;
	private Integer wtEndOffMinute;
	private Long wtId;
	private Long createBy;
	private Integer wtEndOnMinute;
	private Date createDate;
	private Integer wtStaOffMinute2;

	public Integer getWtType() {
		return wtType;
	}

	public void setWtType(Integer wtType) {
		this.wtType = wtType;
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

	public void setWtStaOnMinute(Integer wtStaOnMinute){
		this.wtStaOnMinute=wtStaOnMinute;
	}

	public Integer getWtStaOnMinute(){
		return this.wtStaOnMinute;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setWtEndOffMinute(Integer wtEndOffMinute){
		this.wtEndOffMinute=wtEndOffMinute;
	}

	public Integer getWtEndOffMinute(){
		return this.wtEndOffMinute;
	}

	public void setWtId(Long wtId){
		this.wtId=wtId;
	}

	public Long getWtId(){
		return this.wtId;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setWtEndOnMinute(Integer wtEndOnMinute){
		this.wtEndOnMinute=wtEndOnMinute;
	}

	public Integer getWtEndOnMinute(){
		return this.wtEndOnMinute;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setWtStaOffMinute2(Integer wtStaOffMinute2){
		this.wtStaOffMinute2=wtStaOffMinute2;
	}

	public Integer getWtStaOffMinute2(){
		return this.wtStaOffMinute2;
	}

}