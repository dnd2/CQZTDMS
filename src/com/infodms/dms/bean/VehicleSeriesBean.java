package com.infodms.dms.bean;

import com.infoservice.po3.bean.PO;

public class VehicleSeriesBean extends PO{

	private static final long serialVersionUID = -1273847991075642760L;
	private String group_id;
	private String vehicle_series;

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public String getVehicle_series() {
		return vehicle_series;
	}

	public void setVehicle_series(String vehicle_series) {
		this.vehicle_series = vehicle_series;
	}

}
