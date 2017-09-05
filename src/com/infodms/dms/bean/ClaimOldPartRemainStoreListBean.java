package com.infodms.dms.bean;

import com.infoservice.po3.bean.PO;

public class ClaimOldPartRemainStoreListBean extends PO{

	private static final long serialVersionUID = 8744509116487971518L;
	
	private String supply_name;//供应商简称
	private String part_code;//配件代码
	private String part_name;//配件名称
	private Integer remain_amount;//库存数
	private String yieldly;//产地
	public String getSupply_name() {
		return supply_name;
	}
	public void setSupply_name(String supply_name) {
		this.supply_name = supply_name;
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
	public Integer getRemain_amount() {
		return remain_amount;
	}
	public void setRemain_amount(Integer remain_amount) {
		this.remain_amount = remain_amount;
	}
	public String getYieldly() {
		return yieldly;
	}
	public void setYieldly(String yieldly) {
		this.yieldly = yieldly;
	}
	
}
