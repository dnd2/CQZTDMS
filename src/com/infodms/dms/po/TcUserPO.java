/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-06-13 08:58:57
* CreateBy   : chun_chang
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TcUserPO extends PO{
 
	private Long companyId;
	private Long user_id;
	private Date birthday;
	private String handPhone;
	private String addr;
	private Date updateDate;
	private String empNum;
	private String password;
	private Long createBy;
	private Date lastsigninTime;
	private Integer userStatus;
	private Date createDate;
	private String acnt;
	private Integer approvalLevelCode;
	private String phone;
	private String email;
	private Integer zipCode;
	private Long updateBy;
	private Integer gender;
	private Integer isDown;
	private Long userId;
	private String name;
	private String personCode;
	private Integer balanceLevelCode;
	private Integer isFirst;
	private Date overDate;
	private Integer isLock;
	private Integer isDcs;
	private Long dealerId;
	private Long queryOnly;
	
	private Long poseRank;
	private Long parUserId;
	private Date entryDate;
	private Long groupId;
	private Integer  driverFlag;//司机标记  1
	
	
	
	/**
	 * @return the driverFlag
	 */
	public Integer getDriverFlag() {
		return driverFlag;
	}

	/**
	 * @param driverFlag the driverFlag to set
	 */
	public void setDriverFlag(Integer driverFlag) {
		this.driverFlag = driverFlag;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Date getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	public Long getParUserId() {
		return parUserId;
	}

	public void setParUserId(Long parUserId) {
		this.parUserId = parUserId;
	}

	public Long getPoseRank() {
		return poseRank;
	}

	public void setPoseRank(Long poseRank) {
		this.poseRank = poseRank;
	}

	public Long getQueryOnly() {
		return queryOnly;
	}

	public void setQueryOnly(Long queryOnly) {
		this.queryOnly = queryOnly;
	}

	public Long getDealerId() {
		return dealerId;
	}

	public void setDealerId(Long dealerId) {
		this.dealerId = dealerId;
	}

	/****xiongchuan 2011-10-11*******用户类型***************/
	private Integer  userType;

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public Integer getBalanceLevelCode() {
		return balanceLevelCode;
	}

	public void setBalanceLevelCode(Integer balanceLevelCode) {
		this.balanceLevelCode = balanceLevelCode;
	}

	public void setCompanyId(Long companyId){
		this.companyId=companyId;
	}

	public Long getCompanyId(){
		return this.companyId;
	}

	public void setBirthday(Date birthday){
		this.birthday=birthday;
	}

	public Date getBirthday(){
		return this.birthday;
	}

	public void setHandPhone(String handPhone){
		this.handPhone=handPhone;
	}

	public String getHandPhone(){
		return this.handPhone;
	}

	public void setAddr(String addr){
		this.addr=addr;
	}

	public String getAddr(){
		return this.addr;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setEmpNum(String empNum){
		this.empNum=empNum;
	}

	public String getEmpNum(){
		return this.empNum;
	}

	public void setPassword(String password){
		this.password=password;
	}

	public String getPassword(){
		return this.password;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setLastsigninTime(Date lastsigninTime){
		this.lastsigninTime=lastsigninTime;
	}

	public Date getLastsigninTime(){
		return this.lastsigninTime;
	}

	public void setUserStatus(Integer userStatus){
		this.userStatus=userStatus;
	}

	public Integer getUserStatus(){
		return this.userStatus;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setAcnt(String acnt){
		this.acnt=acnt;
	}

	public String getAcnt(){
		return this.acnt;
	}

	public void setApprovalLevelCode(Integer approvalLevelCode){
		this.approvalLevelCode=approvalLevelCode;
	}

	public Integer getApprovalLevelCode(){
		return this.approvalLevelCode;
	}

	public void setPhone(String phone){
		this.phone=phone;
	}

	public String getPhone(){
		return this.phone;
	}

	public void setEmail(String email){
		this.email=email;
	}

	public String getEmail(){
		return this.email;
	}

	public void setZipCode(Integer zipCode){
		this.zipCode=zipCode;
	}

	public Integer getZipCode(){
		return this.zipCode;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setGender(Integer gender){
		this.gender=gender;
	}

	public Integer getGender(){
		return this.gender;
	}

	public void setIsDown(Integer isDown){
		this.isDown=isDown;
	}

	public Integer getIsDown(){
		return this.isDown;
	}

	public void setUserId(Long userId){
		this.userId=userId;
	}

	public Long getUserId(){
		return this.userId;
	}

	public void setName(String name){
		this.name=name;
	}

	public String getName(){
		return this.name;
	}

	public void setPersonCode(String personCode){
		this.personCode=personCode;
	}

	public String getPersonCode(){
		return this.personCode;
	}

	public Integer getIsFirst() {
		return isFirst;
	}

	public void setIsFirst(Integer isFirst) {
		this.isFirst = isFirst;
	}

	public Date getOverDate() {
		return overDate;
	}

	public void setOverDate(Date overDate) {
		this.overDate = overDate;
	}

	public Integer getIsLock() {
		return isLock;
	}

	public void setIsLock(Integer isLock) {
		this.isLock = isLock;
	}

	public Integer getIsDcs() {
		return isDcs;
	}

	public void setIsDcs(Integer isDcs) {
		this.isDcs = isDcs;
	}

	public Long getUser_id()
	{
		return user_id;
	}

	public void setUser_id(Long user_id)
	{
		this.user_id = user_id;
	}

}