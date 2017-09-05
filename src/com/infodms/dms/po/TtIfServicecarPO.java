/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-06-11 08:56:17
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtIfServicecarPO extends PO{

	private Date updateDate;
	private Long groupId;
	private Long createBy;
	private String fax;
	private String orderId;
	private Integer appStatus;
	private Date createDate;
	private Double saleAmount;
	private Long modelId;
	private Long dealerId;
	private String linkMan;
	private String status;
	private Long updateBy;
	private Date appDate;
	private String content;
	private String tel;
	private Integer isDel;
	private String remark;
	private Long id;
	private Long companyId;
	
	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setGroupId(Long groupId){
		this.groupId=groupId;
	}

	public Long getGroupId(){
		return this.groupId;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setFax(String fax){
		this.fax=fax;
	}

	public String getFax(){
		return this.fax;
	}

	public void setOrderId(String orderId){
		this.orderId=orderId;
	}

	public String getOrderId(){
		return this.orderId;
	}

	public void setAppStatus(Integer appStatus){
		this.appStatus=appStatus;
	}

	public Integer getAppStatus(){
		return this.appStatus;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setSaleAmount(Double saleAmount){
		this.saleAmount=saleAmount;
	}

	public Double getSaleAmount(){
		return this.saleAmount;
	}

	public void setModelId(Long modelId){
		this.modelId=modelId;
	}

	public Long getModelId(){
		return this.modelId;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setLinkMan(String linkMan){
		this.linkMan=linkMan;
	}

	public String getLinkMan(){
		return this.linkMan;
	}

	public void setStatus(String status){
		this.status=status;
	}

	public String getStatus(){
		return this.status;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setAppDate(Date appDate){
		this.appDate=appDate;
	}

	public Date getAppDate(){
		return this.appDate;
	}

	public void setContent(String content){
		this.content=content;
	}

	public String getContent(){
		return this.content;
	}

	public void setTel(String tel){
		this.tel=tel;
	}

	public String getTel(){
		return this.tel;
	}

	public void setIsDel(Integer isDel){
		this.isDel=isDel;
	}

	public Integer getIsDel(){
		return this.isDel;
	}

}