/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-11-12 18:30:01
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TPcInsurencePO extends PO{

	private Long ctmId;
	private String insurenceVar;
	private Date updateDate;
	private Long status;
	private Long insurenceMoney;
	private Long insurenceId;
	private Long updateBy;
	private Long createBy;
	private String insurenceCompany;
	private Date createDate;
	private Date insurenceDate;
	private String remark;

	public void setCtmId(Long ctmId){
		this.ctmId=ctmId;
	}

	public Long getCtmId(){
		return this.ctmId;
	}

	public void setInsurenceVar(String insurenceVar){
		this.insurenceVar=insurenceVar;
	}

	public String getInsurenceVar(){
		return this.insurenceVar;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setStatus(Long status){
		this.status=status;
	}

	public Long getStatus(){
		return this.status;
	}

	public void setInsurenceMoney(Long insurenceMoney){
		this.insurenceMoney=insurenceMoney;
	}

	public Long getInsurenceMoney(){
		return this.insurenceMoney;
	}

	public void setInsurenceId(Long insurenceId){
		this.insurenceId=insurenceId;
	}

	public Long getInsurenceId(){
		return this.insurenceId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setInsurenceCompany(String insurenceCompany){
		this.insurenceCompany=insurenceCompany;
	}

	public String getInsurenceCompany(){
		return this.insurenceCompany;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setInsurenceDate(Date insurenceDate){
		this.insurenceDate=insurenceDate;
	}

	public Date getInsurenceDate(){
		return this.insurenceDate;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

}