/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-09-29 13:47:30
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmWarehousePO extends PO{

	private Integer warehouseType;
	private Integer warehouseLevel;
	private Long dealerId;
	private String warehouseName;
	private Long warehouseId;
	private Long companyId;
	private String warehouseCode;
	private Date updateDate;
	private String erpWarehouseCode;
	private Long createBy;
	private Integer status;
	private String erpWarehouseName;
	private Long updateBy;
	private Long erpOrganizationId;
	private Date createDate;
	private Long areaId;
	private String provCode;
	private String cityCode;
	private String countyCode;
	private String address;	
	private String linkMan;
	private String tel;
	
	public String getProvCode() {
		return provCode;
	}

	public void setProvCode(String provCode) {
		this.provCode = provCode;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getCountyCode() {
		return countyCode;
	}

	public void setCountyCode(String countyCode) {
		this.countyCode = countyCode;
	}

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public void setWarehouseType(Integer warehouseType){
		this.warehouseType=warehouseType;
	}

	public Integer getWarehouseType(){
		return this.warehouseType;
	}

	public void setWarehouseLevel(Integer warehouseLevel){
		this.warehouseLevel=warehouseLevel;
	}

	public Integer getWarehouseLevel(){
		return this.warehouseLevel;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setWarehouseName(String warehouseName){
		this.warehouseName=warehouseName;
	}

	public String getWarehouseName(){
		return this.warehouseName;
	}

	public void setWarehouseId(Long warehouseId){
		this.warehouseId=warehouseId;
	}

	public Long getWarehouseId(){
		return this.warehouseId;
	}

	public void setCompanyId(Long companyId){
		this.companyId=companyId;
	}

	public Long getCompanyId(){
		return this.companyId;
	}

	public void setWarehouseCode(String warehouseCode){
		this.warehouseCode=warehouseCode;
	}

	public String getWarehouseCode(){
		return this.warehouseCode;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setErpWarehouseCode(String erpWarehouseCode){
		this.erpWarehouseCode=erpWarehouseCode;
	}

	public String getErpWarehouseCode(){
		return this.erpWarehouseCode;
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

	public void setErpWarehouseName(String erpWarehouseName){
		this.erpWarehouseName=erpWarehouseName;
	}

	public String getErpWarehouseName(){
		return this.erpWarehouseName;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setErpOrganizationId(Long erpOrganizationId){
		this.erpOrganizationId=erpOrganizationId;
	}

	public Long getErpOrganizationId(){
		return this.erpOrganizationId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}
		
	public String getAddress(){
		return address;
	}
	
	public String getLinkMan(){
		return linkMan;
	}
	
	public String getTel(){
		return tel;
	}
	
	public void setAddress(String address){
		this.address = address;
	}
	
	public void setLinkMan(String linkMan){
		this.linkMan  = linkMan;
	}
	
	public void setTel(String tel){
		this.tel = tel;
	}
	
	
}