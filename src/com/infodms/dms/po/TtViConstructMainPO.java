/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-11-14 10:12:23
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtViConstructMainPO extends PO{

	private Integer state;
	private Date acceptDate;
	private Long dealerId;
	private Integer imageLevel;
	private Date constructEdate;
	private Long id;
	private Long createBy;
	private Integer checkedImageLevel;
	private Date createDate;
	private Date checkedDate;
	private Integer checkedType;
	private Date constructSdate;
	private Double offlineRebate;
	private Long vehicleSeriesId;

	public void setState(Integer state){
		this.state=state;
	}

	public Integer getState(){
		return this.state;
	}

	public void setAcceptDate(Date acceptDate){
		this.acceptDate=acceptDate;
	}

	public Date getAcceptDate(){
		return this.acceptDate;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setImageLevel(Integer imageLevel){
		this.imageLevel=imageLevel;
	}

	public Integer getImageLevel(){
		return this.imageLevel;
	}

	public void setConstructEdate(Date constructEdate){
		this.constructEdate=constructEdate;
	}

	public Date getConstructEdate(){
		return this.constructEdate;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setCheckedImageLevel(Integer checkedImageLevel){
		this.checkedImageLevel=checkedImageLevel;
	}

	public Integer getCheckedImageLevel(){
		return this.checkedImageLevel;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setCheckedDate(Date checkedDate){
		this.checkedDate=checkedDate;
	}

	public Date getCheckedDate(){
		return this.checkedDate;
	}

	public void setCheckedType(Integer checkedType){
		this.checkedType=checkedType;
	}

	public Integer getCheckedType(){
		return this.checkedType;
	}

	public void setConstructSdate(Date constructSdate){
		this.constructSdate=constructSdate;
	}

	public Date getConstructSdate(){
		return this.constructSdate;
	}

	public Double getOfflineRebate() {
		return offlineRebate;
	}

	public void setOfflineRebate(Double offlineRebate) {
		this.offlineRebate = offlineRebate;
	}

	public Long getVehicleSeriesId() {
		return vehicleSeriesId;
	}

	public void setVehicleSeriesId(Long vehicleSeriesId) {
		this.vehicleSeriesId = vehicleSeriesId;
	}

}