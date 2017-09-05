/**
 * Copyright (c) 2006-2008 OEM Infoservice Corp. 2006-2008,All Rights Reserved.
 * This software is published under the Infoservice DMS Service Inner Solution Team.
 * License version 1.0, a copy of which has been included with this
 * distribution in the LICENSE.txt file.
 *
 * @File name:  com.infoservice.dms.dc.vo.GmsROLabourVO.java
 * @Create on:  2009-4-2
 * @Author   :  zhangdongjie
 *
 * @ChangeList
 * ---------------------------------------------------
 * NO    Date                     Editor               ChangeReasons
 * 1.    2009-4-2                  zhangdongjie              Add 
 *
 */
package com.infoservice.dms.chana.vo;

@SuppressWarnings("serial")
public class RoLabourVO extends BaseVO {
	private String claimLabourName; //下端：索赔工时名称  VARCHAR(300)  上端：
	private String chargePartitionCode; //下端：收费区分代码  VARCHAR(4)  上端：  
	private String troubleCause; //下端：故障原因  VARCHAR(60)  上端：  
	private String troubleDesc; //下端：故障描述  VARCHAR(120)  上端：  
	private String localLabourCode; //下端：行管项目代码  CHAR(10)  上端：  
	private String localLabourName; //下端：行管项目名称  VARCHAR(90)  上端：  
	private String labourCode; //下端：维修项目代码  VARCHAR(30)  上端：  
	private Float labourPrice; //下端：工时单价  NUMERIC(8,2)  上端：  
	private String labourName; //下端：维修项目名称  VARCHAR(150)  上端：  
	private Double stdLabourHour; //下端：标准工时  NUMERIC(10,2)  上端：  
	private Double labourAmount; //下端：工时费  NUMERIC(12,2)  上端：  
	private String manageSortCode; //下端：收费类别代码  VARCHAR(4)  上端：  
	private String technician; //下端：技师  CHAR(4)  上端：  
	private String workerTypeCode; //下端：工种代码  CHAR(4)  上端：  
	private String Remark; //下端：备注  VARCHAR(300)  上端：  
	private Float discount; //下端：折扣率  NUMERIC(5,4)  上端：  
	private Integer interReturn; //下端：是否内返  NUMERIC(8) 下端传以下值
	private Integer needlessRepair; //下端：是否不修  NUMERIC(8) 下端传以下值
	private Integer preCheck; //下端：是否预检  NUMERIC(8) 下端传以下值
	private Integer consignExterior; //下端：是否委外  NUMERIC(8) 下端传以下值
	private Double assignLabourHour; //下端：派工工时  NUMERIC(10,2)  上端：  
	private Integer assignTag; //下端：派工标志  NUMERIC(8) 下端传以下值
	private String activityCode; //下端：活动编号  VARCHAR(15)  上端：  
	private String packageCode; //下端：组合代码  VARCHAR(30)  上端：  
	private String repairTypeCode; //下端：维修类型代码  CHAR(4)  上端：  
	private String modelLabourCode; //下端：维修车型分组代码  VARCHAR(15)  上端：  
	private String claimLabourCode; //下端：索赔工时代码  VARCHAR(15)  上端：  
	
	public String getClaimLabourName() {
		return claimLabourName;
	}
	public void setClaimLabourName(String claimLabourName) {
		this.claimLabourName = claimLabourName;
	}
	public String getChargePartitionCode() {
		return chargePartitionCode;
	}
	public void setChargePartitionCode(String chargePartitionCode) {
		this.chargePartitionCode = chargePartitionCode;
	}
	public String getTroubleCause() {
		return troubleCause;
	}
	public void setTroubleCause(String troubleCause) {
		this.troubleCause = troubleCause;
	}
	public String getTroubleDesc() {
		return troubleDesc;
	}
	public void setTroubleDesc(String troubleDesc) {
		this.troubleDesc = troubleDesc;
	}
	public String getLocalLabourCode() {
		return localLabourCode;
	}
	public void setLocalLabourCode(String localLabourCode) {
		this.localLabourCode = localLabourCode;
	}
	public String getLocalLabourName() {
		return localLabourName;
	}
	public void setLocalLabourName(String localLabourName) {
		this.localLabourName = localLabourName;
	}
	public String getLabourCode() {
		return labourCode;
	}
	public void setLabourCode(String labourCode) {
		this.labourCode = labourCode;
	}
	public Float getLabourPrice() {
		return labourPrice;
	}
	public void setLabourPrice(Float labourPrice) {
		this.labourPrice = labourPrice;
	}
	public String getLabourName() {
		return labourName;
	}
	public void setLabourName(String labourName) {
		this.labourName = labourName;
	}
	public Double getStdLabourHour() {
		return stdLabourHour;
	}
	public void setStdLabourHour(Double stdLabourHour) {
		this.stdLabourHour = stdLabourHour;
	}
	public Double getLabourAmount() {
		return labourAmount;
	}
	public void setLabourAmount(Double labourAmount) {
		this.labourAmount = labourAmount;
	}
	public String getManageSortCode() {
		return manageSortCode;
	}
	public void setManageSortCode(String manageSortCode) {
		this.manageSortCode = manageSortCode;
	}
	public String getTechnician() {
		return technician;
	}
	public void setTechnician(String technician) {
		this.technician = technician;
	}
	public String getWorkerTypeCode() {
		return workerTypeCode;
	}
	public void setWorkerTypeCode(String workerTypeCode) {
		this.workerTypeCode = workerTypeCode;
	}
	public String getRemark() {
		return Remark;
	}
	public void setRemark(String remark) {
		Remark = remark;
	}
	public Float getDiscount() {
		return discount;
	}
	public void setDiscount(Float discount) {
		this.discount = discount;
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
	public Integer getPreCheck() {
		return preCheck;
	}
	public void setPreCheck(Integer preCheck) {
		this.preCheck = preCheck;
	}
	public Integer getConsignExterior() {
		return consignExterior;
	}
	public void setConsignExterior(Integer consignExterior) {
		this.consignExterior = consignExterior;
	}
	public Double getAssignLabourHour() {
		return assignLabourHour;
	}
	public void setAssignLabourHour(Double assignLabourHour) {
		this.assignLabourHour = assignLabourHour;
	}
	public Integer getAssignTag() {
		return assignTag;
	}
	public void setAssignTag(Integer assignTag) {
		this.assignTag = assignTag;
	}
	public String getActivityCode() {
		return activityCode;
	}
	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}
	public String getPackageCode() {
		return packageCode;
	}
	public void setPackageCode(String packageCode) {
		this.packageCode = packageCode;
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
	public String getClaimLabourCode() {
		return claimLabourCode;
	}
	public void setClaimLabourCode(String claimLabourCode) {
		this.claimLabourCode = claimLabourCode;
	}

}

