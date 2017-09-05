/*
 * Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
 * This software is published under the terms of the Infoservice Software
 * License version 1.0, a copy of which has been included with this
 * distribution in the LICENSE.txt file.
 *
 * CreateDate : 2008-06-26 13:55:40
 * CreateBy   : Ks
 * Comment    : generate by com.infoservice.po.POGen
 */

package com.infoservice.dms.chana.vo;

@SuppressWarnings("serial")
public class RoManageVO extends BaseVO {

	private String manageSortCode; //下端：收费类别代码  VARCHAR(4)  上端：  
	private Double overItemAmount; //下端：辅料管理费  NUMERIC(10,2)  上端：  
	private Float labourRate; //下端：工时率  NUMERIC(3,2)  上端：  
	private Float repairPartRate; //下端：维修材料费率  NUMERIC(3,2)  上端：  
	private Float salesPartRate; //下端：销售材料费率  NUMERIC(3,2)  上端：  
	private Float addItemRate; //下端：附加费率  NUMERIC(3,2)  上端：  
	private Float labourAmountRate; //下端：工时费率  NUMERIC(3,2)  上端：  
	private Float overheadExpensesRate; //下端：管理费率  NUMERIC(3,2)  上端：  
	private Integer isManaging; //下端：是否管理费  NUMERIC(8) 下端传以下值
	private Float discount; //下端：折扣率  NUMERIC(5,4)  上端：  
	public String getManageSortCode() {
		return manageSortCode;
	}
	public void setManageSortCode(String manageSortCode) {
		this.manageSortCode = manageSortCode;
	}
	public Double getOverItemAmount() {
		return overItemAmount;
	}
	public void setOverItemAmount(Double overItemAmount) {
		this.overItemAmount = overItemAmount;
	}
	public Float getLabourRate() {
		return labourRate;
	}
	public void setLabourRate(Float labourRate) {
		this.labourRate = labourRate;
	}
	public Float getRepairPartRate() {
		return repairPartRate;
	}
	public void setRepairPartRate(Float repairPartRate) {
		this.repairPartRate = repairPartRate;
	}
	public Float getSalesPartRate() {
		return salesPartRate;
	}
	public void setSalesPartRate(Float salesPartRate) {
		this.salesPartRate = salesPartRate;
	}
	public Float getAddItemRate() {
		return addItemRate;
	}
	public void setAddItemRate(Float addItemRate) {
		this.addItemRate = addItemRate;
	}
	public Float getLabourAmountRate() {
		return labourAmountRate;
	}
	public void setLabourAmountRate(Float labourAmountRate) {
		this.labourAmountRate = labourAmountRate;
	}
	public Float getOverheadExpensesRate() {
		return overheadExpensesRate;
	}
	public void setOverheadExpensesRate(Float overheadExpensesRate) {
		this.overheadExpensesRate = overheadExpensesRate;
	}
	public Integer getIsManaging() {
		return isManaging;
	}
	public void setIsManaging(Integer isManaging) {
		this.isManaging = isManaging;
	}
	public Float getDiscount() {
		return discount;
	}
	public void setDiscount(Float discount) {
		this.discount = discount;
	}

}