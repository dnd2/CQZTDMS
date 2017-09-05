/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-02-04 14:06:42
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.math.BigDecimal;
import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtDailyReportPO extends PO{

	private BigDecimal aLevelOrder;
	private BigDecimal testDriver;
	private BigDecimal hRetain;
	private BigDecimal bRetain;
	private BigDecimal callPassenger;
	private Date createDate;
	private BigDecimal total;
	private BigDecimal bLevelOrder;
	private BigDecimal cRetain;
	private BigDecimal invitePassenger;
	private BigDecimal firstPassenger;
	private BigDecimal cLevelOrder;
	private BigDecimal largerDelivery;
	private BigDecimal lost;
	private BigDecimal secondDelivery;
	private BigDecimal createCard;
	private BigDecimal oLevelOrder;
	private BigDecimal realStock;
	private BigDecimal hLevelOrder;
	private Long dailyReportId;
	private BigDecimal regularRecommend;
	private String carType;
	private BigDecimal aRetain;
	private BigDecimal delivery;
	private Long dlrId;
	private BigDecimal oldCarReplace;
	private BigDecimal uncommitOrder;
	private Long contentId;
	private Integer status;
	private Long createBy;
	private Date updateDate;
	private Long updateBy;
	private Date auditDate;
	private Long auditBy;

	public void setALevelOrder(BigDecimal aLevelOrder){
		this.aLevelOrder=aLevelOrder;
	}

	public BigDecimal getALevelOrder(){
		return this.aLevelOrder;
	}

	public void setTestDriver(BigDecimal testDriver){
		this.testDriver=testDriver;
	}

	public BigDecimal getTestDriver(){
		return this.testDriver;
	}

	public void setHRetain(BigDecimal hRetain){
		this.hRetain=hRetain;
	}

	public BigDecimal getHRetain(){
		return this.hRetain;
	}

	public void setBRetain(BigDecimal bRetain){
		this.bRetain=bRetain;
	}

	public BigDecimal getBRetain(){
		return this.bRetain;
	}

	public void setCallPassenger(BigDecimal callPassenger){
		this.callPassenger=callPassenger;
	}

	public BigDecimal getCallPassenger(){
		return this.callPassenger;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setTotal(BigDecimal total){
		this.total=total;
	}

	public BigDecimal getTotal(){
		return this.total;
	}

	public void setBLevelOrder(BigDecimal bLevelOrder){
		this.bLevelOrder=bLevelOrder;
	}

	public BigDecimal getBLevelOrder(){
		return this.bLevelOrder;
	}

	public void setCRetain(BigDecimal cRetain){
		this.cRetain=cRetain;
	}

	public BigDecimal getCRetain(){
		return this.cRetain;
	}

	public void setInvitePassenger(BigDecimal invitePassenger){
		this.invitePassenger=invitePassenger;
	}

	public BigDecimal getInvitePassenger(){
		return this.invitePassenger;
	}

	public void setFirstPassenger(BigDecimal firstPassenger){
		this.firstPassenger=firstPassenger;
	}

	public BigDecimal getFirstPassenger(){
		return this.firstPassenger;
	}

	public void setCLevelOrder(BigDecimal cLevelOrder){
		this.cLevelOrder=cLevelOrder;
	}

	public BigDecimal getCLevelOrder(){
		return this.cLevelOrder;
	}

	public void setLargerDelivery(BigDecimal largerDelivery){
		this.largerDelivery=largerDelivery;
	}

	public BigDecimal getLargerDelivery(){
		return this.largerDelivery;
	}

	public void setLost(BigDecimal lost){
		this.lost=lost;
	}

	public BigDecimal getLost(){
		return this.lost;
	}

	public void setSecondDelivery(BigDecimal secondDelivery){
		this.secondDelivery=secondDelivery;
	}

	public BigDecimal getSecondDelivery(){
		return this.secondDelivery;
	}

	public void setCreateCard(BigDecimal createCard){
		this.createCard=createCard;
	}

	public BigDecimal getCreateCard(){
		return this.createCard;
	}

	public void setOLevelOrder(BigDecimal oLevelOrder){
		this.oLevelOrder=oLevelOrder;
	}

	public BigDecimal getOLevelOrder(){
		return this.oLevelOrder;
	}

	public void setRealStock(BigDecimal realStock){
		this.realStock=realStock;
	}

	public BigDecimal getRealStock(){
		return this.realStock;
	}

	public void setHLevelOrder(BigDecimal hLevelOrder){
		this.hLevelOrder=hLevelOrder;
	}

	public BigDecimal getHLevelOrder(){
		return this.hLevelOrder;
	}

	public void setDailyReportId(Long dailyReportId){
		this.dailyReportId=dailyReportId;
	}

	public Long getDailyReportId(){
		return this.dailyReportId;
	}

	public void setRegularRecommend(BigDecimal regularRecommend){
		this.regularRecommend=regularRecommend;
	}

	public BigDecimal getRegularRecommend(){
		return this.regularRecommend;
	}



	public String getCarType() {
		return carType;
	}

	public void setCarType(String carType) {
		this.carType = carType;
	}

	public void setARetain(BigDecimal aRetain){
		this.aRetain=aRetain;
	}

	public BigDecimal getARetain(){
		return this.aRetain;
	}

	public void setDelivery(BigDecimal delivery){
		this.delivery=delivery;
	}

	public BigDecimal getDelivery(){
		return this.delivery;
	}

	public void setDlrId(Long dlrId){
		this.dlrId=dlrId;
	}

	public Long getDlrId(){
		return this.dlrId;
	}

	public void setOldCarReplace(BigDecimal oldCarReplace){
		this.oldCarReplace=oldCarReplace;
	}

	public BigDecimal getOldCarReplace(){
		return this.oldCarReplace;
	}

	public void setUncommitOrder(BigDecimal uncommitOrder){
		this.uncommitOrder=uncommitOrder;
	}

	public BigDecimal getUncommitOrder(){
		return this.uncommitOrder;
	}

	public Long getContentId() {
		return contentId;
	}

	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Long getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}

	public Date getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}

	public Long getAuditBy() {
		return auditBy;
	}

	public void setAuditBy(Long auditBy) {
		this.auditBy = auditBy;
	}

	

}