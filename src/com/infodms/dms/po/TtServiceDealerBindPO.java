/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-10-27 13:50:26
* CreateBy   : wizard_lee
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtServiceDealerBindPO extends PO{

	private Long bindId;
	private Long bindFlag;
	private Long bindDealerId;
	private Integer status;
	private Date createDate;
	private Long createBy;
	private Date updateDate;
	private Long updateBy;
	
	public Long getBindId() {
		return bindId;
	}
	public void setBindId(Long bindId) {
		this.bindId = bindId;
	}
	public Long getBindFlag() {
		return bindFlag;
	}
	public void setBindFlag(Long bindFlag) {
		this.bindFlag = bindFlag;
	}
	public Long getBindDealerId() {
		return bindDealerId;
	}
	public void setBindDealerId(Long bindDealerId) {
		this.bindDealerId = bindDealerId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Long getCreateBy() {
		return createBy;
	}
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public Long getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}
}