/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-12-02 17:34:13
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TPcOrderAuditPO extends PO{

	private String managerAuditRemark;
	private Long orderId;
	private Integer managerAudit;
	private String updateBy;
	private Date updateDate;
	private Long orderAuditId;
	private String createBy;
	private Long customerId;
	private Integer updateType;
	private Date createDate;
	private Integer status;
	private String reasonRemark;
	private String old_customer_name;
	private String old_telephone;
	private String old_vehicle_id;
	private String old_relation_code;

	public String getOld_customer_name() {
		return old_customer_name;
	}

	public void setOld_customer_name(String oldCustomerName) {
		old_customer_name = oldCustomerName;
	}

	public String getOld_telephone() {
		return old_telephone;
	}

	public void setOld_telephone(String oldTelephone) {
		old_telephone = oldTelephone;
	}

	public String getOld_vehicle_id() {
		return old_vehicle_id;
	}

	public void setOld_vehicle_id(String oldVehicleId) {
		old_vehicle_id = oldVehicleId;
	}

	public String getOld_relation_code() {
		return old_relation_code;
	}

	public void setOld_relation_code(String oldRelationCode) {
		old_relation_code = oldRelationCode;
	}

	public String getReasonRemark() {
		return reasonRemark;
	}

	public void setReasonRemark(String reasonRemark) {
		this.reasonRemark = reasonRemark;
	}

	public void setManagerAuditRemark(String managerAuditRemark){
		this.managerAuditRemark=managerAuditRemark;
	}

	public String getManagerAuditRemark(){
		return this.managerAuditRemark;
	}

	public void setOrderId(Long orderId){
		this.orderId=orderId;
	}

	public Long getOrderId(){
		return this.orderId;
	}

	public void setManagerAudit(Integer managerAudit){
		this.managerAudit=managerAudit;
	}

	public Integer getManagerAudit(){
		return this.managerAudit;
	}

	public void setUpdateBy(String updateBy){
		this.updateBy=updateBy;
	}

	public String getUpdateBy(){
		return this.updateBy;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setOrderAuditId(Long orderAuditId){
		this.orderAuditId=orderAuditId;
	}

	public Long getOrderAuditId(){
		return this.orderAuditId;
	}

	public void setCreateBy(String createBy){
		this.createBy=createBy;
	}

	public String getCreateBy(){
		return this.createBy;
	}

	public void setCustomerId(Long customerId){
		this.customerId=customerId;
	}

	public Long getCustomerId(){
		return this.customerId;
	}

	public void setUpdateType(Integer updateType){
		this.updateType=updateType;
	}

	public Integer getUpdateType(){
		return this.updateType;
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

}