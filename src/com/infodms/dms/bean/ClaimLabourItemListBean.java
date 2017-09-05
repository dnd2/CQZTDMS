package com.infodms.dms.bean;

import com.infoservice.po3.bean.PO;

public class ClaimLabourItemListBean extends PO{

	private static final long serialVersionUID = 7732427331169653586L;
	
	private Long labour_id;//工时id
	private String claim_no;//索赔单号
	private String wr_labourcode;//工时代码
	private String wr_labourname;//工时名称
	private Double labour_hours;//工时
	private Double labour_price;//工时单价
	private Double labour_amount;//工时金额
	private Double balance_amount;//已结算金额
	private Double deduct_amount;//已抵扣金额
	public Long getLabour_id() {
		return labour_id;
	}
	public void setLabour_id(Long labour_id) {
		this.labour_id = labour_id;
	}
	public String getClaim_no() {
		return claim_no;
	}
	public void setClaim_no(String claim_no) {
		this.claim_no = claim_no;
	}
	public String getWr_labourcode() {
		return wr_labourcode;
	}
	public void setWr_labourcode(String wr_labourcode) {
		this.wr_labourcode = wr_labourcode;
	}
	public String getWr_labourname() {
		return wr_labourname;
	}
	public void setWr_labourname(String wr_labourname) {
		this.wr_labourname = wr_labourname;
	}
	public Double getLabour_hours() {
		return labour_hours;
	}
	public void setLabour_hours(Double labour_hours) {
		this.labour_hours = labour_hours;
	}
	public Double getLabour_price() {
		return labour_price;
	}
	public void setLabour_price(Double labour_price) {
		this.labour_price = labour_price;
	}
	public Double getLabour_amount() {
		return labour_amount;
	}
	public void setLabour_amount(Double labour_amount) {
		this.labour_amount = labour_amount;
	}
	public Double getBalance_amount() {
		return balance_amount;
	}
	public void setBalance_amount(Double balance_amount) {
		this.balance_amount = balance_amount;
	}
	public Double getDeduct_amount() {
		return deduct_amount;
	}
	public void setDeduct_amount(Double deduct_amount) {
		this.deduct_amount = deduct_amount;
	}
}
