/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-06-23 16:16:11
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPtClaimItemPO extends PO{

	private Integer claimCount;
	private Long claimTypeId;
	private Long updateBy;
	private Date updateDate;
	private Long partId;
	private Long claimId;
	private Long createBy;
	private String remark;
	private Date createDate;
	private Long itemId;

	public void setClaimCount(Integer claimCount){
		this.claimCount=claimCount;
	}

	public Integer getClaimCount(){
		return this.claimCount;
	}

	public void setClaimTypeId(Long claimTypeId){
		this.claimTypeId=claimTypeId;
	}

	public Long getClaimTypeId(){
		return this.claimTypeId;
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

	public void setPartId(Long partId){
		this.partId=partId;
	}

	public Long getPartId(){
		return this.partId;
	}

	public void setClaimId(Long claimId){
		this.claimId=claimId;
	}

	public Long getClaimId(){
		return this.claimId;
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

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setItemId(Long itemId){
		this.itemId=itemId;
	}

	public Long getItemId(){
		return this.itemId;
	}

}