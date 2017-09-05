/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-07-09 10:54:26
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtSalesPosHisPO extends PO{

	private Long vehicleId;
	private Date updateDate;
	private Date chDate;
	private Long updateBy;
	private Long newSitId;
	private Long createBy;
	private Date createDate;
	private String chRemark;
	private Long chPer;
	private Long oldSitId;
	private Long chHisId;

	public void setVehicleId(Long vehicleId){
		this.vehicleId=vehicleId;
	}

	public Long getVehicleId(){
		return this.vehicleId;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setChDate(Date chDate){
		this.chDate=chDate;
	}

	public Date getChDate(){
		return this.chDate;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setNewSitId(Long newSitId){
		this.newSitId=newSitId;
	}

	public Long getNewSitId(){
		return this.newSitId;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setChRemark(String chRemark){
		this.chRemark=chRemark;
	}

	public String getChRemark(){
		return this.chRemark;
	}

	public void setChPer(Long chPer){
		this.chPer=chPer;
	}

	public Long getChPer(){
		return this.chPer;
	}

	public void setOldSitId(Long oldSitId){
		this.oldSitId=oldSitId;
	}

	public Long getOldSitId(){
		return this.oldSitId;
	}

	public void setChHisId(Long chHisId){
		this.chHisId=chHisId;
	}

	public Long getChHisId(){
		return this.chHisId;
	}

}