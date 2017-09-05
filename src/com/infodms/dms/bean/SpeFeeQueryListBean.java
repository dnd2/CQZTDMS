package com.infodms.dms.bean;

import com.infoservice.po3.bean.PO;

public class SpeFeeQueryListBean extends PO{

	private static final long serialVersionUID = 807687949577257114L;
	
	private Long id;
	private Long cr_id;
	private String dealer_code;
	private String dealer_name;
	private String fee_no;
	private String cr_no;
	private String make_date;
	private String cr_whither;
	private String status_desc;
	private String total_fee;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getCr_id() {
		return cr_id;
	}
	public void setCr_id(Long cr_id) {
		this.cr_id = cr_id;
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
	public String getFee_no() {
		return fee_no;
	}
	public void setFee_no(String fee_no) {
		this.fee_no = fee_no;
	}
	public String getCr_no() {
		return cr_no;
	}
	public void setCr_no(String cr_no) {
		this.cr_no = cr_no;
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
	public String getStatus_desc() {
		return status_desc;
	}
	public void setStatus_desc(String status_desc) {
		this.status_desc = status_desc;
	}
	public String getTotal_fee() {
		return total_fee;
	}
	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}
}
