/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-07-19 11:31:09
* CreateBy   : KFQ
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsPackgeChangeApplyPO extends PO{

	private String applyRemark;
	private Long updateBy;
	private String vin;
	private Date updateDate;
	private Long id;
	private Double auditAcount;
	private Date applyDate;
	private Date reportDate;
	private Integer applyStatus;
	private Long applyBy;
	
	private Long printBy;
	private Date printDate;
	private Integer printTimes;
	public void setApplyRemark(String applyRemark){
		this.applyRemark=applyRemark;
	}

	public String getApplyRemark(){
		return this.applyRemark;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setVin(String vin){
		this.vin=vin;
	}

	public String getVin(){
		return this.vin;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setAuditAcount(Double auditAcount){
		this.auditAcount=auditAcount;
	}

	public Double getAuditAcount(){
		return this.auditAcount;
	}

	public void setApplyDate(Date applyDate){
		this.applyDate=applyDate;
	}

	public Date getApplyDate(){
		return this.applyDate;
	}

	public void setReportDate(Date reportDate){
		this.reportDate=reportDate;
	}

	public Date getReportDate(){
		return this.reportDate;
	}

	public void setApplyStatus(Integer applyStatus){
		this.applyStatus=applyStatus;
	}

	public Integer getApplyStatus(){
		return this.applyStatus;
	}

	public void setApplyBy(Long applyBy){
		this.applyBy=applyBy;
	}

	public Long getApplyBy(){
		return this.applyBy;
	}

	public Long getPrintBy() {
		return printBy;
	}

	public void setPrintBy(Long printBy) {
		this.printBy = printBy;
	}

	public Date getPrintDate() {
		return printDate;
	}

	public void setPrintDate(Date printDate) {
		this.printDate = printDate;
	}

	public Integer getPrintTimes() {
		return printTimes;
	}

	public void setPrintTimes(Integer printTimes) {
		this.printTimes = printTimes;
	}

}