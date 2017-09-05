/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2011-02-28 16:39:16
* CreateBy   : nova
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsReportShowparamPO extends PO{

	private String otherName;
	private Long id;
	private String showName;
	private Long reportId;

	public void setOtherName(String otherName){
		this.otherName=otherName;
	}

	public String getOtherName(){
		return this.otherName;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setShowName(String showName){
		this.showName=showName;
	}

	public String getShowName(){
		return this.showName;
	}

	public void setReportId(Long reportId){
		this.reportId=reportId;
	}

	public Long getReportId(){
		return this.reportId;
	}

}