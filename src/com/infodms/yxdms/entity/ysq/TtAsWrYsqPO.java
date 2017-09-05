/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2015-02-09 14:04:15
* CreateBy   : yuewei
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.yxdms.entity.ysq;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrYsqPO extends PO{

	private Integer claimType;
	private String bcRemark;
	private Long dealerId;
	private String vin;
	private String partName;
	private String troubleReason;
	private String producerCode;
	private Long createBy;
	private Integer status;
	private String ysqNo;
	private Long modelId;
	private Float bcApply;
	private Double mileage;
	private Long partId;
	private Long seriesId;
	private Long id;
	private String troubleDesc;
	private Float bcPass;
	private Integer isReturn;
	private String producerName;
	private Integer isDelete;
	private String partCode;
	private Float comApply;
	private Float maxEstimate;
	private Float comPass;
	private String relationRo;
	private Date createDate;
	private String comRemark;
	private Integer isEnd;
	private Integer jugeRes;//判断情况标示
	private Double bcMileage;

	public void setClaimType(Integer claimType){
		this.claimType=claimType;
	}

	public Integer getClaimType(){
		return this.claimType;
	}

	public void setBcRemark(String bcRemark){
		this.bcRemark=bcRemark;
	}

	public String getBcRemark(){
		return this.bcRemark;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setVin(String vin){
		this.vin=vin;
	}

	public String getVin(){
		return this.vin;
	}

	public void setPartName(String partName){
		this.partName=partName;
	}

	public String getPartName(){
		return this.partName;
	}

	public void setTroubleReason(String troubleReason){
		this.troubleReason=troubleReason;
	}

	public String getTroubleReason(){
		return this.troubleReason;
	}

	public void setProducerCode(String producerCode){
		this.producerCode=producerCode;
	}

	public String getProducerCode(){
		return this.producerCode;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setYsqNo(String ysqNo){
		this.ysqNo=ysqNo;
	}

	public String getYsqNo(){
		return this.ysqNo;
	}

	public void setModelId(Long modelId){
		this.modelId=modelId;
	}

	public Long getModelId(){
		return this.modelId;
	}

	public void setBcApply(Float bcApply){
		this.bcApply=bcApply;
	}

	public Float getBcApply(){
		return this.bcApply;
	}

	public void setMileage(Double mileage){
		this.mileage=mileage;
	}

	public Double getMileage(){
		return this.mileage;
	}

	public void setPartId(Long partId){
		this.partId=partId;
	}

	public Long getPartId(){
		return this.partId;
	}

	public void setSeriesId(Long seriesId){
		this.seriesId=seriesId;
	}

	public Long getSeriesId(){
		return this.seriesId;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setTroubleDesc(String troubleDesc){
		this.troubleDesc=troubleDesc;
	}

	public String getTroubleDesc(){
		return this.troubleDesc;
	}

	public void setBcPass(Float bcPass){
		this.bcPass=bcPass;
	}

	public Float getBcPass(){
		return this.bcPass;
	}

	public void setIsReturn(Integer isReturn){
		this.isReturn=isReturn;
	}

	public Integer getIsReturn(){
		return this.isReturn;
	}

	public void setProducerName(String producerName){
		this.producerName=producerName;
	}

	public String getProducerName(){
		return this.producerName;
	}

	public void setIsDelete(Integer isDelete){
		this.isDelete=isDelete;
	}

	public Integer getIsDelete(){
		return this.isDelete;
	}

	public void setPartCode(String partCode){
		this.partCode=partCode;
	}

	public String getPartCode(){
		return this.partCode;
	}

	public void setComApply(Float comApply){
		this.comApply=comApply;
	}

	public Float getComApply(){
		return this.comApply;
	}

	public void setMaxEstimate(Float maxEstimate){
		this.maxEstimate=maxEstimate;
	}

	public Float getMaxEstimate(){
		return this.maxEstimate;
	}

	public void setComPass(Float comPass){
		this.comPass=comPass;
	}

	public Float getComPass(){
		return this.comPass;
	}

	public void setRelationRo(String relationRo){
		this.relationRo=relationRo;
	}

	public String getRelationRo(){
		return this.relationRo;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setComRemark(String comRemark){
		this.comRemark=comRemark;
	}

	public String getComRemark(){
		return this.comRemark;
	}

	public void setIsEnd(Integer isEnd){
		this.isEnd=isEnd;
	}

	public Integer getIsEnd(){
		return this.isEnd;
	}

	public Integer getJugeRes() {
		return jugeRes;
	}

	public void setJugeRes(Integer jugeRes) {
		this.jugeRes = jugeRes;
	}

	public Double getBcMileage() {
		return bcMileage;
	}

	public void setBcMileage(Double bcMileage) {
		this.bcMileage = bcMileage;
	}

}