package com.infodms.dms.bean;

import com.infoservice.po3.bean.PO;

public class SpeFeeConfirmInfoListBean extends PO{

	private static final long serialVersionUID = 6234909720252594192L;
	
	private Long id;
	private Long cr_id;
	private String fee_no;
	private String cr_no;
	private String dealer_code;
	private String dealer_name;
	private String make_date;
	private String cr_whither;
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
	public String getTotal_fee() {
		return total_fee;
	}
	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}
}
