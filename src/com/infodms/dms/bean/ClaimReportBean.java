package com.infodms.dms.bean;

/**
 * 索赔申请单上报-查询条件
 * @author XZM
 */
public class ClaimReportBean {
	/** 索赔申请单号 */
	private String claimNo;
	/** 工单号 */
	private String roNo;
	/** 行号 */
	private String lineNo;
	/** 索赔类型 */
	private String claimType;
	/** 车辆唯一标识码 */
	private String vin;
	/** 申请日期范围（开始时间） */
	private String applyStartDate;
	/** 申请日期范围（结束时间） */
	private String applyEndDate;
	/** 状态 */
	private String claimStatus;
	/** 经销商ID */
	private String dealerCode;
	/** 所属公司ID */
	private Long companyId;

	public String getClaimNo() {
		return claimNo;
	}

	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}

	public String getRoNo() {
		return roNo;
	}

	public void setRoNo(String roNo) {
		this.roNo = roNo;
	}

	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}

	public String getClaimType() {
		return claimType;
	}

	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getApplyStartDate() {
		return applyStartDate;
	}

	public void setApplyStartDate(String applyStartDate) {
		this.applyStartDate = applyStartDate;
	}

	public String getApplyEndDate() {
		return applyEndDate;
	}

	public void setApplyEndDate(String applyEndDate) {
		this.applyEndDate = applyEndDate;
	}

	public String getClaimStatus() {
		return claimStatus;
	}

	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}

	public String getDealerCode() {
		return dealerCode;
	}

	public void setDealerCode(String dealerCode) {
		this.dealerCode = dealerCode;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		if(companyId==null)
			companyId = new Long(-1);
		this.companyId = companyId;
	}
	
}
