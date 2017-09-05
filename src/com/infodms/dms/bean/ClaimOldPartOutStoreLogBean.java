package com.infodms.dms.bean;

import com.infoservice.po3.bean.PO;

public class ClaimOldPartOutStoreLogBean extends PO{

	private static final long serialVersionUID = -7445621234302607551L;
    
	private String supply_name;//供应商简称
	private String part_code;//配件代码
	private String part_name;//配件名称
	private Integer out_before_amount;//出库前数
	private Integer out_amount;//出库数
	private Integer remain_amount;//出库后数
	private String out_date;//出库日期
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
	public Integer getOut_before_amount() {
		return out_before_amount;
	}
	public void setOut_before_amount(Integer out_before_amount) {
		this.out_before_amount = out_before_amount;
	}
	public Integer getOut_amount() {
		return out_amount;
	}
	public void setOut_amount(Integer out_amount) {
		this.out_amount = out_amount;
	}
	public Integer getRemain_amount() {
		return remain_amount;
	}
	public void setRemain_amount(Integer remain_amount) {
		this.remain_amount = remain_amount;
	}
	public String getOut_date() {
		return out_date;
	}
	public void setOut_date(String out_date) {
		this.out_date = out_date;
	}
	public String getYieldly() {
		return yieldly;
	}
	public void setYieldly(String yieldly) {
		this.yieldly = yieldly;
	}
	
}
