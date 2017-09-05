/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-08-25 14:51:29
* CreateBy   : gouwentan
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtCampaignPoseChkRPO extends PO{

	private Integer checkStatus;
	private Long updateBy;
	private Date updateDate;
	private Long createBy;
	private Integer queryStatus;
	private Long poseId;
	private Date createDate;
	private Integer isFleet ;
	private Integer isFleetFlow ; 
	private Integer isProtocolFlow ;
	private Integer dlrLowStep ;
	private Integer areaLowStep ;

	public Integer getIsFleetFlow() {
		return isFleetFlow;
	}

	public void setIsFleetFlow(Integer isFleetFlow) {
		this.isFleetFlow = isFleetFlow;
	}

	public Integer getIsFleet() {
		return isFleet;
	}

	public void setIsFleet(Integer isFleet) {
		this.isFleet = isFleet;
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

	public void setQueryStatus(Integer queryStatus){
		this.queryStatus=queryStatus;
	}

	public Integer getQueryStatus(){
		return this.queryStatus;
	}

	public void setPoseId(Long poseId){
		this.poseId=poseId;
	}

	public Long getPoseId(){
		return this.poseId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public Integer getDlrLowStep() {
		return dlrLowStep;
	}

	public void setDlrLowStep(Integer dlrLowStep) {
		this.dlrLowStep = dlrLowStep;
	}

	public Integer getAreaLowStep() {
		return areaLowStep;
	}

	public void setAreaLowStep(Integer areaLowStep) {
		this.areaLowStep = areaLowStep;
	}

	public Integer getIsProtocolFlow() {
		return isProtocolFlow;
	}

	public void setIsProtocolFlow(Integer isAllProtocol) {
		this.isProtocolFlow = isAllProtocol;
	}

}