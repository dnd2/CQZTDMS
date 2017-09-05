/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2009-12-29 11:42:12
* CreateBy   : Andy
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmStationInfosChangePO extends PO{

	private Date updateDate;
	private String linkTel;
	private Long stationId;
	private Long createBy;
	private Date createDate;
	private Date auditDate;
	private String work;
	private Integer infosStatus;
	private Long auditUser;
	private Long dealerId;
	private String email;
	private Integer auditStatus;
	private Long updateBy;
	private Integer gender;
	private Long recordId;
	private String name;
	private String personal;
	private String train;
	private String other;

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setLinkTel(String linkTel){
		this.linkTel=linkTel;
	}

	public String getLinkTel(){
		return this.linkTel;
	}

	public void setStationId(Long stationId){
		this.stationId=stationId;
	}

	public Long getStationId(){
		return this.stationId;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setAuditDate(Date auditDate){
		this.auditDate=auditDate;
	}

	public Date getAuditDate(){
		return this.auditDate;
	}

	public void setWork(String work){
		this.work=work;
	}

	public String getWork(){
		return this.work;
	}

	public void setInfosStatus(Integer infosStatus){
		this.infosStatus=infosStatus;
	}

	public Integer getInfosStatus(){
		return this.infosStatus;
	}

	public void setAuditUser(Long auditUser){
		this.auditUser=auditUser;
	}

	public Long getAuditUser(){
		return this.auditUser;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setEmail(String email){
		this.email=email;
	}

	public String getEmail(){
		return this.email;
	}

	public void setAuditStatus(Integer auditStatus){
		this.auditStatus=auditStatus;
	}

	public Integer getAuditStatus(){
		return this.auditStatus;
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

	public void setRecordId(Long recordId){
		this.recordId=recordId;
	}

	public Long getRecordId(){
		return this.recordId;
	}

	public void setName(String name){
		this.name=name;
	}

	public String getName(){
		return this.name;
	}

	public void setPersonal(String personal){
		this.personal=personal;
	}

	public String getPersonal(){
		return this.personal;
	}

	public void setTrain(String train){
		this.train=train;
	}

	public String getTrain(){
		return this.train;
	}

	public void setOther(String other){
		this.other=other;
	}

	public String getOther(){
		return this.other;
	}

}