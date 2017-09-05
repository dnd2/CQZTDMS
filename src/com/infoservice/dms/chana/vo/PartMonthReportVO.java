package com.infoservice.dms.chana.vo;

public class PartMonthReportVO extends BaseVO {
	private String reportMonth; //下端：报表月份  CHAR(2)  上端：  
	private String reportYear; //下端：报表年份  CHAR(4)  上端：  
	private String storageCode; //下端：仓库代码  CHAR(4)  上端：  
	private String partBatchNo; //下端：进货批号  VARCHAR(30)  上端：  
	private String partNo; //下端：配件代码  VARCHAR(27)  上端：  
	private String partName; //下端：配件名称  VARCHAR(120)  上端：  
	private Float openQuantity; //下端：期初数量  NUMERIC(8,2)  上端：  
	private Double openPrice; //下端：期初单价  NUMERIC(12,4)  上端：  
	private Double openAmount; //下端：期初金额  NUMERIC(12,2)  上端：  
	private Float inQuantity; //下端：入库数量  NUMERIC(8,2)  上端：  
	private Double stockInAmount; //下端：入库金额  NUMERIC(12,2)  上端：  
	private Float buyInCount; //下端：采购入库数量  NUMERIC(8,2)  上端：  
	private Double buyInAmount; //下端：采购入库金额  NUMERIC(12,2)  上端：  
	private Float allocateInCount; //下端：调拨入库数量  NUMERIC(8,2)  上端：  
	private Double allocateInAmount; //下端：调拨入库金额  NUMERIC(12,2)  上端：  
	private Float transferInCount; //下端：移库入库数量  NUMERIC(8,2)  上端：  
	private Double transferInAmount; //下端：移库入库金额  NUMERIC(12,2)  上端：  
	private Float otherInCount; //下端：其它入库数量  NUMERIC(8,2)  上端：  
	private Double otherInAmount; //下端：其它入库金额  NUMERIC(12,2)  上端：  
	private Float profitInCount; //下端：报溢入库数量  NUMERIC(8,2)  上端：  
	private Double profitInAmount; //下端：报溢入库金额  NUMERIC(12,2)  上端：  
	private Float outQuantity; //下端：出库数量  NUMERIC(8,2)  上端：  
	private Double stockOutCostAmount; //下端：出库成本金额  NUMERIC(12,2)  上端：  
	private Double outAmount; //下端：出库金额  NUMERIC(12,2)  上端：  
	private Float repairOutCount; //下端：维修领料数量  NUMERIC(8,2)  上端：  
	private Double repairOutCostAmount; //下端：维修领料成本金额  NUMERIC(12,2)  上端：  
	private Double repairOutSaleAmount; //下端：维修领料销售金额  NUMERIC(12,2)  上端：  
	private Float saleOutCount; //下端：配件销售数量  NUMERIC(8,2)  上端：  
	private Double saleOutCostAmount; //下端：配件销售成本金额  NUMERIC(12,2)  上端：  
	private Double saleOutSaleAmount; //下端：配件销售销售金额  NUMERIC(12,2)  上端：  
	private Float innerOutCount; //下端：内部领用数量  NUMERIC(8,2)  上端：  
	private Double innerOutCostAmount; //下端：内部领用成本金额  NUMERIC(12,2)  上端：  
	private Double innerOutSaleAmount; //下端：内部领用销售金额  NUMERIC(12,2)  上端：  
	private Float allocateOutCount; //下端：调拨出库数量  NUMERIC(8,2)  上端：  
	private Double allocateOutCostAmount; //下端：调拨出库成本金额  NUMERIC(12,2)  上端：  
	private Double allocateOutSaleAmount; //下端：调拨出库销售金额  NUMERIC(12,2)  上端：  
	private Float transferOutCount; //下端：移库出库数量  NUMERIC(8,2)  上端：  
	private Double transferOutCostAmount; //下端：移库出库成本金额  NUMERIC(12,2)  上端：  
	private Float otherOutCount; //下端：其它出库数量  NUMERIC(8,2)  上端：  
	private Double otherOutCostAmount; //下端：其它出库成本金额  NUMERIC(12,2)  上端：  
	private Double otherOutSaleAmount; //下端：其它出库销售金额  NUMERIC(12,2)  上端：  
	private Float lossOutCount; //下端：报损出库数量  NUMERIC(8,2)  上端：  
	private Double lossOutAmount; //下端：报损出库金额  NUMERIC(12,2)  上端：  
	private Float closeQuantity; //下端：期末数量  NUMERIC(8,2)  上端：  
	private Double closePrice; //下端：期末单价  NUMERIC(12,4)  上端：  
	private Double closeAmount; //下端：期末金额  NUMERIC(12,2)  上端：  
	public String getReportMonth() {
		return reportMonth;
	}
	public void setReportMonth(String reportMonth) {
		this.reportMonth = reportMonth;
	}
	public String getReportYear() {
		return reportYear;
	}
	public void setReportYear(String reportYear) {
		this.reportYear = reportYear;
	}
	public String getStorageCode() {
		return storageCode;
	}
	public void setStorageCode(String storageCode) {
		this.storageCode = storageCode;
	}
	public String getPartBatchNo() {
		return partBatchNo;
	}
	public void setPartBatchNo(String partBatchNo) {
		this.partBatchNo = partBatchNo;
	}
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
	public Float getOpenQuantity() {
		return openQuantity;
	}
	public void setOpenQuantity(Float openQuantity) {
		this.openQuantity = openQuantity;
	}
	public Double getOpenPrice() {
		return openPrice;
	}
	public void setOpenPrice(Double openPrice) {
		this.openPrice = openPrice;
	}
	public Double getOpenAmount() {
		return openAmount;
	}
	public void setOpenAmount(Double openAmount) {
		this.openAmount = openAmount;
	}
	public Float getInQuantity() {
		return inQuantity;
	}
	public void setInQuantity(Float inQuantity) {
		this.inQuantity = inQuantity;
	}
	public Double getStockInAmount() {
		return stockInAmount;
	}
	public void setStockInAmount(Double stockInAmount) {
		this.stockInAmount = stockInAmount;
	}
	public Float getBuyInCount() {
		return buyInCount;
	}
	public void setBuyInCount(Float buyInCount) {
		this.buyInCount = buyInCount;
	}
	public Double getBuyInAmount() {
		return buyInAmount;
	}
	public void setBuyInAmount(Double buyInAmount) {
		this.buyInAmount = buyInAmount;
	}
	public Float getAllocateInCount() {
		return allocateInCount;
	}
	public void setAllocateInCount(Float allocateInCount) {
		this.allocateInCount = allocateInCount;
	}
	public Double getAllocateInAmount() {
		return allocateInAmount;
	}
	public void setAllocateInAmount(Double allocateInAmount) {
		this.allocateInAmount = allocateInAmount;
	}
	public Float getTransferInCount() {
		return transferInCount;
	}
	public void setTransferInCount(Float transferInCount) {
		this.transferInCount = transferInCount;
	}
	public Double getTransferInAmount() {
		return transferInAmount;
	}
	public void setTransferInAmount(Double transferInAmount) {
		this.transferInAmount = transferInAmount;
	}
	public Float getOtherInCount() {
		return otherInCount;
	}
	public void setOtherInCount(Float otherInCount) {
		this.otherInCount = otherInCount;
	}
	public Double getOtherInAmount() {
		return otherInAmount;
	}
	public void setOtherInAmount(Double otherInAmount) {
		this.otherInAmount = otherInAmount;
	}
	public Float getProfitInCount() {
		return profitInCount;
	}
	public void setProfitInCount(Float profitInCount) {
		this.profitInCount = profitInCount;
	}
	public Double getProfitInAmount() {
		return profitInAmount;
	}
	public void setProfitInAmount(Double profitInAmount) {
		this.profitInAmount = profitInAmount;
	}
	public Float getOutQuantity() {
		return outQuantity;
	}
	public void setOutQuantity(Float outQuantity) {
		this.outQuantity = outQuantity;
	}
	public Double getStockOutCostAmount() {
		return stockOutCostAmount;
	}
	public void setStockOutCostAmount(Double stockOutCostAmount) {
		this.stockOutCostAmount = stockOutCostAmount;
	}
	public Double getOutAmount() {
		return outAmount;
	}
	public void setOutAmount(Double outAmount) {
		this.outAmount = outAmount;
	}
	public Float getRepairOutCount() {
		return repairOutCount;
	}
	public void setRepairOutCount(Float repairOutCount) {
		this.repairOutCount = repairOutCount;
	}
	public Double getRepairOutCostAmount() {
		return repairOutCostAmount;
	}
	public void setRepairOutCostAmount(Double repairOutCostAmount) {
		this.repairOutCostAmount = repairOutCostAmount;
	}
	public Double getRepairOutSaleAmount() {
		return repairOutSaleAmount;
	}
	public void setRepairOutSaleAmount(Double repairOutSaleAmount) {
		this.repairOutSaleAmount = repairOutSaleAmount;
	}
	public Float getSaleOutCount() {
		return saleOutCount;
	}
	public void setSaleOutCount(Float saleOutCount) {
		this.saleOutCount = saleOutCount;
	}
	public Double getSaleOutCostAmount() {
		return saleOutCostAmount;
	}
	public void setSaleOutCostAmount(Double saleOutCostAmount) {
		this.saleOutCostAmount = saleOutCostAmount;
	}
	public Double getSaleOutSaleAmount() {
		return saleOutSaleAmount;
	}
	public void setSaleOutSaleAmount(Double saleOutSaleAmount) {
		this.saleOutSaleAmount = saleOutSaleAmount;
	}
	public Float getInnerOutCount() {
		return innerOutCount;
	}
	public void setInnerOutCount(Float innerOutCount) {
		this.innerOutCount = innerOutCount;
	}
	public Double getInnerOutCostAmount() {
		return innerOutCostAmount;
	}
	public void setInnerOutCostAmount(Double innerOutCostAmount) {
		this.innerOutCostAmount = innerOutCostAmount;
	}
	public Double getInnerOutSaleAmount() {
		return innerOutSaleAmount;
	}
	public void setInnerOutSaleAmount(Double innerOutSaleAmount) {
		this.innerOutSaleAmount = innerOutSaleAmount;
	}
	public Float getAllocateOutCount() {
		return allocateOutCount;
	}
	public void setAllocateOutCount(Float allocateOutCount) {
		this.allocateOutCount = allocateOutCount;
	}
	public Double getAllocateOutCostAmount() {
		return allocateOutCostAmount;
	}
	public void setAllocateOutCostAmount(Double allocateOutCostAmount) {
		this.allocateOutCostAmount = allocateOutCostAmount;
	}
	public Double getAllocateOutSaleAmount() {
		return allocateOutSaleAmount;
	}
	public void setAllocateOutSaleAmount(Double allocateOutSaleAmount) {
		this.allocateOutSaleAmount = allocateOutSaleAmount;
	}
	public Float getTransferOutCount() {
		return transferOutCount;
	}
	public void setTransferOutCount(Float transferOutCount) {
		this.transferOutCount = transferOutCount;
	}
	public Double getTransferOutCostAmount() {
		return transferOutCostAmount;
	}
	public void setTransferOutCostAmount(Double transferOutCostAmount) {
		this.transferOutCostAmount = transferOutCostAmount;
	}
	public Float getOtherOutCount() {
		return otherOutCount;
	}
	public void setOtherOutCount(Float otherOutCount) {
		this.otherOutCount = otherOutCount;
	}
	public Double getOtherOutCostAmount() {
		return otherOutCostAmount;
	}
	public void setOtherOutCostAmount(Double otherOutCostAmount) {
		this.otherOutCostAmount = otherOutCostAmount;
	}
	public Double getOtherOutSaleAmount() {
		return otherOutSaleAmount;
	}
	public void setOtherOutSaleAmount(Double otherOutSaleAmount) {
		this.otherOutSaleAmount = otherOutSaleAmount;
	}
	public Float getLossOutCount() {
		return lossOutCount;
	}
	public void setLossOutCount(Float lossOutCount) {
		this.lossOutCount = lossOutCount;
	}
	public Double getLossOutAmount() {
		return lossOutAmount;
	}
	public void setLossOutAmount(Double lossOutAmount) {
		this.lossOutAmount = lossOutAmount;
	}
	public Float getCloseQuantity() {
		return closeQuantity;
	}
	public void setCloseQuantity(Float closeQuantity) {
		this.closeQuantity = closeQuantity;
	}
	public Double getClosePrice() {
		return closePrice;
	}
	public void setClosePrice(Double closePrice) {
		this.closePrice = closePrice;
	}
	public Double getCloseAmount() {
		return closeAmount;
	}
	public void setCloseAmount(Double closeAmount) {
		this.closeAmount = closeAmount;
	}

}
