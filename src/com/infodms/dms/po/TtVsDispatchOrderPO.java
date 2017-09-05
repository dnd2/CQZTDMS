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
public class TtVsDispatchOrderPO extends PO{

	private Long dispId;
	private String dispNo;
	private Long sendWareId;
	private String sendWarehouse;
	private Long receiveWareId;
	private String receiveWarehouse;
	private Integer orderStatus;
	private Date planDeliverDate;
	private Integer sendWay;
	private String address;
	private Long addressId;
	private String accPerson;
	private String accTel;
	private Date subDate;
	private Integer subNum;
	private Integer boNum;
	private Integer sendNum;
	private Integer accNum;
	private Integer repNum;
	private String remark;
	private Long createBy;
	private Date createDate;
	private Long updateBy;
	private Date updateDate;
	private Integer fpNum;
	
	
	public Integer getFpNum() {
		return fpNum;
	}
	public void setFpNum(Integer fpNum) {
		this.fpNum = fpNum;
	}
	public Long getDispId() {
		return dispId;
	}
	public void setDispId(Long dispId) {
		this.dispId = dispId;
	}
	public String getDispNo() {
		return dispNo;
	}
	public void setDispNo(String dispNo) {
		this.dispNo = dispNo;
	}
	public Long getSendWareId() {
		return sendWareId;
	}
	public void setSendWareId(Long sendWareId) {
		this.sendWareId = sendWareId;
	}
	public String getSendWarehouse() {
		return sendWarehouse;
	}
	public void setSendWarehouse(String sendWarehouse) {
		this.sendWarehouse = sendWarehouse;
	}
	public Long getReceiveWareId() {
		return receiveWareId;
	}
	public void setReceiveWareId(Long receiveWareId) {
		this.receiveWareId = receiveWareId;
	}
	public String getReceiveWarehouse() {
		return receiveWarehouse;
	}
	public void setReceiveWarehouse(String receiveWarehouse) {
		this.receiveWarehouse = receiveWarehouse;
	}
	public Integer getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}
	public Date getPlanDeliverDate() {
		return planDeliverDate;
	}
	public void setPlanDeliverDate(Date planDeliverDate) {
		this.planDeliverDate = planDeliverDate;
	}
	public Integer getSendWay() {
		return sendWay;
	}
	public void setSendWay(Integer sendWay) {
		this.sendWay = sendWay;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Long getAddressId() {
		return addressId;
	}
	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}
	public String getAccPerson() {
		return accPerson;
	}
	public void setAccPerson(String accPerson) {
		this.accPerson = accPerson;
	}
	public String getAccTel() {
		return accTel;
	}
	public void setAccTel(String accTel) {
		this.accTel = accTel;
	}
	public Date getSubDate() {
		return subDate;
	}
	public void setSubDate(Date subDate) {
		this.subDate = subDate;
	}
	public Integer getSubNum() {
		return subNum;
	}
	public void setSubNum(Integer subNum) {
		this.subNum = subNum;
	}
	public Integer getBoNum() {
		return boNum;
	}
	public void setBoNum(Integer boNum) {
		this.boNum = boNum;
	}
	public Integer getSendNum() {
		return sendNum;
	}
	public void setSendNum(Integer sendNum) {
		this.sendNum = sendNum;
	}
	public Integer getAccNum() {
		return accNum;
	}
	public void setAccNum(Integer accNum) {
		this.accNum = accNum;
	}
	public Integer getRepNum() {
		return repNum;
	}
	public void setRepNum(Integer repNum) {
		this.repNum = repNum;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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