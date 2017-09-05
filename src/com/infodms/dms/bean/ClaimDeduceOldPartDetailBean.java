package com.infodms.dms.bean;

import com.infoservice.po3.bean.PO;

public class ClaimDeduceOldPartDetailBean extends PO{
	private static final long serialVersionUID = 6291195127735771428L;
	
	private Long return_id;//
	private String dealer_code;//
	private String dealer_name;//
	private String attach_area;//
	private Integer transport_type;//
	private String transport_desc;//
	private String return_no;//
	private String claim_no;//
	private String create_date;//
	private String report_date;//
	private String store_date;//
	private String wr_start_date;//
	private Integer parkage_amount;//
	private Integer diff_amount;//
	private String approve_name;//
	private String tran_no;//
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
	public String getAttach_area() {
		return attach_area;
	}
	public void setAttach_area(String attach_area) {
		this.attach_area = attach_area;
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
	public String getReturn_no() {
		return return_no;
	}
	public void setReturn_no(String return_no) {
		this.return_no = return_no;
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
	public String getWr_start_date() {
		return wr_start_date;
	}
	public void setWr_start_date(String wr_start_date) {
		this.wr_start_date = wr_start_date;
	}
	public Integer getParkage_amount() {
		return parkage_amount;
	}
	public void setParkage_amount(Integer parkage_amount) {
		this.parkage_amount = parkage_amount;
	}
	public Integer getDiff_amount() {
		return diff_amount;
	}
	public void setDiff_amount(Integer diff_amount) {
		this.diff_amount = diff_amount;
	}
	public String getApprove_name() {
		return approve_name;
	}
	public void setApprove_name(String approve_name) {
		this.approve_name = approve_name;
	}
	public String getTran_no() {
		return tran_no;
	}
	public void setTran_no(String tran_no) {
		this.tran_no = tran_no;
	}
	public String getClaim_no() {
		return claim_no;
	}
	public void setClaim_no(String claim_no) {
		this.claim_no = claim_no;
	}
}
