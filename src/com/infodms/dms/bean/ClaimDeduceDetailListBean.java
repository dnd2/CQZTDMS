package com.infodms.dms.bean;

import com.infoservice.po3.bean.PO;

public class ClaimDeduceDetailListBean extends PO{

	private static final long serialVersionUID = -1229502844289063074L;
	
	private Long return_id;//
	private String vin;//
	private String claim_no;//
	private Integer n_return_amount;//
	private Integer sign_amount;//
	private Integer diff_amount;//
	private Integer balance_status;
	private Double deduct_amount;//抵扣金额
	private Double balance_deduct;
	
	private String claim_id;//
	public String getClaim_id() {
		return claim_id;
	}
	public void setClaim_id(String claimId) {
		claim_id = claimId;
	}
	public Double getBalance_deduct() {
		return balance_deduct;
	}
	public void setBalance_deduct(Double balanceDeduct) {
		balance_deduct = balanceDeduct;
	}
	public Long getReturn_id() {
		return return_id;
	}
	public void setReturn_id(Long return_id) {
		this.return_id = return_id;
	}
	public String getClaim_no() {
		return claim_no;
	}
	public void setClaim_no(String claim_no) {
		this.claim_no = claim_no;
	}
	public Integer getN_return_amount() {
		return n_return_amount;
	}
	public void setN_return_amount(Integer n_return_amount) {
		this.n_return_amount = n_return_amount;
	}
	public Integer getSign_amount() {
		return sign_amount;
	}
	public void setSign_amount(Integer sign_amount) {
		this.sign_amount = sign_amount;
	}
	public Integer getDiff_amount() {
		return diff_amount;
	}
	public void setDiff_amount(Integer diff_amount) {
		this.diff_amount = diff_amount;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public Integer getBalance_status() {
		return balance_status;
	}
	public void setBalance_status(Integer balance_status) {
		this.balance_status = balance_status;
	}
	public Double getDeduct_amount() {
		return deduct_amount;
	}
	public void setDeduct_amount(Double deduct_amount) {
		this.deduct_amount = deduct_amount;
	}
}
