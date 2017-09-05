package com.infodms.dms.bean;

import com.infoservice.po3.bean.PO;

public class QueryCruiInfoBean extends PO{
	private static final long serialVersionUID = 6974026391003162647L;
	
	private Long id;
	private String dealer_code;
	private String dealer_name;
	private String cr_no;
	private String status;
	private String status_desc;
	private String make_date;
	private String cr_whither;
	private String cr_mileage;
	private String cr_day;
	private String audit_date;
	private String cr_principal;
	private String outfee_status;
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
	public String getCr_no() {
		return cr_no;
	}
	public void setCr_no(String cr_no) {
		this.cr_no = cr_no;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatus_desc() {
		return status_desc;
	}
	public void setStatus_desc(String status_desc) {
		this.status_desc = status_desc;
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
	public String getAudit_date() {
		return audit_date;
	}
	public void setAudit_date(String audit_date) {
		this.audit_date = audit_date;
	}
	public String getOutfee_status() {
		return outfee_status;
	}
	public void setOutfee_status(String outfee_status) {
		this.outfee_status = outfee_status;
	}
}
