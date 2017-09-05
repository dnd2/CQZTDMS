package com.infodms.dms.bean;
/**
 * 
 * @ClassName     : ClientSearchCondition 
 * @Description   : 客户信息查询条件封装 
 * @author        : Administrator
 * CreateDate     : 2013-4-9
 */
public class ClientSearchCondition {
	
	private String purchasedDateStart;
	private String purchasedDateEnd;
	private String ctmName;
	private String tel;
	private String yieldly;
	private String series;
	private String model;
	private String engineNo;
	private String vin;
	private String guestStars;
	private String province;
	private String city;
	private String use;
	
	public String getUse() {
		return use;
	}
	public void setUse(String use) {
		this.use = use;
	}
	public String getPurchasedDateStart() {
		return purchasedDateStart;
	}
	public void setPurchasedDateStart(String purchasedDateStart) {
		this.purchasedDateStart = purchasedDateStart;
	}
	public String getPurchasedDateEnd() {
		return purchasedDateEnd;
	}
	public void setPurchasedDateEnd(String purchasedDateEnd) {
		this.purchasedDateEnd = purchasedDateEnd;
	}
	public String getCtmName() {
		return ctmName;
	}
	public void setCtmName(String ctmName) {
		this.ctmName = ctmName;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getYieldly() {
		return yieldly;
	}
	public void setYieldly(String yieldly) {
		this.yieldly = yieldly;
	}
	public String getSeries() {
		return series;
	}
	public void setSeries(String series) {
		this.series = series;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getEngineNo() {
		return engineNo;
	}
	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public String getGuestStars() {
		return guestStars;
	}
	public void setGuestStars(String guestStars) {
		this.guestStars = guestStars;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
}
