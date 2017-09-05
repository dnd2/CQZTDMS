package com.infoservice.dms.chana.vo;

import java.util.Date;

/**
 * @Title: WarrantyPartVO.java
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
public class WarrantyPartVO extends BaseVO {
	private String partNo; //下端：配件代码  VARCHAR(27)  上端：  
	private String partName; //下端：配件名称  VARCHAR(120)  上端：  
	private Date warrantyBeginDate; //下端：三包期开始日期  TIMESTAMP  上端：  
	private Date warrantyEndDate; //下端：三包期结束日期  TIMESTAMP  上端：  
	private Integer isInWarranty; //下端：是否在保修期内  NUMERIC(8)  上端：  
	private Double warrantyBeginMileage;	//三包期开始里程			
	private Double warrantyEndMileage;	//三包期结束里程			
	private Double overDay;	//超出天数			
	private Double overMileage;	//超出里程			

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
	public Date getWarrantyBeginDate() {
		return warrantyBeginDate;
	}
	public void setWarrantyBeginDate(Date warrantyBeginDate) {
		this.warrantyBeginDate = warrantyBeginDate;
	}
	public Date getWarrantyEndDate() {
		return warrantyEndDate;
	}
	public void setWarrantyEndDate(Date warrantyEndDate) {
		this.warrantyEndDate = warrantyEndDate;
	}
	public Integer getIsInWarranty() {
		return isInWarranty;
	}
	public void setIsInWarranty(Integer isInWarranty) {
		this.isInWarranty = isInWarranty;
	}
	public Double getWarrantyBeginMileage() {
		return warrantyBeginMileage;
	}
	public void setWarrantyBeginMileage(Double warrantyBeginMileage) {
		this.warrantyBeginMileage = warrantyBeginMileage;
	}
	public Double getWarrantyEndMileage() {
		return warrantyEndMileage;
	}
	public void setWarrantyEndMileage(Double warrantyEndMileage) {
		this.warrantyEndMileage = warrantyEndMileage;
	}
	public Double getOverDay() {
		return overDay;
	}
	public void setOverDay(Double overDay) {
		this.overDay = overDay;
	}
	public Double getOverMileage() {
		return overMileage;
	}
	public void setOverMileage(Double overMileage) {
		this.overMileage = overMileage;
	}

}
