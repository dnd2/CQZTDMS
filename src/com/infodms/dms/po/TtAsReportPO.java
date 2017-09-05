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

import java.sql.Clob;
import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsReportPO extends PO{

	private Long updateBy;
	private String reportName;
	private Date updateDate;
	private Clob mainSql;
	private Long createBy;
	private String remark;
	private Date createDate;
	private Long reportId;
	private Integer oemOnly;
	private String remark2;
	private String mentionPerson;
	private Date mentionTime;

	public String getMentionPerson() {
		return mentionPerson;
	}

	public void setMentionPerson(String mentionPerson) {
		this.mentionPerson = mentionPerson;
	}

	public Date getMentionTime() {
		return mentionTime;
	}

	public void setMentionTime(Date mentionTime) {
		this.mentionTime = mentionTime;
	}

	public String getRemark2() {
		return remark2;
	}

	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}

	public Integer getOemOnly() {
		return oemOnly;
	}

	public void setOemOnly(Integer oemOnly) {
		this.oemOnly = oemOnly;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setReportName(String reportName){
		this.reportName=reportName;
	}

	public String getReportName(){
		return this.reportName;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setMainSql(Clob mainSql){
		this.mainSql=mainSql;
	}

	public Clob getMainSql(){
		return this.mainSql;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setReportId(Long reportId){
		this.reportId=reportId;
	}

	public Long getReportId(){
		return this.reportId;
	}

}