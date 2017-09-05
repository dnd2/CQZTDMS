/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-05-21 16:50:12
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrGamePO extends PO{

	private Long ruleId;
	private Integer claimMonth;
	private Long companyId;
	private Date endDate;
	private Date updateDate;
	private Double wrMelieage;
	private String remark;
	private Long createBy;
	private Integer wrMonth;
	private Date startDate;
	private String gameName;
	private Long maintainNum;
	private Long updateBy;
	private Long id;
	private String gameCode;
	private Integer gameStatus;
	private Date createDate;
	private Double claimMelieage;
	private Integer gameType;
	private String vehicelProperty;//三包策略增加车辆属性
	private Integer isForBusi;
	private String vehicleProBusi;
	private String vehicleProBusiDesc;
	private Integer isNew;
	private Integer isOperating;
	private Integer vehicleType;
	
	public Integer getIsNew() {
		return isNew;
	}

	public void setIsNew(Integer isNew) {
		this.isNew = isNew;
	}

	public Integer getIsOperating() {
		return isOperating;
	}

	public void setIsOperating(Integer isOperating) {
		this.isOperating = isOperating;
	}

	public Integer getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(Integer vehicleType) {
		this.vehicleType = vehicleType;
	}

	public String getVehicleProBusiDesc() {
		return vehicleProBusiDesc;
	}

	public void setVehicleProBusiDesc(String vehicleProBusiDesc) {
		this.vehicleProBusiDesc = vehicleProBusiDesc;
	}

	public String getVehicelProperty() {
		return vehicelProperty;
	}

	public void setVehicelProperty(String vehicelProperty) {
		this.vehicelProperty = vehicelProperty;
	}

	public void setRuleId(Long ruleId){
		this.ruleId=ruleId;
	}

	public Long getRuleId(){
		return this.ruleId;
	}

	public void setClaimMonth(Integer claimMonth){
		this.claimMonth=claimMonth;
	}

	public Integer getClaimMonth(){
		return this.claimMonth;
	}

	public void setCompanyId(Long companyId){
		this.companyId=companyId;
	}

	public Long getCompanyId(){
		return this.companyId;
	}

	public void setEndDate(Date endDate){
		this.endDate=endDate;
	}

	public Date getEndDate(){
		return this.endDate;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setWrMelieage(Double wrMelieage){
		this.wrMelieage=wrMelieage;
	}

	public Double getWrMelieage(){
		return this.wrMelieage;
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

	public void setWrMonth(Integer wrMonth){
		this.wrMonth=wrMonth;
	}

	public Integer getWrMonth(){
		return this.wrMonth;
	}

	public void setStartDate(Date startDate){
		this.startDate=startDate;
	}

	public Date getStartDate(){
		return this.startDate;
	}

	public void setGameName(String gameName){
		this.gameName=gameName;
	}

	public String getGameName(){
		return this.gameName;
	}

	public void setMaintainNum(Long maintainNum){
		this.maintainNum=maintainNum;
	}

	public Long getMaintainNum(){
		return this.maintainNum;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setGameCode(String gameCode){
		this.gameCode=gameCode;
	}

	public String getGameCode(){
		return this.gameCode;
	}

	public void setGameStatus(Integer gameStatus){
		this.gameStatus=gameStatus;
	}

	public Integer getGameStatus(){
		return this.gameStatus;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setClaimMelieage(Double claimMelieage){
		this.claimMelieage=claimMelieage;
	}

	public Double getClaimMelieage(){
		return this.claimMelieage;
	}

	public void setGameType(Integer gameType){
		this.gameType=gameType;
	}

	public Integer getGameType(){
		return this.gameType;
	}

	public Integer getIsForBusi() {
		return isForBusi;
	}

	public void setIsForBusi(Integer isForBusi) {
		this.isForBusi = isForBusi;
	}

	public String getVehicleProBusi() {
		return vehicleProBusi;
	}

	public void setVehicleProBusi(String vehicleProBusi) {
		this.vehicleProBusi = vehicleProBusi;
	}

}