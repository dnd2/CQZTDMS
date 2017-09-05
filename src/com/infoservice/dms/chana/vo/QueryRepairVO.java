package com.infoservice.dms.chana.vo;



@SuppressWarnings("serial")
public class QueryRepairVO extends BaseVO {

	private String beginDate;// 工单开单日期
	private String endDate;// 工单开单日期
	private String vin;// VIN

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

}
