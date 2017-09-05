/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-10-27 13:50:26
* CreateBy   : wizard_lee
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmpVehicleRebatePO extends PO{
	
	private Integer rowno;
	private Long vehicleId;
	private Date rebateDate;
	private Long dealerId;
	private String dealerCode;
	private String dealerName;
	private String vin;
	private Double rebateAmount;
	private Long isHaveRebate;
	
	
	public Integer getRowno() {
		return rowno;
	}
	public void setRowno(Integer rowno) {
		this.rowno = rowno;
	}
	public Long getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(Long vehicleId) {
		this.vehicleId = vehicleId;
	}
	public Date getRebateDate() {
		return rebateDate;
	}
	public void setRebateDate(Date rebateDate) {
		this.rebateDate = rebateDate;
	}
	public Long getDealerId() {
		return dealerId;
	}
	public void setDealerId(Long dealerId) {
		this.dealerId = dealerId;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public Double getRebateAmount() {
		return rebateAmount;
	}
	public void setRebateAmount(Double rebateAmount) {
		this.rebateAmount = rebateAmount;
	}
	public Long getIsHaveRebate() {
		return isHaveRebate;
	}
	public void setIsHaveRebate(Long isHaveRebate) {
		this.isHaveRebate = isHaveRebate;
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
	
}