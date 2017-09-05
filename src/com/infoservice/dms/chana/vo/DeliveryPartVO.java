package com.infoservice.dms.chana.vo;

@SuppressWarnings("serial")
public class DeliveryPartVO extends BaseVO {
	private String orderNo; //下端：订单编号 ORDER_NO CHAR(12) 截后12位 上端：ORDER_NO VARCHAR2(30) 
	private String partNo; //下端：配件代码 PART_NO VARCHAR(27)  上端：PART_CODE  
	private String PartName; //下端：配件名称 PART_NAME VARCHAR(120)  上端：PART_NAME  
	private String UnitName; //下端：计量单位名称 UNIT_NAME VARCHAR(12)  上端：UNIT  
	private Double supplyQty; //下端：供应数量 SUPPLY_QTY NUMERIC(12,2) 实到数量 上端：COUNT  
	private String oriOrderPart; //下端：原始订货配件 ORI_ORDER_PART VARCHAR(27)  上端：  
	private Double count; //下端：订货数量 COUNT NUMERIC(10,2)  上端：ORDER_COUNT  
	private String caseNo; //下端：包装箱号 CASE_NO VARCHAR(90)  上端：CARTON_NO  
	private Float tax; //下端：税率 TAX NUMERIC(3,2) 下端默认取参数 上端：  
	private Double taxAmount; //下端：税额 TAX_AMOUNT NUMERIC(12,2) 下端计算 上端：  
	private Double amount; //下端：金额 AMOUNT NUMERIC(12,2)  上端：TOTAL_PRICE  
	private String remark; //下端：备注 REMARK VARCHAR(300)  上端：  
	private Double instructPrice; //下端：阳光价 INSTRUCT_PRICE NUMERIC(10,2) 建议销售价 上端：INSTRUCT_PRICE  
	private Double planPrice; //下端：中心计划价（采购价） PLAN_PRICE NUMERIC(10,2)  上端：PRICE  
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getPartNo() {
		return partNo;
	}
	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}
	public String getPartName() {
		return PartName;
	}
	public void setPartName(String partName) {
		PartName = partName;
	}
	public String getUnitName() {
		return UnitName;
	}
	public void setUnitName(String unitName) {
		UnitName = unitName;
	}
	public Double getSupplyQty() {
		return supplyQty;
	}
	public void setSupplyQty(Double supplyQty) {
		this.supplyQty = supplyQty;
	}
	public String getOriOrderPart() {
		return oriOrderPart;
	}
	public void setOriOrderPart(String oriOrderPart) {
		this.oriOrderPart = oriOrderPart;
	}
	public Double getCount() {
		return count;
	}
	public void setCount(Double count) {
		this.count = count;
	}
	public String getCaseNo() {
		return caseNo;
	}
	public void setCaseNo(String caseNo) {
		this.caseNo = caseNo;
	}
	public Float getTax() {
		return tax;
	}
	public void setTax(Float tax) {
		this.tax = tax;
	}
	public Double getTaxAmount() {
		return taxAmount;
	}
	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Double getInstructPrice() {
		return instructPrice;
	}
	public void setInstructPrice(Double instructPrice) {
		this.instructPrice = instructPrice;
	}
	public Double getPlanPrice() {
		return planPrice;
	}
	public void setPlanPrice(Double planPrice) {
		this.planPrice = planPrice;
	}
}
