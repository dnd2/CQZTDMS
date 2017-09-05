/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-08-30 11:34:17
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtExamPO extends PO{

	private String examName;
	private Date examEndTime;
	private Integer examStatus;
	private Long updateBy;
	private Date updateDate;
	private Long createBy;
	private String examRemark;
	private Date createDate;
	private Date examStartTime;
	private String examCode;
	private Long examId;

	public void setExamName(String examName){
		this.examName=examName;
	}

	public String getExamName(){
		return this.examName;
	}

	public void setExamEndTime(Date examEndTime){
		this.examEndTime=examEndTime;
	}

	public Date getExamEndTime(){
		return this.examEndTime;
	}

	public void setExamStatus(Integer examStatus){
		this.examStatus=examStatus;
	}

	public Integer getExamStatus(){
		return this.examStatus;
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

	public void setExamRemark(String examRemark){
		this.examRemark=examRemark;
	}

	public String getExamRemark(){
		return this.examRemark;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setExamStartTime(Date examStartTime){
		this.examStartTime=examStartTime;
	}

	public Date getExamStartTime(){
		return this.examStartTime;
	}

	public void setExamCode(String examCode){
		this.examCode=examCode;
	}

	public String getExamCode(){
		return this.examCode;
	}

	public void setExamId(Long examId){
		this.examId=examId;
	}

	public Long getExamId(){
		return this.examId;
	}

}