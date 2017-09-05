package com.infodms.dms.bean;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class SpefeeBaseBean extends PO {
	private Long id;
	private String feeNo;
	private String vin;
	private Integer feeType;
	private Integer status;
	private String partCode;
	private String partName;
	private String supplyCode;
	private String supplyName;
	private String modelCode;
	private Double claimPrice;
	private String tel;
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFeeNo() {
		return feeNo;
	}
	public void setFeeNo(String feeNo) {
		this.feeNo = feeNo;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public Integer getFeeType() {
		return feeType;
	}
	public void setFeeType(Integer feeType) {
		this.feeType = feeType;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getPartCode() {
		return partCode;
	}
	public void setPartCode(String partCode) {
		this.partCode = partCode;
	}
	public String getPartName() {
		return partName;
	}
	public void setPartName(String partName) {
		this.partName = partName;
	}
	public String getSupplyCode() {
		return supplyCode;
	}
	public void setSupplyCode(String supplyCode) {
		this.supplyCode = supplyCode;
	}
	public String getSupplyName() {
		return supplyName;
	}
	public void setSupplyName(String supplyName) {
		this.supplyName = supplyName;
	}
	public String getModelCode() {
		return modelCode;
	}
	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}
	public Double getClaimPrice() {
		return claimPrice;
	}
	public void setClaimPrice(Double claimPrice) {
		this.claimPrice = claimPrice;
	}
	
	
}
