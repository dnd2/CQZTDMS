/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2015-01-16 10:11:47
* CreateBy   : wizard_lee
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtSalesMarketingFeeSdtlPO extends PO{

	private String mediaName;
	private Double feeUnit;
	private Integer sdtlTypeId;
	private Date endDate;
	private Integer displayNumber;
	private Double squre;
	private String remark;
	private Integer personNumber;
	private Double auditAmount;
	private Long dtlId;
	private Date startDate;
	private String place;
	private Double auditSplusAmount;
	private String placetimeSection;
	private Long sdtlId;
	private Integer feeTypeId;
	private Double submitAmount;
	private Double actualAmount;
	private Double miles;
	private Double disRate;

	public void setMediaName(String mediaName){
		this.mediaName=mediaName;
	}

	public String getMediaName(){
		return this.mediaName;
	}

	public void setFeeUnit(Double feeUnit){
		this.feeUnit=feeUnit;
	}

	public Double getFeeUnit(){
		return this.feeUnit;
	}

	public void setSdtlTypeId(Integer sdtlTypeId){
		this.sdtlTypeId=sdtlTypeId;
	}

	public Integer getSdtlTypeId(){
		return this.sdtlTypeId;
	}

	public void setEndDate(Date endDate){
		this.endDate=endDate;
	}

	public Date getEndDate(){
		return this.endDate;
	}

	public void setDisplayNumber(Integer displayNumber){
		this.displayNumber=displayNumber;
	}

	public Integer getDisplayNumber(){
		return this.displayNumber;
	}

	public void setSqure(Double squre){
		this.squre=squre;
	}

	public Double getSqure(){
		return this.squre;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setPersonNumber(Integer personNumber){
		this.personNumber=personNumber;
	}

	public Integer getPersonNumber(){
		return this.personNumber;
	}

	public void setAuditAmount(Double auditAmount){
		this.auditAmount=auditAmount;
	}

	public Double getAuditAmount(){
		return this.auditAmount;
	}

	public void setDtlId(Long dtlId){
		this.dtlId=dtlId;
	}

	public Long getDtlId(){
		return this.dtlId;
	}

	public void setStartDate(Date startDate){
		this.startDate=startDate;
	}

	public Date getStartDate(){
		return this.startDate;
	}

	public void setPlace(String place){
		this.place=place;
	}

	public String getPlace(){
		return this.place;
	}

	public void setAuditSplusAmount(Double auditSplusAmount){
		this.auditSplusAmount=auditSplusAmount;
	}

	public Double getAuditSplusAmount(){
		return this.auditSplusAmount;
	}

	public void setPlacetimeSection(String placetimeSection){
		this.placetimeSection=placetimeSection;
	}

	public String getPlacetimeSection(){
		return this.placetimeSection;
	}

	public void setSdtlId(Long sdtlId){
		this.sdtlId=sdtlId;
	}

	public Long getSdtlId(){
		return this.sdtlId;
	}

	public void setFeeTypeId(Integer feeTypeId){
		this.feeTypeId=feeTypeId;
	}

	public Integer getFeeTypeId(){
		return this.feeTypeId;
	}

	public void setSubmitAmount(Double submitAmount){
		this.submitAmount=submitAmount;
	}

	public Double getSubmitAmount(){
		return this.submitAmount;
	}

	public void setActualAmount(Double actualAmount){
		this.actualAmount=actualAmount;
	}

	public Double getActualAmount(){
		return this.actualAmount;
	}

	public void setMiles(Double miles){
		this.miles=miles;
	}

	public Double getMiles(){
		return this.miles;
	}

	public void setDisRate(Double disRate){
		this.disRate=disRate;
	}

	public Double getDisRate(){
		return this.disRate;
	}

}