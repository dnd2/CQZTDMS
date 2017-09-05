/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-10-04 12:33:50
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsAccountLogPO extends PO{

	private Date opdate;
	private Long accountId;
	private Double oBalanceAmount;
	private Double oAvailableAmount;
	private Long freezeId;
	private Double nFreezeAmount;
	private Double oFreezeAmount;
	private Long userId;
	private Double nBalanceAmount;
	private Double nAvailableAmount;
	private Double tFreezeAmount;
	private Long operateGroupId;
	private Long logId;
	private Double changeAmount;
	
	
	public void calculateChangeAmount(){
		this.changeAmount = nAvailableAmount - oAvailableAmount;
	}
	
	
	public Long getOperateGroupId() {
		return operateGroupId;
	}
	public void setOperateGroupId(Long operateGroupId) {
		this.operateGroupId = operateGroupId;
	}
	public Date getOpdate() {
		return opdate;
	}
	public void setOpdate(Date opdate) {
		this.opdate = opdate;
	}
	public Long getAccountId() {
		return accountId;
	}
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
	public Double getoBalanceAmount() {
		return oBalanceAmount;
	}
	public void setoBalanceAmount(Double oBalanceAmount) {
		this.oBalanceAmount = oBalanceAmount;
	}
	public Double getoAvailableAmount() {
		return oAvailableAmount;
	}
	public void setoAvailableAmount(Double oAvailableAmount) {
		this.oAvailableAmount = oAvailableAmount;
	}
	public Long getFreezeId() {
		return freezeId;
	}
	public void setFreezeId(Long freezeId) {
		this.freezeId = freezeId;
	}
	public Double getnFreezeAmount() {
		return nFreezeAmount;
	}
	public void setnFreezeAmount(Double nFreezeAmount) {
		this.nFreezeAmount = nFreezeAmount;
	}
	public Double getoFreezeAmount() {
		return oFreezeAmount;
	}
	public void setoFreezeAmount(Double oFreezeAmount) {
		this.oFreezeAmount = oFreezeAmount;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Double getnBalanceAmount() {
		return nBalanceAmount;
	}
	public void setnBalanceAmount(Double nBalanceAmount) {
		this.nBalanceAmount = nBalanceAmount;
	}
	public Double getnAvailableAmount() {
		return nAvailableAmount;
	}
	public void setnAvailableAmount(Double nAvailableAmount) {
		this.nAvailableAmount = nAvailableAmount;
	}
	public Double gettFreezeAmount() {
		return tFreezeAmount;
	}
	public void settFreezeAmount(Double tFreezeAmount) {
		this.tFreezeAmount = tFreezeAmount;
	}
	public void setLogId(Long logId) {
		this.logId = logId;
	}
	public Long getLogId() {
		return logId;
	}
	public void setChangeAmount(Double changeAmount) {
		this.changeAmount = changeAmount;
	}
	public Double getChangeAmount() {
		return changeAmount;
	}

	
}