package com.infoservice.dms.chana.vo;

import java.util.Date;

public class MonitorLabourVO extends BaseVO {
	
	private String entityCode; //下端：经销商代码  CHAR(8)  上端：  
	private String labourCode; //下端：维修项目代码  VARCHAR(30)  上端：LABOUR_OPERATION_NO VARCHAR2(10) 
	private String labourName; //下端：维修项目名称  VARCHAR(150)  上端：LABOUR_OPERATION_NAME VARCHAR2(300) 
	private Date openDate; //下端：启用日期  TIMESTAMP  上端：OPEN_DATE DATE 
	private Date closeDate; //下端：停用日期  TIMESTAMP  上端：CLOSE_DATE DATE 

	public String getEntityCode() {
		return entityCode;
	}
	public void setEntityCode(String entityCode) {
		this.entityCode = entityCode;
	}
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
	public Date getOpenDate() {
		return openDate;
	}
	public void setOpenDate(Date openDate) {
		this.openDate = openDate;
	}
	public Date getCloseDate() {
		return closeDate;
	}
	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}

}
