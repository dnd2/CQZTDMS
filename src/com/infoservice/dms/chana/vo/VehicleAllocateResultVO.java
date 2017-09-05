package com.infoservice.dms.chana.vo;

import java.util.Date;

/**
 * @Title: VehicleAllocateResultVO.java
 *
 * @Description:CHANADMS
 *
 * @Copyright: Copyright (c) 2010
 *
 * @Company: www.infoservice.com.cn
 * @Date: 2010-7-27
 *
 * @author lishuai 
 * @mail   lishuai103@yahoo.cn	
 * @version 1.0
 * @remark 
 */
@SuppressWarnings("serial")
public class VehicleAllocateResultVO extends BaseVO {
	private String allocateOutNo; //下端：调拨单号  CHAR(12)  上端：TRANSFER_ID NUMBER(16) 上端数据库里只有调拨ID
	private Integer allocateType; //下端：调拨类型  NUMERIC(8)  上端：  上端暂时没有
	private String allocateOutEntityCode; //下端：调出经销商代码 DEALER_CODE VARCHAR(30)  上端：DEALER_CODE VARCHAR2(30) 根据调出ID
	private String allocateOutEntityName; //下端：调出经销商名称 ASC_NAME VARCHAR(300)  上端：DEALER_NAME VARCHAR2(60) 根据调出ID
	private String allocateInEntityCode; //下端：调入经销商代码 DEALER_CODE VARCHAR(30)  上端：DEALER_CODE VARCHAR2(30) 根据调入ID
	private String allocateInEntityName; //下端：调入经销商名称 ASC_NAME VARCHAR(300)  上端：DEALER_NAME VARCHAR2(60) 根据调入ID
	private String productCode; //下端：产品代码  VARCHAR(30)  上端：MATERIAL_CODE VARCHAR2(30) 
	private String vin; //下端：VIN VIN VARCHAR(17)  上端：VIN VARCHAR2(17) 
	private String engineNo; //下端：发动机号  VARCHAR(30)  上端：ENGINE_NO VARCHAR2(30) 
	private String keyNumber; //下端：钥匙编号  VARCHAR(30)  上端：  
	private Integer hasCertificate; //下端：是否有合格证  NUMERIC(8)  上端：  
	private String certificateNumber; //下端：合格证号  VARCHAR(30)  上端：  
	private Date manufactureDate; //下端：生产日期  TIMESTAMP  上端：PRODUCT_DATE DATE 
	private Date factoryDate; //下端：出厂日期  TIMESTAMP  上端：FACTORY_DATE DATE 
	private Double vehiclePrice; //下端：车辆价格  NUMERIC(12,2)  上端：VHCL_PRICE NUMBER(12,2) 物料价格
	private String remark; //下端：备注  VARCHAR(300) 上端ID放这里 上端：  


	public String getAllocateOutNo() {
		return allocateOutNo;
	}
	public void setAllocateOutNo(String allocateOutNo) {
		this.allocateOutNo = allocateOutNo;
	}
	public Integer getAllocateType() {
		return allocateType;
	}
	public void setAllocateType(Integer allocateType) {
		this.allocateType = allocateType;
	}
	public String getAllocateOutEntityCode() {
		return allocateOutEntityCode;
	}
	public void setAllocateOutEntityCode(String allocateOutEntityCode) {
		this.allocateOutEntityCode = allocateOutEntityCode;
	}
	public String getAllocateOutEntityName() {
		return allocateOutEntityName;
	}
	public void setAllocateOutEntityName(String allocateOutEntityName) {
		this.allocateOutEntityName = allocateOutEntityName;
	}
	public String getAllocateInEntityCode() {
		return allocateInEntityCode;
	}
	public void setAllocateInEntityCode(String allocateInEntityCode) {
		this.allocateInEntityCode = allocateInEntityCode;
	}
	public String getAllocateInEntityName() {
		return allocateInEntityName;
	}
	public void setAllocateInEntityName(String allocateInEntityName) {
		this.allocateInEntityName = allocateInEntityName;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

}
