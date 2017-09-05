/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-09-20 11:43:10
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmPtSupplierPO extends PO{

	private Date updateDate;
	private String zipcode;
	private Long createBy;
	private String phoneNumber;
	private String fax;
	private Date createDate;
	private String supplierCode;
	private String shortName;
	private String linkMan;
	private Integer isArc;
	private Long updateBy;
	private Integer stopFlag;
	private Long supplierId;
	private String supplierName;
	private String remark;
	private Integer isDel;
	private String address;

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setZipcode(String zipcode){
		this.zipcode=zipcode;
	}

	public String getZipcode(){
		return this.zipcode;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setPhoneNumber(String phoneNumber){
		this.phoneNumber=phoneNumber;
	}

	public String getPhoneNumber(){
		return this.phoneNumber;
	}

	public void setFax(String fax){
		this.fax=fax;
	}

	public String getFax(){
		return this.fax;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setSupplierCode(String supplierCode){
		this.supplierCode=supplierCode;
	}

	public String getSupplierCode(){
		return this.supplierCode;
	}

	public void setShortName(String shortName){
		this.shortName=shortName;
	}

	public String getShortName(){
		return this.shortName;
	}

	public void setLinkMan(String linkMan){
		this.linkMan=linkMan;
	}

	public String getLinkMan(){
		return this.linkMan;
	}

	public void setIsArc(Integer isArc){
		this.isArc=isArc;
	}

	public Integer getIsArc(){
		return this.isArc;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setStopFlag(Integer stopFlag){
		this.stopFlag=stopFlag;
	}

	public Integer getStopFlag(){
		return this.stopFlag;
	}

	public void setSupplierId(Long supplierId){
		this.supplierId=supplierId;
	}

	public Long getSupplierId(){
		return this.supplierId;
	}

	public void setSupplierName(String supplierName){
		this.supplierName=supplierName;
	}

	public String getSupplierName(){
		return this.supplierName;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setIsDel(Integer isDel){
		this.isDel=isDel;
	}

	public Integer getIsDel(){
		return this.isDel;
	}

	public void setAddress(String address){
		this.address=address;
	}

	public String getAddress(){
		return this.address;
	}

}