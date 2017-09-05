/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-12-22 10:02:57
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TPcTestDriveRoutePO extends PO{

	private Long routeId;
	private String routeName;
	private String remarks;
	private Long updateBy;
	private String mileage;
	private Date updateDate;
	private String startLine;
	private Long createBy;
	private String endLine;
	private Date createDate;
	private Integer status;

	public void setRouteId(Long routeId){
		this.routeId=routeId;
	}

	public Long getRouteId(){
		return this.routeId;
	}

	public void setRouteName(String routeName){
		this.routeName=routeName;
	}

	public String getRouteName(){
		return this.routeName;
	}

	public void setRemarks(String remarks){
		this.remarks=remarks;
	}

	public String getRemarks(){
		return this.remarks;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setMileage(String mileage){
		this.mileage=mileage;
	}

	public String getMileage(){
		return this.mileage;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setStartLine(String startLine){
		this.startLine=startLine;
	}

	public String getStartLine(){
		return this.startLine;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setEndLine(String endLine){
		this.endLine=endLine;
	}

	public String getEndLine(){
		return this.endLine;
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

}