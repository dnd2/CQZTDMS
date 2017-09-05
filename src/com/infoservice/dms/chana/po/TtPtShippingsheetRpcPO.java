/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-09-04 14:54:20
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infoservice.dms.chana.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPtShippingsheetRpcPO extends PO{
	private Integer signCount;
	private String deliveryPdc;
	private Date updateDate;
	private String shippingCondition;
	private Integer isSigned;
	private Long createBy;
	private Date createDate;
	private String orderNo;
	private String doNo;
	private Date consignmentDate;
	private String deliveryCompany;
	private Long updateBy;
	private String soNo;
	private Integer cartonCount;
	private Date signDate;
	private String signPerson;
	private Integer ifStatus;
	private String remark;
	public void setSignCount(Integer signCount){
		this.signCount=signCount;
	}

	public Integer getSignCount(){
		return this.signCount;
	}

	public void setDeliveryPdc(String deliveryPdc){
		this.deliveryPdc=deliveryPdc;
	}

	public String getDeliveryPdc(){
		return this.deliveryPdc;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public String getShippingCondition() {
		return shippingCondition;
	}

	public void setShippingCondition(String shippingCondition) {
		this.shippingCondition = shippingCondition;
	}

	public void setIsSigned(Integer isSigned){
		this.isSigned=isSigned;
	}

	public Integer getIsSigned(){
		return this.isSigned;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setOrderNo(String orderNo){
		this.orderNo=orderNo;
	}

	public String getOrderNo(){
		return this.orderNo;
	}

	public void setDoNo(String doNo){
		this.doNo=doNo;
	}

	public String getDoNo(){
		return this.doNo;
	}

	public void setConsignmentDate(Date consignmentDate){
		this.consignmentDate=consignmentDate;
	}

	public Date getConsignmentDate(){
		return this.consignmentDate;
	}

	public void setDeliveryCompany(String deliveryCompany){
		this.deliveryCompany=deliveryCompany;
	}

	public String getDeliveryCompany(){
		return this.deliveryCompany;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setSoNo(String soNo){
		this.soNo=soNo;
	}

	public String getSoNo(){
		return this.soNo;
	}

	public void setCartonCount(Integer cartonCount){
		this.cartonCount=cartonCount;
	}

	public Integer getCartonCount(){
		return this.cartonCount;
	}

	public void setSignDate(Date signDate){
		this.signDate=signDate;
	}

	public Date getSignDate(){
		return this.signDate;
	}

	public void setSignPerson(String signPerson){
		this.signPerson=signPerson;
	}

	public String getSignPerson(){
		return this.signPerson;
	}

	public Integer getIfStatus() {
		return ifStatus;
	}

	public void setIfStatus(Integer ifStatus) {
		this.ifStatus = ifStatus;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}