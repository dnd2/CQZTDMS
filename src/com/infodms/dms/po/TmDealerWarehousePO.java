/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-08-20 13:33:51
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmDealerWarehousePO extends PO{

	private Integer warehouseType;
	private String warehouseName;
	private Long updateBy;
	private Long warehouseId;
	private Long companyId;
	private Date updateDate;
	private String remark;
	private Long createBy;
	private Date createDate;
	private Integer status;
	private Long dealerComanyId;
	private Long dealerId;
	private Long manageDealerId ;

	public Long getDealerId() {
		return dealerId;
	}

	public void setDealerId(Long dealerId) {
		this.dealerId = dealerId;
	}

	public Long getManageDealerId() {
		return manageDealerId;
	}

	public void setManageDealerId(Long manageDealerId) {
		this.manageDealerId = manageDealerId;
	}

	public void setWarehouseType(Integer warehouseType){
		this.warehouseType=warehouseType;
	}

	public Integer getWarehouseType(){
		return this.warehouseType;
	}

	public void setWarehouseName(String warehouseName){
		this.warehouseName=warehouseName;
	}

	public String getWarehouseName(){
		return this.warehouseName;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setWarehouseId(Long warehouseId){
		this.warehouseId=warehouseId;
	}

	public Long getWarehouseId(){
		return this.warehouseId;
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

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
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

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setDealerComanyId(Long dealerComanyId){
		this.dealerComanyId=dealerComanyId;
	}

	public Long getDealerComanyId(){
		return this.dealerComanyId;
	}

}