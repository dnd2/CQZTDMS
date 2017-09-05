package com.infoservice.dms.chana.vo;

public class ActivityPartVO extends BaseVO {
	private String partNo; //下端：配件代码  varchar(27)  上端：PART_NO VARCHAR(27) 
	private String partName; //下端：配件名称  varchar(120)  上端：PART_NAME VARCHAR2(100) 
	private String unitName; //下端：计量单位  varchar(12)  上端：PART_UNIT VARCHAR2(10) 
	private Float partQuantity; //下端：数量  NUMERIC(8,2)  上端：PART_QUANTITY NUMBER(8,2) 
	private Double partSalesPrice; //下端：单价  NUMERIC(10,2)  上端：PART_PRICE NUMBER(8,2) 
	private Double partSalesAmount; //下端：金额  NUMERIC(12,2)  上端：PART_AMOUNT NUMBER(8,2) 
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
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public Float getPartQuantity() {
		return partQuantity;
	}
	public void setPartQuantity(Float partQuantity) {
		this.partQuantity = partQuantity;
	}
	public Double getPartSalesPrice() {
		return partSalesPrice;
	}
	public void setPartSalesPrice(Double partSalesPrice) {
		this.partSalesPrice = partSalesPrice;
	}
	public Double getPartSalesAmount() {
		return partSalesAmount;
	}
	public void setPartSalesAmount(Double partSalesAmount) {
		this.partSalesAmount = partSalesAmount;
	}

}
