/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-07-01 17:03:00
* CreateBy   : 
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtCamCampaignCostPO extends PO{

	private Integer itemCount;
	private Double realCost;
	private Double itemPrice;
	private Date updateDate;
	private Integer costType;
	private Double itemCost;
	private Long createBy;
	private Double planCost;
	private Long costAccount;
	private String itemRemark;
	private Long executeId;
	private Long campaignId;
	private Long updateBy;
	private Long costId;
	private String itemName;
	private Date createDate;
	private Integer activityType;
	private String activityContent;
	private Long spaceCostId ;
	private Date startDate;
	private Date endDate ;
	private Long region ;
	
	public Long getRegion() {
		return region;
	}

	public void setRegion(Long region) {
		this.region = region;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Long getSpaceCostId() {
		return spaceCostId;
	}

	public void setSpaceCostId(Long spaceCostId) {
		this.spaceCostId = spaceCostId;
	}
	public Integer getActivityType() {
		return activityType;
	}

	public void setActivityType(Integer activityType) {
		this.activityType = activityType;
	}

	public String getActivityContent() {
		return activityContent;
	}

	public void setActivityContent(String activityContent) {
		this.activityContent = activityContent;
	}

	public void setItemCount(Integer itemCount){
		this.itemCount=itemCount;
	}

	public Integer getItemCount(){
		return this.itemCount;
	}

	public void setRealCost(Double realCost){
		this.realCost=realCost;
	}

	public Double getRealCost(){
		return this.realCost;
	}

	public void setItemPrice(Double itemPrice){
		this.itemPrice=itemPrice;
	}

	public Double getItemPrice(){
		return this.itemPrice;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setCostType(Integer costType){
		this.costType=costType;
	}

	public Integer getCostType(){
		return this.costType;
	}

	public void setItemCost(Double itemCost){
		this.itemCost=itemCost;
	}

	public Double getItemCost(){
		return this.itemCost;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setPlanCost(Double planCost){
		this.planCost=planCost;
	}

	public Double getPlanCost(){
		return this.planCost;
	}

	public void setCostAccount(Long costAccount){
		this.costAccount=costAccount;
	}

	public Long getCostAccount(){
		return this.costAccount;
	}

	public void setItemRemark(String itemRemark){
		this.itemRemark=itemRemark;
	}

	public String getItemRemark(){
		return this.itemRemark;
	}

	public void setExecuteId(Long executeId){
		this.executeId=executeId;
	}

	public Long getExecuteId(){
		return this.executeId;
	}

	public void setCampaignId(Long campaignId){
		this.campaignId=campaignId;
	}

	public Long getCampaignId(){
		return this.campaignId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setCostId(Long costId){
		this.costId=costId;
	}

	public Long getCostId(){
		return this.costId;
	}

	public void setItemName(String itemName){
		this.itemName=itemName;
	}

	public String getItemName(){
		return this.itemName;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

}