/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-06-12 17:25:22
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsOrderChngPO extends PO{

	private Long newDealerId;
	private Date updateDate;
	private Long createBy;
	private Long orderId;
	private Date createDate;
	private Integer chngType;
	private Integer chngAmt;
	private Long oldDealerId;
	private Date chngDate;
	private Long updateBy;
	private Long chngUserId;
	private String chngReason;
	private Long detailId;
	private Long chngId;
	private Long chngPositionId;
	private Long chngOrgId;

	public void setNewDealerId(Long newDealerId){
		this.newDealerId=newDealerId;
	}

	public Long getNewDealerId(){
		return this.newDealerId;
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

	public void setOrderId(Long orderId){
		this.orderId=orderId;
	}

	public Long getOrderId(){
		return this.orderId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setChngType(Integer chngType){
		this.chngType=chngType;
	}

	public Integer getChngType(){
		return this.chngType;
	}

	public void setChngAmt(Integer chngAmt){
		this.chngAmt=chngAmt;
	}

	public Integer getChngAmt(){
		return this.chngAmt;
	}

	public void setOldDealerId(Long oldDealerId){
		this.oldDealerId=oldDealerId;
	}

	public Long getOldDealerId(){
		return this.oldDealerId;
	}

	public void setChngDate(Date chngDate){
		this.chngDate=chngDate;
	}

	public Date getChngDate(){
		return this.chngDate;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setChngUserId(Long chngUserId){
		this.chngUserId=chngUserId;
	}

	public Long getChngUserId(){
		return this.chngUserId;
	}

	public void setChngReason(String chngReason){
		this.chngReason=chngReason;
	}

	public String getChngReason(){
		return this.chngReason;
	}

	public void setDetailId(Long detailId){
		this.detailId=detailId;
	}

	public Long getDetailId(){
		return this.detailId;
	}

	public void setChngId(Long chngId){
		this.chngId=chngId;
	}

	public Long getChngId(){
		return this.chngId;
	}

	public void setChngPositionId(Long chngPositionId){
		this.chngPositionId=chngPositionId;
	}

	public Long getChngPositionId(){
		return this.chngPositionId;
	}

	public void setChngOrgId(Long chngOrgId){
		this.chngOrgId=chngOrgId;
	}

	public Long getChngOrgId(){
		return this.chngOrgId;
	}

}