package com.infodms.dms.bean;

public class UnsalesCarBean {
	private String seriesName;
	private String carTypeCode;
	private String carTypeName;
	private String vin;
	private Integer carAge;
	private String carStatus;
	private String remark;
	private Long vehicleId;
	private Integer carStatusCode;
	public Integer getCarStatusCode() {
		return carStatusCode;
	}
	public void setCarStatusCode(Integer carStatusCode) {
		this.carStatusCode = carStatusCode;
	}
	public Long getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(Long vehicleId) {
		this.vehicleId = vehicleId;
	}
	public String getSeriesName() {
		return seriesName;
	}
	public void setSeriesName(String seriesName) {
		this.seriesName = seriesName;
	}
	public String getCarTypeCode() {
		return carTypeCode;
	}
	public void setCarTypeCode(String carTypeCode) {
		this.carTypeCode = carTypeCode;
	}
	public String getCarTypeName() {
		return carTypeName;
	}
	public void setCarTypeName(String carTypeName) {
		this.carTypeName = carTypeName;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public Integer getCarAge() {
		return carAge;
	}
	public void setCarAge(Integer carAge) {
		this.carAge = carAge;
	}

	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCarStatus() {
		return carStatus;
	}
	public void setCarStatus(String carStatus) {
		this.carStatus = carStatus;
	}
	
}
