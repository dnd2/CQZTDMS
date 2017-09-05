package com.infoservice.dms.chana.vo;

public class RepairValueVO extends BaseVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String monthDate;          //月份
	private Integer repairCount;       //当月工单数
	private Integer repairCarCount;    //当月进场台次
	private Double repairValueRate;    //当月平均工单产值
	private Double partSalesAmount;    //当月零件销售收入
	private Double repairValueAmount;  //当月售后营业收入
	public String getMonthDate() {
		return monthDate;
	}
	public void setMonthDate(String monthDate) {
		this.monthDate = monthDate;
	}
	public Integer getRepairCount() {
		return repairCount;
	}
	public void setRepairCount(Integer repairCount) {
		this.repairCount = repairCount;
	}
	public Integer getRepairCarCount() {
		return repairCarCount;
	}
	public void setRepairCarCount(Integer repairCarCount) {
		this.repairCarCount = repairCarCount;
	}
	public Double getRepairValueRate() {
		return repairValueRate;
	}
	public void setRepairValueRate(Double repairValueRate) {
		this.repairValueRate = repairValueRate;
	}
	public Double getPartSalesAmount() {
		return partSalesAmount;
	}
	public void setPartSalesAmount(Double partSalesAmount) {
		this.partSalesAmount = partSalesAmount;
	}
	public Double getRepairValueAmount() {
		return repairValueAmount;
	}
	public void setRepairValueAmount(Double repairValueAmount) {
		this.repairValueAmount = repairValueAmount;
	}

}
