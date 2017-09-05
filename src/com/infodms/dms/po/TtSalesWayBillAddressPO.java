/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-04-09 14:17:14
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtSalesWayBillAddressPO extends PO{
	private Long addressId;
	private Long dtlId;
	private Long billId;
	private Long isAcc;
	private Long vehicleId;
	private String address;
	private String vin;
	private String driverPhone;//司机手机号
	private Date addressDate;
	private Date createDate;
	private Long createBy;
	private Date updateDate;
	private Long updateBy;
	private Integer isSub;//是否交车
	private Long matId;//物料id
	private String  billNo;//交接单号
	private String  orderNo;//订单单号
	
	
	
	/**
	 * @return the isSub
	 */
	public Integer getIsSub() {
		return isSub;
	}
	/**
	 * @param isSub the isSub to set
	 */
	public void setIsSub(Integer isSub) {
		this.isSub = isSub;
	}
	/**
	 * @return the matId
	 */
	public Long getMatId() {
		return matId;
	}
	/**
	 * @param matId the matId to set
	 */
	public void setMatId(Long matId) {
		this.matId = matId;
	}
	/**
	 * @return the billNo
	 */
	public String getBillNo() {
		return billNo;
	}
	/**
	 * @param billNo the billNo to set
	 */
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	/**
	 * @return the orderNo
	 */
	public String getOrderNo() {
		return orderNo;
	}
	/**
	 * @param orderNo the orderNo to set
	 */
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	/**
	 * @return the driverPhone
	 */
	public String getDriverPhone() {
		return driverPhone;
	}
	/**
	 * @param driverPhone the driverPhone to set
	 */
	public void setDriverPhone(String driverPhone) {
		this.driverPhone = driverPhone;
	}
	/**
	 * @return the vin
	 */
	public String getVin() {
		return vin;
	}
	/**
	 * @param vin the vin to set
	 */
	public void setVin(String vin) {
		this.vin = vin;
	}
	public Date getAddressDate() {
		return addressDate;
	}
	public void setAddressDate(Date addressDate) {
		this.addressDate = addressDate;
	}
	public Long getAddressId() {
		return addressId;
	}
	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}
	public Long getDtlId() {
		return dtlId;
	}
	public void setDtlId(Long dtlId) {
		this.dtlId = dtlId;
	}
	public Long getBillId() {
		return billId;
	}
	public void setBillId(Long billId) {
		this.billId = billId;
	}
	public Long getIsAcc() {
		return isAcc;
	}
	public void setIsAcc(Long isAcc) {
		this.isAcc = isAcc;
	}
	public Long getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(Long vehicleId) {
		this.vehicleId = vehicleId;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
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