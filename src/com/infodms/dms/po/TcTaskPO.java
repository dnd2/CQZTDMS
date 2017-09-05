/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2009-11-03 16:36:03
* CreateBy   : xianchao zhang
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TcTaskPO extends PO{

	private String p1value;
	private Integer startTimeout;
	private String executeMsg;
	private String errorStack;
	private String p3value;
	private String p2value;
	private String p4value;
	private Date realStartTime;
	private Date realEndTime;
	private Long logId;
	private String errorCode;
	private Date planStartTime;
	private Integer marker;
	private Long planId;
	private String executeType;
	private Integer ignoreFlag;
	private Long taskId;
	private String p5value;
	private Integer runType;
	private String errorMsg;
	private Integer executeResult;

	public void setP1value(String p1value){
		this.p1value=p1value;
	}

	public String getP1value(){
		return this.p1value;
	}

	public void setStartTimeout(Integer startTimeout){
		this.startTimeout=startTimeout;
	}

	public Integer getStartTimeout(){
		return this.startTimeout;
	}

	public void setExecuteMsg(String executeMsg){
		this.executeMsg=executeMsg;
	}

	public String getExecuteMsg(){
		return this.executeMsg;
	}

	public void setErrorStack(String errorStack){
		this.errorStack=errorStack;
	}

	public String getErrorStack(){
		return this.errorStack;
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

	public void setP4value(String p4value){
		this.p4value=p4value;
	}

	public String getP4value(){
		return this.p4value;
	}

	public void setRealStartTime(Date realStartTime){
		this.realStartTime=realStartTime;
	}

	public Date getRealStartTime(){
		return this.realStartTime;
	}

	public void setRealEndTime(Date realEndTime){
		this.realEndTime=realEndTime;
	}

	public Date getRealEndTime(){
		return this.realEndTime;
	}

	public void setLogId(Long logId){
		this.logId=logId;
	}

	public Long getLogId(){
		return this.logId;
	}

	public void setErrorCode(String errorCode){
		this.errorCode=errorCode;
	}

	public String getErrorCode(){
		return this.errorCode;
	}

	public void setPlanStartTime(Date planStartTime){
		this.planStartTime=planStartTime;
	}

	public Date getPlanStartTime(){
		return this.planStartTime;
	}

	public void setMarker(Integer marker){
		this.marker=marker;
	}

	public Integer getMarker(){
		return this.marker;
	}

	public void setPlanId(Long planId){
		this.planId=planId;
	}

	public Long getPlanId(){
		return this.planId;
	}

	public void setExecuteType(String executeType){
		this.executeType=executeType;
	}

	public String getExecuteType(){
		return this.executeType;
	}

	public void setIgnoreFlag(Integer ignoreFlag){
		this.ignoreFlag=ignoreFlag;
	}

	public Integer getIgnoreFlag(){
		return this.ignoreFlag;
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

	public void setRunType(Integer runType){
		this.runType=runType;
	}

	public Integer getRunType(){
		return this.runType;
	}

	public void setErrorMsg(String errorMsg){
		this.errorMsg=errorMsg;
	}

	public String getErrorMsg(){
		return this.errorMsg;
	}

	public void setExecuteResult(Integer executeResult){
		this.executeResult=executeResult;
	}

	public Integer getExecuteResult(){
		return this.executeResult;
	}

}