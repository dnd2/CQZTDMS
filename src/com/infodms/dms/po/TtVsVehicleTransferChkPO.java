/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-06-03 15:18:33
* CreateBy   : Arthur_Liu
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsVehicleTransferChkPO extends PO{

	private Long checkUserId;
	private Long checkId;
	private Integer checkStatus;
	private Long updateBy;
	private Long checkOrgId;
	private Date updateDate;
	private Long createBy;
	private String checkDesc;
	private Long transferId;
	private Date createDate;
	private Long checkPositionId;
	private Integer ifStatus;

	public void setCheckUserId(Long checkUserId){
		this.checkUserId=checkUserId;
	}

	public Long getCheckUserId(){
		return this.checkUserId;
	}

	public void setCheckId(Long checkId){
		this.checkId=checkId;
	}

	public Long getCheckId(){
		return this.checkId;
	}

	public void setCheckStatus(Integer checkStatus){
		this.checkStatus=checkStatus;
	}

	public Integer getCheckStatus(){
		return this.checkStatus;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setCheckOrgId(Long checkOrgId){
		this.checkOrgId=checkOrgId;
	}

	public Long getCheckOrgId(){
		return this.checkOrgId;
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

	public void setCheckDesc(String checkDesc){
		this.checkDesc=checkDesc;
	}

	public String getCheckDesc(){
		return this.checkDesc;
	}

	public void setTransferId(Long transferId){
		this.transferId=transferId;
	}

	public Long getTransferId(){
		return this.transferId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setCheckPositionId(Long checkPositionId){
		this.checkPositionId=checkPositionId;
	}

	public Long getCheckPositionId(){
		return this.checkPositionId;
	}

	public Integer getIfStatus() {
		return ifStatus;
	}

	public void setIfStatus(Integer ifStatus) {
		this.ifStatus = ifStatus;
	}

}