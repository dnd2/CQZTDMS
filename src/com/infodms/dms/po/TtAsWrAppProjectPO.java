/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2017-08-28 17:14:01
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrAppProjectPO extends PO{

	private Float hoursSettlementAmount;
	private Long appClaimId;
	private Integer paymentMethod;
	private Long createBy;
	private Float hoursApplyAmount;
	private String labourCode;
	private Float labourClaimHour;
	private Float labourHour;
	private String customerProblem;
	private Double labourPrice;
	private Long labourId;
	private String cnDes;
	private Date createDate;
	private Long claimProjectId;
	private Float labourSettlementHour;
	private Float hoursClaimAmount;

	public void setHoursSettlementAmount(Float hoursSettlementAmount){
		this.hoursSettlementAmount=hoursSettlementAmount;
	}

	public Float getHoursSettlementAmount(){
		return this.hoursSettlementAmount;
	}

	public void setAppClaimId(Long appClaimId){
		this.appClaimId=appClaimId;
	}

	public Long getAppClaimId(){
		return this.appClaimId;
	}

	public void setPaymentMethod(Integer paymentMethod){
		this.paymentMethod=paymentMethod;
	}

	public Integer getPaymentMethod(){
		return this.paymentMethod;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setHoursApplyAmount(Float hoursApplyAmount){
		this.hoursApplyAmount=hoursApplyAmount;
	}

	public Float getHoursApplyAmount(){
		return this.hoursApplyAmount;
	}

	public void setLabourCode(String labourCode){
		this.labourCode=labourCode;
	}

	public String getLabourCode(){
		return this.labourCode;
	}

	public void setLabourClaimHour(Float labourClaimHour){
		this.labourClaimHour=labourClaimHour;
	}

	public Float getLabourClaimHour(){
		return this.labourClaimHour;
	}

	public void setLabourHour(Float labourHour){
		this.labourHour=labourHour;
	}

	public Float getLabourHour(){
		return this.labourHour;
	}

	public void setCustomerProblem(String customerProblem){
		this.customerProblem=customerProblem;
	}

	public String getCustomerProblem(){
		return this.customerProblem;
	}

	public void setLabourPrice(Double labourPrice){
		this.labourPrice=labourPrice;
	}

	public Double getLabourPrice(){
		return this.labourPrice;
	}

	public void setLabourId(Long labourId){
		this.labourId=labourId;
	}

	public Long getLabourId(){
		return this.labourId;
	}

	public void setCnDes(String cnDes){
		this.cnDes=cnDes;
	}

	public String getCnDes(){
		return this.cnDes;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setClaimProjectId(Long claimProjectId){
		this.claimProjectId=claimProjectId;
	}

	public Long getClaimProjectId(){
		return this.claimProjectId;
	}

	public void setLabourSettlementHour(Float labourSettlementHour){
		this.labourSettlementHour=labourSettlementHour;
	}

	public Float getLabourSettlementHour(){
		return this.labourSettlementHour;
	}

	public void setHoursClaimAmount(Float hoursClaimAmount){
		this.hoursClaimAmount=hoursClaimAmount;
	}

	public Float getHoursClaimAmount(){
		return this.hoursClaimAmount;
	}

}