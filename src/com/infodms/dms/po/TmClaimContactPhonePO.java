/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-03-21 15:13:37
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmClaimContactPhonePO extends PO{

	private Integer perPose;
	private String perName;
	private Long dealerId;
	private Long userId;
	private Long updateBy;
	private String perPhone;
	private Date updateDate;
	private Long id;
	private Long createBy;
	private Date createDate;
	private Integer status;
	private String perRemark;

	public void setPerPose(Integer perPose){
		this.perPose=perPose;
	}

	public Integer getPerPose(){
		return this.perPose;
	}

	public void setPerName(String perName){
		this.perName=perName;
	}

	public String getPerName(){
		return this.perName;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setUserId(Long userId){
		this.userId=userId;
	}

	public Long getUserId(){
		return this.userId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setPerPhone(String perPhone){
		this.perPhone=perPhone;
	}

	public String getPerPhone(){
		return this.perPhone;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
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

	public void setPerRemark(String perRemark){
		this.perRemark=perRemark;
	}

	public String getPerRemark(){
		return this.perRemark;
	}

}