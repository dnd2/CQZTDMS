package com.infoservice.dms.chana.vo;

import java.util.LinkedList;

/**
 * 
 * @ClassName     : PtDeliverpDetailVO 
 * @Description   : 下发货运单明细VO
 * @author        : luole
 * CreateDate     : 2013-5-28
 */
@SuppressWarnings("serial")
public class PtDeliverpDetailVO extends BaseVO{
	private String partNo;// 上端：发运日期  VARCHAR(27) 下端：
	private String partName;// 上端：配件名称      VARCHAR(120)  下端：
	private String unitName;// 上端：计量单位名称         VARCHAR(12)	下端：
	private Double supplyQty;// 上端：供应数量	  NUMERIC(12,2)  下端：
	private Double count;// 上端：订货数量	NUMERIC(10,2)	 下端：
	private String caseNo;// 上端：包装箱号	  VARCHAR(90)	 下端：
	private Double tax; // 上端：税率	 NUMERIC(3,2) 下端：
	private Double taxAmount;// 上端：税额		NUMERIC(12,2)  下端：
	private Double amount;// 上端：金额（不含税）		NUMERIC(12,2) 下端：
	private String remark;// 上端：备注	   VARCHAR(400)  下端：
	private Integer sort;// 上端：排序	  NUMERIC(8)  下端：
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
	public Double getSupplyQty() {
		return supplyQty;
	}
	public void setSupplyQty(Double supplyQty) {
		this.supplyQty = supplyQty;
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
	public Double getTax() {
		return tax;
	}
	public void setTax(Double tax) {
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
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
}
