/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-12-01 09:46:05
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TPcDefeatfailurePO extends PO{

	private Integer defeatfailureType;
	private Date updateDate;
	private Date mgrConDate;
	private String salesProgress;
	private Date defeatStartDate;
	private String createBy;
	private Date createDate;
	private Date defeatEndDate;
	private Long auditStatus;
	private String defeatType;
	private String defeatModel;
	private Integer status;
	private Long previousTask;
	private Long customerId;
	private String updateBy;
	private String newLevel;
	private Long defeatfailureId;
	private String oldLevel;
	private String reasonAnalysis;
	private String oldSalesProgress;
	private Date dcrcConDate;
	private String defeatWay;
	private Date failureDate;
	private String dataFrom;
	private Long nextTask;
	private Long leadsCode;
	
	public Long getNextTask() {
		return nextTask;
	}

	public void setNextTask(Long nextTask) {
		this.nextTask = nextTask;
	}

	public String getDataFrom() {
		return dataFrom;
	}

	public void setDataFrom(String dataFrom) {
		this.dataFrom = dataFrom;
	}

	public Date getFailureDate() {
		return failureDate;
	}

	public void setFailureDate(Date failureDate) {
		this.failureDate = failureDate;
	}

	public String getDefeatType() {
		return defeatType;
	}

	public void setDefeatType(String defeatType) {
		this.defeatType = defeatType;
	}

	public String getDefeatWay() {
		return defeatWay;
	}

	public void setDefeatWay(String defeatWay) {
		this.defeatWay = defeatWay;
	}

	public void setDefeatfailureType(Integer defeatfailureType){
		this.defeatfailureType=defeatfailureType;
	}

	public Integer getDefeatfailureType(){
		return this.defeatfailureType;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setMgrConDate(Date mgrConDate){
		this.mgrConDate=mgrConDate;
	}

	public Date getMgrConDate(){
		return this.mgrConDate;
	}

	public void setSalesProgress(String salesProgress){
		this.salesProgress=salesProgress;
	}

	public String getSalesProgress(){
		return this.salesProgress;
	}

	public void setDefeatStartDate(Date defeatStartDate){
		this.defeatStartDate=defeatStartDate;
	}

	public Date getDefeatStartDate(){
		return this.defeatStartDate;
	}

	public void setCreateBy(String createBy){
		this.createBy=createBy;
	}

	public String getCreateBy(){
		return this.createBy;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setDefeatEndDate(Date defeatEndDate){
		this.defeatEndDate=defeatEndDate;
	}

	public Date getDefeatEndDate(){
		return this.defeatEndDate;
	}

	public void setAuditStatus(Long auditStatus){
		this.auditStatus=auditStatus;
	}

	public Long getAuditStatus(){
		return this.auditStatus;
	}

	public String getDefeatModel() {
		return defeatModel;
	}

	public void setDefeatModel(String defeatModel) {
		this.defeatModel = defeatModel;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setPreviousTask(Long previousTask){
		this.previousTask=previousTask;
	}

	public Long getPreviousTask(){
		return this.previousTask;
	}

	public void setCustomerId(Long customerId){
		this.customerId=customerId;
	}

	public Long getCustomerId(){
		return this.customerId;
	}

	public void setUpdateBy(String updateBy){
		this.updateBy=updateBy;
	}

	public String getUpdateBy(){
		return this.updateBy;
	}

	public void setNewLevel(String newLevel){
		this.newLevel=newLevel;
	}

	public String getNewLevel(){
		return this.newLevel;
	}

	public void setDefeatfailureId(Long defeatfailureId){
		this.defeatfailureId=defeatfailureId;
	}

	public Long getDefeatfailureId(){
		return this.defeatfailureId;
	}

	public void setOldLevel(String oldLevel){
		this.oldLevel=oldLevel;
	}

	public String getOldLevel(){
		return this.oldLevel;
	}

	public void setReasonAnalysis(String reasonAnalysis){
		this.reasonAnalysis=reasonAnalysis;
	}

	public String getReasonAnalysis(){
		return this.reasonAnalysis;
	}

	public void setOldSalesProgress(String oldSalesProgress){
		this.oldSalesProgress=oldSalesProgress;
	}

	public String getOldSalesProgress(){
		return this.oldSalesProgress;
	}

	public void setDcrcConDate(Date dcrcConDate){
		this.dcrcConDate=dcrcConDate;
	}

	public Date getDcrcConDate(){
		return this.dcrcConDate;
	}

	public Long getLeadsCode() {
		return leadsCode;
	}

	public void setLeadsCode(Long leadsCode) {
		this.leadsCode = leadsCode;
	}

}