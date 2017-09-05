/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-04-26 19:59:42
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsVehicleAllocatePO extends PO{

	private Long allocateId;
	private Long outDealerId;
	private Long inDealerId;
	private Long companyId;
	private Date updateDate;
	private Long createBy;
	private String allocateNo;
	private String allocateReason;
	private Long fundtypeOutId;
	private Integer checkStatus;
	private Long billAmount;
	private Date allocateDateOut;
	private Long updateBy;
	private Long interfaceFlag;
	private Date allocateDate;
	private Date createDate;
	private Long vehicleId;
	private Long fundtypeInId;
	private Date allocateDateIn;

	public void setAllocateId(Long allocateId){
		this.allocateId=allocateId;
	}

	public Long getAllocateId(){
		return this.allocateId;
	}

	public void setOutDealerId(Long outDealerId){
		this.outDealerId=outDealerId;
	}

	public Long getOutDealerId(){
		return this.outDealerId;
	}

	public void setInDealerId(Long inDealerId){
		this.inDealerId=inDealerId;
	}

	public Long getInDealerId(){
		return this.inDealerId;
	}

	public void setCompanyId(Long companyId){
		this.companyId=companyId;
	}

	public Long getCompanyId(){
		return this.companyId;
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

	public void setAllocateNo(String allocateNo){
		this.allocateNo=allocateNo;
	}

	public String getAllocateNo(){
		return this.allocateNo;
	}

	public void setAllocateReason(String allocateReason){
		this.allocateReason=allocateReason;
	}

	public String getAllocateReason(){
		return this.allocateReason;
	}

	public void setFundtypeOutId(Long fundtypeOutId){
		this.fundtypeOutId=fundtypeOutId;
	}

	public Long getFundtypeOutId(){
		return this.fundtypeOutId;
	}

	public void setCheckStatus(Integer checkStatus){
		this.checkStatus=checkStatus;
	}

	public Integer getCheckStatus(){
		return this.checkStatus;
	}

	public void setBillAmount(Long billAmount){
		this.billAmount=billAmount;
	}

	public Long getBillAmount(){
		return this.billAmount;
	}

	public void setAllocateDateOut(Date allocateDateOut){
		this.allocateDateOut=allocateDateOut;
	}

	public Date getAllocateDateOut(){
		return this.allocateDateOut;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setInterfaceFlag(Long interfaceFlag){
		this.interfaceFlag=interfaceFlag;
	}

	public Long getInterfaceFlag(){
		return this.interfaceFlag;
	}

	public void setAllocateDate(Date allocateDate){
		this.allocateDate=allocateDate;
	}

	public Date getAllocateDate(){
		return this.allocateDate;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setVehicleId(Long vehicleId){
		this.vehicleId=vehicleId;
	}

	public Long getVehicleId(){
		return this.vehicleId;
	}

	public void setFundtypeInId(Long fundtypeInId){
		this.fundtypeInId=fundtypeInId;
	}

	public Long getFundtypeInId(){
		return this.fundtypeInId;
	}

	public void setAllocateDateIn(Date allocateDateIn){
		this.allocateDateIn=allocateDateIn;
	}

	public Date getAllocateDateIn(){
		return this.allocateDateIn;
	}

}