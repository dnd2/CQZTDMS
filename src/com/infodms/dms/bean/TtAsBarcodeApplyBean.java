package com.infodms.dms.bean;

import com.infodms.dms.po.TtAsBarcodeApplyPO;

@SuppressWarnings("serial")
public class TtAsBarcodeApplyBean extends TtAsBarcodeApplyPO {
	
	private String dealerCode;
	private String ownName;
	public String getDealerCode() {
		return dealerCode;
	}
	public void setDealerCode(String dealerCode) {
		this.dealerCode = dealerCode;
	}
	public String getOwnName() {
		return ownName;
	}
	public void setOwnName(String ownName) {
		this.ownName = ownName;
	}
}
