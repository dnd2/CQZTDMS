/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-06-06 11:14:35
* CreateBy   : Yu
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartAccountDefinePO extends PO{

	private Integer state;
	private Date disableDate;
	private Long accountId;
	private Date deleteDate;
	private Long childorgId;
	private Date updateDate;
	private Long createBy;
	private Integer status;
	private Integer accountPurpose;
	private Long updateBy;
	private Long parentorgId;
	private Integer accountKind;
	private Double creditLimit;
	private Double accountSum;
	private Date createDate;
	private Long disableBy;
	private Long deleteBy;

	public void setState(Integer state){
		this.state=state;
	}

	public Integer getState(){
		return this.state;
	}

	public void setDisableDate(Date disableDate){
		this.disableDate=disableDate;
	}

	public Date getDisableDate(){
		return this.disableDate;
	}

	public void setAccountId(Long accountId){
		this.accountId=accountId;
	}

	public Long getAccountId(){
		return this.accountId;
	}

	public void setDeleteDate(Date deleteDate){
		this.deleteDate=deleteDate;
	}

	public Date getDeleteDate(){
		return this.deleteDate;
	}

	public void setChildorgId(Long childorgId){
		this.childorgId=childorgId;
	}

	public Long getChildorgId(){
		return this.childorgId;
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

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setAccountPurpose(Integer accountPurpose){
		this.accountPurpose=accountPurpose;
	}

	public Integer getAccountPurpose(){
		return this.accountPurpose;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setParentorgId(Long parentorgId){
		this.parentorgId=parentorgId;
	}

	public Long getParentorgId(){
		return this.parentorgId;
	}

	public void setAccountKind(Integer accountKind){
		this.accountKind=accountKind;
	}

	public Integer getAccountKind(){
		return this.accountKind;
	}

	public void setCreditLimit(Double creditLimit){
		this.creditLimit=creditLimit;
	}

	public Double getCreditLimit(){
		return this.creditLimit;
	}

	public void setAccountSum(Double accountSum){
		this.accountSum=accountSum;
	}

	public Double getAccountSum(){
		return this.accountSum;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setDisableBy(Long disableBy){
		this.disableBy=disableBy;
	}

	public Long getDisableBy(){
		return this.disableBy;
	}

	public void setDeleteBy(Long deleteBy){
		this.deleteBy=deleteBy;
	}

	public Long getDeleteBy(){
		return this.deleteBy;
	}

}