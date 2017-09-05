/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-01-22 14:32:24
* CreateBy   : wswcx
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmPlanDetailPO extends PO{

	private Long updateBy;
	private Long planId;
	private Date updateDate;
	private Long planNum;
	private Long planDetailId;
	private Long createBy;
	private String totalOrderNo;
	private Long detailId;
	private Date createDate;
	private Long maiId;
	private Long orgId;
	private Long inNum;
	
	private String remark;
	private Long checkStatus;
	private Long isFleet;

	public Long getIsFleet() {
		return isFleet;
	}

	public void setIsFleet(Long isFleet) {
		this.isFleet = isFleet;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(Long checkStatus) {
		this.checkStatus = checkStatus;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setPlanId(Long planId){
		this.planId=planId;
	}

	public Long getPlanId(){
		return this.planId;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setPlanNum(Long planNum){
		this.planNum=planNum;
	}

	public Long getPlanNum(){
		return this.planNum;
	}

	public void setPlanDetailId(Long planDetailId){
		this.planDetailId=planDetailId;
	}

	public Long getPlanDetailId(){
		return this.planDetailId;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setTotalOrderNo(String totalOrderNo){
		this.totalOrderNo=totalOrderNo;
	}

	public String getTotalOrderNo(){
		return this.totalOrderNo;
	}

	public void setDetailId(Long detailId){
		this.detailId=detailId;
	}

	public Long getDetailId(){
		return this.detailId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setMaiId(Long maiId){
		this.maiId=maiId;
	}

	public Long getMaiId(){
		return this.maiId;
	}

	public void setOrgId(Long orgId){
		this.orgId=orgId;
	}

	public Long getOrgId(){
		return this.orgId;
	}

	public void setInNum(Long inNum){
		this.inNum=inNum;
	}

	public Long getInNum(){
		return this.inNum;
	}

}