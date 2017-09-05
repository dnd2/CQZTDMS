package com.infoservice.dms.chana.vo;

import java.util.Date;

/**
 * @Title: VehicleShippingDetailVO.java
 *
 * @Description:CHANADMS
 *
 * @Copyright: Copyright (c) 2010
 *
 * @Company: www.infoservice.com.cn
 * @Date: 2010-7-26
 *
 * @author lishuai 
 * @mail   lishuai103@yahoo.cn	
 * @version 1.0
 * @remark 
 */
@SuppressWarnings("serial")
public class VehicleShippingDetailVO extends BaseVO {
	
	private String shippingOrderNo; //发运单编号 VARCHAR(30)
	private String vin; //VIN VARCHAR(17)
	private String productCode; //产品代码 VARCHAR(30)
	private String engineNo; //发动机号 VARCHAR(30)
	private String keyNumber; //钥匙编号 VARCHAR(30)
	private Integer hasCertificate; //是否有合格证 NUMERIC(8)
	private String certificateNumber; //合格证号 VARCHAR(30)
	private Date manufactureDate; //生产日期 TIMESTAMP
	private Date factoryDate; //出厂日期 TIMESTAMP
	private Double vehiclePrice; //车辆价格 NUMERIC(12,2)
	private String productingAreaCode;	//产地代码
	private String productingAreaName;	//产地名称
	
	public String getShippingOrderNo() {
		return shippingOrderNo;
	}
	public void setShippingOrderNo(String shippingOrderNo) {
		this.shippingOrderNo = shippingOrderNo;
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
	public Integer getHasCertificate() {
		return hasCertificate;
	}
	public void setHasCertificate(Integer hasCertificate) {
		this.hasCertificate = hasCertificate;
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
	public String getProductingAreaCode() {
		return productingAreaCode;
	}
	public void setProductingAreaCode(String productingAreaCode) {
		this.productingAreaCode = productingAreaCode;
	}
	public String getProductingAreaName() {
		return productingAreaName;
	}
	public void setProductingAreaName(String productingAreaName) {
		this.productingAreaName = productingAreaName;
	}
}
