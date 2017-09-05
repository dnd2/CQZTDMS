package com.infoservice.dms.chana.vo;

import java.util.LinkedList;

public class RepairKPIMonthVO extends BaseVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String monthDate;               //月份
	private Double repairHourAmount;        //工时收入
	private Double repairPartAmount;        //零件收入
	private Double repairJXAmount;          //机修收入
	private Double repairBPAmount;          //钣喷收入
	private Double repairJPZHAmount;        //精品装潢收入
	private Double repaiFWYZAmount;         //服务延展业务收入
	private Integer repairJXCount;           //机修工单数量
	private Integer repairBPCount;           //钣喷工单数量
	private Integer repairCarCount;          //入厂台次
	private Double repairValueAmount;       //营业收入
	private Double singelCarAmount;         //单车收入
	private Double noOemRepairValueAmount;  //非长安营业收入
	private Double repairAmountTotal;       //服务总收入
	private Double partBuyCost;             //零件采购成本
	private Double partInCost;              //零件入库成本
	private Double partSalesCost;           //零件销售成本
	private Integer partInventoryCount;     //实盘项数
	private Double partInventoryStock;      //实盘库存
	private Double partNooutStock;          //在厂库存
	private Integer stockMonthCount;        //库存供应月数
	private Double partMeetRate;            //零件满足率
	private Double noOemPartStock;          //非长安零件库存
	private Double partStock;               //零件总库存
	private LinkedList seriesList;          //车系列表
	private Double notRoPartAmount;         //非工单零件销售收入
	public String getMonthDate() {
		return monthDate;
	}
	public void setMonthDate(String monthDate) {
		this.monthDate = monthDate;
	}
	public Double getRepairHourAmount() {
		return repairHourAmount;
	}
	public void setRepairHourAmount(Double repairHourAmount) {
		this.repairHourAmount = repairHourAmount;
	}
	public Double getRepairPartAmount() {
		return repairPartAmount;
	}
	public void setRepairPartAmount(Double repairPartAmount) {
		this.repairPartAmount = repairPartAmount;
	}
	public Double getRepairJXAmount() {
		return repairJXAmount;
	}
	public void setRepairJXAmount(Double repairJXAmount) {
		this.repairJXAmount = repairJXAmount;
	}
	public Double getRepairBPAmount() {
		return repairBPAmount;
	}
	public void setRepairBPAmount(Double repairBPAmount) {
		this.repairBPAmount = repairBPAmount;
	}
	public Double getRepairJPZHAmount() {
		return repairJPZHAmount;
	}
	public void setRepairJPZHAmount(Double repairJPZHAmount) {
		this.repairJPZHAmount = repairJPZHAmount;
	}
	public Double getRepaiFWYZAmount() {
		return repaiFWYZAmount;
	}
	public void setRepaiFWYZAmount(Double repaiFWYZAmount) {
		this.repaiFWYZAmount = repaiFWYZAmount;
	}
	public Integer getRepairJXCount() {
		return repairJXCount;
	}
	public void setRepairJXCount(Integer repairJXCount) {
		this.repairJXCount = repairJXCount;
	}
	public Integer getRepairBPCount() {
		return repairBPCount;
	}
	public void setRepairBPCount(Integer repairBPCount) {
		this.repairBPCount = repairBPCount;
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
	public Double getNoOemRepairValueAmount() {
		return noOemRepairValueAmount;
	}
	public void setNoOemRepairValueAmount(Double noOemRepairValueAmount) {
		this.noOemRepairValueAmount = noOemRepairValueAmount;
	}
	public Double getRepairAmountTotal() {
		return repairAmountTotal;
	}
	public void setRepairAmountTotal(Double repairAmountTotal) {
		this.repairAmountTotal = repairAmountTotal;
	}
	public Double getPartBuyCost() {
		return partBuyCost;
	}
	public void setPartBuyCost(Double partBuyCost) {
		this.partBuyCost = partBuyCost;
	}
	public Double getPartInCost() {
		return partInCost;
	}
	public void setPartInCost(Double partInCost) {
		this.partInCost = partInCost;
	}
	public Double getPartSalesCost() {
		return partSalesCost;
	}
	public void setPartSalesCost(Double partSalesCost) {
		this.partSalesCost = partSalesCost;
	}
	public Integer getPartInventoryCount() {
		return partInventoryCount;
	}
	public void setPartInventoryCount(Integer partInventoryCount) {
		this.partInventoryCount = partInventoryCount;
	}
	public Double getPartInventoryStock() {
		return partInventoryStock;
	}
	public void setPartInventoryStock(Double partInventoryStock) {
		this.partInventoryStock = partInventoryStock;
	}
	public Double getPartNooutStock() {
		return partNooutStock;
	}
	public void setPartNooutStock(Double partNooutStock) {
		this.partNooutStock = partNooutStock;
	}
	public Integer getStockMonthCount() {
		return stockMonthCount;
	}
	public void setStockMonthCount(Integer stockMonthCount) {
		this.stockMonthCount = stockMonthCount;
	}
	public Double getPartMeetRate() {
		return partMeetRate;
	}
	public void setPartMeetRate(Double partMeetRate) {
		this.partMeetRate = partMeetRate;
	}
	public Double getNoOemPartStock() {
		return noOemPartStock;
	}
	public void setNoOemPartStock(Double noOemPartStock) {
		this.noOemPartStock = noOemPartStock;
	}
	public Double getPartStock() {
		return partStock;
	}
	public void setPartStock(Double partStock) {
		this.partStock = partStock;
	}
	public LinkedList getSeriesList() {
		return seriesList;
	}
	public void setSeriesList(LinkedList seriesList) {
		this.seriesList = seriesList;
	}
	public Double getNotRoPartAmount() {
		return notRoPartAmount;
	}
	public void setNotRoPartAmount(Double notRoPartAmount) {
		this.notRoPartAmount = notRoPartAmount;
	}

}
