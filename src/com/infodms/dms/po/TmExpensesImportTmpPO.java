/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-03-04 09:56:24
* CreateBy   : Andy
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmExpensesImportTmpPO extends PO{

	private Date updateDate;
	private Long expensesId;
	private Integer paymentType;
	private Integer accountingUnits;
	private Long createBy;
	private Long orgId;
	private Date createDate;
	private Integer accountingSubjects;
	private Long brandId;
	private String planName;
	private Integer status;
	private Long updateBy;
	private Integer businessType;
	private String planCode;
	private Double payAmount;
	private String remark;

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setExpensesId(Long expensesId){
		this.expensesId=expensesId;
	}

	public Long getExpensesId(){
		return this.expensesId;
	}

	public void setPaymentType(Integer paymentType){
		this.paymentType=paymentType;
	}

	public Integer getPaymentType(){
		return this.paymentType;
	}

	public void setAccountingUnits(Integer accountingUnits){
		this.accountingUnits=accountingUnits;
	}

	public Integer getAccountingUnits(){
		return this.accountingUnits;
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

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
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

	public void setPlanCode(String planCode){
		this.planCode=planCode;
	}

	public String getPlanCode(){
		return this.planCode;
	}

	public void setPayAmount(Double payAmount){
		this.payAmount=payAmount;
	}

	public Double getPayAmount(){
		return this.payAmount;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

}