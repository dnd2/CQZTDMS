package com.infoservice.dms.chana.vo;

public class ActivityLabourVO extends BaseVO {
	
	private String labourCode; //下端：维修项目代码  varchar(30)  上端：ITEM_CODE VARCHAR2(20) 
	private String labourName; //下端：维修项目名称  varchar(150)  上端：ITEM_NAME VARCHAR2(50) 
	private Double stdLabourHour; //下端：标准工时  NUMERIC(10,2)  上端：NORMAL_LABOR NUMBER(6,2) 
	private Double labourAmount; //下端：工时费  NUMERIC(12,2)  上端：LABOR_FEE NUMBER(9,2) 
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
	public Double getStdLabourHour() {
		return stdLabourHour;
	}
	public void setStdLabourHour(Double stdLabourHour) {
		this.stdLabourHour = stdLabourHour;
	}
	public Double getLabourAmount() {
		return labourAmount;
	}
	public void setLabourAmount(Double labourAmount) {
		this.labourAmount = labourAmount;
	}
	
	
}
