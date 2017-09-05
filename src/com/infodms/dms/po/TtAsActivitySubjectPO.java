/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-07-16 15:28:57
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsActivitySubjectPO extends PO{

	private Integer days;
	private Long companyId;
	private Date updateDate;
	private Long subjectId;
	private String remark;
	private Long createBy;
	private Long dutyPerson;
	private Integer isDel;
	private Integer activityNum;
	private Long updateBy;
	private Integer evaluate;
	private Date subjectStartDate;
	private Date createDate;
	private Long newsId;
	private String subjectNo;
	private Date subjectEndDate;
	private String subjectName;
	private Integer activityType;
	private Date factStartDate;
	private Date factEndDate;
	
	public Date getFactStartDate() {
		return factStartDate;
	}

	public void setFactStartDate(Date factStartDate) {
		this.factStartDate = factStartDate;
	}

	public Date getFactEndDate() {
		return factEndDate;
	}

	public void setFactEndDate(Date factEndDate) {
		this.factEndDate = factEndDate;
	}

	public void setDays(Integer days){
		this.days=days;
	}

	public Integer getDays(){
		return this.days;
	}

	public void setCompanyId(Long companyId){
		this.companyId=companyId;
	}

	public Long getCompanyId(){
		return this.companyId;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setSubjectId(Long subjectId){
		this.subjectId=subjectId;
	}

	public Long getSubjectId(){
		return this.subjectId;
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

	public void setDutyPerson(Long dutyPerson){
		this.dutyPerson=dutyPerson;
	}

	public Long getDutyPerson(){
		return this.dutyPerson;
	}

	public void setIsDel(Integer isDel){
		this.isDel=isDel;
	}

	public Integer getIsDel(){
		return this.isDel;
	}

	public void setActivityNum(Integer activityNum){
		this.activityNum=activityNum;
	}

	public Integer getActivityNum(){
		return this.activityNum;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setEvaluate(Integer evaluate){
		this.evaluate=evaluate;
	}

	public Integer getEvaluate(){
		return this.evaluate;
	}

	public void setSubjectStartDate(Date subjectStartDate){
		this.subjectStartDate=subjectStartDate;
	}

	public Date getSubjectStartDate(){
		return this.subjectStartDate;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setNewsId(Long newsId){
		this.newsId=newsId;
	}

	public Long getNewsId(){
		return this.newsId;
	}

	public void setSubjectNo(String subjectNo){
		this.subjectNo=subjectNo;
	}

	public String getSubjectNo(){
		return this.subjectNo;
	}

	public void setSubjectEndDate(Date subjectEndDate){
		this.subjectEndDate=subjectEndDate;
	}

	public Date getSubjectEndDate(){
		return this.subjectEndDate;
	}

	public void setSubjectName(String subjectName){
		this.subjectName=subjectName;
	}

	public String getSubjectName(){
		return this.subjectName;
	}

	public void setActivityType(Integer activityType){
		this.activityType=activityType;
	}

	public Integer getActivityType(){
		return this.activityType;
	}

}