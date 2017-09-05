/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-04-10 17:14:30
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtRebateDetailImportPO extends PO{

	private Long batchNum;
	private Integer detailId;
	private String dealerCode;
	private String finReturnTypeCode;
	private Double amount;
	private Date finMonth;
	private Long isTrue;
	private Long createBy;
	private Date createDate;
	public Long getBatchNum() {
		return batchNum;
	}
	public void setBatchNum(Long batchNum) {
		this.batchNum = batchNum;
	}
	
	public Integer getDetailId() {
		return detailId;
	}
	public void setDetailId(Integer detailId) {
		this.detailId = detailId;
	}
	public String getDealerCode() {
		return dealerCode;
	}
	public void setDealerCode(String dealerCode) {
		this.dealerCode = dealerCode;
	}
	public String getFinReturnTypeCode() {
		return finReturnTypeCode;
	}
	public void setFinReturnTypeCode(String finReturnTypeCode) {
		this.finReturnTypeCode = finReturnTypeCode;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Date getFinMonth() {
		return finMonth;
	}
	public void setFinMonth(Date finMonth) {
		this.finMonth = finMonth;
	}
	public Long getIsTrue() {
		return isTrue;
	}
	public void setIsTrue(Long isTrue) {
		this.isTrue = isTrue;
	}
	public Long getCreateBy() {
		return createBy;
	}
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
}