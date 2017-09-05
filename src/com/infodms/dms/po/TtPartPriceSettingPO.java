/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-04-07 14:20:13
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartPriceSettingPO extends PO{

	private Integer state;
	private String typeDesc;
	private String dealerId;
	private Integer isFirst;
	private Date updateDate;
	private Long createBy;
	private Integer status;
	private Integer isUsing;
	private Long updateBy;
	private Integer dealerType;
	private Long typeId;
	private Integer scopeType;
	private Date createDate;
	private String typeCode;

	public void setState(Integer state){
		this.state=state;
	}

	public Integer getState(){
		return this.state;
	}

	public void setTypeDesc(String typeDesc){
		this.typeDesc=typeDesc;
	}

	public String getTypeDesc(){
		return this.typeDesc;
	}

	public void setDealerId(String dealerId){
		this.dealerId=dealerId;
	}

	public String getDealerId(){
		return this.dealerId;
	}

	public void setIsFirst(Integer isFirst){
		this.isFirst=isFirst;
	}

	public Integer getIsFirst(){
		return this.isFirst;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setIsUsing(Integer isUsing){
		this.isUsing=isUsing;
	}

	public Integer getIsUsing(){
		return this.isUsing;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setDealerType(Integer dealerType){
		this.dealerType=dealerType;
	}

	public Integer getDealerType(){
		return this.dealerType;
	}

	public void setTypeId(Long typeId){
		this.typeId=typeId;
	}

	public Long getTypeId(){
		return this.typeId;
	}

	public void setScopeType(Integer scopeType){
		this.scopeType=scopeType;
	}

	public Integer getScopeType(){
		return this.scopeType;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setTypeCode(String typeCode){
		this.typeCode=typeCode;
	}

	public String getTypeCode(){
		return this.typeCode;
	}

}