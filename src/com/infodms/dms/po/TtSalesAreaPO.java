/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-04-01 17:49:32
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtSalesAreaPO extends PO{

	private Long type;
	private Long yieldly;
	private Date updateDate;
	private Long inStatus;
	private String dutyPer;
	private String dutyTel;
	private Long outStatus;
	private Long createBy;
	private String areaName;
	private Date createDate;
	private String areaCode;
	private Long status;
	private Long updateBy;
	private Long areaId;
	private String waCode;
	private String remark;

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getWaCode() {
		return waCode;
	}

	public void setWaCode(String waCode) {
		this.waCode = waCode;
	}

	public void setType(Long type){
		this.type=type;
	}

	public Long getType(){
		return this.type;
	}

	public void setYieldly(Long yieldly){
		this.yieldly=yieldly;
	}

	public Long getYieldly(){
		return this.yieldly;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setInStatus(Long inStatus){
		this.inStatus=inStatus;
	}

	public Long getInStatus(){
		return this.inStatus;
	}

	public void setDutyPer(String dutyPer){
		this.dutyPer=dutyPer;
	}

	public String getDutyPer(){
		return this.dutyPer;
	}

	public void setDutyTel(String dutyTel){
		this.dutyTel=dutyTel;
	}

	public String getDutyTel(){
		return this.dutyTel;
	}

	public void setOutStatus(Long outStatus){
		this.outStatus=outStatus;
	}

	public Long getOutStatus(){
		return this.outStatus;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setAreaName(String areaName){
		this.areaName=areaName;
	}

	public String getAreaName(){
		return this.areaName;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setAreaCode(String areaCode){
		this.areaCode=areaCode;
	}

	public String getAreaCode(){
		return this.areaCode;
	}

	public void setStatus(Long status){
		this.status=status;
	}

	public Long getStatus(){
		return this.status;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setAreaId(Long areaId){
		this.areaId=areaId;
	}

	public Long getAreaId(){
		return this.areaId;
	}

}