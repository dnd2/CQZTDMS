/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-09-11 16:37:12
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrNewsPO extends PO{

	private Date newsDate;
	private String orgName;
	private String contents;
	private Date updateDate;
	private Long createBy;
	private Integer status;
	private Long orgId;
	private Long oemCompanyId;
	private Long updateBy;
	private String newsTitle;
	private Integer newsType;
	private Long newsId;
	private Date createDate;
	private String newsCode;
	private String voicePerson;
	private Integer dutyType;
	private String orgTypeId;
	private	String msgType;
	private	String isPrivate; //区分 大区消息 是否个人所有 YH 2010.12.24	
	private Integer archiveFlag;
	private Date expiryDate;
	
	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Integer getArchiveFlag() {
		return archiveFlag;
	}

	public void setArchiveFlag(Integer archiveFlag) {
		this.archiveFlag = archiveFlag;
	}

	public String getOrgTypeId() {
		return orgTypeId;
	}

	public void setOrgTypeId(String orgTypeId) {
		this.orgTypeId = orgTypeId;
	}

	public Integer getDutyType() {
		return dutyType;
	}

	public void setDutyType(Integer dutyType) {
		this.dutyType = dutyType;
	}

	public String getVoicePerson() {
		return voicePerson;
	}

	public void setVoicePerson(String voicePerson) {
		this.voicePerson = voicePerson;
	}

	public void setNewsDate(Date newsDate){
		this.newsDate=newsDate;
	}

	public Date getNewsDate(){
		return this.newsDate;
	}

	public void setOrgName(String orgName){
		this.orgName=orgName;
	}

	public String getOrgName(){
		return this.orgName;
	}

	public void setContents(String contents){
		this.contents=contents;
	}

	public String getContents(){
		return this.contents;
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

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setOrgId(Long orgId){
		this.orgId=orgId;
	}

	public Long getOrgId(){
		return this.orgId;
	}

	public void setOemCompanyId(Long oemCompanyId){
		this.oemCompanyId=oemCompanyId;
	}

	public Long getOemCompanyId(){
		return this.oemCompanyId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setNewsTitle(String newsTitle){
		this.newsTitle=newsTitle;
	}

	public String getNewsTitle(){
		return this.newsTitle;
	}

	public void setNewsType(Integer newsType){
		this.newsType=newsType;
	}

	public Integer getNewsType(){
		return this.newsType;
	}

	public void setNewsId(Long newsId){
		this.newsId=newsId;
	}

	public Long getNewsId(){
		return this.newsId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setNewsCode(String newsCode){
		this.newsCode=newsCode;
	}

	public String getNewsCode(){
		return this.newsCode;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getIsPrivate() {
		return isPrivate;
	}

	public void setIsPrivate(String isPrivate) {
		this.isPrivate = isPrivate;
	}

	

}