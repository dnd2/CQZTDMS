package com.infodms.dms.bean;


/**
 * 索赔申请单查询-条件
 * @author XZM
 */
public class ClaimOrderBean {
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
	/** 经销商代码(多个代码以","分隔) */
	private String dealerCodes;
	/** 经销商名称 */
	private String dealerName;
	/** 授权代码 */
	private String authCode;
	/** 所属公司ID */
	private Long companyId;
	/** 职位ID */
	private Long poseId;
	/** 组织ID */
	private Long orgId;
	/** 业务范围集合ID */
	private String areaIds;
	/** 用户拥有产地权限 */
	private String yieldlys;
	/** 产地查询条件 */
	private String yieldly;
	/** 索赔单状态 */
	private String status;
	/** 省份代码 */
	private String provinceCode;
	private String dealerId;
	/** 用户ID */
	
	/** 旧件条码 */
	private String barcodeNo;
	public String getBarcodeNo() {
		return barcodeNo;
	}

	public void setBarcodeNo(String barcodeNo) {
		this.barcodeNo = barcodeNo;
	}

	/**addUser xiongchuan 2011-09-22****start****/
	private  String claimZhishun;
	public String getClaimZhishun() {
		return claimZhishun;
	}

	public void setClaimZhishun(String claimZhishun) {
		this.claimZhishun = claimZhishun;
	}

	/**addUser xiongchuan 2011-09-22****end*****/
	private String userId;
	private String approveDate;//审核时间
	private String approveDate2;
	private String approveName;//授权人
	
	public String getApproveDate() {
		return approveDate;
	}

	public void setApproveDate(String approveDate) {
		this.approveDate = approveDate;
	}

	public String getApproveDate2() {
		return approveDate2;
	}

	public void setApproveDate2(String approveDate2) {
		this.approveDate2 = approveDate2;
	}

	public String getApproveName() {
		return approveName;
	}

	public void setApproveName(String approveName) {
		this.approveName = approveName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

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

	public String getDealerCodes() {
		return dealerCodes;
	}

	public void setDealerCodes(String dealerCodes) {
		this.dealerCodes = dealerCodes;
	}

	public String getDealerName() {
		return dealerName;
	}

	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		if(companyId==null)
			companyId = new Long(-1);
		this.companyId = companyId;
	}

	public Long getPoseId() {
		return poseId;
	}

	public void setPoseId(Long poseId) {
		this.poseId = poseId;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getAreaIds() {
		return areaIds;
	}

	public void setAreaIds(String areaIds) {
		this.areaIds = areaIds;
	}

	public String getYieldlys() {
		return yieldlys;
	}

	public void setYieldlys(String yieldlys) {
		this.yieldlys = yieldlys;
	}

	public String getYieldly() {
		return yieldly;
	}

	public void setYieldly(String yieldly) {
		this.yieldly = yieldly;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getDealerId() {
		return dealerId;
	}

	public void setDealerId(String dealerId) {
		this.dealerId = dealerId;
	}
	
}
