/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-09-04 14:53:13
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infoservice.dms.chana.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPtOrditemRpcPO extends PO{

	private Integer orderCount;
	private Date updateDate;
	private Integer receivedCount;
	private Long createBy;
	private Integer carryingCount;
	private Long orderId;
	private Integer onfittingCount;
	private Date createDate;
	private Integer canceledCount;
	private Integer replacedCount;
	private Integer orderItemStatus;
	private Long updateBy;
	private Long partId;
	private Long detailId;
	private String remark;
	private Integer isReplaced;
	private String partCode;

	public void setOrderCount(Integer orderCount){
		this.orderCount=orderCount;
	}

	public Integer getOrderCount(){
		return this.orderCount;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setReceivedCount(Integer receivedCount){
		this.receivedCount=receivedCount;
	}

	public Integer getReceivedCount(){
		return this.receivedCount;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setCarryingCount(Integer carryingCount){
		this.carryingCount=carryingCount;
	}

	public Integer getCarryingCount(){
		return this.carryingCount;
	}

	public void setOrderId(Long orderId){
		this.orderId=orderId;
	}

	public Long getOrderId(){
		return this.orderId;
	}

	public void setOnfittingCount(Integer onfittingCount){
		this.onfittingCount=onfittingCount;
	}

	public Integer getOnfittingCount(){
		return this.onfittingCount;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setCanceledCount(Integer canceledCount){
		this.canceledCount=canceledCount;
	}

	public Integer getCanceledCount(){
		return this.canceledCount;
	}

	public void setReplacedCount(Integer replacedCount){
		this.replacedCount=replacedCount;
	}

	public Integer getReplacedCount(){
		return this.replacedCount;
	}

	public void setOrderItemStatus(Integer orderItemStatus){
		this.orderItemStatus=orderItemStatus;
	}

	public Integer getOrderItemStatus(){
		return this.orderItemStatus;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setPartId(Long partId){
		this.partId=partId;
	}

	public Long getPartId(){
		return this.partId;
	}

	public void setDetailId(Long detailId){
		this.detailId=detailId;
	}

	public Long getDetailId(){
		return this.detailId;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setIsReplaced(Integer isReplaced){
		this.isReplaced=isReplaced;
	}

	public Integer getIsReplaced(){
		return this.isReplaced;
	}

	public String getPartCode() {
		return partCode;
	}

	public void setPartCode(String partCode) {
		this.partCode = partCode;
	}

}