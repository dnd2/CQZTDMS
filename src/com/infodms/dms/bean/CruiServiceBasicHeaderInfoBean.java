package com.infodms.dms.bean;

import com.infoservice.po3.bean.PO;

public class CruiServiceBasicHeaderInfoBean extends PO{
	private static final long serialVersionUID = 770876154367454463L;
	
	private Long id;
	private String cr_no;
	private String create_by;
	private String make_date;
	private String dealer_name;
	private String cr_day;
	private String cr_whither;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCr_no() {
		return cr_no;
	}
	public void setCr_no(String cr_no) {
		this.cr_no = cr_no;
	}
	public String getCreate_by() {
		return create_by;
	}
	public void setCreate_by(String create_by) {
		this.create_by = create_by;
	}
	public String getMake_date() {
		return make_date;
	}
	public void setMake_date(String make_date) {
		this.make_date = make_date;
	}
	public String getDealer_name() {
		return dealer_name;
	}
	public void setDealer_name(String dealer_name) {
		this.dealer_name = dealer_name;
	}
	public String getCr_day() {
		return cr_day;
	}
	public void setCr_day(String cr_day) {
		this.cr_day = cr_day;
	}
	public String getCr_whither() {
		return cr_whither;
	}
	public void setCr_whither(String cr_whither) {
		this.cr_whither = cr_whither;
	}
}
