/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-06-28 09:31:00
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TsiTtSalesBoardPO extends PO{

	private String carTeam;
	private Integer sendNum;
	private Date allocaDate;
	private Integer outNum;
	private Integer allocaNum;
	private Date updateDate;
	private Long allocaPer;
	private Date boDate;
	private Long createBy;
	private String boNo;
	private Integer isEnable;
	private Date createDate;
	private String carNo;
	private Long boId;
	private Integer accNum;
	private Integer handleStatus;
	private Long boPer;
	private String loads;
	private Long updateBy;
	private Long areaId;
	private Integer boNum;
	private String policyNo;
	private Long policyType;
	private String driverName;
	private String driverTel;
	private String haveRetail;//是否有零售
	private String boStatus;
	private Integer dlvShipType;
	private Long dlvLogiId;
	private Long dlvBalProvId;
	private Long dlvBalCityId;
	private Long dlvBalCountyId;
	private Date dlvFyDate;
	private Date dlvJjDate;
	private Date planLoadDate;
	
	public Date getDlvFyDate() {
		return dlvFyDate;
	}

	public void setDlvFyDate(Date dlvFyDate) {
		this.dlvFyDate = dlvFyDate;
	}

	public Date getDlvJjDate() {
		return dlvJjDate;
	}

	public void setDlvJjDate(Date dlvJjDate) {
		this.dlvJjDate = dlvJjDate;
	}

	public Date getPlanLoadDate() {
		return planLoadDate;
	}

	public void setPlanLoadDate(Date planLoadDate) {
		this.planLoadDate = planLoadDate;
	}

	public Integer getDlvShipType() {
		return dlvShipType;
	}

	public void setDlvShipType(Integer dlvShipType) {
		this.dlvShipType = dlvShipType;
	}

	public Long getDlvLogiId() {
		return dlvLogiId;
	}

	public void setDlvLogiId(Long dlvLogiId) {
		this.dlvLogiId = dlvLogiId;
	}

	public Long getDlvBalProvId() {
		return dlvBalProvId;
	}

	public void setDlvBalProvId(Long dlvBalProvId) {
		this.dlvBalProvId = dlvBalProvId;
	}

	public Long getDlvBalCityId() {
		return dlvBalCityId;
	}

	public void setDlvBalCityId(Long dlvBalCityId) {
		this.dlvBalCityId = dlvBalCityId;
	}

	public Long getDlvBalCountyId() {
		return dlvBalCountyId;
	}

	public void setDlvBalCountyId(Long dlvBalCountyId) {
		this.dlvBalCountyId = dlvBalCountyId;
	}

	public String getBoStatus() {
		return boStatus;
	}

	public void setBoStatus(String boStatus) {
		this.boStatus = boStatus;
	}

	public String getHaveRetail() {
		return haveRetail;
	}

	public void setHaveRetail(String haveRetail) {
		this.haveRetail = haveRetail;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public Long getPolicyType() {
		return policyType;
	}

	public void setPolicyType(Long policyType) {
		this.policyType = policyType;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getDriverTel() {
		return driverTel;
	}

	public void setDriverTel(String driverTel) {
		this.driverTel = driverTel;
	}

	public void setCarTeam(String carTeam){
		this.carTeam=carTeam;
	}

	public String getCarTeam(){
		return this.carTeam;
	}

	public void setSendNum(Integer sendNum){
		this.sendNum=sendNum;
	}

	public Integer getSendNum(){
		return this.sendNum;
	}

	public void setAllocaDate(Date allocaDate){
		this.allocaDate=allocaDate;
	}

	public Date getAllocaDate(){
		return this.allocaDate;
	}

	public void setOutNum(Integer outNum){
		this.outNum=outNum;
	}

	public Integer getOutNum(){
		return this.outNum;
	}

	public void setAllocaNum(Integer allocaNum){
		this.allocaNum=allocaNum;
	}

	public Integer getAllocaNum(){
		return this.allocaNum;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setAllocaPer(Long allocaPer){
		this.allocaPer=allocaPer;
	}

	public Long getAllocaPer(){
		return this.allocaPer;
	}

	public void setBoDate(Date boDate){
		this.boDate=boDate;
	}

	public Date getBoDate(){
		return this.boDate;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setBoNo(String boNo){
		this.boNo=boNo;
	}

	public String getBoNo(){
		return this.boNo;
	}

	public void setIsEnable(Integer isEnable){
		this.isEnable=isEnable;
	}

	public Integer getIsEnable(){
		return this.isEnable;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setCarNo(String carNo){
		this.carNo=carNo;
	}

	public String getCarNo(){
		return this.carNo;
	}

	public void setBoId(Long boId){
		this.boId=boId;
	}

	public Long getBoId(){
		return this.boId;
	}

	public void setAccNum(Integer accNum){
		this.accNum=accNum;
	}

	public Integer getAccNum(){
		return this.accNum;
	}

	public void setHandleStatus(Integer handleStatus){
		this.handleStatus=handleStatus;
	}

	public Integer getHandleStatus(){
		return this.handleStatus;
	}

	public void setBoPer(Long boPer){
		this.boPer=boPer;
	}

	public Long getBoPer(){
		return this.boPer;
	}

	public void setLoads(String loads){
		this.loads=loads;
	}

	public String getLoads(){
		return this.loads;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setAreaId(Long areaId){
		this.areaId=areaId;
	}

	public Long getAreaId(){
		return this.areaId;
	}

	public void setBoNum(Integer boNum){
		this.boNum=boNum;
	}

	public Integer getBoNum(){
		return this.boNum;
	}

}