package com.infodms.dms.bean;

import com.infodms.dms.po.TtAsRepairOrderExtPO;

@SuppressWarnings("serial")
public class TtAsRepairOrderExtBean extends TtAsRepairOrderExtPO{
	private Long dealerId;
	private String yieldlyName;
	private String creDate;
	private String dealerPhone;
	private String dealerCodeS;
	private String dealerIdS;
	private String color;

	public String getDealerCodeS() {
		return dealerCodeS;
	}

	public void setDealerCodeS(String dealerCodeS) {
		this.dealerCodeS = dealerCodeS;
	}
	
	public String getCreDate() {
		return creDate;
	}

	public void setCreDate(String creDate) {
		this.creDate = creDate;
	}

	public String getYieldlyName() {
		return yieldlyName;
	}

	public void setYieldlyName(String yieldlyName) {
		this.yieldlyName = yieldlyName;
	}

	public Long getDealerId() {
		return dealerId;
	}

	public void setDealerId(Long dealerId) {
		this.dealerId = dealerId;
	}

	public String getDealerPhone() {
		return dealerPhone;
	}

	public void setDealerPhone(String dealerPhone) {
		this.dealerPhone = dealerPhone;
	}

	public String getDealerIdS() {
		return dealerIdS;
	}

	public void setDealerIdS(String dealerIdS) {
		this.dealerIdS = dealerIdS;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

}
