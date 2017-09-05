/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2017-08-23 11:00:09
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartDisplacementRecordPO extends PO{

	private String locCode;
	private Integer state;
	private String remark;
	private Long createBy;
	private Long ableDisQty;
	private Long whId;
	private Long newLocId;
	private String newLocCode;
	private Long orgId;
	private Long recordId;
	private Long partId;
	private Long locId;
	private Date createDate;
	private Long disQty;
	private String batchNo;
	private Long inId;

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Long getInId() {
		return inId;
	}

	public void setInId(Long inId) {
		this.inId = inId;
	}

	public void setLocCode(String locCode){
		this.locCode=locCode;
	}

	public String getLocCode(){
		return this.locCode;
	}

	public void setState(Integer state){
		this.state=state;
	}

	public Integer getState(){
		return this.state;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setAbleDisQty(Long ableDisQty){
		this.ableDisQty=ableDisQty;
	}

	public Long getAbleDisQty(){
		return this.ableDisQty;
	}

	public void setWhId(Long whId){
		this.whId=whId;
	}

	public Long getWhId(){
		return this.whId;
	}

	public void setNewLocId(Long newLocId){
		this.newLocId=newLocId;
	}

	public Long getNewLocId(){
		return this.newLocId;
	}

	public void setNewLocCode(String newLocCode){
		this.newLocCode=newLocCode;
	}

	public String getNewLocCode(){
		return this.newLocCode;
	}

	public void setOrgId(Long orgId){
		this.orgId=orgId;
	}

	public Long getOrgId(){
		return this.orgId;
	}

	public void setRecordId(Long recordId){
		this.recordId=recordId;
	}

	public Long getRecordId(){
		return this.recordId;
	}

	public void setPartId(Long partId){
		this.partId=partId;
	}

	public Long getPartId(){
		return this.partId;
	}

	public void setLocId(Long locId){
		this.locId=locId;
	}

	public Long getLocId(){
		return this.locId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setDisQty(Long disQty){
		this.disQty=disQty;
	}

	public Long getDisQty(){
		return this.disQty;
	}

}