/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-09-29 13:47:30
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsPriceDtlTempPO extends PO{

	private String groupCode ;
	private String groupName;
	private Double salesPrice;
	private Long line;
	
	public void setGroupCode(String groupCode){
		this.groupCode = groupCode;
	}

	public String getGroupCode(){
		return groupCode;
	}
	
	public String getGroupName(){
		return groupName;
	}
	
	public void setGroupName(String groupName){
		this.groupName = groupName;
	}
	
	public void setSalesPrice(Double salesPrice){
		this.salesPrice = salesPrice;
	}
	
	public Double getSalesPrice(){
		return salesPrice;
	}
	
	public void setLine(Long line){
		this.line = line;
	}
	
	public Long getLine(){
		return line;
	}
}