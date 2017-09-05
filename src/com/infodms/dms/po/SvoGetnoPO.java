/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-03-12 19:01:50
* CreateBy   : longwenjun
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class SvoGetnoPO extends PO{

	private Integer nums;
	private String sclass;
	private String dealerCode;
	private String stime;

	public void setNums(Integer nums){
		this.nums=nums;
	}

	public Integer getNums(){
		return this.nums;
	}

	public void setSclass(String sclass){
		this.sclass=sclass;
	}

	public String getSclass(){
		return this.sclass;
	}

	public void setDealerCode(String dealerCode){
		this.dealerCode=dealerCode;
	}

	public String getDealerCode(){
		return this.dealerCode;
	}

	public void setStime(String stime){
		this.stime=stime;
	}

	public String getStime(){
		return this.stime;
	}

}