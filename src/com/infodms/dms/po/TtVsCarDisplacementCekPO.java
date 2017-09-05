/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2011-05-17 15:45:53
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsCarDisplacementCekPO extends PO{

	private Long department;
	private Date updateDate;
	private Integer status;
	private Long checkId;
	private String opinion;
	private Date checkDate;
	private Long updateBy;
	private Long createBy;
	private Date createDate;
	private Long poseId;
	private Long displacementId;

	public void setDepartment(Long department){
		this.department=department;
	}

	public Long getDepartment(){
		return this.department;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setCheckId(Long checkId){
		this.checkId=checkId;
	}

	public Long getCheckId(){
		return this.checkId;
	}

	public void setOpinion(String opinion){
		this.opinion=opinion;
	}

	public String getOpinion(){
		return this.opinion;
	}

	public void setCheckDate(Date checkDate){
		this.checkDate=checkDate;
	}

	public Date getCheckDate(){
		return this.checkDate;
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

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setPoseId(Long poseId){
		this.poseId=poseId;
	}

	public Long getPoseId(){
		return this.poseId;
	}

	public void setDisplacementId(Long displacementId){
		this.displacementId=displacementId;
	}

	public Long getDisplacementId(){
		return this.displacementId;
	}

}