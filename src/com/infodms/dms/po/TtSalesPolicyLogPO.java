/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-07-09 10:04:10
* CreateBy   : wangsw
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtSalesPolicyLogPO extends PO{

	private Date beforeStartDate;
	private Date beforeStopDate;
	private Date afterStartDate;
	private Date afterStopDate;
	private Long createUser;
	private Date deployTime;
	private Date updateDate;
	private Integer deployStatus;
	private String policyName;
	private Date createDate;
	private Long updateUser;
	private Double beforeDismoney;
	private Double afterDismoney;
	private Long policyId;
	private Long recordId;
	private Long deployUser;
	private Long beforePolicyType;
	private Long afterPolicyType;

	
	public void setCreateUser(Long createUser){
		this.createUser=createUser;
	}

	public Long getCreateUser(){
		return this.createUser;
	}

	public void setDeployTime(Date deployTime){
		this.deployTime=deployTime;
	}

	public Date getDeployTime(){
		return this.deployTime;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setDeployStatus(Integer deployStatus){
		this.deployStatus=deployStatus;
	}

	public Integer getDeployStatus(){
		return this.deployStatus;
	}

	public void setPolicyName(String policyName){
		this.policyName=policyName;
	}

	public String getPolicyName(){
		return this.policyName;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setUpdateUser(Long updateUser){
		this.updateUser=updateUser;
	}

	public Long getUpdateUser(){
		return this.updateUser;
	}

	public void setPolicyId(Long policyId){
		this.policyId=policyId;
	}

	public Long getPolicyId(){
		return this.policyId;
	}

	public Date getBeforeStartDate() {
		return beforeStartDate;
	}

	public void setBeforeStartDate(Date beforeStartDate) {
		this.beforeStartDate = beforeStartDate;
	}

	public Date getBeforeStopDate() {
		return beforeStopDate;
	}

	public void setBeforeStopDate(Date beforeStopDate) {
		this.beforeStopDate = beforeStopDate;
	}

	public Date getAfterStartDate() {
		return afterStartDate;
	}

	public void setAfterStartDate(Date afterStartDate) {
		this.afterStartDate = afterStartDate;
	}

	public Date getAfterStopDate() {
		return afterStopDate;
	}

	public void setAfterStopDate(Date afterStopDate) {
		this.afterStopDate = afterStopDate;
	}

	public Double getBeforeDismoney() {
		return beforeDismoney;
	}

	public void setBeforeDismoney(Double beforeDismoney) {
		this.beforeDismoney = beforeDismoney;
	}

	public Double getAfterDismoney() {
		return afterDismoney;
	}

	public void setAfterDismoney(Double afterDismoney) {
		this.afterDismoney = afterDismoney;
	}

	public Long getRecordId() {
		return recordId;
	}

	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}

	public Long getDeployUser() {
		return deployUser;
	}

	public void setDeployUser(Long deployUser) {
		this.deployUser = deployUser;
	}

	public Long getBeforePolicyType() {
		return beforePolicyType;
	}

	public void setBeforePolicyType(Long beforePolicyType) {
		this.beforePolicyType = beforePolicyType;
	}

	public Long getAfterPolicyType() {
		return afterPolicyType;
	}

	public void setAfterPolicyType(Long afterPolicyType) {
		this.afterPolicyType = afterPolicyType;
	}

	
	

}