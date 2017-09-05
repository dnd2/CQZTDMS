/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-05-21 15:11:47
* CreateBy   : Arthur_Liu
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtInspectionPO extends PO{

	private String inspectionPerson;
	private Date updateDate;
	private String vehicleArea;
	private String remark;
	private Long createBy;
	private Long inspectionId;
	private Long updateBy;
	private Date arriveDate;
	private Date createDate;
	private String transportPerson;
	private Long vehicleId;
	private String arriveTime;
	private String inspectionNo;

	public void setInspectionPerson(String inspectionPerson){
		this.inspectionPerson=inspectionPerson;
	}

	public String getInspectionPerson(){
		return this.inspectionPerson;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setVehicleArea(String vehicleArea){
		this.vehicleArea=vehicleArea;
	}

	public String getVehicleArea(){
		return this.vehicleArea;
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

	public void setInspectionId(Long inspectionId){
		this.inspectionId=inspectionId;
	}

	public Long getInspectionId(){
		return this.inspectionId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setArriveDate(Date arriveDate){
		this.arriveDate=arriveDate;
	}

	public Date getArriveDate(){
		return this.arriveDate;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setTransportPerson(String transportPerson){
		this.transportPerson=transportPerson;
	}

	public String getTransportPerson(){
		return this.transportPerson;
	}

	public void setVehicleId(Long vehicleId){
		this.vehicleId=vehicleId;
	}

	public Long getVehicleId(){
		return this.vehicleId;
	}

	public void setArriveTime(String arriveTime){
		this.arriveTime=arriveTime;
	}

	public String getArriveTime(){
		return this.arriveTime;
	}

	public void setInspectionNo(String inspectionNo){
		this.inspectionNo=inspectionNo;
	}

	public String getInspectionNo(){
		return this.inspectionNo;
	}

}