package com.infodms.dms.po;

import java.util.Date;

public class TmVehicleExtPO extends TmVehiclePO{
	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	
	
	
	private static final long serialVersionUID = 8335283925891322096L;
	private String customerName;//车主姓名
	private String brandName;  //品牌
	private String seriesName; //车系
	private String modelName;  //车型
	private String brandCode;  //品牌
	private String seriesCode; //车系
	private String modelCode;  //车型
	private String ctmName;
	private String address;
	private String mainPhone;
	private String otherPhone;
	private String ruleCode;
	private String carUseDesc;
	private Integer carUseType;
	/********zhumingwei 2011-6-9******/
	private String packageName;  //车型
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	/********zhumingwei 2011-6-9******/
	private Date purchasedDateAct; //实销表中的销售日期
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getSeriesName() {
		return seriesName;
	}
	public void setSeriesName(String seriesName) {
		this.seriesName = seriesName;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	/** 
	 * @return brandCode 
	 */
	public String getBrandCode() {
		return brandCode;
	}
	/** 
	 * @param brandCode 要设置的 brandCode 
	 */
	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}
	/** 
	 * @return modelCode 
	 */
	public String getModelCode() {
		return modelCode;
	}
	/** 
	 * @param modelCode 要设置的 modelCode 
	 */
	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}
	/** 
	 * @return seriesCode 
	 */
	public String getSeriesCode() {
		return seriesCode;
	}
	/** 
	 * @param seriesCode 要设置的 seriesCode 
	 */
	public void setSeriesCode(String seriesCode) {
		this.seriesCode = seriesCode;
	}
	public Date getPurchasedDateAct() {
		return purchasedDateAct;
	}
	public void setPurchasedDateAct(Date purchasedDateAct) {
		this.purchasedDateAct = purchasedDateAct;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	public String getCtmName() {
		return ctmName;
	}
	public void setCtmName(String ctmName) {
		this.ctmName = ctmName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getMainPhone() {
		return mainPhone;
	}
	public void setMainPhone(String mainPhone) {
		this.mainPhone = mainPhone;
	}
	public String getOtherPhone() {
		return otherPhone;
	}
	public void setOtherPhone(String otherPhone) {
		this.otherPhone = otherPhone;
	}
	public String getRuleCode() {
		return ruleCode;
	}
	public void setRuleCode(String ruleCode) {
		this.ruleCode = ruleCode;
	}
	public String getCarUseDesc() {
		return carUseDesc;
	}
	public void setCarUseDesc(String carUseDesc) {
		this.carUseDesc = carUseDesc;
	}
	public Integer getCarUseType() {
		return carUseType;
	}
	public void setCarUseType(Integer carUseType) {
		this.carUseType = carUseType;
	}
	
	
}
