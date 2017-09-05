/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-07-01 17:02:41
* CreateBy   : 
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtCampaignTargetPO extends PO{

	private Long targetId;
	private Integer callsHousesCntTgt;
	private Integer reserveCntAct;
	private Date updateDate;
	private Integer orderCntAct;
	private Integer callsHousesCntAct;
	private Integer orderCntTgt;
	private Long createBy;
	private Integer deliveryCntTgt;
	private Long executeId;
	private Integer reserveCntTgt;
	private Integer deliveryCntAct;
	private Long updateBy;
	private Date createDate;

	public void setTargetId(Long targetId){
		this.targetId=targetId;
	}

	public Long getTargetId(){
		return this.targetId;
	}

	public void setCallsHousesCntTgt(Integer callsHousesCntTgt){
		this.callsHousesCntTgt=callsHousesCntTgt;
	}

	public Integer getCallsHousesCntTgt(){
		return this.callsHousesCntTgt;
	}

	public void setReserveCntAct(Integer reserveCntAct){
		this.reserveCntAct=reserveCntAct;
	}

	public Integer getReserveCntAct(){
		return this.reserveCntAct;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setOrderCntAct(Integer orderCntAct){
		this.orderCntAct=orderCntAct;
	}

	public Integer getOrderCntAct(){
		return this.orderCntAct;
	}

	public void setCallsHousesCntAct(Integer callsHousesCntAct){
		this.callsHousesCntAct=callsHousesCntAct;
	}

	public Integer getCallsHousesCntAct(){
		return this.callsHousesCntAct;
	}

	public void setOrderCntTgt(Integer orderCntTgt){
		this.orderCntTgt=orderCntTgt;
	}

	public Integer getOrderCntTgt(){
		return this.orderCntTgt;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setDeliveryCntTgt(Integer deliveryCntTgt){
		this.deliveryCntTgt=deliveryCntTgt;
	}

	public Integer getDeliveryCntTgt(){
		return this.deliveryCntTgt;
	}

	public void setExecuteId(Long executeId){
		this.executeId=executeId;
	}

	public Long getExecuteId(){
		return this.executeId;
	}

	public void setReserveCntTgt(Integer reserveCntTgt){
		this.reserveCntTgt=reserveCntTgt;
	}

	public Integer getReserveCntTgt(){
		return this.reserveCntTgt;
	}

	public void setDeliveryCntAct(Integer deliveryCntAct){
		this.deliveryCntAct=deliveryCntAct;
	}

	public Integer getDeliveryCntAct(){
		return this.deliveryCntAct;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

}