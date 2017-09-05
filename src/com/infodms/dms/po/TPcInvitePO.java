/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-11-20 10:46:09
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TPcInvitePO extends PO{

	private Long inviteId;
	private String trustDesign;
	private String sceneDesign;
	private String oldLevel;
	private String remark;
	private String salesProgress;
	private Long customerId;
	private Long nextTask;
	private Date planMeetDate;
	private Date finishDate;
	private Integer ifInvite;
	private Date planInviteDate;
	private Integer inviteWay;
	private Integer taskStatus;
	private Integer inviteType;
	private Integer ifPlan;
	private Long previousTask;
	private String newLevel;
	private String inviteTarget;
	private String auditRemark;
	private Date createDate;
	private Integer directorAudit;
	private String requirement;
	private String createBy;
	private String oldSalesProgress;
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

	public String getOldSalesProgress() {
		return oldSalesProgress;
	}

	public void setOldSalesProgress(String oldSalesProgress) {
		this.oldSalesProgress = oldSalesProgress;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setInviteId(Long inviteId){
		this.inviteId=inviteId;
	}

	public Long getInviteId(){
		return this.inviteId;
	}

	public void setTrustDesign(String trustDesign){
		this.trustDesign=trustDesign;
	}

	public String getTrustDesign(){
		return this.trustDesign;
	}

	public void setSceneDesign(String sceneDesign){
		this.sceneDesign=sceneDesign;
	}

	public String getSceneDesign(){
		return this.sceneDesign;
	}

	public void setOldLevel(String oldLevel){
		this.oldLevel=oldLevel;
	}

	public String getOldLevel(){
		return this.oldLevel;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setSalesProgress(String salesProgress){
		this.salesProgress=salesProgress;
	}

	public String getSalesProgress(){
		return this.salesProgress;
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

	public void setPlanMeetDate(Date planMeetDate){
		this.planMeetDate=planMeetDate;
	}

	public Date getPlanMeetDate(){
		return this.planMeetDate;
	}

	public void setFinishDate(Date finishDate){
		this.finishDate=finishDate;
	}

	public Date getFinishDate(){
		return this.finishDate;
	}

	public void setIfInvite(Integer ifInvite){
		this.ifInvite=ifInvite;
	}

	public Integer getIfInvite(){
		return this.ifInvite;
	}

	public void setPlanInviteDate(Date planInviteDate){
		this.planInviteDate=planInviteDate;
	}

	public Date getPlanInviteDate(){
		return this.planInviteDate;
	}

	public void setInviteWay(Integer inviteWay){
		this.inviteWay=inviteWay;
	}

	public Integer getInviteWay(){
		return this.inviteWay;
	}

	public void setTaskStatus(Integer taskStatus){
		this.taskStatus=taskStatus;
	}

	public Integer getTaskStatus(){
		return this.taskStatus;
	}

	public void setInviteType(Integer inviteType){
		this.inviteType=inviteType;
	}

	public Integer getInviteType(){
		return this.inviteType;
	}

	public void setIfPlan(Integer ifPlan){
		this.ifPlan=ifPlan;
	}

	public Integer getIfPlan(){
		return this.ifPlan;
	}

	public void setPreviousTask(Long previousTask){
		this.previousTask=previousTask;
	}

	public Long getPreviousTask(){
		return this.previousTask;
	}

	public void setNewLevel(String newLevel){
		this.newLevel=newLevel;
	}

	public String getNewLevel(){
		return this.newLevel;
	}

	public void setInviteTarget(String inviteTarget){
		this.inviteTarget=inviteTarget;
	}

	public String getInviteTarget(){
		return this.inviteTarget;
	}

	public void setAuditRemark(String auditRemark){
		this.auditRemark=auditRemark;
	}

	public String getAuditRemark(){
		return this.auditRemark;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setDirectorAudit(Integer directorAudit){
		this.directorAudit=directorAudit;
	}

	public Integer getDirectorAudit(){
		return this.directorAudit;
	}

	public void setRequirement(String requirement){
		this.requirement=requirement;
	}

	public String getRequirement(){
		return this.requirement;
	}

}