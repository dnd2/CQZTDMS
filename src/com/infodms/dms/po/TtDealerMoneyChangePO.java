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
public class TtDealerMoneyChangePO extends PO{

	private Long changeId;
	private Long dealerId;
	private Long inDetailId;
	private Long outDetailId;
	private Long finInType;
	private Long inAccId;
	private Long createBy;
	private Long outAccId;
	private Date createDate;
	private Date ticketDate;
	private Date expireDate;
	private String remark;
	private String inBankName;
	private String inBankCode;
	private String inTicketNo;
	private Double inAmount;
	private Double outAmount;
	public Long getChangeId() {
		return changeId;
	}
	public void setChangeId(Long changeId) {
		this.changeId = changeId;
	}
	public Long getDealerId() {
		return dealerId;
	}
	public void setDealerId(Long dealerId) {
		this.dealerId = dealerId;
	}
	public Long getInDetailId() {
		return inDetailId;
	}
	public void setInDetailId(Long inDetailId) {
		this.inDetailId = inDetailId;
	}
	public Long getOutDetailId() {
		return outDetailId;
	}
	public void setOutDetailId(Long outDetailId) {
		this.outDetailId = outDetailId;
	}
	public Long getFinInType() {
		return finInType;
	}
	public void setFinInType(Long finInType) {
		this.finInType = finInType;
	}
	public Long getInAccId() {
		return inAccId;
	}
	public void setInAccId(Long inAccId) {
		this.inAccId = inAccId;
	}
	public Long getCreateBy() {
		return createBy;
	}
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}
	public Long getOutAccId() {
		return outAccId;
	}
	public void setOutAccId(Long outAccId) {
		this.outAccId = outAccId;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getTicketDate() {
		return ticketDate;
	}
	public void setTicketDate(Date ticketDate) {
		this.ticketDate = ticketDate;
	}
	public Date getExpireDate() {
		return expireDate;
	}
	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getInBankName() {
		return inBankName;
	}
	public void setInBankName(String inBankName) {
		this.inBankName = inBankName;
	}
	public String getInBankCode() {
		return inBankCode;
	}
	public void setInBankCode(String inBankCode) {
		this.inBankCode = inBankCode;
	}
	public String getInTicketNo() {
		return inTicketNo;
	}
	public void setInTicketNo(String inTicketNo) {
		this.inTicketNo = inTicketNo;
	}
	public Double getInAmount() {
		return inAmount;
	}
	public void setInAmount(Double inAmount) {
		this.inAmount = inAmount;
	}
	public Double getOutAmount() {
		return outAmount;
	}
	public void setOutAmount(Double outAmount) {
		this.outAmount = outAmount;
	}
	
}