/*
-- Add/modify columns 
alter table TT_VS_DLVRY_REQ add REQ_REMARK varchar2(1000);
-- Add comments to the columns 
comment on column TT_VS_DLVRY_REQ.REQ_REMARK
  is '发运申请备注';* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-09-14 11:57:05
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsDlvryReqPO extends PO{

	private Long addressId;
	private Double reqTotalPrice;
	private Integer reqStatus;
	private Integer reserveTotalAmount;
	private Long warehouseId;
	private Date updateDate;
	private Long createBy;
	private Long areaId;
	private Integer isFleet;
	private String fleetAddress;
	private Long updateBy;
	private String tel;
	private Double discount;
	private String dlvryReqNo;
	private Long fleetId;
	private Long receiver;
	private Integer reqExecStatus;
	private String linkMan;
	private Date reqDate;
	private Integer deliveryType;
	private Long orderId;
	private Long reqId;
	private String otherPriceReason;
	private Integer ver;
	private Integer reqTotalAmount;
	private Date createDate;
	private Long fundType;
	private Integer modifyFlag;
	private Integer callLeavel ;
	private String reqRemark ;
	
	private String priceId;
	private Integer tmpLicenseAmount;
	private Date auditTime;
	private Date fAuditTime;
	private Long dealerId;
	private Integer isRebate;
	private Integer isRebateBill;
	private Double rebateAmount;
	private Integer orderType;
	
	
	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public Date getfAuditTime() {
		return fAuditTime;
	}

	public void setfAuditTime(Date fAuditTime) {
		this.fAuditTime = fAuditTime;
	}

	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	public Integer getTmpLicenseAmount() {
		return tmpLicenseAmount;
	}

	public void setTmpLicenseAmount(Integer tmpLicenseAmount) {
		this.tmpLicenseAmount = tmpLicenseAmount;
	}

	public String getPriceId() {
		return priceId;
	}

	public void setPriceId(String priceId) {
		this.priceId = priceId;
	}

	public String getReqRemark() {
		return reqRemark;
	}

	public void setReqRemark(String reqRemark) {
		this.reqRemark = reqRemark;
	}
	
	
	public Integer getCallLeavel() {
		return callLeavel;
	}

	public void setCallLeavel(Integer callLeavel) {
		this.callLeavel = callLeavel;
	}

	public Long getOrderDealerId() {
		return OrderDealerId;
	}

	public void setOrderDealerId(Long orderDealerId) {
		OrderDealerId = orderDealerId;
	}

	public Long getBilDealerId() {
		return BilDealerId;
	}

	public void setBilDealerId(Long bilDealerId) {
		BilDealerId = bilDealerId;
	}

	private Long OrderDealerId ;
	private Long BilDealerId ;

	public void setAddressId(Long addressId){
		this.addressId=addressId;
	}

	public Long getAddressId(){
		return this.addressId;
	}

	public void setReqTotalPrice(Double reqTotalPrice){
		this.reqTotalPrice=reqTotalPrice;
	}

	public Double getReqTotalPrice(){
		return this.reqTotalPrice;
	}

	public void setReqStatus(Integer reqStatus){
		this.reqStatus=reqStatus;
	}

	public Integer getReqStatus(){
		return this.reqStatus;
	}

	public void setReserveTotalAmount(Integer reserveTotalAmount){
		this.reserveTotalAmount=reserveTotalAmount;
	}

	public Integer getReserveTotalAmount(){
		return this.reserveTotalAmount;
	}

	public void setWarehouseId(Long warehouseId){
		this.warehouseId=warehouseId;
	}

	public Long getWarehouseId(){
		return this.warehouseId;
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

	public void setAreaId(Long areaId){
		this.areaId=areaId;
	}

	public Long getAreaId(){
		return this.areaId;
	}

	public void setIsFleet(Integer isFleet){
		this.isFleet=isFleet;
	}

	public Integer getIsFleet(){
		return this.isFleet;
	}

	public void setFleetAddress(String fleetAddress){
		this.fleetAddress=fleetAddress;
	}

	public String getFleetAddress(){
		return this.fleetAddress;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setTel(String tel){
		this.tel=tel;
	}

	public String getTel(){
		return this.tel;
	}


	public void setDiscount(Double discount){
		this.discount=discount;
	}

	public Double getDiscount(){
		return this.discount;
	}

	public void setDlvryReqNo(String dlvryReqNo){
		this.dlvryReqNo=dlvryReqNo;
	}

	public String getDlvryReqNo(){
		return this.dlvryReqNo;
	}

	public void setFleetId(Long fleetId){
		this.fleetId=fleetId;
	}

	public Long getFleetId(){
		return this.fleetId;
	}

	public void setReceiver(Long receiver){
		this.receiver=receiver;
	}

	public Long getReceiver(){
		return this.receiver;
	}

	public void setReqExecStatus(Integer reqExecStatus){
		this.reqExecStatus=reqExecStatus;
	}

	public Integer getReqExecStatus(){
		return this.reqExecStatus;
	}

	public void setLinkMan(String linkMan){
		this.linkMan=linkMan;
	}

	public String getLinkMan(){
		return this.linkMan;
	}

	public void setReqDate(Date reqDate){
		this.reqDate=reqDate;
	}

	public Date getReqDate(){
		return this.reqDate;
	}

	public void setDeliveryType(Integer deliveryType){
		this.deliveryType=deliveryType;
	}

	public Integer getDeliveryType(){
		return this.deliveryType;
	}

	public void setOrderId(Long orderId){
		this.orderId=orderId;
	}

	public Long getOrderId(){
		return this.orderId;
	}

	public void setReqId(Long reqId){
		this.reqId=reqId;
	}

	public Long getReqId(){
		return this.reqId;
	}

	public void setOtherPriceReason(String otherPriceReason){
		this.otherPriceReason=otherPriceReason;
	}

	public String getOtherPriceReason(){
		return this.otherPriceReason;
	}

	public void setVer(Integer ver){
		this.ver=ver;
	}

	public Integer getVer(){
		return this.ver;
	}

	public void setReqTotalAmount(Integer reqTotalAmount){
		this.reqTotalAmount=reqTotalAmount;
	}

	public Integer getReqTotalAmount(){
		return this.reqTotalAmount;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setFundType(Long fundType){
		this.fundType=fundType;
	}

	public Long getFundType(){
		return this.fundType;
	}

	public void setModifyFlag(Integer modifyFlag){
		this.modifyFlag=modifyFlag;
	}

	public Integer getModifyFlag(){
		return this.modifyFlag;
	}

	public Long getDealerId() {
		return dealerId;
	}

	public void setDealerId(Long dealerId) {
		this.dealerId = dealerId;
	}

	public Integer getIsRebate() {
		return isRebate;
	}

	public void setIsRebate(Integer isRebate) {
		this.isRebate = isRebate;
	}

	public Integer getIsRebateBill() {
		return isRebateBill;
	}

	public void setIsRebateBill(Integer isRebateBill) {
		this.isRebateBill = isRebateBill;
	}

	public Double getRebateAmount() {
		return rebateAmount;
	}

	public void setRebateAmount(Double rebateAmount) {
		this.rebateAmount = rebateAmount;
	}
	
	

}