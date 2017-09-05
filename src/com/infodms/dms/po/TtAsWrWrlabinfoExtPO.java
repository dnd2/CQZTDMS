package com.infodms.dms.po;

public class TtAsWrWrlabinfoExtPO extends TtAsWrWrlabinfoPO{
	
	private String parameterValue;
	private String wrgroupName;
	private Double labourFix;
	private Integer fore;
	private Integer isSpec;
	private String approvalLevel;
	public String getApprovalLevel() {
		return approvalLevel;
	}

	public void setApprovalLevel(String approvalLevel) {
		this.approvalLevel = approvalLevel;
	}

	public String getParameterValue() {
		return parameterValue;
	}

	public void setParameterValue(String parameterValue) {
		this.parameterValue = parameterValue;
	}

	public String getWrgroupName() {
		return wrgroupName;
	}

	public void setWrgroupName(String wrgroupName) {
		this.wrgroupName = wrgroupName;
	}

	public Double getLabourFix() {
		return labourFix;
	}

	public void setLabourFix(Double labourFix) {
		this.labourFix = labourFix;
	}

	public Integer getFore() {
		return fore;
	}

	public void setFore(Integer fore) {
		this.fore = fore;
	}

	public Integer getIsSpec() {
		return isSpec;
	}

	public void setIsSpec(Integer isSpec) {
		this.isSpec = isSpec;
	}
	
}
