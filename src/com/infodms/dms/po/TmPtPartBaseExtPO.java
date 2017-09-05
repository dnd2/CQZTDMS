package com.infodms.dms.po;

public class TmPtPartBaseExtPO extends TmPtPartBasePO{
	
	private String supplierCode;
	private String supplierName;
	private Integer fore;
	private Integer isSpec;
	private Double  claimPriceParam;
	private String partLevel;
	private String roNo;
	
	public String getRoNo() {
		return roNo;
	}

	public void setRoNo(String roNo) {
		this.roNo = roNo;
	}

	public String getPartLevel() {
		return partLevel;
	}

	public void setPartLevel(String partLevel) {
		this.partLevel = partLevel;
	}

	public Double getClaimPriceParam() {
		return claimPriceParam;
	}

	public void setClaimPriceParam(Double claimPriceParam) {
		this.claimPriceParam = claimPriceParam;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public Integer getFore() {
		return fore;
	}

	public void setFore(Integer fore) {
		this.fore = fore;
	}

	public Integer getIsSpec() {
		return isSpec;
	}

	public void setIsSpec(Integer isSpec) {
		this.isSpec = isSpec;
	}
	
	

}
