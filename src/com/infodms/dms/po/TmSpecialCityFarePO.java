/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-07-10 20:36:02
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmSpecialCityFarePO extends PO{

	private Float amount;
	private Long seriesId;
	private Long cityId;
	private Long yieldly;
	private Date updateDate;
	private Long cityIdd;
	private Long greateBy;
	private Long updateBy;
	private Long provinceId;
	private Long fareId;
	private Date createDate;

	public void setAmount(Float amount){
		this.amount=amount;
	}

	public Float getAmount(){
		return this.amount;
	}

	public void setSeriesId(Long seriesId){
		this.seriesId=seriesId;
	}

	public Long getSeriesId(){
		return this.seriesId;
	}

	public void setCityId(Long cityId){
		this.cityId=cityId;
	}

	public Long getCityId(){
		return this.cityId;
	}

	public void setYieldly(Long yieldly){
		this.yieldly=yieldly;
	}

	public Long getYieldly(){
		return this.yieldly;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setCityIdd(Long cityIdd){
		this.cityIdd=cityIdd;
	}

	public Long getCityIdd(){
		return this.cityIdd;
	}

	public void setGreateBy(Long greateBy){
		this.greateBy=greateBy;
	}

	public Long getGreateBy(){
		return this.greateBy;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setProvinceId(Long provinceId){
		this.provinceId=provinceId;
	}

	public Long getProvinceId(){
		return this.provinceId;
	}

	public void setFareId(Long fareId){
		this.fareId=fareId;
	}

	public Long getFareId(){
		return this.fareId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

}