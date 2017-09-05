/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-11-04 10:55:33
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class NetSpeciaStrokeOperatRelaPO extends PO{

	private String   linkmanId;
	private String   operatId;   
	private String   LinkmanName; 
	private String   linkmanJob;  
	private String   linkmanWay;  
	private Date     createDate;  
	private Date     updateDate;
	private String  createBy;
	
	
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	
	public String getLinkmanId() {
		return linkmanId;
	}
	public void setLinkmanId(String linkmanId) {
		this.linkmanId = linkmanId;
	}
	public String getOperatId() {
		return operatId;
	}
	public void setOperatId(String operatId) {
		this.operatId = operatId;
	}
	public String getLinkmanName() {
		return LinkmanName;
	}
	public void setLinkmanName(String linkmanName) {
		LinkmanName = linkmanName;
	}
	public String getLinkmanJob() {
		return linkmanJob;
	}
	public void setLinkmanJob(String linkmanJob) {
		this.linkmanJob = linkmanJob;
	}
	public String getLinkmanWay() {
		return linkmanWay;
	}
	public void setLinkmanWay(String linkmanWay) {
		this.linkmanWay = linkmanWay;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	
	
	
	
	
	
	
	
	

}