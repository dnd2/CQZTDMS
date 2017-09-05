/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-07-14 10:43:15
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class VwMaterialGroupServicePO extends PO{

	private String brandCode;
	private Long seriesId;
	private String packageName;
	private Long brandId;
	private String seriesName;
	private String modelName;
	private String seriesCode;
	private Long modelId;
	private Long packageId;
	private String modelCode;
	private String brandName;
	private String packageCode;

	public void setBrandCode(String brandCode){
		this.brandCode=brandCode;
	}

	public String getBrandCode(){
		return this.brandCode;
	}

	public void setSeriesId(Long seriesId){
		this.seriesId=seriesId;
	}

	public Long getSeriesId(){
		return this.seriesId;
	}

	public void setPackageName(String packageName){
		this.packageName=packageName;
	}

	public String getPackageName(){
		return this.packageName;
	}

	public void setBrandId(Long brandId){
		this.brandId=brandId;
	}

	public Long getBrandId(){
		return this.brandId;
	}

	public void setSeriesName(String seriesName){
		this.seriesName=seriesName;
	}

	public String getSeriesName(){
		return this.seriesName;
	}

	public void setModelName(String modelName){
		this.modelName=modelName;
	}

	public String getModelName(){
		return this.modelName;
	}

	public void setSeriesCode(String seriesCode){
		this.seriesCode=seriesCode;
	}

	public String getSeriesCode(){
		return this.seriesCode;
	}

	public void setModelId(Long modelId){
		this.modelId=modelId;
	}

	public Long getModelId(){
		return this.modelId;
	}

	public void setPackageId(Long packageId){
		this.packageId=packageId;
	}

	public Long getPackageId(){
		return this.packageId;
	}

	public void setModelCode(String modelCode){
		this.modelCode=modelCode;
	}

	public String getModelCode(){
		return this.modelCode;
	}

	public void setBrandName(String brandName){
		this.brandName=brandName;
	}

	public String getBrandName(){
		return this.brandName;
	}

	public void setPackageCode(String packageCode){
		this.packageCode=packageCode;
	}

	public String getPackageCode(){
		return this.packageCode;
	}

}