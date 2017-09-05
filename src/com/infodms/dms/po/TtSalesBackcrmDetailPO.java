/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-11-19 14:30:35
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtSalesBackcrmDetailPO extends PO{

	private Long vehicleId;
	private Date updateDate;
	private Date arrDate;
	private String remark;
	private Long createBy;
	private Long balId;
	private Long logiId;
	private Long updateBy;
	private Long backcrmId;
	private Date backCrmDate;
	private Long backCrmPer;
	private Long billId;
	private Date createDate;
	private Integer status;
	private Long addressId;

	public Long getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(Long vehicleId) {
		this.vehicleId = vehicleId;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setArrDate(Date arrDate){
		this.arrDate=arrDate;
	}

	public Date getArrDate(){
		return this.arrDate;
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

	public void setBalId(Long balId){
		this.balId=balId;
	}

	public Long getBalId(){
		return this.balId;
	}

	public void setLogiId(Long logiId){
		this.logiId=logiId;
	}

	public Long getLogiId(){
		return this.logiId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setBackcrmId(Long backcrmId){
		this.backcrmId=backcrmId;
	}

	public Long getBackcrmId(){
		return this.backcrmId;
	}

	public void setBackCrmDate(Date backCrmDate){
		this.backCrmDate=backCrmDate;
	}

	public Date getBackCrmDate(){
		return this.backCrmDate;
	}

	public void setBackCrmPer(Long backCrmPer){
		this.backCrmPer=backCrmPer;
	}

	public Long getBackCrmPer(){
		return this.backCrmPer;
	}

	public void setBillId(Long billId){
		this.billId=billId;
	}

	public Long getBillId(){
		return this.billId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}
}