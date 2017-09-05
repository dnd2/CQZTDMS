/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2009-12-09 15:59:03
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.bean;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class RemitInfoBean extends PO{

	private Long remittanceId;
	private Long accConfBy;
	private String outName;
	private String remitMarkNo;
	private String dealerId;
	private String dealerName;
	private String remitStartDate;
	private String remitEndDate;
	private String useType;
	private Long createBy;
	private Date createDate;
	private String faxConfDate;
	private String status;
	private String remitType;
	private String remark2;
	private String accConfDate;
	private Date updateDate;
	private Long receiveAccountId;
	private String orgCode;
	private String orgName;
	private String outBank;
	private Long faxConfirmBy;
	private String remitDate;
	private Double amount;
	private String outType;
	private Long updateBy;
	private String remitNo;
	private String remark1;
	private String outAccountNo;
	private String accDate;
	private String receName;
	private String receBank;
	private String accountNo;
	private String advice;
	private String faxUser;
	private String conUser;
	private String conDate;
	private Long historyId;
	private Integer version =1;
	private String name;
	private Double totalMoney = 0.0;
	private Double userMoney;
	private Long orderId;
	public void setRemittanceId(Long remittanceId){
		this.remittanceId=remittanceId;
	}

	public Long getRemittanceId(){
		return this.remittanceId;
	}

	public void setAccConfBy(Long accConfBy){
		this.accConfBy=accConfBy;
	}

	public Long getAccConfBy(){
		return this.accConfBy;
	}

	public void setOutName(String outName){
		this.outName=outName;
	}

	public String getOutName(){
		return this.outName;
	}

	public void setRemitMarkNo(String remitMarkNo){
		this.remitMarkNo=remitMarkNo;
	}

	public String getRemitMarkNo(){
		return this.remitMarkNo;
	}

	public void setUseType(String useType){
		this.useType=useType;
	}

	public String getUseType(){
		return this.useType;
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

	public void setFaxConfDate(String faxConfDate){
		this.faxConfDate=faxConfDate;
	}

	public String getFaxConfDate(){
		return this.faxConfDate;
	}

	public void setRemitType(String remitType){
		this.remitType=remitType;
	}

	public String getRemitType(){
		return this.remitType;
	}

	public void setRemark2(String remark2){
		this.remark2=remark2;
	}

	public String getRemark2(){
		return this.remark2;
	}

	public void setAccConfDate(String accConfDate){
		this.accConfDate=accConfDate;
	}

	public String getAccConfDate(){
		return this.accConfDate;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setReceiveAccountId(Long receiveAccountId){
		this.receiveAccountId=receiveAccountId;
	}

	public Long getReceiveAccountId(){
		return this.receiveAccountId;
	}

	public void setFaxConfirmBy(Long faxConfirmBy){
		this.faxConfirmBy=faxConfirmBy;
	}

	public Long getFaxConfirmBy(){
		return this.faxConfirmBy;
	}

	public void setRemitDate(String remitDate){
		this.remitDate=remitDate;
	}

	public String getRemitDate(){
		return this.remitDate;
	}

	public void setAmount(Double amount){
		this.amount=amount;
	}

	public Double getAmount(){
		return this.amount;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOutBank() {
		return outBank;
	}

	public void setOutBank(String outBank) {
		this.outBank = outBank;
	}

	public String getOutType() {
		return outType;
	}

	public void setOutType(String outType) {
		this.outType = outType;
	}

	public void setRemitNo(String remitNo){
		this.remitNo=remitNo;
	}

	public String getRemitNo(){
		return this.remitNo;
	}

	public void setRemark1(String remark1){
		this.remark1=remark1;
	}

	public String getRemark1(){
		return this.remark1;
	}

	public void setOutAccountNo(String outAccountNo){
		this.outAccountNo=outAccountNo;
	}

	public String getOutAccountNo(){
		return this.outAccountNo;
	}

	public void setAccDate(String accDate){
		this.accDate=accDate;
	}

	public String getAccDate(){
		return this.accDate;
	}

	public String getReceName() {
		return receName;
	}

	public void setReceName(String receName) {
		this.receName = receName;
	}

	public String getReceBank() {
		return receBank;
	}

	public void setReceBank(String receBank) {
		this.receBank = receBank;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getAdvice() {
		return advice;
	}

	public void setAdvice(String advice) {
		this.advice = advice;
	}

	public String getFaxUser() {
		return faxUser;
	}

	public void setFaxUser(String faxUser) {
		this.faxUser = faxUser;
	}

	public String getConUser() {
		return conUser;
	}

	public void setConUser(String conUser) {
		this.conUser = conUser;
	}

	public String getConDate() {
		return conDate;
	}

	public void setConDate(String conDate) {
		this.conDate = conDate;
	}

	public Long getHistoryId() {
		return historyId;
	}

	public void setHistoryId(Long historyId) {
		this.historyId = historyId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(Double totalMoney) {
		this.totalMoney = totalMoney;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Double getUserMoney() {
		return userMoney;
	}

	public void setUserMoney(Double userMoney) {
		this.userMoney = userMoney;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getDealerId() {
		return dealerId;
	}

	public void setDealerId(String dealerId) {
		this.dealerId = dealerId;
	}

	public String getDealerName() {
		return dealerName;
	}

	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}

	public String getRemitStartDate() {
		return remitStartDate;
	}

	public void setRemitStartDate(String remitStartDate) {
		this.remitStartDate = remitStartDate;
	}

	public String getRemitEndDate() {
		return remitEndDate;
	}

	public void setRemitEndDate(String remitEndDate) {
		this.remitEndDate = remitEndDate;
	}

}