/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2017-08-18 17:14:58
* CreateBy   : liaoyu
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsServiceWarnNumPO extends PO{

	private Long repairNum;
	private String vin;
	private Long serviceOrderId;
	private String failureModeCode;
	private Long partId;
	private Long warnRuleId;
	private Long createBy;
	private Date createDate;
	private String partCode;
	private Long warnNumId;

	public void setRepairNum(Long repairNum){
		this.repairNum=repairNum;
	}

	public Long getRepairNum(){
		return this.repairNum;
	}

	public void setVin(String vin){
		this.vin=vin;
	}

	public String getVin(){
		return this.vin;
	}

	public void setServiceOrderId(Long serviceOrderId){
		this.serviceOrderId=serviceOrderId;
	}

	public Long getServiceOrderId(){
		return this.serviceOrderId;
	}

	public void setFailureModeCode(String failureModeCode){
		this.failureModeCode=failureModeCode;
	}

	public String getFailureModeCode(){
		return this.failureModeCode;
	}

	public void setPartId(Long partId){
		this.partId=partId;
	}

	public Long getPartId(){
		return this.partId;
	}

	public void setWarnRuleId(Long warnRuleId){
		this.warnRuleId=warnRuleId;
	}

	public Long getWarnRuleId(){
		return this.warnRuleId;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setPartCode(String partCode){
		this.partCode=partCode;
	}

	public String getPartCode(){
		return this.partCode;
	}

	public void setWarnNumId(Long warnNumId){
		this.warnNumId=warnNumId;
	}

	public Long getWarnNumId(){
		return this.warnNumId;
	}

}