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
public class TtVsDispatchOrderDtlPO extends PO{

	private Long detailId;
	private Long dispId;
	private Long materialId;
	private Integer orderAmount;
	private Integer deliveryAmount;
	private Integer boardNumber;
	private Integer accAmount;
	private Long createBy;
	private Date createDate;
	private Long updateBy;
	private Date updateDate;
	private Long sendWareId;
	
	
	public Long getSendWareId() {
		return sendWareId;
	}
	public void setSendWareId(Long sendWareId) {
		this.sendWareId = sendWareId;
	}
	public Long getDetailId() {
		return detailId;
	}
	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}
	public Long getDispId() {
		return dispId;
	}
	public void setDispId(Long dispId) {
		this.dispId = dispId;
	}
	public Long getMaterialId() {
		return materialId;
	}
	public void setMaterialId(Long materialId) {
		this.materialId = materialId;
	}
	public Integer getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(Integer orderAmount) {
		this.orderAmount = orderAmount;
	}
	public Integer getDeliveryAmount() {
		return deliveryAmount;
	}
	public void setDeliveryAmount(Integer deliveryAmount) {
		this.deliveryAmount = deliveryAmount;
	}
	
	public Integer getBoardNumber() {
		return boardNumber;
	}
	public void setBoardNumber(Integer boardNumber) {
		this.boardNumber = boardNumber;
	}
	public Integer getAccAmount() {
		return accAmount;
	}
	public void setAccAmount(Integer accAmount) {
		this.accAmount = accAmount;
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