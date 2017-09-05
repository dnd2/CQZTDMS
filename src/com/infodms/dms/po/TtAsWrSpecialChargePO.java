/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-06-18 20:28:30
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrSpecialChargePO extends PO{

	private Long oemCompanyId;
	private String speCode;
	private Integer var;
	private Double maxAmount;
	private Long updateBy;
	private Integer speLevel;
	private Date updateDate;
	private Long speId;
	private Long createBy;
	private Date createDate;
	private Double minAmount;

	public void setOemCompanyId(Long oemCompanyId){
		this.oemCompanyId=oemCompanyId;
	}

	public Long getOemCompanyId(){
		return this.oemCompanyId;
	}

	public void setSpeCode(String speCode){
		this.speCode=speCode;
	}

	public String getSpeCode(){
		return this.speCode;
	}

	public void setVar(Integer var){
		this.var=var;
	}

	public Integer getVar(){
		return this.var;
	}

	public void setMaxAmount(Double maxAmount){
		this.maxAmount=maxAmount;
	}

	public Double getMaxAmount(){
		return this.maxAmount;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setSpeLevel(Integer speLevel){
		this.speLevel=speLevel;
	}

	public Integer getSpeLevel(){
		return this.speLevel;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setSpeId(Long speId){
		this.speId=speId;
	}

	public Long getSpeId(){
		return this.speId;
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

	public void setMinAmount(Double minAmount){
		this.minAmount=minAmount;
	}

	public Double getMinAmount(){
		return this.minAmount;
	}

}