package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrDeductBalancePO extends PO {
	private Long id;
	private String balanceNo;
	private Long yieldly;
	private String dealerCode;
	private String dealerName;
	private Integer status;
	private Long deductCount;
	private Double labourAmount;
	private Double partAmount;
	private Double otherAmount;
	private Double totalAmount;
	private Long oemCompanyId;
	private Date startTime;
	private Date endTime;
	private Long updateBy;
	private Date updateDate;
	private Long createBy;
	private Date createDate;
	private Long claimbalanceId;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return this.id;
	}

	public void setBalanceNo(String balanceNo) {
		this.balanceNo = balanceNo;
	}

	public String getBalanceNo() {
		return this.balanceNo;
	}

	public void setDealerCode(String dealerCode) {
		this.dealerCode = dealerCode;
	}

	public String getDealerCode() {
		return this.dealerCode;
	}

	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}

	public String getDealerName() {
		return this.dealerName;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setDeductCount(Long deductCount) {
		this.deductCount = deductCount;
	}

	public Long getDeductCount() {
		return this.deductCount;
	}

	public void setLabourAmount(Double labourAmount) {
		this.labourAmount = labourAmount;
	}

	public Double getLabourAmount() {
		return this.labourAmount;
	}

	public void setPartAmount(Double partAmount) {
		this.partAmount = partAmount;
	}

	public Double getPartAmount() {
		return this.partAmount;
	}

	public void setOtherAmount(Double otherAmount) {
		this.otherAmount = otherAmount;
	}

	public Double getOtherAmount() {
		return this.otherAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getTotalAmount() {
		return this.totalAmount;
	}

	public void setOemCompanyId(Long oemCompanyId) {
		this.oemCompanyId = oemCompanyId;
	}

	public Long getOemCompanyId() {
		return this.oemCompanyId;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getStartTime() {
		return this.startTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getEndTime() {
		return this.endTime;
	}

	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}

	public Long getUpdateBy() {
		return this.updateBy;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Date getUpdateDate() {
		return this.updateDate;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	public Long getCreateBy() {
		return this.createBy;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setClaimbalanceId(Long claimbalanceId) {
		this.claimbalanceId = claimbalanceId;
	}

	public Long getClaimbalanceId() {
		return this.claimbalanceId;
	}

	public Long getYieldly() {
		return yieldly;
	}

	public void setYieldly(Long yieldly) {
		this.yieldly = yieldly;
	}
}
