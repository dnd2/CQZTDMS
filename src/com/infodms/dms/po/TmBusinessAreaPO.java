/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-10-27 19:45:53
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmBusinessAreaPO extends PO{

	private Long companyId;
	private Date updateDate;
	private Long createBy;
	private String areaShortcode;
	private Integer areaType;
	private String areaName;
	private Date createDate;
	private String areaCode;
	private Long produceBase;
	private Integer status;
	private Long updateBy;
	private Long erpCode;
	private Long areaId;

	public void setCompanyId(Long companyId){
		this.companyId=companyId;
	}

	public Long getCompanyId(){
		return this.companyId;
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

	public void setAreaShortcode(String areaShortcode){
		this.areaShortcode=areaShortcode;
	}

	public String getAreaShortcode(){
		return this.areaShortcode;
	}

	public void setAreaType(Integer areaType){
		this.areaType=areaType;
	}

	public Integer getAreaType(){
		return this.areaType;
	}

	public void setAreaName(String areaName){
		this.areaName=areaName;
	}

	public String getAreaName(){
		return this.areaName;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setAreaCode(String areaCode){
		this.areaCode=areaCode;
	}

	public String getAreaCode(){
		return this.areaCode;
	}

	public void setProduceBase(Long produceBase){
		this.produceBase=produceBase;
	}

	public Long getProduceBase(){
		return this.produceBase;
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

	public void setErpCode(Long erpCode){
		this.erpCode=erpCode;
	}

	public Long getErpCode(){
		return this.erpCode;
	}

	public void setAreaId(Long areaId){
		this.areaId=areaId;
	}

	public Long getAreaId(){
		return this.areaId;
	}

}