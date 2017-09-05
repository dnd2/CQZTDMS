package com.infoservice.dms.chana.vo;

import java.util.Date;

/**
 * @Title: VehicleStorageVO.java
 *
 * @Description:CHANADMS
 *
 * @Copyright: Copyright (c) 2012
 *
 * @Company: www.infoservice.com.cn
 * @Date: 2012-02-08
 *
 * @author chenchao 
 * @mail   chenchaof@ufida.com	
 * @version 1.0
 * @remark 
 */
@SuppressWarnings("serial")
public class VehicleStorageVO extends BaseVO {
	private String sendCarNo; //送车交接单号VARCHAR(30)
	private String orderNo; //采购订单编号 CHAR(12)
	private String deliveryNo;//发运单号VARCHAR(30
	private Date purchaseDate; //采购日期
	private Date shippingDate; //发货时间 Timestamp
	private Date arrivingDate;// 预计送到日期
	private String deliveryType; //运送方式 VARCHAR(20)
	private String shipperName; //承运商名称 VARCHAR(120)
	private String shipperLicense; //承运车牌号 VARCHAR(30)
	private String deliverymanName; //送车人姓名 VARCHAR(30)
	private String deliverymanPhone; //送车人电话 VARCHAR(60)
	private String shippingAddress; //收货地址 VARCHAR(120)
	private String stockInType;//入库方式 VARCHAR(20)
	private String vin; //VIN VARCHAR(17)
	private String productCode; //产品代码 VARCHAR(30)
	private String engineNo; //发动机号 VARCHAR(30)
	private String keyNumber; //钥匙编号 VARCHAR(30)
	private String certificateNumber; //合格证号 VARCHAR(30)
	private Date manufactureDate; //生产日期 TIMESTAMP
	private Date factoryDate; //出厂日期 TIMESTAMP
	private Double vehiclePrice; //车辆价格 NUMERIC(12,2)
	private String productingAreaName;	//产地名称
	private String vendorCode;  //供应单位代码
    private String vendorName;  //供应单位名称
	private String remark; //备注 VARCHAR(300)
	public String getSendCarNo() {
		return sendCarNo;
	}
	public void setSendCarNo(String sendCarNo) {
		this.sendCarNo = sendCarNo;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getDeliveryNo() {
		return deliveryNo;
	}
	public void setDeliveryNo(String deliveryNo) {
		this.deliveryNo = deliveryNo;
	}
	public Date getShippingDate() {
		return shippingDate;
	}
	public void setShippingDate(Date shippingDate) {
		this.shippingDate = shippingDate;
	}
	public String getDeliveryType() {
		return deliveryType;
	}
	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}
	public String getShipperName() {
		return shipperName;
	}
	public void setShipperName(String shipperName) {
		this.shipperName = shipperName;
	}
	public String getShipperLicense() {
		return shipperLicense;
	}
	public void setShipperLicense(String shipperLicense) {
		this.shipperLicense = shipperLicense;
	}
	public String getDeliverymanName() {
		return deliverymanName;
	}
	public void setDeliverymanName(String deliverymanName) {
		this.deliverymanName = deliverymanName;
	}
	public String getDeliverymanPhone() {
		return deliverymanPhone;
	}
	public void setDeliverymanPhone(String deliverymanPhone) {
		this.deliverymanPhone = deliverymanPhone;
	}
	public String getShippingAddress() {
		return shippingAddress;
	}
	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getEngineNo() {
		return engineNo;
	}
	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}
	public String getKeyNumber() {
		return keyNumber;
	}
	public void setKeyNumber(String keyNumber) {
		this.keyNumber = keyNumber;
	}
	public String getCertificateNumber() {
		return certificateNumber;
	}
	public void setCertificateNumber(String certificateNumber) {
		this.certificateNumber = certificateNumber;
	}
	public Date getManufactureDate() {
		return manufactureDate;
	}
	public void setManufactureDate(Date manufactureDate) {
		this.manufactureDate = manufactureDate;
	}
	public Date getFactoryDate() {
		return factoryDate;
	}
	public void setFactoryDate(Date factoryDate) {
		this.factoryDate = factoryDate;
	}
	public Double getVehiclePrice() {
		return vehiclePrice;
	}
	public void setVehiclePrice(Double vehiclePrice) {
		this.vehiclePrice = vehiclePrice;
	}
	public String getProductingAreaName() {
		return productingAreaName;
	}
	public void setProductingAreaName(String productingAreaName) {
		this.productingAreaName = productingAreaName;
	}
	public Date getArrivingDate() {
		return arrivingDate;
	}
	public void setArrivingDate(Date arrivingDate) {
		this.arrivingDate = arrivingDate;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getStockInType() {
		return stockInType;
	}
	public void setStockInType(String stockInType) {
		this.stockInType = stockInType;
	}
	public Date getPurchaseDate() {
		return purchaseDate;
	}
	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	public String getVendorCode() {
		return vendorCode;
	}
	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

}
