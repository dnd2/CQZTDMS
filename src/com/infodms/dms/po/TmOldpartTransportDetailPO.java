/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-03-06 16:32:50
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmOldpartTransportDetailPO extends PO{

	private Integer arriveWay;
	private Double priceOther;
	private Date updateDate;
	private Double priceCubic;
	private Double sendCosts;
	private Long createBy;
	private String remark;
	private Long detailId;
	private String linkPhone;
	private String sendPhone;
	private Double priceWeight;
	private Long updateBy;
	private String sendPerson;
	private String transportName;
	private Date createDate;
	private String arrivePlace;
	private String returnType;
	private Long transportId;
	private String auditRemark;//审核备注

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	private Integer status;
	public Long getTransportId() {
		return transportId;
	}

	public void setTransportId(Long transportId) {
		this.transportId = transportId;
	}

	public void setArriveWay(Integer arriveWay){
		this.arriveWay=arriveWay;
	}

	public Integer getArriveWay(){
		return this.arriveWay;
	}

	public void setPriceOther(Double priceOther){
		this.priceOther=priceOther;
	}

	public Double getPriceOther(){
		return this.priceOther;
	}


	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setPriceCubic(Double priceCubic){
		this.priceCubic=priceCubic;
	}

	public Double getPriceCubic(){
		return this.priceCubic;
	}

	public void setSendCosts(Double sendCosts){
		this.sendCosts=sendCosts;
	}

	public Double getSendCosts(){
		return this.sendCosts;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setDetailId(Long detailId){
		this.detailId=detailId;
	}

	public Long getDetailId(){
		return this.detailId;
	}

	public void setLinkPhone(String linkPhone){
		this.linkPhone=linkPhone;
	}

	public String getLinkPhone(){
		return this.linkPhone;
	}


	public void setSendPhone(String sendPhone){
		this.sendPhone=sendPhone;
	}

	public String getSendPhone(){
		return this.sendPhone;
	}

	public void setPriceWeight(Double priceWeight){
		this.priceWeight=priceWeight;
	}

	public Double getPriceWeight(){
		return this.priceWeight;
	}


	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setSendPerson(String sendPerson){
		this.sendPerson=sendPerson;
	}

	public String getSendPerson(){
		return this.sendPerson;
	}

	public void setTransportName(String transportName){
		this.transportName=transportName;
	}

	public String getTransportName(){
		return this.transportName;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public String getArrivePlace() {
		return arrivePlace;
	}

	public void setArrivePlace(String arrivePlace) {
		this.arrivePlace = arrivePlace;
	}

	public String getAuditRemark() {
		return auditRemark;
	}

	public void setAuditRemark(String auditRemark) {
		this.auditRemark = auditRemark;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}
}