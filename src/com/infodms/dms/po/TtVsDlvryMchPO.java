/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-09-25 13:01:12
* CreateBy   : fengalon
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsDlvryMchPO extends PO{

	private Long matchId;
	private Integer ifInspection;
	private Long updateBy;
	private Date updateDate;
	private Long createBy;
	private Long erpSendcarId;
	private Date createDate;
	private Integer status;
	private Long deliveryDetailId;
	private Long vehicleId;

	public void setMatchId(Long matchId){
		this.matchId=matchId;
	}

	public Long getMatchId(){
		return this.matchId;
	}

	public void setIfInspection(Integer ifInspection){
		this.ifInspection=ifInspection;
	}

	public Integer getIfInspection(){
		return this.ifInspection;
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

	public void setErpSendcarId(Long erpSendcarId){
		this.erpSendcarId=erpSendcarId;
	}

	public Long getErpSendcarId(){
		return this.erpSendcarId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setDeliveryDetailId(Long deliveryDetailId){
		this.deliveryDetailId=deliveryDetailId;
	}

	public Long getDeliveryDetailId(){
		return this.deliveryDetailId;
	}

	public void setVehicleId(Long vehicleId){
		this.vehicleId=vehicleId;
	}

	public Long getVehicleId(){
		return this.vehicleId;
	}

}