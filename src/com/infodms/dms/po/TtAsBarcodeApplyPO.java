/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-05-01 15:38:48
* CreateBy   : -Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsBarcodeApplyPO extends PO{

	private String applyRemark;
	private Long reportNumber;
	private String vin;
	private Date distributionTime;
	private Double auditAcount;
	private Date applyDate;
	private Long applyBy;
	private Long printBy;
	private String distributionPeople;
	private Date printDate;
	private String checkPeople;
	private Long id;
	private Long chapterCode;
	private String auditRemark;
	private String applicantPeople;
	private Integer printTimes;
	private String dealerShortname;
	private Long auditBy;
	private Date checkTime;
	private String reviewRemark;
	private String dealerName;
	private String expressMail;
	private Date auditDate;
	private Date reportDate;
	private Integer applyStatus;
	private String signName;
	private Date signDate;
	private Integer signStatus;
	
	public String getSignName() {
		return signName;
	}

	public void setSignName(String signName) {
		this.signName = signName;
	}

	public Date getSignDate() {
		return signDate;
	}

	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}

	public Integer getSignStatus() {
		return signStatus;
	}

	public void setSignStatus(Integer signStatus) {
		this.signStatus = signStatus;
	}

	public void setApplyRemark(String applyRemark){
		this.applyRemark=applyRemark;
	}

	public String getApplyRemark(){
		return this.applyRemark;
	}

	public void setReportNumber(Long reportNumber){
		this.reportNumber=reportNumber;
	}

	public Long getReportNumber(){
		return this.reportNumber;
	}

	public void setVin(String vin){
		this.vin=vin;
	}

	public String getVin(){
		return this.vin;
	}

	public void setDistributionTime(Date distributionTime){
		this.distributionTime=distributionTime;
	}

	public Date getDistributionTime(){
		return this.distributionTime;
	}

	public void setAuditAcount(Double auditAcount){
		this.auditAcount=auditAcount;
	}

	public Double getAuditAcount(){
		return this.auditAcount;
	}

	public void setApplyDate(Date applyDate){
		this.applyDate=applyDate;
	}

	public Date getApplyDate(){
		return this.applyDate;
	}

	public void setApplyBy(Long applyBy){
		this.applyBy=applyBy;
	}

	public Long getApplyBy(){
		return this.applyBy;
	}

	public void setPrintBy(Long printBy){
		this.printBy=printBy;
	}

	public Long getPrintBy(){
		return this.printBy;
	}

	public void setDistributionPeople(String distributionPeople){
		this.distributionPeople=distributionPeople;
	}

	public String getDistributionPeople(){
		return this.distributionPeople;
	}

	public void setPrintDate(Date printDate){
		this.printDate=printDate;
	}

	public Date getPrintDate(){
		return this.printDate;
	}

	public void setCheckPeople(String checkPeople){
		this.checkPeople=checkPeople;
	}

	public String getCheckPeople(){
		return this.checkPeople;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setChapterCode(Long chapterCode){
		this.chapterCode=chapterCode;
	}

	public Long getChapterCode(){
		return this.chapterCode;
	}

	public void setAuditRemark(String auditRemark){
		this.auditRemark=auditRemark;
	}

	public String getAuditRemark(){
		return this.auditRemark;
	}

	public void setApplicantPeople(String applicantPeople){
		this.applicantPeople=applicantPeople;
	}

	public String getApplicantPeople(){
		return this.applicantPeople;
	}

	public void setPrintTimes(Integer printTimes){
		this.printTimes=printTimes;
	}

	public Integer getPrintTimes(){
		return this.printTimes;
	}

	public void setDealerShortname(String dealerShortname){
		this.dealerShortname=dealerShortname;
	}

	public String getDealerShortname(){
		return this.dealerShortname;
	}

	public void setAuditBy(Long auditBy){
		this.auditBy=auditBy;
	}

	public Long getAuditBy(){
		return this.auditBy;
	}

	public void setCheckTime(Date checkTime){
		this.checkTime=checkTime;
	}

	public Date getCheckTime(){
		return this.checkTime;
	}

	public void setReviewRemark(String reviewRemark){
		this.reviewRemark=reviewRemark;
	}

	public String getReviewRemark(){
		return this.reviewRemark;
	}

	public void setDealerName(String dealerName){
		this.dealerName=dealerName;
	}

	public String getDealerName(){
		return this.dealerName;
	}

	public void setAuditDate(Date auditDate){
		this.auditDate=auditDate;
	}

	public Date getAuditDate(){
		return this.auditDate;
	}

	public void setReportDate(Date reportDate){
		this.reportDate=reportDate;
	}

	public Date getReportDate(){
		return this.reportDate;
	}

	public void setApplyStatus(Integer applyStatus){
		this.applyStatus=applyStatus;
	}

	public Integer getApplyStatus(){
		return this.applyStatus;
	}

	public void setExpressMail(String expressMail) {
		this.expressMail = expressMail;
	}

	public String getExpressMail() {
		return expressMail;
	}

}