/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-08-19 15:11:11
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsRoRepairitemPO extends PO{

	private String sgmLabourCode;
	private Date updateDate;
	private String troubleReason;
	private String labourName;
	private Double assignLabourHour;
	private Long createBy;
	private String workerType;
	private String balanceNo;
	private String technician;
	private String chargeMode;
	private Double stdLabourHour;
	private String roNo;
	private Long updateBy;
	private Double labourAmount;
	private Integer isSgmTag;
	private Long id;
	private String troubleDesc;
	private Date createDate;
	private Double addLabourHour;
	private Long repairItemId;

	public void setSgmLabourCode(String sgmLabourCode){
		this.sgmLabourCode=sgmLabourCode;
	}

	public String getSgmLabourCode(){
		return this.sgmLabourCode;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setTroubleReason(String troubleReason){
		this.troubleReason=troubleReason;
	}

	public String getTroubleReason(){
		return this.troubleReason;
	}

	public void setLabourName(String labourName){
		this.labourName=labourName;
	}

	public String getLabourName(){
		return this.labourName;
	}

	public void setAssignLabourHour(Double assignLabourHour){
		this.assignLabourHour=assignLabourHour;
	}

	public Double getAssignLabourHour(){
		return this.assignLabourHour;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setWorkerType(String workerType){
		this.workerType=workerType;
	}

	public String getWorkerType(){
		return this.workerType;
	}

	public void setBalanceNo(String balanceNo){
		this.balanceNo=balanceNo;
	}

	public String getBalanceNo(){
		return this.balanceNo;
	}

	public void setTechnician(String technician){
		this.technician=technician;
	}

	public String getTechnician(){
		return this.technician;
	}

	public void setChargeMode(String chargeMode){
		this.chargeMode=chargeMode;
	}

	public String getChargeMode(){
		return this.chargeMode;
	}

	public void setStdLabourHour(Double stdLabourHour){
		this.stdLabourHour=stdLabourHour;
	}

	public Double getStdLabourHour(){
		return this.stdLabourHour;
	}

	public void setRoNo(String roNo){
		this.roNo=roNo;
	}

	public String getRoNo(){
		return this.roNo;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setLabourAmount(Double labourAmount){
		this.labourAmount=labourAmount;
	}

	public Double getLabourAmount(){
		return this.labourAmount;
	}

	public void setIsSgmTag(Integer isSgmTag){
		this.isSgmTag=isSgmTag;
	}

	public Integer getIsSgmTag(){
		return this.isSgmTag;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setTroubleDesc(String troubleDesc){
		this.troubleDesc=troubleDesc;
	}

	public String getTroubleDesc(){
		return this.troubleDesc;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setAddLabourHour(Double addLabourHour){
		this.addLabourHour=addLabourHour;
	}

	public Double getAddLabourHour(){
		return this.addLabourHour;
	}

	public void setRepairItemId(Long repairItemId){
		this.repairItemId=repairItemId;
	}

	public Long getRepairItemId(){
		return this.repairItemId;
	}

}