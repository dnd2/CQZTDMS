/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-05-29 09:59:40
* CreateBy   : zhuwei
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPtWarehouseRecardPO extends PO{

	private Date topDate;
	private String partNo;
	private Date updateDate;
	private Float stockOutQuantity;
	private Integer topSuccess;
	private String dmsWarehouseCode;
	private Long repairPartId;
	private Float stockInQuantity;
	private Long createBy;
	private String entityCode;
	private String storageCode;
	private Date createDate;
	private String sheetNo;
	private String partName;
	private String topWarehouseCode;
	private Integer inOutType;
	private Date operateDate;
	private Long updateBy;
	private Integer dmsSuccess;
	private Float stockQuantity;
	private Integer dataSource;
	private Long flowId;
	private Date dmsDate;
	private Integer inOutTag;
	private Integer wjFlag;

	public Integer getWjFlag() {
		return wjFlag;
	}

	public void setWjFlag(Integer wjFlag) {
		this.wjFlag = wjFlag;
	}

	public void setTopDate(Date topDate){
		this.topDate=topDate;
	}

	public Date getTopDate(){
		return this.topDate;
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

	public void setStockOutQuantity(Float stockOutQuantity){
		this.stockOutQuantity=stockOutQuantity;
	}

	public Float getStockOutQuantity(){
		return this.stockOutQuantity;
	}

	public void setTopSuccess(Integer topSuccess){
		this.topSuccess=topSuccess;
	}

	public Integer getTopSuccess(){
		return this.topSuccess;
	}

	public void setDmsWarehouseCode(String dmsWarehouseCode){
		this.dmsWarehouseCode=dmsWarehouseCode;
	}

	public String getDmsWarehouseCode(){
		return this.dmsWarehouseCode;
	}

	public void setRepairPartId(Long repairPartId){
		this.repairPartId=repairPartId;
	}

	public Long getRepairPartId(){
		return this.repairPartId;
	}

	public void setStockInQuantity(Float stockInQuantity){
		this.stockInQuantity=stockInQuantity;
	}

	public Float getStockInQuantity(){
		return this.stockInQuantity;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setEntityCode(String entityCode){
		this.entityCode=entityCode;
	}

	public String getEntityCode(){
		return this.entityCode;
	}

	public void setStorageCode(String storageCode){
		this.storageCode=storageCode;
	}

	public String getStorageCode(){
		return this.storageCode;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setSheetNo(String sheetNo){
		this.sheetNo=sheetNo;
	}

	public String getSheetNo(){
		return this.sheetNo;
	}

	public void setPartName(String partName){
		this.partName=partName;
	}

	public String getPartName(){
		return this.partName;
	}

	public void setTopWarehouseCode(String topWarehouseCode){
		this.topWarehouseCode=topWarehouseCode;
	}

	public String getTopWarehouseCode(){
		return this.topWarehouseCode;
	}

	public void setInOutType(Integer inOutType){
		this.inOutType=inOutType;
	}

	public Integer getInOutType(){
		return this.inOutType;
	}

	public void setOperateDate(Date operateDate){
		this.operateDate=operateDate;
	}

	public Date getOperateDate(){
		return this.operateDate;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setDmsSuccess(Integer dmsSuccess){
		this.dmsSuccess=dmsSuccess;
	}

	public Integer getDmsSuccess(){
		return this.dmsSuccess;
	}

	public void setStockQuantity(Float stockQuantity){
		this.stockQuantity=stockQuantity;
	}

	public Float getStockQuantity(){
		return this.stockQuantity;
	}

	public void setDataSource(Integer dataSource){
		this.dataSource=dataSource;
	}

	public Integer getDataSource(){
		return this.dataSource;
	}

	public void setFlowId(Long flowId){
		this.flowId=flowId;
	}

	public Long getFlowId(){
		return this.flowId;
	}

	public void setDmsDate(Date dmsDate){
		this.dmsDate=dmsDate;
	}

	public Date getDmsDate(){
		return this.dmsDate;
	}

	public void setInOutTag(Integer inOutTag){
		this.inOutTag=inOutTag;
	}

	public Integer getInOutTag(){
		return this.inOutTag;
	}

}