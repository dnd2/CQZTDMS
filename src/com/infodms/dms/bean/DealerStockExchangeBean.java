package com.infodms.dms.bean;

import java.util.Date;

public class DealerStockExchangeBean {
	private Long vehicleId; //车辆唯一编号
	private String orgCode;  //组织代码
	private String orgName;//组织名称
	private String vin;  //底盘号
	private String engineNo; //发动机号
	private String modelCode;  //车型代码
	private String modelName;	  //车型名称	
	private Date nodeDate;  //节点时间
	private String node;     //节点
	private String stockDate;   //库龄
	private Long  orgId; //组织唯一编号
	private Long  modelId;//车型唯一编号

    private String fromOrgName;
    private String toOrgName;
    private String seriesName;
    private Integer status;
    private Date exchangeApplyDate;
    private Date exchangeConfirmDate;
    private String exchangeType;
    private String dealerCode;
    private String dealerShortname;
    
    
	public Long getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(Long vehicleId) {
		this.vehicleId = vehicleId;
	}
	
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	
	public String getEngineNo() {
		return engineNo;
	}
	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}
	
	public String getModelCode() {
		return modelCode;
	}
	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}
	
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	
	public Date getNodeDate() {
		return nodeDate;
	}
	public void setNodeDate(Date nodeDate) {
		this.nodeDate = nodeDate;
	}
	
	public String getNode() {
		return node;
	}
	public void setNode(String node) {
		this.node = node;
	}
	
	public String getStockDate() {
		return stockDate;
	}
	public void setStockDate(String stockDate) {
		this.stockDate = stockDate;
	}
	
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	
	public Long getModelId() {
		return modelId;
	}
	public void setModelId(Long modelId) {
		this.modelId = modelId;
	}
	
	public String getFromOrgName() {
		return fromOrgName;
	}
	public void setFromOrgName(String fromOrgName) {
		this.fromOrgName = fromOrgName;
	}
	
	public String getToOrgName() {
		return toOrgName;
	}
	public void setToOrgName(String toOrgName) {
		this.toOrgName = toOrgName;
	}
	
	public String getSeriesName() {
		return seriesName;
	}
	public void setSeriesName(String seriesName) {
		this.seriesName = seriesName;
	}
	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public Date getExchangeApplyDate() {
		return exchangeApplyDate;
	}
	public void setExchangeApplyDate(Date exchangeApplyDate) {
		this.exchangeApplyDate = exchangeApplyDate;
	}
	
	public Date getExchangeConfirmDate() {
		return exchangeConfirmDate;
	}
	public void setExchangeConfirmDate(Date exchangeConfirmDate) {
		this.exchangeConfirmDate = exchangeConfirmDate;
	}
	
	public String getExchangeType() {
		return exchangeType;
	}
	public void setExchangeType(String exchangeType) {
		this.exchangeType = exchangeType;
	}
	
	public String getDealerCode() {
		return dealerCode;
	}
	public void setDealerCode(String dealerCode) {
		this.dealerCode = dealerCode;
	}
	
	public String getDealerShortname() {
		return dealerShortname;
	}
	public void setDealerShortname(String dealerShortname) {
		this.dealerShortname = dealerShortname;
	}
	
}
