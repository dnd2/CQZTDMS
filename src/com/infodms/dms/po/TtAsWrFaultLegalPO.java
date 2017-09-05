/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-04-15 09:38:45
* CreateBy   : zhuwei
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrFaultLegalPO extends PO{

	private String faultName;
	private String faultCode;
	private Long faultTypeId;
	private Date updateDate;
	private Integer status;
	private Long updateBy;
	private Long createBy;
	private Long faultId;
	private Integer isDe;
	private Date createDate;
	

	public void setFaultName(String faultName){
		this.faultName=faultName;
	}

	public String getFaultName(){
		return this.faultName;
	}

	public void setFaultCode(String faultCode){
		this.faultCode=faultCode;
	}

	public String getFaultCode(){
		return this.faultCode;
	}

	public void setFaultTypeId(Long faultTypeId){
		this.faultTypeId=faultTypeId;
	}

	public Long getFaultTypeId(){
		return this.faultTypeId;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
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

	public void setFaultId(Long faultId){
		this.faultId=faultId;
	}

	public Long getFaultId(){
		return this.faultId;
	}

	public void setIsDe(Integer isDe){
		this.isDe=isDe;
	}

	public Integer getIsDe(){
		return this.isDe;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

}