/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-05-29 08:08:52
* CreateBy   : zhuwei
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrWarehousePO extends PO{

	private Date updateDate;
	private Long warehouseId;
	private Long createBy;
	private Date createDate;
	private Integer stockProportion;
	private Long dealerId;
	private String dealerName;
	private Integer status;
	private String dealerCode;
	private Long updateBy;
	private String warehouseCode;
	private String topupWarehouseCode;
	private String remark;
	private Integer isOem;
	private Integer isAllocation;
	private Integer isRechard;
	private Long shiftWarehouseBy;
	private Date shiftWarehouseDate;
	
	public Long getShiftWarehouseBy() {
		return shiftWarehouseBy;
	}

	public void setShiftWarehouseBy(Long shiftWarehouseBy) {
		this.shiftWarehouseBy = shiftWarehouseBy;
	}

	public Date getShiftWarehouseDate() {
		return shiftWarehouseDate;
	}

	public void setShiftWarehouseDate(Date shiftWarehouseDate) {
		this.shiftWarehouseDate = shiftWarehouseDate;
	}

	public Integer getIsOem() {
		return isOem;
	}

	public void setIsOem(Integer isOem) {
		this.isOem = isOem;
	}

	public Integer getIsAllocation() {
		return isAllocation;
	}

	public void setIsAllocation(Integer isAllocation) {
		this.isAllocation = isAllocation;
	}

	public Integer getIsRechard() {
		return isRechard;
	}

	public void setIsRechard(Integer isRechard) {
		this.isRechard = isRechard;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setWarehouseId(Long warehouseId){
		this.warehouseId=warehouseId;
	}

	public Long getWarehouseId(){
		return this.warehouseId;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setStockProportion(Integer stockProportion){
		this.stockProportion=stockProportion;
	}

	public Integer getStockProportion(){
		return this.stockProportion;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setDealerName(String dealerName){
		this.dealerName=dealerName;
	}

	public String getDealerName(){
		return this.dealerName;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setDealerCode(String dealerCode){
		this.dealerCode=dealerCode;
	}

	public String getDealerCode(){
		return this.dealerCode;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setWarehouseCode(String warehouseCode){
		this.warehouseCode=warehouseCode;
	}

	public String getWarehouseCode(){
		return this.warehouseCode;
	}

	public void setTopupWarehouseCode(String topupWarehouseCode){
		this.topupWarehouseCode=topupWarehouseCode;
	}

	public String getTopupWarehouseCode(){
		return this.topupWarehouseCode;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

}