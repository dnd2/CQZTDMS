package com.infodms.dms.bean;

import com.infoservice.po3.bean.PO;

public class CruiServiceDealerInfoBean extends PO{

	private static final long serialVersionUID = -1372039721657480247L;

	private String dealer_id;//经销商id
	private String dealer_code;//经销商代码
	private String dealer_name;//经销商名称
	private String privince_name;//经销商所在省份
	public String getDealer_id() {
		return dealer_id;
	}
	public void setDealer_id(String dealer_id) {
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
	public String getPrivince_name() {
		return privince_name;
	}
	public void setPrivince_name(String privince_name) {
		this.privince_name = privince_name;
	}
}
