/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2015-03-12 16:51:19
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmpVehicleCertificatePledgePO extends PO{

	private Integer rowno;
	private Long dealerId;
	private Integer accType;
	private String accNo;
	private Long vehicleId;
	private Date zhiyaShijian;
	private Double amount;
	private String dealerName;
	private String accTypeName;
	private String vin;
	private Integer dealerVehicleXuhao;
	
	public Integer getRowno() {
		return rowno;
	}
	public void setRowno(Integer rowno) {
		this.rowno = rowno;
	}
	public Long getDealerId() {
		return dealerId;
	}
	public void setDealerId(Long dealerId) {
		this.dealerId = dealerId;
	}
	public Integer getAccType() {
		return accType;
	}
	public void setAccType(Integer accType) {
		this.accType = accType;
	}
	public String getAccNo() {
		return accNo;
	}
	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}
	public Long getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(Long vehicleId) {
		this.vehicleId = vehicleId;
	}
	public Date getZhiyaShijian() {
		return zhiyaShijian;
	}
	public void setZhiyaShijian(Date zhiyaShijian) {
		this.zhiyaShijian = zhiyaShijian;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getDealerName() {
		return dealerName;
	}
	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}
	public String getAccTypeName() {
		return accTypeName;
	}
	public void setAccTypeName(String accTypeName) {
		this.accTypeName = accTypeName;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public Integer getDealerVehicleXuhao() {
		return dealerVehicleXuhao;
	}
	public void setDealerVehicleXuhao(Integer dealerVehicleXuhao) {
		this.dealerVehicleXuhao = dealerVehicleXuhao;
	}
	
}