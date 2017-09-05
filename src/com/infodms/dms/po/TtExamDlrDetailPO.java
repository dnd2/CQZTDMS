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
public class TtExamDlrDetailPO extends PO{

	private Long updateBy;
	private Date updateDate;
	private Long createBy;
	private Long examDlrId;
	private Long examDlrDetailId;
	private Long detailId;
	private Date createDate;

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

	public void setExamDlrId(Long examDlrId){
		this.examDlrId=examDlrId;
	}

	public Long getExamDlrId(){
		return this.examDlrId;
	}

	public void setExamDlrDetailId(Long examDlrDetailId){
		this.examDlrDetailId=examDlrDetailId;
	}

	public Long getExamDlrDetailId(){
		return this.examDlrDetailId;
	}

	public void setDetailId(Long detailId){
		this.detailId=detailId;
	}

	public Long getDetailId(){
		return this.detailId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

}