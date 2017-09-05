/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-09-24 15:12:22
* CreateBy   : POLA
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartCityDisPO extends PO{

	private Long endPlaceId;
	private Long distance;
	private Date updateDate;
	private String remark;
	private Long createBy;
	private Long startPlaceId;
	private Long logiId;
	private String transType;
	private Long updateBy;
	private Double miniWeight;
	private Long disId;
	private Double additionalWeight;
	private Double firstWeight;
	private Integer arriveDays;
	private Date createDate;

	public void setEndPlaceId(Long endPlaceId){
		this.endPlaceId=endPlaceId;
	}

	public Long getEndPlaceId(){
		return this.endPlaceId;
	}

	public void setDistance(Long distance){
		this.distance=distance;
	}

	public Long getDistance(){
		return this.distance;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
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

	public void setStartPlaceId(Long startPlaceId){
		this.startPlaceId=startPlaceId;
	}

	public Long getStartPlaceId(){
		return this.startPlaceId;
	}

	public void setLogiId(Long logiId){
		this.logiId=logiId;
	}

	public Long getLogiId(){
		return this.logiId;
	}

	public void setTransType(String transType){
		this.transType=transType;
	}

	public String getTransType(){
		return this.transType;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setMiniWeight(Double miniWeight){
		this.miniWeight=miniWeight;
	}

	public Double getMiniWeight(){
		return this.miniWeight;
	}

	public void setDisId(Long disId){
		this.disId=disId;
	}

	public Long getDisId(){
		return this.disId;
	}

	public void setAdditionalWeight(Double additionalWeight){
		this.additionalWeight=additionalWeight;
	}

	public Double getAdditionalWeight(){
		return this.additionalWeight;
	}

	public void setFirstWeight(Double firstWeight){
		this.firstWeight=firstWeight;
	}

	public Double getFirstWeight(){
		return this.firstWeight;
	}

	public void setArriveDays(Integer arriveDays){
		this.arriveDays=arriveDays;
	}

	public Integer getArriveDays(){
		return this.arriveDays;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

}