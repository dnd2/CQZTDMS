/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-01-05 18:40:34
* CreateBy   : xwj
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.eai.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TiQadRfidPO extends PO{

	private String rfid;
	private Integer dmsStatus;
	private Double chassisNo;
	private Date dmsDate;

	public void setRfid(String rfid){
		this.rfid=rfid;
	}

	public String getRfid(){
		return this.rfid;
	}

	public void setDmsStatus(Integer dmsStatus){
		this.dmsStatus=dmsStatus;
	}

	public Integer getDmsStatus(){
		return this.dmsStatus;
	}

	public void setChassisNo(Double chassisNo){
		this.chassisNo=chassisNo;
	}

	public Double getChassisNo(){
		return this.chassisNo;
	}

	public void setDmsDate(Date dmsDate){
		this.dmsDate=dmsDate;
	}

	public Date getDmsDate(){
		return this.dmsDate;
	}

}