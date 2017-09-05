/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-01-15 13:58:04
* CreateBy   : wswcx
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtSalesDlCreditLmtRecordPO extends PO{
	//信用额度ID
	private Long creditId;
	//记录ID
	private Long recordId;
	//修改前信用额度
	private Double beforeCreditAmount;
	//修改后信用额度
	private Double afterCreditAmount;
	//经销商ID
	private Long dealerId;
	//更新时间
	private Date updateDate;
	//更新人
	private Long updatePer;
	//备注
	private String remark;
	//创建时间
	private Date createDate;
	//生效时间
	private Date effectDate;
	//状态
	private Integer status;
	//审核状态
	private Integer recordStatus;
	//创建人
	private Long createPer;
	//终止时间
	private Date terminationDate;
	//审核时间
	private Date examineDate;
	//审核人
	private Long examinePer;

	public void setCreditId(Long creditId){
		this.creditId=creditId;
	}

	public Long getCreditId(){
		return this.creditId;
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

	public void setUpdatePer(Long updatePer){
		this.updatePer=updatePer;
	}

	public Long getUpdatePer(){
		return this.updatePer;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setEffectDate(Date effectDate){
		this.effectDate=effectDate;
	}

	public Date getEffectDate(){
		return this.effectDate;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setCreatePer(Long createPer){
		this.createPer=createPer;
	}

	public Long getCreatePer(){
		return this.createPer;
	}

	public void setTerminationDate(Date terminationDate){
		this.terminationDate=terminationDate;
	}

	public Date getTerminationDate(){
		return this.terminationDate;
	}

	public Long getRecordId() {
		return recordId;
	}

	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}

	public Double getBeforeCreditAmount() {
		return beforeCreditAmount;
	}

	public void setBeforeCreditAmount(Double beforeCreditAmount) {
		this.beforeCreditAmount = beforeCreditAmount;
	}

	public Double getAfterCreditAmount() {
		return afterCreditAmount;
	}

	public void setAfterCreditAmount(Double afterCreditAmount) {
		this.afterCreditAmount = afterCreditAmount;
	}

	public Integer getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}

	public Date getExamineDate() {
		return examineDate;
	}

	public void setExamineDate(Date examineDate) {
		this.examineDate = examineDate;
	}

	public Long getExaminePer() {
		return examinePer;
	}

	public void setExaminePer(Long examinePer) {
		this.examinePer = examinePer;
	}
	

}