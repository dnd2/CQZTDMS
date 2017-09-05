package com.infoservice.dms.chana.vo;

import java.util.Date;

public class ActivityResultVO extends BaseVO {
	private String entityCode; //下端：经销商代码  CHAR(8)  上端：  
	private String activityCode; //下端：活动编号  varchar(15)  上端：ACTIVITY_ID  
	private String activityName; //下端：活动名称  varchar(60)  上端：  
	private String realEntityCode; //下端：实际维修站代码  CHAR(8)  上端：OPERATE_DEALER_CODE VARCHAR2(10) 
	private String realEntityName; //下端：实际维修站名称  VARCHAR(150)  上端：  
	private String vin; //下端：VIN  varchar(17)  上端：VIN  
	private Date CampaignDate; //下端：参加活动时间  TIMESTAMP  上端：  
	public String getEntityCode() {
		return entityCode;
	}
	public void setEntityCode(String entityCode) {
		this.entityCode = entityCode;
	}
	public String getActivityCode() {
		return activityCode;
	}
	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	public String getRealEntityCode() {
		return realEntityCode;
	}
	public void setRealEntityCode(String realEntityCode) {
		this.realEntityCode = realEntityCode;
	}
	public String getRealEntityName() {
		return realEntityName;
	}
	public void setRealEntityName(String realEntityName) {
		this.realEntityName = realEntityName;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public Date getCampaignDate() {
		return CampaignDate;
	}
	public void setCampaignDate(Date campaignDate) {
		CampaignDate = campaignDate;
	}

}
