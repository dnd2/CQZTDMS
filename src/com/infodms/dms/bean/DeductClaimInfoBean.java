package com.infodms.dms.bean;

import com.infoservice.po3.bean.PO;

public class DeductClaimInfoBean extends PO{

	private static final long serialVersionUID = 6904516585719637919L;
	
	private Long dealer_id;//经销商id
	private String dealer_code;//经销商代码
	private String dealer_name;//经销商简称
	private String claim_id;//索赔id
	private String claim_no;//索赔单号
	private String ro_no;//维修工单号
	private String ro_startdate;//工单开始日期
	private String ro_enddate;//工单结束日期
	private String in_mileage;//进厂公里数
	private String guarantee_date;//保修开始日期
	private String serve_advisor;//接待员
	
	
	public String getClaim_id() {
		return claim_id;
	}
	public void setClaim_id(String claim_id) {
		this.claim_id = claim_id;
	}
	public Long getDealer_id() {
		return dealer_id;
	}
	public void setDealer_id(Long dealer_id) {
		this.dealer_id = dealer_id;
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
	public String getClaim_no() {
		return claim_no;
	}
	public void setClaim_no(String claim_no) {
		this.claim_no = claim_no;
	}
	public String getRo_no() {
		return ro_no;
	}
	public void setRo_no(String ro_no) {
		this.ro_no = ro_no;
	}
	public String getRo_startdate() {
		return ro_startdate;
	}
	public void setRo_startdate(String ro_startdate) {
		this.ro_startdate = ro_startdate;
	}
	public String getRo_enddate() {
		return ro_enddate;
	}
	public void setRo_enddate(String ro_enddate) {
		this.ro_enddate = ro_enddate;
	}
	public String getIn_mileage() {
		return in_mileage;
	}
	public void setIn_mileage(String in_mileage) {
		this.in_mileage = in_mileage;
	}
	public String getGuarantee_date() {
		return guarantee_date;
	}
	public void setGuarantee_date(String guarantee_date) {
		this.guarantee_date = guarantee_date;
	}
	public String getServe_advisor() {
		return serve_advisor;
	}
	public void setServe_advisor(String serve_advisor) {
		this.serve_advisor = serve_advisor;
	}
}
