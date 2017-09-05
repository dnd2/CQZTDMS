/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-04-22 16:11:04
* CreateBy   : -Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtSalesModleDisrateDealerPO extends PO{

	private Long auditPer;
	private Long dealerId;
	private Double otherDisrate;
	private Long disrateId;
	private Double disRateAmount;
	private Long createBy;
	private Double buildAmount;
	private Integer status;
	private Double lastSettleAmount;
	private Double settleAmount;
	private Date auditDate;
	private Long packageId;
	private Date createDate;
	private Integer auditStatus;
	private Double disRate;
	private Date startDate;
	private Date stopDate;

	public Date getStartDate()
	{
		return startDate;
	}

	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}

	public Date getStopDate()
	{
		return stopDate;
	}

	public void setStopDate(Date stopDate)
	{
		this.stopDate = stopDate;
	}

	public void setAuditPer(Long auditPer){
		this.auditPer=auditPer;
	}

	public Long getAuditPer(){
		return this.auditPer;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setOtherDisrate(Double otherDisrate){
		this.otherDisrate=otherDisrate;
	}

	public Double getOtherDisrate(){
		return this.otherDisrate;
	}

	public void setDisrateId(Long disrateId){
		this.disrateId=disrateId;
	}

	public Long getDisrateId(){
		return this.disrateId;
	}

	public void setDisRateAmount(Double disRateAmount){
		this.disRateAmount=disRateAmount;
	}

	public Double getDisRateAmount(){
		return this.disRateAmount;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setBuildAmount(Double buildAmount){
		this.buildAmount=buildAmount;
	}

	public Double getBuildAmount(){
		return this.buildAmount;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setLastSettleAmount(Double lastSettleAmount){
		this.lastSettleAmount=lastSettleAmount;
	}

	public Double getLastSettleAmount(){
		return this.lastSettleAmount;
	}

	public void setSettleAmount(Double settleAmount){
		this.settleAmount=settleAmount;
	}

	public Double getSettleAmount(){
		return this.settleAmount;
	}

	public void setAuditDate(Date auditDate){
		this.auditDate=auditDate;
	}

	public Date getAuditDate(){
		return this.auditDate;
	}

	public void setPackageId(Long packageId){
		this.packageId=packageId;
	}

	public Long getPackageId(){
		return this.packageId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setAuditStatus(Integer auditStatus){
		this.auditStatus=auditStatus;
	}

	public Integer getAuditStatus(){
		return this.auditStatus;
	}

	public void setDisRate(Double disRate){
		this.disRate=disRate;
	}

	public Double getDisRate(){
		return this.disRate;
	}

}