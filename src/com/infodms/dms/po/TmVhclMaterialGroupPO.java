/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-07-26 09:09:21
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmVhclMaterialGroupPO extends PO{

	private Long companyId;
	private Date updateDate;
	private Long groupId;
	private String modelYear;
	private Long createBy;
	private String trimCode;
	private Date createDate;
	private Integer ifStatus;
	private String groupCode;
	private String groupName;
	private String prCode;
	private Integer status;
	private Long updateBy;
	private Integer forcastFlag;
	private String treeCode;
	private Long parentGroupId;
	private String modelCode;
	private String remark;
	private Integer groupLevel;
	private String ksid;
	private Integer salesFlag;
	
	private Long motivateInteg;
	

	public Long getMotivateInteg() {
		return motivateInteg;
	}

	public void setMotivateInteg(Long motivateInteg) {
		this.motivateInteg = motivateInteg;
	}

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

	public void setGroupId(Long groupId){
		this.groupId=groupId;
	}

	public Long getGroupId(){
		return this.groupId;
	}

	public void setModelYear(String modelYear){
		this.modelYear=modelYear;
	}

	public String getModelYear(){
		return this.modelYear;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setTrimCode(String trimCode){
		this.trimCode=trimCode;
	}

	public String getTrimCode(){
		return this.trimCode;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setIfStatus(Integer ifStatus){
		this.ifStatus=ifStatus;
	}

	public Integer getIfStatus(){
		return this.ifStatus;
	}

	public void setGroupCode(String groupCode){
		this.groupCode=groupCode;
	}

	public String getGroupCode(){
		return this.groupCode;
	}

	public void setGroupName(String groupName){
		this.groupName=groupName;
	}

	public String getGroupName(){
		return this.groupName;
	}

	public void setPrCode(String prCode){
		this.prCode=prCode;
	}

	public String getPrCode(){
		return this.prCode;
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

	public void setForcastFlag(Integer forcastFlag){
		this.forcastFlag=forcastFlag;
	}

	public Integer getForcastFlag(){
		return this.forcastFlag;
	}

	public void setTreeCode(String treeCode){
		this.treeCode=treeCode;
	}

	public String getTreeCode(){
		return this.treeCode;
	}

	public void setParentGroupId(Long parentGroupId){
		this.parentGroupId=parentGroupId;
	}

	public Long getParentGroupId(){
		return this.parentGroupId;
	}

	public void setModelCode(String modelCode){
		this.modelCode=modelCode;
	}

	public String getModelCode(){
		return this.modelCode;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setGroupLevel(Integer groupLevel){
		this.groupLevel=groupLevel;
	}

	public Integer getGroupLevel(){
		return this.groupLevel;
	}

	public String getKsid() {
		return ksid;
	}

	public void setKsid(String ksid) {
		this.ksid = ksid;
	}

	public Integer getSalesFlag() {
		return salesFlag;
	}

	public void setSalesFlag(Integer salesFlag) {
		this.salesFlag = salesFlag;
	}

}