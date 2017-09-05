/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-07-18 17:33:04
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtSalesRecProcessHisPO extends PO{

	private String recRemark;
	private Long sitId;
	private Long vehicleId;
	private Integer recType;
	private Long createBy;
	private Date recDate;
	private Long recHisId;
	private Date createDate;
	private Long recPer;

	public void setRecRemark(String recRemark){
		this.recRemark=recRemark;
	}

	public String getRecRemark(){
		return this.recRemark;
	}

	public void setSitId(Long sitId){
		this.sitId=sitId;
	}

	public Long getSitId(){
		return this.sitId;
	}

	public void setVehicleId(Long vehicleId){
		this.vehicleId=vehicleId;
	}

	public Long getVehicleId(){
		return this.vehicleId;
	}

	public void setRecType(Integer recType){
		this.recType=recType;
	}

	public Integer getRecType(){
		return this.recType;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setRecDate(Date recDate){
		this.recDate=recDate;
	}

	public Date getRecDate(){
		return this.recDate;
	}

	public void setRecHisId(Long recHisId){
		this.recHisId=recHisId;
	}

	public Long getRecHisId(){
		return this.recHisId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setRecPer(Long recPer){
		this.recPer=recPer;
	}

	public Long getRecPer(){
		return this.recPer;
	}

}