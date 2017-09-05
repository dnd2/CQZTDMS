package com.infodms.dms.bean;

import com.infoservice.po3.bean.PO;

public class DeductDelDetailListBean extends PO{

	private static final long serialVersionUID = 1278265941022507144L;
	
	private Long id;
	private Long deduct_id;
	private Long claim_id;
	private Integer item_type;
	private String item_code;
	private String item_name;
	private Double deduct_money;
	
	private Long part_id;
	public Long getPart_id() {
		return part_id;
	}
	public void setPart_id(Long partId) {
		part_id = partId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
	public Integer getItem_type() {
		return item_type;
	}
	public void setItem_type(Integer item_type) {
		this.item_type = item_type;
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
	public Double getDeduct_money() {
		return deduct_money;
	}
	public void setDeduct_money(Double deduct_money) {
		this.deduct_money = deduct_money;
	}
}
