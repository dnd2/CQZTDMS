/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-11-17 20:10:04
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TPcFollowPO extends PO{

	private String oldLevel;
	private Long followId;
	private String remark;
	private String createBy;
	private Long customerId;
	private Long nextTask;
	private Date followDate;
	private Integer taskStatus;
	private Date finishDate;
	private String followInfo;
	private Long previousTask;
	private String newLevel;
	private Date createDate;
	private String salesProgress;
	private String oldSalesProgress;
	private String followType;
	private String followPlan;
	private String mgrTip;
	private String restart_type;
	private String dataFrom;
	private Integer ifHandle;
	
	public Integer getIfHandle() {
		return ifHandle;
	}

	public void setIfHandle(Integer ifHandle) {
		this.ifHandle = ifHandle;
	}

	public String getDataFrom() {
		return dataFrom;
	}

	public void setDataFrom(String dataFrom) {
		this.dataFrom = dataFrom;
	}
	
	public String getMgrTip() {
		return mgrTip;
	}

	public void setMgrTip(String mgrTip) {
		this.mgrTip = mgrTip;
	}

	public String getRestart_type() {
		return restart_type;
	}

	public void setRestart_type(String restart_type) {
		this.restart_type = restart_type;
	}

	public String getFollowType() {
		return followType;
	}

	public void setFollowType(String followType) {
		this.followType = followType;
	}

	public String getFollowPlan() {
		return followPlan;
	}

	public void setFollowPlan(String followPlan) {
		this.followPlan = followPlan;
	}

	public String getOldSalesProgress() {
		return oldSalesProgress;
	}

	public void setOldSalesProgress(String oldSalesProgress) {
		this.oldSalesProgress = oldSalesProgress;
	}

	public String getSalesProgress() {
		return salesProgress;
	}

	public void setSalesProgress(String salesProgress) {
		this.salesProgress = salesProgress;
	}

	public void setFollowId(Long followId){
		this.followId=followId;
	}

	public Long getFollowId(){
		return this.followId;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setCreateBy(String createBy){
		this.createBy=createBy;
	}

	public String getCreateBy(){
		return this.createBy;
	}

	public void setCustomerId(Long customerId){
		this.customerId=customerId;
	}

	public Long getCustomerId(){
		return this.customerId;
	}

	public void setNextTask(Long nextTask){
		this.nextTask=nextTask;
	}

	public Long getNextTask(){
		return this.nextTask;
	}

	public void setFollowDate(Date followDate){
		this.followDate=followDate;
	}

	public Date getFollowDate(){
		return this.followDate;
	}

	public Integer getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(Integer taskStatus) {
		this.taskStatus = taskStatus;
	}

	public void setFinishDate(Date finishDate){
		this.finishDate=finishDate;
	}

	public Date getFinishDate(){
		return this.finishDate;
	}

	public void setFollowInfo(String followInfo){
		this.followInfo=followInfo;
	}

	public String getFollowInfo(){
		return this.followInfo;
	}

	public void setPreviousTask(Long previousTask){
		this.previousTask=previousTask;
	}

	public Long getPreviousTask(){
		return this.previousTask;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public String getOldLevel() {
		return oldLevel;
	}

	public void setOldLevel(String oldLevel) {
		this.oldLevel = oldLevel;
	}

	public String getNewLevel() {
		return newLevel;
	}

	public void setNewLevel(String newLevel) {
		this.newLevel = newLevel;
	}

}