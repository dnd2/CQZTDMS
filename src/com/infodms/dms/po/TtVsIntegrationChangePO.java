/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-04-17 15:13:37
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsIntegrationChangePO extends PO{

	private Long integBefore;
	private Long thisChangeInteg;
	private Date createDate;
	private Long integType;
	private Long dealerId;
	private Long relationId;
	private Long status;
	private Long authenticationInteg;
	private Long integChangeId;
	private Long yearInteg;
	private Long performanceInteg;
	private Long totalCommentInteg;
	private String name;
	private String idNo;
	private Long changeType;
	private Long integAfter;

	public void setIntegBefore(Long integBefore){
		this.integBefore=integBefore;
	}

	public Long getIntegBefore(){
		return this.integBefore;
	}

	public void setThisChangeInteg(Long thisChangeInteg){
		this.thisChangeInteg=thisChangeInteg;
	}

	public Long getThisChangeInteg(){
		return this.thisChangeInteg;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setIntegType(Long integType){
		this.integType=integType;
	}

	public Long getIntegType(){
		return this.integType;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setRelationId(Long relationId){
		this.relationId=relationId;
	}

	public Long getRelationId(){
		return this.relationId;
	}

	public void setStatus(Long status){
		this.status=status;
	}

	public Long getStatus(){
		return this.status;
	}

	public void setAuthenticationInteg(Long authenticationInteg){
		this.authenticationInteg=authenticationInteg;
	}

	public Long getAuthenticationInteg(){
		return this.authenticationInteg;
	}

	public void setIntegChangeId(Long integChangeId){
		this.integChangeId=integChangeId;
	}

	public Long getIntegChangeId(){
		return this.integChangeId;
	}

	public void setYearInteg(Long yearInteg){
		this.yearInteg=yearInteg;
	}

	public Long getYearInteg(){
		return this.yearInteg;
	}

	public void setPerformanceInteg(Long performanceInteg){
		this.performanceInteg=performanceInteg;
	}

	public Long getPerformanceInteg(){
		return this.performanceInteg;
	}

	public void setTotalCommentInteg(Long totalCommentInteg){
		this.totalCommentInteg=totalCommentInteg;
	}

	public Long getTotalCommentInteg(){
		return this.totalCommentInteg;
	}

	public void setName(String name){
		this.name=name;
	}

	public String getName(){
		return this.name;
	}

	public void setIdNo(String idNo){
		this.idNo=idNo;
	}

	public String getIdNo(){
		return this.idNo;
	}

	public void setChangeType(Long changeType){
		this.changeType=changeType;
	}

	public Long getChangeType(){
		return this.changeType;
	}

	public void setIntegAfter(Long integAfter){
		this.integAfter=integAfter;
	}

	public Long getIntegAfter(){
		return this.integAfter;
	}

}