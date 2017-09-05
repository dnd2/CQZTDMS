/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-05-31 17:33:23
* CreateBy   : Arthur_Liu
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVehicleTransferPO extends PO{

	private Date transferDate;
	private Integer checkStatus;
	private Long outDealerId;
	private Long updateBy;
	private Long inDealerId;
	private String transferReason;
	private Date updateDate;
	private Long createBy;
	private Long transferId;
	private Date createDate;
	private Long vehicleId;

	public void setTransferDate(Date transferDate){
		this.transferDate=transferDate;
	}

	public Date getTransferDate(){
		return this.transferDate;
	}

	public void setCheckStatus(Integer checkStatus){
		this.checkStatus=checkStatus;
	}

	public Integer getCheckStatus(){
		return this.checkStatus;
	}

	public void setOutDealerId(Long outDealerId){
		this.outDealerId=outDealerId;
	}

	public Long getOutDealerId(){
		return this.outDealerId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setInDealerId(Long inDealerId){
		this.inDealerId=inDealerId;
	}

	public Long getInDealerId(){
		return this.inDealerId;
	}

	public void setTransferReason(String transferReason){
		this.transferReason=transferReason;
	}

	public String getTransferReason(){
		return this.transferReason;
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

	public void setTransferId(Long transferId){
		this.transferId=transferId;
	}

	public Long getTransferId(){
		return this.transferId;
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

}