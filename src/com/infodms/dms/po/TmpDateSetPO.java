/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-07-02 09:50:08
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmpDateSetPO extends PO{

	private String rownumber;
	private String setMonth;
	private String setWeek;
	private String setDate;
	private String userId;
	private String setYear;

	public void setRownumber(String rownumber){
		this.rownumber=rownumber;
	}

	public String getRownumber(){
		return this.rownumber;
	}

	public void setSetMonth(String setMonth){
		this.setMonth=setMonth;
	}

	public String getSetMonth(){
		return this.setMonth;
	}

	public void setSetWeek(String setWeek){
		this.setWeek=setWeek;
	}

	public String getSetWeek(){
		return this.setWeek;
	}

	public void setSetDate(String setDate){
		this.setDate=setDate;
	}

	public String getSetDate(){
		return this.setDate;
	}

	public void setUserId(String userId){
		this.userId=userId;
	}

	public String getUserId(){
		return this.userId;
	}

	public void setSetYear(String setYear){
		this.setYear=setYear;
	}

	public String getSetYear(){
		return this.setYear;
	}

}