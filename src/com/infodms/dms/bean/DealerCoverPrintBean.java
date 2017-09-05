package com.infodms.dms.bean;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class DealerCoverPrintBean extends PO{

	private String dealerCode;
	private String dealerName;
	private Integer baoyCount;
	private Integer shouqCount;
	private Double shouqAmount;
	private Integer normalCount;
	private Double normalAmount;
	private Integer specialCount;
	private Double specialAmount;
	private Integer outCount;
	private Double outAmount;
	private Integer activityCount;
	private Double activityAmount;
	private Double baoyAmount;
	private Integer transCount;
	private Double transAmount;
	private Double partPrice;
	private Double labourPrice;
	private Integer otherCount;
	private Double otherAmount;
	private Double freeMPrice;
	private Double countAmount;
	private Double amountTotal;
	public Double getCountAmount() {
		return countAmount;
	}
	public void setCountAmount(Double countAmount) {
		this.countAmount = countAmount;
	}
	public Double getAmountTotal() {
		return amountTotal;
	}
	public void setAmountTotal(Double amountTotal) {
		this.amountTotal = amountTotal;
	}
	public String getDealerCode() {
		return dealerCode;
	}
	public void setDealerCode(String dealerCode) {
		this.dealerCode = dealerCode;
	}
	public String getDealerName() {
		return dealerName;
	}
	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}
	public Integer getBaoyCount() {
		return baoyCount;
	}
	public void setBaoyCount(Integer baoyCount) {
		this.baoyCount = baoyCount;
	}
	public Integer getShouqCount() {
		return shouqCount;
	}
	public void setShouqCount(Integer shouqCount) {
		this.shouqCount = shouqCount;
	}
	public Double getShouqAmount() {
		return shouqAmount;
	}
	public void setShouqAmount(Double shouqAmount) {
		this.shouqAmount = shouqAmount;
	}
	public Integer getNormalCount() {
		return normalCount;
	}
	public void setNormalCount(Integer normalCount) {
		this.normalCount = normalCount;
	}
	public Double getNormalAmount() {
		return normalAmount;
	}
	public void setNormalAmount(Double normalAmount) {
		this.normalAmount = normalAmount;
	}
	public Integer getSpecialCount() {
		return specialCount;
	}
	public void setSpecialCount(Integer specialCount) {
		this.specialCount = specialCount;
	}
	public Double getSpecialAmount() {
		return specialAmount;
	}
	public void setSpecialAmount(Double specialAmount) {
		this.specialAmount = specialAmount;
	}
	public Integer getOutCount() {
		return outCount;
	}
	public void setOutCount(Integer outCount) {
		this.outCount = outCount;
	}
	public Double getOutAmount() {
		return outAmount;
	}
	public void setOutAmount(Double outAmount) {
		this.outAmount = outAmount;
	}
	public Integer getActivityCount() {
		return activityCount;
	}
	public void setActivityCount(Integer activityCount) {
		this.activityCount = activityCount;
	}
	public Double getActivityAmount() {
		return activityAmount;
	}
	public void setActivityAmount(Double activityAmount) {
		this.activityAmount = activityAmount;
	}
	public Double getBaoyAmount() {
		return baoyAmount;
	}
	public void setBaoyAmount(Double baoyAmount) {
		this.baoyAmount = baoyAmount;
	}
	public Integer getTransCount() {
		return transCount;
	}
	public void setTransCount(Integer transCount) {
		this.transCount = transCount;
	}
	public Double getTransAmount() {
		return transAmount;
	}
	public void setTransAmount(Double transAmount) {
		this.transAmount = transAmount;
	}
	public Double getPartPrice() {
		return partPrice;
	}
	public void setPartPrice(Double partPrice) {
		this.partPrice = partPrice;
	}
	public Double getLabourPrice() {
		return labourPrice;
	}
	public void setLabourPrice(Double labourPrice) {
		this.labourPrice = labourPrice;
	}
	public Integer getOtherCount() {
		return otherCount;
	}
	public void setOtherCount(Integer otherCount) {
		this.otherCount = otherCount;
	}
	public Double getOtherAmount() {
		return otherAmount;
	}
	public void setOtherAmount(Double otherAmount) {
		this.otherAmount = otherAmount;
	}
	public Double getFreeMPrice() {
		return freeMPrice;
	}
	public void setFreeMPrice(Double freeMPrice) {
		this.freeMPrice = freeMPrice;
	}
}
