/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-12-22 10:49:26
* CreateBy   : Yu
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartStockChgDtlPO extends PO{

	private String unit;
	private Date deleteDate;
	private Date updateDate;
	private String remark;
	private Long createBy;
	private Long chgId;
	private Long towhId;
	private Integer status;
	private String partCode;
	private Long tolocId;
	private Long whId;
	private Long checkQty;
	private Long outQty;
	private Long dtlId;
	private String partCname;
	private Long updateBy;
	private Long inQty;
	private Long partId;
	private Long applyQty;
	private Long locId;
	private Integer ver;
	private String partOldcode;
	private Long deleteBy;
	private Date createDate;
	
	private Long inpartId;
	private String inpartCode;
	private String inpartOldcode;
	private Double adjustAmount;
	private Long sumQty;
	private String inpartCname;
	private Double costPrice;
	private Double inCost;
	private Long venderId;
	private Long balanceId;
	
	private Long batId;
	public Long getBatId() {
		return batId;
	}

	public void setBatId(Long batId) {
		this.batId = batId;
	}

	public Double getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(Double costPrice) {
		this.costPrice = costPrice;
	}

	public Double getInCost() {
		return inCost;
	}

	public void setInCost(Double inCost) {
		this.inCost = inCost;
	}

	public Long getVenderId() {
		return venderId;
	}

	public void setVenderId(Long venderId) {
		this.venderId = venderId;
	}

	public Long getBalanceId() {
		return balanceId;
	}

	public void setBalanceId(Long balanceId) {
		this.balanceId = balanceId;
	}

	public Double getAdjustAmount() {
		return adjustAmount;
	}

	public void setAdjustAmount(Double adjustAmount) {
		this.adjustAmount = adjustAmount;
	}

	public Long getSumQty() {
		return sumQty;
	}

	public void setSumQty(Long sumQty) {
		this.sumQty = sumQty;
	}

	public String getInpartCname() {
		return inpartCname;
	}

	public void setInpartCname(String inpartCname) {
		this.inpartCname = inpartCname;
	}

	public Long getInpartId() {
		return inpartId;
	}

	public void setInpartId(Long inpartId) {
		this.inpartId = inpartId;
	}

	public String getInpartCode() {
		return inpartCode;
	}

	public void setInpartCode(String inpartCode) {
		this.inpartCode = inpartCode;
	}

	public String getInpartOldcode() {
		return inpartOldcode;
	}

	public void setInpartOldcode(String inpartOldcode) {
		this.inpartOldcode = inpartOldcode;
	}

//	private Long batId;
//
//	public Long getBatId() {
//		return batId;
//	}
//
//	public void setBatId(Long batId) {
//		this.batId = batId;
//	}

	public void setUnit(String unit){
		this.unit=unit;
	}

	public String getUnit(){
		return this.unit;
	}

	public void setDeleteDate(Date deleteDate){
		this.deleteDate=deleteDate;
	}

	public Date getDeleteDate(){
		return this.deleteDate;
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

	public void setChgId(Long chgId){
		this.chgId=chgId;
	}

	public Long getChgId(){
		return this.chgId;
	}

	public void setTowhId(Long towhId){
		this.towhId=towhId;
	}

	public Long getTowhId(){
		return this.towhId;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setPartCode(String partCode){
		this.partCode=partCode;
	}

	public String getPartCode(){
		return this.partCode;
	}

	public void setTolocId(Long tolocId){
		this.tolocId=tolocId;
	}

	public Long getTolocId(){
		return this.tolocId;
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

	public void setOutQty(Long outQty){
		this.outQty=outQty;
	}

	public Long getOutQty(){
		return this.outQty;
	}

	public void setDtlId(Long dtlId){
		this.dtlId=dtlId;
	}

	public Long getDtlId(){
		return this.dtlId;
	}

	public void setPartCname(String partCname){
		this.partCname=partCname;
	}

	public String getPartCname(){
		return this.partCname;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setInQty(Long inQty){
		this.inQty=inQty;
	}

	public Long getInQty(){
		return this.inQty;
	}

	public void setPartId(Long partId){
		this.partId=partId;
	}

	public Long getPartId(){
		return this.partId;
	}

	public void setApplyQty(Long applyQty){
		this.applyQty=applyQty;
	}

	public Long getApplyQty(){
		return this.applyQty;
	}

	public void setLocId(Long locId){
		this.locId=locId;
	}

	public Long getLocId(){
		return this.locId;
	}

	public void setVer(Integer ver){
		this.ver=ver;
	}

	public Integer getVer(){
		return this.ver;
	}

	public void setPartOldcode(String partOldcode){
		this.partOldcode=partOldcode;
	}

	public String getPartOldcode(){
		return this.partOldcode;
	}

	public void setDeleteBy(Long deleteBy){
		this.deleteBy=deleteBy;
	}

	public Long getDeleteBy(){
		return this.deleteBy;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

}