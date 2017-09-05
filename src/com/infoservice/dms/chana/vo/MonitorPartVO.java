package com.infoservice.dms.chana.vo;

import java.util.Date;

@SuppressWarnings("serial")
public class MonitorPartVO extends BaseVO {
	 
	private String partNo; //下端：配件代码  VARCHAR2(27)  上端：PART_CODE VARCHAR2(27) 
	private String partName; //下端：配件名称  VARCHAR2(100)  上端：PART_NAME VARCHAR2(100) 
	private Date openDate; //下端：启用日期  TIMESTAMP  上端：OPEN_DATE DATE 
	private Date closeDate; //下端：停用日期  TIMESTAMP  上端：CLOSE_DATE DATE 

	public String getPartNo() {
		return partNo;
	}
	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}
	public String getPartName() {
		return partName;
	}
	public void setPartName(String partName) {
		this.partName = partName;
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
