/**********************************************************************
 * <pre>
 * FILE : SvoOrderSearchBean.java
 * CLASS : SvoOrderSearchBean
 *
 * AUTHOR : lwj
 *
 * FUNCTION : TODO
 *
 *
 *======================================================================
 * CHANGE HISTORY LOG
 *----------------------------------------------------------------------
 * MOD. NO.|   DATE   |   NAME  | REASON  | CHANGE REQ.
 *----------------------------------------------------------------------
 * 		    |2009-12-17|   lwj   | Created |
 * DESCRIPTION:
 * </pre>
 ***********************************************************************/
package com.infodms.dms.bean;

import java.util.Date;

public class SvoOrderSearchBean {
	private String svoNo;
	private String brand;
	private String carModel;
	private String baseVehicleCode;
	private String baseVehicleName;
	private Double orderNum;
	private String createDate;
	private String preSubDate;
	private String appStatus;
	private Long svoId;
	private Long preSvoId;
	private String submitDate;
	private String submitDate1;
	private String orgName;
	private String colorName;
	private String linkMan;
	private String linkTel;
	private String specialRequest1;
	private String specialRequest2;
	private String specialRequest3;
	private String specialRequest4;
	private String specialRequest5;
	private String hopePrice1;
	private String hopePrice2;
	private String changeAim;
	private String productUnit;
	private String productId;
	private String carStyle;
	private String toCustomer;
	private String toSales;
	private String orderType;
	private String carModelId;
	private String changeTypeCode;
	private String agentOption;
	private String fxName;
	private String fxCode;
	private String priceOne;
	
	public String getAgentOption() {
		return agentOption;
	}
	public void setAgentOption(String agentOption) {
		this.agentOption = agentOption;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getColorName() {
		return colorName;
	}
	public void setColorName(String colorName) {
		this.colorName = colorName;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getSvoNo() {
		return svoNo;
	}
	public void setSvoNo(String svoNo) {
		this.svoNo = svoNo;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getCarModel() {
		return carModel;
	}
	public void setCarModel(String carModel) {
		this.carModel = carModel;
	}
	public String getBaseVehicleCode() {
		return baseVehicleCode;
	}
	public void setBaseVehicleCode(String baseVehicleCode) {
		this.baseVehicleCode = baseVehicleCode;
	}
	public String getBaseVehicleName() {
		return baseVehicleName;
	}
	public void setBaseVehicleName(String baseVehicleName) {
		this.baseVehicleName = baseVehicleName;
	}
	public Double getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(Double orderNum) {
		this.orderNum = orderNum;
	}
	public String getAppStatus() {
		return appStatus;
	}
	public void setAppStatus(String appStatus) {
		this.appStatus = appStatus;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public Long getSvoId() {
		return svoId;
	}
	public void setSvoId(Long svoId) {
		this.svoId = svoId;
	}
	public Long getPreSvoId() {
		return preSvoId;
	}
	public void setPreSvoId(Long preSvoId) {
		this.preSvoId = preSvoId;
	}
	public String getSubmitDate() {
		return submitDate;
	}
	public void setSubmitDate(String submitDate) {
		this.submitDate = submitDate;
	}
	public String getSubmitDate1() {
		return submitDate1;
	}
	public void setSubmitDate1(String submitDate1) {
		this.submitDate1 = submitDate1;
	}
	public String getLinkMan() {
		return linkMan;
	}
	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}
	public String getLinkTel() {
		return linkTel;
	}
	public void setLinkTel(String linkTel) {
		this.linkTel = linkTel;
	}
	public String getSpecialRequest1() {
		return specialRequest1;
	}
	public void setSpecialRequest1(String specialRequest1) {
		this.specialRequest1 = specialRequest1;
	}
	public String getSpecialRequest2() {
		return specialRequest2;
	}
	public void setSpecialRequest2(String specialRequest2) {
		this.specialRequest2 = specialRequest2;
	}
	public String getSpecialRequest3() {
		return specialRequest3;
	}
	public void setSpecialRequest3(String specialRequest3) {
		this.specialRequest3 = specialRequest3;
	}
	public String getSpecialRequest4() {
		return specialRequest4;
	}
	public void setSpecialRequest4(String specialRequest4) {
		this.specialRequest4 = specialRequest4;
	}
	public String getSpecialRequest5() {
		return specialRequest5;
	}
	public void setSpecialRequest5(String specialRequest5) {
		this.specialRequest5 = specialRequest5;
	}
	public String getHopePrice1() {
		return hopePrice1;
	}
	public void setHopePrice1(String hopePrice1) {
		this.hopePrice1 = hopePrice1;
	}
	public String getHopePrice2() {
		return hopePrice2;
	}
	public void setHopePrice2(String hopePrice2) {
		this.hopePrice2 = hopePrice2;
	}
	public String getChangeAim() {
		return changeAim;
	}
	public void setChangeAim(String changeAim) {
		this.changeAim = changeAim;
	}
	public String getProductUnit() {
		return productUnit;
	}
	public void setProductUnit(String productUnit) {
		this.productUnit = productUnit;
	}
	public String getToCustomer() {
		return toCustomer;
	}
	public void setToCustomer(String toCustomer) {
		this.toCustomer = toCustomer;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getCarStyle() {
		return carStyle;
	}
	public void setCarStyle(String carStyle) {
		this.carStyle = carStyle;
	}
	public String getCarModelId() {
		return carModelId;
	}
	public void setCarModelId(String carModelId) {
		this.carModelId = carModelId;
	}
	public String getChangeTypeCode() {
		return changeTypeCode;
	}
	public void setChangeTypeCode(String changeTypeCode) {
		this.changeTypeCode = changeTypeCode;
	}
	public String getToSales() {
		return toSales;
	}
	public void setToSales(String toSales) {
		this.toSales = toSales;
	}
	public String getFxName() {
		return fxName;
	}
	public void setFxName(String fxName) {
		this.fxName = fxName;
	}
	public String getFxCode() {
		return fxCode;
	}
	public void setFxCode(String fxCode) {
		this.fxCode = fxCode;
	}
	public String getPreSubDate() {
		return preSubDate;
	}
	public void setPreSubDate(String preSubDate) {
		this.preSubDate = preSubDate;
	}
	public String getPriceOne() {
		return priceOne;
	}
	public void setPriceOne(String priceOne) {
		this.priceOne = priceOne;
	}
}
