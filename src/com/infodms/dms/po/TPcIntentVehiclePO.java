/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-11-20 17:03:20
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TPcIntentVehiclePO extends PO{

	private Long seriesId;
	private Date updateDate;
	private Integer status;
	private String updateBy;
	private String upSeriesId;
	private String createBy;
	private String seriesName;
	private Date createDate;
	private String seriesCode;
	private Long isForeign;
	
	

	public Long getIsForeign() {
		return isForeign;
	}

	public void setIsForeign(Long isForeign) {
		this.isForeign = isForeign;
	}

	public void setSeriesId(Long seriesId){
		this.seriesId=seriesId;
	}

	public Long getSeriesId(){
		return this.seriesId;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setUpdateBy(String updateBy){
		this.updateBy=updateBy;
	}

	public String getUpdateBy(){
		return this.updateBy;
	}

	public void setUpSeriesId(String upSeriesId){
		this.upSeriesId=upSeriesId;
	}

	public String getUpSeriesId(){
		return this.upSeriesId;
	}

	public void setCreateBy(String createBy){
		this.createBy=createBy;
	}

	public String getCreateBy(){
		return this.createBy;
	}

	public void setSeriesName(String seriesName){
		this.seriesName=seriesName;
	}

	public String getSeriesName(){
		return this.seriesName;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setSeriesCode(String seriesCode){
		this.seriesCode=seriesCode;
	}

	public String getSeriesCode(){
		return this.seriesCode;
	}

}