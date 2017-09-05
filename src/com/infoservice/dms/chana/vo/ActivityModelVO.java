package com.infoservice.dms.chana.vo;

public class ActivityModelVO extends BaseVO {
	private String modelCode; //下端：车型代码  varchar(30)  上端：物料组ID-》GROUP_CODE VARCHAR2(30) 		

	public String getModelCode() {
		return modelCode;
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}

}
