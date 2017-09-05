/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-11-26 15:29:26
* CreateBy   : wizard_lee
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsRoOldRepairPartPO extends PO{

	private Date rollDate;
	private String partName;
	private Long partId;
	private Integer rollStatus;
	private Long createBy;
	private Long roId;
	private Long detailId;
	private Date createDate;
	private String partCode;
	private Long rollBy;
	private Long repairtypecode;
	private Long payType;
	private Long repairPartId;

	public Long getRepairPartId() {
		return repairPartId;
	}

	public void setRepairPartId(Long repairPartId) {
		this.repairPartId = repairPartId;
	}

	public Long getPayType() {
		return payType;
	}

	public void setPayType(Long payType) {
		this.payType = payType;
	}

	public Long getRepairtypecode() {
		return repairtypecode;
	}

	public void setRepairtypecode(Long repairtypecode) {
		this.repairtypecode = repairtypecode;
	}

	public void setRollDate(Date rollDate){
		this.rollDate=rollDate;
	}

	public Date getRollDate(){
		return this.rollDate;
	}

	public void setPartName(String partName){
		this.partName=partName;
	}

	public String getPartName(){
		return this.partName;
	}

	public void setPartId(Long partId){
		this.partId=partId;
	}

	public Long getPartId(){
		return this.partId;
	}

	public void setRollStatus(Integer rollStatus){
		this.rollStatus=rollStatus;
	}

	public Integer getRollStatus(){
		return this.rollStatus;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setRoId(Long roId){
		this.roId=roId;
	}

	public Long getRoId(){
		return this.roId;
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

	public void setPartCode(String partCode){
		this.partCode=partCode;
	}

	public String getPartCode(){
		return this.partCode;
	}

	public void setRollBy(Long rollBy){
		this.rollBy=rollBy;
	}

	public Long getRollBy(){
		return this.rollBy;
	}

}