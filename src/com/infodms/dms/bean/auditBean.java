/**
 * 
 */
package com.infodms.dms.bean;

/**
 * @author IBM
 *
 */
public class auditBean {

	private String balanceNo;//结算单号
	private String dealerCode;//经销商代码
	private String dealerName;//经销商名称
	public String getDealerName() {
		return dealerName;
	}
	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}
	private String dealerId;//经销商ID
	private String yieldly;//产地
	private String startDate;//开始时间
	private String endDate;//结束时间
	private String status;//结算单状态
	private String oemCompanyId;//车厂公司ID
	private String claimNo;//索赔单号
	private String vin;//VIN
	private String modelCode;//车型代码
	private String id;//结算单ID
	private String claimStatus;//索赔单状态
	private String authCode;//授权级别
	private String authCodeOrder;//授权级别(用来控制排序)
	private String claimType;//索赔单类型
	private String yieldlys;//用户拥有的产地权限
	private String startReportDate;//经销商上报开始时间
	private String endReportDate;//经销商上报结算时间
	private String cantNotAudit;//不能审核的单
	private String person;//复核人
	/**xiongchuan add by 2011-7-5  ***/
	private String balanceSatartDate;
	public String getBalanceSatartDate() {
		return balanceSatartDate;
	}
	public void setBalanceSatartDate(String balanceSatartDate) {
		this.balanceSatartDate = balanceSatartDate;
	}
	public String getBalanceEndtDate() {
		return balanceEndtDate;
	}
	public void setBalanceEndtDate(String balanceEndtDate) {
		this.balanceEndtDate = balanceEndtDate;
	}
	private String balanceEndtDate;
	
	/*********iverson add by 2011-01-20*****************************/
	private String reviewBy;
	private String reviewBeginDate;
	private String reviewEndDate;
	
	public String getReviewBy() {
		return reviewBy;
	}
	public void setReviewBy(String reviewBy) {
		this.reviewBy = reviewBy;
	}
	public String getReviewBeginDate() {
		return reviewBeginDate;
	}
	public void setReviewBeginDate(String reviewBeginDate) {
		this.reviewBeginDate = reviewBeginDate;
	}
	public String getReviewEndDate() {
		return reviewEndDate;
	}
	public void setReviewEndDate(String reviewEndDate) {
		this.reviewEndDate = reviewEndDate;
	}
	/*********iverson add by 2011-01-20*****************************/
	
	public String getPerson() {
		return person;
	}
	public void setPerson(String person) {
		this.person = person;
	}
	public String getCantNotAudit() {
		return cantNotAudit;
	}
	public void setCantNotAudit(String cantNotAudit) {
		this.cantNotAudit = cantNotAudit;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getClaimNo() {
		return claimNo;
	}
	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public String getModelCode() {
		return modelCode;
	}
	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}
	public String getOemCompanyId() {
		return oemCompanyId;
	}
	public void setOemCompanyId(String oemCompanyId) {
		this.oemCompanyId = oemCompanyId;
	}
	public String getBalanceNo() {
		return balanceNo;
	}
	public void setBalanceNo(String balanceNo) {
		this.balanceNo = balanceNo;
	}
	public String getDealerCode() {
		return dealerCode;
	}
	public void setDealerCode(String dealerCode) {
		this.dealerCode = dealerCode;
	}
	public String getYieldly() {
		return yieldly;
	}
	public void setYieldly(String yieldly) {
		this.yieldly = yieldly;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getClaimStatus() {
		return claimStatus;
	}
	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}
	public String getAuthCode() {
		return authCode;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	public String getDealerId() {
		return dealerId;
	}
	public void setDealerId(String dealerId) {
		this.dealerId = dealerId;
	}
	public String getAuthCodeOrder() {
		return authCodeOrder;
	}
	public void setAuthCodeOrder(String authCodeOrder) {
		this.authCodeOrder = authCodeOrder;
	}
	public String getClaimType() {
		return claimType;
	}
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}
	public String getYieldlys() {
		return yieldlys;
	}
	public void setYieldlys(String yieldlys) {
		this.yieldlys = yieldlys;
	}
	public String getStartReportDate() {
		return startReportDate;
	}
	public void setStartReportDate(String startReportDate) {
		this.startReportDate = startReportDate;
	}
	public String getEndReportDate() {
		return endReportDate;
	}
	public void setEndReportDate(String endReportDate) {
		this.endReportDate = endReportDate;
	}
	
}
