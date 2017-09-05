/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-03-03 11:36:25
* CreateBy   : wangsongwei
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmCompanyPO extends PO{

	private Integer handleByHandle;
	private Long companyId;
	private String companyName;
	private Date updateDate;
	private Integer isSamePerson;
	private Long createBy;
	private Long relationCompany;
	private Integer status;
	private String phone;
	private Integer companyType;
	private Long oemCompanyId;
	private String companyShortname;
	private String erpOrgCode;
	private Long updateBy;
	private Integer isBeforeAfter;
	private String companyCode;
	private Date createDate;
	private String topupCode;
	private String fax;
	private Long provinceId;
	private Long cityId;
	private String address ;
	private String zipCode ;

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public void setHandleByHandle(Integer handleByHandle){
		this.handleByHandle=handleByHandle;
	}

	public Integer getHandleByHandle(){
		return this.handleByHandle;
	}

	public void setCompanyId(Long companyId){
		this.companyId=companyId;
	}

	public Long getCompanyId(){
		return this.companyId;
	}

	public void setCompanyName(String companyName){
		this.companyName=companyName;
	}

	public String getCompanyName(){
		return this.companyName;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setIsSamePerson(Integer isSamePerson){
		this.isSamePerson=isSamePerson;
	}

	public Integer getIsSamePerson(){
		return this.isSamePerson;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setRelationCompany(Long relationCompany){
		this.relationCompany=relationCompany;
	}

	public Long getRelationCompany(){
		return this.relationCompany;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setPhone(String phone){
		this.phone=phone;
	}

	public String getPhone(){
		return this.phone;
	}

	public void setCompanyType(Integer companyType){
		this.companyType=companyType;
	}

	public Integer getCompanyType(){
		return this.companyType;
	}

	public void setOemCompanyId(Long oemCompanyId){
		this.oemCompanyId=oemCompanyId;
	}

	public Long getOemCompanyId(){
		return this.oemCompanyId;
	}

	public void setCompanyShortname(String companyShortname){
		this.companyShortname=companyShortname;
	}

	public String getCompanyShortname(){
		return this.companyShortname;
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

	public void setIsBeforeAfter(Integer isBeforeAfter){
		this.isBeforeAfter=isBeforeAfter;
	}

	public Integer getIsBeforeAfter(){
		return this.isBeforeAfter;
	}

	public void setCompanyCode(String companyCode){
		this.companyCode=companyCode;
	}

	public String getCompanyCode(){
		return this.companyCode;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setTopupCode(String topupCode){
		this.topupCode=topupCode;
	}

	public String getTopupCode(){
		return this.topupCode;
	}

	public void setFax(String fax){
		this.fax=fax;
	}

	public String getFax(){
		return this.fax;
	}

}