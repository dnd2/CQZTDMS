/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-07-14 11:02:53
* CreateBy   : 
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtCampaignPO extends PO{
	
	private String campaignSubject;
	private String campaignObject;
	private Long groupId;
	private Long campaignModel;
	private Integer campaignType;
	private Date updateDate;
	private Date endDate;
	private String campaignName;
	private Long createBy;
	private String campaignPurpose;
	private Date startDate;
	private Long oemCompanyId;
	private String campaignNo;
	private Long campaignId;
	private Long updateBy;
	private String campaignNeed;
	private Date createDate;
	private String campaignDesc;
	private Long areaId;
	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

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

	public void setCampaignModel(Long campaignModel){
		this.campaignModel=campaignModel;
	}

	public Long getCampaignModel(){
		return this.campaignModel;
	}

	public void setCampaignType(Integer campaignType){
		this.campaignType=campaignType;
	}

	public Integer getCampaignType(){
		return this.campaignType;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setEndDate(Date endDate){
		this.endDate=endDate;
	}

	public Date getEndDate(){
		return this.endDate;
	}

	public void setCampaignName(String campaignName){
		this.campaignName=campaignName;
	}

	public String getCampaignName(){
		return this.campaignName;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setCampaignPurpose(String campaignPurpose){
		this.campaignPurpose=campaignPurpose;
	}

	public String getCampaignPurpose(){
		return this.campaignPurpose;
	}

	public void setStartDate(Date startDate){
		this.startDate=startDate;
	}

	public Date getStartDate(){
		return this.startDate;
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

	public void setCampaignNeed(String campaignNeed){
		this.campaignNeed=campaignNeed;
	}

	public String getCampaignNeed(){
		return this.campaignNeed;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setCampaignDesc(String campaignDesc){
		this.campaignDesc=campaignDesc;
	}

	public String getCampaignDesc(){
		return this.campaignDesc;
	}

}