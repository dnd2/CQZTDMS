/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-07-05 09:09:02
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmRegionPO extends PO{

	private String regionName;
	private Long regionId;
	private Date updateDate;
	private Integer status;
	private String regionCode;
	private Long updateBy;
	private Long createBy;
	private String treeCode;
	private Date createDate;
	private Integer regionType;
	private String zipCode;
	private Long parentId;
	private Long orgId;
	

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public void setRegionName(String regionName){
		this.regionName=regionName;
	}

	public String getRegionName(){
		return this.regionName;
	}

	public void setRegionId(Long regionId){
		this.regionId=regionId;
	}

	public Long getRegionId(){
		return this.regionId;
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

	public void setRegionCode(String regionCode){
		this.regionCode=regionCode;
	}

	public String getRegionCode(){
		return this.regionCode;
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

	public void setTreeCode(String treeCode){
		this.treeCode=treeCode;
	}

	public String getTreeCode(){
		return this.treeCode;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setRegionType(Integer regionType){
		this.regionType=regionType;
	}

	public Integer getRegionType(){
		return this.regionType;
	}

	public void setZipCode(String zipCode){
		this.zipCode=zipCode;
	}

	public String getZipCode(){
		return this.zipCode;
	}

	public void setParentId(Long parentId){
		this.parentId=parentId;
	}

	public Long getParentId(){
		return this.parentId;
	}

}