/**
 * Copyright (c) 2006-2008 OEM Infoservice Corp. 2006-2008,All Rights Reserved.
 * This software is published under the Infoservice DMS Service Inner Solution Team.
 * License version 1.0, a copy of which has been included with this
 * distribution in the LICENSE.txt file.
 *
 * @File name:  com.infoservice.dms.dc.vo.GmsRoAddItem.java
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

import java.util.Date;

@SuppressWarnings("serial")
public class RoAddItemVO extends BaseVO {

	private String manageSortCode; //下端：收费类别代码  VARCHAR(4)  上端：  
	private String addItemCode; //下端：附加项目代码  CHAR(4)  上端：  
	private String addItemName; //下端：附加项目名称  VARCHAR(90)  上端：  
	private Double addItemAmount; //下端：附加项目费  NUMERIC(12,2)  上端：  
	private String chargePartitionCode; //下端：收费区分代码  VARCHAR(4)  上端：  
	private String activityCode; //下端：活动编号  VARCHAR(15)  上端：  
	private String remark; //下端：备注  VARCHAR(300)  上端：  
	private Float discount; //下端：折扣率  NUMERIC(5,4)  上端：  
	public String getManageSortCode() {
		return manageSortCode;
	}
	public void setManageSortCode(String manageSortCode) {
		this.manageSortCode = manageSortCode;
	}
	public String getAddItemCode() {
		return addItemCode;
	}
	public void setAddItemCode(String addItemCode) {
		this.addItemCode = addItemCode;
	}
	public String getAddItemName() {
		return addItemName;
	}
	public void setAddItemName(String addItemName) {
		this.addItemName = addItemName;
	}
	public Double getAddItemAmount() {
		return addItemAmount;
	}
	public void setAddItemAmount(Double addItemAmount) {
		this.addItemAmount = addItemAmount;
	}
	public String getChargePartitionCode() {
		return chargePartitionCode;
	}
	public void setChargePartitionCode(String chargePartitionCode) {
		this.chargePartitionCode = chargePartitionCode;
	}
	public String getActivityCode() {
		return activityCode;
	}
	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Float getDiscount() {
		return discount;
	}
	public void setDiscount(Float discount) {
		this.discount = discount;
	}


}
