package com.infodms.yxdms.entity.maintain;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrWinterPackagePO extends PO {
   
	private Long WintweId;
	private Long DealerId;
	private String PackageCode;
	public Long getWintweId() {
		return WintweId;
	}
	public void setWintweId(Long wintweId) {
		WintweId = wintweId;
	}
	public Long getDealerId() {
		return DealerId;
	}
	public void setDealerId(Long dealerId) {
		DealerId = dealerId;
	}
	public String getPackageCode() {
		return PackageCode;
	}
	public void setPackageCode(String packageCode) {
		PackageCode = packageCode;
	}
	
}
