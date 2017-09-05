/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-05-04 10:01:53
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtOutOrderCheckPO extends PO{

	private Long checkId;
	private Long checkOrgId;
	private Date updateDate;
	private Long createBy;
	private Date checkDate;
	private Long checkUserId;
	private Long orderId;
	private Integer checkStatus;
	private Long updateBy;
	private Integer checkType;
	private String checkDesc;
	private Date createDate;
	private Long checkPositionId;

	public void setCheckId(Long checkId){
		this.checkId=checkId;
	}

	public Long getCheckId(){
		return this.checkId;
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

	public void setCheckDate(Date checkDate){
		this.checkDate=checkDate;
	}

	public Date getCheckDate(){
		return this.checkDate;
	}

	public void setCheckUserId(Long checkUserId){
		this.checkUserId=checkUserId;
	}

	public Long getCheckUserId(){
		return this.checkUserId;
	}

	public void setOrderId(Long orderId){
		this.orderId=orderId;
	}

	public Long getOrderId(){
		return this.orderId;
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

	public void setCheckType(Integer checkType){
		this.checkType=checkType;
	}

	public Integer getCheckType(){
		return this.checkType;
	}

	public void setCheckDesc(String checkDesc){
		this.checkDesc=checkDesc;
	}

	public String getCheckDesc(){
		return this.checkDesc;
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

}