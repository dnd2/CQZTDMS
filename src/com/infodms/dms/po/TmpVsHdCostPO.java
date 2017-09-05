/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-09-21 15:21:54
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmpVsHdCostPO extends PO{

	private String userId;
	private String hdName;
	private String orgName;
	private String hdAmount;
	private Date updateDate;
	private String orgAmount;
	private String costType;
	private Long createBy;
	private Long areaId;
	private Long updateBy;
	private String costDesc;
	private String hdCode;
	private Long rowNumber;
	private String orgCode;
	private Date createDate;

	public void setUserId(String userId){
		this.userId=userId;
	}

	public String getUserId(){
		return this.userId;
	}

	public void setHdName(String hdName){
		this.hdName=hdName;
	}

	public String getHdName(){
		return this.hdName;
	}

	public void setOrgName(String orgName){
		this.orgName=orgName;
	}

	public String getOrgName(){
		return this.orgName;
	}

	public void setHdAmount(String hdAmount){
		this.hdAmount=hdAmount;
	}

	public String getHdAmount(){
		return this.hdAmount;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setOrgAmount(String orgAmount){
		this.orgAmount=orgAmount;
	}

	public String getOrgAmount(){
		return this.orgAmount;
	}

	public void setCostType(String costType){
		this.costType=costType;
	}

	public String getCostType(){
		return this.costType;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setAreaId(Long areaId){
		this.areaId=areaId;
	}

	public Long getAreaId(){
		return this.areaId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setCostDesc(String costDesc){
		this.costDesc=costDesc;
	}

	public String getCostDesc(){
		return this.costDesc;
	}

	public void setHdCode(String hdCode){
		this.hdCode=hdCode;
	}

	public String getHdCode(){
		return this.hdCode;
	}

	public void setRowNumber(Long rowNumber){
		this.rowNumber=rowNumber;
	}

	public Long getRowNumber(){
		return this.rowNumber;
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

}