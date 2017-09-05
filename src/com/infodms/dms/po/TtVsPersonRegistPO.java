/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-05-02 10:38:39
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsPersonRegistPO extends PO{

	private Long degree;
	private Long registId;
	private Long bank;
	private String mobile;
	private Long position;
	private Date createDate;
	private Long auditUserId;
	private String bankcardNo;
	private Long dealerId;
	private String email;
	private Long isInvestor;
	private Long status;
	private Date entryDate;
	private Long gender;
	private String name;
	private String auditOpinion;
	private Long createUserId;
	private String idNo;
	private String remark;
	private Date auditTime;

	public void setDegree(Long degree){
		this.degree=degree;
	}

	public Long getDegree(){
		return this.degree;
	}

	public void setRegistId(Long registId){
		this.registId=registId;
	}

	public Long getRegistId(){
		return this.registId;
	}

	public void setBank(Long bank){
		this.bank=bank;
	}

	public Long getBank(){
		return this.bank;
	}

	public void setMobile(String mobile){
		this.mobile=mobile;
	}

	public String getMobile(){
		return this.mobile;
	}

	public void setPosition(Long position){
		this.position=position;
	}

	public Long getPosition(){
		return this.position;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setAuditUserId(Long auditUserId){
		this.auditUserId=auditUserId;
	}

	public Long getAuditUserId(){
		return this.auditUserId;
	}

	public void setBankcardNo(String bankcardNo){
		this.bankcardNo=bankcardNo;
	}

	public String getBankcardNo(){
		return this.bankcardNo;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setEmail(String email){
		this.email=email;
	}

	public String getEmail(){
		return this.email;
	}

	public void setIsInvestor(Long isInvestor){
		this.isInvestor=isInvestor;
	}

	public Long getIsInvestor(){
		return this.isInvestor;
	}

	public void setStatus(Long status){
		this.status=status;
	}

	public Long getStatus(){
		return this.status;
	}

	public void setEntryDate(Date entryDate){
		this.entryDate=entryDate;
	}

	public Date getEntryDate(){
		return this.entryDate;
	}

	public void setGender(Long gender){
		this.gender=gender;
	}

	public Long getGender(){
		return this.gender;
	}

	public void setName(String name){
		this.name=name;
	}

	public String getName(){
		return this.name;
	}

	public void setAuditOpinion(String auditOpinion){
		this.auditOpinion=auditOpinion;
	}

	public String getAuditOpinion(){
		return this.auditOpinion;
	}

	public void setCreateUserId(Long createUserId){
		this.createUserId=createUserId;
	}

	public Long getCreateUserId(){
		return this.createUserId;
	}

	public void setIdNo(String idNo){
		this.idNo=idNo;
	}

	public String getIdNo(){
		return this.idNo;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setAuditTime(Date auditTime){
		this.auditTime=auditTime;
	}

	public Date getAuditTime(){
		return this.auditTime;
	}

}