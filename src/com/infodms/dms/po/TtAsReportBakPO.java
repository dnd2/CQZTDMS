/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2011-03-10 15:06:46
* CreateBy   : nova
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsReportBakPO extends PO{

	private String sql;
	private String reportName;
	private Date createDate;
	private Long userid;
	private Long reportId;

	public Long getReportId() {
		return reportId;
	}

	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}

	public void setSql(String sql){
		this.sql=sql;
	}

	public String getSql(){
		return this.sql;
	}

	public void setReportName(String reportName){
		this.reportName=reportName;
	}

	public String getReportName(){
		return this.reportName;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setUserid(Long userid){
		this.userid=userid;
	}

	public Long getUserid(){
		return this.userid;
	}

}