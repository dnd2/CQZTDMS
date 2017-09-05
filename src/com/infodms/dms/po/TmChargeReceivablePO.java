/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-03-04 09:51:59
* CreateBy   : Andy
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmChargeReceivablePO extends PO{

	private String referenceNo;
	private String promotionCode;
	private String paymentTypeCode;
	private Date updateDate;
	private Integer accountingUnits;
	private Integer paymentType;
	private String printTitle;
	private Long createBy;
	private Long orgId;
	private String chVoucherNo;
	private String fkVoucherNo;
	private Date createDate;
	private String gkVoucherNo;
	private Integer accountingSubjects;
	private Long brandId;
	private String planName;
	private Integer status;
	private Long chargeId;
	private Long updateBy;
	private Integer businessType;
	private Date signDate;
	private String planCode;
	private String remark;
	private Double payAmount;

	public void setReferenceNo(String referenceNo){
		this.referenceNo=referenceNo;
	}

	public String getReferenceNo(){
		return this.referenceNo;
	}

	public void setPromotionCode(String promotionCode){
		this.promotionCode=promotionCode;
	}

	public String getPromotionCode(){
		return this.promotionCode;
	}

	public void setPaymentTypeCode(String paymentTypeCode){
		this.paymentTypeCode=paymentTypeCode;
	}

	public String getPaymentTypeCode(){
		return this.paymentTypeCode;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setAccountingUnits(Integer accountingUnits){
		this.accountingUnits=accountingUnits;
	}

	public Integer getAccountingUnits(){
		return this.accountingUnits;
	}

	public void setPaymentType(Integer paymentType){
		this.paymentType=paymentType;
	}

	public Integer getPaymentType(){
		return this.paymentType;
	}

	public void setPrintTitle(String printTitle){
		this.printTitle=printTitle;
	}

	public String getPrintTitle(){
		return this.printTitle;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setOrgId(Long orgId){
		this.orgId=orgId;
	}

	public Long getOrgId(){
		return this.orgId;
	}

	public void setChVoucherNo(String chVoucherNo){
		this.chVoucherNo=chVoucherNo;
	}

	public String getChVoucherNo(){
		return this.chVoucherNo;
	}

	public void setFkVoucherNo(String fkVoucherNo){
		this.fkVoucherNo=fkVoucherNo;
	}

	public String getFkVoucherNo(){
		return this.fkVoucherNo;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setGkVoucherNo(String gkVoucherNo){
		this.gkVoucherNo=gkVoucherNo;
	}

	public String getGkVoucherNo(){
		return this.gkVoucherNo;
	}

	public void setAccountingSubjects(Integer accountingSubjects){
		this.accountingSubjects=accountingSubjects;
	}

	public Integer getAccountingSubjects(){
		return this.accountingSubjects;
	}

	public void setBrandId(Long brandId){
		this.brandId=brandId;
	}

	public Long getBrandId(){
		return this.brandId;
	}

	public void setPlanName(String planName){
		this.planName=planName;
	}

	public String getPlanName(){
		return this.planName;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setChargeId(Long chargeId){
		this.chargeId=chargeId;
	}

	public Long getChargeId(){
		return this.chargeId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setBusinessType(Integer businessType){
		this.businessType=businessType;
	}

	public Integer getBusinessType(){
		return this.businessType;
	}

	public void setSignDate(Date signDate){
		this.signDate=signDate;
	}

	public Date getSignDate(){
		return this.signDate;
	}

	public void setPlanCode(String planCode){
		this.planCode=planCode;
	}

	public String getPlanCode(){
		return this.planCode;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setPayAmount(Double payAmount){
		this.payAmount=payAmount;
	}

	public Double getPayAmount(){
		return this.payAmount;
	}

}