package com.infoservice.dms.chana.vo;

public class RepairInfoMonthVO extends BaseVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String monthDate;              //月份
	private String series;                 //车系
	private String workType;               //工位类别
	private Integer oemClaimCount;         //OEM索赔工单数
	private Integer repairSBCount;         //首保工单数
	private Double oemHourAmount;          //OEM工单工时收入
	private Double oemPartAmount;          //OEM工单零件收入
	private Integer customerRepairCount;   //客户自费维修工单数
	private Integer customerZFBYCount;      //客户自费保养工单数
	private Double customerHourAmount;     //客户自费工单工时收入
	private Double customerPartAmount;    //客户自费工单零件收入
	private Integer insurationRepairCount; //保险理赔工单数
	private Integer myselfRepairCount;     //内部维修工单数
	private Double myselfHourAmount;       //内部维修工单工时收入
	private Double myselfPartAmount;       //内部维修工单零件收入
	private Integer noCheckCount;          //免费检查工单数
	public String getMonthDate() {
		return monthDate;
	}
	public void setMonthDate(String monthDate) {
		this.monthDate = monthDate;
	}
	public String getSeries() {
		return series;
	}
	public void setSeries(String series) {
		this.series = series;
	}
	public String getWorkType() {
		return workType;
	}
	public void setWorkType(String workType) {
		this.workType = workType;
	}
	public Integer getOemClaimCount() {
		return oemClaimCount;
	}
	public void setOemClaimCount(Integer oemClaimCount) {
		this.oemClaimCount = oemClaimCount;
	}
	public Integer getRepairSBCount() {
		return repairSBCount;
	}
	public void setRepairSBCount(Integer repairSBCount) {
		this.repairSBCount = repairSBCount;
	}
	public Double getOemHourAmount() {
		return oemHourAmount;
	}
	public void setOemHourAmount(Double oemHourAmount) {
		this.oemHourAmount = oemHourAmount;
	}
	public Double getOemPartAmount() {
		return oemPartAmount;
	}
	public void setOemPartAmount(Double oemPartAmount) {
		this.oemPartAmount = oemPartAmount;
	}
	public Integer getCustomerRepairCount() {
		return customerRepairCount;
	}
	public void setCustomerRepairCount(Integer customerRepairCount) {
		this.customerRepairCount = customerRepairCount;
	}
	public Integer getCustomerZFBYCount() {
		return customerZFBYCount;
	}
	public void setCustomerZFBYCount(Integer customerZFBYCount) {
		this.customerZFBYCount = customerZFBYCount;
	}
	public Double getCustomerHourAmount() {
		return customerHourAmount;
	}
	public void setCustomerHourAmount(Double customerHourAmount) {
		this.customerHourAmount = customerHourAmount;
	}
	public Double getCustomerPartAmount() {
		return customerPartAmount;
	}
	public void setCustomerPartAmount(Double customerPartAmount) {
		this.customerPartAmount = customerPartAmount;
	}
	public Integer getInsurationRepairCount() {
		return insurationRepairCount;
	}
	public void setInsurationRepairCount(Integer insurationRepairCount) {
		this.insurationRepairCount = insurationRepairCount;
	}
	public Integer getMyselfRepairCount() {
		return myselfRepairCount;
	}
	public void setMyselfRepairCount(Integer myselfRepairCount) {
		this.myselfRepairCount = myselfRepairCount;
	}
	public Double getMyselfHourAmount() {
		return myselfHourAmount;
	}
	public void setMyselfHourAmount(Double myselfHourAmount) {
		this.myselfHourAmount = myselfHourAmount;
	}
	public Double getMyselfPartAmount() {
		return myselfPartAmount;
	}
	public void setMyselfPartAmount(Double myselfPartAmount) {
		this.myselfPartAmount = myselfPartAmount;
	}
	public Integer getNoCheckCount() {
		return noCheckCount;
	}
	public void setNoCheckCount(Integer noCheckCount) {
		this.noCheckCount = noCheckCount;
	}
}
