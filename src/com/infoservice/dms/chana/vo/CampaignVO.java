package com.infoservice.dms.chana.vo;

import java.util.Date;
import java.util.LinkedList;

import com.infoservice.de.convertor.f2.VO;

public class CampaignVO extends BaseVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String campaignCode;          //广告/活动代码
	private String campaignName;          //广告/活动名称
	private Date startTime;               //开始时间
	private Date endTime;                 //结束时间
	private LinkedList camGroupVoList;    //车系
	private Date applyTime;               //申请时间
	private String location;              //地点
	private String targetCust;            //目标客户
	private double campaignPreCost;       //活动预算
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
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public LinkedList getCamGroupVoList() {
		return camGroupVoList;
	}
	public void setCamGroupVoList(LinkedList camGroupVoList) {
		this.camGroupVoList = camGroupVoList;
	}
	public Date getApplyTime() {
		return applyTime;
	}
	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getTargetCust() {
		return targetCust;
	}
	public void setTargetCust(String targetCust) {
		this.targetCust = targetCust;
	}
	public double getCampaignPreCost() {
		return campaignPreCost;
	}
	public void setCampaignPreCost(double campaignPreCost) {
		this.campaignPreCost = campaignPreCost;
	}
	public String toXMLString() {
		// TODO Auto-generated method stub
		return null;
	}
}
