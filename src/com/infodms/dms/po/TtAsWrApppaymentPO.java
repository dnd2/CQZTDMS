/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-07-17 10:48:19
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrApppaymentPO extends PO{

	private Double taxamtOtheritem;
	private Double taxSum;
	private Integer reductionFlag;
	private Date updateDate;
	private Long createBy;
	private String roNo;
	private Long claimId;
	private Date createDate;
	private Double otherlabourAmount;
	private Double otheritemAmount;
	private Long dealerId;
	private Double grossCredit;
	private Double diffreqpaidAmt;
	private Double partsAmount;
	private Double taxamtLabour;
	private Long updateBy;
	private Integer claimType;
	private Double labourAmount;
	private Double taxamtParts;
	private Long lineNo;
	private Double linetotal;
	private Long id;

	public void setTaxamtOtheritem(Double taxamtOtheritem){
		this.taxamtOtheritem=taxamtOtheritem;
	}

	public Double getTaxamtOtheritem(){
		return this.taxamtOtheritem;
	}

	public void setTaxSum(Double taxSum){
		this.taxSum=taxSum;
	}

	public Double getTaxSum(){
		return this.taxSum;
	}

	public void setReductionFlag(Integer reductionFlag){
		this.reductionFlag=reductionFlag;
	}

	public Integer getReductionFlag(){
		return this.reductionFlag;
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

	public void setRoNo(String roNo){
		this.roNo=roNo;
	}

	public String getRoNo(){
		return this.roNo;
	}

	public void setClaimId(Long claimId){
		this.claimId=claimId;
	}

	public Long getClaimId(){
		return this.claimId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setOtherlabourAmount(Double otherlabourAmount){
		this.otherlabourAmount=otherlabourAmount;
	}

	public Double getOtherlabourAmount(){
		return this.otherlabourAmount;
	}

	public void setOtheritemAmount(Double otheritemAmount){
		this.otheritemAmount=otheritemAmount;
	}

	public Double getOtheritemAmount(){
		return this.otheritemAmount;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setGrossCredit(Double grossCredit){
		this.grossCredit=grossCredit;
	}

	public Double getGrossCredit(){
		return this.grossCredit;
	}

	public void setDiffreqpaidAmt(Double diffreqpaidAmt){
		this.diffreqpaidAmt=diffreqpaidAmt;
	}

	public Double getDiffreqpaidAmt(){
		return this.diffreqpaidAmt;
	}

	public void setPartsAmount(Double partsAmount){
		this.partsAmount=partsAmount;
	}

	public Double getPartsAmount(){
		return this.partsAmount;
	}

	public void setTaxamtLabour(Double taxamtLabour){
		this.taxamtLabour=taxamtLabour;
	}

	public Double getTaxamtLabour(){
		return this.taxamtLabour;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setClaimType(Integer claimType){
		this.claimType=claimType;
	}

	public Integer getClaimType(){
		return this.claimType;
	}

	public void setLabourAmount(Double labourAmount){
		this.labourAmount=labourAmount;
	}

	public Double getLabourAmount(){
		return this.labourAmount;
	}

	public void setTaxamtParts(Double taxamtParts){
		this.taxamtParts=taxamtParts;
	}

	public Double getTaxamtParts(){
		return this.taxamtParts;
	}

	

	public Long getLineNo() {
		return lineNo;
	}

	public void setLineNo(Long lineNo) {
		this.lineNo = lineNo;
	}

	public void setLinetotal(Double linetotal){
		this.linetotal=linetotal;
	}

	public Double getLinetotal(){
		return this.linetotal;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

}