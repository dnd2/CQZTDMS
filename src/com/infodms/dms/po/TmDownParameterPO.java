/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-07-19 10:35:13
* CreateBy   : Arthur_Liu
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmDownParameterPO extends PO{

	private String parameterCode;
	private Long oemCompanyId;
	private Long dealerId;
	private Long updateBy;
	private Date updateDate;
	private String areaCode;
	private Long createBy;
	private Date createDate;
	private String parameterDesc;
	private Long downParaId;
	private String parameterValue;

	public void setParameterCode(String parameterCode){
		this.parameterCode=parameterCode;
	}

	public String getParameterCode(){
		return this.parameterCode;
	}

	public void setOemCompanyId(Long oemCompanyId){
		this.oemCompanyId=oemCompanyId;
	}

	public Long getOemCompanyId(){
		return this.oemCompanyId;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
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

	public void setAreaCode(String areaCode){
		this.areaCode=areaCode;
	}

	public String getAreaCode(){
		return this.areaCode;
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

	public void setParameterDesc(String parameterDesc){
		this.parameterDesc=parameterDesc;
	}

	public String getParameterDesc(){
		return this.parameterDesc;
	}

	public void setDownParaId(Long downParaId){
		this.downParaId=downParaId;
	}

	public Long getDownParaId(){
		return this.downParaId;
	}

	public void setParameterValue(String parameterValue){
		this.parameterValue=parameterValue;
	}

	public String getParameterValue(){
		return this.parameterValue;
	}

}