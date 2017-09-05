/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-01-15 10:12:23
* CreateBy   : xwj
* Comment    : generate by com.sgm.po.POGen
*/

package com.infoservice.dms.chana.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TaskPlanPO extends PO{

	private String planEnd;
	private Long planExecuteTimes;
	private String p1value;
	private Date planLastExecuteTime;
	private String p3value;
	private String p2value;
	private Integer planRunType;
	private Integer taskInterval;
	private String p4value;
	private Integer planIgnoreFlag;
	private Integer planStartTimeout;
	private Integer planStatus;
	private Integer planPriority;
	private String planRepeatType;
	private String planPattern;
	private String planStart;
	private Long planId;
	private Long taskId;
	private String p5value;

	public void setPlanEnd(String planEnd){
		this.planEnd=planEnd;
	}

	public String getPlanEnd(){
		return this.planEnd;
	}

	public void setPlanExecuteTimes(Long planExecuteTimes){
		this.planExecuteTimes=planExecuteTimes;
	}

	public Long getPlanExecuteTimes(){
		return this.planExecuteTimes;
	}

	public void setP1value(String p1value){
		this.p1value=p1value;
	}

	public String getP1value(){
		return this.p1value;
	}

	public void setPlanLastExecuteTime(Date planLastExecuteTime){
		this.planLastExecuteTime=planLastExecuteTime;
	}

	public Date getPlanLastExecuteTime(){
		return this.planLastExecuteTime;
	}

	public void setP3value(String p3value){
		this.p3value=p3value;
	}

	public String getP3value(){
		return this.p3value;
	}

	public void setP2value(String p2value){
		this.p2value=p2value;
	}

	public String getP2value(){
		return this.p2value;
	}

	public void setPlanRunType(Integer planRunType){
		this.planRunType=planRunType;
	}

	public Integer getPlanRunType(){
		return this.planRunType;
	}

	public void setTaskInterval(Integer taskInterval){
		this.taskInterval=taskInterval;
	}

	public Integer getTaskInterval(){
		return this.taskInterval;
	}

	public void setP4value(String p4value){
		this.p4value=p4value;
	}

	public String getP4value(){
		return this.p4value;
	}

	public void setPlanIgnoreFlag(Integer planIgnoreFlag){
		this.planIgnoreFlag=planIgnoreFlag;
	}

	public Integer getPlanIgnoreFlag(){
		return this.planIgnoreFlag;
	}

	public void setPlanStartTimeout(Integer planStartTimeout){
		this.planStartTimeout=planStartTimeout;
	}

	public Integer getPlanStartTimeout(){
		return this.planStartTimeout;
	}

	public void setPlanStatus(Integer planStatus){
		this.planStatus=planStatus;
	}

	public Integer getPlanStatus(){
		return this.planStatus;
	}

	public void setPlanPriority(Integer planPriority){
		this.planPriority=planPriority;
	}

	public Integer getPlanPriority(){
		return this.planPriority;
	}

	public void setPlanRepeatType(String planRepeatType){
		this.planRepeatType=planRepeatType;
	}

	public String getPlanRepeatType(){
		return this.planRepeatType;
	}

	public void setPlanPattern(String planPattern){
		this.planPattern=planPattern;
	}

	public String getPlanPattern(){
		return this.planPattern;
	}

	public void setPlanStart(String planStart){
		this.planStart=planStart;
	}

	public String getPlanStart(){
		return this.planStart;
	}

	public void setPlanId(Long planId){
		this.planId=planId;
	}

	public Long getPlanId(){
		return this.planId;
	}

	public void setTaskId(Long taskId){
		this.taskId=taskId;
	}

	public Long getTaskId(){
		return this.taskId;
	}

	public void setP5value(String p5value){
		this.p5value=p5value;
	}

	public String getP5value(){
		return this.p5value;
	}

}