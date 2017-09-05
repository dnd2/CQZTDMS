/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2009-11-03 16:36:02
* CreateBy   : xianchao zhang
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TcTaskInfoPO extends PO{

	private Integer taskPriority;
	private Date updateDate;
	private String createBy;
	private String actionDesc;
	private Date createDate;
	private String p1name;
	private String taskType;
	private String p5name;
	private String actionName;
	private Integer taskDuration;
	private String updateBy;
	private String p3name;
	private String taskManual;
	private String p2name;
	private String taskId;
	private String actionId;
	private String p4name;
	private Integer taskStatus;

	public void setTaskPriority(Integer taskPriority){
		this.taskPriority=taskPriority;
	}

	public Integer getTaskPriority(){
		return this.taskPriority;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setCreateBy(String createBy){
		this.createBy=createBy;
	}

	public String getCreateBy(){
		return this.createBy;
	}

	public void setActionDesc(String actionDesc){
		this.actionDesc=actionDesc;
	}

	public String getActionDesc(){
		return this.actionDesc;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setP1name(String p1name){
		this.p1name=p1name;
	}

	public String getP1name(){
		return this.p1name;
	}

	public void setTaskType(String taskType){
		this.taskType=taskType;
	}

	public String getTaskType(){
		return this.taskType;
	}

	public void setP5name(String p5name){
		this.p5name=p5name;
	}

	public String getP5name(){
		return this.p5name;
	}

	public void setActionName(String actionName){
		this.actionName=actionName;
	}

	public String getActionName(){
		return this.actionName;
	}

	public void setTaskDuration(Integer taskDuration){
		this.taskDuration=taskDuration;
	}

	public Integer getTaskDuration(){
		return this.taskDuration;
	}

	public void setUpdateBy(String updateBy){
		this.updateBy=updateBy;
	}

	public String getUpdateBy(){
		return this.updateBy;
	}

	public void setP3name(String p3name){
		this.p3name=p3name;
	}

	public String getP3name(){
		return this.p3name;
	}

	public void setTaskManual(String taskManual){
		this.taskManual=taskManual;
	}

	public String getTaskManual(){
		return this.taskManual;
	}

	public void setP2name(String p2name){
		this.p2name=p2name;
	}

	public String getP2name(){
		return this.p2name;
	}

	public void setTaskId(String taskId){
		this.taskId=taskId;
	}

	public String getTaskId(){
		return this.taskId;
	}

	public void setActionId(String actionId){
		this.actionId=actionId;
	}

	public String getActionId(){
		return this.actionId;
	}

	public void setP4name(String p4name){
		this.p4name=p4name;
	}

	public String getP4name(){
		return this.p4name;
	}

	public void setTaskStatus(Integer taskStatus){
		this.taskStatus=taskStatus;
	}

	public Integer getTaskStatus(){
		return this.taskStatus;
	}

}