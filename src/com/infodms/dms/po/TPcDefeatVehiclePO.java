/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2015-01-23 16:01:46
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TPcDefeatVehiclePO extends PO{

	private Long seriesId;
	private Integer status;
	private String upSeriesId;
	private String seriesName;
	private String seriesCode;

	public void setSeriesId(Long seriesId){
		this.seriesId=seriesId;
	}

	public Long getSeriesId(){
		return this.seriesId;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setUpSeriesId(String upSeriesId){
		this.upSeriesId=upSeriesId;
	}

	public String getUpSeriesId(){
		return this.upSeriesId;
	}

	public void setSeriesName(String seriesName){
		this.seriesName=seriesName;
	}

	public String getSeriesName(){
		return this.seriesName;
	}

	public void setSeriesCode(String seriesCode){
		this.seriesCode=seriesCode;
	}

	public String getSeriesCode(){
		return this.seriesCode;
	}

}