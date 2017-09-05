/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2017-07-21 15:11:05
* CreateBy   : zhumingwei
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartPlanScrollPO extends PO{

	private String monthDate;
	private Date updateDate;
	private Long createBy;
	private String remark;
	private Integer planType;
	private Integer status;
	private Integer createType;
	private String planNo;
	private Integer planTypes;
	private Long submitBy;
	private Long updateBy;
	private Long id;
	private Date createDate;
	private Integer isSubmit;
	private Date submitDate;
	private Date yearMonth;
	
	public Long getShBy() {
		return shBy;
	}

	public void setShBy(Long shBy) {
		this.shBy = shBy;
	}

	public Date getShDate() {
		return shDate;
	}

	public void setShDate(Date shDate) {
		this.shDate = shDate;
	}

	private Long shBy;
	private Date shDate;

	public void setMonthDate(String monthDate){
		this.monthDate=monthDate;
	}

	public String getMonthDate(){
		return this.monthDate;
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

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setPlanType(Integer planType){
		this.planType=planType;
	}

	public Integer getPlanType(){
		return this.planType;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setCreateType(Integer createType){
		this.createType=createType;
	}

	public Integer getCreateType(){
		return this.createType;
	}

	public void setPlanNo(String planNo){
		this.planNo=planNo;
	}

	public String getPlanNo(){
		return this.planNo;
	}

	public void setPlanTypes(Integer planTypes){
		this.planTypes=planTypes;
	}

	public Integer getPlanTypes(){
		return this.planTypes;
	}

	public void setSubmitBy(Long submitBy){
		this.submitBy=submitBy;
	}

	public Long getSubmitBy(){
		return this.submitBy;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setIsSubmit(Integer isSubmit){
		this.isSubmit=isSubmit;
	}

	public Integer getIsSubmit(){
		return this.isSubmit;
	}

	public void setSubmitDate(Date submitDate){
		this.submitDate=submitDate;
	}

	public Date getSubmitDate(){
		return this.submitDate;
	}

	public void setYearMonth(Date yearMonth){
		this.yearMonth=yearMonth;
	}

	public Date getYearMonth(){
		return this.yearMonth;
	}

}