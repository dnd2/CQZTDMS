/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-09-02 16:17:39
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmpVsResourcesPO extends PO{

	private String userId;
	private String planAmt;
	private String groupName;
	private String planYear;
	private String rowNumber;
	private String groupCode;
	private String planWeek;
	private String planMonth;

	public void setUserId(String userId){
		this.userId=userId;
	}

	public String getUserId(){
		return this.userId;
	}

	public void setPlanAmt(String planAmt){
		this.planAmt=planAmt;
	}

	public String getPlanAmt(){
		return this.planAmt;
	}

	public void setGroupName(String groupName){
		this.groupName=groupName;
	}

	public String getGroupName(){
		return this.groupName;
	}

	public void setPlanYear(String planYear){
		this.planYear=planYear;
	}

	public String getPlanYear(){
		return this.planYear;
	}

	public void setRowNumber(String rowNumber){
		this.rowNumber=rowNumber;
	}

	public String getRowNumber(){
		return this.rowNumber;
	}

	public void setGroupCode(String groupCode){
		this.groupCode=groupCode;
	}

	public String getGroupCode(){
		return this.groupCode;
	}

	public void setPlanWeek(String planWeek){
		this.planWeek=planWeek;
	}

	public String getPlanWeek(){
		return this.planWeek;
	}

	public void setPlanMonth(String planMonth){
		this.planMonth=planMonth;
	}

	public String getPlanMonth(){
		return this.planMonth;
	}

}