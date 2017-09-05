/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2011-07-05 15:35:23
* CreateBy   : Iverson
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsChangePriceDetailPO extends PO{

	private Long groupId;
	private Date policyEndDate;
	private Long dealerId;
	private Date updateDate;
	private Long createBy;
	private Double oldLabourPrice;
	private Long status;
	private Double changeLabourPrice;
	private Long updateBy;
	private Long id;
	private Long labourId;
	private Double newLabourPrice;
	private Date createDate;
	private Date policyStartDate;

	public void setGroupId(Long groupId){
		this.groupId=groupId;
	}

	public Long getGroupId(){
		return this.groupId;
	}

	public void setPolicyEndDate(Date policyEndDate){
		this.policyEndDate=policyEndDate;
	}

	public Date getPolicyEndDate(){
		return this.policyEndDate;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
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

	public void setOldLabourPrice(Double oldLabourPrice){
		this.oldLabourPrice=oldLabourPrice;
	}

	public Double getOldLabourPrice(){
		return this.oldLabourPrice;
	}

	public void setStatus(Long status){
		this.status=status;
	}

	public Long getStatus(){
		return this.status;
	}

	public void setChangeLabourPrice(Double changeLabourPrice){
		this.changeLabourPrice=changeLabourPrice;
	}

	public Double getChangeLabourPrice(){
		return this.changeLabourPrice;
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

	public void setLabourId(Long labourId){
		this.labourId=labourId;
	}

	public Long getLabourId(){
		return this.labourId;
	}

	public void setNewLabourPrice(Double newLabourPrice){
		this.newLabourPrice=newLabourPrice;
	}

	public Double getNewLabourPrice(){
		return this.newLabourPrice;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setPolicyStartDate(Date policyStartDate){
		this.policyStartDate=policyStartDate;
	}

	public Date getPolicyStartDate(){
		return this.policyStartDate;
	}

}