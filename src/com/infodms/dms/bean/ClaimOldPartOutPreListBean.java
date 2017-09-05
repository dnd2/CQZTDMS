package com.infodms.dms.bean;

import com.infoservice.po3.bean.PO;

public class ClaimOldPartOutPreListBean extends PO{

	private static final long serialVersionUID = 6638069922500961963L;
	
	private String id;//
	private String is_notice;//供应商简称
	private String supply_name;//供应商简称
	private String supply_code;//供应商简称
	private String part_code;//配件代码
	private String part_name;//配件名称
	private Integer return_amount;//回运数
	private Integer out_amount;//出库数
	private String yieldly;//产地
	private Integer all_amount;
	private String claim_no;
	private String supplier_code;
	private String supplier_name;
	private String  barcode_no;
	private Long area_id;
	private String model_name;
	private String dealer_code;
	private String dealer_name;
	private String vin;
	private String wr_labourcode;
	private Integer is_print;
	private Double partPrice;
	private String totals;
	private String claim_id;
	private String all_in_amount;
	private String all_out_amount;
	private String all_amount_kc;
	private String qhj_flag;
	private String is_qhj;
	private String bill_type;
	private String fee_type;
	
	public String getAll_in_amount() {
		return all_in_amount;
	}
	public void setAll_in_amount(String all_in_amount) {
		this.all_in_amount = all_in_amount;
	}
	public String getAll_out_amount() {
		return all_out_amount;
	}
	public void setAll_out_amount(String all_out_amount) {
		this.all_out_amount = all_out_amount;
	}
	public String getTotals() {
		return totals;
	}
	public void setTotals(String totals) {
		this.totals = totals;
	}
	public Integer getDayIn() {
		return dayIn;
	}
	public void setDayIn(Integer dayIn) {
		this.dayIn = dayIn;
	}
	public Integer getMonthIn() {
		return monthIn;
	}
	public void setMonthIn(Integer monthIn) {
		this.monthIn = monthIn;
	}
	private String num;
	private Integer partReturn;
	private Integer dayIn;
	public Integer getClaimType() {
		return claimType;
	}
	public void setClaimType(Integer claimType) {
		this.claimType = claimType;
	}
	private Integer monthIn;
	private Integer claimType;
	private Integer returnType;
	public Integer getPartReturn() {
		return partReturn;
	}
	public void setPartReturn(Integer partReturn) {
		this.partReturn = partReturn;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getModel_name() {
		return model_name;
	}
	public void setModel_name(String model_name) {
		this.model_name = model_name;
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
	public Long getArea_id() {
		return area_id;
	}
	public void setArea_id(Long area_id) {
		this.area_id = area_id;
	}
	public Integer getAll_amount() {
		return all_amount;
	}
	public void setAll_amount(Integer all_amount) {
		this.all_amount = all_amount;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
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
	public Integer getReturn_amount() {
		return return_amount;
	}
	public void setReturn_amount(Integer return_amount) {
		this.return_amount = return_amount;
	}
	public Integer getOut_amount() {
		return out_amount;
	}
	public void setOut_amount(Integer out_amount) {
		this.out_amount = out_amount;
	}
	public String getYieldly() {
		return yieldly;
	}
	public void setYieldly(String yieldly) {
		this.yieldly = yieldly;
	}
	public String getClaim_no() {
		return claim_no;
	}
	public void setClaim_no(String claim_no) {
		this.claim_no = claim_no;
	}
	public String getSupplier_code() {
		return supplier_code;
	}
	public void setSupplier_code(String supplier_code) {
		this.supplier_code = supplier_code;
	}
	public String getSupplier_name() {
		return supplier_name;
	}
	public void setSupplier_name(String supplier_name) {
		this.supplier_name = supplier_name;
	}

	public String getBarcode_no() {
		return barcode_no;
	}
	public void setBarcode_no(String barcode_no) {
		this.barcode_no = barcode_no;
	}
	public String getSupply_code() {
		return supply_code;
	}
	public void setSupply_code(String supply_code) {
		this.supply_code = supply_code;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}

	public Integer getIs_print() {
		return is_print;
	}
	public void setIs_print(Integer is_print) {
		this.is_print = is_print;
	}
	public String getWr_labourcode() {
		return wr_labourcode;
	}
	public void setWr_labourcode(String wr_labourcode) {
		this.wr_labourcode = wr_labourcode;
	}
	public String getIs_notice() {
		return is_notice;
	}
	public void setIs_notice(String is_notice) {
		this.is_notice = is_notice;
	}
	public Double getPartPrice() {
		return partPrice;
	}
	public void setPartPrice(Double partPrice) {
		this.partPrice = partPrice;
	}
	public Integer getReturnType() {
		return returnType;
	}
	public void setReturnType(Integer returnType) {
		this.returnType = returnType;
	}
	public String getClaim_id() {
		return claim_id;
	}
	public void setClaim_id(String claim_id) {
		this.claim_id = claim_id;
	}
	public String getAll_amount_kc() {
		return all_amount_kc;
	}
	public void setAll_amount_kc(String all_amount_kc) {
		this.all_amount_kc = all_amount_kc;
	}
	public String getQhj_flag() {
		return qhj_flag;
	}
	public void setQhj_flag(String qhj_flag) {
		this.qhj_flag = qhj_flag;
	}
	public String getIs_qhj() {
		return is_qhj;
	}
	public void setIs_qhj(String is_qhj) {
		this.is_qhj = is_qhj;
	}
	public String getBill_type() {
		return bill_type;
	}
	public void setBill_type(String bill_type) {
		this.bill_type = bill_type;
	}
	public String getFee_type() {
		return fee_type;
	}
	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}

	
}
