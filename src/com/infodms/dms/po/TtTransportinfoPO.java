/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2017-07-10 15:07:46
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtTransportinfoPO extends PO{

	private Double price;
	private String placeCityId;
	private Date updateDate;
	private Long createBy;
	private String tvId;
	private String status;
	private String destCounties;
	private Long updateBy;
	private String carrier;
	private String placeCounties;
	private String destCityId;
	private Date createDate;
	private String destProvinceId;
	private String placeProvinceId;
	private String pkId;

	public String getPkId() {
		return pkId;
	}

	public void setPkId(String pkId) {
		this.pkId = pkId;
	}

	public void setPrice(Double price){
		this.price=price;
	}

	public Double getPrice(){
		return this.price;
	}

	public void setPlaceCityId(String placeCityId){
		this.placeCityId=placeCityId;
	}

	public String getPlaceCityId(){
		return this.placeCityId;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setTvId(String tvId){
		this.tvId=tvId;
	}

	public String getTvId(){
		return this.tvId;
	}

	public void setStatus(String status){
		this.status=status;
	}

	public String getStatus(){
		return this.status;
	}

	public void setDestCounties(String destCounties){
		this.destCounties=destCounties;
	}

	public String getDestCounties(){
		return this.destCounties;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setCarrier(String carrier){
		this.carrier=carrier;
	}

	public String getCarrier(){
		return this.carrier;
	}

	public void setPlaceCounties(String placeCounties){
		this.placeCounties=placeCounties;
	}

	public String getPlaceCounties(){
		return this.placeCounties;
	}

	public void setDestCityId(String destCityId){
		this.destCityId=destCityId;
	}

	public String getDestCityId(){
		return this.destCityId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setDestProvinceId(String destProvinceId){
		this.destProvinceId=destProvinceId;
	}

	public String getDestProvinceId(){
		return this.destProvinceId;
	}

	public void setPlaceProvinceId(String placeProvinceId){
		this.placeProvinceId=placeProvinceId;
	}

	public String getPlaceProvinceId(){
		return this.placeProvinceId;
	}

}