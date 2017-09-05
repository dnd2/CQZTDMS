/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2017-07-17 10:00:15
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsPinApplyPO extends PO{

	private Long auditor;
	private Long dealerId;
	private String pinCode;
	private String vin;
	private Long id;
	private Long createBy;
	private String remark;
	private Date createDate;
	private Integer status;
	private String reply;
	private Date auditorTime;
	private String pinNo;

	public void setAuditor(Long auditor){
		this.auditor=auditor;
	}

	public Long getAuditor(){
		return this.auditor;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setPinCode(String pinCode){
		this.pinCode=pinCode;
	}

	public String getPinCode(){
		return this.pinCode;
	}

	public void setVin(String vin){
		this.vin=vin;
	}

	public String getVin(){
		return this.vin;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
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

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setReply(String reply){
		this.reply=reply;
	}

	public String getReply(){
		return this.reply;
	}

	public void setAuditorTime(Date auditorTime){
		this.auditorTime=auditorTime;
	}

	public Date getAuditorTime(){
		return this.auditorTime;
	}

	public void setPinNo(String pinNo){
		this.pinNo=pinNo;
	}

	public String getPinNo(){
		return this.pinNo;
	}

}