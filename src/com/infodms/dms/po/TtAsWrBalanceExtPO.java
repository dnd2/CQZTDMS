package com.infodms.dms.po;

public class TtAsWrBalanceExtPO extends TtAsWrBalancePO{
	
	private String dealerCode; //经销商代码
	private String dealerName; //经销商名称
	private String dealerShortname; //经销商简称
	public String getDealerCode() {
		return dealerCode;
	}
	public void setDealerCode(String dealerCode) {
		this.dealerCode = dealerCode;
	}
	public String getDealerName() {
		return dealerName;
	}
	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}
	public String getDealerShortname() {
		return dealerShortname;
	}
	public void setDealerShortname(String dealerShortname) {
		this.dealerShortname = dealerShortname;
	}
	
	

}
