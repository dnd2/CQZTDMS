package com.infodms.dms.po;

@SuppressWarnings("serial")
public class TmVehicleExtendPO extends TmVehicleExtPO {

	private String yieldlyName;
	private Long orderId;
	private String inStoreDate;
	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getYieldlyName() {
		return yieldlyName;
	}

	public void setYieldlyName(String yieldlyName) {
		this.yieldlyName = yieldlyName;
	}

	public String getInStoreDate() {
		return inStoreDate;
	}

	public void setInStoreDate(String inStoreDate) {
		this.inStoreDate = inStoreDate;
	}
}
