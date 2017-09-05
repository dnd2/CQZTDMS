/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-03-01 18:55:50
* CreateBy   : xwj
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmVehicleChangePO extends PO{

	private Long vehicleId;
	private Long vehicleChangeId;
	private Date nodeDate;
	private Long createBy;
	private Long orgId;
	private Date createDate;
	private String remark;
	private Integer node;

	public void setVehicleId(Long vehicleId){
		this.vehicleId=vehicleId;
	}

	public Long getVehicleId(){
		return this.vehicleId;
	}

	public void setVehicleChangeId(Long vehicleChangeId){
		this.vehicleChangeId=vehicleChangeId;
	}

	public Long getVehicleChangeId(){
		return this.vehicleChangeId;
	}

	public void setNodeDate(Date nodeDate){
		this.nodeDate=nodeDate;
	}

	public Date getNodeDate(){
		return this.nodeDate;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setOrgId(Long orgId){
		this.orgId=orgId;
	}

	public Long getOrgId(){
		return this.orgId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setNode(Integer node){
		this.node=node;
	}

	public Integer getNode(){
		return this.node;
	}

}