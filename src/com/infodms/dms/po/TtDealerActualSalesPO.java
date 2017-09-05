/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-05-07 14:57:23
* CreateBy   : -Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;

import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtDealerActualSalesPO extends PO{

	private String soNo;
	private Double price;
	private Long dlrCompanyId;
	private Integer fleetStatus;
	private Long dealerId;
	private Date insuranceDate;
	private Date consignationDate;
	private Date updateDate;
	private Long createBy;
	private Integer displacementStatus;
	private Integer status;
	private String insuranceCompany;
	private Long ctmId;
	private Integer isDel;
	private String memo;
	private Long oemCompanyId;
	private Integer isCrm;
	private Integer isFleet;
	private Integer isOldCtm;
	private Long updateBy;
	private Integer isForBusi;
	private String salesReson;
	private Double shoufuRatio;
	private Long mortgageType;
	private String salesAddress;
	private Integer isReturn;
	private Float miles;
	private Long vehicleId;
	private Integer isUnCust;
	private Long contractId;
	private String dealerShortname;
	private Long fleetId;
	private Integer saleType;
	private String invoiceNo;
	private Long loansType;
	private Date salesDate;
	private String vehicleNo;
	private Long loansYear;
	private String knowAddress;
	private Date invoiceDate;
	private String dealerName;
	private Long salesConId;
	private Long orderId;
	private Date returnDate;
	private Integer payment;
	private Date createDate;
	private Integer carCharactor;
	private String contractNo;
	private Integer callcenterCheckStatus;
	private Integer salesCheckStatus;
	private String ctmPresent; // 赠送礼品;
	private Integer isStatus;
	private Long	salesDealer;
	
	private Long bank;
	private Double money;
	private Double lv;
	private Long thischange;
	private String loanschange;
	private String qkOrderDetailId;
	private String delvDetailId;
	private String oldCustomerName;//老客户姓名  
	private String oldTelephone;//老客户电话
	private String oldVehicleId;//老客户车架号
	private String oldRelationCode;//老客户关系代码
	private Long activityId;
	private Integer couno;
	private Long integ;
	private String salesConName;//销售顾问名称 手动上报实销时 用

	
	public String getSalesConName() {
		return salesConName;
	}

	public void setSalesConName(String salesConName) {
		this.salesConName = salesConName;
	}

	public Long getInteg() {
		return integ;
	}

	public void setInteg(Long integ) {
		this.integ = integ;
	}

	public Integer getCouno() {
		return couno;
	}

	public void setCouno(Integer couno) {
		this.couno = couno;
	}

	public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	public String getQkOrderDetailId() {
		return qkOrderDetailId;
	}

	public void setQkOrderDetailId(String qkOrderDetailId) {
		this.qkOrderDetailId = qkOrderDetailId;
	}

	public String getDelvDetailId() {
		return delvDetailId;
	}

	public void setDelvDetailId(String delvDetailId) {
		this.delvDetailId = delvDetailId;
	}

	public String getOldCustomerName() {
		return oldCustomerName;
	}

	public void setOldCustomerName(String oldCustomerName) {
		this.oldCustomerName = oldCustomerName;
	}

	public String getOldTelephone() {
		return oldTelephone;
	}

	public void setOldTelephone(String oldTelephone) {
		this.oldTelephone = oldTelephone;
	}

	public String getOldVehicleId() {
		return oldVehicleId;
	}

	public void setOldVehicleId(String oldVehicleId) {
		this.oldVehicleId = oldVehicleId;
	}

	public String getOldRelationCode() {
		return oldRelationCode;
	}

	public void setOldRelationCode(String oldRelationCode) {
		this.oldRelationCode = oldRelationCode;
	}

	public Long getBank() {
		return bank;
	}

	public void setBank(Long bank) {
		this.bank = bank;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public Double getLv() {
		return lv;
	}

	public void setLv(Double lv) {
		this.lv = lv;
	}

	public Long getThischange() {
		return thischange;
	}

	public void setThischange(Long thischange) {
		this.thischange = thischange;
	}

	public String getLoanschange() {
		return loanschange;
	}

	public void setLoanschange(String loanschange) {
		this.loanschange = loanschange;
	}

	public Long getSalesDealer() {
		return salesDealer;
	}

	public void setSalesDealer(Long salesDealer) {
		this.salesDealer = salesDealer;
	}

	public Integer getIsStatus() {
		return isStatus;
	}

	public void setIsStatus(Integer isStatus) {
		this.isStatus = isStatus;
	}

	public String getCtmPresent()
	{
		return ctmPresent;
	}

	public void setCtmPresent(String ctmPresent)
	{
		this.ctmPresent = ctmPresent;
	}

	public void setSoNo(String soNo){
		this.soNo=soNo;
	}

	public String getSoNo(){
		return this.soNo;
	}

	public void setPrice(Double price){
		this.price=price;
	}

	public Double getPrice(){
		return this.price;
	}

	public void setDlrCompanyId(Long dlrCompanyId){
		this.dlrCompanyId=dlrCompanyId;
	}

	public Long getDlrCompanyId(){
		return this.dlrCompanyId;
	}

	public void setFleetStatus(Integer fleetStatus){
		this.fleetStatus=fleetStatus;
	}

	public Integer getFleetStatus(){
		return this.fleetStatus;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setInsuranceDate(Date insuranceDate){
		this.insuranceDate=insuranceDate;
	}

	public Date getInsuranceDate(){
		return this.insuranceDate;
	}

	public void setConsignationDate(Date consignationDate){
		this.consignationDate=consignationDate;
	}

	public Date getConsignationDate(){
		return this.consignationDate;
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

	public void setDisplacementStatus(Integer displacementStatus){
		this.displacementStatus=displacementStatus;
	}

	public Integer getDisplacementStatus(){
		return this.displacementStatus;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setInsuranceCompany(String insuranceCompany){
		this.insuranceCompany=insuranceCompany;
	}

	public String getInsuranceCompany(){
		return this.insuranceCompany;
	}

	public void setCtmId(Long ctmId){
		this.ctmId=ctmId;
	}

	public Long getCtmId(){
		return this.ctmId;
	}

	public void setIsDel(Integer isDel){
		this.isDel=isDel;
	}

	public Integer getIsDel(){
		return this.isDel;
	}

	public void setMemo(String memo){
		this.memo=memo;
	}

	public String getMemo(){
		return this.memo;
	}

	public void setOemCompanyId(Long oemCompanyId){
		this.oemCompanyId=oemCompanyId;
	}

	public Long getOemCompanyId(){
		return this.oemCompanyId;
	}

	public void setIsCrm(Integer isCrm){
		this.isCrm=isCrm;
	}

	public Integer getIsCrm(){
		return this.isCrm;
	}

	public void setIsFleet(Integer isFleet){
		this.isFleet=isFleet;
	}

	public Integer getIsFleet(){
		return this.isFleet;
	}

	public void setIsOldCtm(Integer isOldCtm){
		this.isOldCtm=isOldCtm;
	}

	public Integer getIsOldCtm(){
		return this.isOldCtm;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setIsForBusi(Integer isForBusi){
		this.isForBusi=isForBusi;
	}

	public Integer getIsForBusi(){
		return this.isForBusi;
	}

	public void setSalesReson(String salesReson){
		this.salesReson=salesReson;
	}

	public String getSalesReson(){
		return this.salesReson;
	}

	public void setShoufuRatio(Double shoufuRatio){
		this.shoufuRatio=shoufuRatio;
	}

	public Double getShoufuRatio(){
		return this.shoufuRatio;
	}

	public void setMortgageType(Long mortgageType){
		this.mortgageType=mortgageType;
	}

	public Long getMortgageType(){
		return this.mortgageType;
	}

	public void setSalesAddress(String salesAddress){
		this.salesAddress=salesAddress;
	}

	public String getSalesAddress(){
		return this.salesAddress;
	}

	public void setIsReturn(Integer isReturn){
		this.isReturn=isReturn;
	}

	public Integer getIsReturn(){
		return this.isReturn;
	}

	public void setMiles(Float miles){
		this.miles=miles;
	}

	public Float getMiles(){
		return this.miles;
	}

	public void setVehicleId(Long vehicleId){
		this.vehicleId=vehicleId;
	}

	public Long getVehicleId(){
		return this.vehicleId;
	}

	public void setIsUnCust(Integer isUnCust){
		this.isUnCust=isUnCust;
	}

	public Integer getIsUnCust(){
		return this.isUnCust;
	}

	public void setContractId(Long contractId){
		this.contractId=contractId;
	}

	public Long getContractId(){
		return this.contractId;
	}

	public void setDealerShortname(String dealerShortname){
		this.dealerShortname=dealerShortname;
	}

	public String getDealerShortname(){
		return this.dealerShortname;
	}

	public void setFleetId(Long fleetId){
		this.fleetId=fleetId;
	}

	public Long getFleetId(){
		return this.fleetId;
	}

	public void setSaleType(Integer saleType){
		this.saleType=saleType;
	}

	public Integer getSaleType(){
		return this.saleType;
	}

	public void setInvoiceNo(String invoiceNo){
		this.invoiceNo=invoiceNo;
	}

	public String getInvoiceNo(){
		return this.invoiceNo;
	}

	public void setLoansType(Long loansType){
		this.loansType=loansType;
	}

	public Long getLoansType(){
		return this.loansType;
	}

	public void setSalesDate(Date salesDate){
		this.salesDate=salesDate;
	}

	public Date getSalesDate(){
		return this.salesDate;
	}

	public void setVehicleNo(String vehicleNo){
		this.vehicleNo=vehicleNo;
	}

	public String getVehicleNo(){
		return this.vehicleNo;
	}

	public void setLoansYear(Long loansYear){
		this.loansYear=loansYear;
	}

	public Long getLoansYear(){
		return this.loansYear;
	}

	public void setKnowAddress(String knowAddress){
		this.knowAddress=knowAddress;
	}

	public String getKnowAddress(){
		return this.knowAddress;
	}

	public void setInvoiceDate(Date invoiceDate){
		this.invoiceDate=invoiceDate;
	}

	public Date getInvoiceDate(){
		return this.invoiceDate;
	}

	public void setDealerName(String dealerName){
		this.dealerName=dealerName;
	}

	public String getDealerName(){
		return this.dealerName;
	}

	public void setSalesConId(Long salesConId){
		this.salesConId=salesConId;
	}

	public Long getSalesConId(){
		return this.salesConId;
	}

	public void setOrderId(Long orderId){
		this.orderId=orderId;
	}

	public Long getOrderId(){
		return this.orderId;
	}

	public void setReturnDate(Date returnDate){
		this.returnDate=returnDate;
	}

	public Date getReturnDate(){
		return this.returnDate;
	}

	public void setPayment(Integer payment){
		this.payment=payment;
	}

	public Integer getPayment(){
		return this.payment;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setCarCharactor(Integer carCharactor){
		this.carCharactor=carCharactor;
	}

	public Integer getCarCharactor(){
		return this.carCharactor;
	}

	public void setContractNo(String contractNo){
		this.contractNo=contractNo;
	}

	public String getContractNo(){
		return this.contractNo;
	}

	public Integer getCallcenterCheckStatus() {
		return callcenterCheckStatus;
	}

	public void setCallcenterCheckStatus(Integer callcenterCheckStatus) {
		this.callcenterCheckStatus = callcenterCheckStatus;
	}

	public Integer getSalesCheckStatus() {
		return salesCheckStatus;
	}

	public void setSalesCheckStatus(Integer salesCheckStatus) {
		this.salesCheckStatus = salesCheckStatus;
	}
}