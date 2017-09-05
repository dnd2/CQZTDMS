package com.infodms.dms.bean;

import java.awt.geom.Arc2D.Double;

import com.infoservice.po3.bean.PO;

public class ClaimOldPartDeduceListBean extends PO{

	private static final long serialVersionUID = 4041719849562229476L;
	
	private Long return_id;//回运主键
	private String dealer_code;//经销商代码
	private String dealer_name;//经销商简称
	private String return_no;//回运单号
	private Integer transport_type;//回运类型
	private String transport_desc;//回运解释
	private String create_date;//建单日期
	private String report_date;//提报日期
	private String store_date;//入库日期
	private Integer wr_amount;//索赔单数
	private Integer part_amount;//索赔旧件数
	private Integer diff_amount;//差异数
	private String is_deduct;
	private String balance_amount;
	private String deduct_amount;
	private double price;
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getIs_deduct() {
		return is_deduct;
	}
	public void setIs_deduct(String isDeduct) {
		is_deduct = isDeduct;
	}
	public String getBalance_amount() {
		return balance_amount;
	}
	public void setBalance_amount(String balanceAmount) {
		balance_amount = balanceAmount;
	}
	public String getDeduct_amount() {
		return deduct_amount;
	}
	public void setDeduct_amount(String deductAmount) {
		deduct_amount = deductAmount;
	}
	public Long getReturn_id() {
		return return_id;
	}
	public void setReturn_id(Long return_id) {
		this.return_id = return_id;
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
	public String getReturn_no() {
		return return_no;
	}
	public void setReturn_no(String return_no) {
		this.return_no = return_no;
	}
	public Integer getTransport_type() {
		return transport_type;
	}
	public void setTransport_type(Integer transport_type) {
		this.transport_type = transport_type;
	}
	public String getTransport_desc() {
		return transport_desc;
	}
	public void setTransport_desc(String transport_desc) {
		this.transport_desc = transport_desc;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	public String getReport_date() {
		return report_date;
	}
	public void setReport_date(String report_date) {
		this.report_date = report_date;
	}
	public String getStore_date() {
		return store_date;
	}
	public void setStore_date(String store_date) {
		this.store_date = store_date;
	}
	public Integer getWr_amount() {
		return wr_amount;
	}
	public void setWr_amount(Integer wr_amount) {
		this.wr_amount = wr_amount;
	}
	public Integer getPart_amount() {
		return part_amount;
	}
	public void setPart_amount(Integer part_amount) {
		this.part_amount = part_amount;
	}
	public Integer getDiff_amount() {
		return diff_amount;
	}
	public void setDiff_amount(Integer diff_amount) {
		this.diff_amount = diff_amount;
	}
}
