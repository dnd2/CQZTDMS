package com.infoservice.dms.chana.vo;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @Title: WarrantyQueryVO.java
 *
 * @Description:CHANADMS
 *
 * @Copyright: Copyright (c) 2010
 *
 * @Company: www.infoservice.com.cn
 * @Date: 2010-7-29
 *
 * @author lishuai 
 * @mail   lishuai103@yahoo.cn	
 * @version 1.0
 * @remark 
 */
@SuppressWarnings("serial")
public class WarrantyQueryVO extends BaseVO {
	private String vin; //下端：VIN  VARCHAR(17)  上端：  
	private Double inMileage;//进厂行驶里程
	private LinkedList partVoList; //下端：配件列表    上端：
	private String repairTypeCode; //维修类型ID
	
	public String getRepairTypeCode() {
		return repairTypeCode;
	}
	public void setRepairTypeCode(String repairTypeCode) {
		this.repairTypeCode = repairTypeCode;
	}
	public Double getInMileage() {
		return inMileage;
	}
	public void setInMileage(Double inMileage) {
		this.inMileage = inMileage;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public LinkedList getPartVoList() {
		return partVoList;
	}
	public void setPartVoList(LinkedList partVoList) {
		this.partVoList = partVoList;
	}

}
