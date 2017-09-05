/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-09-16 14:13:09
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsActivityPartsPO extends PO{

	private Integer isMainPart;
	private String partNo;
	private Date updateDate;
	private Long partsId;
	private Float partQuantity;
	private Float partPrice;
	private Long createBy;
	private Long activityId;
	private Date createDate;
	private String supplierCode;
	private String partUnit;
	private Integer isGua;
	private String partName;
	private Long updateBy;
	private String supplierName;
	private Float partAmount;

	public void setIsMainPart(Integer isMainPart){
		this.isMainPart=isMainPart;
	}

	public Integer getIsMainPart(){
		return this.isMainPart;
	}

	public void setPartNo(String partNo){
		this.partNo=partNo;
	}

	public String getPartNo(){
		return this.partNo;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setPartsId(Long partsId){
		this.partsId=partsId;
	}

	public Long getPartsId(){
		return this.partsId;
	}

	public void setPartQuantity(Float partQuantity){
		this.partQuantity=partQuantity;
	}

	public Float getPartQuantity(){
		return this.partQuantity;
	}

	public void setPartPrice(Float partPrice){
		this.partPrice=partPrice;
	}

	public Float getPartPrice(){
		return this.partPrice;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setActivityId(Long activityId){
		this.activityId=activityId;
	}

	public Long getActivityId(){
		return this.activityId;
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

	public void setPartUnit(String partUnit){
		this.partUnit=partUnit;
	}

	public String getPartUnit(){
		return this.partUnit;
	}

	public void setIsGua(Integer isGua){
		this.isGua=isGua;
	}

	public Integer getIsGua(){
		return this.isGua;
	}

	public void setPartName(String partName){
		this.partName=partName;
	}

	public String getPartName(){
		return this.partName;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setSupplierName(String supplierName){
		this.supplierName=supplierName;
	}

	public String getSupplierName(){
		return this.supplierName;
	}

	public void setPartAmount(Float partAmount){
		this.partAmount=partAmount;
	}

	public Float getPartAmount(){
		return this.partAmount;
	}

}