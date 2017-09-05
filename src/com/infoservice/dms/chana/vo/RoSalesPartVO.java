package com.infoservice.dms.chana.vo;

import java.util.Date;

public class RoSalesPartVO extends BaseVO {
	private String partNo; //下端：配件代码  VARCHAR(27)  上端：  
	private String partName; //下端：配件名称  VARCHAR(120)  上端：  
	private String storageCode; //下端：仓库代码  CHAR(4)  上端：  
	private String chargePartitionCode; //下端：收费区分代码  VARCHAR(4)  上端：  
	private String storagePositionCode; //下端：库位代码  VARCHAR(30)  上端：  
	private String unitCode; //下端：计量单位代码  CHAR(4)  上端：  
	private String partBatchNo; //下端：进货批号  VARCHAR(30)  上端：  
	private String manageSortCode; //下端：收费类别代码  VARCHAR(4)  上端：  
	private Integer priceType; //下端：价格类型  NUMERIC(8) 12361001 成本价
	private Float partQuantity; //下端：配件数量  NUMERIC(8,2)  上端：  
	private Float priceRate; //下端：价格系数  NUMERIC(3,2)  上端：  
	private Double oemLimitPrice; //下端：OEM销售限价  NUMERIC(10,2)  上端：  
	private Double partCostPrice; //下端：配件成本单价  NUMERIC(12,4)  上端：  
	private Double partSalesPrice; //下端：配件销售单价  NUMERIC(10,2)  上端：  
	private Double partCostAmount; //下端：配件成本金额  NUMERIC(12,2)  上端：  
	private Double partSalesAmount; //下端：配件销售金额  NUMERIC(12,2)  上端：  
	private String sender; //下端：发料人  CHAR(4)  上端：  
	private String receiver; //下端：领料人  CHAR(4)  上端：  
	private Date sendTime; //下端：发料时间  TIMESTAMP  上端：  
	private Integer isFinished; //下端：是否入帐  NUMERIC(8) 下端传以下值
	private Integer isDiscount; //下端：是否打折  NUMERIC(8) 下端传以下值
	private Float discount; //下端：折扣率  NUMERIC(5,4)  上端：  
	public String getPartNo() {
		return partNo;
	}
	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}
	public String getPartName() {
		return partName;
	}
	public void setPartName(String partName) {
		this.partName = partName;
	}
	public String getStorageCode() {
		return storageCode;
	}
	public void setStorageCode(String storageCode) {
		this.storageCode = storageCode;
	}
	public String getChargePartitionCode() {
		return chargePartitionCode;
	}
	public void setChargePartitionCode(String chargePartitionCode) {
		this.chargePartitionCode = chargePartitionCode;
	}
	public String getStoragePositionCode() {
		return storagePositionCode;
	}
	public void setStoragePositionCode(String storagePositionCode) {
		this.storagePositionCode = storagePositionCode;
	}
	public String getUnitCode() {
		return unitCode;
	}
	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}
	public String getPartBatchNo() {
		return partBatchNo;
	}
	public void setPartBatchNo(String partBatchNo) {
		this.partBatchNo = partBatchNo;
	}
	public String getManageSortCode() {
		return manageSortCode;
	}
	public void setManageSortCode(String manageSortCode) {
		this.manageSortCode = manageSortCode;
	}
	public Integer getPriceType() {
		return priceType;
	}
	public void setPriceType(Integer priceType) {
		this.priceType = priceType;
	}
	public Float getPartQuantity() {
		return partQuantity;
	}
	public void setPartQuantity(Float partQuantity) {
		this.partQuantity = partQuantity;
	}
	public Float getPriceRate() {
		return priceRate;
	}
	public void setPriceRate(Float priceRate) {
		this.priceRate = priceRate;
	}
	public Double getOemLimitPrice() {
		return oemLimitPrice;
	}
	public void setOemLimitPrice(Double oemLimitPrice) {
		this.oemLimitPrice = oemLimitPrice;
	}
	public Double getPartCostPrice() {
		return partCostPrice;
	}
	public void setPartCostPrice(Double partCostPrice) {
		this.partCostPrice = partCostPrice;
	}
	public Double getPartSalesPrice() {
		return partSalesPrice;
	}
	public void setPartSalesPrice(Double partSalesPrice) {
		this.partSalesPrice = partSalesPrice;
	}
	public Double getPartCostAmount() {
		return partCostAmount;
	}
	public void setPartCostAmount(Double partCostAmount) {
		this.partCostAmount = partCostAmount;
	}
	public Double getPartSalesAmount() {
		return partSalesAmount;
	}
	public void setPartSalesAmount(Double partSalesAmount) {
		this.partSalesAmount = partSalesAmount;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public Date getSendTime() {
		return sendTime;
	}
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	public Integer getIsFinished() {
		return isFinished;
	}
	public void setIsFinished(Integer isFinished) {
		this.isFinished = isFinished;
	}
	public Integer getIsDiscount() {
		return isDiscount;
	}
	public void setIsDiscount(Integer isDiscount) {
		this.isDiscount = isDiscount;
	}
	public Float getDiscount() {
		return discount;
	}
	public void setDiscount(Float discount) {
		this.discount = discount;
	}

}
