/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-01-18 09:59:43
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsPersonChangePO extends PO{

	private Long degree;
	private Long bank;
	private String bankCardno;
	private String mobile;
	private Long position;
	private Date createDate;
	private Long authenticationLevel;
	private Long dealerId;
	private String email;
	private Long isInvestor;
	private Date changeTime;
	private Long personChangeId;
	private Date entryDate;
	private Long gender;
	private String name;
	private Long remainInteg;
	private Date authenticationTime;
	private Long changeType;
	private String idNo;
	private Long positionStatus;
	private String remark;

	public void setDegree(Long degree){
		this.degree=degree;
	}

	public Long getDegree(){
		return this.degree;
	}

	public void setBank(Long bank){
		this.bank=bank;
	}

	public Long getBank(){
		return this.bank;
	}

	public void setBankCardno(String bankCardno){
		this.bankCardno=bankCardno;
	}

	public String getBankCardno(){
		return this.bankCardno;
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

	public void setAuthenticationLevel(Long authenticationLevel){
		this.authenticationLevel=authenticationLevel;
	}

	public Long getAuthenticationLevel(){
		return this.authenticationLevel;
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

	public void setChangeTime(Date changeTime){
		this.changeTime=changeTime;
	}

	public Date getChangeTime(){
		return this.changeTime;
	}

	public void setPersonChangeId(Long personChangeId){
		this.personChangeId=personChangeId;
	}

	public Long getPersonChangeId(){
		return this.personChangeId;
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

	public void setRemainInteg(Long remainInteg){
		this.remainInteg=remainInteg;
	}

	public Long getRemainInteg(){
		return this.remainInteg;
	}

	public void setAuthenticationTime(Date authenticationTime){
		this.authenticationTime=authenticationTime;
	}

	public Date getAuthenticationTime(){
		return this.authenticationTime;
	}

	public void setChangeType(Long changeType){
		this.changeType=changeType;
	}

	public Long getChangeType(){
		return this.changeType;
	}

	public void setIdNo(String idNo){
		this.idNo=idNo;
	}

	public String getIdNo(){
		return this.idNo;
	}

	public void setPositionStatus(Long positionStatus){
		this.positionStatus=positionStatus;
	}

	public Long getPositionStatus(){
		return this.positionStatus;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

}