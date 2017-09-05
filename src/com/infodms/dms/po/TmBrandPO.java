/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-02-26 06:35:52
* CreateBy   : tianheng
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmBrandPO extends PO{

	private Date updateDate;
	private Long createBy;
	private String billTotal;
	private Date createDate;
	private String brandName;
	private String englishName;
	private String brandCode;
	private Integer oldModelSift;
	private Long brandId;
	private Integer status;
	private Long updateBy;
	private String businessType;
	private Integer isDown;
	private String remark;

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

	public void setBillTotal(String billTotal){
		this.billTotal=billTotal;
	}

	public String getBillTotal(){
		return this.billTotal;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setBrandName(String brandName){
		this.brandName=brandName;
	}

	public String getBrandName(){
		return this.brandName;
	}

	public void setEnglishName(String englishName){
		this.englishName=englishName;
	}

	public String getEnglishName(){
		return this.englishName;
	}

	public void setBrandCode(String brandCode){
		this.brandCode=brandCode;
	}

	public String getBrandCode(){
		return this.brandCode;
	}

	public void setOldModelSift(Integer oldModelSift){
		this.oldModelSift=oldModelSift;
	}

	public Integer getOldModelSift(){
		return this.oldModelSift;
	}

	public void setBrandId(Long brandId){
		this.brandId=brandId;
	}

	public Long getBrandId(){
		return this.brandId;
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

	public void setBusinessType(String businessType){
		this.businessType=businessType;
	}

	public String getBusinessType(){
		return this.businessType;
	}

	public void setIsDown(Integer isDown){
		this.isDown=isDown;
	}

	public Integer getIsDown(){
		return this.isDown;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

}