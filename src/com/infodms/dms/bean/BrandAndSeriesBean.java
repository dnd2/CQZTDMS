package com.infodms.dms.bean;

import com.infoservice.po3.bean.PO;

/**
 * @author yuchanghong
 * 
 * 车辆整备查询页面中的车辆信息
 */
public class BrandAndSeriesBean extends PO{
	private String brand; //厂家品牌
	private String series; //车系
	
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


}
