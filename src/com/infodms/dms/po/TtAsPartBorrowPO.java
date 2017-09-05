/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-07-23 12:49:55
* CreateBy   : yuewei
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsPartBorrowPO extends PO{

	private String productAddr;
	private String borrowNo;
	private String borrowPerson;
	private Long dealerId;
	private String dealerCode;
	private Long createBy;
	private Integer isDelete;
	private Integer isReturn;
	private String applyDept;
	private Long status;
	private String borrowDept;
	private String consigneePerson;
	private String borrowReason;
	private String consigneePhone;
	private Long id;
	private Date createDate;
	private Date nextTime;
	private String consigneeAddr;
	private String consigneeEmail;
	private String returnNo;
	private  String borrowPhone;
	private Date requireDate;
	private  String remark;
	

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getRequireDate() {
		return requireDate;
	}

	public void setRequireDate(Date requireDate) {
		this.requireDate = requireDate;
	}

	public String getBorrowPhone() {
		return borrowPhone;
	}

	public void setBorrowPhone(String borrowPhone) {
		this.borrowPhone = borrowPhone;
	}

	public String getReturnNo() {
		return returnNo;
	}

	public void setReturnNo(String returnNo) {
		this.returnNo = returnNo;
	}

	public void setProductAddr(String productAddr){
		this.productAddr=productAddr;
	}

	public String getProductAddr(){
		return this.productAddr;
	}

	public void setBorrowNo(String borrowNo){
		this.borrowNo=borrowNo;
	}

	public String getBorrowNo(){
		return this.borrowNo;
	}

	public void setBorrowPerson(String borrowPerson){
		this.borrowPerson=borrowPerson;
	}

	public String getBorrowPerson(){
		return this.borrowPerson;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setDealerCode(String dealerCode){
		this.dealerCode=dealerCode;
	}

	public String getDealerCode(){
		return this.dealerCode;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setIsDelete(Integer isDelete){
		this.isDelete=isDelete;
	}

	public Integer getIsDelete(){
		return this.isDelete;
	}

	public void setApplyDept(String applyDept){
		this.applyDept=applyDept;
	}

	public String getApplyDept(){
		return this.applyDept;
	}

	public void setStatus(Long status){
		this.status=status;
	}

	public Long getStatus(){
		return this.status;
	}

	public void setBorrowDept(String borrowDept){
		this.borrowDept=borrowDept;
	}

	public String getBorrowDept(){
		return this.borrowDept;
	}

	public void setConsigneePerson(String consigneePerson){
		this.consigneePerson=consigneePerson;
	}

	public String getConsigneePerson(){
		return this.consigneePerson;
	}

	public void setBorrowReason(String borrowReason){
		this.borrowReason=borrowReason;
	}

	public String getBorrowReason(){
		return this.borrowReason;
	}

	public void setConsigneePhone(String consigneePhone){
		this.consigneePhone=consigneePhone;
	}

	public String getConsigneePhone(){
		return this.consigneePhone;
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

	public void setConsigneeAddr(String consigneeAddr){
		this.consigneeAddr=consigneeAddr;
	}

	public String getConsigneeAddr(){
		return this.consigneeAddr;
	}

	public void setConsigneeEmail(String consigneeEmail){
		this.consigneeEmail=consigneeEmail;
	}

	public String getConsigneeEmail(){
		return this.consigneeEmail;
	}

	public Date getNextTime() {
		return nextTime;
	}

	public void setNextTime(Date nextTime) {
		this.nextTime = nextTime;
	}

	public Integer getIsReturn() {
		return isReturn;
	}

	public void setIsReturn(Integer isReturn) {
		this.isReturn = isReturn;
	}

}