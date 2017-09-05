/**
 * Copyright (c) 2006-2008 OEM Infoservice Corp. 2006-2008,All Rights Reserved.
 * This software is published under the Infoservice DMS Service Inner Solution Team.
 * License version 1.0, a copy of which has been included with this
 * distribution in the LICENSE.txt file.
 *
 * @File name:  com.infoservice.dms.dc.vo.TtRoRepairPartVO.java
 * @Create on:  2009-4-2
 * @Author   :  
 *
 * @ChangeList
 * ---------------------------------------------------
 * NO    Date                     Editor               ChangeReasons
 * 1.    2009-4-2                  zhangdongjie              Add 
 *
 */
package com.infoservice.dms.chana.vo;

import java.util.Date;

@SuppressWarnings("serial")
public class RoRepairPartVO extends BaseVO {

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
	private Integer batchNo; //下端：流水号  NUMERIC(4)  上端：  
	private String activityCode; //下端：活动编号  VARCHAR(15)  上端：  
	private Integer preCheck; //下端：是否预检  NUMERIC(8) 下端传以下值
	private Integer interReturn; //下端：是否内返  NUMERIC(8) 下端传以下值
	private Integer needlessRepair; //下端：是否不修  NUMERIC(8) 下端传以下值
	private Integer consignExterior; //下端：是否委外  NUMERIC(8) 下端传以下值
	private Date printRpTime; //下端：预先捡料单打印时间  TIMESTAMP  上端：  
	private Integer printBatchNo; //下端：预捡单打印流水号  NUMERIC(4)  上端：  
	private String labourCode; //下端：维修项目代码  VARCHAR(30)  上端：  
	private String repairTypeCode; //下端：维修类型代码  CHAR(4)  上端：  
	private String modelLabourCode; //下端：维修车型分组代码  VARCHAR(15)  上端：  
	private String packageCode; //下端：组合代码  VARCHAR(30)  上端：  
	
	private String failureModeCode;//下端：失效模式代码  VARCHAR(30)  上端：  
	private String failureModeName;//下端：失效模式名称  VARCHAR(80)  上端：  
	private Integer mainForPart;//下端：是否主因件  NUMBER(8)    10041001:是，10041002:否   上端：  
	
	public String getFailureModeCode() {
		return failureModeCode;
	}
	public void setFailureModeCode(String failureModeCode) {
		this.failureModeCode = failureModeCode;
	}
	public String getFailureModeName() {
		return failureModeName;
	}
	public void setFailureModeName(String failureModeName) {
		this.failureModeName = failureModeName;
	}
	public Integer getMainForPart() {
		return mainForPart;
	}
	public void setMainForPart(Integer mainForPart) {
		this.mainForPart = mainForPart;
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
	public Integer getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(Integer batchNo) {
		this.batchNo = batchNo;
	}
	public String getActivityCode() {
		return activityCode;
	}
	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}
	public Integer getPreCheck() {
		return preCheck;
	}
	public void setPreCheck(Integer preCheck) {
		this.preCheck = preCheck;
	}
	public Integer getInterReturn() {
		return interReturn;
	}
	public void setInterReturn(Integer interReturn) {
		this.interReturn = interReturn;
	}
	public Integer getNeedlessRepair() {
		return needlessRepair;
	}
	public void setNeedlessRepair(Integer needlessRepair) {
		this.needlessRepair = needlessRepair;
	}
	public Integer getConsignExterior() {
		return consignExterior;
	}
	public void setConsignExterior(Integer consignExterior) {
		this.consignExterior = consignExterior;
	}
	public Date getPrintRpTime() {
		return printRpTime;
	}
	public void setPrintRpTime(Date printRpTime) {
		this.printRpTime = printRpTime;
	}
	public Integer getPrintBatchNo() {
		return printBatchNo;
	}
	public void setPrintBatchNo(Integer printBatchNo) {
		this.printBatchNo = printBatchNo;
	}
	public String getLabourCode() {
		return labourCode;
	}
	public void setLabourCode(String labourCode) {
		this.labourCode = labourCode;
	}
	public String getRepairTypeCode() {
		return repairTypeCode;
	}
	public void setRepairTypeCode(String repairTypeCode) {
		this.repairTypeCode = repairTypeCode;
	}
	public String getModelLabourCode() {
		return modelLabourCode;
	}
	public void setModelLabourCode(String modelLabourCode) {
		this.modelLabourCode = modelLabourCode;
	}
	public String getPackageCode() {
		return packageCode;
	}
	public void setPackageCode(String packageCode) {
		this.packageCode = packageCode;
	}

}
