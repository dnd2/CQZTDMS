/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-08-31 10:35:54
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtExamDlrPO extends PO{

	private Long dealerId;
	private Long updateBy;
	private Date submitTime;
	private Date updateDate;
	private Long createBy;
	private Long examDlrId;
	private Date createDate;
	private Integer status;
	private Float examResult;
	private Long examId;

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setSubmitTime(Date submitTime){
		this.submitTime=submitTime;
	}

	public Date getSubmitTime(){
		return this.submitTime;
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

	public void setExamDlrId(Long examDlrId){
		this.examDlrId=examDlrId;
	}

	public Long getExamDlrId(){
		return this.examDlrId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setExamResult(Float examResult){
		this.examResult=examResult;
	}

	public Float getExamResult(){
		return this.examResult;
	}

	public void setExamId(Long examId){
		this.examId=examId;
	}

	public Long getExamId(){
		return this.examId;
	}

}