package com.infodms.dms.bean;

import com.infoservice.po3.bean.PO;

public class SpeFeeDetailInfoBean extends PO{

	private static final long serialVersionUID = 5180749717968482911L;
	
    private Long id;
    private String dealer_code;
    private String dealer_name;
    private String fee_no;
    private Long cr_id;
    private String start_date;
    private String end_date;
    private String out_days;
    private String person_num;
    private String person_name;
    private String single_mileage;
    private String pass_fee;
    private String traffic_fee;
    private String quarter_fee;
    private String eat_fee;
    private String person_subside;
    private String apply_content;
    private String total_fee;
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
	public String getFee_no() {
		return fee_no;
	}
	public void setFee_no(String fee_no) {
		this.fee_no = fee_no;
	}
	public Long getCr_id() {
		return cr_id;
	}
	public void setCr_id(Long cr_id) {
		this.cr_id = cr_id;
	}
	public String getStart_date() {
		return start_date;
	}
	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}
	public String getEnd_date() {
		return end_date;
	}
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}
	public String getPerson_num() {
		return person_num;
	}
	public void setPerson_num(String person_num) {
		this.person_num = person_num;
	}
	public String getPerson_name() {
		return person_name;
	}
	public void setPerson_name(String person_name) {
		this.person_name = person_name;
	}
	public String getSingle_mileage() {
		return single_mileage;
	}
	public void setSingle_mileage(String single_mileage) {
		this.single_mileage = single_mileage;
	}
	public String getPass_fee() {
		return pass_fee;
	}
	public void setPass_fee(String pass_fee) {
		this.pass_fee = pass_fee;
	}
	public String getTraffic_fee() {
		return traffic_fee;
	}
	public void setTraffic_fee(String traffic_fee) {
		this.traffic_fee = traffic_fee;
	}
	public String getQuarter_fee() {
		return quarter_fee;
	}
	public void setQuarter_fee(String quarter_fee) {
		this.quarter_fee = quarter_fee;
	}
	public String getEat_fee() {
		return eat_fee;
	}
	public void setEat_fee(String eat_fee) {
		this.eat_fee = eat_fee;
	}
	public String getPerson_subside() {
		return person_subside;
	}
	public void setPerson_subside(String person_subside) {
		this.person_subside = person_subside;
	}
	public String getApply_content() {
		return apply_content;
	}
	public void setApply_content(String apply_content) {
		this.apply_content = apply_content;
	}
	public String getTotal_fee() {
		return total_fee;
	}
	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}
	public String getOut_days() {
		return out_days;
	}
	public void setOut_days(String out_days) {
		this.out_days = out_days;
	}
	
}
