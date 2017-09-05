/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-03-21 15:58:51
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmDealerUserInfoRecordPO extends PO{

	private String newHotLine;
	private String oldPose;
	private String newPhone;
	private Long historyId;
	private Long changeUser;
	private String newPose;
	private String oldName;
	private Integer oldStatus;
	private Date changeDate;
	private String oldPhone;
	private Long id;
	private String oldHotLine;
	private Integer newStatus;
	private String newName;
	private Long dealerId;

	public Long getDealerId() {
		return dealerId;
	}

	public void setDealerId(Long dealerId) {
		this.dealerId = dealerId;
	}

	public void setNewHotLine(String newHotLine){
		this.newHotLine=newHotLine;
	}

	public String getNewHotLine(){
		return this.newHotLine;
	}

	public void setOldPose(String oldPose){
		this.oldPose=oldPose;
	}

	public String getOldPose(){
		return this.oldPose;
	}

	public void setNewPhone(String newPhone){
		this.newPhone=newPhone;
	}

	public String getNewPhone(){
		return this.newPhone;
	}

	public void setHistoryId(Long historyId){
		this.historyId=historyId;
	}

	public Long getHistoryId(){
		return this.historyId;
	}

	public void setChangeUser(Long changeUser){
		this.changeUser=changeUser;
	}

	public Long getChangeUser(){
		return this.changeUser;
	}

	public void setNewPose(String newPose){
		this.newPose=newPose;
	}

	public String getNewPose(){
		return this.newPose;
	}

	public void setOldName(String oldName){
		this.oldName=oldName;
	}

	public String getOldName(){
		return this.oldName;
	}

	public void setOldStatus(Integer oldStatus){
		this.oldStatus=oldStatus;
	}

	public Integer getOldStatus(){
		return this.oldStatus;
	}

	public void setChangeDate(Date changeDate){
		this.changeDate=changeDate;
	}

	public Date getChangeDate(){
		return this.changeDate;
	}

	public void setOldPhone(String oldPhone){
		this.oldPhone=oldPhone;
	}

	public String getOldPhone(){
		return this.oldPhone;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setOldHotLine(String oldHotLine){
		this.oldHotLine=oldHotLine;
	}

	public String getOldHotLine(){
		return this.oldHotLine;
	}

	public void setNewStatus(Integer newStatus){
		this.newStatus=newStatus;
	}

	public Integer getNewStatus(){
		return this.newStatus;
	}

	public void setNewName(String newName){
		this.newName=newName;
	}

	public String getNewName(){
		return this.newName;
	}

}