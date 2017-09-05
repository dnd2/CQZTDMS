/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2015-01-30 10:46:49
* CreateBy   : yuewei
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.yxdms.entity.oldpart;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class LogUpatePartProductCodePO extends PO{

	private String oldProducerName;
	private String newProducerName;
	private String newProducerCode;
	private Long userId;
	private String userName;
	private String oldProducerCode;
	private Long id;
	private Long claimId;
	private Date createDate;
	private String PartCode; 
	private Long PartId;	
    
	
	public String getPartCode() {
		return PartCode;
	}

	public void setPartCode(String partCode) {
		PartCode = partCode;
	}

	public Long getPartId() {
		return PartId;
	}

	public void setPartId(Long partId) {
		PartId = partId;
	}

	public void setOldProducerName(String oldProducerName){
		this.oldProducerName=oldProducerName;
	}

	public String getOldProducerName(){
		return this.oldProducerName;
	}

	public void setNewProducerName(String newProducerName){
		this.newProducerName=newProducerName;
	}

	public String getNewProducerName(){
		return this.newProducerName;
	}

	public void setNewProducerCode(String newProducerCode){
		this.newProducerCode=newProducerCode;
	}

	public String getNewProducerCode(){
		return this.newProducerCode;
	}

	public void setUserId(Long userId){
		this.userId=userId;
	}

	public Long getUserId(){
		return this.userId;
	}

	public void setUserName(String userName){
		this.userName=userName;
	}

	public String getUserName(){
		return this.userName;
	}

	public void setOldProducerCode(String oldProducerCode){
		this.oldProducerCode=oldProducerCode;
	}

	public String getOldProducerCode(){
		return this.oldProducerCode;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public Long getClaimId() {
		return claimId;
	}

	public void setClaimId(Long claimId) {
		this.claimId = claimId;
	}

}