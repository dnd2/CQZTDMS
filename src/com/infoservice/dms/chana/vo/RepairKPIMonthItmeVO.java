package com.infoservice.dms.chana.vo;

public class RepairKPIMonthItmeVO extends BaseVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String series;               //车系
	private Integer repairCarCount;      //入厂台次
	private Double repairValueAmount;    //营业收入
	private Double singelCarAmount;      //单车收入
	public String getSeries() {
		return series;
	}
	public void setSeries(String series) {
		this.series = series;
	}
	public Integer getRepairCarCount() {
		return repairCarCount;
	}
	public void setRepairCarCount(Integer repairCarCount) {
		this.repairCarCount = repairCarCount;
	}
	public Double getRepairValueAmount() {
		return repairValueAmount;
	}
	public void setRepairValueAmount(Double repairValueAmount) {
		this.repairValueAmount = repairValueAmount;
	}
	public Double getSingelCarAmount() {
		return singelCarAmount;
	}
	public void setSingelCarAmount(Double singelCarAmount) {
		this.singelCarAmount = singelCarAmount;
	}
}
