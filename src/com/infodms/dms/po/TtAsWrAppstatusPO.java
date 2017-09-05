/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-06-01 09:06:13
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrAppstatusPO extends PO{

	private Date updateDate;
	private Long updateBy;
	private Long createBy;
	private Integer processingStatus;
	private Date createDate;
	private Date processingDate;
	private Integer submitTimes;
	private String remark;
	private Long id;

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
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

	public void setProcessingStatus(Integer processingStatus){
		this.processingStatus=processingStatus;
	}

	public Integer getProcessingStatus(){
		return this.processingStatus;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setProcessingDate(Date processingDate){
		this.processingDate=processingDate;
	}

	public Date getProcessingDate(){
		return this.processingDate;
	}

	public void setSubmitTimes(Integer submitTimes){
		this.submitTimes=submitTimes;
	}

	public Integer getSubmitTimes(){
		return this.submitTimes;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

}