package com.infoservice.dms.chana.vo;

import com.infoservice.dms.chana.vo.BaseVO;

@SuppressWarnings("serial")
public class ActivityYieldlyVO extends BaseVO {
	private Integer yieldlyCode; //下端：车辆生产地代码 YIELDLY_CODE NUMERIC(8)  上端：  
	private String yieldlyName; //下端：车辆生产地名称 YIELDLY_NAME varchar(150)  上端：  
	public Integer getYieldlyCode() {
		return yieldlyCode;
	}
	public void setYieldlyCode(Integer yieldlyCode) {
		this.yieldlyCode = yieldlyCode;
	}
	public String getYieldlyName() {
		return yieldlyName;
	}
	public void setYieldlyName(String yieldlyName) {
		this.yieldlyName = yieldlyName;
	}

}
