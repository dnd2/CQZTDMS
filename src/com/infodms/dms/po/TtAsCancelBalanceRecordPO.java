/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-11-26 15:01:33
* CreateBy   : wizard_lee
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsCancelBalanceRecordPO extends PO{

	private Integer recordStatus;
	private String roNo;
	private Long recordId;
	private Long createBy;
	private String remark;
	private Long roId;
	private Date createDate;
	private Long recordType;

	public Long getRecordType() {
		return recordType;
	}

	public void setRecordType(Long recordType) {
		this.recordType = recordType;
	}

	public void setRecordStatus(Integer recordStatus){
		this.recordStatus=recordStatus;
	}

	public Integer getRecordStatus(){
		return this.recordStatus;
	}

	public void setRoNo(String roNo){
		this.roNo=roNo;
	}

	public String getRoNo(){
		return this.roNo;
	}

	public void setRecordId(Long recordId){
		this.recordId=recordId;
	}

	public Long getRecordId(){
		return this.recordId;
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

	public void setRoId(Long roId){
		this.roId=roId;
	}

	public Long getRoId(){
		return this.roId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

}