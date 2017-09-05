package com.infoservice.dms.chana.vo;

import java.util.Date;

/**
 * @Title: PartInfoVO.java
 *
 * @Description:CHANADMS
 *
 * @Copyright: Copyright (c) 2010
 *
 * @Company: www.infoservice.com.cn
 * @Date: 2010-7-28
 *
 * @author lishuai 
 * @mail   lishuai103@yahoo.cn	
 * @version 1.0
 * @remark 
 */
@SuppressWarnings("serial")
public class PartInfoVO extends BaseVO {
	private String partNo; //下端：配件代码  VARCHAR(27)  上端：PART_CODE VARCHAR2(30) 
	private String partName; //下端：配件名称  VARCHAR(120)  上端：PART_NAME VARCHAR2(100) 
	private String partNameEn; //下端：配件英文名  VARCHAR(120)  上端：  
	private String spellCode; //下端：拼音代码  VARCHAR(30)  上端：  
	private Integer partGroupCode; //下端：配件类别  NUMERIC(8)  上端：PART_TYPE VARCHAR2(60) 
	private String unitName; //下端：计量单位名称  VARCHAR(12)  上端：UNIT VARCHAR2(20) 
	private String unitCode; //下端：计量单位代码  CHAR(4)  上端：  
	private String optionRelation; //下端：替代关系  VARCHAR(20)  上端：CHANGE_CODE VARCHAR2(30) 
	private String optionNo; //下端：替代配件  VARCHAR(27)  上端：REPLACE_PART_ID NUMBER(16) 根据ID查询code
	private String brand; //下端：品牌  VARCHAR(30)  上端：GROUP_NAME VARCHAR2(300) 根据group_id查询
	private String series; //下端：车系  VARCHAR(30)  上端：GROUP_NAME VARCHAR2(300) 根据group_id查询
	private String partModelGroupCodeSet; //下端：配件车型组集  VARCHAR(90)  上端：  
	private String quantityPerCar; //下端：单车用量  VARCHAR(20)  上端：CAR_AMOUNT NUMBER(5) 
	private Double nodePrice; //下端：网点价  NUMERIC(10,2)  上端：  
	private Double planPrice; //下端：中心计划价（采购价）  NUMERIC(10,2)  上端：STOCK_PRICE NUMBER(8,2) 
	private Double claimPrice; //下端：索赔价  NUMERIC(10,2)  上端：CLAIM_PRICE NUMBER(8,2) 
	private Double limitPrice; //下端：销售限价  NUMERIC(10,2)  上端：  
	private Double oemLimitPrice; //下端：OEM销售限价  NUMERIC(10,2)  上端：  
	private Double regularPrice; //下端：常规订单价格  NUMERIC(10,2)  上端：  
	private Double urgentPrice; //下端：急件价格  NUMERIC(10,2)  上端：  
	private Double instructPrice; //下端：阳光价  NUMERIC(10,2)  上端：  
	private Double insurancePrice; //下端：保险价  NUMERIC(10,2)  上端：  
	private String remark; //下端：备注  VARCHAR(300)  上端：REMARK VARCHAR2(200) 
	private Float maxStock; //下端：最大库存  NUMERIC(8,2)  上端：  
	private Float minStock; //下端：最小库存  NUMERIC(8,2)  上端：  
	private Integer leadTime; //下端：订货周期  NUMERIC(3)  上端：CYCLE_TYPE NUMBER(8) 
	private String providerCode; //下端：供应商代码  VARCHAR(300)  上端：SUPPLIER_CODE VARCHAR2(30) 
	private Integer partStatus; //下端：是否停用  NUMERIC(8)  上端：STOP_FLAG NUMBER(1) 0正常，1停用
	private Integer isUnsafe; //下端：是否危险品  NUMERIC(8)  上端：  
	private Double minPackage; //下端：最小包装数  NUMERIC(10,2)  上端：MINI_PACK NUMBER(5) 
	private String orderType; //下端：配件订单类型  VARCHAR(60)  上端：ORDER_TYPE NUMBER(8) 在主数据下发里要订单类型做什么
	private String productingArea; //下端：产地  VARCHAR(150)  上端：  
	private Integer mainOrderType; //下端：订单分类  NUMERIC(8)  上端：  
	private String partProductCode; //下端：配件产品代码  VARCHAR(4)  上端：  和配件代码有什么区别
	private Integer partMainType; //下端：配件大类  NUMERIC(8)  上端：  
	private String fromEntity; //下端：数据来源  CHAR(8)  上端：  
	private Integer isFreeze; //下端：是否冻结  NUMERIC(8)  上端：  
	private Integer downTag; //下端：下发标志  NUMERIC(8)  上端：  
	private String oriProCode; //下端：原产地代码  VARCHAR(2)  上端：  
	private Integer abcType; //下端：ABC分类  NUMERIC(8)  上端：  
	private Integer isThings; //下端：是否用品  NUMERIC(8)  上端：  
	private Integer isOil; //下端：是否油品  NUMERIC(8)  上端：  
	private Integer isSpecial; //下端：是否特殊件  NUMERIC(8)  上端：  
	
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
	public String getPartNameEn() {
		return partNameEn;
	}
	public void setPartNameEn(String partNameEn) {
		this.partNameEn = partNameEn;
	}
	public String getSpellCode() {
		return spellCode;
	}
	public void setSpellCode(String spellCode) {
		this.spellCode = spellCode;
	}
	public Integer getPartGroupCode() {
		return partGroupCode;
	}
	public void setPartGroupCode(Integer partGroupCode) {
		this.partGroupCode = partGroupCode;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public String getUnitCode() {
		return unitCode;
	}
	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}
	public String getOptionRelation() {
		return optionRelation;
	}
	public void setOptionRelation(String optionRelation) {
		this.optionRelation = optionRelation;
	}
	public String getOptionNo() {
		return optionNo;
	}
	public void setOptionNo(String optionNo) {
		this.optionNo = optionNo;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getSeries() {
		return series;
	}
	public void setSeries(String series) {
		this.series = series;
	}
	public String getPartModelGroupCodeSet() {
		return partModelGroupCodeSet;
	}
	public void setPartModelGroupCodeSet(String partModelGroupCodeSet) {
		this.partModelGroupCodeSet = partModelGroupCodeSet;
	}
	public String getQuantityPerCar() {
		return quantityPerCar;
	}
	public void setQuantityPerCar(String quantityPerCar) {
		this.quantityPerCar = quantityPerCar;
	}
	public Double getNodePrice() {
		return nodePrice;
	}
	public void setNodePrice(Double nodePrice) {
		this.nodePrice = nodePrice;
	}
	public Double getPlanPrice() {
		return planPrice;
	}
	public void setPlanPrice(Double planPrice) {
		this.planPrice = planPrice;
	}
	public Double getClaimPrice() {
		return claimPrice;
	}
	public void setClaimPrice(Double claimPrice) {
		this.claimPrice = claimPrice;
	}
	public Double getLimitPrice() {
		return limitPrice;
	}
	public void setLimitPrice(Double limitPrice) {
		this.limitPrice = limitPrice;
	}
	public Double getOemLimitPrice() {
		return oemLimitPrice;
	}
	public void setOemLimitPrice(Double oemLimitPrice) {
		this.oemLimitPrice = oemLimitPrice;
	}
	public Double getRegularPrice() {
		return regularPrice;
	}
	public void setRegularPrice(Double regularPrice) {
		this.regularPrice = regularPrice;
	}
	public Double getUrgentPrice() {
		return urgentPrice;
	}
	public void setUrgentPrice(Double urgentPrice) {
		this.urgentPrice = urgentPrice;
	}
	public Double getInstructPrice() {
		return instructPrice;
	}
	public void setInstructPrice(Double instructPrice) {
		this.instructPrice = instructPrice;
	}
	public Double getInsurancePrice() {
		return insurancePrice;
	}
	public void setInsurancePrice(Double insurancePrice) {
		this.insurancePrice = insurancePrice;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Float getMaxStock() {
		return maxStock;
	}
	public void setMaxStock(Float maxStock) {
		this.maxStock = maxStock;
	}
	public Float getMinStock() {
		return minStock;
	}
	public void setMinStock(Float minStock) {
		this.minStock = minStock;
	}
	public Integer getLeadTime() {
		return leadTime;
	}
	public void setLeadTime(Integer leadTime) {
		this.leadTime = leadTime;
	}
	public String getProviderCode() {
		return providerCode;
	}
	public void setProviderCode(String providerCode) {
		this.providerCode = providerCode;
	}
	public Integer getPartStatus() {
		return partStatus;
	}
	public void setPartStatus(Integer partStatus) {
		this.partStatus = partStatus;
	}
	public Integer getIsUnsafe() {
		return isUnsafe;
	}
	public void setIsUnsafe(Integer isUnsafe) {
		this.isUnsafe = isUnsafe;
	}
	public Double getMinPackage() {
		return minPackage;
	}
	public void setMinPackage(Double minPackage) {
		this.minPackage = minPackage;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getProductingArea() {
		return productingArea;
	}
	public void setProductingArea(String productingArea) {
		this.productingArea = productingArea;
	}
	public Integer getMainOrderType() {
		return mainOrderType;
	}
	public void setMainOrderType(Integer mainOrderType) {
		this.mainOrderType = mainOrderType;
	}
	public String getPartProductCode() {
		return partProductCode;
	}
	public void setPartProductCode(String partProductCode) {
		this.partProductCode = partProductCode;
	}
	public Integer getPartMainType() {
		return partMainType;
	}
	public void setPartMainType(Integer partMainType) {
		this.partMainType = partMainType;
	}
	public String getFromEntity() {
		return fromEntity;
	}
	public void setFromEntity(String fromEntity) {
		this.fromEntity = fromEntity;
	}
	public Integer getIsFreeze() {
		return isFreeze;
	}
	public void setIsFreeze(Integer isFreeze) {
		this.isFreeze = isFreeze;
	}
	public Integer getDownTag() {
		return downTag;
	}
	public void setDownTag(Integer downTag) {
		this.downTag = downTag;
	}
	public String getOriProCode() {
		return oriProCode;
	}
	public void setOriProCode(String oriProCode) {
		this.oriProCode = oriProCode;
	}
	public Integer getAbcType() {
		return abcType;
	}
	public void setAbcType(Integer abcType) {
		this.abcType = abcType;
	}
	public Integer getIsThings() {
		return isThings;
	}
	public void setIsThings(Integer isThings) {
		this.isThings = isThings;
	}
	public Integer getIsOil() {
		return isOil;
	}
	public void setIsOil(Integer isOil) {
		this.isOil = isOil;
	}
	public Integer getIsSpecial() {
		return isSpecial;
	}
	public void setIsSpecial(Integer isSpecial) {
		this.isSpecial = isSpecial;
	}
	
}
