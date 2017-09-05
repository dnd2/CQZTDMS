/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-04-23 13:24:30
* CreateBy   : Yu
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartWarehouseDefinePO extends PO{

	private Integer state;
	private Date disableDate;
	private Date deleteDate;
	private Date updateDate;
	private Long createBy;
	private Integer status;
	private Long whId;
	private String produceFac;
	private Long orgId;
	private String whCode;
	private Long updateBy;
	private String linkman;
	private String tel;
	private String erpWhcode;
	private String whName;
	private Long deleteBy;
	private Long disableBy;
	private Date createDate;
	private String addr;
	private Integer whType;	
	private Integer provinceId;
	private Integer cityId;
	private Integer counties;
	
	
	public Integer getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public Integer getCounties() {
		return counties;
	}

	public void setCounties(Integer counties) {
		this.counties = counties;
	}

	public void setState(Integer state){
		this.state=state;
	}

	public Integer getState(){
		return this.state;
	}

	public void setDisableDate(Date disableDate){
		this.disableDate=disableDate;
	}

	public Date getDisableDate(){
		return this.disableDate;
	}

	public void setDeleteDate(Date deleteDate){
		this.deleteDate=deleteDate;
	}

	public Date getDeleteDate(){
		return this.deleteDate;
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

	public void setWhId(Long whId){
		this.whId=whId;
	}

	public Long getWhId(){
		return this.whId;
	}

	public void setProduceFac(String produceFac){
		this.produceFac=produceFac;
	}

	public String getProduceFac(){
		return this.produceFac;
	}

	public void setOrgId(Long orgId){
		this.orgId=orgId;
	}

	public Long getOrgId(){
		return this.orgId;
	}

	public void setWhCode(String whCode){
		this.whCode=whCode;
	}

	public String getWhCode(){
		return this.whCode;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setLinkman(String linkman){
		this.linkman=linkman;
	}

	public String getLinkman(){
		return this.linkman;
	}

	public void setTel(String tel){
		this.tel=tel;
	}

	public String getTel(){
		return this.tel;
	}

	public void setErpWhcode(String erpWhcode){
		this.erpWhcode=erpWhcode;
	}

	public String getErpWhcode(){
		return this.erpWhcode;
	}

	public void setWhName(String whName){
		this.whName=whName;
	}

	public String getWhName(){
		return this.whName;
	}

	public void setDeleteBy(Long deleteBy){
		this.deleteBy=deleteBy;
	}

	public Long getDeleteBy(){
		return this.deleteBy;
	}

	public void setDisableBy(Long disableBy){
		this.disableBy=disableBy;
	}

	public Long getDisableBy(){
		return this.disableBy;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setAddr(String addr){
		this.addr=addr;
	}

	public String getAddr(){
		return this.addr;
	}

	public void setWhType(Integer whType){
		this.whType=whType;
	}

	public Integer getWhType(){
		return this.whType;
	}

}