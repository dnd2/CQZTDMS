/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-06-28 17:52:12
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmpVsQuotaPO extends PO{

	private String orgCode;
	private String quotaYear;
	private String dealerName;
	private String quotaAmt;
	private String groupCode;
	private String groupName;
	private String rowNumber;
	private String dealerCode;
	private String orgName;
	private String quotaMonth;
	private String userId;
	private String quotaWeek;
	private Long areaId;

	public void setOrgCode(String orgCode){
		this.orgCode=orgCode;
	}

	public String getOrgCode(){
		return this.orgCode;
	}

	public void setQuotaYear(String quotaYear){
		this.quotaYear=quotaYear;
	}

	public String getQuotaYear(){
		return this.quotaYear;
	}

	public void setDealerName(String dealerName){
		this.dealerName=dealerName;
	}

	public String getDealerName(){
		return this.dealerName;
	}

	public void setQuotaAmt(String quotaAmt){
		this.quotaAmt=quotaAmt;
	}

	public String getQuotaAmt(){
		return this.quotaAmt;
	}

	public void setGroupCode(String groupCode){
		this.groupCode=groupCode;
	}

	public String getGroupCode(){
		return this.groupCode;
	}

	public void setGroupName(String groupName){
		this.groupName=groupName;
	}

	public String getGroupName(){
		return this.groupName;
	}

	public void setRowNumber(String rowNumber){
		this.rowNumber=rowNumber;
	}

	public String getRowNumber(){
		return this.rowNumber;
	}

	public void setDealerCode(String dealerCode){
		this.dealerCode=dealerCode;
	}

	public String getDealerCode(){
		return this.dealerCode;
	}

	public void setOrgName(String orgName){
		this.orgName=orgName;
	}

	public String getOrgName(){
		return this.orgName;
	}

	public void setQuotaMonth(String quotaMonth){
		this.quotaMonth=quotaMonth;
	}

	public String getQuotaMonth(){
		return this.quotaMonth;
	}

	public void setUserId(String userId){
		this.userId=userId;
	}

	public String getUserId(){
		return this.userId;
	}

	public void setQuotaWeek(String quotaWeek){
		this.quotaWeek=quotaWeek;
	}

	public String getQuotaWeek(){
		return this.quotaWeek;
	}

	public void setAreaId(Long areaId){
		this.areaId=areaId;
	}

	public Long getAreaId(){
		return this.areaId;
	}

}