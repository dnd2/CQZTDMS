/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-04-07 21:21:03
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtSalesCityDisPO extends PO{

	private Long cityId;
	private Long yieldly;
	private Date updateDate;
	private Long updateBy;
	private Long provinceId;
	private Long createBy;
	private Date createDate;
	private Integer arriveDays;
	private Long distance;
	private Long disId;
	
	
	private Long carTieId;
	private String startPlace;
	private String transWay;
	private Double singlePlace;
	private Date fuelBeginDate;
	private Date fuelEndDate;
	private Double fuelCoefficient;
	private Double handPrice;
	private String remark;
	
	public Double getHandPrice() {
		return handPrice;
	}

	public void setHandPrice(Double handPrice) {
		this.handPrice = handPrice;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Double getFuelCoefficient() {
		return fuelCoefficient;
	}

	public void setFuelCoefficient(Double fuelCoefficient) {
		this.fuelCoefficient = fuelCoefficient;
	}

	public String getStartPlace() {
		return startPlace;
	}

	public void setStartPlace(String startPlace) {
		this.startPlace = startPlace;
	}

	public String getTransWay() {
		return transWay;
	}

	public void setTransWay(String transWay) {
		this.transWay = transWay;
	}

	public Double getSinglePlace() {
		return singlePlace;
	}

	public void setSinglePlace(Double singlePlace) {
		this.singlePlace = singlePlace;
	}

	public Date getFuelBeginDate() {
		return fuelBeginDate;
	}

	public void setFuelBeginDate(Date fuelBeginDate) {
		this.fuelBeginDate = fuelBeginDate;
	}

	public Date getFuelEndDate() {
		return fuelEndDate;
	}

	public void setFuelEndDate(Date fuelEndDate) {
		this.fuelEndDate = fuelEndDate;
	}

	public Long getCarTieId() {
		return carTieId;
	}

	public void setCarTieId(Long carTieId) {
		this.carTieId = carTieId;
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

	public void setArriveDays(Integer arriveDays){
		this.arriveDays=arriveDays;
	}

	public Integer getArriveDays(){
		return this.arriveDays;
	}

	public void setDistance(Long distance){
		this.distance=distance;
	}

	public Long getDistance(){
		return this.distance;
	}

	public void setDisId(Long disId){
		this.disId=disId;
	}

	public Long getDisId(){
		return this.disId;
	}

}