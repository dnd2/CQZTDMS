package com.infodms.dms.bean;

public class PartClaimBean {
	private Long claimId;         //索赔单ID
	private String sighNo;        //签收单号
	private String doNo;		  //货运订单号
	private String orderNo;       //采购订单号
	private String beginDate;       //签收起始日期
	private String endDate;         //签收结束日期
	private Long dealerId;        //经销商
	private String dealerName;	  //经销商名称
	private String dealerCode;	  //经销商代码
	private String dealerCodes;     //经销商code,可能有多个,用逗号分隔
	private String claimNo;       //索赔单号
	private String checkStatus;   //审核状态
	private String orgCodes;         //组织code,可能有多个,用逗号分隔
	public Long getClaimId() {
		return claimId;
	}
	public void setClaimId(Long claimId) {
		this.claimId = claimId;
	}
	public String getSighNo() {
		return sighNo;
	}
	public void setSighNo(String sighNo) {
		this.sighNo = sighNo;
	}
	public String getDoNo() {
		return doNo;
	}
	public void setDoNo(String doNo) {
		this.doNo = doNo;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public Long getDealerId() {
		return dealerId;
	}
	public void setDealerId(Long dealerId) {
		this.dealerId = dealerId;
	}
	public String getDealerName() {
		return dealerName;
	}
	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}
	public String getDealerCode() {
		return dealerCode;
	}
	public void setDealerCode(String dealerCode) {
		this.dealerCode = dealerCode;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	public String getDealerCodes() {
		return dealerCodes;
	}
	public void setDealerCodes(String dealerCodes) {
		this.dealerCodes = dealerCodes;
	}
	public String getClaimNo() {
		return claimNo;
	}
	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}
	public String getCheckStatus() {
		return checkStatus;
	}
	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}
	public String getOrgCodes() {
		return orgCodes;
	}
	public void setOrgCodes(String orgCodes) {
		this.orgCodes = orgCodes;
	}

	
}
