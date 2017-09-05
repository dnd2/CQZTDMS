package com.infoservice.dms.chana.vo;

import java.util.Date;

public class CampaignStatisticsVO extends BaseVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Date uploadDate;             //日期
	private String businessType;         //业务范围
	private String series;               //车系
	private Integer campaignCount;       //广告/活动
	private Integer inCallCount;         //来电客流
	private Integer inBobyCount;         //来店客流
	private Integer orderTotal;          //订单数
	private Integer orderUploadTotal;    //零售数
	private String campaignCode;          //广告/活动代码
	public String getCampaignCode() {
		return campaignCode;
	}
	public void setCampaignCode(String campaignCode) {
		this.campaignCode = campaignCode;
	}
	public String getCampaignName() {
		return campaignName;
	}
	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}
	private String campaignName;          //广告/活动名称
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
	public String getSeries() {
		return series;
	}
	public void setSeries(String series) {
		this.series = series;
	}
	public Integer getCampaignCount() {
		return campaignCount;
	}
	public void setCampaignCount(Integer campaignCount) {
		this.campaignCount = campaignCount;
	}
	public Integer getInCallCount() {
		return inCallCount;
	}
	public void setInCallCount(Integer inCallCount) {
		this.inCallCount = inCallCount;
	}
	public Integer getInBobyCount() {
		return inBobyCount;
	}
	public void setInBobyCount(Integer inBobyCount) {
		this.inBobyCount = inBobyCount;
	}
	public Integer getOrderTotal() {
		return orderTotal;
	}
	public void setOrderTotal(Integer orderTotal) {
		this.orderTotal = orderTotal;
	}
	public Integer getOrderUploadTotal() {
		return orderUploadTotal;
	}
	public void setOrderUploadTotal(Integer orderUploadTotal) {
		this.orderUploadTotal = orderUploadTotal;
	}

}
