/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-03-26 11:25:50
* CreateBy   : KFQ
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrRangeSinglePO extends PO{

	private Double relatedLosses;
	private Double outAmount;
	private String partName;
	private Long createBy;
	private String remark;
	private String outNo;
	private String partCode;
	private Double partAmount;
	private String partUnit;
	private Double smallAmount;
	private Double labourAmount;
	private String supplyName;
	private Long id;
	private Date createDate;
	private Integer partQuantity;
	private String supplyCode;
	private Integer outType;
	private Long auditBy;
	private Date auditDate;
	private Integer printNum;
	private Double printPart;
	private Long printBy;
	private Date printDate;
	private String rangeNo;
	private Double oldLabourAmount;
	
	public Double getOldLabourAmount() {
		return oldLabourAmount;
	}

	public void setOldLabourAmount(Double oldLabourAmount) {
		this.oldLabourAmount = oldLabourAmount;
	}

	public String getRangeNo() {
		return rangeNo;
	}

	public void setRangeNo(String rangeNo) {
		this.rangeNo = rangeNo;
	}

	public Long getPrintBy() {
		return printBy;
	}

	public void setPrintBy(Long printBy) {
		this.printBy = printBy;
	}

	public Date getPrintDate() {
		return printDate;
	}

	public void setPrintDate(Date printDate) {
		this.printDate = printDate;
	}

	public Double getPrintPart() {
		return printPart;
	}

	public void setPrintPart(Double printPart) {
		this.printPart = printPart;
	}

	public Integer getPrintNum() {
		return printNum;
	}

	public void setPrintNum(Integer printNum) {
		this.printNum = printNum;
	}

	public Long getAuditBy() {
		return auditBy;
	}

	public void setAuditBy(Long auditBy) {
		this.auditBy = auditBy;
	}

	public Date getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}

	public Integer getOutType() {
		return outType;
	}

	public void setOutType(Integer outType) {
		this.outType = outType;
	}

	public void setRelatedLosses(Double relatedLosses){
		this.relatedLosses=relatedLosses;
	}

	public Double getRelatedLosses(){
		return this.relatedLosses;
	}

	public void setOutAmount(Double outAmount){
		this.outAmount=outAmount;
	}

	public Double getOutAmount(){
		return this.outAmount;
	}

	public void setPartName(String partName){
		this.partName=partName;
	}

	public String getPartName(){
		return this.partName;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setOutNo(String outNo){
		this.outNo=outNo;
	}

	public String getOutNo(){
		return this.outNo;
	}

	public void setPartCode(String partCode){
		this.partCode=partCode;
	}

	public String getPartCode(){
		return this.partCode;
	}

	public void setPartAmount(Double partAmount){
		this.partAmount=partAmount;
	}

	public Double getPartAmount(){
		return this.partAmount;
	}

	public void setPartUnit(String partUnit){
		this.partUnit=partUnit;
	}

	public String getPartUnit(){
		return this.partUnit;
	}

	public void setSmallAmount(Double smallAmount){
		this.smallAmount=smallAmount;
	}

	public Double getSmallAmount(){
		return this.smallAmount;
	}

	public void setLabourAmount(Double labourAmount){
		this.labourAmount=labourAmount;
	}

	public Double getLabourAmount(){
		return this.labourAmount;
	}

	public void setSupplyName(String supplyName){
		this.supplyName=supplyName;
	}

	public String getSupplyName(){
		return this.supplyName;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setPartQuantity(Integer partQuantity){
		this.partQuantity=partQuantity;
	}

	public Integer getPartQuantity(){
		return this.partQuantity;
	}

	public void setSupplyCode(String supplyCode){
		this.supplyCode=supplyCode;
	}

	public String getSupplyCode(){
		return this.supplyCode;
	}

}