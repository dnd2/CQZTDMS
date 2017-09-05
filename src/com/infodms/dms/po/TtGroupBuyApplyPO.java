/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-10-18 12:06:21
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtGroupBuyApplyPO extends PO{

	private Long groupId;
	private Integer state;
	private String customerName;
	private Long dealerId;
	private Long groupbuyNo;
	private Date updateDate;
	private Long createBy;
	private String remark;
	private Long total;
	private Integer status;
	private String describe;
	private String configureType;
	private Long updateBy;
	private String groupName;
	private Long id;
	private Long packageId;
	private Date createDate;
	private String colorName;
	private String contractNo;
	private Integer groupbuyUse;

	public void setGroupId(Long groupId){
		this.groupId=groupId;
	}

	public Long getGroupId(){
		return this.groupId;
	}

	public void setState(Integer state){
		this.state=state;
	}

	public Integer getState(){
		return this.state;
	}

	public void setCustomerName(String customerName){
		this.customerName=customerName;
	}

	public String getCustomerName(){
		return this.customerName;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setGroupbuyNo(Long groupbuyNo){
		this.groupbuyNo=groupbuyNo;
	}

	public Long getGroupbuyNo(){
		return this.groupbuyNo;
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

	public void setTotal(Long total){
		this.total=total;
	}

	public Long getTotal(){
		return this.total;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setDescribe(String describe){
		this.describe=describe;
	}

	public String getDescribe(){
		return this.describe;
	}

	public void setConfigureType(String configureType){
		this.configureType=configureType;
	}

	public String getConfigureType(){
		return this.configureType;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setGroupName(String groupName){
		this.groupName=groupName;
	}

	public String getGroupName(){
		return this.groupName;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setPackageId(Long packageId){
		this.packageId=packageId;
	}

	public Long getPackageId(){
		return this.packageId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setColorName(String colorName){
		this.colorName=colorName;
	}

	public String getColorName(){
		return this.colorName;
	}

	public void setContractNo(String contractNo){
		this.contractNo=contractNo;
	}

	public String getContractNo(){
		return this.contractNo;
	}

	public void setGroupbuyUse(Integer groupbuyUse){
		this.groupbuyUse=groupbuyUse;
	}

	public Integer getGroupbuyUse(){
		return this.groupbuyUse;
	}

}