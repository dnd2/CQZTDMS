/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-01-05 18:40:34
* CreateBy   : xwj
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.eai.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TiSCarListInsteadPO extends PO{

	private Date updateDate;
	private String agentCode;
	private Long createBy;
	private String chassisNoNew;
	private String engineNoNew;
	private Date createDate;
	private String chassisNo;
	private Date sellTime;
	private String orderNo;
	private Long updateBy;
	private Integer dmsStatus;
	private Long seqId;
	private String rmark;
	private Date dmsDate;

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setAgentCode(String agentCode){
		this.agentCode=agentCode;
	}

	public String getAgentCode(){
		return this.agentCode;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setChassisNoNew(String chassisNoNew){
		this.chassisNoNew=chassisNoNew;
	}

	public String getChassisNoNew(){
		return this.chassisNoNew;
	}

	public void setEngineNoNew(String engineNoNew){
		this.engineNoNew=engineNoNew;
	}

	public String getEngineNoNew(){
		return this.engineNoNew;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setChassisNo(String chassisNo){
		this.chassisNo=chassisNo;
	}

	public String getChassisNo(){
		return this.chassisNo;
	}

	public void setSellTime(Date sellTime){
		this.sellTime=sellTime;
	}

	public Date getSellTime(){
		return this.sellTime;
	}

	public void setOrderNo(String orderNo){
		this.orderNo=orderNo;
	}

	public String getOrderNo(){
		return this.orderNo;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setDmsStatus(Integer dmsStatus){
		this.dmsStatus=dmsStatus;
	}

	public Integer getDmsStatus(){
		return this.dmsStatus;
	}

	public void setSeqId(Long seqId){
		this.seqId=seqId;
	}

	public Long getSeqId(){
		return this.seqId;
	}

	public void setRmark(String rmark){
		this.rmark=rmark;
	}

	public String getRmark(){
		return this.rmark;
	}

	public void setDmsDate(Date dmsDate){
		this.dmsDate=dmsDate;
	}

	public Date getDmsDate(){
		return this.dmsDate;
	}

}