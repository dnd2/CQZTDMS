/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-10-29 14:13:38
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrPartsitemTempPO extends PO{

	private String producerCode;
	private Integer isMainpart;
	private Long createBy;
	private String downPartCode;
	private String downProductName;
	private String wrLabourcode;
	private String downProductCode;
	private Date createDate;
	private Long debitPartId;
	private String oldPartCode;
	private String producerName;
	private String authCode;
	private Double price;
	private Long partId;
	private Integer isFore;
	private Double deductAmount;
	private String authRemark;
	private String downPartName;
	private String bMpCode;
	private Date updateDate;
	private Integer isAgree;
	private Float returnNum;
	private String partCode;
	private Integer payType;
	private Float balanceQuantity;
	private Integer isGua;
	private Double amount;
	private String partName;
	private Float quantity;
	private Long updateBy;
	private Double balanceAmount;
	private String remark;
	private Long id;
	private Long isClaim;
	private Double balancePrice;
	private Double applyQuantity;
	private Double applyPrice;
	private Double applyAmount;
	private Integer responsibilityType;
	private Integer isOldClaimPrint;
	private Long isCliams;
	private Long realPartId;//旧件配件ID
	
	
	public Long getRealPartId() {
		return realPartId;
	}
	public void setRealPartId(Long realPartId) {
		this.realPartId = realPartId;
	}
	public Long getIsCliams() {
		return isCliams;
	}
	public void setIsCliams(Long isCliams) {
		this.isCliams = isCliams;
	}
	public Integer getResponsibilityType() {
		return responsibilityType;
	}
	public void setResponsibilityType(Integer responsibilityType) {
		this.responsibilityType = responsibilityType;
	}
	//zhumingwei 2011-03-11
	//private String troubleType;

	//public String getTroubleType() {
	//	return troubleType;
	//}

	//public void setTroubleType(String troubleType) {
	//	this.troubleType = troubleType;
	//}
	//zhumingwei 2011-03-11

	public Double getBalancePrice() {
		return balancePrice;
	}

	public void setBalancePrice(Double balancePrice) {
		this.balancePrice = balancePrice;
	}

	public Double getApplyQuantity() {
		return applyQuantity;
	}

	public void setApplyQuantity(Double applyQuantity) {
		this.applyQuantity = applyQuantity;
	}

	public Double getApplyPrice() {
		return applyPrice;
	}

	public void setApplyPrice(Double applyPrice) {
		this.applyPrice = applyPrice;
	}

	public Double getApplyAmount() {
		return applyAmount;
	}

	public void setApplyAmount(Double applyAmount) {
		this.applyAmount = applyAmount;
	}

	public String getbMpCode() {
		return bMpCode;
	}

	public void setbMpCode(String bMpCode) {
		this.bMpCode = bMpCode;
	}

	public Long getIsClaim() {
		return isClaim;
	}

	public void setIsClaim(Long isClaim) {
		this.isClaim = isClaim;
	}

	public void setProducerCode(String producerCode){
		this.producerCode=producerCode;
	}

	public String getProducerCode(){
		return this.producerCode;
	}

	public void setIsMainpart(Integer isMainpart){
		this.isMainpart=isMainpart;
	}

	public Integer getIsMainpart(){
		return this.isMainpart;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setDownPartCode(String downPartCode){
		this.downPartCode=downPartCode;
	}

	public String getDownPartCode(){
		return this.downPartCode;
	}

	public void setDownProductName(String downProductName){
		this.downProductName=downProductName;
	}

	public String getDownProductName(){
		return this.downProductName;
	}

	public void setWrLabourcode(String wrLabourcode){
		this.wrLabourcode=wrLabourcode;
	}

	public String getWrLabourcode(){
		return this.wrLabourcode;
	}

	public void setDownProductCode(String downProductCode){
		this.downProductCode=downProductCode;
	}

	public String getDownProductCode(){
		return this.downProductCode;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setDebitPartId(Long debitPartId){
		this.debitPartId=debitPartId;
	}

	public Long getDebitPartId(){
		return this.debitPartId;
	}

	public void setOldPartCode(String oldPartCode){
		this.oldPartCode=oldPartCode;
	}

	public String getOldPartCode(){
		return this.oldPartCode;
	}

	public void setProducerName(String producerName){
		this.producerName=producerName;
	}

	public String getProducerName(){
		return this.producerName;
	}

	public void setAuthCode(String authCode){
		this.authCode=authCode;
	}

	public String getAuthCode(){
		return this.authCode;
	}

	public void setPrice(Double price){
		this.price=price;
	}

	public Double getPrice(){
		return this.price;
	}

	public void setPartId(Long partId){
		this.partId=partId;
	}

	public Long getPartId(){
		return this.partId;
	}

	public void setIsFore(Integer isFore){
		this.isFore=isFore;
	}

	public Integer getIsFore(){
		return this.isFore;
	}

	public void setDeductAmount(Double deductAmount){
		this.deductAmount=deductAmount;
	}

	public Double getDeductAmount(){
		return this.deductAmount;
	}

	public void setAuthRemark(String authRemark){
		this.authRemark=authRemark;
	}

	public String getAuthRemark(){
		return this.authRemark;
	}

	public void setDownPartName(String downPartName){
		this.downPartName=downPartName;
	}

	public String getDownPartName(){
		return this.downPartName;
	}

	public void setBMpCode(String bMpCode){
		this.bMpCode=bMpCode;
	}

	public String getBMpCode(){
		return this.bMpCode;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setIsAgree(Integer isAgree){
		this.isAgree=isAgree;
	}

	public Integer getIsAgree(){
		return this.isAgree;
	}

	public void setReturnNum(Float returnNum){
		this.returnNum=returnNum;
	}

	public Float getReturnNum(){
		return this.returnNum;
	}

	public void setPartCode(String partCode){
		this.partCode=partCode;
	}

	public String getPartCode(){
		return this.partCode;
	}

	public void setPayType(Integer payType){
		this.payType=payType;
	}

	public Integer getPayType(){
		return this.payType;
	}

	public void setBalanceQuantity(Float balanceQuantity){
		this.balanceQuantity=balanceQuantity;
	}

	public Float getBalanceQuantity(){
		return this.balanceQuantity;
	}

	public void setIsGua(Integer isGua){
		this.isGua=isGua;
	}

	public Integer getIsGua(){
		return this.isGua;
	}

	public void setAmount(Double amount){
		this.amount=amount;
	}

	public Double getAmount(){
		return this.amount;
	}

	public void setPartName(String partName){
		this.partName=partName;
	}

	public String getPartName(){
		return this.partName;
	}

	public void setQuantity(Float quantity){
		this.quantity=quantity;
	}

	public Float getQuantity(){
		return this.quantity;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setBalanceAmount(Double balanceAmount){
		this.balanceAmount=balanceAmount;
	}

	public Double getBalanceAmount(){
		return this.balanceAmount;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}
	public Integer getIsOldClaimPrint() {
		return isOldClaimPrint;
	}
	public void setIsOldClaimPrint(Integer isOldClaimPrint) {
		this.isOldClaimPrint = isOldClaimPrint;
	}

}