/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-12-18 16:04:41
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtDealerVehicleReadyPO extends PO{

	private Long dealerId;
	private String seriesName;
	private Long subBy;
	private Date deleteDate;
	private Long createBy;
	private String remark;
	private Integer total;
	private Integer todayTotal;
	private Integer status;
	private String modelName;
	private Date subDate;
	private Long materialId;
	private Long modelId;
	private Long id;
	private Long seriesId;
	private Long packageId;
	private Long deleteBy;
	private Date createDate;
	private String packageName;

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setSeriesName(String seriesName){
		this.seriesName=seriesName;
	}

	public String getSeriesName(){
		return this.seriesName;
	}

	public void setSubBy(Long subBy){
		this.subBy=subBy;
	}

	public Long getSubBy(){
		return this.subBy;
	}

	public void setDeleteDate(Date deleteDate){
		this.deleteDate=deleteDate;
	}

	public Date getDeleteDate(){
		return this.deleteDate;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setTotal(Integer total){
		this.total=total;
	}

	public Integer getTotal(){
		return this.total;
	}

	public void setTodayTotal(Integer todayTotal){
		this.todayTotal=todayTotal;
	}

	public Integer getTodayTotal(){
		return this.todayTotal;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setModelName(String modelName){
		this.modelName=modelName;
	}

	public String getModelName(){
		return this.modelName;
	}

	public void setSubDate(Date subDate){
		this.subDate=subDate;
	}

	public Date getSubDate(){
		return this.subDate;
	}

	public void setMaterialId(Long materialId){
		this.materialId=materialId;
	}

	public Long getMaterialId(){
		return this.materialId;
	}

	public void setModelId(Long modelId){
		this.modelId=modelId;
	}

	public Long getModelId(){
		return this.modelId;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setSeriesId(Long seriesId){
		this.seriesId=seriesId;
	}

	public Long getSeriesId(){
		return this.seriesId;
	}

	public void setPackageId(Long packageId){
		this.packageId=packageId;
	}

	public Long getPackageId(){
		return this.packageId;
	}

	public void setDeleteBy(Long deleteBy){
		this.deleteBy=deleteBy;
	}

	public Long getDeleteBy(){
		return this.deleteBy;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setPackageName(String packageName){
		this.packageName=packageName;
	}

	public String getPackageName(){
		return this.packageName;
	}

}