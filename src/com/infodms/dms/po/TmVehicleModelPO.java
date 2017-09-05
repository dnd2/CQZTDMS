package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmVehicleModelPO extends PO{

	private String vehicleModelCode;
	private Long vehicleModelId;
	private String vehicleModelName;
	private Long seriesId;
	
	public Long getSeriesId() {
		return seriesId;
	}

	public void setSeriesId(Long seriesId) {
		this.seriesId = seriesId;
	}

	public void setVehicleModelCode(String vehicleModelCode){
		this.vehicleModelCode=vehicleModelCode;
	}

	public String getVehicleModelCode(){
		return this.vehicleModelCode;
	}

	public void setVehicleModelId(Long vehicleModelId){
		this.vehicleModelId=vehicleModelId;
	}

	public Long getVehicleModelId(){
		return this.vehicleModelId;
	}

	public void setVehicleModelName(String vehicleModelName){
		this.vehicleModelName=vehicleModelName;
	}

	public String getVehicleModelName(){
		return this.vehicleModelName;
	}

}