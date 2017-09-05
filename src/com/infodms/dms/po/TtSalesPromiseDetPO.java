/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-04-22 17:27:50
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtSalesPromiseDetPO extends PO{

	private Long groupId;
	private Long applyNum;
	private Long proId;
	private Long updateBy;
	private Date updateDate;
	private Long createBy;
	private Long detailId;
	private Date createDate;
	private Long useNum;
	private Long auditNum;

	public void setGroupId(Long groupId){
		this.groupId=groupId;
	}

	public Long getGroupId(){
		return this.groupId;
	}

	public void setApplyNum(Long applyNum){
		this.applyNum=applyNum;
	}

	public Long getApplyNum(){
		return this.applyNum;
	}

	public void setProId(Long proId){
		this.proId=proId;
	}

	public Long getProId(){
		return this.proId;
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

	public void setUseNum(Long useNum){
		this.useNum=useNum;
	}

	public Long getUseNum(){
		return this.useNum;
	}

	public void setAuditNum(Long auditNum){
		this.auditNum=auditNum;
	}

	public Long getAuditNum(){
		return this.auditNum;
	}

}