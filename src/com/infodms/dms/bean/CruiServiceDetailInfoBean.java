package com.infodms.dms.bean;

import com.infoservice.po3.bean.PO;

public class CruiServiceDetailInfoBean extends PO{

	private static final long serialVersionUID = -1035887134655531060L;

	private Long id;
	private String dealer_code;
	private String dealer_name;
	private String make_date;
	private String cr_whither;
	private String cr_mileage;
	private String cr_day;
	private String cr_principal;
	private String cr_phone;
	private String cr_cause;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDealer_code() {
		return dealer_code;
	}
	public void setDealer_code(String dealer_code) {
		this.dealer_code = dealer_code;
	}
	public String getDealer_name() {
		return dealer_name;
	}
	public void setDealer_name(String dealer_name) {
		this.dealer_name = dealer_name;
	}
	public String getMake_date() {
		return make_date;
	}
	public void setMake_date(String make_date) {
		this.make_date = make_date;
	}
	public String getCr_whither() {
		return cr_whither;
	}
	public void setCr_whither(String cr_whither) {
		this.cr_whither = cr_whither;
	}
	public String getCr_mileage() {
		return cr_mileage;
	}
	public void setCr_mileage(String cr_mileage) {
		this.cr_mileage = cr_mileage;
	}
	public String getCr_day() {
		return cr_day;
	}
	public void setCr_day(String cr_day) {
		this.cr_day = cr_day;
	}
	public String getCr_principal() {
		return cr_principal;
	}
	public void setCr_principal(String cr_principal) {
		this.cr_principal = cr_principal;
	}
	public String getCr_phone() {
		return cr_phone;
	}
	public void setCr_phone(String cr_phone) {
		this.cr_phone = cr_phone;
	}
	public String getCr_cause() {
		return cr_cause;
	}
	public void setCr_cause(String cr_cause) {
		this.cr_cause = cr_cause;
	}
}
