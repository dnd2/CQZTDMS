/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-08-01 14:41:34
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrClaimBalancePO extends PO{

	private Integer balanceYieldly;
	private Double marketActivityAmount;
	private Long specFeeCount;
	private Double plusMinusLabourSum;
	private Double serviceLabourAmount;
	private Double appendLabourAmountBak;
	private Date endDate;
	private Double freeDeduct;
	private Double plusMinusDatumSum;
	private Long chargeId;
	private String remark;
	private String balanceNo;
	private Double serviceOtherAmount;
	private Double appendAmountBak;
	private String funancialRemark;
	private Double freePartAmount;
	private Double serviceTotalAmountBak;
	private Long oemCompanyId;
	private Double serviceOtherAmountMarket;
	private Double servicePartAmountMarket;
	private Double labourAmount;
	private Long updateBy;
	private String applyPersonName;
	private Double otherAmountBak;
	private Double speoutfeeAmount;
	private Long id;
	private Double freeLabourAmount;
	private Double marketAmount;
	private Date reviewApplicationTime;
	private Double otherAmount;
	private Double marketMarketAmount;
	private Double balanceAmount;
	private Long topDealerId;
	private Long applyPersonId;
	private Double marketAmountBak;
	private Double serviceDeduct;
	private Double serviceFixedAmount;
	private String dealerCode;
	private Double oldDeduct;
	private Date startDate;
	private Double barcodeSum;
	private String stationerTel;
	private Double checkDeduct;
	private Integer var;
	private Long secondDealerId;
	private Long kpDealerId;
	private Date createDate;
	private Long specFeeAuthCount;
	private Double appendLabourAmount;
	private String invoiceMaker;
	private Long claimCount;
	private String reviewApplicationBy;
	private Double freeAmountBak;
	private Long dealerId;
	private Date updateDate;
	private Long createBy;
	private Integer status;
	private Integer isInvoice;
	private Double partAmount;
	private Double returnAmountBak;
	private Double packgeChangeSum;
	private Double applyAmount;
	private Double freeAmount;
	private Long yieldly;
	private Double speoutfeeAmountBak;
	private Double serviceTotalAmountMarket;
	private Double adminDeduct;
	private Double partAmountBak;
	private Double serviceLabourAmountMarket;
	private Double amountSum;
	private Double specialDatumSum;
	private Double serviceTotalAmount;
	private Double servicePartAmount;
	private Double financialDeduct;
	private Double labourAmountBak;
	private String dealerName;
	private Double noteAmount;
	private Integer dealerLevel;
	private Long province;
	private String claimerTel;
	private Double returnAmount;
	private Double appendAmount;
	private Double specialLabourSum;
	private Integer ocstatus;
	private Double accessoriesPrice;
	private Double compensationMoney;//补偿费COMPENSATION_MONEY
	private Double compensationDealerMoney;//补偿费服务站COMPENSATION_DEALER_MONEY
	
	private Long invoiceId;
	private Double selectmentAmount;
	private Long invoiceTaxrate;
	

	public Long getInvoiceTaxrate() {
		return invoiceTaxrate;
	}

	public void setInvoiceTaxrate(Long invoiceTaxrate) {
		this.invoiceTaxrate = invoiceTaxrate;
	}

	public Long getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(Long invoiceId) {
		this.invoiceId = invoiceId;
	}


	public Double getSelectmentAmount() {
		return selectmentAmount;
	}

	public void setSelectmentAmount(Double selectmentAmount) {
		this.selectmentAmount = selectmentAmount;
	}

	public void setBalanceYieldly(Integer balanceYieldly){
		this.balanceYieldly=balanceYieldly;
	}

	public Integer getBalanceYieldly(){
		return this.balanceYieldly;
	}

	public void setMarketActivityAmount(Double marketActivityAmount){
		this.marketActivityAmount=marketActivityAmount;
	}

	public Double getMarketActivityAmount(){
		return this.marketActivityAmount;
	}

	public void setSpecFeeCount(Long specFeeCount){
		this.specFeeCount=specFeeCount;
	}

	public Long getSpecFeeCount(){
		return this.specFeeCount;
	}

	public void setPlusMinusLabourSum(Double plusMinusLabourSum){
		this.plusMinusLabourSum=plusMinusLabourSum;
	}

	public Double getPlusMinusLabourSum(){
		return this.plusMinusLabourSum;
	}

	public void setServiceLabourAmount(Double serviceLabourAmount){
		this.serviceLabourAmount=serviceLabourAmount;
	}

	public Double getServiceLabourAmount(){
		return this.serviceLabourAmount;
	}

	public void setAppendLabourAmountBak(Double appendLabourAmountBak){
		this.appendLabourAmountBak=appendLabourAmountBak;
	}

	public Double getAppendLabourAmountBak(){
		return this.appendLabourAmountBak;
	}

	public void setEndDate(Date endDate){
		this.endDate=endDate;
	}

	public Date getEndDate(){
		return this.endDate;
	}

	public void setFreeDeduct(Double freeDeduct){
		this.freeDeduct=freeDeduct;
	}

	public Double getFreeDeduct(){
		return this.freeDeduct;
	}

	public void setPlusMinusDatumSum(Double plusMinusDatumSum){
		this.plusMinusDatumSum=plusMinusDatumSum;
	}

	public Double getPlusMinusDatumSum(){
		return this.plusMinusDatumSum;
	}

	public void setChargeId(Long chargeId){
		this.chargeId=chargeId;
	}

	public Long getChargeId(){
		return this.chargeId;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setBalanceNo(String balanceNo){
		this.balanceNo=balanceNo;
	}

	public String getBalanceNo(){
		return this.balanceNo;
	}

	public void setServiceOtherAmount(Double serviceOtherAmount){
		this.serviceOtherAmount=serviceOtherAmount;
	}

	public Double getServiceOtherAmount(){
		return this.serviceOtherAmount;
	}

	public void setAppendAmountBak(Double appendAmountBak){
		this.appendAmountBak=appendAmountBak;
	}

	public Double getAppendAmountBak(){
		return this.appendAmountBak;
	}

	public void setFunancialRemark(String funancialRemark){
		this.funancialRemark=funancialRemark;
	}

	public String getFunancialRemark(){
		return this.funancialRemark;
	}

	public void setFreePartAmount(Double freePartAmount){
		this.freePartAmount=freePartAmount;
	}

	public Double getFreePartAmount(){
		return this.freePartAmount;
	}

	public void setServiceTotalAmountBak(Double serviceTotalAmountBak){
		this.serviceTotalAmountBak=serviceTotalAmountBak;
	}

	public Double getServiceTotalAmountBak(){
		return this.serviceTotalAmountBak;
	}

	public void setOemCompanyId(Long oemCompanyId){
		this.oemCompanyId=oemCompanyId;
	}

	public Long getOemCompanyId(){
		return this.oemCompanyId;
	}

	public void setServiceOtherAmountMarket(Double serviceOtherAmountMarket){
		this.serviceOtherAmountMarket=serviceOtherAmountMarket;
	}

	public Double getServiceOtherAmountMarket(){
		return this.serviceOtherAmountMarket;
	}

	public void setServicePartAmountMarket(Double servicePartAmountMarket){
		this.servicePartAmountMarket=servicePartAmountMarket;
	}

	public Double getServicePartAmountMarket(){
		return this.servicePartAmountMarket;
	}

	public void setLabourAmount(Double labourAmount){
		this.labourAmount=labourAmount;
	}

	public Double getLabourAmount(){
		return this.labourAmount;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setApplyPersonName(String applyPersonName){
		this.applyPersonName=applyPersonName;
	}

	public String getApplyPersonName(){
		return this.applyPersonName;
	}

	public void setOtherAmountBak(Double otherAmountBak){
		this.otherAmountBak=otherAmountBak;
	}

	public Double getOtherAmountBak(){
		return this.otherAmountBak;
	}

	public void setSpeoutfeeAmount(Double speoutfeeAmount){
		this.speoutfeeAmount=speoutfeeAmount;
	}

	public Double getSpeoutfeeAmount(){
		return this.speoutfeeAmount;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setFreeLabourAmount(Double freeLabourAmount){
		this.freeLabourAmount=freeLabourAmount;
	}

	public Double getFreeLabourAmount(){
		return this.freeLabourAmount;
	}

	public void setMarketAmount(Double marketAmount){
		this.marketAmount=marketAmount;
	}

	public Double getMarketAmount(){
		return this.marketAmount;
	}

	public void setReviewApplicationTime(Date reviewApplicationTime){
		this.reviewApplicationTime=reviewApplicationTime;
	}

	public Date getReviewApplicationTime(){
		return this.reviewApplicationTime;
	}

	public void setOtherAmount(Double otherAmount){
		this.otherAmount=otherAmount;
	}

	public Double getOtherAmount(){
		return this.otherAmount;
	}

	public void setMarketMarketAmount(Double marketMarketAmount){
		this.marketMarketAmount=marketMarketAmount;
	}

	public Double getMarketMarketAmount(){
		return this.marketMarketAmount;
	}

	public void setBalanceAmount(Double balanceAmount){
		this.balanceAmount=balanceAmount;
	}

	public Double getBalanceAmount(){
		return this.balanceAmount;
	}

	public void setTopDealerId(Long topDealerId){
		this.topDealerId=topDealerId;
	}

	public Long getTopDealerId(){
		return this.topDealerId;
	}

	public void setApplyPersonId(Long applyPersonId){
		this.applyPersonId=applyPersonId;
	}

	public Long getApplyPersonId(){
		return this.applyPersonId;
	}

	public void setMarketAmountBak(Double marketAmountBak){
		this.marketAmountBak=marketAmountBak;
	}

	public Double getMarketAmountBak(){
		return this.marketAmountBak;
	}

	public void setServiceDeduct(Double serviceDeduct){
		this.serviceDeduct=serviceDeduct;
	}

	public Double getServiceDeduct(){
		return this.serviceDeduct;
	}

	public void setServiceFixedAmount(Double serviceFixedAmount){
		this.serviceFixedAmount=serviceFixedAmount;
	}

	public Double getServiceFixedAmount(){
		return this.serviceFixedAmount;
	}

	public void setDealerCode(String dealerCode){
		this.dealerCode=dealerCode;
	}

	public String getDealerCode(){
		return this.dealerCode;
	}

	public void setOldDeduct(Double oldDeduct){
		this.oldDeduct=oldDeduct;
	}

	public Double getOldDeduct(){
		return this.oldDeduct;
	}

	public void setStartDate(Date startDate){
		this.startDate=startDate;
	}

	public Date getStartDate(){
		return this.startDate;
	}

	public void setBarcodeSum(Double barcodeSum){
		this.barcodeSum=barcodeSum;
	}

	public Double getBarcodeSum(){
		return this.barcodeSum;
	}

	public void setStationerTel(String stationerTel){
		this.stationerTel=stationerTel;
	}

	public String getStationerTel(){
		return this.stationerTel;
	}

	public void setCheckDeduct(Double checkDeduct){
		this.checkDeduct=checkDeduct;
	}

	public Double getCheckDeduct(){
		return this.checkDeduct;
	}

	public void setVar(Integer var){
		this.var=var;
	}

	public Integer getVar(){
		return this.var;
	}

	public void setSecondDealerId(Long secondDealerId){
		this.secondDealerId=secondDealerId;
	}

	public Long getSecondDealerId(){
		return this.secondDealerId;
	}

	public void setKpDealerId(Long kpDealerId){
		this.kpDealerId=kpDealerId;
	}

	public Long getKpDealerId(){
		return this.kpDealerId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setSpecFeeAuthCount(Long specFeeAuthCount){
		this.specFeeAuthCount=specFeeAuthCount;
	}

	public Long getSpecFeeAuthCount(){
		return this.specFeeAuthCount;
	}

	public void setAppendLabourAmount(Double appendLabourAmount){
		this.appendLabourAmount=appendLabourAmount;
	}

	public Double getAppendLabourAmount(){
		return this.appendLabourAmount;
	}

	public void setInvoiceMaker(String invoiceMaker){
		this.invoiceMaker=invoiceMaker;
	}

	public String getInvoiceMaker(){
		return this.invoiceMaker;
	}

	public void setClaimCount(Long claimCount){
		this.claimCount=claimCount;
	}

	public Long getClaimCount(){
		return this.claimCount;
	}

	public void setReviewApplicationBy(String reviewApplicationBy){
		this.reviewApplicationBy=reviewApplicationBy;
	}

	public String getReviewApplicationBy(){
		return this.reviewApplicationBy;
	}

	public void setFreeAmountBak(Double freeAmountBak){
		this.freeAmountBak=freeAmountBak;
	}

	public Double getFreeAmountBak(){
		return this.freeAmountBak;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
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

	public void setPartAmount(Double partAmount){
		this.partAmount=partAmount;
	}

	public Double getPartAmount(){
		return this.partAmount;
	}

	public void setReturnAmountBak(Double returnAmountBak){
		this.returnAmountBak=returnAmountBak;
	}

	public Double getReturnAmountBak(){
		return this.returnAmountBak;
	}

	public void setPackgeChangeSum(Double packgeChangeSum){
		this.packgeChangeSum=packgeChangeSum;
	}

	public Double getPackgeChangeSum(){
		return this.packgeChangeSum;
	}

	public void setApplyAmount(Double applyAmount){
		this.applyAmount=applyAmount;
	}

	public Double getApplyAmount(){
		return this.applyAmount;
	}

	public void setFreeAmount(Double freeAmount){
		this.freeAmount=freeAmount;
	}

	public Double getFreeAmount(){
		return this.freeAmount;
	}

	public void setYieldly(Long yieldly){
		this.yieldly=yieldly;
	}

	public Long getYieldly(){
		return this.yieldly;
	}

	public void setSpeoutfeeAmountBak(Double speoutfeeAmountBak){
		this.speoutfeeAmountBak=speoutfeeAmountBak;
	}

	public Double getSpeoutfeeAmountBak(){
		return this.speoutfeeAmountBak;
	}

	public void setServiceTotalAmountMarket(Double serviceTotalAmountMarket){
		this.serviceTotalAmountMarket=serviceTotalAmountMarket;
	}

	public Double getServiceTotalAmountMarket(){
		return this.serviceTotalAmountMarket;
	}

	public void setAdminDeduct(Double adminDeduct){
		this.adminDeduct=adminDeduct;
	}

	public Double getAdminDeduct(){
		return this.adminDeduct;
	}

	public void setPartAmountBak(Double partAmountBak){
		this.partAmountBak=partAmountBak;
	}

	public Double getPartAmountBak(){
		return this.partAmountBak;
	}

	public void setServiceLabourAmountMarket(Double serviceLabourAmountMarket){
		this.serviceLabourAmountMarket=serviceLabourAmountMarket;
	}

	public Double getServiceLabourAmountMarket(){
		return this.serviceLabourAmountMarket;
	}

	public void setAmountSum(Double amountSum){
		this.amountSum=amountSum;
	}

	public Double getAmountSum(){
		return this.amountSum;
	}

	public void setSpecialDatumSum(Double specialDatumSum){
		this.specialDatumSum=specialDatumSum;
	}

	public Double getSpecialDatumSum(){
		return this.specialDatumSum;
	}

	public void setServiceTotalAmount(Double serviceTotalAmount){
		this.serviceTotalAmount=serviceTotalAmount;
	}

	public Double getServiceTotalAmount(){
		return this.serviceTotalAmount;
	}

	public void setServicePartAmount(Double servicePartAmount){
		this.servicePartAmount=servicePartAmount;
	}

	public Double getServicePartAmount(){
		return this.servicePartAmount;
	}

	public void setFinancialDeduct(Double financialDeduct){
		this.financialDeduct=financialDeduct;
	}

	public Double getFinancialDeduct(){
		return this.financialDeduct;
	}

	public void setLabourAmountBak(Double labourAmountBak){
		this.labourAmountBak=labourAmountBak;
	}

	public Double getLabourAmountBak(){
		return this.labourAmountBak;
	}

	public void setDealerName(String dealerName){
		this.dealerName=dealerName;
	}

	public String getDealerName(){
		return this.dealerName;
	}

	public void setNoteAmount(Double noteAmount){
		this.noteAmount=noteAmount;
	}

	public Double getNoteAmount(){
		return this.noteAmount;
	}

	public void setDealerLevel(Integer dealerLevel){
		this.dealerLevel=dealerLevel;
	}

	public Integer getDealerLevel(){
		return this.dealerLevel;
	}

	public void setProvince(Long province){
		this.province=province;
	}

	public Long getProvince(){
		return this.province;
	}

	public void setClaimerTel(String claimerTel){
		this.claimerTel=claimerTel;
	}

	public String getClaimerTel(){
		return this.claimerTel;
	}

	public void setReturnAmount(Double returnAmount){
		this.returnAmount=returnAmount;
	}

	public Double getReturnAmount(){
		return this.returnAmount;
	}

	public void setAppendAmount(Double appendAmount){
		this.appendAmount=appendAmount;
	}

	public Double getAppendAmount(){
		return this.appendAmount;
	}

	public void setSpecialLabourSum(Double specialLabourSum){
		this.specialLabourSum=specialLabourSum;
	}

	public Double getSpecialLabourSum(){
		return this.specialLabourSum;
	}

	public Integer getIsInvoice() {
		return isInvoice;
	}

	public void setIsInvoice(Integer isInvoice) {
		this.isInvoice = isInvoice;
	}

	public Integer getOcstatus() {
		return ocstatus;
	}

	public void setOcstatus(Integer ocstatus) {
		this.ocstatus = ocstatus;
	}

	public Double getAccessoriesPrice() {
		return accessoriesPrice;
	}

	public void setAccessoriesPrice(Double accessoriesPrice) {
		this.accessoriesPrice = accessoriesPrice;
	}

	public Double getCompensationMoney() {
		return compensationMoney;
	}

	public void setCompensationMoney(Double compensationMoney) {
		this.compensationMoney = compensationMoney;
	}

	public Double getCompensationDealerMoney() {
		return compensationDealerMoney;
	}

	public void setCompensationDealerMoney(Double compensationDealerMoney) {
		this.compensationDealerMoney = compensationDealerMoney;
	}

}