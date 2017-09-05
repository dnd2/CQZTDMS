/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-06-02 09:32:10
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmpVsMonthlyPlanPO extends PO{

	private String dealerName;
	private String planType;
	private String groupName;
	private String groupCode;
	private String orgCode;
	private String dealerCode;
	private String rowNumber;
	private String planYear;
	private String orgName;
	private String userId;
	private String planMonth;
	private String sumAmt;

	public void setDealerName(String dealerName){
		this.dealerName=dealerName;
	}

	public String getDealerName(){
		return this.dealerName;
	}

	public void setPlanType(String planType){
		this.planType=planType;
	}

	public String getPlanType(){
		return this.planType;
	}

	public void setGroupName(String groupName){
		this.groupName=groupName;
	}

	public String getGroupName(){
		return this.groupName;
	}

	public void setGroupCode(String groupCode){
		this.groupCode=groupCode;
	}

	public String getGroupCode(){
		return this.groupCode;
	}

	public void setOrgCode(String orgCode){
		this.orgCode=orgCode;
	}

	public String getOrgCode(){
		return this.orgCode;
	}

	public void setDealerCode(String dealerCode){
		this.dealerCode=dealerCode;
	}

	public String getDealerCode(){
		return this.dealerCode;
	}

	public void setRowNumber(String rowNumber){
		this.rowNumber=rowNumber;
	}

	public String getRowNumber(){
		return this.rowNumber;
	}

	public void setPlanYear(String planYear){
		this.planYear=planYear;
	}

	public String getPlanYear(){
		return this.planYear;
	}

	public void setOrgName(String orgName){
		this.orgName=orgName;
	}

	public String getOrgName(){
		return this.orgName;
	}

	public void setUserId(String userId){
		this.userId=userId;
	}

	public String getUserId(){
		return this.userId;
	}

	public void setPlanMonth(String planMonth){
		this.planMonth=planMonth;
	}

	public String getPlanMonth(){
		return this.planMonth;
	}

	public void setSumAmt(String sumAmt){
		this.sumAmt=sumAmt;
	}

	public String getSumAmt(){
		return this.sumAmt;
	}

}