/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-04-03 11:38:57
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtSalesRoadPO extends PO{

	private String roadName;
	private Date updateDate;
	private Long status;
	private Long inStatus;
	private Long updateBy;
	private Long createBy;
	private Long outStatus;
	private Long roadId;
	private Date createDate;
	private Long areaId;

	public void setRoadName(String roadName){
		this.roadName=roadName;
	}

	public String getRoadName(){
		return this.roadName;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setStatus(Long status){
		this.status=status;
	}

	public Long getStatus(){
		return this.status;
	}

	public void setInStatus(Long inStatus){
		this.inStatus=inStatus;
	}

	public Long getInStatus(){
		return this.inStatus;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setOutStatus(Long outStatus){
		this.outStatus=outStatus;
	}

	public Long getOutStatus(){
		return this.outStatus;
	}

	public void setRoadId(Long roadId){
		this.roadId=roadId;
	}

	public Long getRoadId(){
		return this.roadId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setAreaId(Long areaId){
		this.areaId=areaId;
	}

	public Long getAreaId(){
		return this.areaId;
	}

}