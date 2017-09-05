/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-02-25 11:48:30
* CreateBy   : wangsongwei
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtSalesOtherCreditPO extends PO{

	private Integer finType;
	private Date endDate;
	private Date updateDate;
	private Long creId;
	private String remark;
	private Long createBy;
	private Double amount;
	private Integer status;
	private Long dealerId;
	private String bankName;
	private String sysStatus;
	private String bankNo;
	private Date startDate;
	private Integer cerType;
	private Long updateBy;
	private Date createDate;

	public void setFinType(Integer finType){
		this.finType=finType;
	}

	public Integer getFinType(){
		return this.finType;
	}

	public void setEndDate(Date endDate){
		this.endDate=endDate;
	}

	public Date getEndDate(){
		return this.endDate;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setCreId(Long creId){
		this.creId=creId;
	}

	public Long getCreId(){
		return this.creId;
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

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setBankName(String bankName){
		this.bankName=bankName;
	}

	public String getBankName(){
		return this.bankName;
	}

	public void setSysStatus(String sysStatus){
		this.sysStatus=sysStatus;
	}

	public String getSysStatus(){
		return this.sysStatus;
	}

	public void setBankNo(String bankNo){
		this.bankNo=bankNo;
	}

	public String getBankNo(){
		return this.bankNo;
	}

	public void setStartDate(Date startDate){
		this.startDate=startDate;
	}

	public Date getStartDate(){
		return this.startDate;
	}

	public void setCerType(Integer cerType){
		this.cerType=cerType;
	}

	public Integer getCerType(){
		return this.cerType;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

}