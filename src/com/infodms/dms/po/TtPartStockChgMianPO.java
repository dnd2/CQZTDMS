/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2015-05-22 11:29:34
* CreateBy   : Yu
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartStockChgMianPO extends PO{

	private Integer state;
	private String chgCode;
	private String orgName;
	private Date deleteDate;
	private Date updateDate;
	private String remark;
	private Long createBy;
	private Date checkDate;
	private Long chgId;
	private Integer chgType;
	private Long towhId;
	private Integer status;
	private Long whId;
	private Long orgId;
	private Long checkBy;
	private Long submitBy;
	private String checkRemark;
	private Long updateBy;
	private Integer ver;
	private String orgCode;
	private Date createDate;
	private Long deleteBy;
	private Integer flag;
	private Date submitDate;
	
	private String toorgName;
	private String toorgCode;
	private Long toorgId;
	private Integer dodept;
	public Long getBatId() {
		return batId;
	}

	public void setBatId(Long batId) {
		this.batId = batId;
	}

	private Long batId;
	private Double adjustmentSumamount;


	public Double getAdjustmentSumamount() {
		return adjustmentSumamount;
	}

	public void setAdjustmentSumamount(Double adjustmentSumamount) {
		this.adjustmentSumamount = adjustmentSumamount;
	}

	public Integer getDodept() {
		return dodept;
	}

	public void setDodept(Integer dodept) {
		this.dodept = dodept;
	}

//	public Long getBatId() {
//		return batId;
//	}
//
//	public void setBatId(Long batId) {
//		this.batId = batId;
//	}

	public String getToorgName() {
		return toorgName;
	}

	public void setToorgName(String toorgName) {
		this.toorgName = toorgName;
	}

	public String getToorgCode() {
		return toorgCode;
	}

	public void setToorgCode(String toorgCode) {
		this.toorgCode = toorgCode;
	}

	public Long getToorgId() {
		return toorgId;
	}

	public void setToorgId(Long toorgId) {
		this.toorgId = toorgId;
	}

	public void setState(Integer state){
		this.state=state;
	}

	public Integer getState(){
		return this.state;
	}

	public void setChgCode(String chgCode){
		this.chgCode=chgCode;
	}

	public String getChgCode(){
		return this.chgCode;
	}

	public void setOrgName(String orgName){
		this.orgName=orgName;
	}

	public String getOrgName(){
		return this.orgName;
	}

	public void setDeleteDate(Date deleteDate){
		this.deleteDate=deleteDate;
	}

	public Date getDeleteDate(){
		return this.deleteDate;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
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

	public void setCheckDate(Date checkDate){
		this.checkDate=checkDate;
	}

	public Date getCheckDate(){
		return this.checkDate;
	}

	public void setChgId(Long chgId){
		this.chgId=chgId;
	}

	public Long getChgId(){
		return this.chgId;
	}

	public void setChgType(Integer chgType){
		this.chgType=chgType;
	}

	public Integer getChgType(){
		return this.chgType;
	}

	public void setTowhId(Long towhId){
		this.towhId=towhId;
	}

	public Long getTowhId(){
		return this.towhId;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setWhId(Long whId){
		this.whId=whId;
	}

	public Long getWhId(){
		return this.whId;
	}

	public void setOrgId(Long orgId){
		this.orgId=orgId;
	}

	public Long getOrgId(){
		return this.orgId;
	}

	public void setCheckBy(Long checkBy){
		this.checkBy=checkBy;
	}

	public Long getCheckBy(){
		return this.checkBy;
	}

	public void setSubmitBy(Long submitBy){
		this.submitBy=submitBy;
	}

	public Long getSubmitBy(){
		return this.submitBy;
	}

	public void setCheckRemark(String checkRemark){
		this.checkRemark=checkRemark;
	}

	public String getCheckRemark(){
		return this.checkRemark;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setVer(Integer ver){
		this.ver=ver;
	}

	public Integer getVer(){
		return this.ver;
	}

	public void setOrgCode(String orgCode){
		this.orgCode=orgCode;
	}

	public String getOrgCode(){
		return this.orgCode;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setDeleteBy(Long deleteBy){
		this.deleteBy=deleteBy;
	}

	public Long getDeleteBy(){
		return this.deleteBy;
	}

	public void setFlag(Integer flag){
		this.flag=flag;
	}

	public Integer getFlag(){
		return this.flag;
	}

	public void setSubmitDate(Date submitDate){
		this.submitDate=submitDate;
	}

	public Date getSubmitDate(){
		return this.submitDate;
	}

}