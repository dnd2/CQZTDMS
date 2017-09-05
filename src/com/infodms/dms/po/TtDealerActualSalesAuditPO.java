/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-05-07 16:58:57
* CreateBy   : -Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtDealerActualSalesAuditPO extends PO{

	private Integer fleetStatus;
	private Date consignationDate;
	private Long ctmEditId;
	private Long ctmId;
	private String memo;
	private Long oemCompanyId;
	private Integer isFleet;
	private Long updateBy;
	private String salesReson;
	private Long mortgageType;
	private Long auditUser;
	private Float miles;
	private Long fleetId;
	private Date salesDate;
	private String vehicleNo;
	private Long orderId;
	private Date auditDate;
	private Date createDate;
	private Double price;
	private Long dlrCompanyId;
	private Long dealerId;
	private Date insuranceDate;
	private Date updateDate;
	private Long createBy;
	private Integer status;
	private String insuranceCompany;
	private Integer isDel;
	private Long flagFleet;
	private Integer isOldCtm;
	private Integer isForBusi;
	private Double shoufuRatio;
	private String salesAddress;
	private String auditRemark;
	private Long vehicleId;
	private Long contractId;
	private Integer isUnCust;
	private String invoiceNo;
	private Integer saleType;
	private Long loansType;
	private Integer isSecond;
	private Long loansYear;
	private String knowAddress;
	private String checkRamark;
	private Date invoiceDate;
	private Long logId;
	private Long salesConId;
	private Integer payment;
	private String contractNo;
	private Long auditId;
	private String ctmPresent;
	
	private Long carCharactor;
	private Double money;
	private Long bank;
	private Double lv;
	private Long thischange;
	private String loanschange;
	

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public Long getBank() {
		return bank;
	}

	public void setBank(Long bank) {
		this.bank = bank;
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

	public Long getCarCharactor() {
		return carCharactor;
	}

	public void setCarCharactor(Long carCharactor) {
		this.carCharactor = carCharactor;
	}

	public String getCtmPresent()
	{
		return ctmPresent;
	}

	public void setCtmPresent(String ctmPresent)
	{
		this.ctmPresent = ctmPresent;
	}

	public Long getAuditId()
	{
		return auditId;
	}

	public void setAuditId(Long auditId)
	{
		this.auditId = auditId;
	}

	public void setFleetStatus(Integer fleetStatus){
		this.fleetStatus=fleetStatus;
	}

	public Integer getFleetStatus(){
		return this.fleetStatus;
	}

	public void setConsignationDate(Date consignationDate){
		this.consignationDate=consignationDate;
	}

	public Date getConsignationDate(){
		return this.consignationDate;
	}

	public void setCtmEditId(Long ctmEditId){
		this.ctmEditId=ctmEditId;
	}

	public Long getCtmEditId(){
		return this.ctmEditId;
	}

	public void setCtmId(Long ctmId){
		this.ctmId=ctmId;
	}

	public Long getCtmId(){
		return this.ctmId;
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

	public void setIsFleet(Integer isFleet){
		this.isFleet=isFleet;
	}

	public Integer getIsFleet(){
		return this.isFleet;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setSalesReson(String salesReson){
		this.salesReson=salesReson;
	}

	public String getSalesReson(){
		return this.salesReson;
	}

	public void setMortgageType(Long mortgageType){
		this.mortgageType=mortgageType;
	}

	public Long getMortgageType(){
		return this.mortgageType;
	}

	public void setAuditUser(Long auditUser){
		this.auditUser=auditUser;
	}

	public Long getAuditUser(){
		return this.auditUser;
	}

	public void setMiles(Float miles){
		this.miles=miles;
	}

	public Float getMiles(){
		return this.miles;
	}

	public void setFleetId(Long fleetId){
		this.fleetId=fleetId;
	}

	public Long getFleetId(){
		return this.fleetId;
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

	public void setOrderId(Long orderId){
		this.orderId=orderId;
	}

	public Long getOrderId(){
		return this.orderId;
	}

	public void setAuditDate(Date auditDate){
		this.auditDate=auditDate;
	}

	public Date getAuditDate(){
		return this.auditDate;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
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

	public void setIsDel(Integer isDel){
		this.isDel=isDel;
	}

	public Integer getIsDel(){
		return this.isDel;
	}

	public void setFlagFleet(Long flagFleet){
		this.flagFleet=flagFleet;
	}

	public Long getFlagFleet(){
		return this.flagFleet;
	}

	public void setIsOldCtm(Integer isOldCtm){
		this.isOldCtm=isOldCtm;
	}

	public Integer getIsOldCtm(){
		return this.isOldCtm;
	}

	public void setIsForBusi(Integer isForBusi){
		this.isForBusi=isForBusi;
	}

	public Integer getIsForBusi(){
		return this.isForBusi;
	}

	public void setShoufuRatio(Double shoufuRatio){
		this.shoufuRatio=shoufuRatio;
	}

	public Double getShoufuRatio(){
		return this.shoufuRatio;
	}

	public void setSalesAddress(String salesAddress){
		this.salesAddress=salesAddress;
	}

	public String getSalesAddress(){
		return this.salesAddress;
	}

	public void setAuditRemark(String auditRemark){
		this.auditRemark=auditRemark;
	}

	public String getAuditRemark(){
		return this.auditRemark;
	}

	public void setVehicleId(Long vehicleId){
		this.vehicleId=vehicleId;
	}

	public Long getVehicleId(){
		return this.vehicleId;
	}

	public void setContractId(Long contractId){
		this.contractId=contractId;
	}

	public Long getContractId(){
		return this.contractId;
	}

	public void setIsUnCust(Integer isUnCust){
		this.isUnCust=isUnCust;
	}

	public Integer getIsUnCust(){
		return this.isUnCust;
	}

	public void setInvoiceNo(String invoiceNo){
		this.invoiceNo=invoiceNo;
	}

	public String getInvoiceNo(){
		return this.invoiceNo;
	}

	public void setSaleType(Integer saleType){
		this.saleType=saleType;
	}

	public Integer getSaleType(){
		return this.saleType;
	}

	public void setLoansType(Long loansType){
		this.loansType=loansType;
	}

	public Long getLoansType(){
		return this.loansType;
	}

	public void setIsSecond(Integer isSecond){
		this.isSecond=isSecond;
	}

	public Integer getIsSecond(){
		return this.isSecond;
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

	public void setCheckRamark(String checkRamark){
		this.checkRamark=checkRamark;
	}

	public String getCheckRamark(){
		return this.checkRamark;
	}

	public void setInvoiceDate(Date invoiceDate){
		this.invoiceDate=invoiceDate;
	}

	public Date getInvoiceDate(){
		return this.invoiceDate;
	}

	public void setLogId(Long logId){
		this.logId=logId;
	}

	public Long getLogId(){
		return this.logId;
	}

	public void setSalesConId(Long salesConId){
		this.salesConId=salesConId;
	}

	public Long getSalesConId(){
		return this.salesConId;
	}

	public void setPayment(Integer payment){
		this.payment=payment;
	}

	public Integer getPayment(){
		return this.payment;
	}

	public void setContractNo(String contractNo){
		this.contractNo=contractNo;
	}

	public String getContractNo(){
		return this.contractNo;
	}


}