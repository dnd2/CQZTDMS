/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-11-19 15:21:20
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartPoBalancePO extends PO{

	private Integer isBalances;
	private String locCode;
	private String balanceCode;
	private Integer partType;
	private String batchNo;
	private String remark;
	private Integer isGauge;
	private Long updateBy;
	private Long partId;
	private Integer inType;
	private String inCode;
	private Double balAmount;
	private String remark1;
	private Double buyPrice1;
	private Long returnQty;
	private Date deleteDate;
	private String orderCode;
	private String venderCode;
	private Long whId;
	private Long checkQty;
	private Double inAmount;
	private String partCname;
	private Long checkBy;
	private Long inBy;
	private Double buyPrice;
	private Integer balanceType;
	private Double buyPriceNotax;
	private Long balQty;
	private Long balanceBy;
	private String whName;
	private Long ftvenderId;
	private Integer ver;
	private Double taxRate;
	private Long itemQty;
	private Date createDate;
	private Double balAmountNotax;
	private Integer state;
	private Long buyGroup;
	private Date updateDate;
	private Long ckPriceBy;
	private Long fcvderId;
	private Long createBy;
	private Long makerId;
	private Date checkDate;
	private Double inAmountNotax;
	private Integer status;
	private Long inId;
	private Integer isCheck;
	private String venderName;
	private Date ckPriceDate;
	private Long venderId;
	private Long locId;
	private Long deleteBy;
	private Long disableBy;
	private Long buyQty;
	private Double buyPrice1Notax;
	private Long checkId;
	private String fcvderrCode;
	private String unit;
	private Long balanceId;
	private Date disableDate;
	private String planCode;
	private Long poId;
	private String partCode;
	private Date balanceDate;
	private String checkCode;
	private Long orgId;
	private Long buyerId;
	private String invoNo;
	private Long inQty;
	private Date inDate;
	private Integer originType;
	private String partOldcode;
	
	private Long produceFac;
	private Integer produceState;
	private Integer superiorPurchasing;
	private Long batId;

	public Long getBatId() {
		return batId;
	}

	public void setBatId(Long batId) {
		this.batId = batId;
	}

	public Integer getSuperiorPurchasing() {
		return superiorPurchasing;
	}

	public void setSuperiorPurchasing(Integer superiorPurchasing) {
		this.superiorPurchasing = superiorPurchasing;
	}

	public Long getProduceFac() {
		return produceFac;
	}

	public void setProduceFac(Long produceFac) {
		this.produceFac = produceFac;
	}

	public Integer getProduceState() {
		return produceState;
	}

	public void setProduceState(Integer produceState) {
		this.produceState = produceState;
	}

	public void setIsBalances(Integer isBalances){
		this.isBalances=isBalances;
	}

	public Integer getIsBalances(){
		return this.isBalances;
	}

	public void setLocCode(String locCode){
		this.locCode=locCode;
	}

	public String getLocCode(){
		return this.locCode;
	}

	public void setBalanceCode(String balanceCode){
		this.balanceCode=balanceCode;
	}

	public String getBalanceCode(){
		return this.balanceCode;
	}

	public void setPartType(Integer partType){
		this.partType=partType;
	}

	public Integer getPartType(){
		return this.partType;
	}

	public void setBatchNo(String batchNo){
		this.batchNo=batchNo;
	}

	public String getBatchNo(){
		return this.batchNo;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setIsGauge(Integer isGauge){
		this.isGauge=isGauge;
	}

	public Integer getIsGauge(){
		return this.isGauge;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setPartId(Long partId){
		this.partId=partId;
	}

	public Long getPartId(){
		return this.partId;
	}

	public void setInType(Integer inType){
		this.inType=inType;
	}

	public Integer getInType(){
		return this.inType;
	}

	public void setInCode(String inCode){
		this.inCode=inCode;
	}

	public String getInCode(){
		return this.inCode;
	}

	public void setBalAmount(Double balAmount){
		this.balAmount=balAmount;
	}

	public Double getBalAmount(){
		return this.balAmount;
	}

	public void setRemark1(String remark1){
		this.remark1=remark1;
	}

	public String getRemark1(){
		return this.remark1;
	}

	public void setBuyPrice1(Double buyPrice1){
		this.buyPrice1=buyPrice1;
	}

	public Double getBuyPrice1(){
		return this.buyPrice1;
	}

	public void setReturnQty(Long returnQty){
		this.returnQty=returnQty;
	}

	public Long getReturnQty(){
		return this.returnQty;
	}

	public void setDeleteDate(Date deleteDate){
		this.deleteDate=deleteDate;
	}

	public Date getDeleteDate(){
		return this.deleteDate;
	}

	public void setOrderCode(String orderCode){
		this.orderCode=orderCode;
	}

	public String getOrderCode(){
		return this.orderCode;
	}

	public void setVenderCode(String venderCode){
		this.venderCode=venderCode;
	}

	public String getVenderCode(){
		return this.venderCode;
	}

	public void setWhId(Long whId){
		this.whId=whId;
	}

	public Long getWhId(){
		return this.whId;
	}

	public void setCheckQty(Long checkQty){
		this.checkQty=checkQty;
	}

	public Long getCheckQty(){
		return this.checkQty;
	}

	public void setInAmount(Double inAmount){
		this.inAmount=inAmount;
	}

	public Double getInAmount(){
		return this.inAmount;
	}

	public void setPartCname(String partCname){
		this.partCname=partCname;
	}

	public String getPartCname(){
		return this.partCname;
	}

	public void setCheckBy(Long checkBy){
		this.checkBy=checkBy;
	}

	public Long getCheckBy(){
		return this.checkBy;
	}

	public void setInBy(Long inBy){
		this.inBy=inBy;
	}

	public Long getInBy(){
		return this.inBy;
	}

	public void setBuyPrice(Double buyPrice){
		this.buyPrice=buyPrice;
	}

	public Double getBuyPrice(){
		return this.buyPrice;
	}

	public void setBalanceType(Integer balanceType){
		this.balanceType=balanceType;
	}

	public Integer getBalanceType(){
		return this.balanceType;
	}

	public void setBuyPriceNotax(Double buyPriceNotax){
		this.buyPriceNotax=buyPriceNotax;
	}

	public Double getBuyPriceNotax(){
		return this.buyPriceNotax;
	}

	public void setBalQty(Long balQty){
		this.balQty=balQty;
	}

	public Long getBalQty(){
		return this.balQty;
	}

	public void setBalanceBy(Long balanceBy){
		this.balanceBy=balanceBy;
	}

	public Long getBalanceBy(){
		return this.balanceBy;
	}

	public void setWhName(String whName){
		this.whName=whName;
	}

	public String getWhName(){
		return this.whName;
	}

	public void setFtvenderId(Long ftvenderId){
		this.ftvenderId=ftvenderId;
	}

	public Long getFtvenderId(){
		return this.ftvenderId;
	}

	public void setVer(Integer ver){
		this.ver=ver;
	}

	public Integer getVer(){
		return this.ver;
	}

	public void setTaxRate(Double taxRate){
		this.taxRate=taxRate;
	}

	public Double getTaxRate(){
		return this.taxRate;
	}

	public void setItemQty(Long itemQty){
		this.itemQty=itemQty;
	}

	public Long getItemQty(){
		return this.itemQty;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setBalAmountNotax(Double balAmountNotax){
		this.balAmountNotax=balAmountNotax;
	}

	public Double getBalAmountNotax(){
		return this.balAmountNotax;
	}

	public void setState(Integer state){
		this.state=state;
	}

	public Integer getState(){
		return this.state;
	}

	public void setBuyGroup(Long buyGroup){
		this.buyGroup=buyGroup;
	}

	public Long getBuyGroup(){
		return this.buyGroup;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setCkPriceBy(Long ckPriceBy){
		this.ckPriceBy=ckPriceBy;
	}

	public Long getCkPriceBy(){
		return this.ckPriceBy;
	}

	public void setFcvderId(Long fcvderId){
		this.fcvderId=fcvderId;
	}

	public Long getFcvderId(){
		return this.fcvderId;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setMakerId(Long makerId){
		this.makerId=makerId;
	}

	public Long getMakerId(){
		return this.makerId;
	}

	public void setCheckDate(Date checkDate){
		this.checkDate=checkDate;
	}

	public Date getCheckDate(){
		return this.checkDate;
	}

	public void setInAmountNotax(Double inAmountNotax){
		this.inAmountNotax=inAmountNotax;
	}

	public Double getInAmountNotax(){
		return this.inAmountNotax;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setInId(Long inId){
		this.inId=inId;
	}

	public Long getInId(){
		return this.inId;
	}

	public void setIsCheck(Integer isCheck){
		this.isCheck=isCheck;
	}

	public Integer getIsCheck(){
		return this.isCheck;
	}

	public void setVenderName(String venderName){
		this.venderName=venderName;
	}

	public String getVenderName(){
		return this.venderName;
	}

	public void setCkPriceDate(Date ckPriceDate){
		this.ckPriceDate=ckPriceDate;
	}

	public Date getCkPriceDate(){
		return this.ckPriceDate;
	}

	public void setVenderId(Long venderId){
		this.venderId=venderId;
	}

	public Long getVenderId(){
		return this.venderId;
	}

	public void setLocId(Long locId){
		this.locId=locId;
	}

	public Long getLocId(){
		return this.locId;
	}

	public void setDeleteBy(Long deleteBy){
		this.deleteBy=deleteBy;
	}

	public Long getDeleteBy(){
		return this.deleteBy;
	}

	public void setDisableBy(Long disableBy){
		this.disableBy=disableBy;
	}

	public Long getDisableBy(){
		return this.disableBy;
	}

	public void setBuyQty(Long buyQty){
		this.buyQty=buyQty;
	}

	public Long getBuyQty(){
		return this.buyQty;
	}

	public void setBuyPrice1Notax(Double buyPrice1Notax){
		this.buyPrice1Notax=buyPrice1Notax;
	}

	public Double getBuyPrice1Notax(){
		return this.buyPrice1Notax;
	}

	public void setCheckId(Long checkId){
		this.checkId=checkId;
	}

	public Long getCheckId(){
		return this.checkId;
	}

	public void setFcvderrCode(String fcvderrCode){
		this.fcvderrCode=fcvderrCode;
	}

	public String getFcvderrCode(){
		return this.fcvderrCode;
	}

	public void setUnit(String unit){
		this.unit=unit;
	}

	public String getUnit(){
		return this.unit;
	}

	public void setBalanceId(Long balanceId){
		this.balanceId=balanceId;
	}

	public Long getBalanceId(){
		return this.balanceId;
	}

	public void setDisableDate(Date disableDate){
		this.disableDate=disableDate;
	}

	public Date getDisableDate(){
		return this.disableDate;
	}

	public void setPlanCode(String planCode){
		this.planCode=planCode;
	}

	public String getPlanCode(){
		return this.planCode;
	}

	public void setPoId(Long poId){
		this.poId=poId;
	}

	public Long getPoId(){
		return this.poId;
	}

	public void setPartCode(String partCode){
		this.partCode=partCode;
	}

	public String getPartCode(){
		return this.partCode;
	}

	public void setBalanceDate(Date balanceDate){
		this.balanceDate=balanceDate;
	}

	public Date getBalanceDate(){
		return this.balanceDate;
	}

	public void setCheckCode(String checkCode){
		this.checkCode=checkCode;
	}

	public String getCheckCode(){
		return this.checkCode;
	}

	public void setOrgId(Long orgId){
		this.orgId=orgId;
	}

	public Long getOrgId(){
		return this.orgId;
	}

	public void setBuyerId(Long buyerId){
		this.buyerId=buyerId;
	}

	public Long getBuyerId(){
		return this.buyerId;
	}

	public void setInvoNo(String invoNo){
		this.invoNo=invoNo;
	}

	public String getInvoNo(){
		return this.invoNo;
	}

	public void setInQty(Long inQty){
		this.inQty=inQty;
	}

	public Long getInQty(){
		return this.inQty;
	}

	public void setInDate(Date inDate){
		this.inDate=inDate;
	}

	public Date getInDate(){
		return this.inDate;
	}

	public void setOriginType(Integer originType){
		this.originType=originType;
	}

	public Integer getOriginType(){
		return this.originType;
	}

	public void setPartOldcode(String partOldcode){
		this.partOldcode=partOldcode;
	}

	public String getPartOldcode(){
		return this.partOldcode;
	}

}