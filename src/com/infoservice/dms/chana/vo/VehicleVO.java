package com.infoservice.dms.chana.vo;

import java.util.Date;

/**
 * @Title: VehicleVO.java
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
public class VehicleVO extends BaseVO {
	private String vin; //下端：VIN  VARCHAR(17)  上端：  
	private String license; //下端：车牌号  VARCHAR(30)  上端：  
	private String engineNo; //下端：发动机号  VARCHAR(30)  上端：  
	private String gearBox; //下端：变速箱箱号  VARCHAR(30)  上端：  
	private String innerColor; //下端：内饰颜色  VARCHAR(20)  上端：  
	private String brand; //下端：品牌  VARCHAR(30)  上端：  
	private String series; //下端：车系  VARCHAR(30)  上端：  
	private String model; //下端：车型  VARCHAR(30)  上端：  
	private String color; //下端：颜色  VARCHAR(20)  上端：  
	private String apackage; //下端：配置  VARCHAR(120)  上端：  
	private String modelYear; //下端：车型年  CHAR(4)  上端：  
	private String exhaustQuantity; //下端：排气量  VARCHAR(20)  上端：  
	private Date productDate; //下端：制造日期  TIMESTAMP  上端：  
	private String handbookNo; //下端：保修手册号码  VARCHAR(10)  上端：  
	private String shiftType; //下端：排挡类别  VARCHAR(20)  上端：  
	private Integer fuelType; //下端：燃料类别  NUMERIC(8)  上端：  
	private String yieldly; //下端：产地  无  上端：   
	
	private Date salesDate; //下端：销售日期 SALES_DATE   上端：  	
	private Date licenseDate; //下端：上牌日期 LICENSE_DATE   上端：  	
	private String ownerName; //下端：车主 OWENER_NAME   上端：  	
	private Double totalChangeMileage; //下端：累计换表里程 TOTAL_CHANGE_MILEAGE   上端：  	
	private Double mileage; //下端：行驶里程 MILEAGE   上端：  	
	private Date changeDate; //下端：上次换表日期 CHANGE_DATE   上端：  	
	private Date lastMaintainDate; //下端：上次维修日期 LAST_MAINTAIN_DATE   上端：  	
	private String productCode; //下端：产品代码 PRODUCT_CODE   上端：  	

	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public String getLicense() {
		return license;
	}
	public void setLicense(String license) {
		this.license = license;
	}
	public String getEngineNo() {
		return engineNo;
	}
	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}
	public String getGearBox() {
		return gearBox;
	}
	public void setGearBox(String gearBox) {
		this.gearBox = gearBox;
	}
	public String getInnerColor() {
		return innerColor;
	}
	public void setInnerColor(String innerColor) {
		this.innerColor = innerColor;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getSeries() {
		return series;
	}
	public void setSeries(String series) {
		this.series = series;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getApackage() {
		return apackage;
	}
	public void setApackage(String apackage) {
		this.apackage = apackage;
	}
	public String getModelYear() {
		return modelYear;
	}
	public void setModelYear(String modelYear) {
		this.modelYear = modelYear;
	}
	public String getExhaustQuantity() {
		return exhaustQuantity;
	}
	public void setExhaustQuantity(String exhaustQuantity) {
		this.exhaustQuantity = exhaustQuantity;
	}
	public Date getProductDate() {
		return productDate;
	}
	public void setProductDate(Date productDate) {
		this.productDate = productDate;
	}
	public String getHandbookNo() {
		return handbookNo;
	}
	public void setHandbookNo(String handbookNo) {
		this.handbookNo = handbookNo;
	}
	public String getShiftType() {
		return shiftType;
	}
	public void setShiftType(String shiftType) {
		this.shiftType = shiftType;
	}
	public Integer getFuelType() {
		return fuelType;
	}
	public void setFuelType(Integer fuelType) {
		this.fuelType = fuelType;
	}
	public String getYieldly() {
		return yieldly;
	}
	public void setYieldly(String yieldly) {
		this.yieldly = yieldly;
	}
	public Date getSalesDate() {
		return salesDate;
	}
	public void setSalesDate(Date salesDate) {
		this.salesDate = salesDate;
	}
	public Date getLicenseDate() {
		return licenseDate;
	}
	public void setLicenseDate(Date licenseDate) {
		this.licenseDate = licenseDate;
	}
	public String getOwenerName() {
		return ownerName;
	}
	public void setOwenerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public Double getTotalChangeMileage() {
		return totalChangeMileage;
	}
	public void setTotalChangeMileage(Double totalChangeMileage) {
		this.totalChangeMileage = totalChangeMileage;
	}
	public Double getMileage() {
		return mileage;
	}
	public void setMileage(Double mileage) {
		this.mileage = mileage;
	}
	public Date getChangeDate() {
		return changeDate;
	}
	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}
	public Date getLastMaintainDate() {
		return lastMaintainDate;
	}
	public void setLastMaintainDate(Date lastMaintainDate) {
		this.lastMaintainDate = lastMaintainDate;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

}
