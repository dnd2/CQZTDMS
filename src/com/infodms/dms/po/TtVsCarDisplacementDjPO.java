/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2011-05-27 09:22:42
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsCarDisplacementDjPO extends PO{

	private String orderCreateName;
	private Date orderCreateDate;
	private Long djId;
	private String djCode;
	private String orderArea;
	private Long displacementId;

	public void setOrderCreateName(String orderCreateName){
		this.orderCreateName=orderCreateName;
	}

	public String getOrderCreateName(){
		return this.orderCreateName;
	}

	public void setOrderCreateDate(Date orderCreateDate){
		this.orderCreateDate=orderCreateDate;
	}

	public Date getOrderCreateDate(){
		return this.orderCreateDate;
	}

	public void setDjId(Long djId){
		this.djId=djId;
	}

	public Long getDjId(){
		return this.djId;
	}

	public void setDjCode(String djCode){
		this.djCode=djCode;
	}

	public String getDjCode(){
		return this.djCode;
	}

	public void setOrderArea(String orderArea){
		this.orderArea=orderArea;
	}

	public String getOrderArea(){
		return this.orderArea;
	}

	public void setDisplacementId(Long displacementId){
		this.displacementId=displacementId;
	}

	public Long getDisplacementId(){
		return this.displacementId;
	}

}