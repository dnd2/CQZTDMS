/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2017-07-31 16:02:14
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmOrgPO extends PO{

	private String orgName;
	private Long companyId;
	private Integer orgType;
	private Date updateDate;
	private Long nameSort;
	private String treeCode;
	private Integer orgLevel;
	private Long createBy;
	private String orgDesc;
	private Integer status;
	private String logiId;
	private Long orgId;
	private Integer dutyType;
	private String erpOrgCode;
	private Long updateBy;
	private Long parentOrgId;
	private String orgCode;
	private Date createDate;
	private Integer isCount;
	private Integer isStatus;

	public void setOrgName(String orgName){
		this.orgName=orgName;
	}

	public String getOrgName(){
		return this.orgName;
	}

	public void setCompanyId(Long companyId){
		this.companyId=companyId;
	}

	public Long getCompanyId(){
		return this.companyId;
	}

	public void setOrgType(Integer orgType){
		this.orgType=orgType;
	}

	public Integer getOrgType(){
		return this.orgType;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setNameSort(Long nameSort){
		this.nameSort=nameSort;
	}

	public Long getNameSort(){
		return this.nameSort;
	}

	public void setTreeCode(String treeCode){
		this.treeCode=treeCode;
	}

	public String getTreeCode(){
		return this.treeCode;
	}

	public void setOrgLevel(Integer orgLevel){
		this.orgLevel=orgLevel;
	}

	public Integer getOrgLevel(){
		return this.orgLevel;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setOrgDesc(String orgDesc){
		this.orgDesc=orgDesc;
	}

	public String getOrgDesc(){
		return this.orgDesc;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setLogiId(String logiId){
		this.logiId=logiId;
	}

	public String getLogiId(){
		return this.logiId;
	}

	public void setOrgId(Long orgId){
		this.orgId=orgId;
	}

	public Long getOrgId(){
		return this.orgId;
	}

	public void setDutyType(Integer dutyType){
		this.dutyType=dutyType;
	}

	public Integer getDutyType(){
		return this.dutyType;
	}

	public void setErpOrgCode(String erpOrgCode){
		this.erpOrgCode=erpOrgCode;
	}

	public String getErpOrgCode(){
		return this.erpOrgCode;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setParentOrgId(Long parentOrgId){
		this.parentOrgId=parentOrgId;
	}

	public Long getParentOrgId(){
		return this.parentOrgId;
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

	public void setIsCount(Integer isCount){
		this.isCount=isCount;
	}

	public Integer getIsCount(){
		return this.isCount;
	}

	public void setIsStatus(Integer isStatus){
		this.isStatus=isStatus;
	}

	public Integer getIsStatus(){
		return this.isStatus;
	}

}