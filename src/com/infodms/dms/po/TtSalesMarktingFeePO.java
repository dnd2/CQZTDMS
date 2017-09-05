/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2015-01-16 10:10:16
* CreateBy   : wizard_lee
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtSalesMarktingFeePO extends PO{

	private Date updateDate;
	private Integer actualFee;
	private Long createBy;
	private Float punishmentRate;
	private Long feeId;
	private Date feeStartDate;
	private Double totalFee;
	private Long updateBy;
	private Double feePunishment;
	private Date auditDate;
	private Date feeEndDate;
	private Double feeThreshold;
	private Date createDate;
	private Integer auditStatus;
	private Date submitDate;
	private Double actualSalesMoney;
	private Long dealerId;
	private Date firstSubmitDate;
	private String remark;
	private Integer actualSalesNum;
	
	public Date getFirstSubmitDate() {
		return firstSubmitDate;
	}

	public void setFirstSubmitDate(Date firstSubmitDate) {
		this.firstSubmitDate = firstSubmitDate;
	}

	public Long getDealerId() {
		return dealerId;
	}

	public void setDealerId(Long dealerId) {
		this.dealerId = dealerId;
	}

	public Double getActualSalesMoney() {
		return actualSalesMoney;
	}

	public void setActualSalesMoney(Double actualSalesMoney) {
		this.actualSalesMoney = actualSalesMoney;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setActualFee(Integer actualFee){
		this.actualFee=actualFee;
	}

	public Integer getActualFee(){
		return this.actualFee;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setPunishmentRate(Float punishmentRate){
		this.punishmentRate=punishmentRate;
	}

	public Float getPunishmentRate(){
		return this.punishmentRate;
	}

	public void setFeeId(Long feeId){
		this.feeId=feeId;
	}

	public Long getFeeId(){
		return this.feeId;
	}

	public void setFeeStartDate(Date feeStartDate){
		this.feeStartDate=feeStartDate;
	}

	public Date getFeeStartDate(){
		return this.feeStartDate;
	}

	public void setTotalFee(Double totalFee){
		this.totalFee=totalFee;
	}

	public Double getTotalFee(){
		return this.totalFee;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setFeePunishment(Double feePunishment){
		this.feePunishment=feePunishment;
	}

	public Double getFeePunishment(){
		return this.feePunishment;
	}

	public void setAuditDate(Date auditDate){
		this.auditDate=auditDate;
	}

	public Date getAuditDate(){
		return this.auditDate;
	}

	public void setFeeEndDate(Date feeEndDate){
		this.feeEndDate=feeEndDate;
	}

	public Date getFeeEndDate(){
		return this.feeEndDate;
	}

	public void setFeeThreshold(Double feeThreshold){
		this.feeThreshold=feeThreshold;
	}

	public Double getFeeThreshold(){
		return this.feeThreshold;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setAuditStatus(Integer auditStatus){
		this.auditStatus=auditStatus;
	}

	public Integer getAuditStatus(){
		return this.auditStatus;
	}

	public void setSubmitDate(Date submitDate){
		this.submitDate=submitDate;
	}

	public Date getSubmitDate(){
		return this.submitDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getActualSalesNum() {
		return actualSalesNum;
	}

	public void setActualSalesNum(Integer actualSalesNum) {
		this.actualSalesNum = actualSalesNum;
	}

}