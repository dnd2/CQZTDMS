package com.infodms.dms.bean;

import com.infoservice.po3.bean.PO;

public class DeductClaimInfoListBean extends PO{

	private static final long serialVersionUID = 2500509306823627183L;
	
	private Long deduct_id;//
	private String deduct_no;//
	private Long claim_id;
	private String claim_no;//
	private String vin;//
	private String part_code;//
	private String part_name;//
	private Integer part_amount;//
	private Integer deduct_amount;//
	private String part_deduct_money;//
	private String hour_deduct_money;//
	private String other_deduct_money;//
	
	public Long getDeduct_id() {
		return deduct_id;
	}
	public void setDeduct_id(Long deduct_id) {
		this.deduct_id = deduct_id;
	}
	public String getDeduct_no() {
		return deduct_no;
	}
	public void setDeduct_no(String deduct_no) {
		this.deduct_no = deduct_no;
	}
	public String getClaim_no() {
		return claim_no;
	}
	public void setClaim_no(String claim_no) {
		this.claim_no = claim_no;
	}
	public String getPart_code() {
		return part_code;
	}
	public void setPart_code(String part_code) {
		this.part_code = part_code;
	}
	public String getPart_name() {
		return part_name;
	}
	public void setPart_name(String part_name) {
		this.part_name = part_name;
	}
	public Integer getPart_amount() {
		return part_amount;
	}
	public void setPart_amount(Integer part_amount) {
		this.part_amount = part_amount;
	}
	public Integer getDeduct_amount() {
		return deduct_amount;
	}
	public void setDeduct_amount(Integer deduct_amount) {
		this.deduct_amount = deduct_amount;
	}
	public String getPart_deduct_money() {
		return part_deduct_money;
	}
	public void setPart_deduct_money(String part_deduct_money) {
		this.part_deduct_money = part_deduct_money;
	}
	public String getHour_deduct_money() {
		return hour_deduct_money;
	}
	public void setHour_deduct_money(String hour_deduct_money) {
		this.hour_deduct_money = hour_deduct_money;
	}
	public String getOther_deduct_money() {
		return other_deduct_money;
	}
	public void setOther_deduct_money(String other_deduct_money) {
		this.other_deduct_money = other_deduct_money;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public Long getClaim_id() {
		return claim_id;
	}
	public void setClaim_id(Long claim_id) {
		this.claim_id = claim_id;
	}
}
