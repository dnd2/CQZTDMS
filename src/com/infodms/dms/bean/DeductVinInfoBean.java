package com.infodms.dms.bean;

import com.infoservice.po3.bean.PO;

public class DeductVinInfoBean extends PO{

	private static final long serialVersionUID = 4186989760624512759L;
	
	private String vin;//
	private String vehicle_no;//牌照号
	private String engine_no;//发动机号
	private String series_name;//车系
	private String package_name;//车型
	private String gearbox_no;//变速箱号
	private String rearaxle_no;//后桥号
	private String transfer_no;//分动器号
	private String yieldly;//产地
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public String getVehicle_no() {
		return vehicle_no;
	}
	public void setVehicle_no(String vehicle_no) {
		this.vehicle_no = vehicle_no;
	}
	public String getEngine_no() {
		return engine_no;
	}
	public void setEngine_no(String engine_no) {
		this.engine_no = engine_no;
	}
	public String getSeries_name() {
		return series_name;
	}
	public void setSeries_name(String series_name) {
		this.series_name = series_name;
	}
	public String getPackage_name() {
		return package_name;
	}
	public void setPackage_name(String package_name) {
		this.package_name = package_name;
	}
	public String getGearbox_no() {
		return gearbox_no;
	}
	public void setGearbox_no(String gearbox_no) {
		this.gearbox_no = gearbox_no;
	}
	public String getRearaxle_no() {
		return rearaxle_no;
	}
	public void setRearaxle_no(String rearaxle_no) {
		this.rearaxle_no = rearaxle_no;
	}
	public String getTransfer_no() {
		return transfer_no;
	}
	public void setTransfer_no(String transfer_no) {
		this.transfer_no = transfer_no;
	}
	public String getYieldly() {
		return yieldly;
	}
	public void setYieldly(String yieldly) {
		this.yieldly = yieldly;
	}
}
