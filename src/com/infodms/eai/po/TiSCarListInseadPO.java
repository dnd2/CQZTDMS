/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2009-12-17 17:01:50
* CreateBy   : xwj
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.eai.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TiSCarListInseadPO extends PO{

	private String agentCode;
	private String engineNoNew;
	private String chassisNoNew;
	private Integer dmsStatus;
	private String chassisNo;
	private String rmark;
	private Date sellTime;
	private Date dmsDate;
	private String orderNo;

	public void setAgentCode(String agentCode){
		this.agentCode=agentCode;
	}

	public String getAgentCode(){
		return this.agentCode;
	}

	public void setEngineNoNew(String engineNoNew){
		this.engineNoNew=engineNoNew;
	}

	public String getEngineNoNew(){
		return this.engineNoNew;
	}

	public void setChassisNoNew(String chassisNoNew){
		this.chassisNoNew=chassisNoNew;
	}

	public String getChassisNoNew(){
		return this.chassisNoNew;
	}

	public void setDmsStatus(Integer dmsStatus){
		this.dmsStatus=dmsStatus;
	}

	public Integer getDmsStatus(){
		return this.dmsStatus;
	}

	public void setChassisNo(String chassisNo){
		this.chassisNo=chassisNo;
	}

	public String getChassisNo(){
		return this.chassisNo;
	}

	public void setRmark(String rmark){
		this.rmark=rmark;
	}

	public String getRmark(){
		return this.rmark;
	}

	public void setSellTime(Date sellTime){
		this.sellTime=sellTime;
	}

	public Date getSellTime(){
		return this.sellTime;
	}

	public void setDmsDate(Date dmsDate){
		this.dmsDate=dmsDate;
	}

	public Date getDmsDate(){
		return this.dmsDate;
	}

	public void setOrderNo(String orderNo){
		this.orderNo=orderNo;
	}

	public String getOrderNo(){
		return this.orderNo;
	}

}