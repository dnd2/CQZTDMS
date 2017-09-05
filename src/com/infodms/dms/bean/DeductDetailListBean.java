package com.infodms.dms.bean;

import com.infoservice.po3.bean.PO;

public class DeductDetailListBean extends PO{

	private static final long serialVersionUID = -2692221407197707200L;
    
	private Long deduct_id;
	private Long claim_id;
    private String item_code;
    private String item_name;
    private String balance_amount;
    private String deduct_amount;
    private String deduct_money;
    private String deduct_reason;
    private String remark;
	public Long getDeduct_id() {
		return deduct_id;
	}
	public void setDeduct_id(Long deduct_id) {
		this.deduct_id = deduct_id;
	}
	public Long getClaim_id() {
		return claim_id;
	}
	public void setClaim_id(Long claim_id) {
		this.claim_id = claim_id;
	}
	public String getItem_code() {
		return item_code;
	}
	public void setItem_code(String item_code) {
		this.item_code = item_code;
	}
	public String getItem_name() {
		return item_name;
	}
	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}
	public String getBalance_amount() {
		return balance_amount;
	}
	public void setBalance_amount(String balance_amount) {
		this.balance_amount = balance_amount;
	}
	public String getDeduct_amount() {
		return deduct_amount;
	}
	public void setDeduct_amount(String deduct_amount) {
		this.deduct_amount = deduct_amount;
	}
	public String getDeduct_money() {
		return deduct_money;
	}
	public void setDeduct_money(String deduct_money) {
		this.deduct_money = deduct_money;
	}
	public String getDeduct_reason() {
		return deduct_reason;
	}
	public void setDeduct_reason(String deduct_reason) {
		this.deduct_reason = deduct_reason;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
