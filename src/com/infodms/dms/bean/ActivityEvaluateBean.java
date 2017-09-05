package com.infodms.dms.bean;

import java.util.Date;
import java.util.List;

public class ActivityEvaluateBean {
	private Long subjectId; // 服务活动主题Id
	private String subjectNo; // 服务活动主题代码
	private String subjectName; // 服务活动主题名称
	private Long dealerId; // 一级经销商ID
	private String dealerCode; // 一级经销商代码
	private String dealerName; // 一级经销商名称
	private Long secDealerId; // 二级经销商Id
	private String secDealerCode; // 二级经销商代码
	private String secDealerName; // 二级经销商名称
	private Double balanceAmount; // 结算总金额
	private Double firBalAmount; // 一级经销商结算金额
	private Double secBalAmount; // 二级经销商结算金额
	private Integer actEvaType; // 服务活动评估类型(合格,基本合格,不合格)
	private Integer actEvaScale; // 服务活动评估比例(1.0, 0.9, 0)
	private Long actEvaUserId; // 服务活动评估人
	private Date actEvaDate; // 服务活动评估时间
	private Long actPayUserId; // 服务活动付款人
	private Date actPayDate; // 服务活动支付时间
	private List<String> claimNos; // 索赔结算单号 
	private List<Long> claimIds; // 索赔结算单Id
	
	public List<String> getClaimNos() {
		return claimNos;
	}

	public void setClaimNos(List<String> claimNos) {
		this.claimNos = claimNos;
	}

	public List<Long> getClaimIds() {
		return claimIds;
	}

	public void setClaimIds(List<Long> claimIds) {
		this.claimIds = claimIds;
	}
	public Integer getActEvaScale() {
		return actEvaScale;
	}

	public void setActEvaScale(Integer actEvaScale) {
		this.actEvaScale = actEvaScale;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public Long getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Long subjectId) {
		this.subjectId = subjectId;
	}

	public String getSubjectNo() {
		return subjectNo;
	}

	public void setSubjectNo(String subjectNo) {
		this.subjectNo = subjectNo;
	}

	public Long getDealerId() {
		return dealerId;
	}

	public void setDealerId(Long dealerId) {
		this.dealerId = dealerId;
	}

	public String getDealerCode() {
		return dealerCode;
	}

	public void setDealerCode(String dealerCode) {
		this.dealerCode = dealerCode;
	}

	public String getDealerName() {
		return dealerName;
	}

	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}

	public Long getSecDealerId() {
		return secDealerId;
	}

	public void setSecDealerId(Long secDealerId) {
		this.secDealerId = secDealerId;
	}

	public String getSecDealerCode() {
		return secDealerCode;
	}

	public void setSecDealerCode(String secDealerCode) {
		this.secDealerCode = secDealerCode;
	}

	public String getSecDealerName() {
		return secDealerName;
	}

	public void setSecDealerName(String secDealerName) {
		this.secDealerName = secDealerName;
	}

	public Double getBalanceAmount() {
		return balanceAmount;
	}

	public void setBalanceAmount(Double balanceAmount) {
		this.balanceAmount = balanceAmount;
	}

	public Double getFirBalAmount() {
		return firBalAmount;
	}

	public void setFirBalAmount(Double firBalAmount) {
		this.firBalAmount = firBalAmount;
	}

	public Double getSecBalAmount() {
		return secBalAmount;
	}

	public void setSecBalAmount(Double secBalAmount) {
		this.secBalAmount = secBalAmount;
	}

	public Integer getActEvaType() {
		return actEvaType;
	}

	public void setActEvaType(Integer actEvaType) {
		this.actEvaType = actEvaType;
	}

	public Long getActEvaUserId() {
		return actEvaUserId;
	}

	public void setActEvaUserId(Long actEvaUserId) {
		this.actEvaUserId = actEvaUserId;
	}

	public Date getActEvaDate() {
		return actEvaDate;
	}

	public void setActEvaDate(Date actEvaDate) {
		this.actEvaDate = actEvaDate;
	}

	public Long getActPayUserId() {
		return actPayUserId;
	}

	public void setActPayUserId(Long actPayUserId) {
		this.actPayUserId = actPayUserId;
	}

	public Date getActPayDate() {
		return actPayDate;
	}

	public void setActPayDate(Date actPayDate) {
		this.actPayDate = actPayDate;
	}

}
