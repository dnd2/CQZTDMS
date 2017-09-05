package com.infodms.dms.bean;

import com.infoservice.po3.bean.PO;

public class NewPartBean extends PO {
	private String partCode ;
	private String partName ;
	private Integer isWarranty ;
	private Long claimMonth;
	private Double claimMelieage;
	private String typeName ;
	private String typeCode ;
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	public Long getClaimMonth() {
		return claimMonth;
	}
	public void setClaimMonth(Long claimMonth) {
		this.claimMonth = claimMonth;
	}
	public Double getClaimMelieage() {
		return claimMelieage;
	}
	public void setClaimMelieage(Double claimMelieage) {
		this.claimMelieage = claimMelieage;
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
	public Integer getIsWarranty() {
		return isWarranty;
	}
	public void setIsWarranty(Integer isWarranty) {
		this.isWarranty = isWarranty;
	}
	
}
