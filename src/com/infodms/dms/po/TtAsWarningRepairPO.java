/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-04-24 02:16:18
* CreateBy   : zhuwei
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWarningRepairPO extends PO{

	private Date updateDate;
	private Integer warningNumStart;
	private String warningCode;
	private Long createBy;
	private Integer warningType;
	private String clauseStatute;
	private Date createDate;
	private String pprovalLeverCode;
	private String wainingRemark;
	private Integer wainingLevel;
	private Integer status;
	private Integer isAccumulative;
	private Long updateBy;
	private String remark1;
	private Integer validMileage;
	private Long warningRepairId;
	private Integer validDate;
	private String remark;
	private Integer warningNumEnd;
	private Integer validStartDate;
	private Integer validStartMileage;



	public Integer getValidStartDate() {
		return validStartDate;
	}

	public void setValidStartDate(Integer validStartDate) {
		this.validStartDate = validStartDate;
	}

	public Integer getValidStartMileage() {
		return validStartMileage;
	}

	public void setValidStartMileage(Integer validStartMileage) {
		this.validStartMileage = validStartMileage;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setWarningNumStart(Integer warningNumStart){
		this.warningNumStart=warningNumStart;
	}

	public Integer getWarningNumStart(){
		return this.warningNumStart;
	}

	public void setWarningCode(String warningCode){
		this.warningCode=warningCode;
	}

	public String getWarningCode(){
		return this.warningCode;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setWarningType(Integer warningType){
		this.warningType=warningType;
	}

	public Integer getWarningType(){
		return this.warningType;
	}

	public void setClauseStatute(String clauseStatute){
		this.clauseStatute=clauseStatute;
	}

	public String getClauseStatute(){
		return this.clauseStatute;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setPprovalLeverCode(String pprovalLeverCode){
		this.pprovalLeverCode=pprovalLeverCode;
	}

	public String getPprovalLeverCode(){
		return this.pprovalLeverCode;
	}

	public void setWainingRemark(String wainingRemark){
		this.wainingRemark=wainingRemark;
	}

	public String getWainingRemark(){
		return this.wainingRemark;
	}

	public void setWainingLevel(Integer wainingLevel){
		this.wainingLevel=wainingLevel;
	}

	public Integer getWainingLevel(){
		return this.wainingLevel;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setIsAccumulative(Integer isAccumulative){
		this.isAccumulative=isAccumulative;
	}

	public Integer getIsAccumulative(){
		return this.isAccumulative;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setRemark1(String remark1){
		this.remark1=remark1;
	}

	public String getRemark1(){
		return this.remark1;
	}

	public void setValidMileage(Integer validMileage){
		this.validMileage=validMileage;
	}

	public Integer getValidMileage(){
		return this.validMileage;
	}

	public void setWarningRepairId(Long warningRepairId){
		this.warningRepairId=warningRepairId;
	}

	public Long getWarningRepairId(){
		return this.warningRepairId;
	}

	public void setValidDate(Integer validDate){
		this.validDate=validDate;
	}

	public Integer getValidDate(){
		return this.validDate;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setWarningNumEnd(Integer warningNumEnd){
		this.warningNumEnd=warningNumEnd;
	}

	public Integer getWarningNumEnd(){
		return this.warningNumEnd;
	}

}