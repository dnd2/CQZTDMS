/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2015-03-02 14:40:44
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsAccountTypePO extends PO{

	private Long companyId;
	private Date updateDate;
	private Long createBy;
	private Float discountRate;
	private Integer status;
	private String erpType;
	private String typeName;
	private Integer isDiscount;
	private Long updateBy;
	private Integer typeClass;
	private Long typeId;
	private Date createDate;
	private Integer isUseOrderAccount;
	private String typeCode;
	
	private String markCode;//标识码
	

	public String getMarkCode() {
		return markCode;
	}

	public void setMarkCode(String markCode) {
		this.markCode = markCode;
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

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setDiscountRate(Float discountRate){
		this.discountRate=discountRate;
	}

	public Float getDiscountRate(){
		return this.discountRate;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setErpType(String erpType){
		this.erpType=erpType;
	}

	public String getErpType(){
		return this.erpType;
	}

	public void setTypeName(String typeName){
		this.typeName=typeName;
	}

	public String getTypeName(){
		return this.typeName;
	}

	public void setIsDiscount(Integer isDiscount){
		this.isDiscount=isDiscount;
	}

	public Integer getIsDiscount(){
		return this.isDiscount;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setTypeClass(Integer typeClass){
		this.typeClass=typeClass;
	}

	public Integer getTypeClass(){
		return this.typeClass;
	}

	public void setTypeId(Long typeId){
		this.typeId=typeId;
	}

	public Long getTypeId(){
		return this.typeId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setIsUseOrderAccount(Integer isUseOrderAccount){
		this.isUseOrderAccount=isUseOrderAccount;
	}

	public Integer getIsUseOrderAccount(){
		return this.isUseOrderAccount;
	}

	public void setTypeCode(String typeCode){
		this.typeCode=typeCode;
	}

	public String getTypeCode(){
		return this.typeCode;
	}

}