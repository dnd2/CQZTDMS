/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-11-03 19:09:47
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtViConstructDetailPO extends PO{

	private Long deployId;
	private Integer state;
	private Integer scale;
	private Integer userId;
	private Long dealerId;
	private Date vioverDate;
	private Date supportSdate;
	private String remark;
	private Long createBy;
	private Long detailId;
	private Double amount;
	private Integer status;
	private Date supportEdate;
	private Double yearRent;
	private Long id;
	private Date createDate;
	private Integer yearFlag;
	private Integer supportNumber;

	public void setDeployId(Long deployId){
		this.deployId=deployId;
	}

	public Long getDeployId(){
		return this.deployId;
	}

	public void setState(Integer state){
		this.state=state;
	}

	public Integer getState(){
		return this.state;
	}

	public void setScale(Integer scale){
		this.scale=scale;
	}

	public Integer getScale(){
		return this.scale;
	}

	public void setUserId(Integer userId){
		this.userId=userId;
	}

	public Integer getUserId(){
		return this.userId;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setVioverDate(Date vioverDate){
		this.vioverDate=vioverDate;
	}

	public Date getVioverDate(){
		return this.vioverDate;
	}

	public void setSupportSdate(Date supportSdate){
		this.supportSdate=supportSdate;
	}

	public Date getSupportSdate(){
		return this.supportSdate;
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

	public void setDetailId(Long detailId){
		this.detailId=detailId;
	}

	public Long getDetailId(){
		return this.detailId;
	}

	public void setAmount(Double amount){
		this.amount=amount;
	}

	public Double getAmount(){
		return this.amount;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setSupportEdate(Date supportEdate){
		this.supportEdate=supportEdate;
	}

	public Date getSupportEdate(){
		return this.supportEdate;
	}

	public void setYearRent(Double yearRent){
		this.yearRent=yearRent;
	}

	public Double getYearRent(){
		return this.yearRent;
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

	public void setYearFlag(Integer yearFlag){
		this.yearFlag=yearFlag;
	}

	public Integer getYearFlag(){
		return this.yearFlag;
	}

	public Integer getSupportNumber() {
		return supportNumber;
	}

	public void setSupportNumber(Integer supportNumber) {
		this.supportNumber = supportNumber;
	}

}