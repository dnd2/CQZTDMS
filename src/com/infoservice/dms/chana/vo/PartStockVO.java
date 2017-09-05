package com.infoservice.dms.chana.vo;

import java.util.Date;

/**
 * @Title: PartStockVO.java
 *
 * @Description:CHANADMS
 *
 * @Copyright: Copyright (c) 2010
 *
 * @Company: www.infoservice.com.cn
 * @Date: 2010-7-27
 *
 * @author lishuai 
 * @mail   lishuai103@yahoo.cn	
 * @version 1.0
 * @remark 
 */
@SuppressWarnings("serial")
public class PartStockVO extends BaseVO {
	private String entityCode; //下端：经销商代码  CHAR(8)  上端：  
	private String partNo; //下端：配件代码  VARCHAR(27)  上端：PART_CODE VARCHAR2(30) 
	private String storageCode; //下端：仓库代码  CHAR(4)  上端：  
	private String storagePositionCode; //下端：库位代码  VARCHAR(30)  上端：  
	private String partName; //下端：配件名称  VARCHAR(120)  上端：PART_NAME VARCHAR2(100) 
	private String spellCode; //下端：拼音代码  VARCHAR(30)  上端：  
	private Integer partGroupCode; //下端：配件类别  NUMERIC(8) 上报时前三个对应转，后三个转为11361001 常用
	private String unitCode; //下端：计量单位代码  CHAR(4)  上端：UNIT VARCHAR2(20) 上端只有单位名称没有代码
	private Float paperQuantity; //下端：账面库存 stock_Quantity  下端的库存数量 上端：  
	private Float actualQuantity; //下端：实际库存   =帐面库存+借进-借出 上端：ACTUAL_QUANTITY  
	private Double salesPrice; //下端：销售价  NUMERIC(10,2)  上端：SALE_PRICE NUMBER(8,2) 
	private Double claimPrice; //下端：索赔价  NUMERIC(10,2)  上端：CLAIM_PRICE NUMBER(8,2) 
	private Double limitPrice; //下端：销售限价  NUMERIC(10,2)  上端：  
	private Double latestPrice; //下端：最新进货价  NUMERIC(12,4)  上端：  
	private Double insurancePrice; //下端：保险价  NUMERIC(10,2)  上端：  
	private Double costPrice; //下端：成本单价  NUMERIC(12,4)  上端：  
	private Double costAmount; //下端：成本金额  NUMERIC(12,2)  上端：  
	private Float maxStock; //下端：最大库存  NUMERIC(8,2)  上端：  
	private Float minStock; //下端：最小库存  NUMERIC(8,2)  上端：  
	private Float BorrowQuantity; //下端：借进数量  NUMERIC(8,2)  上端：  
	private Float lendQuantity; //下端：借出数量  NUMERIC(8,2)  上端：  
	private Float lockedQuantity; //下端：锁定数量  NUMERIC(8,2)  上端：  
	private Integer partStatus; //下端：是否停用  NUMERIC(8)  上端：  
	private String partModelGroupCodeSet; //下端：配件车型组集  VARCHAR(90)  上端：  
	private Integer partSpeType; //下端：配件特殊类别  NUMERIC(8)  上端：  
	private Integer isSuggestOrder; //下端：是否建议采购  NUMERIC(8)  上端：  
	private Integer monthlyQtyFormula; //下端：月用量公式  NUMERIC(8)  上端：  
	private Date lastStockIn; //下端：最新入库日期  TIMESTAMP  上端：DO_DATE DATE 根据DO_STATUS来判断
	private Date lastStockOut; //下端：最新出库日期  TIMESTAMP  上端：DO_DATE DATE 
	private Date foundDate; //下端：建档日期  TIMESTAMP  上端：  
	private Integer partMainType; //下端：配件大类  NUMERIC(8)  上端：  
	private String remark; //下端：备注  VARCHAR(300)  上端：  
	private Date downTimestamp; //下端：下发时序  Timestamp  上端：  
	private Integer isValid; //下端：有效状态  NUMERIC(8) 下端传以下值

	public String getEntityCode() {
		return entityCode;
	}
	public void setEntityCode(String entityCode) {
		this.entityCode = entityCode;
	}
	public String getPartNo() {
		return partNo;
	}
	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}
	public String getStorageCode() {
		return storageCode;
	}
	public void setStorageCode(String storageCode) {
		this.storageCode = storageCode;
	}
	public String getStoragePositionCode() {
		return storagePositionCode;
	}
	public void setStoragePositionCode(String storagePositionCode) {
		this.storagePositionCode = storagePositionCode;
	}
	public String getPartName() {
		return partName;
	}
	public void setPartName(String partName) {
		this.partName = partName;
	}
	public String getSpellCode() {
		return spellCode;
	}
	public void setSpellCode(String spellCode) {
		this.spellCode = spellCode;
	}
	public Integer getPartGroupCode() {
		return partGroupCode;
	}
	public void setPartGroupCode(Integer partGroupCode) {
		this.partGroupCode = partGroupCode;
	}
	public String getUnitCode() {
		return unitCode;
	}
	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}
	public Float getPaperQuantity() {
		return paperQuantity;
	}
	public void setPaperQuantity(Float paperQuantity) {
		this.paperQuantity = paperQuantity;
	}
	public Float getActualQuantity() {
		return actualQuantity;
	}
	public void setActualQuantity(Float actualQuantity) {
		this.actualQuantity = actualQuantity;
	}
	public Double getSalesPrice() {
		return salesPrice;
	}
	public void setSalesPrice(Double salesPrice) {
		this.salesPrice = salesPrice;
	}
	public Double getClaimPrice() {
		return claimPrice;
	}
	public void setClaimPrice(Double claimPrice) {
		this.claimPrice = claimPrice;
	}
	public Double getLimitPrice() {
		return limitPrice;
	}
	public void setLimitPrice(Double limitPrice) {
		this.limitPrice = limitPrice;
	}
	public Double getLatestPrice() {
		return latestPrice;
	}
	public void setLatestPrice(Double latestPrice) {
		this.latestPrice = latestPrice;
	}
	public Double getInsurancePrice() {
		return insurancePrice;
	}
	public void setInsurancePrice(Double insurancePrice) {
		this.insurancePrice = insurancePrice;
	}
	public Double getCostPrice() {
		return costPrice;
	}
	public void setCostPrice(Double costPrice) {
		this.costPrice = costPrice;
	}
	public Double getCostAmount() {
		return costAmount;
	}
	public void setCostAmount(Double costAmount) {
		this.costAmount = costAmount;
	}
	public Float getMaxStock() {
		return maxStock;
	}
	public void setMaxStock(Float maxStock) {
		this.maxStock = maxStock;
	}
	public Float getMinStock() {
		return minStock;
	}
	public void setMinStock(Float minStock) {
		this.minStock = minStock;
	}
	public Float getBorrowQuantity() {
		return BorrowQuantity;
	}
	public void setBorrowQuantity(Float borrowQuantity) {
		BorrowQuantity = borrowQuantity;
	}
	public Float getLendQuantity() {
		return lendQuantity;
	}
	public void setLendQuantity(Float lendQuantity) {
		this.lendQuantity = lendQuantity;
	}
	public Float getLockedQuantity() {
		return lockedQuantity;
	}
	public void setLockedQuantity(Float lockedQuantity) {
		this.lockedQuantity = lockedQuantity;
	}
	public Integer getPartStatus() {
		return partStatus;
	}
	public void setPartStatus(Integer partStatus) {
		this.partStatus = partStatus;
	}
	public String getPartModelGroupCodeSet() {
		return partModelGroupCodeSet;
	}
	public void setPartModelGroupCodeSet(String partModelGroupCodeSet) {
		this.partModelGroupCodeSet = partModelGroupCodeSet;
	}
	public Integer getPartSpeType() {
		return partSpeType;
	}
	public void setPartSpeType(Integer partSpeType) {
		this.partSpeType = partSpeType;
	}
	public Integer getIsSuggestOrder() {
		return isSuggestOrder;
	}
	public void setIsSuggestOrder(Integer isSuggestOrder) {
		this.isSuggestOrder = isSuggestOrder;
	}
	public Integer getMonthlyQtyFormula() {
		return monthlyQtyFormula;
	}
	public void setMonthlyQtyFormula(Integer monthlyQtyFormula) {
		this.monthlyQtyFormula = monthlyQtyFormula;
	}
	public Date getLastStockIn() {
		return lastStockIn;
	}
	public void setLastStockIn(Date lastStockIn) {
		this.lastStockIn = lastStockIn;
	}
	public Date getLastStockOut() {
		return lastStockOut;
	}
	public void setLastStockOut(Date lastStockOut) {
		this.lastStockOut = lastStockOut;
	}
	public Date getFoundDate() {
		return foundDate;
	}
	public void setFoundDate(Date foundDate) {
		this.foundDate = foundDate;
	}
	public Integer getPartMainType() {
		return partMainType;
	}
	public void setPartMainType(Integer partMainType) {
		this.partMainType = partMainType;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Date getDownTimestamp() {
		return downTimestamp;
	}
	public void setDownTimestamp(Date downTimestamp) {
		this.downTimestamp = downTimestamp;
	}
	public Integer getIsValid() {
		return isValid;
	}
	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}

}
