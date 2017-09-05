package com.infodms.dms.bean;

public class ServiceCenterMonthlyReportBean {
	private String seriesName;// 车系
	private String modelName;// 车型
	private String areaName;// 大区
	private String dealerCode;// 经销商代码集合
	private String beginTime;// 结算单开始时间
	private String endTime;// 结算单结束时间
	private String province;// 省份代码
	private String auditBeginTime;
	private String auditEndTime;
	

	public String getAuditBeginTime() {
		return auditBeginTime;
	}

	public void setAuditBeginTime(String auditBeginTime) {
		this.auditBeginTime = auditBeginTime;
	}

	public String getAuditEndTime() {
		return auditEndTime;
	}

	public void setAuditEndTime(String auditEndTime) {
		this.auditEndTime = auditEndTime;
	}

	public String getSeriesName() {
		return seriesName;
	}

	public void setSeriesName(String seriesName) {
		this.seriesName = seriesName;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getDealerCode() {
		return dealerCode;
	}

	public void setDealerCode(String dealerCode) {
		this.dealerCode = dealerCode;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

}
