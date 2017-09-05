/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-09-30 18:47:48
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsOrderResourceReservePO extends PO{

	private Date updateDate;
	private Long warehouseId;
	private String batchNo;
	private Long createBy;
	private Integer reserveStatus;
	private Date createDate;
	private Long reqDetailId;
	private Integer amount;
	private Long updateBy;
	private Long reserveId;
	private Long materialId;
	private Long oemCompanyId;
	private Integer deliveryAmount;
	private Integer reserveType;
	private Long stoId;
	private String warehouseCode;
	
	private Long orderDetailId;
	private Long orgId;
	private Long createId;

	
	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public Long getCreateId() {
		return createId;
	}

	public void setCreateId(Long createId) {
		this.createId = createId;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getOrderDetailId() {
		return orderDetailId;
	}

	public void setOrderDetailId(Long orderDetailId) {
		this.orderDetailId = orderDetailId;
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

	public void setBatchNo(String batchNo){
		this.batchNo=batchNo;
	}

	public String getBatchNo(){
		return this.batchNo;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setReserveStatus(Integer reserveStatus){
		this.reserveStatus=reserveStatus;
	}

	public Integer getReserveStatus(){
		return this.reserveStatus;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setReqDetailId(Long reqDetailId){
		this.reqDetailId=reqDetailId;
	}

	public Long getReqDetailId(){
		return this.reqDetailId;
	}

	public void setAmount(Integer amount){
		this.amount=amount;
	}

	public Integer getAmount(){
		return this.amount;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setReserveId(Long reserveId){
		this.reserveId=reserveId;
	}

	public Long getReserveId(){
		return this.reserveId;
	}

	public void setMaterialId(Long materialId){
		this.materialId=materialId;
	}

	public Long getMaterialId(){
		return this.materialId;
	}

	public void setOemCompanyId(Long oemCompanyId){
		this.oemCompanyId=oemCompanyId;
	}

	public Long getOemCompanyId(){
		return this.oemCompanyId;
	}

	public void setDeliveryAmount(Integer deliveryAmount){
		this.deliveryAmount=deliveryAmount;
	}

	public Integer getDeliveryAmount(){
		return this.deliveryAmount;
	}

	public void setReserveType(Integer reserveType){
		this.reserveType=reserveType;
	}

	public Integer getReserveType(){
		return this.reserveType;
	}

	public void setStoId(Long stoId){
		this.stoId=stoId;
	}

	public Long getStoId(){
		return this.stoId;
	}

}