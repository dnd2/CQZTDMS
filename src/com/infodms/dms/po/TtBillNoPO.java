/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-07-05 13:24:10
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtBillNoPO extends PO{

	private String billType;
	private String day;
	private String dealerId;
	private Long updateBy;
	private Date updateDate;
	private Long id;
	private String year;
	private Long createBy;
	private String orgCode;
	private Date createDate;
	private Long billNoId;
	private String month;

	public void setBillType(String billType){
		this.billType=billType;
	}

	public String getBillType(){
		return this.billType;
	}

	public void setDay(String day){
		this.day=day;
	}

	public String getDay(){
		return this.day;
	}

	public void setDealerId(String dealerId){
		this.dealerId=dealerId;
	}

	public String getDealerId(){
		return this.dealerId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
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

	public void setYear(String year){
		this.year=year;
	}

	public String getYear(){
		return this.year;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setOrgCode(String orgCode){
		this.orgCode=orgCode;
	}

	public String getOrgCode(){
		return this.orgCode;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setBillNoId(Long billNoId){
		this.billNoId=billNoId;
	}

	public Long getBillNoId(){
		return this.billNoId;
	}

	public void setMonth(String month){
		this.month=month;
	}

	public String getMonth(){
		return this.month;
	}

}