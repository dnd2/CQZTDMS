/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-09-13 09:59:53
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtCampaignSpaceExecutePO extends PO{

	private Integer campaignModel;
	private String adviceDesc;
	private Date endDate;
	private Date updateDate;
	private Long createBy;
	private Long oemCompanyId;
	private String campaignNo;
	private Integer checkStatus;
	private Long updateBy;
	private String campaignNeed;
	//private String execAddDesc;
	private String campaignSubject;
	private String campaignObject;
	private Long groupId;
	private Integer campaignType;
	private String campaignName;
	private String evaluateDesc;
	private String campaignPurpose;
	private Long orgId;
	private Date startDate;
	private Long spaceId;
	private Long campaignId;
	private String executeDesc;
	private Date submitsDate ;
	private Long isPrint ;
	private Long printBy ;
	private Date printDate ;
	private String remark ;
	private Long priceType ;
	private Long isAllProtocol ;
	private String assignType;
	
	public Long getIsAllProtocol() {
		return isAllProtocol;
	}

	public void setIsAllProtocol(Long isAllProtocol) {
		this.isAllProtocol = isAllProtocol;
	}

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

	public Date getSubmitsDate() {
		return submitsDate;
	}

	public void setSubmitsDate(Date submitsDate) {
		this.submitsDate = submitsDate;
	}

	public String getExecuteDesc() {
		return executeDesc;
	}

	public void setExecuteDesc(String executeDesc) {
		this.executeDesc = executeDesc;
	}

	public String getActiviceSummaryDesc() {
		return activiceSummaryDesc;
	}

	public void setActiviceSummaryDesc(String activiceSummaryDesc) {
		this.activiceSummaryDesc = activiceSummaryDesc;
	}

	public String getExecAddDesc() {
		return execAddDesc;
	}

	public void setExecAddDesc(String execAddDesc) {
		this.execAddDesc = execAddDesc;
	}

	public Long getIsFleet() {
		return isFleet;
	}

	public void setIsFleet(Long isFleet) {
		this.isFleet = isFleet;
	}

	private String campaignDesc;
	private Date createDate;
	private String spaceInfo;
	private String activiceSummaryDesc;
	private String execAddDesc;
	private Long isFleet ;

	public void setCampaignModel(Integer campaignModel){
		this.campaignModel=campaignModel;
	}

	public Integer getCampaignModel(){
		return this.campaignModel;
	}

	public void setAdviceDesc(String adviceDesc){
		this.adviceDesc=adviceDesc;
	}

	public String getAdviceDesc(){
		return this.adviceDesc;
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

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setOemCompanyId(Long oemCompanyId){
		this.oemCompanyId=oemCompanyId;
	}

	public Long getOemCompanyId(){
		return this.oemCompanyId;
	}

	public void setCampaignNo(String campaignNo){
		this.campaignNo=campaignNo;
	}

	public String getCampaignNo(){
		return this.campaignNo;
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

	public void setCampaignNeed(String campaignNeed){
		this.campaignNeed=campaignNeed;
	}

	public String getCampaignNeed(){
		return this.campaignNeed;
	}

//	public void setExecAddDesc(String execAddDesc){
//		this.execAddDesc=execAddDesc;
//	}
//
//	public String getExecAddDesc(){
//		return this.execAddDesc;
//	}

	public void setCampaignSubject(String campaignSubject){
		this.campaignSubject=campaignSubject;
	}

	public String getCampaignSubject(){
		return this.campaignSubject;
	}

	public void setCampaignObject(String campaignObject){
		this.campaignObject=campaignObject;
	}

	public String getCampaignObject(){
		return this.campaignObject;
	}

	public void setGroupId(Long groupId){
		this.groupId=groupId;
	}

	public Long getGroupId(){
		return this.groupId;
	}

	public void setCampaignType(Integer campaignType){
		this.campaignType=campaignType;
	}

	public Integer getCampaignType(){
		return this.campaignType;
	}

	public void setCampaignName(String campaignName){
		this.campaignName=campaignName;
	}

	public String getCampaignName(){
		return this.campaignName;
	}

	public void setEvaluateDesc(String evaluateDesc){
		this.evaluateDesc=evaluateDesc;
	}

	public String getEvaluateDesc(){
		return this.evaluateDesc;
	}

	public void setCampaignPurpose(String campaignPurpose){
		this.campaignPurpose=campaignPurpose;
	}

	public String getCampaignPurpose(){
		return this.campaignPurpose;
	}

	public void setOrgId(Long orgId){
		this.orgId=orgId;
	}

	public Long getOrgId(){
		return this.orgId;
	}

	public void setStartDate(Date startDate){
		this.startDate=startDate;
	}

	public Date getStartDate(){
		return this.startDate;
	}

	public void setSpaceId(Long spaceId){
		this.spaceId=spaceId;
	}

	public Long getSpaceId(){
		return this.spaceId;
	}

	public void setCampaignId(Long campaignId){
		this.campaignId=campaignId;
	}

	public Long getCampaignId(){
		return this.campaignId;
	}

	public void setCampaignDesc(String campaignDesc){
		this.campaignDesc=campaignDesc;
	}

	public String getCampaignDesc(){
		return this.campaignDesc;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setSpaceInfo(String spaceInfo){
		this.spaceInfo=spaceInfo;
	}

	public String getSpaceInfo(){
		return this.spaceInfo;
	}

	public String getAssignType()
	{
		return assignType;
	}

	public void setAssignType(String assignType)
	{
		this.assignType = assignType;
	}

}