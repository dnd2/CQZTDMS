package com.infodms.dms.po;

import com.infodms.dms.po.TtAsRoRepairPartPO;

@SuppressWarnings("serial")
public class TtAsRoRepairPartBean extends TtAsRoRepairPartPO {

	private String labourName;
	private String labourCode;
	private Double labourAmount;
	private String unit;
	public String getLabourName() {
		return labourName;
	}

	public void setLabourName(String labourName) {
		this.labourName = labourName;
	}

	public Double getLabourAmount() {
		return labourAmount;
	}

	public void setLabourAmount(Double labourAmount) {
		this.labourAmount = labourAmount;
	}

	public String getLabourCode() {
		return labourCode;
	}

	public void setLabourCode(String labourCode) {
		this.labourCode = labourCode;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
}
