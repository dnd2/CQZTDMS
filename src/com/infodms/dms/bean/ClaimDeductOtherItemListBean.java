package com.infodms.dms.bean;

import com.infoservice.po3.bean.PO;

public class ClaimDeductOtherItemListBean extends PO{
	private static final long serialVersionUID = 889220926621859406L;
	
	private Long netitem_id;//
	private String clai_no;//
	private String item_desc;//
	private String amount;//
	private String remark;//
	private String balance_amount;//已结算金额
	private String deduct_amount;//
	public Long getNetitem_id() {
		return netitem_id;
	}
	public void setNetitem_id(Long netitem_id) {
		this.netitem_id = netitem_id;
	}
	public String getClai_no() {
		return clai_no;
	}
	public void setClai_no(String clai_no) {
		this.clai_no = clai_no;
	}
	public String getItem_desc() {
		return item_desc;
	}
	public void setItem_desc(String item_desc) {
		this.item_desc = item_desc;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getDeduct_amount() {
		return deduct_amount;
	}
	public void setDeduct_amount(String deduct_amount) {
		this.deduct_amount = deduct_amount;
	}
	public String getBalance_amount() {
		return balance_amount;
	}
	public void setBalance_amount(String balance_amount) {
		this.balance_amount = balance_amount;
	}
}
