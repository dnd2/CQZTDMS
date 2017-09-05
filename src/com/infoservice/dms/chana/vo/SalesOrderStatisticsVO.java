package com.infoservice.dms.chana.vo;

import java.util.Date;

/**
 * @author Administrator
 *
 */
public class SalesOrderStatisticsVO extends BaseVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Date uploadDate;                   //日期
	private String businessType;               //业务范围
	private String model;                      //车型
	private String config;                     //配置
	private String colorCode;                  //颜色
	private Integer lastNoSendCarCount;        //截止上月未交车订单
	private Integer lastHaveBargainCount;      //上月潜客成交数
	private Integer oldCustHaveBargainCount;   //老客户推荐成交数
	private Integer outHaveBargainCount;       //外拓客户成交数
	private Integer newOrderCount;             //当日新增订单
	private String garnitureRate;              //精品装饰（件数/金额）
	private String insuranceRate;              //新车保险（件数/金额）
	private Integer sendCarCount;              //当日交车数
	private Integer returnCarCount;            //当日退单
	public Date getUploadDate() {
		return uploadDate;
	}
	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}
	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getConfig() {
		return config;
	}
	public void setConfig(String config) {
		this.config = config;
	}
	public String getColorCode() {
		return colorCode;
	}
	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}
	public Integer getLastNoSendCarCount() {
		return lastNoSendCarCount;
	}
	public void setLastNoSendCarCount(Integer lastNoSendCarCount) {
		this.lastNoSendCarCount = lastNoSendCarCount;
	}
	public Integer getLastHaveBargainCount() {
		return lastHaveBargainCount;
	}
	public void setLastHaveBargainCount(Integer lastHaveBargainCount) {
		this.lastHaveBargainCount = lastHaveBargainCount;
	}
	public Integer getOldCustHaveBargainCount() {
		return oldCustHaveBargainCount;
	}
	public void setOldCustHaveBargainCount(Integer oldCustHaveBargainCount) {
		this.oldCustHaveBargainCount = oldCustHaveBargainCount;
	}
	public Integer getOutHaveBargainCount() {
		return outHaveBargainCount;
	}
	public void setOutHaveBargainCount(Integer outHaveBargainCount) {
		this.outHaveBargainCount = outHaveBargainCount;
	}
	public Integer getNewOrderCount() {
		return newOrderCount;
	}
	public void setNewOrderCount(Integer newOrderCount) {
		this.newOrderCount = newOrderCount;
	}
	public String getGarnitureRate() {
		return garnitureRate;
	}
	public void setGarnitureRate(String garnitureRate) {
		this.garnitureRate = garnitureRate;
	}
	public String getInsuranceRate() {
		return insuranceRate;
	}
	public void setInsuranceRate(String insuranceRate) {
		this.insuranceRate = insuranceRate;
	}
	public Integer getSendCarCount() {
		return sendCarCount;
	}
	public void setSendCarCount(Integer sendCarCount) {
		this.sendCarCount = sendCarCount;
	}
	public Integer getReturnCarCount() {
		return returnCarCount;
	}
	public void setReturnCarCount(Integer returnCarCount) {
		this.returnCarCount = returnCarCount;
	}

}
