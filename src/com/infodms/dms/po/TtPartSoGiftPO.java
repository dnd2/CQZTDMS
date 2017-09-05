/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-10-22 13:49:35
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartSoGiftPO extends PO{
	private Long orderId;
	private String orderCode;
	private Long soId;
	private String soCode;
	private String cxType;
	private Long partId; 
	private Long buyQty;
	private Long giftQty;
	private Date createDate;
	private Long createBy;
	private Date updateDate;
	private Long updateBy;
	private Date deleteDate;
	private Long deleteBy;
	private Integer state;
	private Integer status;
	private String pickOrderId;
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public Long getSoId() {
		return soId;
	}
	public void setSoId(Long soId) {
		this.soId = soId;
	}
	public String getSoCode() {
		return soCode;
	}
	public void setSoCode(String soCode) {
		this.soCode = soCode;
	}
	public String getCxType() {
		return cxType;
	}
	public void setCxType(String cxType) {
		this.cxType = cxType;
	}
	public Long getPartId() {
		return partId;
	}
	public void setPartId(Long partId) {
		this.partId = partId;
	}
	public Long getBuyQty() {
		return buyQty;
	}
	public void setBuyQty(Long buyQty) {
		this.buyQty = buyQty;
	}
	public Long getGiftQty() {
		return giftQty;
	}
	public void setGiftQty(Long giftQty) {
		this.giftQty = giftQty;
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
	public Date getDeleteDate() {
		return deleteDate;
	}
	public void setDeleteDate(Date deleteDate) {
		this.deleteDate = deleteDate;
	}
	public Long getDeleteBy() {
		return deleteBy;
	}
	public void setDeleteBy(Long deleteBy) {
		this.deleteBy = deleteBy;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getPickOrderId() {
		return pickOrderId;
	}
	public void setPickOrderId(String pickOrderId) {
		this.pickOrderId = pickOrderId;
	}
	
	

}