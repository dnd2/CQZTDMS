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
public class TPcInviteShopPO extends PO{

	private Long inviteId;
	private String oldLevel;
	private Integer ifShop;
	private String remark;
	private String createBy;
	private String salesProgress;
	private Long customerId;
	private Long nextTask;
	private Date finishDate;
	private Long inviteShopId;
	private Date inviteShopDate;
	private Integer taskStatus;
	private Date shopDate;
	private Long previousTask;
	private String newLevel;
	private Date createDate;
	private String oldSalesProgress;
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

	public String getOldSalesProgress() {
		return oldSalesProgress;
	}

	public void setOldSalesProgress(String oldSalesProgress) {
		this.oldSalesProgress = oldSalesProgress;
	}

	public void setInviteId(Long inviteId){
		this.inviteId=inviteId;
	}

	public Long getInviteId(){
		return this.inviteId;
	}

	public void setOldLevel(String oldLevel){
		this.oldLevel=oldLevel;
	}

	public String getOldLevel(){
		return this.oldLevel;
	}

	public Integer getIfShop() {
		return ifShop;
	}

	public void setIfShop(Integer ifShop) {
		this.ifShop = ifShop;
	}

	public Date getShopDate() {
		return shopDate;
	}

	public void setShopDate(Date shopDate) {
		this.shopDate = shopDate;
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

	public void setFinishDate(Date finishDate){
		this.finishDate=finishDate;
	}

	public Date getFinishDate(){
		return this.finishDate;
	}

	public void setInviteShopId(Long inviteShopId){
		this.inviteShopId=inviteShopId;
	}

	public Long getInviteShopId(){
		return this.inviteShopId;
	}

	public void setInviteShopDate(Date inviteShopDate){
		this.inviteShopDate=inviteShopDate;
	}

	public Date getInviteShopDate(){
		return this.inviteShopDate;
	}

	public void setTaskStatus(Integer taskStatus){
		this.taskStatus=taskStatus;
	}

	public Integer getTaskStatus(){
		return this.taskStatus;
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

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

}