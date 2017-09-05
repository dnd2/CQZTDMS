/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-09-13 11:20:34
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtCampaignExecutePO extends PO{

	private Long dealerId;
	private String adviceDesc;
	private Date updateDate;
	private String executeDesc;
	private String evaluateDesc;
	private Long createBy;
	private Long executeId;
	private Long orgId;
	private String activiceSummaryDesc;
	private Integer checkStatus;
	private Long updateBy;
	private Long campaignId;
	private String execAddDesc;
	private Date createDate;
	private Long isFleet ;
	private Date submitsDate ;
	private Long isPrint ;
	private Long printBy ;
	private String remark ;
	private Long priceType ;
	
	public Long getPriceType() {
		return priceType;
	}

	public void setPriceType(Long priceType) {
		this.priceType = priceType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getIsPrint() {
		return isPrint;
	}

	public void setIsPrint(Long isPrint) {
		this.isPrint = isPrint;
	}

	public Long getPrintBy() {
		return printBy;
	}

	public void setPrintBy(Long printBy) {
		this.printBy = printBy;
	}

	public Date getPrintDate() {
		return printDate;
	}

	public void setPrintDate(Date printDate) {
		this.printDate = printDate;
	}

	private Date printDate ;

	public Date getSubmitsDate() {
		return submitsDate;
	}

	public void setSubmitsDate(Date submitsDate) {
		this.submitsDate = submitsDate;
	}

	public Long getIsFleet() {
		return isFleet;
	}

	public void setIsFleet(Long isFleet) {
		this.isFleet = isFleet;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setAdviceDesc(String adviceDesc){
		this.adviceDesc=adviceDesc;
	}

	public String getAdviceDesc(){
		return this.adviceDesc;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setExecuteDesc(String executeDesc){
		this.executeDesc=executeDesc;
	}

	public String getExecuteDesc(){
		return this.executeDesc;
	}

	public void setEvaluateDesc(String evaluateDesc){
		this.evaluateDesc=evaluateDesc;
	}

	public String getEvaluateDesc(){
		return this.evaluateDesc;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setExecuteId(Long executeId){
		this.executeId=executeId;
	}

	public Long getExecuteId(){
		return this.executeId;
	}

	public void setOrgId(Long orgId){
		this.orgId=orgId;
	}

	public Long getOrgId(){
		return this.orgId;
	}

	public void setActiviceSummaryDesc(String activiceSummaryDesc){
		this.activiceSummaryDesc=activiceSummaryDesc;
	}

	public String getActiviceSummaryDesc(){
		return this.activiceSummaryDesc;
	}

	public void setCheckStatus(Integer checkStatus){
		this.checkStatus=checkStatus;
	}

	public Integer getCheckStatus(){
		return this.checkStatus;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setCampaignId(Long campaignId){
		this.campaignId=campaignId;
	}

	public Long getCampaignId(){
		return this.campaignId;
	}

	public void setExecAddDesc(String execAddDesc){
		this.execAddDesc=execAddDesc;
	}

	public String getExecAddDesc(){
		return this.execAddDesc;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

}