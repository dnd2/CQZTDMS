/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-12-09 16:25:05
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TPcRemindPO extends PO{

	private Integer remindStatus;
	private Date remindDate;
	private String remindType;
	private Long beremindId;
	private String createBy;
	private Long customerId;
	private Date createDate;
	private Long remindId;
	private String adviser;
	private String dealerId;
	private Integer remindNum;

	public Integer getRemindNum() {
		return remindNum;
	}

	public void setRemindNum(Integer remindNum) {
		this.remindNum = remindNum;
	}

	public String getDealerId() {
		return dealerId;
	}

	public void setDealerId(String dealerId) {
		this.dealerId = dealerId;
	}

	public String getAdviser() {
		return adviser;
	}

	public void setAdviser(String adviser) {
		this.adviser = adviser;
	}

	public void setRemindStatus(Integer remindStatus){
		this.remindStatus=remindStatus;
	}

	public Integer getRemindStatus(){
		return this.remindStatus;
	}

	public void setRemindDate(Date remindDate){
		this.remindDate=remindDate;
	}

	public Date getRemindDate(){
		return this.remindDate;
	}

	public void setRemindType(String remindType){
		this.remindType=remindType;
	}

	public String getRemindType(){
		return this.remindType;
	}

	public void setBeremindId(Long beremindId){
		this.beremindId=beremindId;
	}

	public Long getBeremindId(){
		return this.beremindId;
	}

	public void setCreateBy(String createBy){
		this.createBy=createBy;
	}

	public String getCreateBy(){
		return this.createBy;
	}

	public void setCustomerId(Long customerId){
		this.customerId=customerId;
	}

	public Long getCustomerId(){
		return this.customerId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setRemindId(Long remindId){
		this.remindId=remindId;
	}

	public Long getRemindId(){
		return this.remindId;
	}

}