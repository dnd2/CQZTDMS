/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-11-27 17:08:13
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtSalesLogiIntrevalPO extends PO{

	private Long liId;
	private Integer beginNum;
	private Integer endNum;
	private Integer assDays;
	private Long updateBy;
	private Date updateDate;
	private Long createBy;
	private String remark;
	private Date createDate;
	private Integer status;
	public Long getLiId() {
		return liId;
	}
	public void setLiId(Long liId) {
		this.liId = liId;
	}
	public Integer getBeginNum() {
		return beginNum;
	}
	public void setBeginNum(Integer beginNum) {
		this.beginNum = beginNum;
	}
	public Integer getEndNum() {
		return endNum;
	}
	public void setEndNum(Integer endNum) {
		this.endNum = endNum;
	}
	public Integer getAssDays() {
		return assDays;
	}
	public void setAssDays(Integer assDays) {
		this.assDays = assDays;
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
	public Long getCreateBy() {
		return createBy;
	}
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
}