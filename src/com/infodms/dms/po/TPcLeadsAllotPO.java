/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-11-04 10:55:33
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TPcLeadsAllotPO extends PO{

	private String customerName;
	private Date allotAdviserDate;
	private String dealerId;
	private Long oldLeadsCode;
	private String remark;
	private String createBy;
	private Integer status;
	private String telephone;
	private String adviser;
	private Integer allotAgain;
	private Long leadsAllotId;
	private Date allotDealerDate;
	private Date createDate;
	private Long leadsCode;
	private Integer ifConfirm;
	private Long customerId;
	private Date confirmDate;
	private String sex;
	private Long isRepeat;
	private Date updateDate;
	private Long updateBy;
	
	
	

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Long getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}

	public Long getIsRepeat() {
		return isRepeat;
	}

	public void setIsRepeat(Long isRepeat) {
		this.isRepeat = isRepeat;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Date getConfirmDate() {
		return confirmDate;
	}

	public void setConfirmDate(Date confirmDate) {
		this.confirmDate = confirmDate;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Integer getIfConfirm() {
		return ifConfirm;
	}

	public void setIfConfirm(Integer ifConfirm) {
		this.ifConfirm = ifConfirm;
	}

	public void setCustomerName(String customerName){
		this.customerName=customerName;
	}

	public String getCustomerName(){
		return this.customerName;
	}

	public void setAllotAdviserDate(Date allotAdviserDate){
		this.allotAdviserDate=allotAdviserDate;
	}

	public Date getAllotAdviserDate(){
		return this.allotAdviserDate;
	}

	public void setDealerId(String dealerId){
		this.dealerId=dealerId;
	}

	public String getDealerId(){
		return this.dealerId;
	}

	public void setOldLeadsCode(Long oldLeadsCode){
		this.oldLeadsCode=oldLeadsCode;
	}

	public Long getOldLeadsCode(){
		return this.oldLeadsCode;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setCreateBy(String createBy){
		this.createBy=createBy;
	}

	public String getCreateBy(){
		return this.createBy;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setTelephone(String telephone){
		this.telephone=telephone;
	}

	public String getTelephone(){
		return this.telephone;
	}

	public void setAdviser(String adviser){
		this.adviser=adviser;
	}

	public String getAdviser(){
		return this.adviser;
	}

	public void setAllotAgain(Integer allotAgain){
		this.allotAgain=allotAgain;
	}

	public Integer getAllotAgain(){
		return this.allotAgain;
	}

	public void setLeadsAllotId(Long leadsAllotId){
		this.leadsAllotId=leadsAllotId;
	}

	public Long getLeadsAllotId(){
		return this.leadsAllotId;
	}

	public void setAllotDealerDate(Date allotDealerDate){
		this.allotDealerDate=allotDealerDate;
	}

	public Date getAllotDealerDate(){
		return this.allotDealerDate;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setLeadsCode(Long leadsCode){
		this.leadsCode=leadsCode;
	}

	public Long getLeadsCode(){
		return this.leadsCode;
	}

}