/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-04-28 17:18:03
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtSalesWayBillDtlPO extends PO{
	  private Long dtlId;
	private Long billId;
	private Long vehicleId;
	private Long createBy;
	private Date createDate;
	private Long updateBy;
	private Date updateDate;
	private Date assDate;
	private Date allocaDate;
	private String orderNo;
	private String vin;
	private Long isAcc;
	private Long matId;
	private Long isStatus;
	private String erpMaterialCode;
	private Long isStatus1;
	private Long orderId;
	private Long orderDetailId;
	private Long isTc;
	private Integer status;
	private Long driverUserId;//司机用户id
	private Date driverBindDate;
	private String driverPhone;//司机手机
	private String reportAddress;//上报地址
	private Date reportDate;//上报地址
	private Long dlvWhId;
	private Double price;//价格
	private Double priceFactor;//价格系数
	private Double mileage  ;//里程
	private Double oneBillAmount;//挂账金额
	private Integer dlvIsSd;
	private Integer dlvIsZz;
	private Long dlvZzProvId;
	private Long dlvZzCityId;
	private Long dlvZzCountyId;
	private Double newPrice;//新单价
	private Double newMileage  ;//新里程
	private Double newAmount;//新运费
	private Long zzWhId;
	private Double priceZz;//价格
	private Double mileageZz;//里程
	
	public Double getPriceZz() {
		return priceZz;
	}
	public void setPriceZz(Double priceZz) {
		this.priceZz = priceZz;
	}
	public Double getMileageZz() {
		return mileageZz;
	}
	public void setMileageZz(Double mileageZz) {
		this.mileageZz = mileageZz;
	}
	public Long getZzWhId() {
		return zzWhId;
	}
	public void setZzWhId(Long zzWhId) {
		this.zzWhId = zzWhId;
	}
	public Double getNewAmount() {
		return newAmount;
	}
	public void setNewAmount(Double newAmount) {
		this.newAmount = newAmount;
	}
	public Double getNewPrice() {
		return newPrice;
	}
	public void setNewPrice(Double newPrice) {
		this.newPrice = newPrice;
	}
	
	public Double getNewMileage() {
		return newMileage;
	}
	public void setNewMileage(Double newMileage) {
		this.newMileage = newMileage;
	}
	public Integer getDlvIsSd() {
		return dlvIsSd;
	}
	public void setDlvIsSd(Integer dlvIsSd) {
		this.dlvIsSd = dlvIsSd;
	}
	public Integer getDlvIsZz() {
		return dlvIsZz;
	}
	public void setDlvIsZz(Integer dlvIsZz) {
		this.dlvIsZz = dlvIsZz;
	}
	public Long getDlvZzProvId() {
		return dlvZzProvId;
	}
	public void setDlvZzProvId(Long dlvZzProvId) {
		this.dlvZzProvId = dlvZzProvId;
	}
	public Long getDlvZzCityId() {
		return dlvZzCityId;
	}
	public void setDlvZzCityId(Long dlvZzCityId) {
		this.dlvZzCityId = dlvZzCityId;
	}
	public Long getDlvZzCountyId() {
		return dlvZzCountyId;
	}
	public void setDlvZzCountyId(Long dlvZzCountyId) {
		this.dlvZzCountyId = dlvZzCountyId;
	}
	/**
	 * @return the price
	 */
	public Double getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(Double price) {
		this.price = price;
	}
	/**
	 * @return the priceFactor
	 */
	public Double getPriceFactor() {
		return priceFactor;
	}
	/**
	 * @param priceFactor the priceFactor to set
	 */
	public void setPriceFactor(Double priceFactor) {
		this.priceFactor = priceFactor;
	}
	/**
	 * @return the mileage
	 */
	public Double getMileage() {
		return mileage;
	}
	/**
	 * @param mileage the mileage to set
	 */
	public void setMileage(Double mileage) {
		this.mileage = mileage;
	}
	/**
	 * @return the oneBillAmount
	 */
	public Double getOneBillAmount() {
		return oneBillAmount;
	}
	/**
	 * @param oneBillAmount the oneBillAmount to set
	 */
	public void setOneBillAmount(Double oneBillAmount) {
		this.oneBillAmount = oneBillAmount;
	}
	public Long getDlvWhId() {
		return dlvWhId;
	}
	public void setDlvWhId(Long dlvWhId) {
		this.dlvWhId = dlvWhId;
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
	 * @return the reportAddress
	 */
	public String getReportAddress() {
		return reportAddress;
	}
	/**
	 * @param reportAddress the reportAddress to set
	 */
	public void setReportAddress(String reportAddress) {
		this.reportAddress = reportAddress;
	}
	/**
	 * @return the reportDate
	 */
	public Date getReportDate() {
		return reportDate;
	}
	/**
	 * @param reportDate the reportDate to set
	 */
	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}
	public Date getDriverBindDate() {
		return driverBindDate;
	}
	public void setDriverBindDate(Date driverBindDate) {
		this.driverBindDate = driverBindDate;
	}
	/**
	 * @return the driverUserId
	 */
	public Long getDriverUserId() {
		return driverUserId;
	}
	/**
	 * @param driverUserId the driverUserId to set
	 */
	public void setDriverUserId(Long driverUserId) {
		this.driverUserId = driverUserId;
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
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
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
	public Long getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(Long vehicleId) {
		this.vehicleId = vehicleId;
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
	public Date getAssDate() {
		return assDate;
	}
	public void setAssDate(Date assDate) {
		this.assDate = assDate;
	}
	public Date getAllocaDate() {
		return allocaDate;
	}
	public void setAllocaDate(Date allocaDate) {
		this.allocaDate = allocaDate;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public Long getIsAcc() {
		return isAcc;
	}
	public void setIsAcc(Long isAcc) {
		this.isAcc = isAcc;
	}
	public Long getMatId() {
		return matId;
	}
	public void setMatId(Long matId) {
		this.matId = matId;
	}
	public Long getIsStatus() {
		return isStatus;
	}
	public void setIsStatus(Long isStatus) {
		this.isStatus = isStatus;
	}
	public String getErpMaterialCode() {
		return erpMaterialCode;
	}
	public void setErpMaterialCode(String erpMaterialCode) {
		this.erpMaterialCode = erpMaterialCode;
	}
	public Long getIsStatus1() {
		return isStatus1;
	}
	public void setIsStatus1(Long isStatus1) {
		this.isStatus1 = isStatus1;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public Long getOrderDetailId() {
		return orderDetailId;
	}
	public void setOrderDetailId(Long orderDetailId) {
		this.orderDetailId = orderDetailId;
	}
	public Long getIsTc() {
		return isTc;
	}
	public void setIsTc(Long isTc) {
		this.isTc = isTc;
	}
	
	
}