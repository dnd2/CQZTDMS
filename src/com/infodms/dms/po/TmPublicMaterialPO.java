/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-03-12 14:50:47
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmPublicMaterialPO extends PO{

	private Double salePrice;
	private Integer freeStatus;
	private Double purchasePrice;
	private Date updateDate;
	private Long createBy;
	private String publicMaterialCode;
	private Date createDate;
	private Integer confirmStatus;
	private String publicMaterialName;
	private Long publicMaterialId;
	private Long status;
	private Long updateBy;
	private String unit;

	public void setSalePrice(Double salePrice){
		this.salePrice=salePrice;
	}

	public Double getSalePrice(){
		return this.salePrice;
	}

	public void setFreeStatus(Integer freeStatus){
		this.freeStatus=freeStatus;
	}

	public Integer getFreeStatus(){
		return this.freeStatus;
	}

	public void setPurchasePrice(Double purchasePrice){
		this.purchasePrice=purchasePrice;
	}

	public Double getPurchasePrice(){
		return this.purchasePrice;
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

	public void setPublicMaterialCode(String publicMaterialCode){
		this.publicMaterialCode=publicMaterialCode;
	}

	public String getPublicMaterialCode(){
		return this.publicMaterialCode;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setConfirmStatus(Integer confirmStatus){
		this.confirmStatus=confirmStatus;
	}

	public Integer getConfirmStatus(){
		return this.confirmStatus;
	}

	public void setPublicMaterialName(String publicMaterialName){
		this.publicMaterialName=publicMaterialName;
	}

	public String getPublicMaterialName(){
		return this.publicMaterialName;
	}

	public void setPublicMaterialId(Long publicMaterialId){
		this.publicMaterialId=publicMaterialId;
	}

	public Long getPublicMaterialId(){
		return this.publicMaterialId;
	}

	public void setStatus(Long status){
		this.status=status;
	}

	public Long getStatus(){
		return this.status;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setUnit(String unit){
		this.unit=unit;
	}

	public String getUnit(){
		return this.unit;
	}

}