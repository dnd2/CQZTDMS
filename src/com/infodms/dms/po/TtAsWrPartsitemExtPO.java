package com.infodms.dms.po;

public class TtAsWrPartsitemExtPO extends TtAsWrPartsitemPO{
	
	private String wrLabourname; //工时名称
	private Double labourHours;  //工时数
	private Double labourAmount; //工时费用
	private String firstPart;//第一备件
	private String troubleType;
	
	public String getTroubleType() {
		return troubleType;
	}
	public void setTroubleType(String troubleType) {
		this.troubleType = troubleType;
	}
	public String getFirstPart() {
		return firstPart;
	}
	public void setFirstPart(String firstPart) {
		this.firstPart = firstPart;
	}
	public String getWrLabourname() {
		return wrLabourname;
	}
	public void setWrLabourname(String wrLabourname) {
		this.wrLabourname = wrLabourname;
	}
	public Double getLabourHours() {
		return labourHours;
	}
	public void setLabourHours(Double labourHours) {
		this.labourHours = labourHours;
	}
	public Double getLabourAmount() {
		return labourAmount;
	}
	public void setLabourAmount(Double labourAmount) {
		this.labourAmount = labourAmount;
	}
}
