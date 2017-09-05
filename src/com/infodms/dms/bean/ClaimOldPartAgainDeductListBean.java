package com.infodms.dms.bean;

import com.infoservice.po3.bean.PO;

public class ClaimOldPartAgainDeductListBean extends PO{

	private static final long serialVersionUID = 3388314630303257401L;
	
	private Long part_id;//配件id
	private String claim_no;//索赔单号
	private String down_part_code;//换下件代码
	private String part_code;//换上件代码
	private String part_name;//换上件名称
	private Integer quantity;//需回运数
	private Integer return_num;//已回运数
	private Integer price;//单价
	private String producer_code;//生产商代码
	private String old_part_code;//故障代码
	private Double balance_amount;//已结算金额
	private Double deduct_amount;//已抵扣金额
	public Long getPart_id() {
		return part_id;
	}
	public void setPart_id(Long part_id) {
		this.part_id = part_id;
	}
	public String getClaim_no() {
		return claim_no;
	}
	public void setClaim_no(String claim_no) {
		this.claim_no = claim_no;
	}
	public String getDown_part_code() {
		return down_part_code;
	}
	public void setDown_part_code(String down_part_code) {
		this.down_part_code = down_part_code;
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
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Integer getReturn_num() {
		return return_num;
	}
	public void setReturn_num(Integer return_num) {
		this.return_num = return_num;
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	public String getProducer_code() {
		return producer_code;
	}
	public void setProducer_code(String producer_code) {
		this.producer_code = producer_code;
	}
	public String getOld_part_code() {
		return old_part_code;
	}
	public void setOld_part_code(String old_part_code) {
		this.old_part_code = old_part_code;
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
