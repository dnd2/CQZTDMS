package com.infodms.dms.bean;

import com.infodms.dms.po.TtAsWrForeapprovalPO;

@SuppressWarnings("serial")
public class TtAsWrForeapprovalBean extends TtAsWrForeapprovalPO {
	private String dealerName;
	private String orgname;
	private String auditName;
	private Integer startMileage;
	private Integer endMileage;
	private Integer maxDays;
	private Integer minDays;
	private Integer differencestime;
	private String approvalLevelName;
	private String modelName;
	private String lastName;
	private Double repairPartAmount;
	private String updateDat;
	private Double labourAmount;
	private Double addItemAmount;
	private Double repairAmount;
	private Double accessoriesPrice;
	//新加字段显示
	private String troubleDescriptions;
	private String troubleReason;
	private String remarks;
	private String repairMethod;
	private String orgname2;
	private Integer isWarning;
	private String createName;
	
	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public String getOrgname2() {
		return orgname2;
	}

	public void setOrgname2(String orgname2) {
		this.orgname2 = orgname2;
	}

	public Double getAccessoriesPrice() {
		return accessoriesPrice;
	}

	public void setAccessoriesPrice(Double accessoriesPrice) {
		this.accessoriesPrice = accessoriesPrice;
	}

	public Double getRepairPartAmount() {
		return repairPartAmount;
	}

	public void setRepairPartAmount(Double repairPartAmount) {
		this.repairPartAmount = repairPartAmount;
	}

	public Double getLabourAmount() {
		return labourAmount;
	}

	public void setLabourAmount(Double labourAmount) {
		this.labourAmount = labourAmount;
	}

	public Double getAddItemAmount() {
		return addItemAmount;
	}

	public void setAddItemAmount(Double addItemAmount) {
		this.addItemAmount = addItemAmount;
	}

	public Double getRepairAmount() {
		return repairAmount;
	}

	public void setRepairAmount(Double repairAmount) {
		this.repairAmount = repairAmount;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public Integer getMinDays() {
		return minDays;
	}

	public Integer getDifferencestime() {
		return differencestime;
	}

	public void setDifferencestime(Integer differencestime) {
		this.differencestime = differencestime;
	}

	public void setMinDays(Integer minDays) {
		this.minDays = minDays;
	}

	public Integer getStartMileage() {
		return startMileage;
	}

	public Integer getMaxDays() {
		return maxDays;
	}

	public void setMaxDays(Integer maxDays) {
		this.maxDays = maxDays;
	}

	public void setStartMileage(Integer startMileage) {
		this.startMileage = startMileage;
	}

	public Integer getEndMileage() {
		return endMileage;
	}

	public void setEndMileage(Integer endMileage) {
		this.endMileage = endMileage;
	}

	public String getAuditName() {
		return auditName;
	}

	public void setAuditName(String auditName) {
		this.auditName = auditName;
	}

	public String getDealerName() {
		return dealerName;
	}

	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}

	public String getApprovalLevelName() {
		return approvalLevelName;
	}

	public void setApprovalLevelName(String approvalLevelName) {
		this.approvalLevelName = approvalLevelName;
	}

	public String getOrgname() {
		return orgname;
	}

	public void setOrgname(String orgname) {
		this.orgname = orgname;
	}

	public String getTroubleDescriptions() {
		return troubleDescriptions;
	}

	public void setTroubleDescriptions(String troubleDescriptions) {
		this.troubleDescriptions = troubleDescriptions;
	}

	public String getTroubleReason() {
		return troubleReason;
	}

	public void setTroubleReason(String troubleReason) {
		this.troubleReason = troubleReason;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getRepairMethod() {
		return repairMethod;
	}

	public void setRepairMethod(String repairMethod) {
		this.repairMethod = repairMethod;
	}

	public String getUpdateDat() {
		return updateDat;
	}

	public void setUpdateDat(String updateDat) {
		this.updateDat = updateDat;
	}

	public Integer getIsWarning() {
		return isWarning;
	}

	public void setIsWarning(Integer isWarning) {
		this.isWarning = isWarning;
	}
}
