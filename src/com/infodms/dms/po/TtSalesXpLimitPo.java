/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-03-04 12:36:20
* CreateBy   : -Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtSalesXpLimitPo extends PO{

	private Long xpLimitId;
	
	private Long dealerId;
	
	private Long xpDetailId;
	
	private Integer status;
	
	private Long createBy;
	private Date createDate;
	private Long updateBy;
	private Date updateDate;
	public Long getXpLimitId() {
		return xpLimitId;
	}
	public void setXpLimitId(Long xpLimitId) {
		this.xpLimitId = xpLimitId;
	}
	public Long getDealerId() {
		return dealerId;
	}
	public void setDealerId(Long dealerId) {
		this.dealerId = dealerId;
	}
	public Long getXpDetailId() {
		return xpDetailId;
	}
	public void setXpDetailId(Long xpDetailId) {
		this.xpDetailId = xpDetailId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
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
	public Long getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	} 

	
}