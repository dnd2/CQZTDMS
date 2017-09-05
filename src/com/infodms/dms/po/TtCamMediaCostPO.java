/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-09-15 10:57:09
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtCamMediaCostPO extends PO{

	private Date advDate;
	private String advMedia;
	private Integer itemCount;
	private Long advModel;
	private Double itemPrice;
	private Double realCost;
	private Date endDate;
	private Date updateDate;
	private Integer costType;
	private Long createBy;
	private Double itemCost;
	private String remark;
	private Double planCost;
	private String advSubject;
	private Long executeId;
	private Integer paymentAccount;
	private Long updateBy;
	private Long costId;
	private Date createDate;
	private Integer mediaType;
	private Long spaceCostId ;
	private Integer region ;
	private Integer mediaModel ;
	private String mediaName ;
	private String mediaColumn ;
	private String mediaPublish ;
	private String mediaSize ;
	private Integer totalCount ;
	
	public Integer getRegion() {
		return region;
	}

	public void setRegion(Integer region) {
		this.region = region;
	}

	public Integer getMediaModel() {
		return mediaModel;
	}

	public void setMediaModel(Integer mediaModel) {
		this.mediaModel = mediaModel;
	}

	public String getMediaName() {
		return mediaName;
	}

	public void setMediaName(String mediaName) {
		this.mediaName = mediaName;
	}

	public String getMediaColumn() {
		return mediaColumn;
	}

	public void setMediaColumn(String mediaColumn) {
		this.mediaColumn = mediaColumn;
	}

	public String getMediaPublish() {
		return mediaPublish;
	}

	public void setMediaPublish(String mediaPublish) {
		this.mediaPublish = mediaPublish;
	}

	public String getMediaSize() {
		return mediaSize;
	}

	public void setMediaSize(String mediaSize) {
		this.mediaSize = mediaSize;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Long getSpaceCostId() {
		return spaceCostId;
	}

	public void setSpaceCostId(Long spaceCostId) {
		this.spaceCostId = spaceCostId;
	}
	public Integer getMediaType() {
		return mediaType;
	}

	public void setMediaType(Integer mediaType) {
		this.mediaType = mediaType;
	}

	public void setAdvDate(Date advDate){
		this.advDate=advDate;
	}

	public Date getAdvDate(){
		return this.advDate;
	}

	public void setAdvMedia(String advMedia){
		this.advMedia=advMedia;
	}

	public String getAdvMedia(){
		return this.advMedia;
	}

	public void setItemCount(Integer itemCount){
		this.itemCount=itemCount;
	}

	public Integer getItemCount(){
		return this.itemCount;
	}

	public void setAdvModel(Long advModel){
		this.advModel=advModel;
	}

	public Long getAdvModel(){
		return this.advModel;
	}

	public void setItemPrice(Double itemPrice){
		this.itemPrice=itemPrice;
	}

	public Double getItemPrice(){
		return this.itemPrice;
	}

	public void setRealCost(Double realCost){
		this.realCost=realCost;
	}

	public Double getRealCost(){
		return this.realCost;
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

	public void setCostType(Integer costType){
		this.costType=costType;
	}

	public Integer getCostType(){
		return this.costType;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setItemCost(Double itemCost){
		this.itemCost=itemCost;
	}

	public Double getItemCost(){
		return this.itemCost;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setPlanCost(Double planCost){
		this.planCost=planCost;
	}

	public Double getPlanCost(){
		return this.planCost;
	}

	public void setAdvSubject(String advSubject){
		this.advSubject=advSubject;
	}

	public String getAdvSubject(){
		return this.advSubject;
	}

	public void setExecuteId(Long executeId){
		this.executeId=executeId;
	}

	public Long getExecuteId(){
		return this.executeId;
	}

	public void setPaymentAccount(Integer paymentAccount){
		this.paymentAccount=paymentAccount;
	}

	public Integer getPaymentAccount(){
		return this.paymentAccount;
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

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

}