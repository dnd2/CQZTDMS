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
public class TtExamDetailPO extends PO{

	private String detailAddress;
	private String detailName;
	private Long updateBy;
	private Date updateDate;
	private String detailRemark;
	private Long createBy;
	private Long detailId;
	private Date createDate;
	private Long examId;

	public void setDetailAddress(String detailAddress){
		this.detailAddress=detailAddress;
	}

	public String getDetailAddress(){
		return this.detailAddress;
	}

	public void setDetailName(String detailName){
		this.detailName=detailName;
	}

	public String getDetailName(){
		return this.detailName;
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

	public void setDetailRemark(String detailRemark){
		this.detailRemark=detailRemark;
	}

	public String getDetailRemark(){
		return this.detailRemark;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
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

	public void setExamId(Long examId){
		this.examId=examId;
	}

	public Long getExamId(){
		return this.examId;
	}

}