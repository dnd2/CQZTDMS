package com.infoservice.dms.chana.vo;

public class ClaimLabourVO extends BaseVO {
	private String labourCode; //下端：维修项目代码  VARCHAR(30)  上端：LABOUR_CODE VARCHAR2(10) 
	private String labourName; //下端：维修项目名称  VARCHAR(150)  上端：CN_DES VARCHAR2(300) 
	private Float labourHour; //下端：索赔工时 LABOUR_HOUR NUMBER(6,2)  上端：LABOUR_HOUR NUMBER(6,2) 
	private String wrGroupCode;	//车型组代码
	private String wrGroupName; //车型组名称
	public String getLabourCode() {
		return labourCode;
	}
	public void setLabourCode(String labourCode) {
		this.labourCode = labourCode;
	}
	public String getLabourName() {
		return labourName;
	}
	public void setLabourName(String labourName) {
		this.labourName = labourName;
	}
	public Float getLabourHour() {
		return labourHour;
	}
	public void setLabourHour(Float labourHour) {
		this.labourHour = labourHour;
	}
	public String getWrGroupCode() {
		return wrGroupCode;
	}
	public void setWrGroupCode(String wrGroupCode) {
		this.wrGroupCode = wrGroupCode;
	}
	public String getWrGroupName() {
		return wrGroupName;
	}
	public void setWrGroupName(String wrGroupName) {
		this.wrGroupName = wrGroupName;
	}
}
