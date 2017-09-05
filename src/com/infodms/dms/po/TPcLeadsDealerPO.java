/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2015-04-09 09:34:58
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TPcLeadsDealerPO extends PO{

	private String customerName;
	private String dealerId;
	private String area;
	private String failureRemark;
	private String customerDescribe;
	private String createBy;
	private String remark;
	private Date leaveDate;
	private String defaultRemark;
	private String telephone;
	private Integer leadsType;
	private Date comeDate;
	private String leadsOrigin;
	private String province;
	private String rowNumber;
	private String collectDate;
	private Date createDate;
	private String city;
	private String adviser;
	
	
	
	public String getAdviser() {
		return adviser;
	}

	public void setAdviser(String adviser) {
		this.adviser = adviser;
	}

	public void setCustomerName(String customerName){
		this.customerName=customerName;
	}

	public String getCustomerName(){
		return this.customerName;
	}

	public void setDealerId(String dealerId){
		this.dealerId=dealerId;
	}

	public String getDealerId(){
		return this.dealerId;
	}

	public void setArea(String area){
		this.area=area;
	}

	public String getArea(){
		return this.area;
	}

	public void setFailureRemark(String failureRemark){
		this.failureRemark=failureRemark;
	}

	public String getFailureRemark(){
		return this.failureRemark;
	}

	public void setCustomerDescribe(String customerDescribe){
		this.customerDescribe=customerDescribe;
	}

	public String getCustomerDescribe(){
		return this.customerDescribe;
	}

	public void setCreateBy(String createBy){
		this.createBy=createBy;
	}

	public String getCreateBy(){
		return this.createBy;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setLeaveDate(Date leaveDate){
		this.leaveDate=leaveDate;
	}

	public Date getLeaveDate(){
		return this.leaveDate;
	}

	public void setDefaultRemark(String defaultRemark){
		this.defaultRemark=defaultRemark;
	}

	public String getDefaultRemark(){
		return this.defaultRemark;
	}

	public void setTelephone(String telephone){
		this.telephone=telephone;
	}

	public String getTelephone(){
		return this.telephone;
	}

	public void setLeadsType(Integer leadsType){
		this.leadsType=leadsType;
	}

	public Integer getLeadsType(){
		return this.leadsType;
	}

	public void setComeDate(Date comeDate){
		this.comeDate=comeDate;
	}

	public Date getComeDate(){
		return this.comeDate;
	}

	public void setLeadsOrigin(String leadsOrigin){
		this.leadsOrigin=leadsOrigin;
	}

	public String getLeadsOrigin(){
		return this.leadsOrigin;
	}

	public void setProvince(String province){
		this.province=province;
	}

	public String getProvince(){
		return this.province;
	}

	public void setRowNumber(String rowNumber){
		this.rowNumber=rowNumber;
	}

	public String getRowNumber(){
		return this.rowNumber;
	}

	public void setCollectDate(String collectDate){
		this.collectDate=collectDate;
	}

	public String getCollectDate(){
		return this.collectDate;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setCity(String city){
		this.city=city;
	}

	public String getCity(){
		return this.city;
	}

}